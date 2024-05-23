package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.adapter.DateTimeAdapter;
import th.com.bloomcode.paymentservice.webservice.adapter.SimpleDateAdapter;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class FIHeader2 {
	@XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
	private String docType = null;
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = null;
	@XmlElement(name = StandardXMLTagName.USER_WEBONLINE)
	private String userWebOnline = null;
	
	@XmlElement(name = StandardXMLTagName.DATE_DOC)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp dateDoc = null;
	
	@XmlElement(name = StandardXMLTagName.DATE_ACCT)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp dateAcct = null;
	@XmlElement(name = StandardXMLTagName.HEADER_REFERENCE)
	private String headerReference = null;
	@XmlElement(name = StandardXMLTagName.DOC_HEADER_TEXT)
	private String docHeaderText = null;
	@XmlElement(name = StandardXMLTagName.CURRENCY)
	private String currency = null;
	
	@XmlElement(name = StandardXMLTagName.REVERSAL_DATE_ACCT)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp revDateAcct = null;
	@XmlElement(name = StandardXMLTagName.REVERSAL_REASON)
	private String revReason = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_CENTER)
	private String paymentCenter = null;
	@XmlElement(name = StandardXMLTagName.AMOUNT)
	private BigDecimal amount = null;
	@XmlElement(name = StandardXMLTagName.PO_DOCUMENT_NO)
	private String poDocNo = null;
	@XmlElement(name = StandardXMLTagName.INVOICE_DOC_NO)
	private String invDocNo = null;
	@XmlElement(name = StandardXMLTagName.REVERSAL_INVOICE_DOC_NO)
	private String revInvDocNo = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_METHOD)
	private String paymentMethod = null;
	@XmlElement(name = StandardXMLTagName.COST_CENTER1)
	private String costCenter1 = null;
	@XmlElement(name = StandardXMLTagName.COST_CENTER2)
	private String costCenter2 = null;
	@XmlElement(name = StandardXMLTagName.BR_DOCUMENT_NO)
	private String brDocNo = null;
	@XmlElement(name = StandardXMLTagName.PERIOD)
	private int period = 0;
	@XmlElement(name = StandardXMLTagName.PAYEE)
	private String payee = null;
	@XmlElement(name = StandardXMLTagName.PAYEE_TAXID)
	private String payeeTaxID = null;
	@XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
	private String accDocNo = null;
	@XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
	private String fiscalYear = null;
	// return only
	@XmlElement(name = StandardXMLTagName.REVERSAL_ACCOUNT_DOCUMENT_NO)
	private String revAccDocNo = null;
	@XmlElement(name = StandardXMLTagName.REVERSAL_FISCAL_YEAR)
	private String revFiscalYear = null;
	@XmlElement(name = StandardXMLTagName.REVERSAL_DOCUMENT_TYPE)
	private String revDocType = null;
	@XmlElement(name = StandardXMLTagName.DOCUMENT_STATUS)
	private String docStatus = null;
	@XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE_NAME)
	private String docTypeName = null;
	@XmlElement(name = StandardXMLTagName.PAYEE_NAME)
	private String payeeName = null;
	@XmlElement(name = StandardXMLTagName.DOCUMENT_STATUS_NAME)
	private String docStatusName = null;
	@XmlElement(name = StandardXMLTagName.FI_AREA)
	private String fiArea = null;
	@XmlElement(name = StandardXMLTagName.FI_AREA_NAME)
	private String fiAreaName = null;
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE_NAME)
	private String compCodeName = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_CENTER_NAME)
	private String paymentCenterName = null;
	@XmlElement(name = StandardXMLTagName.COST_CENTER1_NAME)
	private String costCenter1Name = null;
	@XmlElement(name = StandardXMLTagName.COST_CENTER2_NAME)
	private String costCenter2Name = null;
	@XmlElement(name = StandardXMLTagName.REVERSAL_MARK)
	private String reversalMark = null;
	
	@XmlElement(name = StandardXMLTagName.SAVE_DATE_DOC)
	@XmlJavaTypeAdapter(SimpleDateAdapter.class)
	private Timestamp saveDateDoc = null;
	
	@XmlElement(name = StandardXMLTagName.CREATED)
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	private Timestamp created = null;
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@XmlElement(name = StandardXMLTagName.UPDATED)
	private Timestamp updated = null;
	
	@XmlElement(name = StandardXMLTagName.SAP_DOCUMENT_NO)
	private String sapDocNo = null;
	
	@XmlElement(name = StandardXMLTagName.DOC_BASE_TYPE)
	private String docBaseType = null;
	
	@XmlElement(name = StandardXMLTagName.ORIGINAL_DOC)
	private String originalDoc = null;
	@XmlElement(name = StandardXMLTagName.REF_INTER_DOC_NO)
	private String refInterDocNo = null;
	@XmlElement(name = StandardXMLTagName.REF_INTER_COMP_CODE)
	private String refInterCompCode = null;
	@XmlElement(name = StandardXMLTagName.REF_AUTOGEN)
	private String refAutoGen = null;
	
	@XmlElement(name = StandardXMLTagName.K3_DOCUMENT_NO)
	private String k3DocNo = null;
	@XmlElement(name = StandardXMLTagName.K3_FISCAL_YEAR)
	private String k3FiscalYear = null;
	
	@XmlElement(name = StandardXMLTagName.USERPARK)
	private String userPark = null;
	@XmlElement(name = StandardXMLTagName.USERPOST)
	private String userPost = null;
	@XmlElement(name = StandardXMLTagName.USERVOID)
	private String userVoid = null;
	

	
}
