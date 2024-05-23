package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.DateTimeAdapter;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ZFIHeader {

    @XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
    private String docType = null;
    @XmlElement(name = StandardXMLTagName.COMPANY_CODE)
    private String compCode = null;

    @XmlElement(name = StandardXMLTagName.DATE_DOC)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    private Timestamp dateDoc = null;

    @XmlElement(name = StandardXMLTagName.DATE_ACCT)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    private Timestamp dateAcct = null;
    @XmlElement(name = StandardXMLTagName.PERIOD)
    private int period = 0;
    @XmlElement(name = StandardXMLTagName.CURRENCY)
    private String currency = null;
    @XmlElement(name = StandardXMLTagName.AMOUNT)
    private BigDecimal amount = null;
    @XmlElement(name = StandardXMLTagName.PAYMENT_CENTER)
    private String paymentCenter = null;
    @XmlElement(name = StandardXMLTagName.BR_DOCUMENT_NO)
    private String brDocNo = null;
    @XmlElement(name = StandardXMLTagName.PO_DOC_NO)
    private String poDocNo = null;
    @XmlElement(name = StandardXMLTagName.INVOICE_DOC_NO)
    private String invDocNo = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_INVOICE_DOC_NO)
    private String revInvDocNo = null;
    @XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
    private String accDocNo = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_ACCOUNT_DOCUMENT_NO)
    private String revAccDocNo = null;
    @XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
    private String fiscalYear = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_FISCAL_YEAR)
    private String revFiscalYear = null;
    @XmlElement(name = StandardXMLTagName.PAYMENT_METHOD)
    private String paymentMethod = null;
    @XmlElement(name = StandardXMLTagName.COST_CENTER1)
    private String costCenter1 = null;
    @XmlElement(name = StandardXMLTagName.COST_CENTER2)
    private String costCenter2 = null;
    @XmlElement(name = StandardXMLTagName.HEADER_REFERENCE)
    private String headerReference = null;
    @XmlElement(name = StandardXMLTagName.DOC_HEADER_TEXT)
    private String docHeaderText = null;
    @XmlElement(name = StandardXMLTagName.HEADER_REFERENCE2)
    private String headerReference2 = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_DATE_ACCT)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    private Timestamp revDateAcct = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_REASON)
    private String revReason = null;
    @XmlElement(name = StandardXMLTagName.CREATED)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Timestamp created = null;
    @XmlElement(name = StandardXMLTagName.USERPARK)
    private String userPark = null;
    @XmlElement(name = StandardXMLTagName.USERPOST)
    private String userPost = null;

    @XmlElement(name = StandardXMLTagName.ORIGINAL_DOC)
    private String originalDoc = null;
    @XmlElement(name = StandardXMLTagName.REF_INTER_DOC_NO)
    private String refInterDocNo = null;
    @XmlElement(name = StandardXMLTagName.REF_INTER_COMP_CODE)
    private String refInterCompCode = null;
    @XmlElement(name = StandardXMLTagName.REF_AUTOGEN)
    private String refAutoGen = null;

    @XmlElement(name = StandardXMLTagName.DOCUMENT_STATUS)
    private String docStatus = null;
    @XmlElement(name = StandardXMLTagName.RP_APPROVE)
    private String rpApproved = null;
    @XmlElement(name = StandardXMLTagName.PAYMENT_GROUP_NO)
    private String pmGroupNo = null;
    @XmlElement(name = StandardXMLTagName.PAYMENT_GROUP_DOC)
    private String pmGroupDoc = null;

    @XmlElement(name = StandardXMLTagName.DOC_BASE_TYPE)
    private String docBaseType = null;
}
