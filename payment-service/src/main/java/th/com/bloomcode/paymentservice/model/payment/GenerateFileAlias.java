package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GenerateFileAlias extends BaseModel {

    public static final String TABLE_NAME = "GENERATE_FILE_ALIAS";

    public static final String COLUMN_NAME_GENERATE_FILE_ALIAS_ID = "ID";
    public static final String COLUMN_NAME_GENERATE_FILE_DATE = "GENERATE_FILE_DATE";
    public static final String COLUMN_NAME_GENERATE_FILE_NAME = "GENERATE_FILE_NAME";
    public static final String COLUMN_NAME_FILE_NAME = "FILE_NAME";
    public static final String COLUMN_NAME_SWIFT_AMOUNT_DAY = "SWIFT_AMOUNT_DAY";
    public static final String COLUMN_NAME_SWIFT_DATE = "SWIFT_DATE";
    public static final String COLUMN_NAME_SMART_AMOUNT_DAY = "SMART_AMOUNT_DAY";
    public static final String COLUMN_NAME_SMART_DATE = "SMART_DATE";;
    public static final String COLUMN_NAME_GIRO_AMOUNT_DAY = "GIRO_AMOUNT_DAY";
    public static final String COLUMN_NAME_GIRO_DATE = "GIRO_DATE";;
    public static final String COLUMN_NAME_INHOUSE_AMOUNT_DAY = "INHOUSE_AMOUNT_DAY";
    public static final String COLUMN_NAME_INHOUSE_DATE = "INHOUSE_DATE";
    public static final String COLUMN_NAME_IS_CREATE_AGAIN = "CREATE_AGAIN";
    public static final String COLUMN_NAME_IS_TEST_RUN = "TEST_RUN";
    public static final String COLUMN_NAME_RUN_STATUS = "RUN_STATUS";
    public static final String COLUMN_NAME_PAYMENT_ALIAS_ID = "PAYMENT_ALIAS_ID";

    public GenerateFileAlias(Long id, Timestamp created, String createdBy, Timestamp updated, String updatedBy, Timestamp generateFileDate, String generateFileName, String fileName, int swiftAmountDay, Timestamp swiftDate, int smartAmountDay, Timestamp smartDate, int giroAmountDay, Timestamp giroDate, int inhouseAmountDay, Timestamp inhouseDate, boolean createAgain, boolean testRun, String runStatus, Long paymentAliasId, PaymentAlias paymentAlias) {
        super(id, created, createdBy, updated, updatedBy);
        this.generateFileDate = generateFileDate;
        this.generateFileName = generateFileName;
        this.fileName = fileName;
        this.swiftAmountDay = swiftAmountDay;
        this.swiftDate = swiftDate;
        this.smartAmountDay = smartAmountDay;
        this.smartDate = smartDate;
        this.giroAmountDay = giroAmountDay;
        this.giroDate = giroDate;
        this.inhouseAmountDay = inhouseAmountDay;
        this.inhouseDate = inhouseDate;
        this.createAgain = createAgain;
        this.testRun = testRun;
        this.runStatus = runStatus;
        this.paymentAliasId = paymentAliasId;
        this.paymentAlias = paymentAlias;
    }

    private Timestamp generateFileDate;
    private String generateFileName;
    private String fileName;
    private int swiftAmountDay;
    private Timestamp swiftDate;
    private int smartAmountDay;
    private Timestamp smartDate;
    private int giroAmountDay;
    private Timestamp giroDate;
    private int inhouseAmountDay;
    private Timestamp inhouseDate;
    private boolean createAgain;
    private boolean testRun;
    private String runStatus;
    private Long paymentAliasId;
    private PaymentAlias paymentAlias;

    // Pull from child
    private SmartFileHeader smartFileHeader;
    private List<SwiftFile> swiftFileMasters = new ArrayList<>();
    private List<SwiftFile> swiftFiles = new ArrayList<>();
    private List<GIROFileHeader> giroFileHeaders = new ArrayList<>();
    private List<InhouseFileHeader> inhouseFileHeaders = new ArrayList<>();
    private SmartFileHeader smartFileHeaderSumFile;
    private List<SwiftFile> swiftFilesSumFile = new ArrayList<>();
    private List<GIROFileHeader> giroFileHeadersSumFile = new ArrayList<>();
    private List<InhouseFileHeader> inhouseFileHeadersSumFile = new ArrayList<>();
    private List<SmartFileGM> smartFileGMs = new ArrayList<>();
    private ProposalLogHeader interfaceD1D2;

    public void addSwiftFileMaster(SwiftFile swiftFile) {
        swiftFileMasters.add(swiftFile);
    }

    public void addSwiftFile(SwiftFile swiftFile) {
        swiftFiles.add(swiftFile);
    }

    public void addGiroFileHeader(GIROFileHeader giroFileHeader) {
        giroFileHeaders.add(giroFileHeader);
    }

    public void addInhouseHeader(InhouseFileHeader inhouseFileHeader) {
        inhouseFileHeaders.add(inhouseFileHeader);
    }

    public void addSwiftFileSumFile(SwiftFile swiftFile) {
        swiftFilesSumFile.add(swiftFile);
    }

    public void addGiroFileHeaderSumFile(GIROFileHeader giroFileHeader) {
        giroFileHeadersSumFile.add(giroFileHeader);
    }

    public void addInhouseHeaderSumFile(InhouseFileHeader inhouseFileHeader) {
        inhouseFileHeadersSumFile.add(inhouseFileHeader);
    }

    public void addSmartFileGM(SmartFileGM smartFileGM) {
        smartFileGMs.add(smartFileGM);
    }
}
