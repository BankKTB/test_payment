package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GenerateFileSequence extends BaseModel {

    public static final String TABLE_NAME = "GENERATE_FILE_SEQUENCE";

    public static final String COLUMN_NAME_GENERATE_FILE_SEQUENCE_ID = "ID";
    public static final String COLUMN_NAME_SEQUENCE_NAME = "SEQUENCE_NAME";
    public static final String COLUMN_NAME_EFFECTIVE_DATE = "EFFECTIVE_DATE";
    public static final String COLUMN_NAME_SEQUENCE_FROM = "SEQUENCE_FROM";
    public static final String COLUMN_NAME_SEQUENCE_TO = "SEQUENCE_TO";
    public static final String COLUMN_NAME_SEQUENCE_NO = "SEQUENCE_NO";

    public GenerateFileSequence(Long id, Timestamp created, String createdBy, Timestamp updated, String updatedBy, String sequenceName, Timestamp effectiveDate, Long sequenceFrom, Long sequenceTo, Long sequenceNo) {
        super(id, created, createdBy, updated, updatedBy);
        this.sequenceName = sequenceName;
        this.effectiveDate = effectiveDate;
        this.sequenceFrom = sequenceFrom;
        this.sequenceTo = sequenceTo;
        this.sequenceNo = sequenceNo;
    }

    private String sequenceName;
    private Timestamp effectiveDate;
    private Long sequenceFrom;
    private Long sequenceTo;
    private Long sequenceNo;
}
