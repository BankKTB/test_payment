package th.com.bloomcode.paymentservice.model.xml.pain007;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class InstdAmt {
	@XmlElement(name = "Ccy")
	public String ccy;
	@XmlElement(name = "Text")
	public BigDecimal text;
}