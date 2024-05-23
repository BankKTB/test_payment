package th.com.bloomcode.paymentservice.model.xml.pain001;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PmtInf {
    @XmlElement(name = "PmtInfId")
    private String pmtInfID;
    @XmlElement(name = "PmtMtd")
    private String pmtMtd;
    @XmlElement(name = "NbOfTxs")
    private String nbOfTxs;
    @XmlElement(name = "CtrlSum")
    private String ctrlSum;
    @XmlElement(name = "ReqdExctnDt")
    private ReqdExctnDt reqdExctnDt;
    @XmlElement(name = "Dbtr")
    private InitgPty dbtr;
    @XmlElement(name = "DbtrAcct")
    private TrAcct dbtrAcct;
    @XmlElement(name = "DbtrAgt")
    private TrAgt dbtrAgt;
    @XmlElement(name = "CdtTrfTxInf")
    private List<CdtTrfTxInf> cdtTrfTxInfs;
}
