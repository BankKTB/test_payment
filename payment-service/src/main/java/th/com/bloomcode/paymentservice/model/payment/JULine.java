package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class JULine extends BaseModel {
    public static final String TABLE_NAME = "JU_LINE";

    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_REF_RUNNING = "REF_RUNNING";
    public static final String COLUMN_NAME_PAY_ACCOUNT = "PAY_ACCOUNT";
    public static final String COLUMN_NAME_REF_LINE = "REF_LINE";
    public static final String COLUMN_NAME_ACCOUNT_NO_FROM = "ACCOUNT_NO_FROM";
    public static final String COLUMN_NAME_ACCOUNT_NO_TO = "ACCOUNT_NO_TO";
    public static final String COLUMN_NAME_FILE_TYPE = "FILE_TYPE";
    public static final String COLUMN_NAME_FILE_NAME = "FILE_NAME";
    public static final String COLUMN_NAME_AMOUNT_DR = "AMOUNT_DR";
    public static final String COLUMN_NAME_GL_ACCOUNT_DR = "GL_ACCOUNT_DR";
    public static final String COLUMN_NAME_ASSIGNMENT = "ASSIGNMENT";
    public static final String COLUMN_NAME_BG_CODE = "BG_CODE";
    public static final String COLUMN_NAME_COST_CENTER = "COST_CENTER";
    public static final String COLUMN_NAME_FI_AREA = "FI_AREA";
    public static final String COLUMN_NAME_POSTING_KEY = "POSTING_KEY";
    public static final String COLUMN_NAME_FUND_SOURCE = "FUND_SOURCE";
    public static final String COLUMN_NAME_BR_DOC_NO = "BR_DOC_NO";
    public static final String COLUMN_NAME_WTX_AMOUNT = "WTX_AMOUNT";
    public static final String COLUMN_NAME_WTX_BASE = "WTX_BASE";
    public static final String COLUMN_NAME_WTX_AMOUNT_P = "WTX_AMOUNT_P";
    public static final String COLUMN_NAME_WTX_BASE_P = "WTX_BASE_P";
    public static final String COLUMN_NAME_MAIN_ACTIVITY = "MAIN_ACTIVITY";
    public static final String COLUMN_NAME_COST_ACTIVITY = "COST_ACTIVITY";
    public static final String COLUMN_NAME_SUB_ACCOUNT = "SUB_ACCOUNT";
    public static final String COLUMN_NAME_SUB_ACCOUNT_OWNER = "SUB_ACCOUNT_OWNER";
    public static final String COLUMN_NAME_DEPOSIT_ACCOUNT = "DEPOSIT_ACCOUNT";
    public static final String COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER = "DEPOSIT_ACCOUNT_OWNER";
    public static final String COLUMN_NAME_JU_HEAD_ID = "JU_HEAD_ID";

    private Long refRunning;
    private String payAccount;
    private int refLine;
    private String accountNoFrom;
    private String accountNoTo;
    private String fileType;
    private String fileName;
    private BigDecimal amountDr;
    private BigDecimal glAccountDr;
    private String assignment;
    private String bgCode;
    private String costCenter;
    private String fiArea;
    private String postingKey;
    private String fundSource;
    private String brDocNo;
    private BigDecimal wtxAmount;
    private BigDecimal wtxBase;
    private BigDecimal wtxAmountP;
    private BigDecimal wtxBaseP;
    private String mainActivity;
    private String costActivity;
    private String subAccount;
    private String subAccountOwner;
    private String depositAccount;
    private String depositAccountOwner;
    private long juHeadId;
    public JULine(Long id
            , Long refRunning
            , String payAccount
            , int refLine
            , String accountNoFrom
            , String accountNoTo
            , String fileType
            , String fileName
            , BigDecimal amountDr
            , BigDecimal glAccountDr
            , String assignment
            , String bgCode
            , String costCenter
            , String fiArea
            , String postingKey
            , String fundSource
            , String brDocNo
            , BigDecimal wtxAmount
            , BigDecimal wtxBase
            , BigDecimal wtxAmountP
            , BigDecimal wtxBaseP
            , String mainActivity
            , String costActivity
            , String subAccount
            , String subAccountOwner
            , String depositAccount
            , String depositAccountOwner
            ,long juHeadId) {
        super(id);
        this.refRunning = refRunning;
        this.payAccount = payAccount;
        this.refLine = refLine;
        this.accountNoFrom = accountNoFrom;
        this.accountNoTo = accountNoTo;
        this.fileType = fileType;
        this.fileName = fileName;
        this.amountDr = amountDr;
        this.glAccountDr = glAccountDr;
        this.assignment = assignment;
        this.bgCode = bgCode;
        this.costCenter = costCenter;
        this.fiArea = fiArea;
        this.postingKey = postingKey;
        this.fundSource = fundSource;
        this.brDocNo = brDocNo;
        this.wtxAmount = wtxAmount;
        this.wtxBase = wtxBase;
        this.wtxAmountP = wtxAmountP;
        this.wtxBaseP = wtxBaseP;
        this.mainActivity = mainActivity;
        this.costActivity = costActivity;
        this.subAccount = subAccount;
        this.subAccountOwner = subAccountOwner;
        this.depositAccount = depositAccount;
        this.depositAccountOwner = depositAccountOwner;
        this.juHeadId = juHeadId;
    }
}
