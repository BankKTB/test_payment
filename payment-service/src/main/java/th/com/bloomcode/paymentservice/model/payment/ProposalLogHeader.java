package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProposalLogHeader extends BaseModel {

    public static final String TABLE_NAME = "PROPOSAL_LOG_HEADER";

    public static final String COLUMN_NAME_PROPOSAL_LOG_HEADER_ID = "ID";
    public static final String COLUMN_NAME_REF_RUNNING = "REF_RUNNING";
    public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
    public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";
    public static final String COLUMN_NAME_SUM_RECORD = "SUM_RECORD";
    public static final String COLUMN_NAME_IS_CANCEL = "IS_CANCEL";
    public static final String COLUMN_NAME_IS_USE = "IS_USE";
//    public static final String COLUMN_NAME_CREATED = "CREATED";
//    public static final String COLUMN_NAME_CREATED_BY = "CREATED_BY";
    public static final String COLUMN_NAME_GENERATE_FILE_ALIAS_ID = "GENERATE_FILE_ALIAS_ID";

    private Long refRunning;
    private Timestamp paymentDate;
    private String paymentName;
    private boolean sumRecord;
    private boolean cancel;
    private boolean use;
//    private Timestamp created;
//    private String createdBy;
    private Long generateFileAliasId;
    private GenerateFileAlias generateFileAlias;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "proposalLogHeader", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ProposalLog> proposalLogs;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "PAYMENT_ALIAS_ID")
//    private PaymentAlias paymentAlias;

    private List<ProposalLog> interfaceD1D2 = new ArrayList<>();

    public ProposalLogHeader(Long id, Timestamp created, String createdBy, Timestamp updated, String updatedBy, Long refRunning, Timestamp paymentDate, String paymentName, boolean sumRecord, boolean cancel, boolean use, Long generateFileAliasId, GenerateFileAlias generateFileAlias) {
        super(id, created, createdBy, updated, updatedBy);
        this.refRunning = refRunning;
        this.paymentDate = paymentDate;
        this.paymentName = paymentName;
        this.sumRecord = sumRecord;
        this.cancel = cancel;
        this.use = use;
        this.generateFileAliasId = generateFileAliasId;
        this.generateFileAlias = generateFileAlias;
    }

    public void addInterfaceD1D2(ProposalLog proposalLog) {
        this.interfaceD1D2.add(proposalLog);
    }
}
