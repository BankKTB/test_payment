package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.adapter.SimpleDateAdapter;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSRequestBaseXML;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class FISearchRequest2 extends WSRequestBaseXML {
	
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = null;
	@XmlElement(name = StandardXMLTagName.FI_AREA)
	private String fiArea = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_CENTER)
	private String paymentCenter = null;
	
	@XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO_FROM)
	private String accountDocNoFrom = null;
	@XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO_TO)
	private String accountDocNoTo = null;
	@XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
	private String fiscalYear = null;
	
	@XmlElement(name = StandardXMLTagName.CREATED_FROM)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp createdFrom = null;
	
	@XmlElement(name = StandardXMLTagName.CREATED_TO)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp createdTo = null;
	
	@XmlElement(name = StandardXMLTagName.DATE_ACCT_FROM)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp dateAcctFrom = null;
	
	@XmlElement(name = StandardXMLTagName.DATE_ACCT_TO)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp dateAcctTo = null;
	
	@XmlElement(name = StandardXMLTagName.DATE_DOC_FROM)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp dateDocFrom = null;
	
	@XmlElement(name = StandardXMLTagName.DATE_DOC_TO)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp dateDocTo = null;
	
	@XmlElement(name = StandardXMLTagName.HEADER_REFERENCE_FROM)
	private String headerReferenceFrom = null;
	@XmlElement(name = StandardXMLTagName.HEADER_REFERENCE_TO)
	private String headerReferenceTo = null;
	@XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE_FROM)
	private String docTypeFrom = null;
	@XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE_TO)
	private String docTypeTo = null;
	@XmlElement(name = StandardXMLTagName.VENDOR_TAXID)
	private String vendorTaxID = null;
	@XmlElement(name = StandardXMLTagName.PERIOD)
	private int period = 0;
	@XmlElement(name = StandardXMLTagName.COST_CENTER1_FROM)
	private String costCenter1From = null;
	@XmlElement(name = StandardXMLTagName.COST_CENTER1_TO)
	private String costCenter1To = null;
	@XmlElement(name = StandardXMLTagName.COST_CENTER2_FROM)
	private String costCenter2From = null;
	@XmlElement(name = StandardXMLTagName.COST_CENTER2_TO)
	private String costCenter2To = null;
	
	// สำหรับ นส09-1 ถึง 4
	@XmlElement(name = StandardXMLTagName.DOC_HEADER_TEXT + "_FROM")
	private String docHeaderTextFrom = null;
	@XmlElement(name = StandardXMLTagName.DOC_HEADER_TEXT + "_TO")
	private String docHeaderTextTo = null;
	@XmlElement(name = StandardXMLTagName.ASSIGNMENT + "_FROM")
	private String assignmentFrom = null;
	@XmlElement(name = StandardXMLTagName.ASSIGNMENT + "_TO")
	private String assignmentTo = null;
	@XmlElement(name = StandardXMLTagName.REFERENCE3 + "_FROM")
	private String reference3From = null;
	@XmlElement(name = StandardXMLTagName.REFERENCE3 + "_TO")
	private String reference3To = null;
	
	@XmlElement(name = StandardXMLTagName.OPTION)
	private Integer option = null;

	
}
