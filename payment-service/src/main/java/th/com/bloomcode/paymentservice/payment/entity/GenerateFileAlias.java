package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "GENERATE_FILE_ALIAS")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class GenerateFileAlias {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_ALIAS_SEQ")
    @SequenceGenerator(sequenceName = "PAYMENT_ALIAS_SEQ", allocationSize = 1, name = "PAYMENT_ALIAS_SEQ")
    private Long id;

    @NotNull(message = "Payment date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp generateFileDate;

    @NotEmpty(message = "Payment name is required")
    private String generateFileName;

    private String fileName;

    private int swiftAmountDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp swiftDate;
    private int smartAmountDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp smartDate;
    private int giroAmountDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp giroDate;
    @Column(name = "INHOUSE_AMOUNT_DAY")
    private int inHouseAmountDay;
    @Column(name = "INHOUSE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp inHouseDate;
    private boolean createAgain;
    private boolean testRun;

//    private String parameterStatus;
//    private String proposalStatus;
    private String runStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    @JoinColumn(name = "PAYMENT_ALIAS_ID")
    private PaymentAlias paymentAlias;

//    @OneToMany(mappedBy = "generateFileAlias", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SmartFileHeader> smartFileHeaders;

    @OneToMany(mappedBy = "generateFileAlias", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SwiftFile> swiftFiles;

    @OneToMany(mappedBy = "generateFileAlias", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InhouseFileHeader> inhouseFileHeaders;

    @OneToMany(mappedBy = "generateFileAlias", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GIROFileHeader> giroFileHeaders;
}
