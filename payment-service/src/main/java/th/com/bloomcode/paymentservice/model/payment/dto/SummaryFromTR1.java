package th.com.bloomcode.paymentservice.model.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class SummaryFromTR1 extends BaseModel {

    @Column(name = "ROW_LEVEL")
    private String rowLevel;
    @Column(name = "REF_RUNNING")
    private Long refRunning;
    @Column(name = "PERIOD_NO")
    private String periodNo;
    @Column(name = "YEAR")
    private String year;
    @Column(name = "TRANSFER_DATE")
    private Timestamp transferDate;
    @Column(name = "FLAG")
    private String flag;
    @Column(name = "ORIGINAL_COMP_CODE")
    private String originalCompCode;
    @Column(name = "INV_COMP_CODE")
    private String invCompCode;
    @Column(name = "FUND_SOURCE")
    private String fundSource;
    @Column(name = "FUND_CENTER")
    private String fundCenter;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "PAYMENT_NAME")
    private String paymentName;
    @Column(name = "PAYMENT_DATE")
    private Timestamp paymentDate;
    @Column(name = "VENDOR")
    private String vendor;
    @Column(name = "REF_LINE")
    private int refLine;
    @Column(name = "ORIGINAL_DOC_NO")
    private String originalDocNo;
    @Column(name = "ORIGINAL_FISCAL_YEAR")
    private String originalFiscalYear;
    @Column(name = "PAYMENT_DOCUMENT")
    private String paymentDocument;
    @Column(name = "PAYMENT_FISCAL_YEAR")
    private String paymentFiscalYear;
    @Column(name = "DOC_TYPE")
    private String docType;
    @Column(name = "GL_ACCOUNT")
    private String GlAccount;
    @Column(name = "BUDGET_CODE")
    private String budgetCode;
    @Column(name = "FILE_STATUS")
    private String fileStatus;

//    public SummaryFromTR1(
//            String rowLevel,
//            Long refRunning,
//            String periodNo,
//            String year,
//            Timestamp transferDate,
//            String originalCompCode,
//            String invCompCode,
//            String fundSource,
//            String fundCenter,
//            BigDecimal amount,
//            String paymentName,
//            Timestamp paymentDate,
//            String vendor,
//            int refLine,
//            String originalDocumentNo,
//            String originalFiscalYear,
//            String paymentDocumentNo,
//            String paymentFiscalYear,
//            String docType,
//            String GlAccount) {
//        this.rowLevel = rowLevel;
//        this.refRunning = refRunning;
//        this.periodNo = periodNo;
//        this.year = year;
//        this.transferDate = transferDate;
//        this.originalCompCode = originalCompCode;
//        this.invCompCode = invCompCode;
//        this.fundSource = fundSource;
//        this.fundCenter = fundCenter;
//        this.amount = amount;
//        this.paymentName = paymentName;
//        this.paymentDate = paymentDate;
//        this.vendor = vendor;
//        this.refLine = refLine;
//        this.originalDocumentNo = originalDocumentNo;
//        this.originalFiscalYear = originalFiscalYear;
//        this.paymentDocumentNo = paymentDocumentNo;
//        this.paymentFiscalYear = paymentFiscalYear;
//        this.docType = docType;
//        this.GlAccount = GlAccount;
//
//    }


}
