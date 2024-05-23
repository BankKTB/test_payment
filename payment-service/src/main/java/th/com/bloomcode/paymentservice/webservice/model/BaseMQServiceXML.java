package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseMQServiceXML {
    @XmlElement(name = "ID")
    private String id = null;

    @XmlElement(name = "TYPE")
    private String type = null;

    @XmlElement(name = "FROM")
    private String from = null;

    @XmlElement(name = "TO")
    private String to = null;
}
