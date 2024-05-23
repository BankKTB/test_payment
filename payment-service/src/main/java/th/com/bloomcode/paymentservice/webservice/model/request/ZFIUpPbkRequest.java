//package th.com.bloomcode.paymentservice.webservice.model.request;
//
//import lombok.Data;
//import th.com.bloomcode.paymentservice.model.request.UnBlockDocumentLogRequest;
//import th.com.bloomcode.paymentservice.model.util.IWSStandardTagName;
//
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//
//@Data
//@XmlRootElement(name = IWSStandardTagName.Request)
//@XmlAccessorType(XmlAccessType.FIELD)
//public class ZFIUpPbkRequest {
//
//    @XmlElement(name = IWSStandardTagName.DocType)
//    private String docType;
//
//    @XmlElement(name = IWSStandardTagName.CompanyCode)
//    private String compCode;
//
//    @XmlElement(name = IWSStandardTagName.AccountDocNo)
//    private String accDocNo;
//
//    @XmlElement(name = IWSStandardTagName.FiscYear)
//    private String fiscYear;
//
//    @XmlElement(name = IWSStandardTagName.Approve)
//    private boolean approve;
//
//    @XmlElement(name = IWSStandardTagName.Reason)
//    private String reason;
//
//    @XmlElement(name = IWSStandardTagName.UserWebOnline)
//    private String userWeb;
//
//    public ZFIUpPbkRequest() {
//    }
//
//    public ZFIUpPbkRequest(UnBlockDocumentLogRequest request) {
////        this.docType = request.getDocType();
////        this.compCode = request.getCompCode();
////        this.accDocNo = request.getAccDocNo();
////        this.fiscYear = request.getFiscalYear();
////        this.approve = request.isApprove();
////        this.reason = request.getReason();
////        this.userWeb = request.getUserWeb();
//    }
//}
