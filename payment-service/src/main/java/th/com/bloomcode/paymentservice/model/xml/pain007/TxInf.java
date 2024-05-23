package th.com.bloomcode.paymentservice.model.xml.pain007;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class TxInf {
	@XmlElement(name = "OrgnlEndToEndId")
	public String orgnlEndToEndId;
	@XmlElement(name = "OrgnlInstdAmt")
	public OrgnlInstdAmt orgnlInstdAmt;
	@XmlElement(name = "RvsdInstdAmt")
	public RvsdInstdAmt rvsdInstdAmt;
	@XmlElement(name = "RvslRsnInf")
	public RvslRsnInf rvslRsnInf;
	@XmlElement(name = "OrgnlTxRef")
	public OrgnlTxRef orgnlTxRef;
}