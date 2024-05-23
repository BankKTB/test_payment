package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class FICreateResponseBase extends WSResponseBaseXML {

	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = null;
	@XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
	private String accDocNo = null;
	@XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
	private String fiscalYear = null;
	@XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
	private String docType = null;
	@XmlElement(name = StandardXMLTagName.DOCUMENT_STATUS)
	private String docStatus = null;
	@XmlElement(name = StandardXMLTagName.DOCUMENT_STATUS_NAME)
	private String docStatusName = null;
	@XmlElement(name = StandardXMLTagName.REVERSAL_ACCOUNT_DOCUMENT_NO)
	private String revAccDocNo = null;
	@XmlElement(name = StandardXMLTagName.REVERSAL_FISCAL_YEAR)
	private String revFiscalYear = null;
	@XmlElement(name = StandardXMLTagName.REVERSAL_DOCUMENT_TYPE)
	private String revDocType = null;
	@XmlElement(name = StandardXMLTagName.REVERSAL_DATE_ACCT)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp revDateAcct = null;
	@XmlElement(name = StandardXMLTagName.REMARK)
	private String remark = null;
	@XmlElement(name = StandardXMLTagName.STATUS)
	private String status = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_BLOCK)
	private String paymentBlock = null;
	@XmlElement(name = StandardXMLTagName.DOC_OIA_TYPE)
	private String docOIAType = null;
	@XmlElement(name = StandardXMLTagName.DATE_DOC)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp dateDoc = null;
	@XmlElement(name = StandardXMLTagName.DATE_ACCT)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp dateAcct = null;

	public enum Status {

		SUCCESS("S"), ERROR("E"), NEED_REPAIR("N"), PROCESSING("P");

		private final String code;

		Status(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	public enum DocOIAType {

		ORIGINAL("O"), INTER_COMP("I"), AUTO_GEN("A");

		private final String code;

		DocOIAType(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}



}