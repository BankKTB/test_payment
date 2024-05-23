package th.com.bloomcode.paymentservice.webservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Holiday {
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	@XmlElement(name = StandardXMLTagName.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
	private Timestamp date = null;
	
	@XmlElement(name = StandardXMLTagName.DESCRIPTION)
	private String description = null;
	
	@XmlElement(name = StandardXMLTagName.ACTIVE_STATUS)
	private boolean active = true;
	
	@XmlElement(name = StandardXMLTagName.ERROR_ENTRY)
	private String error = null;
	
	@XmlElement(name = StandardXMLTagName.RECORD_ID)
	private int recordID = 0;
}
