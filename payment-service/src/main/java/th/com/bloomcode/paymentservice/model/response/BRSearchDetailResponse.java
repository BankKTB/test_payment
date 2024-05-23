package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.*;


import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class BRSearchDetailResponse extends WSResponseBaseXML {

  @XmlElement(name = StandardXMLTagName.HEADER)
  private BRHeader header;

  @XmlElementWrapper(name = "ITEMS")
  @XmlElement(name = "ITEM")
  private List<BRLine> lines = new ArrayList<>();

  @XmlElementWrapper(name = "CONTRACTS")
  @XmlElement(name = "CONTRACT")
  private List<BRContract> contracts = new ArrayList<>();

  @XmlElementWrapper(name = "REFERENCES")
  @XmlElement(name = "BR")
  private List<BRSearchResult> references = new ArrayList<>();
}
