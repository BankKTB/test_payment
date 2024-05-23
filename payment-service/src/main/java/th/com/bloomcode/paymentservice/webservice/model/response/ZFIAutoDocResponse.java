package th.com.bloomcode.paymentservice.webservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.DateTimeAdapter;
import th.com.bloomcode.paymentservice.webservice.model.BaseMessage;
import th.com.bloomcode.paymentservice.webservice.model.StandardXMLTagName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@XmlRootElement(name = StandardXMLTagName.RESPONSE)
@XmlAccessorType(XmlAccessType.FIELD)
public class ZFIAutoDocResponse extends BaseMessage {
    @XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
    private String docType = null;
    @XmlElement(name = StandardXMLTagName.COMPANY_CODE)
    private String compCode = null;
    @XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
    private String accDocNo = null;
    @XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
    private String fiscalYear = null;
    @XmlElement(name = StandardXMLTagName.DOCUMENT_STATUS)
    private String docStatus = null;
    @XmlElement(name = StandardXMLTagName.AMOUNT)
    private BigDecimal amount = null;
    @XmlElement(name = StandardXMLTagName.PAYMENT_BLOCK)
    private String paymentBlock = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_DOCUMENT_TYPE)
    private String revDocType = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_ACCOUNT_DOCUMENT_NO)
    private String revAccDocNo = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_FISCAL_YEAR)
    private String revFiscalYear = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_DATE_ACCT)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Timestamp revDatePost = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_REASON)
    private String revReason = null;
}
