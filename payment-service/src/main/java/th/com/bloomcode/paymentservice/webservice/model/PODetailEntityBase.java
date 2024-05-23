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
public class PODetailEntityBase {
  /** Line No TODO:it should be int? */
  @XmlElement(name = "PO_ITEM")
  public String lineNo;

  /** Account Assignment */
  @XmlElement(name = "ACCTASSCAT")
  public String accType;

  @XmlElement(name = "ACCTASSCAT_NAME")
  private String accTypeName;


  /** Description */
  @XmlElement(name = "SHORT_TEXT")
  public String description;

  @XmlElement(name = "QUANTITY")
  public String qty;

  @XmlElement(name = "RESERVED")
  public String reserve;

  @XmlElement(name = "PO_UNIT")
  public String uom;

  /** iDempiere Unused */
  @XmlElement(name = "ORDERPR_UN")
  public String er;

  @XmlElement(name = "NET_PRICE")
  public String price;

  @XmlElement(name = "LAST_PRICE")
  public String priceLastPO;

  /** iDempiere Unused */
  @XmlElement(name = "MATL_GROUP")
  public String matlGroup;

  /** iDempiere Unused */
  @XmlElement(name = "PLANT")
  public String plaint;

  /**
   * Flag to display when Search : when search result of PO Detail…it is Flag (if Create this will
   * blank)
   */
  @XmlElement(name = "GR_IND")
  public String searchFlag;

  /** GL Account = Product (ใช้หา Budget Account เพื่อ Field tab Budget) */
  @XmlElement(name = "GL_ACCOUNT")
  public String glAccount;
  /** Cost Center (Fill ใน Budget tab) */
  @XmlElement(name = "COST_CENTER")
  public String budgetCostCenter;
  /** Budget Code (Fill ใน Budget tab) - Year dependent */
  @XmlElement(name = "BG_CODE")
  public String budgetCode;

  /** Budget Activity (Fill ใน Budget tab) - Year dependent */
  @XmlElement(name = "BG_ACTIVITY")
  public String budgetActivity;

  /** Source of Fund (Fill ใน Budget tab) */
  @XmlElement(name = "FUND_SOURCE")
  public String budgetFundSource;

  /** Cost Activity - Year dependent */
  @XmlElement(name = "COST_ACTIVITY")
  public String budgetCostActivity;

  /** Budget Reserve Line Item (Fill ใน Budget tab) */
  @XmlElement(name = "RES_DOC")
  public String budgetReserveLineDoc;
  /** iDempiere Unused */
  @XmlElement(name = "RES_ITEM")
  public String budgetReserveLineItem;
  /** GPSC Code */
  @XmlElement(name = "GPSC")
  public String gPSCCode;

  /** derived this "Payment center" to get Fund center */
  @XmlElement(name = "PAYMENT_CENTER")
  public String paymentCenter;

  /** Deposit Account(บัญชีเงินฝาก) */
  @XmlElement(name = "DEPOSIT_ACCOUNT")
  public String depositAccount;
  /** Deposit Account Owner (เจ้าของบัญชีเงินฝาก) */
  @XmlElement(name = "DEPOSIT_ACCOUNT_OWNER")
  public String depositAccountOwner;
  /** Sub Account(รหัสบัญชีย่อย) */
  @XmlElement(name = "SUB_ACCOUNT")
  public String depositAccountSub;
  /** Deposit Account Owner(รหัสเจ้าของบัญชีย่อย) */
  @XmlElement(name = "SUB_ACCOUNT_OWNER")
  public String depositAccountSubOwner;

  @XmlJavaTypeAdapter(TimestampAdapter.class)
  @XmlElement(name = "DELIVERY_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  public Timestamp eGPDateDelivery;
  /** iDempiere Unused */
  @XmlElement(name = "ITEM_CAT")
  public String itemCategory;
  /** iDempiere Unused */
  @XmlElement(name = "LIMIT")
  public String limit;
  /** in new PO.04 we will do Material Receipt in value by use Qty column */
  @XmlElement(name = "EXP_VALUE")
  public String amount;
  /** iDempiere Unused */
  @XmlElement(name = "ASSET_NO")
  public String assetNo;
  /** iDempiere Unused */
  @XmlElement(name = "SUB_NUMBER")
  public String assetSubNo;
  /** iDempiere Unused */
  @XmlElement(name = "CMMT_ITEM")
  public String cmmtItem;
  /** iDempiere Unused */
  @XmlElement(name = "AGREEMENT")
  public String agreement;
  /** iDempiere Unused */
  @XmlElement(name = "AGMT_ITEM")
  public String agreementItem;
  /** Inactive item (New field) */
  @XmlElement(name = "DELETE_IND")
  public String active;
  /** iDempiere Unused */
  @XmlElement(name = "PREQ_NAME")
  public String preqName;
  /** Status จาก PO history TODO:status line? */
  @XmlElement(name = "PO_STATUS")
  public String poStatus;

  @XmlElementWrapper(name = "ITEMS_TEXT")
  @XmlElementRef()
  public List<POTextItem> detailTexts;

  @XmlElementWrapper(name = "ITEMS_COND")
  @XmlElementRef()
  public List<POConditionItem> detailLineConditions;

  @XmlTransient public Timestamp dateOrder;

  @XmlTransient public Timestamp datePromised;

  @XmlElement(name = "BG_YEAR")
  public String bgYear;

  @XmlElement(name = "DOWN_PAYMENT")
  public BigDecimal downPayment;

  @XmlElement(name = "PO_LINE_STATUS")
  public String poLineStatus;

  @XmlElement(name = "MR_DOC_NO")
  public String mrDocNo;

  @XmlElement(name = "MR_LINE")
  public String mrLine;

  @XmlElement(name = "MR_FISCAL_YEAR")
  public String mrFiscalYear;

  @XmlElement(name = "lINE_NET_AMT")
  public BigDecimal lineNetAmt;

  @XmlElement(name = "IS_MR")
  public String isMr;

  @XmlElement(name = "IS_INVOICE")
  public String isInvoice;

  @XmlElement(name = "IS_DOWNPAYMENT")
  public String isDownPayment;

  @XmlElement(name = "IS_DOWNPAYMENT_PAID")
  public String isDownPaymentPaid = "N";

  @XmlElement(name = "IS_CONVERSION_COMPLETE")
  public String isConversionComplete;

  @XmlElement(name = "BALANCE_PRICE")
  public BigDecimal balancePrice = BigDecimal.ZERO;

  @XmlElement(name = "COLLECTION_BUDGET")
  public BigDecimal collectionBudget = BigDecimal.ZERO;

  @XmlElement(name = "COLLECTION_PHASE")
  public String collectionPhase;

  @XmlElement(name = "ADVANCE_PRICE")
  public BigDecimal advancePrice = BigDecimal.ZERO;

  @XmlElement(name = "IS_HISTORY")
  public String isHistory;

  @XmlElement(name = "INVOICE_AMT")
  public BigDecimal invoiceAmt;

  @XmlElement(name = "CLOSE_STATUS")
  private boolean isClose;

  @XmlElement(name = "EDIT_STATUS")
  private boolean isEditable;

  @XmlElement(name = StandardXMLTagName.RECORD_ID)
  private int recordID;

  @XmlElement(name = "SUB_BOOK_GL")
  private String subBookGL = null;

  @XmlElement(name = "SUB_BOOK_GL_NAME")
  private String subBookGLName = null;

  @XmlElement(name = StandardXMLTagName.SUB_BOOK + "_OPTION")
  private String subBookOption = null;
}
