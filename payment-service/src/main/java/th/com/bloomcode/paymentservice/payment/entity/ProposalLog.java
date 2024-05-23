package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "PROPOSAL_LOG")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class ProposalLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPOSAL_LOG_SEQ")
    @SequenceGenerator(sequenceName = "PROPOSAL_LOG_SEQ", allocationSize = 1, name = "PROPOSAL_LOG_SEQ")
    private Long id;

    private Long refRunning;
    private int refLine;
    private Date paymentDate;
    private String paymentName;
    private String accountNoFrom;
    private String accountNoTo;
    private String fileType;
    private String fileName;
    private Date transferDate;
    private String vendor;
    private String bankKey;
    private String vendorBankAccount;
    private String transferLevel;
    private String payAccount;
    private String payingCompCode;
    private String paymentDocument;
    private String paymentFiscalYear;
    @Column(name = "REV_PAYMENT_DOCUMENT")
    private String revPaymentDocument;
    @Column(name = "REV_PAYMENT_FISCAL_YEAR")
    private String revPaymentFiscalYear;
    private String paymentCompCode;

    private String fiscalYear;
    private String fiArea;
    private BigDecimal amount;
    private BigDecimal bankFee;
    private BigDecimal creditMemoAmount;
    private Timestamp cancelDate;

    @Column(name = "IS_RERUN")
    private boolean rerun;

    @Column(name = "INV_COMP_CODE")
    private String invCompCode;
    @Column(name = "INV_DOC_NO")
    private String invDocNo;
    @Column(name = "INV_FISCAL_YEAR")
    private String invFiscalYear;
    @Column(name = "INV_DOC_TYPE")
    private String invDocType;

    @Column(name = "REV_INV_DOC_NO")
    private String revInvDocNo;
    @Column(name = "REV_INV_FISCAL_YEAR")
    private String revInvFiscalYear;

    @Column(name = "ORIGINAL_COMP_CODE")
    private String originalCompCode;
    @Column(name = "ORIGINAL_DOC_NO")
    private String originalDocNo;
    @Column(name = "ORIGINAL_FISCAL_YEAR")
    private String originalFiscalYear;
    @Column(name = "ORIGINAL_DOC_TYPE")
    private String originalDocType;

    @Column(name = "REV_ORIGINAL_DOC_NO")
    private String revOriginalDocNo;
    @Column(name = "REV_ORIGINAL_FISCAL_YEAR")
    private String revOriginalFiscalYear;
    @Column(name = "REV_ORIGINAL_REASON")
    private String revOriginalReason;


    @Column(name = "ORIGINAL_WTX_AMOUNT")
    private BigDecimal originalWtxAmount;
    @Column(name = "ORIGINAL_WTX_BASE")
    private BigDecimal originalWtxBase;

    @Column(name = "ORIGINAL_WTX_AMOUNT_P")
    private BigDecimal originalWtxAmountP;
    @Column(name = "ORIGINAL_WTX_BASE_P")
    private BigDecimal originalWtxBaseP;


    private Timestamp created;
    private String createdBy;
    private Timestamp updated;
    private String updatedBy;
    private String fileStatus;
    private String sendStatus;


    @Column(name = "IS_JU_CREATE")
    private boolean juCreate;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProposalLogHeader proposalLogHeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "REF_RUNNING_SUM", referencedColumnName = "refRunning", insertable = false, updatable = false),
            @JoinColumn(name = "REF_LINE_SUM", referencedColumnName = "refLine", insertable = false, updatable = false)
    })
    private ProposalLogSum proposalLogSum;
}
