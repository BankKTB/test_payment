//package th.com.bloomcode.paymentservice.payment.entity;
//
//import lombok.Data;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//
//@Entity
//@Table(name = "SMART_FILE_FOOTER")
//@Data
//public class SmartFileFooter {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SMART_FILE_FOOTER_SEQ")
//    @SequenceGenerator(sequenceName = "SMART_FILE_FOOTER_SEQ", allocationSize = 1, name = "SMART_FILE_FOOTER_SEQ")
//    private Long id;
//
//    @Column(name = "FILE_TYPE", length = 2)
//    private String fileType;
//
//    @Column(name = "REC_TYPE", length = 1)
//    private String recType;
//
//    @Column(name = "REC_COUNT", length = 6)
//    private String recCount;
//
//    @Column(name = "FILLER", length = 303)
//    private String filler;
//
//    @Column(name = "AUTHORIZE", length = 8)
//    private String authorize;
//
//    @Column(name = "TOTAL_RECORD")
//    private int totalRecord;
//
//    @Column(name = "TOTAL_AMT")
//    private BigDecimal totalAmt;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "SMART_FILE_HEADER_ID")
//    private SmartFileHeader smartFileHeader;
//
//    @Override
//    public String toString() {
//        return fileType + recType + recCount + filler + authorize;
//    }
//}
