package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SwiftFileLog extends BaseModel {

    public static final String TABLE_NAME = "SWIFT_FILE_LOG";

    public static final String COLUMN_NAME_SWIFT_FILE_LOG_ID = "ID";
    public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
    public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";
    public static final String COLUMN_NAME_VENDOR = "VENDOR";
    public static final String COLUMN_NAME_BANK_KEY = "BANK_KEY";
    public static final String COLUMN_NAME_BANK_ACCOUNT_NO = "BANK_ACCOUNT_NO";
    public static final String COLUMN_NAME_PAYMENT_METHOD = "PAYMENT_METHOD";
    public static final String COLUMN_NAME_PAYING_COMP_CODE = "PAYING_COMP_CODE";
    public static final String COLUMN_NAME_PAYMENT_DOC_NO = "PAYMENT_DOC_NO";
    public static final String COLUMN_NAME_PAYMENT_YEAR = "PAYMENT_YEAR";
    public static final String COLUMN_NAME_COMP_CODE = "COMP_CODE";
    public static final String COLUMN_NAME_INV_DOC_NO = "INV_DOC_NO";
    public static final String COLUMN_NAME_FISCAL_YEAR = "FISCAL_YEAR";
    public static final String COLUMN_NAME_FI_AREA = "FI_AREA";
    public static final String COLUMN_NAME_TRANSFER_LEVEL = "TRANSFER_LEVEL";
    public static final String COLUMN_NAME_FEE = "FEE";
    public static final String COLUMN_NAME_CREDIT_MEMO = "CREDIT_MEMO";
    public static final String COLUMN_NAME_ORIGINAL_ACC_DOC_NO = "ORIGINAL_ACC_DOC_NO";
    public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_ORIGINAL_COMP_CODE = "ORIGINAL_COMP_CODE";
    public static final String COLUMN_NAME_ORIGINAL_DOC_TYPE = "ORIGINAL_DOC_TYPE";
    public static final String COLUMN_NAME_ORIGINAL_WTX_AMOUNT = "ORIGINAL_WTX_AMOUNT";
    public static final String COLUMN_NAME_ORIGINAL_WTX_BASE = "ORIGINAL_WTX_BASE";
    public static final String COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P = "ORIGINAL_WTX_AMOUNT_P";
    public static final String COLUMN_NAME_ORIGINAL_WTX_BASE_P = "ORIGINAL_WTX_BASE_P";
    public static final String COLUMN_NAME_PAYMENT_FISCAL_YEAR = "PAYMENT_FISCAL_YEAR";
    public static final String COLUMN_NAME_PAYMENT_COMP_CODE = "PAYMENT_COMP_CODE";
    public static final String COLUMN_NAME_SWIFT_FILE_ID = "SWIFT_FILE_ID";

    private Timestamp paymentDate;
    private String paymentName;
    private String vendor;
    private String bankKey;
    private String bankAccountNo;
    private String paymentMethod;
    private String payingCompCode;
    private String paymentDocNo;
    private String paymentYear;
    private String compCode;
    private String invDocNo;
    private String fiscalYear;
    private String fiArea;
    private String transferLevel;
    private BigDecimal fee;
    private BigDecimal creditMemo;
    private String originalAccDocNo;
    private String originalFiscalYear;
    private String originalCompCode;
    private String originalDocType;
    private BigDecimal originalWtxAmount;
    private BigDecimal originalWtxBase;
    private BigDecimal originalWtxAmountP;
    private BigDecimal originalWtxBaseP;
    private String invoiceAccDocNo;
    private String invoiceFiscalYear;
    private String invoiceCompCode;
    private String invoiceDocType;
    private BigDecimal invoiceWtxAmount;
    private BigDecimal invoiceWtxBase;
    private BigDecimal invoiceWtxAmountP;
    private BigDecimal invoiceWtxBaseP;
    private String paymentFiscalYear;
    private String paymentCompCode;
    private Long swiftFileId;

//    @OneToOne(mappedBy = "swiftFileLog", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, optional = false)
//    private SwiftFile swiftFile;
}
