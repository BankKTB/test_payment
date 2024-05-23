package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class FIDocNo {
	
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = null;
	
	@XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
	private String fiscalYear = null;
	
	@XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
	private String docNo = null;

}
