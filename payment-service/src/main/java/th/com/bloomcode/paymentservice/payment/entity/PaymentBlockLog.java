package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.payment.GLLine;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "PAYMENT_BLOCK_LOG")
@Data
public class PaymentBlockLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_BLOCK_LOG_SEQ")
    @SequenceGenerator(sequenceName = "PAYMENT_BLOCK_LOG_SEQ", allocationSize = 1, name = "PAYMENT_BLOCK_LOG_SEQ")
    private Long id;

    @Column(name = "STATUS_NEW")
    private String statusNew;

    @Column(name = "STATUS_OLD")
    private String statusOld;

    @Column(name = "UNBLOCK_DATE")
    private Timestamp unblockDate;

    @Column(name = "COMP_CODE")
    private String compCode;

    @Column(name = "FI_AREA")
    private String fiArea;

    @Column(name = "PAYMENT_CENTER")
    private String paymentCenter;

    @Column(name = "DOC_TYPE")
    private String docType;

    @Column(name = "FISCAL_YEAR")
    private String fiscalYear;

    @Column(name = "ACC_DOC_NO")
    private String accDocNo;

    @Column(name = "DATE_ACCT")
    private Timestamp dateAcct;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "WTX_AMOUNT")
    private BigDecimal wtxAmount;

    @Column(name = "AMOUNT_PAID")
    private BigDecimal amountPaid;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "VENDOR_NAME")
    private String vendorName;

    public PaymentBlockLog() {

    }

    public PaymentBlockLog(GLLine glLine, String statusNew) {
        BigDecimal wtxAmount = null == glLine.getWtxAmount() ? BigDecimal.ZERO : glLine.getWtxAmount();

        this.statusNew = statusNew;
        this.statusOld = "B";
        this.unblockDate = new Timestamp(System.currentTimeMillis());
        this.compCode = glLine.getCompanyCode();
        this.fiArea = glLine.getFiArea();
        this.paymentCenter = glLine.getPaymentCenter();
//        this.docType = glLine.getGlHead().getDocType();
        this.fiscalYear = glLine.getOriginalFiscalYear();
        this.accDocNo = glLine.getOriginalDocumentNo();
//        this.dateAcct = glLine.getGlHead().getDateAcct();
        this.paymentMethod = glLine.getPaymentMethod();
        this.amount = glLine.getAmount();
        this.wtxAmount = wtxAmount;
        this.amountPaid = glLine.getAmount().subtract(wtxAmount);
        this.vendor = glLine.getVendor();
        this.vendorName = glLine.getVendorName();
    }
}
