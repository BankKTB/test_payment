package th.com.bloomcode.paymentservice.model.xml.pain007;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class RvslRsnInf {
	@XmlElement(name = "Rsn")
	public Rsn rsn;
	@XmlElement(name = "AddtlInf")
	public String addtlInf;
}