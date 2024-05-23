package th.com.bloomcode.paymentservice.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.com.bloomcode.paymentservice.context.Context;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.common.CompanyCondition;
import th.com.bloomcode.paymentservice.model.config.*;
import th.com.bloomcode.paymentservice.model.idem.HouseBankPaymentMethod;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareProposalDocument;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;
import th.com.bloomcode.paymentservice.payment.entity.mapping.PaymentDocument;
import th.com.bloomcode.paymentservice.payment.entity.mapping.PaymentRealRun;
import th.com.bloomcode.paymentservice.payment.entity.mapping.PaymentRunDocument;
import th.com.bloomcode.paymentservice.payment.entity.mapping.ProposalDocumentResponse;
import th.com.bloomcode.paymentservice.service.payment.PaymentAliasService;
import th.com.bloomcode.paymentservice.service.payment.PaymentProcessService;
import th.com.bloomcode.paymentservice.service.payment.impl.GLHeadServiceImpl;
import th.com.bloomcode.paymentservice.util.JSONUtil;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.model.FIMessage;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;
import th.com.bloomcode.paymentservice.webservice.model.request.FIPostRequest;

import javax.jms.Message;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class PaymentService {

    private static final String MESSAGE_PROPOSAL = "Log for proposal run for payment on {}, identification {}";
    private final JmsTemplate jmsTemplate;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EntityManager entityManager;

    private final PaymentAliasService paymentAliasService;

    private final GLHeadServiceImpl glHeadService;

    private final PaymentProcessService paymentProcessService;

    @Value("${payment.dblink.schema}")
    private String schema;

    @Value("${payment.dblink.user}")
    private String user;

    @Autowired
    public PaymentService(@Qualifier("paymentEntityManagerFactory") EntityManager entityManager, JmsTemplate jmsTemplate,
                          PaymentAliasService paymentAliasService,
                          GLHeadServiceImpl glHeadService,
                          PaymentProcessService paymentProcessService) {
        this.entityManager = entityManager;
        this.jmsTemplate = jmsTemplate;
        this.paymentAliasService = paymentAliasService;
        this.glHeadService = glHeadService;
        this.paymentProcessService = paymentProcessService;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void process(Long id, Long type) {

        PaymentAlias paymentAlias = paymentAliasService.findOneById(id);
        Map<String, Object> params = new HashMap<>();
        ParameterConfig parameterConfig = JSONUtil.convertJsonToObject(paymentAlias.getJsonText(),
                ParameterConfig.class);
        Payment payment = parameterConfig.getPayment();
        Parameter parameter = parameterConfig.getParameter();
        AdditionLog additionLog = parameterConfig.getAdditionLog();
        List<CompanyCondition> companyConditions = parameter.getCompanyCondition();
        List<VendorConfig> vendors = parameter.getVendor();
        List<Independent> independents = parameterConfig.getIndependent();


//        List<PrepareProposalDocument> prepareProposalDocuments = prepareProposalDocumentService.findUnBlockDocumentCanPayByParameter(parameterConfig);
//
//        log.info("PrepareProposalDocument {} ", prepareProposalDocuments.size());
//
//        if (type == 0) {
//            proposal(id, prepareProposalDocuments, paymentAlias, parameter, additionLog);
//        } else if (type == 1) {
////            run(id, paymentRunDocuments, paymentAlias, parameter, additionLog);
//        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void paymentRun(Long id, WSWebInfo webInfo) {
//        PaymentAlias paymentAlias = paymentAliasService.findOneById(id);
//        ParameterConfig parameterConfig = JSONUtil.convertJsonToObject(paymentAlias.getJsonText(),
//                ParameterConfig.class);
//        Parameter parameter = parameterConfig.getParameter();
//
//        List<PaymentRealRun> listDocument = selectDocument(String.valueOf(paymentAlias.getId()), true);
//
//        int seqNo = 0;
//        for (PaymentRealRun item : listDocument) {
//            seqNo++;
//            logger.info("seq : {}", seqNo);
//
//            APPaymentRequest apPaymentRequest = new APPaymentRequest();
//
//            APPaymentHeader aPPaymentHeader = new APPaymentHeader();
//            aPPaymentHeader.setPmCompCode("99999");
//            aPPaymentHeader.setPmDate(item.getPaymentDate());
//            aPPaymentHeader.setPmIden(item.getPaymentName());
//            aPPaymentHeader.setVendor(item.getVendor());
//            aPPaymentHeader.setPayee(item.getPayeeCode());
//
//            aPPaymentHeader.setPmGroupNo(item.getPmGroupNo());
//            aPPaymentHeader.setPmGroupDoc(item.getPmGroupDoc());
//
//            aPPaymentHeader.setVendorTaxID(item.getTaxId());
//            aPPaymentHeader.setBankAccNo(item.getPayeeBankAccountNo());
//            aPPaymentHeader.setBranchNo(item.getPayeeBankKey());
//            aPPaymentHeader.setGlAccount(item.getGlAccount());
//            aPPaymentHeader.setAmount(item.getAmount());
//            aPPaymentHeader.setReceiptTaxID(item.getVendor());
//            aPPaymentHeader.setCountLine(1);
////            PayMethodConfig payMethodConfig = payMethodConfigService.findOneByCountryAndPayMethod("TH",
////                    item.getPaymentMethod());
//            PayMethodConfig payMethodConfig = Context.sessionPayMethodConfig.get("TH-" + item.getPaymentMethod());
//            aPPaymentHeader.setDocType(payMethodConfig.getDocumentTypeForPayment());
//
//            aPPaymentHeader.setDateDoc(Util.stringToTimestamp(parameter.getSaveDate()));
//            aPPaymentHeader.setDateAcct(Util.stringToTimestamp(parameter.getPostDate()));
//
//            List<APPaymentLine> listaPPaymentLine = new ArrayList<>();
//
//            APPaymentLine aPPaymentLine = new APPaymentLine();
//            aPPaymentLine.setInvCompCode(item.getCompCode());
//            aPPaymentLine.setInvDocNo(item.getAccDocNo());
//            aPPaymentLine.setInvFiscalYear(item.getFiscalYear());
//            aPPaymentLine.setInvLine(item.getLine());
//            aPPaymentLine.setDocType(item.getDocType());
//            aPPaymentLine.setDateAcct(item.getDateAcct());
//            aPPaymentLine.setDateDoc(item.getDateDue());
//            aPPaymentLine.setVendor(item.getVendor());
//            aPPaymentLine.setPayee(item.getPayeeCode());
//
//            aPPaymentLine.setDrCr(item.getDrCr());
//            aPPaymentLine.setAmount(item.getAmount());
//            aPPaymentLine.setWtxAmount(item.getWtxAmount());
//
//            listaPPaymentLine.add(aPPaymentLine);
//
//            BigDecimal totalAmount = BigDecimal.ZERO;
////            if (item.getDocType().equalsIgnoreCase("KA") || item.getDocType().equalsIgnoreCase("KB") ||
////                    item.getDocType().equalsIgnoreCase("KG") || item.getDocType().equalsIgnoreCase("KC")) {
////
////                List<PaymentRunDocument> specialDoc = this.findDocTypeK3ORKX(item.getAccDocNo(), item.getFiscalYear());
////
////                for (PaymentRunDocument specialLine : specialDoc) {
////
////
////                    APPaymentLine line = new APPaymentLine();
////                    line.setInvCompCode(specialLine.getCompCode());
////                    line.setInvDocNo(specialLine.getAccDocNo());
////                    line.setInvFiscalYear(specialLine.getFiscalYear());
////                    line.setInvLine(specialLine.getLineNo());
////                    line.setDocType(specialLine.getDocType());
////                    line.setDateAcct(specialLine.getDateAcct());
////                    line.setDateDoc(specialLine.getDateDoc());
////                    line.setVendor(specialLine.getVendor());
////                    line.setPayee(specialLine.getPayee());
////
////                    line.setDrCr(specialLine.getDrCr());
////                    line.setAmount(specialLine.getAmount());
////                    line.setWtxAmount(specialLine.getWtxAmount());
////
////                    totalAmount = totalAmount.add(specialLine.getAmount());
////
////                    listaPPaymentLine.add(line);
////
////                }
////                listaPPaymentLine.get(0).setAmount(listaPPaymentLine.get(0).getAmount().add(totalAmount));
////            }
//
//
//            apPaymentRequest.setHeader(aPPaymentHeader);
//            apPaymentRequest.setLines(listaPPaymentLine);
//            apPaymentRequest.setWebInfo(webInfo);
//
//            try {
//                FIMessage fiMessage = new FIMessage();
//                fiMessage.setId(item.getPmGroupDoc());
//                fiMessage.setType(FIMessage.Type.REQUEST.getCode());
//                fiMessage.setDataType(FIMessage.DataType.CREATE.getCode());
//                fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
//                fiMessage.setTo("99999");
//                logger.info("apPaymentRequest {}", apPaymentRequest);
//                fiMessage.setData(XMLUtil.xmlMarshall(APPaymentRequest.class, apPaymentRequest));
//
//                logger.info("fiMessage {}", fiMessage);
//                this.send(fiMessage);
//
//
////                if (item.getDocType().equalsIgnoreCase("KA") || item.getDocType().equalsIgnoreCase("KB") ||
////                        item.getDocType().equalsIgnoreCase("KG") || item.getDocType().equalsIgnoreCase("KC")) {
////
////                    logger.info("getPaymentId {}", item.getPaymentId());
////                    logger.info("getCompCode {}", item.getCompCode());
////                    logger.info("getAccDocNo {}", item.getAccDocNo());
////                    logger.info("getFiscalYear {}", item.getFiscalYear());
////
////                    List<PaymentProcess> specialDocProcess = this.paymentProcessService.findAllByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalTrueAndIsChildTrue(item.getPaymentId(), item.getCompCode(), item.getAccDocNo(), item.getFiscalYear());
////
////                    List<PaymentInformation> specialDocInformation = this.paymentInformationService.findAllByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalTrueAndIsChildTrue(item.getPaymentId(), item.getCompCode(), item.getAccDocNo(), item.getFiscalYear());
////
////
////                    logger.info("specialDocProcess {}", specialDocProcess.size());
////                    logger.info("specialDocInformation {}", specialDocInformation.size());
////
////                    if (specialDocProcess.size() > 0) {
////                        for (PaymentProcess process : specialDocProcess) {
////                            PaymentProcess paymentProcess = new PaymentProcess();
////                            BeanUtils.copyProperties(process, paymentProcess);
////                            paymentProcess.setId(null);
////                            paymentProcess.setPaymentDocNo("XXXXXXXXXX");
////                            paymentProcess.setChild(true);
////                            paymentProcess.setProposal(false);
////                            this.paymentProcessService.save(paymentProcess);
////                        }
////                    }
////                    if (specialDocInformation.size() > 0) {
////                        for (PaymentInformation information : specialDocInformation) {
////                            PaymentInformation paymentInformation = new PaymentInformation();
////                            BeanUtils.copyProperties(information, paymentInformation);
////                            paymentInformation.setId(null);
////                            paymentInformation.setPaymentDocNo("XXXXXXXXXX");
////                            paymentInformation.setChild(true);
////                            paymentInformation.setProposal(false);
////                            this.paymentInformationService.save(paymentInformation);
////                        }
////
////                    }
////
////                }
//
//                PaymentProcess paymentProcess = new PaymentProcess(item);
//                paymentProcess.setPmGroupNo(item.getPmGroupNo());
//                paymentProcess.setPmGroupDoc(item.getPmGroupDoc());
//
//                paymentProcess.setInvoiceAccDocNo(item.getInvoiceAccDocNo());
//                paymentProcess.setInvoiceCompCode(item.getInvoiceCompCode());
//                paymentProcess.setInvoiceFiscalYear(item.getInvoiceFiscalYear());
//                paymentProcess.setInvoiceDocType(item.getInvoiceDocType());
//                paymentProcess.setInvoicePaymentCenter(item.getInvoicePaymentCenter());
//                paymentProcess.setAccDocNoPaymentCenter(item.getAccDocNoPaymentCenter());
//
//                paymentProcess.setInvoiceWtxAmount(item.getInvoiceWtxAmount());
//                paymentProcess.setInvoiceWtxBase(item.getInvoiceWtxBase());
//                paymentProcess.setInvoiceWtxAmountP(item.getInvoiceWtxAmountP());
//                paymentProcess.setInvoiceWtxBaseP(item.getInvoiceWtxBaseP());
//
//                this.paymentProcessService.save(paymentProcess);
//
//                PaymentInformation paymentInformation = new PaymentInformation(item);
//                paymentInformation.setPmGroupNo(item.getPmGroupNo());
//                paymentInformation.setPmGroupDoc(item.getPmGroupDoc());
//                this.paymentInformationService.save(paymentInformation);
//
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
////            }
//
//        }
//
//        paymentAlias.setRunStatus("S");
//        paymentAlias.setRunTotalDocument(listDocument.size());
//        paymentAlias.setRunSuccessDocument(listDocument.size());
////        paymentAliasService.updateStatus(paymentAlias);


    }

    private void proposal(Long id, List<PrepareProposalDocument> paymentDocuments, PaymentAlias paymentAlias,
                          Parameter parameter, AdditionLog additionLog) {

        AtomicInteger seq = new AtomicInteger(1);
        String vendorOld = "";
        String compCodeOld = "";

        int index = 1;



        List<PrepareProposalDocument> paymentDocumentList = new ArrayList<>();
        logger.info("paymentDocumentList : {}", paymentDocumentList);
        logger.info("paymentDocumentList : {}", paymentDocumentList.size());


//        List<Vendor> alternatePayees = vendorService.findAllAlternativeVendor();
//        List<Vendor> vendors = Context.sessionVendors;
//        List<VendorBankAccount> vendorBankAccounts = vendorBankAccountService.findAll();

        int seqDoc = 0;
        Long seqNo = getNextSeries(true);
        for (PrepareProposalDocument paymentDocument : paymentDocuments) {
            seqDoc++;
//            logger.info("paymentDocument : {}", paymentDocument.getAccDocNo());
//            logger.info("seq : {}", seqDoc);

            String paymentDocNo = SqlUtil.generateProposalDocument(seqNo++);
            paymentDocument.setPaymentClearingDocNo(paymentDocNo);
            paymentDocument.setPaymentClearingDate(parameter.getPostDate());
            paymentDocument.setPaymentClearingEntryDate(new Timestamp(new Date().getTime()));
            paymentDocument.setPaymentClearingYear(paymentDocument.getPaymentClearingYear());
            paymentDocument.setPaymentDate(paymentAlias.getPaymentDate());
            paymentDocument.setPaymentDateAcct(paymentAlias.getPaymentDate());
            paymentDocument.setPaymentName(paymentAlias.getPaymentName());
            paymentDocument.setPaymentId(paymentAlias.getId());


//            HouseBankPaymentMethod houseBankPaymentMethod = this.houseBankPaymentMethodService.findOneByHouseBankAndPaymentMethod("99999", "01000", paymentDocument.getPaymentMethod());
            HouseBankPaymentMethod houseBankPaymentMethod = Context.sessionHouseBankPaymentMethod.get(paymentDocument.getPaymentMethod());

            paymentDocument.setPayingBankCode(houseBankPaymentMethod.getAccountCode());
            paymentDocument.setPayingHouseBank(houseBankPaymentMethod.getHouseBank());
            paymentDocument.setPayingBankAccountNo(houseBankPaymentMethod.getBankAccountCode());
            paymentDocument.setPayingBankCountry(houseBankPaymentMethod.getCountryCode());
            paymentDocument.setPayingBankNo(houseBankPaymentMethod.getBankAccountCode());
            paymentDocument.setPayingGLAccount(houseBankPaymentMethod.getGlAccount());
            paymentDocument.setPayingBankKey(houseBankPaymentMethod.getBankBranch());
            paymentDocument.setPayingBankName(houseBankPaymentMethod.getHouseBank());
            paymentDocument.setPayingCompCode("99999");

            paymentDocument.setAmountPaid(paymentDocument.getAmount().subtract(paymentDocument.getWtxAmount()));
            if (null != paymentDocument.getPaymentBlock() && !paymentDocument.getPaymentBlock().equalsIgnoreCase("")
                    && !paymentDocument.getPaymentBlock().equalsIgnoreCase(" ")) {
                String errorCode = "003";
                paymentDocument.setStatus("E");
                paymentDocument.setErrorCode(errorCode);
            }

//             003 payment block <> blank รายการถูกระงับในการชำระเงิน
//             012 amount < config paid min amount จำนวนเงินที่จะจ่ายยังไม่ถึงจำนวนเงินขั้นต่ำสำหรับการชำระเงิน
//             016 payment method == blank วิธีการชำระเงินในการประมวลผลครั้งนี้ไม่ได้ระบุในรายการ
//             024 TH_APBPAccessControl == null ข้อมูลหลักของผู้รับเงินหายไป
//            if (null != paymentDocument.getPayee() && !paymentDocument.getPayee().equalsIgnoreCase("")) {
//                Vendor alternatePayee = alternatePayees.stream().filter(a -> paymentDocument.getPayee().equalsIgnoreCase(a.getValueCode())).findAny()
//                        .orElse(null);
//                if (null == alternatePayee) {
//                    String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                            ? paymentDocument.getErrorCode() + ",024" : "024";
//                    paymentDocument.setStatus("E");
//                    paymentDocument.setErrorCode(errorCode);
//                } else {
//                    // 034 C_BP_Relation TH_APAlternatePayeeStatus.ConfirmStatus == 0 ข้อมูลผู้รับเงินมีสถานะ "รอการยืนยัน"
//                    // 036 C_BP_Relation TH_APAlternatePayeeStatus.ConfirmStatus == 2 ข้อมูลผู้รับเงินมีสถานะ "ไม่ยืนยัน"
//                    log.info("alternatePayee.getVendorStatus() : {}", alternatePayee.getVendorStatus());
//                    if (null != alternatePayee.getVendorStatus() && alternatePayee.getVendorStatus().equalsIgnoreCase("0")) {
//                        String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                                ? paymentDocument.getErrorCode() + ",034" : "034";
//                        paymentDocument.setStatus("E");
//                        paymentDocument.setErrorCode(errorCode);
//                    } else if (null != alternatePayee.getVendorStatus() && alternatePayee.getVendorStatus().equalsIgnoreCase("2")) {
//                        String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                                ? paymentDocument.getErrorCode() + ",036" : "036";
//                        paymentDocument.setStatus("E");
//                        paymentDocument.setErrorCode(errorCode);
//                    }
//                }
//            }
//            Vendor vendor = vendors.stream().filter(v -> paymentDocument.getVendor().equalsIgnoreCase(v.getValueCode()) && paymentDocument.getCompCode().equalsIgnoreCase(v.getCompCode())).findAny().orElse(null);
//            if (null != vendor) {
//                // 031 C_BPartner !isActive ข้อมูลผู้ขายได้รับการทำเครื่องหมายแฟลกสำหรับการลบ
//                if (!vendor.isActive()) {
//                    String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                            ? paymentDocument.getErrorCode() + ",031" : "031";
//                    paymentDocument.setStatus("E");
//                    paymentDocument.setErrorCode(errorCode);
//                }
//
//                // 033 TH_APBPartnerStatus.ConfirmStatus == 0 ข้อมูลผู้ขายมีสถานะ "รอการยืนยัน"
//                // 035 TH_APBPartnerStatus.ConfirmStatus == 2 ข้อมูลผู้ขายมีสถานะ "ไม่ยืนยัน"
//                if (vendor.getVendorStatus().equalsIgnoreCase("0")) {
//                    String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                            ? paymentDocument.getErrorCode() + ",033" : "033";
//                    paymentDocument.setStatus("E");
//                    paymentDocument.setErrorCode(errorCode);
//                } else if (vendor.getVendorStatus().equalsIgnoreCase("2")) {
//                    String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                            ? paymentDocument.getErrorCode() + ",035" : "035";
//                    paymentDocument.setStatus("E");
//                    paymentDocument.setErrorCode(errorCode);
//                }
//            }
//
//            // 060 DrCr = D || paid amount <= 0 การถูกระงับเนื่องจากยอดคงเหลือทางด้านเดบิท
//            if (paymentDocument.getDrCr().equalsIgnoreCase("D") || paymentDocument.getAmountPaid().compareTo(BigDecimal.ZERO) == 0) {
//                String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                        ? paymentDocument.getErrorCode() + ",060" : "060";
//                paymentDocument.setStatus("E");
//                paymentDocument.setErrorCode(errorCode);
//            }
//            // 071 bankAccountNo != C_BPartner.BankNo ไม่พบธนาคารคุ่ค้าที่ถูกต้อง
//            VendorBankAccount vendorBankAccount = vendorBankAccounts.stream().filter(v -> paymentDocument.getBankAccountNo().equalsIgnoreCase(v.getBankAccountNo()) && paymentDocument.getVendor().equalsIgnoreCase(v.getVendor())).findAny().orElse(null);
//            if (null == vendorBankAccount) {
//                String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                        ? paymentDocument.getErrorCode() + ",071" : "071";
//                paymentDocument.setStatus("E");
//                paymentDocument.setErrorCode(errorCode);
//            }

//            if (!Context.sessionPayment.contains(paymentDocument.getPaymentMethod())) {
//                String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                        ? paymentDocument.getErrorCode() + ",012" : "012";
//                paymentDocument.setStatus("E");
//                paymentDocument.setErrorCode(errorCode);
//            }
//
//            if (Context.sessionVendor.get(paymentDocument.getVendor()) == null) {
//                String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                        ? paymentDocument.getErrorCode() + ",002" : "002";
//                paymentDocument.setStatus("E");
//                paymentDocument.setErrorCode(errorCode);
//
//
//            } else {
//                if (!Context.sessionVendor.get(paymentDocument.getVendor()).isActive()) {
//                    String errorCode = null != paymentDocument.getErrorCode() && paymentDocument.getErrorCode().length() > 0
//                            ? paymentDocument.getErrorCode() + ",003" : "003";
//                    paymentDocument.setStatus("E");
//                    paymentDocument.setErrorCode(errorCode);
//                }
//            }

            paymentDocumentList.add(paymentDocument);
            logger.info("after add : {}", paymentDocument.getOriginalDocumentNo());
        }

        updateNextSeries(seqNo, true);

//        Map<String, List<PaymentRunDocument>> paymentGroupCompCode = paymentDocumentList.stream()
//                .filter(p -> !"E".equalsIgnoreCase(p.getStatus()))
//                .collect(Collectors.groupingBy(this::getGroupingByKey, Collectors.mapping((PaymentRunDocument p) -> p, toList())));
//        Map<String, List<PaymentRunDocument>> paymentGroupPaymentMethod = paymentDocumentList.stream()
//                .filter(p -> !"E".equalsIgnoreCase(p.getStatus()))
//                .collect(Collectors.groupingBy(PaymentRunDocument::getPaymentMethod, Collectors.mapping((PaymentRunDocument p) -> p, toList())));
//        List<PaymentRunDocument> paymentError = paymentDocumentList.stream()
//                .filter(p -> "E".equalsIgnoreCase(p.getStatus())).collect(toList());
////        logger.info("paymentGroupCompCode : {}", paymentGroupCompCode);
////        logger.info("paymentGroupPaymentMethod : {}", paymentGroupPaymentMethod);
////        logger.info("paymentError : {}", paymentError);
//        this.paymentProposalLogService.clearLog();
//
//        if (!paymentDocuments.isEmpty()) {
//            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                    MessageFormat.format(Message.MESSAGE_516.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
//                    Message.MESSAGE_516.getMessageNo(), id);
//            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                    MessageFormat.format(Message.MESSAGE_550.getMessageText(), 1, "PAY SYSTEMS",
//                            paymentAlias.getPaymentName(), "99999CD0301"),
//                    MessageConstant.MESSAGE_CLASS_00, Message.MESSAGE_550.getMessageNo(), id);
//            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                    MessageFormat.format(Message.MESSAGE_402.getMessageText(), paymentAlias.getPaymentDate(),
//                            paymentAlias.getPaymentName()),
//                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_402.getMessageNo(), id);
//            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                    MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_FZ,
//                    Message.MESSAGE_693.getMessageNo(), id);
//
//            if (additionLog.isCheckBoxDueDate()) {
//                paymentGroupCompCode.forEach((k, v) -> {
////                    logger.info("key : {}", k);
//                    String[] key = k.split("-");
//                    String compCode = key[0];
//                    String vendor = key[1];
//
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_691.getMessageText(), vendor, compCode),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_691.getMessageNo(), id);
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_693.getMessageNo(), id);
//
//                    for (PaymentRunDocument paymentDocument : v) {
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_799.getMessageText(), ""),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_799.getMessageNo(), id);
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_700.getMessageText(),
//                                        paymentDocument.getAccDocNo(), paymentDocument.getLineNo(), "THB",
//                                        paymentDocument.getAmount()),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_700.getMessageNo(), id);
//
//                        if (null != paymentDocument.getPayee() && !paymentDocument.getPayee().equalsIgnoreCase("")) {
//                            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                    MessageFormat.format(Message.MESSAGE_735.getMessageText(),
//                                            paymentDocument.getPayee()),
//                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_735.getMessageNo(), id);
//                        }
//
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_701.getMessageText(),
//                                        paymentDocument.getDateBaseline(), 0, 0, 0, 0, 0),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_701.getMessageNo(), id);
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_726.getMessageText(), 0),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_726.getMessageNo(), id);
////			this.paymentProposalLogService.addSuccessLog(seq++,
////							MessageFormat.format(Message.MESSAGE_719.getMessageText(), "3200006", 2),
////							MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_719.getMessageNo(), id);
////			this.paymentProposalLogService.addSuccessLog(seq++,
////							MessageFormat.format(Message.MESSAGE_720.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_FZ,
////							Message.MESSAGE_720.getMessageNo(), id);
//
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_728.getMessageText(), Util.stringToDate(parameter.getPostDate()), Util.stringToDate(parameter.getPaymentDate())),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_728.getMessageNo(), id);
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_721.getMessageText(), 0),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_721.getMessageNo(), id);
////				this.paymentProposalLogService.addSuccessLog(seq++,
////								MessageFormat.format(Message.MESSAGE_708.getMessageText(), new Date(), new Date()),
////								MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_708.getMessageNo(), id);
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_693.getMessageNo(), id);
//
//                    }
//                });
//            }
//
//            if (additionLog.isCheckBoxPaymentMethodAll()) {
//                this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                        MessageFormat.format(Message.MESSAGE_699.getMessageText(), ""),
//                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_699.getMessageNo(), id);
//
//                paymentGroupPaymentMethod.forEach((k, v) -> {
//                    BigDecimal sumAmountPerPM = v.stream().map(PaymentDocument::getAmount).reduce(BigDecimal.ZERO,
//                            BigDecimal::add);
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_601.getMessageText(), "THB", sumAmountPerPM),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_601.getMessageNo(), id);
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_603.getMessageText(), k),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_603.getMessageNo(), id);
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_640.getMessageText(), ""),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_640.getMessageNo(), id);
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_644.getMessageText(), ""),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_644.getMessageNo(), id);
//
//                    for (PaymentRunDocument paymentDocument : v) {
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_645.getMessageText(),
//                                        paymentDocument.getCountryCode(), paymentDocument.getPayeeBankKey(),
//                                        paymentDocument.getBankAccountNo()),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_645.getMessageNo(), id);
//
//                    }
//
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_665.getMessageText(), ""),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_665.getMessageNo(), id);
//
//                    for (PaymentRunDocument paymentDocument : v) {
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_663.getMessageText(),
//                                        paymentDocument.getPayeeBankKey(), paymentDocument.getBankAccountNo(),
//                                        "ออมทรัพย์", "1010"),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_663.getMessageNo(), id);
//
//                    }
//
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_668.getMessageText(), ""),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_668.getMessageNo(), id);
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_648.getMessageText(), "01000"),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_648.getMessageNo(), id);
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_658.getMessageText(), "THB", "0",
//                                    "999999999999999.00"),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_658.getMessageNo(), id);
//
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_641.getMessageText(), "010001", "G1006"),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_641.getMessageNo(), id);
//
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_642.getMessageText(), "TH", "0010001", "0010041771"),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_642.getMessageNo(), id);
//
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_609.getMessageText(), k),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_609.getMessageNo(), id);
//
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_693.getMessageNo(), id);
//                });
//            }
//
//            if (additionLog.isCheckBoxPaymentMethodUnSuccess()) {
//                this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                        MessageFormat.format(Message.MESSAGE_699.getMessageText(), ""),
//                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_699.getMessageNo(), id);
//
//                paymentError.forEach((v) -> {
//                    BigDecimal sumAmountPerPM = v.getAmount();
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_601.getMessageText(), "THB", sumAmountPerPM),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_601.getMessageNo(), id);
//
//                    if (v.getErrorCode().equalsIgnoreCase("003")) {
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_503.getMessageText(), ""),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_503.getMessageNo(), id);
//                    }
//
//                    if (v.getErrorCode().equalsIgnoreCase("012")) {
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_512.getMessageText(), ""),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_512.getMessageNo(), id);
//                    }
//
//                    if (v.getErrorCode().equalsIgnoreCase("016")) {
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_518.getMessageText(), ""),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_518.getMessageNo(), id);
//                    }
//
//                    if (v.getErrorCode().equalsIgnoreCase("024")) {
//                        this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                MessageFormat.format(Message.MESSAGE_524.getMessageText(), ""),
//                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_524.getMessageNo(), id);
//                    }
//
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_398.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
//                            Message.MESSAGE_398.getMessageNo(), id);
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_517.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
//                            Message.MESSAGE_517.getMessageNo(), id);
//                });
//            }
//            if (additionLog.isCheckBoxDisplayDetail()) {
//                this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                        MessageFormat.format(Message.MESSAGE_798.getMessageText(), ""),
//                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_798.getMessageNo(), id);
//
//                paymentDocumentList.forEach(paymentRunDocument -> {
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_741.getMessageText(), paymentRunDocument.getPaymentClearingDocNo(), paymentRunDocument.getCompCode(), paymentRunDocument.getCurrency(), paymentRunDocument.getPaymentMethod()),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_741.getMessageNo(), id);
//
//                    this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                            MessageFormat.format(Message.MESSAGE_743.getMessageText(), ""),
//                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_743.getMessageNo(), id);
//
//                    GLLine glLine = glLineService.findOneByCompCodeAndAccDocNoAndFiscalYearAndAccountType(paymentRunDocument.getCompCode(), paymentRunDocument.getAccDocNo(), paymentRunDocument.getFiscalYear());
//                    PayMethodConfig payMethodConfig = payMethodConfigService.findOneByCountryAndPayMethod("TH",
//                            paymentRunDocument.getPaymentMethod());
//
//                    String fundSource = "";
//                    if (null != glLine) {
//                        if (glLine.getFundSource().substring(2, 4).equalsIgnoreCase("10")) {
//                            fundSource = glLine.getFundSource().substring(2, 4);
//                        } else {
//                            fundSource = glLine.getFundSource().substring(2, 5);
//                        }
//                    }
//                    GLAccount glAccount;
//                    logger.info("payMethodConfig : {}", payMethodConfig);
//                    logger.info("fundSource : {}", fundSource);
//
//                    if (payMethodConfig.getDocumentTypeForPayment().equalsIgnoreCase("PB") || payMethodConfig.getDocumentTypeForPayment().equalsIgnoreCase("PD")) {
//
////                        glAccount = glAccountService.findOneByDocTypeAndFundSource(payMethodConfig.getDocumentTypeForPayment(), "**");
//                        glAccount = Context.sessionGlAccountForPayment.get(payMethodConfig.getDocumentTypeForPayment() + "-**");
//                        if (null == glAccount) {
//                            glAccount = new GLAccount();
//                            glAccount.setAgencyGLAccount("-");
//                            glAccount.setCentralGLAccount("-");
//                        }
//                        logger.info("glAccount : {}", glAccount);
//
//                    } else {
//                        String fundSource1 = fundSource.length() == 2 ? StringUtils.leftPad(fundSource, 4, "_") : StringUtils.leftPad(fundSource, 5, "_");
//                        String fundSource2 = StringUtils.rightPad(fundSource1, 7, "_");
//                        logger.info("fundSource2 : {}", fundSource2);
//                        glAccount = Context.sessionGlAccountForPayment.get(payMethodConfig.getDocumentTypeForPayment() + "-" + fundSource2);
////                        glAccount = glAccountService.findOneByDocTypeAndFundSource(payMethodConfig.getDocumentTypeForPayment(), fundSource);
//                        if (null == glAccount) {
//                            glAccount = new GLAccount();
//                            glAccount.setAgencyGLAccount("-");
//                            glAccount.setCentralGLAccount("-");
//                        }
//                        logger.info("glAccount : {}", glAccount);
//                    }
//
//                    if (!paymentRunDocument.getCompCode().equalsIgnoreCase("99999")) {
//
//                        if (null != paymentRunDocument.getWtxCode()) {
//                            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", paymentRunDocument.getGlAccount(), paymentRunDocument.getAmount(), BigDecimal.ZERO),
//                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//
//                            Wtx wtx = Context.sessionWtx.get(paymentRunDocument.getWtxCode());
//
//                            if (null != wtx) {
//                                this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                        MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", wtx.getGlAccount(), paymentRunDocument.getWtxAmount(), BigDecimal.ZERO),
//                                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//
//                            } else {
//                                this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                        MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", "-", paymentRunDocument.getWtxAmount(), BigDecimal.ZERO),
//                                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//
//                            }
//
//
//                            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 003, "50", glAccount.getAgencyGLAccount(), paymentRunDocument.getAmountPaid(), BigDecimal.ZERO),
//                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//
//                        } else {
//                            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", paymentRunDocument.getGlAccount(), paymentRunDocument.getAmount(), BigDecimal.ZERO),
//                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//
//                            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", glAccount.getAgencyGLAccount(), paymentRunDocument.getAmountPaid(), BigDecimal.ZERO),
//                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//                        }
//                    } else {
//
//                        if (null != paymentRunDocument.getWtxCode()) {
//                            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", paymentRunDocument.getGlAccount(), paymentRunDocument.getAmount(), BigDecimal.ZERO),
//                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//
//                            Wtx wtx = Context.sessionWtx.get(paymentRunDocument.getWtxCode());
//
//                            if (null != wtx) {
//                                this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                        MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", wtx.getGlAccount(), paymentRunDocument.getWtxAmount(), BigDecimal.ZERO),
//                                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//                            } else {
//                                this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                        MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", "-", paymentRunDocument.getWtxAmount(), BigDecimal.ZERO),
//                                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//                            }
//
//                            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 003, "50", glAccount.getCentralGLAccount(), paymentRunDocument.getAmountPaid(), BigDecimal.ZERO),
//                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//
//                        } else {
//                            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", paymentRunDocument.getGlAccount(), paymentRunDocument.getAmount(), BigDecimal.ZERO),
//                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//
//                            this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", glAccount.getCentralGLAccount(), paymentRunDocument.getAmountPaid(), BigDecimal.ZERO),
//                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), id);
//                        }
//                    }
//
//
//                });
//
//                this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                        MessageFormat.format(Message.MESSAGE_398.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
//                        Message.MESSAGE_398.getMessageNo(), id);
//                this.paymentProposalLogService.addSuccessLog(seq.getAndIncrement(),
//                        MessageFormat.format(Message.MESSAGE_517.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
//                        Message.MESSAGE_517.getMessageNo(), id);
//            }
//
//
//        }
//
//        this.paymentProposalLogService.saveAll();

        int seqPayment = 0;
        Long seqDocK3 = getNextSeries(true);
        for (PrepareProposalDocument paymentDocument : paymentDocuments) {
            seqPayment++;
            log.info(" seqPayment {}", seqPayment);
//            GLHead glHeads = this.glHeadService.findAccDocNo(paymentDocument.getAccDocNo(), paymentDocument.getCompCode(), paymentDocument.getFiscalYear());

            // TODO
//            String sql = "UPDATE GL_HEAD SET PAYMENT_ID = ?, PAYMENT_DOC_NO = ? WHERE ACC_DOC_NO = ? AND COMP_CODE = ? AND FISCAL_YEAR = ?";
//            DBConnection.getJdbcTemplate("payment").update(sql, paymentAlias.getId(), paymentDocument.getPaymentClearingDocNo(), paymentDocument.getAccDocNo(), paymentDocument.getCompCode(), paymentDocument.getFiscalYear());
//            glHeads.setPaymentId(paymentAlias.getId());
//            glHeads.setPaymentDocNo(paymentDocument.getPaymentClearingDocNo());
//            this.glHeadService.save(glHeads);


//            if (paymentDocument.getDocType().equalsIgnoreCase("KA") || paymentDocument.getDocType().equalsIgnoreCase("KB") ||
//                    paymentDocument.getDocType().equalsIgnoreCase("KG") || paymentDocument.getDocType().equalsIgnoreCase("KC")) {
//
//                logger.info("K3 KX getAccDocNo : {}", paymentDocument.getAccDocNo());
//                logger.info("K3 KX getFiscalYear : {}", paymentDocument.getFiscalYear());
//
//                List<PaymentRunDocument> specialDoc = this.findDocTypeK3ORKX(paymentDocument.getAccDocNo(), paymentDocument.getFiscalYear());
//                logger.info("before add specialDoc : {}", specialDoc.size());
//                BigDecimal amount = BigDecimal.ZERO;
//                if (specialDoc.size() > 0) {
//                    for (int i = 0; i < specialDoc.size(); i++) {
//                        amount = amount.add(specialDoc.get(i).getAmount());
//
//                        String paymentDocNo = SqlUtil.generateProposalDocument(seqDocK3++);
//                        specialDoc.get(i).setPaymentClearingDocNo(paymentDocNo);
//                        specialDoc.get(i).setPaymentClearingDate(Util.stringToTimestamp(parameter.getPostDate()));
//                        specialDoc.get(i).setPaymentClearingEntryDate(new Timestamp(new Date().getTime()));
//                        specialDoc.get(i).setPaymentClearingYear(specialDoc.get(i).getFiscalYear());
//                        specialDoc.get(i).setPaymentDate(paymentAlias.getPaymentDate());
//                        specialDoc.get(i).setPaymentDateAcct(new Timestamp(paymentAlias.getPaymentDate().getTime()));
//                        specialDoc.get(i).setPaymentName(paymentAlias.getPaymentName());
//                        specialDoc.get(i).setPaymentId(paymentAlias.getId());
//
//                        HouseBankPaymentMethod houseBankPaymentMethod = Context.sessionHouseBankPaymentMethod.get(paymentDocument.getPaymentMethod());
////                        HouseBankPaymentMethod houseBankPaymentMethod = this.houseBankPaymentMethodService.findOneByHouseBankAndPaymentMethod("99999", "01000", specialDoc.get(i).getPaymentMethod());
//
//                        specialDoc.get(i).setPayingBankCode(houseBankPaymentMethod.getAccountCode());
//                        specialDoc.get(i).setPayingHouseBank(houseBankPaymentMethod.getHouseBank());
//                        specialDoc.get(i).setPayingBankAccountNo(houseBankPaymentMethod.getBankAccountNo());
//                        specialDoc.get(i).setPayingBankCountry(houseBankPaymentMethod.getCountryCode());
//                        specialDoc.get(i).setPayingBankNo(houseBankPaymentMethod.getBankAccountNo());
//                        specialDoc.get(i).setPayingGLAccount(houseBankPaymentMethod.getGlAccount());
//                        specialDoc.get(i).setPayingBankKey(houseBankPaymentMethod.getBankBranch());
//                        specialDoc.get(i).setPayingBankName(houseBankPaymentMethod.getHouseBankName());
//                        specialDoc.get(i).setPayingCompCode("99999");
//
//                        PaymentProcess paymentProcess = new PaymentProcess(specialDoc.get(i), true, true);
//                        paymentProcess.setParentCompCode(paymentDocument.getCompCode());
//                        paymentProcess.setParentDocNo(paymentDocument.getAccDocNo());
//                        paymentProcess.setParentFiscalYear(paymentDocument.getFiscalYear());
//                        PaymentInformation paymentInformation = new PaymentInformation(specialDoc.get(i), true, true);
//                        paymentInformation.setParentCompCode(paymentDocument.getCompCode());
//                        paymentInformation.setParentDocNo(paymentDocument.getAccDocNo());
//                        paymentInformation.setParentFiscalYear(paymentDocument.getFiscalYear());
//                        if (null != specialDoc.get(i).getOriginalDocNo()) {
//
//                            String invoiceAccDocNo = specialDoc.get(i).getOriginalDocNo().substring(0, 10);
//                            String invoiceCompCode = specialDoc.get(i).getOriginalDocNo().substring(10, 15);
//                            String invoiceFiscalYear = specialDoc.get(i).getOriginalDocNo().substring(15, 19);
//
//                            GLHead glHead = glHeadService.findAccDocNo(invoiceAccDocNo, invoiceCompCode, invoiceFiscalYear);
//                            GLLine glLinePay = glLineService.findOneByCompCodeAndAccDocNoAndFiscalYearAndAccountType(paymentProcess.getCompCode(), paymentProcess.getAccDocNo(), paymentProcess.getFiscalYear());
//                            GLLine glLineOriginal = glLineService.findOneByCompCodeAndAccDocNoAndFiscalYearAndAccountType(glHead.getCompCode(), glHead.getAccDocNo(), glHead.getFiscalYear());
//
//                            paymentProcess.setInvoiceAccDocNo(invoiceAccDocNo);
//                            paymentProcess.setInvoiceCompCode(invoiceCompCode);
//                            paymentProcess.setInvoiceFiscalYear(invoiceFiscalYear);
//                            paymentProcess.setInvoiceDocType(glHead.getDocType());
//
//                            paymentProcess.setInvoicePaymentCenter(glLineOriginal.getPaymentCenter());
//                            paymentProcess.setAccDocNoPaymentCenter(glLinePay.getPaymentCenter());
//
//                            paymentProcess.setInvoiceWtxAmount(specialDoc.get(i).getWtxAmount());
//                            paymentProcess.setInvoiceWtxBase(specialDoc.get(i).getWtxBase());
//                            paymentProcess.setInvoiceWtxAmountP(specialDoc.get(i).getWtxAmountP());
//                            paymentProcess.setInvoiceWtxBaseP(specialDoc.get(i).getWtxBaseP());
//
//                        }
//                        this.paymentProcessService.save(paymentProcess);
//                        this.paymentInformationService.save(paymentInformation);
//                    }
//                    paymentDocument.setAmount(paymentDocument.getAmount().subtract(amount));
//
//                }
//
//            }


//            PaymentProcess paymentProcess = new PaymentProcess(paymentDocument, true, false);
//
//
//            paymentProcess.setPmGroupNo(paymentDocument.getPaymentName() + "-" + paymentDocument.getPaymentDate().getTime());
//            paymentProcess.setPmGroupDoc(paymentDocument.getAccDocNo() + "-" + paymentDocument.getFiscalYear() + "-" + paymentDocument.getPaymentName() + "-" + paymentDocument.getPaymentDate().getTime());
//            paymentProcess.setProposalBlock(false);
//
//            if (null != paymentDocument.getOriginalDocNo()) {
//
////                logger.info("getOriginalDocNo : {}", paymentDocument.getOriginalDocNo());
//
//                String invoiceAccDocNo = paymentDocument.getOriginalDocNo().substring(0, 10);
//                String invoiceCompCode = paymentDocument.getOriginalDocNo().substring(10, 15);
//                String invoiceFiscalYear = paymentDocument.getOriginalDocNo().substring(15, 19);
//
////                GLHead glHead = glHeadService.findAccDocNo(invoiceAccDocNo, invoiceCompCode, invoiceFiscalYear);
////                GLLine glLinePay = glLineService.findOneByCompCodeAndAccDocNoAndFiscalYearAndAccountType(paymentProcess.getCompCode(), paymentProcess.getAccDocNo(), paymentProcess.getFiscalYear());
////                GLLine glLineOriginal = glLineService.findOneByCompCodeAndAccDocNoAndFiscalYearAndAccountType(glHead.getCompCode(), glHead.getAccDocNo(), glHead.getFiscalYear());
//
//                paymentProcess.setInvoiceAccDocNo(invoiceAccDocNo);
//                paymentProcess.setInvoiceCompCode(invoiceCompCode);
//                paymentProcess.setInvoiceFiscalYear(invoiceFiscalYear);
////                paymentProcess.setInvoiceDocType(glHead.getDocType());
////
////                paymentProcess.setInvoicePaymentCenter(glLineOriginal.getPaymentCenter());
////                paymentProcess.setAccDocNoPaymentCenter(glLinePay.getPaymentCenter());
//
//
//                paymentProcess.setInvoiceWtxAmount(paymentDocument.getWtxAmount());
//                paymentProcess.setInvoiceWtxBase(paymentDocument.getWtxBase());
//                paymentProcess.setInvoiceWtxAmountP(paymentDocument.getWtxAmountP());
//                paymentProcess.setInvoiceWtxBaseP(paymentDocument.getWtxBaseP());
//
//            }
//
//
//            this.paymentProcessService.save(paymentProcess);
//
//            PaymentInformation paymentInformation = new PaymentInformation(paymentDocument, true, false);
//
//            paymentInformation.setPmGroupNo(paymentDocument.getPaymentName() + "-" + paymentDocument.getPaymentDate().getTime());
//            paymentInformation.setPmGroupDoc(paymentDocument.getAccDocNo() + "-" + paymentDocument.getFiscalYear() + "-" + paymentDocument.getPaymentName() + "-" + paymentDocument.getPaymentDate().getTime());
//
//            this.paymentInformationService.save(paymentInformation);

//            log.info(" end save {}", "");

        }
        logger.info("seq : {}", seqDoc);
//        updateNextSeries(seqDocK3, true);
        paymentAlias.setProposalStatus("S");
        paymentAlias.setProposalSuccessDocument(paymentDocumentList.size());
        paymentAlias.setProposalTotalDocument(paymentDocumentList.size());
//        paymentAliasService.updateStatus(paymentAlias);
    }


    public ResponseEntity<Result<PaymentAlias>> delete(Long id) {
        Result<PaymentAlias> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != id) {
                PaymentAlias paymentAlias = paymentAliasService.findOneById(id);
//				paymentAliasService.delete(id);
                if (null != paymentAlias) {
                    paymentAlias.setProposalStatus("");
                    paymentAliasService.save(paymentAlias);

//					PaymentLine paymentLine = paymentLineService.findOne(id);
//					if (null != paymentLine) {
//						paymentLineService.delete(paymentLine);
//					}

                    PaymentProcess paymentProcess = paymentProcessService.findOneById(id);
                    if (null != paymentProcess) {
                        paymentProcessService.delete(paymentProcess);
                    }

//                    PaymentInformation paymentInformation = paymentInformationService.findOne(id);
//                    if (null != paymentInformation) {
//                        paymentInformationService.delete(paymentInformation);
//                    }

                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(paymentAlias);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setStatus(HttpStatus.NO_CONTENT.value());
                    result.setData(null);
                    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Long getNextSeries(boolean isProposal) {
        String sql = "";
        if (isProposal) {
            sql = "SELECT PROPOSAL_DOCUMENT_SEQ.NEXTVAL FROM DUAL";
        } else {
            sql = "SELECT PAYMENT_DOCUMENT_SEQ.NEXTVAL FROM DUAL";
        }
        Query q = entityManager.createNativeQuery(sql);
        return ((BigDecimal) q.getSingleResult()).longValue();
    }

    private void updateNextSeries(Long lastNumber, boolean isProposal) {
        String sql;
        if (isProposal) {
            sql = "ALTER SEQUENCE PROPOSAL_DOCUMENT_SEQ RESTART START WITH " + lastNumber;
        } else {
            sql = "ALTER SEQUENCE PAYMENT_DOCUMENT_SEQ RESTART START WITH " + lastNumber;
        }
        entityManager.createNativeQuery(sql).executeUpdate();
    }

    private String getGroupingByKey(PaymentDocument paymentDocument) {
        return paymentDocument.getCompCode() + "-" + paymentDocument.getVendor();
    }


    public ResponseEntity<Result<List<ProposalDocumentResponse>>> findProposalDocument(String id) {
        Result<List<ProposalDocumentResponse>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            Map<String, Object> params = new HashMap<>();

            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT ");
            sb.append(" im.ID as PAYMENT_INFORMATION_ID,");
            sb.append(" im.AMOUNT,");
            sb.append(" im.AMOUNT_PAID,");
            sb.append(" im.COMP_CODE,");
            sb.append(" im.CURRENCY,");
            sb.append(" im.FISCAL_YEAR,");
            sb.append(" im.PAYING_BANK_CODE,");
            sb.append(" im.PAYING_HOUSE_BANK,");
            sb.append(" im.PAYMENT_METHOD,");
            sb.append(" im.PAYMENT_ID,");
            sb.append(" im.VENDOR,");
            sb.append(" pc.ACC_DOC_NO,");
            sb.append(" pc.PM_GROUP_NO,");
            sb.append(" pc.PM_GROUP_DOC,");
            sb.append(" pc.PROPOSAL_BLOCK,");
            sb.append(" pc.ID as PAYMENT_PROCESS_ID");
            sb.append(" from PAYMENT_INFORMATION im,");
            sb.append(" PAYMENT_PROCESS pc");
            sb.append(" where im.ACC_DOC_NO = pc.ACC_DOC_NO");
            sb.append(" and im.FISCAL_YEAR = pc.FISCAL_YEAR");
            sb.append(" and im.COMP_CODE = pc.COMP_CODE");
            sb.append(" and im.PAYMENT_ID = pc.PAYMENT_ID");
            sb.append(" and im.PM_GROUP_NO = pc.PM_GROUP_NO");
            sb.append(" and im.PM_GROUP_DOC = pc.PM_GROUP_DOC");
            sb.append(" and im.PROPOSAL = pc.PROPOSAL");
            sb.append(" and pc.PROPOSAL = '1'");
            sb.append(" and pc.STATUS = 'S'");


            if (!Util.isEmpty(id)) {
                sb.append(SqlUtil.whereClause(id, "im.PAYMENT_ID", params));
            }
            Query q = entityManager.createNativeQuery(sb.toString(), ProposalDocumentResponse.class);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
            List<ProposalDocumentResponse> proposalDocumentResponse = q.getResultList();

            logger.info("size {}", proposalDocumentResponse.size());

            result.setData(proposalDocumentResponse);
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    public void blockDocument(ProposalDocumentRequest request) {
//
//        try {
//
//            List<ProposalDocument> proposalDocumentList = request.getProposalDocumentList();
//
//            for (ProposalDocument proposalDocument : proposalDocumentList) {
//
//
//                GLHead glHeadDocumentPay = glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(proposalDocument.getCompCode(), proposalDocument.getAccDocNo(), proposalDocument.getFiscalYear());
//
//                List<GLHead> listHead = glHeadService.findOriginalDocNo(glHeadDocumentPay.getOriginalDocument().substring(0, 10));
//
//                GLHead originalDocument = null;
//                for (GLHead item : listHead) {
//
//                    if (item.getOriginalDocument().startsWith(item.getOriginalDocumentNo())) {
//                        originalDocument = item;
//                    }
//
//                    GLLine glLine = glLineService.findOneByCompCodeAndAccDocNoAndFiscalYearAndAccountType(item.getCompanyCode(), item.getOriginalDocumentNo(), item.getOriginalFiscalYear());
//                    if (null != glLine) {
//                        glLine.setPaymentBlock("B");
//                        glLineService.save(glLine);
//                    }
//
//
//                }
//                glHeadDocumentPay.setPaymentId(null);
//                glHeadDocumentPay.setPaymentDocumentNo(null);
//                glHeadService.save(glHeadDocumentPay);
//
//                APUpPbkToBRequest aPUpPbkToBRequest = new APUpPbkToBRequest(originalDocument, request.getWebInfo());
//
//                FIMessage fiMessage = new FIMessage();
//                fiMessage.setId(aPUpPbkToBRequest.getCompCode().substring(0, 2).concat(".PBKB.").concat(aPUpPbkToBRequest.getAccountDocNo()));
//                fiMessage.setType(FIMessage.Type.REQUEST.getCode());
//                fiMessage.setDataType("PBKB");
//                fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
//                fiMessage.setTo(aPUpPbkToBRequest.getCompCode().substring(0, 2));
//
//                fiMessage.setData(XMLUtil.xmlMarshall(APUpPbkToBRequest.class, aPUpPbkToBRequest));
//
//                logger.info("aPUpPbkToBRequest : {}", XMLUtil.xmlMarshall(APUpPbkToBRequest.class, aPUpPbkToBRequest));
//
//                this.send(fiMessage, aPUpPbkToBRequest.getCompCode());
//
////                paymentInformationService.deleteById(proposalDocumentRequest.getPaymentInformationId());
////                paymentProcessService.deleteById(proposalDocumentRequest.getPaymentProcessId());
//                paymentProcessService.updateProposalBlock(proposalDocument.getPaymentProcessId());
//
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public List<PaymentRealRun> selectDocument(String paymentAliasId, boolean isProposal) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" im.ID,");
        sb.append(" im.ACCOUNT_HOLDER_NAME,");
        sb.append(" im.ADDRESS,");
        sb.append(" im.AMOUNT,");
        sb.append(" im.AMOUNT_PAID,");
        sb.append(" im.CITY,");
        sb.append(" im.COMP_CODE,");
        sb.append(" im.COUNTRY,");
        sb.append(" im.COUNTRY_NAME,");
        sb.append(" im.CURRENCY,");
        sb.append(" im.DATE_DUE,");
        sb.append(" im.DATE_VALUE,");
        sb.append(" im.FI_AREA,");
        sb.append(" im.AREA_NAME,");
        sb.append(" im.NAME1,");
        sb.append(" im.NAME2,");
        sb.append(" im.NAME3,");
        sb.append(" im.NAME4,");
        sb.append(" im.PAYEE_ADDRESS,");
        sb.append(" im.PAYEE_BANK_ACCOUNT_NO,");
        sb.append(" im.PAYEE_BANK_KEY,");
        sb.append(" im.PAYEE_BANK_NO,");
        sb.append(" im.PAYEE_BANK_REFERENCE,");
        sb.append(" im.PAYEE_CITY,");
        sb.append(" im.PAYEE_CODE,");
        sb.append(" im.PAYEE_COUNTRY,");
        sb.append(" im.PAYEE_NAME1,");
        sb.append(" im.PAYEE_NAME2,");
        sb.append(" im.PAYEE_NAME3,");
        sb.append(" im.PAYEE_NAME4,");
        sb.append(" im.PAYEE_PORTAL_CODE,");
        sb.append(" im.PAYEE_TAX_ID,");
        sb.append(" im.PAYEE_TITLE,");
        sb.append(" im.PAYING_BANK_ACCOUNT_NO,");
        sb.append(" im.PAYING_BANK_CODE,");
        sb.append(" im.PAYING_BANK_COUNTRY,");
        sb.append(" im.PAYING_BANK_KEY,");
        sb.append(" im.PAYING_BANK_NO,");
        sb.append(" im.PAYING_COMP_CODE,");
        sb.append(" im.PAYING_GL_ACCOUNT,");
        sb.append(" im.PAYING_HOUSE_BANK,");
        sb.append(" im.PAYMENT_BLOCK,");
        sb.append(" im.PAYMENT_DATE,");
        sb.append(" im.PAYMENT_DATE_ACCT,");
        sb.append(" im.PAYMENT_DOC_NO,");
        sb.append(" im.PAYMENT_ID,");
        sb.append(" im.PAYMENT_METHOD,");
        sb.append(" im.PAYMENT_NAME,");
        sb.append(" im.PAYMENT_REF,");
        sb.append(" im.PAYMENT_SPECIAL_GL,");
        sb.append(" im.PORTAL_CODE,");
        sb.append(" im.PROPOSAL,");
        sb.append(" im.SWIFT_CODE,");
        sb.append(" im.TAX_ID,");
        sb.append(" im.TITLE,");
        sb.append(" im.VENDOR_CODE,");
        sb.append(" im.PAYING_BANK_NAME,");
        sb.append(" pc.ACC_DOC_NO,");
        sb.append(" pc.ACCOUNT_TYPE,");
        sb.append(" pc.ASSET_NO,");
        sb.append(" pc.ASSET_SUB_NO,");
        sb.append(" pc.ASSIGNMENT,");
        sb.append(" pc.BANK_ACCOUNT_NO,");
        sb.append(" pc.BR_DOC_NO,");
        sb.append(" pc.BR_LINE,");
        sb.append(" pc.BUDGET_ACCOUNT,");
        sb.append(" pc.BUDGET_ACTIVITY,");
        sb.append(" pc.BUDGET_ACTIVITY_NAME,");
        sb.append(" pc.CN_DOC_NO,");
        sb.append(" pc.CN_FISCAL_YEAR,");
        sb.append(" pc.CN_LINE,");
        sb.append(" pc.COMP_CODE_NAME,");
        sb.append(" pc.COST_CENTER,");
        sb.append(" pc.COST_CENTER_NAME,");
        sb.append(" pc.DATE_ACCT,");
        sb.append(" pc.DATE_BASELINE,");
        sb.append(" pc.DATE_DOC,");
        sb.append(" pc.DOC_TYPE,");
        sb.append(" pc.DR_CR,");
        sb.append(" pc.ERROR_CODE,");
        sb.append(" pc.FISCAL_YEAR,");
        sb.append(" pc.FUND_CENTER,");
        sb.append(" pc.FUND_CENTER_NAME,");
        sb.append(" pc.FUND_SOURCE,");
        sb.append(" pc.FUND_SOURCE_NAME,");
        sb.append(" pc.GL_ACCOUNT,");
        sb.append(" pc.GL_ACCOUNT_NAME,");
        sb.append(" pc.HD_REFERENCE,");
        sb.append(" pc.HOUSE_BANK,");
        sb.append(" pc.LINE,");
        sb.append(" pc.LINE_ITEM_TEXT,");
        sb.append(" PC.PAYMENT_METHOD_NAME,");
        sb.append(" PC.PAYMENT_TERM,");
        sb.append(" PC.PO_DOC_NO,");
        sb.append(" PC.PO_LINE,");
        sb.append(" PC.POSTING_KEY,");
        sb.append(" pc.RECON_ACCOUNT,");
        sb.append(" pc.RECON_ACCOUNT_NAME,");
        sb.append(" pc.REFERENCE1,");
        sb.append(" pc.REFERENCE2,");
        sb.append(" pc.REFERENCE3,");
        sb.append(" pc.SPECIAL_GL,");
        sb.append(" pc.SPECIAL_GL_NAME,");
        sb.append(" pc.STATUS,");
        sb.append(" pc.TRADING_PARTNER,");
        sb.append(" pc.TRADING_PARTNER_NAME,");
        sb.append(" pc.VENDOR_FLAG,");
        sb.append(" pc.VENDOR_NAME,");
        sb.append(" pc.WTX_AMOUNT,");
        sb.append(" pc.WTX_BASE,");
        sb.append(" pc.WTX_CODE,");
        sb.append(" pc.PM_GROUP_NO,");
        sb.append(" pc.PM_GROUP_DOC,");
        sb.append(" pc.PROPOSAL_BLOCK,");
        sb.append(" pc.INVOICE_ACC_DOC_NO,");
        sb.append(" pc.INVOICE_FISCAL_YEAR,");
        sb.append(" pc.INVOICE_COMP_CODE,");
        sb.append(" pc.INVOICE_DOC_TYPE,");
        sb.append(" pc.INVOICE_WTX_AMOUNT,");
        sb.append(" pc.INVOICE_WTX_BASE,");
        sb.append(" pc.INVOICE_WTX_AMOUNT_P,");
        sb.append(" pc.INVOICE_WTX_BASE_P,");
        sb.append(" pc.PAYMENT_COMP_CODE,");
        sb.append(" pc.PAYMENT_FISCAL_YEAR,");
        sb.append(" pc.PAYMENT_CENTER,");
        sb.append(" pc.INVOICE_PAYMENT_CENTER,");
        sb.append(" pc.ACC_DOC_NO_PAYMENT_CENTER,");
        sb.append(" pc.PARENT_COMP_CODE,");
        sb.append(" pc.PARENT_DOC_NO,");
        sb.append(" pc.PARENT_FISCAL_YEAR");

        sb.append(" from PAYMENT_INFORMATION im,");
        sb.append(" PAYMENT_PROCESS pc");
        sb.append(" where im.ACC_DOC_NO = pc.ACC_DOC_NO");
        sb.append(" and im.FISCAL_YEAR = pc.FISCAL_YEAR");
        sb.append(" and im.COMP_CODE = pc.COMP_CODE");
        sb.append(" and im.PAYMENT_ID = pc.PAYMENT_ID");
        sb.append(" and im.PM_GROUP_NO = pc.PM_GROUP_NO");
        sb.append(" and im.PM_GROUP_DOC = pc.PM_GROUP_DOC");
        sb.append(" and im.PROPOSAL = pc.PROPOSAL");
        sb.append(" and pc.STATUS = 'S'");
        sb.append(" and pc.IS_CHILD = '0'");
        if (isProposal) {
            sb.append(" and pc.PROPOSAL = '1'");
            sb.append(" and pc.PROPOSAL_BLOCK = '0'");
        } else {
            sb.append(" and pc.PROPOSAL = '0'");
        }


        if (!Util.isEmpty(paymentAliasId)) {
            sb.append(SqlUtil.whereClause(paymentAliasId, "im.PAYMENT_ID", params));
        }

        sb.append(" ORDER BY im.VENDOR_CODE ");

        logger.info(" sql real run : {}", sb.toString());
        Query q = entityManager.createNativeQuery(sb.toString(), PaymentRealRun.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<PaymentRealRun> paymentRealRun = q.getResultList();
        return paymentRealRun;
    }


    public void reversePaymentDocument(ReverseDocumentRequest request) {
        logger.info("FIPostRequest {}", request);
        try {
            FIPostRequest fiPostRequest = new FIPostRequest();
            BeanUtils.copyProperties(request, fiPostRequest);

            FIMessage fiMessage = new FIMessage();
            fiMessage.setId(request.getCompCode() + "." + request.getFiscalYear() + "." + request.getAccountDocNo());
            fiMessage.setType(FIMessage.Type.REQUEST.getCode());
            fiMessage.setDataType(FIMessage.DataType.REVERSE.getCode());
            fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
            fiMessage.setTo("99999");
            logger.info("FIPostRequest {}", request);
            fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));

            logger.info("fiMessage {}", fiMessage);
            this.send(fiMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ResponseEntity<Result<ReverseDocumentRequest>> reverseInvoiceDocument(ReverseDocumentRequest request) {
        Result<ReverseDocumentRequest> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            GLHead glHead = glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(request.getCompCode(), request.getAccountDocNo(), request.getFiscalYear());

            if (null == glHead.getPaymentId() && null == glHead.getPaymentDocumentNo()) {


                FIPostRequest fiPostRequest = new FIPostRequest();
                BeanUtils.copyProperties(request, fiPostRequest);

                FIMessage fiMessage = new FIMessage();
                fiMessage.setId(request.getCompCode() + "." + request.getFiscalYear() + "." + request.getAccountDocNo());
                fiMessage.setType(FIMessage.Type.REQUEST.getCode());
                fiMessage.setDataType(FIMessage.DataType.REVERSE_INVOICE.getCode());
                fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
                fiMessage.setTo(request.getCompCode().substring(0, 2));
                logger.info("FIPostRequest {}", request);
                fiMessage.setData(XMLUtil.xmlMarshall(FIPostRequest.class, fiPostRequest));

                logger.info("fiMessage {}", fiMessage);
                this.sendReverse(fiMessage, request.getCompCode());
                result.setData(request);
                result.setStatus(HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setData(request);
                result.setStatus(HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void send(FIMessage message) throws Exception {
        logger.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
        final AtomicReference<Message> msg = new AtomicReference<>();
        jmsTemplate.convertAndSend("99999.AP.Payment", XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
            msg.set(message1);
            return message1;
        });
        log.info("msg id : {}", msg.get().getJMSMessageID());
    }

    public void send(FIMessage message, String compCode) throws Exception {
        logger.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
        final AtomicReference<Message> msg = new AtomicReference<>();
        jmsTemplate.convertAndSend(compCode.substring(0, 2) + ".AP.UpPbk",
                XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
                    msg.set(message1);
                    return message1;
                });
        log.info("msg id : {}", msg.get().getJMSMessageID());
    }

    public void sendReverse(FIMessage message, String compCode) throws Exception {
        logger.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
        logger.info("client : {}", compCode.substring(0, 2) + ".AP.Payment");
        final AtomicReference<Message> msg = new AtomicReference<>();
        jmsTemplate.convertAndSend(compCode.substring(0, 2) + ".AP.Payment",
                XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
                    msg.set(message1);
                    return message1;
                });
        log.info("msg id : {}", msg.get().getJMSMessageID());
    }


    public List<PaymentRunDocument> findDocTypeK3ORKX(String originalDocNo, String originalFiscalYear) {

        Map<String, Object> params = new HashMap<>();
        params.put("MAP_INV_DOC_NO", originalDocNo);
        params.put("MAP_INV_FISCAL_YEAR", originalFiscalYear);


        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("    gh.id                AS id, ");
        sb.append("    gh.doc_type                AS doc_type, ");
        sb.append("    gh.comp_code               AS comp_code, ");
        sb.append("    gh.date_doc                AS date_doc, ");
        sb.append("    gh.date_acct               AS date_acct, ");
        sb.append("    gh.period                  AS period, ");
        sb.append("    gh.currency                AS currency, ");
        sb.append("    gh.inv_doc_no              AS inv_doc_no, ");
        sb.append("    gh.rev_inv_doc_no          AS rev_inv_doc_no, ");
        sb.append("    gh.acc_doc_no              AS acc_doc_no, ");
        sb.append("    gh.rev_acc_doc_no          AS rev_acc_doc_no, ");
        sb.append("    gh.fiscal_year             AS fiscal_year, ");
        sb.append("    gh.rev_fiscal_year         AS rev_fiscal_year, ");
        sb.append("    gh.cost_center1            AS cost_center1, ");
        sb.append("    gh.cost_center2            AS cost_center2, ");
        sb.append("    gh.header_reference            AS hd_reference, ");
        sb.append("    gh.doc_header_text         AS doc_header_text, ");
        sb.append("    gh.header_reference2           AS hd_reference2, ");
        sb.append("    gh.rev_date_acct           AS rev_date_acct, ");
        sb.append("    gh.rev_reason              AS rev_reason, ");
        sb.append("    gh.original_doc        AS original_doc_no, ");
        sb.append("    gh.created                 AS created, ");
        sb.append("    gh.userpark               AS user_park, ");
        sb.append("    gh.userpost               AS user_post, ");
        sb.append("    gl.posting_key             AS posting_key, ");
        sb.append("    gl.account_type            AS account_type, ");
        sb.append("    gl.dr_cr                   AS dr_cr, ");
        sb.append("    gl.gl_account              AS gl_account, ");
        sb.append("    gl.fi_area                 AS fi_area, ");
        sb.append("    gl.cost_center             AS cost_center, ");
        sb.append("    gl.fund_source             AS fund_source, ");
        sb.append("    gl.bg_code                 AS bg_code, ");
        sb.append("    gl.bg_activity             AS bg_activity, ");
        sb.append("    gl.cost_activity           AS cost_activity, ");
        sb.append("    gl.amount                  AS amount, ");
        sb.append("    gl.reference3              AS reference3, ");
        sb.append("    gl.assignment              AS assignment, ");
        sb.append("    gl.br_doc_no               AS br_doc_no, ");
        sb.append("    gl.br_line                 AS br_line, ");
        sb.append("    gl.payment_center          AS payment_center, ");
        sb.append("    gl.bank_book               AS bank_book, ");
        sb.append("    gl.sub_account             AS sub_account, ");
        sb.append("    gl.sub_account_owner       AS sub_account_owner, ");
        sb.append("    gl.deposit_account         AS deposit_account, ");
        sb.append("    gl.deposit_account_owner   AS deposit_account_owner, ");
        sb.append("    gl.gpsc                    AS gpsc, ");
        sb.append("    gl.gpsc_group              AS gpsc_group, ");
        sb.append("    gl.line_item_text          AS line_item_text, ");
        sb.append("    gl.line_desc               AS line_desc, ");
        sb.append("    gl.payment_term            AS payment_term, ");
        sb.append("    gl.payment_method          AS payment_method, ");
        sb.append("    gl.wtx_type                AS wtx_type, ");
        sb.append("    gl.wtx_code                AS wtx_code, ");
        sb.append("    gl.wtx_base                AS wtx_base, ");
        sb.append("    nvl(gl.wtx_amount,0)       AS wtx_amount, ");
        sb.append("    gl.wtx_type_p              AS wtx_type_p, ");
        sb.append("    gl.wtx_code_p              AS wtx_code_p, ");
        sb.append("    gl.wtx_base_p              AS wtx_base_p, ");
        sb.append("    gl.wtx_amount_p            AS wtx_amount_p, ");
        sb.append("    gl.vendor                  AS vendor, ");
        sb.append("    gl.vendor_tax_id           AS vendor_tax_id, ");
        sb.append("    gl.payee                   AS payee_code, ");
        sb.append("    gl.payee_tax_id            AS payee_tax_id, ");
        sb.append("    gl.bank_account_no         AS bank_account_no, ");
        sb.append("    gl.bank_branch_no          AS bank_branch_no, ");
        sb.append("    gl.trading_partner         AS trading_partner, ");
        sb.append("    gl.trading_partner_park    AS trading_partner_park, ");
        sb.append("    gl.special_gl              AS special_gl, ");
        sb.append("    gl.date_baseline           AS date_baseline, ");
        sb.append("    gl.date_value              AS date_value, ");
        sb.append("    gl.asset_no                AS asset_no, ");
        sb.append("    gl.asset_sub_no            AS asset_sub_no, ");
        sb.append("    gl.qty                     AS qty, ");
        sb.append("    gl.uom                     AS uom, ");
        sb.append("    gl.reference1              AS reference1, ");
        sb.append("    gl.reference2              AS reference2, ");
        sb.append("    gl.po_doc_no               AS po_doc_no, ");
        sb.append("    gl.po_line                 AS po_line, ");
        sb.append("    gl.income                  AS income, ");
        sb.append("    gl.payment_block           AS payment_block, ");
        sb.append("    gl.payment_ref             AS payment_ref, ");
        sb.append("    gl.line                    AS line_no, ");
        sb.append("    cd.name                    AS comp_code_name, ");
        sb.append("    pc.name                    AS payment_center_name, ");
        sb.append("    cc.name                    AS cost_center_name, ");
        sb.append("    pm.name                    AS payment_method_name, ");
        sb.append("    sf.name                    AS fund_source_name, ");
        sb.append("    bc.name                    AS bg_code_name, ");
        sb.append("    ma.name                    AS bg_activity_name, ");
        sb.append("    vd.name                    AS name1, ");
        sb.append("    vd.name2                   AS name2, ");
        sb.append("    vd.taxid                   AS tax_id, ");
        sb.append("    vl.address1                AS address1, ");
        sb.append("    vl.address2                AS address2, ");
        sb.append("    vl.address3                AS address3, ");
        sb.append("    vl.address4                AS address4, ");
        sb.append("    vl.address5                AS address5, ");
        sb.append("    vl.city                    AS city, ");
        sb.append("    vl.postal                  AS postal, ");
        sb.append("    vl.regionname              AS region_name, ");
        sb.append("    vc.name                    AS country, ");
        sb.append("    vc.countrycode             AS country_code, ");
        sb.append("    vb.bankaccounttype         AS payee_bank_account_type, ");
        sb.append("    vb.routingno               AS payee_bank_no, ");
        sb.append("    vb.accountno               AS payee_bank_account_no, ");
        sb.append("    vb.a_name                  AS account_holder_name, ");
        sb.append("    bk.name                    AS payee_bank_name, ");
        sb.append("    bk.routingno               AS payee_bank_key, ");
        sb.append("    bk.swiftcode               AS swift_code, ");
        sb.append("    bk.description             AS payee_bank_reference, ");
        sb.append("    ba.name                    AS area_name ");
        sb.append("FROM ");
        sb.append("    gl_head               gh ");
        sb.append("    INNER JOIN gl_line               gl ON gh.acc_doc_no = gl.acc_doc_no ");
        sb.append("    AND (gl.PAYMENT_BLOCK = '' OR gl.PAYMENT_BLOCK = ' ' OR gl.PAYMENT_BLOCK IS NULL) AND gl.ACCOUNT_TYPE = 'K' AND GH.COMP_CODE = GL.COMP_CODE  ");


        sb.append("    LEFT OUTER JOIN ").append(schema).append(".AD_ORG").append(" ad on ad.value = gh.COMP_CODE ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".TH_CACOMPCODE").append(" cd ON gh.comp_code = cd.valuecode ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGPAYMENTCENTER").append(" pc ON gl.payment_center = pc.valuecode ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGCOSTCENTER").append(" cc ON gl.cost_center = cc.valuecode ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGFUNDSOURCE").append(" sf ON gl.fund_source = sf.valuecode ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".TH_CAPAYMENTMETHOD").append(" pm ON gl.payment_method = pm.valuecode ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".C_YEAR").append(" cy ON cy.fiscalyear = gh.FISCAL_YEAR ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETCODE").append(" bc ON gl.bg_code = bc.valuecode AND ad.ad_org_id = bc.ad_org_id and bc.c_year_id = cy.c_year_id ");

        sb.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETACTIVITY").append(" ma ON gl.bg_activity = ma.valuecode AND (ad.ad_org_id = ma.ad_org_id OR ad.ad_org_id = '0') AND ma.c_year_id = cy.c_year_id ");

        sb.append("    LEFT OUTER JOIN ").append(schema).append(".c_bpartner").append(" vd ON vd.value = (CASE WHEN gl.PAYEE IS NULL THEN gl.VENDOR ELSE gl.PAYEE END) ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".c_bpartner_location").append(" vdl ON vd.c_bpartner_id = vdl.c_bpartner_id ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".c_location").append(" vl ON vdl.c_location_id = vl.c_location_id ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".c_country").append("             vc ON vl.c_country_id = vc.c_country_id ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".c_bp_bankaccount").append("      vb ON vd.c_bpartner_id = vb.c_bpartner_id ");
        sb.append("                                      AND gl.bank_account_no = vb.accountno AND vb.isactive = 'Y' ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".c_bank").append("                bk ON vb.c_bank_id = bk.c_bank_id ");
        sb.append("    LEFT OUTER JOIN ").append(schema).append(".th_bgbudgetarea").append(" ba on ba.fiarea = gl.FI_AREA ");
        sb.append("WHERE ");
        sb.append("    1 = 1 ");
        sb.append("   AND gh.PAYMENT_DOC_NO IS NULL ");
        sb.append("   AND gh.PAYMENT_ID IS NULL ");
        sb.append("   AND gh.REV_ACC_DOC_NO IS NULL ");

        sb.append("   AND gl.INV_DOC_NO =:MAP_INV_DOC_NO ");
        sb.append("   AND gl.INV_FISCAL_YEAR =:MAP_INV_FISCAL_YEAR ");
//        sb.append("   AND gh.HEADER_REFERENCE =:HEADER_REFERENCE ");

        logger.info("sql find K3 or KX : {}", sb.toString());

        Query q = entityManager.createNativeQuery(sb.toString(), PaymentDocument.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        logger.info("get result ", q.getResultList());
        List<PaymentDocument> paymentDocuments = q.getResultList();
        List<PaymentRunDocument> paymentRunDocuments = new ArrayList<PaymentRunDocument>();
        for (PaymentDocument paymentDocument : paymentDocuments) {
            PaymentRunDocument paymentRunDocument = new PaymentRunDocument();
            BeanUtils.copyProperties(paymentDocument, paymentRunDocument);
            paymentRunDocuments.add(paymentRunDocument);
        }

        return paymentRunDocuments;

    }

    public List<PaymentRealRun> selectSpecialDocument(String paymentAliasId, String parentCompCode, String parentDocNo, String parentFiscalYear, boolean isProposal) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" im.ID,");
        sb.append(" im.ACCOUNT_HOLDER_NAME,");
        sb.append(" im.ADDRESS,");
        sb.append(" im.AMOUNT,");
        sb.append(" im.AMOUNT_PAID,");
        sb.append(" im.CITY,");
        sb.append(" im.COMP_CODE,");
        sb.append(" im.COUNTRY,");
        sb.append(" im.COUNTRY_NAME,");
        sb.append(" im.CURRENCY,");
        sb.append(" im.DATE_DUE,");
        sb.append(" im.DATE_VALUE,");
        sb.append(" im.FI_AREA,");
        sb.append(" im.AREA_NAME,");
        sb.append(" im.NAME1,");
        sb.append(" im.NAME2,");
        sb.append(" im.NAME3,");
        sb.append(" im.NAME4,");
        sb.append(" im.PAYEE_ADDRESS,");
        sb.append(" im.PAYEE_BANK_ACCOUNT_NO,");
        sb.append(" im.PAYEE_BANK_KEY,");
        sb.append(" im.PAYEE_BANK_NO,");
        sb.append(" im.PAYEE_BANK_REFERENCE,");
        sb.append(" im.PAYEE_CITY,");
        sb.append(" im.PAYEE_CODE,");
        sb.append(" im.PAYEE_COUNTRY,");
        sb.append(" im.PAYEE_NAME1,");
        sb.append(" im.PAYEE_NAME2,");
        sb.append(" im.PAYEE_NAME3,");
        sb.append(" im.PAYEE_NAME4,");
        sb.append(" im.PAYEE_PORTAL_CODE,");
        sb.append(" im.PAYEE_TAX_ID,");
        sb.append(" im.PAYEE_TITLE,");
        sb.append(" im.PAYING_BANK_ACCOUNT_NO,");
        sb.append(" im.PAYING_BANK_CODE,");
        sb.append(" im.PAYING_BANK_COUNTRY,");
        sb.append(" im.PAYING_BANK_KEY,");
        sb.append(" im.PAYING_BANK_NO,");
        sb.append(" im.PAYING_COMP_CODE,");
        sb.append(" im.PAYING_GL_ACCOUNT,");
        sb.append(" im.PAYING_HOUSE_BANK,");
        sb.append(" im.PAYMENT_BLOCK,");
        sb.append(" im.PAYMENT_DATE,");
        sb.append(" im.PAYMENT_DATE_ACCT,");
        sb.append(" im.PAYMENT_DOC_NO,");
        sb.append(" im.PAYMENT_ID,");
        sb.append(" im.PAYMENT_METHOD,");
        sb.append(" im.PAYMENT_NAME,");
        sb.append(" im.PAYMENT_REF,");
        sb.append(" im.PAYMENT_SPECIAL_GL,");
        sb.append(" im.PORTAL_CODE,");
        sb.append(" im.PROPOSAL,");
        sb.append(" im.SWIFT_CODE,");
        sb.append(" im.TAX_ID,");
        sb.append(" im.TITLE,");
        sb.append(" im.VENDOR,");
        sb.append(" im.PAYING_BANK_NAME,");
        sb.append(" pc.ACC_DOC_NO,");
        sb.append(" pc.ACCOUNT_TYPE,");
        sb.append(" pc.ASSET_NO,");
        sb.append(" pc.ASSET_SUB_NO,");
        sb.append(" pc.ASSIGNMENT,");
        sb.append(" pc.BANK_ACCOUNT_NO,");
        sb.append(" pc.BR_DOC_NO,");
        sb.append(" pc.BR_LINE,");
        sb.append(" pc.BUDGET_ACCOUNT,");
        sb.append(" pc.BUDGET_ACTIVITY,");
        sb.append(" pc.BUDGET_ACTIVITY_NAME,");
        sb.append(" pc.CN_DOC_NO,");
        sb.append(" pc.CN_FISCAL_YEAR,");
        sb.append(" pc.CN_LINE,");
        sb.append(" pc.COMP_CODE_NAME,");
        sb.append(" pc.COST_CENTER,");
        sb.append(" pc.COST_CENTER_NAME,");
        sb.append(" pc.DATE_ACCT,");
        sb.append(" pc.DATE_BASELINE,");
        sb.append(" pc.DATE_DOC,");
        sb.append(" pc.DOC_TYPE,");
        sb.append(" pc.DR_CR,");
        sb.append(" pc.ERROR_CODE,");
        sb.append(" pc.FISCAL_YEAR,");
        sb.append(" pc.FUND_CENTER,");
        sb.append(" pc.FUND_CENTER_NAME,");
        sb.append(" pc.FUND_SOURCE,");
        sb.append(" pc.FUND_SOURCE_NAME,");
        sb.append(" pc.GL_ACCOUNT,");
        sb.append(" pc.GL_ACCOUNT_NAME,");
        sb.append(" pc.HD_REFERENCE,");
        sb.append(" pc.HOUSE_BANK,");
        sb.append(" pc.LINE,");
        sb.append(" pc.LINE_ITEM_TEXT,");
        sb.append(" PC.PAYMENT_METHOD_NAME,");
        sb.append(" PC.PAYMENT_TERM,");
        sb.append(" PC.PO_DOC_NO,");
        sb.append(" PC.PO_LINE,");
        sb.append(" PC.POSTING_KEY,");
        sb.append(" pc.RECON_ACCOUNT,");
        sb.append(" pc.RECON_ACCOUNT_NAME,");
        sb.append(" pc.REFERENCE1,");
        sb.append(" pc.REFERENCE2,");
        sb.append(" pc.REFERENCE3,");
        sb.append(" pc.SPECIAL_GL,");
        sb.append(" pc.SPECIAL_GL_NAME,");
        sb.append(" pc.STATUS,");
        sb.append(" pc.TRADING_PARTNER,");
        sb.append(" pc.TRADING_PARTNER_NAME,");
        sb.append(" pc.VENDOR_FLAG,");
        sb.append(" pc.VENDOR_NAME,");
        sb.append(" pc.WTX_AMOUNT,");
        sb.append(" pc.WTX_BASE,");
        sb.append(" pc.WTX_CODE,");
        sb.append(" pc.PM_GROUP_NO,");
        sb.append(" pc.PM_GROUP_DOC,");
        sb.append(" pc.PROPOSAL_BLOCK,");
        sb.append(" pc.INVOICE_ACC_DOC_NO,");
        sb.append(" pc.INVOICE_FISCAL_YEAR,");
        sb.append(" pc.INVOICE_COMP_CODE,");
        sb.append(" pc.INVOICE_DOC_TYPE,");
        sb.append(" pc.INVOICE_WTX_AMOUNT,");
        sb.append(" pc.INVOICE_WTX_BASE,");
        sb.append(" pc.INVOICE_WTX_AMOUNT_P,");
        sb.append(" pc.INVOICE_WTX_BASE_P,");
        sb.append(" pc.PAYMENT_COMP_CODE,");
        sb.append(" pc.PAYMENT_FISCAL_YEAR,");
        sb.append(" pc.PAYMENT_CENTER,");
        sb.append(" pc.INVOICE_PAYMENT_CENTER,");
        sb.append(" pc.ACC_DOC_NO_PAYMENT_CENTER,");
        sb.append(" pc.PARENT_COMP_CODE,");
        sb.append(" pc.PARENT_DOC_NO,");
        sb.append(" pc.PARENT_FISCAL_YEAR");

        sb.append(" from PAYMENT_INFORMATION im,");
        sb.append(" PAYMENT_PROCESS pc");
        sb.append(" where im.ACC_DOC_NO = pc.ACC_DOC_NO");
        sb.append(" and im.FISCAL_YEAR = pc.FISCAL_YEAR");
        sb.append(" and im.COMP_CODE = pc.COMP_CODE");
        sb.append(" and im.PAYMENT_ID = pc.PAYMENT_ID");
//        sb.append(" and im.PM_GROUP_NO = pc.PM_GROUP_NO");
//        sb.append(" and im.PM_GROUP_DOC = pc.PM_GROUP_DOC");
        sb.append(" and im.PROPOSAL = pc.PROPOSAL");
        sb.append(" and pc.STATUS = 'S'");
        sb.append(" and pc.IS_CHILD = '1'");
        if (isProposal) {
            sb.append(" and pc.PROPOSAL = '1'");
            sb.append(" and pc.PROPOSAL_BLOCK = '0'");
        } else {
            sb.append(" and pc.PROPOSAL = '0'");
        }


        if (!Util.isEmpty(paymentAliasId)) {
            sb.append(SqlUtil.whereClause(paymentAliasId, "im.PAYMENT_ID", params));
        }
        if (!Util.isEmpty(parentCompCode)) {
            sb.append(SqlUtil.whereClause(parentCompCode, "pc.PARENT_COMP_CODE", params));
        }
        if (!Util.isEmpty(parentDocNo)) {
            sb.append(SqlUtil.whereClause(parentDocNo, "pc.PARENT_DOC_NO", params));
        }
        if (!Util.isEmpty(parentFiscalYear)) {
            sb.append(SqlUtil.whereClause(parentFiscalYear, "pc.PARENT_FISCAL_YEAR", params));
        }

        sb.append(" ORDER BY im.VENDOR ");

        logger.info(" sql real run : {}", sb.toString());
        Query q = entityManager.createNativeQuery(sb.toString(), PaymentRealRun.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<PaymentRealRun> paymentRealRun = q.getResultList();
        return paymentRealRun;
    }

}
