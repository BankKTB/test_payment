package th.com.bloomcode.paymentservice.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.com.bloomcode.paymentservice.context.Context;
import th.com.bloomcode.paymentservice.idem.entity.CompCode;
import th.com.bloomcode.paymentservice.model.Raw;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.common.GlAccount;
import th.com.bloomcode.paymentservice.model.idem.CompanyCode;
import th.com.bloomcode.paymentservice.model.payment.JUHead;
import th.com.bloomcode.paymentservice.model.payment.JULine;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.model.response.JuDetailDocument;
import th.com.bloomcode.paymentservice.model.response.JuHeadDocumentResponse;
import th.com.bloomcode.paymentservice.model.response.JuLineDocumentResponse;
import th.com.bloomcode.paymentservice.payment.entity.mapping.JUDocument;
import th.com.bloomcode.paymentservice.payment.entity.mapping.JUPropLog;
import th.com.bloomcode.paymentservice.service.payment.JUHeadService;
import th.com.bloomcode.paymentservice.service.payment.JULineService;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogService;
import th.com.bloomcode.paymentservice.util.Util;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.model.FIMessage;
import th.com.bloomcode.paymentservice.webservice.model.request.APCreateJULine;
import th.com.bloomcode.paymentservice.webservice.model.request.APCreateJURequest;
import th.com.bloomcode.paymentservice.service.common.ExportJasperService;

import javax.jms.Message;
import javax.persistence.EntityManager;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class GenerateDocumentJuService {
    private final JUHeadService juHeadService;
    private final JULineService juLineService;
    private final th.com.bloomcode.paymentservice.service.payment.ProposalLogService proposalLogService;
    private final JmsTemplate jmsTemplate;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ExportJasperService exportJasperService;

    @Value("${payment.dblink.schema}")
    private String schema;

    @Value("${payment.dblink.user}")
    private String user;

    public GenerateDocumentJuService(JUHeadService juHeadService, JULineService juLineService, ProposalLogService proposalLogService, JmsTemplate jmsTemplate, ExportJasperService exportJasperService) {
        this.juHeadService = juHeadService;
        this.juLineService = juLineService;
        this.proposalLogService = proposalLogService;
        this.jmsTemplate = jmsTemplate;
        this.exportJasperService = exportJasperService;
    }

//    public ResponseEntity<Result<List<JuHeadDocumentResponse>>> getJUDocumentDetail(GenerateJuRequest request) {
        public ResponseEntity<Result<JuDetailDocument>> getJUDocumentDetail(GenerateJuRequest request) {
        Result<JuDetailDocument> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<JuDetailDocument> juDetailDocumentResponses = new ArrayList<>();
            JuDetailDocument juDetailDocumentResponse = new JuDetailDocument();
            List<JuHeadDocumentResponse> juHeadDocumentResponses = new ArrayList<>();
            List<JUDocument> juDocumentList = selectJuDetail(request);
            Map<Object, List<JUDocument>> groupByJuHeadId = juDocumentList.stream()
                    .collect(Collectors.groupingBy(data -> data.getJuHeadId()));
            groupByJuHeadId.forEach((key, value) -> {
                List<JuLineDocumentResponse> juLineDocumentResponses = new ArrayList<>();
                JuHeadDocumentResponse juHeadDocumentResponse = new JuHeadDocumentResponse();
                BeanUtils.copyProperties(value.get(0), juHeadDocumentResponse);
                List<JUDocument> item = value;
                for (JUDocument juDocument : item) {
                    JuLineDocumentResponse juLineDocumentResponse = new JuLineDocumentResponse();
                    BeanUtils.copyProperties(juDocument, juLineDocumentResponse);
                    juLineDocumentResponses.add(juLineDocumentResponse);
                }
                juHeadDocumentResponse.setJuHeadId(value.get(0).getId());
                juHeadDocumentResponse.setList(juLineDocumentResponses);
                juHeadDocumentResponses.add(juHeadDocumentResponse);
            });
            int sumFile = (int) juHeadDocumentResponses.stream().mapToLong(aa -> aa.getList().size()).sum();
            BigDecimal sumAmountDr = juDocumentList.stream().map(e -> Util.getBigDecimal(e.getAmountDr())).reduce(BigDecimal.valueOf(0), (x, y) -> x.add(y));
            juDetailDocumentResponse.setListJuHead(juHeadDocumentResponses);
//            juDetailDocumentResponse.setSummaryFile(groupByJuHeadId.size());
//            juDetailDocumentResponse.setSummaryDocument(sumDocument);
            juDetailDocumentResponse.setSummaryFile(sumFile);
            juDetailDocumentResponse.setSummaryDocument(groupByJuHeadId.size());
            juDetailDocumentResponse.setSummaryAmount(sumAmountDr);
//            juDetailDocumentResponses.add(juDetailDocumentResponse);
            result.setData(juDetailDocumentResponse);
            result.setStatus(HttpStatus.OK.value());
            result.setMessage("");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setStatus(HttpStatus.OK.value());
            result.setMessage("");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    public ResponseEntity<Result<List<JuHeadDocumentResponse>>> generateFileJu(GenerateJuRequest request) {
        Result<List<JuHeadDocumentResponse>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            this.generateJuPropLog(request);

            log.info("=============== 1 ===================");
            List<JuHeadDocumentResponse> juHeadDocumentResponses = new ArrayList<>();
            List<JUDocument> juDocumentList = selectJuDetail(request);
            log.info("=============== 2 =================== {} ", juDocumentList.size());
            log.info("=============== 3 =================== {}", juDocumentList.get(0).getCompanyName());
            Map<Object, List<JUDocument>> groupByJuHeadId = juDocumentList.stream()
                    .collect(Collectors.groupingBy(data -> data.getJuHeadId()));
            groupByJuHeadId.forEach((key, value) -> {
                log.info("=============== 4 =================== {} ", value);
                List<JuLineDocumentResponse> juLineDocumentResponses = new ArrayList<>();
                JuHeadDocumentResponse juHeadDocumentResponse = new JuHeadDocumentResponse();
                log.info("=============== 5 =================== {} ", value.get(0));
                BeanUtils.copyProperties(value.get(0), juHeadDocumentResponse);


                juHeadDocumentResponse.setCompanyName(value.get(0).getCompanyName());
                List<JUDocument> item = value;
                for (JUDocument juDocument : item) {
                    JuLineDocumentResponse juLineDocumentResponse = new JuLineDocumentResponse();
                    BeanUtils.copyProperties(juDocument, juLineDocumentResponse);
                    juLineDocumentResponses.add(juLineDocumentResponse);
                }
                juHeadDocumentResponse.setList(juLineDocumentResponses);
                juHeadDocumentResponses.add(juHeadDocumentResponse);
            });
            result.setData(juHeadDocumentResponses);
            result.setStatus(HttpStatus.OK.value());
            result.setMessage("");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setStatus(HttpStatus.OK.value());
            result.setMessage("");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    public ResponseEntity<Result<Raw>> generateFileJuExportPdf(GenerateJuRequest request) {
        return juLineService.selectJuExportPdf(request);
    }

    public ResponseEntity<Result<Raw>> generateFileJuExportExcel(GenerateJuRequest request) {
        return juLineService.selectJuExportExcel(request);
    }

    public List<JUDocument> selectJuDetail(GenerateJuRequest request) {
        try {
            return juLineService.selectJuDetail(request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<Result<Raw>> getJUDocumentDetailExportPdf(GenerateJuRequest request) {
        return juLineService.selectJuDetailExportPdf(request);
    }

    public ResponseEntity<Result<Raw>> getJUDocumentDetailExportExcel(GenerateJuRequest request) {
        return juLineService.selectJuDetailExportExcel(request);
    }

    public List<ProposalLog> selectPropLogByRequest(GenerateJuRequest request) {

        try {
            return proposalLogService.selectProposalLogForGenerateJu(request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void generateJuPropLog(GenerateJuRequest request) {
        try {
            deleteJuPropLog(request);
            log.info("DONE deleteJuPropLog ");
            List<ProposalLog> proposalLogs = selectPropLogByRequest(request);
            log.info("DONE selectPropLogByRequest ");
            log.info("proposalLogs : {} ", proposalLogs.size());
            Map<Object, List<ProposalLog>> groupByPaymentDateAndPaymentName = proposalLogs.stream()
                    .collect(Collectors.groupingBy(data -> data.getPaymentDate() + "|" + data.getPaymentName()));
            log.info("groupByPaymentDateAndPaymentName : {} ", groupByPaymentDateAndPaymentName.size());
            groupByPaymentDateAndPaymentName.forEach((key, value) -> {
                JUHead juHead = new JUHead();
                List<JULine> juLineList = new ArrayList<>();
                log.info("key : {} ", key);
                log.info("value : {} ", value.size());
                juHead.setMessageQueueId(value.get(0).getPaymentName() + "-" + value.get(0).getPaymentDate().getTime());
                juHead.setCompanyCode("99999");
                juHead.setDocumentType(request.getDocType());
                juHead.setPaymentDate((Timestamp) value.get(0).getPaymentDate());
                juHead.setPaymentName(value.get(0).getPaymentName());
                juHead.setTransferDate(new Timestamp(value.get(0).getTransferDate().getTime()));
                juHead.setDateAcct(request.getTransferDate());
                juHead.setGlAccountCr(new BigDecimal(request.getGlCredit()));
                juHead.setAmountCr(new BigDecimal(value.stream().mapToDouble(item -> Double.parseDouble(item.getAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                juHead.setUsername(request.getUsername());
                juHead.setTestRun(request.isTestRun());
                String reference = Util.timestampToStringForIndependent(juHead.getPaymentDate()) + juHead.getPaymentName();
                juHead.setReference(reference);
                juHead.setPaymentCenter(request.getPaymentCenter());
                juHead.setCreated(new Timestamp(System.currentTimeMillis()));
                List<ProposalLog> juPropLogList = value;
                for (ProposalLog juPropLog : juPropLogList) {
                    JULine juLine = new JULine();
                    juLine.setPayAccount(juPropLog.getPayAccount());
                    juLine.setRefRunning(juPropLog.getRefRunning());
                    juLine.setRefLine(juPropLog.getRefLine());
                    juLine.setAccountNoFrom(juPropLog.getAccountNoFrom());
                    juLine.setAccountNoTo(juPropLog.getAccountNoTo());
                    juLine.setFileType(juPropLog.getFileType());
                    juLine.setFileName(juPropLog.getFileName());
                    juLine.setAmountDr(juPropLog.getAmount());

                    juLine.setPostingKey(request.getKeyDebit());
                    juLine.setCostCenter(request.getCostCenter());
                    juLine.setFundSource(request.getFundSource());
                    juLine.setBgCode(request.getBudgetCost());
                    juLine.setFiArea(request.getFiArea());
                    juLine.setMainActivity(request.getMainActivity());
                    List<GlAccount> glAccounts = request.getGlAccounts();
                    GlAccount glAccount = glAccounts.stream()
                            .filter(item -> juPropLog.getPayAccount().equalsIgnoreCase(item.getPayType()))
                            .findAny()
                            .orElse(null);
                    juLine.setGlAccountDr(new BigDecimal(glAccount.getAccountNo()));
                    juLine.setPayAccount(glAccount.getPayType());
                    juLineList.add(juLine);
                }
                JUHead newJuHead = juHeadService.save(juHead);
                log.info("juHead : {} ", juHead);
                log.info("juLineList : {} ", juLineList);
                for (JULine juLine : juLineList) {
                    log.info("juLine : {} ", juLine);
                    log.info("newJuHead : {} ", newJuHead.getId());
//                    juLine.setJuHead(newJuHead);
                    juLine.setJuHeadId(newJuHead.getId());
                    juLineService.save(juLine);
                }

                List<APCreateJULine> apCreateJULineList = new ArrayList<>();
                APCreateJURequest apCreateJURequest = new APCreateJURequest();
                apCreateJURequest.setFlag(1);
                apCreateJURequest.setWebInfo(request.getWebInfo());
                apCreateJURequest.setTransferDate(request.getTransferDate());
                apCreateJURequest.setPmDate(new Timestamp(newJuHead.getPaymentDate().getTime()));
                apCreateJURequest.setPmIden(newJuHead.getPaymentName());
                apCreateJURequest.setCompCode(request.getCompanyCode());
                apCreateJURequest.setDocType(request.getDocType());
                apCreateJURequest.setGlAccountCR(request.getGlCredit());
                apCreateJURequest.setFundSource(request.getFundSource());
                apCreateJURequest.setCostCenter(request.getCostCenter());
                apCreateJURequest.setBgCode(request.getBudgetCost());
                apCreateJURequest.setFiArea(request.getFiArea());
                apCreateJURequest.setBgActivity(request.getMainActivity());
                apCreateJURequest.setPaymentCenter(request.getPaymentCenter());
                apCreateJURequest.setPostingKeyCR(request.getKeyCredit());
                apCreateJURequest.setPostingKeyDR(request.getKeyDebit());
                for (JULine juLine : juLineList) {
                    APCreateJULine apCreateJULine = new APCreateJULine();
                    apCreateJULine.setPayAccount(juLine.getPayAccount());
                    apCreateJULine.setGlAccount(juLine.getGlAccountDr().toString());
                    apCreateJULine.setAmount(juLine.getAmountDr());
                    apCreateJULineList.add(apCreateJULine);
                }
                apCreateJURequest.setLines(apCreateJULineList);
                if (!request.isTestRun()) {
                    log.info(" REAL RUN ");
                    updateProposalLog(request);
                    try {
                        FIMessage fiMessage = new FIMessage();
                        fiMessage.setId(apCreateJURequest.getPmIden() + "-" + apCreateJURequest.getPmDate().getTime());
                        fiMessage.setType(FIMessage.Type.REQUEST.getCode());
                        fiMessage.setDataType(FIMessage.DataType.CREATE_JU.getCode());
                        fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
                        fiMessage.setTo("99999");
                        logger.info("apCreateJURequest {}", apCreateJURequest);
                        fiMessage.setData(XMLUtil.xmlMarshall(APCreateJURequest.class, apCreateJURequest));
                        logger.info("fiMessage {}", fiMessage);
                        sendCreateJu(fiMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getGroupingByKey(JUPropLog juPropLog) {
        return juPropLog.getPaymentName() + "-" + juPropLog.getPaymentDate();
    }

    public void deleteJuPropLog(GenerateJuRequest request) {
        try {
            List<JUHead> juHeads = selectJuHead(request);
            for (JUHead juHead : juHeads) {
                juLineService.deleteAllByJuHeadId(juHead.getId());
                juHeadService.deleteAllById(juHead.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public List<JUHead> selectJuHead(GenerateJuRequest request) {
        try {
            return juHeadService.findJuHeadByListPaymentDateAndListPaymentName(request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateProposalLog(GenerateJuRequest request) {
        try {
            proposalLogService.updateJuCreate(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCreateJu(FIMessage message) throws Exception {
        logger.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
        final AtomicReference<Message> msg = new AtomicReference<>();
        jmsTemplate.convertAndSend("99999.AP.Payment", XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
            msg.set(message1);
            return message1;
        });
        log.info("msg id : {}", msg.get().getJMSMessageID());
    }

    public ResponseEntity<Result<Raw>> selectJuExportPdf(GenerateJuRequest request) {
        return juLineService.selectJuExportPdf(request);
    }

}
