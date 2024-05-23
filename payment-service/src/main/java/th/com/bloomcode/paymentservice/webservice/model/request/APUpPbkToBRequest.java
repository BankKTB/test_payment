package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.DateTimeAdapter;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;


@Data
@XmlRootElement(name = StandardXMLTagName.REQUEST)
@XmlAccessorType(XmlAccessType.FIELD)
public class APUpPbkToBRequest {

    @XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
    private String docType = null;
    @XmlElement(name = StandardXMLTagName.COMPANY_CODE)
    private String compCode = null;
    @XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
    private String accountDocNo = null;
    @XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
    private String fiscalYear = null;
    @XmlElement(name = StandardXMLTagName.CREATED)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Timestamp created = null;

    @XmlElement(name = StandardXMLTagName.WEB_INFO)
    private WSWebInfo webInfo;

    public APUpPbkToBRequest() {
    }

    public APUpPbkToBRequest(GLHead request, WSWebInfo webInfo) {
        this.docType = request.getDocumentType();
        this.compCode = request.getCompanyCode();
        this.accountDocNo = request.getOriginalDocumentNo();
        this.fiscalYear = request.getOriginalFiscalYear();

        this.webInfo = webInfo;
        this.created = new Timestamp(System.currentTimeMillis());


    }

}
