package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.SearchPaymentBlockRequest;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentMQ;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.model.request.PaymentBlockListDocumentRequest;
import th.com.bloomcode.paymentservice.model.request.UnBlockChangeDocumentRequest;
import th.com.bloomcode.paymentservice.model.response.UnBlockDocumentResponse;
import th.com.bloomcode.paymentservice.repository.payment.UnBlockDocumentRepository;
import th.com.bloomcode.paymentservice.service.payment.GLLineService;
import th.com.bloomcode.paymentservice.service.payment.UnBlockDocumentLogService;
import th.com.bloomcode.paymentservice.service.payment.UnBlockDocumentMQService;
import th.com.bloomcode.paymentservice.service.payment.UnBlockDocumentService;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.model.FIMessage;
import th.com.bloomcode.paymentservice.webservice.model.request.APUpPbkRequest;

import javax.jms.Message;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UnBlockDocumentServiceImpl implements UnBlockDocumentService {
    private final JmsTemplate jmsTemplate;
    private final GLLineService glLineService;
    private final UnBlockDocumentRepository unBlockDocumentRepository;
    private final UnBlockDocumentLogService unBlockDocumentLogService;
    private final UnBlockDocumentMQService unBlockDocumentMQService;

    public UnBlockDocumentServiceImpl(JmsTemplate jmsTemplate, GLLineService glLineService, UnBlockDocumentRepository unBlockDocumentRepository, UnBlockDocumentLogService unBlockDocumentLogService, UnBlockDocumentMQService unBlockDocumentMQService) {
        this.jmsTemplate = jmsTemplate;
        this.glLineService = glLineService;
        this.unBlockDocumentRepository = unBlockDocumentRepository;
        this.unBlockDocumentLogService = unBlockDocumentLogService;
        this.unBlockDocumentMQService = unBlockDocumentMQService;
    }

    @Override
    public ResponseEntity<Result<List<UnBlockChangeDocumentRequest>>> changePaymentBlock(List<UnBlockChangeDocumentRequest> request) {
        log.info("request {}", request);
        Result<List<UnBlockChangeDocumentRequest>> result = new Result<>();
        result.setTimestamp(new Date());
        UUID uuid = UUID.randomUUID();
        try {
            List<UnBlockDocument> unBlockDocuments = new ArrayList<>();
            List<UnBlockDocumentLog> unBlockDocumentLogs = new ArrayList<>();
            List<UnBlockDocumentMQ> unBlockDocumentMQs = new ArrayList<>();
            List<UnBlockDocumentLog> unBlockDocumentLogsUpdate = new ArrayList<>();
            List<UnBlockDocumentMQ> unBlockDocumentMQsUpdate = new ArrayList<>();
//            for (UnBlockChangeDocumentRequest unBlockChangeDocumentRequest : request) {
//                UnBlockDocumentLog checkDuplicate = unBlockDocumentLogService.findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(unBlockChangeDocumentRequest.getCompanyCode(), unBlockChangeDocumentRequest.getOriginalDocumentNo(), unBlockChangeDocumentRequest.getOriginalFiscalYear());
//
//                if (unBlockChangeDocumentRequest.isCanUnblock()) {
//                    UnBlockDocument unBlockDocument = new UnBlockDocument();
//                    unBlockDocument.setCompanyCode(unBlockChangeDocumentRequest.getCompanyCode());
//                    unBlockDocument.setOriginalDocumentNo(unBlockChangeDocumentRequest.getOriginalDocumentNo());
//                    unBlockDocument.setOriginalFiscalYear(unBlockChangeDocumentRequest.getOriginalFiscalYear());
//                    unBlockDocument.setAccountType("K");
////                GLLine glLine = glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(
////                        unBlockChangeDocumentRequest.getCompanyCode(), unBlockChangeDocumentRequest.getOriginalDocumentNo(),
////                        unBlockChangeDocumentRequest.getOriginalFiscalYear(), "K");
//                    if (unBlockChangeDocumentRequest.isApprove()) {
//                        unBlockDocument.setPaymentBlock("BP");
//                    } else {
//                        unBlockDocument.setPaymentBlock("EP");
//                    }
//                    unBlockDocuments.add(unBlockDocument);
////                glLineService.save(glLine);
//                }
//                if (null == checkDuplicate) {
//                    UnBlockDocumentLog unBlockDocumentLog = new UnBlockDocumentLog();
//                    BeanUtils.copyProperties(unBlockChangeDocumentRequest, unBlockDocumentLog);
//                    log.info("unBlockDocumentLogService : {}", unBlockDocumentLog);
//                    unBlockDocumentLog.setIdemStatus("P");
//                    unBlockDocumentLogs.add(unBlockDocumentLog);
//
//                    UnBlockDocumentMQ unBlockDocumentMQ = new UnBlockDocumentMQ();
//                    BeanUtils.copyProperties(unBlockChangeDocumentRequest, unBlockDocumentMQ);
//                    unBlockDocumentMQ.setGroupDoc(uuid.toString());
//                    if (unBlockChangeDocumentRequest.isCanUnblock()) {
//                        unBlockDocumentMQ.setIdemStatus("P");
//                    } else {
//                        unBlockDocumentMQ.setIdemStatus("NP");
//                    }
//                    unBlockDocumentMQs.add(unBlockDocumentMQ);
////                    unBlockDocumentMQService.save(unBlockDocumentMQ);
//                }
//            }
            for (UnBlockChangeDocumentRequest u : request) {
                UnBlockDocumentLog unBlockDocumentLog = unBlockDocumentLogService.findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(u.getCompanyCode(), u.getOriginalDocumentNo(), u.getOriginalFiscalYear());
                UnBlockDocumentMQ unBlockDocumentMQ = unBlockDocumentMQService.findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(u.getCompanyCode(), u.getOriginalDocumentNo(), u.getOriginalFiscalYear());
                if (u.isCanUnblock()) {
                    UnBlockDocument unBlockDocument = new UnBlockDocument();
                    unBlockDocument.setCompanyCode(u.getCompanyCode());
                    unBlockDocument.setOriginalDocumentNo(u.getOriginalDocumentNo());
                    unBlockDocument.setOriginalFiscalYear(u.getOriginalFiscalYear());
                    unBlockDocument.setAccountType("K");
//                GLLine glLine = glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(
//                        unBlockChangeDocumentRequest.getCompanyCode(), unBlockChangeDocumentRequest.getOriginalDocumentNo(),
//                        unBlockChangeDocumentRequest.getOriginalFiscalYear(), "K");
                    if (u.isApprove()) {
                        unBlockDocument.setPaymentBlock("BP");
                    } else {
                        unBlockDocument.setPaymentBlock("EP");
                    }
                    unBlockDocuments.add(unBlockDocument);
//                glLineService.save(glLine);
                    if (null == unBlockDocumentLog) {
                        unBlockDocumentLog = new UnBlockDocumentLog();
                        BeanUtils.copyProperties(u, unBlockDocumentLog);
                        unBlockDocumentLog.setGroupDoc(uuid.toString());
                        log.info("unBlockDocumentLogService : {}", unBlockDocumentLog);
                        unBlockDocumentLog.setIdemStatus("P");
                        unBlockDocumentLogs.add(unBlockDocumentLog);
//                        unBlockDocumentLogService.save(unBlockDocumentLog);
                    } else {
                        unBlockDocumentLog.setGroupDoc(uuid.toString());
                        unBlockDocumentLog.setIdemStatus("P");
                        unBlockDocumentLog.setValueNew(u.getValueNew());

                        unBlockDocumentLogsUpdate.add(unBlockDocumentLog);
                    }

                    if (null == unBlockDocumentMQ) {
                        unBlockDocumentMQ = new UnBlockDocumentMQ();
                        BeanUtils.copyProperties(u, unBlockDocumentMQ);
                        unBlockDocumentMQ.setGroupDoc(uuid.toString());
                        if (u.isCanUnblock()) {
                            unBlockDocumentMQ.setIdemStatus("P");
                        } else {
                            unBlockDocumentMQ.setIdemStatus("NP");
                        }
                        unBlockDocumentMQs.add(unBlockDocumentMQ);
                    } else {
                        unBlockDocumentMQ.setGroupDoc(uuid.toString());
                        unBlockDocumentMQ.setValueNew(u.getValueNew());
                        if (u.isCanUnblock()) {
                            unBlockDocumentMQ.setIdemStatus("P");
                        } else {
                            unBlockDocumentMQ.setIdemStatus("NP");
                        }
                        unBlockDocumentMQsUpdate.add(unBlockDocumentMQ);
                    }
                }
            }

            log.info("unBlockDocumentLogs : {}", unBlockDocumentLogs.size());
            log.info("unBlockDocumentMQs : {}", unBlockDocumentMQs.size());

            glLineService.updateBlockStatusBatch(unBlockDocuments);
            unBlockDocumentLogService.saveBatch(unBlockDocumentLogs);
            unBlockDocumentMQService.saveBatch(unBlockDocumentMQs);
            log.info("unBlockDocumentLogsUpdate : {}", unBlockDocumentLogsUpdate.size());
            log.info("unBlockDocumentMQsUpdate : {}", unBlockDocumentMQsUpdate.size());
            if (!unBlockDocumentLogsUpdate.isEmpty() && !unBlockDocumentMQsUpdate.isEmpty()) {
//                unBlockDocumentLogService.updateBatch(unBlockDocumentLogs);
//                unBlockDocumentMQService.updateBatch(unBlockDocumentMQs);
                unBlockDocumentLogService.updateBatch(unBlockDocumentLogsUpdate);
                unBlockDocumentMQService.updateBatch(unBlockDocumentMQsUpdate);
            }

            for (UnBlockChangeDocumentRequest unBlockChangeDocumentRequest : request) {
                if (unBlockChangeDocumentRequest.isCanUnblock()) {

//                    if(unBlockChangeDocumentRequest.getDocumentType().equalsIgnoreCase("KC")
//                    &&unBlockChangeDocumentRequest.getPaymentMethod().equalsIgnoreCase("F")
//                    &&unBlockChangeDocumentRequest.getDocumentHeaderText().startsWith("IFI")){
//                        APUpPbkRequest apUpPbkRequest = new APUpPbkRequest(unBlockChangeDocumentRequest);
//                        FIMessage fiMessage = new FIMessage();
//                        fiMessage.setId(apUpPbkRequest.getCompCode() + "-" + apUpPbkRequest.getAccountDocNo() + "-" + apUpPbkRequest.getFiscalYear() + "-" + apUpPbkRequest.getCreated());
//                        fiMessage.setType(FIMessage.Type.REQUEST.getCode());
//                        fiMessage.setDataType(FIMessage.DataType.UPDATE_PBK.getCode());
//                        fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
//                        fiMessage.setTo(apUpPbkRequest.getCompCode().substring(0, 2));
//                        fiMessage.setData(XMLUtil.xmlMarshall(APUpPbkRequest.class, apUpPbkRequest));
//                        log.info("apUpPbkRequest sendCure : {}", XMLUtil.xmlMarshall(APUpPbkRequest.class, apUpPbkRequest));
//                        log.info("sending message sendCure : {}", XMLUtil.xmlMarshall(FIMessage.class, fiMessage));
//                        this.sendCure(fiMessage, apUpPbkRequest.getCompCode());
//
//                    }else{
                    APUpPbkRequest apUpPbkRequest = new APUpPbkRequest(unBlockChangeDocumentRequest);
                    FIMessage fiMessage = new FIMessage();
                    fiMessage.setId(apUpPbkRequest.getCompCode() + "-" + apUpPbkRequest.getAccountDocNo() + "-" + apUpPbkRequest.getFiscalYear() + "-" + apUpPbkRequest.getCreated());
                    fiMessage.setType(FIMessage.Type.REQUEST.getCode());
                    fiMessage.setDataType(FIMessage.DataType.UPDATE_PBK.getCode());
                    fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
                    fiMessage.setTo(apUpPbkRequest.getCompCode().substring(0, 2));
                    fiMessage.setData(XMLUtil.xmlMarshall(APUpPbkRequest.class, apUpPbkRequest));
                    log.info("apUpPbkRequest : {}", XMLUtil.xmlMarshall(APUpPbkRequest.class, apUpPbkRequest));
                    log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, fiMessage));
                    this.send(fiMessage, apUpPbkRequest.getCompCode());
//                    }
                }
            }

            result.setData(request);
            result.setMessage(uuid.toString());
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<UnBlockDocumentResponse>> findUnBlockByCondition(HttpServletRequest httpServletRequest, SearchPaymentBlockRequest request) {
        log.info("findUnBlockByCondition : {} ", request);
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Result<UnBlockDocumentResponse> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            UnBlockDocumentResponse unBlockDocumentResponse = new UnBlockDocumentResponse();
            List<UnBlockDocument> unBlock = unBlockDocumentRepository.findUnBlockByCondition(jwt, request);
            List<UnBlockDocument> unBlockFilter = unBlock.stream().filter(item -> (item.getDocumentType().equalsIgnoreCase("KC") && !item.getPaymentMethod().equalsIgnoreCase("F"))
                    || item.getDocumentType().equalsIgnoreCase("KA") || item.getDocumentType().equalsIgnoreCase("KB")
                    || item.getDocumentType().equalsIgnoreCase("KG") || item.getDocumentType().equalsIgnoreCase("K3")
                    || item.getDocumentType().equalsIgnoreCase("KX")).collect(Collectors.toList());

            List<UnBlockDocument> documentRelation = new ArrayList<>();
            if (unBlock.size() > 0 && !unBlockFilter.isEmpty()) {
                documentRelation = unBlockDocumentRepository.findUnBlockForCheckRelation(jwt, unBlockFilter, request);
            }
            unBlockDocumentResponse.setDocumentForDisplay(unBlock);
            unBlockDocumentResponse.setDocumentForCheckRelation(documentRelation);
            result.setStatus(HttpStatus.OK.value());
            result.setData(unBlockDocumentResponse);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<Result<List<UnBlockDocument>>> findUnBlockParentByCondition(List<PaymentBlockListDocumentRequest> request) {
        Result<List<UnBlockDocument>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<UnBlockDocument> unBlock = unBlockDocumentRepository.findUnBlockParentByCondition(request);
            result.setStatus(HttpStatus.OK.value());
            result.setData(unBlock);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    public void send(FIMessage message, String compCode) throws Exception {
        log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));

        final AtomicReference<Message> msg = new AtomicReference<>();
        jmsTemplate.convertAndSend(compCode.substring(0, 2) + ".AP.UpPbk",
                XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
                    msg.set(message1);
                    return message1;
                });
        log.info("msg id : {}", msg.get().getJMSMessageID());
    }

    public void sendCure(FIMessage message, String compCode) throws Exception {
        log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));

        final AtomicReference<Message> msg = new AtomicReference<>();
        jmsTemplate.convertAndSend(compCode.substring(0, 2) + ".AP.UpPbk1",
                XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
                    msg.set(message1);
                    return message1;
                });
        log.info("msg id : {}", msg.get().getJMSMessageID());
    }

}
