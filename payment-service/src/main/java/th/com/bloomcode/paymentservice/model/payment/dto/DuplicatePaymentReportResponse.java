package th.com.bloomcode.paymentservice.model.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class DuplicatePaymentReportResponse {
    @Column(name = "ORIGINAL_COMP_CODE")
    private String originalCompCode;
    @Column(name = "ORIGINAL_DOC_NO")
    private String originalDocNo;
    @Column(name = "ORIGINAL_FISCAL_YEAR")
    private String originalFiscalYear;
    @Column(name = "PAYMENT_DOCUMENT")
    private String paymentDocument;
    @Column(name = "COUNT_DUPLICATE")
    private Integer countDuplicate;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "PAYMENT_NAME")
    private String paymentName;
    @Column(name = "PAYMENT_DATE")
    private Timestamp paymentDate;
}
