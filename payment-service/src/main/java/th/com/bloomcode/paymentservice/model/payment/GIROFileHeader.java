package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GIROFileHeader extends BaseModel {

  public static final String TABLE_NAME = "GIRO_FILE_HEADER";

  public static final String COLUMN_NAME_GIRO_FILE_HEADER_ID = "ID";
  public static final String COLUMN_NAME_REC_TYPE = "REC_TYPE";
  public static final String COLUMN_NAME_SEQ_NO = "SEQ_NO";
  public static final String COLUMN_NAME_BANK_CODE = "BANK_CODE";
  public static final String COLUMN_NAME_COMP_ACC_NO = "COMP_ACC_NO";
  public static final String COLUMN_NAME_COMP_NAME = "COMP_NAME";
  public static final String COLUMN_NAME_POST_DATE = "POST_DATE";
  public static final String COLUMN_NAME_RUNNING = "RUNNING";
  public static final String COLUMN_NAME_BATCH = "BATCH";
  public static final String COLUMN_NAME_EFF_DATE = "EFF_DATE";
  public static final String COLUMN_NAME_FILLER = "FILLER";
  public static final String COLUMN_NAME_IS_PROPOSAL = "IS_PROPOSAL";
  public static final String COLUMN_NAME_USER_TREF = "USER_TREF";
  public static final String COLUMN_NAME_GENERATE_FILE_ALIAS_ID = "GENERATE_FILE_ALIAS_ID";

  private String recType;
  private String seqNo;
  private String bankCode;
  private String compAccNo;
  private String compName;
  private String postDate;
  private String running;
  private String batch;
  private String effDate;
  private String filler;
  private boolean proposal;
  private String userTRef;
  private Long generateFileAliasId;

  private List<GIROFileDetail> giroFileDetails;
  private GIROFileTrailer giroFileTrailer;

  // Pull from

  public GIROFileHeader(Long id, String recType, String seqNo, String bankCode, String compAccNo, String compName, String postDate, String running, String batch, String effDate, String filler, boolean proposal, String userTRef, Long generateFileAliasId) {
    super(id);
    this.recType = recType;
    this.seqNo = seqNo;
    this.bankCode = bankCode;
    this.compAccNo = compAccNo;
    this.compName = compName;
    this.postDate = postDate;
    this.running = running;
    this.batch = batch;
    this.effDate = effDate;
    this.filler = filler;
    this.proposal = proposal;
    this.userTRef = userTRef;
    this.generateFileAliasId = generateFileAliasId;
  }

  @Override
  public String toString() {
    return recType + seqNo + bankCode + compAccNo + compName + postDate + batch + effDate + filler;
  }
}
