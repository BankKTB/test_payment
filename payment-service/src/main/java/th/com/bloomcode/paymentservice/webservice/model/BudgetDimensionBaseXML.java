package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BudgetDimensionBaseXML {
  // Value code
  @XmlElement(name = StandardXMLTagName.BUDGET_YEAR)
  private String budgetYear = null;

  @XmlElement(name = StandardXMLTagName.SOURCE_OF_FUND)
  private String fundSource = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_CODE)
  private String budgetCode = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_AREA)
  private String budgetArea = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_ACCOUNT)
  private String budgetAccount = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_ACTIVITY)
  private String budgetActivity = null;

  @XmlElement(name = StandardXMLTagName.FUND_CENTER)
  private String fundCenter = null;

  @XmlElement(name = StandardXMLTagName.COST_CENTER)
  private String costCenter = null;

  @XmlElement(name = StandardXMLTagName.COST_ACTIVITY)
  private String costAtivity = null;

  @XmlElement(name = StandardXMLTagName.AREA_OF_WORK)
  private String areaOfWork = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_PROJECT)
  private String project = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_STRATEGY)
  private String strategy = null;

  // Name
  @XmlElement(name = StandardXMLTagName.SOURCE_OF_FUND_NAME)
  private String fundSourceName = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_CODE_NAME)
  private String budgetCodeName = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_AREA_NAME)
  private String budgetAreaName = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_ACCOUNT_NAME)
  private String budgetAccountName = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_ACTIVITY_NAME)
  private String budgetActivityName = null;

  @XmlElement(name = StandardXMLTagName.FUND_CENTER_NAME)
  private String fundCenterName = null;

  @XmlElement(name = StandardXMLTagName.COST_CENTER_NAME)
  private String costCenterName = null;

  @XmlElement(name = StandardXMLTagName.COST_ACTIVITY_NAME)
  private String costAtivityName = null;

  @XmlElement(name = StandardXMLTagName.AREA_OF_WORK_NAME)
  private String areaOfWorkName = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_PROJECT_NAME)
  private String projectName = null;

  @XmlElement(name = StandardXMLTagName.BUDGET_STATEGY_NAME)
  private String strategyName = null;
}
