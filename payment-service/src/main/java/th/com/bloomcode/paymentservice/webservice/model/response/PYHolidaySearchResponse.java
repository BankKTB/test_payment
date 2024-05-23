package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.Holiday;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSResponseBaseXML;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class PYHolidaySearchResponse extends WSResponseBaseXML {
	@XmlElement(name = StandardXMLTagName.ITEM_ENTRY)
	private List<Holiday> holidays = new ArrayList<>();
}