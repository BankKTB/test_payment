package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class POChangeHistoryResponse extends WSResponseBaseXML {
  @XmlElementWrapper(name = "HISTORIES")
  @XmlElement(name = "HISTORY")
  public List<POChangeHistoryResult> histories = new ArrayList<>();
}
