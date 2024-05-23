package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class PaymentProposalLog extends BaseModel {
    public static final String TABLE_NAME = "PAYMENT_PROPOSAL_LOG";

    public static final String COLUMN_NAME_PAYMENT_PROPOSAL_LOG_ID = "ID";
    public static final String COLUMN_NAME_PAYMENT_ALIAS_ID = "PAYMENT_ALIAS_ID";
    public static final String COLUMN_NAME_SEQUENCE = "SEQUENCE";
    public static final String COLUMN_NAME_LOG_DATE = "LOG_DATE";
    public static final String COLUMN_NAME_MESSAGE_TEXT = "MESSAGE_TEXT";
    public static final String COLUMN_NAME_MESSAGE_CLASS = "MESSAGE_CLASS";
    public static final String COLUMN_NAME_MESSAGE_NO = "MESSAGE_NO";
    public static final String COLUMN_NAME_MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String COLUMN_NAME_IS_PROPOSAL = "IS_PROPOSAL";

    private Long paymentAliasId;
    private int sequence;
    private Timestamp logDate;
    private String messageText;
    private String messageClass;
    private String messageNo;
    private String messageType;
    private boolean proposal;

    private Long total;
    private Long success;
    private Long error;

}
