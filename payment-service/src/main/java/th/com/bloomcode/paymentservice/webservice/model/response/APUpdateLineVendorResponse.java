package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.DateTimeAdapter;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSResponseBaseXML;
import th.com.bloomcode.paymentservice.webservice.model.ZFIDoc;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class APUpdateLineVendorResponse extends WSResponseBaseXML {
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = null;
	@XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
	private String accountDocNo = null;
	@XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
	private String fiscalYear = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_BLOCK)
	private String paymentBlock = null;
	@XmlElement(name = StandardXMLTagName.ASSIGNMENT)
	private String assignment = null;
	@XmlElement(name = StandardXMLTagName.LINE_DESC)
	private String lineDesc = null;
	@XmlElement(name = StandardXMLTagName.REFERENCE1)
	private String reference1 = null;
	@XmlElement(name = StandardXMLTagName.BANK_ACC_NO)
	private String bankAccNo = null;
	@XmlElement(name = StandardXMLTagName.BANK_BRANCH_NO)
	private String bankBranchNo = null;

	@XmlElement(name = StandardXMLTagName.DATE_BASELINE)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp dateBaseLine = null;

	@XmlElement(name = StandardXMLTagName.CREATED)
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	private Timestamp created = null;

	@XmlElement(name = StandardXMLTagName.FIDOC)
	private ZFIDoc fiDoc = null;
}
