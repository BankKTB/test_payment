package th.com.bloomcode.paymentservice.model.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PrepareProposalDocument extends BaseModel {

    public static final String COLUMN_NAME_GL_HEAD_ID = "ID";
    public static final String COLUMN_NAME_DOCUMENT_TYPE = "DOCUMENT_TYPE";
    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_UNBLOCK_DATE = "UNBLOCK_DATE";
    public static final String COLUMN_NAME_DATE_DOC = "DATE_DOC";
    public static final String COLUMN_NAME_DATE_ACCT = "DATE_ACCT";
    public static final String COLUMN_NAME_PERIOD = "PERIOD";
    public static final String COLUMN_NAME_CURRENCY = "CURRENCY";
    public static final String COLUMN_NAME_INVOICE_DOCUMENT_NO = "INVOICE_DOCUMENT_NO";
    public static final String COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO = "REVERSE_INVOICE_DOCUMENT_NO";
    public static final String COLUMN_NAME_ORIGINAL_DOCUMENT_NO = "ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO = "REVERSE_ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR = "REVERSE_ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_COST_CENTER1 = "COST_CENTER1";
    public static final String COLUMN_NAME_COST_CENTER2 = "COST_CENTER2";
    public static final String COLUMN_NAME_HEADER_REFERENCE = "HEADER_REFERENCE";
    public static final String COLUMN_NAME_DOCUMENT_HEADER_TEXT = "DOCUMENT_HEADER_TEXT";
    public static final String COLUMN_NAME_HEADER_REFERENCE2 = "HEADER_REFERENCE2";
    public static final String COLUMN_NAME_REVERSE_DATE_ACCT = "REVERSE_DATE_ACCT";
    public static final String COLUMN_NAME_REVERSE_REASON = "REVERSE_REASON";
    public static final String COLUMN_NAME_ORIGINAL_DOCUMENT = "ORIGINAL_DOCUMENT";
    public static final String COLUMN_NAME_DOCUMENT_CREATED = "DOCUMENT_CREATED";
    public static final String COLUMN_NAME_USER_PARK = "USER_PARK";
    public static final String COLUMN_NAME_USER_POST = "USER_POST";
    public static final String COLUMN_NAME_POSTING_KEY = "POSTING_KEY";
    public static final String COLUMN_NAME_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String COLUMN_NAME_DR_CR = "DR_CR";
    public static final String COLUMN_NAME_GL_ACCOUNT = "GL_ACCOUNT";
    public static final String COLUMN_NAME_FI_AREA = "FI_AREA";
    public static final String COLUMN_NAME_COST_CENTER = "COST_CENTER";
    public static final String COLUMN_NAME_FUND_SOURCE = "FUND_SOURCE";
    public static final String COLUMN_NAME_BG_CODE = "BG_CODE";
    public static final String COLUMN_NAME_BG_ACTIVITY = "BG_ACTIVITY";
    public static final String COLUMN_NAME_COST_ACTIVITY = "COST_ACTIVITY";
    public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
    public static final String COLUMN_NAME_REFERENCE3 = "REFERENCE3";
    public static final String COLUMN_NAME_ASSIGNMENT = "ASSIGNMENT";
    public static final String COLUMN_NAME_BR_DOCUMENT_NO = "BR_DOCUMENT_NO";
    public static final String COLUMN_NAME_BR_LINE = "BR_LINE";
    public static final String COLUMN_NAME_PAYMENT_CENTER = "PAYMENT_CENTER";
    public static final String COLUMN_NAME_BANK_BOOK = "BANK_BOOK";
    public static final String COLUMN_NAME_SUB_ACCOUNT = "SUB_ACCOUNT";
    public static final String COLUMN_NAME_SUB_ACCOUNT_OWNER = "SUB_ACCOUNT_OWNER";
    public static final String COLUMN_NAME_DEPOSIT_ACCOUNT = "DEPOSIT_ACCOUNT";
    public static final String COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER = "DEPOSIT_ACCOUNT_OWNER";
    public static final String COLUMN_NAME_GPSC = "GPSC";
    public static final String COLUMN_NAME_GPSC_GROUP = "GPSC_GROUP";
    public static final String COLUMN_NAME_LINE_ITEM_TEXT = "LINE_ITEM_TEXT";
    public static final String COLUMN_NAME_LINE_DESC = "LINE_DESC";
    public static final String COLUMN_NAME_PAYMENT_TERM = "PAYMENT_TERM";
    public static final String COLUMN_NAME_PAYMENT_METHOD = "PAYMENT_METHOD";
    public static final String COLUMN_NAME_WTX_TYPE = "WTX_TYPE";
    public static final String COLUMN_NAME_WTX_CODE = "WTX_CODE";
    public static final String COLUMN_NAME_WTX_BASE = "WTX_BASE";
    public static final String COLUMN_NAME_WTX_AMOUNT = "WTX_AMOUNT";
    public static final String COLUMN_NAME_WTX_TYPE_P = "WTX_TYPE_P";
    public static final String COLUMN_NAME_WTX_CODE_P = "WTX_CODE_P";
    public static final String COLUMN_NAME_WTX_BASE_P = "WTX_BASE_P";
    public static final String COLUMN_NAME_WTX_AMOUNT_P = "WTX_AMOUNT_P";
    public static final String COLUMN_NAME_VENDOR = "VENDOR";
    public static final String COLUMN_NAME_VENDOR_TAX_ID = "VENDOR_TAX_ID";
    public static final String COLUMN_NAME_PAYEE = "PAYEE";
    public static final String COLUMN_NAME_PAYEE_TAX_ID = "PAYEE_TAX_ID";
    public static final String COLUMN_NAME_BANK_ACCOUNT_NO = "BANK_ACCOUNT_NO";
    public static final String COLUMN_NAME_BANK_BRANCH_NO = "BANK_BRANCH_NO";
    public static final String COLUMN_NAME_TRADING_PARTNER = "TRADING_PARTNER";
    public static final String COLUMN_NAME_TRADING_PARTNER_PARK = "TRADING_PARTNER_PARK";
    public static final String COLUMN_NAME_SPECIAL_GL = "SPECIAL_GL";
    public static final String COLUMN_NAME_DATE_BASE_LINE = "DATE_BASE_LINE";
    public static final String COLUMN_NAME_DATE_VALUE = "DATE_VALUE";
    public static final String COLUMN_NAME_ASSET_NO = "ASSET_NO";
    public static final String COLUMN_NAME_ASSET_SUB_NO = "ASSET_SUB_NO";
    public static final String COLUMN_NAME_QTY = "QTY";
    public static final String COLUMN_NAME_UOM = "UOM";
    public static final String COLUMN_NAME_REFERENCE1 = "REFERENCE1";
    public static final String COLUMN_NAME_REFERENCE2 = "REFERENCE2";
    public static final String COLUMN_NAME_PO_DOCUMENT_NO = "PO_DOCUMENT_NO";
    public static final String COLUMN_NAME_PO_LINE = "PO_LINE";
    public static final String COLUMN_NAME_INCOME = "INCOME";
    public static final String COLUMN_NAME_PAYMENT_BLOCK = "PAYMENT_BLOCK";
    public static final String COLUMN_NAME_PAYMENT_REFERENCE = "PAYMENT_REFERENCE";
    public static final String COLUMN_NAME_LINE = "LINE";
    public static final String COLUMN_NAME_COMP_CODE_NAME = "COMP_CODE_NAME";
    public static final String COLUMN_NAME_PAYMENT_CENTER_NAME = "PAYMENT_CENTER_NAME";
    public static final String COLUMN_NAME_COST_CENTER_NAME = "COST_CENTER_NAME";
    public static final String COLUMN_NAME_PAYMENT_METHOD_NAME = "PAYMENT_METHOD_NAME";
    public static final String COLUMN_NAME_FUND_SOURCE_NAME = "FUND_SOURCE_NAME";
    public static final String COLUMN_NAME_BG_CODE_NAME = "BG_CODE_NAME";
    public static final String COLUMN_NAME_BG_ACTIVITY_NAME = "BG_ACTIVITY_NAME";
    public static final String COLUMN_NAME_NAME1 = "NAME1";
    public static final String COLUMN_NAME_NAME2 = "NAME2";
    public static final String COLUMN_NAME_TAX_ID = "TAX_ID";
    public static final String COLUMN_NAME_ADDRESS = "ADDRESS";
    public static final String COLUMN_NAME_CITY = "CITY";
    public static final String COLUMN_NAME_POSTAL = "POSTAL";
    public static final String COLUMN_NAME_REGION_NAME = "REGION_NAME";
    public static final String COLUMN_NAME_COUNTRY = "COUNTRY";
    public static final String COLUMN_NAME_COUNTRY_CODE = "COUNTRY_CODE";
    public static final String COLUMN_NAME_PAYEE_BANK = "PAYEE_BANK";
    public static final String COLUMN_NAME_PAYEE_BANK_ACCOUNT_TYPE = "PAYEE_BANK_ACCOUNT_TYPE";
    public static final String COLUMN_NAME_PAYEE_BANK_NO = "PAYEE_BANK_NO";
    public static final String COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO = "PAYEE_BANK_ACCOUNT_NO";
    public static final String COLUMN_NAME_ACCOUNT_HOLDER_NAME = "ACCOUNT_HOLDER_NAME";
    public static final String COLUMN_NAME_PAYEE_BANK_NAME = "PAYEE_BANK_NAME";
    public static final String COLUMN_NAME_PAYEE_BANK_KEY = "PAYEE_BANK_KEY";
    public static final String COLUMN_NAME_SWIFT_CODE = "SWIFT_CODE";
    public static final String COLUMN_NAME_PAYEE_BANK_REFERENCE = "PAYEE_BANK_REFERENCE";
    public static final String COLUMN_NAME_AREA_NAME = "AREA_NAME";
    public static final String COLUMN_NAME_PAYMENT_CLEARING_DATE = "PAYMENT_CLEARING_DATE";
    public static final String COLUMN_NAME_PAYMENT_CLEARING_ENTRY_DATE = "PAYMENT_CLEARING_ENTRY_DATE";
    public static final String COLUMN_NAME_PAYMENT_CLEARING_DOC_NO = "PAYMENT_CLEARING_DOC_NO";
    public static final String COLUMN_NAME_PAYMENT_CLEARING_YEAR = "PAYMENT_CLEARING_YEAR";
    public static final String COLUMN_NAME_PAYMENT_ID = "PAYMENT_ID";
    public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
    public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";
    public static final String COLUMN_NAME_PAYMENT_DATE_ACCT = "PAYMENT_DATE_ACCT";
    public static final String COLUMN_NAME_HOUSE_BANK = "HOUSE_BANK";
    public static final String COLUMN_NAME_PAYING_COMP_CODE = "PAYING_COMP_CODE";
    public static final String COLUMN_NAME_PAYING_COMP_CODE_NAME = "PAYING_COMP_CODE_NAME";
    public static final String COLUMN_NAME_PAYING_BANK_CODE = "PAYING_BANK_CODE";
    public static final String COLUMN_NAME_PAYING_HOUSE_BANK = "PAYING_HOUSE_BANK";
    public static final String COLUMN_NAME_PAYING_BANK_ACCOUNT_NO = "PAYING_BANK_ACCOUNT_NO";
    public static final String COLUMN_NAME_PAYING_BANK_COUNTRY = "PAYING_BANK_COUNTRY";
    public static final String COLUMN_NAME_PAYING_BANK_NO = "PAYING_BANK_NO";
    public static final String COLUMN_NAME_PAYING_GL_ACCOUNT = "PAYING_GL_ACCOUNT";
    public static final String COLUMN_NAME_PAYING_BANK_KEY = "PAYING_BANK_KEY";
    public static final String COLUMN_NAME_PAYING_BANK_NAME = "PAYING_BANK_NAME";
    public static final String COLUMN_NAME_AMOUNT_PAID = "AMOUNT_PAID";
    public static final String COLUMN_NAME_STATUS = "STATUS";
    public static final String COLUMN_NAME_ERROR_CODE = "ERROR_CODE";

    public static final String COLUMN_NAME_VENDOR_ACTIVE = "VENDOR_ACTIVE";
    public static final String COLUMN_NAME_VENDOR_STATUS = "VENDOR_STATUS";
    public static final String COLUMN_NAME_PAYEE_ACTIVE = "PAYEE_ACTIVE";
    public static final String COLUMN_NAME_PAYEE_STATUS = "PAYEE_STATUS";
    public static final String COLUMN_NAME_PAYEE_CONFIRM = "PAYEE_CONFIRM";

    public static final String COLUMN_NAME_REAL_ORIGINAL_DOCUMENT_NO = "REAL_ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_REAL_ORIGINAL_DOCUMENT_TYPE = "REAL_ORIGINAL_DOCUMENT_TYPE";
    public static final String COLUMN_NAME_REAL_ORIGINAL_COMPANY_CODE = "REAL_ORIGINAL_COMPANY_CODE";
    public static final String COLUMN_NAME_REAL_ORIGINAL_FISCAL_YEAR = "REAL_ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_LINE_PAYMENT_CENTER = "LINE_PAYMENT_CENTER";
    public static final String COLUMN_NAME_LINE_WTX_AMOUNT = "LINE_WTX_AMOUNT";
    public static final String COLUMN_NAME_LINE_WTX_BASE = "LINE_WTX_BASE";
    public static final String COLUMN_NAME_LINE_WTX_AMOUNT_P = "LINE_WTX_AMOUNT_P";
    public static final String COLUMN_NAME_LINE_WTX_BASE_P = "LINE_WTX_BASE_P";


    private String documentType;
    private String companyCode;
    private Timestamp dateDoc;
    private Timestamp dateAcct;
    private int period;
    private String currency;
    private String invoiceDocumentNo;
    private String reverseInvoiceDocumentNo;
    private String originalDocumentNo;
    private String originalFiscalYear;
    private String reverseOriginalDocumentNo;
    private String reverseOriginalFiscalYear;
    private String costCenter1;
    private String costCenter2;
    private String headerReference;
    private String documentHeaderText;
    private String headerReference2;
    private Timestamp reverseDateAcct;
    private String reverseReason;
    private String originalDocument;
    private Timestamp documentCreated;
    private String userPark;
    private String userPost;
    private String postingKey;
    private String accountType;
    private String drCr;
    private String glAccount;
    private String fiArea;
    private String costCenter;
    private String fundSource;
    private String bgCode;
    private String bgActivity;
    private String costActivity;
    private BigDecimal amount;
    private String reference3;
    private String assignment;
    private String brDocumentNo;
    private int brLine;
    private String paymentCenter;
    private String bankBook;
    private String subAccount;
    private String subAccountOwner;
    private String depositAccount;
    private String depositAccountOwner;
    private String gpsc;
    private String gpscGroup;
    private String lineItemText;
    private String lineDesc;
    private String paymentTerm;
    private String paymentMethod;
    private String wtxType;
    private String wtxCode;
    private BigDecimal wtxBase;
    private BigDecimal wtxAmount;
    private String wtxTypeP;
    private String wtxCodeP;
    private BigDecimal wtxBaseP;
    private BigDecimal wtxAmountP;
    private String vendor;
    private String vendorTaxId;
    private String payee;
    private String payeeTaxId;
    private String bankAccountNo;
    private String bankBranchNo;
    private String tradingPartner;
    private String tradingPartnerPark;
    private String specialGL;
    private Timestamp dateBaseLine;
    private Timestamp dateValue;
    private String assetNo;
    private String assetSubNo;
    private BigDecimal qty;
    private String uom;
    private String reference1;
    private String reference2;
    private String poDocNo;
    private int poLine;
    private String income;
    private String paymentBlock;
    private String paymentRef;
    private int line;
    private String compCodeName;
    private String paymentCenterName;
    private String costCenterName;
    private String paymentMethodName;
    private String fundSourceName;
    private String bgCodeName;
    private String bgActivityName;
    private String name1;
    private String name2;
    private String taxId;
    private String address;
    private String city;
    private String postal;
    private String regionName;
    private String country;
    private String countryCode;
    private String payeeBank;
    private String payeeBankAccountType;
    private String payeeBankNo;
    private String payeeBankAccountNo;
    private String accountHolderName;
    private String payeeBankName;
    private String payeeBankKey;
    private String swiftCode;
    private String payeeBankReference;
    private String areaName;
    private Timestamp paymentClearingDate;
    private Timestamp paymentClearingEntryDate;
    private String paymentClearingDocNo;
    private String paymentClearingYear;
    private Long paymentId;
    private Timestamp paymentDate;
    private String paymentName;
    private Timestamp paymentDateAcct;
    private String houseBank;
    private String payingCompCode;
    private String payingCompCodeName;
    private String payingBankCode;
    private String payingHouseBank;
    private String payingBankAccountNo;
    private String payingBankCountry;
    private String payingBankNo;
    private String payingGLAccount;
    private String payingBankKey;
    private String payingBankName;
    private BigDecimal amountPaid;
    private String status;
    private String errorCode;

    private String vendorActive;
    private String vendorStatus;
    private String payeeActive;
    private String payeeStatus;
    private String payeeConfirm;


    private String realOriginalDocumentNo;
    private String realOriginalDocumentType;
    private String realOriginalCompanyCode;
    private String realOriginalFiscalYear;
    private String linePaymentCenter;
    private BigDecimal lineWtxAmount;
    private BigDecimal lineWtxBase;
    private BigDecimal lineWtxAmountP;
    private BigDecimal lineWtxBaseP;


    public PrepareProposalDocument(Long id, String documentType, String companyCode, Timestamp dateDoc, Timestamp dateAcct, int period, String currency, String invoiceDocumentNo, String reverseInvoiceDocumentNo, String originalDocumentNo, String originalFiscalYear, String reverseOriginalDocumentNo, String reverseOriginalFiscalYear, String costCenter1, String costCenter2, String headerReference, String documentHeaderText, String headerReference2, Timestamp reverseDateAcct, String reverseReason, String originalDocument, Timestamp documentCreated, String userPark, String userPost, String postingKey, String accountType, String drCr, String glAccount, String fiArea, String costCenter, String fundSource, String bgCode, String bgActivity, String costActivity, BigDecimal amount, String reference3, String assignment, String brDocumentNo, int brLine, String paymentCenter, String bankBook, String subAccount, String subAccountOwner, String depositAccount, String depositAccountOwner, String gpsc, String gpscGroup, String lineItemText, String lineDesc, String paymentTerm, String paymentMethod, String wtxType, String wtxCode, BigDecimal wtxBase, BigDecimal wtxAmount, String wtxTypeP, String wtxCodeP, BigDecimal wtxBaseP, BigDecimal wtxAmountP, String vendor, String vendorTaxId, String payee, String payeeTaxId, String bankAccountNo, String bankBranchNo, String tradingPartner, String tradingPartnerPark, String specialGL, Timestamp dateBaseLine, Timestamp dateValue, String assetNo, String assetSubNo, BigDecimal qty, String uom, String reference1, String reference2, String poDocNo, int poLine, String income, String paymentBlock, String paymentRef, int line, String compCodeName, String paymentCenterName, String costCenterName, String paymentMethodName, String fundSourceName, String bgCodeName, String bgActivityName, String name1, String name2, String taxId, String address, String city, String postal, String regionName, String country, String countryCode, String payeeBank, String payeeBankAccountType, String payeeBankNo, String payeeBankAccountNo, String accountHolderName, String payeeBankName, String payeeBankKey, String swiftCode, String payeeBankReference, String areaName
            , String vendorActive, String vendorStatus, String payeeActive, String payeeStatus, String payeeConfirm,
                                   String realOriginalDocumentNo, String realOriginalDocumentType, String realOriginalCompanyCode, String realOriginalFiscalYear, String linePaymentCenter, BigDecimal lineWtxAmount, BigDecimal lineWtxBase, BigDecimal lineWtxAmountP, BigDecimal lineWtxBaseP
    ) {
        super(id);
        this.documentType = documentType;
        this.companyCode = companyCode;
        this.dateDoc = dateDoc;
        this.dateAcct = dateAcct;
        this.period = period;
        this.currency = currency;
        this.invoiceDocumentNo = invoiceDocumentNo;
        this.reverseInvoiceDocumentNo = reverseInvoiceDocumentNo;
        this.originalDocumentNo = originalDocumentNo;
        this.originalFiscalYear = originalFiscalYear;
        this.reverseOriginalDocumentNo = reverseOriginalDocumentNo;
        this.reverseOriginalFiscalYear = reverseOriginalFiscalYear;
        this.costCenter1 = costCenter1;
        this.costCenter2 = costCenter2;
        this.headerReference = headerReference;
        this.documentHeaderText = documentHeaderText;
        this.headerReference2 = headerReference2;
        this.reverseDateAcct = reverseDateAcct;
        this.reverseReason = reverseReason;
        this.originalDocument = originalDocument;
        this.documentCreated = documentCreated;
        this.userPark = userPark;
        this.userPost = userPost;
        this.postingKey = postingKey;
        this.accountType = accountType;
        this.drCr = drCr;
        this.glAccount = glAccount;
        this.fiArea = fiArea;
        this.costCenter = costCenter;
        this.fundSource = fundSource;
        this.bgCode = bgCode;
        this.bgActivity = bgActivity;
        this.costActivity = costActivity;
        this.amount = amount;
        this.reference3 = reference3;
        this.assignment = assignment;
        this.brDocumentNo = brDocumentNo;
        this.brLine = brLine;
        this.paymentCenter = paymentCenter;
        this.bankBook = bankBook;
        this.subAccount = subAccount;
        this.subAccountOwner = subAccountOwner;
        this.depositAccount = depositAccount;
        this.depositAccountOwner = depositAccountOwner;
        this.gpsc = gpsc;
        this.gpscGroup = gpscGroup;
        this.lineItemText = lineItemText;
        this.lineDesc = lineDesc;
        this.paymentTerm = paymentTerm;
        this.paymentMethod = paymentMethod;
        this.wtxType = wtxType;
        this.wtxCode = wtxCode;
        this.wtxBase = wtxBase;
        this.wtxAmount = wtxAmount;
        this.wtxTypeP = wtxTypeP;
        this.wtxCodeP = wtxCodeP;
        this.wtxBaseP = wtxBaseP;
        this.wtxAmountP = wtxAmountP;
        this.vendor = vendor;
        this.vendorTaxId = vendorTaxId;
        this.payee = payee;
        this.payeeTaxId = payeeTaxId;
        this.bankAccountNo = bankAccountNo;
        this.bankBranchNo = bankBranchNo;
        this.tradingPartner = tradingPartner;
        this.tradingPartnerPark = tradingPartnerPark;
        this.specialGL = specialGL;
        this.dateBaseLine = dateBaseLine;
        this.dateValue = dateValue;
        this.assetNo = assetNo;
        this.assetSubNo = assetSubNo;
        this.qty = qty;
        this.uom = uom;
        this.reference1 = reference1;
        this.reference2 = reference2;
        this.poDocNo = poDocNo;
        this.poLine = poLine;
        this.income = income;
        this.paymentBlock = paymentBlock;
        this.paymentRef = paymentRef;
        this.line = line;
        this.compCodeName = compCodeName;
        this.paymentCenterName = paymentCenterName;
        this.costCenterName = costCenterName;
        this.paymentMethodName = paymentMethodName;
        this.fundSourceName = fundSourceName;
        this.bgCodeName = bgCodeName;
        this.bgActivityName = bgActivityName;
        this.name1 = name1;
        this.name2 = name2;
        this.taxId = taxId;
        this.address = address;
        this.city = city;
        this.postal = postal;
        this.regionName = regionName;
        this.country = country;
        this.countryCode = countryCode;
        this.payeeBank = payeeBank;
        this.payeeBankAccountType = payeeBankAccountType;
        this.payeeBankNo = payeeBankNo;
        this.payeeBankAccountNo = payeeBankAccountNo;
        this.accountHolderName = accountHolderName;
        this.payeeBankName = payeeBankName;
        this.payeeBankKey = payeeBankKey;
        this.swiftCode = swiftCode;
        this.payeeBankReference = payeeBankReference;
        this.areaName = areaName;
        this.vendorActive = vendorActive;
        this.vendorStatus = vendorStatus;
        this.payeeActive = payeeActive;
        this.payeeStatus = payeeStatus;
        this.payeeConfirm = payeeConfirm;

        this.realOriginalDocumentNo = realOriginalDocumentNo;
        this.realOriginalDocumentType = realOriginalDocumentType;
        this.realOriginalCompanyCode = realOriginalCompanyCode;
        this.realOriginalFiscalYear = realOriginalFiscalYear;
        this.linePaymentCenter = linePaymentCenter;
        this.lineWtxAmount = lineWtxAmount;
        this.lineWtxBase = lineWtxBase;
        this.lineWtxAmountP = lineWtxAmountP;
        this.lineWtxBaseP = lineWtxBaseP;
    }
}
