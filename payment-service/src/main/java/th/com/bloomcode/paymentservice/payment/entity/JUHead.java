package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "JU_HEAD")
public class JUHead {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JU_HEAD_SEQ")
    @SequenceGenerator(sequenceName = "JU_HEAD_SEQ", allocationSize = 1, name = "JU_HEAD_SEQ")
    private Long id;

    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;
    @Column(name = "PAYMENT_NAME")
    private String paymentName;

    @Column(name = "DOC_TYPE")
    private String docType;

    @Column(name = "DOCUMENT_NO")
    private String documentNo;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "PAYMENT_CENTER")
    private String paymentCenter;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "DOCUMENT_STATUS")
    private String documentStatus;

    @Column(name = "DOCUMENT_STATUS_NAME")
    private String documentStatusName;

    @Column(name = "DATE_DOC")
    private Timestamp dateDoc;

    @Column(name = "DATE_ACCT")
    private Timestamp dateAcct;

    @Column(name = "TRANSFER_DATE")
    private Timestamp transferDate;

    @Column(name = "AMOUNT_CR")
    private BigDecimal amountCr;

    @Column(name = "GL_ACCOUNT_CR")
    private String glAccountCr;

    @Column(name = "TEST_RUN")
    private boolean testRun;

    @OneToMany(mappedBy = "juHead", fetch= FetchType.LAZY, cascade=CascadeType.ALL)
    private List<JULine> guLines;

}
