package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class ProposalDocumentResponse {
    @Id
    @Column(name = "PAYMENT_INFORMATION_ID")
    private Long paymentInformationId;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "AMOUNT_PAID")
    private BigDecimal amountPaid;

    @Column(name = "COMP_CODE")
    private String compCode;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "FISCAL_YEAR")
    private String fiscalYear;

    @Column(name = "PAYING_BANK_CODE")
    private String payingBankCode;

    @Column(name = "PAYING_HOUSE_BANK")
    private String payingHouseBank;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "ACC_DOC_NO")
    private String accDocNo;

    @Column(name = "PM_GROUP_NO")
    private String pmGroupNo;

    @Column(name = "PM_GROUP_DOC")
    private String pmGroupDoc;

    @Column(name = "PROPOSAL_BLOCK")
    private boolean proposalBlock;

    @Column(name = "PAYMENT_PROCESS_ID")
    private Long paymentProcessId;

}