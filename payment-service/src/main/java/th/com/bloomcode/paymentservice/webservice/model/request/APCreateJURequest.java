package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSRequestBaseXML;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class APCreateJURequest extends WSRequestBaseXML {

	@XmlElement(name = StandardXMLTagName.TRANSFER_DATE)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp transferDate = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_DATE)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp pmDate = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_IDEN)
	private String pmIden = null;

	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = null;
	@XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
	private String docType = null;

	@XmlElement(name = StandardXMLTagName.GL_ACCOUNT + "_CR")
	private String glAccountCR = null;
	@XmlElement(name = StandardXMLTagName.POSTING_KEY + "_DR")
	private String postingKeyDR = null;
	@XmlElement(name = StandardXMLTagName.POSTING_KEY + "_CR")
	private String postingKeyCR = null;
	@XmlElement(name = StandardXMLTagName.SOURCE_OF_FUND)
	private String fundSource = "";
	@XmlElement(name = StandardXMLTagName.COST_CENTER)
	private String costCenter = "";
	@XmlElement(name = StandardXMLTagName.BUDGET_CODE)
	private String bgCode = null;
	@XmlElement(name = StandardXMLTagName.FI_AREA)
	private String fiArea = null;
	@XmlElement(name = StandardXMLTagName.BUDGET_ACTIVITY)
	private String bgActivity = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_CENTER)
	private String paymentCenter = null;

	@XmlElementWrapper(name = StandardXMLTagName.ITEM_LIST)
	@XmlElement(name = StandardXMLTagName.ITEM_ENTRY)
	protected List<APCreateJULine> lines = new ArrayList<>();
	

	
}
