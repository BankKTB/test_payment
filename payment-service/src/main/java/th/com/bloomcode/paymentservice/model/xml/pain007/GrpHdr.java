package th.com.bloomcode.paymentservice.model.xml.pain007;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class GrpHdr {
	@XmlElement(name = "MsgId")
	public String msgId;
	@XmlElement(name = "CreDtTm")
	public Date creDtTm;
	@XmlElement(name = "NbOfTxs")
	public int nbOfTxs;
	@XmlElement(name = "CtrlSum")
	public double ctrlSum;
}