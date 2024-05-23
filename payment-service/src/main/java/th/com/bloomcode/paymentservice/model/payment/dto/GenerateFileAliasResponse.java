package th.com.bloomcode.paymentservice.model.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;
import th.com.bloomcode.paymentservice.model.payment.*;
import th.com.bloomcode.paymentservice.payment.entity.PaymentAlias;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GenerateFileAliasResponse {

    public static final String COLUMN_NAME_PAYMENT_ALIAS_ID = "PAYMENT_ALIAS_ID";

    public static final String COLUMN_NAME_PAYMENT_RUN_STATUS = "PAYMENT_RUN_STATUS";
    public static final String COLUMN_NAME_FILE_NAME = "FILE_NAME";
    public static final String COLUMN_NAME_SWIFT_AMOUNT_DAY = "SWIFT_AMOUNT_DAY";
    public static final String COLUMN_NAME_SWIFT_DATE = "SWIFT_DATE";
    public static final String COLUMN_NAME_SMART_AMOUNT_DAY = "SMART_AMOUNT_DAY";
    public static final String COLUMN_NAME_SMART_DATE = "SMART_DATE";
    public static final String COLUMN_NAME_GIRO_AMOUNT_DAY = "GIRO_AMOUNT_DAY";
    public static final String COLUMN_NAME_GIRO_DATE = "GIRO_DATE";
    public static final String COLUMN_NAME_INHOUSE_AMOUNT_DAY = "INHOUSE_AMOUNT_DAY";
    public static final String COLUMN_NAME_INHOUSE_DATE = "INHOUSE_DATE";
    public static final String COLUMN_NAME_GENERATE_FILE_DATE = "GENERATE_FILE_DATE";
    public static final String COLUMN_NAME_GENERATE_FILE_NAME = "GENERATE_FILE_NAME";
    public static final String COLUMN_NAME_GENERATE_FILE_RUN_STATUS = "GENERATE_FILE_RUN_STATUS";
    public static final String COLUMN_NAME_GENERATE_FILE_RUN_STATUS_NAME = "GENERATE_FILE_RUN_STATUS_NAME";
    public static final String COLUMN_NAME_GENERATE_FILE_CREATE_AGAIN = "GENERATE_FILE_CREATE_AGAIN";
    public static final String COLUMN_NAME_GENERATE_FILE_ALIAS_ID = "GENERATE_FILE_ALIAS_ID";


    public static final String COLUMN_NAME_RUN_TOTAL_DOCUMENT = "RUN_TOTAL_DOCUMENT";
    public static final String COLUMN_NAME_RUN_SUCCESS_DOCUMENT = "RUN_SUCCESS_DOCUMENT";
    public static final String COLUMN_NAME_RUN_REVERSE_DOCUMENT = "RUN_REVERSE_DOCUMENT";
    public static final String COLUMN_NAME_IDEM_CREATE_PAYMENT_REPLY = "IDEM_CREATE_PAYMENT_REPLY";
    public static final String COLUMN_NAME_IDEM_REVERSE_PAYMENT_REPLY = "IDEM_REVERSE_PAYMENT_REPLY";


    private Long paymentAliasId;
    private String paymentRunStatus;
    private String fileName;
    private int swiftAmountDay;
    private Timestamp swiftDate;
    private int smartAmountDay;
    private Timestamp smartDate;
    private int giroAmountDay;
    private Timestamp giroDate;
    private int inhouseAmountDay;
    private Timestamp inhouseDate;
    private Timestamp generateFileDate;
    private String generateFileName;
    private String generateFileRunStatus;
    private String generateFileRunStatusName;
    private boolean generateFileRunStatusCreateAgain;
    private Long generateFileAliasId;

    private int runTotalDocument;
    private int runSuccessDocument;
    private int runReverseDocument;
    private int idemCreatePaymentReply;
    private int idemReversePaymentReply;


    public GenerateFileAliasResponse(Long paymentAliasId, String paymentRunStatus, String fileName, int swiftAmountDay, Timestamp swiftDate, int smartAmountDay, Timestamp smartDate, int giroAmountDay, Timestamp giroDate, int inhouseAmountDay, Timestamp inhouseDate, Timestamp generateFileDate, String generateFileName,
                                     String generateFileRunStatus, String generateFileRunStatusName, boolean generateFileRunStatusCreateAgain,
                                     Long generateFileAliasId, int runTotalDocument, int runSuccessDocument, int runReverseDocument,
                                     int idemCreatePaymentReply, int idemReversePaymentReply) {
        this.paymentAliasId = paymentAliasId;
        this.paymentRunStatus = paymentRunStatus;
        this.fileName = fileName;
        this.swiftAmountDay = swiftAmountDay;
        this.swiftDate = swiftDate;
        this.smartAmountDay = smartAmountDay;
        this.smartDate = smartDate;
        this.giroAmountDay = giroAmountDay;
        this.giroDate = giroDate;
        this.inhouseAmountDay = inhouseAmountDay;
        this.inhouseDate = inhouseDate;
        this.generateFileDate = generateFileDate;
        this.generateFileName = generateFileName;
        this.generateFileRunStatus = generateFileRunStatus;
        this.generateFileRunStatusName = generateFileRunStatusName;
        this.generateFileRunStatusCreateAgain = generateFileRunStatusCreateAgain;
        this.generateFileAliasId = generateFileAliasId;
        this.runTotalDocument = runTotalDocument;
        this.runSuccessDocument = runSuccessDocument;
        this.runReverseDocument = runReverseDocument;
        this.idemCreatePaymentReply = idemCreatePaymentReply;
        this.idemReversePaymentReply = idemReversePaymentReply;
    }
}
