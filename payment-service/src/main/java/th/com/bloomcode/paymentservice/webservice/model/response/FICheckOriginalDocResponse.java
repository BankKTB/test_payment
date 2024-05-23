package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.FIDocNoOriginalDoc;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSResponseBaseXML;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class FICheckOriginalDocResponse extends WSResponseBaseXML {
	
	@XmlElementWrapper(name = StandardXMLTagName.ITEM_LIST)
	@XmlElement(name = StandardXMLTagName.ITEM_ENTRY)
	private List<FIDocNoOriginalDoc> lines = new ArrayList<>();
	

	
}
