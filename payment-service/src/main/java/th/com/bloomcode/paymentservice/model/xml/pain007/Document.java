package th.com.bloomcode.paymentservice.model.xml.pain007;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {

	@XmlElement(name = "CstmrPmtRvsl")
	public CstmrPmtRvsl cstmrPmtRvsl;
}