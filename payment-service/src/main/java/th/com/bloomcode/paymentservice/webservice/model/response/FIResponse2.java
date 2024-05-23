package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.FICreateResponseBase;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class FIResponse2 extends FICreateResponseBase {

    @XmlElement(name = StandardXMLTagName.COMPANY_CODE_2)
    private String compCode2 = "";
    @XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO_2)
    private String accDocNo2 = "";

    @XmlElementWrapper(name = StandardXMLTagName.AUTO_DOCUMENT_LIST)
    @XmlElement(name = StandardXMLTagName.AUTO_DOCUMENT_ENTRY)
    private List<FICreateResponseBase> autoDoc = new ArrayList<>();
}
