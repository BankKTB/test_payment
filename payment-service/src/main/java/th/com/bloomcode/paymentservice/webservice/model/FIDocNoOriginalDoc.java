package th.com.bloomcode.paymentservice.webservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class FIDocNoOriginalDoc extends FIDocNo {
	
	@XmlElement(name = "ORI_" + StandardXMLTagName.COMPANY_CODE)
	private String oriCompCode = null;
	
	@XmlElement(name = "ORI_" + StandardXMLTagName.FISCAL_YEAR)
	private String oriFiscalYear = null;
	
	@XmlElement(name = "ORI_" + StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
	private String oriDocNo = null;
	
	@XmlElement(name = StandardXMLTagName.ERROR_TEXT)
	private String errorTxt = null;
	

}
