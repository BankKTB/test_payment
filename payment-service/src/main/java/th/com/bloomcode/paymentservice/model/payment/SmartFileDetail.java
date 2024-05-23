package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SmartFileDetail extends BaseModel {

  public static final String TABLE_NAME = "SMART_FILE_DETAIL";

  public static final String COLUMN_NAME_SMART_FILE_DETAIL_ID = "ID";
  public static final String COLUMN_NAME_FILE_TYPE = "FILE_TYPE";
  public static final String COLUMN_NAME_REC_TYPE = "REC_TYPE";
  public static final String COLUMN_NAME_BATCH_NUM = "BATCH_NUM";
  public static final String COLUMN_NAME_REC_BANK_CODE = "REC_BANK_CODE";
  public static final String COLUMN_NAME_REC_BRANCH_CODE = "REC_BRANCH_CODE";
  public static final String COLUMN_NAME_REC_ACCT = "REC_ACCT";
  public static final String COLUMN_NAME_SEND_BANK_CODE = "SEND_BANK_CODE";
  public static final String COLUMN_NAME_SEND_BRANCH_CODE = "SEND_BRANCH_CODE";
  public static final String COLUMN_NAME_SEND_ACCT = "SEND_ACCT";
  public static final String COLUMN_NAME_EFF_DATE = "EFF_DATE";
  public static final String COLUMN_NAME_PAYMENT_TYPE = "PAYMENT_TYPE";
  public static final String COLUMN_NAME_SERVICE_TYPE = "SERVICE_TYPE";
  public static final String COLUMN_NAME_CLEAR_HOUSE_CODE = "CLEAR_HOUSE_CODE";
  public static final String COLUMN_NAME_TRANSFER_AMT = "TRANSFER_AMT";
  public static final String COLUMN_NAME_REC_INFORM = "REC_INFORM";
  public static final String COLUMN_NAME_SEND_INFORM = "SEND_INFORM";
  public static final String COLUMN_NAME_OTH_INFORM = "OTH_INFORM";
  public static final String COLUMN_NAME_REF_SEQ_NUM = "REF_SEQ_NUM";
  public static final String COLUMN_NAME_FILLER = "FILLER";
  public static final String COLUMN_NAME_TRANSFER_AMOUNT = "TRANSFER_AMOUNT";
  public static final String COLUMN_NAME_SMART_FILE_BATCH_ID = "SMART_FILE_BATCH_ID";

  public SmartFileDetail(Long id, String fileType, String recType, String batchNum, String recBankCode, String recBranchCode, String recAcct, String sendBankCode, String sendBranchCode, String sendAcct, String effDate, String paymentType, String serviceType, String clearHouseCode, String transferAmt, String recInform, String sendInform, String othInform, String refSeqNum, String filler, BigDecimal transferAmount, SmartFileBatch smartFileBatch) {
    super(id);
    this.fileType = fileType;
    this.recType = recType;
    this.batchNum = batchNum;
    this.recBankCode = recBankCode;
    this.recBranchCode = recBranchCode;
    this.recAcct = recAcct;
    this.sendBankCode = sendBankCode;
    this.sendBranchCode = sendBranchCode;
    this.sendAcct = sendAcct;
    this.effDate = effDate;
    this.paymentType = paymentType;
    this.serviceType = serviceType;
    this.clearHouseCode = clearHouseCode;
    this.transferAmt = transferAmt;
    this.recInform = recInform;
    this.sendInform = sendInform;
    this.othInform = othInform;
    this.refSeqNum = refSeqNum;
    this.filler = filler;
    this.transferAmount = transferAmount;
    this.smartFileBatch = smartFileBatch;
  }

  private String fileType;
  private String recType;
  private String batchNum;
  private String recBankCode;
  private String recBranchCode;
  private String recAcct;
  private String sendBankCode;
  private String sendBranchCode;
  private String sendAcct;
  private String effDate;
  private String paymentType;
  private String serviceType;
  private String clearHouseCode;
  private String transferAmt;
  private String recInform;
  private String sendInform;
  private String othInform;
  private String refSeqNum;
  private String filler;
  private BigDecimal transferAmount;
  private Long smartFileBatchId;
  private SmartFileBatch smartFileBatch;

  // Pull from child
  private SmartFileLog smartFileLog;

  public SmartFileDetail(SmartFileDetail smartFileDetail) {
    this.fileType = smartFileDetail.getFileType();
    this.recType = smartFileDetail.getRecType();
    this.batchNum = smartFileDetail.getBatchNum();
    this.recBankCode = smartFileDetail.getRecBankCode();
    this.recBranchCode = smartFileDetail.getRecBranchCode();
    this.recAcct = smartFileDetail.getRecAcct();
    this.sendBankCode = smartFileDetail.getSendBankCode();
    this.sendBranchCode = smartFileDetail.getSendBranchCode();
    this.sendAcct = smartFileDetail.getSendAcct();
    this.effDate = smartFileDetail.getEffDate();
    this.paymentType = smartFileDetail.getPaymentType();
    this.serviceType = smartFileDetail.getServiceType();
    this.clearHouseCode = smartFileDetail.getClearHouseCode();
    this.transferAmt = smartFileDetail.getTransferAmt();
    this.recInform = smartFileDetail.getRecInform();
    this.sendInform = smartFileDetail.getSendInform();
    this.othInform = smartFileDetail.getOthInform();
    this.refSeqNum = smartFileDetail.getRefSeqNum();
    this.filler = smartFileDetail.getFiller();
    this.transferAmount = smartFileDetail.getTransferAmount();
    this.smartFileBatchId = smartFileDetail.getSmartFileBatchId();
    this.smartFileBatch = smartFileDetail.getSmartFileBatch();
    this.smartFileLog = smartFileDetail.getSmartFileLog();
  }

  @Override
  public String toString() {
    return fileType + recType + batchNum + recBankCode + recBranchCode + recAcct + sendBankCode + sendBranchCode + sendAcct + effDate
            + serviceType + clearHouseCode + transferAmt + recInform + sendInform + othInform + refSeqNum + filler;
  }
}
