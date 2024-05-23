package th.com.bloomcode.paymentservice.model.xml.pain001;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class TrAcct {
    @XmlElement(name = "Id")
    private DbtrAcctID id;
    @XmlElement(name = "Ccy")
    private String ccy;
}