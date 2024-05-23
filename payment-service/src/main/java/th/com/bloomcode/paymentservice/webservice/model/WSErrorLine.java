package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class WSErrorLine {

	public WSErrorLine() {
	}

	@XmlElement(name = "TYPE")
	private String type = "E";
	@XmlElement(name = "CODE")
	private String code = null;
	@XmlElement(name = "LINE")
	private int line = 0;
	@XmlElement(name = "TEXT")
	private String text = null;

}
