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
//
//@Entity
//@Table(name = "SMART_FILE_DETAIL")
//@Getter
//@Setter
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//public class SmartFileDetail {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SMART_FILE_DETAIL_SEQ")
//    @SequenceGenerator(sequenceName = "SMART_FILE_DETAIL_SEQ", allocationSize = 1, name = "SMART_FILE_DETAIL_SEQ")
//    private Long id;
//
//    @Column(name = "FILE_TYPE", length = 2)
//    private String fileType;
//
//    @Column(name = "REC_TYPE", length = 1)
//    private String recType;
//
//    @Column(name = "BATCH_NUM", length = 6)
//    private String batchNum;
//
//    @Column(name = "REC_BANK_CODE", length = 3)
//    private String recBankCode;
//
//    @Column(name = "REC_BRANCH_CODE", length = 4)
//    private String recBranchCode;
//
//    @Column(name = "REC_ACCT", length = 12)
//    private String recAcct;
//
//    @Column(name = "SEND_BANK_CODE", length = 3)
//    private String sendBankCode;
//
//    @Column(name = "SEND_BRANCH_CODE", length = 4)
//    private String sendBranchCode;
//
//    @Column(name = "SEND_ACCT", length = 11)
//    private String sendAcct;
//
//    @Column(name = "EFF_DATE", length = 8)
//    private String effDate;
//
//    @Column(name = "PAYMENT_TYPE", length = 2)
//    private String paymentType;
//
//    @Column(name = "SERVICE_TYPE", length = 2)
//    private String serviceType;
//
//    @Column(name = "CLEAR_HOUSE_CODE", length = 2)
//    private String clearHouseCode;
//
//    @Column(name = "TRANSFER_AMT", length = 12)
//    private String transferAmt;
//
//    @Column(name = "REC_INFORM", length = 60)
//    private String recInform;
//
//    @Column(name = "SEND_INFORM", length = 60)
//    private String sendInform;
//
//    @Column(name = "OTH_INFORM", length = 100)
//    private String othInform;
//
//    @Column(name = "REF_SEQ_NUM", length = 6)
//    private String refSeqNum;
//
//    @Column(name = "FILLER", length = 25)
//    private String filler;
//
//    @Column(name = "TRANSFER_AMOUNT")
//    private BigDecimal transferAmount;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
//    private SmartFileBatch smartFileBatch;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "SMART_FILE_LOG_ID")
//    private SmartFileLog smartFileLog;
//
//    @Override
//    public String toString() {
//        return fileType + recType + batchNum + recBankCode + recBranchCode + recAcct + sendBankCode + sendBranchCode + sendAcct + effDate
//                + serviceType + clearHouseCode + transferAmt + recInform + sendInform + othInform + refSeqNum + filler;
//    }
//}
