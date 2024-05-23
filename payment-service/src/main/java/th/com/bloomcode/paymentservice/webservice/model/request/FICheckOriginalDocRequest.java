package th.com.bloomcode.paymentservice.webservice.model.request;


import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.FIDocNo;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSRequestBaseXML;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class FICheckOriginalDocRequest extends WSRequestBaseXML {
	
	@XmlElementWrapper(name = StandardXMLTagName.ITEM_LIST)
	@XmlElement(name = StandardXMLTagName.ITEM_ENTRY)
	private List<FIDocNo> lines = new ArrayList<>();

	
}
