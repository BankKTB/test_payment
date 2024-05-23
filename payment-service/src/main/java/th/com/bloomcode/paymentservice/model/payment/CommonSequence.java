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
public class CommonSequence extends BaseModel {

    public static final String TABLE_NAME = "COMMON_SEQUENCE";

    public static final String COLUMN_NAME_COMMON_SEQUENCE_ID = "ID";
    public static final String COLUMN_NAME_SEQUENCE_NAME = "SEQUENCE_NAME";
    public static final String COLUMN_NAME_SEQUENCE_KEY = "SEQUENCE_KEY";
    public static final String COLUMN_NAME_SEQUENCE_FROM = "SEQUENCE_FROM";
    public static final String COLUMN_NAME_SEQUENCE_TO = "SEQUENCE_TO";
    public static final String COLUMN_NAME_SEQUENCE_NO = "SEQUENCE_NO";

    public CommonSequence(Long id, Timestamp created, String createdBy, Timestamp updated, String updatedBy, String sequenceName, String sequenceKey, Long sequenceFrom, Long sequenceTo, Long sequenceNo) {
        super(id, created, createdBy, updated, updatedBy);
        this.sequenceName = sequenceName;
        this.sequenceKey = sequenceKey;
        this.sequenceFrom = sequenceFrom;
        this.sequenceTo = sequenceTo;
        this.sequenceNo = sequenceNo;
    }

    private String sequenceName;
    private String sequenceKey;
    private Long sequenceFrom;
    private Long sequenceTo;
    private Long sequenceNo;
}
