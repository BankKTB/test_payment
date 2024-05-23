package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PROPOSAL_LOG_HEADER")
@Data
public class ProposalLogHeader {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPOSAL_LOG_HEADER_SEQ")
    @SequenceGenerator(sequenceName = "PROPOSAL_LOG_HEADER_SEQ", allocationSize = 1, name = "PROPOSAL_LOG_HEADER_SEQ")
    private Long id;

    private Long refRunning;
    private Date paymentDate;
    private String paymentName;
    private boolean sumRecord;
    private Timestamp created;
    private String createdBy;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "proposalLogHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProposalLog> proposalLogs;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_ALIAS_ID")
    private PaymentAlias paymentAlias;
}
