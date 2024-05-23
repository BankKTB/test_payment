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
public class UnBlockDocumentLog extends BaseModel {

    public static final String TABLE_NAME = "UNBLOCK_DOCUMENT_LOG";

    public static final String COLUMN_NAME_UNBLOCK_DOCUMENT_LOG_ID = "ID";
    public static final String COLUMN_NAME_VALUE_OLD = "VALUE_OLD";
    public static final String COLUMN_NAME_VALUE_NEW = "VALUE_NEW";
    public static final String COLUMN_NAME_UNBLOCK_DATE = "UNBLOCK_DATE";
    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_FI_AREA = "FI_AREA";
    public static final String COLUMN_NAME_PAYMENT_CENTER = "PAYMENT_CENTER";
    public static final String COLUMN_NAME_DOCUMENT_TYPE = "DOCUMENT_TYPE";
    public static final String COLUMN_NAME_ORIGINAL_DOCUMENT_NO = "ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_DATE_ACCT = "DATE_ACCT";
    public static final String COLUMN_NAME_PAYMENT_METHOD = "PAYMENT_METHOD";
    public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
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
    public static final String COLUMN_NAME_USER_POST = "USER_POST";
    public static final String COLUMN_NAME_USER_NAME = "USER_NAME";
    public static final String COLUMN_NAME_REASON = "REASON";
    public static final String COLUMN_NAME_IDEM_STATUS = "IDEM_STATUS";

    public static final String COLUMN_NAME_GROUP_DOC = "GROUP_DOC";

    public static final String COLUMN_NAME_DOCUMENT_HEADER_TEXT = "DOCUMENT_HEADER_TEXT";

    private String valueOld;
    private String valueNew;
    private Timestamp unblockDate;
    private String companyCode;
    private String fiArea;
    private String paymentCenter;
    private String documentType;
    private String originalFiscalYear;
    private String originalDocumentNo;
    private Timestamp dateAcct;
    private String paymentMethod;
    private BigDecimal amount;
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
    private String userPost;
    private String userName;
    private String reason;
    private String idemStatus;

    private String groupDoc;
    private String documentHeaderText;

    public UnBlockDocumentLog(Long id, String valueOld, String valueNew, Timestamp unblockDate, String companyCode, String fiArea, String paymentCenter, String documentType, String originalFiscalYear, String originalDocumentNo, Timestamp dateAcct, String paymentMethod, BigDecimal amount, String wtxType, String wtxCode, BigDecimal wtxBase, BigDecimal wtxAmount, String wtxTypeP, String wtxCodeP, BigDecimal wtxBaseP, BigDecimal wtxAmountP, String vendor, String vendorName, String userPost, String userName, String reason, String idemStatus) {
        super(id);
        this.valueOld = valueOld;
        this.valueNew = valueNew;
        this.unblockDate = unblockDate;
        this.companyCode = companyCode;
        this.fiArea = fiArea;
        this.paymentCenter = paymentCenter;
        this.documentType = documentType;
        this.originalFiscalYear = originalFiscalYear;
        this.originalDocumentNo = originalDocumentNo;
        this.dateAcct = dateAcct;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
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
        this.userPost = userPost;
        this.userName = userName;
        this.reason = reason;
        this.idemStatus = idemStatus;
    }

    @Override
    public String toString() {
        return "UnBlockDocumentLog{" +
                "valueOld='" + valueOld + '\'' +
                ", valueNew='" + valueNew + '\'' +
                ", unblockDate=" + unblockDate +
                ", companyCode='" + companyCode + '\'' +
                ", fiArea='" + fiArea + '\'' +
                ", paymentCenter='" + paymentCenter + '\'' +
                ", documentType='" + documentType + '\'' +
                ", originalFiscalYear='" + originalFiscalYear + '\'' +
                ", originalDocumentNo='" + originalDocumentNo + '\'' +
                ", dateAcct=" + dateAcct +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", amount=" + amount +
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
                ", userPost='" + userPost + '\'' +
                ", userName='" + userName + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
