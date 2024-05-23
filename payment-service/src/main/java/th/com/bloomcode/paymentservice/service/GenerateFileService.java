package th.com.bloomcode.paymentservice.service;

import com.google.common.collect.ComparisonChain;
import com.google.gson.Gson;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.*;
import th.com.bloomcode.paymentservice.model.idem.Vendor;
import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;
import th.com.bloomcode.paymentservice.model.payment.*;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.model.request.GenerateFileAliasRequest;
import th.com.bloomcode.paymentservice.model.xml.pain001.*;
import th.com.bloomcode.paymentservice.payment.entity.SmartFee;
import th.com.bloomcode.paymentservice.payment.entity.SumFileCondition;
import th.com.bloomcode.paymentservice.payment.entity.SwiftFee;
import th.com.bloomcode.paymentservice.payment.entity.mapping.PaymentRealRun;
import th.com.bloomcode.paymentservice.service.idem.VendorBankAccountService;
import th.com.bloomcode.paymentservice.service.idem.VendorService;
import th.com.bloomcode.paymentservice.service.payment.*;
import th.com.bloomcode.paymentservice.util.BahtnetUtil;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;
import th.com.bloomcode.paymentservice.util.XMLUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class GenerateFileService {

  private final ProposalLogSumService proposalLogSumService;
  private final ProposalLogService proposalLogService;
  private final SmartFeeService smartFeeService;
  private final SwiftFeeService swiftFeeService;
  private final GenerateFileAliasService generateFileAliasService;
  private final PaymentService paymentService;
  private final MetaDataService metaDataService;
  private final ProposalLogHeaderService proposalLogHeaderService;
  private final FileTransferService fileTransferService;
  private final PrepareRunDocumentService prepareRunDocumentService;
  private final JdbcTemplate paymentJdbcTemplate;
  private final VendorService vendorService;
  private final GenerateFileSequenceService generateFileSequenceService;
  private final SumFileConditionService sumFileConditionService;
  private final BankCodeService bankCodeService;
  private final GenerateFileOutputService generateFileOutputService;
  private final VendorBankAccountService vendorBankAccountService;

  //    private final SmartFileLogService

  private Map<String, Account> account = new HashMap<>();

  private final List<String> direct = List.of("1", "3", "6", "K", "G", "H", "A", "B");
  //    private String[] indirect = {"A", "M", "P", "B", "1", "3", "6", "8", "E", "G", "H", "F",
  // "2", "4", "7", "9", "I", "J", "K", "L"};

  //    @Value("${gen.file.path}")
  //    private String path;

  private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

  private DecimalFormat df = new DecimalFormat("0.00##");

  @Autowired
  public GenerateFileService(
      ProposalLogSumService proposalLogSumService,
      ProposalLogService proposalLogService,
      SmartFeeService smartFeeService,
      SwiftFeeService swiftFeeService,
      GenerateFileAliasService generateFileAliasService,
      PaymentService paymentService,
      MetaDataService metaDataService,
      ProposalLogHeaderService proposalLogHeaderService,
      FileTransferService fileTransferService,
      PrepareRunDocumentService prepareRunDocumentService,
      @Qualifier("paymentJdbcTemplate") JdbcTemplate paymentJdbcTemplate,
      VendorService vendorService,
      GenerateFileSequenceService generateFileSequenceService,
      SumFileConditionService sumFileConditionService,
      BankCodeService bankCodeService,
      GenerateFileOutputService generateFileOutputService,
      VendorBankAccountService vendorBankAccountService) {
    this.proposalLogSumService = proposalLogSumService;
    this.proposalLogService = proposalLogService;
    this.smartFeeService = smartFeeService;
    this.swiftFeeService = swiftFeeService;
    this.generateFileAliasService = generateFileAliasService;
    this.paymentService = paymentService;
    this.metaDataService = metaDataService;
    this.proposalLogHeaderService = proposalLogHeaderService;
    this.fileTransferService = fileTransferService;
    this.prepareRunDocumentService = prepareRunDocumentService;
    this.paymentJdbcTemplate = paymentJdbcTemplate;
    this.vendorService = vendorService;
    this.generateFileSequenceService = generateFileSequenceService;
    this.sumFileConditionService = sumFileConditionService;
    this.bankCodeService = bankCodeService;
    this.generateFileOutputService = generateFileOutputService;
    this.vendorBankAccountService = vendorBankAccountService;
    this.setAccountId();
  }

  public ResponseEntity<Result<String>> generateFile(Long generateFileAliasId, GenerateFileAliasRequest request, boolean isPac) throws IOException {
    Result<String> result = new Result<>();
    //        log.info("generateFileAliasId : {} ", generateFileAliasId);
    GenerateFileAlias generateFileAlias = generateFileAliasService.findOneById(generateFileAliasId);
    //        log.info("generateFileAlias : {}", generateFileAlias);
    if (generateFileAlias != null
        || null == generateFileAlias.getRunStatus()
        || generateFileAlias.isTestRun()) {
      generateFileAliasService.save(generateFileAlias);
      result.setData(prepareData(generateFileAlias, generateFileAlias.isTestRun(), request, isPac).getReference());
      result.setStatus(HttpStatus.OK.value());
      result.setMessage("");
      return new ResponseEntity<>(result, HttpStatus.OK);
    } else if (generateFileAlias != null || null == generateFileAlias.getRunStatus()) {
      generateFileAlias.setRunStatus("P");
      generateFileAliasService.save(generateFileAlias);
      result.setData(prepareData(generateFileAlias, generateFileAlias.isTestRun(), request, isPac).getReference());
      result.setStatus(HttpStatus.OK.value());
      result.setMessage("");
      return new ResponseEntity<>(result, HttpStatus.OK);
    } else if ("P".equalsIgnoreCase(generateFileAlias.getRunStatus())) {
      result.setData(null);
      result.setStatus(HttpStatus.ACCEPTED.value());
      result.setMessage("");
      return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    } else {
      result.setData(null);
      result.setStatus(HttpStatus.NOT_FOUND.value());
      result.setMessage("");
      return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Result<GenerateFile>> report(Long generateFileAliasId) {
    Result<GenerateFile> result = new Result<>();
    try {
      GenerateFileOutput generateFileOutput =
          generateFileOutputService.findOneByRefRunning(generateFileAliasId);
      if (null != generateFileOutput) {
        Gson gson = new Gson();
        GenerateFile generateFile =
            gson.fromJson(generateFileOutput.getJsonText(), GenerateFile.class);
        result.setData(generateFile);
      }
      result.setStatus(HttpStatus.OK.value());
      result.setMessage("");
      //            result.setData(generateTextFile(generateFileAlias,
      // generateFileAlias.isTestRun()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  private GenerateFile prepareData(GenerateFileAlias generateFileAlias, boolean isTestRun, GenerateFileAliasRequest request, boolean isPac) {
    try {
      JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
      Timestamp created = new Timestamp(System.currentTimeMillis());
      if (!isTestRun) {
        generateFileAlias.setRunStatus("P");
        generateFileAlias.setCreated(created);
        generateFileAlias.setCreatedBy(jwt.getSub());
        generateFileAlias.setUpdated(created);
        generateFileAlias.setUpdatedBy(jwt.getSub());
        generateFileAliasService.save(generateFileAlias);
      }

      String format = "";
      boolean isAll = true;
      String transferLevel = "";
      String fileNameLevel1 = "";
      boolean isRegen = false;
      boolean isSameday = 0 == generateFileAlias.getSmartAmountDay();
      if (null != request.getFileName()
          && !request.getFileName().equalsIgnoreCase("")) {
        ProposalLog proposalLog =
            proposalLogService.findOneFileName(
                generateFileAlias.getGenerateFileDate(),
                generateFileAlias.getGenerateFileName(),
                request.getFileName());
        if (null != proposalLog) {
          transferLevel = proposalLog.getTransferLevel();
          if (transferLevel.equalsIgnoreCase("1")) {
            format = "";
            isAll = true;
            fileNameLevel1 = proposalLog.getFileName();
          } else {
            List<ProposalLog> groupFile = proposalLogService.findGroupFileName(generateFileAlias.getGenerateFileDate(), generateFileAlias.getGenerateFileName());
            ProposalLog proposalLogLevel1 =
                    proposalLogService.findOneFileNameLevel1(
                            generateFileAlias.getGenerateFileDate(),
                            generateFileAlias.getGenerateFileName());
            isAll = null != groupFile && groupFile.size() == 1;
            format = proposalLog.getFileType();
            fileNameLevel1 = proposalLogLevel1.getFileName();
          }
          isRegen = true;
        }
      }

      List<PrepareRunDocument> listDocument;
      if (isRegen) {
        if (!Util.isEmpty(format)) {
          listDocument =
              prepareRunDocumentService.findProposalDocForReGen(
                  generateFileAlias.getPaymentAliasId(), request.getFileName(), false);
        } else {
          listDocument =
              prepareRunDocumentService.findProposalDocForGen(
                  generateFileAlias.getPaymentAliasId(), false);
        }
      } else {
        listDocument =
            prepareRunDocumentService.findProposalDocForGen(
                generateFileAlias.getPaymentAliasId(), false);
      }
      List<SwiftFee> swiftFees = swiftFeeService.findAll();
      List<SumFileCondition> sumFileConditions = sumFileConditionService.findAll();
      List<BankCode> bankCodes = bankCodeService.findAllBank(true);
      List<SmartFee> smartFees = smartFeeService.findAll();
      Map<String, BankCode> bankCodeMap = new HashMap<>();
      for (BankCode bankCode : bankCodes) {
        bankCodeMap.put(bankCode.getBankKey(), bankCode);
      }
      List<ProposalLog> proposalLogs = new ArrayList<>();
      String sendRef = "";
      List<SwiftFile> swiftFiles = new ArrayList<>();
      List<SwiftFile> swiftFilesSumFile = new ArrayList<>();
      boolean usePain = true;

      // new logic TEMP
      List<PrepareRunDocument> smarts = new ArrayList<>();
      List<PrepareRunDocument> swifts = new ArrayList<>();
      List<PrepareRunDocument> inhouses = new ArrayList<>();
      List<PrepareRunDocument> giros = new ArrayList<>();

      List<PrepareRunDocument> smartsSumFile = new ArrayList<>();
      List<PrepareRunDocument> swiftsSumFile = new ArrayList<>();
      List<PrepareRunDocument> inhousesSumFile = new ArrayList<>();
      List<PrepareRunDocument> girosSumFile = new ArrayList<>();

      List<String> errors = new ArrayList<>();

      PaymentAlias paymentAlias = generateFileAlias.getPaymentAlias();
      List<ProposalLog> interfaceD1D2 = new ArrayList<>();
      Long proposalHeaderId = 0L;
      Long proposalId = 0L;
      Long proposalSumId = 0L;
      Long refRunning;
      if (isTestRun) {
        refRunning = 1000000000000000L;
      } else {
        refRunning = SqlUtil.getNextSeries(paymentJdbcTemplate, "PROPOSAL_RUNNING_SEQ", null);
        proposalHeaderId =
            SqlUtil.getNextSeries(
                paymentJdbcTemplate, ProposalLogHeader.TABLE_NAME + ProposalLogHeader.SEQ, null);
        proposalId =
            SqlUtil.getNextSeries(paymentJdbcTemplate, ProposalLog.TABLE_NAME + ProposalLog.SEQ, (long) (listDocument.size() + 10));
        proposalSumId =
            SqlUtil.getNextSeries(
                paymentJdbcTemplate, ProposalLogSum.TABLE_NAME + ProposalLogSum.SEQ, (long) (listDocument.size() + 10));

//        Long allocateSum = proposalSumId + listDocument.size() + 10;
//        Long allocate = proposalId + listDocument.size() + 10;
//        SqlUtil.updateNextSeries(
//            paymentJdbcTemplate, allocateSum, ProposalLogSum.TABLE_NAME + ProposalLogSum.SEQ);
//        SqlUtil.updateNextSeries(
//            paymentJdbcTemplate, allocate, ProposalLog.TABLE_NAME + ProposalLog.SEQ);
      }
      ProposalLogHeader proposalLogHeader = null;
      int line = 0;
      for (PrepareRunDocument paymentRealRun : listDocument) {
        ProposalLog proposalLog = new ProposalLog();
        String bankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
        if (null == bankCodeMap.get(bankKey)) {
          errors.add(
              "เลขที่เอกสาร "
                  + paymentRealRun.getInvoiceDocumentNo()
                  + " : คีย์ธนาคาร "
                  + bankKey
                  + " ไม่มีในระบบ");
        } else if (!paymentRealRun.getPaymentMethod().equalsIgnoreCase("C")) {
          boolean isGPF =
              checkGPF(
                  paymentRealRun.getOriginalCompanyCode(),
                  paymentRealRun.getOriginalDocumentType(),
                  paymentRealRun.getAccountType(),
                  paymentRealRun.getPaymentMethod(),
                  paymentRealRun.getCostCenter(),
                  paymentRealRun.getPaymentReference());
          String paymentType = checkPaymentType(paymentRealRun.getPaymentMethod(), isGPF);
          boolean isSwift =
              checkSwiftFormat(
                  swiftFees,
                  bankCodes,
                  bankKey,
                  paymentRealRun.getReference1(),
                  paymentType,
                  paymentRealRun.getPaymentMethod());
          boolean isGiro = checkGiroFormat(bankKey, paymentType);
          boolean isInhouse =
              checkInhouseFormat(
                  bankCodes, bankKey, paymentType, paymentRealRun.getPaymentMethod());
          boolean isSmart = checkSmartFormat(isSwift, isGiro, isInhouse);
          boolean isSumFile =
              checkSumFile(
                  paymentRealRun.getPaymentMethod(),
                  paymentRealRun.getVendorCode(),
                  sumFileConditions);
          boolean isInterfaceD1D2 = checkInterfaceD1D2(paymentType);
          paymentRealRun.setSumFile(isSumFile);
          paymentRealRun.setPaymentType(paymentType);
          proposalLog.setAmount(
              paymentRealRun
                  .getInvoiceAmountPaid()
                  .subtract(
                      paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
          proposalLog.setPayAccount(paymentType);
          proposalLog.setBankKey(bankKey);
          if (isInterfaceD1D2) {
            if (null == proposalLogHeader) {
              proposalLogHeader = new ProposalLogHeader();
              proposalLogHeader.setId(proposalHeaderId);
              proposalLogHeader.setRefRunning(refRunning);
              proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
              proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
              proposalLogHeader.setCreatedBy(jwt.getSub());
              proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
              proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
              proposalLogHeader.setCancel(false);
              proposalLogHeader.setUse(isAll);
              proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
            }
            ProposalLog propInterface = new ProposalLog();
            propInterface.setId(proposalId++);
            propInterface.setRefRunning(refRunning);
            propInterface.setRefLine(++line);
            propInterface.setPaymentName(paymentAlias.getPaymentName());
            propInterface.setPaymentDate(paymentAlias.getPaymentDate());
            String tr2Acc = this.account.get("TR2").getAccountNo();
            String accountNo = this.account.get(paymentType).getAccountNo();
            propInterface.setAccountNoFrom(tr2Acc);
            propInterface.setAccountNoTo(accountNo);
            propInterface.setFileType("CGD");
            propInterface.setTransferDate(generateFileAlias.getSwiftDate());
            propInterface.setAmount(
                paymentRealRun
                    .getInvoiceAmountPaid()
                    .subtract(
                        paymentRealRun
                            .getCreditMemo()
                            .subtract(paymentRealRun.getWtxCreditMemo())));
            propInterface.setBankFee(BigDecimal.ZERO);
            propInterface.setVendor(paymentRealRun.getVendorCode());
            propInterface.setBankKey(paymentRealRun.getPayeeBankKey().substring(0, 3));
            propInterface.setVendorBankAccount(paymentRealRun.getPayeeBankAccountNo());
            propInterface.setTransferLevel("9");
            propInterface.setPayAccount(paymentType);
            propInterface.setPayingCompCode(paymentRealRun.getPayingCompanyCode());
            propInterface.setPaymentDocument(paymentRealRun.getPaymentDocumentNo());
            propInterface.setFiscalYear(paymentRealRun.getPaymentFiscalYear());
            propInterface.setFiArea(paymentRealRun.getFiArea());
            propInterface.setCreditMemoAmount(
                paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()));
            propInterface.setCreatedBy(jwt.getSub());
            propInterface.setUpdatedBy(jwt.getSub());
            propInterface.setCreated(created);
            propInterface.setUpdated(created);
            propInterface.setSendStatus("S");

            propInterface.setOriginalCompCode(paymentRealRun.getOriginalCompanyCode());
            propInterface.setOriginalDocNo(paymentRealRun.getOriginalDocumentNo());
            propInterface.setOriginalDocType(paymentRealRun.getOriginalDocumentType());
            propInterface.setOriginalFiscalYear(paymentRealRun.getOriginalFiscalYear());

            propInterface.setInvCompCode(paymentRealRun.getInvoiceCompanyCode());
            propInterface.setInvDocNo(paymentRealRun.getInvoiceDocumentNo());
            propInterface.setInvDocType(paymentRealRun.getInvoiceDocumentType());
            propInterface.setInvFiscalYear(paymentRealRun.getInvoiceFiscalYear());

            propInterface.setOriginalWtxAmount(paymentRealRun.getOriginalWtxAmount());
            propInterface.setOriginalWtxBase(paymentRealRun.getOriginalWtxBase());
            propInterface.setOriginalWtxAmountP(paymentRealRun.getOriginalWtxAmountP());
            propInterface.setOriginalWtxBaseP(paymentRealRun.getOriginalWtxBaseP());

            propInterface.setInvWtxAmount(paymentRealRun.getInvoiceWtxAmount());
            propInterface.setInvWtxBase(paymentRealRun.getInvoiceWtxBase());
            propInterface.setInvWtxAmountP(paymentRealRun.getInvoiceWtxAmountP());
            propInterface.setInvWtxBaseP(paymentRealRun.getInvoiceWtxBaseP());

            propInterface.setPaymentCompCode(paymentRealRun.getPaymentCompanyCode());
            propInterface.setPaymentFiscalYear(paymentRealRun.getPaymentFiscalYear());

            propInterface.setPaymentType(paymentType);

            propInterface.setRefRunningSum(0L);
            propInterface.setRefLineSum(0);

            propInterface.setProposalLogHeaderId(proposalHeaderId);
            proposalLogHeader.addInterfaceD1D2(propInterface);

            proposalLog.setAmount(
                paymentRealRun
                    .getInvoiceAmountPaid()
                    .subtract(
                        paymentRealRun
                            .getCreditMemo()
                            .subtract(paymentRealRun.getWtxCreditMemo())));
            proposalLog.setPayAccount(paymentType);
            proposalLog.setBankKey(paymentRealRun.getPayeeBankKey());
            proposalLog.setFileType("CGD");
            proposalLogs.add(proposalLog);
          } else {
            if ((isSmart && !isRegen)
                || (isRegen && isSmart && format.equalsIgnoreCase("SMART")
                    || (isRegen && isSmart && transferLevel.equalsIgnoreCase("1")))
                || (isRegen && isSmart && format.equalsIgnoreCase("SWIFT"))) {
              Account account = this.account.get(paymentType);
              BigDecimal smartFee =
                  calculateSmartFee(
                      smartFees,
                      account.getAccountNo(),
                      paymentRealRun
                          .getInvoiceAmountPaid()
                          .subtract(
                              paymentRealRun
                                  .getCreditMemo()
                                  .subtract(paymentRealRun.getWtxCreditMemo())),
                      isSameday);
              BigDecimal transferAmount =
                  paymentRealRun
                      .getInvoiceAmountPaid()
                      .subtract(
                          paymentRealRun
                              .getCreditMemo()
                              .subtract(paymentRealRun.getWtxCreditMemo()))
                      .subtract(smartFee);
              if (transferAmount.compareTo(BigDecimal.ZERO) > 0) {
                if (isSumFile) {
                  smartsSumFile.add(paymentRealRun);
                } else {
                  smarts.add(paymentRealRun);
                }
              } else {
                errors.add(
                    "เลขที่เอกสาร "
                        + paymentRealRun.getInvoiceDocumentNo()
                        + " ยอดเงินหลังหักค่าธรรมเนียมน้อยกว่า 1 บาท");
              }

            } else if ((isSwift && !isRegen)
                || (isRegen && isSwift && format.equalsIgnoreCase("SWIFT")
                    || (isRegen && isSwift && transferLevel.equalsIgnoreCase("1")))) {
              if (isSumFile) {
                swiftsSumFile.add(paymentRealRun);
              } else {
                swifts.add(paymentRealRun);
              }
            } else if ((isInhouse && !isRegen)
                || (isRegen && isInhouse && format.equalsIgnoreCase("INHOU")
                    || (isRegen && isInhouse && transferLevel.equalsIgnoreCase("1")))) {
              if (isSumFile) {
                inhousesSumFile.add(paymentRealRun);
              } else {
                inhouses.add(paymentRealRun);
              }
            } else if ((isGiro && !isRegen)
                || (isRegen && isGiro && format.equalsIgnoreCase("GIRO")
                    || (isRegen && isGiro && transferLevel.equalsIgnoreCase("1")))) {
              if (isSumFile) {
                girosSumFile.add(paymentRealRun);
              } else {
                giros.add(paymentRealRun);
              }
            }
          }
        }
      }
      if (null != proposalLogHeader) {
        generateFileAlias.setInterfaceD1D2(proposalLogHeader);
      }

      if (null != smarts && !smarts.isEmpty()) {
        List<PrepareRunDocument> over =
            smarts.stream()
                .filter(
                    s ->
                        s.getInvoiceAmountPaid()
                                .subtract(s.getCreditMemo().subtract(s.getWtxCreditMemo()))
                                .compareTo(Util.getBigDecimal(2000000))
                            > 0)
                .collect(toList());
        if (null != over && !over.isEmpty()) {
          swifts.addAll(over);
          smarts.removeAll(over);
        }

        if (isRegen && format.equalsIgnoreCase("SWIFT")) {
          smarts.clear();
        }

        if (isRegen && format.equalsIgnoreCase("SMART")) {
          swifts.clear();
        }
      }

      if (null != smartsSumFile && !smartsSumFile.isEmpty()) {
        Map<String, List<PrepareRunDocument>> groupByVendor =
            smartsSumFile.stream()
                .collect(
                    Collectors.groupingBy(
                        this::groupByMultipleKey,
                        Collectors.mapping((PrepareRunDocument p) -> p, toList())));

        for (var entry : groupByVendor.entrySet()) {
          BigDecimal totalTransferAmount =
              entry.getValue().stream()
                  .map(PrepareRunDocument::getInvoiceAmountPaid) // map
                  .reduce(BigDecimal.ZERO, BigDecimal::add);
          if (totalTransferAmount.compareTo(Util.getBigDecimal(2000000)) > 0) {
            swiftsSumFile.addAll(entry.getValue());
            smartsSumFile.removeAll(entry.getValue());
          }
        }
      }

      if (!smarts.isEmpty()) {
        smarts.sort(
            (p1, p2) ->
                ComparisonChain.start()
                    .compare(p1.getPaymentType(), p2.getPaymentType())
                    .compare(
                        calculateSmartFee(
                            smartFees,
                            account.get(p1.getPaymentType()).getAccountNo(),
                            p1.getInvoiceAmountPaid()
                                .subtract(p1.getCreditMemo().subtract(p1.getWtxCreditMemo())),
                            isSameday),
                        calculateSmartFee(
                            smartFees,
                            account.get(p2.getPaymentType()).getAccountNo(),
                            p2.getInvoiceAmountPaid()
                                .subtract(p2.getCreditMemo().subtract(p1.getWtxCreditMemo())),
                            isSameday))
                    .result());
        SmartFileHeader smartFileHeader = null;
        SmartFileBatch smartFileBatch = null;
        BigDecimal sumSmart = BigDecimal.ZERO;
        BigDecimal sumSmartPerBatch = BigDecimal.ZERO;
        BigDecimal sumFee = BigDecimal.ZERO;
        BigDecimal sumFeePerBatch = BigDecimal.ZERO;
        int sumAllBatch = 0;
        int countSmart = 0;
        String paymentType = "";
        BigDecimal smartFee = null;
        Account account = null;
        for (int i = 0; i < smarts.size(); i++) {
          ProposalLog proposalLog = new ProposalLog();
          PrepareRunDocument paymentRealRun = smarts.get(i);

          if (paymentType.equalsIgnoreCase("")) {
            paymentType = paymentRealRun.getPaymentType();
          }
          if (null == smartFee) {
            account = this.account.get(paymentType);
            smartFee =
                calculateSmartFee(
                    smartFees,
                    account.getAccountNo(),
                    paymentRealRun
                        .getInvoiceAmountPaid()
                        .subtract(
                            paymentRealRun
                                .getCreditMemo()
                                .subtract(paymentRealRun.getWtxCreditMemo())),
                    isSameday);
          }
          if (null == smartFileHeader) {
            smartFileHeader = generateSmartFileHeader(generateFileAlias, isTestRun);
          }
          if (countSmart == 0) {
            smartFileBatch =
                generateSmartFileBatch(
                    isTestRun, generateFileAlias.getSmartDate(), smartFileHeader);
          } else if (countSmart % 10000 == 0
              || !paymentType.equalsIgnoreCase(paymentRealRun.getPaymentType())
              || smartFee.compareTo(
                      calculateSmartFee(
                          smartFees,
                          account.getAccountNo(),
                          paymentRealRun
                              .getInvoiceAmountPaid()
                              .subtract(
                                  paymentRealRun
                                      .getCreditMemo()
                                      .subtract(paymentRealRun.getWtxCreditMemo())),
                          isSameday))
                  != 0) {
            paymentType = paymentRealRun.getPaymentType();
            account = this.account.get(paymentType);
            smartFee =
                calculateSmartFee(
                    smartFees,
                    account.getAccountNo(),
                    paymentRealRun
                        .getInvoiceAmountPaid()
                        .subtract(
                            paymentRealRun
                                .getCreditMemo()
                                .subtract(paymentRealRun.getWtxCreditMemo())),
                    isSameday);
            sumAllBatch += countSmart;
            smartFileBatch.setBatchNum(String.valueOf(countSmart));
            smartFileBatch.setTotalNum(String.format("%06d", countSmart));
            long sum = sumSmartPerBatch.multiply(Util.getBigDecimal(100)).longValue();
            smartFileBatch.setTotalAmount(StringUtils.leftPad(String.valueOf(sum), 15, "0"));
            smartFileBatch.setFee(df.format(sumFeePerBatch));
            smartFileBatch.setTotal(df.format(sumSmartPerBatch.add(sumFeePerBatch)));
            SmartFileGM smartFileGM =
                new SmartFileGM(
                    StringUtils.rightPad("ECGD01", 10, " "),
                    "",
                    StringUtils.leftPad(df.format(sumSmartPerBatch), 25, " "),
                    StringUtils.leftPad(df.format(sumFeePerBatch), 25, " "),
                    StringUtils.leftPad(df.format(sumSmartPerBatch.add(sumFeePerBatch)), 25, " "),
                    StringUtils.leftPad(String.valueOf(countSmart), 25, " "));
            generateFileAlias.addSmartFileGM(smartFileGM);
            sumSmartPerBatch = BigDecimal.ZERO;
            sumFeePerBatch = BigDecimal.ZERO;
            countSmart = 0;
            smartFileHeader.addSmartFileBatch(smartFileBatch);
            smartFileBatch =
                generateSmartFileBatch(
                    isTestRun, generateFileAlias.getSmartDate(), smartFileHeader);
          }
          countSmart++;
          sumFee = sumFee.add(smartFee);
          sumFeePerBatch = sumFeePerBatch.add(smartFee);
          sumSmart =
              sumSmart.add(
                  paymentRealRun
                      .getInvoiceAmountPaid()
                      .subtract(
                          paymentRealRun
                              .getCreditMemo()
                              .subtract(paymentRealRun.getWtxCreditMemo()))
                      .subtract(smartFee));
          sumSmartPerBatch =
              sumSmartPerBatch.add(
                  paymentRealRun
                      .getInvoiceAmountPaid()
                      .subtract(
                          paymentRealRun
                              .getCreditMemo()
                              .subtract(paymentRealRun.getWtxCreditMemo()))
                      .subtract(smartFee));
          SmartFileLog smartFileLog =
              generateSmartFileLog(paymentRealRun, generateFileAlias, smartFee);
          SmartFileDetail smartFileDetail =
              generateSmartFileDetail(
                  paymentRealRun,
                  smartFileBatch.getBatchNum(),
                  generateFileAlias.getSmartDate(),
                  smartFee,
                  usePain);
          smartFileDetail.setSmartFileLog(smartFileLog);
          smartFileBatch.addSmartFileDetail(smartFileDetail);
          proposalLog.setAmount(
              paymentRealRun
                  .getInvoiceAmountPaid()
                  .subtract(
                      paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
          proposalLog.setPayAccount(paymentType);
          proposalLog.setBankKey(paymentRealRun.getPayeeBankKey());
          if (usePain) {
            proposalLog.setFileType("PAIN001");
          } else {
            proposalLog.setFileType("SMART");
          }
          proposalLogs.add(proposalLog);

          if (i == smarts.size() - 1) {
            sumAllBatch += countSmart;
            smartFileBatch.setTotalNum(String.format("%06d", countSmart));
            //                        log.info("sumSmartPerBatch 2 : {}", sumSmartPerBatch);
            long sum = sumSmartPerBatch.multiply(Util.getBigDecimal(100)).longValue();
            smartFileBatch.setTotalAmount(StringUtils.leftPad(String.valueOf(sum), 15, "0"));
            smartFileBatch.setFee(df.format(sumFeePerBatch));
            smartFileBatch.setTotal(df.format(sumSmartPerBatch.add(sumFeePerBatch)));
            smartFileHeader.addSmartFileBatch(smartFileBatch);
            SmartFileGM smartFileGM =
                new SmartFileGM(
                    StringUtils.rightPad("ECGD01", 10, " "),
                    "",
                    StringUtils.leftPad(df.format(sumSmartPerBatch), 25, " "),
                    StringUtils.leftPad(df.format(sumFeePerBatch), 25, " "),
                    StringUtils.leftPad(df.format(sumSmartPerBatch.add(sumFeePerBatch)), 25, " "),
                    StringUtils.leftPad(String.valueOf(countSmart), 25, " "));
            generateFileAlias.addSmartFileGM(smartFileGM);
          }
        }
        SmartFileFooter smartFileFooter =
            generateSmartFileFooter(smartFileHeader, sumAllBatch, sumSmart);
        smartFileHeader.setSmartFileFooter(smartFileFooter);
        generateFileAlias.setSmartFileHeader(smartFileHeader);
      }

      if (!smartsSumFile.isEmpty()) {
        //                log.info("smart");
        SmartFileHeader smartFileHeader = null;
        SmartFileBatch smartFileBatch = null;
        BigDecimal sumSmart = BigDecimal.ZERO;
        BigDecimal sumSmartPerBatch = BigDecimal.ZERO;
        BigDecimal sumFeePerBatch = BigDecimal.ZERO;
        BigDecimal sumFee = BigDecimal.ZERO;
        BigDecimal sumFileFee = BigDecimal.ZERO;
        BigDecimal sumFileFeeGM = BigDecimal.ZERO;
        int sumAllBatch = 0;
        int countSmart = 0;
        Map<String, List<PrepareRunDocument>> groupByVendor =
            smartsSumFile.stream()
                .collect(
                    Collectors.groupingBy(
                        this::groupByMultipleKey,
                        Collectors.mapping((PrepareRunDocument p) -> p, toList())));
        int record = 0;
        int sumFile = 0;
        DecimalFormat df = new DecimalFormat("0.00##");
        for (var entry : groupByVendor.entrySet()) {
          BigDecimal totalTransferAmount =
              entry.getValue().stream()
                  .map(PrepareRunDocument::getInvoiceAmountPaid) // map
                  .reduce(BigDecimal.ZERO, BigDecimal::add);
          boolean isGPF =
              checkGPF(
                  entry.getValue().get(0).getOriginalCompanyCode(),
                  entry.getValue().get(0).getOriginalDocumentType(),
                  entry.getValue().get(0).getAccountType(),
                  entry.getValue().get(0).getPaymentMethod(),
                  entry.getValue().get(0).getCostCenter(),
                  entry.getValue().get(0).getPaymentReference());
          String paymentType = checkPaymentType(entry.getValue().get(0).getPaymentMethod(), isGPF);
          Account account = this.account.get(paymentType);
          BigDecimal sumFileFeePerVendor =
              calculateSmartFee(smartFees, account.getAccountNo(), totalTransferAmount, isSameday);
          if (entry.getValue().size() > 1) {
            sumFileFee = sumFileFee.add(sumFileFeePerVendor);
          }
          sumFileFeeGM = sumFileFeeGM.add(sumFileFeePerVendor);
          sumFile++;
          for (int i = 0; i < entry.getValue().size(); i++) {
            ProposalLog proposalLog = new ProposalLog();
            PrepareRunDocument paymentRealRun = entry.getValue().get(i);
            if (null == smartFileHeader) {
              smartFileHeader = generateSmartFileHeader(generateFileAlias, isTestRun);
            }
            if (countSmart == 0) {
              smartFileBatch =
                  generateSmartFileBatch(
                      isTestRun, generateFileAlias.getSmartDate(), smartFileHeader);
            } else if (countSmart % 10000000 == 0) {
              sumAllBatch += countSmart;
              smartFileBatch.setBatchNum("10000000");
              int sum = sumSmartPerBatch.multiply(Util.getBigDecimal(100)).intValue();
              smartFileBatch.setTotalAmount(StringUtils.leftPad(String.valueOf(sum), 15, "0"));
              SmartFileGM smartFileGM = null;
              if (sumFee.compareTo(BigDecimal.ZERO) == 0) {
                smartFileGM =
                    new SmartFileGM(
                        StringUtils.rightPad("ECGD01", 10, " "),
                        "",
                        StringUtils.leftPad(
                            df.format(sumSmartPerBatch.add(sumFileFee).subtract(sumFileFeeGM)),
                            25,
                            " "),
                        StringUtils.leftPad(df.format(sumFileFeeGM), 25, " "),
                        StringUtils.leftPad(df.format(sumSmartPerBatch.add(sumFileFee)), 25, " "),
                        StringUtils.leftPad(String.valueOf(sumFile), 25, " "));
              } else {
                smartFileGM =
                    new SmartFileGM(
                        StringUtils.rightPad("ECGD01", 10, " "),
                        "",
                        StringUtils.leftPad(df.format(sumSmartPerBatch), 25, " "),
                        StringUtils.leftPad(df.format(sumFeePerBatch), 25, " "),
                        StringUtils.leftPad(
                            df.format(sumSmartPerBatch.add(sumFeePerBatch)), 25, " "),
                        StringUtils.leftPad(String.valueOf(sumFile), 25, " "));
              }
              generateFileAlias.addSmartFileGM(smartFileGM);
              sumSmartPerBatch = BigDecimal.ZERO;
              sumFeePerBatch = BigDecimal.ZERO;
              countSmart = 0;
              smartFileHeader.addSmartFileBatch(smartFileBatch);
              smartFileBatch =
                  generateSmartFileBatch(
                      isTestRun, generateFileAlias.getSmartDate(), smartFileHeader);
            }
            countSmart++;
            BigDecimal smartFee = BigDecimal.ZERO;
            if (entry.getValue().size() == 1) {
              smartFee =
                  calculateSmartFee(
                      smartFees,
                      account.getAccountNo(),
                      paymentRealRun
                          .getInvoiceAmountPaid()
                          .subtract(
                              paymentRealRun
                                  .getCreditMemo()
                                  .subtract(paymentRealRun.getWtxCreditMemo())),
                      isSameday);
            }
            sumFee = sumFee.add(smartFee);
            sumFeePerBatch = sumFeePerBatch.add(smartFee);
            sumSmart =
                sumSmart.add(
                    paymentRealRun
                        .getInvoiceAmountPaid()
                        .subtract(
                            paymentRealRun
                                .getCreditMemo()
                                .subtract(paymentRealRun.getWtxCreditMemo()))
                        .subtract(smartFee));
            sumSmartPerBatch =
                sumSmartPerBatch.add(
                    paymentRealRun
                        .getInvoiceAmountPaid()
                        .subtract(
                            paymentRealRun
                                .getCreditMemo()
                                .subtract(paymentRealRun.getWtxCreditMemo()))
                        .subtract(smartFee));
            SmartFileLog smartFileLog =
                generateSmartFileLog(paymentRealRun, generateFileAlias, smartFee);
            smartFileLog.setSumFileFee(sumFileFeePerVendor);
            SmartFileDetail smartFileDetail =
                generateSmartFileDetailSum(
                    paymentRealRun,
                    smartFileBatch.getBatchNum(),
                    generateFileAlias.getSmartDate(),
                    smartFee,
                    usePain);
            smartFileDetail.setSmartFileLog(smartFileLog);
            smartFileBatch.addSmartFileDetail(smartFileDetail);
            BigDecimal amount =
                paymentRealRun
                    .getInvoiceAmountPaid()
                    .subtract(
                        paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()));
            proposalLog.setAmount(amount);
            proposalLog.setPayAccount(paymentType);
            proposalLog.setBankKey(paymentRealRun.getPayeeBankKey());
            proposalLog.setOriginalDocNo(paymentRealRun.getOriginalDocumentNo());
            if (usePain) {
              proposalLog.setFileType("PAIN001");
            } else {
              proposalLog.setFileType("SMART");
            }
            proposalLogs.add(proposalLog);

            if (record == smartsSumFile.size() - 1) {
              sumAllBatch += countSmart;
              smartFileBatch.setTotalNum(String.format("%03d", countSmart));
              int sum = sumSmartPerBatch.multiply(Util.getBigDecimal(100)).intValue();
              smartFileBatch.setTotalAmount(StringUtils.leftPad(String.valueOf(sum), 15, "0"));
              smartFileHeader.addSmartFileBatch(smartFileBatch);
              SmartFileGM smartFileGM = null;
              if (sumFee.compareTo(BigDecimal.ZERO) == 0) {
                smartFileGM =
                    new SmartFileGM(
                        StringUtils.rightPad("ECGD01", 10, " "),
                        "",
                        StringUtils.leftPad(
                            df.format(sumSmartPerBatch.add(sumFileFee).subtract(sumFileFeeGM)),
                            25,
                            " "),
                        StringUtils.leftPad(df.format(sumFileFeeGM), 25, " "),
                        StringUtils.leftPad(df.format(sumSmartPerBatch.add(sumFileFee)), 25, " "),
                        StringUtils.leftPad(String.valueOf(sumFile), 25, " "));
              } else {
                smartFileGM =
                    new SmartFileGM(
                        StringUtils.rightPad("ECGD01", 10, " "),
                        "",
                        StringUtils.leftPad(df.format(sumSmartPerBatch), 25, " "),
                        StringUtils.leftPad(df.format(sumFeePerBatch), 25, " "),
                        StringUtils.leftPad(
                            df.format(sumSmartPerBatch.add(sumFeePerBatch)), 25, " "),
                        StringUtils.leftPad(String.valueOf(sumFile), 25, " "));
              }
              generateFileAlias.addSmartFileGM(smartFileGM);
            }
            record++;
          }
        }
        SmartFileFooter smartFileFooter =
            generateSmartFileFooter(smartFileHeader, sumAllBatch, sumSmart.subtract(sumFileFee));
        smartFileHeader.setSmartFileFooter(smartFileFooter);
        generateFileAlias.setSmartFileHeaderSumFile(smartFileHeader);
      }

      if (!swifts.isEmpty()) {
        BigDecimal sumSwift = BigDecimal.ZERO;
        int countSwift = 0;
        for (PrepareRunDocument swift : swifts) {
          ProposalLog proposalLog = new ProposalLog();
          boolean isGPF =
              checkGPF(
                  swift.getOriginalCompanyCode(),
                  swift.getOriginalDocumentType(),
                  swift.getAccountType(),
                  swift.getPaymentMethod(),
                  swift.getCostCenter(),
                  swift.getPaymentReference());
          String paymentType = checkPaymentType(swift.getPaymentMethod(), isGPF);
          SwiftFile swiftFile = generateSwiftFile(swift, generateFileAlias, bankCodes, false, isPac);
          SwiftFileLog swiftFileLog = generateSwiftFileLog(swift);
          swiftFile.setSwiftFileLog(swiftFileLog);
          swiftFiles.add(swiftFile);
          countSwift++;
          sumSwift =
              sumSwift.add(
                  swift
                      .getInvoiceAmountPaid()
                      .subtract(
                          null == swift.getCreditMemo() ? BigDecimal.ZERO : swift.getCreditMemo()));
          proposalLog.setAmount(
              swift
                  .getInvoiceAmountPaid()
                  .subtract(
                      null == swift.getCreditMemo() ? BigDecimal.ZERO : swift.getCreditMemo()));
          proposalLog.setPayAccount(paymentType);
          proposalLog.setBankKey(swift.getPayeeBankKey());
          proposalLog.setFileType("SWIFT");
          proposalLogs.add(proposalLog);
        }
      }
      if (!swiftsSumFile.isEmpty()) {
        BigDecimal sumSwift = BigDecimal.ZERO;
        for (PrepareRunDocument swift : swiftsSumFile) {
          ProposalLog proposalLog = new ProposalLog();
          boolean isGPF =
              checkGPF(
                  swift.getOriginalCompanyCode(),
                  swift.getOriginalDocumentType(),
                  swift.getAccountType(),
                  swift.getPaymentMethod(),
                  swift.getCostCenter(),
                  swift.getPaymentReference());
          String paymentType = checkPaymentType(swift.getPaymentMethod(), isGPF);
          SwiftFile swiftFile = generateSwiftFile(swift, generateFileAlias, bankCodes, true, isPac);
          SwiftFileLog swiftFileLog = generateSwiftFileLog(swift);
          swiftFile.setSwiftFileLog(swiftFileLog);
          swiftFilesSumFile.add(swiftFile);
          sumSwift =
              sumSwift.add(
                  swift
                      .getInvoiceAmountPaid()
                      .subtract(
                          null == swift.getCreditMemo() ? BigDecimal.ZERO : swift.getCreditMemo()));
          proposalLog.setAmount(
              swift
                  .getInvoiceAmountPaid()
                  .subtract(
                      null == swift.getCreditMemo() ? BigDecimal.ZERO : swift.getCreditMemo()));
          proposalLog.setPayAccount(paymentType);
          proposalLog.setBankKey(swift.getPayeeBankKey());
          proposalLog.setFileType("SWIFT");
          proposalLogs.add(proposalLog);
        }
      }

      if (!inhouses.isEmpty()) {
        List<InhouseFileDetail> inhouseFileDetails = new ArrayList<>();
        InhouseFileHeader inhouseFileHeader = null;
        String payeeBankKey = "";
        String inhouseBatchNum = "";
        String inhouseRunning = "";
        int seqNoFileInhouse = 0;
        int seqNoInhouse = 0;
        inhouses.sort(Comparator.comparing(PrepareRunDocument::getPayeeBankKey));
        String bankKey = "";
        String paymentMethod = "";
        int countInhou = 0;
        for (int i = 0; i < inhouses.size(); i++) {
          ProposalLog proposalLog = new ProposalLog();
          PrepareRunDocument paymentRealRun = inhouses.get(i);
          boolean isGPF =
              checkGPF(
                  paymentRealRun.getOriginalCompanyCode(),
                  paymentRealRun.getOriginalDocumentType(),
                  paymentRealRun.getAccountType(),
                  paymentRealRun.getPaymentMethod(),
                  paymentRealRun.getCostCenter(),
                  paymentRealRun.getPaymentReference());
          String paymentType = checkPaymentType(paymentRealRun.getPaymentMethod(), isGPF);
          String fileType =
              paymentType.equalsIgnoreCase("D3")
                      || paymentType.equalsIgnoreCase("D4")
                      || paymentType.equalsIgnoreCase("D5")
                  ? "A1"
                  : "A2";
          String userTref =
              paymentType.equalsIgnoreCase("D3")
                      || paymentType.equalsIgnoreCase("D4")
                      || paymentType.equalsIgnoreCase("D5")
                  ? "A1"
                  : paymentType.equalsIgnoreCase("D1") ? "A2" : "A2";

          if (payeeBankKey.equalsIgnoreCase("")) {
            payeeBankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
            String seqName = "";
            if (payeeBankKey.equalsIgnoreCase("011")) {
              seqName = "INHOUSE_TMB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("030")) {
              seqName = "INHOUSE_GSB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("034")) {
              seqName = "INHOUSE_BAAC_SEQ";
            }
            if (isTestRun) {
              inhouseRunning = "$$$";
            } else {
              Long running =
                  generateFileSequenceService.getCurrentSeq(
                      seqName, generateFileAlias.getInhouseDate());
              inhouseRunning = StringUtils.leftPad(String.valueOf(running), 3, "0");
            }
            if ("A1".equalsIgnoreCase(userTref)) {
              inhouseBatchNum = "I" + "P" + getRound() + inhouseRunning;
            } else {
              inhouseBatchNum = "I" + "S" + getRound() + inhouseRunning;
            }
            inhouseFileHeader =
                generateInhouseFileHeader(
                    generateFileAlias,
                    bankCodeMap.get(payeeBankKey),
                    inhouseRunning,
                    inhouseBatchNum,
                    paymentRealRun.getPayeeBankKey(),
                    paymentRealRun.getVendorName(),
                    paymentRealRun.getPaymentDate(),
                    userTref,
                    isTestRun);
            ++seqNoFileInhouse;
            seqNoInhouse = 1;
            bankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
            paymentMethod = paymentRealRun.getPaymentMethod();
          } else if (!payeeBankKey.equalsIgnoreCase(
              paymentRealRun.getPayeeBankKey().substring(0, 3))) {
            int numDr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoDr())
                        .count();
            int numCr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoCr())
                        .count();
            BigDecimal amtDr =
                inhouseFileDetails.stream()
                    .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoDr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtDr()), BigDecimal::add);
            BigDecimal amtCr =
                inhouseFileDetails.stream()
                    .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoCr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtCr()), BigDecimal::add);
            InhouseFileTrailer inhouseFileTrailer =
                generateInhouseFileTrailer(
                    bankCodeMap.get(bankKey),
                    fileType,
                    bankKey,
                    paymentMethod,
                    numDr,
                    amtDr,
                    numCr,
                    amtCr,
                    ++seqNoInhouse);
            bankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
            paymentMethod = paymentRealRun.getPaymentMethod();
            payeeBankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
            String seqName = "";
            if (payeeBankKey.equalsIgnoreCase("011")) {
              seqName = "INHOUSE_TMB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("030")) {
              seqName = "INHOUSE_GSB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("034")) {
              seqName = "INHOUSE_BAAC_SEQ";
            }
            if (isTestRun) {
              inhouseRunning = "$$$";
            } else {
              Long running =
                  generateFileSequenceService.getCurrentSeq(
                      seqName, generateFileAlias.getInhouseDate());
              inhouseRunning = StringUtils.leftPad(String.valueOf(running), 3, "0");
            }
            if ("A1".equalsIgnoreCase(userTref)) {
              inhouseBatchNum = "I" + "P" + getRound() + inhouseRunning;
            } else {
              inhouseBatchNum = "I" + "S" + getRound() + inhouseRunning;
            }
            inhouseFileHeader.setInhouseFileDetails(inhouseFileDetails);
            inhouseFileHeader.setInhouseFileTrailer(inhouseFileTrailer);
            generateFileAlias.addInhouseHeader(inhouseFileHeader);
            inhouseFileHeader =
                generateInhouseFileHeader(
                    generateFileAlias,
                    bankCodeMap.get(payeeBankKey),
                    inhouseRunning,
                    inhouseBatchNum,
                    paymentRealRun.getPayeeBankKey(),
                    paymentRealRun.getVendorName(),
                    paymentRealRun.getPaymentDate(),
                    userTref,
                    isTestRun);
            inhouseFileDetails = new ArrayList<>();
            seqNoInhouse = 1;
          }
          if (countInhou != 0 && countInhou % 30000 == 0) {
            countInhou = 0;
            int numDr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoDr())
                        .count();
            int numCr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoCr())
                        .count();
            BigDecimal amtDr =
                inhouseFileDetails.stream()
                    .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoDr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtDr()), BigDecimal::add);
            BigDecimal amtCr =
                inhouseFileDetails.stream()
                    .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoCr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtCr()), BigDecimal::add);
            InhouseFileTrailer inhouseFileTrailer =
                generateInhouseFileTrailer(
                    bankCodeMap.get(payeeBankKey),
                    fileType,
                    bankKey,
                    paymentMethod,
                    numDr,
                    amtDr,
                    numCr,
                    amtCr,
                    ++seqNoInhouse);
            inhouseFileHeader.setInhouseFileDetails(inhouseFileDetails);
            inhouseFileHeader.setInhouseFileTrailer(inhouseFileTrailer);
            generateFileAlias.addInhouseHeader(inhouseFileHeader);
            payeeBankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
            String seqName = "";
            if (payeeBankKey.equalsIgnoreCase("011")) {
              seqName = "INHOUSE_TMB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("030")) {
              seqName = "INHOUSE_GSB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("034")) {
              seqName = "INHOUSE_BAAC_SEQ";
            }
            if (isTestRun) {
              inhouseRunning = "$$$";
            } else {
              Long running =
                  generateFileSequenceService.getCurrentSeq(
                      seqName, generateFileAlias.getInhouseDate());
              inhouseRunning = StringUtils.leftPad(String.valueOf(running), 3, "0");
            }
            if ("A1".equalsIgnoreCase(userTref)) {
              inhouseBatchNum = "I" + "P" + getRound() + inhouseRunning;
            } else {
              inhouseBatchNum = "I" + "S" + getRound() + inhouseRunning;
            }
            inhouseFileHeader =
                generateInhouseFileHeader(
                    generateFileAlias,
                    bankCodeMap.get(payeeBankKey),
                    inhouseRunning,
                    inhouseBatchNum,
                    paymentRealRun.getPayeeBankKey(),
                    paymentRealRun.getVendorName(),
                    paymentRealRun.getPaymentDate(),
                    userTref,
                    isTestRun);
            inhouseFileDetails = new ArrayList<>();
            seqNoInhouse = 1;
          }
          InhouseFileDetail inhouseFileDetail =
              generateInhouseFileDetail(
                  paymentRealRun, inhouseBatchNum, seqNoFileInhouse, ++seqNoInhouse, isTestRun);
          inhouseFileDetails.add(inhouseFileDetail);
          proposalLog.setAmount(
              paymentRealRun
                  .getInvoiceAmountPaid()
                  .subtract(
                      paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
          proposalLog.setPayAccount(paymentType);
          proposalLog.setBankKey(paymentRealRun.getPayeeBankKey());
          proposalLog.setFileType("INHOU");
          proposalLogs.add(proposalLog);
          countInhou++;

          if (i == inhouses.size() - 1) {
            int numDr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFile -> 1 == inhouseFile.getTmpNoDr())
                        .count();
            int numCr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFile -> 1 == inhouseFile.getTmpNoCr())
                        .count();
            BigDecimal amtDr =
                inhouseFileDetails.stream()
                    .filter(inhouseFile -> 1 == inhouseFile.getTmpNoDr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtDr()), BigDecimal::add);
            BigDecimal amtCr =
                inhouseFileDetails.stream()
                    .filter(inhouseFile -> 1 == inhouseFile.getTmpNoCr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtCr()), BigDecimal::add);
            InhouseFileTrailer inhouseFileTrailer =
                generateInhouseFileTrailer(
                    bankCodeMap.get(bankKey),
                    fileType,
                    bankKey,
                    paymentMethod,
                    numDr,
                    amtDr,
                    numCr,
                    amtCr,
                    ++seqNoInhouse);
            inhouseFileHeader.setInhouseFileDetails(inhouseFileDetails);
            inhouseFileHeader.setInhouseFileTrailer(inhouseFileTrailer);
            generateFileAlias.addInhouseHeader(inhouseFileHeader);
          }
        }
      }

      if (!inhousesSumFile.isEmpty()) {
        List<InhouseFileDetail> inhouseFileDetails = new ArrayList<>();
        InhouseFileHeader inhouseFileHeader = null;
        String payeeBankKey = "";
        String inhouseBatchNum = "";
        String inhouseRunning = "";
        int seqNoFileInhouse = 0;
        int seqNoInhouse = 0;
        String bankKey = "";
        String paymentMethod = "";
        int countInhou = 0;
        inhousesSumFile.sort(Comparator.comparing(PrepareRunDocument::getPayeeBankKey));
        for (int i = 0; i < inhousesSumFile.size(); i++) {
          ProposalLog proposalLog = new ProposalLog();
          PrepareRunDocument paymentRealRun = inhousesSumFile.get(i);
          boolean isGPF =
              checkGPF(
                  paymentRealRun.getOriginalCompanyCode(),
                  paymentRealRun.getOriginalDocumentType(),
                  paymentRealRun.getAccountType(),
                  paymentRealRun.getPaymentMethod(),
                  paymentRealRun.getCostCenter(),
                  paymentRealRun.getPaymentReference());
          String paymentType = checkPaymentType(paymentRealRun.getPaymentMethod(), isGPF);
          String fileType =
              paymentType.equalsIgnoreCase("D3")
                      || paymentType.equalsIgnoreCase("D4")
                      || paymentType.equalsIgnoreCase("D5")
                  ? "A1"
                  : "A2";
          String userTref =
              paymentType.equalsIgnoreCase("D3")
                      || paymentType.equalsIgnoreCase("D4")
                      || paymentType.equalsIgnoreCase("D5")
                  ? "A1"
                  : paymentType.equalsIgnoreCase("D1") ? "A2" : "A2";

          if (payeeBankKey.equalsIgnoreCase("")) {
            payeeBankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
            String seqName = "";
            if (payeeBankKey.equalsIgnoreCase("011")) {
              seqName = "INHOUSE_TMB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("030")) {
              seqName = "INHOUSE_GSB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("034")) {
              seqName = "INHOUSE_BAAC_SEQ";
            }
            if (isTestRun) {
              inhouseRunning = "$$$";
            } else {
              Long running =
                  generateFileSequenceService.getCurrentSeq(
                      seqName, generateFileAlias.getInhouseDate());
              inhouseRunning = StringUtils.leftPad(String.valueOf(running), 3, "0");
            }
            if ("A1".equalsIgnoreCase(userTref)) {
              inhouseBatchNum = "I" + "P" + getRound() + inhouseRunning;
            } else {
              inhouseBatchNum = "I" + "S" + getRound() + inhouseRunning;
            }
            inhouseFileHeader =
                generateInhouseFileHeader(
                    generateFileAlias,
                    bankCodeMap.get(payeeBankKey),
                    inhouseRunning,
                    inhouseBatchNum,
                    paymentRealRun.getPayeeBankKey(),
                    paymentRealRun.getVendorName(),
                    paymentRealRun.getPaymentDate(),
                    userTref,
                    isTestRun);
            ++seqNoFileInhouse;
            seqNoInhouse = 1;
            bankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
            paymentMethod = paymentRealRun.getPaymentMethod();
          } else if (!payeeBankKey.equalsIgnoreCase(
              paymentRealRun.getPayeeBankKey().substring(0, 3))) {
            int numDr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoDr())
                        .count();
            int numCr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoCr())
                        .count();
            BigDecimal amtDr =
                inhouseFileDetails.stream()
                    .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoDr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtDr()), BigDecimal::add);
            BigDecimal amtCr =
                inhouseFileDetails.stream()
                    .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoCr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtCr()), BigDecimal::add);
            InhouseFileTrailer inhouseFileTrailer =
                generateInhouseFileTrailer(
                    bankCodeMap.get(bankKey),
                    fileType,
                    bankKey,
                    paymentMethod,
                    numDr,
                    amtDr,
                    numCr,
                    amtCr,
                    ++seqNoInhouse);
            bankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
            paymentMethod = paymentRealRun.getPaymentMethod();
            payeeBankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
            String seqName = "";
            if (payeeBankKey.equalsIgnoreCase("011")) {
              seqName = "INHOUSE_TMB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("030")) {
              seqName = "INHOUSE_GSB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("034")) {
              seqName = "INHOUSE_BAAC_SEQ";
            }
            if (isTestRun) {
              inhouseRunning = "$$$";
            } else {
              Long running =
                  generateFileSequenceService.getCurrentSeq(
                      seqName, generateFileAlias.getInhouseDate());
              inhouseRunning = StringUtils.leftPad(String.valueOf(running), 3, "0");
            }
            if ("A1".equalsIgnoreCase(userTref)) {
              inhouseBatchNum = "I" + "P" + getRound() + inhouseRunning;
            } else {
              inhouseBatchNum = "I" + "S" + getRound() + inhouseRunning;
            }
            inhouseFileHeader.setInhouseFileDetails(inhouseFileDetails);
            inhouseFileHeader.setInhouseFileTrailer(inhouseFileTrailer);
            generateFileAlias.addInhouseHeaderSumFile(inhouseFileHeader);
            inhouseFileHeader =
                generateInhouseFileHeader(
                    generateFileAlias,
                    bankCodeMap.get(payeeBankKey),
                    inhouseRunning,
                    inhouseBatchNum,
                    paymentRealRun.getPayeeBankKey(),
                    paymentRealRun.getVendorName(),
                    paymentRealRun.getPaymentDate(),
                    userTref,
                    isTestRun);
            inhouseFileDetails = new ArrayList<>();
            seqNoInhouse = 1;
          }
          if (countInhou != 0 && countInhou % 10000000 == 0) {
            countInhou = 0;
            int numDr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoDr())
                        .count();
            int numCr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoCr())
                        .count();
            BigDecimal amtDr =
                inhouseFileDetails.stream()
                    .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoDr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtDr()), BigDecimal::add);
            BigDecimal amtCr =
                inhouseFileDetails.stream()
                    .filter(inhouseFileDetail -> 1 == inhouseFileDetail.getTmpNoCr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtCr()), BigDecimal::add);
            InhouseFileTrailer inhouseFileTrailer =
                generateInhouseFileTrailer(
                    bankCodeMap.get(bankKey),
                    fileType,
                    bankKey,
                    paymentMethod,
                    numDr,
                    amtDr,
                    numCr,
                    amtCr,
                    ++seqNoInhouse);
            inhouseFileHeader.setInhouseFileDetails(inhouseFileDetails);
            inhouseFileHeader.setInhouseFileTrailer(inhouseFileTrailer);
            generateFileAlias.addInhouseHeaderSumFile(inhouseFileHeader);
            payeeBankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
            String seqName = "";
            if (payeeBankKey.equalsIgnoreCase("011")) {
              seqName = "INHOUSE_TMB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("030")) {
              seqName = "INHOUSE_GSB_SEQ";
            } else if (payeeBankKey.equalsIgnoreCase("034")) {
              seqName = "INHOUSE_BAAC_SEQ";
            }
            if (isTestRun) {
              inhouseRunning = "$$$";
            } else {
              Long running =
                  generateFileSequenceService.getCurrentSeq(
                      seqName, generateFileAlias.getInhouseDate());
              inhouseRunning = StringUtils.leftPad(String.valueOf(running), 3, "0");
            }
            if ("A1".equalsIgnoreCase(userTref)) {
              inhouseBatchNum = "I" + "P" + getRound() + inhouseRunning;
            } else {
              inhouseBatchNum = "I" + "S" + getRound() + inhouseRunning;
            }
            inhouseFileHeader =
                generateInhouseFileHeader(
                    generateFileAlias,
                    bankCodeMap.get(payeeBankKey),
                    inhouseRunning,
                    inhouseBatchNum,
                    paymentRealRun.getPayeeBankKey(),
                    paymentRealRun.getVendorName(),
                    paymentRealRun.getPaymentDate(),
                    userTref,
                    isTestRun);

            inhouseFileDetails = new ArrayList<>();
            seqNoInhouse = 1;
          }

          InhouseFileDetail inhouseFileDetail =
              generateInhouseFileDetail(
                  paymentRealRun, inhouseBatchNum, seqNoFileInhouse, ++seqNoInhouse, isTestRun);
          inhouseFileDetails.add(inhouseFileDetail);
          BigDecimal amount =
              paymentRealRun
                  .getInvoiceAmountPaid()
                  .subtract(
                      paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()));
          proposalLog.setAmount(
              paymentRealRun
                  .getInvoiceAmountPaid()
                  .subtract(
                      paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
          proposalLog.setPayAccount(paymentType);
          proposalLog.setBankKey(paymentRealRun.getPayeeBankKey());
          proposalLog.setFileType("INHOU");
          proposalLogs.add(proposalLog);
          countInhou++;

          if (i == inhousesSumFile.size() - 1) {
            int numDr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFile -> 1 == inhouseFile.getTmpNoDr())
                        .count();
            int numCr =
                (int)
                    inhouseFileDetails.stream()
                        .filter(inhouseFile -> 1 == inhouseFile.getTmpNoCr())
                        .count();
            BigDecimal amtDr =
                inhouseFileDetails.stream()
                    .filter(inhouseFile -> 1 == inhouseFile.getTmpNoDr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtDr()), BigDecimal::add);
            BigDecimal amtCr =
                inhouseFileDetails.stream()
                    .filter(inhouseFile -> 1 == inhouseFile.getTmpNoCr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtCr()), BigDecimal::add);
            InhouseFileTrailer inhouseFileTrailer =
                generateInhouseFileTrailer(
                    bankCodeMap.get(bankKey),
                    fileType,
                    bankKey,
                    paymentMethod,
                    numDr,
                    amtDr,
                    numCr,
                    amtCr,
                    ++seqNoInhouse);
            inhouseFileHeader.setInhouseFileDetails(inhouseFileDetails);
            inhouseFileHeader.setInhouseFileTrailer(inhouseFileTrailer);
            generateFileAlias.addInhouseHeaderSumFile(inhouseFileHeader);
          }
        }
      }

      if (!giros.isEmpty()) {
        String bankKey = "";
        String vendor = "";
        int countGiro = 0;
        int seqNoFileGIRO = 0;
        int seqNoGIRO = 0;
        String giroBatchNum = "";
        String giroRunning = "";
        GIROFileHeader giroFileHeader = null;
        List<GIROFileDetail> giroFileDetails = new ArrayList<>();
        for (int i = 0; i < giros.size(); i++) {
          ProposalLog proposalLog = new ProposalLog();
          PrepareRunDocument paymentRealRun = giros.get(i);
          bankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
          boolean isGPF =
              checkGPF(
                  paymentRealRun.getOriginalCompanyCode(),
                  paymentRealRun.getOriginalDocumentType(),
                  paymentRealRun.getAccountType(),
                  paymentRealRun.getPaymentMethod(),
                  paymentRealRun.getCostCenter(),
                  paymentRealRun.getPaymentReference());
          String paymentType = checkPaymentType(paymentRealRun.getPaymentMethod(), isGPF);
          String fileType =
              paymentType.equalsIgnoreCase("D3")
                      || paymentType.equalsIgnoreCase("D4")
                      || paymentType.equalsIgnoreCase("D5")
                  ? "A1"
                  : "A2";
          String userTref =
              paymentType.equalsIgnoreCase("D3")
                      || paymentType.equalsIgnoreCase("D4")
                      || paymentType.equalsIgnoreCase("D5")
                  ? "A1"
                  : paymentType.equalsIgnoreCase("D1") ? "A2" : "A2";

          if (vendor.equalsIgnoreCase("")) {
            if (isTestRun) {
              giroRunning = "$$$";
            } else {
              Long running =
                  generateFileSequenceService.getCurrentSeq(
                      "GIRO_SEQ", generateFileAlias.getGiroDate());
              giroRunning = StringUtils.leftPad(String.valueOf(running), 3, "0");
            }

            if ("A1".equalsIgnoreCase(userTref)) {
              giroBatchNum = "G" + "P" + getRound() + giroRunning;
            } else {
              giroBatchNum = "G" + "S" + getRound() + giroRunning;
            }
            vendor = paymentRealRun.getVendorCode();
            giroFileHeader =
                generateGIROFileHeader(
                    generateFileAlias,
                    bankCodeMap.get(bankKey),
                    giroRunning,
                    giroBatchNum,
                    paymentRealRun.getPayeeBankKey(),
                    paymentRealRun.getVendorName(),
                    paymentRealRun.getPaymentDate(),
                    userTref,
                    isTestRun);
            ++seqNoFileGIRO;
            seqNoGIRO = 1;
          }
          if (countGiro != 0 && countGiro % 30000 == 0) {
            countGiro = 0;
            int numDr =
                (int)
                    giroFileDetails.stream().filter(giroFile -> 1 == giroFile.getTmpNoDr()).count();
            int numCr =
                (int)
                    giroFileDetails.stream().filter(giroFile -> 1 == giroFile.getTmpNoCr()).count();
            BigDecimal amtDr =
                giroFileDetails.stream()
                    .filter(giroFile -> 1 == giroFile.getTmpNoDr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtDr()), BigDecimal::add);
            BigDecimal amtCr =
                giroFileDetails.stream()
                    .filter(giroFile -> 1 == giroFile.getTmpNoCr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtCr()), BigDecimal::add);
            GIROFileTrailer giroFileTrailer =
                generateGIROFileTrailer(
                    bankCodeMap.get(bankKey),
                    fileType,
                    bankKey,
                    paymentRealRun.getPaymentMethod(),
                    numDr,
                    amtDr,
                    numCr,
                    amtCr,
                    ++seqNoGIRO);
            giroFileHeader.setGiroFileDetails(giroFileDetails);
            giroFileHeader.setGiroFileTrailer(giroFileTrailer);
            generateFileAlias.addGiroFileHeader(giroFileHeader);
            if (isTestRun) {
              giroRunning = "$$$";
            } else {
              Long running =
                  generateFileSequenceService.getCurrentSeq(
                      "GIRO_SEQ", generateFileAlias.getGiroDate());
              giroRunning = StringUtils.leftPad(String.valueOf(running), 3, "0");
            }
            if ("A1".equalsIgnoreCase(userTref)) {
              giroBatchNum = "G" + "P" + getRound() + giroRunning;
            } else {
              giroBatchNum = "G" + "S" + getRound() + giroRunning;
            }

            giroFileHeader =
                generateGIROFileHeader(
                    generateFileAlias,
                    bankCodeMap.get(bankKey),
                    giroRunning,
                    giroBatchNum,
                    paymentRealRun.getPayeeBankKey(),
                    paymentRealRun.getVendorName(),
                    paymentRealRun.getPaymentDate(),
                    userTref,
                    isTestRun);
            seqNoGIRO = 1;

            giroFileDetails = new ArrayList<>();
          }

          GIROFileDetail giroFileDetail =
              generateGIROFileDetail(
                  paymentRealRun, giroBatchNum, seqNoFileGIRO, ++seqNoGIRO, isTestRun);
          giroFileDetails.add(giroFileDetail);
          proposalLog.setAmount(
              paymentRealRun
                  .getInvoiceAmountPaid()
                  .subtract(
                      paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
          proposalLog.setPayAccount(paymentType);
          proposalLog.setBankKey(paymentRealRun.getPayeeBankKey());
          proposalLog.setFileType("GIRO");
          proposalLogs.add(proposalLog);
          countGiro++;
          if (i == giros.size() - 1) {
            int numDr =
                (int)
                    giroFileDetails.stream().filter(giroFile -> 1 == giroFile.getTmpNoDr()).count();
            int numCr =
                (int)
                    giroFileDetails.stream().filter(giroFile -> 1 == giroFile.getTmpNoCr()).count();
            BigDecimal amtDr =
                giroFileDetails.stream()
                    .filter(giroFile -> 1 == giroFile.getTmpNoDr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtDr()), BigDecimal::add);
            BigDecimal amtCr =
                giroFileDetails.stream()
                    .filter(giroFile -> 1 == giroFile.getTmpNoCr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtCr()), BigDecimal::add);
            GIROFileTrailer giroFileTrailer =
                generateGIROFileTrailer(
                    bankCodeMap.get(bankKey),
                    fileType,
                    bankKey,
                    paymentRealRun.getPaymentMethod(),
                    numDr,
                    amtDr,
                    numCr,
                    amtCr,
                    ++seqNoGIRO);
            giroFileHeader.setGiroFileDetails(giroFileDetails);
            giroFileHeader.setGiroFileTrailer(giroFileTrailer);
            String vendorName =
                paymentRealRun.getVendorName().length() >= 25
                    ? paymentRealRun.getVendorName().substring(0, 25)
                    : StringUtils.rightPad(paymentRealRun.getVendorName(), 25, "0");
            giroFileHeader.setCompName(vendorName);
            generateFileAlias.addGiroFileHeader(giroFileHeader);
            vendor = "";
          }
        }
      }

      if (!girosSumFile.isEmpty()) {
        String bankKey = "";
        String vendor = "";
        int countGiro = 0;
        int seqNoFileGIRO = 0;
        int seqNoGIRO = 0;
        String giroBatchNum = "";
        String giroRunning = "";
        GIROFileHeader giroFileHeader = null;
        List<GIROFileDetail> giroFileDetails = new ArrayList<>();
        for (int i = 0; i < girosSumFile.size(); i++) {
          ProposalLog proposalLog = new ProposalLog();
          PrepareRunDocument paymentRealRun = girosSumFile.get(i);
          bankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
          boolean isGPF =
              checkGPF(
                  paymentRealRun.getOriginalCompanyCode(),
                  paymentRealRun.getOriginalDocumentType(),
                  paymentRealRun.getAccountType(),
                  paymentRealRun.getPaymentMethod(),
                  paymentRealRun.getCostCenter(),
                  paymentRealRun.getPaymentReference());
          String paymentType = checkPaymentType(paymentRealRun.getPaymentMethod(), isGPF);
          String fileType =
              paymentType.equalsIgnoreCase("D3")
                      || paymentType.equalsIgnoreCase("D4")
                      || paymentType.equalsIgnoreCase("D5")
                  ? "A1"
                  : "A2";
          String userTref =
              paymentType.equalsIgnoreCase("D3")
                      || paymentType.equalsIgnoreCase("D4")
                      || paymentType.equalsIgnoreCase("D5")
                  ? "A1"
                  : paymentType.equalsIgnoreCase("D1") ? "A2" : "A2";

          if (vendor.equalsIgnoreCase("")) {
            if (isTestRun) {
              giroRunning = "$$$";
            } else {
              Long running =
                  generateFileSequenceService.getCurrentSeq(
                      "GIRO_SEQ", generateFileAlias.getGiroDate());
              giroRunning = StringUtils.leftPad(String.valueOf(running), 3, "0");
            }
            if ("A1".equalsIgnoreCase(userTref)) {
              giroBatchNum = "G" + "P" + getRound() + giroRunning;
            } else {
              giroBatchNum = "G" + "S" + getRound() + giroRunning;
            }
            vendor = paymentRealRun.getVendorCode();

            giroFileHeader =
                generateGIROFileHeader(
                    generateFileAlias,
                    bankCodeMap.get(bankKey),
                    giroRunning,
                    giroBatchNum,
                    paymentRealRun.getPayeeBankKey(),
                    paymentRealRun.getVendorName(),
                    paymentRealRun.getPaymentDate(),
                    userTref,
                    isTestRun);
            ++seqNoFileGIRO;
            seqNoGIRO = 1;
          }
          if (countGiro != 0 && countGiro % 10000000 == 0) {
            countGiro = 0;
            int numDr =
                (int)
                    giroFileDetails.stream().filter(giroFile -> 1 == giroFile.getTmpNoDr()).count();
            int numCr =
                (int)
                    giroFileDetails.stream().filter(giroFile -> 1 == giroFile.getTmpNoCr()).count();
            BigDecimal amtDr =
                giroFileDetails.stream()
                    .filter(giroFile -> 1 == giroFile.getTmpNoDr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtDr()), BigDecimal::add);
            BigDecimal amtCr =
                giroFileDetails.stream()
                    .filter(giroFile -> 1 == giroFile.getTmpNoCr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtCr()), BigDecimal::add);
            GIROFileTrailer giroFileTrailer =
                generateGIROFileTrailer(
                    bankCodeMap.get(bankKey),
                    fileType,
                    bankKey,
                    paymentRealRun.getPaymentMethod(),
                    numDr,
                    amtDr,
                    numCr,
                    amtCr,
                    ++seqNoGIRO);
            giroFileHeader.setGiroFileDetails(giroFileDetails);
            giroFileHeader.setGiroFileTrailer(giroFileTrailer);
            generateFileAlias.addGiroFileHeaderSumFile(giroFileHeader);
            if (isTestRun) {
              giroRunning = "$$$";
            } else {
              Long running =
                  generateFileSequenceService.getCurrentSeq(
                      "GIRO_SEQ", generateFileAlias.getGiroDate());
              giroRunning = StringUtils.leftPad(String.valueOf(running), 3, "0");
            }
            if ("A1".equalsIgnoreCase(userTref)) {
              giroBatchNum = "G" + "P" + getRound() + giroRunning;
            } else {
              giroBatchNum = "G" + "S" + getRound() + giroRunning;
            }

            giroFileHeader =
                generateGIROFileHeader(
                    generateFileAlias,
                    bankCodeMap.get(bankKey),
                    giroRunning,
                    giroBatchNum,
                    paymentRealRun.getPayeeBankKey(),
                    paymentRealRun.getVendorName(),
                    paymentRealRun.getPaymentDate(),
                    userTref,
                    isTestRun);
            seqNoGIRO = 1;

            giroFileDetails = new ArrayList<>();
          }

          GIROFileDetail giroFileDetail =
              generateGIROFileDetail(
                  paymentRealRun, giroBatchNum, seqNoFileGIRO, ++seqNoGIRO, isTestRun);
          giroFileDetails.add(giroFileDetail);
          proposalLog.setAmount(
              paymentRealRun
                  .getInvoiceAmountPaid()
                  .subtract(
                      paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
          proposalLog.setPayAccount(paymentType);
          proposalLog.setBankKey(paymentRealRun.getPayeeBankKey());
          proposalLog.setFileType("GIRO");
          proposalLogs.add(proposalLog);
          countGiro++;
          if (i == girosSumFile.size() - 1) {
            int numDr =
                (int)
                    giroFileDetails.stream().filter(giroFile -> 1 == giroFile.getTmpNoDr()).count();
            int numCr =
                (int)
                    giroFileDetails.stream().filter(giroFile -> 1 == giroFile.getTmpNoCr()).count();
            BigDecimal amtDr =
                giroFileDetails.stream()
                    .filter(giroFile -> 1 == giroFile.getTmpNoDr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtDr()), BigDecimal::add);
            BigDecimal amtCr =
                giroFileDetails.stream()
                    .filter(giroFile -> 1 == giroFile.getTmpNoCr())
                    .reduce(BigDecimal.ZERO, (n, m) -> n.add(m.getAmtCr()), BigDecimal::add);
            GIROFileTrailer giroFileTrailer =
                generateGIROFileTrailer(
                    bankCodeMap.get(bankKey),
                    fileType,
                    bankKey,
                    paymentRealRun.getPaymentMethod(),
                    numDr,
                    amtDr,
                    numCr,
                    amtCr,
                    ++seqNoGIRO);
            giroFileHeader.setGiroFileDetails(giroFileDetails);
            giroFileHeader.setGiroFileTrailer(giroFileTrailer);
            String vendorName =
                paymentRealRun.getVendorName().length() >= 25
                    ? paymentRealRun.getVendorName().substring(0, 25)
                    : StringUtils.rightPad(paymentRealRun.getVendorName(), 25, "0");
            giroFileHeader.setCompName(vendorName);
            generateFileAlias.addGiroFileHeaderSumFile(giroFileHeader);
            vendor = "";
          }
        }
      }

      List<ProposalLog> d1 =
          proposalLogs.stream()
              .filter(proposalLog -> "D1".equalsIgnoreCase(proposalLog.getPayAccount()))
              .collect(Collectors.toList());
      List<ProposalLog> d2 =
          proposalLogs.stream()
              .filter(proposalLog -> "D2".equalsIgnoreCase(proposalLog.getPayAccount()))
              .collect(Collectors.toList());
      List<ProposalLog> d3 =
          proposalLogs.stream()
              .filter(proposalLog -> "D3".equalsIgnoreCase(proposalLog.getPayAccount()))
              .collect(Collectors.toList());
      List<ProposalLog> d4 =
          proposalLogs.stream()
              .filter(proposalLog -> "D4".equalsIgnoreCase(proposalLog.getPayAccount()))
              .collect(Collectors.toList());
      List<ProposalLog> d5 =
          proposalLogs.stream()
              .filter(proposalLog -> "D5".equalsIgnoreCase(proposalLog.getPayAccount()))
              .collect(Collectors.toList());

      // level 1
      Long runningLevelOne = 0L;
      if (!d1.isEmpty()) {
        if (runningLevelOne == 0) {
          if (isTestRun) {
            runningLevelOne = 0L;
          } else {
            runningLevelOne =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_SEQ", generateFileAlias.getSwiftDate());
          }
        }
        Long running = 0L;
        if (isTestRun) {
          running = 0L;
        } else {
          running =
              generateFileSequenceService.getCurrentSeq(
                  "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
        }
        SwiftFile swiftFileLevelOne =
            generateSwiftLevelOne(
                d1,
                generateFileAlias,
                "D1",
                generateFileAlias.getSwiftDate(),
                new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                generateFileAlias.getPaymentAlias().getPaymentName(),
                runningLevelOne,
                running,
                isTestRun);
        generateFileAlias.addSwiftFileMaster(swiftFileLevelOne);
      }
      if (!d2.isEmpty()) {
        if (runningLevelOne == 0) {
          if (isTestRun) {
            runningLevelOne = 0L;
          } else {
            runningLevelOne =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_SEQ", generateFileAlias.getSwiftDate());
          }
        }
        Long running = 0L;
        if (isTestRun) {
          running = 0L;
        } else {
          running =
              generateFileSequenceService.getCurrentSeq(
                  "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
        }
        SwiftFile swiftFileLevelOne =
            generateSwiftLevelOne(
                d2,
                generateFileAlias,
                "D2",
                generateFileAlias.getSwiftDate(),
                new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                generateFileAlias.getPaymentAlias().getPaymentName(),
                runningLevelOne,
                running,
                isTestRun);
        generateFileAlias.addSwiftFileMaster(swiftFileLevelOne);
      }
      if (!d3.isEmpty()) {
        if (runningLevelOne == 0) {
          if (isTestRun) {
            runningLevelOne = 0L;
          } else {
            runningLevelOne =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_SEQ", generateFileAlias.getSwiftDate());
          }
        }
        Long running = 0L;
        if (isTestRun) {
          running = 0L;
        } else {
          running =
              generateFileSequenceService.getCurrentSeq(
                  "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
        }
        SwiftFile swiftFileLevelOne =
            generateSwiftLevelOne(
                d3,
                generateFileAlias,
                "D3",
                generateFileAlias.getSwiftDate(),
                new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                generateFileAlias.getPaymentAlias().getPaymentName(),
                runningLevelOne,
                running,
                isTestRun);
        generateFileAlias.addSwiftFileMaster(swiftFileLevelOne);
      }
      if (!d4.isEmpty()) {
        if (runningLevelOne == 0) {
          if (isTestRun) {
            runningLevelOne = 0L;
          } else {
            runningLevelOne =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_SEQ", generateFileAlias.getSwiftDate());
          }
        }
        Long running = 0L;
        if (isTestRun) {
          running = 0L;
        } else {
          running =
              generateFileSequenceService.getCurrentSeq(
                  "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
        }
        SwiftFile swiftFileLevelOne =
            generateSwiftLevelOne(
                d4,
                generateFileAlias,
                "D4",
                generateFileAlias.getSwiftDate(),
                new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                generateFileAlias.getPaymentAlias().getPaymentName(),
                runningLevelOne,
                running,
                isTestRun);
        generateFileAlias.addSwiftFileMaster(swiftFileLevelOne);
      } else if (!d5.isEmpty()) {
        if (runningLevelOne == 0) {
          if (isTestRun) {
            runningLevelOne = 0L;
          } else {
            runningLevelOne =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_SEQ", generateFileAlias.getSwiftDate());
          }
        }
        Long running = 0L;
        if (isTestRun) {
          running = 0L;
        } else {
          running =
              generateFileSequenceService.getCurrentSeq(
                  "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
        }
        SwiftFile swiftFileLevelOne =
            generateSwiftLevelOne(
                d5,
                generateFileAlias,
                "D5",
                generateFileAlias.getSwiftDate(),
                new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                generateFileAlias.getPaymentAlias().getPaymentName(),
                runningLevelOne,
                running,
                isTestRun);
        generateFileAlias.addSwiftFileMaster(swiftFileLevelOne);
      }

      boolean isSmartD3 =
          d3.stream()
              .anyMatch(
                  proposalLog ->
                      "SMART".equalsIgnoreCase(proposalLog.getFileType())
                          || "PAIN001".equalsIgnoreCase(proposalLog.getFileType()));
      boolean isSmartD4 =
          d4.stream()
              .anyMatch(
                  proposalLog ->
                      "SMART".equalsIgnoreCase(proposalLog.getFileType())
                          || "PAIN001".equalsIgnoreCase(proposalLog.getFileType()));
      boolean isSmartD5 =
          d5.stream()
              .anyMatch(
                  proposalLog ->
                      "SMART".equalsIgnoreCase(proposalLog.getFileType())
                          || "PAIN001".equalsIgnoreCase(proposalLog.getFileType()));
      boolean isInhouseD3 =
          d3.stream().anyMatch(proposalLog -> "INHOU".equalsIgnoreCase(proposalLog.getFileType()));
      boolean isInhouseD4TMB =
          d4.stream()
              .anyMatch(
                  proposalLog ->
                      "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                          && "011".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)));
      boolean isInhouseD5TMB =
          d5.stream()
              .anyMatch(
                  proposalLog ->
                      "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                          && "011".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)));
      boolean isInhouseD4GSB =
          d4.stream()
              .anyMatch(
                  proposalLog ->
                      "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                          && "030".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)));
      boolean isInhouseD5GSB =
          d5.stream()
              .anyMatch(
                  proposalLog ->
                      "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                          && "030".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)));
      boolean isInhouseD4BAAC =
          d4.stream()
              .anyMatch(
                  proposalLog ->
                      "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                          && "034".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)));
      boolean isInhouseD5BAAC =
          d5.stream()
              .anyMatch(
                  proposalLog ->
                      "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                          && "034".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)));
      boolean isGIROD3 =
          d3.stream().anyMatch(proposalLog -> "GIRO".equalsIgnoreCase(proposalLog.getFileType()));
      boolean isGIROD4 =
          d4.stream().anyMatch(proposalLog -> "GIRO".equalsIgnoreCase(proposalLog.getFileType()));
      boolean isGIROD5 =
          d5.stream().anyMatch(proposalLog -> "GIRO".equalsIgnoreCase(proposalLog.getFileType()));
      Long runningLevelTwo = 0L;

      if (!d3.isEmpty()) {
        if (isSmartD3) {
          if (runningLevelTwo == 0) {
            if (isTestRun) {
              runningLevelTwo = 0L;
            } else {
              runningLevelTwo =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          SwiftFile swiftFileLevelTwo =
              generateSwiftLevelTwo(
                  d3,
                  generateFileAlias,
                  "D3",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  runningLevelTwo,
                  running,
                  isTestRun);
          generateFileAlias.addSwiftFileMaster(swiftFileLevelTwo);
        }
      }
      if (!d4.isEmpty()) {
        if (isSmartD4) {
          if (runningLevelTwo == 0) {
            if (isTestRun) {
              runningLevelTwo = 0L;
            } else {
              runningLevelTwo =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          SwiftFile swiftFileLevelTwo =
              generateSwiftLevelTwo(
                  d4,
                  generateFileAlias,
                  "D4",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  runningLevelTwo,
                  running,
                  isTestRun);
          generateFileAlias.addSwiftFileMaster(swiftFileLevelTwo);
        }
      }
      if (!d5.isEmpty()) {
        if (isSmartD5) {
          if (runningLevelTwo == 0) {
            if (isTestRun) {
              runningLevelTwo = 0L;
            } else {
              runningLevelTwo =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          SwiftFile swiftFileLevelTwo =
              generateSwiftLevelTwo(
                  d5,
                  generateFileAlias,
                  "D5",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  runningLevelTwo,
                  running,
                  isTestRun);
          generateFileAlias.addSwiftFileMaster(swiftFileLevelTwo);
        }
      }

      Long gorvermentBank = 0L;
      //      if (!d3.isEmpty()) {
      //        if (isInhouseD3 || isGIROD3) {
      //          if (runningLevelNine == 0) {
      //            runningLevelNine = metaDataService.getFileRunning("SWIFT");
      //          }
      //          swiftFileLevelNine = generateSwiftLevelNine(d3, generateFileAlias, "D3",
      // generateFileAlias.getSwiftDate(), new
      // Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
      //              generateFileAlias.getPaymentAlias().getPaymentName(), d3.get(0).getBankKey(),
      // runningLevelNine, isGIROD3, isTestRun);
      //        }
      //      } else
      String levelNineFileName = null;
      if (!d4.isEmpty()) {
        if (isGIROD4) {
          if (gorvermentBank == 0) {
            if (isTestRun) {
              gorvermentBank = 0L;
            } else {
              gorvermentBank =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          String effectiveDate =
              Util.dateToStringPattern_ddMMyyyy(generateFileAlias.getSwiftDate());
          if (null == levelNineFileName) {
            levelNineFileName =
                !isTestRun
                    ? "S"
                        + effectiveDate
                        + getRound()
                        + StringUtils.leftPad(String.valueOf(gorvermentBank), 6, "0")
                    : "S" + effectiveDate + getRound() + "$$$$$$";
          }
          SwiftFile swiftFileLevelNineGIRO =
              generateSwiftLevelNineGIRO(
                  d4,
                  generateFileAlias,
                  bankCodeMap,
                  "D4",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  levelNineFileName,
                  running,
                  isGIROD4,
                  isTestRun);
          generateFileAlias.addSwiftFile(swiftFileLevelNineGIRO);
        }
      }
      if (!d5.isEmpty()) {
        if (isGIROD5) {
          if (gorvermentBank == 0) {
            if (isTestRun) {
              gorvermentBank = 0L;
            } else {
              gorvermentBank =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          String effectiveDate =
              Util.dateToStringPattern_ddMMyyyy(generateFileAlias.getSwiftDate());
          if (null == levelNineFileName) {
            levelNineFileName =
                !isTestRun
                    ? "S"
                        + effectiveDate
                        + getRound()
                        + StringUtils.leftPad(String.valueOf(gorvermentBank), 6, "0")
                    : "S" + effectiveDate + getRound() + "$$$$$$";
          }
          SwiftFile swiftFileLevelNineGIRO =
              generateSwiftLevelNineGIRO(
                  d5,
                  generateFileAlias,
                  bankCodeMap,
                  "D5",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  levelNineFileName,
                  running,
                  isGIROD5,
                  isTestRun);
          generateFileAlias.addSwiftFile(swiftFileLevelNineGIRO);
        }
      }

      if (!d4.isEmpty()) {
        if (isInhouseD4TMB) {
          if (gorvermentBank == 0) {
            if (isTestRun) {
              gorvermentBank = 0L;
            } else {
              gorvermentBank =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          String effectiveDate =
              Util.dateToStringPattern_ddMMyyyy(generateFileAlias.getSwiftDate());
          if (null == levelNineFileName) {
            levelNineFileName =
                !isTestRun
                    ? "S"
                        + effectiveDate
                        + getRound()
                        + StringUtils.leftPad(String.valueOf(gorvermentBank), 6, "0")
                    : "S" + effectiveDate + getRound() + "$$$$$$";
          }
          SwiftFile swiftFileLevelNineInhouseTMB =
              generateSwiftLevelNineInhouseTMB(
                  d4,
                  generateFileAlias,
                  bankCodeMap,
                  "D4",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  levelNineFileName,
                  running,
                  false,
                  isTestRun);
          generateFileAlias.addSwiftFile(swiftFileLevelNineInhouseTMB);
        }
      }
      if (!d5.isEmpty()) {
        if (isInhouseD5TMB) {
          if (gorvermentBank == 0) {
            if (isTestRun) {
              gorvermentBank = 0L;
            } else {
              gorvermentBank =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          String effectiveDate =
              Util.dateToStringPattern_ddMMyyyy(generateFileAlias.getSwiftDate());
          if (null == levelNineFileName) {
            levelNineFileName =
                !isTestRun
                    ? "S"
                        + effectiveDate
                        + getRound()
                        + StringUtils.leftPad(String.valueOf(gorvermentBank), 6, "0")
                    : "S" + effectiveDate + getRound() + "$$$$$$";
          }
          SwiftFile swiftFileLevelNineInhouseTMB =
              generateSwiftLevelNineInhouseTMB(
                  d5,
                  generateFileAlias,
                  bankCodeMap,
                  "D5",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  levelNineFileName,
                  running,
                  false,
                  isTestRun);
          generateFileAlias.addSwiftFile(swiftFileLevelNineInhouseTMB);
        }
      }

      if (!d4.isEmpty()) {
        if (isInhouseD4GSB) {
          if (gorvermentBank == 0) {
            if (isTestRun) {
              gorvermentBank = 0L;
            } else {
              gorvermentBank =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          String effectiveDate =
              Util.dateToStringPattern_ddMMyyyy(generateFileAlias.getSwiftDate());
          if (null == levelNineFileName) {
            levelNineFileName =
                !isTestRun
                    ? "S"
                        + effectiveDate
                        + getRound()
                        + StringUtils.leftPad(String.valueOf(gorvermentBank), 6, "0")
                    : "S" + effectiveDate + getRound() + "$$$$$$";
          }
          SwiftFile swiftFileLevelNineInhouseGSB =
              generateSwiftLevelNineInhouseGSB(
                  d4,
                  generateFileAlias,
                  bankCodeMap,
                  "D4",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  levelNineFileName,
                  running,
                  false,
                  isTestRun);
          generateFileAlias.addSwiftFile(swiftFileLevelNineInhouseGSB);
        }
      }
      if (!d5.isEmpty()) {
        if (isInhouseD5GSB) {
          if (gorvermentBank == 0) {
            if (isTestRun) {
              gorvermentBank = 0L;
            } else {
              gorvermentBank =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          String effectiveDate =
              Util.dateToStringPattern_ddMMyyyy(generateFileAlias.getSwiftDate());
          if (null == levelNineFileName) {
            levelNineFileName =
                !isTestRun
                    ? "S"
                        + effectiveDate
                        + getRound()
                        + StringUtils.leftPad(String.valueOf(gorvermentBank), 6, "0")
                    : "S" + effectiveDate + getRound() + "$$$$$$";
          }
          SwiftFile swiftFileLevelNineInhouseGSB =
              generateSwiftLevelNineInhouseGSB(
                  d5,
                  generateFileAlias,
                  bankCodeMap,
                  "D5",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  levelNineFileName,
                  running,
                  false,
                  isTestRun);
          generateFileAlias.addSwiftFile(swiftFileLevelNineInhouseGSB);
        }
      }

      if (!d4.isEmpty()) {
        if (isInhouseD4BAAC) {
          if (gorvermentBank == 0) {
            if (isTestRun) {
              gorvermentBank = 0L;
            } else {
              gorvermentBank =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          String effectiveDate =
              Util.dateToStringPattern_ddMMyyyy(generateFileAlias.getSwiftDate());
          if (null == levelNineFileName) {
            levelNineFileName =
                !isTestRun
                    ? "S"
                        + effectiveDate
                        + getRound()
                        + StringUtils.leftPad(String.valueOf(gorvermentBank), 6, "0")
                    : "S" + effectiveDate + getRound() + "$$$$$$";
          }
          SwiftFile swiftFileLevelNineInhouseBAAC =
              generateSwiftLevelNineInhouseBAC(
                  d4,
                  generateFileAlias,
                  bankCodeMap,
                  "D4",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  levelNineFileName,
                  running,
                  false,
                  isTestRun);
          generateFileAlias.addSwiftFile(swiftFileLevelNineInhouseBAAC);
        }
      }
      if (!d5.isEmpty()) {
        if (isInhouseD5BAAC) {
          if (gorvermentBank == 0) {
            if (isTestRun) {
              gorvermentBank = 0L;
            } else {
              gorvermentBank =
                  generateFileSequenceService.getCurrentSeq(
                      "SWIFT_SEQ", generateFileAlias.getSwiftDate());
            }
          }
          Long running = 0L;
          if (isTestRun) {
            running = 0L;
          } else {
            running =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate());
          }
          String effectiveDate =
              Util.dateToStringPattern_ddMMyyyy(generateFileAlias.getSwiftDate());
          if (null == levelNineFileName) {
            levelNineFileName =
                !isTestRun
                    ? "S"
                        + effectiveDate
                        + getRound()
                        + StringUtils.leftPad(String.valueOf(gorvermentBank), 6, "0")
                    : "S" + effectiveDate + getRound() + "$$$$$$";
          }
          SwiftFile swiftFileLevelNineInhouseBAAC =
              generateSwiftLevelNineInhouseBAC(
                  d5,
                  generateFileAlias,
                  bankCodeMap,
                  "D5",
                  generateFileAlias.getSwiftDate(),
                  new Timestamp(generateFileAlias.getPaymentAlias().getPaymentDate().getTime()),
                  generateFileAlias.getPaymentAlias().getPaymentName(),
                  levelNineFileName,
                  running,
                  false,
                  isTestRun);
          generateFileAlias.addSwiftFile(swiftFileLevelNineInhouseBAAC);
        }
      }

      if ((null != swiftFiles && !swiftFiles.isEmpty())
          || (null != swiftFilesSumFile && !swiftFilesSumFile.isEmpty())) {
        Long fileSeq;
        Long runningSeq;
        if (isTestRun) {
          runningSeq = 0L;
        } else {
          runningSeq =
              generateFileSequenceService.getCurrentSeq(
                  "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate(), swiftFiles.size());
        }
        String effectiveDate = Util.dateToStringPattern_ddMMyyyy(generateFileAlias.getSwiftDate());
        if (null == levelNineFileName) {
          if (isTestRun) {
            fileSeq = 0L;
          } else {
            fileSeq =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_SEQ", generateFileAlias.getSwiftDate());
          }
          levelNineFileName =
              !isTestRun
                  ? "S"
                      + effectiveDate
                      + getRound()
                      + StringUtils.leftPad(String.valueOf(fileSeq), 6, "0")
                  : "S" + effectiveDate + getRound() + "$$$$$$";
        }
        for (int i = 0; i < swiftFiles.size(); i++) {
          SwiftFile swiftFile = swiftFiles.get(i);
          swiftFile.setFileName(levelNineFileName);
          swiftFile.setSendRef(String.valueOf(runningSeq++));
          swiftFile.setSumFile(false);
          generateFileAlias.addSwiftFile(swiftFile);
          if (i != swiftFiles.size() - 1) {
            if (generateFileAlias.getSwiftFiles().size() % 50 == 0) {
              if (isTestRun) {
                fileSeq = 0L;
              } else {
                fileSeq =
                        generateFileSequenceService.getCurrentSeq(
                                "SWIFT_SEQ", generateFileAlias.getSwiftDate());
              }
              levelNineFileName =
                      !isTestRun
                              ? "S"
                              + effectiveDate
                              + getRound()
                              + StringUtils.leftPad(String.valueOf(fileSeq), 6, "0")
                              : "S" + effectiveDate + getRound() + "$$$$$$";
            }
          }
        }

        if (null != swiftFilesSumFile && !swiftFilesSumFile.isEmpty()) {
          Long runningSeqSum;
          Map<String, List<SwiftFile>> groupByVendor =
              swiftFilesSumFile.stream()
                  .collect(
                      Collectors.groupingBy(
                          this::groupByMultipleKey,
                          Collectors.mapping((SwiftFile s) -> s, toList())));
          if (isTestRun) {
            runningSeqSum = 0L;
          } else {
            runningSeqSum =
                generateFileSequenceService.getCurrentSeq(
                    "SWIFT_RUNNING_SEQ", generateFileAlias.getSwiftDate(), groupByVendor.size());
          }
          for (var entry : groupByVendor.entrySet()) {
            BigDecimal totalTransferAmount =
                entry.getValue().stream()
                    .map(SwiftFile::getSetAmount) // map
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            SwiftFile swiftFile = new SwiftFile(entry.getValue().get(0));
            swiftFile.setFileName(levelNineFileName);
            swiftFile.setSumFile(true);
            swiftFile.setSetAmount(totalTransferAmount);
            swiftFile.setTotalTransferAmount(totalTransferAmount);
            swiftFile.setSendRef(String.valueOf(runningSeqSum++));
            generateFileAlias.addSwiftFile(swiftFile);
            if ((generateFileAlias.getSwiftFiles().size()) % 50 == 0) {
              if (isTestRun) {
                fileSeq = 0L;
              } else {
                fileSeq =
                    generateFileSequenceService.getCurrentSeq(
                        "SWIFT_SEQ", generateFileAlias.getSwiftDate());
              }
              levelNineFileName =
                  !isTestRun
                      ? "S"
                          + effectiveDate
                          + getRound()
                          + StringUtils.leftPad(String.valueOf(fileSeq), 6, "0")
                      : "S" + effectiveDate + getRound() + "$$$$$$";
            }
            for (SwiftFile swiftFileChild : entry.getValue()) {
              swiftFileChild.setFileName(levelNineFileName);
              generateFileAlias.addSwiftFileSumFile(swiftFileChild);
            }
          }
        }
      }

      return generateTextFile(
          generateFileAlias,
          refRunning,
          bankCodeMap,
          errors,
          isTestRun,
          isRegen,
          isAll,
          format,
          !Util.isEmpty(format) ? generateFileAlias.getFileName() : "",
          proposalHeaderId,
          proposalId,
          proposalSumId,
          isPac,
          fileNameLevel1);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private GenerateFile generateTextFile(
      GenerateFileAlias generateFileAlias,
      Long refRunning,
      Map<String, BankCode> bankCodeMap,
      List<String> errors,
      boolean isTestRun,
      boolean isRegen,
      boolean isAll,
      String fileType,
      String regenFileName,
      Long proposalHeaderId,
      Long proposalId,
      Long proposalSumId,
      boolean isPac,
      String fileNameLevel1)
      throws IOException {
    try {
      JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
      //            log.info("file name : {}", generateFileAlias.getFileName());
      Timestamp created = new Timestamp(new Date().getTime());
      boolean usePain = true;
      PaymentAlias paymentAlias = generateFileAlias.getPaymentAlias();
      String directory = Util.dateToStringPattern_ddMMyyyy(paymentAlias.getPaymentDate());
      GenerateFile generateFile = new GenerateFile();
      generateFile.setErrors(errors);
      ProposalLogHeader proposalLogHeader = null;
      List<ProposalLogSum> proposalLogSums = new ArrayList<>();
      List<ProposalLog> proposalLogs = new ArrayList<>();
      int i = 0;
      int refLineSum = 0;
      proposalLogHeader = generateFileAlias.getInterfaceD1D2();
      boolean isSumFile =
          null != generateFileAlias.getSmartFileHeaderSumFile()
              || (null != generateFileAlias.getSwiftFilesSumFile()
                  && !generateFileAlias.getSwiftFilesSumFile().isEmpty())
              || (null != generateFileAlias.getInhouseFileHeadersSumFile()
                  && !generateFileAlias.getInhouseFileHeadersSumFile().isEmpty())
              || (null != generateFileAlias.getGiroFileHeadersSumFile()
                  && !generateFileAlias.getGiroFileHeadersSumFile().isEmpty());
      if (null != proposalLogHeader) {
        if (!generateFileAlias.isTestRun()) {
          proposalLogHeaderService.save(proposalLogHeader);
        }
        proposalLogs = proposalLogHeader.getInterfaceD1D2();
      }
      SmartFileHeader smartFileHeader = generateFileAlias.getSmartFileHeader();
      if (null != smartFileHeader) {
        if (usePain) {
          String xmlType = "";
          Timestamp curDate =
              new Timestamp(
                  Date.from(
                          new Date()
                              .toInstant()
                              .atZone(ZoneId.systemDefault())
                              .truncatedTo(ChronoUnit.DAYS)
                              .toInstant())
                      .getTime());
          Timestamp effDate =
              Util.stringSmartToTimestamp(
                  smartFileHeader.getSmartFileBatches().get(0).getEffDate());
          if (curDate.equals(effDate)) {
            xmlType = "SDVA";
          } else {
            xmlType = "NURG";
          }
          List<OutputReport> outputReports = new ArrayList<>();
          List<OutputReport> outputReports2 = new ArrayList<>();
          List<SmartFileBatch> smartFileBatches = smartFileHeader.getSmartFileBatches();
          List<SmartFileGM> smartFileGMs = generateFileAlias.getSmartFileGMs();
          int batch = 0;
          generateFile.setReference(String.valueOf(refRunning));
          if (!smartFileBatches.isEmpty()) {
            int k = 0;
            String gmFilename = null;
            for (SmartFileBatch smartFileBatch : smartFileBatches) {
              String paymentType = smartFileBatch.getSmartFileDetails().get(0).getPaymentType();
              if (paymentType.equalsIgnoreCase("D3")
                  || paymentType.equalsIgnoreCase("D4")
                  || paymentType.equalsIgnoreCase("D5")) {
                SimpleDateFormat sdfGM = new SimpleDateFormat("yyyyMMdd");
                if (isTestRun) {
                  gmFilename = "GM" + sdfGM.format(generateFileAlias.getSmartDate()) + "$$$";
                } else {
                  if (null == gmFilename) {
                    Long gmRunning =
                        generateFileSequenceService.getCurrentSeq(
                            "GM_SEQ", generateFileAlias.getSmartDate());
                    gmFilename = "GM" + sdfGM.format(generateFileAlias.getSmartDate()) + gmRunning;
                  }
                }

                OutputReport outputReport = new OutputReport();
                OutputReport outputReport2 = new OutputReport();
                StringBuilder outputText = new StringBuilder(smartFileHeader.toString());
                outputText.append("\n");
                outputText.append(smartFileBatch.toString());
                Account account = this.account.get(paymentType);
                //                        Pain001 pain001 = new Pain001();
                Document document = new Document();
                CstmrCdtTrfInitn cstmrCdtTrfInitn = new CstmrCdtTrfInitn();
                // cstmrCdtTrfInitn section
                GrpHdr grpHdr = new GrpHdr();
                //            Long painRunning = metaDataService.getFileRunning("PAIN");
                String painRunning;
                if (isTestRun) {
                  painRunning = "$$$$$";
                } else {
                  painRunning =
                      String.valueOf(
                          generateFileSequenceService.getCurrentSeq(
                              "PAIN_SEQ", generateFileAlias.getSmartDate()));
                }
                String msgId =
                    "ECGD01-"
                        + Util.timestampToStringForIndependent(smartFileHeader.getEffDate())
                        + "-OC-"
                        + xmlType
                        + "-"
                        + painRunning;
                String recordId =
                    "ECGD01-"
                        + Util.timestampToStringForIndependent(smartFileHeader.getEffDate())
                        + "-"
                        + xmlType
                        + "-"
                        + painRunning;
                smartFileGMs.get(k).setFilename(msgId);
                outputReport2.setOutputText(smartFileGMs.get(k).toString());
                outputReport2.setFileName(gmFilename);
                grpHdr.setMsgID(msgId);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String creDtTm = sdf.format(date);
                grpHdr.setCreDtTm(creDtTm);
                grpHdr.setNbOfTxs(String.valueOf(Integer.parseInt(smartFileBatch.getTotalNum())));
                grpHdr.setCtrlSum(
                    String.format(
                        "%.2f",
                        Util.getBigDecimal(smartFileBatch.getTotalAmount())
                            .divide(Util.getBigDecimal(100))));
                InitgPty initgPty = new InitgPty();
                initgPty.setNm("กรมบัญชีกลาง");
                InitgPtyID initgPtyID = new InitgPtyID();
                OrgID orgID = new OrgID();
                Othr othr = new Othr();
                othr.setId("0994000159510");
                CtgyPurp schmeNm = new CtgyPurp();
                schmeNm.setCd("TXID");
                othr.setSchmeNm(schmeNm);
                orgID.setOthr(othr);
                initgPtyID.setOrgID(orgID);
                initgPty.setId(initgPtyID);
                grpHdr.setInitgPty(initgPty);
                PmtInf pmtInf = new PmtInf();
                pmtInf.setPmtInfID(recordId + "001");
                pmtInf.setPmtMtd("TRF");
                pmtInf.setNbOfTxs(String.valueOf(Integer.parseInt(smartFileBatch.getTotalNum())));
                pmtInf.setCtrlSum(
                    String.format(
                        "%.2f",
                        Util.getBigDecimal(smartFileBatch.getTotalAmount())
                            .divide(Util.getBigDecimal(100))));
                ReqdExctnDt reqdExctnDt = new ReqdExctnDt();
                reqdExctnDt.setDt(Util.timestampToString(smartFileHeader.getEffDate()));
                pmtInf.setReqdExctnDt(reqdExctnDt);
                pmtInf.setDbtr(initgPty);
                TrAcct trAcct = new TrAcct();
                DbtrAcctID dbtrAcctID = new DbtrAcctID();
                BrnchID brnchID = new BrnchID();
                brnchID.setId(account.getAccountNo());
                dbtrAcctID.setOthr(brnchID);
                trAcct.setId(dbtrAcctID);
                trAcct.setCcy("THB");
                pmtInf.setDbtrAcct(trAcct);

                TrAgt trAgt = new TrAgt();
                FinInstnID finInstnID = new FinInstnID();
                CLRSysMmbID clrSysMmbID = new CLRSysMmbID();
                CtgyPurp clrSysID = new CtgyPurp();
                clrSysID.setCd("THCBC");
                clrSysMmbID.setClrSysID(clrSysID);
                clrSysMmbID.setMmbID("001");
                finInstnID.setClrSysMmbID(clrSysMmbID);
                trAgt.setFinInstnID(finInstnID);
                BrnchID brnchID1 = new BrnchID();
                brnchID1.setId("0001");
                trAgt.setBrnchID(brnchID1);
                pmtInf.setDbtrAgt(trAgt);
                int record = 0;
                List<CdtTrfTxInf> cdtTrfTxInfs = new ArrayList<>();

                for (SmartFileDetail smartFileDetail : smartFileBatch.getSmartFileDetails()) {
                  SmartFileLog smartFileLog = smartFileDetail.getSmartFileLog();
                  outputText.append("\n");
                  outputText.append(smartFileDetail.toString());
                  CdtTrfTxInf cdtTrfTxInf = new CdtTrfTxInf();
                  PmtID pmtID = new PmtID();
                  pmtID.setEndToEndID(
                      recordId + "001" + StringUtils.leftPad(String.valueOf(++record), 5, "0"));
                  cdtTrfTxInf.setPmtID(pmtID);
                  PmtTpInf pmtTpInf = new PmtTpInf();
                  CtgyPurp svcLvl = new CtgyPurp();
                  svcLvl.setCd(xmlType);
                  pmtTpInf.setSvcLvl(svcLvl);
                  CtgyPurp ctgyPurp = new CtgyPurp();
                  ctgyPurp.setCd("SUPP"); // ขาด logic
                  pmtTpInf.setCtgyPurp(ctgyPurp);
                  cdtTrfTxInf.setPmtTpInf(pmtTpInf);
                  Amt amt = new Amt();
                  InstdAmt instdAmt = new InstdAmt();
                  instdAmt.setText(String.format("%.2f", smartFileDetail.getTransferAmount()));
                  instdAmt.setCcy("THB");
                  amt.setInstdAmt(instdAmt);
                  cdtTrfTxInf.setAmt(amt);
                  cdtTrfTxInf.setChrgBr("SLEV");
                  TrAgt cdtrAgt = new TrAgt();
                  CtgyPurp clrSysID1 = new CtgyPurp();
                  clrSysID1.setCd("THCBC");
                  CLRSysMmbID clrSysMmbID1 = new CLRSysMmbID();
                  clrSysMmbID1.setClrSysID(clrSysID1);
                  clrSysMmbID1.setMmbID(smartFileDetail.getRecBankCode());
                  FinInstnID finInstnID1 = new FinInstnID();
                  finInstnID1.setClrSysMmbID(clrSysMmbID1);
                  cdtrAgt.setFinInstnID(finInstnID1);
                  BrnchID brnchID2 = new BrnchID();
                  brnchID2.setId(smartFileDetail.getRecBranchCode());
                  cdtrAgt.setBrnchID(brnchID2);
                  cdtTrfTxInf.setCdtrAgt(cdtrAgt);
                  Cdtr cdtr = new Cdtr();
                  cdtr.setNm(smartFileDetail.getSendInform().trim());
                  cdtTrfTxInf.setCdtr(cdtr);
                  TrAcct cdtrAcct = new TrAcct();
                  DbtrAcctID dbtrAcctID1 = new DbtrAcctID();
                  BrnchID othr1 = new BrnchID();
                  othr1.setId(smartFileDetail.getRecAcct());
                  dbtrAcctID1.setOthr(othr1);
                  cdtrAcct.setId(dbtrAcctID1);
                  cdtrAcct.setCcy("THB");
                  cdtTrfTxInf.setCdtrAcct(cdtrAcct);
                  //                                log.info("fee : {}", smartFileLog.getFee());
                  if (smartFileLog.getFee().compareTo(Util.getBigDecimal(12)) == 0
                      || smartFileDetail.getPaymentType().equalsIgnoreCase("D3")) {
                    cdtTrfTxInf.setInstrForDbtrAgt("INSF");
                  } else {
                    cdtTrfTxInf.setInstrForDbtrAgt("EXSF");
                  }
                  cdtTrfTxInfs.add(cdtTrfTxInf);

                  if (!generateFileAlias.isTestRun()) {
                    if (null == proposalLogHeader) {
                      proposalLogHeader = new ProposalLogHeader();
                      proposalLogHeader.setId(proposalHeaderId);
                      proposalLogHeader.setRefRunning(refRunning);
                      proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
                      proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
                      proposalLogHeader.setCreatedBy(jwt.getSub());
                      proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
                      proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
                      proposalLogHeader.setCancel(false);
                      proposalLogHeader.setUse(isAll);
                      proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
                    }
                    ProposalLog proposalLog = new ProposalLog();
                    proposalLog.setId(proposalId++);
                    proposalLog.setRefRunning(refRunning);
                    proposalLog.setRefLine(++i);
                    proposalLog.setPaymentName(smartFileLog.getPaymentName());
                    proposalLog.setPaymentDate(smartFileLog.getPaymentDate());
                    proposalLog.setAccountNoFrom(smartFileDetail.getSendAcct());
                    proposalLog.setAccountNoTo(smartFileDetail.getRecAcct());
                    proposalLog.setFileType("SMART");
                    String accountNo = "0" + this.account.get(paymentType).getAccountNo();
                    LocalDate localDate =
                        LocalDate.ofInstant(
                            generateFileAlias.getSmartDate().toInstant(), ZoneId.systemDefault());
                    //                                    fileName = "2" + accountNo.substring(6) +
                    // String.format("%02d", localDate.getDayOfMonth()) + "." +
                    // String.format("%03d", smartRunning);
                    outputReport.setFileName(msgId);
                    proposalLog.setFileName(msgId);
                    proposalLog.setTransferDate(generateFileAlias.getSmartDate());
                    //                                        log.info("transferAmount : {}",
                    // smartFileDetail.getTransferAmt());
                    //
                    // log.info(smartFileLog.getPaymentDocNo());
                    BigDecimal amount =
                        Util.getBigDecimal(smartFileDetail.getTransferAmt())
                            .divide(Util.getBigDecimal(100));
                    proposalLog.setAmount(amount);
                    proposalLog.setBankFee(smartFileLog.getFee());
                    proposalLog.setVendor(smartFileLog.getVendor());
                    proposalLog.setBankKey(smartFileLog.getBankKey());
                    proposalLog.setVendorBankAccount(smartFileLog.getBankAccountNo());
                    proposalLog.setTransferLevel("9");
                    //                  String payAccount = "D" + smartFileLog.getPaymentMethod();

                    proposalLog.setPayAccount(smartFileDetail.getPaymentType());
                    proposalLog.setPayingCompCode(smartFileLog.getPayingCompCode());
                    proposalLog.setPaymentDocument(smartFileLog.getPaymentDocNo());
                    proposalLog.setFiscalYear(smartFileLog.getPaymentYear());
                    proposalLog.setFiArea(smartFileLog.getFiArea());
                    proposalLog.setCreditMemoAmount(smartFileLog.getCreditMemo());
                    proposalLog.setCreatedBy(jwt.getSub());
                    proposalLog.setUpdatedBy(jwt.getSub());
                    proposalLog.setCreated(created);
                    proposalLog.setUpdated(created);
                    proposalLog.setSendStatus("S");

                    proposalLog.setOriginalCompCode(smartFileLog.getOriginalCompCode());
                    proposalLog.setOriginalDocNo(smartFileLog.getOriginalAccDocNo());
                    proposalLog.setOriginalDocType(smartFileLog.getOriginalDocType());
                    proposalLog.setOriginalFiscalYear(smartFileLog.getOriginalFiscalYear());

                    proposalLog.setInvCompCode(smartFileLog.getInvoiceCompCode());
                    proposalLog.setInvDocNo(smartFileLog.getInvoiceAccDocNo());
                    proposalLog.setInvDocType(smartFileLog.getInvoiceDocType());
                    proposalLog.setInvFiscalYear(smartFileLog.getInvoiceFiscalYear());

                    proposalLog.setOriginalWtxAmount(smartFileLog.getOriginalWtxAmount());
                    proposalLog.setOriginalWtxBase(smartFileLog.getOriginalWtxBase());
                    proposalLog.setOriginalWtxAmountP(smartFileLog.getOriginalWtxAmountP());
                    proposalLog.setOriginalWtxBaseP(smartFileLog.getOriginalWtxBaseP());

                    proposalLog.setInvWtxAmount(smartFileLog.getInvoiceWtxAmount());
                    proposalLog.setInvWtxBase(smartFileLog.getInvoiceWtxBase());
                    proposalLog.setInvWtxAmountP(smartFileLog.getInvoiceWtxAmountP());
                    proposalLog.setInvWtxBaseP(smartFileLog.getInvoiceWtxBaseP());

                    proposalLog.setPaymentCompCode(smartFileLog.getPaymentCompCode());
                    proposalLog.setPaymentFiscalYear(smartFileLog.getPaymentFiscalYear());

                    proposalLog.setPaymentType(smartFileDetail.getPaymentType());

                    proposalLog.setRefRunningSum(0L);
                    proposalLog.setRefLineSum(0);

                    proposalLog.setRerun(false);

                    proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());
                    //                            log.info("SMART ADD PROPOSAL LOG FROM smartFileLog
                    // : {} ", smartFileLog.getId());
                    //                  ProposalLog checkDuplicateFile =
                    // proposalLogService.findOneByFileNameAndPaymentDocument(msgId,
                    // smartFileLog.getPaymentDocNo());
                    //                  log.info("checkDuplicateFile : {}", checkDuplicateFile);
                    //                  if (null == checkDuplicateFile) {
                    proposalLogs.add(proposalLog);
                    //                  }
                  }
                }
                SmartFileFooter smartFileFooter = smartFileHeader.getSmartFileFooter();
                outputText.append("\n");
                outputText.append(smartFileFooter.toString());
                outputReport.setFileName(msgId);
                outputReport.setOutputText(outputText.toString());
                outputReports.add(outputReport);
                outputReports2.add(outputReport2);

                generateFile.setSmarts(outputReports);
                generateFile.setGm(outputReports2);

                pmtInf.setCdtTrfTxInfs(cdtTrfTxInfs);
                cstmrCdtTrfInitn.setGrpHdr(grpHdr);
                cstmrCdtTrfInitn.setPmtInf(pmtInf);
                document.setCstmrCdtTrfInitn(cstmrCdtTrfInitn);
                //                        pain001.setDocument(document);
                String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
                String xml = header + XMLUtil.xmlMarshall(Document.class, document);

                if (!generateFileAlias.isTestRun()) {
                  InputStream inputStream =
                      new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
                  fileTransferService.uploadFile(inputStream, directory, msgId + ".xml");
                }
                if (!generateFileAlias.isTestRun()) {
                  BigDecimal totalAmt = BigDecimal.ZERO;
                  BigDecimal totalFee = BigDecimal.ZERO;
                  BigDecimal netAmt = BigDecimal.ZERO;
                  SimpleDateFormat gm = new SimpleDateFormat("dd/MM/yyyy");
                  StringBuilder gmXml =
                      new StringBuilder()
                          .append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Upload>")
                          .append("<SWML version=\"1.0\"><BasicHdrBlk><LTID/></BasicHdrBlk>")
                          .append(
                              "<AppHdrBlk><MsgType>298</MsgType><MsgIPRef><BIC>CGDXTHB1</BIC></MsgIPRef><DataRefNum/><UserID/></AppHdrBlk><UsrHdrBlk><BankPrior>0XXX</BankPrior></UsrHdrBlk><TxtBlk>")
                          .append("<T20_TxRefNum>")
                          .append(gmFilename.replaceAll("GM", "GFMIS").trim())
                          .append("</T20_TxRefNum>")
                          .append("<T12_SubMsgType>")
                          .append("101")
                          .append("</T12_SubMsgType>")
                          .append("<T77_PropMsg Opt = \"E\">")
                          .append("<Info1 Code=\"SPRO\">GGC</Info1>")
                          .append("<Info2 Code=\"RECE\">BOTHTHB1CAT</Info2>")
                          .append("<Info3>For SMART Transfer Effective Date")
                          .append(gm.format(generateFileAlias.getSmartDate()))
                          .append("</Info3>")
                          .append("<Info4>Reference CodeECGD01</Info4>");
                  for (int j = 0; j < smartFileGMs.size(); j++) {
                    gmXml
                        .append("<Info5>Seq No.")
                        .append(j + 1)
                        .append(",Source File")
                        .append(smartFileGMs.get(j).getFilename().trim())
                        .append(",Number of transaction")
                        .append(smartFileGMs.get(j).getTransNo().trim())
                        .append("</Info5>")
                        .append("<Info6>Amount")
                        .append(smartFileGMs.get(j).getAmount().trim())
                        .append("BAHT,Fee")
                        .append(smartFileGMs.get(j).getFee().trim())
                        .append("BAHT,Total")
                        .append(smartFileGMs.get(j).getTotal().trim())
                        .append("BAHT")
                        .append("</Info6>");
                    totalAmt =
                        totalAmt.add(Util.getBigDecimal(smartFileGMs.get(j).getAmount().trim()));
                    totalFee =
                        totalFee.add(Util.getBigDecimal(smartFileGMs.get(j).getFee().trim()));
                    netAmt = netAmt.add(Util.getBigDecimal(smartFileGMs.get(j).getTotal().trim()));
                  }
                  gmXml
                      .append("<Info7>Grand Total Amount")
                      .append(df.format(totalAmt))
                      .append("BAHT,Fee")
                      .append(df.format(totalFee))
                      .append("BAHT,Total")
                      .append(df.format(netAmt))
                      .append("BAHT")
                      .append("</Info7>")
                      .append("</T77_PropMsg>")
                      .append("</TxtBlk></SWML></Upload>");
                  InputStream gmStream =
                      new ByteArrayInputStream(gmXml.toString().getBytes(StandardCharsets.UTF_8));
                  fileTransferService.uploadFile(gmStream, directory, gmFilename + ".xml");
                }
                k++;
              }
            }
          }
        } else {
          List<OutputReport> outputReports = new ArrayList<>();
          OutputReport outputReport = new OutputReport();
          StringBuilder outputText = new StringBuilder(smartFileHeader.toString());
          List<SmartFileBatch> smartFileBatches = smartFileHeader.getSmartFileBatches();
          int batch = 0;
          generateFile.setReference(String.valueOf(refRunning));
          for (SmartFileBatch smartFileBatch : smartFileBatches) {
            outputText.append("\n");
            outputText.append(smartFileBatch.toString());

            List<SmartFileDetail> smartFileDetails = smartFileBatch.getSmartFileDetails();
            String smartRunning = "";
            if (batch == 0) {
              if (isTestRun) {
                smartRunning = "$$$$$";
              } else {
                smartRunning =
                    String.valueOf(
                        generateFileSequenceService.getCurrentSeq(
                            "PAIN_SEQ", generateFileAlias.getSmartDate()));
              }
            } else if (batch % 40 == 0) {
              //                            metaDataService.saveAllProposalLog(proposalLogs);
              proposalLogs = new ArrayList<>();
              SmartFileFooter smartFileFooter = smartFileHeader.getSmartFileFooter();
              outputText.append("\n");
              outputText.append(smartFileFooter.toString());
              outputReport.setOutputText(outputText.toString());
              //                            Files.writeString(Paths.get(path + "/" +
              // outputReport.getFileName()), outputText.toString());
              InputStream inputStream =
                  new ByteArrayInputStream(outputText.toString().getBytes(StandardCharsets.UTF_8));
              fileTransferService.uploadFile(inputStream, directory, outputReport.getFileName());
              outputReports.add(outputReport);
              generateFile.setSmarts(outputReports);
            }

            for (SmartFileDetail smartFileDetail : smartFileDetails) {
              outputText.append("\n");
              outputText.append(smartFileDetail.toString());
              SmartFileLog smartFileLog = smartFileDetail.getSmartFileLog();
              if (null == proposalLogHeader) {
                proposalLogHeader = new ProposalLogHeader();
                proposalLogHeader.setId(proposalHeaderId);
                proposalLogHeader.setRefRunning(refRunning);
                proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
                proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
                proposalLogHeader.setCreatedBy(jwt.getSub());
                proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
                proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
                proposalLogHeader.setCancel(false);
                proposalLogHeader.setUse(isAll);
                if (!generateFileAlias.isTestRun()) {
                  proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
                }
              }
              ProposalLog proposalLog = new ProposalLog();
              proposalLog.setId(proposalId++);
              proposalLog.setRefRunning(refRunning);
              proposalLog.setRefLine(++i);
              proposalLog.setPaymentName(smartFileLog.getPaymentName());
              proposalLog.setPaymentDate(smartFileLog.getPaymentDate());
              String paymentType = smartFileDetail.getPaymentType();
              String fileName = "";
              if (paymentType.equalsIgnoreCase("D3")
                  || paymentType.equalsIgnoreCase("D4")
                  || paymentType.equalsIgnoreCase("D5")) {
                proposalLog.setAccountNoFrom(smartFileDetail.getSendAcct());
                proposalLog.setAccountNoTo(smartFileDetail.getRecAcct());
                proposalLog.setFileType("SMART");
                String accountNo = "0" + this.account.get(paymentType).getAccountNo();
                LocalDate localDate =
                    LocalDate.ofInstant(
                        generateFileAlias.getSmartDate().toInstant(), ZoneId.systemDefault());
                fileName =
                    "2"
                        + accountNo.substring(6)
                        + String.format("%02d", localDate.getDayOfMonth())
                        + "."
                        + smartRunning;
                outputReport.setFileName(fileName);
                proposalLog.setFileName(fileName);
                proposalLog.setTransferDate(generateFileAlias.getSmartDate());
                BigDecimal amount =
                    Util.getBigDecimal(smartFileDetail.getTransferAmt())
                        .divide(Util.getBigDecimal(100));
                proposalLog.setAmount(amount);
                proposalLog.setBankFee(smartFileLog.getFee());
              } else {
                String tr2Acc = "0" + this.account.get("TR2").getAccountNo();
                String accountNo = "0" + this.account.get(paymentType).getAccountNo();
                proposalLog.setAccountNoFrom(tr2Acc);
                proposalLog.setAccountNoTo(accountNo);
                proposalLog.setFileType("CGD");
                proposalLog.setTransferDate(generateFileAlias.getSwiftDate());
                BigDecimal amount =
                    Util.getBigDecimal(smartFileDetail.getTransferAmt())
                        .divide(Util.getBigDecimal(100))
                        .add(smartFileLog.getFee());
                proposalLog.setAmount(amount);
              }
              proposalLog.setVendor(smartFileLog.getVendor());
              proposalLog.setBankKey(smartFileLog.getBankKey());
              proposalLog.setVendorBankAccount(smartFileLog.getBankAccountNo());
              proposalLog.setTransferLevel("9");
              //              String payAccount = "D" + smartFileLog.getPaymentMethod();

              proposalLog.setPayAccount(smartFileDetail.getPaymentType());
              proposalLog.setPayingCompCode(smartFileLog.getPayingCompCode());
              proposalLog.setPaymentDocument(smartFileLog.getPaymentDocNo());
              proposalLog.setFiscalYear(smartFileLog.getPaymentYear());
              proposalLog.setFiArea(smartFileLog.getFiArea());
              proposalLog.setCreditMemoAmount(smartFileLog.getCreditMemo());
              proposalLog.setCreatedBy(jwt.getSub());
              proposalLog.setUpdatedBy(jwt.getSub());
              proposalLog.setCreated(created);
              proposalLog.setUpdated(created);
              proposalLog.setSendStatus("S");

              proposalLog.setOriginalCompCode(smartFileLog.getOriginalCompCode());
              proposalLog.setOriginalDocNo(smartFileLog.getOriginalAccDocNo());
              proposalLog.setOriginalDocType(smartFileLog.getOriginalDocType());
              proposalLog.setOriginalFiscalYear(smartFileLog.getOriginalFiscalYear());

              proposalLog.setInvCompCode(smartFileLog.getInvoiceCompCode());
              proposalLog.setInvDocNo(smartFileLog.getInvoiceAccDocNo());
              proposalLog.setInvDocType(smartFileLog.getInvoiceDocType());
              proposalLog.setInvFiscalYear(smartFileLog.getFiscalYear());

              proposalLog.setOriginalWtxAmount(smartFileLog.getOriginalWtxAmount());
              proposalLog.setOriginalWtxBase(smartFileLog.getOriginalWtxBase());
              proposalLog.setOriginalWtxAmountP(smartFileLog.getOriginalWtxAmountP());
              proposalLog.setOriginalWtxBaseP(smartFileLog.getOriginalWtxBaseP());

              proposalLog.setInvWtxAmount(smartFileLog.getInvoiceWtxAmount());
              proposalLog.setInvWtxBase(smartFileLog.getInvoiceWtxBase());
              proposalLog.setInvWtxAmountP(smartFileLog.getInvoiceWtxAmountP());
              proposalLog.setInvWtxBaseP(smartFileLog.getInvoiceWtxBaseP());

              proposalLog.setPaymentCompCode(smartFileLog.getPaymentCompCode());
              proposalLog.setPaymentFiscalYear(smartFileLog.getPaymentFiscalYear());

              proposalLog.setPaymentType(smartFileDetail.getPaymentType());

              proposalLog.setRefRunningSum(0L);
              proposalLog.setRefLineSum(0);

              proposalLog.setRerun(false);

              proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());
              //                            log.info("SMART ADD PROPOSAL LOG FROM smartFileLog : {}
              // ", smartFileLog.getId());
              if (!generateFileAlias.isTestRun()) {
                //                ProposalLog checkDuplicateFile =
                // proposalLogService.findOneByFileNameAndPaymentDocument(fileName,
                // smartFileLog.getPaymentDocNo());
                //                log.info("checkDuplicateFile : {}", checkDuplicateFile);
                //                if (null == checkDuplicateFile) {
                proposalLogs.add(proposalLog);
                //                }
              }
            }
            batch++;
          }

          SmartFileFooter smartFileFooter = smartFileHeader.getSmartFileFooter();
          outputText.append("\n");
          outputText.append(smartFileFooter.toString());
          outputReport.setOutputText(outputText.toString());
          //                    Files.writeString(Paths.get(path + "/" +
          // outputReport.getFileName()), outputText.toString());
          if (!generateFileAlias.isTestRun()) {
            InputStream inputStream =
                new ByteArrayInputStream(outputText.toString().getBytes(StandardCharsets.UTF_8));
            fileTransferService.uploadFile(inputStream, directory, outputReport.getFileName());
          }

          outputReports.add(outputReport);
          generateFile.setSmarts(outputReports);
          //                    metaDataService.saveAllProposalLog(proposalLogs);
        }
      }

      SmartFileHeader smartFileHeaderSumFile = generateFileAlias.getSmartFileHeaderSumFile();
      if (null != smartFileHeaderSumFile) {
        if (usePain) {
          String xmlType = "";
          Timestamp curDate =
              new Timestamp(
                  Date.from(
                          new Date()
                              .toInstant()
                              .atZone(ZoneId.systemDefault())
                              .truncatedTo(ChronoUnit.DAYS)
                              .toInstant())
                      .getTime());
          Timestamp effDate =
              Util.stringSmartToTimestamp(
                  smartFileHeaderSumFile.getSmartFileBatches().get(0).getEffDate());
          if (curDate.equals(effDate)) {
            xmlType = "SDVA";
          } else {
            xmlType = "NURG";
          }
          List<OutputReport> outputReports = new ArrayList<>();
          List<OutputReport> outputReports2 = new ArrayList<>();
          List<SmartFileBatch> smartFileBatches = smartFileHeaderSumFile.getSmartFileBatches();
          List<SmartFileGM> smartFileGMs = generateFileAlias.getSmartFileGMs();
          int batch = 0;
          generateFile.setReference(String.valueOf(refRunning));
          if (!smartFileBatches.isEmpty()) {
            String paymentType =
                smartFileBatches.get(0).getSmartFileDetails().get(0).getPaymentType();
            if (paymentType.equalsIgnoreCase("D3")
                || paymentType.equalsIgnoreCase("D4")
                || paymentType.equalsIgnoreCase("D5")) {
              SimpleDateFormat sdfGM = new SimpleDateFormat("yyyyMMdd");
              String gmFilename;
              if (isTestRun) {
                gmFilename = "GM" + sdfGM.format(generateFileAlias.getSmartDate()) + "$$$";
              } else {
                Long gmRunning =
                    generateFileSequenceService.getCurrentSeq(
                        "GM_SEQ", generateFileAlias.getSmartDate());
                gmFilename = "GM" + sdfGM.format(generateFileAlias.getSmartDate()) + gmRunning;
              }

              int k = 0;
              for (SmartFileBatch smartFileBatch : smartFileBatches) {
                Map<String, List<SmartFileDetail>> groupByVendor =
                    smartFileBatch.getSmartFileDetails().stream()
                        .collect(
                            Collectors.groupingBy(
                                this::groupByMultipleKey,
                                Collectors.mapping((SmartFileDetail s) -> s, toList())));

                OutputReport outputReport = new OutputReport();
                OutputReport outputReport2 = new OutputReport();
                StringBuilder outputText = new StringBuilder(smartFileHeaderSumFile.toString());
                outputText.append("\n");
                outputText.append(smartFileBatch.toString());
                Account account = this.account.get(paymentType);
                //                                BigDecimal bankFee = calculateSmartFee(smartFees,
                // account.getAccountNo(),
                // smartFileHeaderSumFile.getSmartFileFooter().getTotalAmt());

                //                        Pain001 pain001 = new Pain001();
                Document document = new Document();
                CstmrCdtTrfInitn cstmrCdtTrfInitn = new CstmrCdtTrfInitn();
                // cstmrCdtTrfInitn section
                GrpHdr grpHdr = new GrpHdr();
                String painRunning;
                if (isTestRun) {
                  painRunning = "$$$$$";
                } else {
                  painRunning =
                      String.valueOf(
                          generateFileSequenceService.getCurrentSeq(
                              "PAIN_SEQ", generateFileAlias.getSmartDate()));
                }
                String msgId =
                    "ECGD01-"
                        + Util.timestampToStringForIndependent(smartFileHeaderSumFile.getEffDate())
                        + "-OC-"
                        + xmlType
                        + "-"
                        + painRunning;
                String recordId =
                    "ECGD01-"
                        + Util.timestampToStringForIndependent(smartFileHeaderSumFile.getEffDate())
                        + "-"
                        + xmlType
                        + "-"
                        + painRunning;
                smartFileGMs.get(k).setFilename(msgId);
                outputReport2.setOutputText(smartFileGMs.get(k).toString());
                outputReport2.setFileName(gmFilename);
                grpHdr.setMsgID(msgId);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String creDtTm = sdf.format(date);
                grpHdr.setCreDtTm(creDtTm);
                grpHdr.setNbOfTxs(String.valueOf(groupByVendor.size()));
                grpHdr.setCtrlSum(
                    String.format(
                        "%.2f", smartFileHeaderSumFile.getSmartFileFooter().getTotalAmt()));
                InitgPty initgPty = new InitgPty();
                initgPty.setNm("กรมบัญชีกลาง");
                InitgPtyID initgPtyID = new InitgPtyID();
                OrgID orgID = new OrgID();
                Othr othr = new Othr();
                othr.setId("0994000159510");
                CtgyPurp schmeNm = new CtgyPurp();
                schmeNm.setCd("TXID");
                othr.setSchmeNm(schmeNm);
                orgID.setOthr(othr);
                initgPtyID.setOrgID(orgID);
                initgPty.setId(initgPtyID);
                grpHdr.setInitgPty(initgPty);
                PmtInf pmtInf = new PmtInf();
                pmtInf.setPmtInfID(recordId + "001");
                pmtInf.setPmtMtd("TRF");
                pmtInf.setNbOfTxs(String.valueOf(groupByVendor.size()));
                pmtInf.setCtrlSum(
                    String.format(
                        "%.2f", smartFileHeaderSumFile.getSmartFileFooter().getTotalAmt()));
                ReqdExctnDt reqdExctnDt = new ReqdExctnDt();
                reqdExctnDt.setDt(Util.timestampToString(smartFileHeaderSumFile.getEffDate()));
                pmtInf.setReqdExctnDt(reqdExctnDt);
                pmtInf.setDbtr(initgPty);
                TrAcct trAcct = new TrAcct();
                DbtrAcctID dbtrAcctID = new DbtrAcctID();
                BrnchID brnchID = new BrnchID();
                brnchID.setId(account.getAccountNo());
                dbtrAcctID.setOthr(brnchID);
                trAcct.setId(dbtrAcctID);
                trAcct.setCcy("THB");
                pmtInf.setDbtrAcct(trAcct);

                TrAgt trAgt = new TrAgt();
                FinInstnID finInstnID = new FinInstnID();
                CLRSysMmbID clrSysMmbID = new CLRSysMmbID();
                CtgyPurp clrSysID = new CtgyPurp();
                clrSysID.setCd("THCBC");
                clrSysMmbID.setClrSysID(clrSysID);
                clrSysMmbID.setMmbID("001");
                finInstnID.setClrSysMmbID(clrSysMmbID);
                trAgt.setFinInstnID(finInstnID);
                BrnchID brnchID1 = new BrnchID();
                brnchID1.setId("0001");
                trAgt.setBrnchID(brnchID1);
                pmtInf.setDbtrAgt(trAgt);
                AtomicInteger record = new AtomicInteger();
                List<CdtTrfTxInf> cdtTrfTxInfs = new ArrayList<>();

                for (var entry : groupByVendor.entrySet()) {
                  BigDecimal totalTransferAmount =
                      entry.getValue().stream()
                          .map(SmartFileDetail::getTransferAmount) // map
                          .reduce(BigDecimal.ZERO, BigDecimal::add);
                  //                                    log.info("vendor : {}", entry.getKey());
                  //                                    log.info("totalTransferAmount : {}",
                  // totalTransferAmount);
                  SmartFileDetail smartFileDetailSum = new SmartFileDetail(entry.getValue().get(0));
                  SmartFileLog smartFileLogSum = smartFileDetailSum.getSmartFileLog();
                  if (smartFileLogSum.getFee().compareTo(BigDecimal.ZERO) == 0) {
                    int transferAmt =
                        totalTransferAmount
                            .subtract(smartFileLogSum.getSumFileFee())
                            .multiply(Util.getBigDecimal(100))
                            .intValue();
                    smartFileDetailSum.setTransferAmt(
                        StringUtils.leftPad(String.valueOf(transferAmt), 12, "0"));
                  } else {
                    int transferAmt =
                        totalTransferAmount
                            .subtract(smartFileLogSum.getFee())
                            .multiply(Util.getBigDecimal(100))
                            .intValue();
                    smartFileDetailSum.setTransferAmt(
                        StringUtils.leftPad(String.valueOf(transferAmt), 12, "0"));
                  }
                  outputText.append("\n");
                  outputText.append(smartFileDetailSum.toString());
                  CdtTrfTxInf cdtTrfTxInf = new CdtTrfTxInf();
                  PmtID pmtID = new PmtID();
                  pmtID.setEndToEndID(
                      recordId
                          + "001"
                          + StringUtils.leftPad(String.valueOf(record.incrementAndGet()), 5, "0"));
                  cdtTrfTxInf.setPmtID(pmtID);
                  PmtTpInf pmtTpInf = new PmtTpInf();
                  CtgyPurp svcLvl = new CtgyPurp();
                  svcLvl.setCd(xmlType);
                  pmtTpInf.setSvcLvl(svcLvl);
                  CtgyPurp ctgyPurp = new CtgyPurp();
                  ctgyPurp.setCd("SUPP"); // ขาด logic
                  pmtTpInf.setCtgyPurp(ctgyPurp);
                  cdtTrfTxInf.setPmtTpInf(pmtTpInf);
                  Amt amt = new Amt();
                  InstdAmt instdAmt = new InstdAmt();
                  if (entry
                          .getValue()
                          .get(0)
                          .getSmartFileLog()
                          .getSumFileFee()
                          .compareTo(BigDecimal.ZERO)
                      > 0) {
                    instdAmt.setText(
                        String.format(
                            "%.2f",
                            totalTransferAmount.subtract(
                                entry.getValue().get(0).getSmartFileLog().getSumFileFee())));
                  } else {
                    instdAmt.setText(
                        String.format(
                            "%.2f",
                            totalTransferAmount.subtract(
                                entry.getValue().get(0).getSmartFileLog().getFee())));
                  }
                  instdAmt.setCcy("THB");
                  amt.setInstdAmt(instdAmt);
                  cdtTrfTxInf.setAmt(amt);
                  cdtTrfTxInf.setChrgBr("SLEV");
                  TrAgt cdtrAgt = new TrAgt();
                  CtgyPurp clrSysID1 = new CtgyPurp();
                  clrSysID1.setCd("THCBC");
                  CLRSysMmbID clrSysMmbID1 = new CLRSysMmbID();
                  clrSysMmbID1.setClrSysID(clrSysID1);
                  clrSysMmbID1.setMmbID(entry.getValue().get(0).getRecBankCode());
                  FinInstnID finInstnID1 = new FinInstnID();
                  finInstnID1.setClrSysMmbID(clrSysMmbID1);
                  cdtrAgt.setFinInstnID(finInstnID1);
                  BrnchID brnchID2 = new BrnchID();
                  brnchID2.setId(entry.getValue().get(0).getRecBranchCode());
                  cdtrAgt.setBrnchID(brnchID2);
                  cdtTrfTxInf.setCdtrAgt(cdtrAgt);
                  Cdtr cdtr = new Cdtr();
                  cdtr.setNm(entry.getValue().get(0).getSendInform().trim());
                  cdtTrfTxInf.setCdtr(cdtr);
                  TrAcct cdtrAcct = new TrAcct();
                  DbtrAcctID dbtrAcctID1 = new DbtrAcctID();
                  BrnchID othr1 = new BrnchID();
                  othr1.setId(entry.getValue().get(0).getRecAcct());
                  dbtrAcctID1.setOthr(othr1);
                  cdtrAcct.setId(dbtrAcctID1);
                  cdtrAcct.setCcy("THB");
                  cdtTrfTxInf.setCdtrAcct(cdtrAcct);
                  if (entry.getValue().get(0).getSmartFileLog().getFee().compareTo(BigDecimal.ZERO)
                      == 0) {
                    if (entry
                                .getValue()
                                .get(0)
                                .getSmartFileLog()
                                .getSumFileFee()
                                .compareTo(Util.getBigDecimal(12))
                            == 0
                        || entry.getValue().get(0).getPaymentType().equalsIgnoreCase("D3")) {
                      cdtTrfTxInf.setInstrForDbtrAgt("INSF");
                    } else {
                      cdtTrfTxInf.setInstrForDbtrAgt("EXSF");
                    }
                  } else {
                    if (entry
                                .getValue()
                                .get(0)
                                .getSmartFileLog()
                                .getFee()
                                .compareTo(Util.getBigDecimal(12))
                            == 0
                        || entry.getValue().get(0).getPaymentType().equalsIgnoreCase("D3")) {
                      cdtTrfTxInf.setInstrForDbtrAgt("INSF");
                    } else {
                      cdtTrfTxInf.setInstrForDbtrAgt("EXSF");
                    }
                  }

                  cdtTrfTxInfs.add(cdtTrfTxInf);

                  if (!generateFileAlias.isTestRun()) {
                    if (null == proposalLogHeader) {
                      proposalLogHeader = new ProposalLogHeader();
                      proposalLogHeader.setId(proposalHeaderId);
                      proposalLogHeader.setRefRunning(refRunning);
                      proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
                      proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
                      proposalLogHeader.setCreatedBy(jwt.getSub());
                      proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
                      proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
                      proposalLogHeader.setCancel(false);
                      proposalLogHeader.setUse(isAll);
                      proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
                    }
                    ProposalLogSum proposalLogSum = new ProposalLogSum();
                    proposalLogSum.setId(proposalSumId++);
                    proposalLogSum.setRefRunning(refRunning);
                    proposalLogSum.setRefLine(++refLineSum);
                    proposalLogSum.setPaymentName(paymentAlias.getPaymentName());
                    proposalLogSum.setPaymentDate(paymentAlias.getPaymentDate());
                    proposalLogSum.setAccountNoFrom(entry.getValue().get(0).getSendAcct());
                    proposalLogSum.setAccountNoTo(entry.getValue().get(0).getRecAcct());
                    proposalLogSum.setFileType("SMART");
                    proposalLogSum.setFileName(msgId);
                    proposalLogSum.setTransferDate(generateFileAlias.getSmartDate());
                    if (entry
                            .getValue()
                            .get(0)
                            .getSmartFileLog()
                            .getFee()
                            .compareTo(BigDecimal.ZERO)
                        == 0) {
                      proposalLogSum.setAmount(
                          totalTransferAmount.subtract(
                              entry.getValue().get(0).getSmartFileLog().getSumFileFee()));
                      proposalLogSum.setBankFee(
                          entry.getValue().get(0).getSmartFileLog().getSumFileFee());
                    } else {
                      proposalLogSum.setAmount(
                          totalTransferAmount.subtract(
                              entry.getValue().get(0).getSmartFileLog().getFee()));
                      proposalLogSum.setBankFee(entry.getValue().get(0).getSmartFileLog().getFee());
                    }
                    proposalLogSum.setVendor(entry.getValue().get(0).getSmartFileLog().getVendor());
                    proposalLogSum.setBankKey(
                        entry.getValue().get(0).getSmartFileLog().getBankKey());
                    proposalLogSum.setVendorBankAccount(
                        entry.getValue().get(0).getSmartFileLog().getBankAccountNo());
                    proposalLogSum.setTransferLevel("9");
                    proposalLogSum.setPayAccount(entry.getValue().get(0).getPaymentType());
                    proposalLogSum.setPayingCompCode(
                        entry.getValue().get(0).getSmartFileLog().getPayingCompCode());
                    proposalLogSum.setCreditMemoAmount(
                        entry.getValue().get(0).getSmartFileLog().getCreditMemo());
                    proposalLogSum.setCreatedBy(jwt.getSub());
                    proposalLogSum.setUpdatedBy(jwt.getSub());
                    proposalLogSum.setCreated(created);
                    proposalLogSum.setUpdated(created);
                    proposalLogSum.setSendStatus("S");
                    proposalLogSum.setPaymentType(entry.getValue().get(0).getPaymentType());
                    proposalLogSum.setProposalLogHeaderId(proposalLogHeader.getId());

                    proposalLogSum.setRerun(false);

                    proposalLogSums.add(proposalLogSum);
                  }
                  for (SmartFileDetail smartFileDetail : entry.getValue()) {
                    SmartFileLog smartFileLog = smartFileDetail.getSmartFileLog();
                    BigDecimal amount =
                        Util.getBigDecimal(smartFileDetail.getTransferAmt())
                            .divide(Util.getBigDecimal(100));
                    if (!generateFileAlias.isTestRun()) {
                      ProposalLog proposalLog = new ProposalLog();
                      proposalLog.setId(proposalId++);
                      proposalLog.setRefRunning(refRunning);
                      proposalLog.setRefLine(++i);
                      proposalLog.setPaymentName(smartFileLog.getPaymentName());
                      proposalLog.setPaymentDate(smartFileLog.getPaymentDate());
                      proposalLog.setAccountNoFrom(smartFileDetail.getSendAcct());
                      proposalLog.setAccountNoTo(smartFileDetail.getRecAcct());
                      proposalLog.setFileType("SMART");
                      outputReport.setFileName(msgId);
                      proposalLog.setFileName(msgId);
                      proposalLog.setTransferDate(generateFileAlias.getSmartDate());
                      proposalLog.setAmount(amount);
                      if (entry.getValue().size() > 1) {
                        proposalLog.setBankFee(BigDecimal.ZERO);
                      } else {
                        proposalLog.setBankFee(smartFileLog.getFee());
                      }
                      proposalLog.setVendor(smartFileLog.getVendor());
                      proposalLog.setBankKey(smartFileLog.getBankKey());
                      proposalLog.setVendorBankAccount(smartFileLog.getBankAccountNo());
                      proposalLog.setTransferLevel("9");

                      proposalLog.setPayAccount(smartFileDetail.getPaymentType());
                      proposalLog.setPayingCompCode(smartFileLog.getPayingCompCode());
                      proposalLog.setPaymentDocument(smartFileLog.getPaymentDocNo());
                      proposalLog.setFiscalYear(smartFileLog.getPaymentYear());
                      proposalLog.setFiArea(smartFileLog.getFiArea());
                      proposalLog.setCreditMemoAmount(smartFileLog.getCreditMemo());
                      proposalLog.setCreatedBy(jwt.getSub());
                      proposalLog.setUpdatedBy(jwt.getSub());
                      proposalLog.setCreated(created);
                      proposalLog.setUpdated(created);
                      proposalLog.setSendStatus("S");

                      proposalLog.setOriginalCompCode(smartFileLog.getOriginalCompCode());
                      proposalLog.setOriginalDocNo(smartFileLog.getOriginalAccDocNo());
                      proposalLog.setOriginalDocType(smartFileLog.getOriginalDocType());
                      proposalLog.setOriginalFiscalYear(smartFileLog.getOriginalFiscalYear());

                      proposalLog.setInvCompCode(smartFileLog.getInvoiceCompCode());
                      proposalLog.setInvDocNo(smartFileLog.getInvoiceAccDocNo());
                      proposalLog.setInvDocType(smartFileLog.getInvoiceDocType());
                      proposalLog.setInvFiscalYear(smartFileLog.getInvoiceFiscalYear());

                      proposalLog.setOriginalWtxAmount(smartFileLog.getOriginalWtxAmount());
                      proposalLog.setOriginalWtxBase(smartFileLog.getOriginalWtxBase());
                      proposalLog.setOriginalWtxAmountP(smartFileLog.getOriginalWtxAmountP());
                      proposalLog.setOriginalWtxBaseP(smartFileLog.getOriginalWtxBaseP());

                      proposalLog.setInvWtxAmount(smartFileLog.getInvoiceWtxAmount());
                      proposalLog.setInvWtxBase(smartFileLog.getInvoiceWtxBase());
                      proposalLog.setInvWtxAmountP(smartFileLog.getInvoiceWtxAmountP());
                      proposalLog.setInvWtxBaseP(smartFileLog.getInvoiceWtxBaseP());

                      proposalLog.setPaymentCompCode(smartFileLog.getPaymentCompCode());
                      proposalLog.setPaymentFiscalYear(smartFileLog.getPaymentFiscalYear());

                      proposalLog.setPaymentType(smartFileDetail.getPaymentType());

                      proposalLog.setRefRunningSum(refRunning);
                      proposalLog.setRefLineSum(refLineSum);

                      proposalLog.setRerun(false);

                      proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());
                      proposalLogs.add(proposalLog);
                      //                  }
                    }
                  }
                }

                SmartFileFooter smartFileFooter = smartFileHeaderSumFile.getSmartFileFooter();
                outputText.append("\n");
                outputText.append(smartFileFooter.toString());
                outputReport.setFileName(msgId);
                outputReport.setOutputText(outputText.toString());
                outputReports.add(outputReport);
                outputReports2.add(outputReport2);

                generateFile.setSmarts(outputReports);
                generateFile.setGm(outputReports2);

                pmtInf.setCdtTrfTxInfs(cdtTrfTxInfs);
                cstmrCdtTrfInitn.setGrpHdr(grpHdr);
                cstmrCdtTrfInitn.setPmtInf(pmtInf);
                document.setCstmrCdtTrfInitn(cstmrCdtTrfInitn);
                //                        pain001.setDocument(document);
                String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
                String xml = header + XMLUtil.xmlMarshall(Document.class, document);

                if (!generateFileAlias.isTestRun()) {
                  InputStream inputStream =
                      new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
                  fileTransferService.uploadFile(inputStream, directory, msgId + ".xml");
                }
                k++;
              }
              if (!generateFileAlias.isTestRun()) {
                BigDecimal totalAmt = BigDecimal.ZERO;
                BigDecimal totalFee = BigDecimal.ZERO;
                BigDecimal netAmt = BigDecimal.ZERO;
                SimpleDateFormat gm = new SimpleDateFormat("dd/MM/yyyy");
                StringBuilder gmXml =
                    new StringBuilder()
                        .append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Upload>")
                        .append("<SWML version=\"1.0\"><BasicHdrBlk><LTID/></BasicHdrBlk>")
                        .append(
                            "<AppHdrBlk><MsgType>298</MsgType><MsgIPRef><BIC>CGDXTHB1</BIC></MsgIPRef><DataRefNum/><UserID/></AppHdrBlk><UsrHdrBlk><BankPrior>0XXX</BankPrior></UsrHdrBlk><TxtBlk>")
                        .append("<T20_TxRefNum>")
                        .append(gmFilename.replaceAll("GM", "GFMIS").trim())
                        .append("</T20_TxRefNum>")
                        .append("<T12_SubMsgType>")
                        .append("101")
                        .append("</T12_SubMsgType>")
                        .append("<T77_PropMsg Opt = \"E\">")
                        .append("<Info1 Code=\"SPRO\">GGC</Info1>")
                        .append("<Info2 Code=\"RECE\">BOTHTHB1CAT</Info2>")
                        .append("<Info3>For SMART Transfer Effective Date")
                        .append(gm.format(generateFileAlias.getSmartDate()))
                        .append("</Info3>")
                        .append("<Info4>Reference CodeECGD01</Info4>");
                for (int j = 0; j < smartFileGMs.size(); j++) {
                  gmXml
                      .append("<Info5>Seq No.")
                      .append(j + 1)
                      .append(",Source File")
                      .append(smartFileGMs.get(j).getFilename().trim())
                      .append(",Number of transaction")
                      .append(smartFileGMs.get(j).getTransNo().trim())
                      .append("</Info5>")
                      .append("<Info6>Amount")
                      .append(smartFileGMs.get(j).getAmount().trim())
                      .append("BAHT,Fee")
                      .append(smartFileGMs.get(j).getFee().trim())
                      .append("BAHT,Total")
                      .append(smartFileGMs.get(j).getTotal().trim())
                      .append("BAHT")
                      .append("</Info6>");
                  totalAmt =
                      totalAmt.add(Util.getBigDecimal(smartFileGMs.get(j).getAmount().trim()));
                  totalFee = totalFee.add(Util.getBigDecimal(smartFileGMs.get(j).getFee().trim()));
                  netAmt = netAmt.add(Util.getBigDecimal(smartFileGMs.get(j).getTotal().trim()));
                }
                gmXml
                    .append("<Info7>Grand Total Amount")
                    .append(df.format(totalAmt))
                    .append("BAHT,Fee")
                    .append(df.format(totalFee))
                    .append("BAHT,Total")
                    .append(df.format(netAmt))
                    .append("BAHT")
                    .append("</Info7>")
                    .append("</T77_PropMsg>")
                    .append("</TxtBlk></SWML></Upload>");
                InputStream gmStream =
                    new ByteArrayInputStream(gmXml.toString().getBytes(StandardCharsets.UTF_8));
                fileTransferService.uploadFile(gmStream, directory, gmFilename + ".xml");
              }
            }
          }
        }
      }

      List<SwiftFile> swiftFileMasters = generateFileAlias.getSwiftFileMasters();
      Map<String, List<SwiftFile>> swiftMapLevelMaster =
          swiftFileMasters.stream()
              .collect(
                  Collectors.groupingBy(
                      this::getGroupingByKey,
                      Collectors.mapping((SwiftFile s) -> s, Collectors.toList())));

      List<OutputReport> outputReportSwifts = new ArrayList<>();
      List<Integer> refLineList = new ArrayList<>();
      if (null != swiftFileMasters && !swiftFileMasters.isEmpty()) {
        for (SwiftFile swiftFile : swiftFileMasters) {
          generateFile.setReference(String.valueOf(refRunning));
          OutputReport outputReport = new OutputReport();

          SwiftFileLog swiftFileLog = swiftFile.getSwiftFileLog();
          String fileName = swiftFile.getFileName();
          outputReport.setFileName(fileName);
          if (!generateFileAlias.isTestRun()) {
            if (null == proposalLogHeader) {
              proposalLogHeader = new ProposalLogHeader();
              proposalLogHeader.setId(proposalHeaderId);
              proposalLogHeader.setRefRunning(refRunning);
              proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
              proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
              proposalLogHeader.setCreatedBy(jwt.getSub());
              proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
              proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
              proposalLogHeader.setCancel(false);
              proposalLogHeader.setUse(isAll);
              proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
            }
            if (isSumFile) {
              ProposalLogSum proposalLog = new ProposalLogSum();
              proposalLog.setId(proposalSumId++);
              proposalLog.setRefRunning(refRunning);
              proposalLog.setRefLine(++refLineSum);
              proposalLog.setPaymentName(paymentAlias.getPaymentName());
              proposalLog.setPaymentDate(paymentAlias.getPaymentDate());
              proposalLog.setAccountNoFrom(swiftFile.getSendAcct());
              proposalLog.setAccountNoTo(swiftFile.getRecAcct());
              proposalLog.setFileType("SWIFT");

              proposalLog.setFileName(fileName);
              proposalLog.setTransferDate(generateFileAlias.getSwiftDate());
              proposalLog.setAmount(swiftFile.getSetAmount());
              proposalLog.setBankFee(Util.getBigDecimal(0));
              proposalLog.setVendor(swiftFileLog.getVendor());
              proposalLog.setBankKey(swiftFileLog.getBankKey());
              proposalLog.setVendorBankAccount(swiftFileLog.getBankAccountNo());
              proposalLog.setTransferLevel(swiftFileLog.getTransferLevel());
              proposalLog.setPayAccount(swiftFile.getPayAccount());
              proposalLog.setPaymentType(swiftFile.getPayAccount());
              proposalLog.setPayingCompCode("99999");
              proposalLog.setPaymentDocument(swiftFileLog.getPaymentDocNo());
              proposalLog.setFiscalYear(swiftFileLog.getPaymentFiscalYear());
              proposalLog.setFiArea(swiftFileLog.getFiArea());
              proposalLog.setCreditMemoAmount(swiftFileLog.getCreditMemo());
              proposalLog.setCreatedBy(jwt.getSub());
              proposalLog.setUpdatedBy(jwt.getSub());
              proposalLog.setCreated(created);
              proposalLog.setUpdated(created);
              if (isRegen && !isAll) {
                proposalLog.setSendStatus("");
              } else {
                proposalLog.setSendStatus("S");
              }

              proposalLog.setOriginalCompCode(swiftFileLog.getOriginalCompCode());
              proposalLog.setOriginalDocNo(swiftFileLog.getOriginalAccDocNo());
              proposalLog.setOriginalDocType(swiftFileLog.getOriginalDocType());
              proposalLog.setOriginalFiscalYear(swiftFileLog.getOriginalFiscalYear());

              proposalLog.setOriginalWtxAmount(swiftFileLog.getOriginalWtxAmount());
              proposalLog.setOriginalWtxBase(swiftFileLog.getOriginalWtxBase());
              proposalLog.setOriginalWtxAmountP(swiftFileLog.getOriginalWtxAmountP());
              proposalLog.setOriginalWtxBaseP(swiftFileLog.getOriginalWtxBaseP());

              proposalLog.setInvCompCode(swiftFileLog.getInvoiceCompCode());
              proposalLog.setInvDocNo(swiftFileLog.getInvoiceAccDocNo());
              proposalLog.setInvDocType(swiftFileLog.getInvoiceDocType());
              proposalLog.setInvFiscalYear(swiftFileLog.getInvoiceFiscalYear());

              proposalLog.setInvWtxAmount(swiftFileLog.getInvoiceWtxAmount());
              proposalLog.setInvWtxBase(swiftFileLog.getInvoiceWtxBase());
              proposalLog.setInvWtxAmountP(swiftFileLog.getInvoiceWtxAmountP());
              proposalLog.setInvWtxBaseP(swiftFileLog.getInvoiceWtxBaseP());

              proposalLog.setPaymentCompCode(swiftFileLog.getPaymentCompCode());
              proposalLog.setPaymentFiscalYear(swiftFileLog.getPaymentFiscalYear());

              proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());

              proposalLog.setRerun(false);

              proposalLogSums.add(proposalLog);
            }
            int refLine = ++i;
            refLineList.add(refLine);
            ProposalLog proposalLog = new ProposalLog();
            proposalLog.setId(proposalId++);
            proposalLog.setRefRunning(refRunning);
            proposalLog.setRefLine(refLine);
            proposalLog.setPaymentName(paymentAlias.getPaymentName());
            proposalLog.setPaymentDate(paymentAlias.getPaymentDate());
            proposalLog.setAccountNoFrom(swiftFile.getSendAcct());
            proposalLog.setAccountNoTo(swiftFile.getRecAcct());
            proposalLog.setFileType("SWIFT");

            proposalLog.setFileName(fileName);
            proposalLog.setTransferDate(generateFileAlias.getSwiftDate());
            proposalLog.setAmount(swiftFile.getSetAmount());
            proposalLog.setBankFee(Util.getBigDecimal(0));
            proposalLog.setVendor(swiftFileLog.getVendor());
            proposalLog.setBankKey(swiftFileLog.getBankKey());
            proposalLog.setVendorBankAccount(swiftFileLog.getBankAccountNo());
            proposalLog.setTransferLevel(swiftFileLog.getTransferLevel());
            proposalLog.setPayAccount(swiftFile.getPayAccount());
            proposalLog.setPaymentType(swiftFile.getPayAccount());
            proposalLog.setPayingCompCode("99999");
            proposalLog.setPaymentDocument(swiftFileLog.getPaymentDocNo());
            proposalLog.setFiscalYear(swiftFileLog.getPaymentFiscalYear());
            proposalLog.setFiArea(swiftFileLog.getFiArea());
            proposalLog.setCreditMemoAmount(swiftFileLog.getCreditMemo());
            proposalLog.setCreatedBy(jwt.getSub());
            proposalLog.setUpdatedBy(jwt.getSub());
            proposalLog.setCreated(created);
            proposalLog.setUpdated(created);
            if (isRegen && !isAll) {
              proposalLog.setSendStatus("");
            } else {
              proposalLog.setSendStatus("S");
            }

            proposalLog.setOriginalCompCode(swiftFileLog.getOriginalCompCode());
            proposalLog.setOriginalDocNo(swiftFileLog.getOriginalAccDocNo());
            proposalLog.setOriginalDocType(swiftFileLog.getOriginalDocType());
            proposalLog.setOriginalFiscalYear(swiftFileLog.getOriginalFiscalYear());

            proposalLog.setOriginalWtxAmount(swiftFileLog.getOriginalWtxAmount());
            proposalLog.setOriginalWtxBase(swiftFileLog.getOriginalWtxBase());
            proposalLog.setOriginalWtxAmountP(swiftFileLog.getOriginalWtxAmountP());
            proposalLog.setOriginalWtxBaseP(swiftFileLog.getOriginalWtxBaseP());

            proposalLog.setInvCompCode(swiftFileLog.getInvoiceCompCode());
            proposalLog.setInvDocNo(swiftFileLog.getInvoiceAccDocNo());
            proposalLog.setInvDocType(swiftFileLog.getInvoiceDocType());
            proposalLog.setInvFiscalYear(swiftFileLog.getInvoiceFiscalYear());

            proposalLog.setInvWtxAmount(swiftFileLog.getInvoiceWtxAmount());
            proposalLog.setInvWtxBase(swiftFileLog.getInvoiceWtxBase());
            proposalLog.setInvWtxAmountP(swiftFileLog.getInvoiceWtxAmountP());
            proposalLog.setInvWtxBaseP(swiftFileLog.getInvoiceWtxBaseP());

            proposalLog.setPaymentCompCode(swiftFileLog.getPaymentCompCode());
            proposalLog.setPaymentFiscalYear(swiftFileLog.getPaymentFiscalYear());

            if (isSumFile) {
              proposalLog.setRefRunningSum(refRunning);
              proposalLog.setRefLineSum(refLineSum);
            } else {
              proposalLog.setRefRunningSum(0L);
              proposalLog.setRefLineSum(0);
            }

            proposalLog.setRerun(false);

            proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());

            proposalLogs.add(proposalLog);
          }

          outputReport.setOutputText(swiftFile.toString());
          outputReportSwifts.add(outputReport);
        }
      }
      AtomicInteger index = new AtomicInteger();

      if (!generateFileAlias.isTestRun()) {
        swiftMapLevelMaster.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(
                k -> {
                  List<SwiftFile> swiftFileList = k.getValue();
                  StringBuilder swiftXml =
                      new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Upload>");
                  String fileName = "";
                  for (SwiftFile swiftFile : swiftFileList) {
                    fileName = swiftFile.getFileName();
                    if (!isPac) {
                      swiftXml.append(
                          "<SWML version=\"1.0\"><BasicHdrBlk><LTID></LTID></BasicHdrBlk>");
                      swiftXml.append(
                          "<AppHdrBlk><MsgType>103</MsgType><MsgIPRef><BIC></BIC></MsgIPRef><DataRefNum></DataRefNum><UserID></UserID></AppHdrBlk>");
                      swiftXml.append("<UsrHdrBlk><BankPrior>8XXX</BankPrior></UsrHdrBlk>");
                      swiftXml.append("<TxtBlk>");
                      swiftXml.append("<T20_SendRef>");
                      swiftXml.append(
                          (null == swiftFile.getSendRef() ? "" : swiftFile.getSendRef()));
                      swiftXml.append("</T20_SendRef>");
                      swiftXml.append("<T23_BankOperCode Opt=\"B\">");
                      swiftXml.append(
                          (null == swiftFile.getBankCode() ? "" : swiftFile.getBankCode()));
                      swiftXml.append("</T23_BankOperCode>");
                      swiftXml.append("<T26_TxTypeCode Opt=\"T\">");
                      swiftXml.append(
                          (null == swiftFile.getTransferType() ? "" : swiftFile.getTransferType()));
                      swiftXml.append("</T26_TxTypeCode>");
                      swiftXml.append("<T32_DateAmt Opt=\"A\">");
                      swiftXml.append("<ValueDate>");
                      swiftXml.append(
                          (null == swiftFile.getValueDate() ? "" : swiftFile.getValueDate()));
                      swiftXml.append("</ValueDate>");
                      swiftXml.append("<Ccy>");
                      swiftXml.append(
                          (null == swiftFile.getCurrency() ? "" : swiftFile.getCurrency()));
                      swiftXml.append("</Ccy>");
                      swiftXml.append("<Amt>");
                      swiftXml.append(
                          (null == swiftFile.getSetAmount()
                              ? ""
                              : String.format("%.2f", swiftFile.getSetAmount())));
                      swiftXml.append("</Amt>");
                      swiftXml.append("</T32_DateAmt>");
                      swiftXml.append("<T50_OrdCust Opt=\"K\">");
                      swiftXml.append("<Acct>");
                      swiftXml.append(
                          (null == swiftFile.getOrdAcct() ? "" : swiftFile.getOrdAcct()));
                      swiftXml.append("</Acct>");
                      swiftXml.append("<Info1>");
                      swiftXml.append(
                          (null == swiftFile.getOrdName1()
                              ? ""
                              : "99999".equalsIgnoreCase(swiftFile.getOrdName1())
                                  ? "9999"
                                  : swiftFile.getOrdName1()));
                      swiftXml.append("</Info1>");
                      swiftXml.append("<Info2>");
                      swiftXml.append(
                          (null == swiftFile.getOrdName2() ? "" : swiftFile.getOrdName2()));
                      swiftXml.append("</Info2>");
                      swiftXml.append("<Info3>");
                      swiftXml.append(
                          (null == swiftFile.getOrdName3() ? "" : swiftFile.getOrdName3()));
                      swiftXml.append("</Info3>");
                      swiftXml.append("<Info4>");
                      swiftXml.append(
                          (null == swiftFile.getOrdName4() ? "" : swiftFile.getOrdName4()));
                      swiftXml.append("</Info4>");
                      swiftXml.append("</T50_OrdCust>");
                      swiftXml.append("<T53_SendCorres Opt=\"B\">");
                      swiftXml.append("<DrCrInd>");
                      swiftXml.append(
                          (null == swiftFile.getSendCode() ? "" : swiftFile.getSendCode()));
                      swiftXml.append("</DrCrInd>");
                      swiftXml.append("<PrtyID>");
                      swiftXml.append(
                          (null == swiftFile.getSendAcct() ? "" : swiftFile.getSendAcct()));
                      swiftXml.append("</PrtyID>");
                      swiftXml.append("<Loc></Loc>");
                      swiftXml.append("</T53_SendCorres>");
                      swiftXml.append("<T57_AcctWithInst Opt=\"A\">");
                      swiftXml.append("<DrCrInd>");
                      swiftXml.append(
                          (null == swiftFile.getRecCode() ? "" : swiftFile.getRecCode()));
                      swiftXml.append("</DrCrInd>");
                      swiftXml.append("<PrtyID>");
                      swiftXml.append(
                          (null == swiftFile.getRecAcct() ? "" : swiftFile.getRecAcct()));
                      swiftXml.append("</PrtyID>");
                      swiftXml.append("<BIC>");
                      swiftXml.append(
                          (null == swiftFile.getRecInsti() ? "" : swiftFile.getRecInsti()));
                      swiftXml.append("</BIC>");
                      swiftXml.append("</T57_AcctWithInst>");
                      swiftXml.append("<T59_BenefCust Opt=\"\">");
                      swiftXml.append("<Acct>");
                      swiftXml.append(
                          (null == swiftFile.getBenAcct() ? "" : swiftFile.getBenAcct()));
                      swiftXml.append("</Acct>");
                      swiftXml.append("<Info1>");
                      swiftXml.append(
                          (null == swiftFile.getBenName1() ? "" : swiftFile.getBenName1()));
                      swiftXml.append("</Info1>");
                      swiftXml.append("<Info2>");
                      swiftXml.append(
                          (null == swiftFile.getBenName2() ? "" : swiftFile.getBenName2()));
                      swiftXml.append("</Info2>");
                      swiftXml.append("<Info3>");
                      swiftXml.append(
                          (null == swiftFile.getBenName3() ? "" : swiftFile.getBenName3()));
                      swiftXml.append("</Info3>");
                      swiftXml.append("<Info4>");
                      swiftXml.append(
                          (null == swiftFile.getBenName4() ? "" : swiftFile.getBenName4()));
                      swiftXml.append("</Info4>");
                      swiftXml.append("</T59_BenefCust>");
                      swiftXml.append("<T71_DetChrg Opt=\"A\">");
                      swiftXml.append(
                          (null == swiftFile.getDetailCharg() ? "" : swiftFile.getDetailCharg()));
                      swiftXml.append("</T71_DetChrg>");
                      if (null != swiftFile.getSendToRec1()
                          && !swiftFile.getSendToRec1().equalsIgnoreCase("")) {
                        swiftXml.append("<T72_SendToRecvInfo>");
                        swiftXml.append("<Info1 Code=\"DETA\">");
                        swiftXml.append(
                            (null == swiftFile.getSendToRec1() ? "" : swiftFile.getSendToRec1()));
                        swiftXml.append("</Info1>");
                        swiftXml.append("<Info2 Code=\"DETA\">");
                        swiftXml.append(
                            (null == swiftFile.getSendToRec2() ? "" : swiftFile.getSendToRec2()));
                        swiftXml.append("</Info2>");
                        swiftXml.append("<Info3 Code=\"DETA\">");
                        swiftXml.append(
                            (null == swiftFile.getSendToRec3() ? "" : swiftFile.getSendToRec3()));
                        swiftXml.append("</Info3>");
                        if (null != swiftFile.getSendToRec4()
                            && !swiftFile.getSendToRec4().equalsIgnoreCase("")) {
                          swiftXml.append("<Info4 Code=\"DETA\">");
                          swiftXml.append(
                              (null == swiftFile.getSendToRec4() ? "" : swiftFile.getSendToRec4()));
                          swiftXml.append("</Info4>");
                        }
                        swiftXml.append("</T72_SendToRecvInfo>");
                      }

                      if (null != swiftFile.getRegalRep1()
                          && !swiftFile.getRegalRep1().equalsIgnoreCase("")) {
                        swiftXml.append("<T77_RegltryRpt Opt=\"B\">");
                        swiftXml.append("<Info1 Code=\"ORDERRES\" Cntry=\"TH\">");
                        swiftXml.append(
                            (null == swiftFile.getRegalRep1() ? "" : swiftFile.getRegalRep1()));
                        swiftXml.append("</Info1>");
                        swiftXml.append("</T77_RegltryRpt>");
                      }

                      swiftXml.append("</TxtBlk></SWML>");
                    } else {
                      swiftXml.append(
                          BahtnetUtil.generateBahtnetLevelMaster(
                              swiftFile, refRunning, refLineList.get(index.getAndIncrement())));
                    }
                  }
                  swiftXml.append("</Upload>");

                  InputStream inputStream =
                      new ByteArrayInputStream(
                          swiftXml.toString().getBytes(StandardCharsets.UTF_8));
                  try {
                    fileTransferService.uploadFile(inputStream, directory, fileName);
                  } catch (SftpException e) {
                    throw new RuntimeException(e);
                  }
                });
      }

      if ((null != generateFileAlias.getSwiftFiles()
              && !generateFileAlias.getSwiftFiles().isEmpty())
          || (null != generateFileAlias.getSwiftFilesSumFile()
              && !generateFileAlias.getSwiftFilesSumFile().isEmpty())) {

        List<SwiftFile> swiftFiles = generateFileAlias.getSwiftFiles();
        Map<String, List<SwiftFile>> swiftMapLevel =
            swiftFiles.stream()
                .collect(
                    Collectors.groupingBy(
                        this::getGroupingByKey,
                        Collectors.mapping((SwiftFile s) -> s, Collectors.toList())));
        String swiftFileName = "";
        if (null != swiftFiles && !swiftFiles.isEmpty()) {
          for (SwiftFile swiftFile : swiftFiles) {
            generateFile.setReference(String.valueOf(refRunning));
            OutputReport outputReport = new OutputReport();

            SwiftFileLog swiftFileLog = swiftFile.getSwiftFileLog();
            swiftFileName = swiftFile.getFileName();
            outputReport.setFileName(swiftFileName);
            if (!generateFileAlias.isTestRun()) {
              if (!swiftFile.isSumFile()) {
                if (null == proposalLogHeader) {
                  proposalLogHeader = new ProposalLogHeader();
                  proposalLogHeader.setId(proposalHeaderId);
                  proposalLogHeader.setRefRunning(refRunning);
                  proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
                  proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
                  proposalLogHeader.setCreatedBy(jwt.getSub());
                  proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
                  proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
                  proposalLogHeader.setCancel(false);
                  proposalLogHeader.setUse(isAll);
                  proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
                }
                if ((null != generateFileAlias.getSmartFileHeaderSumFile())
                    || (null != generateFileAlias.getGiroFileHeadersSumFile()
                        && !generateFileAlias.getGiroFileHeadersSumFile().isEmpty())
                    || (null != generateFileAlias.getInhouseFileHeadersSumFile()
                        && !generateFileAlias.getInhouseFileHeadersSumFile().isEmpty())) {
                  ProposalLogSum proposalLogSum = new ProposalLogSum();
                  proposalLogSum.setId(proposalSumId++);
                  proposalLogSum.setRefRunning(refRunning);
                  proposalLogSum.setRefLine(++refLineSum);
                  proposalLogSum.setPaymentName(paymentAlias.getPaymentName());
                  proposalLogSum.setPaymentDate(paymentAlias.getPaymentDate());
                  proposalLogSum.setAccountNoFrom(swiftFile.getSendAcct());
                  proposalLogSum.setAccountNoTo(swiftFile.getBenAcct());
                  proposalLogSum.setFileType("SWIFT");

                  proposalLogSum.setFileName(swiftFileName);
                  proposalLogSum.setTransferDate(generateFileAlias.getSwiftDate());
                  proposalLogSum.setAmount(swiftFile.getSetAmount());
                  proposalLogSum.setBankFee(Util.getBigDecimal(0));
                  proposalLogSum.setVendor(swiftFileLog.getVendor());
                  proposalLogSum.setBankKey(swiftFileLog.getBankKey());
                  proposalLogSum.setVendorBankAccount(swiftFileLog.getBankAccountNo());
                  proposalLogSum.setTransferLevel(swiftFileLog.getTransferLevel());
                  proposalLogSum.setPayAccount(swiftFile.getPayAccount());
                  proposalLogSum.setPaymentType(swiftFile.getPayAccount());
                  proposalLogSum.setPayingCompCode("99999");
                  proposalLogSum.setCreditMemoAmount(swiftFileLog.getCreditMemo());
                  proposalLogSum.setCreatedBy(jwt.getSub());
                  proposalLogSum.setUpdatedBy(jwt.getSub());
                  proposalLogSum.setCreated(created);
                  proposalLogSum.setUpdated(created);
                  proposalLogSum.setSendStatus("S");
                  proposalLogSum.setRerun(false);

                  proposalLogSum.setProposalLogHeaderId(proposalLogHeader.getId());

                  proposalLogSums.add(proposalLogSum);
                }

                //                            Long id = proposalId++;
                //                            log.info("proposalId : {}", id);
                int refLine = ++i;
                refLineList.add(refLine);
                ProposalLog proposalLog = new ProposalLog();
                proposalLog.setId(proposalId++);
                proposalLog.setRefRunning(refRunning);
                proposalLog.setRefLine(refLine);
                proposalLog.setPaymentName(paymentAlias.getPaymentName());
                proposalLog.setPaymentDate(paymentAlias.getPaymentDate());
                proposalLog.setAccountNoFrom(swiftFile.getSendAcct());
                proposalLog.setAccountNoTo(swiftFile.getBenAcct());
                proposalLog.setFileType("SWIFT");

                proposalLog.setFileName(swiftFileName);
                proposalLog.setTransferDate(generateFileAlias.getSwiftDate());
                proposalLog.setAmount(swiftFile.getSetAmount());
                proposalLog.setBankFee(Util.getBigDecimal(0));
                proposalLog.setVendor(swiftFileLog.getVendor());
                proposalLog.setBankKey(swiftFileLog.getBankKey());
                proposalLog.setVendorBankAccount(swiftFileLog.getBankAccountNo());
                proposalLog.setTransferLevel(swiftFileLog.getTransferLevel());
                proposalLog.setPayAccount(swiftFile.getPayAccount());
                proposalLog.setPaymentType(swiftFile.getPayAccount());
                proposalLog.setPayingCompCode("99999");
                proposalLog.setPaymentDocument(swiftFileLog.getPaymentDocNo());
                proposalLog.setFiscalYear(swiftFileLog.getPaymentFiscalYear());
                proposalLog.setFiArea(swiftFileLog.getFiArea());
                proposalLog.setCreditMemoAmount(swiftFileLog.getCreditMemo());
                proposalLog.setCreatedBy(jwt.getSub());
                proposalLog.setUpdatedBy(jwt.getSub());
                proposalLog.setCreated(created);
                proposalLog.setUpdated(created);
                proposalLog.setSendStatus("S");

                proposalLog.setOriginalCompCode(swiftFileLog.getOriginalCompCode());
                proposalLog.setOriginalDocNo(swiftFileLog.getOriginalAccDocNo());
                proposalLog.setOriginalDocType(swiftFileLog.getOriginalDocType());
                proposalLog.setOriginalFiscalYear(swiftFileLog.getOriginalFiscalYear());

                proposalLog.setOriginalWtxAmount(swiftFileLog.getOriginalWtxAmount());
                proposalLog.setOriginalWtxBase(swiftFileLog.getOriginalWtxBase());
                proposalLog.setOriginalWtxAmountP(swiftFileLog.getOriginalWtxAmountP());
                proposalLog.setOriginalWtxBaseP(swiftFileLog.getOriginalWtxBaseP());

                proposalLog.setInvCompCode(swiftFileLog.getInvoiceCompCode());
                proposalLog.setInvDocNo(swiftFileLog.getInvoiceAccDocNo());
                proposalLog.setInvDocType(swiftFileLog.getInvoiceDocType());
                proposalLog.setInvFiscalYear(swiftFileLog.getInvoiceFiscalYear());

                proposalLog.setInvWtxAmount(swiftFileLog.getInvoiceWtxAmount());
                proposalLog.setInvWtxBase(swiftFileLog.getInvoiceWtxBase());
                proposalLog.setInvWtxAmountP(swiftFileLog.getInvoiceWtxAmountP());
                proposalLog.setInvWtxBaseP(swiftFileLog.getInvoiceWtxBaseP());

                proposalLog.setPaymentCompCode(swiftFileLog.getPaymentCompCode());
                proposalLog.setPaymentFiscalYear(swiftFileLog.getPaymentFiscalYear());

                proposalLog.setRefRunningSum(0L);
                proposalLog.setRefLineSum(0);

                proposalLog.setRerun(false);

                proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());

                proposalLogs.add(proposalLog);
              }
            }

            if (!swiftFile.isSumFile()) {
              outputReport.setOutputText(swiftFile.toString());
              outputReportSwifts.add(outputReport);
            }
          }
          //                generateFile.setSwifts(outputReports);
        }

        if (!generateFileAlias.isTestRun()) {
          List<SwiftFile> swiftFileSumFiles = generateFileAlias.getSwiftFilesSumFile();
          if (null != swiftFileSumFiles && !swiftFileSumFiles.isEmpty()) {
            Map<String, List<SwiftFile>> groupByVendor =
                swiftFileSumFiles.stream()
                    .collect(
                        Collectors.groupingBy(
                            this::groupByMultipleKey,
                            Collectors.mapping((SwiftFile s) -> s, toList())));

            for (var entry : groupByVendor.entrySet()) {
              BigDecimal totalTransferAmount =
                  entry.getValue().stream()
                      .map(SwiftFile::getSetAmount) // map
                      .reduce(BigDecimal.ZERO, BigDecimal::add);
              SwiftFile swiftFileSum = entry.getValue().get(0);
              String detailSum =
                  StringUtils.rightPad(
                      null == entry.getValue().get(0).getFileName()
                          ? ""
                          : entry.getValue().get(0).getFileName(),
                      16,
                      " ")
                      + StringUtils.rightPad(
                      null == entry.getValue().get(0).getBankCode()
                          ? ""
                          : entry.getValue().get(0).getBankCode(),
                      4,
                      " ")
                      + StringUtils.rightPad(
                      null == entry.getValue().get(0).getTransferType()
                          ? ""
                          : entry.getValue().get(0).getTransferType(),
                      3,
                      " ")
                      + StringUtils.rightPad(
                      null == entry.getValue().get(0).getValueDate()
                          ? ""
                          : entry.getValue().get(0).getValueDate(),
                      10,
                      " ")
                      + StringUtils.rightPad(
                      null == entry.getValue().get(0).getCurrency()
                          ? ""
                          : entry.getValue().get(0).getCurrency(),
                      3,
                      " ")
                      + StringUtils.rightPad(
                      null == totalTransferAmount
                          ? ""
                          : String.format("%.2f", totalTransferAmount),
                      15,
                      " ")
                      + StringUtils.rightPad(
                      null == entry.getValue().get(0).getOrdAcct()
                          ? ""
                          : entry.getValue().get(0).getOrdAcct(),
                      34,
                      " ")
                      + StringUtils.rightPad(
                      null == entry.getValue().get(0).getOrdName1()
                          ? ""
                          : entry.getValue().get(0).getOrdName1(),
                      35,
                      " ");

              swiftFileName = swiftFileSum.getFileName();
              generateFile.setReference(String.valueOf(refRunning));
              OutputReport outputReport = new OutputReport();
              //                    String fileName = entry.getValue().get(0).getFileName();
              outputReport.setFileName(swiftFileName);
              outputReport.setOutputText(detailSum);
              outputReportSwifts.add(outputReport);
              if (!generateFileAlias.isTestRun()) {
                if (null == proposalLogHeader) {
                  proposalLogHeader = new ProposalLogHeader();
                  proposalLogHeader.setId(proposalHeaderId);
                  proposalLogHeader.setRefRunning(refRunning);
                  proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
                  proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
                  proposalLogHeader.setCreatedBy(jwt.getSub());
                  proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
                  proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
                  proposalLogHeader.setCancel(false);
                  proposalLogHeader.setUse(isAll);
                  proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
                }
                ProposalLogSum proposalLogSum = new ProposalLogSum();
                proposalLogSum.setId(proposalSumId++);
                proposalLogSum.setRefRunning(refRunning);
                proposalLogSum.setRefLine(++refLineSum);
                proposalLogSum.setPaymentName(paymentAlias.getPaymentName());
                proposalLogSum.setPaymentDate(paymentAlias.getPaymentDate());
                proposalLogSum.setAccountNoFrom(entry.getValue().get(0).getSendAcct());
                proposalLogSum.setAccountNoTo(entry.getValue().get(0).getBenAcct());
                proposalLogSum.setFileType("SWIFT");

                proposalLogSum.setFileName(swiftFileName);
                proposalLogSum.setTransferDate(generateFileAlias.getSwiftDate());
                proposalLogSum.setAmount(totalTransferAmount);
                proposalLogSum.setBankFee(Util.getBigDecimal(0));
                proposalLogSum.setVendor(entry.getValue().get(0).getSwiftFileLog().getVendor());
                proposalLogSum.setBankKey(entry.getValue().get(0).getSwiftFileLog().getBankKey());
                proposalLogSum.setVendorBankAccount(
                    entry.getValue().get(0).getSwiftFileLog().getBankAccountNo());
                proposalLogSum.setTransferLevel(
                    entry.getValue().get(0).getSwiftFileLog().getTransferLevel());
                proposalLogSum.setPayAccount(entry.getValue().get(0).getPayAccount());
                proposalLogSum.setPaymentType(entry.getValue().get(0).getPayAccount());
                proposalLogSum.setPayingCompCode("99999");
                proposalLogSum.setCreditMemoAmount(
                    entry.getValue().get(0).getSwiftFileLog().getCreditMemo());
                proposalLogSum.setCreatedBy(jwt.getSub());
                proposalLogSum.setUpdatedBy(jwt.getSub());
                proposalLogSum.setCreated(created);
                proposalLogSum.setUpdated(created);
                proposalLogSum.setSendStatus("S");

                proposalLogSum.setRerun(false);

                proposalLogSum.setProposalLogHeaderId(proposalLogHeader.getId());

                proposalLogSums.add(proposalLogSum);

                for (SwiftFile swiftFile : entry.getValue()) {
                  SwiftFileLog swiftFileLog = swiftFile.getSwiftFileLog();
                  if (!generateFileAlias.isTestRun()) {
                    int refLine = ++i;
                    refLineList.add(refLine);
                    ProposalLog proposalLog = new ProposalLog();
                    proposalLog.setId(proposalId++);
                    proposalLog.setRefRunning(refRunning);
                    proposalLog.setRefLine(refLine);
                    proposalLog.setPaymentName(paymentAlias.getPaymentName());
                    proposalLog.setPaymentDate(paymentAlias.getPaymentDate());
                    proposalLog.setAccountNoFrom(swiftFile.getSendAcct());
                    proposalLog.setAccountNoTo(swiftFile.getBenAcct());
                    proposalLog.setFileType("SWIFT");

                    proposalLog.setFileName(swiftFileName);
                    proposalLog.setTransferDate(generateFileAlias.getSwiftDate());
                    proposalLog.setAmount(swiftFile.getSetAmount());
                    proposalLog.setBankFee(Util.getBigDecimal(0));
                    proposalLog.setVendor(swiftFileLog.getVendor());
                    proposalLog.setBankKey(swiftFileLog.getBankKey());
                    proposalLog.setVendorBankAccount(swiftFileLog.getBankAccountNo());
                    proposalLog.setTransferLevel(swiftFileLog.getTransferLevel());
                    proposalLog.setPayAccount(swiftFile.getPayAccount());
                    proposalLog.setPaymentType(swiftFile.getPayAccount());
                    proposalLog.setPayingCompCode("99999");
                    proposalLog.setPaymentDocument(swiftFileLog.getPaymentDocNo());
                    proposalLog.setFiscalYear(swiftFileLog.getPaymentFiscalYear());
                    proposalLog.setFiArea(swiftFileLog.getFiArea());
                    proposalLog.setCreditMemoAmount(swiftFileLog.getCreditMemo());
                    proposalLog.setCreatedBy(jwt.getSub());
                    proposalLog.setUpdatedBy(jwt.getSub());
                    proposalLog.setCreated(created);
                    proposalLog.setUpdated(created);
                    proposalLog.setSendStatus("S");

                    proposalLog.setOriginalCompCode(swiftFileLog.getOriginalCompCode());
                    proposalLog.setOriginalDocNo(swiftFileLog.getOriginalAccDocNo());
                    proposalLog.setOriginalDocType(swiftFileLog.getOriginalDocType());
                    proposalLog.setOriginalFiscalYear(swiftFileLog.getOriginalFiscalYear());

                    proposalLog.setOriginalWtxAmount(swiftFileLog.getOriginalWtxAmount());
                    proposalLog.setOriginalWtxBase(swiftFileLog.getOriginalWtxBase());
                    proposalLog.setOriginalWtxAmountP(swiftFileLog.getOriginalWtxAmountP());
                    proposalLog.setOriginalWtxBaseP(swiftFileLog.getOriginalWtxBaseP());

                    proposalLog.setInvCompCode(swiftFileLog.getInvoiceCompCode());
                    proposalLog.setInvDocNo(swiftFileLog.getInvoiceAccDocNo());
                    proposalLog.setInvDocType(swiftFileLog.getInvoiceDocType());
                    proposalLog.setInvFiscalYear(swiftFileLog.getInvoiceFiscalYear());

                    proposalLog.setInvWtxAmount(swiftFileLog.getInvoiceWtxAmount());
                    proposalLog.setInvWtxBase(swiftFileLog.getInvoiceWtxBase());
                    proposalLog.setInvWtxAmountP(swiftFileLog.getInvoiceWtxAmountP());
                    proposalLog.setInvWtxBaseP(swiftFileLog.getInvoiceWtxBaseP());

                    proposalLog.setPaymentCompCode(swiftFileLog.getPaymentCompCode());
                    proposalLog.setPaymentFiscalYear(swiftFileLog.getPaymentFiscalYear());

                    proposalLog.setRefRunningSum(refRunning);
                    proposalLog.setRefLineSum(refLineSum);

                    proposalLog.setRerun(false);

                    proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());

                    proposalLogs.add(proposalLog);
                  }
                }
              }
            }
          }
          swiftMapLevel.entrySet().stream()
              .sorted(Map.Entry.comparingByKey())
              .forEach(
                  k -> {
                    StringBuilder swiftXml =
                        new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Upload>");
                    List<SwiftFile> swiftFileList = k.getValue();
                    String fileName = "";
                    for (SwiftFile swiftFile : swiftFileList) {
                      fileName = swiftFile.getFileName();
                      if (!isPac) {
                        if (!swiftFile.isSumFile()) {
                          swiftXml.append(
                              "<SWML version=\"1.0\"><BasicHdrBlk><LTID></LTID></BasicHdrBlk>");
                          swiftXml.append(
                              "<AppHdrBlk><MsgType>103</MsgType><MsgIPRef><BIC></BIC></MsgIPRef><DataRefNum></DataRefNum><UserID></UserID></AppHdrBlk>");
                          swiftXml.append("<UsrHdrBlk><BankPrior>8XXX</BankPrior></UsrHdrBlk>");
                          swiftXml.append("<TxtBlk>");
                          swiftXml.append("<T20_SendRef>");
                          swiftXml.append(
                              (null == swiftFile.getSendRef() ? "" : swiftFile.getSendRef()));
                          swiftXml.append("</T20_SendRef>");
                          swiftXml.append("<T23_BankOperCode Opt=\"B\">");
                          swiftXml.append(
                              (null == swiftFile.getBankCode() ? "" : swiftFile.getBankCode()));
                          swiftXml.append("</T23_BankOperCode>");
                          swiftXml.append("<T26_TxTypeCode Opt=\"T\">");
                          swiftXml.append(
                              (null == swiftFile.getTransferType()
                                  ? ""
                                  : swiftFile.getTransferType()));
                          swiftXml.append("</T26_TxTypeCode>");
                          swiftXml.append("<T32_DateAmt Opt=\"A\">");
                          swiftXml.append("<ValueDate>");
                          swiftXml.append(
                              (null == swiftFile.getValueDate() ? "" : swiftFile.getValueDate()));
                          swiftXml.append("</ValueDate>");
                          swiftXml.append("<Ccy>");
                          swiftXml.append(
                              (null == swiftFile.getCurrency() ? "" : swiftFile.getCurrency()));
                          swiftXml.append("</Ccy>");
                          swiftXml.append("<Amt>");
                          swiftXml.append(
                              (null == swiftFile.getSetAmount()
                                  ? ""
                                  : String.format("%.2f", swiftFile.getSetAmount())));
                          swiftXml.append("</Amt>");
                          swiftXml.append("</T32_DateAmt>");
                          swiftXml.append("<T50_OrdCust Opt=\"K\">");
                          swiftXml.append("<Acct>");
                          swiftXml.append(
                              (null == swiftFile.getOrdAcct() ? "" : swiftFile.getOrdAcct()));
                          swiftXml.append("</Acct>");
                          swiftXml.append("<Info1>");
                          swiftXml.append(
                              (null == swiftFile.getOrdName1()
                                  ? ""
                                  : "99999".equalsIgnoreCase(swiftFile.getOrdName1())
                                      ? "9999"
                                      : swiftFile.getOrdName1()));
                          swiftXml.append("</Info1>");
                          swiftXml.append("<Info2>");
                          swiftXml.append(
                              (null == swiftFile.getOrdName2() ? "" : swiftFile.getOrdName2()));
                          swiftXml.append("</Info2>");
                          swiftXml.append("<Info3>");
                          swiftXml.append(
                              (null == swiftFile.getOrdName3() ? "" : swiftFile.getOrdName3()));
                          swiftXml.append("</Info3>");
                          swiftXml.append("<Info4>");
                          swiftXml.append(
                              (null == swiftFile.getOrdName4() ? "" : swiftFile.getOrdName4()));
                          swiftXml.append("</Info4>");
                          swiftXml.append("</T50_OrdCust>");
                          swiftXml.append("<T53_SendCorres Opt=\"B\">");
                          swiftXml.append("<DrCrInd>");
                          swiftXml.append(
                              (null == swiftFile.getSendCode() ? "" : swiftFile.getSendCode()));
                          swiftXml.append("</DrCrInd>");
                          swiftXml.append("<PrtyID>");
                          swiftXml.append(
                              (null == swiftFile.getSendAcct() ? "" : swiftFile.getSendAcct()));
                          swiftXml.append("</PrtyID>");
                          swiftXml.append("<Loc></Loc>");
                          swiftXml.append("</T53_SendCorres>");
                          swiftXml.append("<T57_AcctWithInst Opt=\"A\">");
                          swiftXml.append("<DrCrInd>");
                          swiftXml.append(
                              (null == swiftFile.getRecCode() ? "" : swiftFile.getRecCode()));
                          swiftXml.append("</DrCrInd>");
                          swiftXml.append("<PrtyID>");
                          swiftXml.append(
                              (null == swiftFile.getRecAcct() ? "" : swiftFile.getRecAcct()));
                          swiftXml.append("</PrtyID>");
                          swiftXml.append("<BIC>");
                          swiftXml.append(
                              (null == swiftFile.getRecInsti() ? "" : swiftFile.getRecInsti()));
                          swiftXml.append("</BIC>");
                          swiftXml.append("</T57_AcctWithInst>");
                          swiftXml.append("<T59_BenefCust Opt=\"\">");
                          swiftXml.append("<Acct>");
                          swiftXml.append(
                              (null == swiftFile.getBenAcct() ? "" : swiftFile.getBenAcct()));
                          swiftXml.append("</Acct>");
                          swiftXml.append("<Info1>");
                          swiftXml.append(
                              (null == swiftFile.getBenName1() ? "" : swiftFile.getBenName1()));
                          swiftXml.append("</Info1>");
                          swiftXml.append("<Info2>");
                          swiftXml.append(
                              (null == swiftFile.getBenName2() ? "" : swiftFile.getBenName2()));
                          swiftXml.append("</Info2>");
                          swiftXml.append("<Info3>");
                          swiftXml.append(
                              (null == swiftFile.getBenName3() ? "" : swiftFile.getBenName3()));
                          swiftXml.append("</Info3>");
                          swiftXml.append("<Info4>");
                          swiftXml.append(
                              (null == swiftFile.getBenName4() ? "" : swiftFile.getBenName4()));
                          swiftXml.append("</Info4>");
                          swiftXml.append("</T59_BenefCust>");
                          swiftXml.append("<T71_DetChrg Opt=\"A\">");
                          swiftXml.append(
                              (null == swiftFile.getDetailCharg()
                                  ? ""
                                  : swiftFile.getDetailCharg()));
                          swiftXml.append("</T71_DetChrg>");
                          if (null != swiftFile.getSendToRec1()
                              && !swiftFile.getSendToRec1().equalsIgnoreCase("")) {
                            swiftXml.append("<T72_SendToRecvInfo>");
                            swiftXml.append("<Info1 Code=\"DETA\">");
                            swiftXml.append(
                                (null == swiftFile.getSendToRec1()
                                    ? ""
                                    : swiftFile.getSendToRec1()));
                            swiftXml.append("</Info1>");
                            swiftXml.append("<Info2 Code=\"DETA\">");
                            swiftXml.append(
                                (null == swiftFile.getSendToRec2()
                                    ? ""
                                    : swiftFile.getSendToRec2()));
                            swiftXml.append("</Info2>");
                            swiftXml.append("<Info3 Code=\"DETA\">");
                            swiftXml.append(
                                (null == swiftFile.getSendToRec3()
                                    ? ""
                                    : swiftFile.getSendToRec3()));
                            swiftXml.append("</Info3>");
                            if (null != swiftFile.getSendToRec4()
                                && !swiftFile.getSendToRec4().equalsIgnoreCase("")) {
                              swiftXml.append("<Info4 Code=\"DETA\">");
                              swiftXml.append(
                                  (null == swiftFile.getSendToRec4()
                                      ? ""
                                      : swiftFile.getSendToRec4()));
                              swiftXml.append("</Info4>");
                            }
                            swiftXml.append("</T72_SendToRecvInfo>");
                          }

                          if (null != swiftFile.getRegalRep1()
                              && !swiftFile.getRegalRep1().equalsIgnoreCase("")) {
                            swiftXml.append("<T77_RegltryRpt Opt=\"B\">");
                            swiftXml.append("<Info1 Code=\"ORDERRES\" Cntry=\"TH\">");
                            swiftXml.append(
                                (null == swiftFile.getRegalRep1() ? "" : swiftFile.getRegalRep1()));
                            swiftXml.append("</Info1>");
                            swiftXml.append("</T77_RegltryRpt>");
                          }
                        } else {
                          swiftXml.append(
                              "<SWML version=\"1.0\"><BasicHdrBlk><LTID></LTID></BasicHdrBlk>");
                          swiftXml.append(
                              "<AppHdrBlk><MsgType>103</MsgType><MsgIPRef><BIC></BIC></MsgIPRef><DataRefNum></DataRefNum><UserID></UserID></AppHdrBlk>");
                          swiftXml.append("<UsrHdrBlk><BankPrior>8XXX</BankPrior></UsrHdrBlk>");
                          swiftXml.append("<TxtBlk>");
                          swiftXml.append("<T20_SendRef>");
                          swiftXml.append(
                              (null == swiftFile.getSendRef() ? "" : swiftFile.getSendRef()));
                          swiftXml.append("</T20_SendRef>");
                          swiftXml.append("<T23_BankOperCode Opt=\"B\">");
                          swiftXml.append(
                              (null == swiftFile.getBankCode() ? "" : swiftFile.getBankCode()));
                          swiftXml.append("</T23_BankOperCode>");
                          swiftXml.append("<T26_TxTypeCode Opt=\"T\">");
                          swiftXml.append(
                              (null == swiftFile.getTransferType()
                                  ? ""
                                  : swiftFile.getTransferType()));
                          swiftXml.append("</T26_TxTypeCode>");
                          swiftXml.append("<T32_DateAmt Opt=\"A\">");
                          swiftXml.append("<ValueDate>");
                          swiftXml.append(
                              (null == swiftFile.getValueDate() ? "" : swiftFile.getValueDate()));
                          swiftXml.append("</ValueDate>");
                          swiftXml.append("<Ccy>");
                          swiftXml.append(
                              (null == swiftFile.getCurrency() ? "" : swiftFile.getCurrency()));
                          swiftXml.append("</Ccy>");
                          swiftXml.append("<Amt>");
                          swiftXml.append(
                              (null == swiftFile.getTotalTransferAmount()
                                  ? ""
                                  : String.format("%.2f", swiftFile.getTotalTransferAmount())));
                          swiftXml.append("</Amt>");
                          swiftXml.append("</T32_DateAmt>");
                          swiftXml.append("<T50_OrdCust Opt=\"K\">");
                          swiftXml.append("<Acct>");
                          swiftXml.append(
                              (null == swiftFile.getOrdAcct() ? "" : swiftFile.getOrdAcct()));
                          swiftXml.append("</Acct>");
                          swiftXml.append("<Info1>");
                          swiftXml.append(
                              (null == swiftFile.getOrdName1()
                                  ? ""
                                  : "99999".equalsIgnoreCase(swiftFile.getOrdName1())
                                      ? "9999"
                                      : swiftFile.getOrdName1()));
                          swiftXml.append("</Info1>");
                          swiftXml.append("<Info2>");
                          swiftXml.append(
                              (null == swiftFile.getOrdName2() ? "" : swiftFile.getOrdName2()));
                          swiftXml.append("</Info2>");
                          swiftXml.append("<Info3>");
                          swiftXml.append(
                              (null == swiftFile.getOrdName3() ? "" : swiftFile.getOrdName3()));
                          swiftXml.append("</Info3>");
                          swiftXml.append("<Info4>");
                          swiftXml.append(
                              (null == swiftFile.getOrdName4() ? "" : swiftFile.getOrdName4()));
                          swiftXml.append("</Info4>");
                          swiftXml.append("</T50_OrdCust>");
                          swiftXml.append("<T53_SendCorres Opt=\"B\">");
                          swiftXml.append("<DrCrInd>");
                          swiftXml.append(
                              (null == swiftFile.getSendCode() ? "" : swiftFile.getSendCode()));
                          swiftXml.append("</DrCrInd>");
                          swiftXml.append("<PrtyID>");
                          swiftXml.append(
                              (null == swiftFile.getSendAcct() ? "" : swiftFile.getSendAcct()));
                          swiftXml.append("</PrtyID>");
                          swiftXml.append("<Loc></Loc>");
                          swiftXml.append("</T53_SendCorres>");
                          swiftXml.append("<T57_AcctWithInst Opt=\"A\">");
                          swiftXml.append("<DrCrInd>");
                          swiftXml.append(
                              (null == swiftFile.getRecCode() ? "" : swiftFile.getRecCode()));
                          swiftXml.append("</DrCrInd>");
                          swiftXml.append("<PrtyID>");
                          swiftXml.append(
                              (null == swiftFile.getRecAcct() ? "" : swiftFile.getRecAcct()));
                          swiftXml.append("</PrtyID>");
                          swiftXml.append("<BIC>");
                          swiftXml.append(
                              (null == swiftFile.getRecInsti() ? "" : swiftFile.getRecInsti()));
                          swiftXml.append("</BIC>");
                          swiftXml.append("</T57_AcctWithInst>");
                          swiftXml.append("<T59_BenefCust Opt=\"\">");
                          swiftXml.append("<Acct>");
                          swiftXml.append(
                              (null == swiftFile.getBenAcct() ? "" : swiftFile.getBenAcct()));
                          swiftXml.append("</Acct>");
                          swiftXml.append("<Info1>");
                          swiftXml.append(
                              (null == swiftFile.getBenName1() ? "" : swiftFile.getBenName1()));
                          swiftXml.append("</Info1>");
                          swiftXml.append("<Info2>");
                          swiftXml.append(
                              (null == swiftFile.getBenName2() ? "" : swiftFile.getBenName2()));
                          swiftXml.append("</Info2>");
                          swiftXml.append("<Info3>");
                          swiftXml.append(
                              (null == swiftFile.getBenName3() ? "" : swiftFile.getBenName3()));
                          swiftXml.append("</Info3>");
                          swiftXml.append("<Info4>");
                          swiftXml.append(
                              (null == swiftFile.getBenName4() ? "" : swiftFile.getBenName4()));
                          swiftXml.append("</Info4>");
                          swiftXml.append("</T59_BenefCust>");
                          swiftXml.append("<T71_DetChrg Opt=\"A\">");
                          swiftXml.append(
                              (null == swiftFile.getDetailCharg()
                                  ? ""
                                  : swiftFile.getDetailCharg()));
                          swiftXml.append("</T71_DetChrg>");
                          if (null != swiftFile.getSendToRec1()
                              && !swiftFile.getSendToRec1().equalsIgnoreCase("")) {
                            swiftXml.append("<T72_SendToRecvInfo>");
                            swiftXml.append("<Info1 Code=\"DETA\">");
                            swiftXml.append(
                                (null == swiftFile.getSendToRec1()
                                    ? ""
                                    : swiftFile.getSendToRec1().substring(0, 14) + "0000"));
                            swiftXml.append("</Info1>");
                            swiftXml.append("<Info2 Code=\"DETA\">");
                            swiftXml.append(
                                (null == swiftFile.getSendToRec2()
                                    ? ""
                                    : swiftFile.getSendToRec2().substring(0, 13)));
                            swiftXml.append("</Info2>");
                            swiftXml.append("<Info3 Code=\"DETA\">");
                            swiftXml.append(
                                (null == swiftFile.getSendToRec3()
                                    ? ""
                                    : swiftFile.getSendToRec3()));
                            swiftXml.append("</Info3>");
                            if (null != swiftFile.getSendToRec4()
                                && !swiftFile.getSendToRec4().equalsIgnoreCase("")) {
                              swiftXml.append("<Info4 Code=\"DETA\">");
                              swiftXml.append(
                                  (null == swiftFile.getSendToRec4()
                                      ? ""
                                      : swiftFile.getSendToRec4()));
                              swiftXml.append("</Info4>");
                            }
                            swiftXml.append("</T72_SendToRecvInfo>");
                          }
                          if (null != swiftFile.getRegalRep1()
                              && !swiftFile.getRegalRep1().equalsIgnoreCase("")) {
                            swiftXml.append("<T77_RegltryRpt Opt=\"B\">");
                            swiftXml.append("<Info1 Code=\"ORDERRES\" Cntry=\"TH\">");
                            swiftXml.append(
                                (null == swiftFile.getRegalRep1() ? "" : swiftFile.getRegalRep1()));
                            swiftXml.append("</Info1>");
                            swiftXml.append("</T77_RegltryRpt>");
                          }
                        }

                        swiftXml.append("</TxtBlk></SWML>");
                      } else {
                        swiftXml.append(
                            BahtnetUtil.generateBahtnetLevelMaster(
                                swiftFile, refRunning, refLineList.get(index.getAndIncrement())));
                      }
                    }
                    swiftXml.append("</Upload>");
                    InputStream inputStream =
                        new ByteArrayInputStream(
                            swiftXml.toString().getBytes(StandardCharsets.UTF_8));
                    try {
                      fileTransferService.uploadFile(inputStream, directory, fileName);
                    } catch (SftpException e) {
                      throw new RuntimeException(e);
                    }
                  });
        }
      }

      generateFile.setSwifts(outputReportSwifts);

      // GIRO
      List<GIROFileHeader> giroFileHeaders = generateFileAlias.getGiroFileHeaders();
      if (null != giroFileHeaders && !giroFileHeaders.isEmpty()) {
        List<OutputReport> outputReports = new ArrayList<>();
        List<OutputReport> outputReports1 = new ArrayList<>();
        for (GIROFileHeader giroFileHeader : giroFileHeaders) {
          OutputReport outputReport = new OutputReport();
          OutputReport outputReport1 = new OutputReport();
          List<GIROFileDetail> giroFileDetails = giroFileHeader.getGiroFileDetails();
          giroFileDetails.sort(Comparator.comparing(GIROFileDetail::getSeqNo));
          generateFile.setReference(String.valueOf(refRunning));
          StringBuilder outputText = new StringBuilder(giroFileHeader.toString());
          String fileName =
              "G"
                  + giroFileHeader.getUserTRef()
                  + Util.dateToStringPattern_yyMMdd(generateFileAlias.getGiroDate())
                  + getRound()
                  + giroFileHeader.getRunning()
                  + "_____"
                  + giroFileHeader.getBatch();
          outputReport.setFileName(fileName);
          for (GIROFileDetail giroFileDetail : giroFileDetails) {
            if (null == proposalLogHeader) {
              proposalLogHeader = new ProposalLogHeader();
              proposalLogHeader.setId(proposalHeaderId);
              proposalLogHeader.setRefRunning(refRunning);
              proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
              proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
              proposalLogHeader.setCreatedBy(jwt.getSub());
              proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
              proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
              proposalLogHeader.setCancel(false);
              proposalLogHeader.setUse(isAll);
              if (!generateFileAlias.isTestRun()) {
                proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
              }
            }
            outputText.append("\n");
            outputText.append(giroFileDetail.toString());
            ProposalLog proposalLog = new ProposalLog();
            proposalLog.setId(proposalId++);
            proposalLog.setRefRunning(refRunning);
            proposalLog.setRefLine(++i);
            proposalLog.setPaymentName(paymentAlias.getPaymentName());
            proposalLog.setPaymentDate(paymentAlias.getPaymentDate());
            proposalLog.setAccountNoFrom(giroFileHeader.getCompAccNo());
            proposalLog.setAccountNoTo(giroFileDetail.getBankAccountNo());
            proposalLog.setFileType("GIRO");

            proposalLog.setFileName(fileName);
            proposalLog.setTransferDate(generateFileAlias.getGiroDate());
            proposalLog.setAmount(giroFileDetail.getAmountValue());
            proposalLog.setBankFee(Util.getBigDecimal(0));
            proposalLog.setVendor(giroFileDetail.getRecId());
            proposalLog.setBankKey(giroFileDetail.getBankKey());
            proposalLog.setVendorBankAccount(giroFileDetail.getBankAccountNo());
            proposalLog.setTransferLevel("9");
            proposalLog.setPayAccount(giroFileDetail.getUserTref());
            proposalLog.setPaymentType(giroFileDetail.getPaymentType());
            proposalLog.setPayingCompCode("99999");
            proposalLog.setPaymentDocument(giroFileDetail.getPaymentDocNo());
            proposalLog.setFiscalYear(giroFileDetail.getPaymentFiscalYear());
            proposalLog.setFiArea(giroFileDetail.getFiArea());
            proposalLog.setCreditMemoAmount(null != giroFileDetail.getCreditMemoAmount() ? giroFileDetail.getCreditMemoAmount() : BigDecimal.ZERO);
            proposalLog.setCreatedBy(jwt.getSub());
            proposalLog.setUpdatedBy(jwt.getSub());
            proposalLog.setCreated(created);
            proposalLog.setUpdated(created);
            proposalLog.setSendStatus("S");

            proposalLog.setOriginalCompCode(giroFileDetail.getOriginalCompCode());
            proposalLog.setOriginalDocNo(giroFileDetail.getOriginalAccDocNo());
            proposalLog.setOriginalDocType(giroFileDetail.getOriginalDocType());
            proposalLog.setOriginalFiscalYear(giroFileDetail.getOriginalFiscalYear());

            proposalLog.setOriginalWtxAmount(giroFileDetail.getOriginalWtxAmount());
            proposalLog.setOriginalWtxBase(giroFileDetail.getOriginalWtxBase());
            proposalLog.setOriginalWtxAmountP(giroFileDetail.getOriginalWtxAmountP());
            proposalLog.setOriginalWtxBaseP(giroFileDetail.getOriginalWtxBaseP());

            proposalLog.setInvCompCode(giroFileDetail.getInvoiceCompCode());
            proposalLog.setInvDocNo(giroFileDetail.getInvoiceAccDocNo());
            proposalLog.setInvDocType(giroFileDetail.getInvoiceDocType());
            proposalLog.setInvFiscalYear(giroFileDetail.getInvoiceFiscalYear());

            proposalLog.setInvWtxAmount(giroFileDetail.getInvoiceWtxAmount());
            proposalLog.setInvWtxBase(giroFileDetail.getInvoiceWtxBase());
            proposalLog.setInvWtxAmountP(giroFileDetail.getInvoiceWtxAmountP());
            proposalLog.setInvWtxBaseP(giroFileDetail.getInvoiceWtxBaseP());

            proposalLog.setPaymentCompCode(giroFileDetail.getPaymentCompCode());
            proposalLog.setPaymentFiscalYear(giroFileDetail.getPaymentFiscalYear());

            proposalLog.setRefRunningSum(0L);
            proposalLog.setRefLineSum(0);

            proposalLog.setRerun(false);

            proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());

            if (!generateFileAlias.isTestRun()) {
              proposalLogs.add(proposalLog);
            }
          }

          GIROFileTrailer giroFileTrailer = giroFileHeader.getGiroFileTrailer();
          outputText.append("\n");
          outputText.append(giroFileTrailer.toString());
          outputText.append("\n");
          outputReport.setOutputText(outputText.toString());
          if (!generateFileAlias.isTestRun()) {
            InputStream inputStream =
                new ByteArrayInputStream(
                    outputText.toString().getBytes(Charset.forName("TIS-620")));
            fileTransferService.uploadFile(inputStream, directory, outputReport.getFileName());
            InputStream head =
                new ByteArrayInputStream(
                    outputReport
                        .getFileName()
                        .replaceAll("_", " ")
                        .getBytes(Charset.forName("TIS-620")));
            fileTransferService.uploadFile(
                head, directory, "GGIRO" + outputReport.getFileName().substring(3, 13));
            outputReport1.setFileName("GGIRO" + outputReport.getFileName().substring(3, 13));
            outputReport1.setOutputText(outputReport.getFileName().replaceAll("_", " "));
            outputReports1.add(outputReport1);
          }
          outputReports.add(outputReport);
          generateFile.setGiros(outputReports);
          generateFile.setGgiro(outputReports1);
        }
      }

      List<GIROFileHeader> giroFileHeaderSumFiles = generateFileAlias.getGiroFileHeadersSumFile();
      if (null != giroFileHeaderSumFiles && !giroFileHeaderSumFiles.isEmpty()) {
        List<OutputReport> outputReports = new ArrayList<>();
        List<OutputReport> outputReports1 = new ArrayList<>();
        for (GIROFileHeader giroFileHeader : giroFileHeaderSumFiles) {
          OutputReport outputReport = new OutputReport();
          OutputReport outputReport1 = new OutputReport();
          List<GIROFileDetail> giroFileDetails = giroFileHeader.getGiroFileDetails();
          giroFileDetails.sort(Comparator.comparing(GIROFileDetail::getSeqNo));
          generateFile.setReference(String.valueOf(refRunning));
          StringBuilder outputText = new StringBuilder(giroFileHeader.toString());
          String fileName =
              "G"
                  + giroFileHeader.getUserTRef()
                  + Util.dateToStringPattern_yyMMdd(generateFileAlias.getGiroDate())
                  + getRound()
                  + giroFileHeader.getRunning()
                  + "_____"
                  + giroFileHeader.getBatch();
          outputReport.setFileName(fileName);
          Map<String, List<GIROFileDetail>> groupByVendor =
              giroFileDetails.stream()
                  .collect(
                      Collectors.groupingBy(
                          this::groupByMultipleKey,
                          Collectors.mapping((GIROFileDetail g) -> g, toList())));
          int seqNo = 2;
          for (var entry : groupByVendor.entrySet()) {
            BigDecimal totalTransferAmount =
                entry.getValue().stream()
                    .map(GIROFileDetail::getAmountValue) // map
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            String transferAmt = String.format("%.2f", totalTransferAmount);
            String detailSum =
                entry.getValue().get(0).getRecType()
                    + StringUtils.leftPad(String.valueOf(seqNo++), 6, "0")
                    + entry.getValue().get(0).getBankCode()
                    + entry.getValue().get(0).getAccountNo()
                    + entry.getValue().get(0).getTranCode()
                    + StringUtils.leftPad(transferAmt.replace(".", ""), 13, "0")
                    + entry.getValue().get(0).getServiceType()
                    + entry.getValue().get(0).getStatus()
                    + entry.getValue().get(0).getBatch()
                    + entry.getValue().get(0).getRecId()
                    + StringUtils.rightPad("0000", 18, " ")
                    + StringUtils.rightPad(
                        entry.getValue().get(0).getRef1().substring(0, 11), 15, " ")
                    + entry.getValue().get(0).getChkReg()
                    + entry.getValue().get(0).getVendorTaxId()
                    + entry.getValue().get(0).getUserRef();
            outputText.append("\n");
            outputText.append(detailSum);
            if (!generateFileAlias.isTestRun()) {
              if (null == proposalLogHeader) {
                proposalLogHeader = new ProposalLogHeader();
                proposalLogHeader.setId(proposalHeaderId);
                proposalLogHeader.setRefRunning(refRunning);
                proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
                proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
                proposalLogHeader.setCreatedBy(jwt.getSub());
                proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
                proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
                proposalLogHeader.setCancel(false);
                proposalLogHeader.setUse(isAll);
                if (!generateFileAlias.isTestRun()) {
                  proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
                }
              }
              ProposalLogSum proposalLog = new ProposalLogSum();
              proposalLog.setId(proposalSumId++);
              proposalLog.setRefRunning(refRunning);
              proposalLog.setRefLine(++refLineSum);
              proposalLog.setPaymentName(paymentAlias.getPaymentName());
              proposalLog.setPaymentDate(paymentAlias.getPaymentDate());
              proposalLog.setAccountNoFrom(giroFileHeader.getCompAccNo());
              proposalLog.setAccountNoTo(entry.getValue().get(0).getBankAccountNo());
              proposalLog.setFileType("GIRO");

              proposalLog.setFileName(fileName);
              proposalLog.setTransferDate(generateFileAlias.getGiroDate());
              proposalLog.setAmount(totalTransferAmount);
              proposalLog.setBankFee(Util.getBigDecimal(0));
              proposalLog.setVendor(entry.getValue().get(0).getRecId());
              proposalLog.setBankKey(entry.getValue().get(0).getBankKey());
              proposalLog.setVendorBankAccount(entry.getValue().get(0).getBankAccountNo());
              proposalLog.setTransferLevel("9");
              proposalLog.setPayAccount(entry.getValue().get(0).getUserTref());
              proposalLog.setPaymentType(entry.getValue().get(0).getPaymentType());
              proposalLog.setPayingCompCode("99999");
              proposalLog.setCreditMemoAmount(null != entry.getValue().get(0).getCreditMemoAmount() ? entry.getValue().get(0).getCreditMemoAmount() : BigDecimal.ZERO);
              proposalLog.setCreatedBy(jwt.getSub());
              proposalLog.setUpdatedBy(jwt.getSub());
              proposalLog.setCreated(created);
              proposalLog.setUpdated(created);
              proposalLog.setSendStatus("S");
              proposalLog.setRerun(false);

              proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());
              proposalLogSums.add(proposalLog);
            }
            for (GIROFileDetail giroFileDetail : entry.getValue()) {
              if (!generateFileAlias.isTestRun()) {
                ProposalLog proposalLog = new ProposalLog();
                proposalLog.setId(proposalId++);
                proposalLog.setRefRunning(refRunning);
                proposalLog.setRefLine(++i);
                proposalLog.setPaymentName(paymentAlias.getPaymentName());
                proposalLog.setPaymentDate(paymentAlias.getPaymentDate());
                proposalLog.setAccountNoFrom(giroFileHeader.getCompAccNo());
                proposalLog.setAccountNoTo(giroFileDetail.getBankAccountNo());
                proposalLog.setFileType("GIRO");

                proposalLog.setFileName(fileName);
                proposalLog.setTransferDate(generateFileAlias.getGiroDate());
                proposalLog.setAmount(giroFileDetail.getAmountValue());
                proposalLog.setBankFee(Util.getBigDecimal(0));
                proposalLog.setVendor(giroFileDetail.getRecId());
                proposalLog.setBankKey(giroFileDetail.getBankKey());
                proposalLog.setVendorBankAccount(giroFileDetail.getBankAccountNo());
                proposalLog.setTransferLevel("9");
                proposalLog.setPayAccount(giroFileDetail.getUserTref());
                proposalLog.setPaymentType(giroFileDetail.getPaymentType());
                proposalLog.setPayingCompCode("99999");
                proposalLog.setPaymentDocument(giroFileDetail.getPaymentDocNo());
                proposalLog.setFiscalYear(giroFileDetail.getPaymentFiscalYear());
                proposalLog.setFiArea(giroFileDetail.getFiArea());
                proposalLog.setCreditMemoAmount(null != giroFileDetail.getCreditMemoAmount() ? giroFileDetail.getCreditMemoAmount() : BigDecimal.ZERO);
                proposalLog.setCreatedBy(jwt.getSub());
                proposalLog.setUpdatedBy(jwt.getSub());
                proposalLog.setCreated(created);
                proposalLog.setUpdated(created);
                proposalLog.setSendStatus("S");

                proposalLog.setOriginalCompCode(giroFileDetail.getOriginalCompCode());
                proposalLog.setOriginalDocNo(giroFileDetail.getOriginalAccDocNo());
                proposalLog.setOriginalDocType(giroFileDetail.getOriginalDocType());
                proposalLog.setOriginalFiscalYear(giroFileDetail.getOriginalFiscalYear());

                proposalLog.setOriginalWtxAmount(giroFileDetail.getOriginalWtxAmount());
                proposalLog.setOriginalWtxBase(giroFileDetail.getOriginalWtxBase());
                proposalLog.setOriginalWtxAmountP(giroFileDetail.getOriginalWtxAmountP());
                proposalLog.setOriginalWtxBaseP(giroFileDetail.getOriginalWtxBaseP());

                proposalLog.setInvCompCode(giroFileDetail.getInvoiceCompCode());
                proposalLog.setInvDocNo(giroFileDetail.getInvoiceAccDocNo());
                proposalLog.setInvDocType(giroFileDetail.getInvoiceDocType());
                proposalLog.setInvFiscalYear(giroFileDetail.getInvoiceFiscalYear());

                proposalLog.setInvWtxAmount(giroFileDetail.getInvoiceWtxAmount());
                proposalLog.setInvWtxBase(giroFileDetail.getInvoiceWtxBase());
                proposalLog.setInvWtxAmountP(giroFileDetail.getInvoiceWtxAmountP());
                proposalLog.setInvWtxBaseP(giroFileDetail.getInvoiceWtxBaseP());

                proposalLog.setPaymentCompCode(giroFileDetail.getPaymentCompCode());
                proposalLog.setPaymentFiscalYear(giroFileDetail.getPaymentFiscalYear());

                proposalLog.setRefRunningSum(refRunning);
                proposalLog.setRefLineSum(refLineSum);

                proposalLog.setRerun(false);

                proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());

                proposalLogs.add(proposalLog);
              }
            }
          }

          GIROFileTrailer giroFileTrailer = giroFileHeader.getGiroFileTrailer();
          int numberCr = seqNo - 2;
          String footerSum =
              giroFileTrailer.getRecType()
                  + StringUtils.leftPad(String.valueOf(seqNo++), 6, '0')
                  + giroFileTrailer.getBankCode()
                  + giroFileTrailer.getCompAccNo()
                  + giroFileTrailer.getNumberDr()
                  + giroFileTrailer.getTotalDr()
                  + StringUtils.leftPad(String.valueOf(numberCr), 7, "0")
                  + giroFileTrailer.getTotalCr()
                  + giroFileTrailer.getFiller();

          outputText.append("\n");
          outputText.append(footerSum);
          outputText.append("\n");
          outputReport.setOutputText(outputText.toString());
          if (!generateFileAlias.isTestRun()) {
            InputStream inputStream =
                new ByteArrayInputStream(
                    outputText.toString().getBytes(Charset.forName("TIS-620")));
            fileTransferService.uploadFile(inputStream, directory, outputReport.getFileName());
            InputStream head =
                new ByteArrayInputStream(
                    outputReport
                        .getFileName()
                        .replaceAll("_", " ")
                        .getBytes(Charset.forName("TIS-620")));
            fileTransferService.uploadFile(
                head, directory, "GGIRO" + outputReport.getFileName().substring(3, 13));
            outputReport1.setFileName("GGIRO" + outputReport.getFileName().substring(3, 13));
            outputReport1.setOutputText(outputReport.getFileName().replaceAll("_", " "));
            outputReports1.add(outputReport1);
          }
          outputReports.add(outputReport);
          generateFile.setGiros(outputReports);
          generateFile.setGgiro(outputReports1);
        }
      }

      // INHOUSE
      List<InhouseFileHeader> inhouseFileHeaders = generateFileAlias.getInhouseFileHeaders();
      if (null != inhouseFileHeaders && !inhouseFileHeaders.isEmpty()) {
        List<OutputReport> outputReports = new ArrayList<>();
        List<OutputReport> outputReports1 = new ArrayList<>();
        for (InhouseFileHeader inhouseFileHeader : inhouseFileHeaders) {
          OutputReport outputReport = new OutputReport();
          OutputReport outputReport1 = new OutputReport();
          BankCode bankCode = bankCodeMap.get(inhouseFileHeader.getBankCode());
          List<InhouseFileDetail> inhouseFileDetails = inhouseFileHeader.getInhouseFileDetails();
          generateFile.setReference(String.valueOf(refRunning));
          StringBuilder outputText = new StringBuilder(inhouseFileHeader.toString());
          String fileName =
              "I"
                  + bankCode.getIncstCode().substring(0, 4)
                  + Util.dateToStringPattern_yyMMdd(generateFileAlias.getInhouseDate())
                  + getRound()
                  + inhouseFileHeader.getRunning();
          outputReport.setFileName(fileName);
          for (InhouseFileDetail inhouseFileDetail : inhouseFileDetails) {
            if (null == proposalLogHeader) {
              proposalLogHeader = new ProposalLogHeader();
              proposalLogHeader.setId(proposalHeaderId);
              proposalLogHeader.setRefRunning(refRunning);
              proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
              proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
              proposalLogHeader.setCreatedBy(jwt.getSub());
              proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
              proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
              proposalLogHeader.setCancel(false);
              proposalLogHeader.setUse(isAll);
              if (!generateFileAlias.isTestRun()) {
                proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
              }
            }
            outputText.append("\n");
            outputText.append(inhouseFileDetail.toString());
            ProposalLog proposalLog = new ProposalLog();
            proposalLog.setId(proposalId++);
            proposalLog.setRefRunning(refRunning);
            proposalLog.setRefLine(++i);
            proposalLog.setPaymentName(paymentAlias.getPaymentName());
            proposalLog.setPaymentDate(paymentAlias.getPaymentDate());
            proposalLog.setAccountNoFrom(inhouseFileHeader.getCompAccNo());
            proposalLog.setAccountNoTo(inhouseFileDetail.getBankAccountNo());
            proposalLog.setFileType("INHOU");

            proposalLog.setFileName(fileName);
            proposalLog.setTransferDate(generateFileAlias.getInhouseDate());
            proposalLog.setAmount(inhouseFileDetail.getAmountValue());
            proposalLog.setBankFee(Util.getBigDecimal(0));
            proposalLog.setVendor(inhouseFileDetail.getRecId());
            proposalLog.setBankKey(inhouseFileDetail.getBankKey());
            proposalLog.setVendorBankAccount(inhouseFileDetail.getBankAccountNo());
            proposalLog.setTransferLevel("9");
            proposalLog.setPayAccount(inhouseFileDetail.getUserTref());
            proposalLog.setPaymentType(inhouseFileDetail.getPaymentType());
            proposalLog.setPayingCompCode("99999");
            proposalLog.setPaymentDocument(inhouseFileDetail.getPaymentDocNo());
            proposalLog.setFiscalYear(inhouseFileDetail.getPaymentFiscalYear());
            proposalLog.setFiArea(inhouseFileDetail.getFiArea());
            proposalLog.setCreditMemoAmount(null != inhouseFileDetail.getCreditMemoAmount() ? inhouseFileDetail.getCreditMemoAmount() : BigDecimal.ZERO);
            proposalLog.setCreatedBy(jwt.getSub());
            proposalLog.setUpdatedBy(jwt.getSub());
            proposalLog.setCreated(created);
            proposalLog.setUpdated(created);
            proposalLog.setSendStatus("S");

            proposalLog.setOriginalCompCode(inhouseFileDetail.getOriginalCompCode());
            proposalLog.setOriginalDocNo(inhouseFileDetail.getOriginalAccDocNo());
            proposalLog.setOriginalDocType(inhouseFileDetail.getOriginalDocType());
            proposalLog.setOriginalFiscalYear(inhouseFileDetail.getOriginalFiscalYear());

            proposalLog.setOriginalWtxAmount(inhouseFileDetail.getOriginalWtxAmount());
            proposalLog.setOriginalWtxBase(inhouseFileDetail.getOriginalWtxBase());
            proposalLog.setOriginalWtxAmountP(inhouseFileDetail.getOriginalWtxAmountP());
            proposalLog.setOriginalWtxBaseP(inhouseFileDetail.getOriginalWtxBaseP());

            proposalLog.setInvCompCode(inhouseFileDetail.getInvoiceCompCode());
            proposalLog.setInvDocNo(inhouseFileDetail.getInvoiceAccDocNo());
            proposalLog.setInvDocType(inhouseFileDetail.getInvoiceDocType());
            proposalLog.setInvFiscalYear(inhouseFileDetail.getInvoiceFiscalYear());

            proposalLog.setInvWtxAmount(inhouseFileDetail.getInvoiceWtxAmount());
            proposalLog.setInvWtxBase(inhouseFileDetail.getInvoiceWtxBase());
            proposalLog.setInvWtxAmountP(inhouseFileDetail.getInvoiceWtxAmountP());
            proposalLog.setInvWtxBaseP(inhouseFileDetail.getInvoiceWtxBaseP());

            proposalLog.setPaymentCompCode(inhouseFileDetail.getPaymentCompCode());
            proposalLog.setPaymentFiscalYear(inhouseFileDetail.getPaymentFiscalYear());

            proposalLog.setRefRunningSum(0L);
            proposalLog.setRefLineSum(0);

            proposalLog.setRerun(false);

            proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());

            if (!generateFileAlias.isTestRun()) {
              proposalLogs.add(proposalLog);
            }
          }

          InhouseFileTrailer inhouseFileTrailer = inhouseFileHeader.getInhouseFileTrailer();
          outputText.append("\n");
          outputText.append(inhouseFileTrailer.toString());
          outputReport.setOutputText(outputText.toString());
          //                    Files.writeString(Paths.get(path + "/" +
          // outputReport.getFileName()), outputText.toString());
          if (!generateFileAlias.isTestRun()) {
            InputStream inputStream =
                new ByteArrayInputStream(
                    outputText.toString().getBytes(Charset.forName("TIS-620")));
            fileTransferService.uploadFile(inputStream, directory, outputReport.getFileName());
            InputStream head =
                new ByteArrayInputStream(
                    outputReport.getFileName().getBytes(Charset.forName("TIS-620")));
            fileTransferService.uploadFile(
                head, directory, "INH" + outputReport.getFileName().substring(1));
            outputReport1.setFileName("INH" + outputReport.getFileName().substring(1));
            outputReport1.setOutputText(outputReport.getFileName());
            outputReports1.add(outputReport1);
          }
          outputReports.add(outputReport);
          generateFile.setInhouses(outputReports);
          generateFile.setInh(outputReports1);

          //                    metaDataService.saveAllProposalLog(proposalLogs);
        }
      }

      List<InhouseFileHeader> inhouseFileHeaderSumFiles =
          generateFileAlias.getInhouseFileHeadersSumFile();
      if (null != inhouseFileHeaderSumFiles && !inhouseFileHeaderSumFiles.isEmpty()) {
        List<OutputReport> outputReports = new ArrayList<>();
        List<OutputReport> outputReports1 = new ArrayList<>();
        for (InhouseFileHeader inhouseFileHeader : inhouseFileHeaderSumFiles) {
          OutputReport outputReport = new OutputReport();
          OutputReport outputReport1 = new OutputReport();
          BankCode bankCode = bankCodeMap.get(inhouseFileHeader.getBankCode());
          List<InhouseFileDetail> inhouseFileDetails = inhouseFileHeader.getInhouseFileDetails();
          generateFile.setReference(String.valueOf(refRunning));
          StringBuilder outputText = new StringBuilder(inhouseFileHeader.toString());
          String fileName =
              "I"
                  + bankCode.getIncstCode().substring(0, 4)
                  + Util.dateToStringPattern_yyMMdd(generateFileAlias.getInhouseDate())
                  + getRound()
                  + inhouseFileHeader.getRunning();
          outputReport.setFileName(fileName);
          Map<String, List<InhouseFileDetail>> groupByVendor =
              inhouseFileDetails.stream()
                  .collect(
                      Collectors.groupingBy(
                          this::groupByMultipleKey,
                          Collectors.mapping((InhouseFileDetail h) -> h, toList())));
          int seqNo = 2;
          for (var entry : groupByVendor.entrySet()) {
            BigDecimal totalTransferAmount =
                entry.getValue().stream()
                    .map(InhouseFileDetail::getAmountValue) // map
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            String transferAmt = String.format("%.2f", totalTransferAmount);
            String detailSum =
                entry.getValue().get(0).getRecType()
                    + StringUtils.leftPad(String.valueOf(seqNo++), 6, "0")
                    + entry.getValue().get(0).getBankCode()
                    + entry.getValue().get(0).getAccountNo()
                    + entry.getValue().get(0).getTranCode()
                    + StringUtils.leftPad(transferAmt.replace(".", ""), 13, "0")
                    + entry.getValue().get(0).getServiceType()
                    + entry.getValue().get(0).getStatus()
                    + entry.getValue().get(0).getBatch()
                    + entry.getValue().get(0).getRecId()
                    + StringUtils.rightPad("0000", 18, " ")
                    + StringUtils.rightPad(
                        entry.getValue().get(0).getRef1().substring(0, 11), 15, " ")
                    + entry.getValue().get(0).getChkReg()
                    + entry.getValue().get(0).getVendorTaxId()
                    + entry.getValue().get(0).getUserRef();
            outputText.append("\n");
            outputText.append(detailSum);
            if (!generateFileAlias.isTestRun()) {
              if (null == proposalLogHeader) {
                proposalLogHeader = new ProposalLogHeader();
                proposalLogHeader.setId(proposalHeaderId);
                proposalLogHeader.setRefRunning(refRunning);
                proposalLogHeader.setPaymentName(paymentAlias.getPaymentName());
                proposalLogHeader.setPaymentDate(paymentAlias.getPaymentDate());
                proposalLogHeader.setCreatedBy(jwt.getSub());
                proposalLogHeader.setCreated(new Timestamp(new Date().getTime()));
                proposalLogHeader.setGenerateFileAliasId(generateFileAlias.getId());
                proposalLogHeader.setCancel(false);
                proposalLogHeader.setUse(isAll);
                if (!generateFileAlias.isTestRun()) {
                  proposalLogHeader = proposalLogHeaderService.save(proposalLogHeader);
                }
              }
              ProposalLogSum proposalLog = new ProposalLogSum();
              proposalLog.setId(proposalSumId++);
              proposalLog.setRefRunning(refRunning);
              proposalLog.setRefLine(++refLineSum);
              proposalLog.setPaymentName(paymentAlias.getPaymentName());
              proposalLog.setPaymentDate(paymentAlias.getPaymentDate());
              proposalLog.setAccountNoFrom(inhouseFileHeader.getCompAccNo());
              proposalLog.setAccountNoTo(entry.getValue().get(0).getBankAccountNo());
              proposalLog.setFileType("INHOU");

              proposalLog.setFileName(fileName);
              proposalLog.setTransferDate(generateFileAlias.getGiroDate());
              proposalLog.setAmount(totalTransferAmount);
              proposalLog.setBankFee(Util.getBigDecimal(0));
              proposalLog.setVendor(entry.getValue().get(0).getRecId());
              proposalLog.setBankKey(entry.getValue().get(0).getBankKey());
              proposalLog.setVendorBankAccount(entry.getValue().get(0).getBankAccountNo());
              proposalLog.setTransferLevel("9");
              proposalLog.setPayAccount(entry.getValue().get(0).getUserTref());
              proposalLog.setPaymentType(entry.getValue().get(0).getPaymentType());
              proposalLog.setPayingCompCode("99999");
              proposalLog.setPaymentDocument(entry.getValue().get(0).getPaymentDocNo());
              proposalLog.setFiscalYear(entry.getValue().get(0).getPaymentFiscalYear());
              proposalLog.setFiArea(entry.getValue().get(0).getFiArea());
              proposalLog.setCreditMemoAmount(null != entry.getValue().get(0).getCreditMemoAmount() ? entry.getValue().get(0).getCreditMemoAmount() : BigDecimal.ZERO);
              proposalLog.setCreatedBy(jwt.getSub());
              proposalLog.setUpdatedBy(jwt.getSub());
              proposalLog.setCreated(created);
              proposalLog.setUpdated(created);
              proposalLog.setSendStatus("S");

              proposalLog.setOriginalCompCode(entry.getValue().get(0).getOriginalCompCode());
              proposalLog.setOriginalDocNo(entry.getValue().get(0).getOriginalAccDocNo());
              proposalLog.setOriginalDocType(entry.getValue().get(0).getOriginalDocType());
              proposalLog.setOriginalFiscalYear(entry.getValue().get(0).getOriginalFiscalYear());

              proposalLog.setOriginalWtxAmount(entry.getValue().get(0).getOriginalWtxAmount());
              proposalLog.setOriginalWtxBase(entry.getValue().get(0).getOriginalWtxBase());
              proposalLog.setOriginalWtxAmountP(entry.getValue().get(0).getOriginalWtxAmountP());
              proposalLog.setOriginalWtxBaseP(entry.getValue().get(0).getOriginalWtxBaseP());

              proposalLog.setInvCompCode(entry.getValue().get(0).getInvoiceCompCode());
              proposalLog.setInvDocNo(entry.getValue().get(0).getInvoiceAccDocNo());
              proposalLog.setInvDocType(entry.getValue().get(0).getInvoiceDocType());
              proposalLog.setInvFiscalYear(entry.getValue().get(0).getInvoiceFiscalYear());

              proposalLog.setInvWtxAmount(entry.getValue().get(0).getInvoiceWtxAmount());
              proposalLog.setInvWtxBase(entry.getValue().get(0).getInvoiceWtxBase());
              proposalLog.setInvWtxAmountP(entry.getValue().get(0).getInvoiceWtxAmountP());
              proposalLog.setInvWtxBaseP(entry.getValue().get(0).getInvoiceWtxBaseP());

              proposalLog.setPaymentCompCode(entry.getValue().get(0).getPaymentCompCode());
              proposalLog.setPaymentFiscalYear(entry.getValue().get(0).getPaymentFiscalYear());

              proposalLog.setRerun(false);

              proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());
              proposalLogSums.add(proposalLog);
            }
            for (InhouseFileDetail inhouseFileDetail : entry.getValue()) {
              if (!generateFileAlias.isTestRun()) {
                ProposalLog proposalLog = new ProposalLog();
                proposalLog.setId(proposalId++);
                proposalLog.setRefRunning(refRunning);
                proposalLog.setRefLine(++i);
                proposalLog.setPaymentName(paymentAlias.getPaymentName());
                proposalLog.setPaymentDate(paymentAlias.getPaymentDate());
                proposalLog.setAccountNoFrom(inhouseFileHeader.getCompAccNo());
                proposalLog.setAccountNoTo(inhouseFileDetail.getBankAccountNo());
                proposalLog.setFileType("INHOU");

                proposalLog.setFileName(fileName);
                proposalLog.setTransferDate(generateFileAlias.getGiroDate());
                proposalLog.setAmount(inhouseFileDetail.getAmountValue());
                proposalLog.setBankFee(Util.getBigDecimal(0));
                proposalLog.setVendor(inhouseFileDetail.getRecId());
                proposalLog.setBankKey(inhouseFileDetail.getBankKey());
                proposalLog.setVendorBankAccount(inhouseFileDetail.getBankAccountNo());
                proposalLog.setTransferLevel("9");
                proposalLog.setPayAccount(inhouseFileDetail.getUserTref());
                proposalLog.setPaymentType(inhouseFileDetail.getPaymentType());
                proposalLog.setPayingCompCode("99999");
                proposalLog.setPaymentDocument(inhouseFileDetail.getPaymentDocNo());
                proposalLog.setFiscalYear(inhouseFileDetail.getPaymentFiscalYear());
                proposalLog.setFiArea(inhouseFileDetail.getFiArea());
                proposalLog.setCreditMemoAmount(null != inhouseFileDetail.getCreditMemoAmount() ? inhouseFileDetail.getCreditMemoAmount() : BigDecimal.ZERO);
                proposalLog.setCreatedBy(jwt.getSub());
                proposalLog.setUpdatedBy(jwt.getSub());
                proposalLog.setCreated(created);
                proposalLog.setUpdated(created);
                proposalLog.setSendStatus("S");

                proposalLog.setOriginalCompCode(inhouseFileDetail.getOriginalCompCode());
                proposalLog.setOriginalDocNo(inhouseFileDetail.getOriginalAccDocNo());
                proposalLog.setOriginalDocType(inhouseFileDetail.getOriginalDocType());
                proposalLog.setOriginalFiscalYear(inhouseFileDetail.getOriginalFiscalYear());

                proposalLog.setOriginalWtxAmount(inhouseFileDetail.getOriginalWtxAmount());
                proposalLog.setOriginalWtxBase(inhouseFileDetail.getOriginalWtxBase());
                proposalLog.setOriginalWtxAmountP(inhouseFileDetail.getOriginalWtxAmountP());
                proposalLog.setOriginalWtxBaseP(inhouseFileDetail.getOriginalWtxBaseP());

                proposalLog.setInvCompCode(inhouseFileDetail.getInvoiceCompCode());
                proposalLog.setInvDocNo(inhouseFileDetail.getInvoiceAccDocNo());
                proposalLog.setInvDocType(inhouseFileDetail.getInvoiceDocType());
                proposalLog.setInvFiscalYear(inhouseFileDetail.getInvoiceFiscalYear());

                proposalLog.setInvWtxAmount(inhouseFileDetail.getInvoiceWtxAmount());
                proposalLog.setInvWtxBase(inhouseFileDetail.getInvoiceWtxBase());
                proposalLog.setInvWtxAmountP(inhouseFileDetail.getInvoiceWtxAmountP());
                proposalLog.setInvWtxBaseP(inhouseFileDetail.getInvoiceWtxBaseP());

                proposalLog.setPaymentCompCode(inhouseFileDetail.getPaymentCompCode());
                proposalLog.setPaymentFiscalYear(inhouseFileDetail.getPaymentFiscalYear());

                proposalLog.setRefRunningSum(refRunning);
                proposalLog.setRefLineSum(refLineSum);

                proposalLog.setRerun(false);

                proposalLog.setProposalLogHeaderId(proposalLogHeader.getId());

                proposalLogs.add(proposalLog);
              }
            }
          }

          InhouseFileTrailer inhouseFileTrailer = inhouseFileHeader.getInhouseFileTrailer();
          int numberCr = seqNo - 2;
          String footerSum =
              inhouseFileTrailer.getRecType()
                  + StringUtils.leftPad(String.valueOf(seqNo++), 6, '0')
                  + inhouseFileTrailer.getBankCode()
                  + inhouseFileTrailer.getCompAccNo()
                  + inhouseFileTrailer.getNumberDr()
                  + inhouseFileTrailer.getTotalDr()
                  + StringUtils.leftPad(String.valueOf(numberCr), 7, "0")
                  + inhouseFileTrailer.getTotalCr()
                  + inhouseFileTrailer.getFiller();
          outputText.append("\n");
          outputText.append(footerSum);
          outputReport.setOutputText(outputText.toString());
          //                    Files.writeString(Paths.get(path + "/" +
          // outputReport.getFileName()), outputText.toString());
          if (!generateFileAlias.isTestRun()) {
            InputStream inputStream =
                new ByteArrayInputStream(
                    outputText.toString().getBytes(Charset.forName("TIS-620")));
            fileTransferService.uploadFile(inputStream, directory, outputReport.getFileName());
            InputStream head =
                new ByteArrayInputStream(
                    outputReport.getFileName().getBytes(Charset.forName("TIS-620")));
            fileTransferService.uploadFile(
                head, directory, "INH" + outputReport.getFileName().substring(1));
            outputReport1.setFileName("INH" + outputReport.getFileName().substring(1));
            outputReport1.setOutputText(outputReport.getFileName());
            outputReports1.add(outputReport1);
          }
          outputReports.add(outputReport);
          generateFile.setInhouses(outputReports);
          generateFile.setInh(outputReports1);

          //                    metaDataService.saveAllProposalLog(proposalLogs);
        }
      }

      if (!generateFileAlias.isTestRun()) {
        if ((isRegen && !Util.isEmpty(regenFileName) && !Util.isEmpty(fileType)) || (isRegen && isAll)) {
          updateRegen(generateFileAlias, fileType, regenFileName, refRunning, isAll, fileNameLevel1);
        }
        proposalLogSumService.saveBatch(proposalLogSums);
        proposalLogService.saveBatch(proposalLogs);
        generateFileAliasService.save(generateFileAlias);
      }
      Gson gson = new Gson();
      String jsonInString = gson.toJson(generateFile);
      GenerateFileOutput oldOutput =
          generateFileOutputService.findOneByRefRunning(generateFileAlias.getId());
      if (null != oldOutput) {
        generateFileOutputService.delete(oldOutput);
      }
      GenerateFileOutput generateFileOutput = new GenerateFileOutput();
      generateFileOutput.setGenerateFileAliasId(generateFileAlias.getId());
      generateFileOutput.setJsonText(jsonInString);
      generateFileOutputService.save(generateFileOutput);
      if (!isTestRun) {
        generateFileAlias.setRunStatus("S");
        generateFileAlias.setCreated(created);
        generateFileAlias.setCreatedBy(jwt.getSub());
        generateFileAlias.setUpdated(created);
        generateFileAlias.setUpdatedBy(jwt.getSub());
        generateFileAliasService.save(generateFileAlias);
      }
      return generateFile;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private int getRound() {
    Calendar c = Calendar.getInstance();
    int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

    if (timeOfDay > 12) {
      //            return 2;
      return 1; // TEMP
    } else {
      return 1;
    }
  }

  private void setAccountId() {
    this.account.put("TR1", new Account("G1001", "TR1", "0010004343"));
    this.account.put("TR2", new Account("G1002", "TR2", "0010000046"));
    this.account.put("CA", new Account("G1009", "CA (ขาเข้า)", "0010041788"));
    this.account.put("CA2", new Account("G1010", "CA2 (ขาออก)", "0010040250"));
    this.account.put("D3", new Account("G1004", "Vendor (D3)", "0010042563"));
    this.account.put("D5", new Account("G1005", "Agency (D5)", "0010042555"));
    this.account.put("D1", new Account("G1006", "Salary (D1)", "0010041771"));
    this.account.put("D2", new Account("G1007", "Pension (D2)", "0010041052"));
    this.account.put("D4", new Account("G1008", "Medical (D4)", "0010042237"));
  }

  private void checkRerun() {}

  private String checkExist(
      Timestamp paymentDate, String paymentName, boolean isRerun, String fileName) {
    boolean isExist = proposalLogService.isExist(paymentDate, paymentName);
    String errText = "";
    if (isExist) {
      if (!isRerun) {
        errText = "กรุณาเลือกสร้างไฟล์อีกครั้งหากต้องการประมวลผลต่อไป";
      } else {
        if (fileName.isEmpty()) {
          errText = "กรุณาระบุชื่อไฟล์ที่ต้องการสร้างใหม่";
        }
      }
    }
    return errText;
  }

  public boolean checkSwiftFormat(
      List<SwiftFee> swiftFees,
      List<BankCode> bankCodes,
      String bankKey,
      String reference,
      String paymentType,
      String paymentMethod) {
    return swiftFees.stream().anyMatch(swiftFee -> bankKey.equalsIgnoreCase(swiftFee.getBankKey()))
        || ("SWIFT".equalsIgnoreCase(reference)
            && !checkInhouseFormat(bankCodes, bankKey, paymentType, paymentMethod));
  }

  private boolean checkGiroFormat(String bankKey, String paymentType) {
    return bankKey.startsWith("006") && !"D3".equalsIgnoreCase(paymentType);
  }

  private boolean checkInhouseFormat(
      List<BankCode> bankCodes, String bankKey, String paymentType, String paymentMethod) {
    //        log.info("paymentMethod : {}", paymentMethod);
    return bankCodes.stream()
        .anyMatch(
            bankCode ->
                bankKey.equalsIgnoreCase(bankCode.getBankKey())
                    && paymentType.equalsIgnoreCase(bankCode.getPayAccount())
                    && bankCode.isInHouse()
                    && !direct.contains(paymentMethod));
  }

  private boolean checkSmartFormat(
      boolean checkSwiftFormat, boolean checkGiroFormat, boolean checkInhouseFormat) {
    return !checkSwiftFormat && !checkGiroFormat && !checkInhouseFormat;
  }

  public BigDecimal calculateSmartFee(
      List<SmartFee> smarts, String glAccount, BigDecimal amount, boolean isSameday) {
    List<SmartFee> smartFees =
        smarts.stream()
            .filter(smartFee -> glAccount.equalsIgnoreCase(smartFee.getGlAccount()))
            .collect(Collectors.toList());
    for (SmartFee smartFee : smartFees) {
      if (amount.compareTo(smartFee.getAmountMin()) >= 0
          && amount.compareTo(smartFee.getAmountMax()) <= 0) {
        if (isSameday) {
          return smartFee.getSamedayBankFee().add(smartFee.getSamedayBotFee());
        } else {
          return smartFee.getBankFee().add(smartFee.getBotFee());
        }
      }
    }
    return Util.getBigDecimal(0);
  }

  private void checkEffectiveDate(Date swift, Date smart, Date giro, Date inhouse) {
    if (!swift.before(smart)) {
      String errCode = "E6000";
      String errText = "วันที่จ่าย/โอนเงิน ของ swift มีค่ามากกว่า smart";
      String errType = "E";
    }
  }

  private boolean checkSumFile(
      String paymentMethod, String vendor, List<SumFileCondition> sumFileConditions) {
    for (SumFileCondition sumFileCondition : sumFileConditions) {
      if (paymentMethod.equalsIgnoreCase(sumFileCondition.getPaymentMethod())) {
        if (vendor.compareToIgnoreCase(sumFileCondition.getVendorFrom()) >= 0
            && vendor.compareToIgnoreCase(sumFileCondition.getVendorTo()) <= 0) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isNumeric(String strNum) {
    if (null == strNum) {
      return false;
    }
    return pattern.matcher(strNum).matches();
  }

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
        || "K".equalsIgnoreCase(paymentMethod)
            || "R".equalsIgnoreCase(paymentMethod)) {
      return "D3";
    } else if ("F".equalsIgnoreCase(paymentMethod)) {
      return "D4";
    } else if ("2".equalsIgnoreCase(paymentMethod)
        || "4".equalsIgnoreCase(paymentMethod)
        || "7".equalsIgnoreCase(paymentMethod)
        || "9".equalsIgnoreCase(paymentMethod)
        || "I".equalsIgnoreCase(paymentMethod)
        || "L".equalsIgnoreCase(paymentMethod)
            || "Q".equalsIgnoreCase(paymentMethod)
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
    return ("03004".equalsIgnoreCase(compCode)
        && ("KH".equalsIgnoreCase(docType))
        && "K".equalsIgnoreCase(accountType)
        && "J".equalsIgnoreCase(paymentMethod)
        && "03004A0001".equalsIgnoreCase(costCenter)
        && paymentReference.endsWith("0010041052") || "03004".equalsIgnoreCase(compCode)
            && ("PC".equalsIgnoreCase(docType))
            && "K".equalsIgnoreCase(accountType)
            && "J".equalsIgnoreCase(paymentMethod)
            && "03004A0001".equalsIgnoreCase(costCenter));
  }

  private boolean checkInterfaceD1D2(String paymentType) {
    return paymentType.equalsIgnoreCase("D1") || paymentType.equalsIgnoreCase("D2");
  }

  private BankCode getBankCode(List<BankCode> bankCodes, String bankKey) {
    return bankCodes.stream()
        .filter(bankCode -> bankKey.startsWith(bankCode.getBankKey()))
        .findFirst()
        .orElse(new BankCode());
  }

  private SmartFileHeader generateSmartFileHeader(
      GenerateFileAlias generateFileAlias, boolean isTestRun) {
    SmartFileHeader smartFileHeader = new SmartFileHeader();
    smartFileHeader.setFileType("10");
    smartFileHeader.setRecType("0");
    smartFileHeader.setRelVer("00000000");
    smartFileHeader.setRelDate(StringUtils.leftPad("", 8, " "));
    smartFileHeader.setFiller(StringUtils.leftPad("", 301, " "));
    smartFileHeader.setGenerateFileAlias(generateFileAlias);
    smartFileHeader.setProposal(isTestRun);
    smartFileHeader.setEffDate(generateFileAlias.getSmartDate());
    //        return smartFileHeaderService.save(smartFileHeader);
    return smartFileHeader;
  }

  private SmartFileBatch generateSmartFileBatch(
      boolean isTestRun, Date smartDate, SmartFileHeader smartFileHeader) {
    Long running = metaDataService.getFileRunning("SMART");
    String batchNum = isTestRun ? "$$$$$$" : String.format("%06d", running);
    SmartFileBatch smartFileBatch = new SmartFileBatch();
    //        smartFileBatch.setId(seqNo);
    smartFileBatch.setFileType("10");
    smartFileBatch.setRecType("1");
    smartFileBatch.setBatchNum(batchNum);
    smartFileBatch.setSendBankCode("001");
    smartFileBatch.setTotalNum("000");
    smartFileBatch.setTotalAmount(StringUtils.leftPad("", 15, "0"));
    smartFileBatch.setEffDate(Util.dateToStringPattern_ddMMyyyy(smartDate));
    smartFileBatch.setKindTrans("C");
    smartFileBatch.setFiller(StringUtils.leftPad("", 281, " "));
    smartFileBatch.setSmartFileHeaderId(smartFileHeader.getId());
    //        smartFileBatch.setSmartFileHeader(smartFileHeader);
    return smartFileBatch;
  }

  private SmartFileFooter generateSmartFileFooter(
      SmartFileHeader smartFileHeader, int totalRecord, BigDecimal totalAmt) {
    SmartFileFooter smartFileFooter = new SmartFileFooter();
    smartFileFooter.setFileType("10");
    smartFileFooter.setRecType("3");
    smartFileFooter.setRecCount(String.format("%06d", totalRecord));
    smartFileFooter.setFiller(StringUtils.leftPad("", 303, " "));
    smartFileFooter.setAuthorize(StringUtils.leftPad("", 8, " "));
    smartFileFooter.setTotalRecord(totalRecord);
    smartFileFooter.setTotalAmt(totalAmt);
    smartFileFooter.setSmartFileHeaderId(smartFileHeader.getId());
    //        smartFileFooter.setSmartFileHeader(smartFileHeader);
    //        smartFileFooterService.save(smartFileFooter);
    return smartFileFooter;
  }

  //    public String convertBankAccount(String bankKey, String bankAccount, int length) {
  //        if (bankKey.startsWith("030")) {
  //            if (length == 15) {
  //                if (bankAccount.length() == 15) {
  //                    return bankAccount;
  //                } else if (bankAccount.length() == 12) {
  //                    return "999" + bankAccount;
  //                }
  //            } else if (length == 11) {
  //                if (bankAccount.length() == 15) {
  //                    return bankAccount.substring(4);
  //                } else if (bankAccount.length() == 12) {
  //                    return bankAccount.substring(1);
  //                }
  //            }
  //        } else if (bankKey.startsWith("033")) {
  //            if (bankAccount.length() == 10) {
  //                String tempAccount = "0" + bankAccount.substring(0, 4) + "0" +
  // bankAccount.substring(4);
  //                if (length == 15) {
  //                    return "0" + tempAccount.substring(0, 3) + "00" + tempAccount.substring(3);
  //                } else if (length == 11) {
  //                    return "00" + tempAccount.substring(3);
  //                }
  //            } else if (bankAccount.length() == 12) {
  //                String tempAccount = "0" + bankAccount;
  //                if (length == 15) {
  //                    return tempAccount.substring(0, 4) + "00" + tempAccount.substring(4);
  //                } else if (length == 11) {
  //                    return "00" + tempAccount.substring(4);
  //                }
  //            }
  //        } else if (bankKey.startsWith("012")) {
  //            if (length == 15) {
  //                return bankAccount.substring(0, 4) + bankAccount;
  //            } else if (length == 11) {
  //                return bankAccount;
  //            }
  //        } else if (bankKey.startsWith("006")) {
  //            if (length == 15) {
  //                return ("0" + bankAccount).substring(0, 3) + bankAccount;
  //            } else if (length == 11) {
  //                return "0" + bankAccount;
  //            }
  //        } else if (bankKey.startsWith("031")) {
  //            if (length == 15) {
  //                return bankAccount.substring(1, 5) + bankAccount.substring(1);
  //            } else if (length == 11) {
  //                return bankAccount.substring(1);
  //            }
  //        } else if (bankKey.startsWith("028")) {
  //            if (length == 15) {
  //                return (bankAccount.substring(2, 7) + bankAccount.substring(8, 13)).substring(0,
  // 4) + (bankAccount.substring(2, 7) + bankAccount.substring(8, 13));
  //            } else if (length == 11) {
  //                return bankAccount.substring(2, 7) + bankAccount.substring(8, 13);
  //            }
  //        } else if (bankKey.startsWith("010")) {
  //            String accountEleven = "11100" + bankAccount;
  //            if (length == 15) {
  //                return "0000" + accountEleven;
  //            } else if (length == 11) {
  //                return accountEleven;
  //            }
  //        } else if (bankKey.startsWith("067")) {
  //            String accountEleven = "0" + bankAccount.substring(4, 14);
  //            if (length == 15) {
  //                return bankAccount.substring(0, 4) + accountEleven;
  //            } else if (length == 11) {
  //                return accountEleven;
  //            }
  //        } else if (bankKey.startsWith("008")) {
  //            if (bankAccount.length() == 10) {
  //                String accountEleven = "0" + bankAccount;
  //                if (length == 15) {
  //                    return "0001" + accountEleven;
  //                } else if (length == 11) {
  //                    return accountEleven;
  //                }
  //            } else if (bankAccount.length() == 12) {
  //                String accountEleven = bankAccount.substring(1);
  //                if (length == 15) {
  //                    return "0001" + accountEleven;
  //                } else if (length == 11) {
  //                    return accountEleven;
  //                }
  //            }
  //        } else if (bankKey.startsWith("034")) {
  //            if (bankAccount.length() == 10) {
  //                String accountEleven = "0" + bankAccount;
  //                if (length == 15) {
  //                    return accountEleven.substring(0, 4) + accountEleven;
  //                } else if (length == 11) {
  //                    return accountEleven;
  //                }
  //            } else if (bankAccount.length() == 12) {
  //                String accountEleven = bankAccount.substring(1);
  //                if (length == 15) {
  //                    return bankKey.substring(3, 7) + accountEleven;
  //                } else if (length == 11) {
  //                    return accountEleven;
  //                }
  //            }
  //        } else if (bankKey.startsWith("039")) {
  //            if (length == 15) {
  //                return "0001" + bankAccount;
  //            } else if (length == 11) {
  //                return bankAccount;
  //            }
  //        } else if (bankKey.startsWith("079")) {
  //            String accountEleven = bankAccount.substring(0, 6) + bankAccount.substring(9, 14);
  //            if (length == 15) {
  //                return "0001" + accountEleven;
  //            } else if (length == 11) {
  //                return accountEleven;
  //            }
  //        } else if (bankKey.startsWith("069")) {
  //            if (bankAccount.length() == 10) {
  //                String accountEleven = "0" + bankAccount;
  //                if (length == 15) {
  //                    return bankKey.substring(3, 7) + accountEleven;
  //                } else if (length == 11) {
  //                    return accountEleven;
  //                }
  //            } else if (bankAccount.length() == 14) {
  //                String accountEleven = "0" + bankAccount.substring(4, 14);
  //                if (length == 15) {
  //                    return bankKey.substring(3, 7) + accountEleven;
  //                } else if (length == 11) {
  //                    return accountEleven;
  //                }
  //            }
  //        } else {
  //            String accountEleven = "0" + bankAccount;
  //            if (length == 15) {
  //                return accountEleven.substring(0, 4) + accountEleven;
  //            } else if (length == 11) {
  //                return accountEleven;
  //            }
  //        }
  //        return "";
  //    }

  private String getGroupingByKey(SwiftFile swiftFile) {
    // งงว่าเขียนแบบนี้ทำไมมันทำให้ผิดข้อ 4
    //    SwiftFileLog swiftFileLog = swiftFile.getSwiftFileLog();
    //    log.info("swiftFile : {}", swiftFile);
    //    log.info("swiftFileLog : {}", swiftFileLog);

    //    return swiftFileLog.getTransferLevel() + "-" + swiftFileLog.getPaymentMethod();
    return swiftFile.getFileName();
  }

  private SmartFileLog generateSmartFileLog(
      PrepareRunDocument paymentRealRun, GenerateFileAlias generateFileAlias, BigDecimal bankFee) {
    SmartFileLog smartFileLog = new SmartFileLog();
    smartFileLog.setPaymentDate(paymentRealRun.getPaymentDate());
    smartFileLog.setPaymentName(paymentRealRun.getPaymentName());
    smartFileLog.setVendor(paymentRealRun.getVendorCode());
    smartFileLog.setBankKey(paymentRealRun.getPayeeBankKey());
    smartFileLog.setBankAccountNo(paymentRealRun.getBankAccountNo());
    smartFileLog.setPaymentMethod(paymentRealRun.getPaymentMethod());
    smartFileLog.setPayingCompCode(paymentRealRun.getPayingCompanyCode());
    smartFileLog.setPaymentDocNo(paymentRealRun.getPaymentDocumentNo());
    smartFileLog.setPaymentYear(paymentRealRun.getPaymentFiscalYear());
    smartFileLog.setCompCode(paymentRealRun.getOriginalCompanyCode());
    smartFileLog.setInvDocNo(paymentRealRun.getOriginalDocumentNo());
    smartFileLog.setFiscalYear(paymentRealRun.getInvoiceFiscalYear());
    smartFileLog.setFiArea(paymentRealRun.getFiArea());

    smartFileLog.setOriginalAccDocNo(paymentRealRun.getOriginalDocumentNo());
    smartFileLog.setOriginalCompCode(paymentRealRun.getOriginalCompanyCode());
    smartFileLog.setOriginalFiscalYear(paymentRealRun.getInvoiceFiscalYear());
    smartFileLog.setOriginalDocType(paymentRealRun.getOriginalDocumentType());
    smartFileLog.setOriginalWtxAmount(paymentRealRun.getOriginalWtxAmount());
    smartFileLog.setOriginalWtxBase(paymentRealRun.getOriginalWtxBase());
    smartFileLog.setOriginalWtxAmountP(paymentRealRun.getOriginalWtxAmountP());
    smartFileLog.setOriginalWtxBaseP(paymentRealRun.getOriginalWtxBaseP());

    smartFileLog.setInvoiceAccDocNo(paymentRealRun.getInvoiceDocumentNo());
    smartFileLog.setInvoiceCompCode(paymentRealRun.getInvoiceCompanyCode());
    smartFileLog.setInvoiceFiscalYear(paymentRealRun.getInvoiceFiscalYear());
    smartFileLog.setInvoiceDocType(paymentRealRun.getInvoiceDocumentType());
    smartFileLog.setInvoiceWtxAmount(paymentRealRun.getInvoiceWtxAmount());
    smartFileLog.setInvoiceWtxBase(paymentRealRun.getInvoiceWtxBase());
    smartFileLog.setInvoiceWtxAmountP(paymentRealRun.getInvoiceWtxAmountP());
    smartFileLog.setInvoiceWtxBaseP(paymentRealRun.getInvoiceWtxBaseP());

    smartFileLog.setPaymentCompCode(paymentRealRun.getPaymentCompanyCode());
    smartFileLog.setPaymentFiscalYear(paymentRealRun.getPaymentFiscalYear());

    smartFileLog.setTransferLevel("9");
    smartFileLog.setFee(bankFee);
    smartFileLog.setCreditMemo(
        paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()));
    return smartFileLog;
  }

  private SmartFileDetail generateSmartFileDetail(
      PrepareRunDocument paymentRealRun,
      String batchNum,
      Date effDate,
      BigDecimal smartFee,
      boolean isPain) {
    boolean isGPF =
        checkGPF(
            paymentRealRun.getOriginalCompanyCode(),
            paymentRealRun.getOriginalDocumentType(),
            paymentRealRun.getAccountType(),
            paymentRealRun.getPaymentMethod(),
            paymentRealRun.getCostCenter(),
            paymentRealRun.getReference1());
    String paymentType = checkPaymentType(paymentRealRun.getPaymentMethod(), isGPF);
    String serviceType =
        paymentType.equalsIgnoreCase("D1")
                || paymentType.equalsIgnoreCase("D2")
                || paymentType.equalsIgnoreCase("D4")
                || paymentType.equalsIgnoreCase("D5")
            ? "01"
            : "04";
    String sendAcct = "";
    if (paymentType.equalsIgnoreCase("D1") || paymentType.equalsIgnoreCase("D2")) {
      sendAcct = this.account.get("TR2") == null ? "" : this.account.get("TR2").getAccountNo();
    } else {
      sendAcct =
          this.account.get(paymentType) == null ? "" : this.account.get(paymentType).getAccountNo();
    }
    //    if (paymentType.equalsIgnoreCase("D1") || paymentType.equalsIgnoreCase("D2")) {
    //      sendAcct = "0010000046";
    //    } else {
    //      sendAcct = "0010040
    //            areaType="FI_AREA"250";
    //    }
    SmartFileDetail smartFileDetail = new SmartFileDetail();
    smartFileDetail.setFileType("10");
    smartFileDetail.setRecType("2");
    smartFileDetail.setBatchNum(batchNum);
    smartFileDetail.setRecBankCode(paymentRealRun.getPayeeBankKey().substring(0, 3));
    //        if (isPain) {
    smartFileDetail.setRecBranchCode(paymentRealRun.getPayeeBankKey().substring(3));
    smartFileDetail.setRecAcct(paymentRealRun.getPayeeBankAccountNo());
    //        }
    //        else {
    //
    // smartFileDetail.setRecBranchCode(convertBankAccount(paymentRealRun.getPayeeBankKey(),
    // paymentRealRun.getPayeeBankAccountNo(), 15).substring(0, 4));
    //            smartFileDetail.setRecAcct(convertBankAccount(paymentRealRun.getPayeeBankKey(),
    // paymentRealRun.getPayeeBankAccountNo(), 11));
    //        }
    smartFileDetail.setSendBankCode("001");
    smartFileDetail.setSendBranchCode("0001");
    smartFileDetail.setSendAcct(sendAcct);
    smartFileDetail.setEffDate(Util.dateToStringPattern_ddMMyyyy(effDate));
    smartFileDetail.setPaymentType(paymentType);
    smartFileDetail.setServiceType(serviceType);
    smartFileDetail.setClearHouseCode("00");
    Account account = this.account.get(paymentType);
    int transferAmt =
        paymentRealRun
            .getInvoiceAmountPaid()
            .subtract(paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()))
            .subtract(smartFee)
            .multiply(Util.getBigDecimal(100))
            .intValue();
    smartFileDetail.setTransferAmt(StringUtils.leftPad(String.valueOf(transferAmt), 12, "0"));
    smartFileDetail.setRecInform(StringUtils.leftPad("", 60, " "));
    String inform =
        paymentRealRun.getVendorCode()
            + paymentRealRun.getPayingCompanyCode().substring(0, 4)
            + paymentRealRun.getPaymentDocumentNo()
            + paymentRealRun.getPaymentFiscalYear()
            + Util.timestampToStringPattern_ddMMyyyy(paymentRealRun.getPaymentDate())
            + paymentRealRun.getPaymentName()
            + paymentRealRun.getFiArea()
            + "0000"
            + paymentType.substring(1);
    smartFileDetail.setSendInform(StringUtils.rightPad(inform, 60, " "));
    smartFileDetail.setOthInform(StringUtils.leftPad("", 100, " "));
    smartFileDetail.setRefSeqNum("2" + account.getAccountNo().substring(5));
    smartFileDetail.setFiller(StringUtils.leftPad("", 25, " "));
    smartFileDetail.setTransferAmount(
        paymentRealRun
            .getInvoiceAmountPaid()
            .subtract(paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()))
            .subtract(smartFee));

    return smartFileDetail;
  }

  private SmartFileDetail generateSmartFileDetailSum(
      PrepareRunDocument paymentRealRun,
      String batchNum,
      Date effDate,
      BigDecimal smartFee,
      boolean isPain) {
    boolean isGPF =
        checkGPF(
            paymentRealRun.getOriginalCompanyCode(),
            paymentRealRun.getOriginalDocumentType(),
            paymentRealRun.getAccountType(),
            paymentRealRun.getPaymentMethod(),
            paymentRealRun.getCostCenter(),
            paymentRealRun.getReference1());
    String paymentType = checkPaymentType(paymentRealRun.getPaymentMethod(), isGPF);
    String serviceType =
        paymentType.equalsIgnoreCase("D1")
                || paymentType.equalsIgnoreCase("D2")
                || paymentType.equalsIgnoreCase("D4")
                || paymentType.equalsIgnoreCase("D5")
            ? "01"
            : "04";
    String sendAcct = "";
    if (paymentType.equalsIgnoreCase("D1") || paymentType.equalsIgnoreCase("D2")) {
      sendAcct = this.account.get("TR2") == null ? "" : this.account.get("TR2").getAccountNo();
    } else {
      sendAcct =
          this.account.get(paymentType) == null ? "" : this.account.get(paymentType).getAccountNo();
    }
    //    if (paymentType.equalsIgnoreCase("D1") || paymentType.equalsIgnoreCase("D2")) {
    //      sendAcct = "0010000046";
    //    } else {
    //      sendAcct = "0010040
    //            areaType="FI_AREA"250";
    //    }
    SmartFileDetail smartFileDetail = new SmartFileDetail();
    smartFileDetail.setFileType("10");
    smartFileDetail.setRecType("2");
    smartFileDetail.setBatchNum(batchNum);
    smartFileDetail.setRecBankCode(paymentRealRun.getPayeeBankKey().substring(0, 3));
    //        if (isPain) {
    smartFileDetail.setRecBranchCode(paymentRealRun.getPayeeBankKey().substring(3));
    smartFileDetail.setRecAcct(paymentRealRun.getPayeeBankAccountNo());
    //        }
    //        else {
    //
    // smartFileDetail.setRecBranchCode(convertBankAccount(paymentRealRun.getPayeeBankKey(),
    // paymentRealRun.getPayeeBankAccountNo(), 15).substring(0, 4));
    //            smartFileDetail.setRecAcct(convertBankAccount(paymentRealRun.getPayeeBankKey(),
    // paymentRealRun.getPayeeBankAccountNo(), 11));
    //        }
    smartFileDetail.setSendBankCode("001");
    smartFileDetail.setSendBranchCode("0001");
    smartFileDetail.setSendAcct(sendAcct);
    smartFileDetail.setEffDate(Util.dateToStringPattern_ddMMyyyy(effDate));
    smartFileDetail.setPaymentType(paymentType);
    smartFileDetail.setServiceType(serviceType);
    smartFileDetail.setClearHouseCode("00");
    Account account = this.account.get(paymentType);
    int transferAmt =
        paymentRealRun
            .getInvoiceAmountPaid()
            .subtract(paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()))
            .subtract(smartFee)
            .multiply(Util.getBigDecimal(100))
            .intValue();
    smartFileDetail.setTransferAmt(StringUtils.leftPad(String.valueOf(transferAmt), 12, "0"));
    smartFileDetail.setRecInform(StringUtils.leftPad("", 60, " "));
    String inform =
        paymentRealRun.getVendorCode()
            + paymentRealRun.getPayingCompanyCode().substring(0, 4)
            + "0000"
            + Util.timestampToStringPattern_ddMMyyyy(paymentRealRun.getPaymentDate())
            + paymentRealRun.getPaymentName()
            + "0000"
            + paymentType.substring(1);
    smartFileDetail.setSendInform(StringUtils.rightPad(inform, 60, " "));
    smartFileDetail.setOthInform(StringUtils.leftPad("", 100, " "));
    smartFileDetail.setRefSeqNum("2" + account.getAccountNo().substring(5));
    smartFileDetail.setFiller(StringUtils.leftPad("", 25, " "));
    smartFileDetail.setTransferAmount(
        paymentRealRun
            .getInvoiceAmountPaid()
            .subtract(paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));

    return smartFileDetail;
  }

  private SwiftFile generateSwiftFile(
      PrepareRunDocument paymentRealRun,
      GenerateFileAlias generateFileAlias,
      List<BankCode> bankCodes,
      boolean isSumFile,
      boolean isPac) {
    SwiftFile swiftFile = new SwiftFile();
    swiftFile.setBankCode("CRED");
    swiftFile.setTransferType("RFT");
    swiftFile.setValueDate(
        Util.dateToStringPatternWithScore_yyyyMMdd(generateFileAlias.getSwiftDate()));
    swiftFile.setCurrency("THB");
    swiftFile.setSetAmount(
        paymentRealRun
            .getInvoiceAmountPaid()
            .subtract(paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
    boolean isGPF =
        checkGPF(
            paymentRealRun.getOriginalCompanyCode(),
            paymentRealRun.getOriginalDocumentType(),
            paymentRealRun.getAccountType(),
            paymentRealRun.getPaymentMethod(),
            paymentRealRun.getCostCenter(),
            paymentRealRun.getReference1());
    String paymentType = checkPaymentType(paymentRealRun.getPaymentMethod(), isGPF);
    swiftFile.setPayAccount(paymentType);
    swiftFile.setOrdAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setOrdName1("99999");
    swiftFile.setOrdName2("");
    swiftFile.setOrdName3("");
    swiftFile.setOrdName4("");
    swiftFile.setSendCode("D");
    swiftFile.setRecCode("C");
    //        log.info("paymentRealRun.getPayeeBankKey() : {}", paymentRealRun.getPayeeBankKey());
    BankCode bankCode = getBankCode(bankCodes, paymentRealRun.getPayeeBankKey());
    if (paymentType.equalsIgnoreCase("D3")
        || paymentType.equalsIgnoreCase("D4")
        || paymentType.equalsIgnoreCase("D5")) {
      swiftFile.setSendAcct(this.account.get(paymentType).getAccountNo());
    } else {
      swiftFile.setSendAcct(this.account.get("TR2").getAccountNo());
    }
    //        if (isSumFile) {
    swiftFile.setRecAcct(bankCode.getAccountNo());
    //        } else {
    //            swiftFile.setRecAcct(paymentRealRun.getBankAccountNo());
    //        }
    swiftFile.setRecInsti(bankCode.getIncstCode());
    swiftFile.setBenAcct(paymentRealRun.getBankAccountNo());
    if (null != paymentRealRun.getAccountHolderName()) {
      if (paymentRealRun.getAccountHolderName().trim().length() > 35) {
        String[] accountHolderName =
            paymentRealRun.getAccountHolderName().trim().split("(?<=\\G.{35})");
        if (accountHolderName.length > 0) {
          swiftFile.setBenName1(accountHolderName[0].replaceAll("[^a-zA-Z0-9]+", " "));
        }

        if (accountHolderName.length > 1) {
          swiftFile.setBenName2(accountHolderName[1].replaceAll("[^a-zA-Z0-9]+", " "));
        }

        if (accountHolderName.length > 2) {
          swiftFile.setBenName3(accountHolderName[2].replaceAll("[^a-zA-Z0-9]+", " "));
        }

        if (accountHolderName.length > 3) {
          swiftFile.setBenName4(accountHolderName[3].replaceAll("[^a-zA-Z0-9]+", " "));
        }
      } else {
        swiftFile.setBenName1(
            paymentRealRun.getAccountHolderName().trim().replaceAll("[^a-zA-Z0-9]+", " "));
      }
    }

    swiftFile.setDetailCharg("SHA");
    swiftFile.setSendToRec1(
        paymentRealRun.getVendorCode()
            + paymentRealRun.getPayingCompanyCode().substring(0, 4)
            + paymentRealRun.getPaymentDocumentNo()
            + paymentRealRun.getPaymentFiscalYear());
    swiftFile.setSendToRec2(
        Util.dateToStringPattern_ddMMyyyy(paymentRealRun.getPaymentDate())
            + paymentRealRun.getPaymentName()
            + paymentRealRun.getFiArea());
    swiftFile.setSendToRec3("0000" + paymentType.substring(1));
    if (null != paymentRealRun.getPayeeCode()) {
      VendorBankAccount vendorBankAccount =
          vendorBankAccountService.findOneByVendor(paymentRealRun.getVendorCode());
      if (null != vendorBankAccount.getBankAccountHolderName()) {
        if (isPac) {
          if (vendorBankAccount.getBankAccountHolderName().trim().length() > 140) {
            String[] accountHolderName =
                vendorBankAccount.getBankAccountHolderName().trim().split("(?<=\\G.{140})");
            if (accountHolderName.length > 0) {
              swiftFile.setSendToRec4(accountHolderName[0].replaceAll("[^a-zA-Z0-9]+", " ").trim());
            }
          } else {
            swiftFile.setSendToRec4(
                vendorBankAccount
                    .getBankAccountHolderName()
                    .trim()
                    .replaceAll("[^a-zA-Z0-9]+", " ")
                    .trim());
          }
        } else {
          if (vendorBankAccount.getBankAccountHolderName().trim().length() > 29) {
            String[] accountHolderName =
                vendorBankAccount.getBankAccountHolderName().trim().split("(?<=\\G.{29})");
            if (accountHolderName.length > 0) {
              swiftFile.setSendToRec4(accountHolderName[0].replaceAll("[^a-zA-Z0-9]+", " ").trim());
            }
          } else {
            swiftFile.setSendToRec4(
                vendorBankAccount
                    .getBankAccountHolderName()
                    .trim()
                    .replaceAll("[^a-zA-Z0-9]+", " ")
                    .trim());
          }
        }

      }
      //            swiftFile.setSendToRec4(paymentRealRun.getAccountHolderName());
    }
    String regulatory = "PPNO";
    //    Vendor vendor = Context.sessionVendor.get(paymentRealRun.getVendorCode());
    Vendor vendor = vendorService.findOneByVendorCodeForStatus(paymentRealRun.getVendorCode());
    if ("6000".equalsIgnoreCase(vendor.getVendorGroup())) {
      regulatory = "PPNO";
    } else if ("2000".equalsIgnoreCase(vendor.getVendorGroup())) {
      regulatory = "CCID";
    } else if ("1000".equalsIgnoreCase(vendor.getVendorGroup())
        || "5000".equalsIgnoreCase(vendor.getVendorGroup())) {
      regulatory = "TXID";
    } else {
      regulatory = "OTHR";
    }
    String regal = regulatory + paymentRealRun.getVendorTaxId();
    swiftFile.setRegalRep1(regal);
    //        swiftFile.setGenerateFileAlias(generateFileAlias);
    return swiftFile;
  }

  private SwiftFileLog generateSwiftFileLog(PrepareRunDocument paymentRealRun) {
    SwiftFileLog swiftFileLog = new SwiftFileLog();
    swiftFileLog.setPaymentDate(paymentRealRun.getPaymentDate());
    swiftFileLog.setPaymentName(paymentRealRun.getPaymentName());
    swiftFileLog.setVendor(paymentRealRun.getVendorCode());
    swiftFileLog.setBankKey(paymentRealRun.getPayeeBankKey());
    swiftFileLog.setBankAccountNo(paymentRealRun.getBankAccountNo());
    swiftFileLog.setPaymentMethod(paymentRealRun.getPaymentMethod());
    swiftFileLog.setPayingCompCode(paymentRealRun.getPayingCompanyCode());
    swiftFileLog.setPaymentDocNo(paymentRealRun.getPaymentDocumentNo());
    swiftFileLog.setPaymentYear(paymentRealRun.getPaymentFiscalYear());
    swiftFileLog.setCompCode(paymentRealRun.getOriginalCompanyCode());
    swiftFileLog.setInvDocNo(paymentRealRun.getOriginalDocumentNo());
    swiftFileLog.setFiscalYear(paymentRealRun.getOriginalFiscalYear());
    swiftFileLog.setFiArea(paymentRealRun.getFiArea());

    //        log.info("SWIFT getOriginalDocumentNo : {} ", paymentRealRun.getOriginalDocumentNo());
    //        log.info("SWIFT getOriginalCompanyCode : {} ",
    // paymentRealRun.getOriginalCompanyCode());
    //        log.info("SWIFT getOriginalFiscalYear : {} ", paymentRealRun.getOriginalFiscalYear());
    //        log.info("SWIFT getOriginalDocumentType : {} ",
    // paymentRealRun.getOriginalDocumentType());
    swiftFileLog.setOriginalAccDocNo(paymentRealRun.getOriginalDocumentNo());
    swiftFileLog.setOriginalCompCode(paymentRealRun.getOriginalCompanyCode());
    swiftFileLog.setOriginalFiscalYear(paymentRealRun.getInvoiceFiscalYear());
    swiftFileLog.setOriginalDocType(paymentRealRun.getOriginalDocumentType());
    swiftFileLog.setOriginalWtxAmount(paymentRealRun.getOriginalWtxAmount());
    swiftFileLog.setOriginalWtxBase(paymentRealRun.getOriginalWtxBase());
    swiftFileLog.setOriginalWtxAmountP(paymentRealRun.getOriginalWtxAmountP());
    swiftFileLog.setOriginalWtxBaseP(paymentRealRun.getOriginalWtxBaseP());

    swiftFileLog.setInvoiceAccDocNo(paymentRealRun.getInvoiceDocumentNo());
    swiftFileLog.setInvoiceCompCode(paymentRealRun.getInvoiceCompanyCode());
    swiftFileLog.setInvoiceFiscalYear(paymentRealRun.getInvoiceFiscalYear());
    swiftFileLog.setInvoiceDocType(paymentRealRun.getInvoiceDocumentType());
    swiftFileLog.setInvoiceWtxAmount(paymentRealRun.getInvoiceWtxAmount());
    swiftFileLog.setInvoiceWtxBase(paymentRealRun.getInvoiceWtxBase());
    swiftFileLog.setInvoiceWtxAmountP(paymentRealRun.getInvoiceWtxAmountP());
    swiftFileLog.setInvoiceWtxBaseP(paymentRealRun.getInvoiceWtxBaseP());

    swiftFileLog.setPaymentCompCode(paymentRealRun.getPaymentCompanyCode());
    swiftFileLog.setPaymentFiscalYear(paymentRealRun.getPaymentFiscalYear());

    swiftFileLog.setTransferLevel("9");
    swiftFileLog.setFee(Util.getBigDecimal(0));
    swiftFileLog.setCreditMemo(
        paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()));

    return swiftFileLog;
  }

  private GIROFileHeader generateGIROFileHeader(
      GenerateFileAlias generateFileAlias,
      BankCode bankCode,
      String running,
      String batchNum,
      String payeeBankKey,
      String vendorName,
      Timestamp paymentDate,
      String userTref,
      boolean isTestRun) {
    String bankKey = payeeBankKey.substring(0, 3);
    GIROFileHeader giroFileHeader = new GIROFileHeader();
    giroFileHeader.setRecType("H");
    giroFileHeader.setSeqNo("000001");
    //        log.info("bankKey : {}", bankKey);
    //        log.info("bankCode : {}", bankCode);
    giroFileHeader.setBankCode(bankKey);
    giroFileHeader.setCompAccNo(bankCode.getAccountNo());
    String vendor =
        vendorName.length() >= 25
            ? vendorName.substring(0, 25)
            : StringUtils.rightPad(vendorName, 25, "0");
    giroFileHeader.setCompName(vendor);
    giroFileHeader.setPostDate(
        Util.dateToStringGenerateGIROFileStructure(generateFileAlias.getGiroDate()));
    giroFileHeader.setRunning(running);
    giroFileHeader.setBatch(batchNum);
    giroFileHeader.setEffDate(Util.dateToStringPattern_ddMMyyyy(paymentDate));
    giroFileHeader.setProposal(isTestRun);
    giroFileHeader.setFiller(StringUtils.leftPad("", 74, "0"));
    giroFileHeader.setUserTRef(userTref);
    return giroFileHeader;
  }

  private GIROFileDetail generateGIROFileDetail(
      PrepareRunDocument paymentRealRun,
      String batchNum,
      int seqNoFile,
      int seqNo,
      boolean isTestRun) {
    String bankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
    GIROFileDetail giroFileDetail = new GIROFileDetail();
    boolean isGPF =
        checkGPF(
            paymentRealRun.getOriginalCompanyCode(),
            paymentRealRun.getOriginalDocumentType(),
            paymentRealRun.getAccountType(),
            paymentRealRun.getPaymentMethod(),
            paymentRealRun.getCostCenter(),
            paymentRealRun.getReference1());
    String paymentType = checkPaymentType(paymentRealRun.getPaymentMethod(), isGPF);
    String userTref =
        paymentType.equalsIgnoreCase("D3")
                || paymentType.equalsIgnoreCase("D4")
                || paymentType.equalsIgnoreCase("D5")
            ? "A1"
            : paymentType.equalsIgnoreCase("D1") ? "A2" : "A2";
    giroFileDetail.setUserTref(userTref);
    giroFileDetail.setSeqNoFile(StringUtils.leftPad(String.valueOf(seqNoFile), 3, "0"));
    if ("D".equalsIgnoreCase(paymentRealRun.getDrCr())) {
      giroFileDetail.setTmpNoDr(1);
      giroFileDetail.setAmtNoDr(1);
      giroFileDetail.setTmpDr(
          paymentRealRun
              .getInvoiceAmountPaid()
              .subtract(
                  paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
      giroFileDetail.setAmtDr(
          paymentRealRun
              .getInvoiceAmountPaid()
              .subtract(
                  paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
    } else {
      giroFileDetail.setTmpNoCr(1);
      giroFileDetail.setAmtNoCr(1);
      giroFileDetail.setTmpCr(
          paymentRealRun
              .getInvoiceAmountPaid()
              .subtract(
                  paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
      giroFileDetail.setAmtCr(
          paymentRealRun
              .getInvoiceAmountPaid()
              .subtract(
                  paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
    }

    giroFileDetail.setOriginalAccDocNo(paymentRealRun.getOriginalDocumentNo());
    giroFileDetail.setOriginalCompCode(paymentRealRun.getOriginalCompanyCode());
    giroFileDetail.setOriginalFiscalYear(paymentRealRun.getOriginalFiscalYear());
    giroFileDetail.setOriginalDocType(paymentRealRun.getOriginalDocumentType());
    giroFileDetail.setOriginalWtxAmount(paymentRealRun.getOriginalWtxAmount());
    giroFileDetail.setOriginalWtxBase(paymentRealRun.getOriginalWtxBase());
    giroFileDetail.setOriginalWtxAmountP(paymentRealRun.getOriginalWtxAmountP());
    giroFileDetail.setOriginalWtxBaseP(paymentRealRun.getOriginalWtxBaseP());

    giroFileDetail.setInvoiceAccDocNo(paymentRealRun.getInvoiceDocumentNo());
    giroFileDetail.setInvoiceCompCode(paymentRealRun.getInvoiceCompanyCode());
    giroFileDetail.setInvoiceFiscalYear(paymentRealRun.getInvoiceFiscalYear());
    giroFileDetail.setInvoiceDocType(paymentRealRun.getInvoiceDocumentType());
    giroFileDetail.setInvoiceWtxAmount(paymentRealRun.getInvoiceWtxAmount());
    giroFileDetail.setInvoiceWtxBase(paymentRealRun.getInvoiceWtxBase());
    giroFileDetail.setInvoiceWtxAmountP(paymentRealRun.getInvoiceWtxAmountP());
    giroFileDetail.setInvoiceWtxBaseP(paymentRealRun.getInvoiceWtxBaseP());

    giroFileDetail.setPaymentCompCode(paymentRealRun.getPaymentCompanyCode());
    giroFileDetail.setPaymentFiscalYear(paymentRealRun.getPaymentFiscalYear());

    giroFileDetail.setPaymentType(paymentType);
    giroFileDetail.setBankKey(paymentRealRun.getPayeeBankKey());
    giroFileDetail.setBankAccountNo(paymentRealRun.getPayeeBankAccountNo());
    giroFileDetail.setCompCode(paymentRealRun.getOriginalCompanyCode());
    giroFileDetail.setFiArea(paymentRealRun.getFiArea());
    giroFileDetail.setAccDocNo(paymentRealRun.getOriginalDocumentNo());
    giroFileDetail.setFiscalYear(paymentRealRun.getOriginalFiscalYear());
    giroFileDetail.setPayingCompCode(paymentRealRun.getPayingCompanyCode());
    giroFileDetail.setPaymentDocNo(paymentRealRun.getPaymentDocumentNo());
    giroFileDetail.setRecType("D");
    giroFileDetail.setSeqNo(StringUtils.leftPad(String.valueOf(seqNo), 6, "0"));
    giroFileDetail.setBankCode(StringUtils.rightPad(bankKey, 3, " "));
    giroFileDetail.setAccountNo(
        StringUtils.rightPad(paymentRealRun.getPayeeBankAccountNo(), 10, " "));
    giroFileDetail.setTranCode("C");
    String transferAmt =
        String.format(
            "%.2f",
            paymentRealRun
                .getInvoiceAmountPaid()
                .subtract(
                    paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
    giroFileDetail.setAmountValue(
        paymentRealRun
            .getInvoiceAmountPaid()
            .subtract(paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
    giroFileDetail.setAmount(StringUtils.leftPad(transferAmt.replace(".", ""), 13, "0"));
    String serviceType =
        paymentType.equalsIgnoreCase("D1")
                || paymentType.equalsIgnoreCase("D2")
                || paymentType.equalsIgnoreCase("D4")
            ? "02"
            : "14";
    giroFileDetail.setServiceType(serviceType);
    giroFileDetail.setStatus("9");
    giroFileDetail.setBatch(batchNum);
    giroFileDetail.setRecId(StringUtils.rightPad(paymentRealRun.getVendorCode(), 10, " "));
    String refNum =
        paymentRealRun.getPayingCompanyCode().substring(0, 4)
            + paymentRealRun.getPaymentDocumentNo()
            + paymentRealRun.getPaymentFiscalYear();
    giroFileDetail.setRefNum(StringUtils.rightPad(refNum, 18));
    String ref1 =
        Util.dateToStringPattern_ddMMyy(paymentRealRun.getPaymentDate())
            + paymentRealRun.getPaymentName()
            + paymentRealRun.getFiArea();
    giroFileDetail.setRef1(ref1);
    String chkReg =
        "A".equalsIgnoreCase(String.valueOf(paymentRealRun.getVendorCode().charAt(0)))
                || "V".equalsIgnoreCase(String.valueOf(paymentRealRun.getVendorCode().charAt(0)))
            ? "N"
            : "Y";
    giroFileDetail.setChkReg(chkReg);
    giroFileDetail.setVendorTaxId(StringUtils.rightPad(paymentRealRun.getVendorTaxId(), 13, " "));
    giroFileDetail.setUserRef(StringUtils.leftPad(userTref, 39, "0"));
    giroFileDetail.setCrValue(
        paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()));
    giroFileDetail.setCreditMemoAmount(paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()));
    return giroFileDetail;
  }

  private GIROFileTrailer generateGIROFileTrailer(
      BankCode bankCode,
      String fileType,
      String payeeBankKey,
      String paymentMethod,
      int numDr,
      BigDecimal amtDr,
      int numCr,
      BigDecimal amtCr,
      int seqNo) {
    String bankKey = payeeBankKey.substring(0, 3);
    GIROFileTrailer giroFileTrailer = new GIROFileTrailer();
    giroFileTrailer.setFileType(fileType);
    giroFileTrailer.setPaymentMethod(paymentMethod);
    giroFileTrailer.setNumDr(numDr);
    giroFileTrailer.setAmtDr(amtDr);
    giroFileTrailer.setNumCr(numCr);
    giroFileTrailer.setAmtCr(amtCr);
    giroFileTrailer.setRecType("T");
    giroFileTrailer.setSeqNo(StringUtils.leftPad(String.valueOf(seqNo), 6, "0"));
    giroFileTrailer.setBankCode(bankKey);
    giroFileTrailer.setCompAccNo(bankCode.getAccountNo());
    giroFileTrailer.setNumberDr(StringUtils.leftPad(String.valueOf(numDr), 7, "0"));
    String transferAmtDr = String.format("%.2f", amtDr);
    giroFileTrailer.setTotalDr(StringUtils.leftPad(transferAmtDr.replace(".", ""), 18, "0"));
    giroFileTrailer.setNumberCr(StringUtils.leftPad(String.valueOf(numCr), 7, "0"));
    String transferAmtCr = String.format("%.2f", amtCr);
    giroFileTrailer.setTotalCr(StringUtils.leftPad(transferAmtCr.replace(".", ""), 18, "0"));
    giroFileTrailer.setFiller(StringUtils.leftPad("", 69, "0"));

    return giroFileTrailer;
  }

  private InhouseFileHeader generateInhouseFileHeader(
      GenerateFileAlias generateFileAlias,
      BankCode bankCode,
      String running,
      String batchNum,
      String payeeBankKey,
      String vendorName,
      Timestamp paymentDate,
      String userTref,
      boolean isTestRun) {
    String bankKey = payeeBankKey.substring(0, 3);
    InhouseFileHeader inhouseFileHeader = new InhouseFileHeader();
    inhouseFileHeader.setRecType("H");
    inhouseFileHeader.setSeqNo("000001");
    inhouseFileHeader.setBankCode(bankKey);
    inhouseFileHeader.setCompAccNo(bankCode.getAccountNo());
    String vendor =
        vendorName.length() >= 25
            ? vendorName.substring(0, 25)
            : StringUtils.rightPad(vendorName, 25, "0");
    inhouseFileHeader.setCompName(vendor);
    inhouseFileHeader.setPostDate(
        Util.dateToStringGenerateGIROFileStructure(generateFileAlias.getGiroDate()));
    inhouseFileHeader.setRunning(running);
    inhouseFileHeader.setBatch(batchNum);
    inhouseFileHeader.setEffDate(Util.dateToStringPattern_ddMMyyyy(paymentDate));
    inhouseFileHeader.setProposal(isTestRun);
    inhouseFileHeader.setFiller(StringUtils.leftPad("", 74, "0"));
    inhouseFileHeader.setUserTRef(userTref);
    return inhouseFileHeader;
  }

  private InhouseFileDetail generateInhouseFileDetail(
      PrepareRunDocument paymentRealRun,
      String batchNum,
      int seqNoFile,
      int seqNo,
      boolean isTestRun) {
    String bankKey = paymentRealRun.getPayeeBankKey().substring(0, 3);
    InhouseFileDetail inhouseFileDetail = new InhouseFileDetail();
    boolean isGPF =
        checkGPF(
            paymentRealRun.getOriginalCompanyCode(),
            paymentRealRun.getOriginalDocumentType(),
            paymentRealRun.getAccountType(),
            paymentRealRun.getPaymentMethod(),
            paymentRealRun.getCostCenter(),
            paymentRealRun.getReference1());
    String paymentType = checkPaymentType(paymentRealRun.getPaymentMethod(), isGPF);
    String userTref =
        paymentType.equalsIgnoreCase("D3")
                || paymentType.equalsIgnoreCase("D4")
                || paymentType.equalsIgnoreCase("D5")
            ? "A1"
            : "A2";
    inhouseFileDetail.setUserTref(userTref);
    inhouseFileDetail.setSeqNoFile(StringUtils.leftPad(String.valueOf(seqNoFile), 3, "0"));
    if ("D".equalsIgnoreCase(paymentRealRun.getDrCr())) {
      inhouseFileDetail.setTmpNoDr(1);
      inhouseFileDetail.setAmtNoDr(1);
      inhouseFileDetail.setTmpDr(
          paymentRealRun
              .getInvoiceAmountPaid()
              .subtract(
                  paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
      inhouseFileDetail.setAmtDr(
          paymentRealRun
              .getInvoiceAmountPaid()
              .subtract(
                  paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
    } else {
      inhouseFileDetail.setTmpNoCr(1);
      inhouseFileDetail.setAmtNoCr(1);
      inhouseFileDetail.setTmpCr(
          paymentRealRun
              .getInvoiceAmountPaid()
              .subtract(
                  paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
      inhouseFileDetail.setAmtCr(
          paymentRealRun
              .getInvoiceAmountPaid()
              .subtract(
                  paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
    }

    inhouseFileDetail.setOriginalAccDocNo(paymentRealRun.getOriginalDocumentNo());
    inhouseFileDetail.setOriginalCompCode(paymentRealRun.getOriginalCompanyCode());
    inhouseFileDetail.setOriginalFiscalYear(paymentRealRun.getOriginalFiscalYear());
    inhouseFileDetail.setOriginalDocType(paymentRealRun.getOriginalDocumentType());
    inhouseFileDetail.setOriginalWtxAmount(paymentRealRun.getOriginalWtxAmount());
    inhouseFileDetail.setOriginalWtxBase(paymentRealRun.getOriginalWtxBase());
    inhouseFileDetail.setOriginalWtxAmountP(paymentRealRun.getOriginalWtxAmountP());
    inhouseFileDetail.setOriginalWtxBaseP(paymentRealRun.getOriginalWtxBaseP());

    inhouseFileDetail.setInvoiceAccDocNo(paymentRealRun.getInvoiceDocumentNo());
    inhouseFileDetail.setInvoiceCompCode(paymentRealRun.getInvoiceCompanyCode());
    inhouseFileDetail.setInvoiceFiscalYear(paymentRealRun.getInvoiceFiscalYear());
    inhouseFileDetail.setInvoiceDocType(paymentRealRun.getInvoiceDocumentType());
    inhouseFileDetail.setInvoiceWtxAmount(paymentRealRun.getInvoiceWtxAmount());
    inhouseFileDetail.setInvoiceWtxBase(paymentRealRun.getInvoiceWtxBase());
    inhouseFileDetail.setInvoiceWtxAmountP(paymentRealRun.getInvoiceWtxAmountP());
    inhouseFileDetail.setInvoiceWtxBaseP(paymentRealRun.getInvoiceWtxBaseP());

    inhouseFileDetail.setPaymentCompCode(paymentRealRun.getPaymentCompanyCode());
    inhouseFileDetail.setPaymentFiscalYear(paymentRealRun.getPaymentFiscalYear());

    inhouseFileDetail.setPaymentType(paymentType);
    inhouseFileDetail.setBankKey(paymentRealRun.getPayeeBankKey());
    inhouseFileDetail.setBankAccountNo(paymentRealRun.getPayeeBankAccountNo());
    inhouseFileDetail.setCrValue(
        paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()));
    inhouseFileDetail.setCreditMemoAmount(paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()));
    inhouseFileDetail.setCompCode(paymentRealRun.getOriginalCompanyCode());
    inhouseFileDetail.setFiArea(paymentRealRun.getFiArea());
    inhouseFileDetail.setAccDocNo(paymentRealRun.getOriginalDocumentNo());
    inhouseFileDetail.setFiscalYear(paymentRealRun.getOriginalFiscalYear());
    inhouseFileDetail.setPayingCompCode(paymentRealRun.getPayingCompanyCode());
    inhouseFileDetail.setPaymentDocNo(paymentRealRun.getPaymentDocumentNo());
    inhouseFileDetail.setRecType("D");
    inhouseFileDetail.setSeqNo(StringUtils.leftPad(String.valueOf(seqNo), 6, "0"));
    inhouseFileDetail.setBankCode(StringUtils.rightPad(bankKey, 3, " "));
    inhouseFileDetail.setAccountNo(
        StringUtils.rightPad(paymentRealRun.getPayeeBankAccountNo(), 15, " "));
    inhouseFileDetail.setTranCode("C");
    String transferAmt =
        String.format(
            "%.2f",
            paymentRealRun
                .getInvoiceAmountPaid()
                .subtract(
                    paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
    inhouseFileDetail.setAmountValue(
        paymentRealRun
            .getInvoiceAmountPaid()
            .subtract(paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo())));
    inhouseFileDetail.setAmount(StringUtils.leftPad(transferAmt.replace(".", ""), 13, "0"));
    String serviceType =
        paymentType.equalsIgnoreCase("D1")
                || paymentType.equalsIgnoreCase("D2")
                || paymentType.equalsIgnoreCase("D4")
            ? "02"
            : "14";
    inhouseFileDetail.setServiceType(serviceType);
    inhouseFileDetail.setStatus("9");
    inhouseFileDetail.setBatch(batchNum);
    inhouseFileDetail.setRecId(StringUtils.rightPad(paymentRealRun.getVendorCode(), 10, " "));
    String refNum =
        paymentRealRun.getPayingCompanyCode().substring(0, 4)
            + paymentRealRun.getPaymentDocumentNo()
            + paymentRealRun.getPaymentFiscalYear();
    inhouseFileDetail.setRefNum(StringUtils.rightPad(refNum, 18));
    String ref1 =
        Util.dateToStringPattern_ddMMyy(paymentRealRun.getPaymentDate())
            + paymentRealRun.getPaymentName()
            + paymentRealRun.getFiArea();
    inhouseFileDetail.setRef1(StringUtils.rightPad(ref1, 15));
    String chkReg =
        "A".equalsIgnoreCase(String.valueOf(paymentRealRun.getVendorCode().charAt(0)))
                || "V".equalsIgnoreCase(String.valueOf(paymentRealRun.getVendorCode().charAt(0)))
            ? "N"
            : "Y";
    inhouseFileDetail.setChkReg(chkReg);
    inhouseFileDetail.setVendorTaxId(
        StringUtils.rightPad(paymentRealRun.getVendorTaxId(), 13, " "));
    inhouseFileDetail.setUserRef(StringUtils.leftPad(userTref, 34, "0"));
    inhouseFileDetail.setCrValue(
        paymentRealRun.getCreditMemo().subtract(paymentRealRun.getWtxCreditMemo()));
    return inhouseFileDetail;
  }

  private InhouseFileTrailer generateInhouseFileTrailer(
      BankCode bankCode,
      String fileType,
      String payeeBankKey,
      String paymentMethod,
      int numDr,
      BigDecimal amtDr,
      int numCr,
      BigDecimal amtCr,
      int seqNo) {
    String bankKey = payeeBankKey.substring(0, 3);
    InhouseFileTrailer inhouseFileTrailer = new InhouseFileTrailer();
    inhouseFileTrailer.setFileType(fileType);
    inhouseFileTrailer.setPaymentMethod(paymentMethod);
    inhouseFileTrailer.setNumDr(numDr);
    inhouseFileTrailer.setAmtDr(amtDr);
    inhouseFileTrailer.setNumCr(numCr);
    inhouseFileTrailer.setAmtCr(amtCr);
    inhouseFileTrailer.setRecType("T");
    inhouseFileTrailer.setSeqNo(StringUtils.leftPad(String.valueOf(seqNo), 6, "0"));
    inhouseFileTrailer.setBankCode(bankKey);
    inhouseFileTrailer.setCompAccNo(bankCode.getAccountNo());
    inhouseFileTrailer.setNumberDr(StringUtils.leftPad(String.valueOf(numDr), 7, "0"));
    String transferAmtDr = String.format("%.2f", amtDr);
    inhouseFileTrailer.setTotalDr(StringUtils.leftPad(transferAmtDr.replace(".", ""), 18, "0"));
    inhouseFileTrailer.setNumberCr(StringUtils.leftPad(String.valueOf(numCr), 7, "0"));
    String transferAmtCr = String.format("%.2f", amtCr);
    inhouseFileTrailer.setTotalCr(StringUtils.leftPad(transferAmtCr.replace(".", ""), 18, "0"));
    inhouseFileTrailer.setFiller(StringUtils.leftPad("", 69, "0"));

    return inhouseFileTrailer;
  }

  private SwiftFile generateSwiftLevelOne(
      List<ProposalLog> proposalLogs,
      GenerateFileAlias generateFileAlias,
      String paymentType,
      Timestamp swiftDate,
      Timestamp paymentDate,
      String paymentName,
      Long fileName,
      Long runningNo,
      boolean isTestRun) {
    String effectiveDate = Util.dateToStringPattern_ddMMyyyy(swiftDate);
    BigDecimal setAmount =
        proposalLogs.stream()
            .map(ProposalLog::getAmount) // map
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    String accountName = "";
    String paymentMethod = "";
    if ("D1".equalsIgnoreCase(paymentType)) {
      accountName = "SALARY ACCOUNT";
      paymentMethod = "1";
    } else if ("D2".equalsIgnoreCase(paymentType)) {
      accountName = "PENSION ACCOUNT";
      paymentMethod = "2";
    } else if ("D3".equalsIgnoreCase(paymentType)) {
      accountName = "VENDOR ACCOUNT";
      paymentMethod = "3";
    } else if ("D4".equalsIgnoreCase(paymentType)) {
      accountName = "MEDICARE ACCOUNT";
      paymentMethod = "4";
    } else if ("D5".equalsIgnoreCase(paymentType)) {
      accountName = "AGENCY ACCOUNT";
      paymentMethod = "5";
    }

    SwiftFileLog swiftFileLog = new SwiftFileLog();
    swiftFileLog.setPaymentDate(paymentDate);
    swiftFileLog.setPaymentName(paymentName);
    swiftFileLog.setVendor("BOT");
    swiftFileLog.setBankKey("");
    swiftFileLog.setBankAccountNo("");
    swiftFileLog.setPaymentMethod(paymentMethod);
    swiftFileLog.setPayingCompCode("");
    swiftFileLog.setPaymentDocNo("");
    swiftFileLog.setPaymentYear("");
    swiftFileLog.setCompCode("99999");
    swiftFileLog.setInvDocNo("");
    swiftFileLog.setFiscalYear("");
    swiftFileLog.setFiArea("");
    swiftFileLog.setTransferLevel("1");
    swiftFileLog.setFee(Util.getBigDecimal(0));
    swiftFileLog.setCreditMemo(Util.getBigDecimal(0));

    SwiftFile swiftFile = new SwiftFile();
    swiftFile.setFileName(
        !isTestRun
            ? "S"
                + effectiveDate
                + getRound()
                + StringUtils.leftPad(String.valueOf(fileName), 6, "0")
            : "S" + effectiveDate + getRound() + "$$$$$$");
    swiftFile.setSendRef(String.valueOf(runningNo));
    swiftFile.setBankCode("CRED");
    swiftFile.setTransferType("RFT");
    swiftFile.setValueDate(Util.timestampToString(swiftDate));
    swiftFile.setCurrency("THB");
    swiftFile.setSetAmount(setAmount);
    swiftFile.setPayAccount(paymentType);
    swiftFile.setOrdAcct("");
    swiftFile.setOrdName1("99999");
    swiftFile.setOrdName2("");
    swiftFile.setOrdName3("");
    swiftFile.setOrdName4("");
    swiftFile.setSendCode("D");
    swiftFile.setSendAcct(this.account.get("TR2").getAccountNo());
    swiftFile.setRecCode("C");
    swiftFile.setRecAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setRecInsti("CGDXTHB1");
    swiftFile.setBenAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setBenName1(accountName);
    swiftFile.setBenName2("");
    swiftFile.setBenName3("");
    swiftFile.setBenName4("");
    swiftFile.setDetailCharg("SHA");
    swiftFile.setSendToRec1("");
    swiftFile.setSendToRec2("");
    swiftFile.setSendToRec3("");
    swiftFile.setSendToRec4("");
    swiftFile.setRegalRep1("");
    swiftFile.setRegalRep2("");
    swiftFile.setProposal(isTestRun);
    swiftFile.setSwiftFileLog(swiftFileLog);
    return swiftFile;
  }

  private SwiftFile generateSwiftLevelTwo(
      List<ProposalLog> proposalLogs,
      GenerateFileAlias generateFileAlias,
      String paymentType,
      Timestamp swiftDate,
      Timestamp paymentDate,
      String paymentName,
      Long fileName,
      Long runningNo,
      boolean isTestRun) {
    String effectiveDate = Util.dateToStringPattern_ddMMyyyy(swiftDate);
    BigDecimal setAmount =
        proposalLogs.stream()
            .filter(
                proposalLog ->
                    "SMART".equalsIgnoreCase(proposalLog.getFileType())
                        || "PAIN001".equalsIgnoreCase(proposalLog.getFileType()))
            .map(ProposalLog::getAmount) // map
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    String accountName = "";
    String paymentMethod = "";
    if ("D1".equalsIgnoreCase(paymentType)) {
      accountName = "SALARY ACCOUNT";
      paymentMethod = "1";
    } else if ("D2".equalsIgnoreCase(paymentType)) {
      accountName = "PENSION ACCOUNT";
      paymentMethod = "2";
    } else if ("D3".equalsIgnoreCase(paymentType)) {
      accountName = "VENDOR ACCOUNT";
      paymentMethod = "3";
    } else if ("D4".equalsIgnoreCase(paymentType)) {
      accountName = "MEDICARE ACCOUNT";
      paymentMethod = "4";
    } else if ("D5".equalsIgnoreCase(paymentType)) {
      accountName = "AGENCY ACCOUNT";
      paymentMethod = "5";
    }

    SwiftFileLog swiftFileLog = new SwiftFileLog();
    swiftFileLog.setPaymentDate(paymentDate);
    swiftFileLog.setPaymentName(paymentName);
    swiftFileLog.setVendor("BOT");
    swiftFileLog.setBankKey("");
    swiftFileLog.setBankAccountNo("");
    swiftFileLog.setPaymentMethod(paymentMethod);
    swiftFileLog.setPayingCompCode("");
    swiftFileLog.setPaymentDocNo("");
    swiftFileLog.setPaymentYear("");
    swiftFileLog.setCompCode("99999");
    swiftFileLog.setInvDocNo("");
    swiftFileLog.setFiscalYear("");
    swiftFileLog.setFiArea("");
    swiftFileLog.setTransferLevel("2");
    swiftFileLog.setFee(Util.getBigDecimal(0));
    swiftFileLog.setCreditMemo(Util.getBigDecimal(0));

    SwiftFile swiftFile = new SwiftFile();
    swiftFile.setFileName(
        !isTestRun
            ? "S"
                + effectiveDate
                + getRound()
                + StringUtils.leftPad(String.valueOf(fileName), 6, "0")
            : "S" + effectiveDate + getRound() + "$$$$$$");
    swiftFile.setSendRef(String.valueOf(runningNo));
    swiftFile.setBankCode("CRED");
    swiftFile.setTransferType("RFT");
    swiftFile.setValueDate(Util.timestampToString(swiftDate));
    swiftFile.setCurrency("THB");
    swiftFile.setSetAmount(setAmount);
    swiftFile.setPayAccount(paymentType);
    swiftFile.setOrdAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setOrdName1("99999");
    swiftFile.setOrdName2("");
    swiftFile.setOrdName3("");
    swiftFile.setOrdName4("");
    swiftFile.setSendCode("D");
    swiftFile.setSendAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setRecCode("C");
    swiftFile.setRecAcct(this.account.get("CA").getAccountNo());
    swiftFile.setRecInsti("BOTHTHB1CAT");
    swiftFile.setBenAcct(this.account.get("CA").getAccountNo());
    swiftFile.setBenName1("BOT DEPOSIT DEPARTMENT");
    swiftFile.setBenName2("");
    swiftFile.setBenName3("");
    swiftFile.setBenName4("");
    swiftFile.setDetailCharg("SHA");
    swiftFile.setSendToRec1("");
    swiftFile.setSendToRec2("");
    swiftFile.setSendToRec3("");
    swiftFile.setSendToRec4("");
    swiftFile.setRegalRep1("");
    swiftFile.setRegalRep2("");
    swiftFile.setProposal(isTestRun);
    //        swiftFile.setGenerateFileAlias(generateFileAlias);
    swiftFile.setSwiftFileLog(swiftFileLog);

    //        swiftFileLogService.save(swiftFileLog);
    //        swiftFileService.save(swiftFile);
    return swiftFile;
  }

  private SwiftFile generateSwiftLevelNineInhouseTMB(
      List<ProposalLog> proposalLogs,
      GenerateFileAlias generateFileAlias,
      Map<String, BankCode> bankCodeMap,
      String paymentType,
      Timestamp swiftDate,
      Timestamp paymentDate,
      String paymentName,
      String fileName,
      Long runningNo,
      boolean isGIRO,
      boolean isTestRun) {
    String effectiveDate = Util.dateToStringPattern_ddMMyyyy(swiftDate);
    List<ProposalLog> proposalLogsInhouse =
        proposalLogs.stream()
            .filter(
                proposalLog ->
                    "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                        && "011".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)))
            .collect(Collectors.toList());
    String bankKey = "";
    if (!proposalLogsInhouse.isEmpty()) {
      bankKey = proposalLogsInhouse.get(0).getBankKey();
    }
    BigDecimal setAmount =
        proposalLogs.stream()
            .filter(
                proposalLog ->
                    "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                        && "011".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)))
            .map(ProposalLog::getAmount) // map
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    String accountName = "";
    String paymentMethod = "";
    BankCode bankCode = bankCodeMap.get(bankKey.substring(0, 3));
    if ("D1".equalsIgnoreCase(paymentType)) {
      accountName = "SALARY ACCOUNT";
      paymentMethod = "1";
    } else if ("D2".equalsIgnoreCase(paymentType)) {
      accountName = "PENSION ACCOUNT";
      paymentMethod = "2";
    } else if ("D3".equalsIgnoreCase(paymentType)) {
      accountName = "VENDOR ACCOUNT";
      paymentMethod = "3";
    } else if ("D4".equalsIgnoreCase(paymentType)) {
      accountName = "MEDICARE ACCOUNT";
      paymentMethod = "4";
    } else if ("D5".equalsIgnoreCase(paymentType)) {
      accountName = "AGENCY ACCOUNT";
      paymentMethod = "5";
    }

    SwiftFileLog swiftFileLog = new SwiftFileLog();
    swiftFileLog.setPaymentDate(paymentDate);
    swiftFileLog.setPaymentName(paymentName);
    swiftFileLog.setVendor("0000000000");
    swiftFileLog.setBankKey(bankKey.substring(0, 3));
    swiftFileLog.setBankAccountNo(bankCode.getAccountNo());
    swiftFileLog.setPaymentMethod(paymentMethod);
    swiftFileLog.setPayingCompCode("");
    swiftFileLog.setPaymentDocNo("");
    swiftFileLog.setPaymentYear("");
    swiftFileLog.setCompCode("99999");
    swiftFileLog.setInvDocNo("");
    swiftFileLog.setFiscalYear("");
    swiftFileLog.setFiArea("");
    swiftFileLog.setTransferLevel("9");
    swiftFileLog.setFee(Util.getBigDecimal(0));
    swiftFileLog.setCreditMemo(Util.getBigDecimal(0));

    SwiftFile swiftFile = new SwiftFile();
    swiftFile.setFileName(fileName);
    swiftFile.setSendRef(String.valueOf(runningNo));
    swiftFile.setBankCode("CRED");
    swiftFile.setTransferType("RFT");
    swiftFile.setValueDate(Util.timestampToString(swiftDate));
    swiftFile.setCurrency("THB");
    swiftFile.setSetAmount(setAmount);
    swiftFile.setPayAccount(paymentType);
    swiftFile.setOrdAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setOrdName1(isGIRO ? "CGD" : "99999");
    swiftFile.setOrdName2("");
    swiftFile.setOrdName3("");
    swiftFile.setOrdName4("");
    swiftFile.setSendCode("D");
    swiftFile.setSendAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setRecCode("C");
    swiftFile.setRecAcct(bankCode.getAccountNo());
    swiftFile.setRecInsti(bankCode.getIncstCode());
    swiftFile.setBenAcct(bankCode.getAccountNo());
    swiftFile.setBenName1(bankCode.getBankShortName()); // TODO ref doc 6.2.4
    swiftFile.setBenName2("");
    swiftFile.setBenName3("");
    swiftFile.setBenName4("");
    swiftFile.setDetailCharg("SHA");
    swiftFile.setSendToRec1("000000000099990000");
    swiftFile.setSendToRec2(Util.dateToStringPattern_ddMMyyyy(paymentDate) + paymentName);
    swiftFile.setSendToRec3("0000" + paymentType.substring(1));
    swiftFile.setSendToRec4("");
    swiftFile.setRegalRep1("PPNO");
    swiftFile.setRegalRep2("");
    swiftFile.setProposal(isTestRun);
    //        swiftFile.setGenerateFileAlias(generateFileAlias);
    swiftFile.setSwiftFileLog(swiftFileLog);

    //        swiftFileLogService.save(swiftFileLog);
    //        swiftFileService.save(swiftFile);
    return swiftFile;
  }

  private SwiftFile generateSwiftLevelNineInhouseGSB(
      List<ProposalLog> proposalLogs,
      GenerateFileAlias generateFileAlias,
      Map<String, BankCode> bankCodeMap,
      String paymentType,
      Timestamp swiftDate,
      Timestamp paymentDate,
      String paymentName,
      String fileName,
      Long runningNo,
      boolean isGIRO,
      boolean isTestRun) {
    String effectiveDate = Util.dateToStringPattern_ddMMyyyy(swiftDate);
    List<ProposalLog> proposalLogsInhouse =
        proposalLogs.stream()
            .filter(
                proposalLog ->
                    "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                        && "030".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)))
            .collect(Collectors.toList());
    String bankKey = "";
    if (!proposalLogsInhouse.isEmpty()) {
      bankKey = proposalLogsInhouse.get(0).getBankKey();
    }
    BigDecimal setAmount =
        proposalLogs.stream()
            .filter(
                proposalLog ->
                    "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                        && "030".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)))
            .map(ProposalLog::getAmount) // map
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    String accountName = "";
    String paymentMethod = "";
    BankCode bankCode = bankCodeMap.get(bankKey.substring(0, 3));
    if ("D1".equalsIgnoreCase(paymentType)) {
      accountName = "SALARY ACCOUNT";
      paymentMethod = "1";
    } else if ("D2".equalsIgnoreCase(paymentType)) {
      accountName = "PENSION ACCOUNT";
      paymentMethod = "2";
    } else if ("D3".equalsIgnoreCase(paymentType)) {
      accountName = "VENDOR ACCOUNT";
      paymentMethod = "3";
    } else if ("D4".equalsIgnoreCase(paymentType)) {
      accountName = "MEDICARE ACCOUNT";
      paymentMethod = "4";
    } else if ("D5".equalsIgnoreCase(paymentType)) {
      accountName = "AGENCY ACCOUNT";
      paymentMethod = "5";
    }

    SwiftFileLog swiftFileLog = new SwiftFileLog();
    swiftFileLog.setPaymentDate(paymentDate);
    swiftFileLog.setPaymentName(paymentName);
    swiftFileLog.setVendor("0000000000");
    swiftFileLog.setBankKey(bankKey.substring(0, 3));
    swiftFileLog.setBankAccountNo(bankCode.getAccountNo());
    swiftFileLog.setPaymentMethod(paymentMethod);
    swiftFileLog.setPayingCompCode("");
    swiftFileLog.setPaymentDocNo("");
    swiftFileLog.setPaymentYear("");
    swiftFileLog.setCompCode("99999");
    swiftFileLog.setInvDocNo("");
    swiftFileLog.setFiscalYear("");
    swiftFileLog.setFiArea("");
    swiftFileLog.setTransferLevel("9");
    swiftFileLog.setFee(Util.getBigDecimal(0));
    swiftFileLog.setCreditMemo(Util.getBigDecimal(0));

    SwiftFile swiftFile = new SwiftFile();
    swiftFile.setFileName(fileName);
    swiftFile.setSendRef(String.valueOf(runningNo));
    swiftFile.setBankCode("CRED");
    swiftFile.setTransferType("RFT");
    swiftFile.setValueDate(Util.timestampToString(swiftDate));
    swiftFile.setCurrency("THB");
    swiftFile.setSetAmount(setAmount);
    swiftFile.setPayAccount(paymentType);
    swiftFile.setOrdAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setOrdName1(isGIRO ? "CGD" : "99999");
    swiftFile.setOrdName2("");
    swiftFile.setOrdName3("");
    swiftFile.setOrdName4("");
    swiftFile.setSendCode("D");
    swiftFile.setSendAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setRecCode("C");
    swiftFile.setRecAcct(bankCode.getAccountNo());
    swiftFile.setRecInsti(bankCode.getIncstCode());
    swiftFile.setBenAcct(bankCode.getAccountNo());
    swiftFile.setBenName1(bankCode.getBankShortName()); // TODO ref doc 6.2.4
    swiftFile.setBenName2("");
    swiftFile.setBenName3("");
    swiftFile.setBenName4("");
    swiftFile.setDetailCharg("SHA");
    swiftFile.setSendToRec1("000000000099990000");
    swiftFile.setSendToRec2(Util.dateToStringPattern_ddMMyyyy(paymentDate) + paymentName);
    swiftFile.setSendToRec3("0000" + paymentType.substring(1));
    swiftFile.setSendToRec4("");
    swiftFile.setRegalRep1("PPNO");
    swiftFile.setRegalRep2("");
    swiftFile.setProposal(isTestRun);
    swiftFile.setSwiftFileLog(swiftFileLog);
    return swiftFile;
  }

  private SwiftFile generateSwiftLevelNineInhouseBAC(
      List<ProposalLog> proposalLogs,
      GenerateFileAlias generateFileAlias,
      Map<String, BankCode> bankCodeMap,
      String paymentType,
      Timestamp swiftDate,
      Timestamp paymentDate,
      String paymentName,
      String fileName,
      Long runningNo,
      boolean isGIRO,
      boolean isTestRun) {
    String effectiveDate = Util.dateToStringPattern_ddMMyyyy(swiftDate);
    List<ProposalLog> proposalLogsInhouse =
        proposalLogs.stream()
            .filter(
                proposalLog ->
                    "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                        && "034".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)))
            .collect(Collectors.toList());
    String bankKey = "";
    if (!proposalLogsInhouse.isEmpty()) {
      bankKey = proposalLogsInhouse.get(0).getBankKey();
    }
    BigDecimal setAmount =
        proposalLogs.stream()
            .filter(
                proposalLog ->
                    "INHOU".equalsIgnoreCase(proposalLog.getFileType())
                        && "034".equalsIgnoreCase(proposalLog.getBankKey().substring(0, 3)))
            .map(ProposalLog::getAmount) // map
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    String accountName = "";
    String paymentMethod = "";
    BankCode bankCode = bankCodeMap.get(bankKey.substring(0, 3));
    if ("D1".equalsIgnoreCase(paymentType)) {
      accountName = "SALARY ACCOUNT";
      paymentMethod = "1";
    } else if ("D2".equalsIgnoreCase(paymentType)) {
      accountName = "PENSION ACCOUNT";
      paymentMethod = "2";
    } else if ("D3".equalsIgnoreCase(paymentType)) {
      accountName = "VENDOR ACCOUNT";
      paymentMethod = "3";
    } else if ("D4".equalsIgnoreCase(paymentType)) {
      accountName = "MEDICARE ACCOUNT";
      paymentMethod = "4";
    } else if ("D5".equalsIgnoreCase(paymentType)) {
      accountName = "AGENCY ACCOUNT";
      paymentMethod = "5";
    }

    SwiftFileLog swiftFileLog = new SwiftFileLog();
    swiftFileLog.setPaymentDate(paymentDate);
    swiftFileLog.setPaymentName(paymentName);
    swiftFileLog.setVendor("0000000000");
    swiftFileLog.setBankKey(bankKey.substring(0, 3));
    swiftFileLog.setBankAccountNo(bankCode.getAccountNo());
    swiftFileLog.setPaymentMethod(paymentMethod);
    swiftFileLog.setPayingCompCode("");
    swiftFileLog.setPaymentDocNo("");
    swiftFileLog.setPaymentYear("");
    swiftFileLog.setCompCode("99999");
    swiftFileLog.setInvDocNo("");
    swiftFileLog.setFiscalYear("");
    swiftFileLog.setFiArea("");
    swiftFileLog.setTransferLevel("9");
    swiftFileLog.setFee(Util.getBigDecimal(0));
    swiftFileLog.setCreditMemo(Util.getBigDecimal(0));

    SwiftFile swiftFile = new SwiftFile();
    swiftFile.setFileName(fileName);
    swiftFile.setSendRef(String.valueOf(runningNo));
    swiftFile.setBankCode("CRED");
    swiftFile.setTransferType("RFT");
    swiftFile.setValueDate(Util.timestampToString(swiftDate));
    swiftFile.setCurrency("THB");
    swiftFile.setSetAmount(setAmount);
    swiftFile.setPayAccount(paymentType);
    swiftFile.setOrdAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setOrdName1(isGIRO ? "CGD" : "99999");
    swiftFile.setOrdName2("");
    swiftFile.setOrdName3("");
    swiftFile.setOrdName4("");
    swiftFile.setSendCode("D");
    swiftFile.setSendAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setRecCode("C");
    swiftFile.setRecAcct(bankCode.getAccountNo());
    swiftFile.setRecInsti(bankCode.getIncstCode());
    swiftFile.setBenAcct(bankCode.getAccountNo());
    swiftFile.setBenName1(bankCode.getBankShortName()); // TODO ref doc 6.2.4
    swiftFile.setBenName2("");
    swiftFile.setBenName3("");
    swiftFile.setBenName4("");
    swiftFile.setDetailCharg("SHA");
    swiftFile.setSendToRec1("000000000099990000");
    swiftFile.setSendToRec2(Util.dateToStringPattern_ddMMyyyy(paymentDate) + paymentName);
    swiftFile.setSendToRec3("0000" + paymentType.substring(1));
    swiftFile.setSendToRec4("");
    swiftFile.setRegalRep1("PPNO");
    swiftFile.setRegalRep2("");
    swiftFile.setProposal(isTestRun);
    swiftFile.setSwiftFileLog(swiftFileLog);
    return swiftFile;
  }

  private SwiftFile generateSwiftLevelNineGIRO(
      List<ProposalLog> proposalLogs,
      GenerateFileAlias generateFileAlias,
      Map<String, BankCode> bankCodeMap,
      String paymentType,
      Timestamp swiftDate,
      Timestamp paymentDate,
      String paymentName,
      String fileName,
      Long runningNo,
      boolean isGIRO,
      boolean isTestRun) {
    String effectiveDate = Util.dateToStringPattern_ddMMyyyy(swiftDate);
    List<ProposalLog> proposalLogsGIRO =
        proposalLogs.stream()
            .filter(proposalLog -> "GIRO".equalsIgnoreCase(proposalLog.getFileType()))
            .collect(Collectors.toList());
    String bankKey = "";
    if (!proposalLogsGIRO.isEmpty()) {
      bankKey = proposalLogsGIRO.get(0).getBankKey();
    }
    BigDecimal setAmount =
        proposalLogs.stream()
            .filter(proposalLog -> "GIRO".equalsIgnoreCase(proposalLog.getFileType()))
            .map(ProposalLog::getAmount) // map
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    String accountName = "";
    String paymentMethod = "";
    BankCode bankCode = bankCodeMap.get(bankKey.substring(0, 3));
    if ("D1".equalsIgnoreCase(paymentType)) {
      accountName = "SALARY ACCOUNT";
      paymentMethod = "1";
    } else if ("D2".equalsIgnoreCase(paymentType)) {
      accountName = "PENSION ACCOUNT";
      paymentMethod = "2";
    } else if ("D3".equalsIgnoreCase(paymentType)) {
      accountName = "VENDOR ACCOUNT";
      paymentMethod = "3";
    } else if ("D4".equalsIgnoreCase(paymentType)) {
      accountName = "MEDICARE ACCOUNT";
      paymentMethod = "4";
    } else if ("D5".equalsIgnoreCase(paymentType)) {
      accountName = "AGENCY ACCOUNT";
      paymentMethod = "5";
    }

    SwiftFileLog swiftFileLog = new SwiftFileLog();
    swiftFileLog.setPaymentDate(paymentDate);
    swiftFileLog.setPaymentName(paymentName);
    swiftFileLog.setVendor("0000000000");
    swiftFileLog.setBankKey(bankKey.substring(0, 3));
    swiftFileLog.setBankAccountNo(bankCode.getAccountNo());
    swiftFileLog.setPaymentMethod(paymentMethod);
    swiftFileLog.setPayingCompCode("");
    swiftFileLog.setPaymentDocNo("");
    swiftFileLog.setPaymentYear("");
    swiftFileLog.setCompCode("99999");
    swiftFileLog.setInvDocNo("");
    swiftFileLog.setFiscalYear("");
    swiftFileLog.setFiArea("");
    swiftFileLog.setTransferLevel("9");
    swiftFileLog.setFee(Util.getBigDecimal(0));
    swiftFileLog.setCreditMemo(Util.getBigDecimal(0));

    SwiftFile swiftFile = new SwiftFile();
    swiftFile.setFileName(fileName);
    swiftFile.setSendRef(String.valueOf(runningNo));
    swiftFile.setBankCode("CRED");
    swiftFile.setTransferType("RFT");
    swiftFile.setValueDate(Util.timestampToString(swiftDate));
    swiftFile.setCurrency("THB");
    swiftFile.setSetAmount(setAmount);
    swiftFile.setPayAccount(paymentType);
    swiftFile.setOrdAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setOrdName1(isGIRO ? "CGD" : "99999");
    swiftFile.setOrdName2("");
    swiftFile.setOrdName3("");
    swiftFile.setOrdName4("");
    swiftFile.setSendCode("D");
    swiftFile.setSendAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setRecCode("C");
    swiftFile.setRecAcct(bankCode.getAccountNo());
    swiftFile.setRecInsti(bankCode.getIncstCode());
    swiftFile.setBenAcct(bankCode.getAccountNo());
    swiftFile.setBenName1(bankCode.getBankShortName()); // TODO ref doc 6.2.4
    swiftFile.setBenName2("");
    swiftFile.setBenName3("");
    swiftFile.setBenName4("");
    swiftFile.setDetailCharg("SHA");
    swiftFile.setSendToRec1("000000000099990000");
    swiftFile.setSendToRec2(Util.dateToStringPattern_ddMMyyyy(paymentDate) + paymentName);
    swiftFile.setSendToRec3("0000" + paymentType.substring(1));
    swiftFile.setSendToRec4("");
    swiftFile.setRegalRep1("PPNO");
    swiftFile.setRegalRep2("");
    swiftFile.setProposal(isTestRun);
    swiftFile.setSwiftFileLog(swiftFileLog);
    return swiftFile;
  }

  private SwiftFile generateSwiftLevelNine(
      PaymentRealRun paymentRealRun,
      GenerateFileAlias generateFileAlias,
      Map<String, BankCode> bankCodeMap,
      String paymentType,
      Timestamp swiftDate,
      Timestamp paymentDate,
      String paymentName,
      String bankKey,
      Long running,
      boolean isGIRO,
      boolean isTestRun) {
    String effectiveDate = Util.dateToStringPattern_ddMMyyyy(swiftDate);

    String accountName = "";
    String paymentMethod = "";
    BankCode bankCode = bankCodeMap.get(bankKey);
    if ("D1".equalsIgnoreCase(paymentType)) {
      accountName = "SALARY ACCOUNT";
      paymentMethod = "1";
    } else if ("D2".equalsIgnoreCase(paymentType)) {
      accountName = "PENSION ACCOUNT";
      paymentMethod = "2";
    } else if ("D3".equalsIgnoreCase(paymentType)) {
      accountName = "VENDOR ACCOUNT";
      paymentMethod = "3";
    } else if ("D4".equalsIgnoreCase(paymentType)) {
      accountName = "MEDICARE ACCOUNT";
      paymentMethod = "4";
    } else if ("D5".equalsIgnoreCase(paymentType)) {
      accountName = "AGENCY ACCOUNT";
      paymentMethod = "5";
    }

    SwiftFileLog swiftFileLog = new SwiftFileLog();
    swiftFileLog.setPaymentDate(paymentDate);
    swiftFileLog.setPaymentName(paymentName);
    swiftFileLog.setVendor("0000000000");
    swiftFileLog.setBankKey(bankKey);
    swiftFileLog.setBankAccountNo(bankCode.getAccountNo());
    swiftFileLog.setPaymentMethod(paymentMethod);
    swiftFileLog.setPayingCompCode("");
    swiftFileLog.setPaymentDocNo("");
    swiftFileLog.setPaymentYear("");
    swiftFileLog.setCompCode("99999");
    swiftFileLog.setInvDocNo("");
    swiftFileLog.setFiscalYear("");
    swiftFileLog.setFiArea("");
    swiftFileLog.setTransferLevel("9");
    swiftFileLog.setFee(Util.getBigDecimal(0));
    swiftFileLog.setCreditMemo(Util.getBigDecimal(0));

    SwiftFile swiftFile = new SwiftFile();
    swiftFile.setSendRef(
        !isTestRun
            ? "S"
                + effectiveDate
                + getRound()
                + StringUtils.leftPad(String.valueOf(running), 6, "0")
            : "S" + effectiveDate + getRound() + "$$$$$$");
    swiftFile.setBankCode("CRED");
    swiftFile.setTransferType("RFT");
    swiftFile.setValueDate(Util.timestampToString(swiftDate));
    swiftFile.setCurrency("THB");
    swiftFile.setSetAmount(paymentRealRun.getAmountPaid());
    swiftFile.setOrdAcct(paymentType);
    swiftFile.setOrdName1(isGIRO ? "CGD" : "99999");
    swiftFile.setOrdName2("");
    swiftFile.setOrdName3("");
    swiftFile.setOrdName4("");
    swiftFile.setSendCode("D");
    swiftFile.setSendAcct(this.account.get(paymentType).getAccountNo());
    swiftFile.setRecCode("C");
    swiftFile.setRecAcct(bankCode.getAccountNo());
    swiftFile.setRecInsti(bankCode.getIncstCode());
    swiftFile.setBenAcct(bankCode.getAccountNo());
    swiftFile.setBenName1("0000000000"); // TODO ref doc 6.2.4
    swiftFile.setBenName2("");
    swiftFile.setBenName3("");
    swiftFile.setBenName4("");
    swiftFile.setDetailCharg("SHA");
    swiftFile.setSendToRec1("");
    swiftFile.setSendToRec2("");
    swiftFile.setSendToRec3("");
    swiftFile.setSendToRec4("");
    swiftFile.setRegalRep1("");
    swiftFile.setRegalRep2("");
    swiftFile.setProposal(isTestRun);
    swiftFile.setSwiftFileLog(swiftFileLog);
    return swiftFile;
  }

  private String groupByMultipleKey(PrepareRunDocument prepareRunDocument) {
    return prepareRunDocument.getVendorCode()
        + ":"
        + prepareRunDocument.getPayeeBankKey()
        + ":"
        + prepareRunDocument.getBankAccountNo();
  }

  private String groupByMultipleKey(SmartFileDetail smartFileDetail) {
    SmartFileLog smartFileLog = smartFileDetail.getSmartFileLog();
    return smartFileLog.getVendor()
        + ":"
        + smartFileLog.getBankKey()
        + ":"
        + smartFileLog.getBankAccountNo();
  }

  private String groupByMultipleKey(SwiftFile swiftFile) {
    SwiftFileLog swiftFileLog = swiftFile.getSwiftFileLog();
    return swiftFileLog.getVendor()
        + ":"
        + swiftFileLog.getBankKey()
        + ":"
        + swiftFileLog.getBankAccountNo();
  }

  private String groupByMultipleKey(GIROFileDetail giroFileDetail) {
    return giroFileDetail.getBankKey() + ":" + giroFileDetail.getBankAccountNo();
  }

  private String groupByMultipleKey(InhouseFileDetail inhouseFileDetail) {
    return inhouseFileDetail.getBankKey() + ":" + inhouseFileDetail.getBankAccountNo();
  }

  private void updateRegen(GenerateFileAlias generateFileAlias, String fileType, String fileName, Long refRunning, boolean isAll, String fileNameLevel1) {
    if (isAll) {
      proposalLogHeaderService.updateRegen(generateFileAlias.getGenerateFileDate(), generateFileAlias.getGenerateFileName(), refRunning);
      proposalLogService.updateRegenLevel1(
              generateFileAlias.getGenerateFileDate(),
              generateFileAlias.getGenerateFileName(),
              fileNameLevel1);
    }
    proposalLogService.updateRegen(
        generateFileAlias.getGenerateFileDate(),
        generateFileAlias.getGenerateFileName(),
        fileType,
        fileName);
    proposalLogSumService.updateRegen(
        generateFileAlias.getGenerateFileDate(),
        generateFileAlias.getGenerateFileName(),
        fileType,
        fileName);
  }
}
