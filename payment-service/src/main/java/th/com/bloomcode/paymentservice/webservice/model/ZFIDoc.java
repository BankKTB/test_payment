package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class ZFIDoc {

    /**
     * post = doing post doc, park = doing park doc
     */
    @XmlTransient
    public static final String DOCACTION_PARK = "V";
    @XmlTransient
    public static final String DOCACTION_POST = "P";
    @XmlElement(name = "PARK_POST")
    private String parkPost = ""; // V=Park, P=Post

    @XmlElement(name = StandardXMLTagName.FLAG)
    private int flag = 0; // 0=Test, 1=Do

    @XmlElement(name = StandardXMLTagName.FORM_ID)
    private String formID = "";

    /* true = this doc is autoGen (use in line budget) */
    @XmlElement(name = StandardXMLTagName.AUTO_DOCUMENT_ENTRY)
    private boolean autoDoc = false;

    @XmlElement(name = StandardXMLTagName.HEADER)
    private ZFIHeader header;

    @XmlElementWrapper(name = StandardXMLTagName.ITEM_LIST)
    @XmlElement(name = StandardXMLTagName.ITEM_ENTRY)
    private List<ZFILine> lines = new ArrayList<>();

    @XmlElement(name = StandardXMLTagName.WEB_INFO)
    private WSWebInfo webInfo;

    @XmlElement(name = StandardXMLTagName.FROMDOC_ENTRY)
    private ZFIFromDoc fromDoc;

    @XmlElementWrapper(name = StandardXMLTagName.EXITS_COMPLETES_LIST)
    @XmlElement(name = StandardXMLTagName.EXITS_COMPLETE_ENTRY)
    private List<String> exitsComplete = new ArrayList<>();

    @XmlTransient
    private BaseMessage errLines = new BaseMessage();

    public void addExitsComplete(String value) {
        if (this.exitsComplete.contains(value))
            return;
        this.exitsComplete.add(value);
    }

    public WSWebInfo getWebInfo() {
        return webInfo;
    }

    public void setWebInfo(WSWebInfo webInfo) {
        WSWebInfo obj = new WSWebInfo();
        obj.setIpAddress(webInfo.getIpAddress());
        obj.setFiArea(webInfo.getFiArea());
        obj.setCompCode(webInfo.getCompCode());
        obj.setUserWebOnline(webInfo.getUserWebOnline());
        obj.setPaymentCenter(webInfo.getPaymentCenter());
        this.webInfo = obj;
    }

}
