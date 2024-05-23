package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class ReturnReversePaymentResponse {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "REV_PAYMENT_STATUS")
    private boolean revPaymentStatus;

    @Column(name = "PAYMENT_COMP_CODE")
    private String paymentCompCode;

    @Column(name = "PAYMENT_DOCUMENT")
    private String paymentDocument;

    @Column(name = "PAYMENT_FISCAL_YEAR")
    private String paymentFiscalYear;

    @Column(name = "PAYMENT_DATE")
    private String paymentDate;

    @Column(name = "PAYMENT_NAME")
    private String paymentName;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "ORIGINAL_DOC_NO")
    private String originalDocNo;

    @Column(name = "ORIGINAL_COMP_CODE")
    private String originalCompCode;

    @Column(name = "REF_LINE")
    private String refLine;

    @Column(name = "ORIGINAL_FISCAL_YEAR")
    private String originalFiscalYear;

    @Column(name = "TRANSFER_DATE")
    private String transferDate;

    @Column(name = "FILE_NAME")
    private String fileName;
}
