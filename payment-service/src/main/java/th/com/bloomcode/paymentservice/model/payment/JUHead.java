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
public class JUHead extends BaseModel {

    public static final String TABLE_NAME = "JU_HEAD";

    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
    public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";
    public static final String COLUMN_NAME_DOC_TYPE = "DOC_TYPE";
    public static final String COLUMN_NAME_DOCUMENT_NO = "DOCUMENT_NO";
    public static final String COLUMN_NAME_REFERENCE = "REFERENCE";
    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_PAYMENT_CENTER = "PAYMENT_CENTER";
    public static final String COLUMN_NAME_USERNAME = "USERNAME";
    public static final String COLUMN_NAME_DOCUMENT_STATUS = "DOCUMENT_STATUS";
    public static final String COLUMN_NAME_DOCUMENT_STATUS_NAME = "DOCUMENT_STATUS_NAME";
    public static final String COLUMN_NAME_DATE_DOC = "DATE_DOC";
    public static final String COLUMN_NAME_DATE_ACCT = "DATE_ACCT";
    public static final String COLUMN_NAME_TRANSFER_DATE = "TRANSFER_DATE";
    public static final String COLUMN_NAME_AMOUNT_CR = "AMOUNT_CR";
    public static final String COLUMN_NAME_GL_ACCOUNT_CR = "GL_ACCOUNT_CR";
    public static final String COLUMN_NAME_TEST_RUN = "TEST_RUN";
    public static final String COLUMN_NAME_MESSAGE_QUEUE_ID = "MESSAGE_QUEUE_ID";
    public static final String COLUMN_NAME_FISCAL_YEAR = "FISCAL_YEAR";

    private Timestamp paymentDate;
    private String paymentName;
    private String documentType;
    private String documentNo;
    private String reference;
    private String companyCode;
    private String paymentCenter;
    private String username;
    private String documentStatus;
    private String documentStatusName;
    private Timestamp dateDoc;
    private Timestamp dateAcct;
    private Timestamp transferDate;
    private BigDecimal amountCr;
    private BigDecimal glAccountCr;
    private Boolean testRun;
    private String messageQueueId;
    private String fiscalYear;

    public JUHead(Long id, Timestamp paymentDate, String paymentName, String documentType, String documentNo, String reference, String companyCode
            , String paymentCenter, String username, String documentStatus, String documentStatusName, Timestamp dateDoc, Timestamp dateAcct
            , Timestamp transferDate, BigDecimal amountCr, BigDecimal glAccountCr, Boolean testRun, String messageQueueId, String fiscalYear) {
        super(id);
        this.paymentDate = paymentDate;
        this.paymentName = paymentName;
        this.documentType = documentType;
        this.documentNo = documentNo;
        this.reference = reference;
        this.companyCode = companyCode;
        this.paymentCenter = paymentCenter;
        this.username = username;
        this.documentStatus = documentStatus;
        this.documentStatusName = documentStatusName;
        this.dateDoc = dateDoc;
        this.dateAcct = dateAcct;
        this.transferDate = transferDate;
        this.amountCr = amountCr;
        this.glAccountCr = glAccountCr;
        this.testRun = testRun;
        this.messageQueueId = messageQueueId;
        this.fiscalYear = fiscalYear;
    }
}