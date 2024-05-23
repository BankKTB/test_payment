package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.context.Context;
import th.com.bloomcode.paymentservice.model.config.Parameter;
import th.com.bloomcode.paymentservice.model.config.ParameterConfig;
import th.com.bloomcode.paymentservice.model.idem.HouseBankPaymentMethod;
import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;
import th.com.bloomcode.paymentservice.model.payment.*;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareProposalDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareSelectProposalDocument;
import th.com.bloomcode.paymentservice.repository.payment.PrepareProposalDocumentRepository;
import th.com.bloomcode.paymentservice.service.idem.CompanyPayingMinimalAmountService;
import th.com.bloomcode.paymentservice.service.idem.VendorBankAccountService;
import th.com.bloomcode.paymentservice.service.payment.*;
import th.com.bloomcode.paymentservice.util.JSONUtil;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentHeader;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentLine;
import th.com.bloomcode.paymentservice.webservice.model.FIMessage;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;
import th.com.bloomcode.paymentservice.webservice.model.request.APPaymentRequest;

import javax.jms.Message;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class PaymentAsyncServiceImpl implements PaymentAsyncService {

  private final PrepareProposalDocumentRepository prepareProposalDocumentRepository;
  private final PaymentProcessService paymentProcessService;
  private final GLHeadService glHeadService;
  private final PaymentInformationService paymentInformationService;
  private final PaymentAliasService paymentAliasService;
  private final PrepareRunDocumentService prepareRunDocumentService;
  private final CompanyPayingMinimalAmountService companyPayingMinimalAmountService;
  private final VendorBankAccountService vendorBankAccountService;
  private final WriteLogAsyncService writeLogAsyncService;
  private final JmsTemplate jmsTemplate;
  private final BankCodeService bankCodeService;
  private final JdbcTemplate paymentJdbcTemplate;

  private final List<String> direct = List.of("1", "3", "6", "K", "G", "H", "A", "B");

  public PaymentAsyncServiceImpl(
      PrepareProposalDocumentRepository prepareProposalDocumentRepository,
      PaymentProcessService paymentProcessService,
      GLHeadService glHeadService,
      PaymentInformationService paymentInformationService,
      PaymentAliasService paymentAliasService,
      PrepareRunDocumentService prepareRunDocumentService,
      CompanyPayingMinimalAmountService companyPayingMinimalAmountService,
      VendorBankAccountService vendorBankAccountService,
      WriteLogAsyncService writeLogAsyncService,
      JmsTemplate jmsTemplate,
      BankCodeService bankCodeService,
      @Qualifier("paymentJdbcTemplate") JdbcTemplate paymentJdbcTemplate) {
    this.prepareProposalDocumentRepository = prepareProposalDocumentRepository;
    this.paymentProcessService = paymentProcessService;
    this.glHeadService = glHeadService;
    this.paymentInformationService = paymentInformationService;
    this.paymentAliasService = paymentAliasService;
    this.prepareRunDocumentService = prepareRunDocumentService;
    this.companyPayingMinimalAmountService = companyPayingMinimalAmountService;
    this.vendorBankAccountService = vendorBankAccountService;
    this.writeLogAsyncService = writeLogAsyncService;
    this.jmsTemplate = jmsTemplate;
    this.bankCodeService = bankCodeService;
    this.paymentJdbcTemplate = paymentJdbcTemplate;
  }

  //    @Override
  //    @Async
  //    public void createProposal(JwtBody jwt, PaymentAlias paymentAlias, WSWebInfo webInfo) {
  //        try {
  //
  //            ParameterConfig parameterConfig =
  // JSONUtil.convertJsonToObject(paymentAlias.getJsonText(),
  //                    ParameterConfig.class);
  //            List<PrepareProposalDocument> prepareProposalDocumentList =
  // prepareProposalDocumentRepository.findUnBlockDocumentCanPayByParameter(parameterConfig,
  // jwt.getSub());
  //            log.info("prepareProposalDocumentList create : {}",
  // prepareProposalDocumentList.size());
  //            glHeadService.updateGLHeadLockDocument(jwt, parameterConfig, paymentAlias.getId());
  //
  ////            PaymentAlias paymentAlias = paymentAliasService.findOneById(paymentAliasId);
  //            CompanyPayingMinimalAmount companyPayingMinimalAmount =
  // companyPayingMinimalAmountService.findOneByValueCode("99999");
  //
  //            Parameter parameter = parameterConfig.getParameter();
  ////            List<PrepareProposalDocument> prepareProposalDocumentList =
  // findUnBlockDocumentCanPayByParameter(jwt, parameterConfig);
  //
  //            log.info("prepareProposalDocumentList : {}", prepareProposalDocumentList.size());
  ////            Long seqNo = prepareProposalDocumentRepository.getNextSeries(true);
  ////            log.info("seqNo : {}", seqNo);
  ////            log.info("seqNo + prepareProposalDocumentList : {}", seqNo +
  // prepareProposalDocumentList.size());
  ////            prepareProposalDocumentRepository.updateNextSeries(seqNo +
  // prepareProposalDocumentList.size(), true);
  ////            Long processSeqNo = paymentProcessService.getNextSeries();
  ////            Long informationSeqNo = paymentInformationService.getNextSeries();
  //            List<GLHead> glHeads = new ArrayList<>();
  //            List<PaymentProcess> paymentProcesses = new ArrayList<>();
  //            List<PaymentInformation> paymentInformations = new ArrayList<>();
  //            boolean haveK3OrKX = false;
  //            BigDecimal amountChild = BigDecimal.ZERO;
  //
  //            List<BankCode> bankCodes = bankCodeService.findAllBank();
  //            for (PrepareProposalDocument prepareProposalDocument : prepareProposalDocumentList)
  // {
  //                Map<String, BigDecimal> listMap = new HashMap<>();
  //                amountChild = BigDecimal.ZERO;
  //                String paymentDocNo =
  // SqlUtil.generateProposalDocument(prepareProposalDocumentRepository.getNextSeries(true));
  //                prepareProposalDocument.setPaymentClearingDocNo(paymentDocNo);
  //
  // prepareProposalDocument.setPaymentClearingDate(Util.stringToTimestamp(parameter.getPostDate()));
  //                prepareProposalDocument.setPaymentClearingEntryDate(new Timestamp(new
  // Date().getTime()));
  //
  // prepareProposalDocument.setPaymentClearingYear(prepareProposalDocument.getOriginalFiscalYear());
  //                prepareProposalDocument.setPaymentDate(paymentAlias.getPaymentDate());
  //                prepareProposalDocument.setPaymentDateAcct(paymentAlias.getPaymentDate());
  //                prepareProposalDocument.setPaymentName(paymentAlias.getPaymentName());
  //                prepareProposalDocument.setPaymentId(paymentAlias.getId());
  //
  //                HouseBankPaymentMethod houseBankPaymentMethod =
  // Context.sessionHouseBankPaymentMethod.get(prepareProposalDocument.getPaymentMethod());
  //
  // prepareProposalDocument.setPayingBankCode(houseBankPaymentMethod.getAccountCode());
  //
  // prepareProposalDocument.setPayingHouseBank(houseBankPaymentMethod.getHouseBank());
  //
  // prepareProposalDocument.setPayingBankAccountNo(houseBankPaymentMethod.getBankAccountCode());
  //
  // prepareProposalDocument.setPayingBankCountry(houseBankPaymentMethod.getCountryCode());
  //
  // prepareProposalDocument.setPayingBankNo(houseBankPaymentMethod.getBankAccountCode());
  //
  // prepareProposalDocument.setPayingGLAccount(houseBankPaymentMethod.getGlAccount());
  //
  // prepareProposalDocument.setPayingBankKey(houseBankPaymentMethod.getBankBranch());
  //
  // prepareProposalDocument.setPayingBankName(houseBankPaymentMethod.getHouseBank());
  //                prepareProposalDocument.setPayingCompCode("99999");
  //
  // prepareProposalDocument.setAmountPaid(prepareProposalDocument.getAmount().subtract(prepareProposalDocument.getWtxAmount()));
  //
  //                this.checkErrorCodePrepareProposalDocument(bankCodes, prepareProposalDocument,
  // parameter, companyPayingMinimalAmount);
  //
  //
  //                if (prepareProposalDocument.getDocumentType().equalsIgnoreCase("KA") ||
  // prepareProposalDocument.getDocumentType().equalsIgnoreCase("KB") ||
  //                        prepareProposalDocument.getDocumentType().equalsIgnoreCase("KG") ||
  // prepareProposalDocument.getDocumentType().equalsIgnoreCase("KC")) {
  //
  //                    boolean checkChild = false;
  //                    try {
  //                        Long.parseLong(prepareProposalDocument.getHeaderReference());
  //                        checkChild = true;
  //                    } catch (Exception e) {
  //                        checkChild = false;
  //                    }
  //
  //
  ////                    if (prepareProposalDocument.getHeaderReference().startsWith("AA#") ||
  // checkChild) {
  //                    listMap = this.createDocumentSpecial(paymentAlias, parameter,
  // prepareProposalDocument, jwt.getSub());
  ////                    }
  //
  //
  //                }
  //
  //                //add list update gl head
  ////                glHeads.add(new GLHead(prepareProposalDocument.getCompanyCode(),
  // prepareProposalDocument.getOriginalDocumentNo(),
  // prepareProposalDocument.getOriginalFiscalYear(),
  // prepareProposalDocument.getPaymentClearingDocNo(), prepareProposalDocument.getPaymentId()));
  ////        glHeadService.updateGLHead(prepareProposalDocument.getCompanyCode(),
  // prepareProposalDocument.getOriginalDocumentNo(),
  // prepareProposalDocument.getOriginalFiscalYear(),
  // prepareProposalDocument.getPaymentClearingDocNo(), prepareProposalDocument.getPaymentId());
  //
  //                PaymentProcess paymentProcess = new PaymentProcess(prepareProposalDocument,
  // true, false);
  //                paymentProcess.setId(paymentProcessService.getNextSeries());
  //                paymentProcess.setPmGroupNo(prepareProposalDocument.getPaymentName() + "-" +
  // prepareProposalDocument.getPaymentDate().getTime());
  //                paymentProcess.setPmGroupDoc(prepareProposalDocument.getCompanyCode() + "-" +
  // prepareProposalDocument.getOriginalDocumentNo() + "-" +
  // prepareProposalDocument.getOriginalFiscalYear() + "-" +
  // prepareProposalDocument.getPaymentName() + "-" +
  // prepareProposalDocument.getPaymentDate().getTime());
  //                paymentProcess.setProposalBlock(false);
  //                paymentProcess.setPaymentCompanyCode("99999");
  //                if (listMap.get("amount") != null &&
  // listMap.get("amount").compareTo(BigDecimal.ZERO) > 0) {
  //                    paymentProcess.setHaveChild(true);
  //                    paymentProcess.setCreditMemo(listMap.get("amount"));
  //                    paymentProcess.setWtxCreditMemo(listMap.get("amountTax"));
  //
  // paymentProcess.setInvoiceAmountPaid(prepareProposalDocument.getAmountPaid().subtract(amountChild));
  //                } else {
  //                    paymentProcess.setHaveChild(false);
  //                    paymentProcess.setCreditMemo(BigDecimal.ZERO);
  //                    paymentProcess.setWtxCreditMemo(BigDecimal.ZERO);
  //                }
  //
  //
  //                if (null != prepareProposalDocument.getOriginalDocument()) {
  //
  //                    String originalDocumentNo =
  // prepareProposalDocument.getOriginalDocument().substring(0, 10);
  //                    String originalCompanyCode =
  // prepareProposalDocument.getOriginalDocument().substring(10, 15);
  //                    String originalFiscalYear =
  // prepareProposalDocument.getOriginalDocument().substring(15, 19);
  //
  ////                    GLHead glHeadOriginal =
  // glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(originalCompanyCode, originalDocumentNo, originalFiscalYear);
  //
  //
  // paymentProcess.setOriginalDocumentNo(prepareProposalDocument.getRealOriginalDocumentNo());
  //
  // paymentProcess.setOriginalCompanyCode(prepareProposalDocument.getRealOriginalCompanyCode());
  //
  // paymentProcess.setOriginalFiscalYear(prepareProposalDocument.getRealOriginalFiscalYear());
  //
  // paymentProcess.setOriginalDocumentType(prepareProposalDocument.getRealOriginalDocumentType());
  //                    paymentProcess.setOriginalAmount(prepareProposalDocument.getAmount());
  //
  // paymentProcess.setOriginalAmountPaid(prepareProposalDocument.getAmountPaid());
  //
  ////                    GLLine glLineOriginalTypeK =
  // glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(originalCompanyCode, originalDocumentNo, originalFiscalYear, "K");
  //
  //
  // paymentProcess.setOriginalPaymentCenter(prepareProposalDocument.getLinePaymentCenter());
  //
  // paymentProcess.setOriginalWtxAmount(prepareProposalDocument.getLineWtxAmount());
  //                    paymentProcess.setOriginalWtxBase(prepareProposalDocument.getLineWtxBase());
  //
  // paymentProcess.setOriginalWtxAmountP(prepareProposalDocument.getLineWtxAmountP());
  //
  // paymentProcess.setOriginalWtxBaseP(prepareProposalDocument.getLineWtxBaseP());
  //                }
  //
  //                // add list payment process
  //                paymentProcesses.add(paymentProcess);
  ////        paymentProcess = this.paymentProcessService.save(paymentProcess);
  //
  //                PaymentInformation paymentInformation = new
  // PaymentInformation(prepareProposalDocument, true, false);
  //                paymentInformation.setId(paymentInformationService.getNextSeries());
  //                paymentInformation.setPaymentProcessId(paymentProcess.getId());
  //
  //                // add list payment information
  //                paymentInformations.add(paymentInformation);
  ////        this.paymentInformationService.save(paymentInformation);
  //
  //                log.info(" end prepare save {}", "");
  //            }
  //
  //            // end loop
  //            // save batch
  //            this.glHeadService.updateGLHeadBatch(glHeads);
  //            this.paymentProcessService.saveBatch(paymentProcesses);
  //            this.paymentInformationService.saveBatch(paymentInformations);
  //
  ////            prepareProposalDocumentRepository.updateNextSeries(seqNo, true);
  ////
  ////            paymentProcessService.updateNextSeries(++processSeqNo);
  ////            paymentInformationService.updateNextSeries(++informationSeqNo);
  ////            this.writeLogPrepareProposalDocument(paymentAlias, parameterConfig,
  // prepareProposalDocumentList, webInfo);
  //
  //            this.writeLogAsyncService.writeLogPrepareProposalDocument(paymentAlias,
  // parameterConfig, prepareProposalDocumentList, webInfo);
  //
  //            List<PrepareProposalDocument> paymentError = prepareProposalDocumentList.stream()
  //                    .filter(p -> "E".equalsIgnoreCase(p.getStatus())).collect(toList());
  //
  //            int listError = paymentError.size();
  //
  //            paymentAlias.setProposalStatus("S");
  //            paymentAlias.setProposalSuccessDocument(prepareProposalDocumentList.size() -
  // listError);
  //            paymentAlias.setProposalTotalDocument(prepareProposalDocumentList.size());
  //            paymentAlias.setProposalEnd(new Timestamp(System.currentTimeMillis()));
  //            paymentAliasService.save(paymentAlias);
  ////            result.setData(paymentAliasService.save(paymentAlias));
  ////            result.setStatus(HttpStatus.OK.value());
  ////            return new ResponseEntity<>(result, HttpStatus.OK);
  //        } catch (Exception e) {
  //            e.printStackTrace();
  ////            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
  ////            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
  ////            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  //        }
  //
  //    }

  //    @Override
  //    @Async
  //    public void createRealRun(List<PrepareRunDocument> prepareRunDocumentList, PaymentAlias
  // paymentAlias, WSWebInfo webInfo) {
  //        try {
  ////            List<PrepareRunDocument> prepareProposalDocumentList =
  // prepareRunDocumentService.findProposalDoc(paymentAlias.getId(), true);
  //            log.info("prepareRunDocumentList : {}", prepareRunDocumentList);
  //
  ////            Long processSeqNo = paymentProcessService.getNextSeries();
  ////            Long informationSeqNo = paymentInformationService.getNextSeries();
  ////            log.info("processSeqNo current : {}", processSeqNo);
  ////            log.info("informationSeqNo current : {}", informationSeqNo);
  //            boolean haveK3OrKX = false;
  //
  //
  //            paymentAlias.setRunTotalDocument(prepareRunDocumentList.size());
  //            paymentAliasService.save(paymentAlias);
  //
  //            for (PrepareRunDocument item : prepareRunDocumentList) {
  //                haveK3OrKX = false;
  //                APPaymentRequest apPaymentRequest = new APPaymentRequest();
  //
  //                APPaymentHeader aPPaymentHeader = new APPaymentHeader();
  //                aPPaymentHeader.setPmCompCode("99999");
  //                aPPaymentHeader.setPmDate(item.getPaymentDate());
  //                aPPaymentHeader.setPmIden(item.getPaymentName());
  //                aPPaymentHeader.setVendor(item.getVendorCode());
  //                aPPaymentHeader.setPayee(item.getPayeeCode());
  //
  //                aPPaymentHeader.setPmGroupNo(item.getPmGroupNo());
  //                aPPaymentHeader.setPmGroupDoc(item.getPmGroupDoc());
  //
  //                aPPaymentHeader.setVendorTaxID(item.getVendorTaxId());
  //                aPPaymentHeader.setBankAccNo(item.getPayeeBankAccountNo());
  //                aPPaymentHeader.setBranchNo(item.getPayeeBankKey());
  //                aPPaymentHeader.setGlAccount(item.getPayingGlAccount());
  //                aPPaymentHeader.setReceiptTaxID(item.getVendorTaxId());
  //
  //                PayMethodConfig payMethodConfig = Context.sessionPayMethodConfig.get("TH-" +
  // item.getPaymentMethod());
  //                aPPaymentHeader.setDocType(payMethodConfig.getDocumentTypeForPayment());
  //
  //                aPPaymentHeader.setDateDoc(item.getPaymentDate());
  //                aPPaymentHeader.setDateAcct(item.getPaymentDateAcct());
  //
  //                List<APPaymentLine> listAPPaymentLine = new ArrayList<>();
  //
  //                APPaymentLine apPaymentLine = new APPaymentLine();
  //                apPaymentLine.setInvCompCode(item.getInvoiceCompanyCode());
  //                apPaymentLine.setInvDocNo(item.getInvoiceDocumentNo());
  //                apPaymentLine.setInvFiscalYear(item.getInvoiceFiscalYear());
  //                apPaymentLine.setInvLine(item.getLine());
  //                apPaymentLine.setDocType(item.getInvoiceDocumentType());
  //                apPaymentLine.setDateAcct(item.getDateAcct());
  //                apPaymentLine.setDateDoc(item.getDateDue());
  //                apPaymentLine.setVendor(item.getVendorCode());
  //                apPaymentLine.setPayee(item.getPayeeCode());
  //
  //                apPaymentLine.setDrCr(item.getDrCr());
  //                apPaymentLine.setAmount(item.getInvoiceAmount());
  //                apPaymentLine.setWtxAmount(item.getInvoiceWtxAmount());
  //
  //                listAPPaymentLine.add(apPaymentLine);
  //
  //                BigDecimal totalAmount = BigDecimal.ZERO;
  //                if (item.getInvoiceDocumentType().equalsIgnoreCase("KA") ||
  // item.getInvoiceDocumentType().equalsIgnoreCase("KB") ||
  //                        item.getInvoiceDocumentType().equalsIgnoreCase("KG") ||
  // item.getInvoiceDocumentType().equalsIgnoreCase("KC")) {
  //
  //
  //                    List<PrepareRunDocument> specialDoc =
  // prepareRunDocumentService.findChildProposalDoc(apPaymentLine, true);
  //
  //                    haveK3OrKX = specialDoc.size() > 0;
  //
  //                    for (PrepareRunDocument specialLine : specialDoc) {
  //
  //                        log.info("K3 KX PrepareRunDocument {} ", specialLine);
  //
  //
  //                        PaymentProcess paymentProcess = new PaymentProcess(specialLine);
  //                        paymentProcess.setId(paymentProcessService.getNextSeries());
  //                        paymentProcess.setPaymentDocumentNo("XXXXXXXXXX");
  //                        paymentProcess.setPaymentCompanyCode("99999");
  //
  //                        paymentProcess.setParentCompanyCode(item.getInvoiceCompanyCode());
  //                        paymentProcess.setParentDocumentNo(item.getInvoiceDocumentNo());
  //                        paymentProcess.setParentFiscalYear(item.getInvoiceFiscalYear());
  //
  //                        paymentProcess.setChild(true);
  //                        paymentProcess.setProposal(false);
  //                        this.paymentProcessService.save(paymentProcess);
  //
  //
  //                        PaymentInformation paymentInformation = new
  // PaymentInformation(specialLine);
  //                        paymentInformation.setId(paymentInformationService.getNextSeries());
  //                        paymentInformation.setPaymentProcessId(paymentProcess.getId());
  //                        this.paymentInformationService.save(paymentInformation);
  ////                        log.info("K3 KX processSeqNo {} ", processSeqNo);
  ////                        log.info("K3 KX informationSeqNo {} ", informationSeqNo);
  //
  //                        APPaymentLine line = new APPaymentLine();
  //                        line.setInvCompCode(specialLine.getInvoiceCompanyCode());
  //                        line.setInvDocNo(specialLine.getInvoiceDocumentNo());
  //                        line.setInvFiscalYear(specialLine.getInvoiceFiscalYear());
  //                        line.setInvLine(specialLine.getLine());
  //                        line.setDocType(specialLine.getInvoiceDocumentType());
  //                        line.setDateAcct(specialLine.getDateAcct());
  //                        line.setDateDoc(specialLine.getDateDoc());
  //                        line.setVendor(specialLine.getVendorCode());
  //                        line.setPayee(specialLine.getPayeeCode());
  //
  //                        line.setDrCr(specialLine.getDrCr());
  //                        line.setAmount(specialLine.getInvoiceAmount());
  //                        line.setWtxAmount(specialLine.getInvoiceWtxAmount());
  //
  //                        totalAmount = totalAmount.add(specialLine.getInvoiceAmountPaid());
  //
  //                        listAPPaymentLine.add(line);
  //
  //                    }
  //                }
  //                log.info("K3 KX processSeqNo {} ", totalAmount);
  //                log.info("K3 KX informationSeqNo {} ",
  // item.getInvoiceAmount().subtract(item.getInvoiceWtxAmount()));
  //                log.info("K3 KX informationSeqNo {} ",
  // totalAmount.subtract(item.getInvoiceAmount().subtract(item.getInvoiceWtxAmount())));
  //                aPPaymentHeader.setCountLine(listAPPaymentLine.size());
  //
  // aPPaymentHeader.setAmount(totalAmount.subtract(item.getInvoiceAmount().subtract(item.getInvoiceWtxAmount())));
  //                apPaymentRequest.setHeader(aPPaymentHeader);
  //                apPaymentRequest.setLines(listAPPaymentLine);
  //                apPaymentRequest.setWebInfo(webInfo);
  //
  //                try {
  //                    FIMessage fiMessage = new FIMessage();
  //                    fiMessage.setId(item.getPmGroupDoc());
  //                    fiMessage.setType(FIMessage.Type.REQUEST.getCode());
  //                    fiMessage.setDataType(FIMessage.DataType.CREATE.getCode());
  //                    fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
  //                    fiMessage.setTo("99999");
  //                    log.info("apPaymentRequest {}", apPaymentRequest);
  //                    fiMessage.setData(XMLUtil.xmlMarshall(APPaymentRequest.class,
  // apPaymentRequest));
  //
  //                    log.info("fiMessage {}", fiMessage);
  //                    this.send(fiMessage);
  //
  //                    PaymentProcess paymentProcess = new PaymentProcess(item);
  //                    paymentProcess.setPaymentCompanyCode("99999");
  //                    paymentProcess.setId(paymentProcessService.getNextSeries());
  //                    paymentProcess.setPaymentDocumentType(aPPaymentHeader.getDocType());
  //
  //                    paymentProcess.setPmGroupNo(item.getPmGroupNo());
  //                    paymentProcess.setPmGroupDoc(item.getPmGroupDoc());
  //                    if (haveK3OrKX) {
  //                        paymentProcess.setHaveChild(true);
  //                    } else {
  //                        paymentProcess.setHaveChild(false);
  //                    }
  //
  //
  //                    this.paymentProcessService.save(paymentProcess);
  //
  //                    PaymentInformation paymentInformation = new PaymentInformation(item);
  //                    paymentInformation.setId(paymentInformationService.getNextSeries());
  //                    paymentInformation.setPaymentProcessId(paymentProcess.getId());
  //                    this.paymentInformationService.save(paymentInformation);
  ////                    log.info("normal running {}", processSeqNo);
  ////                    log.info("normal running {}", informationSeqNo);
  //
  //                } catch (Exception e) {
  //                    e.printStackTrace();
  //                }
  //
  //            }
  //            this.writeLogAsyncService.writeLogPaymentRealRun(paymentAlias,
  // prepareRunDocumentList, webInfo);
  //
  ////            paymentProcessService.updateNextSeries(++processSeqNo);
  ////            paymentInformationService.updateNextSeries(++informationSeqNo);
  //
  //            paymentAlias.setRunEnd(new Timestamp(System.currentTimeMillis()));
  //            paymentAlias.setRunStatus("S");
  //            paymentAlias.setRunJobStatus("S");
  ////      paymentAlias.setRunTotalDocument(prepareRunDocumentList.size());
  //            paymentAlias.setRunSuccessDocument(0);
  //            paymentAliasService.save(paymentAlias);
  //        } catch (Exception e) {
  //            e.printStackTrace();
  //        }
  //
  //    }

  @Override
  public void createProposalJob(PaymentAlias paymentAlias, WSWebInfo webInfo) {
//    try {
//
//      ParameterConfig parameterConfig =
//          JSONUtil.convertJsonToObject(paymentAlias.getJsonText(), ParameterConfig.class);
//      List<PrepareProposalDocument> prepareProposalDocumentList = null;
//      synchronized (this) {
//        prepareProposalDocumentList =
//            prepareProposalDocumentRepository.findUnBlockDocumentCanPayByParameter(
//                parameterConfig, webInfo.getUserWebOnline());
//        log.info("prepareProposalDocumentList create : {}", prepareProposalDocumentList.size());
//        glHeadService.updateGLHeadLockDocument(
//            parameterConfig, paymentAlias.getId(), webInfo.getUserWebOnline());
//      }
//
//      //            PaymentAlias paymentAlias = paymentAliasService.findOneById(paymentAliasId);
//      CompanyPayingMinimalAmount companyPayingMinimalAmount =
//          companyPayingMinimalAmountService.findOneByValueCode("99999");
//
//      Parameter parameter = parameterConfig.getParameter();
//      //            List<PrepareProposalDocument> prepareProposalDocumentList =
//      // findUnBlockDocumentCanPayByParameter(jwt, parameterConfig);
//
//      log.info("prepareProposalDocumentList : {}", prepareProposalDocumentList.size());
//      //            Long seqNo = prepareProposalDocumentRepository.getNextSeries(true);
//      //            log.info("seqNo : {}", seqNo);
//      //            log.info("seqNo + prepareProposalDocumentList : {}", seqNo +
//      // prepareProposalDocumentList.size());
//      //            prepareProposalDocumentRepository.updateNextSeries(seqNo +
//      // prepareProposalDocumentList.size(), true);
//      //            Long processSeqNo = paymentProcessService.getNextSeries();
//      //            Long informationSeqNo = paymentInformationService.getNextSeries();
//      List<GLHead> glHeads = new ArrayList<>();
//      List<PaymentProcess> paymentProcesses = new ArrayList<>();
//      List<PaymentInformation> paymentInformations = new ArrayList<>();
//      boolean haveK3OrKX = false;
//      BigDecimal amountChild = BigDecimal.ZERO;
//
//      List<BankCode> bankCodes = bankCodeService.findAllBank();
//      for (PrepareProposalDocument prepareProposalDocument : prepareProposalDocumentList) {
//        Map<String, BigDecimal> listMap = new HashMap<>();
//        amountChild = BigDecimal.ZERO;
//        prepareProposalDocument.setPaymentClearingDate(parameter.getPostDate());
//        prepareProposalDocument.setPaymentClearingEntryDate(new Timestamp(new Date().getTime()));
//        prepareProposalDocument.setPaymentClearingYear(
//            prepareProposalDocument.getOriginalFiscalYear());
//        prepareProposalDocument.setPaymentDate(paymentAlias.getPaymentDate());
//        prepareProposalDocument.setPaymentDateAcct(paymentAlias.getPaymentDate());
//        prepareProposalDocument.setPaymentName(paymentAlias.getPaymentName());
//        prepareProposalDocument.setPaymentId(paymentAlias.getId());
//
//        HouseBankPaymentMethod houseBankPaymentMethod =
//            Context.sessionHouseBankPaymentMethod.get(prepareProposalDocument.getPaymentMethod());
//        prepareProposalDocument.setPayingBankCode(houseBankPaymentMethod.getAccountCode());
//        prepareProposalDocument.setPayingHouseBank(houseBankPaymentMethod.getHouseBank());
//        prepareProposalDocument.setPayingBankAccountNo(houseBankPaymentMethod.getBankAccountCode());
//        prepareProposalDocument.setPayingBankCountry(houseBankPaymentMethod.getCountryCode());
//        prepareProposalDocument.setPayingBankNo(houseBankPaymentMethod.getBankAccountCode());
//        prepareProposalDocument.setPayingGLAccount(houseBankPaymentMethod.getGlAccount());
//        prepareProposalDocument.setPayingBankKey(houseBankPaymentMethod.getBankBranch());
//        prepareProposalDocument.setPayingBankName(houseBankPaymentMethod.getHouseBank());
//        prepareProposalDocument.setPayingCompCode("99999");
//        prepareProposalDocument.setAmountPaid(
//            prepareProposalDocument.getAmount().subtract(prepareProposalDocument.getWtxAmount()));
//
//        this.checkErrorCodePrepareProposalDocument(
//            bankCodes, prepareProposalDocument, parameter, companyPayingMinimalAmount);
//
//        if (null == prepareProposalDocument.getErrorCode()
//            || (null != prepareProposalDocument.getErrorCode()
//                && !prepareProposalDocument.getErrorCode().equalsIgnoreCase("E"))) {
//          String paymentDocNo =
//              SqlUtil.generateProposalDocument(
//                  prepareProposalDocumentRepository.getNextSeries(true));
//          prepareProposalDocument.setPaymentClearingDocNo(paymentDocNo);
//        }
//
//        if (prepareProposalDocument.getDocumentType().equalsIgnoreCase("KA")
//            || prepareProposalDocument.getDocumentType().equalsIgnoreCase("KB")
//            || prepareProposalDocument.getDocumentType().equalsIgnoreCase("KG")
//            || prepareProposalDocument.getDocumentType().equalsIgnoreCase("KC")) {
//
//          boolean checkChild = false;
//          try {
//            Long.parseLong(prepareProposalDocument.getHeaderReference());
//            checkChild = true;
//          } catch (Exception e) {
//            checkChild = false;
//          }
//
//          //                    if (prepareProposalDocument.getHeaderReference().startsWith("AA#")
//          // || checkChild) {
//          listMap =
//              this.createDocumentSpecial(
//                  paymentAlias, parameter, prepareProposalDocument, webInfo.getUserWebOnline());
//          //                    }
//
//        }
//
//        // add list update gl head
//        //                glHeads.add(new GLHead(prepareProposalDocument.getCompanyCode(),
//        // prepareProposalDocument.getOriginalDocumentNo(),
//        // prepareProposalDocument.getOriginalFiscalYear(),
//        // prepareProposalDocument.getPaymentClearingDocNo(),
//        // prepareProposalDocument.getPaymentId()));
//        //        glHeadService.updateGLHead(prepareProposalDocument.getCompanyCode(),
//        // prepareProposalDocument.getOriginalDocumentNo(),
//        // prepareProposalDocument.getOriginalFiscalYear(),
//        // prepareProposalDocument.getPaymentClearingDocNo(),
//        // prepareProposalDocument.getPaymentId());
//
//        PaymentProcess paymentProcess = new PaymentProcess(prepareProposalDocument, true, false);
//        paymentProcess.setId(paymentProcessService.getNextSeries());
//        paymentProcess.setPmGroupNo(
//            prepareProposalDocument.getPaymentName()
//                + "-"
//                + prepareProposalDocument.getPaymentDate().getTime());
//        paymentProcess.setPmGroupDoc(
//            prepareProposalDocument.getCompanyCode()
//                + "-"
//                + prepareProposalDocument.getOriginalDocumentNo()
//                + "-"
//                + prepareProposalDocument.getOriginalFiscalYear()
//                + "-"
//                + prepareProposalDocument.getPaymentName()
//                + "-"
//                + prepareProposalDocument.getPaymentDate().getTime());
//        paymentProcess.setProposalBlock(false);
//        paymentProcess.setPaymentCompanyCode("99999");
//
//        if (listMap.get("amount") != null && listMap.get("amount").compareTo(BigDecimal.ZERO) > 0) {
//          paymentProcess.setHaveChild(true);
//          paymentProcess.setCreditMemo(listMap.get("amount"));
//          paymentProcess.setWtxCreditMemo(listMap.get("amountTax"));
//          paymentProcess.setInvoiceAmountPaid(
//              prepareProposalDocument.getAmountPaid().subtract(amountChild));
//        } else {
//          paymentProcess.setHaveChild(false);
//          paymentProcess.setCreditMemo(BigDecimal.ZERO);
//          paymentProcess.setWtxCreditMemo(BigDecimal.ZERO);
//        }
//
//        if (null != prepareProposalDocument.getOriginalDocument()) {
//
//          String originalDocumentNo =
//              prepareProposalDocument.getOriginalDocument().substring(0, 10);
//          String originalCompanyCode =
//              prepareProposalDocument.getOriginalDocument().substring(10, 15);
//          String originalFiscalYear =
//              prepareProposalDocument.getOriginalDocument().substring(15, 19);
//
//          //                    GLHead glHeadOriginal =
//          // glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(originalCompanyCode, originalDocumentNo, originalFiscalYear);
//
//          paymentProcess.setOriginalDocumentNo(prepareProposalDocument.getRealOriginalDocumentNo());
//          paymentProcess.setOriginalCompanyCode(
//              prepareProposalDocument.getRealOriginalCompanyCode());
//          paymentProcess.setOriginalFiscalYear(prepareProposalDocument.getRealOriginalFiscalYear());
//          paymentProcess.setOriginalDocumentType(
//              prepareProposalDocument.getRealOriginalDocumentType());
//          paymentProcess.setOriginalAmount(prepareProposalDocument.getAmount());
//          paymentProcess.setOriginalAmountPaid(prepareProposalDocument.getAmountPaid());
//
//          //                    GLLine glLineOriginalTypeK =
//          // glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(originalCompanyCode, originalDocumentNo, originalFiscalYear, "K");
//
//          paymentProcess.setOriginalPaymentCenter(prepareProposalDocument.getLinePaymentCenter());
//          paymentProcess.setOriginalWtxAmount(prepareProposalDocument.getLineWtxAmount());
//          paymentProcess.setOriginalWtxBase(prepareProposalDocument.getLineWtxBase());
//          paymentProcess.setOriginalWtxAmountP(prepareProposalDocument.getLineWtxAmountP());
//          paymentProcess.setOriginalWtxBaseP(prepareProposalDocument.getLineWtxBaseP());
//        }
//
//        // add list payment process
//        paymentProcesses.add(paymentProcess);
//        //        paymentProcess = this.paymentProcessService.save(paymentProcess);
//
//        PaymentInformation paymentInformation =
//            new PaymentInformation(prepareProposalDocument, true, false);
//        paymentInformation.setId(paymentInformationService.getNextSeries());
//        paymentInformation.setPaymentProcessId(paymentProcess.getId());
//
//        // add list payment information
//        paymentInformations.add(paymentInformation);
//        //        this.paymentInformationService.save(paymentInformation);
//
//        log.info(" end prepare save {}", "");
//      }
//
//      // end loop
//      // save batch
//      this.glHeadService.updateGLHeadBatch(glHeads);
//      this.paymentProcessService.saveBatch(paymentProcesses);
//      this.paymentInformationService.saveBatch(paymentInformations);
//
//      //            prepareProposalDocumentRepository.updateNextSeries(seqNo, true);
//      //
//      //            paymentProcessService.updateNextSeries(++processSeqNo);
//      //            paymentInformationService.updateNextSeries(++informationSeqNo);
//      //            this.writeLogPrepareProposalDocument(paymentAlias, parameterConfig,
//      // prepareProposalDocumentList, webInfo);
//
//      this.writeLogAsyncService.writeLogPrepareProposalDocumentJob(
//          paymentAlias, parameterConfig, prepareProposalDocumentList, webInfo);
//
//      List<PrepareProposalDocument> paymentError =
//          prepareProposalDocumentList.stream()
//              .filter(p -> "E".equalsIgnoreCase(p.getStatus()))
//              .collect(toList());
//
//      int listError = paymentError.size();
//
//      paymentAlias.setProposalStatus("S");
//      paymentAlias.setProposalSuccessDocument(prepareProposalDocumentList.size() - listError);
//      paymentAlias.setProposalTotalDocument(prepareProposalDocumentList.size());
//      paymentAlias.setProposalEnd(new Timestamp(System.currentTimeMillis()));
//      paymentAliasService.save(paymentAlias);
//      //            result.setData(paymentAliasService.save(paymentAlias));
//      //            result.setStatus(HttpStatus.OK.value());
//      //            return new ResponseEntity<>(result, HttpStatus.OK);
//    } catch (Exception e) {
//      e.printStackTrace();
//      //            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//      //            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//      //            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
  }

  @Override
  @Async
  public void createRealRunJob(
      List<PrepareRunDocument> prepareRunDocumentList,
      PaymentAlias paymentAlias,
      WSWebInfo webInfo) {
    try {
      //            List<PrepareRunDocument> prepareProposalDocumentList =
      // prepareRunDocumentService.findProposalDoc(paymentAlias.getId(), true);
      //      log.info("prepareRunDocumentList : {}", prepareRunDocumentList);

      //            Long processSeqNo = paymentProcessService.getNextSeries();
      //            Long informationSeqNo = paymentInformationService.getNextSeries();
      //            log.info("processSeqNo current : {}", processSeqNo);
      //            log.info("informationSeqNo current : {}", informationSeqNo);
      boolean haveK3OrKX = false;
      for (PrepareRunDocument item : prepareRunDocumentList) {
        haveK3OrKX = false;
        APPaymentRequest apPaymentRequest = new APPaymentRequest();

        APPaymentHeader aPPaymentHeader = new APPaymentHeader();
        aPPaymentHeader.setPmCompCode("99999");
        aPPaymentHeader.setPmDate(item.getPaymentDate());
        aPPaymentHeader.setPmIden(item.getPaymentName());
        aPPaymentHeader.setVendor(item.getVendorCode());
        aPPaymentHeader.setPayee(item.getPayeeCode());

        aPPaymentHeader.setPmGroupNo(item.getPmGroupNo());
        aPPaymentHeader.setPmGroupDoc(item.getPmGroupDoc());

        aPPaymentHeader.setVendorTaxID(item.getVendorTaxId());
        aPPaymentHeader.setBankAccNo(item.getPayeeBankAccountNo());
        aPPaymentHeader.setBranchNo(item.getPayeeBankKey());
        aPPaymentHeader.setGlAccount(item.getPayingGlAccount());
        aPPaymentHeader.setReceiptTaxID(item.getVendorTaxId());

        PayMethodConfig payMethodConfig =
            Context.sessionPayMethodConfig.get("TH-" + item.getPaymentMethod());
        aPPaymentHeader.setDocType(payMethodConfig.getDocumentTypeForPayment());

        aPPaymentHeader.setDateDoc(item.getPaymentDate());
        aPPaymentHeader.setDateAcct(item.getPaymentDateAcct());

        List<APPaymentLine> listAPPaymentLine = new ArrayList<>();

        APPaymentLine apPaymentLine = new APPaymentLine();
        apPaymentLine.setInvCompCode(item.getInvoiceCompanyCode());
        apPaymentLine.setInvDocNo(item.getInvoiceDocumentNo());
        apPaymentLine.setInvFiscalYear(item.getInvoiceFiscalYear());
        apPaymentLine.setInvLine(item.getLine());
        apPaymentLine.setDocType(item.getInvoiceDocumentType());
        apPaymentLine.setDateAcct(item.getDateAcct());
        apPaymentLine.setDateDoc(item.getDateDue());
        apPaymentLine.setVendor(item.getVendorCode());
        apPaymentLine.setPayee(item.getPayeeCode());

        apPaymentLine.setDrCr(item.getDrCr());
        apPaymentLine.setAmount(item.getInvoiceAmount());
        apPaymentLine.setWtxAmount(item.getInvoiceWtxAmount());

        listAPPaymentLine.add(apPaymentLine);

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (item.getInvoiceDocumentType().equalsIgnoreCase("KA")
            || item.getInvoiceDocumentType().equalsIgnoreCase("KB")
            || item.getInvoiceDocumentType().equalsIgnoreCase("KG")
            || item.getInvoiceDocumentType().equalsIgnoreCase("KC")) {

          List<PrepareRunDocument> specialDoc =
              prepareRunDocumentService.findChildProposalDoc(apPaymentLine, true, paymentAlias.getId());

          haveK3OrKX = specialDoc.size() > 0;

          for (PrepareRunDocument specialLine : specialDoc) {

            //            log.info("K3 KX PrepareRunDocument {} ", specialLine);

            PaymentProcess paymentProcess = new PaymentProcess(specialLine);
            paymentProcess.setId(SqlUtil.getNextSeries(paymentJdbcTemplate, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ, null));
            paymentProcess.setPaymentDocumentNo("XXXXXXXXXX");
            paymentProcess.setPaymentCompanyCode("99999");

            paymentProcess.setParentCompanyCode(item.getInvoiceCompanyCode());
            paymentProcess.setParentDocumentNo(item.getInvoiceDocumentNo());
            paymentProcess.setParentFiscalYear(item.getInvoiceFiscalYear());

            paymentProcess.setChild(true);
            paymentProcess.setProposal(false);
            this.paymentProcessService.save(paymentProcess);

            PaymentInformation paymentInformation = new PaymentInformation(specialLine);
            paymentInformation.setId(SqlUtil.getNextSeries(paymentJdbcTemplate, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ, null));
            paymentInformation.setPaymentProcessId(paymentProcess.getId());
            this.paymentInformationService.save(paymentInformation);
            //                        log.info("K3 KX processSeqNo {} ", processSeqNo);
            //                        log.info("K3 KX informationSeqNo {} ", informationSeqNo);

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
        //        log.info("K3 KX processSeqNo {} ", totalAmount);
        //        log.info("K3 KX informationSeqNo {} ",
        // item.getInvoiceAmount().subtract(item.getInvoiceWtxAmount()));
        //        log.info("K3 KX informationSeqNo {} ",
        // totalAmount.subtract(item.getInvoiceAmount().subtract(item.getInvoiceWtxAmount())));
        aPPaymentHeader.setCountLine(listAPPaymentLine.size());
        aPPaymentHeader.setAmount(
            totalAmount.subtract(item.getInvoiceAmount().subtract(item.getInvoiceWtxAmount())));
        apPaymentRequest.setHeader(aPPaymentHeader);
        apPaymentRequest.setLines(listAPPaymentLine);
        apPaymentRequest.setWebInfo(webInfo);

        try {
          FIMessage fiMessage = new FIMessage();
          fiMessage.setId(item.getPmGroupDoc());
          fiMessage.setType(FIMessage.Type.REQUEST.getCode());
          fiMessage.setDataType(FIMessage.DataType.CREATE.getCode());
          fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
          fiMessage.setTo("99999");
          //          log.info("apPaymentRequest {}", apPaymentRequest);
          fiMessage.setData(XMLUtil.xmlMarshall(APPaymentRequest.class, apPaymentRequest));

          //          log.info("fiMessage {}", fiMessage);
          this.send(fiMessage);

          PaymentProcess paymentProcess = new PaymentProcess(item);
          paymentProcess.setPaymentCompanyCode("99999");
          paymentProcess.setId(SqlUtil.getNextSeries(paymentJdbcTemplate, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ, null));
          paymentProcess.setPaymentDocumentType(aPPaymentHeader.getDocType());

          paymentProcess.setPmGroupNo(item.getPmGroupNo());
          paymentProcess.setPmGroupDoc(item.getPmGroupDoc());
          if (haveK3OrKX) {
            paymentProcess.setHaveChild(true);
          } else {
            paymentProcess.setHaveChild(false);
          }

          this.paymentProcessService.save(paymentProcess);

          PaymentInformation paymentInformation = new PaymentInformation(item);
          paymentInformation.setId(SqlUtil.getNextSeries(paymentJdbcTemplate, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ, null));
          paymentInformation.setPaymentProcessId(paymentProcess.getId());
          this.paymentInformationService.save(paymentInformation);
          //                    log.info("normal running {}", processSeqNo);
          //                    log.info("normal running {}", informationSeqNo);

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      this.writeLogAsyncService.writeLogPaymentRealRunJob(
          paymentAlias, prepareRunDocumentList, webInfo);

      //            paymentProcessService.updateNextSeries(++processSeqNo);
      //            paymentInformationService.updateNextSeries(++informationSeqNo);

      paymentAlias.setRunEnd(new Timestamp(System.currentTimeMillis()));
      paymentAlias.setRunStatus("S");
      paymentAlias.setRunJobStatus("S");
      paymentAlias.setRunTotalDocument(prepareRunDocumentList.size());
      paymentAlias.setRunSuccessDocument(0);
      paymentAliasService.save(paymentAlias);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Map<String, BigDecimal> createDocumentSpecial(
      PaymentAlias paymentAlias,
      Parameter parameter,
      PrepareProposalDocument prepareProposalDocument,
      String username) {
    Map<String, BigDecimal> totalChild = new HashMap<>();
    BigDecimal amount = BigDecimal.ZERO;
    BigDecimal amountTax = BigDecimal.ZERO;
    try {
      List<PrepareProposalDocument> specialDoc =
          this.prepareProposalDocumentRepository.findDocumentK3OrKX(
              prepareProposalDocument.getOriginalDocumentNo(),
              prepareProposalDocument.getOriginalFiscalYear(),
              prepareProposalDocument.getCompanyCode(),
              username);
//      log.info("before add specialDoc : {}", specialDoc.size());
      if (specialDoc.size() > 0) {

        for (int i = 0; i < specialDoc.size(); i++) {
          amount = amount.add(specialDoc.get(i).getAmount());
          amountTax = amountTax.add(specialDoc.get(i).getWtxAmount());

          String paymentDocNo =
              SqlUtil.generateProposalDocument(
                  prepareProposalDocumentRepository.getNextSeries(true));
          specialDoc.get(i).setPaymentClearingDocNo(paymentDocNo);
          specialDoc.get(i).setPaymentClearingDate(parameter.getPostDate());
          specialDoc.get(i).setPaymentClearingEntryDate(new Timestamp(new Date().getTime()));
          specialDoc.get(i).setPaymentClearingYear(specialDoc.get(i).getOriginalFiscalYear());
          specialDoc.get(i).setPaymentDate(paymentAlias.getPaymentDate());
          specialDoc.get(i).setPaymentDateAcct(paymentAlias.getPaymentDate());
          specialDoc.get(i).setPaymentName(paymentAlias.getPaymentName());
          specialDoc.get(i).setPaymentId(paymentAlias.getId());

          HouseBankPaymentMethod houseBankPaymentMethod =
              Context.sessionHouseBankPaymentMethod.get(specialDoc.get(i).getPaymentMethod());

          specialDoc.get(i).setPayingBankCode(houseBankPaymentMethod.getAccountCode());
          specialDoc.get(i).setPayingHouseBank(houseBankPaymentMethod.getHouseBank());
          specialDoc.get(i).setPayingBankAccountNo(houseBankPaymentMethod.getBankAccountCode());
          specialDoc.get(i).setPayingBankCountry(houseBankPaymentMethod.getCountryCode());
          specialDoc.get(i).setPayingBankNo(houseBankPaymentMethod.getBankAccountCode());
          specialDoc.get(i).setPayingGLAccount(houseBankPaymentMethod.getGlAccount());
          specialDoc.get(i).setPayingBankKey(houseBankPaymentMethod.getBankBranch());
          specialDoc.get(i).setPayingBankName(houseBankPaymentMethod.getHouseBank());
          specialDoc.get(i).setPayingCompCode("99999");
          specialDoc
              .get(i)
              .setAmountPaid(
                  specialDoc.get(i).getAmount().subtract(specialDoc.get(i).getWtxAmount()));

          glHeadService.updateGLHead(
              specialDoc.get(i).getCompanyCode(),
              specialDoc.get(i).getOriginalDocumentNo(),
              specialDoc.get(i).getOriginalFiscalYear(),
              specialDoc.get(i).getPaymentClearingDocNo(),
              specialDoc.get(i).getPaymentId());

          PaymentProcess paymentProcess = new PaymentProcess(specialDoc.get(i), true, true);
          paymentProcess.setId(SqlUtil.getNextSeries(paymentJdbcTemplate, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ, null));
          paymentProcess.setParentCompanyCode(prepareProposalDocument.getCompanyCode());
          paymentProcess.setParentDocumentNo(prepareProposalDocument.getOriginalDocumentNo());
          paymentProcess.setParentFiscalYear(prepareProposalDocument.getOriginalFiscalYear());
          PaymentInformation paymentInformation =
              new PaymentInformation(specialDoc.get(i), true, true);
          paymentInformation.setId(SqlUtil.getNextSeries(paymentJdbcTemplate, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ, null));

          if (null != specialDoc.get(i).getOriginalDocumentNo()) {

            String originalDocumentNo = specialDoc.get(i).getOriginalDocument().substring(0, 10);
            String originalCompanyCode = specialDoc.get(i).getOriginalDocument().substring(10, 15);
            String originalFiscalYear = specialDoc.get(i).getOriginalDocument().substring(15, 19);

            //                        GLHead glHeadOriginal =
            // glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(originalCompanyCode, originalDocumentNo, originalFiscalYear);

            paymentProcess.setOriginalDocumentNo(
                prepareProposalDocument.getRealOriginalDocumentNo());
            paymentProcess.setOriginalCompanyCode(
                prepareProposalDocument.getRealOriginalCompanyCode());
            paymentProcess.setOriginalFiscalYear(
                prepareProposalDocument.getRealOriginalFiscalYear());
            paymentProcess.setOriginalDocumentType(
                prepareProposalDocument.getRealOriginalDocumentType());
            paymentProcess.setOriginalAmount(prepareProposalDocument.getAmount());
            paymentProcess.setOriginalAmountPaid(prepareProposalDocument.getAmountPaid());

            //                    GLLine glLineOriginalTypeK =
            // glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(originalCompanyCode, originalDocumentNo, originalFiscalYear, "K");

            paymentProcess.setOriginalPaymentCenter(prepareProposalDocument.getLinePaymentCenter());
            paymentProcess.setOriginalWtxAmount(prepareProposalDocument.getLineWtxAmount());
            paymentProcess.setOriginalWtxBase(prepareProposalDocument.getLineWtxBase());
            paymentProcess.setOriginalWtxAmountP(prepareProposalDocument.getLineWtxAmountP());
            paymentProcess.setOriginalWtxBaseP(prepareProposalDocument.getLineWtxBaseP());
            paymentProcess.setStatus("S");
          }

          this.paymentProcessService.save(paymentProcess);
          paymentInformation.setPaymentProcessId(paymentProcess.getId());
          this.paymentInformationService.save(paymentInformation);
          //                    seqNo++;
          //                    processSeqNo++;
          //                    informationSeqNo++;

        }
        totalChild.put("amount", amount);
        totalChild.put("amountTax", amountTax);

        return totalChild;
      } else {
        return totalChild;
      }

    } catch (Exception e) {
      e.printStackTrace();
      return totalChild;
    }
  }

//  private PrepareProposalDocument checkErrorCodePrepareProposalDocument(
//      List<BankCode> bankCodes,
//      PrepareProposalDocument prepareProposalDocument,
//      Parameter parameter,
//      CompanyPayingMinimalAmount companyPayingMinimalAmount) {
//
//    prepareProposalDocument.setStatus("S");
//
//    if (null != prepareProposalDocument.getPaymentBlock()
//        && !prepareProposalDocument.getPaymentBlock().equalsIgnoreCase("")
//        && !prepareProposalDocument.getPaymentBlock().equalsIgnoreCase(" ")) {
//      // 003 payment block <> blank รายการถูกระงับในการชำระเงิน
//      String errorCode = "003";
//      prepareProposalDocument.setStatus("E");
//      prepareProposalDocument.setErrorCode(errorCode);
//    } else if (null != prepareProposalDocument.getAmountPaid()
//        && prepareProposalDocument
//                .getAmountPaid()
//                .compareTo(companyPayingMinimalAmount.getMiniMalAmount())
//            < 1) {
//      // 012 amount < config paid min amount
//      // จำนวนเงินที่จะจ่ายยังไม่ถึงจำนวนเงินขั้นต่ำสำหรับการชำระเงิน
//      //            if ((prepareProposalDocument.getVendor().startsWith("8") ||
//      // prepareProposalDocument.getVendor().startsWith("A") ||
//      //                    prepareProposalDocument.getVendor().startsWith("V") ||
//      // prepareProposalDocument.getVendor().startsWith("C")
//      //                    || prepareProposalDocument.getVendor().startsWith("O")) &&
//      // prepareProposalDocument.getPayeeBankKey()) {
//      //
//      //            }
//
//      boolean isGPF =
//          checkGPF(
//              prepareProposalDocument.getCompanyCode(),
//              prepareProposalDocument.getDocumentType(),
//              prepareProposalDocument.getAccountType(),
//              prepareProposalDocument.getPaymentMethod(),
//              prepareProposalDocument.getCostCenter(),
//              prepareProposalDocument.getReference1());
//      String paymentType = checkPaymentType(prepareProposalDocument.getPaymentMethod(), isGPF);
//      String bankKey = prepareProposalDocument.getPayeeBankKey().substring(0, 3);
//      boolean isGiro = checkGiroFormat(bankKey, paymentType);
//      boolean isInhouse =
//          checkInhouseFormat(
//              bankCodes, bankKey, paymentType, prepareProposalDocument.getPaymentMethod());
//      if (!isGiro && !isInhouse) {
//        String errorCode = "012";
//        prepareProposalDocument.setStatus("E");
//        prepareProposalDocument.setErrorCode(errorCode);
//      }
//
//    } else if (null == parameter.getPaymentMethod()) {
//      // 016 payment method == blank วิธีการชำระเงินในการประมวลผลครั้งนี้ไม่ได้ระบุในรายการ
//      String errorCode = "016";
//      prepareProposalDocument.setStatus("E");
//      prepareProposalDocument.setErrorCode(errorCode);
//    } else if (null != prepareProposalDocument.getVendor()
//        && !prepareProposalDocument.getVendor().equalsIgnoreCase("")) {
//
//      if (null != prepareProposalDocument.getVendorActive()
//          && prepareProposalDocument.getVendorActive().equalsIgnoreCase("N")) {
//        // 031 C_BPartner !isActive ข้อมูลผู้ขายได้รับการทำเครื่องหมายแฟลกสำหรับการลบ
//        String errorCode = "031";
//        prepareProposalDocument.setStatus("E");
//        prepareProposalDocument.setErrorCode(errorCode);
//      }
////      else if (null == prepareProposalDocument.getPayeeBankKey()) {
////        // 031 C_BPartner เช็คบัญชี
////        String errorCode = "031";
////        prepareProposalDocument.setStatus("E");
////        prepareProposalDocument.setErrorCode(errorCode);
////      }
//      else if (null != prepareProposalDocument.getVendorStatus()
//          && prepareProposalDocument.getVendorStatus().equalsIgnoreCase("0")) {
//        String errorCode = "033";
//        prepareProposalDocument.setStatus("E");
//        prepareProposalDocument.setErrorCode(errorCode);
//      } else if (null != prepareProposalDocument.getVendorStatus()
//          && prepareProposalDocument.getVendorStatus().equalsIgnoreCase("2")) {
//        String errorCode = "035";
//        prepareProposalDocument.setStatus("E");
//        prepareProposalDocument.setErrorCode(errorCode);
//      }
//
//    } else if (null != prepareProposalDocument.getPayee()
//        && !prepareProposalDocument.getPayee().equalsIgnoreCase("")) {
//      //            if (null == alternatePayee) {
//      //                // 024 TH_APBPAccessControl == null ข้อมูลหลักของผู้รับเงินหายไป
//      //                String errorCode = "024";
//      //                prepareProposalDocument.setStatus("E");
//      //                prepareProposalDocument.setErrorCode(errorCode);
//      //            }
//      // 034 C_BP_Relation TH_APAlternatePayeeStatus.ConfirmStatus == 0 ข้อมูลผู้รับเงินมีสถานะ
//      // "รอการยืนยัน"
//      // 036 C_BP_Relation TH_APAlternatePayeeStatus.ConfirmStatus == 2 ข้อมูลผู้รับเงินมีสถานะ
//      // "ไม่ยืนยัน"
//      if (null != prepareProposalDocument.getPayeeStatus()
//          && prepareProposalDocument.getPayeeStatus().equalsIgnoreCase("2")) {
//        String errorCode = "034";
//        prepareProposalDocument.setStatus("E");
//        prepareProposalDocument.setErrorCode(errorCode);
//      } else if (null != prepareProposalDocument.getPayeeStatus()
//          && prepareProposalDocument.getPayeeStatus().equalsIgnoreCase("2")) {
//        String errorCode = "036";
//        prepareProposalDocument.setStatus("E");
//        prepareProposalDocument.setErrorCode(errorCode);
//      }
//    } else if (prepareProposalDocument.getDrCr().equalsIgnoreCase("D")
//        || prepareProposalDocument.getAmountPaid().compareTo(BigDecimal.ZERO) == 0) {
//      // 060 DrCr = D || paid amount <= 0 การถูกระงับเนื่องจากยอดคงเหลือทางด้านเดบิท
//      String errorCode = "060";
//      prepareProposalDocument.setStatus("E");
//      prepareProposalDocument.setErrorCode(errorCode);
//    } else if (null != prepareProposalDocument.getBankAccountNo()
//        && !prepareProposalDocument.getBankAccountNo().equalsIgnoreCase("")) {
//      VendorBankAccount vendorBankAccount =
//          vendorBankAccountService.findOneByBankAccountNoAndVendor(
//              prepareProposalDocument.getBankAccountNo(),
//              null != prepareProposalDocument.getPayee()
//                      && !prepareProposalDocument.getPayee().equalsIgnoreCase("")
//                  ? prepareProposalDocument.getPayee()
//                  : prepareProposalDocument.getVendor());
//      if (!vendorBankAccount.isActive()) {
//        // 071 bankAccountNo != C_BPartner.BankNo ไม่พบธนาคารคุ่ค้าที่ถูกต้อง
//        String errorCode = "071";
//        prepareProposalDocument.setStatus("E");
//        prepareProposalDocument.setErrorCode(errorCode);
//      }
//    }
//
//    //             016 payment method == blank
//    // วิธีการชำระเงินในการประมวลผลครั้งนี้ไม่ได้ระบุในรายการ
//    //             024 TH_APBPAccessControl == null ข้อมูลหลักของผู้รับเงินหายไป
//    //        if (null != prepareProposalDocument.getPayee() &&
//    // !prepareProposalDocument.getPayee().equalsIgnoreCase("")) {
//    //            Vendor alternatePayee =
//    // vendorService.findOneAlternativePayee(prepareProposalDocument.getPayee());
//    //            if (null == alternatePayee) {
//    //                String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                        ? prepareProposalDocument.getErrorCode() + ",024" : "024";
//    //                prepareProposalDocument.setStatus("E");
//    //                prepareProposalDocument.setErrorCode(errorCode);
//    //            } else {
//    //                // 034 C_BP_Relation TH_APAlternatePayeeStatus.ConfirmStatus == 0
//    // ข้อมูลผู้รับเงินมีสถานะ "รอการยืนยัน"
//    //                // 036 C_BP_Relation TH_APAlternatePayeeStatus.ConfirmStatus == 2
//    // ข้อมูลผู้รับเงินมีสถานะ "ไม่ยืนยัน"
//    //                log.info("alternatePayee.getVendorStatus() : {}",
//    // alternatePayee.getVendorStatus());
//    //                if (null != alternatePayee.getVendorStatus() &&
//    // alternatePayee.getVendorStatus().equalsIgnoreCase("0")) {
//    //                    String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                            ? prepareProposalDocument.getErrorCode() + ",034" : "034";
//    //                    prepareProposalDocument.setStatus("E");
//    //                    prepareProposalDocument.setErrorCode(errorCode);
//    //                } else if (null != alternatePayee.getVendorStatus() &&
//    // alternatePayee.getVendorStatus().equalsIgnoreCase("2")) {
//    //                    String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                            ? prepareProposalDocument.getErrorCode() + ",036" : "036";
//    //                    prepareProposalDocument.setStatus("E");
//    //                    prepareProposalDocument.setErrorCode(errorCode);
//    //                }
//    //            }
//    //        }
//    //        Vendor vendor =
//    // vendorService.findOneByValueCodeAndCompCode(prepareProposalDocument.getVendor(),
//    // prepareProposalDocument.getCompanyCode());
//    ////        vendors.stream().filter(v ->
//    // prepareProposalDocument.getVendor().equalsIgnoreCase(v.getValueCode()) &&
//    // prepareProposalDocument.getCompCode().equalsIgnoreCase(v.getCompCode())).findAny().orElse(null);
//    //        if (null != vendor) {
//    //            // 031 C_BPartner !isActive ข้อมูลผู้ขายได้รับการทำเครื่องหมายแฟลกสำหรับการลบ
//    //            if (!vendor.isActive()) {
//    //                String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                        ? prepareProposalDocument.getErrorCode() + ",031" : "031";
//    //                prepareProposalDocument.setStatus("E");
//    //                prepareProposalDocument.setErrorCode(errorCode);
//    //            }
//    //
//    //            // 033 TH_APBPartnerStatus.ConfirmStatus == 0 ข้อมูลผู้ขายมีสถานะ "รอการยืนยัน"
//    //            // 035 TH_APBPartnerStatus.ConfirmStatus == 2 ข้อมูลผู้ขายมีสถานะ "ไม่ยืนยัน"
//    //            if (vendor.getVendorStatus().equalsIgnoreCase("0")) {
//    //                String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                        ? prepareProposalDocument.getErrorCode() + ",033" : "033";
//    //                prepareProposalDocument.setStatus("E");
//    //                prepareProposalDocument.setErrorCode(errorCode);
//    //            } else if (vendor.getVendorStatus().equalsIgnoreCase("2")) {
//    //                String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                        ? prepareProposalDocument.getErrorCode() + ",035" : "035";
//    //                prepareProposalDocument.setStatus("E");
//    //                prepareProposalDocument.setErrorCode(errorCode);
//    //            }
//    //        }
//    //
//    //        // 060 DrCr = D || paid amount <= 0 การถูกระงับเนื่องจากยอดคงเหลือทางด้านเดบิท
//    //        if (prepareProposalDocument.getDrCr().equalsIgnoreCase("D") ||
//    // prepareProposalDocument.getAmountPaid().compareTo(BigDecimal.ZERO) == 0) {
//    //            String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                    ? prepareProposalDocument.getErrorCode() + ",060" : "060";
//    //            prepareProposalDocument.setStatus("E");
//    //            prepareProposalDocument.setErrorCode(errorCode);
//    //        }
//    //        // 071 bankAccountNo != C_BPartner.BankNo ไม่พบธนาคารคุ่ค้าที่ถูกต้อง
//    //        VendorBankAccount vendorBankAccount =
//    // vendorBankAccountService.findOneByBankAccountNoAndVendor(prepareProposalDocument.getBankAccountNo(), prepareProposalDocument.getVendor());
//    ////    vendorBankAccounts.stream().filter(v ->
//    // prepareProposalDocument.getBankAccountNo().equalsIgnoreCase(v.getBankAccountNo()) &&
//    // prepareProposalDocument.getVendor().equalsIgnoreCase(v.getVendor())).findAny().orElse(null);
//    //        if (null == vendorBankAccount) {
//    //            String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                    ? prepareProposalDocument.getErrorCode() + ",071" : "071";
//    //            prepareProposalDocument.setStatus("E");
//    //            prepareProposalDocument.setErrorCode(errorCode);
//    //        }
//    //
//    //        PaymentMethod paymentMethod =
//    // paymentMethodService.findOneByValueCode(prepareProposalDocument.getPaymentMethod());
//    //        if (null == paymentMethod) {
//    //            String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                    ? prepareProposalDocument.getErrorCode() + ",012" : "012";
//    //            prepareProposalDocument.setStatus("E");
//    //            prepareProposalDocument.setErrorCode(errorCode);
//    //        }
//    //
//    //        if (null == vendor) {
//    //            String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                    ? prepareProposalDocument.getErrorCode() + ",002" : "002";
//    //            prepareProposalDocument.setStatus("E");
//    //            prepareProposalDocument.setErrorCode(errorCode);
//    //
//    //
//    //        } else {
//    //            if (!vendor.isActive()) {
//    //                String errorCode = null != prepareProposalDocument.getErrorCode() &&
//    // prepareProposalDocument.getErrorCode().length() > 0
//    //                        ? prepareProposalDocument.getErrorCode() + ",003" : "003";
//    //                prepareProposalDocument.setStatus("E");
//    //                prepareProposalDocument.setErrorCode(errorCode);
//    //            }
//    //        }
//
//    return prepareProposalDocument;
//  }

  private String checkPaymentType(String paymentMethod, boolean isGPF) {
    if ("A".equalsIgnoreCase(paymentMethod)
        || "M".equalsIgnoreCase(paymentMethod)
        || "P".equalsIgnoreCase(paymentMethod)) {
      return "D1";
    } else if ("B".equalsIgnoreCase(paymentMethod)
        || ("J".equalsIgnoreCase(paymentMethod) && isGPF)) {
      return "D2";
    } else if ("1".equalsIgnoreCase(paymentMethod)
        || "3".equalsIgnoreCase(paymentMethod)
        || "6".equalsIgnoreCase(paymentMethod)
        || "8".equalsIgnoreCase(paymentMethod)
        || "E".equalsIgnoreCase(paymentMethod)
        || "G".equalsIgnoreCase(paymentMethod)
        || "H".equalsIgnoreCase(paymentMethod)
        || "K".equalsIgnoreCase(paymentMethod)) {
      return "D3";
    } else if ("F".equalsIgnoreCase(paymentMethod)) {
      return "D4";
    } else if ("2".equalsIgnoreCase(paymentMethod)
        || "4".equalsIgnoreCase(paymentMethod)
        || "7".equalsIgnoreCase(paymentMethod)
        || "9".equalsIgnoreCase(paymentMethod)
        || "I".equalsIgnoreCase(paymentMethod)
        || "L".equalsIgnoreCase(paymentMethod)
        || ("J".equalsIgnoreCase(paymentMethod) && !isGPF)) {
      return "D5";
    }
    return "";
  }

  private boolean checkGPF(
      String compCode,
      String docType,
      String accountType,
      String paymentMethod,
      String costCenter,
      String paymentReference) {
    return "03004".equalsIgnoreCase(compCode)
        && "KH".equalsIgnoreCase(docType)
        && "K".equalsIgnoreCase(accountType)
        && "J".equalsIgnoreCase(paymentMethod)
        && "03004A0001x".equalsIgnoreCase(costCenter)
        && paymentReference.endsWith("0010041052");
  }

  private boolean checkGiroFormat(String bankKey, String paymentType) {
    return bankKey.startsWith("006") && !"D3".equalsIgnoreCase(paymentType);
  }

  private boolean checkInhouseFormat(
      List<BankCode> bankCodes, String bankKey, String paymentType, String paymentMethod) {
    log.info("paymentMethod : {}", paymentMethod);
    return bankCodes.stream()
        .anyMatch(
            bankCode ->
                bankKey.equalsIgnoreCase(bankCode.getBankKey())
                    && paymentType.equalsIgnoreCase(bankCode.getPayAccount())
                    && bankCode.isInHouse()
                    && !direct.contains(paymentMethod));
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
  public void createProposalNew(PaymentAlias paymentAlias, WSWebInfo webInfo) {
    try {

//      PaymentAlias paymentAlias = paymentAliasService.findOneById(id);
      ParameterConfig parameterConfig =
          JSONUtil.convertJsonToObject(paymentAlias.getJsonText(), ParameterConfig.class);
      List<PrepareSelectProposalDocument> prepareSelectProposalDocumentList = null;
      Date runDate = new Date();
      synchronized (this) {
        long startTimeMillis = System.currentTimeMillis();
        prepareSelectProposalDocumentList =
            prepareProposalDocumentRepository.findUnBlockDocumentCanPayByParameterNew(
                parameterConfig, webInfo.getUserWebOnline(), runDate);
        log.info(
            "prepareSelectProposalDocumentList create : {}",
            prepareSelectProposalDocumentList.size());
        long endTimeMillis = System.currentTimeMillis();
        long executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("เวลาการทำงานค้นหา: " + executionTimeSeconds + " วินาที");

        startTimeMillis = System.currentTimeMillis();
        prepareProposalDocumentRepository.newLockProposalDocument(prepareSelectProposalDocumentList, paymentAlias.getId());
//        prepareProposalDocumentRepository.lockProposalDocument(
//            parameterConfig, paymentAlias.getId(), webInfo.getUserWebOnline(), runDate);
        endTimeMillis = System.currentTimeMillis();
        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("เวลาการทำงานอัพเดท: " + executionTimeSeconds + " วินาที");
      }

      CompanyPayingMinimalAmount companyPayingMinimalAmount =
          companyPayingMinimalAmountService.findOneByValueCode("99999");
      Parameter parameter = parameterConfig.getParameter();
//      List<GLHead> glHeads = new ArrayList<>();
      List<PaymentProcess> paymentProcesses = new ArrayList<>();
      List<PaymentInformation> paymentInformations = new ArrayList<>();
      boolean haveK3OrKX = false;
      BigDecimal amountChild = BigDecimal.ZERO;
      List<BankCode> bankCodes = bankCodeService.findAllBank(false);
      long startTimeMillis = System.currentTimeMillis();
//      int parallelism = 3;
//      ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);
//      List<PrepareSelectProposalDocument> finalPrepareSelectProposalDocumentList = prepareSelectProposalDocumentList;
//      PaymentResult result = forkJoinPool.submit(() ->
//              finalPrepareSelectProposalDocumentList.parallelStream()
//                      .map(prepareSelectProposalDocument -> {
//                        Map<String, BigDecimal> listMap = new HashMap<>();
//                        amountChild[0] = BigDecimal.ZERO;
//                        prepareSelectProposalDocument.setPaymentClearingDate(parameter.getPostDate());
//                        prepareSelectProposalDocument.setPaymentClearingEntryDate(
//                                new Timestamp(new Date().getTime()));
//                        prepareSelectProposalDocument.setPaymentClearingYear(
//                                prepareSelectProposalDocument.getOriginalFiscalYear());
//                        prepareSelectProposalDocument.setPaymentDate(paymentAlias.getPaymentDate());
//                        prepareSelectProposalDocument.setPaymentDateAcct(paymentAlias.getPaymentDate());
//                        prepareSelectProposalDocument.setPaymentName(paymentAlias.getPaymentName());
//                        prepareSelectProposalDocument.setPaymentId(paymentAlias.getId());
//                        HouseBankPaymentMethod houseBankPaymentMethod =
//                                null == Context.sessionHouseBankPaymentMethod.get(
//                                        prepareSelectProposalDocument.getPaymentMethod()) ? new HouseBankPaymentMethod() : Context.sessionHouseBankPaymentMethod.get(
//                                        prepareSelectProposalDocument.getPaymentMethod());
//                        prepareSelectProposalDocument.setPayingBankCode(houseBankPaymentMethod.getAccountCode());
//                        prepareSelectProposalDocument.setPayingHouseBank(houseBankPaymentMethod.getHouseBank());
//                        prepareSelectProposalDocument.setPayingBankAccountNo(
//                                houseBankPaymentMethod.getBankAccountCode());
//                        prepareSelectProposalDocument.setPayingBankCountry(houseBankPaymentMethod.getCountryCode());
//                        prepareSelectProposalDocument.setPayingBankNo(houseBankPaymentMethod.getBankAccountCode());
//                        prepareSelectProposalDocument.setPayingGLAccount(houseBankPaymentMethod.getGlAccount());
//                        prepareSelectProposalDocument.setPayingBankKey(houseBankPaymentMethod.getBankBranch());
//                        prepareSelectProposalDocument.setPayingBankName(houseBankPaymentMethod.getHouseBank());
//                        prepareSelectProposalDocument.setPayingCompCode("99999");
//                        prepareSelectProposalDocument.setAmountPaid(
//                                prepareSelectProposalDocument
//                                        .getAmount()
//                                        .subtract(prepareSelectProposalDocument.getWtxAmount()));
//
//                        this.checkErrorCodePrepareProposalDocumentNew(
//                                bankCodes, prepareSelectProposalDocument, parameter, companyPayingMinimalAmount);
//
////                        if (null == prepareSelectProposalDocument.getErrorCode()
////                                || (null != prepareSelectProposalDocument.getErrorCode()
////                                && !prepareSelectProposalDocument.getErrorCode().equalsIgnoreCase("E"))) {
////                          String paymentDocNo =
////                                  SqlUtil.generateProposalDocument(
////                                          prepareProposalDocumentRepository.getNextSeries(true));
////                          prepareSelectProposalDocument.setPaymentClearingDocNo(paymentDocNo);
////                        }
//
//                        if (prepareSelectProposalDocument.getDocumentType().equalsIgnoreCase("KA")
//                                || prepareSelectProposalDocument.getDocumentType().equalsIgnoreCase("KB")
//                                || prepareSelectProposalDocument.getDocumentType().equalsIgnoreCase("KG")
//                                || prepareSelectProposalDocument.getDocumentType().equalsIgnoreCase("KC")) {
//                          listMap =
//                                  this.createDocumentSpecialNew(
//                                          paymentAlias,
//                                          parameter,
//                                          prepareSelectProposalDocument,
//                                          webInfo.getUserWebOnline(),
//                                          paymentProcesses,
//                                          paymentInformations);
//                        }
//                        PaymentProcess paymentProcess =
//                                new PaymentProcess(prepareSelectProposalDocument, true, false);
////                        paymentProcess.setId(paymentProcessService.getNextSeries());
//                        paymentProcess.setPmGroupNo(
//                                prepareSelectProposalDocument.getPaymentName()
//                                        + "-"
//                                        + prepareSelectProposalDocument.getPaymentDate().getTime());
//                        paymentProcess.setPmGroupDoc(
//                                prepareSelectProposalDocument.getCompanyCode()
//                                        + "-"
//                                        + prepareSelectProposalDocument.getOriginalDocumentNo()
//                                        + "-"
//                                        + prepareSelectProposalDocument.getOriginalFiscalYear()
//                                        + "-"
//                                        + prepareSelectProposalDocument.getPaymentName()
//                                        + "-"
//                                        + prepareSelectProposalDocument.getPaymentDate().getTime());
//                        paymentProcess.setProposalBlock(false);
//                        paymentProcess.setPaymentCompanyCode("99999");
//                        if (listMap.get("amount") != null && listMap.get("amount").compareTo(BigDecimal.ZERO) > 0) {
//                          paymentProcess.setHaveChild(true);
//                          paymentProcess.setCreditMemo(listMap.get("amount"));
//                          paymentProcess.setWtxCreditMemo(listMap.get("amountTax"));
//                          paymentProcess.setInvoiceAmountPaid(
//                                  prepareSelectProposalDocument.getAmountPaid().subtract(amountChild[0]));
//                        } else {
//                          paymentProcess.setHaveChild(false);
//                          paymentProcess.setCreditMemo(BigDecimal.ZERO);
//                          paymentProcess.setWtxCreditMemo(BigDecimal.ZERO);
//                        }
//
//                        if (null != prepareSelectProposalDocument.getOriginalDocument()) {
//                          paymentProcess.setOriginalDocumentNo(
//                                  prepareSelectProposalDocument.getRealOriginalDocumentNo());
//                          paymentProcess.setOriginalCompanyCode(
//                                  prepareSelectProposalDocument.getRealOriginalCompanyCode());
//                          paymentProcess.setOriginalFiscalYear(
//                                  prepareSelectProposalDocument.getRealOriginalFiscalYear());
//                          paymentProcess.setOriginalDocumentType(
//                                  prepareSelectProposalDocument.getRealOriginalDocumentType());
//                          paymentProcess.setOriginalAmount(prepareSelectProposalDocument.getAmount());
//                          paymentProcess.setOriginalAmountPaid(prepareSelectProposalDocument.getAmountPaid());
//                          paymentProcess.setOriginalPaymentCenter(
//                                  prepareSelectProposalDocument.getLinePaymentCenter());
//                          paymentProcess.setOriginalWtxAmount(prepareSelectProposalDocument.getLineWtxAmount());
//                          paymentProcess.setOriginalWtxBase(prepareSelectProposalDocument.getLineWtxBase());
//                          paymentProcess.setOriginalWtxAmountP(prepareSelectProposalDocument.getLineWtxAmountP());
//                          paymentProcess.setOriginalWtxBaseP(prepareSelectProposalDocument.getLineWtxBaseP());
//                        }
//                        PaymentInformation paymentInformation =
//                                new PaymentInformation(prepareSelectProposalDocument, true, false);
////                        paymentInformation.setId(paymentInformationService.getNextSeries());
////                        paymentInformation.setPaymentProcessId(paymentProcess.getId());
//                        return new PaymentResult(
//                                Collections.singletonList(paymentProcess),
//                                Collections.singletonList(paymentInformation)
//                        );
//                      })
//                      .collect(Collectors.collectingAndThen(
//                              Collectors.toList(),
//                              list -> {
//                                List<PaymentProcess> processes = list.stream()
//                                        .flatMap(r -> r.getPaymentProcesses().stream())
//                                        .collect(Collectors.toList());
//
//                                List<PaymentInformation> informations = list.stream()
//                                        .flatMap(r -> r.getPaymentInformations().stream())
//                                        .collect(Collectors.toList());
//
//                                return new PaymentResult(processes, informations);
//                              }
//                      ))
//      ).join();
//
//      synchronized (this) {
//        paymentProcesses.addAll(result.getPaymentProcesses());
//        paymentInformations.addAll(result.getPaymentInformations());
//
//        long paymentProcessesSize = paymentProcesses.size();
//        long paymentProcessId = SqlUtil.getNextSeries(
//            paymentJdbcTemplate, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ);
//        SqlUtil.updateNextSeries(
//            paymentJdbcTemplate, paymentProcessId + paymentProcessesSize, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ);
//
//        long paymentInformationsSize = paymentInformations.size();
//        long paymentInformationsId = SqlUtil.getNextSeries(
//            paymentJdbcTemplate, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ);
//        SqlUtil.updateNextSeries(
//            paymentJdbcTemplate, paymentInformationsId + paymentInformationsSize, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ);
//
//        long paymentSuccessSize = paymentProcesses.size();
//        long paymentDocumentSeq = SqlUtil.getNextSeries(
//            paymentJdbcTemplate, "PROPOSAL_DOCUMENT_SEQ");
//        SqlUtil.updateNextSeries(
//            paymentJdbcTemplate, paymentSuccessSize + paymentDocumentSeq, "PROPOSAL_DOCUMENT_SEQ");
//
//        for (var ref = new Object() {
//          int i = 0;
//        }; ref.i < paymentProcessesSize; ref.i++) {
//          Long processId = paymentProcessId++;
//          String paymentDocNo = SqlUtil.generateProposalDocument(paymentDocumentSeq++);
//          paymentProcesses.get(ref.i).setId(processId);
//          paymentProcesses.get(ref.i).setPaymentDocumentNo(paymentDocNo);
//          paymentInformations.get(ref.i).setPaymentProcessId(processId);
//          paymentInformations.get(ref.i).setId(paymentInformationsId++);
//          finalPrepareSelectProposalDocumentList.stream().filter(p -> p.getCompanyCode().equalsIgnoreCase(paymentProcesses.get(ref.i).getOriginalCompanyCode())
//              && p.getOriginalDocumentNo().equalsIgnoreCase(paymentProcesses.get(ref.i).getOriginalDocumentNo())
//              && p.getOriginalFiscalYear().equalsIgnoreCase(paymentProcesses.get(ref.i).getInvoiceFiscalYear())).findFirst().orElseGet(PrepareSelectProposalDocument::new).setPaymentClearingDocNo(paymentDocNo);
//        }
//      }


      for (PrepareSelectProposalDocument prepareSelectProposalDocument :
          prepareSelectProposalDocumentList) {
        Map<String, BigDecimal> listMap = new HashMap<>();
        amountChild = BigDecimal.ZERO;
        prepareSelectProposalDocument.setPaymentClearingDate(parameter.getPostDate());
        prepareSelectProposalDocument.setPaymentClearingEntryDate(
            new Timestamp(new Date().getTime()));
        prepareSelectProposalDocument.setPaymentClearingYear(
            prepareSelectProposalDocument.getOriginalFiscalYear());
        prepareSelectProposalDocument.setPaymentDate(paymentAlias.getPaymentDate());
        prepareSelectProposalDocument.setPaymentDateAcct(paymentAlias.getPaymentDate());
        prepareSelectProposalDocument.setPaymentName(paymentAlias.getPaymentName());
        prepareSelectProposalDocument.setPaymentId(paymentAlias.getId());
        HouseBankPaymentMethod houseBankPaymentMethod =
            null == Context.sessionHouseBankPaymentMethod.get(
                prepareSelectProposalDocument.getPaymentMethod()) ? new HouseBankPaymentMethod() : Context.sessionHouseBankPaymentMethod.get(
                prepareSelectProposalDocument.getPaymentMethod());
        prepareSelectProposalDocument.setPayingBankCode(houseBankPaymentMethod.getAccountCode());
        prepareSelectProposalDocument.setPayingHouseBank(houseBankPaymentMethod.getHouseBank());
        prepareSelectProposalDocument.setPayingBankAccountNo(
            houseBankPaymentMethod.getBankAccountCode());
        prepareSelectProposalDocument.setPayingBankCountry(houseBankPaymentMethod.getCountryCode());
        prepareSelectProposalDocument.setPayingBankNo(houseBankPaymentMethod.getBankAccountCode());
        prepareSelectProposalDocument.setPayingGLAccount(houseBankPaymentMethod.getGlAccount());
        prepareSelectProposalDocument.setPayingBankKey(houseBankPaymentMethod.getBankBranch());
        prepareSelectProposalDocument.setPayingBankName(houseBankPaymentMethod.getHouseBank());
        prepareSelectProposalDocument.setPayingCompCode("99999");
        prepareSelectProposalDocument.setAmountPaid(
            prepareSelectProposalDocument
                .getAmount()
                .subtract(prepareSelectProposalDocument.getWtxAmount()));

        this.checkErrorCodePrepareProposalDocumentNew(
            bankCodes, prepareSelectProposalDocument, parameter, companyPayingMinimalAmount);

        if (null == prepareSelectProposalDocument.getErrorCode() || !prepareSelectProposalDocument.getErrorCode().equalsIgnoreCase("E")) {
//          String paymentDocNo =
//              SqlUtil.generateProposalDocument(
//                  prepareProposalDocumentRepository.getNextSeries(true));
//          prepareSelectProposalDocument.setPaymentClearingDocNo(paymentDocNo);
        }

        if (prepareSelectProposalDocument.getDocumentType().equalsIgnoreCase("KA")
            || prepareSelectProposalDocument.getDocumentType().equalsIgnoreCase("KB")
            || prepareSelectProposalDocument.getDocumentType().equalsIgnoreCase("KG")
            || prepareSelectProposalDocument.getDocumentType().equalsIgnoreCase("KC")
            || prepareSelectProposalDocument.getDocumentType().equalsIgnoreCase("KH")) {
          boolean checkChild = false;
          try {
            Long.parseLong(prepareSelectProposalDocument.getHeaderReference());
            checkChild = true;
          } catch (Exception e) {
            checkChild = false;
          }
          listMap =
              this.createDocumentSpecialNew(
                  paymentAlias,
                  parameter,
                  prepareSelectProposalDocument,
                  webInfo.getUserWebOnline(),
                  paymentProcesses,
                  paymentInformations);
//          listMap =
//              this.createDocumentSpecialNew(
//                  paymentAlias,
//                  parameter,
//                  prepareSelectProposalDocument,
//                  webInfo.getUserWebOnline());
        }
        PaymentProcess paymentProcess =
            new PaymentProcess(prepareSelectProposalDocument, true, false);
//        paymentProcess.setId(paymentProcessService.getNextSeries());
        paymentProcess.setPmGroupNo(
            prepareSelectProposalDocument.getPaymentName()
                + "-"
                + prepareSelectProposalDocument.getPaymentDate().getTime());
        paymentProcess.setPmGroupDoc(
            prepareSelectProposalDocument.getCompanyCode()
                + "-"
                + prepareSelectProposalDocument.getOriginalDocumentNo()
                + "-"
                + prepareSelectProposalDocument.getOriginalFiscalYear()
                + "-"
                + prepareSelectProposalDocument.getPaymentName()
                + "-"
                + prepareSelectProposalDocument.getPaymentDate().getTime());
        paymentProcess.setProposalBlock(false);
        paymentProcess.setPaymentCompanyCode("99999");
        if (listMap.get("amount") != null && listMap.get("amount").compareTo(BigDecimal.ZERO) > 0) {
          paymentProcess.setHaveChild(true);
          paymentProcess.setCreditMemo(listMap.get("amount"));
          paymentProcess.setWtxCreditMemo(listMap.get("amountTax"));
          paymentProcess.setInvoiceAmountPaid(
              prepareSelectProposalDocument.getAmountPaid().subtract(amountChild));
        } else {
          paymentProcess.setHaveChild(false);
          paymentProcess.setCreditMemo(BigDecimal.ZERO);
          paymentProcess.setWtxCreditMemo(BigDecimal.ZERO);
        }

        if (null != prepareSelectProposalDocument.getOriginalDocument()) {
          paymentProcess.setOriginalDocumentNo(
              prepareSelectProposalDocument.getRealOriginalDocumentNo());
          paymentProcess.setOriginalCompanyCode(
              prepareSelectProposalDocument.getRealOriginalCompanyCode());
          paymentProcess.setOriginalFiscalYear(
              prepareSelectProposalDocument.getRealOriginalFiscalYear());
          paymentProcess.setOriginalDocumentType(
              prepareSelectProposalDocument.getRealOriginalDocumentType());
          paymentProcess.setOriginalAmount(prepareSelectProposalDocument.getAmount());
          paymentProcess.setOriginalAmountPaid(prepareSelectProposalDocument.getAmountPaid());
          paymentProcess.setOriginalPaymentCenter(
              prepareSelectProposalDocument.getLinePaymentCenter());
          paymentProcess.setOriginalWtxAmount(prepareSelectProposalDocument.getLineWtxAmount());
          paymentProcess.setOriginalWtxBase(prepareSelectProposalDocument.getLineWtxBase());
          paymentProcess.setOriginalWtxAmountP(prepareSelectProposalDocument.getLineWtxAmountP());
          paymentProcess.setOriginalWtxBaseP(prepareSelectProposalDocument.getLineWtxBaseP());
        }
        paymentProcesses.add(paymentProcess);
        PaymentInformation paymentInformation =
            new PaymentInformation(prepareSelectProposalDocument, true, false);
//        paymentInformation.setId(paymentInformationService.getNextSeries());
//        paymentInformation.setPaymentProcessId(paymentProcess.getId());
        paymentInformations.add(paymentInformation);
      }
      long endTimeMillis = System.currentTimeMillis();
      long executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
      log.info("เวลาการทำงานเตรียมข้อมูล: " + executionTimeSeconds + " วินาที");

      synchronized (this) {
        startTimeMillis = System.currentTimeMillis();
        long paymentProcessesSize = paymentProcesses.size();
        long paymentProcessId = SqlUtil.getNextSeries(
            paymentJdbcTemplate, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ, paymentProcessesSize);
//        SqlUtil.updateNextSeries(
//            paymentJdbcTemplate, paymentProcessId + paymentProcessesSize, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ);
        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("สร้าง seq process: " + executionTimeSeconds + " วินาที");

        startTimeMillis = System.currentTimeMillis();
        long paymentInformationsSize = paymentInformations.size();
        long paymentInformationsId = SqlUtil.getNextSeries(
            paymentJdbcTemplate, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ, paymentInformationsSize);
//        SqlUtil.updateNextSeries(
//            paymentJdbcTemplate, paymentInformationsId + paymentInformationsSize, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ);
        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("สร้าง seq information: " + executionTimeSeconds + " วินาที");

        startTimeMillis = System.currentTimeMillis();
        long paymentSuccessSize = paymentProcesses.size();
        long paymentDocumentSeq = SqlUtil.getNextSeries(
            paymentJdbcTemplate, "PROPOSAL_DOCUMENT_SEQ", paymentSuccessSize);
//        SqlUtil.updateNextSeries(
//            paymentJdbcTemplate, paymentSuccessSize + paymentDocumentSeq, "PROPOSAL_DOCUMENT_SEQ");
        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("สร้าง seq doc: " + executionTimeSeconds + " วินาที");

        startTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < paymentProcessesSize; i++) {
          Long processId = paymentProcessId;
          String paymentDocNo = SqlUtil.generateProposalDocument(paymentDocumentSeq);
          paymentProcesses.get(i).setId(processId);
          paymentProcesses.get(i).setPaymentDocumentNo(paymentDocNo);
          paymentInformations.get(i).setPaymentProcessId(processId);
          paymentInformations.get(i).setId(paymentInformationsId);
          int finalI = i;
          prepareSelectProposalDocumentList.stream().filter(p -> p.getCompanyCode().equalsIgnoreCase(paymentProcesses.get(finalI).getOriginalCompanyCode())
              && p.getOriginalDocumentNo().equalsIgnoreCase(paymentProcesses.get(finalI).getOriginalDocumentNo())
              && p.getOriginalFiscalYear().equalsIgnoreCase(paymentProcesses.get(finalI).getInvoiceFiscalYear())).findFirst().orElseGet(PrepareSelectProposalDocument::new).setPaymentClearingDocNo(paymentDocNo);
          paymentProcessId++;
          paymentInformationsId++;
          paymentDocumentSeq++;
        }
        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("สร้าง id: " + executionTimeSeconds + " วินาที");
      }
      try {
//        startTimeMillis = System.currentTimeMillis();
//        this.glHeadService.updateGLHeadBatch(glHeads);
//        endTimeMillis = System.currentTimeMillis();
//        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
//        log.info("เวลาการทำงานอัพเดท gl: " + executionTimeSeconds + " วินาที");
        startTimeMillis = System.currentTimeMillis();
        this.paymentProcessService.saveBatch(paymentProcesses);
        endTimeMillis = System.currentTimeMillis();
        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("เวลาการทำงานอัพเดท process: " + executionTimeSeconds + " วินาที");
        startTimeMillis = System.currentTimeMillis();
        this.paymentInformationService.saveBatch(paymentInformations);
        endTimeMillis = System.currentTimeMillis();
        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("เวลาการทำงานอัพเดท information: " + executionTimeSeconds + " วินาที");
      } catch (Exception e) {
        e.printStackTrace();
      }
      startTimeMillis = System.currentTimeMillis();
      synchronized (this) {
        this.writeLogAsyncService.writeLogPrepareProposalDocumentNew(paymentAlias,
            parameterConfig, prepareSelectProposalDocumentList, webInfo);
      }
      endTimeMillis = System.currentTimeMillis();
      executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
      log.info("เวลาการทำงานอัพเดท log: " + executionTimeSeconds + " วินาที");

      startTimeMillis = System.currentTimeMillis();
      List<PrepareSelectProposalDocument> paymentError =
          prepareSelectProposalDocumentList.stream()
              .filter(p -> "E".equalsIgnoreCase(p.getStatus()))
              .collect(toList());

      int listError = paymentError.size();

      paymentAlias.setProposalStatus("S");

      List<PaymentProcess> paymentProcessList = paymentProcessService.findAllByPaymentIdAndProposalNotChild(paymentAlias.getId(), true);

//      List<PaymentProcess> success = paymentProcessList.stream().filter(object -> "S".equalsIgnoreCase(object.getIdemStatus())).collect(Collectors.toList());
//      List<PaymentProcess> repair = paymentProcessList.stream().filter(object -> "N".equalsIgnoreCase(object.getIdemStatus())).collect(Collectors.toList());
//      List<PaymentProcess> error = paymentProcessList.stream().filter(object -> "E".equalsIgnoreCase(object.getIdemStatus())).collect(Collectors.toList());
//
      paymentAlias.setProposalSuccessDocument(paymentProcessList.size() - listError);


      paymentAlias.setProposalTotalDocument(paymentProcessList.size());
      paymentAlias.setProposalEnd(new Timestamp(System.currentTimeMillis()));
      paymentAliasService.save(paymentAlias);
      endTimeMillis = System.currentTimeMillis();
      executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
      log.info("เวลาการทำงานอัพเดท alias: " + executionTimeSeconds + " วินาที");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private PrepareSelectProposalDocument checkErrorCodePrepareProposalDocumentNew(
      List<BankCode> bankCodes,
      PrepareSelectProposalDocument prepareSelectProposalDocument,
      Parameter parameter,
      CompanyPayingMinimalAmount companyPayingMinimalAmount) {

    prepareSelectProposalDocument.setStatus("S");

    if (null != prepareSelectProposalDocument.getPaymentBlock()
        && !prepareSelectProposalDocument.getPaymentBlock().equalsIgnoreCase("")
        && !prepareSelectProposalDocument.getPaymentBlock().equalsIgnoreCase(" ")) {
      String errorCode = "003";
      prepareSelectProposalDocument.setStatus("E");
      prepareSelectProposalDocument.setErrorCode(errorCode);
    } else if (null != prepareSelectProposalDocument.getAmountPaid() && prepareSelectProposalDocument.getAmountPaid().compareTo(companyPayingMinimalAmount.getMiniMalAmount())
        < 0) {
      boolean isGPF =
          checkGPF(
              prepareSelectProposalDocument.getCompanyCode(),
              prepareSelectProposalDocument.getDocumentType(),
              prepareSelectProposalDocument.getAccountType(),
              prepareSelectProposalDocument.getPaymentMethod(),
              prepareSelectProposalDocument.getCostCenter(),
              prepareSelectProposalDocument.getReference1());
      String paymentType =
          checkPaymentType(prepareSelectProposalDocument.getPaymentMethod(), isGPF);
      String bankKey =
          prepareSelectProposalDocument.getPayee() != null
              ? prepareSelectProposalDocument.getAlternativePayeeBankKey().substring(0, 3)
              : prepareSelectProposalDocument.getMainPayeeBankKey().substring(0, 3);
      boolean isGiro = checkGiroFormat(bankKey, paymentType);
      boolean isInhouse =
          checkInhouseFormat(
              bankCodes, bankKey, paymentType, prepareSelectProposalDocument.getPaymentMethod());
      if (!isGiro && !isInhouse) {
        String errorCode = "012";
        prepareSelectProposalDocument.setStatus("E");
        prepareSelectProposalDocument.setErrorCode(errorCode);
      }
    } else if (null == parameter.getPaymentMethod()) {
      // 016 payment method == blank วิธีการชำระเงินในการประมวลผลครั้งนี้ไม่ได้ระบุในรายการ
      String errorCode = "016";
      prepareSelectProposalDocument.setStatus("E");
      prepareSelectProposalDocument.setErrorCode(errorCode);
    } else if (null != prepareSelectProposalDocument.getVendor()
        && !prepareSelectProposalDocument.getVendor().equalsIgnoreCase("")) {

      if (null == prepareSelectProposalDocument.getPayee()) {
        // เช็ค Main
        if (null != prepareSelectProposalDocument.getMainVendorActive()
            && prepareSelectProposalDocument.getMainVendorActive().equalsIgnoreCase("N")) {
          // 031 C_BPartner !isActive ข้อมูลผู้ขายได้รับการทำเครื่องหมายแฟลกสำหรับการลบ
          String errorCode = "031";
          prepareSelectProposalDocument.setStatus("E");
          prepareSelectProposalDocument.setErrorCode(errorCode);
        } else if (null == prepareSelectProposalDocument.getMainPayeeBankKey()) {
          // 031 C_BPartner เช็คบัญชี
          String errorCode = "071";
          prepareSelectProposalDocument.setStatus("E");
          prepareSelectProposalDocument.setErrorCode(errorCode);
        } else if (null != prepareSelectProposalDocument.getMainVendorStatus()
            && prepareSelectProposalDocument.getMainVendorStatus().equalsIgnoreCase("0")) {
          String errorCode = "033";
          prepareSelectProposalDocument.setStatus("E");
          prepareSelectProposalDocument.setErrorCode(errorCode);
        } else if (null != prepareSelectProposalDocument.getMainVendorStatus()
            && prepareSelectProposalDocument.getMainVendorStatus().equalsIgnoreCase("2")) {
          String errorCode = "035";
          prepareSelectProposalDocument.setStatus("E");
          prepareSelectProposalDocument.setErrorCode(errorCode);
        } else {
          Optional<BankCode> bankCode = bankCodes.stream()
              .filter(bank -> prepareSelectProposalDocument.getMainPayeeBank().equalsIgnoreCase(bank.getBankKey())).findFirst();
          if (bankCode.isPresent()) {
            if (!bankCode.get().isActive()) {
              // 072 รหัสธนาคารได้รับการทำเครื่องหมายแฟลกสำหรับการลบ
              String errorCode = "072";
              prepareSelectProposalDocument.setStatus("E");
              prepareSelectProposalDocument.setErrorCode(errorCode);
            }
          } else {
            // 073 รหัสธนาคารไม่ถูกต้อง
            String errorCode = "073";
            prepareSelectProposalDocument.setStatus("E");
            prepareSelectProposalDocument.setErrorCode(errorCode);
          }
        }
      }
//      if (null != prepareSelectProposalDocument.getPayee()) {
//        // เช็ค alternative
//        if (null != prepareSelectProposalDocument.getAlternativeVendorActive()
//            && prepareSelectProposalDocument.getAlternativeVendorActive().equalsIgnoreCase("N")) {
//          // 031 C_BPartner !isActive ข้อมูลผู้ขายได้รับการทำเครื่องหมายแฟลกสำหรับการลบ
//          String errorCode = "031";
//          prepareSelectProposalDocument.setStatus("E");
//          prepareSelectProposalDocument.setErrorCode(errorCode);
//        } else if (null == prepareSelectProposalDocument.getAlternativePayeeBankKey()) {
//          // 031 C_BPartner เช็คบัญชี
//          String errorCode = "071";
//          prepareSelectProposalDocument.setStatus("E");
//          prepareSelectProposalDocument.setErrorCode(errorCode);
//        } else if (null != prepareSelectProposalDocument.getAlternativeVendorStatus()
//            && prepareSelectProposalDocument.getAlternativeVendorStatus().equalsIgnoreCase("0")) {
//          String errorCode = "033";
//          prepareSelectProposalDocument.setStatus("E");
//          prepareSelectProposalDocument.setErrorCode(errorCode);
//        } else if (null != prepareSelectProposalDocument.getAlternativeVendorStatus()
//            && prepareSelectProposalDocument.getAlternativeVendorStatus().equalsIgnoreCase("2")) {
//          String errorCode = "035";
//          prepareSelectProposalDocument.setStatus("E");
//          prepareSelectProposalDocument.setErrorCode(errorCode);
//        }
//      }

    }
    if (null != prepareSelectProposalDocument.getPayee()
        && !prepareSelectProposalDocument.getPayee().equalsIgnoreCase("")) {
      //            if (null == alternatePayee) {
      //                // 024 TH_APBPAccessControl == null ข้อมูลหลักของผู้รับเงินหายไป
      //                String errorCode = "024";
      //                prepareSelectProposalDocument.setStatus("E");
      //                prepareSelectProposalDocument.setErrorCode(errorCode);
      //            }
      // 034 C_BP_Relation TH_APAlternatePayeeStatus.ConfirmStatus == 0 ข้อมูลผู้รับเงินมีสถานะ
      // "รอการยืนยัน"
      // 036 C_BP_Relation TH_APAlternatePayeeStatus.ConfirmStatus == 2 ข้อมูลผู้รับเงินมีสถานะ
      // "ไม่ยืนยัน"
      if (null != prepareSelectProposalDocument.getAlternativeVendorStatus()
          && prepareSelectProposalDocument.getAlternativeVendorStatus().equalsIgnoreCase("0")) {
        String errorCode = "034";
        prepareSelectProposalDocument.setStatus("E");
        prepareSelectProposalDocument.setErrorCode(errorCode);
      } else if (null != prepareSelectProposalDocument.getAlternativeVendorStatus()
          && (prepareSelectProposalDocument.getAlternativeVendorStatus().equalsIgnoreCase("2"))) {
        String errorCode = "036";
        prepareSelectProposalDocument.setStatus("E");
        prepareSelectProposalDocument.setErrorCode(errorCode);
      }
    } else if (prepareSelectProposalDocument.getDrCr().equalsIgnoreCase("D")
        || prepareSelectProposalDocument.getAmountPaid().compareTo(BigDecimal.ZERO) == 0) {
      // 060 DrCr = D || paid amount <= 0 การถูกระงับเนื่องจากยอดคงเหลือทางด้านเดบิท
      String errorCode = "060";
      prepareSelectProposalDocument.setStatus("E");
      prepareSelectProposalDocument.setErrorCode(errorCode);
    } else if (null != prepareSelectProposalDocument.getBankAccountNo()
        && !prepareSelectProposalDocument.getBankAccountNo().equalsIgnoreCase("")) {
      VendorBankAccount vendorBankAccount =
          vendorBankAccountService.findOneByBankAccountNoAndVendor(
              prepareSelectProposalDocument.getBankAccountNo(),
              null != prepareSelectProposalDocument.getPayee()
                  && !prepareSelectProposalDocument.getPayee().equalsIgnoreCase("")
                  ? prepareSelectProposalDocument.getPayee()
                  : prepareSelectProposalDocument.getVendor());
      if (!vendorBankAccount.isActive()) {
        // 071 bankAccountNo != C_BPartner.BankNo ไม่พบธนาคารคุ่ค้าที่ถูกต้อง
        String errorCode = "071";
        prepareSelectProposalDocument.setStatus("E");
        prepareSelectProposalDocument.setErrorCode(errorCode);
      }
    } else {
      Optional<BankCode> bankCode = bankCodes.stream()
          .filter(bank -> prepareSelectProposalDocument.getAlternativePayeeBank().equalsIgnoreCase(bank.getBankKey())).findFirst();
      if (bankCode.isPresent()) {
        if (!bankCode.get().isActive()) {
          // 072 รหัสธนาคารได้รับการทำเครื่องหมายแฟลกสำหรับการลบ
          String errorCode = "072";
          prepareSelectProposalDocument.setStatus("E");
          prepareSelectProposalDocument.setErrorCode(errorCode);
        }
      } else {
        // 073 รหัสธนาคารไม่ถูกต้อง
        String errorCode = "073";
        prepareSelectProposalDocument.setStatus("E");
        prepareSelectProposalDocument.setErrorCode(errorCode);
      }
    }
    return prepareSelectProposalDocument;
  }

  public synchronized Map<String, BigDecimal> createDocumentSpecialNew(
      PaymentAlias paymentAlias,
      Parameter parameter,
      PrepareSelectProposalDocument prepareSelectProposalDocument,
      String username,
      List<PaymentProcess> paymentProcesses,
      List<PaymentInformation> paymentInformations) {
    Map<String, BigDecimal> totalChild = new HashMap<>();
    BigDecimal amount = BigDecimal.ZERO;
    BigDecimal amountTax = BigDecimal.ZERO;
    try {
      List<PrepareProposalDocument> specialDoc =
          this.prepareProposalDocumentRepository.findDocumentK3OrKX(
              prepareSelectProposalDocument.getOriginalDocumentNo(),
              prepareSelectProposalDocument.getOriginalFiscalYear(),
              prepareSelectProposalDocument.getCompanyCode(),
              username);
//      log.info("before add specialDoc : {}", specialDoc.size());
      if (!specialDoc.isEmpty()) {

        for (PrepareProposalDocument prepareProposalDocument : specialDoc) {
//          log.info("adasd {}",specialDoc.get(i));
          amount = amount.add(prepareProposalDocument.getAmount());
          amountTax = amountTax.add(prepareProposalDocument.getWtxAmount());

//          String paymentDocNo =
//              SqlUtil.generateProposalDocument(
//                  prepareProposalDocumentRepository.getNextSeries(true));
//          specialDoc.get(i).setPaymentClearingDocNo(paymentDocNo);
          prepareProposalDocument.setPaymentClearingDate(parameter.getPostDate());
          prepareProposalDocument.setPaymentClearingEntryDate(new Timestamp(new Date().getTime()));
          prepareProposalDocument.setPaymentClearingYear(prepareProposalDocument.getOriginalFiscalYear());
          prepareProposalDocument.setPaymentDate(paymentAlias.getPaymentDate());
          prepareProposalDocument.setPaymentDateAcct(paymentAlias.getPaymentDate());
          prepareProposalDocument.setPaymentName(paymentAlias.getPaymentName());
          prepareProposalDocument.setPaymentId(paymentAlias.getId());

          HouseBankPaymentMethod houseBankPaymentMethod =
              Context.sessionHouseBankPaymentMethod.get(prepareProposalDocument.getPaymentMethod());

          prepareProposalDocument.setPayingBankCode(houseBankPaymentMethod.getAccountCode());
          prepareProposalDocument.setPayingHouseBank(houseBankPaymentMethod.getHouseBank());
          prepareProposalDocument.setPayingBankAccountNo(houseBankPaymentMethod.getBankAccountCode());
          prepareProposalDocument.setPayingBankCountry(houseBankPaymentMethod.getCountryCode());
          prepareProposalDocument.setPayingBankNo(houseBankPaymentMethod.getBankAccountCode());
          prepareProposalDocument.setPayingGLAccount(houseBankPaymentMethod.getGlAccount());
          prepareProposalDocument.setPayingBankKey(houseBankPaymentMethod.getBankBranch());
          prepareProposalDocument.setPayingBankName(houseBankPaymentMethod.getHouseBank());
          prepareProposalDocument.setPayingCompCode("99999");
          prepareProposalDocument
              .setAmountPaid(
                  prepareProposalDocument.getAmount().subtract(prepareProposalDocument.getWtxAmount()));

          glHeadService.updateGLHead(
              prepareProposalDocument.getCompanyCode(),
              prepareProposalDocument.getOriginalDocumentNo(),
              prepareProposalDocument.getOriginalFiscalYear(),
              prepareProposalDocument.getPaymentClearingDocNo(),
              prepareProposalDocument.getPaymentId());

          PaymentProcess paymentProcess = new PaymentProcess(prepareProposalDocument, true, true);
//          paymentProcess.setId(paymentProcessService.getNextSeries());
          paymentProcess.setParentCompanyCode(prepareSelectProposalDocument.getCompanyCode());
          paymentProcess.setParentDocumentNo(prepareSelectProposalDocument.getOriginalDocumentNo());
          paymentProcess.setParentFiscalYear(prepareSelectProposalDocument.getOriginalFiscalYear());
          PaymentInformation paymentInformation =
              new PaymentInformation(prepareProposalDocument, true, true);
//          paymentInformation.setId(paymentInformationService.getNextSeries());

          if (null != prepareProposalDocument.getOriginalDocumentNo()) {

//            String originalDocumentNo = specialDoc.get(i).getOriginalDocument().substring(0, 10);
//            String originalCompanyCode = specialDoc.get(i).getOriginalDocument().substring(10, 15);
//            String originalFiscalYear = specialDoc.get(i).getOriginalDocument().substring(15, 19);

            //                        GLHead glHeadOriginal =
            // glHeadService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(originalCompanyCode, originalDocumentNo, originalFiscalYear);

            paymentProcess.setOriginalDocumentNo(
                prepareProposalDocument.getRealOriginalDocumentNo());
            paymentProcess.setOriginalCompanyCode(
                prepareProposalDocument.getRealOriginalCompanyCode());
            paymentProcess.setOriginalFiscalYear(
                prepareProposalDocument.getRealOriginalFiscalYear());
            paymentProcess.setOriginalDocumentType(
                prepareProposalDocument.getRealOriginalDocumentType());
            paymentProcess.setOriginalAmount(prepareProposalDocument.getAmount());
            paymentProcess.setOriginalAmountPaid(prepareProposalDocument.getAmountPaid());

            //                    GLLine glLineOriginalTypeK =
            // glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(originalCompanyCode, originalDocumentNo, originalFiscalYear, "K");

            paymentProcess.setOriginalPaymentCenter(
                prepareProposalDocument.getLinePaymentCenter());
            paymentProcess.setOriginalWtxAmount(prepareProposalDocument.getLineWtxAmount());
            paymentProcess.setOriginalWtxBase(prepareProposalDocument.getLineWtxBase());
            paymentProcess.setOriginalWtxAmountP(prepareProposalDocument.getLineWtxAmountP());
            paymentProcess.setOriginalWtxBaseP(prepareProposalDocument.getLineWtxBaseP());
            paymentProcess.setStatus("S");
          }

          paymentProcesses.add(paymentProcess);
          paymentInformations.add(paymentInformation);
//          this.paymentProcessService.save(paymentProcess);
//          paymentInformation.setPaymentProcessId(paymentProcess.getId());
//          this.paymentInformationService.save(paymentInformation);
          //                    seqNo++;
          //                    processSeqNo++;
          //                    informationSeqNo++;

        }
        totalChild.put("amount", amount);
        totalChild.put("amountTax", amountTax);

        return totalChild;
      } else {
        return totalChild;
      }

    } catch (Exception e) {
      e.printStackTrace();
      return totalChild;
    }
  }

  @Override
  @Async
  public void createRealRunNew(List<PrepareRealRunDocument> prepareRealRunDocumentList, PaymentAlias paymentAlias, WSWebInfo webInfo) {
    try {


//      PaymentAlias paymentAlias = paymentAliasService.findOneById(id);
//      List<PrepareRunDocument> prepareRunDocumentList = prepareRunDocumentService.findProposalDoc(paymentAlias.getId(), true);
//      List<PrepareRealRunDocument> prepareRealRunDocumentList = prepareRunDocumentService.findProposalDocument(paymentAlias.getId(), true);
      boolean haveK3OrKX = false;
      List<PaymentProcess> paymentProcesses = new ArrayList<>();
      List<PaymentInformation> paymentInformations = new ArrayList<>();
      List<FIMessage> fiMessages = new ArrayList<>();
      for (PrepareRealRunDocument item : prepareRealRunDocumentList) {
        haveK3OrKX = false;
        APPaymentRequest apPaymentRequest = new APPaymentRequest();

        APPaymentHeader aPPaymentHeader = new APPaymentHeader();
        aPPaymentHeader.setPmCompCode("99999");
        aPPaymentHeader.setPmDate(item.getPaymentDate());
        aPPaymentHeader.setPmIden(item.getPaymentName());
        aPPaymentHeader.setVendor(item.getVendorCode());
        aPPaymentHeader.setPayee(item.getPayeeCode());

        aPPaymentHeader.setPmGroupNo(item.getPmGroupNo());
        aPPaymentHeader.setPmGroupDoc(item.getPmGroupDoc());

        aPPaymentHeader.setVendorTaxID(item.getVendorTaxId());
        aPPaymentHeader.setBankAccNo(item.getPayeeBankAccountNo());
        aPPaymentHeader.setBranchNo(item.getPayeeBankKey());
        aPPaymentHeader.setGlAccount(item.getPayingGlAccount());
        aPPaymentHeader.setReceiptTaxID(item.getVendorTaxId());

        PayMethodConfig payMethodConfig =
            Context.sessionPayMethodConfig.get("TH-" + item.getPaymentMethod());
        aPPaymentHeader.setDocType(payMethodConfig.getDocumentTypeForPayment());

        aPPaymentHeader.setDateDoc(item.getPaymentDate());
        aPPaymentHeader.setDateAcct(item.getPaymentDateAcct());

        List<APPaymentLine> listAPPaymentLine = new ArrayList<>();

        APPaymentLine apPaymentLine = new APPaymentLine();
        apPaymentLine.setInvCompCode(item.getInvoiceCompanyCode());
        apPaymentLine.setInvDocNo(item.getInvoiceDocumentNo());
        apPaymentLine.setInvFiscalYear(item.getInvoiceFiscalYear());
        apPaymentLine.setInvLine(item.getLine());
        apPaymentLine.setDocType(item.getInvoiceDocumentType());
        apPaymentLine.setDateAcct(item.getDateAcct());
        apPaymentLine.setDateDoc(item.getDateDue());
        apPaymentLine.setVendor(item.getVendorCode());
        apPaymentLine.setPayee(item.getPayeeCode());

        apPaymentLine.setDrCr(item.getDrCr());
        apPaymentLine.setAmount(item.getInvoiceAmount());
        apPaymentLine.setWtxAmount(item.getInvoiceWtxAmount());

        listAPPaymentLine.add(apPaymentLine);

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (item.getInvoiceDocumentType().equalsIgnoreCase("KA")
            || item.getInvoiceDocumentType().equalsIgnoreCase("KB")
            || item.getInvoiceDocumentType().equalsIgnoreCase("KG")
            || item.getInvoiceDocumentType().equalsIgnoreCase("KC")) {

          List<PrepareRunDocument> specialDoc =
              prepareRunDocumentService.findChildProposalDoc(apPaymentLine, true, paymentAlias.getId());

          haveK3OrKX = specialDoc.size() > 0;

          for (PrepareRunDocument specialLine : specialDoc) {


            PaymentProcess paymentProcess = new PaymentProcess(specialLine);
//            paymentProcess.setId(SqlUtil.getNextSeries(paymentJdbcTemplate, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ, null));
            paymentProcess.setPaymentDocumentNo("");
            paymentProcess.setPaymentCompanyCode("99999");

            paymentProcess.setParentCompanyCode(item.getInvoiceCompanyCode());
            paymentProcess.setParentDocumentNo(item.getInvoiceDocumentNo());
            paymentProcess.setParentFiscalYear(item.getInvoiceFiscalYear());

            paymentProcess.setChild(true);
            paymentProcess.setProposal(false);
            paymentProcesses.add(paymentProcess);
//            this.paymentProcessService.save(paymentProcess);

            PaymentInformation paymentInformation = new PaymentInformation(specialLine);
            paymentInformations.add(paymentInformation);
//            paymentInformation.setId(SqlUtil.getNextSeries(paymentJdbcTemplate, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ, null));
//            paymentInformation.setPaymentProcessId(paymentProcess.getId());
//            this.paymentInformationService.save(paymentInformation);

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
        aPPaymentHeader.setAmount(
            totalAmount.subtract(item.getInvoiceAmount().subtract(item.getInvoiceWtxAmount())));
        apPaymentRequest.setHeader(aPPaymentHeader);
        apPaymentRequest.setLines(listAPPaymentLine);
        apPaymentRequest.setWebInfo(webInfo);

        try {
          FIMessage fiMessage = new FIMessage();
          fiMessage.setId(item.getPmGroupDoc());
          fiMessage.setType(FIMessage.Type.REQUEST.getCode());
          fiMessage.setDataType(FIMessage.DataType.CREATE.getCode());
          fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
          fiMessage.setTo("99999");
          //          log.info("apPaymentRequest {}", apPaymentRequest);
          fiMessage.setData(XMLUtil.xmlMarshall(APPaymentRequest.class, apPaymentRequest));
          fiMessages.add(fiMessage);
          //          log.info("fiMessage {}", fiMessage);

          PaymentProcess paymentProcess = new PaymentProcess();
          BeanUtils.copyProperties(item, paymentProcess);
          paymentProcess.setPaymentDocumentNo("");
          paymentProcess.setPaymentAliasId(item.getPaymentAliasId());
          paymentProcess.setProposal(false);
          paymentProcess.setPaymentCompanyCode("99999");
//          paymentProcess.setId(SqlUtil.getNextSeries(paymentJdbcTemplate, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ, null));
          paymentProcess.setPaymentDocumentType(aPPaymentHeader.getDocType());

          paymentProcess.setPmGroupNo(item.getPmGroupNo());
          paymentProcess.setPmGroupDoc(item.getPmGroupDoc());
          if (haveK3OrKX) {
            paymentProcess.setHaveChild(true);
          } else {
            paymentProcess.setHaveChild(false);
          }

          paymentProcesses.add(paymentProcess);
//          this.paymentProcessService.save(paymentProcess);

          PaymentInformation paymentInformation = new PaymentInformation();
          BeanUtils.copyProperties(item, paymentInformation);
//          paymentInformation.setId(SqlUtil.getNextSeries(paymentJdbcTemplate, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ, null));
//          paymentInformation.setPaymentProcessId(paymentProcess.getId());
//          this.paymentInformationService.save(paymentInformation);
          paymentInformations.add(paymentInformation);

//          this.send(fiMessage);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      long paymentProcessesSize = paymentProcesses.size();
      long paymentProcessId = SqlUtil.getNextSeries(
          paymentJdbcTemplate, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ, paymentProcessesSize);
      long paymentInformationsSize = paymentInformations.size();
      long paymentInformationsId = SqlUtil.getNextSeries(
          paymentJdbcTemplate, PaymentInformation.TABLE_NAME + PaymentInformation.SEQ, paymentInformationsSize);
      for (int i = 0; i < paymentProcesses.size(); i++) {
        Long processId = paymentProcessId;
        paymentProcesses.get(i).setId(processId);
        paymentInformations.get(i).setPaymentProcessId(processId);
        paymentInformations.get(i).setId(paymentInformationsId);
        paymentProcessId++;
        paymentInformationsId++;
      }
      try {
        this.paymentProcessService.saveBatch(paymentProcesses);
        this.paymentInformationService.saveBatch(paymentInformations);
        for (FIMessage fiMessage : fiMessages) {
          this.send(fiMessage);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      this.writeLogAsyncService.writeLogPaymentRealRunNew(
          paymentAlias, prepareRealRunDocumentList, webInfo);


      paymentAlias.setRunEnd(new Timestamp(System.currentTimeMillis()));
      paymentAlias.setRunStatus("S");
      paymentAlias.setRunJobStatus("S");
      paymentAlias.setRunTotalDocument(prepareRealRunDocumentList.size());
      paymentAlias.setRunSuccessDocument(0);
      paymentAliasService.save(paymentAlias);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
