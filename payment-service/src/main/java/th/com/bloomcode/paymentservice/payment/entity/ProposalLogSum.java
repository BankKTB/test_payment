package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PROPOSAL_LOG_SUM")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class ProposalLogSum implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPOSAL_LOG_SUM_SEQ")
    @SequenceGenerator(sequenceName = "PROPOSAL_LOG_SUM_SEQ", allocationSize = 1, name = "PROPOSAL_LOG_SUM_SEQ")
    private Long id;

    private Long refRunning;
    private int refLine;
    private Date paymentDate;
    private String paymentName;
    private String accountFrom;
    private String accountTo;
    private String fileType;
    private String fileName;
    private Date transferDate;
    private String vendor;
    private String bankKey;
    private String vendorBankAccount;
    private String transferLevel;
    private String payAccount;
    private String payingCompCode;
    private BigDecimal amount;
    private BigDecimal bankFee;
    private BigDecimal creditMemoAmount;
    private Date cancelDate;

    @Column(name = "IS_RERUN")
    private boolean rerun;
    private Date created;
    private String createdBy;

    @OneToMany(targetEntity = ProposalLog.class, mappedBy = "proposalLogSum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProposalLog> proposalLogs;
}
