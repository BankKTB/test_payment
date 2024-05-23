package th.com.bloomcode.paymentservice.model.xml.pain007;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class OrgnlGrpInf {
	@XmlElement(name = "OrgnlMsgId")
	public String orgnlMsgId;
	@XmlElement(name = "OrgnlMsgNmId")
	public String orgnlMsgNmId;
	@XmlElement(name = "OrgnlCreDtTm")
	public Date orgnlCreDtTm;
}