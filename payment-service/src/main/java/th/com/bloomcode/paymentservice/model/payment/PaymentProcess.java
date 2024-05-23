package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareProposalDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareSelectProposalDocument;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class PaymentProcess extends BaseModel {

    public static final String TABLE_NAME = "PAYMENT_PROCESS";

    public static final String COLUMN_NAME_PAYMENT_PROCESS_ID = "ID";
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
    public static final String COLUMN_NAME_COST_CENTER = "COST_CENTER";
    public static final String COLUMN_NAME_COST_CENTER_NAME = "COST_CENTER_NAME";
    public static final String COLUMN_NAME_CURRENCY = "CURRENCY";
    public static final String COLUMN_NAME_DATE_ACCT = "DATE_ACCT";
    public static final String COLUMN_NAME_DATE_BASE_LINE = "DATE_BASE_LINE";
    public static final String COLUMN_NAME_DATE_DOC = "DATE_DOC";
    public static final String COLUMN_NAME_DR_CR = "DR_CR";
    public static final String COLUMN_NAME_ERROR_CODE = "ERROR_CODE";
    public static final String COLUMN_NAME_FI_AREA = "FI_AREA";
    public static final String COLUMN_NAME_FI_AREA_NAME = "FI_AREA_NAME";
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
    public static final String COLUMN_NAME_INVOICE_AMOUNT_PAID = "INVOICE_AMOUNT_PAID";
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
    public static final String COLUMN_NAME_IS_CHILD = "IS_CHILD";
    public static final String COLUMN_NAME_LINE = "LINE";
    public static final String COLUMN_NAME_LINE_ITEM_TEXT = "LINE_ITEM_TEXT";
    public static final String COLUMN_NAME_ORIGINAL_AMOUNT = "ORIGINAL_AMOUNT";
    public static final String COLUMN_NAME_ORIGINAL_AMOUNT_PAID = "ORIGINAL_AMOUNT_PAID";
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
    public static final String COLUMN_NAME_PAYMENT_ALIAS_ID = "PAYMENT_ALIAS_ID";

    public static final String COLUMN_NAME_REVERSE_PAYMENT_COMPANY_CODE = "REVERSE_PAYMENT_COMPANY_CODE";
    public static final String COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_NO = "REVERSE_PAYMENT_DOCUMENT_NO";
    public static final String COLUMN_NAME_REVERSE_PAYMENT_FISCAL_YEAR = "REVERSE_PAYMENT_FISCAL_YEAR";
    public static final String COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_TYPE = "REVERSE_PAYMENT_DOCUMENT_TYPE";
    public static final String COLUMN_NAME_PAYMENT_DOCUMENT_TYPE = "PAYMENT_DOCUMENT_TYPE";

    public static final String COLUMN_NAME_IDEM_UPDATE = "IDEM_UPDATE";
    public static final String COLUMN_NAME_IS_HAVE_CHILD = "IS_HAVE_CHILD";
    public static final String COLUMN_NAME_CREDIT_MEMO = "CREDIT_MEMO";
    public static final String COLUMN_NAME_WTX_CREDIT_MEMO = "WTX_CREDIT_MEMO";
    public static final String COLUMN_NAME_REVERSE_PAYMENT_STATUS = "REVERSE_PAYMENT_STATUS";

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
    private String costCenter;
    private String costCenterName;
    private String currency;
    private Timestamp dateAcct;
    private Timestamp dateBaseLine;
    private Timestamp dateDoc;
    private String drCr;
    private String errorCode;
    private String fiArea;
    private String fiAreaName;
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
    private BigDecimal invoiceAmountPaid;
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
    private boolean child;
    private int line;
    private String lineItemText;
    private BigDecimal originalAmount;
    private BigDecimal originalAmountPaid;
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
    private String paymentBlock;
    private String paymentCenter;
    private String paymentCompanyCode;
    private String paymentCompanyName;
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
    private boolean proposal;
    private boolean proposalBlock;
    private String reference1;
    private String reference2;
    private String reference3;
    private String specialGL;
    private String specialGLName;
    private String status;
    private String tradingPartner;
    private String tradingPartnerName;
    private Long paymentAliasId;
    private PaymentAlias paymentAlias;

    private String reversePaymentCompanyCode;
    private String reversePaymentDocumentNo;
    private String reversePaymentFiscalYear;
    private String reversePaymentDocumentType;
    private String paymentDocumentType;

    private Timestamp idemUpdate;

    private boolean haveChild;
    private BigDecimal creditMemo = BigDecimal.ZERO;
    private BigDecimal wtxCreditMemo  = BigDecimal.ZERO;
    private String reversePaymentStatus;

    public PaymentProcess(
            PrepareProposalDocument paymentDocument, boolean isProposal, boolean isChild) {
        this.accountType = paymentDocument.getAccountType();
        this.assetNo = paymentDocument.getAssetNo();
        this.assetSubNo = paymentDocument.getAssetSubNo();
        this.assignment = paymentDocument.getAssignment();
        this.bankAccountNo = paymentDocument.getBankAccountNo();
        this.brDocumentNo = paymentDocument.getBrDocumentNo();
        this.brLine = paymentDocument.getBrLine();
        //        this.budgetAccount = paymentDocument;
        this.budgetActivity = paymentDocument.getBgActivity();
        this.budgetActivityName = paymentDocument.getBgActivityName();
        this.costCenter = paymentDocument.getCostCenter();
        this.costCenterName = paymentDocument.getCostCenterName();
        //        this.creditMemoDocumentNo = paymentDocument;
        //        this.creditMemoFiscalYear = paymentDocument;
        //        this.creditMemoLine = paymentDocument;
        this.currency = paymentDocument.getCurrency();
        this.dateAcct = paymentDocument.getDateAcct();
        this.dateBaseLine = paymentDocument.getDateBaseLine();
        this.dateDoc = paymentDocument.getDateDoc();
        this.drCr = paymentDocument.getDrCr();
        this.errorCode = paymentDocument.getErrorCode();
        this.fiArea = paymentDocument.getFiArea();
        this.fiAreaName = paymentDocument.getAreaName();
        //        this.fundCenterName = paymentDocument;
        this.fundSource = paymentDocument.getFundSource();
        this.fundSourceName = paymentDocument.getFundSourceName();
        this.glAccount = paymentDocument.getGlAccount();
//        this.glAccountName = paymentDocument.getGlAccount();
        this.headerReference = paymentDocument.getHeaderReference();
        this.houseBank = paymentDocument.getHouseBank();
        this.idemStatus = "";
        /// find by glHead
        this.invoiceAmount = paymentDocument.getAmount();
        this.invoiceAmountPaid = paymentDocument.getAmountPaid();
        this.invoiceCompanyCode = paymentDocument.getCompanyCode();
        this.invoiceCompanyName = paymentDocument.getCompCodeName();
        this.invoiceDocumentNo = paymentDocument.getOriginalDocumentNo();
        this.invoiceDocumentType = paymentDocument.getDocumentType();
        this.invoiceFiscalYear = paymentDocument.getOriginalFiscalYear();
        this.invoicePaymentCenter = paymentDocument.getPaymentCenter();
        this.invoiceWtxAmount = paymentDocument.getWtxAmount();
        this.invoiceWtxAmountP = paymentDocument.getWtxAmountP();
        this.invoiceWtxBase = paymentDocument.getWtxBase();
        this.invoiceWtxBaseP = paymentDocument.getWtxBaseP();
        this.child = isChild;
        this.line = paymentDocument.getLine();
        this.lineItemText = paymentDocument.getLineItemText();
//        this.originalAmount = paymentDocument.getAmount();
//        this.originalAmountPaid = paymentDocument.getAmountPaid();
//        this.originalCompanyCode = paymentDocument.getCompanyCode();
//        this.originalCompanyName = paymentDocument.getCompCodeName();
//        this.originalDocumentNo = paymentDocument.getOriginalDocumentNo();
//        this.originalDocumentType = paymentDocument.getDocumentType();
//        this.originalFiscalYear = paymentDocument.getOriginalFiscalYear();
//        this.originalPaymentCenter = paymentDocument.getPaymentCenter();
//        this.originalWtxAmount = paymentDocument.getWtxAmount();
//        this.originalWtxAmountP = paymentDocument.getWtxAmountP();
//        this.originalWtxBase = paymentDocument.getWtxBase();
//        this.originalWtxBaseP = paymentDocument.getWtxBaseP();
        //        this.parentCompanyCode = paymentDocument;
        //        this.parentDocumentNo = paymentDocument;
        //        this.parentFiscalYear = paymentDocument;
        this.paymentBlock = paymentDocument.getPaymentBlock();
//        this.paymentCenter = paymentDocument.getPaymentCenter();
        //        this.paymentCompanyCode = paymentDocument;
        this.paymentDate = paymentDocument.getPaymentDate();
        this.paymentDateAcct = paymentDocument.getPaymentClearingDate();
        this.paymentDocumentNo = paymentDocument.getPaymentClearingDocNo();
        this.paymentFiscalYear = paymentDocument.getPaymentClearingYear();
        this.paymentMethod = paymentDocument.getPaymentMethod();
        this.paymentMethodName = paymentDocument.getPaymentMethodName();
        this.paymentName = paymentDocument.getPaymentName();
        this.paymentReference = paymentDocument.getPaymentRef();
        this.paymentTerm = paymentDocument.getPaymentTerm();
        //        this.pmGroupDoc = paymentDocument;
        //        this.pmGroupNo = paymentDocument;
        this.poDocumentNo = paymentDocument.getPoDocNo();
        this.poLine = paymentDocument.getPoLine();
        this.postingKey = paymentDocument.getPostingKey();
        this.proposal = isProposal;
        //        this.proposalBlock = paymentDocument;
        this.reference1 = paymentDocument.getReference1();
        this.reference2 = paymentDocument.getReference2();
        this.reference3 = paymentDocument.getReference3();
        this.specialGL = paymentDocument.getSpecialGL();
        this.specialGLName = paymentDocument.getSpecialGL();
        this.status = paymentDocument.getStatus();
        this.tradingPartner = paymentDocument.getTradingPartner();
        this.tradingPartnerName = paymentDocument.getTradingPartner();
        this.paymentAliasId = paymentDocument.getPaymentId();
    }

    public PaymentProcess(
            PrepareSelectProposalDocument paymentDocument, boolean isProposal, boolean isChild) {
        this.accountType = paymentDocument.getAccountType();
        this.assetNo = paymentDocument.getAssetNo();
        this.assetSubNo = paymentDocument.getAssetSubNo();
        this.assignment = paymentDocument.getAssignment();
        this.bankAccountNo = paymentDocument.getBankAccountNo();
        this.brDocumentNo = paymentDocument.getBrDocumentNo();
        this.brLine = paymentDocument.getBrLine();
        //        this.budgetAccount = paymentDocument;
        this.budgetActivity = paymentDocument.getBgActivity();
        this.budgetActivityName = paymentDocument.getBgActivityName();
        this.costCenter = paymentDocument.getCostCenter();
        this.costCenterName = paymentDocument.getCostCenterName();
        //        this.creditMemoDocumentNo = paymentDocument;
        //        this.creditMemoFiscalYear = paymentDocument;
        //        this.creditMemoLine = paymentDocument;
        this.currency = paymentDocument.getCurrency();
        this.dateAcct = paymentDocument.getDateAcct();
        this.dateBaseLine = paymentDocument.getDateBaseLine();
        this.dateDoc = paymentDocument.getDateDoc();
        this.drCr = paymentDocument.getDrCr();
        this.errorCode = paymentDocument.getErrorCode();
        this.fiArea = paymentDocument.getFiArea();
        this.fiAreaName = paymentDocument.getMainAreaName();
        //        this.fundCenterName = paymentDocument;
        this.fundSource = paymentDocument.getFundSource();
        this.fundSourceName = paymentDocument.getFundSourceName();
        this.glAccount = paymentDocument.getGlAccount();
//        this.glAccountName = paymentDocument.getGlAccount();
        this.headerReference = paymentDocument.getHeaderReference();
        this.houseBank = paymentDocument.getHouseBank();
        this.idemStatus = "";
        /// find by glHead
        this.invoiceAmount = paymentDocument.getAmount();
        this.invoiceAmountPaid = paymentDocument.getAmountPaid();
        this.invoiceCompanyCode = paymentDocument.getCompanyCode();
        this.invoiceCompanyName = paymentDocument.getCompCodeName();
        this.invoiceDocumentNo = paymentDocument.getOriginalDocumentNo();
        this.invoiceDocumentType = paymentDocument.getDocumentType();
        this.invoiceFiscalYear = paymentDocument.getOriginalFiscalYear();
        this.invoicePaymentCenter = paymentDocument.getPaymentCenter();
        this.invoiceWtxAmount = paymentDocument.getWtxAmount();
        this.invoiceWtxAmountP = paymentDocument.getWtxAmountP();
        this.invoiceWtxBase = paymentDocument.getWtxBase();
        this.invoiceWtxBaseP = paymentDocument.getWtxBaseP();
        this.child = isChild;
        this.line = paymentDocument.getLine();
        this.lineItemText = paymentDocument.getLineItemText();
//        this.originalAmount = paymentDocument.getAmount();
//        this.originalAmountPaid = paymentDocument.getAmountPaid();
//        this.originalCompanyCode = paymentDocument.getCompanyCode();
//        this.originalCompanyName = paymentDocument.getCompCodeName();
//        this.originalDocumentNo = paymentDocument.getOriginalDocumentNo();
//        this.originalDocumentType = paymentDocument.getDocumentType();
//        this.originalFiscalYear = paymentDocument.getOriginalFiscalYear();
//        this.originalPaymentCenter = paymentDocument.getPaymentCenter();
//        this.originalWtxAmount = paymentDocument.getWtxAmount();
//        this.originalWtxAmountP = paymentDocument.getWtxAmountP();
//        this.originalWtxBase = paymentDocument.getWtxBase();
//        this.originalWtxBaseP = paymentDocument.getWtxBaseP();
        //        this.parentCompanyCode = paymentDocument;
        //        this.parentDocumentNo = paymentDocument;
        //        this.parentFiscalYear = paymentDocument;
        this.paymentBlock = paymentDocument.getPaymentBlock();
//        this.paymentCenter = paymentDocument.getPaymentCenter();
        //        this.paymentCompanyCode = paymentDocument;
        this.paymentDate = paymentDocument.getPaymentDate();
        this.paymentDateAcct = paymentDocument.getPaymentClearingDate();
        this.paymentDocumentNo = paymentDocument.getPaymentClearingDocNo();
        this.paymentFiscalYear = paymentDocument.getPaymentClearingYear();
        this.paymentMethod = paymentDocument.getPaymentMethod();
        this.paymentMethodName = paymentDocument.getPaymentMethodName();
        this.paymentName = paymentDocument.getPaymentName();
        this.paymentReference = paymentDocument.getPaymentRef();
        this.paymentTerm = paymentDocument.getPaymentTerm();
        //        this.pmGroupDoc = paymentDocument;
        //        this.pmGroupNo = paymentDocument;
        this.poDocumentNo = paymentDocument.getPoDocNo();
        this.poLine = paymentDocument.getPoLine();
        this.postingKey = paymentDocument.getPostingKey();
        this.proposal = isProposal;
        //        this.proposalBlock = paymentDocument;
        this.reference1 = paymentDocument.getReference1();
        this.reference2 = paymentDocument.getReference2();
        this.reference3 = paymentDocument.getReference3();
        this.specialGL = paymentDocument.getSpecialGL();
        this.specialGLName = paymentDocument.getSpecialGL();
        this.status = paymentDocument.getStatus();
        this.tradingPartner = paymentDocument.getTradingPartner();
        this.tradingPartnerName = paymentDocument.getTradingPartner();
        this.paymentAliasId = paymentDocument.getPaymentId();
    }

    public PaymentProcess(
            PrepareRunDocument paymentDocument) {
        this.accountType = paymentDocument.getAccountType();
        this.assetNo = paymentDocument.getAssetNo();
        this.assetSubNo = paymentDocument.getAssetSubNo();
        this.assignment = paymentDocument.getAssignment();
        this.bankAccountNo = paymentDocument.getBankAccountNo();
        this.brDocumentNo = paymentDocument.getBrDocumentNo();
        this.brLine = paymentDocument.getBrLine();
        //        this.budgetAccount = paymentDocument;
        this.budgetActivity = paymentDocument.getBudgetActivity();
        this.budgetActivityName = paymentDocument.getBudgetActivityName();
        this.costCenter = paymentDocument.getCostCenter();
        this.costCenterName = paymentDocument.getCostCenterName();
        //        this.creditMemoDocumentNo = paymentDocument;
        //        this.creditMemoFiscalYear = paymentDocument;
        //        this.creditMemoLine = paymentDocument;
        this.currency = paymentDocument.getCurrency();
        this.dateAcct = paymentDocument.getDateAcct();
        this.dateBaseLine = paymentDocument.getDateBaseLine();
        this.dateDoc = paymentDocument.getDateDoc();
        this.drCr = paymentDocument.getDrCr();
        this.errorCode = paymentDocument.getErrorCode();
        this.fiArea = paymentDocument.getFiArea();
        this.fiAreaName = paymentDocument.getFiAreaName();
        //        this.fundCenterName = paymentDocument;
        this.fundSource = paymentDocument.getFundSource();
        this.fundSourceName = paymentDocument.getFundSourceName();
        this.glAccount = paymentDocument.getGlAccount();
//        this.glAccountName = paymentDocument.getGlAccount();
        this.headerReference = paymentDocument.getHeaderReference();
        this.houseBank = paymentDocument.getHouseBank();
        this.idemStatus = "W";
        this.invoiceAmount = paymentDocument.getInvoiceAmount();
        this.invoiceAmountPaid = paymentDocument.getInvoiceAmountPaid();
        this.invoiceCompanyCode = paymentDocument.getInvoiceCompanyCode();
        this.invoiceCompanyName = paymentDocument.getInvoiceCompanyName();
        this.invoiceDocumentNo = paymentDocument.getInvoiceDocumentNo();
        this.invoiceDocumentType = paymentDocument.getInvoiceDocumentType();
        this.invoiceFiscalYear = paymentDocument.getOriginalFiscalYear();
        this.invoicePaymentCenter = paymentDocument.getInvoicePaymentCenter();
        this.invoiceWtxAmount = paymentDocument.getInvoiceWtxAmount();
        this.invoiceWtxAmountP = paymentDocument.getInvoiceWtxAmountP();
        this.invoiceWtxBase = paymentDocument.getInvoiceWtxBase();
        this.invoiceWtxBaseP = paymentDocument.getInvoiceWtxBaseP();
//        this.child = false;
        this.line = paymentDocument.getLine();
        this.lineItemText = paymentDocument.getLineItemText();
        this.originalAmount = paymentDocument.getOriginalAmount();
        this.originalAmountPaid = paymentDocument.getOriginalAmountPaid();
        this.originalCompanyCode = paymentDocument.getOriginalCompanyCode();
        this.originalCompanyName = paymentDocument.getOriginalCompanyName();
        this.originalDocumentNo = paymentDocument.getOriginalDocumentNo();
        this.originalDocumentType = paymentDocument.getOriginalDocumentType();
        this.originalFiscalYear = paymentDocument.getOriginalFiscalYear();
        this.originalPaymentCenter = paymentDocument.getPaymentCenter();
        this.originalWtxAmount = paymentDocument.getOriginalWtxAmount();
        this.originalWtxAmountP = paymentDocument.getOriginalWtxAmountP();
        this.originalWtxBase = paymentDocument.getOriginalWtxBase();
        this.originalWtxBaseP = paymentDocument.getOriginalWtxBaseP();
        //        this.parentCompanyCode = paymentDocument;
        //        this.parentDocumentNo = paymentDocument;
        //        this.parentFiscalYear = paymentDocument;
        this.paymentBlock = paymentDocument.getPaymentBlock();
        this.paymentCenter = paymentDocument.getPaymentCenter();
        //        this.paymentCompanyCode = paymentDocument;
        this.paymentDate = paymentDocument.getPaymentDate();
        this.paymentDateAcct = paymentDocument.getPaymentDateAcct();
//        this.paymentDocumentNo = paymentDocument.getOriginalDocumentNo();
//        this.paymentFiscalYear = paymentDocument.getPaymentFiscalYear();
        this.paymentMethod = paymentDocument.getPaymentMethod();
        this.paymentMethodName = paymentDocument.getPaymentMethodName();
        this.paymentName = paymentDocument.getPaymentName();
        this.paymentReference = paymentDocument.getPaymentReference();
        this.paymentTerm = paymentDocument.getPaymentTerm();
        //        this.pmGroupDoc = paymentDocument;
        //        this.pmGroupNo = paymentDocument;
        this.poDocumentNo = paymentDocument.getPoDocumentNo();
        this.poLine = paymentDocument.getPoLine();
        this.postingKey = paymentDocument.getPostingKey();
        this.proposal = false;
        //        this.proposalBlock = paymentDocument;
        this.reference1 = paymentDocument.getReference1();
        this.reference2 = paymentDocument.getReference2();
        this.reference3 = paymentDocument.getReference3();
        this.specialGL = paymentDocument.getSpecialGl();
        this.specialGLName = paymentDocument.getSpecialGlName();
        this.status = paymentDocument.getStatus();
        this.tradingPartner = paymentDocument.getTradingPartner();
        this.tradingPartnerName = paymentDocument.getTradingPartner();
        this.paymentAliasId = paymentDocument.getPaymentAliasId();
        this.creditMemo = paymentDocument.getCreditMemo();
        this.wtxCreditMemo = paymentDocument.getWtxCreditMemo();
    }

    public PaymentProcess(
            PrepareRealRunDocument paymentDocument) {
        this.accountType = paymentDocument.getAccountType();
        this.assetNo = paymentDocument.getAssetNo();
        this.assetSubNo = paymentDocument.getAssetSubNo();
        this.assignment = paymentDocument.getAssignment();
        this.bankAccountNo = paymentDocument.getBankAccountNo();
        this.brDocumentNo = paymentDocument.getBrDocumentNo();
        this.brLine = paymentDocument.getBrLine();
        this.budgetActivity = paymentDocument.getBudgetActivity();
        this.budgetActivityName = paymentDocument.getBudgetActivityName();
        this.costCenter = paymentDocument.getCostCenter();
        this.costCenterName = paymentDocument.getCostCenterName();
        this.currency = paymentDocument.getCurrency();
        this.dateAcct = paymentDocument.getDateAcct();
        this.dateBaseLine = paymentDocument.getDateBaseLine();
        this.dateDoc = paymentDocument.getDateDoc();
        this.drCr = paymentDocument.getDrCr();
        this.errorCode = paymentDocument.getErrorCode();
        this.fiArea = paymentDocument.getFiArea();
        this.fiAreaName = paymentDocument.getFiAreaName();
        this.fundSource = paymentDocument.getFundSource();
        this.fundSourceName = paymentDocument.getFundSourceName();
        this.glAccount = paymentDocument.getGlAccount();
        this.headerReference = paymentDocument.getHeaderReference();
        this.houseBank = paymentDocument.getHouseBank();
        this.idemStatus = "W";
        this.invoiceAmount = paymentDocument.getInvoiceAmount();
        this.invoiceAmountPaid = paymentDocument.getInvoiceAmountPaid();
        this.invoiceCompanyCode = paymentDocument.getInvoiceCompanyCode();
        this.invoiceCompanyName = paymentDocument.getInvoiceCompanyName();
        this.invoiceDocumentNo = paymentDocument.getInvoiceDocumentNo();
        this.invoiceDocumentType = paymentDocument.getInvoiceDocumentType();
        this.invoiceFiscalYear = paymentDocument.getOriginalFiscalYear();
        this.invoicePaymentCenter = paymentDocument.getInvoicePaymentCenter();
        this.invoiceWtxAmount = paymentDocument.getInvoiceWtxAmount();
        this.invoiceWtxAmountP = paymentDocument.getInvoiceWtxAmountP();
        this.invoiceWtxBase = paymentDocument.getInvoiceWtxBase();
        this.invoiceWtxBaseP = paymentDocument.getInvoiceWtxBaseP();
        this.line = paymentDocument.getLine();
        this.lineItemText = paymentDocument.getLineItemText();
        this.originalAmount = paymentDocument.getOriginalAmount();
        this.originalAmountPaid = paymentDocument.getOriginalAmountPaid();
        this.originalCompanyCode = paymentDocument.getOriginalCompanyCode();
        this.originalCompanyName = paymentDocument.getOriginalCompanyName();
        this.originalDocumentNo = paymentDocument.getOriginalDocumentNo();
        this.originalDocumentType = paymentDocument.getOriginalDocumentType();
        this.originalFiscalYear = paymentDocument.getOriginalFiscalYear();
        this.originalPaymentCenter = paymentDocument.getOriginalPaymentCenter();
        this.originalWtxAmount = paymentDocument.getOriginalWtxAmount();
        this.originalWtxAmountP = paymentDocument.getOriginalWtxAmountP();
        this.originalWtxBase = paymentDocument.getOriginalWtxBase();
        this.originalWtxBaseP = paymentDocument.getOriginalWtxBaseP();

        this.paymentBlock = paymentDocument.getPaymentBlock();
        this.paymentCenter = paymentDocument.getPaymentCenter();
        this.paymentDate = paymentDocument.getPaymentDate();
        this.paymentDateAcct = paymentDocument.getPaymentDateAcct();
        this.paymentMethod = paymentDocument.getPaymentMethod();
        this.paymentMethodName = paymentDocument.getPaymentMethodName();
        this.paymentName = paymentDocument.getPaymentName();
        this.paymentReference = paymentDocument.getPaymentReference();
        this.paymentTerm = paymentDocument.getPaymentTerm();

        this.poDocumentNo = paymentDocument.getPoDocumentNo();
        this.poLine = paymentDocument.getPoLine();
        this.postingKey = paymentDocument.getPostingKey();
        this.proposal = false;

        this.reference1 = paymentDocument.getReference1();
        this.reference2 = paymentDocument.getReference2();
        this.reference3 = paymentDocument.getReference3();
        this.specialGL = paymentDocument.getSpecialGl();
        this.specialGLName = paymentDocument.getSpecialGlName();
        this.status = paymentDocument.getStatus();
        this.tradingPartner = paymentDocument.getTradingPartner();
        this.tradingPartnerName = paymentDocument.getTradingPartner();
        this.paymentAliasId = paymentDocument.getPaymentAliasId();
        this.creditMemo = paymentDocument.getCreditMemo();
        this.wtxCreditMemo = paymentDocument.getWtxCreditMemo();
    }

    public PaymentProcess(
            Long id,
            Timestamp created,
            String createdBy,
            Timestamp updated,
            String updatedBy,
            String accountType,
            String assetNo,
            String assetSubNo,
            String assignment,
            String bankAccountNo,
            String brDocumentNo,
            int brLine,
            String budgetAccount,
            String budgetActivity,
            String budgetActivityName,
            String costCenter,
            String costCenterName,
            String currency,
            Timestamp dateAcct,
            Timestamp dateBaseLine,
            Timestamp dateDoc,
            String drCr,
            String errorCode,
            String fiArea,
            String fiAreaName,
            String fundCenter,
            String fundCenterName,
            String fundSource,
            String fundSourceName,
            String glAccount,
            String glAccountName,
            String headerReference,
            String houseBank,
            String idemStatus,
            BigDecimal invoiceAmount,
            BigDecimal invoiceAmountPaid,
            String invoiceCompanyCode,
            String invoiceCompanyName,
            String invoiceDocumentNo,
            String invoiceDocumentType,
            String invoiceFiscalYear,
            String invoicePaymentCenter,
            BigDecimal invoiceWtxAmount,
            BigDecimal invoiceWtxAmountP,
            BigDecimal invoiceWtxBase,
            BigDecimal invoiceWtxBaseP,
            boolean child,
            int line,
            String lineItemText,
            BigDecimal originalAmount,
            BigDecimal originalAmountPaid,
            String originalCompanyCode,
            String originalCompanyName,
            String originalDocumentNo,
            String originalDocumentType,
            String originalFiscalYear,
            String originalPaymentCenter,
            BigDecimal originalWtxAmount,
            BigDecimal originalWtxAmountP,
            BigDecimal originalWtxBase,
            BigDecimal originalWtxBaseP,
            String parentCompanyCode,
            String parentDocumentNo,
            String parentFiscalYear,
            String paymentBlock,
            String paymentCenter,
            String paymentCompanyCode,
            Timestamp paymentDate,
            Timestamp paymentDateAcct,
            String paymentDocumentNo,
            String paymentFiscalYear,
            String paymentMethod,
            String paymentMethodName,
            String paymentName,
            String paymentReference,
            String paymentTerm,
            String pmGroupDoc,
            String pmGroupNo,
            String poDocumentNo,
            int poLine,
            String postingKey,
            boolean proposal,
            boolean proposalBlock,
            String reference1,
            String reference2,
            String reference3,
            String specialGL,
            String specialGLName,
            String status,
            String tradingPartner,
            String tradingPartnerName,
            Long paymentAliasId,
            PaymentAlias paymentAlias,
            Timestamp idemUpdate,
            boolean haveChild) {
        super(id, created, createdBy, updated, updatedBy);
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
        this.costCenter = costCenter;
        this.costCenterName = costCenterName;
        this.currency = currency;
        this.dateAcct = dateAcct;
        this.dateBaseLine = dateBaseLine;
        this.dateDoc = dateDoc;
        this.drCr = drCr;
        this.errorCode = errorCode;
        this.fiArea = fiArea;
        this.fiAreaName = fiAreaName;
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
        this.invoiceAmountPaid = invoiceAmountPaid;
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
        this.child = child;
        this.line = line;
        this.lineItemText = lineItemText;
        this.originalAmount = originalAmount;
        this.originalAmountPaid = originalAmountPaid;
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
        this.proposal = proposal;
        this.proposalBlock = proposalBlock;
        this.reference1 = reference1;
        this.reference2 = reference2;
        this.reference3 = reference3;
        this.specialGL = specialGL;
        this.specialGLName = specialGLName;
        this.status = status;
        this.tradingPartner = tradingPartner;
        this.tradingPartnerName = tradingPartnerName;
        this.paymentAliasId = paymentAliasId;
        this.paymentAlias = paymentAlias;
        this.idemUpdate = idemUpdate;
        this.haveChild = haveChild;

    }

    @Override
    public String toString() {
        return "PaymentProcess{" +
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
                ", costCenter='" + costCenter + '\'' +
                ", costCenterName='" + costCenterName + '\'' +
                ", currency='" + currency + '\'' +
                ", dateAcct=" + dateAcct +
                ", dateBaseLine=" + dateBaseLine +
                ", dateDoc=" + dateDoc +
                ", drCr='" + drCr + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", fiArea='" + fiArea + '\'' +
                ", fiAreaName='" + fiAreaName + '\'' +
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
                ", invoiceAmountPaid=" + invoiceAmountPaid +
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
                ", child=" + child +
                ", line=" + line +
                ", lineItemText='" + lineItemText + '\'' +
                ", originalAmount=" + originalAmount +
                ", originalAmountPaid=" + originalAmountPaid +
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
                ", proposal=" + proposal +
                ", proposalBlock=" + proposalBlock +
                ", reference1='" + reference1 + '\'' +
                ", reference2='" + reference2 + '\'' +
                ", reference3='" + reference3 + '\'' +
                ", specialGL='" + specialGL + '\'' +
                ", specialGLName='" + specialGLName + '\'' +
                ", status='" + status + '\'' +
                ", tradingPartner='" + tradingPartner + '\'' +
                ", tradingPartnerName='" + tradingPartnerName + '\'' +
                ", paymentAliasId=" + paymentAliasId +
                ", paymentAlias=" + paymentAlias +
                '}';
    }
}
