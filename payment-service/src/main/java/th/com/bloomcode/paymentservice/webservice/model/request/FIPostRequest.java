package th.com.bloomcode.paymentservice.webservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReport;
import th.com.bloomcode.paymentservice.model.request.UnBlockChangeDocumentRequest;
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
public class FIPostRequest {

    @XmlElement(name = StandardXMLTagName.WEB_INFO)
    private WSWebInfo webInfo;

    @XmlElement(name = StandardXMLTagName.COMPANY_CODE)
    private String compCode = null;
    @XmlElement(name = StandardXMLTagName.ACCOUNT_DOCUMENT_NO)
    private String accountDocNo = null;
    @XmlElement(name = StandardXMLTagName.FISCAL_YEAR)
    private String fiscalYear = null;
    @XmlElement(name = StandardXMLTagName.REASON_NO)
    private String reasonNo = null;
    @XmlElement(name = StandardXMLTagName.REVERSAL_DATE_ACCT)
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    private Timestamp revDateAcct = null;
    @XmlElement(name = StandardXMLTagName.REASON)
    private String reason = null;
    @XmlElement(name = StandardXMLTagName.FLAG)
    private int flag = 0; // 0=Test, 1=Do
    @XmlElement(name = StandardXMLTagName.PERIOD)
    private Integer period = null;

    public FIPostRequest() {

    }


    public FIPostRequest(UnBlockChangeDocumentRequest request) {
//		this.docType = request.getDocType();
        this.compCode = request.getCompanyCode();
        this.accountDocNo = request.getOriginalDocumentNo();
        this.fiscalYear = request.getOriginalFiscalYear();
//		this.approval = request.isApprove();
        this.reason = request.getReason();
        this.webInfo = request.getWebInfo();
//		this.created = new Timestamp(request.getPostDate().getTime());

//		this.flag = request.getReason();
        this.revDateAcct = request.getDateAcct();
//        this.reasonNo = "";

    }


    public FIPostRequest(PaymentReport request) {
//		this.docType = request.getDocType();
        this.compCode = request.getPaymentCompanyCode();
        this.accountDocNo = request.getPaymentDocumentNo();
        this.fiscalYear = request.getPaymentFiscalYear();
//		this.approval = request.isApprove();
//        this.reason = request.getReason();
//        this.webInfo = request.getWebInfo();
//		this.created = new Timestamp(request.getPostDate().getTime());

//		this.flag = request.getReason();
        this.revDateAcct = request.getDateAcct();
//        this.reasonNo = "";

    }
}
