package th.com.bloomcode.paymentservice.model.xml.pain001;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class GrpHdr {
    @XmlElement(name = "MsgId")
    private String msgID;
    @XmlElement(name = "CreDtTm")
    private String creDtTm;
    @XmlElement(name = "NbOfTxs")
    private String nbOfTxs;
    @XmlElement(name = "CtrlSum")
    private String ctrlSum;
    @XmlElement(name = "InitgPty")
    private InitgPty initgPty;
}
