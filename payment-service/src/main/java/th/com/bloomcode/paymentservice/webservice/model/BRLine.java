package th.com.bloomcode.paymentservice.webservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BRLine extends BudgetDimensionBaseXML {
  @XmlElement(name = "ITEM_TEXT")
  private String itemText = null;

  @XmlElement(name = StandardXMLTagName.AMOUNT)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal amount = null;

  @XmlElement(name = StandardXMLTagName.OPEN_AMOUNT)
  private BigDecimal openAmount = null;

  @XmlElement(name = StandardXMLTagName.PAYMENT_CENTER)
  private String paymentCenter = null;

  @XmlElement(name = StandardXMLTagName.PAYMENT_CENTER_NAME)
  private String paymentCenterName = null;
}
