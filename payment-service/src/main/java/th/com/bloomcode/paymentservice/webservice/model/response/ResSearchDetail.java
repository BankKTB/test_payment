package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.AbstractResService;
import th.com.bloomcode.paymentservice.webservice.model.PODetailEntitySearch;
import th.com.bloomcode.paymentservice.webservice.model.POHeaderEntitySearch;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;


import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "RESPONSE")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResSearchDetail extends AbstractResService {
  @XmlElementRef() private POHeaderEntitySearch header;

  @XmlElementWrapper(name = "ITEMS")
  @XmlElementRef()
  private List<PODetailEntitySearch> items;

  @XmlElement(name = StandardXMLTagName.WITHHOLDING_TAX_TYPE)
  private List<String> taxTypes = new ArrayList<>();
}
