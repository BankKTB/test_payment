package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSRequestBaseXML;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class APCreateJULine  {
	
	@XmlElement(name = StandardXMLTagName.PAYMENT_ACCOUNT)
	private String payAccount = null;
	@XmlElement(name = StandardXMLTagName.GL_ACCOUNT)
	private String glAccount = null;
	
	@XmlElement(name = StandardXMLTagName.AMOUNT)
	private BigDecimal amount = null;
	

	
}
