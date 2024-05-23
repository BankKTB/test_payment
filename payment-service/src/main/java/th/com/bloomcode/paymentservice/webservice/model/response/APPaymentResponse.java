package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.FICreateResponseBase;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.ZFIDoc;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class APPaymentResponse extends FICreateResponseBase {

    @XmlElement(name = StandardXMLTagName.FLAG)
    private int flag = 0; // 0=Test, 1=Do

    @XmlElement(name = StandardXMLTagName.PAYMENT_GROUP_NO)
    private String pmGroupNo = null;

    @XmlElement(name = StandardXMLTagName.PAYMENT_GROUP_DOC)
    private String pmGroupDoc = null;

    @XmlElement(name = StandardXMLTagName.FIDOC)
    private ZFIDoc fiDoc;

    @XmlElementWrapper(name = StandardXMLTagName.AUTO_DOCUMENT_LIST)
    @XmlElement(name = StandardXMLTagName.AUTO_DOCUMENT_ENTRY)
    private List<FICreateResponseBase> autoDoc = new ArrayList<>();


}