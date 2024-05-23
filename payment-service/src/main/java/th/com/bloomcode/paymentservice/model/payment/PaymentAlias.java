package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class PaymentAlias extends BaseModel {

    public static final String TABLE_NAME = "PAYMENT_ALIAS";

    public static final String COLUMN_NAME_PAYMENT_ALIAS_ID = "ID";
    public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
    public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";
    public static final String COLUMN_NAME_PARAMETER_STATUS = "PARAMETER_STATUS";
    public static final String COLUMN_NAME_PROPOSAL_STATUS = "PROPOSAL_STATUS";
    public static final String COLUMN_NAME_RUN_STATUS = "RUN_STATUS";
    public static final String COLUMN_NAME_DOCUMENT_STATUS = "DOCUMENT_STATUS";
    public static final String COLUMN_NAME_GENERATE_STATUS = "GENERATE_STATUS";
    public static final String COLUMN_NAME_PROPOSAL_JOB_STATUS = "PROPOSAL_JOB_STATUS";
    public static final String COLUMN_NAME_RUN_JOB_STATUS = "RUN_JOB_STATUS";
    public static final String COLUMN_NAME_PAYMENT_CREATED = "PAYMENT_CREATED";
    public static final String COLUMN_NAME_PAYMENT_POSTED = "PAYMENT_POSTED";
    public static final String COLUMN_NAME_JSON_TEXT = "JSON_TEXT";
    public static final String COLUMN_NAME_PROPOSAL_TOTAL_DOCUMENT = "PROPOSAL_TOTAL_DOCUMENT";
    public static final String COLUMN_NAME_PROPOSAL_SUCCESS_DOCUMENT = "PROPOSAL_SUCCESS_DOCUMENT";
    public static final String COLUMN_NAME_PROPOSAL_SCHEDULE_DATE = "PROPOSAL_SCHEDULE_DATE";
    public static final String COLUMN_NAME_RUN_TOTAL_DOCUMENT = "RUN_TOTAL_DOCUMENT";
    public static final String COLUMN_NAME_RUN_SUCCESS_DOCUMENT = "RUN_SUCCESS_DOCUMENT";
    public static final String COLUMN_NAME_RUN_REVERSE_DOCUMENT = "RUN_REVERSE_DOCUMENT";
    public static final String COLUMN_NAME_RUN_SCHEDULE_DATE = "RUN_SCHEDULE_DATE";
    public static final String COLUMN_NAME_PROPOSAL_TRIGGERS_ID = "PROPOSAL_TRIGGERS_ID";
    public static final String COLUMN_NAME_PAYMENT_TRIGGERS_ID = "PAYMENT_TRIGGERS_ID";

    public static final String COLUMN_NAME_PROPOSAL_START = "PROPOSAL_START";
    public static final String COLUMN_NAME_PROPOSAL_END = "PROPOSAL_END";
    public static final String COLUMN_NAME_RUN_START = "RUN_START";
    public static final String COLUMN_NAME_RUN_END = "RUN_END";
    public static final String COLUMN_NAME_IDEM_END = "IDEM_END";

    //show
    public static final String COLUMN_NAME_IDEM_CREATE_PAYMENT_REPLY = "IDEM_CREATE_PAYMENT_REPLY";
    public static final String COLUMN_NAME_IDEM_REVERSE_PAYMENT_REPLY = "IDEM_REVERSE_PAYMENT_REPLY";
    public static final String COLUMN_NAME_RUN_REPAIR_DOCUMENT = "RUN_REPAIR_DOCUMENT";
    public static final String COLUMN_NAME_RUN_ERROR_DOCUMENT = "RUN_ERROR_DOCUMENT";

    private Timestamp paymentDate;
    private String paymentName;
    private String parameterStatus;
    private String proposalStatus;
    private String runStatus;
    private String documentStatus;
    private String generateStatus;
    private String proposalJobStatus;
    private String runJobStatus;
    private Long paymentCreated;
    private Long paymentPosted;
    private String jsonText;
    private Timestamp proposalStart;
    private Timestamp proposalEnd;
    private int proposalTotalDocument = 0;
    private int proposalSuccessDocument = 0;
    private Timestamp proposalScheduleDate;
    private Timestamp runStart;
    private Timestamp runEnd;
    private int runTotalDocument = 0;
    private int runSuccessDocument = 0;

    private int runReverseDocument = 0;
    private Timestamp runScheduleDate;
    private Long proposalTriggersId;
    private Long paymentTriggersId;
    private Timestamp idemEnd;
    //show
    private int idemCreatePaymentReply;
    private int idemReversePaymentReply;
    private int runRepairDocument = 0;
    private int runErrorDocument = 0;
//  @OneToOne(mappedBy = "paymentAlias", cascade = CascadeType.ALL,
//          fetch = FetchType.LAZY, optional = false)
//  private PaymentParameterConfig paymentParameterConfig;
//    @OneToMany(mappedBy = "paymentAlias", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
//    private List<SmartFileLog> smartFileLogs;

//    @OneToOne(mappedBy = "paymentAlias", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, optional = false)
//    private ProposalLogHeader proposalLogHeader;

    public PaymentAlias(Long id, Timestamp paymentDate, String paymentName, String parameterStatus, String proposalStatus, String runStatus, String documentStatus, String generateStatus, String proposalJobStatus, String runJobStatus, Long paymentCreated, Long paymentPosted, String jsonText, int proposalTotalDocument, int proposalSuccessDocument, Timestamp proposalScheduleDate, int runTotalDocument, int runSuccessDocument, Timestamp runScheduleDate, Long proposalTriggersId, Long paymentTriggersId
            , Timestamp proposalStart, Timestamp proposalEnd, Timestamp runStart, Timestamp runEnd, int runReverseDocument, Timestamp idemEnd, int idemCreatePaymentReply, int idemReversePaymentReply
     ,int runRepairDocument, int runErrorDocument) {
        super(id);
        this.paymentDate = paymentDate;
        this.paymentName = paymentName;
        this.parameterStatus = parameterStatus;
        this.proposalStatus = proposalStatus;
        this.runStatus = runStatus;
        this.documentStatus = documentStatus;
        this.generateStatus = generateStatus;
        this.proposalJobStatus = proposalJobStatus;
        this.runJobStatus = runJobStatus;
        this.paymentCreated = paymentCreated;
        this.paymentPosted = paymentPosted;
        this.jsonText = jsonText;
        this.proposalTotalDocument = proposalTotalDocument;
        this.proposalSuccessDocument = proposalSuccessDocument;
        this.proposalScheduleDate = proposalScheduleDate;
        this.runTotalDocument = runTotalDocument;
        this.runSuccessDocument = runSuccessDocument;
        this.runScheduleDate = runScheduleDate;
        this.proposalTriggersId = proposalTriggersId;

        this.paymentTriggersId = paymentTriggersId;

        this.proposalStart = proposalStart;
        this.proposalEnd = proposalEnd;
        this.runStart = runStart;
        this.runEnd = runEnd;
        this.runReverseDocument = runReverseDocument;
        this.idemEnd = idemEnd;
        this.idemCreatePaymentReply = idemCreatePaymentReply;
        this.idemReversePaymentReply = idemReversePaymentReply;
        this.runRepairDocument = runRepairDocument;
        this.runErrorDocument = runErrorDocument;

    }
}
