//package th.com.bloomcode.paymentservice.payment.entity;
//
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Entity
//@Table(name = "SMART_FILE_BATCH")
//@Getter
//@Setter
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//public class SmartFileBatch {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SMART_FILE_BATCH_SEQ")
//    @SequenceGenerator(sequenceName = "SMART_FILE_BATCH_SEQ", allocationSize = 1, name = "SMART_FILE_BATCH_SEQ")
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
//    @Column(name = "SEND_BANK_CODE", length = 3)
//    private String sendBankCode;
//
//    @Column(name = "TOTAL_NUM", length = 3)
//    private String totalNum;
//
//    @Column(name = "TOTAL_AMOUNT", length = 15)
//    private String totalAmount;
//
//    @Column(name = "EFF_DATE", length = 8)
//    private String effDate;
//
//    @Column(name = "KIND_TRANS", length = 1)
//    private String kindTrans;
//
//    @Column(name = "FILLER", length = 281)
//    private String filler;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "smart_file_header_id")
//    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
//    private SmartFileHeader smartFileHeader;
//
//    @OneToMany(mappedBy = "smartFileBatch")
//    private List<SmartFileDetail> smartFileDetails;
//
//    @Override
//    public String toString() {
//        return fileType + recType + batchNum + sendBankCode + totalNum + totalAmount + effDate + kindTrans + filler;
//    }
//}
