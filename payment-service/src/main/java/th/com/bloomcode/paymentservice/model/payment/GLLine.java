package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;
import th.com.bloomcode.paymentservice.webservice.model.ZFILine;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
public class GLLine extends BaseModel {

    public static final String TABLE_NAME = "GL_LINE";
    public static final String COLUMN_NAME_GL_LINE_ID = "ID";
    public static final String COLUMN_NAME_POSTING_KEY = "POSTING_KEY";
    public static final String COLUMN_NAME_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String COLUMN_NAME_DR_CR = "DR_CR";
    public static final String COLUMN_NAME_GL_ACCOUNT = "GL_ACCOUNT";
    public static final String COLUMN_NAME_FI_AREA = "FI_AREA";
    public static final String COLUMN_NAME_FI_AREA_NAME = "FI_AREA_NAME";
    public static final String COLUMN_NAME_COST_CENTER = "COST_CENTER";
    public static final String COLUMN_NAME_FUND_SOURCE = "FUND_SOURCE";
    public static final String COLUMN_NAME_BG_CODE = "BG_CODE";
    public static final String COLUMN_NAME_BG_ACTIVITY = "BG_ACTIVITY";
    public static final String COLUMN_NAME_COST_ACTIVITY = "COST_ACTIVITY";
    public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
    public static final String COLUMN_NAME_REFERENCE3 = "REFERENCE3";
    public static final String COLUMN_NAME_ASSIGNMENT = "ASSIGNMENT";
    public static final String COLUMN_NAME_ASSIGNMENT_SPECIAL_GL = "ASSIGNMENT_SPECIAL_GL";
    public static final String COLUMN_NAME_BR_DOCUMENT_NO = "BR_DOCUMENT_NO";
    public static final String COLUMN_NAME_BR_LINE = "BR_LINE";
    public static final String COLUMN_NAME_PAYMENT_CENTER = "PAYMENT_CENTER";
    public static final String COLUMN_NAME_PAYMENT_CENTER_NAME = "PAYMENT_CENTER_NAME";
    public static final String COLUMN_NAME_BANK_BOOK = "BANK_BOOK";
    public static final String COLUMN_NAME_SUB_BOOK = "SUB_BOOK";
    public static final String COLUMN_NAME_SUB_ACCOUNT_TYPE = "SUB_ACCOUNT_TYPE";
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
    public static final String COLUMN_NAME_VENDOR_NAME = "VENDOR_NAME";
    public static final String COLUMN_NAME_CONFIRM_VENDOR = "CONFIRM_VENDOR";
    public static final String COLUMN_NAME_VENDOR_TAX_ID = "VENDOR_TAX_ID";
    public static final String COLUMN_NAME_PAYEE = "PAYEE";
    public static final String COLUMN_NAME_PAYEE_NAME = "PAYEE_NAME";
    public static final String COLUMN_NAME_PAYEE_TAX_ID = "PAYEE_TAX_ID";
    public static final String COLUMN_NAME_BANK_KEY = "BANK_KEY";
    public static final String COLUMN_NAME_BANK_NAME = "BANK_NAME";
    public static final String COLUMN_NAME_BANK_ACCOUNT_NO = "BANK_ACCOUNT_NO";
    public static final String COLUMN_NAME_BANK_ACCOUNT_HOLDER_NAME = "BANK_ACCOUNT_HOLDER_NAME";
    public static final String COLUMN_NAME_BANK_BRANCH_NO = "BANK_BRANCH_NO";
    public static final String COLUMN_NAME_TRADING_PARTNER = "TRADING_PARTNER";
    public static final String COLUMN_NAME_TRADING_PARTNER_PARK = "TRADING_PARTNER_PARK";
    public static final String COLUMN_NAME_SPECIAL_GL = "SPECIAL_GL";
    public static final String COLUMN_NAME_DATE_BASE_LINE = "DATE_BASE_LINE";
    public static final String COLUMN_NAME_DUE_DATE = "DUE_DATE";
    public static final String COLUMN_NAME_DATE_VALUE = "DATE_VALUE";
    public static final String COLUMN_NAME_ASSET_NO = "ASSET_NO";
    public static final String COLUMN_NAME_ASSET_SUB_NO = "ASSET_SUB_NO";
    public static final String COLUMN_NAME_QTY = "QTY";
    public static final String COLUMN_NAME_UOM = "UOM";
    public static final String COLUMN_NAME_REFERENCE1 = "REFERENCE1";
    public static final String COLUMN_NAME_REFERENCE2 = "REFERENCE2";
    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_PO_DOCUMENT_NO = "PO_DOCUMENT_NO";
    public static final String COLUMN_NAME_PO_LINE = "PO_LINE";
    public static final String COLUMN_NAME_MR_FISCAL_YEAR = "MR_FISCAL_YEAR";
    public static final String COLUMN_NAME_MR_DOCUMENT_NO = "MR_DOCUMENT_NO";
    public static final String COLUMN_NAME_MR_LINE = "MR_LINE";
    public static final String COLUMN_NAME_INVOICE_FISCAL_YEAR = "INVOICE_FISCAL_YEAR";
    public static final String COLUMN_NAME_INVOICE_DOCUMENT_NO = "INVOICE_DOCUMENT_NO";
    public static final String COLUMN_NAME_INVOICE_LINE = "INVOICE_LINE";
    public static final String COLUMN_NAME_REFERENCE_INVOICE_FISCAL_YEAR = "REFERENCE_INVOICE_FISCAL_YEAR";
    public static final String COLUMN_NAME_REFERENCE_INVOICE_DOCUMENT_NO = "REFERENCE_INVOICE_DOCUMENT_NO";
    public static final String COLUMN_NAME_REFERENCE_INVOICE_LINE = "REFERENCE_INVOICE_LINE";
    public static final String COLUMN_NAME_CLEARING_FISCAL_YEAR = "CLEARING_FISCAL_YEAR";
    public static final String COLUMN_NAME_CLEARING_DOCUMENT_NO = "CLEARING_DOCUMENT_NO";
    public static final String COLUMN_NAME_CLEARING_DOCUMENT_TYPE = "CLEARING_DOCUMENT_TYPE";
    public static final String COLUMN_NAME_CLEARING_DATE_DOC = "CLEARING_DATE_DOC";
    public static final String COLUMN_NAME_CLEARING_DATE_ACCT = "CLEARING_DATE_ACCT";
    public static final String COLUMN_NAME_INCOME = "INCOME";
    public static final String COLUMN_NAME_PAYMENT_BLOCK = "PAYMENT_BLOCK";
    public static final String COLUMN_NAME_PAYMENT_REFERENCE = "PAYMENT_REFERENCE";
    public static final String COLUMN_NAME_AUTO_GEN = "AUTO_GEN";
    public static final String COLUMN_NAME_WTX = "WTX";
    public static final String COLUMN_NAME_LINE = "LINE";
    public static final String COLUMN_NAME_BG_ACCOUNT = "BG_ACCOUNT";
    public static final String COLUMN_NAME_FUND_CENTER = "FUND_CENTER";
    public static final String COLUMN_NAME_ORIGINAL_DOCUMENT_NO = "ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_FUND_TYPE = "FUND_TYPE";
    public static final String COLUMN_NAME_GL_HEAD_ID = "GL_HEAD_ID";

    private String postingKey;
    private String accountType;
    private String drCr;
    private String glAccount;
    private String fiArea;
    private String fiAreaName;
    private String costCenter;
    private String fundSource;
    private String bgCode;
    private String bgActivity;
    private String costActivity;
    private BigDecimal amount;
    private String reference3;
    private String assignment;
    private String assignmentSpecialGL;
    private String brDocumentNo;
    private int brLine;
    private String paymentCenter;
    private String paymentCenterName;
    private String bankBook;
    private String subBook;
    private String subAccountType;
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
    private String vendorName;
    private boolean confirmVendor;
    private String vendorTaxId;
    private String payee;
    private String payeeName;
    private String payeeTaxId;
    private String bankKey;
    private String bankName;
    private String bankAccountNo;
    private String bankAccountHolderName;
    private String bankBranchNo;
    private String tradingPartner;
    private String tradingPartnerPark;
    private String specialGL;
    private Timestamp dateBaseLine;
    private Timestamp dueDate;
    private Timestamp dateValue;
    private String assetNo;
    private String assetSubNo;
    private BigDecimal qty;
    private String uom;
    private String reference1;
    private String reference2;
    private String companyCode;
    private String poDocumentNo;
    private int poLine;
    private String mrFiscalYear;
    private String mrDocumentNo;
    private int mrLine;
    private String invoiceFiscalYear;
    private String invoiceDocumentNo;
    private int invoiceLine;
    private String referenceInvoiceFiscalYear;
    private String referenceInvoiceDocumentNo;
    private int referenceInvoiceLine;
    private String clearingFiscalYear;
    private String clearingDocumentNo;
    private String clearingDocumentType;
    private Timestamp clearingDateDoc;
    private Timestamp clearingDateAcct;
    private String income;
    private String paymentBlock;
    private String paymentReference;
    private boolean autoGen;
    private boolean wtx;
    private int line;
    private String bgAccount;
    private String fundCenter;
    private String originalDocumentNo;
    private String originalFiscalYear;
    private String fundType;
    private Long glHeadId;

    public GLLine(ZFILine zfILine, GLHead glHead) {
        this.postingKey = zfILine.getPostingKey();
        this.accountType = zfILine.getAccountType();
        this.drCr = zfILine.getDrCr();
        this.glAccount = zfILine.getGlAccount();
        this.fiArea = zfILine.getFiArea();
        this.costCenter = zfILine.getCostCenter();
        this.fundSource = zfILine.getFundSource();
        this.bgCode = zfILine.getBgCode();
        this.bgActivity = zfILine.getBgActivity();
        this.costActivity = zfILine.getCostActivity();
        this.amount = zfILine.getAmount();
        this.reference3 = zfILine.getReference3();
        this.assignment = zfILine.getAssignment();
        this.assignmentSpecialGL = zfILine.getAssignmentSpecialGL();
        this.brDocumentNo = zfILine.getBrDocNo();
        this.brLine = zfILine.getBrLine();
        this.paymentCenter = zfILine.getPaymentCenter();
        this.bankBook = zfILine.getBankBook();
        this.subBook = zfILine.getSubBook();
        this.subAccountType = zfILine.getSubAccountType();
        this.subAccount = zfILine.getSubAccount();
        this.subAccountOwner = zfILine.getSubAccountOwner();
        this.depositAccount = zfILine.getDepositAccount();
        this.depositAccountOwner = zfILine.getDepositAccountOwner();
        this.gpsc = zfILine.getGpsc();
        this.gpscGroup = zfILine.getGpscGroup();
        this.lineItemText = zfILine.getLineItemText();
        this.lineDesc = zfILine.getLineDesc();
        this.paymentTerm = zfILine.getPaymentTerm();
        this.paymentMethod = zfILine.getPaymentMethod();
        this.wtxType = zfILine.getWtxType();
        this.wtxCode = zfILine.getWtxCode();
        this.wtxBase = zfILine.getWtxBase();
        this.wtxAmount = zfILine.getWtxAmount();
        this.wtxTypeP = zfILine.getWtxTypeP();
        this.wtxCodeP = zfILine.getWtxCodeP();
        this.wtxBaseP = zfILine.getWtxBaseP();
        this.wtxAmountP = zfILine.getWtxAmountP();
        this.vendor = zfILine.getVendor();
//        this.vendorName = zfILine.getVendor();
//        this.confirmVendor = zfILine.getCon();
        this.vendorTaxId = zfILine.getVendorTaxID();
        this.payee = zfILine.getPayee();
        this.payeeTaxId = zfILine.getPayeeTaxID();
        this.bankAccountNo = zfILine.getBankAccountNo();
//        this.bankAccountHolderName = zfILine.getBank();
        this.bankBranchNo = zfILine.getBankBranchNo();
        this.tradingPartner = zfILine.getTradingPartner();
        this.tradingPartnerPark = zfILine.getTradingPartnerPark();
        this.specialGL = zfILine.getSpecialGL();
        this.dateBaseLine = zfILine.getDateBaseLine();
        this.dueDate = zfILine.getDueDate();
        this.dateValue = zfILine.getDateValue();
        this.assetNo = zfILine.getAssetNo();
        this.assetSubNo = zfILine.getAssetSubNo();
        this.qty = zfILine.getQty();
        this.uom = zfILine.getUom();
        this.reference1 = zfILine.getReference1();
        this.reference2 = zfILine.getReference2();
        this.poDocumentNo = zfILine.getPoDocNo();
        this.poLine = zfILine.getPoLine();
        this.mrFiscalYear = zfILine.getMrFiscalYear();
        this.mrDocumentNo = zfILine.getMrDocNo();
        this.mrLine = zfILine.getMrLine();
        this.invoiceFiscalYear = zfILine.getInvFiscalYear();
        this.invoiceDocumentNo = zfILine.getInvDocNo();
        this.invoiceLine = zfILine.getInvLine();
        this.referenceInvoiceFiscalYear = zfILine.getRefInvFiscalYear();
        this.referenceInvoiceDocumentNo = zfILine.getRefInvDocNo();
        this.referenceInvoiceLine = zfILine.getRefInvLine();
        this.clearingFiscalYear = zfILine.getClearingFiscalYear();
        this.clearingDocumentNo = zfILine.getClearingDocNo();
        this.clearingDocumentType = zfILine.getClearingDocType();
        this.clearingDateDoc = zfILine.getClearingDateDoc();
        this.clearingDateAcct = zfILine.getClearingDateAcct();
        this.income = zfILine.getIncome();
        this.paymentBlock = zfILine.getPaymentBlock();
        this.paymentReference = zfILine.getPaymentRef();
        this.autoGen = zfILine.isAutoGen();
        this.wtx = zfILine.isWtx();
        this.line = zfILine.getLine();
        this.bgAccount = zfILine.getBgAccount();
        this.fundCenter = zfILine.getFundCenter();
        this.companyCode = glHead.getCompanyCode();
        this.originalDocumentNo = glHead.getOriginalDocumentNo();
        this.originalFiscalYear = glHead.getOriginalFiscalYear();
//        this.fundType = zfILine.get();
        this.glHeadId = glHead.getId();
    }


    public GLLine(Long id, String postingKey, String accountType, String drCr, String glAccount, String fiArea, String fiAreaName,
                  String costCenter, String fundSource, String bgCode, String bgActivity, String costActivity,
                  BigDecimal amount, String reference3, String assignment, String assignmentSpecialGL,
                  String brDocumentNo, int brLine, String paymentCenter, String paymentCenterName, String bankBook, String subBook,
                  String subAccountType, String subAccount, String subAccountOwner, String depositAccount,
                  String depositAccountOwner, String gpsc, String gpscGroup, String lineItemText,
                  String lineDesc, String paymentTerm, String paymentMethod, String wtxType,
                  String wtxCode, BigDecimal wtxBase, BigDecimal wtxAmount, String wtxTypeP, String wtxCodeP,
                  BigDecimal wtxBaseP, BigDecimal wtxAmountP, String vendor, String vendorName,
                  boolean confirmVendor, String vendorTaxId, String payee, String payeeName, String payeeTaxId, String bankKey, String bankName,
                  String bankAccountNo, String bankAccountHolderName, String bankBranchNo, String tradingPartner, String tradingPartnerPark,
                  String specialGL, Timestamp dateBaseLine, Timestamp dueDate, Timestamp dateValue, String assetNo,
                  String assetSubNo, BigDecimal qty, String uom, String reference1, String reference2, String companyCode,
                  String poDocumentNo, int poLine, String mrFiscalYear, String mrDocumentNo, int mrLine, String invoiceFiscalYear,
                  String invoiceDocumentNo, int invoiceLine, String referenceInvoiceFiscalYear, String referenceInvoiceDocumentNo,
                  int referenceInvoiceLine, String clearingFiscalYear, String clearingDocumentNo,
                  String clearingDocumentType, Timestamp clearingDateDoc, Timestamp clearingDateAcct,
                  String income, String paymentBlock, String paymentReference, boolean autoGen, boolean wtx,
                  int line, String bgAccount, String fundCenter, String originalDocumentNo, String originalFiscalYear,
                  String fundType, Long glHeadId) {
        super(id);
        this.postingKey = postingKey;
        this.accountType = accountType;
        this.drCr = drCr;
        this.glAccount = glAccount;
        this.fiArea = fiArea;
        this.fiAreaName = fiAreaName;
        this.costCenter = costCenter;
        this.fundSource = fundSource;
        this.bgCode = bgCode;
        this.bgActivity = bgActivity;
        this.costActivity = costActivity;
        this.amount = amount;
        this.reference3 = reference3;
        this.assignment = assignment;
        this.assignmentSpecialGL = assignmentSpecialGL;
        this.brDocumentNo = brDocumentNo;
        this.brLine = brLine;
        this.paymentCenter = paymentCenter;
        this.paymentCenterName = paymentCenterName;
        this.bankBook = bankBook;
        this.subBook = subBook;
        this.subAccountType = subAccountType;
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
        this.vendorName = vendorName;
        this.confirmVendor = confirmVendor;
        this.vendorTaxId = vendorTaxId;
        this.payee = payee;
        this.payeeName = payeeName;
        this.payeeTaxId = payeeTaxId;
        this.bankKey = bankKey;
        this.bankName = bankName;
        this.bankAccountNo = bankAccountNo;
        this.bankAccountHolderName = bankAccountHolderName;
        this.bankBranchNo = bankBranchNo;
        this.tradingPartner = tradingPartner;
        this.tradingPartnerPark = tradingPartnerPark;
        this.specialGL = specialGL;
        this.dateBaseLine = dateBaseLine;
        this.dueDate = dueDate;
        this.dateValue = dateValue;
        this.assetNo = assetNo;
        this.assetSubNo = assetSubNo;
        this.qty = qty;
        this.uom = uom;
        this.reference1 = reference1;
        this.reference2 = reference2;
        this.companyCode = companyCode;
        this.poDocumentNo = poDocumentNo;
        this.poLine = poLine;
        this.mrFiscalYear = mrFiscalYear;
        this.mrDocumentNo = mrDocumentNo;
        this.mrLine = mrLine;
        this.invoiceFiscalYear = invoiceFiscalYear;
        this.invoiceDocumentNo = invoiceDocumentNo;
        this.invoiceLine = invoiceLine;
        this.referenceInvoiceFiscalYear = referenceInvoiceFiscalYear;
        this.referenceInvoiceDocumentNo = referenceInvoiceDocumentNo;
        this.referenceInvoiceLine = referenceInvoiceLine;
        this.clearingFiscalYear = clearingFiscalYear;
        this.clearingDocumentNo = clearingDocumentNo;
        this.clearingDocumentType = clearingDocumentType;
        this.clearingDateDoc = clearingDateDoc;
        this.clearingDateAcct = clearingDateAcct;
        this.income = income;
        this.paymentBlock = paymentBlock;
        this.paymentReference = paymentReference;
        this.autoGen = autoGen;
        this.wtx = wtx;
        this.line = line;
        this.bgAccount = bgAccount;
        this.fundCenter = fundCenter;
        this.originalDocumentNo = originalDocumentNo;
        this.originalFiscalYear = originalFiscalYear;
        this.fundType = fundType;
        this.glHeadId = glHeadId;
    }

    @Override
    public String toString() {
        return "GLLine{" +
                "postingKey='" + postingKey + '\'' +
                ", accountType='" + accountType + '\'' +
                ", drCr='" + drCr + '\'' +
                ", glAccount='" + glAccount + '\'' +
                ", fiArea='" + fiArea + '\'' +
                ", costCenter='" + costCenter + '\'' +
                ", fundSource='" + fundSource + '\'' +
                ", bgCode='" + bgCode + '\'' +
                ", bgActivity='" + bgActivity + '\'' +
                ", costActivity='" + costActivity + '\'' +
                ", amount=" + amount +
                ", reference3='" + reference3 + '\'' +
                ", assignment='" + assignment + '\'' +
                ", assignmentSpecialGL='" + assignmentSpecialGL + '\'' +
                ", brDocumentNo='" + brDocumentNo + '\'' +
                ", brLine=" + brLine +
                ", paymentCenter='" + paymentCenter + '\'' +
                ", bankBook='" + bankBook + '\'' +
                ", subBook='" + subBook + '\'' +
                ", subAccountType='" + subAccountType + '\'' +
                ", subAccount='" + subAccount + '\'' +
                ", subAccountOwner='" + subAccountOwner + '\'' +
                ", depositAccount='" + depositAccount + '\'' +
                ", depositAccountOwner='" + depositAccountOwner + '\'' +
                ", gpsc='" + gpsc + '\'' +
                ", gpscGroup='" + gpscGroup + '\'' +
                ", lineItemText='" + lineItemText + '\'' +
                ", lineDesc='" + lineDesc + '\'' +
                ", paymentTerm='" + paymentTerm + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", wtxType='" + wtxType + '\'' +
                ", wtxCode='" + wtxCode + '\'' +
                ", wtxBase=" + wtxBase +
                ", wtxAmount=" + wtxAmount +
                ", wtxTypeP='" + wtxTypeP + '\'' +
                ", wtxCodeP='" + wtxCodeP + '\'' +
                ", wtxBaseP=" + wtxBaseP +
                ", wtxAmountP=" + wtxAmountP +
                ", vendor='" + vendor + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", confirmVendor=" + confirmVendor +
                ", vendorTaxId='" + vendorTaxId + '\'' +
                ", payee='" + payee + '\'' +
                ", payeeTaxId='" + payeeTaxId + '\'' +
                ", bankAccountNo='" + bankAccountNo + '\'' +
                ", bankAccountHolderName='" + bankAccountHolderName + '\'' +
                ", bankBranchNo='" + bankBranchNo + '\'' +
                ", tradingPartner='" + tradingPartner + '\'' +
                ", tradingPartnerPark='" + tradingPartnerPark + '\'' +
                ", specialGL='" + specialGL + '\'' +
                ", dateBaseLine=" + dateBaseLine +
                ", dueDate=" + dueDate +
                ", dateValue=" + dateValue +
                ", assetNo='" + assetNo + '\'' +
                ", assetSubNo='" + assetSubNo + '\'' +
                ", qty=" + qty +
                ", uom='" + uom + '\'' +
                ", reference1='" + reference1 + '\'' +
                ", reference2='" + reference2 + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", poDocumentNo='" + poDocumentNo + '\'' +
                ", poLine=" + poLine +
                ", mrFiscalYear='" + mrFiscalYear + '\'' +
                ", mrDocumentNo='" + mrDocumentNo + '\'' +
                ", mrLine=" + mrLine +
                ", invoiceFiscalYear='" + invoiceFiscalYear + '\'' +
                ", invoiceDocumentNo='" + invoiceDocumentNo + '\'' +
                ", invoiceLine=" + invoiceLine +
                ", referenceInvoiceFiscalYear='" + referenceInvoiceFiscalYear + '\'' +
                ", referenceInvoiceDocumentNo='" + referenceInvoiceDocumentNo + '\'' +
                ", referenceInvoiceLine=" + referenceInvoiceLine +
                ", clearingFiscalYear='" + clearingFiscalYear + '\'' +
                ", clearingDocumentNo='" + clearingDocumentNo + '\'' +
                ", clearingDocumentType='" + clearingDocumentType + '\'' +
                ", clearingDateDoc=" + clearingDateDoc +
                ", clearingDateAcct=" + clearingDateAcct +
                ", income='" + income + '\'' +
                ", paymentBlock='" + paymentBlock + '\'' +
                ", paymentReference='" + paymentReference + '\'' +
                ", autoGen=" + autoGen +
                ", wtx=" + wtx +
                ", line=" + line +
                ", bgAccount='" + bgAccount + '\'' +
                ", fundCenter='" + fundCenter + '\'' +
                ", originalDocumentNo='" + originalDocumentNo + '\'' +
                ", originalFiscalYear='" + originalFiscalYear + '\'' +
                ", fundType='" + fundType + '\'' +
                ", glHeadId=" + glHeadId +
                '}';
    }
}
