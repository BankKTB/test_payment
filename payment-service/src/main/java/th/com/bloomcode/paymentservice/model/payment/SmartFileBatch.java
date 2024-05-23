package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SmartFileBatch extends BaseModel {

    public static final String TABLE_NAME = "SMART_FILE_BATCH";

    public static final String COLUMN_NAME_SMART_FILE_BATCH_ID = "ID";
    public static final String COLUMN_NAME_FILE_TYPE = "FILE_TYPE";
    public static final String COLUMN_NAME_REC_TYPE = "REC_TYPE";
    public static final String COLUMN_NAME_BATCH_NUM = "BATCH_NUM";
    public static final String COLUMN_NAME_SEND_BANK_CODE = "SEND_BANK_CODE";
    public static final String COLUMN_NAME_TOTAL_NUM = "TOTAL_NUM";
    public static final String COLUMN_NAME_TOTAL_AMOUNT = "TOTAL_AMOUNT";
    public static final String COLUMN_NAME_EFF_DATE = "EFF_DATE";
    public static final String COLUMN_NAME_KIND_TRANS = "KIND_TRANS";
    public static final String COLUMN_NAME_FILLER = "FILLER";
    public static final String COLUMN_NAME_SMART_FILE_HEADER_ID = "SMART_FILE_HEADER_ID";

    private String fileType;
    private String recType;
    private String batchNum;
    private String sendBankCode;
    private String totalNum;
    private String totalAmount;
    private String effDate;
    private String kindTrans;
    private String filler;
    private Long smartFileHeaderId;
    private SmartFileHeader smartFileHeader;

    private String total;
    private String fee;

    // Pull from child
    private List<SmartFileDetail> smartFileDetails = new ArrayList<>();

    public SmartFileBatch(Long id, String fileType, String recType, String batchNum, String sendBankCode, String totalNum, String totalAmount, String effDate, String kindTrans, String filler, SmartFileHeader smartFileHeader) {
        super(id);
        this.fileType = fileType;
        this.recType = recType;
        this.batchNum = batchNum;
        this.sendBankCode = sendBankCode;
        this.totalNum = totalNum;
        this.totalAmount = totalAmount;
        this.effDate = effDate;
        this.kindTrans = kindTrans;
        this.filler = filler;
        this.smartFileHeader = smartFileHeader;
    }

    @Override
    public String toString() {
        return fileType + recType + batchNum + sendBankCode + totalNum + totalAmount + effDate + kindTrans + filler;
    }

    public void addSmartFileDetail(SmartFileDetail smartFileDetail) {
        smartFileDetails.add(smartFileDetail);
    }
}
