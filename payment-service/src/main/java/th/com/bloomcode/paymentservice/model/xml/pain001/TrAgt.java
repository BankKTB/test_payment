package th.com.bloomcode.paymentservice.model.xml.pain001;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class TrAgt {
    @XmlElement(name = "FinInstnId")
    private FinInstnID finInstnID;
    @XmlElement(name = "BrnchId")
    private BrnchID brnchID;
}
