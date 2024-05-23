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
public class ProposalLogTR1 extends BaseModel {
    public static final String TABLE_NAME = "PROPOSAL_LOG_TR1";

    public static final String COLUMN_NAME_PROPOSAL_LOG_TR1_ID = "ID";
    public static final String COLUMN_NAME_TRANSFER_DATE = "TRANSFER_DATE";
    public static final String COLUMN_NAME_COMP_CODE = "COMP_CODE";
    public static final String COLUMN_NAME_FUND_SOURCE = "FUND_SOURCE";
    public static final String COLUMN_NAME_BUDGET_CODE = "BUDGET_CODE";
    public static final String COLUMN_NAME_Z_INDEX = "Z_INDEX";
    public static final String COLUMN_NAME_DOC_TYPE = "DOC_TYPE";
    public static final String COLUMN_NAME_GL_ACCOUNT = "GL_ACCOUNT";
    public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
    public static final String COLUMN_NAME_PERIOD_NO = "PERIOD_NO";
    public static final String COLUMN_NAME_FISCAL_YEAR = "FISCAL_YEAR";
    public static final String COLUMN_NAME_REF_RUNNING = "REF_RUNNING";
    public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
    public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";

    private Timestamp transferDate;
    private String compCode;
    private String fundSource;
    private String budgetCode;
    private String zIndex;
    private String docType;
    private String glAccount;
    private BigDecimal amount;
    private Integer periodNo;
    private String fiscalYear;
    private Long refRunning;
    private Timestamp paymentDate;
    private String paymentName;

    public ProposalLogTR1(Long id, Timestamp created, String createdBy, Timestamp updated,
                          String updatedBy, Timestamp transferDate, String compCode, String fundSource, String budgetCode,
                          String zIndex, String docType, String glAccount, BigDecimal amount, Integer periodNo,
                          String fiscalYear, Long refRunning, Timestamp paymentDate, String paymentName) {
        super(id, created, createdBy, updated, updatedBy);
        this.transferDate = transferDate;
        this.compCode = compCode;
        this.fundSource = fundSource;
        this.budgetCode = budgetCode;
        this.zIndex = zIndex;
        this.docType = docType;
        this.glAccount = glAccount;
        this.amount = amount;
        this.periodNo = periodNo;
        this.fiscalYear = fiscalYear;
        this.refRunning = refRunning;
        this.paymentDate = paymentDate;
        this.paymentName = paymentName;
    }
}
