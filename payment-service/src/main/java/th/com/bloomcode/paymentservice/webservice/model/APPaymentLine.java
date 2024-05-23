package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class APPaymentLine {

    @XmlElement(name = StandardXMLTagName.INVOICE_COMPANY_CODE)
    private String invCompCode = null;
    @XmlElement(name = StandardXMLTagName.INVOICE_DOC_NO)
    private String invDocNo = null;
    @XmlElement(name = StandardXMLTagName.INVOICE_FISCAL_YEAR)
    private String invFiscalYear = null;
    @XmlElement(name = StandardXMLTagName.INVOICE_LINE)
    private int invLine = 0;
    @XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
    private String docType = null;
    @XmlElement(name = StandardXMLTagName.DATE_ACCT)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    private Timestamp dateAcct = null;
    @XmlElement(name = StandardXMLTagName.DATE_DOC)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    private Timestamp dateDoc = null;
    @XmlElement(name = StandardXMLTagName.VENDOR)
    private String vendor = null;
    @XmlElement(name = StandardXMLTagName.PAYEE)
    private String payee = null;
    @XmlElement(name = StandardXMLTagName.REF_DOCUMENT_NO)
    private String refDocNo = null;
    @XmlElement(name = StandardXMLTagName.REF_FISCAL_YEAR)
    private String refFiscalYear = null;
    @XmlElement(name = StandardXMLTagName.REF_LINE)
    private int refLine = 0;
    @XmlElement(name = StandardXMLTagName.HEADER_REFERENCE)
    private int hdReference = 0;
    @XmlElement(name = StandardXMLTagName.DR_CR)
    private String drCr = null;
    @XmlElement(name = StandardXMLTagName.AMOUNT)
    private BigDecimal amount = null;
    @XmlElement(name = StandardXMLTagName.WITHHOLDING_TAX_AMOUNT)
    private BigDecimal wtxAmount = null;

    @XmlTransient
    private ZFIDoc invZFIDoc = null;
}
