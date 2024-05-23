package th.com.bloomcode.paymentservice.webservice.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.DateTimeAdapter;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class FICPbkChangeLog {
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = null;
	@XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
	private String accountDocNo = null;
	@XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
	private String fiscalYear = null;
	@XmlElement(name = StandardXMLTagName.LINE)
	private int line = 0;
	@XmlElement(name = StandardXMLTagName.VALUE_OLD)
	private String valueOld = null;
	@XmlElement(name = StandardXMLTagName.VALUE_NEW)
	private String valueNew = null;
	@XmlElement(name = StandardXMLTagName.USER_WEBONLINE)
	private String userWebOnline = null;
	@XmlElement(name = StandardXMLTagName.CREATED)
	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
	private Timestamp created = null;
}
