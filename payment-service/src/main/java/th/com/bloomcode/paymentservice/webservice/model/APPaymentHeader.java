package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class APPaymentHeader {

    @XmlElement(name = StandardXMLTagName.PAYMENT_DATE)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    private Timestamp pmDate = null;
    @XmlElement(name = StandardXMLTagName.PAYMENT_IDEN)
    private String pmIden = null;
    @XmlElement(name = StandardXMLTagName.PAYMENT_COMPANY_CODE)
    private String pmCompCode = null;
    @XmlElement(name = StandardXMLTagName.PAYMENT_GROUP_NO)
    private String pmGroupNo = null;
    @XmlElement(name = StandardXMLTagName.PAYMENT_GROUP_DOC)
    private String pmGroupDoc = null;
    @XmlElement(name = StandardXMLTagName.VENDOR)
    private String vendor = null;
    @XmlElement(name = StandardXMLTagName.PAYEE)
    private String payee = null;
    @XmlElement(name = StandardXMLTagName.VENDOR_TAX_ID)
    private String vendorTaxID = null;
    @XmlElement(name = StandardXMLTagName.BANK_ACC_NO)
    private String bankAccNo = null;
    @XmlElement(name = StandardXMLTagName.BANK_BRANCH_NO)
    private String branchNo = null;
    @XmlElement(name = StandardXMLTagName.GL_ACCOUNT)
    private String glAccount = null;
    @XmlElement(name = StandardXMLTagName.AMOUNT)
    private BigDecimal amount = null;
    @XmlElement(name = StandardXMLTagName.RECEIPT_TAXID)
    private String receiptTaxID = null;
    @XmlElement(name = StandardXMLTagName.COUNT_LINE)
    private int countLine = 0;
    @XmlElement(name = StandardXMLTagName.DOCUMENT_TYPE)
    private String docType = null;
    @XmlElement(name = StandardXMLTagName.DATE_DOC)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    private Timestamp dateDoc = null;
    @XmlElement(name = StandardXMLTagName.DATE_ACCT)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    private Timestamp dateAcct = null;


}
