package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class FICPbkDoc {
	@XmlElement(name = StandardXMLTagName.VENDOR_CODE)
	private String vendor = "";
	@XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
	private String accountDocNo = "";
	@XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
	private String fiscalYear = "";
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = "";
	@XmlElement(name = StandardXMLTagName.REVERSAL_ACCOUNT_DOCUMENT_NO)
	private String revAccountDocNo = "";
	@XmlElement(name = StandardXMLTagName.REVERSAL_FISCAL_YEAR)
	private String revFiscalYear = "";
}
