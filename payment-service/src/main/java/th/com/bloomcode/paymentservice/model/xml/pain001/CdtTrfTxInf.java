package th.com.bloomcode.paymentservice.model.xml.pain001;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class CdtTrfTxInf {
    @XmlElement(name = "PmtId")
    private PmtID pmtID;
    @XmlElement(name = "PmtTpInf")
    private PmtTpInf pmtTpInf;
    @XmlElement(name = "Amt")
    private Amt amt;
    @XmlElement(name = "ChrgBr")
    private String chrgBr;
    @XmlElement(name = "CdtrAgt")
    private TrAgt cdtrAgt;
    @XmlElement(name = "Cdtr")
    private Cdtr cdtr;
    @XmlElement(name = "CdtrAcct")
    private TrAcct cdtrAcct;
    @XmlElement(name = "InstrForDbtrAgt")
    private String instrForDbtrAgt;
}
