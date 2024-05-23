//package th.com.bloomcode.paymentservice.payment.entity;
//
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.sql.Timestamp;
//import java.util.List;
//
//@Entity
//@Table(name = "SMART_FILE_HEADER")
//@Getter
//@Setter
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//public class SmartFileHeader {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SMART_FILE_HEADER_SEQ")
//    @SequenceGenerator(sequenceName = "SMART_FILE_HEADER_SEQ", allocationSize = 1, name = "SMART_FILE_HEADER_SEQ")
//    private Long id;
//
//    @Column(name = "FILE_TYPE", length = 2)
//    private String fileType;
//
//    @Column(name = "REC_TYPE", length = 1)
//    private String recType;
//
//    @Column(name = "REL_VER", length = 8)
//    private String relVer;
//
//    @Column(name = "REL_DATE", length = 8)
//    private String relDate;
//
//    @Column(name = "FILLER", length = 301)
//    private String filler;
//
//    @Column(name = "IS_PROPOSAL")
//    private boolean proposal;
//
//    @Column(name = "EFF_DATE")
//    private Timestamp effDate;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
//    private GenerateFileAlias generateFileAlias;
//
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "smartFileHeader", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SmartFileBatch> smartFileBatches;
//
//    @OneToOne(mappedBy = "smartFileHeader", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY)
//    private SmartFileFooter smartFileFooter;
//
//    @Override
//    public String toString() {
//        return fileType + recType + relVer + relDate + filler;
//    }
//}
