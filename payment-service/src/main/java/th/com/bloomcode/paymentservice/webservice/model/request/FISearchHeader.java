package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.adapter.SimpleDateAdapter;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Data
public class FISearchHeader extends FIHeader2 {
	
	@XmlElement(name = StandardXMLTagName.PAYMENT_BLOCK)
	private String paymentBlock = null;
	
	@XmlElement(name = StandardXMLTagName.VENDOR)
	private String vendor = null;
	
	@XmlElement(name = StandardXMLTagName.VENDOR_NAME)
	private String vendorName = null;
	
	@XmlElement(name = StandardXMLTagName.ASSIGNMENT)
	private String assignment = null;
	
	@XmlElement(name = StandardXMLTagName.REFERENCE3)
	private String reference3 = null;
	
	@XmlElement(name = StandardXMLTagName.CLEARING_DATE_ACCT)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp clearingDateAcct = null;
	@XmlElement(name = StandardXMLTagName.CLEARING_DOC_NO)
	private String clearingDocNo = null;
	@XmlElement(name = StandardXMLTagName.CLEARING_FISCAL_YEAR)
	private String clearingFiscalYear = null;
	

	
}
