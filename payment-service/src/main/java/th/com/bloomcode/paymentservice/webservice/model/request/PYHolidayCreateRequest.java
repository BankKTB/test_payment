package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.Holiday;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSRequestBaseXML;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class PYHolidayCreateRequest extends WSRequestBaseXML {
	@XmlElement(name = StandardXMLTagName.ITEM_ENTRY)
	private List<Holiday> holidays = new ArrayList<>();
}
