package th.com.bloomcode.paymentservice.webservice.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSRequestBaseXML;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;


@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class APUpdateLineVendorRequest extends WSRequestBaseXML {
	
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp dateBaseLine = null;




}
