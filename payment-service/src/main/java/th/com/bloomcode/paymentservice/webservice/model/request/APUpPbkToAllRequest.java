package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.DateTimeAdapter;
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
public class APUpPbkToAllRequest extends WSRequestBaseXML {
	
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = null;
	@XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
	private String accountDocNo = null;
	@XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
	private String fiscalYear = null;
	@XmlElement(name = StandardXMLTagName.CREATED)
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	private Timestamp created = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_BLOCK)
	private String paymentBlock = null;
	

	
}
