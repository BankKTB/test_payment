//package th.com.bloomcode.paymentservice.payment.entity;
//
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//
//@Entity
//@Table(name = "SMART_FILE_LOG")
//@Getter
//@Setter
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//public class SmartFileLog {
//
//    @Id
////    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SMART_FILE_LOG_SEQ")
//    @SequenceGenerator(sequenceName = "SMART_FILE_LOG_SEQ", allocationSize = 1, name = "SMART_FILE_LOG_SEQ")
//    private Long id;
//
//    @Column(name = "PAYMENT_DATE")
//    private Timestamp paymentDate;
//
//    @Column(name = "PAYMENT_NAME")
//    private String paymentName;
//
//    @Column(name = "VENDOR")
//    private String vendor;
//
//    @Column(name = "BANK_KEY")
//    private String bankKey;
//
//    @Column(name = "BANK_ACCOUNT_NO")
//    private String bankAccountNo;
//
//    @Column(name = "PAYMENT_METHOD")
//    private String paymentMethod;
//
//    @Column(name = "PAYING_COMP_CODE")
//    private String payingCompCode;
//
//    @Column(name = "PAYMENT_DOC_NO")
//    private String paymentDocNo;
//
//    @Column(name = "PAYMENT_YEAR")
//    private String paymentYear;
//
//    @Column(name = "COMP_CODE")
//    private String compCode;
//
//    @Column(name = "INV_DOC_NO")
//    private String invDocNo;
//
//    @Column(name = "FISCAL_YEAR")
//    private String fiscalYear;
//
//    @Column(name = "FI_AREA")
//    private String fiArea;
//
//    @Column(name = "TRANSFER_LEVEL")
//    private String transferLevel;
//
//    @Column(name = "FEE")
//    private BigDecimal fee;
//
//    @Column(name = "CREDIT_MEMO")
//    private BigDecimal creditMemo;
//
//    @Column(name = "ORIGINAL_ACC_DOC_NO")
//    private String originalAccDocNo;
//    @Column(name = "ORIGINAL_FISCAL_YEAR")
//    private String originalFiscalYear;
//    @Column(name = "ORIGINAL_COMP_CODE")
//    private String originalCompCode;
//    @Column(name = "ORIGINAL_DOC_TYPE")
//    private String originalDocType;
//
//    @Column(name = "ORIGINAL_WTX_AMOUNT")
//    private BigDecimal originalWtxAmount;
//    @Column(name = "ORIGINAL_WTX_BASE")
//    private BigDecimal originalWtxBase;
//    @Column(name = "ORIGINAL_WTX_AMOUNT_P")
//    private BigDecimal originalWtxAmountP;
//    @Column(name = "ORIGINAL_WTX_BASE_P")
//    private BigDecimal originalWtxBaseP;
//
//    @Column(name = "PAYMENT_FISCAL_YEAR")
//    private String paymentFiscalYear;
//    @Column(name = "PAYMENT_COMP_CODE")
//    private String paymentCompCode;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private PaymentAlias paymentAlias;
//
//    @OneToOne(mappedBy = "smartFileLog", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY, optional = false)
//    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
//    private SmartFileDetail smartFileDetail;
//}
