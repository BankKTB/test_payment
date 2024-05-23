package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "JU_LINE")
public class JULine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JU_LINE_SEQ")
    @SequenceGenerator(sequenceName = "JU_LINE_SEQ", allocationSize = 1, name = "JU_LINE_SEQ")
    private Long id;


    @Column(name = "REF_RUNNING")
    private Long refRunning;

    @Column(name = "PAY_ACCOUNT")
    private String payAccount;

    @Column(name = "REF_LINE")
    private int refLine;
    @Column(name = "ACCOUNT_NO_FROM")
    private String accountNoFrom;
    @Column(name = "ACCOUNT_NO_TO")
    private String accountNoTo;

    @Column(name = "FILE_TYPE")
    private String fileType;
    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "AMOUNT_DR")
    private BigDecimal amountDr;

    @Column(name = "GL_ACCOUNT_DR")
    private String glAccountDr;


    @Column(name = "ASSIGNMENT")
    private String assignment;

    @Column(name = "BG_CODE")
    private String bgCode;


    @Column(name = "COST_CENTER")
    private String costCenter;


    @Column(name = "FI_AREA")
    private String fiArea;

    @Column(name = "POSTING_KEY")
    private String postingKey;



    @Column(name = "FUND_SOURCE")
    private String fundSource;



    @Column(name = "BR_DOC_NO")
    private String brDocNo;

    @Column(name = "WTX_AMOUNT")
    private BigDecimal WtxAmount;
    @Column(name = "WTX_BASE")
    private BigDecimal WtxBase;

    @Column(name = "WTX_AMOUNT_P")
    private BigDecimal WtxAmountP;
    @Column(name = "WTX_BASE_P")
    private BigDecimal WtxBaseP;

    @Column(name = "MAIN_ACTIVITY")
    private String mainActivity;

    @Column(name = "COST_ACTIVITY")
    private String costActivity;


    @Column(name = "SUB_ACCOUNT")
    private String subAccount;
    @Column(name = "SUB_ACCOUNT_OWNER")
    private String subAccountOwner;
    @Column(name = "DEPOSIT_ACCOUNT")
    private String depositAccount;
    @Column(name = "DEPOSIT_ACCOUNT_OWNER")
    private String depositAccountOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juHeadId")
    @JsonIgnore
    private JUHead juHead;

}
