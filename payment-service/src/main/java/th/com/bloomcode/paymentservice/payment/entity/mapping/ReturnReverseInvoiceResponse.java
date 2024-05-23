package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ReturnReverseInvoiceResponse {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "REV_INVOICE_STATUS")
    private boolean revInvoiceStatus;

    @Column(name = "REV_INVOICE_REASON")
    private String revInvoiceReason;

    @Column(name = "ORIGINAL_COMP_CODE")
    private String originalCompCode;

    @Column(name = "ORIGINAL_DOC_NO")
    private String originalDocument;

    @Column(name = "ORIGINAL_FISCAL_YEAR")
    private String originalFiscalYear;

    @Column(name = "PAYMENT_DATE")
    private String paymentDate;

    @Column(name = "PAYMENT_NAME")
    private String paymentName;

    @Column(name = "PAYING_COMP_CODE")
    private String payingCompCode;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "PAYMENT_DOCUMENT")
    private String paymentDocument;

    @Column(name = "REF_LINE")
    private String refLine;

    @Column(name = "PAYMENT_FISCAL_YEAR")
    private String paymentFiscalYear;

    @Column(name = "TRANSFER_DATE")
    private String transferDate;

    @Column(name = "FILE_NAME")
    private String fileName;
}
