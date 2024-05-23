package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class InhouseFileDetail extends BaseModel {

  public static final String TABLE_NAME = "INHOUSE_FILE_DETAIL";

  public static final String COLUMN_NAME_GIRO_FILE_DETAIL_ID = "ID";
  public static final String COLUMN_NAME_USER_TREF = "USER_TREF";
  public static final String COLUMN_NAME_SEQ_NO_FILE = "SEQ_NO_FILE";
  public static final String COLUMN_NAME_TMP_NO_DR = "TMP_NO_DR";
  public static final String COLUMN_NAME_TMP_NO_CR = "TMP_NO_CR";
  public static final String COLUMN_NAME_AMT_NO_DR = "AMT_NO_DR";
  public static final String COLUMN_NAME_AMT_NO_CR = "AMT_NO_CR";
  public static final String COLUMN_NAME_TMP_DR = "TMP_DR";
  public static final String COLUMN_NAME_TMP_CR = "TMP_CR";
  public static final String COLUMN_NAME_AMT_DR = "AMT_DR";
  public static final String COLUMN_NAME_AMT_CR = "AMT_CR";
  public static final String COLUMN_NAME_PAYMENT_TYPE = "PAYMENT_TYPE";
  public static final String COLUMN_NAME_BANK_KEY = "BANK_KEY";
  public static final String COLUMN_NAME_BANK_ACCOUNT_NO = "BANK_ACCOUNT_NO";
  public static final String COLUMN_NAME_CR_VALUE = "CR_VALUE";
  public static final String COLUMN_NAME_COMP_CODE = "COMP_CODE";
  public static final String COLUMN_NAME_FI_AREA = "FI_AREA";
  public static final String COLUMN_NAME_ACC_DOC_NO = "ACC_DOC_NO";
  public static final String COLUMN_NAME_FISCAL_YEAR = "FISCAL_YEAR";
  public static final String COLUMN_NAME_PAYING_COMP_CODE = "PAYING_COMP_CODE";
  public static final String COLUMN_NAME_PAYMENT_DOC_NO = "PAYMENT_DOC_NO";
  public static final String COLUMN_NAME_REC_TYPE = "REC_TYPE";
  public static final String COLUMN_NAME_SEQ_NO = "SEQ_NO";
  public static final String COLUMN_NAME_BANK_CODE = "BANK_CODE";
  public static final String COLUMN_NAME_ACCOUNT_NO = "ACCOUNT_NO";
  public static final String COLUMN_NAME_TRAN_CODE = "TRAN_CODE";
  public static final String COLUMN_NAME_AMOUNT_VALUE = "AMOUNT_VALUE";
  public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
  public static final String COLUMN_NAME_CREDIT_MEMO_AMOUNT = "CREDIT_MEMO_AMOUNT";
  public static final String COLUMN_NAME_SERVICE_TYPE = "SERVICE_TYPE";
  public static final String COLUMN_NAME_STATUS = "STATUS";
  public static final String COLUMN_NAME_BATCH = "BATCH";
  public static final String COLUMN_NAME_REC_ID = "REC_ID";
  public static final String COLUMN_NAME_REF_NUM = "REF_NUM";
  public static final String COLUMN_NAME_REF1 = "REF1";
  public static final String COLUMN_NAME_CHK_REG = "CHK_REG";
  public static final String COLUMN_NAME_VENDOR_TAX_ID = "VENDOR_TAX_ID";
  public static final String COLUMN_NAME_USER_REF = "USER_REF";
  public static final String COLUMN_NAME_ORIGINAL_ACC_DOC_NO = "ORIGINAL_ACC_DOC_NO";
  public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";
  public static final String COLUMN_NAME_ORIGINAL_COMP_CODE = "ORIGINAL_COMP_CODE";
  public static final String COLUMN_NAME_ORIGINAL_DOC_TYPE = "ORIGINAL_DOC_TYPE";
  public static final String COLUMN_NAME_ORIGINAL_WTX_AMOUNT = "ORIGINAL_WTX_AMOUNT";
  public static final String COLUMN_NAME_ORIGINAL_WTX_BASE = "ORIGINAL_WTX_BASE";
  public static final String COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P = "ORIGINAL_WTX_AMOUNT_P";
  public static final String COLUMN_NAME_ORIGINAL_WTX_BASE_P = "ORIGINAL_WTX_BASE_P";
  public static final String COLUMN_NAME_PAYMENT_FISCAL_YEAR = "PAYMENT_FISCAL_YEAR";
  public static final String COLUMN_NAME_PAYMENT_COMP_CODE = "PAYMENT_COMP_CODE";
  public static final String COLUMN_NAME_GIRO_FILE_HEADER_ID = "INHOUSE_FILE_HEADER_ID";

  private String userTref;
  private String seqNoFile;
  private int tmpNoDr;
  private int tmpNoCr;
  private int amtNoDr;
  private int amtNoCr;
  private BigDecimal tmpDr;
  private BigDecimal tmpCr;
  private BigDecimal amtDr;
  private BigDecimal amtCr;
  private String paymentType;
  private String bankKey;
  private String bankAccountNo;
  private BigDecimal crValue;
  private String compCode;
  private String fiArea;
  private String accDocNo;
  private String fiscalYear;
  private String payingCompCode;
  private String paymentDocNo;
  private String recType;
  private String seqNo;
  private String bankCode;
  private String accountNo;
  private String tranCode;
  private BigDecimal amountValue;
  private String amount;
  private BigDecimal creditMemoAmount;
  private String serviceType;
  private String status;
  private String batch;
  private String recId;
  private String refNum;
  private String ref1;
  private String chkReg;
  private String vendorTaxId;
  private String userRef;
  private String originalAccDocNo;
  private String originalFiscalYear;
  private String originalCompCode;
  private String originalDocType;
  private BigDecimal originalWtxAmount;
  private BigDecimal originalWtxBase;
  private BigDecimal originalWtxAmountP;
  private BigDecimal originalWtxBaseP;
  private String invoiceAccDocNo;
  private String invoiceFiscalYear;
  private String invoiceCompCode;
  private String invoiceDocType;
  private BigDecimal invoiceWtxAmount;
  private BigDecimal invoiceWtxBase;
  private BigDecimal invoiceWtxAmountP;
  private BigDecimal invoiceWtxBaseP;
  private String paymentFiscalYear;
  private String paymentCompCode;
  private Long inhouseFileHeader;

  // log sum
  private boolean sumFile;

  @Override
  public String toString() {
    return recType + seqNo + bankCode + accountNo + tranCode + amount + serviceType + status + batch + recId + refNum + ref1 + chkReg + vendorTaxId + userRef;
  }
}