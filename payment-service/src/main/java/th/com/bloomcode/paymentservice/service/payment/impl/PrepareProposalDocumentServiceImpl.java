package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.context.Context;
import th.com.bloomcode.paymentservice.model.ProposalDocument;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.config.ParameterConfig;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.PayMethodConfig;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReport;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareProposalDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.model.request.ProposalDocumentRequest;
import th.com.bloomcode.paymentservice.repository.payment.PrepareProposalDocumentRepository;
import th.com.bloomcode.paymentservice.service.idem.CompanyPayingMinimalAmountService;
import th.com.bloomcode.paymentservice.service.idem.PaymentMethodService;
import th.com.bloomcode.paymentservice.service.idem.VendorBankAccountService;
import th.com.bloomcode.paymentservice.service.idem.VendorService;
import th.com.bloomcode.paymentservice.service.payment.*;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentHeader;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentLine;
import th.com.bloomcode.paymentservice.webservice.model.FIMessage;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;
import th.com.bloomcode.paymentservice.webservice.model.request.APPaymentRequest;

import javax.jms.Message;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PrepareProposalDocumentServiceImpl implements PrepareProposalDocumentService {

    private final PrepareProposalDocumentRepository prepareProposalDocumentRepository;
    private final PaymentProcessService paymentProcessService;
    private final GLHeadService glHeadService;
    private final GLLineService glLineService;
    private final PaymentInformationService paymentInformationService;
    private final PaymentAliasService paymentAliasService;
    private final VendorService vendorService;
    private final VendorBankAccountService vendorBankAccountService;
    private final PaymentMethodService paymentMethodService;
    private final PrepareRunDocumentService prepareRunDocumentService;
    private final CompanyPayingMinimalAmountService companyPayingMinimalAmountService;
    private final PaymentProposalLogService paymentProposalLogService;
    private final PaymentRealRunLogService paymentRealRunLogService;
    private final PaymentReportService paymentReportService;
    private final PaymentAsyncService paymentAsyncService;

    private final JmsTemplate jmsTemplate;

    public PrepareProposalDocumentServiceImpl(PrepareProposalDocumentRepository prepareProposalDocumentRepository, PaymentProcessService paymentProcessService, GLHeadService glHeadService, GLLineService glLineService, PaymentInformationService paymentInformationService, PaymentAliasService paymentAliasService, VendorService vendorService, VendorBankAccountService vendorBankAccountService, PaymentMethodService paymentMethodService, PrepareRunDocumentService prepareRunDocumentService, CompanyPayingMinimalAmountService companyPayingMinimalAmountService, PaymentProposalLogService paymentProposalLogService, PaymentRealRunLogService paymentRealRunLogService, PaymentReportService paymentReportService, PaymentAsyncService paymentAsyncService, JmsTemplate jmsTemplate) {
        this.prepareProposalDocumentRepository = prepareProposalDocumentRepository;
        this.paymentProcessService = paymentProcessService;
        this.glHeadService = glHeadService;
        this.glLineService = glLineService;
        this.paymentInformationService = paymentInformationService;
        this.paymentAliasService = paymentAliasService;
        this.vendorService = vendorService;
        this.vendorBankAccountService = vendorBankAccountService;
        this.paymentMethodService = paymentMethodService;
        this.prepareRunDocumentService = prepareRunDocumentService;
        this.companyPayingMinimalAmountService = companyPayingMinimalAmountService;
        this.paymentProposalLogService = paymentProposalLogService;
        this.paymentRealRunLogService = paymentRealRunLogService;
        this.paymentReportService = paymentReportService;
        this.paymentAsyncService = paymentAsyncService;
        this.jmsTemplate = jmsTemplate;
    }


    @Override
    public List<PrepareProposalDocument> findUnBlockDocumentCanPayByParameter(ParameterConfig parameterConfig, String username) {
        return prepareProposalDocumentRepository.findUnBlockDocumentCanPayByParameter(parameterConfig, username);
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public ResponseEntity<Result<PaymentAlias>> proposal(Long id, WSWebInfo webInfo) {
//        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
//        Result<PaymentAlias> result = new Result<>();
//        result.setTimestamp(new Date());
//
//        try {
//            PaymentAlias paymentAlias = paymentAliasService.findOneById(id);
//            paymentAlias.setProposalStart(new Timestamp(System.currentTimeMillis()));
//            paymentAlias.setProposalEnd(null);
////            CompanyPayingMinimalAmount companyPayingMinimalAmount = companyPayingMinimalAmountService.findOneByValueCode("99999");
//
//
////            Long seqNo = prepareProposalDocumentRepository.getNextSeries(true);
////            Long processSeqNo = paymentProcessService.getNextSeries();
////            Long informationSeqNo = paymentInformationService.getNextSeries();
////            List<GLHead> glHeads = new ArrayList<>();
////            List<PaymentProcess> paymentProcesses = new ArrayList<>();
////            List<PaymentInformation> paymentInformations = new ArrayList<>();
////            for (PrepareProposalDocument prepareProposalDocument : prepareProposalDocumentList) {
////
////                String paymentDocNo = SqlUtil.generateProposalDocument(seqNo++);
////                prepareProposalDocument.setPaymentClearingDocNo(paymentDocNo);
////                prepareProposalDocument.setPaymentClearingDate(Util.stringToTimestamp(parameter.getPostDate()));
////                prepareProposalDocument.setPaymentClearingEntryDate(new Timestamp(new Date().getTime()));
////                prepareProposalDocument.setPaymentClearingYear(prepareProposalDocument.getOriginalFiscalYear());
////                prepareProposalDocument.setPaymentDate(paymentAlias.getPaymentDate());
////                prepareProposalDocument.setPaymentDateAcct(paymentAlias.getPaymentDate());
////                prepareProposalDocument.setPaymentName(paymentAlias.getPaymentName());
////                prepareProposalDocument.setPaymentId(paymentAlias.getId());
////
////                HouseBankPaymentMethod houseBankPaymentMethod = Context.sessionHouseBankPaymentMethod.get(prepareProposalDocument.getPaymentMethod());
////                prepareProposalDocument.setPayingBankCode(houseBankPaymentMethod.getAccountCode());
////                prepareProposalDocument.setPayingHouseBank(houseBankPaymentMethod.getHouseBank());
////                prepareProposalDocument.setPayingBankAccountNo(houseBankPaymentMethod.getBankAccountCode());
////                prepareProposalDocument.setPayingBankCountry(houseBankPaymentMethod.getCountryCode());
////                prepareProposalDocument.setPayingBankNo(houseBankPaymentMethod.getBankAccountCode());
////                prepareProposalDocument.setPayingGLAccount(houseBankPaymentMethod.getGlAccount());
////                prepareProposalDocument.setPayingBankKey(houseBankPaymentMethod.getBankBranch());
////                prepareProposalDocument.setPayingBankName(houseBankPaymentMethod.getHouseBank());
////                prepareProposalDocument.setPayingCompCode("99999");
////                prepareProposalDocument.setAmountPaid(prepareProposalDocument.getAmount().subtract(prepareProposalDocument.getWtxAmount()));
////
////                this.checkErrorCodePrepareProposalDocument(prepareProposalDocument, parameter, companyPayingMinimalAmount);
////
////                if (prepareProposalDocument.getDocumentType().equalsIgnoreCase("KA") || prepareProposalDocument.getDocumentType().equalsIgnoreCase("KB") ||
////                        prepareProposalDocument.getDocumentType().equalsIgnoreCase("KG") || prepareProposalDocument.getDocumentType().equalsIgnoreCase("KC")) {
////
////                    this.createDocumentSpecial(jwt, paymentAlias, parameter, prepareProposalDocument, seqNo++, processSeqNo++, informationSeqNo++);
////
////
////                }
////
////                //add list update gl head
////                glHeads.add(new GLHead(prepareProposalDocument.getCompanyCode(), prepareProposalDocument.getOriginalDocumentNo(), prepareProposalDocument.getOriginalFiscalYear(), prepareProposalDocument.getPaymentClearingDocNo(), prepareProposalDocument.getPaymentId()));
//////        glHeadService.updateGLHead(prepareProposalDocument.getCompanyCode(), prepareProposalDocument.getOriginalDocumentNo(), prepareProposalDocument.getOriginalFiscalYear(), prepareProposalDocument.getPaymentClearingDocNo(), prepareProposalDocument.getPaymentId());
////
////                PaymentProcess paymentProcess = new PaymentProcess(prepareProposalDocument, true, false);
////                paymentProcess.setId(processSeqNo++);
////                paymentProcess.setPmGroupNo(prepareProposalDocument.getPaymentName() + "-" + prepareProposalDocument.getPaymentDate().getTime());
////                paymentProcess.setPmGroupDoc(prepareProposalDocument.getCompanyCode() + "-" + prepareProposalDocument.getOriginalDocumentNo() + "-" + prepareProposalDocument.getOriginalFiscalYear() + "-" + prepareProposalDocument.getPaymentName() + "-" + prepareProposalDocument.getPaymentDate().getTime());
////                paymentProcess.setProposalBlock(false);
////
////                if (null != prepareProposalDocument.getOriginalDocument()) {
////
////                    String originalDocumentNo = prepareProposalDocument.getOriginalDocument().substring(0, 10);
////                    String originalCompanyCode = prepareProposalDocument.getOriginalDocument().substring(10, 15);
////                    String originalFiscalYear = prepareProposalDocument.getOriginalDocument().substring(15, 19);
////
////                    GLHead glHeadOriginal = glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(originalCompanyCode, originalDocumentNo, originalFiscalYear);
////
////                    paymentProcess.setOriginalDocumentNo(glHeadOriginal.getOriginalDocumentNo());
////                    paymentProcess.setOriginalCompanyCode(glHeadOriginal.getCompanyCode());
////                    paymentProcess.setOriginalFiscalYear(glHeadOriginal.getOriginalFiscalYear());
////                    paymentProcess.setOriginalDocumentType(glHeadOriginal.getDocumentType());
////                    paymentProcess.setOriginalPaymentCenter(glHeadOriginal.getPaymentCenter());
////                    paymentProcess.setOriginalAmount(prepareProposalDocument.getAmount());
////                    paymentProcess.setOriginalAmountPaid(prepareProposalDocument.getAmountPaid());
////
////                    GLLine glLineOriginalTypeK = glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(originalCompanyCode, originalDocumentNo, originalFiscalYear, "K");
////
////                    paymentProcess.setOriginalPaymentCenter(glLineOriginalTypeK.getPaymentCenter());
////                    paymentProcess.setOriginalWtxAmount(glLineOriginalTypeK.getWtxAmount());
////                    paymentProcess.setOriginalWtxBase(glLineOriginalTypeK.getWtxBase());
////                    paymentProcess.setOriginalWtxAmountP(glLineOriginalTypeK.getWtxAmountP());
////                    paymentProcess.setOriginalWtxBaseP(glLineOriginalTypeK.getWtxBaseP());
////                }
////
////                // add list payment process
////                paymentProcesses.add(paymentProcess);
//////        paymentProcess = this.paymentProcessService.save(paymentProcess);
////
////                PaymentInformation paymentInformation = new PaymentInformation(prepareProposalDocument, true, false);
////                paymentInformation.setId(informationSeqNo++);
////                paymentInformation.setPaymentProcessId(paymentProcess.getId());
////
////                // add list payment information
////                paymentInformations.add(paymentInformation);
//////        this.paymentInformationService.save(paymentInformation);
////
////                log.info(" end prepare save {}", "");
////            }
////
////            // end loop
////            // save batch
////            this.glHeadService.updateGLHeadBatch(glHeads);
////            this.paymentProcessService.saveBatch(paymentProcesses);
////            this.paymentInformationService.saveBatch(paymentInformations);
////
////            prepareProposalDocumentRepository.updateNextSeries(seqNo, true);
////            paymentProcessService.updateNextSeries(++processSeqNo);
////            paymentInformationService.updateNextSeries(++informationSeqNo);
//////            this.writeLogPrepareProposalDocument(paymentAlias, parameterConfig, prepareProposalDocumentList, webInfo);
////
//            if (null == paymentAlias.getProposalStatus()) {
//
//                this.paymentAsyncService.createProposal(jwt, paymentAlias, webInfo);
//                paymentAlias.setProposalStatus("P");
//                paymentAlias.setProposalSuccessDocument(0);
//                paymentAlias.setProposalTotalDocument(0);
//
//            } else if (paymentAlias.getProposalStatus().equalsIgnoreCase("P")) {
//                result.setData(null);
//                result.setMessage("การดำเนินการชำระเงิน " + paymentAlias.getPaymentName() + " ได้รับการประมวลผลโดยผู้ใช้อื่น");
//                result.setStatus(HttpStatus.LOCKED.value());
//                return new ResponseEntity<>(result, HttpStatus.LOCKED);
//            }
//
//            result.setData(paymentAliasService.save(paymentAlias));
//            result.setStatus(HttpStatus.OK.value());
//
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public ResponseEntity<Result<PaymentAlias>> realRun(Long id, WSWebInfo webInfo) {
//        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
//        Result<PaymentAlias> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            PaymentAlias paymentAlias = paymentAliasService.findOneById(id);
//            List<PrepareRunDocument> prepareRunDocumentList = prepareRunDocumentService.findProposalDoc(id, true);
//            log.info("prepareRunDocumentList : {}", prepareRunDocumentList);
//
//
//            if (null == paymentAlias.getRunStatus()) {
//                paymentAlias.setRunStart(new Timestamp(System.currentTimeMillis()));
//                this.paymentAsyncService.createRealRun(prepareRunDocumentList, paymentAlias, webInfo);
//
//                paymentAlias.setRunStatus("P");
////                paymentAlias.setRunTotalDocument(0);
////                paymentAlias.setRunSuccessDocument(0);
//
//            } else {
//                result.setData(null);
//                result.setMessage("การดำเนินการชำระเงิน " + paymentAlias.getPaymentName() + " ได้รับการประมวลผลโดยผู้ใช้อื่น");
//                result.setStatus(HttpStatus.LOCKED.value());
//                return new ResponseEntity<>(result, HttpStatus.LOCKED);
//            }
//
//
//            result.setData(paymentAliasService.save(paymentAlias));
//            result.setStatus(HttpStatus.OK.value());
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @Override
    public void repairDocument(HttpServletRequest httpServletRequest, Long paymentProcessId, WSWebInfo webInfo) {

        try {

            PrepareRunDocument prepareRunDocument = prepareRunDocumentService.repairDocument(paymentProcessId, false);

            APPaymentRequest apPaymentRequest = new APPaymentRequest();

            APPaymentHeader aPPaymentHeader = new APPaymentHeader();
            aPPaymentHeader.setPmCompCode("99999");
            aPPaymentHeader.setPmDate(prepareRunDocument.getPaymentDate());
            aPPaymentHeader.setPmIden(prepareRunDocument.getPaymentName());
            aPPaymentHeader.setVendor(prepareRunDocument.getVendorCode());
            aPPaymentHeader.setPayee(prepareRunDocument.getPayeeCode());

            aPPaymentHeader.setPmGroupNo(prepareRunDocument.getPmGroupNo());
            aPPaymentHeader.setPmGroupDoc(prepareRunDocument.getPmGroupDoc());

            aPPaymentHeader.setVendorTaxID(prepareRunDocument.getVendorTaxId());
            aPPaymentHeader.setBankAccNo(prepareRunDocument.getPayeeBankAccountNo());
            aPPaymentHeader.setBranchNo(prepareRunDocument.getPayeeBankKey());
            aPPaymentHeader.setGlAccount(prepareRunDocument.getPayingGlAccount());
            aPPaymentHeader.setReceiptTaxID(prepareRunDocument.getVendorTaxId());

            PayMethodConfig payMethodConfig = Context.sessionPayMethodConfig.get("TH-" + prepareRunDocument.getPaymentMethod());
            aPPaymentHeader.setDocType(payMethodConfig.getDocumentTypeForPayment());

            aPPaymentHeader.setDateDoc(prepareRunDocument.getPaymentDate());
            aPPaymentHeader.setDateAcct(prepareRunDocument.getPaymentDateAcct());

            List<APPaymentLine> listAPPaymentLine = new ArrayList<>();

            APPaymentLine apPaymentLine = new APPaymentLine();
            apPaymentLine.setInvCompCode(prepareRunDocument.getInvoiceCompanyCode());
            apPaymentLine.setInvDocNo(prepareRunDocument.getInvoiceDocumentNo());
            apPaymentLine.setInvFiscalYear(prepareRunDocument.getInvoiceFiscalYear());
            apPaymentLine.setInvLine(prepareRunDocument.getLine());
            apPaymentLine.setDocType(prepareRunDocument.getInvoiceDocumentType());
            apPaymentLine.setDateAcct(prepareRunDocument.getDateAcct());
            apPaymentLine.setDateDoc(prepareRunDocument.getDateDue());
            apPaymentLine.setVendor(prepareRunDocument.getVendorCode());
            apPaymentLine.setPayee(prepareRunDocument.getPayeeCode());

            apPaymentLine.setDrCr(prepareRunDocument.getDrCr());
            apPaymentLine.setAmount(prepareRunDocument.getInvoiceAmount());
            apPaymentLine.setWtxAmount(prepareRunDocument.getInvoiceWtxAmount());

            listAPPaymentLine.add(apPaymentLine);

            BigDecimal totalAmount = BigDecimal.ZERO;
            if (prepareRunDocument.getInvoiceDocumentType().equalsIgnoreCase("KA") || prepareRunDocument.getInvoiceDocumentType().equalsIgnoreCase("KB") ||
                    prepareRunDocument.getInvoiceDocumentType().equalsIgnoreCase("KG") || prepareRunDocument.getInvoiceDocumentType().equalsIgnoreCase("KC")) {


                List<PrepareRunDocument> specialDoc = prepareRunDocumentService.findChildProposalDoc(apPaymentLine, true,prepareRunDocument.getPaymentAliasId());


                for (PrepareRunDocument specialLine : specialDoc) {

//                PaymentProcess paymentProcess = new PaymentProcess(specialLine);
//                paymentProcess.setId(processSeqNo++);
//                paymentProcess.setPaymentDocumentNo("XXXXXXXXXX");
//
//                paymentProcess.setParentCompanyCode(prepareRunDocument.getInvoiceCompanyCode());
//                paymentProcess.setParentDocumentNo(prepareRunDocument.getInvoiceDocumentNo());
//                paymentProcess.setParentFiscalYear(prepareRunDocument.getInvoiceFiscalYear());
//
//                paymentProcess.setChild(true);
//                paymentProcess.setProposal(false);
//                this.paymentProcessService.save(paymentProcess);
//
//
//                PaymentInformation paymentInformation = new PaymentInformation(specialLine);
//                paymentInformation.setId(informationSeqNo++);
//                paymentInformation.setPaymentProcessId(paymentProcess.getId());
//                this.paymentInformationService.save(paymentInformation);
//                log.info("K3 KX processSeqNo {} ", processSeqNo);
//                log.info("K3 KX informationSeqNo {} ", informationSeqNo);

                    APPaymentLine line = new APPaymentLine();
                    line.setInvCompCode(specialLine.getInvoiceCompanyCode());
                    line.setInvDocNo(specialLine.getInvoiceDocumentNo());
                    line.setInvFiscalYear(specialLine.getInvoiceFiscalYear());
                    line.setInvLine(specialLine.getLine());
                    line.setDocType(specialLine.getInvoiceDocumentType());
                    line.setDateAcct(specialLine.getDateAcct());
                    line.setDateDoc(specialLine.getDateDoc());
                    line.setVendor(specialLine.getVendorCode());
                    line.setPayee(specialLine.getPayeeCode());

                    line.setDrCr(specialLine.getDrCr());
                    line.setAmount(specialLine.getInvoiceAmount());
                    line.setWtxAmount(specialLine.getInvoiceWtxAmount());

                    totalAmount = totalAmount.add(specialLine.getInvoiceAmountPaid());

                    listAPPaymentLine.add(line);

                }
            }
            aPPaymentHeader.setCountLine(listAPPaymentLine.size());
            aPPaymentHeader.setAmount(prepareRunDocument.getInvoiceAmountPaid().subtract(totalAmount));
            apPaymentRequest.setHeader(aPPaymentHeader);
            apPaymentRequest.setLines(listAPPaymentLine);
            apPaymentRequest.setWebInfo(webInfo);

            try {
                FIMessage fiMessage = new FIMessage();
                fiMessage.setId(prepareRunDocument.getPmGroupDoc());
                fiMessage.setType(FIMessage.Type.REQUEST.getCode());
                fiMessage.setDataType(FIMessage.DataType.CREATE.getCode());
                fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
                fiMessage.setTo("99999");
                log.info("apPaymentRequest {}", apPaymentRequest);
                fiMessage.setData(XMLUtil.xmlMarshall(APPaymentRequest.class, apPaymentRequest));

                log.info("fiMessage {}", fiMessage);
                this.send(fiMessage);

//            PaymentProcess paymentProcess = new PaymentProcess(item);
//
//            paymentProcess.setId(processSeqNo++);
//            paymentProcess.setPmGroupNo(item.getPmGroupNo());
//            paymentProcess.setPmGroupDoc(item.getPmGroupDoc());
//
//            this.paymentProcessService.save(paymentProcess);
//
//            PaymentInformation paymentInformation = new PaymentInformation(item);
//            paymentInformation.setId(informationSeqNo++);
//            paymentInformation.setPaymentProcessId(paymentProcess.getId());
//            this.paymentInformationService.save(paymentInformation);
//            log.info("normal running {}", processSeqNo);
//            log.info("normal running {}", informationSeqNo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void send(FIMessage message) throws Exception {
        log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
        final AtomicReference<Message> msg = new AtomicReference<>();
        jmsTemplate.convertAndSend("99999.AP.Payment", XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
            msg.set(message1);
            return message1;
        });
        log.info("msg id : {}", msg.get().getJMSMessageID());
    }

    @Override
    public ResponseEntity<Result<List<PaymentReport>>> findProposalDocumentForBlock(HttpServletRequest
                                                                                            httpServletRequest, Long id, WSWebInfo webInfo) {
        Result<List<PaymentReport>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<PaymentReport> list = paymentReportService.findProposalDocument(id);
            result.setStatus(HttpStatus.OK.value());
            result.setData(list);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void blockDocument(ProposalDocumentRequest request) {

        try {

            List<ProposalDocument> proposalDocumentList = request.getProposalDocumentList();

            for (ProposalDocument proposalDocument : proposalDocumentList) {

                log.info(" proposalDocument {} ", proposalDocument);
                GLHead glHeadDocumentPay = glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(proposalDocument.getOriginalCompanyCode(), proposalDocument.getOriginalDocumentNo(), proposalDocument.getOriginalFiscalYear());
//                log.info(" glHeadDocumentPay {} ", glHeadDocumentPay);
//                List<GLHead> listHead = glHeadService.findOriginalDocNo(glHeadDocumentPay.getOriginalDocument().substring(0, 10));
//
//                GLHead originalDocument = null;
//                for (GLHead item : listHead) {
//
//                    if (item.getOriginalDocument().startsWith(item.getOriginalDocumentNo())) {
//                        originalDocument = item;
//                    }
//
//                    GLLine glLine = glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(item.getCompanyCode(), item.getOriginalDocumentNo(), item.getOriginalFiscalYear(),"K");
//                    if (null != glLine) {
//                        glLine.setPaymentBlock("B");
//                        glLineService.save(glLine);
//                    }
//
//
//                }
                glHeadDocumentPay.setPaymentId(null);
                glHeadDocumentPay.setPaymentDocumentNo(null);
                glHeadService.save(glHeadDocumentPay);

//                APUpPbkToBRequest aPUpPbkToBRequest = new APUpPbkToBRequest(originalDocument, request.getWebInfo());

//                APUpPbkToAllRequest aPUpPbkToAllRequest = new APUpPbkToAllRequest();
//                aPUpPbkToAllRequest.setFlag(1);
//                aPUpPbkToAllRequest.setAccountDocNo(originalDocument.getOriginalDocumentNo());
//                aPUpPbkToAllRequest.setCompCode(originalDocument.getCompanyCode());
//                aPUpPbkToAllRequest.setFiscalYear(originalDocument.getOriginalFiscalYear());
//                aPUpPbkToAllRequest.setPaymentBlock("B");
//                aPUpPbkToAllRequest.setCreated(new Timestamp(System.currentTimeMillis()));
//                aPUpPbkToAllRequest.setWebInfo(request.getWebInfo());
//
//                FIMessage fiMessage = new FIMessage();
//                fiMessage.setId(aPUpPbkToAllRequest.getCompCode().substring(0, 2).concat(".PBKAL.").concat(aPUpPbkToAllRequest.getAccountDocNo()));
//                fiMessage.setType(FIMessage.Type.REQUEST.getCode());
//                fiMessage.setDataType(FIMessage.DataType.PBK_ALL.getCode());
//                fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
//                fiMessage.setTo(aPUpPbkToAllRequest.getCompCode().substring(0, 2));
//
//                fiMessage.setData(XMLUtil.xmlMarshall(APUpPbkToAllRequest.class, aPUpPbkToAllRequest));
//
//                log.info("fiMessage all : {}", XMLUtil.xmlMarshall(FIMessage.class, fiMessage));
//
//                log.info("aPUpPbkToAllRequest : {}", XMLUtil.xmlMarshall(APUpPbkToAllRequest.class, aPUpPbkToAllRequest));
//
//                this.sendBlock(fiMessage, aPUpPbkToAllRequest.getCompCode());

//                paymentInformationService.deleteById(proposalDocumentRequest.getPaymentInformationId());
//                paymentProcessService.deleteById(proposalDocumentRequest.getPaymentProcessId());
                log.info("getPaymentProcessId : {}", "");
                System.out.println(proposalDocument.getId());
                paymentProcessService.updateProposalBlock(proposalDocument.getId());
            }
            List<PaymentProcess> paymentProcessList = paymentProcessService.findAllByPaymentIdAndProposalNotChild(proposalDocumentList.get(0).getPaymentAliasId(), true);
            List<PaymentProcess> total = paymentProcessList.stream().filter(object -> !object.isProposalBlock()).collect(Collectors.toList());
            List<PaymentProcess> success = total.stream().filter(object -> "S".equalsIgnoreCase(object.getStatus())).collect(Collectors.toList());
            PaymentAlias paymentAlias =   paymentAliasService.findOneById(proposalDocumentList.get(0).getPaymentAliasId());
            paymentAlias.setProposalTotalDocument(total.size());
            paymentAlias.setProposalSuccessDocument(success.size());
            paymentAliasService.save(paymentAlias);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public ResponseEntity<Result<PaymentAlias>> proposalJob(PaymentAlias paymentAlias, WSWebInfo webInfo) {
        Result<PaymentAlias> result = new Result<>();
        result.setTimestamp(new Date());

        try {
//            PaymentAlias paymentAlias = paymentAliasService.findOneById(id);
            paymentAlias.setProposalStart(new Timestamp(System.currentTimeMillis()));
            paymentAlias.setProposalEnd(null);
            // เอากลับมาชั่วคราว
            if (null == paymentAlias.getProposalStatus() || paymentAlias.getProposalStatus().equalsIgnoreCase("W") || paymentAlias.getProposalStatus().equalsIgnoreCase("WP")) {
                paymentAlias.setProposalStatus("P");
                paymentAlias.setProposalSuccessDocument(0);
                paymentAlias.setProposalTotalDocument(0);
                paymentAliasService.save(paymentAlias);
                this.paymentAsyncService.createProposalNew(paymentAlias, webInfo);
            } else if (paymentAlias.getProposalStatus().equalsIgnoreCase("P")) {
                result.setData(null);
                result.setMessage("การดำเนินการชำระเงิน " + paymentAlias.getPaymentName() + " ได้รับการประมวลผลโดยผู้ใช้อื่น");
                result.setStatus(HttpStatus.LOCKED.value());
                return new ResponseEntity<>(result, HttpStatus.LOCKED);
            }

//            result.setData(paymentAliasService.save(paymentAlias));
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
    public ResponseEntity<Result<PaymentAlias>> realRunJob(PaymentAlias paymentAlias, WSWebInfo webInfo) {
        Result<PaymentAlias> result = new Result<>();
        result.setTimestamp(new Date());
        try {
//            PaymentAlias paymentAlias = paymentAliasService.findOneById(id);
            List<PrepareRealRunDocument> prepareRealRunDocumentList = prepareRunDocumentService.findProposalDocument(paymentAlias.getId(), true);
//            List<PrepareRunDocument> prepareRunDocumentList = prepareRunDocumentService.findProposalDoc(paymentAlias.getId(), true);
            log.info("prepareRunDocumentList : {}", prepareRealRunDocumentList.size());
            this.paymentAsyncService.createRealRunNew(prepareRealRunDocumentList, paymentAlias, webInfo);

            paymentAlias.setRunStart(new Timestamp(System.currentTimeMillis()));
            paymentAlias.setRunStatus("S");
            paymentAlias.setRunJobStatus("S");
            paymentAlias.setRunTotalDocument(prepareRealRunDocumentList.size());
            paymentAlias.setRunSuccessDocument(0);
            result.setData(paymentAliasService.save(paymentAlias));
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendBlock(FIMessage message, String compCode) throws Exception {
        log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
        final AtomicReference<Message> msg = new AtomicReference<>();
        jmsTemplate.convertAndSend(compCode.substring(0, 2) + ".AP.UpPbk",
                XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
                    msg.set(message1);
                    return message1;
                });
        log.info("msg id : {}", msg.get().getJMSMessageID());
    }

}
