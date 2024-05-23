package th.com.bloomcode.paymentservice.model.xml.pain007;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class CdtrAgt {
	@XmlElement(name = "FinInstnId")
	public FinInstnId finInstnId;
	@XmlElement(name = "BrnchId")
	public BrnchId brnchId;
}