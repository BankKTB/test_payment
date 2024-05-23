package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class WSRequestBaseXML {
	@XmlTransient
	private List<BaseErrorLine> errors = new ArrayList<>();
	
	@XmlElement(name = StandardXMLTagName.FORM_ID)
	private String formID = null;
	
	@XmlElement(name = StandardXMLTagName.FLAG)
	private int flag = 1;
	
	@XmlElement(name = StandardXMLTagName.WEB_INFO)
	private WSWebInfo webInfo;
}
