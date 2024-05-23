package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSResponseBaseXML;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class BRConsumptionResponse extends WSResponseBaseXML {
  @XmlElement(name = "CONSUMPTIONS")
  private BRConsumptionGroupResult group = new BRConsumptionGroupResult();
}
