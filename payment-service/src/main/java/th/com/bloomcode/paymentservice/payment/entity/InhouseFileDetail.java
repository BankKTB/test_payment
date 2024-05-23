package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "INHOUSE_FILE_DETAIL")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class InhouseFileDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INHOUSE_FILE_DETAIL_SEQ")
  @SequenceGenerator(sequenceName = "INHOUSE_FILE_DETAIL_SEQ", allocationSize = 1, name = "INHOUSE_FILE_DETAIL_SEQ")
  private Long id;

  @Column(name = "USER_TREF")
  private String userTref;

  @Column(name = "SEQ_NO_FILE")
  private String seqNoFile;

  @Column(name = "TMP_NO_DR")
  private int tmpNoDr;

  @Column(name = "TMP_NO_CR")
  private int tmpNoCr;

  @Column(name = "AMT_NO_DR")
  private int amtNoDr;

  @Column(name = "AMT_NO_CR")
  private int amtNoCr;

  @Column(name = "TMP_DR")
  private BigDecimal tmpDr;

  @Column(name = "TMP_CR")
  private BigDecimal tmpCr;

  @Column(name = "AMT_DR")
  private BigDecimal amtDr;

  @Column(name = "AMT_CR")
  private BigDecimal amtCr;

  @Column(name = "PAYMENT_TYPE")
  private String paymentType;

  @Column(name = "BANK_KEY")
  private String bankKey;

  @Column(name = "BANK_ACCOUNT_NO")
  private String bankAccountNo;

  @Column(name = "CR_VALUE")
  private BigDecimal crValue;

  @Column(name = "COMP_CODE")
  private String compCode;

  @Column(name = "FI_AREA")
  private String fiArea;

  @Column(name = "ACC_DOC_NO")
  private String accDocNo;

  @Column(name = "FISCAL_YEAR")
  private String fiscalYear;

  @Column(name = "PAYING_COMP_CODE")
  private String payingCompCode;

  @Column(name = "PAYMENT_DOC_NO")
  private String paymentDocNo;

//  @Column(name = "PAYMENT_FISCAL_YEAR")
//  private String paymentFiscalYear;

  @Column(name = "REC_TYPE")
  private String recType;

  @Column(name = "SEQ_NO")
  private String seqNo;

  @Column(name = "BANK_CODE")
  private String bankCode;

  @Column(name = "ACCOUNT_NO")
  private String accountNo;

  @Column(name = "TRAN_CODE")
  private String tranCode;

  @Column(name = "AMOUNT_VALUE")
  private BigDecimal amountValue;

  @Column(name = "AMOUNT")
  private String amount;

  @Column(name = "CREDIT_MEMO_AMOUNT")
  private BigDecimal creditMemoAmount;

  @Column(name = "SERVICE_TYPE")
  private String serviceType;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "BATCH")
  private String batch;

  @Column(name = "REC_ID")
  private String recId;

  @Column(name = "REF_NUM")
  private String refNum;

  @Column(name = "REF1")
  private String ref1;

  @Column(name = "CHK_REG")
  private String chkReg;

  @Column(name = "VENDOR_TAX_ID")
  private String vendorTaxId;

  @Column(name = "USER_REF")
  private String userRef;

  @Column(name = "ORIGINAL_ACC_DOC_NO")
  private String originalAccDocNo;
  @Column(name = "ORIGINAL_FISCAL_YEAR")
  private String originalFiscalYear;
  @Column(name = "ORIGINAL_COMP_CODE")
  private String originalCompCode;
  @Column(name = "ORIGINAL_DOC_TYPE")
  private String originalDocType;

  @Column(name = "ORIGINAL_WTX_AMOUNT")
  private BigDecimal originalWtxAmount;
  @Column(name = "ORIGINAL_WTX_BASE")
  private BigDecimal originalWtxBase;
  @Column(name = "ORIGINAL_WTX_AMOUNT_P")
  private BigDecimal originalWtxAmountP;
  @Column(name = "ORIGINAL_WTX_BASE_P")
  private BigDecimal originalWtxBaseP;


  @Column(name = "PAYMENT_FISCAL_YEAR")
  private String paymentFiscalYear;
  @Column(name = "PAYMENT_COMP_CODE")
  private String paymentCompCode;

  @ManyToOne(fetch = FetchType.LAZY)
  private InhouseFileHeader inhouseFileHeader;

  @Override
  public String toString() {
    System.out.println("InhouseFileDetail{" +
            "recType='" + recType + " : " + recType.length() + '\'' +
            ", seqNo='" + seqNo + " : " + seqNo.length() + '\'' +
            ", bankCode='" + bankCode + " : " + bankCode.length() + '\'' +
            ", accountNo='" + accountNo + " : " + accountNo.length() + '\'' +
            ", tranCode='" + tranCode + " : " + tranCode.length() + '\'' +
            ", amount='" + amount + " : " + amount.length() + '\'' +
            ", serviceType='" + serviceType + " : " + serviceType.length() + '\'' +
            ", status='" + status + " : " + status.length() + '\'' +
            ", batch='" + batch + " : " + batch.length() + '\'' +
            ", recId='" + recId + " : " + recId.length() + '\'' +
            ", refNum='" + refNum + " : " + refNum.length() + '\'' +
            ", ref1='" + ref1 + " : " + ref1.length() + '\'' +
            ", chkReg='" + chkReg + " : " + chkReg.length() + '\'' +
            ", vendorTaxId='" + vendorTaxId + " : " + vendorTaxId.length() + '\'' +
            ", userRef='" + userRef + " : " + userRef.length() + '\'' +
            ", inhouseFileHeader=" + inhouseFileHeader +
            '}');
    return recType + seqNo + bankCode + accountNo + tranCode + amount + serviceType + status + batch + recId + refNum + ref1 + chkReg + vendorTaxId + userRef;
  }
}
