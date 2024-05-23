package th.com.bloomcode.paymentservice.webservice.model.response;



import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSResponseBaseXML;
import th.com.bloomcode.paymentservice.webservice.model.request.FISearchHeader;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class FISearchResponse2 extends WSResponseBaseXML {
	@XmlElementWrapper(name = StandardXMLTagName.HEADER_LIST)
	@XmlElement(name = StandardXMLTagName.HEADER)
	private List<FISearchHeader> headers = new ArrayList<>();
	

	
	public void addHeader(FISearchHeader line) {
		headers.add(line);
	}
}
