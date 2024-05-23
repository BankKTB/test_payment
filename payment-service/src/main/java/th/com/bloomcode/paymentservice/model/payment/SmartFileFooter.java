package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SmartFileFooter extends BaseModel {

    public static final String TABLE_NAME = "SMART_FILE_FOOTER";

    public static final String COLUMN_NAME_SMART_FILE_FOOTER_ID = "ID";
    public static final String COLUMN_NAME_FILE_TYPE = "FILE_TYPE";
    public static final String COLUMN_NAME_REC_TYPE = "REC_TYPE";
    public static final String COLUMN_NAME_REC_COUNT = "REC_COUNT";
    public static final String COLUMN_NAME_FILLER = "FILLER";
    public static final String COLUMN_NAME_AUTHORIZE = "AUTHORIZE";
    public static final String COLUMN_NAME_TOTAL_RECORD = "TOTAL_RECORD";
    public static final String COLUMN_NAME_TOTAL_AMT = "TOTAL_AMT";
    public static final String COLUMN_NAME_SMART_FILE_HEADER_ID = "SMART_FILE_HEADER_ID";

    private String fileType;
    private String recType;
    private String recCount;
    private String filler;
    private String authorize;
    private int totalRecord;
    private BigDecimal totalAmt;
    private Long smartFileHeaderId;
    private SmartFileHeader smartFileHeader;

    public SmartFileFooter(Long id, String fileType, String recType, String recCount, String filler, String authorize, int totalRecord, BigDecimal totalAmt, SmartFileHeader smartFileHeader) {
        super(id);
        this.fileType = fileType;
        this.recType = recType;
        this.recCount = recCount;
        this.filler = filler;
        this.authorize = authorize;
        this.totalRecord = totalRecord;
        this.totalAmt = totalAmt;
        this.smartFileHeader = smartFileHeader;
    }

    @Override
    public String toString() {
        return fileType + recType + recCount + filler + authorize;
    }
}
