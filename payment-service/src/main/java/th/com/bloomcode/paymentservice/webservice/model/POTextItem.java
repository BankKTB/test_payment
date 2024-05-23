package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "TEXT_ITEM")
@XmlAccessorType(XmlAccessType.FIELD)
public class POTextItem {
  /** refer to po header or po line */
  @XmlElement(name = "PO_ITEM")
  private String parentCode;

  @XmlElement(name = "TEXT_FORM")
  private String textForm;

  @XmlElement(name = "TEXT_ID")
  private String textCode;

  @XmlElement(name = "TEXT_LINE")
  private String textMsg;
}
