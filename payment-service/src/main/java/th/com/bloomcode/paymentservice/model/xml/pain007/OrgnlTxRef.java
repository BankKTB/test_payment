package th.com.bloomcode.paymentservice.model.xml.pain007;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class OrgnlTxRef {
	@XmlElement(name = "IntrBkSttlmAmt")
	public IntrBkSttlmAmt intrBkSttlmAmt;
	@XmlElement(name = "Amt")
	public Amt amt;
	@XmlElement(name = "IntrBkSttlmDt")
	public Date intrBkSttlmDt;
	@XmlElement(name = "PmtTpInf")
	public PmtTpInf pmtTpInf;
	@XmlElement(name = "Dbtr")
	public Dbtr dbtr;
	@XmlElement(name = "DbtrAcct")
	public DbtrAcct dbtrAcct;
	@XmlElement(name = "DbtrAgt")
	public DbtrAgt dbtrAgt;
	@XmlElement(name = "CdtrAgt")
	public CdtrAgt cdtrAgt;
	@XmlElement(name = "Cdtr")
	public Cdtr cdtr;
	@XmlElement(name = "CdtrAcct")
	public CdtrAcct cdtrAcct;
}