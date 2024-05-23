package th.com.bloomcode.paymentservice.model.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class PrepareRealRunDocument extends BaseModel {

  public static final String COLUMN_NAME_PAYMENT_PROCESS_ID = "PAYMENT_PROCESS_ID";
  public static final String COLUMN_NAME_ACCOUNT_TYPE = "ACCOUNT_TYPE";
  public static final String COLUMN_NAME_ASSET_NO = "ASSET_NO";
  public static final String COLUMN_NAME_ASSET_SUB_NO = "ASSET_SUB_NO";
  public static final String COLUMN_NAME_ASSIGNMENT = "ASSIGNMENT";
  public static final String COLUMN_NAME_BANK_ACCOUNT_NO = "BANK_ACCOUNT_NO";
  public static final String COLUMN_NAME_BR_DOCUMENT_NO = "BR_DOCUMENT_NO";
  public static final String COLUMN_NAME_BR_LINE = "BR_LINE";
  public static final String COLUMN_NAME_BUDGET_ACCOUNT = "BUDGET_ACCOUNT";
  public static final String COLUMN_NAME_BUDGET_ACTIVITY = "BUDGET_ACTIVITY";
  public static final String COLUMN_NAME_BUDGET_ACTIVITY_NAME = "BUDGET_ACTIVITY_NAME";
  public static final String COLUMN_NAME_IS_CHILD = "IS_CHILD";
  public static final String COLUMN_NAME_COST_CENTER = "COST_CENTER";
  public static final String COLUMN_NAME_COST_CENTER_NAME = "COST_CENTER_NAME";
  public static final String COLUMN_NAME_CURRENCY = "CURRENCY";
  public static final String COLUMN_NAME_DATE_ACCT = "DATE_ACCT";
  public static final String COLUMN_NAME_DATE_BASE_LINE = "DATE_BASE_LINE";
  public static final String COLUMN_NAME_DATE_DOC = "DATE_DOC";
  public static final String COLUMN_NAME_DR_CR = "DR_CR";
  public static final String COLUMN_NAME_ERROR_CODE = "ERROR_CODE";
  public static final String COLUMN_NAME_FI_AREA = "FI_AREA";
  public static final String COLUMN_NAME_FUND_CENTER = "FUND_CENTER";
  public static final String COLUMN_NAME_FUND_CENTER_NAME = "FUND_CENTER_NAME";
  public static final String COLUMN_NAME_FUND_SOURCE = "FUND_SOURCE";
  public static final String COLUMN_NAME_FUND_SOURCE_NAME = "FUND_SOURCE_NAME";
  public static final String COLUMN_NAME_GL_ACCOUNT = "GL_ACCOUNT";
  public static final String COLUMN_NAME_GL_ACCOUNT_NAME = "GL_ACCOUNT_NAME";
  public static final String COLUMN_NAME_HEADER_REFERENCE = "HEADER_REFERENCE";
  public static final String COLUMN_NAME_HOUSE_BANK = "HOUSE_BANK";
  public static final String COLUMN_NAME_IDEM_STATUS = "IDEM_STATUS";
  public static final String COLUMN_NAME_INVOICE_AMOUNT = "INVOICE_AMOUNT";
  public static final String COLUMN_NAME_INVOICE_COMPANY_CODE = "INVOICE_COMPANY_CODE";
  public static final String COLUMN_NAME_INVOICE_COMPANY_NAME = "INVOICE_COMPANY_NAME";
  public static final String COLUMN_NAME_INVOICE_DOCUMENT_NO = "INVOICE_DOCUMENT_NO";
  public static final String COLUMN_NAME_INVOICE_DOCUMENT_TYPE = "INVOICE_DOCUMENT_TYPE";
  public static final String COLUMN_NAME_INVOICE_FISCAL_YEAR = "INVOICE_FISCAL_YEAR";
  public static final String COLUMN_NAME_INVOICE_PAYMENT_CENTER = "INVOICE_PAYMENT_CENTER";
  public static final String COLUMN_NAME_INVOICE_WTX_AMOUNT = "INVOICE_WTX_AMOUNT";
  public static final String COLUMN_NAME_INVOICE_WTX_AMOUNT_P = "INVOICE_WTX_AMOUNT_P";
  public static final String COLUMN_NAME_INVOICE_WTX_BASE = "INVOICE_WTX_BASE";
  public static final String COLUMN_NAME_INVOICE_WTX_BASE_P = "INVOICE_WTX_BASE_P";
  public static final String COLUMN_NAME_LINE = "LINE";
  public static final String COLUMN_NAME_LINE_ITEM_TEXT = "LINE_ITEM_TEXT";
  public static final String COLUMN_NAME_ORIGINAL_AMOUNT = "ORIGINAL_AMOUNT";
  public static final String COLUMN_NAME_ORIGINAL_COMPANY_CODE = "ORIGINAL_COMPANY_CODE";
  public static final String COLUMN_NAME_ORIGINAL_COMPANY_NAME = "ORIGINAL_COMPANY_NAME";
  public static final String COLUMN_NAME_ORIGINAL_DOCUMENT_NO = "ORIGINAL_DOCUMENT_NO";
  public static final String COLUMN_NAME_ORIGINAL_DOCUMENT_TYPE = "ORIGINAL_DOCUMENT_TYPE";
  public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";
  public static final String COLUMN_NAME_ORIGINAL_PAYMENT_CENTER = "ORIGINAL_PAYMENT_CENTER";
  public static final String COLUMN_NAME_ORIGINAL_WTX_AMOUNT = "ORIGINAL_WTX_AMOUNT";
  public static final String COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P = "ORIGINAL_WTX_AMOUNT_P";
  public static final String COLUMN_NAME_ORIGINAL_WTX_BASE = "ORIGINAL_WTX_BASE";
  public static final String COLUMN_NAME_ORIGINAL_WTX_BASE_P = "ORIGINAL_WTX_BASE_P";
  public static final String COLUMN_NAME_PARENT_COMPANY_CODE = "PARENT_COMPANY_CODE";
  public static final String COLUMN_NAME_PARENT_DOCUMENT_NO = "PARENT_DOCUMENT_NO";
  public static final String COLUMN_NAME_PARENT_FISCAL_YEAR = "PARENT_FISCAL_YEAR";
  public static final String COLUMN_NAME_PAYMENT_ALIAS_ID = "PAYMENT_ALIAS_ID";
  public static final String COLUMN_NAME_PAYMENT_BLOCK = "PAYMENT_BLOCK";
  public static final String COLUMN_NAME_PAYMENT_CENTER = "PAYMENT_CENTER";
  public static final String COLUMN_NAME_PAYMENT_COMPANY_CODE = "PAYMENT_COMPANY_CODE";
  public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
  public static final String COLUMN_NAME_PAYMENT_DATE_ACCT = "PAYMENT_DATE_ACCT";
  public static final String COLUMN_NAME_PAYMENT_DOCUMENT_NO = "PAYMENT_DOCUMENT_NO";
  public static final String COLUMN_NAME_PAYMENT_FISCAL_YEAR = "PAYMENT_FISCAL_YEAR";
  public static final String COLUMN_NAME_PAYMENT_METHOD = "PAYMENT_METHOD";
  public static final String COLUMN_NAME_PAYMENT_METHOD_NAME = "PAYMENT_METHOD_NAME";
  public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";
  public static final String COLUMN_NAME_PAYMENT_REFERENCE = "PAYMENT_REFERENCE";
  public static final String COLUMN_NAME_PAYMENT_TERM = "PAYMENT_TERM";
  public static final String COLUMN_NAME_PM_GROUP_DOC = "PM_GROUP_DOC";
  public static final String COLUMN_NAME_PM_GROUP_NO = "PM_GROUP_NO";
  public static final String COLUMN_NAME_PO_DOCUMENT_NO = "PO_DOCUMENT_NO";
  public static final String COLUMN_NAME_PO_LINE = "PO_LINE";
  public static final String COLUMN_NAME_POSTING_KEY = "POSTING_KEY";
  public static final String COLUMN_NAME_IS_PROPOSAL = "IS_PROPOSAL";
  public static final String COLUMN_NAME_IS_PROPOSAL_BLOCK = "IS_PROPOSAL_BLOCK";
  public static final String COLUMN_NAME_REFERENCE1 = "REFERENCE1";
  public static final String COLUMN_NAME_REFERENCE2 = "REFERENCE2";
  public static final String COLUMN_NAME_REFERENCE3 = "REFERENCE3";
  public static final String COLUMN_NAME_SPECIAL_GL = "SPECIAL_GL";
  public static final String COLUMN_NAME_SPECIAL_GL_NAME = "SPECIAL_GL_NAME";
  public static final String COLUMN_NAME_STATUS = "STATUS";
  public static final String COLUMN_NAME_TRADING_PARTNER = "TRADING_PARTNER";
  public static final String COLUMN_NAME_TRADING_PARTNER_NAME = "TRADING_PARTNER_NAME";
  public static final String COLUMN_NAME_CREATED = "CREATED";
  public static final String COLUMN_NAME_CREATED_BY = "CREATED_BY";
  public static final String COLUMN_NAME_UPDATED = "UPDATED";
  public static final String COLUMN_NAME_UPDATED_BY = "UPDATED_BY";
  public static final String COLUMN_NAME_INVOICE_AMOUNT_PAID = "INVOICE_AMOUNT_PAID";
  public static final String COLUMN_NAME_ORIGINAL_AMOUNT_PAID = "ORIGINAL_AMOUNT_PAID";
  public static final String COLUMN_NAME_FI_AREA_NAME = "FI_AREA_NAME";
  public static final String COLUMN_NAME_PAYMENT_COMPANY_NAME = "PAYMENT_COMPANY_NAME";
  public static final String COLUMN_NAME_REVERSE_PAYMENT_COMPANY_CODE =
      "REVERSE_PAYMENT_COMPANY_CODE";
  public static final String COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_NO =
      "REVERSE_PAYMENT_DOCUMENT_NO";
  public static final String COLUMN_NAME_REVERSE_PAYMENT_FISCAL_YEAR =
      "REVERSE_PAYMENT_FISCAL_YEAR";
  public static final String COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_TYPE =
      "REVERSE_PAYMENT_DOCUMENT_TYPE";
  public static final String COLUMN_NAME_PAYMENT_DOCUMENT_TYPE = "PAYMENT_DOCUMENT_TYPE";
  public static final String COLUMN_NAME_IDEM_UPDATE = "IDEM_UPDATE";
  public static final String COLUMN_NAME_INVOICE_WTX_AMOUNTP = "INVOICE_WTX_AMOUNTP";
  public static final String COLUMN_NAME_INVOICE_WTX_BASEP = "INVOICE_WTX_BASEP";
  public static final String COLUMN_NAME_ORIGINAL_WTX_AMOUNTP = "ORIGINAL_WTX_AMOUNTP";
  public static final String COLUMN_NAME_ORIGINAL_WTX_BASEP = "ORIGINAL_WTX_BASEP";
  public static final String COLUMN_NAME_SPECIALGL = "SPECIALGL";
  public static final String COLUMN_NAME_SPECIALGLNAME = "SPECIALGLNAME";
  public static final String COLUMN_NAME_IS_HAVE_CHILD = "IS_HAVE_CHILD";
  public static final String COLUMN_NAME_HAVE_CHILD = "HAVE_CHILD";
  public static final String COLUMN_NAME_CREDIT_MEMO = "CREDIT_MEMO";
  public static final String COLUMN_NAME_REVERSE_PAYMENT_STATUS = "REVERSE_PAYMENT_STATUS";
  public static final String COLUMN_NAME_WTX_CREDIT_MEMO = "WTX_CREDIT_MEMO";
  public static final String COLUMN_NAME_PAYMENT_INFORMATION_ID = "PAYMENT_INFORMATION_ID";
  public static final String COLUMN_NAME_ACCOUNT_HOLDER_NAME = "ACCOUNT_HOLDER_NAME";
  public static final String COLUMN_NAME_ADDRESS = "ADDRESS";
  public static final String COLUMN_NAME_CITY = "CITY";
  public static final String COLUMN_NAME_COUNTRY = "COUNTRY";
  public static final String COLUMN_NAME_COUNTRY_NAME = "COUNTRY_NAME";
  public static final String COLUMN_NAME_DATE_DUE = "DATE_DUE";
  public static final String COLUMN_NAME_DATE_VALUE = "DATE_VALUE";
  public static final String COLUMN_NAME_NAME1 = "NAME1";
  public static final String COLUMN_NAME_NAME2 = "NAME2";
  public static final String COLUMN_NAME_NAME3 = "NAME3";
  public static final String COLUMN_NAME_NAME4 = "NAME4";
  public static final String COLUMN_NAME_PAYEE_ADDRESS = "PAYEE_ADDRESS";
  public static final String COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO = "PAYEE_BANK_ACCOUNT_NO";
  public static final String COLUMN_NAME_PAYEE_BANK_KEY = "PAYEE_BANK_KEY";
  public static final String COLUMN_NAME_PAYEE_BANK_NO = "PAYEE_BANK_NO";
  public static final String COLUMN_NAME_PAYEE_BANK_REFERENCE = "PAYEE_BANK_REFERENCE";
  public static final String COLUMN_NAME_PAYEE_CITY = "PAYEE_CITY";
  public static final String COLUMN_NAME_PAYEE_CODE = "PAYEE_CODE";
  public static final String COLUMN_NAME_PAYEE_COUNTRY = "PAYEE_COUNTRY";
  public static final String COLUMN_NAME_PAYEE_NAME1 = "PAYEE_NAME1";
  public static final String COLUMN_NAME_PAYEE_NAME2 = "PAYEE_NAME2";
  public static final String COLUMN_NAME_PAYEE_NAME3 = "PAYEE_NAME3";
  public static final String COLUMN_NAME_PAYEE_NAME4 = "PAYEE_NAME4";
  public static final String COLUMN_NAME_PAYEE_POSTAL_CODE = "PAYEE_POSTAL_CODE";
  public static final String COLUMN_NAME_PAYEE_TAX_ID = "PAYEE_TAX_ID";
  public static final String COLUMN_NAME_PAYEE_TITLE = "PAYEE_TITLE";
  public static final String COLUMN_NAME_PAYING_BANK_ACCOUNT_NO = "PAYING_BANK_ACCOUNT_NO";
  public static final String COLUMN_NAME_PAYING_BANK_CODE = "PAYING_BANK_CODE";
  public static final String COLUMN_NAME_PAYING_BANK_COUNTRY = "PAYING_BANK_COUNTRY";
  public static final String COLUMN_NAME_PAYING_BANK_KEY = "PAYING_BANK_KEY";
  public static final String COLUMN_NAME_PAYING_BANK_NAME = "PAYING_BANK_NAME";
  public static final String COLUMN_NAME_PAYING_BANK_NO = "PAYING_BANK_NO";
  public static final String COLUMN_NAME_PAYING_COMPANY_CODE = "PAYING_COMPANY_CODE";
  public static final String COLUMN_NAME_PAYING_GL_ACCOUNT = "PAYING_GL_ACCOUNT";
  public static final String COLUMN_NAME_PAYING_HOUSE_BANK = "PAYING_HOUSE_BANK";
  public static final String COLUMN_NAME_PAYMENT_SPECIAL_GL = "PAYMENT_SPECIAL_GL";
  public static final String COLUMN_NAME_POSTAL_CODE = "POSTAL_CODE";
  public static final String COLUMN_NAME_SWIFT_CODE = "SWIFT_CODE";
  public static final String COLUMN_NAME_VENDOR_CODE = "VENDOR_CODE";
  public static final String COLUMN_NAME_VENDOR_FLAG = "VENDOR_FLAG";
  public static final String COLUMN_NAME_VENDOR_NAME = "VENDOR_NAME";
  public static final String COLUMN_NAME_VENDOR_TAX_ID = "VENDOR_TAX_ID";
  public static final String COLUMN_NAME_VENDOR_TITLE = "VENDOR_TITLE";
  public static final String COLUMN_NAME_PAYEE_BANK = "PAYEE_BANK";
  public static final String COLUMN_NAME_PAYEE_BANK_NAME = "PAYEE_BANK_NAME";
  public static final String COLUMN_NAME_PAYINGGLACCOUNT = "PAYINGGLACCOUNT";
  public static final String COLUMN_NAME_PAYMENT_SPECIALGL = "PAYMENT_SPECIALGL";

  private String accountType;
  private String assetNo;
  private String assetSubNo;
  private String assignment;
  private String bankAccountNo;
  private String brDocumentNo;
  private int brLine;
  private String budgetAccount;
  private String budgetActivity;
  private String budgetActivityName;
  private boolean isChild;
  private String costCenter;
  private String costCenterName;
  private String currency;
  private Timestamp dateAcct;
  private Timestamp dateBaseLine;
  private Timestamp dateDoc;
  private String drCr;
  private String errorCode;
  private String fiArea;
  private String fundCenter;
  private String fundCenterName;
  private String fundSource;
  private String fundSourceName;
  private String glAccount;
  private String glAccountName;
  private String headerReference;
  private String houseBank;
  private String idemStatus;
  private BigDecimal invoiceAmount;
  private String invoiceCompanyCode;
  private String invoiceCompanyName;
  private String invoiceDocumentNo;
  private String invoiceDocumentType;
  private String invoiceFiscalYear;
  private String invoicePaymentCenter;
  private BigDecimal invoiceWtxAmount;
  private BigDecimal invoiceWtxAmountP;
  private BigDecimal invoiceWtxBase;
  private BigDecimal invoiceWtxBaseP;
  private int line;
  private String lineItemText;
  private BigDecimal originalAmount;
  private String originalCompanyCode;
  private String originalCompanyName;
  private String originalDocumentNo;
  private String originalDocumentType;
  private String originalFiscalYear;
  private String originalPaymentCenter;
  private BigDecimal originalWtxAmount;
  private BigDecimal originalWtxAmountP;
  private BigDecimal originalWtxBase;
  private BigDecimal originalWtxBaseP;
  private String parentCompanyCode;
  private String parentDocumentNo;
  private String parentFiscalYear;
  private Long paymentAliasId;
  private String paymentBlock;
  private String paymentCenter;
  private String paymentCompanyCode;
  private Timestamp paymentDate;
  private Timestamp paymentDateAcct;
  private String paymentDocumentNo;
  private String paymentFiscalYear;
  private String paymentMethod;
  private String paymentMethodName;
  private String paymentName;
  private String paymentReference;
  private String paymentTerm;
  private String pmGroupDoc;
  private String pmGroupNo;
  private String poDocumentNo;
  private int poLine;
  private String postingKey;
  private boolean isProposal;
  private boolean isProposalBlock;
  private String reference1;
  private String reference2;
  private String reference3;
  private String specialGl;
  private String specialGlName;
  private String status;
  private String tradingPartner;
  private String tradingPartnerName;
  private Timestamp created;
  private String createdBy;
  private Timestamp updated;
  private String updatedBy;
  private BigDecimal invoiceAmountPaid;
  private BigDecimal originalAmountPaid;
  private String fiAreaName;
  private String paymentCompanyName;
  private String reversePaymentCompanyCode;
  private String reversePaymentDocumentNo;
  private String reversePaymentFiscalYear;
  private String reversePaymentDocumentType;
  private String paymentDocumentType;
  private String idemUpdate;
  private BigDecimal invoiceWtxAmountp;
  private BigDecimal invoiceWtxBasep;
  private BigDecimal originalWtxAmountp;
  private BigDecimal originalWtxBasep;
  private String specialgl;
  private String specialglname;
  private boolean isHaveChild;
  private boolean haveChild;
  private BigDecimal creditMemo;
  private String reversePaymentStatus;
  private BigDecimal wtxCreditMemo;
  private String paymentInformationId;
  private String accountHolderName;
  private String address;
  private String city;
  private String country;
  private String countryName;
  private Timestamp dateDue;
  private Timestamp dateValue;
  private String name1;
  private String name2;
  private String name3;
  private String name4;
  private String payeeAddress;
  private String payeeBankAccountNo;
  private String payeeBankKey;
  private String payeeBankNo;
  private String payeeBankReference;
  private String payeeCity;
  private String payeeCode;
  private String payeeCountry;
  private String payeeName1;
  private String payeeName2;
  private String payeeName3;
  private String payeeName4;
  private String payeePostalCode;
  private String payeeTaxId;
  private String payeeTitle;
  private String payingBankAccountNo;
  private String payingBankCode;
  private String payingBankCountry;
  private String payingBankKey;
  private String payingBankName;
  private String payingBankNo;
  private String payingCompanyCode;
  private String payingGlAccount;
  private String payingHouseBank;
  private String paymentSpecialGl;
  private String postalCode;
  private String swiftCode;
  private String vendorCode;
  private String vendorFlag;
  private String vendorName;
  private String vendorTaxId;
  private String vendorTitle;
  private String payeeBank;
  private String payeeBankName;
  private String payingglaccount;
  private String paymentSpecialgl;


    public PrepareRealRunDocument(Long id, String accountType, String assetNo, String assetSubNo, String assignment, String bankAccountNo, String brDocumentNo, int brLine, String budgetAccount, String budgetActivity, String budgetActivityName, boolean isChild, String costCenter, String costCenterName, String currency, Timestamp dateAcct, Timestamp dateBaseLine, Timestamp dateDoc, String drCr, String errorCode, String fiArea, String fundCenter, String fundCenterName, String fundSource, String fundSourceName, String glAccount, String glAccountName, String headerReference, String houseBank, String idemStatus, BigDecimal invoiceAmount, String invoiceCompanyCode, String invoiceCompanyName, String invoiceDocumentNo, String invoiceDocumentType, String invoiceFiscalYear, String invoicePaymentCenter, BigDecimal invoiceWtxAmount, BigDecimal invoiceWtxAmountP, BigDecimal invoiceWtxBase, BigDecimal invoiceWtxBaseP, int line, String lineItemText, BigDecimal originalAmount, String originalCompanyCode, String originalCompanyName, String originalDocumentNo, String originalDocumentType, String originalFiscalYear, String originalPaymentCenter, BigDecimal originalWtxAmount, BigDecimal originalWtxAmountP, BigDecimal originalWtxBase, BigDecimal originalWtxBaseP, String parentCompanyCode, String parentDocumentNo, String parentFiscalYear, Long paymentAliasId, String paymentBlock, String paymentCenter, String paymentCompanyCode, Timestamp paymentDate, Timestamp paymentDateAcct, String paymentDocumentNo, String paymentFiscalYear, String paymentMethod, String paymentMethodName, String paymentName, String paymentReference, String paymentTerm, String pmGroupDoc, String pmGroupNo, String poDocumentNo, int poLine, String postingKey, boolean isProposal, boolean isProposalBlock, String reference1, String reference2, String reference3, String specialGl, String specialGlName, String status, String tradingPartner, String tradingPartnerName, Timestamp created, String createdBy, Timestamp updated, String updatedBy, BigDecimal invoiceAmountPaid, BigDecimal originalAmountPaid, String fiAreaName, String paymentCompanyName, String reversePaymentCompanyCode, String reversePaymentDocumentNo, String reversePaymentFiscalYear, String reversePaymentDocumentType, String paymentDocumentType, String idemUpdate, BigDecimal invoiceWtxAmountp, BigDecimal invoiceWtxBasep, BigDecimal originalWtxAmountp, BigDecimal originalWtxBasep, String specialgl, String specialglname, boolean isHaveChild, boolean haveChild, BigDecimal creditMemo, String reversePaymentStatus, BigDecimal wtxCreditMemo, String paymentInformationId, String accountHolderName, String address, String city, String country, String countryName, Timestamp dateDue, Timestamp dateValue, String name1, String name2, String name3, String name4, String payeeAddress, String payeeBankAccountNo, String payeeBankKey, String payeeBankNo, String payeeBankReference, String payeeCity, String payeeCode, String payeeCountry, String payeeName1, String payeeName2, String payeeName3, String payeeName4, String payeePostalCode, String payeeTaxId, String payeeTitle, String payingBankAccountNo, String payingBankCode, String payingBankCountry, String payingBankKey, String payingBankName, String payingBankNo, String payingCompanyCode, String payingGlAccount, String payingHouseBank, String paymentSpecialGl, String postalCode, String swiftCode, String vendorCode, String vendorFlag, String vendorName, String vendorTaxId, String vendorTitle, String payeeBank, String payeeBankName, String payingglaccount, String paymentSpecialgl) {
        super(id);
        this.accountType = accountType;
        this.assetNo = assetNo;
        this.assetSubNo = assetSubNo;
        this.assignment = assignment;
        this.bankAccountNo = bankAccountNo;
        this.brDocumentNo = brDocumentNo;
        this.brLine = brLine;
        this.budgetAccount = budgetAccount;
        this.budgetActivity = budgetActivity;
        this.budgetActivityName = budgetActivityName;
        this.isChild = isChild;
        this.costCenter = costCenter;
        this.costCenterName = costCenterName;
        this.currency = currency;
        this.dateAcct = dateAcct;
        this.dateBaseLine = dateBaseLine;
        this.dateDoc = dateDoc;
        this.drCr = drCr;
        this.errorCode = errorCode;
        this.fiArea = fiArea;
        this.fundCenter = fundCenter;
        this.fundCenterName = fundCenterName;
        this.fundSource = fundSource;
        this.fundSourceName = fundSourceName;
        this.glAccount = glAccount;
        this.glAccountName = glAccountName;
        this.headerReference = headerReference;
        this.houseBank = houseBank;
        this.idemStatus = idemStatus;
        this.invoiceAmount = invoiceAmount;
        this.invoiceCompanyCode = invoiceCompanyCode;
        this.invoiceCompanyName = invoiceCompanyName;
        this.invoiceDocumentNo = invoiceDocumentNo;
        this.invoiceDocumentType = invoiceDocumentType;
        this.invoiceFiscalYear = invoiceFiscalYear;
        this.invoicePaymentCenter = invoicePaymentCenter;
        this.invoiceWtxAmount = invoiceWtxAmount;
        this.invoiceWtxAmountP = invoiceWtxAmountP;
        this.invoiceWtxBase = invoiceWtxBase;
        this.invoiceWtxBaseP = invoiceWtxBaseP;
        this.line = line;
        this.lineItemText = lineItemText;
        this.originalAmount = originalAmount;
        this.originalCompanyCode = originalCompanyCode;
        this.originalCompanyName = originalCompanyName;
        this.originalDocumentNo = originalDocumentNo;
        this.originalDocumentType = originalDocumentType;
        this.originalFiscalYear = originalFiscalYear;
        this.originalPaymentCenter = originalPaymentCenter;
        this.originalWtxAmount = originalWtxAmount;
        this.originalWtxAmountP = originalWtxAmountP;
        this.originalWtxBase = originalWtxBase;
        this.originalWtxBaseP = originalWtxBaseP;
        this.parentCompanyCode = parentCompanyCode;
        this.parentDocumentNo = parentDocumentNo;
        this.parentFiscalYear = parentFiscalYear;
        this.paymentAliasId = paymentAliasId;
        this.paymentBlock = paymentBlock;
        this.paymentCenter = paymentCenter;
        this.paymentCompanyCode = paymentCompanyCode;
        this.paymentDate = paymentDate;
        this.paymentDateAcct = paymentDateAcct;
        this.paymentDocumentNo = paymentDocumentNo;
        this.paymentFiscalYear = paymentFiscalYear;
        this.paymentMethod = paymentMethod;
        this.paymentMethodName = paymentMethodName;
        this.paymentName = paymentName;
        this.paymentReference = paymentReference;
        this.paymentTerm = paymentTerm;
        this.pmGroupDoc = pmGroupDoc;
        this.pmGroupNo = pmGroupNo;
        this.poDocumentNo = poDocumentNo;
        this.poLine = poLine;
        this.postingKey = postingKey;
        this.isProposal = isProposal;
        this.isProposalBlock = isProposalBlock;
        this.reference1 = reference1;
        this.reference2 = reference2;
        this.reference3 = reference3;
        this.specialGl = specialGl;
        this.specialGlName = specialGlName;
        this.status = status;
        this.tradingPartner = tradingPartner;
        this.tradingPartnerName = tradingPartnerName;
        this.created = created;
        this.createdBy = createdBy;
        this.updated = updated;
        this.updatedBy = updatedBy;
        this.invoiceAmountPaid = invoiceAmountPaid;
        this.originalAmountPaid = originalAmountPaid;
        this.fiAreaName = fiAreaName;
        this.paymentCompanyName = paymentCompanyName;
        this.reversePaymentCompanyCode = reversePaymentCompanyCode;
        this.reversePaymentDocumentNo = reversePaymentDocumentNo;
        this.reversePaymentFiscalYear = reversePaymentFiscalYear;
        this.reversePaymentDocumentType = reversePaymentDocumentType;
        this.paymentDocumentType = paymentDocumentType;
        this.idemUpdate = idemUpdate;
        this.invoiceWtxAmountp = invoiceWtxAmountp;
        this.invoiceWtxBasep = invoiceWtxBasep;
        this.originalWtxAmountp = originalWtxAmountp;
        this.originalWtxBasep = originalWtxBasep;
        this.specialgl = specialgl;
        this.specialglname = specialglname;
        this.isHaveChild = isHaveChild;
        this.haveChild = haveChild;
        this.creditMemo = creditMemo;
        this.reversePaymentStatus = reversePaymentStatus;
        this.wtxCreditMemo = wtxCreditMemo;
        this.paymentInformationId = paymentInformationId;
        this.accountHolderName = accountHolderName;
        this.address = address;
        this.city = city;
        this.country = country;
        this.countryName = countryName;
        this.dateDue = dateDue;
        this.dateValue = dateValue;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.name4 = name4;
        this.payeeAddress = payeeAddress;
        this.payeeBankAccountNo = payeeBankAccountNo;
        this.payeeBankKey = payeeBankKey;
        this.payeeBankNo = payeeBankNo;
        this.payeeBankReference = payeeBankReference;
        this.payeeCity = payeeCity;
        this.payeeCode = payeeCode;
        this.payeeCountry = payeeCountry;
        this.payeeName1 = payeeName1;
        this.payeeName2 = payeeName2;
        this.payeeName3 = payeeName3;
        this.payeeName4 = payeeName4;
        this.payeePostalCode = payeePostalCode;
        this.payeeTaxId = payeeTaxId;
        this.payeeTitle = payeeTitle;
        this.payingBankAccountNo = payingBankAccountNo;
        this.payingBankCode = payingBankCode;
        this.payingBankCountry = payingBankCountry;
        this.payingBankKey = payingBankKey;
        this.payingBankName = payingBankName;
        this.payingBankNo = payingBankNo;
        this.payingCompanyCode = payingCompanyCode;
        this.payingGlAccount = payingGlAccount;
        this.payingHouseBank = payingHouseBank;
        this.paymentSpecialGl = paymentSpecialGl;
        this.postalCode = postalCode;
        this.swiftCode = swiftCode;
        this.vendorCode = vendorCode;
        this.vendorFlag = vendorFlag;
        this.vendorName = vendorName;
        this.vendorTaxId = vendorTaxId;
        this.vendorTitle = vendorTitle;
        this.payeeBank = payeeBank;
        this.payeeBankName = payeeBankName;
        this.payingglaccount = payingglaccount;
        this.paymentSpecialgl = paymentSpecialgl;
    }

  @Override
  public String toString() {
    return "PrepareRealRunDocument{" +
            "accountType='" + accountType + '\'' +
            ", assetNo='" + assetNo + '\'' +
            ", assetSubNo='" + assetSubNo + '\'' +
            ", assignment='" + assignment + '\'' +
            ", bankAccountNo='" + bankAccountNo + '\'' +
            ", brDocumentNo='" + brDocumentNo + '\'' +
            ", brLine=" + brLine +
            ", budgetAccount='" + budgetAccount + '\'' +
            ", budgetActivity='" + budgetActivity + '\'' +
            ", budgetActivityName='" + budgetActivityName + '\'' +
            ", isChild=" + isChild +
            ", costCenter='" + costCenter + '\'' +
            ", costCenterName='" + costCenterName + '\'' +
            ", currency='" + currency + '\'' +
            ", dateAcct=" + dateAcct +
            ", dateBaseLine=" + dateBaseLine +
            ", dateDoc=" + dateDoc +
            ", drCr='" + drCr + '\'' +
            ", errorCode='" + errorCode + '\'' +
            ", fiArea='" + fiArea + '\'' +
            ", fundCenter='" + fundCenter + '\'' +
            ", fundCenterName='" + fundCenterName + '\'' +
            ", fundSource='" + fundSource + '\'' +
            ", fundSourceName='" + fundSourceName + '\'' +
            ", glAccount='" + glAccount + '\'' +
            ", glAccountName='" + glAccountName + '\'' +
            ", headerReference='" + headerReference + '\'' +
            ", houseBank='" + houseBank + '\'' +
            ", idemStatus='" + idemStatus + '\'' +
            ", invoiceAmount=" + invoiceAmount +
            ", invoiceCompanyCode='" + invoiceCompanyCode + '\'' +
            ", invoiceCompanyName='" + invoiceCompanyName + '\'' +
            ", invoiceDocumentNo='" + invoiceDocumentNo + '\'' +
            ", invoiceDocumentType='" + invoiceDocumentType + '\'' +
            ", invoiceFiscalYear='" + invoiceFiscalYear + '\'' +
            ", invoicePaymentCenter='" + invoicePaymentCenter + '\'' +
            ", invoiceWtxAmount=" + invoiceWtxAmount +
            ", invoiceWtxAmountP=" + invoiceWtxAmountP +
            ", invoiceWtxBase=" + invoiceWtxBase +
            ", invoiceWtxBaseP=" + invoiceWtxBaseP +
            ", line=" + line +
            ", lineItemText='" + lineItemText + '\'' +
            ", originalAmount=" + originalAmount +
            ", originalCompanyCode='" + originalCompanyCode + '\'' +
            ", originalCompanyName='" + originalCompanyName + '\'' +
            ", originalDocumentNo='" + originalDocumentNo + '\'' +
            ", originalDocumentType='" + originalDocumentType + '\'' +
            ", originalFiscalYear='" + originalFiscalYear + '\'' +
            ", originalPaymentCenter='" + originalPaymentCenter + '\'' +
            ", originalWtxAmount=" + originalWtxAmount +
            ", originalWtxAmountP=" + originalWtxAmountP +
            ", originalWtxBase=" + originalWtxBase +
            ", originalWtxBaseP=" + originalWtxBaseP +
            ", parentCompanyCode='" + parentCompanyCode + '\'' +
            ", parentDocumentNo='" + parentDocumentNo + '\'' +
            ", parentFiscalYear='" + parentFiscalYear + '\'' +
            ", paymentAliasId=" + paymentAliasId +
            ", paymentBlock='" + paymentBlock + '\'' +
            ", paymentCenter='" + paymentCenter + '\'' +
            ", paymentCompanyCode='" + paymentCompanyCode + '\'' +
            ", paymentDate=" + paymentDate +
            ", paymentDateAcct=" + paymentDateAcct +
            ", paymentDocumentNo='" + paymentDocumentNo + '\'' +
            ", paymentFiscalYear='" + paymentFiscalYear + '\'' +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", paymentMethodName='" + paymentMethodName + '\'' +
            ", paymentName='" + paymentName + '\'' +
            ", paymentReference='" + paymentReference + '\'' +
            ", paymentTerm='" + paymentTerm + '\'' +
            ", pmGroupDoc='" + pmGroupDoc + '\'' +
            ", pmGroupNo='" + pmGroupNo + '\'' +
            ", poDocumentNo='" + poDocumentNo + '\'' +
            ", poLine=" + poLine +
            ", postingKey='" + postingKey + '\'' +
            ", isProposal=" + isProposal +
            ", isProposalBlock=" + isProposalBlock +
            ", reference1='" + reference1 + '\'' +
            ", reference2='" + reference2 + '\'' +
            ", reference3='" + reference3 + '\'' +
            ", specialGl='" + specialGl + '\'' +
            ", specialGlName='" + specialGlName + '\'' +
            ", status='" + status + '\'' +
            ", tradingPartner='" + tradingPartner + '\'' +
            ", tradingPartnerName='" + tradingPartnerName + '\'' +
            ", created=" + created +
            ", createdBy='" + createdBy + '\'' +
            ", updated=" + updated +
            ", updatedBy='" + updatedBy + '\'' +
            ", invoiceAmountPaid=" + invoiceAmountPaid +
            ", originalAmountPaid=" + originalAmountPaid +
            ", fiAreaName='" + fiAreaName + '\'' +
            ", paymentCompanyName='" + paymentCompanyName + '\'' +
            ", reversePaymentCompanyCode='" + reversePaymentCompanyCode + '\'' +
            ", reversePaymentDocumentNo='" + reversePaymentDocumentNo + '\'' +
            ", reversePaymentFiscalYear='" + reversePaymentFiscalYear + '\'' +
            ", reversePaymentDocumentType='" + reversePaymentDocumentType + '\'' +
            ", paymentDocumentType='" + paymentDocumentType + '\'' +
            ", idemUpdate='" + idemUpdate + '\'' +
            ", invoiceWtxAmountp=" + invoiceWtxAmountp +
            ", invoiceWtxBasep=" + invoiceWtxBasep +
            ", originalWtxAmountp=" + originalWtxAmountp +
            ", originalWtxBasep=" + originalWtxBasep +
            ", specialgl='" + specialgl + '\'' +
            ", specialglname='" + specialglname + '\'' +
            ", isHaveChild=" + isHaveChild +
            ", haveChild=" + haveChild +
            ", creditMemo=" + creditMemo +
            ", reversePaymentStatus='" + reversePaymentStatus + '\'' +
            ", wtxCreditMemo=" + wtxCreditMemo +
            ", paymentInformationId='" + paymentInformationId + '\'' +
            ", accountHolderName='" + accountHolderName + '\'' +
            ", address='" + address + '\'' +
            ", city='" + city + '\'' +
            ", country='" + country + '\'' +
            ", countryName='" + countryName + '\'' +
            ", dateDue=" + dateDue +
            ", dateValue=" + dateValue +
            ", name1='" + name1 + '\'' +
            ", name2='" + name2 + '\'' +
            ", name3='" + name3 + '\'' +
            ", name4='" + name4 + '\'' +
            ", payeeAddress='" + payeeAddress + '\'' +
            ", payeeBankAccountNo='" + payeeBankAccountNo + '\'' +
            ", payeeBankKey='" + payeeBankKey + '\'' +
            ", payeeBankNo='" + payeeBankNo + '\'' +
            ", payeeBankReference='" + payeeBankReference + '\'' +
            ", payeeCity='" + payeeCity + '\'' +
            ", payeeCode='" + payeeCode + '\'' +
            ", payeeCountry='" + payeeCountry + '\'' +
            ", payeeName1='" + payeeName1 + '\'' +
            ", payeeName2='" + payeeName2 + '\'' +
            ", payeeName3='" + payeeName3 + '\'' +
            ", payeeName4='" + payeeName4 + '\'' +
            ", payeePostalCode='" + payeePostalCode + '\'' +
            ", payeeTaxId='" + payeeTaxId + '\'' +
            ", payeeTitle='" + payeeTitle + '\'' +
            ", payingBankAccountNo='" + payingBankAccountNo + '\'' +
            ", payingBankCode='" + payingBankCode + '\'' +
            ", payingBankCountry='" + payingBankCountry + '\'' +
            ", payingBankKey='" + payingBankKey + '\'' +
            ", payingBankName='" + payingBankName + '\'' +
            ", payingBankNo='" + payingBankNo + '\'' +
            ", payingCompanyCode='" + payingCompanyCode + '\'' +
            ", payingGlAccount='" + payingGlAccount + '\'' +
            ", payingHouseBank='" + payingHouseBank + '\'' +
            ", paymentSpecialGl='" + paymentSpecialGl + '\'' +
            ", postalCode='" + postalCode + '\'' +
            ", swiftCode='" + swiftCode + '\'' +
            ", vendorCode='" + vendorCode + '\'' +
            ", vendorFlag='" + vendorFlag + '\'' +
            ", vendorName='" + vendorName + '\'' +
            ", vendorTaxId='" + vendorTaxId + '\'' +
            ", vendorTitle='" + vendorTitle + '\'' +
            ", payeeBank='" + payeeBank + '\'' +
            ", payeeBankName='" + payeeBankName + '\'' +
            ", payingglaccount='" + payingglaccount + '\'' +
            ", paymentSpecialgl='" + paymentSpecialgl + '\'' +
            '}';
  }
}
