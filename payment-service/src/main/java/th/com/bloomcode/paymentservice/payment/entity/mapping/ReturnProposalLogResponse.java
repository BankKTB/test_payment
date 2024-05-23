package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class ReturnProposalLogResponse {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "FILE_STATUS")
    private String fileStatus;

    @Column(name = "REF_RUNNING")
    private String refRunning;

    @Column(name = "REF_LINE")
    private String refLine;

    @Column(name = "PAYMENT_DATE")
    private String paymentDate;

    @Column(name = "PAYMENT_NAME")
    private String paymentName;

    @Column(name = "ACCOUNT_NO_FROM")
    private String accountNoFrom;

    @Column(name = "ACCOUNT_NO_TO")
    private String accountNoTo;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "TRANSFER_DATE")
    private String transferDate;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "BANK_KEY")
    private String bankKey;

    @Column(name = "VENDOR_BANK_ACCOUNT")
    private String vendorBankAccount;

    @Column(name = "TRANSFER_LEVEL")
    private String transferLevel;

    @Column(name = "PAY_ACCOUNT")
    private String payAccount;

    @Column(name = "PAYMENT_COMP_CODE")
    private String paymentCompCode;

    @Column(name = "PAYMENT_DOCUMENT")
    private String paymentDocument;

    @Column(name = "AMOUNT")
    private BigDecimal amount;
}
