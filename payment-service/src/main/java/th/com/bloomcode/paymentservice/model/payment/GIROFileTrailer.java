package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class GIROFileTrailer extends BaseModel {

  public static final String TABLE_NAME = "GIRO_FILE_TRAILER";

  public static final String COLUMN_NAME_GIRO_FILE_TRAILER_ID = "ID";
  public static final String COLUMN_NAME_FILE_TYPE = "FILE_TYPE";
  public static final String COLUMN_NAME_PAYMENT_METHOD = "PAYMENT_METHOD";
  public static final String COLUMN_NAME_NUM_DR = "NUM_DR";
  public static final String COLUMN_NAME_AMT_DR = "AMT_DR";
  public static final String COLUMN_NAME_NUM_CR = "NUM_CR";
  public static final String COLUMN_NAME_AMT_CR = "AMT_CR";
  public static final String COLUMN_NAME_REC_TYPE = "REC_TYPE";
  public static final String COLUMN_NAME_SEQ_NO = "SEQ_NO";
  public static final String COLUMN_NAME_BANK_CODE = "BANK_CODE";
  public static final String COLUMN_NAME_COMP_ACC_NO = "COMP_ACC_NO";
  public static final String COLUMN_NAME_NUMBER_DR = "NUMBER_DR";
  public static final String COLUMN_NAME_TOTAL_DR = "TOTAL_DR";
  public static final String COLUMN_NAME_NUMBER_CR = "NUMBER_CR";
  public static final String COLUMN_NAME_TOTAL_CR = "TOTAL_CR";
  public static final String COLUMN_NAME_FILLER = "FILLER";
  public static final String COLUMN_NAME_GIRO_FILE_HEADER_ID = "GIRO_FILE_HEADER_ID";

  private String fileType;
  private String paymentMethod;
  private int numDr;
  private BigDecimal amtDr;
  private int numCr;
  private BigDecimal amtCr;
  private String recType;
  private String seqNo;
  private String bankCode;
  private String compAccNo;
  private String numberDr;
  private String totalDr;
  private String numberCr;
  private String totalCr;
  private String filler;
  private String giroFileHeaderId;

//  @OneToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "GIRO_FILE_HEADER_ID")
//  private GIROFileHeader giroFileHeader;

  @Override
  public String toString() {
    return recType + seqNo + bankCode + compAccNo + numberDr + totalDr + numberCr + totalCr + filler;
  }
}
