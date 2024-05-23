package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ProposalLog extends BaseModel {
    public static final String TABLE_NAME = "PROPOSAL_LOG";

    public static final String COLUMN_NAME_PROPOSAL_LOG_ID = "ID";
    public static final String COLUMN_NAME_REF_RUNNING = "REF_RUNNING";
    public static final String COLUMN_NAME_REF_LINE = "REF_LINE";
    public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
    public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";
    public static final String COLUMN_NAME_ACCOUNT_NO_FROM = "ACCOUNT_NO_FROM";
    public static final String COLUMN_NAME_ACCOUNT_NO_TO = "ACCOUNT_NO_TO";
    public static final String COLUMN_NAME_FILE_TYPE = "FILE_TYPE";
    public static final String COLUMN_NAME_FILE_NAME = "FILE_NAME";
    public static final String COLUMN_NAME_TRANSFER_DATE = "TRANSFER_DATE";
    public static final String COLUMN_NAME_VENDOR = "VENDOR";
    public static final String COLUMN_NAME_BANK_KEY = "BANK_KEY";
    public static final String COLUMN_NAME_VENDOR_BANK_ACCOUNT = "VENDOR_BANK_ACCOUNT";
    public static final String COLUMN_NAME_TRANSFER_LEVEL = "TRANSFER_LEVEL";
    public static final String COLUMN_NAME_PAY_ACCOUNT = "PAY_ACCOUNT";
    public static final String COLUMN_NAME_PAYING_COMP_CODE = "PAYING_COMP_CODE";
    public static final String COLUMN_NAME_PAYMENT_DOCUMENT = "PAYMENT_DOCUMENT";
    public static final String COLUMN_NAME_PAYMENT_FISCAL_YEAR = "PAYMENT_FISCAL_YEAR";
    public static final String COLUMN_NAME_REV_PAYMENT_DOCUMENT = "REV_PAYMENT_DOCUMENT";
    public static final String COLUMN_NAME_REV_PAYMENT_FISCAL_YEAR = "REV_PAYMENT_FISCAL_YEAR";
    public static final String COLUMN_NAME_PAYMENT_COMP_CODE = "PAYMENT_COMP_CODE";
    public static final String COLUMN_NAME_FISCAL_YEAR = "FISCAL_YEAR";
    public static final String COLUMN_NAME_FI_AREA = "FI_AREA";
    public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
    public static final String COLUMN_NAME_BANK_FEE = "BANK_FEE";
    public static final String COLUMN_NAME_CREDIT_MEMO_AMOUNT = "CREDIT_MEMO_AMOUNT";
    public static final String COLUMN_NAME_CANCEL_DATE = "CANCEL_DATE";
    public static final String COLUMN_NAME_IS_RERUN = "IS_RERUN";
    public static final String COLUMN_NAME_INV_COMP_CODE = "INV_COMP_CODE";
    public static final String COLUMN_NAME_INV_DOC_NO = "INV_DOC_NO";
    public static final String COLUMN_NAME_INV_FISCAL_YEAR = "INV_FISCAL_YEAR";
    public static final String COLUMN_NAME_INV_DOC_TYPE = "INV_DOC_TYPE";
    public static final String COLUMN_NAME_REV_INV_DOC_NO = "REV_INV_DOC_NO";
    public static final String COLUMN_NAME_REV_INV_FISCAL_YEAR = "REV_INV_FISCAL_YEAR";
    public static final String COLUMN_NAME_ORIGINAL_COMP_CODE = "ORIGINAL_COMP_CODE";
    public static final String COLUMN_NAME_ORIGINAL_DOC_NO = "ORIGINAL_DOC_NO";
    public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_ORIGINAL_DOC_TYPE = "ORIGINAL_DOC_TYPE";
    public static final String COLUMN_NAME_REV_ORIGINAL_DOC_NO = "REV_ORIGINAL_DOC_NO";
    public static final String COLUMN_NAME_REV_ORIGINAL_FISCAL_YEAR = "REV_ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_REV_ORIGINAL_REASON = "REV_ORIGINAL_REASON";
    public static final String COLUMN_NAME_ORIGINAL_WTX_AMOUNT = "ORIGINAL_WTX_AMOUNT";
    public static final String COLUMN_NAME_ORIGINAL_WTX_BASE = "ORIGINAL_WTX_BASE";
    public static final String COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P = "ORIGINAL_WTX_AMOUNT_P";
    public static final String COLUMN_NAME_ORIGINAL_WTX_BASE_P = "ORIGINAL_WTX_BASE_P";
    public static final String COLUMN_NAME_FILE_STATUS = "FILE_STATUS";
    public static final String COLUMN_NAME_SEND_STATUS = "SEND_STATUS";
    public static final String COLUMN_NAME_IS_JU_CREATE = "IS_JU_CREATE";
    public static final String COLUMN_NAME_INV_WTX_AMOUNT = "INV_WTX_AMOUNT";
    public static final String COLUMN_NAME_INV_WTX_BASE = "INV_WTX_BASE";
    public static final String COLUMN_NAME_INV_WTX_AMOUNT_P = "INV_WTX_AMOUNT_P";
    public static final String COLUMN_NAME_INV_WTX_BASE_P = "INV_WTX_BASE_P";
    public static final String COLUMN_NAME_PAYMENT_TYPE = "PAYMENT_TYPE";
    public static final String COLUMN_NAME_PROPOSAL_LOG_HEADER_ID = "PROPOSAL_LOG_HEADER_ID";
    public static final String COLUMN_NAME_RETURN_DATE = "RETURN_DATE";
    public static final String COLUMN_NAME_RETURN_BY = "RETURN_BY";
    public static final String COLUMN_NAME_REF_RUNNING_SUM = "REF_RUNNING_SUM";
    public static final String COLUMN_NAME_REF_LINE_SUM = "REF_LINE_SUM";

    private Long refRunning;
    private int refLine;
    private Timestamp paymentDate;
    private String paymentName;
    private String accountNoFrom;
    private String accountNoTo;
    private String fileType;
    private String fileName;
    private Timestamp transferDate;
    private String vendor;
    private String bankKey;
    private String vendorBankAccount;
    private String transferLevel;
    private String payAccount;
    private String payingCompCode;
    private String paymentDocument;
    private String paymentFiscalYear;
    private String revPaymentDocument;
    private String revPaymentFiscalYear;
    private String paymentCompCode;
    private String fiscalYear;
    private String fiArea;
    private BigDecimal amount;
    private BigDecimal bankFee;
    private BigDecimal creditMemoAmount;
    private Timestamp cancelDate;
    private boolean rerun;
    private String invCompCode;
    private String invDocNo;
    private String invFiscalYear;
    private String invDocType;
    private String revInvDocNo;
    private String revInvFiscalYear;
    private BigDecimal invWtxAmount;
    private BigDecimal invWtxBase;
    private BigDecimal invWtxAmountP;
    private BigDecimal invWtxBaseP;
    private String originalCompCode;
    private String originalDocNo;
    private String originalFiscalYear;
    private String originalDocType;
    private String revOriginalDocNo;
    private String revOriginalFiscalYear;
    private String revOriginalReason;
    private BigDecimal originalWtxAmount;
    private BigDecimal originalWtxBase;
    private BigDecimal originalWtxAmountP;
    private BigDecimal originalWtxBaseP;
    private String fileStatus;
    private String sendStatus;
    private boolean juCreate;
    private String paymentType;
    private Timestamp returnDate;
    private String returnBy;
    private Long refRunningSum;
    private int refLineSum;
    private Long proposalLogHeaderId;

    // for interface d1 d2
    private ProposalLogHeader proposalLogHeader;

    public ProposalLog(Long id, Timestamp created, String createdBy, Timestamp updated, String updatedBy, Long refRunning, int refLine, Timestamp paymentDate, String paymentName, String accountNoFrom, String accountNoTo, String fileType, String fileName, Timestamp transferDate, String vendor, String bankKey, String vendorBankAccount, String transferLevel, String payAccount, String payingCompCode, String paymentDocument, String paymentFiscalYear, String revPaymentDocument, String revPaymentFiscalYear, String paymentCompCode, String fiscalYear, String fiArea, BigDecimal amount, BigDecimal bankFee, BigDecimal creditMemoAmount, Timestamp cancelDate, boolean rerun, String invCompCode, String invDocNo, String invFiscalYear, String invDocType, String revInvDocNo, String revInvFiscalYear, String originalCompCode, String originalDocNo, String originalFiscalYear, String originalDocType, String revOriginalDocNo, String revOriginalFiscalYear, String revOriginalReason, BigDecimal originalWtxAmount, BigDecimal originalWtxBase, BigDecimal originalWtxAmountP, BigDecimal originalWtxBaseP, String fileStatus, String sendStatus, boolean juCreate, BigDecimal invWtxAmount, BigDecimal invWtxBase, BigDecimal invWtxAmountP, BigDecimal invWtxBaseP, String paymentType, Timestamp returnDate, String returnBy, Long refRunningSum, int refLineSum, Long proposalLogHeaderId) {
        super(id, created, createdBy, updated, updatedBy);
        this.refRunning = refRunning;
        this.refLine = refLine;
        this.paymentDate = paymentDate;
        this.paymentName = paymentName;
        this.accountNoFrom = accountNoFrom;
        this.accountNoTo = accountNoTo;
        this.fileType = fileType;
        this.fileName = fileName;
        this.transferDate = transferDate;
        this.vendor = vendor;
        this.bankKey = bankKey;
        this.vendorBankAccount = vendorBankAccount;
        this.transferLevel = transferLevel;
        this.payAccount = payAccount;
        this.payingCompCode = payingCompCode;
        this.paymentDocument = paymentDocument;
        this.paymentFiscalYear = paymentFiscalYear;
        this.revPaymentDocument = revPaymentDocument;
        this.revPaymentFiscalYear = revPaymentFiscalYear;
        this.paymentCompCode = paymentCompCode;
        this.fiscalYear = fiscalYear;
        this.fiArea = fiArea;
        this.amount = amount;
        this.bankFee = bankFee;
        this.creditMemoAmount = creditMemoAmount;
        this.cancelDate = cancelDate;
        this.rerun = rerun;
        this.invCompCode = invCompCode;
        this.invDocNo = invDocNo;
        this.invFiscalYear = invFiscalYear;
        this.invDocType = invDocType;
        this.revInvDocNo = revInvDocNo;
        this.revInvFiscalYear = revInvFiscalYear;
        this.originalCompCode = originalCompCode;
        this.originalDocNo = originalDocNo;
        this.originalFiscalYear = originalFiscalYear;
        this.originalDocType = originalDocType;
        this.revOriginalDocNo = revOriginalDocNo;
        this.revOriginalFiscalYear = revOriginalFiscalYear;
        this.revOriginalReason = revOriginalReason;
        this.originalWtxAmount = originalWtxAmount;
        this.originalWtxBase = originalWtxBase;
        this.originalWtxAmountP = originalWtxAmountP;
        this.originalWtxBaseP = originalWtxBaseP;
        this.fileStatus = fileStatus;
        this.sendStatus = sendStatus;
        this.juCreate = juCreate;
        this.invWtxAmount = invWtxAmount;
        this.invWtxBase = invWtxBase;
        this.invWtxAmountP = invWtxAmountP;
        this.invWtxBaseP = invWtxBaseP;
        this.paymentType = paymentType;
        this.returnDate = returnDate;
        this.returnBy = returnBy;
        this.refRunningSum = refRunningSum;
        this.refLineSum = refLineSum;
        this.proposalLogHeaderId = proposalLogHeaderId;
    }

    @Override
    public String toString() {
        return "ProposalLog{" +
                "refRunning=" + refRunning +
                ", refLine=" + refLine +
                ", paymentDate=" + paymentDate +
                ", paymentName='" + paymentName + '\'' +
                ", accountNoFrom='" + accountNoFrom + '\'' +
                ", accountNoTo='" + accountNoTo + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", transferDate=" + transferDate +
                ", vendor='" + vendor + '\'' +
                ", bankKey='" + bankKey + '\'' +
                ", vendorBankAccount='" + vendorBankAccount + '\'' +
                ", transferLevel='" + transferLevel + '\'' +
                ", payAccount='" + payAccount + '\'' +
                ", payingCompCode='" + payingCompCode + '\'' +
                ", paymentDocument='" + paymentDocument + '\'' +
                ", paymentFiscalYear='" + paymentFiscalYear + '\'' +
                ", revPaymentDocument='" + revPaymentDocument + '\'' +
                ", revPaymentFiscalYear='" + revPaymentFiscalYear + '\'' +
                ", paymentCompCode='" + paymentCompCode + '\'' +
                ", fiscalYear='" + fiscalYear + '\'' +
                ", fiArea='" + fiArea + '\'' +
                ", amount=" + amount +
                ", bankFee=" + bankFee +
                ", creditMemoAmount=" + creditMemoAmount +
                ", cancelDate=" + cancelDate +
                ", rerun=" + rerun +
                ", invCompCode='" + invCompCode + '\'' +
                ", invDocNo='" + invDocNo + '\'' +
                ", invFiscalYear='" + invFiscalYear + '\'' +
                ", invDocType='" + invDocType + '\'' +
                ", revInvDocNo='" + revInvDocNo + '\'' +
                ", revInvFiscalYear='" + revInvFiscalYear + '\'' +
                ", invWtxAmount=" + invWtxAmount +
                ", invWtxBase=" + invWtxBase +
                ", invWtxAmountP=" + invWtxAmountP +
                ", invWtxBaseP=" + invWtxBaseP +
                ", originalCompCode='" + originalCompCode + '\'' +
                ", originalDocNo='" + originalDocNo + '\'' +
                ", originalFiscalYear='" + originalFiscalYear + '\'' +
                ", originalDocType='" + originalDocType + '\'' +
                ", revOriginalDocNo='" + revOriginalDocNo + '\'' +
                ", revOriginalFiscalYear='" + revOriginalFiscalYear + '\'' +
                ", revOriginalReason='" + revOriginalReason + '\'' +
                ", originalWtxAmount=" + originalWtxAmount +
                ", originalWtxBase=" + originalWtxBase +
                ", originalWtxAmountP=" + originalWtxAmountP +
                ", originalWtxBaseP=" + originalWtxBaseP +
                ", fileStatus='" + fileStatus + '\'' +
                ", sendStatus='" + sendStatus + '\'' +
                ", juCreate=" + juCreate +
                ", paymentType='" + paymentType + '\'' +
                ", returnDate=" + returnDate +
                ", returnBy='" + returnBy + '\'' +
                ", refRunningSum=" + refRunningSum +
                ", refLineSum=" + refLineSum +
                ", proposalLogHeaderId=" + proposalLogHeaderId +
                ", proposalLogHeader=" + proposalLogHeader +
                '}';
    }
}
