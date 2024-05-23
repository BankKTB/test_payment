package th.com.bloomcode.paymentservice.model.xml.pain007;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PmtTpInf {
	@XmlElement(name = "SvcLvl")
	public SvcLvl svcLvl;
	@XmlElement(name = "CtgyPurp")
	public CtgyPurp ctgyPurp;
}