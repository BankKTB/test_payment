package th.com.bloomcode.paymentservice.webservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public class POHeaderEntityBase {
  @XmlElement(name = "IS_DOWNPAYMENT_PAID")
  public boolean isDownPaymentPaid;
  /** 0 = Simulate mode before save Doc 1 = Create Doc */
  @XmlElement(name = "FLAG")
  private Integer flag;
  /** Web Online Form ID */
  @XmlElement(name = "FORMID")
  private String formID;
  /** for edit mode only, WEBUI will pass Doc no for create mode, don't pass */
  @XmlElement(name = "DOC_NO")
  private String docNo;
  /** id of Doc Type TODO: id or name? */
  @XmlElement(name = "DOC_TYPE")
  private String doctype;
  /** Vendor Code */
  @XmlElement(name = "VENDOR")
  private String vendorCode;
  /**
   * eGPContractNumber, eGPProjectNumber on format (59016000125,590114000004) 1st for
   * eGPContractNumber
   */
  @XmlElement(name = "EGP_NUMBER")
  private String eGPCPNumber;
  /** Doc Date */
  @XmlElement(name = "DOC_DATE")
  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp docDate;
  /** iDempiere UnusedString */
  @XmlElement(name = "PURCH_ORG")
  private String purchOrg;
  /** iDempiere Unused */
  @XmlElement(name = "PUR_GROUP")
  private String purchGroup;
  /** Company code = AD_Org TODO:? */
  @XmlElement(name = "COMP_CODE")
  private String companyCode;
  @XmlElement(name = "COMP_CODE_NAME")
  private String companyName;
  /** iDempiere Unused */
  @XmlElement(name = "CURRENCY")
  private String currencyIsoCode;
  /** eGPOrderNo */
  @XmlElement(name = "EGP_ORDER_NO")
  private String eGPOrderNo;
  /** ProcureMethod */
  @XmlElement(name = "PROCURE_METHOD")
  private String procureMethod;
  /** eGPBankaccount TODO:? */
  @XmlElement(name = "BANK_ACCOUNT")
  private String eGPBpartnerBankAccountNo;
  /** Tax ID for search to get Business partner */
  @XmlElement(name = "TAX_ID")
  private String eGPBpartnerTaxID;
  /** eGPDateContractEnd */
  @XmlElement(name = "CONTRACT_DATE_END")
  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp eGPDateContractEnd;
  /** Saleperson (SAP เก็บ Ref มาจาก Web) */
  @XmlElement(name = "SALES_PERS")
  private String saleRepresentative;
  /** Payment Center */
  @XmlElement(name = "PAYMENT_CENTER")
  private String paymentCenterCode;
  @XmlElement(name = "PAYMENT_CENTER_NAME")
  private String paymentCenterName;
  /** iDempiere Unused */
  @XmlElement(name = "EXCH_RATE")
  private String exchRate;
  /** iDempiere Unused */
  @XmlElement(name = "EX_RATE_FX")
  private String exchRateFx;
  /** eGPDateContract */
  @XmlElement(name = "CONTRACT_DATE_START")
  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  private Timestamp eGPDateContractStart;
  /** iDempiere Unused */
  @XmlElement(name = "COLLECT_NO")
  private String collectNo;
  /** Idempiere : new field to keep web online user code */
  @XmlElement(name = "USERWEBONLINE")
  private String webUserCode;

  @XmlElementWrapper(name = "HEADERS_COND")
  @XmlElementRef()
  private List<POConditionItem> headerConditions;

  @XmlElementWrapper(name = "HEADERS_TEXT")
  @XmlElementRef()
  private List<POTextItem> headerTextItems;

  @XmlElement(name = "DOC_STATUS")
  private String docStatus;

  @XmlElement(name = "VENDOR_GROUP")
  private String vendorGroup;

  @XmlElement(name = "APPROVE_STATUS")
  private String approveStatus;

  @XmlElement(name = "PAYEE_NAME")
  private String payeeName;

  @XmlElement(name = "FI_AREA")
  private String fiArea;

  @XmlElement(name = "WTX_CODE")
  private String wtxCode;

  @XmlElement(name = "WTX_TYPE")
  private String wtxType;

  @XmlElement(name = "IS_ADVANCE")
  private String isAdvance;

  @XmlElement(name = "IS_HEAD_ADVANCE")
  private String isHeadAdvance;

  @XmlElement(name = "DOWN_PAYMENT")
  private BigDecimal downPayment;

  @XmlElement(name = "IS_CONVERSION")
  private boolean isConversion;

  @XmlElement(name = "CONTRACT_PRICE")
  private BigDecimal contractPrice = BigDecimal.ZERO;

  @XmlElement(name = "RELATE_BUDGET")
  private String relateBudget;

  @XmlElement(name = "ADVANCE_PAYMENT")
  private BigDecimal advancePayment = BigDecimal.ZERO;

  @XmlElement(name = "CREATED")
  private String CREATED;

  @XmlElement(name = "IS_ALT_PAYEE")
  private boolean isAlternatePayee = false;

  @XmlElement(name = "IS_LOAN")
  private boolean loan = false;

}
