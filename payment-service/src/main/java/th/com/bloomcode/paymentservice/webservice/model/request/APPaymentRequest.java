package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentHeader;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentLine;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class APPaymentRequest {

    @XmlElement(name = StandardXMLTagName.HEADER)
    protected APPaymentHeader header;

    @XmlElementWrapper(name = StandardXMLTagName.ITEM_LIST)
    @XmlElement(name = StandardXMLTagName.ITEM_ENTRY)
    protected List<APPaymentLine> lines = new ArrayList<>();

    @XmlElement(name = StandardXMLTagName.WEB_INFO)
    private WSWebInfo webInfo;
}
