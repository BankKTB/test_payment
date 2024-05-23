package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SmartFileHeader extends BaseModel {

    public static final String TABLE_NAME = "SMART_FILE_HEADER";

    public static final String COLUMN_NAME_SMART_FILE_HEADER_ID = "ID";
    public static final String COLUMN_NAME_FILE_TYPE = "FILE_TYPE";
    public static final String COLUMN_NAME_REC_TYPE = "REC_TYPE";
    public static final String COLUMN_NAME_REL_VER = "REL_VER";
    public static final String COLUMN_NAME_REL_DATE = "REL_DATE";
    public static final String COLUMN_NAME_FILLER = "FILLER";
    public static final String COLUMN_NAME_IS_PROPOSAL = "IS_PROPOSAL";
    public static final String COLUMN_NAME_EFF_DATE = "EFF_DATE";
    public static final String COLUMN_NAME_GENERATE_FILE_ALIAS_ID = "GENERATE_FILE_ALIAS_ID";

    public SmartFileHeader(Long id, String fileType, String recType, String relVer, String relDate, String filler, boolean proposal, Timestamp effDate) {
        super(id);
        this.fileType = fileType;
        this.recType = recType;
        this.relVer = relVer;
        this.relDate = relDate;
        this.filler = filler;
        this.proposal = proposal;
        this.effDate = effDate;
    }

    private String fileType;
    private String recType;
    private String relVer;
    private String relDate;
    private String filler;
    private boolean proposal;
    private Timestamp effDate;
    private Long generateFileAliasId;
    private GenerateFileAlias generateFileAlias;

    // Pull from child
    private List<SmartFileBatch> smartFileBatches = new ArrayList<>();
    private SmartFileFooter smartFileFooter;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "smartFileHeader", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SmartFileBatch> smartFileBatches;
//
//    @OneToOne(mappedBy = "smartFileHeader", cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY)
//    private SmartFileFooter smartFileFooter;

    public void addSmartFileBatch(SmartFileBatch smartFileBatch) {
        smartFileBatches.add(smartFileBatch);
    }

    @Override
    public String toString() {
        return fileType + recType + relVer + relDate + filler;
    }
}
