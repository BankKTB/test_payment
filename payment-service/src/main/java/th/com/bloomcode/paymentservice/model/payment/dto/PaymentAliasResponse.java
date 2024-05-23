package th.com.bloomcode.paymentservice.model.payment.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PaymentAliasResponse {

    public static final String COLUMN_NAME_PAYMENT_ALIAS_ID = "ID";
    public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
    public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";
    public static final String COLUMN_NAME_PARAMETER_STATUS = "PARAMETER_STATUS";
    public static final String COLUMN_NAME_PROPOSAL_STATUS = "PROPOSAL_STATUS";
    public static final String COLUMN_NAME_RUN_STATUS = "RUN_STATUS";
    public static final String COLUMN_NAME_PROPOSAL_JOB_STATUS = "PROPOSAL_JOB_STATUS";
    public static final String COLUMN_NAME_RUN_JOB_STATUS = "RUN_JOB_STATUS";
    public static final String COLUMN_NAME_STATUS_NAME = "STATUS_NAME";
    public static final String COLUMN_NAME_PARAMETER_JSON_TEXT = "PARAMETER_JSON_TEXT";

    public static final String COLUMN_NAME_PROPOSAL_START = "PROPOSAL_START";
    public static final String COLUMN_NAME_PROPOSAL_END = "PROPOSAL_END";
    public static final String COLUMN_NAME_RUN_START = "RUN_START";
    public static final String COLUMN_NAME_RUN_END = "RUN_END";

    private Long id;
    private Timestamp paymentDate;
    private String paymentName;
    private String parameterStatus;
    private String proposalStatus;
    private String runStatus;
    private String proposalJobStatus;
    private String runJobStatus;
    private String parameterJsonText;
    private String statusName;
    private Timestamp proposalStart;
    private Timestamp proposalEnd;
    private Timestamp runStart;
    private Timestamp runEnd;

    public PaymentAliasResponse(Long id, Timestamp paymentDate, String paymentName, String parameterStatus, String proposalStatus, String runStatus, String proposalJobStatus, String runJobStatus, String parameterJsonText, String statusName
            , Timestamp proposalStart, Timestamp proposalEnd, Timestamp runStart, Timestamp runEnd) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.paymentName = paymentName;
        this.parameterStatus = parameterStatus;
        this.proposalStatus = proposalStatus;
        this.runStatus = runStatus;
        this.proposalJobStatus = proposalJobStatus;
        this.runJobStatus = runJobStatus;
        this.parameterJsonText = parameterJsonText;
        this.statusName = statusName;

        this.proposalStart = proposalStart;
        this.proposalEnd = proposalEnd;
        this.runStart = runStart;
        this.runEnd = runEnd;

    }
}