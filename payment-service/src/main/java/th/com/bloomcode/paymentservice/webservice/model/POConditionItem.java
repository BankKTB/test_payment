package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "COND_ITEM")
@XmlAccessorType(XmlAccessType.FIELD)
public class POConditionItem {
  @XmlElement(name = "CONDITION_NO")
  private String conditionNo;

  @XmlElement(name = "ITM_NUMBER")
  private String itmNum;

  @XmlElement(name = "COND_ST_NO")
  private String conditionStNo;

  @XmlElement(name = "COND_COUNT")
  private String conditionCount;

  @XmlElement(name = "COND_TYPE")
  private String conditionType;
  /** % VAT */
  @XmlElement(name = "COND_VALUE")
  private String conditionValue;
  /** iDempiere Unused */
  @XmlElement(name = "CURRENCY")
  private String currency;
  /** Change Type (I, E, D, U) */
  @XmlElement(name = "CHANGE_ID")
  private String changeType;
}
