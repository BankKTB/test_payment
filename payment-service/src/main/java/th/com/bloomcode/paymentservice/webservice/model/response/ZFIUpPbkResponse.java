package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.BaseMessage;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class ZFIUpPbkResponse extends BaseMessage {
    @XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
    private String docType = null;
    @XmlElement(name = StandardXMLTagName.COMPANY_CODE)
    private String compCode = null;
    @XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
    private String accountDocNo = null;
    @XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
    private String fiscalYear = null;
    @XmlElement(name = StandardXMLTagName.APPROVAL)
    private boolean approval = false;
    @XmlElement(name = StandardXMLTagName.PAYMENT_BLOCK)
    private String paymentBlock = null;
    @XmlElement(name = StandardXMLTagName.REASON)
    private String reason = null;
    @XmlElement(name = StandardXMLTagName.USER_WEBONLINE)
    private String userWebOnline = null;
    @XmlElement(name = StandardXMLTagName.DOCUMENT_STATUS)
    private String docStatus = null;

    /* response */
    @XmlElement(name = StandardXMLTagName.SUCCESS)
    private boolean success = false;
    @XmlElement(name = StandardXMLTagName.REVERSAL_ACCOUNT_DOCUMENT_NO)
    private String revAccountDocNo = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_FISCAL_YEAR)
    private String revFiscalYear = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_DOCUMENT_TYPE)
    private String revDocType = null;
}
