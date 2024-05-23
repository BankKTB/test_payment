package th.com.bloomcode.paymentservice.model.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Setter
public class ReturnReverseInvoiceResponse {
    @Column(name = "ID")
    private Long id;

    @Column(name = "REV_INVOICE_STATUS")
    private boolean revInvoiceStatus;

    @Column(name = "REV_INVOICE_REASON")
    private String revInvoiceReason;

    @Column(name = "ORIGINAL_COMP_CODE")
    private String originalCompCode;

    @Column(name = "ORIGINAL_DOC_NO")
    private String originalDocNo;

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
