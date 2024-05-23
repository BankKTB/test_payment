package th.com.bloomcode.paymentservice.webservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ITEM")
@XmlAccessorType(XmlAccessType.FIELD)
public class PODetailEntitySearch extends PODetailEntityBase {
  /** Cost Center (Fill ใน Budget tab) */
  @XmlElement(name = "COST_CENTER_NAME")
  public String budgetCostCenterName;

  /** GL Account = Product (ใช้หา Budget Account เพื่อ Field tab Budget) */
  @XmlElement(name = "GL_ACCOUNT_NAME")
  public String glAccountName;

  /** Budget Code (Fill ใน Budget tab) - Year dependent */
  @XmlElement(name = "BG_CODE_NAME")
  public String budgetCodeName;

  /** Budget Activity (Fill ใน Budget tab) - Year dependent */
  @XmlElement(name = "BG_ACTIVITY_NAME")
  public String budgetActivityName;

  /** Source of Fund (Fill ใน Budget tab) */
  @XmlElement(name = "FUND_SOURCE_NAME")
  public String budgetFundSourceName;

  /** GPSC Code */
  @XmlElement(name = "GPSC_NAME")
  public String gPSCCodeName;

  /** Deposit Account(บัญชีเงินฝาก) */
  @XmlElement(name = "DEPOSIT_ACCOUNT_NAME")
  public String depositAccountName;

  /** Deposit Account Owner (เจ้าของบัญชีเงินฝาก) */
  @XmlElement(name = "DEPOSIT_ACCOUNT_OWNER_NAME")
  public String depositAccountOwnerName;

  /** Sub Account(รหัสบัญชีย่อย) */
  @XmlElement(name = "SUB_ACCOUNT_NAME")
  public String depositAccountSubName;

  /** Deposit Account Owner(รหัสเจ้าของบัญชีย่อย) */
  @XmlElement(name = "SUB_ACCOUNT_OWNER_NAME")
  public String depositAccountSubOwnerName;

  /** Cost Activity - Year dependent */
  @XmlElement(name = "COST_ACTIVITY_NAME")
  public String budgetCostActivityName;

  @XmlElement(name = "UOM_NAME")
  public String uomName;
}
