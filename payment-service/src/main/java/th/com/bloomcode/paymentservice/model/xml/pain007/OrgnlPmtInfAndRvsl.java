package th.com.bloomcode.paymentservice.model.xml.pain007;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class OrgnlPmtInfAndRvsl {
	@XmlElement(name = "OrgnlPmtInfId")
	public String orgnlPmtInfId;
	@XmlElement(name = "TxInf")
	public List<TxInf> txInf;
}