package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class FICPbkDetailRequest {
	
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = null;
	
	@XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
	private String fiscalYear = null;
	
	@XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
	private String docNo = null;

	@XmlElement(name = StandardXMLTagName.WEB_INFO)
	private WSWebInfo webInfo;
}
