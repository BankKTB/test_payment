package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;
import th.com.bloomcode.paymentservice.webservice.model.ZFIHeader;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
public class GLHead extends BaseModel {

    public static final String TABLE_NAME = "GL_HEAD";

    public static final String COLUMN_NAME_GL_HEAD_ID = "ID";
    public static final String COLUMN_NAME_DOCUMENT_TYPE = "DOCUMENT_TYPE";
    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_COMPANY_NAME = "COMPANY_NAME";
    public static final String COLUMN_NAME_DATE_DOC = "DATE_DOC";
    public static final String COLUMN_NAME_DATE_ACCT = "DATE_ACCT";
    public static final String COLUMN_NAME_PERIOD = "PERIOD";
    public static final String COLUMN_NAME_CURRENCY = "CURRENCY";
    public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
    public static final String COLUMN_NAME_PAYMENT_CENTER = "PAYMENT_CENTER";
    public static final String COLUMN_NAME_BR_DOCUMENT_NO = "BR_DOCUMENT_NO";
    public static final String COLUMN_NAME_PO_DOCUMENT_NO = "PO_DOCUMENT_NO";
    public static final String COLUMN_NAME_INVOICE_DOCUMENT_NO = "INVOICE_DOCUMENT_NO";
    public static final String COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO = "REVERSE_INVOICE_DOCUMENT_NO";
    public static final String COLUMN_NAME_ORIGINAL_DOCUMENT_NO = "ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO = "REVERSE_ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR = "REVERSE_ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_PAYMENT_METHOD = "PAYMENT_METHOD";
    public static final String COLUMN_NAME_COST_CENTER1 = "COST_CENTER1";
    public static final String COLUMN_NAME_COST_CENTER2 = "COST_CENTER2";

    public static final String COLUMN_NAME_HEADER_REFERENCE = "HEADER_REFERENCE";
    public static final String COLUMN_NAME_HEADER_REFERENCE2 = "HEADER_REFERENCE2";
    public static final String COLUMN_NAME_DOCUMENT_HEADER_TEXT = "DOCUMENT_HEADER_TEXT";
    public static final String COLUMN_NAME_REVERSE_DATE_ACCT = "REVERSE_DATE_ACCT";
    public static final String COLUMN_NAME_REVERSE_REASON = "REVERSE_REASON";
    public static final String COLUMN_NAME_DOCUMENT_CREATED = "DOCUMENT_CREATED";
    public static final String COLUMN_NAME_DOCUMENT_CREATED_REAL = "DOCUMENT_CREATED_REAL";
    public static final String COLUMN_NAME_USER_PARK = "USER_PARK";
    public static final String COLUMN_NAME_USER_POST = "USER_POST";
    public static final String COLUMN_NAME_ORIGINAL_DOCUMENT = "ORIGINAL_DOCUMENT";
    public static final String COLUMN_NAME_REFERENCE_INTER_DOCUMENT_NO = "REFERENCE_INTER_DOCUMENT_NO";
    public static final String COLUMN_NAME_REFERENCE_INTER_COMPANY_CODE = "REFERENCE_INTER_COMPANY_CODE";
    public static final String COLUMN_NAME_REFERENCE_AUTO_GEN = "REFERENCE_AUTO_GEN";
    public static final String COLUMN_NAME_DOCUMENT_STATUS = "DOCUMENT_STATUS";
    public static final String COLUMN_NAME_RP_APPROVED = "RP_APPROVED";
    public static final String COLUMN_NAME_DOCUMENT_BASE_TYPE = "DOCUMENT_BASE_TYPE";
    public static final String COLUMN_NAME_PAYMENT_DOCUMENT_NO = "PAYMENT_DOCUMENT_NO";
    public static final String COLUMN_NAME_PAYMENT_ID = "PAYMENT_ID";

    public static final String COLUMN_NAME_SELECT_GROUP_DOCUMENT = "SELECT_GROUP_DOCUMENT";

    private String documentType;
    private String companyCode;
    private String companyName;
    private Timestamp dateDoc;
    private Timestamp dateAcct;
    private int period;
    private String currency;
    private BigDecimal amount;
    private String paymentCenter;
    private String brDocumentNo;
    private String poDocumentNo;
    private String invoiceDocumentNo;
    private String reverseInvoiceDocumentNo;
    private String originalDocumentNo;
    private String originalFiscalYear;
    private String reverseOriginalDocumentNo;
    private String reverseOriginalFiscalYear;
    private String paymentMethod;
    private String costCenter1;
    private String costCenter2;
    private String headerReference;
    private String documentHeaderText;
    private String headerReference2;
    private Timestamp reverseDateAcct;
    private String reverseReason;
    private Timestamp documentCreated;
    private Timestamp documentCreatedReal;
    private String userPark;
    private String userPost;
    private String originalDocument;
    private String referenceInterDocumentNo;
    private String referenceInterCompanyCode;
    private String referenceAutoGen;
    private String documentStatus;
    private String rpApproved;
    private String documentBaseType;
    private String paymentDocumentNo;
    private Long paymentId= Long.parseLong("0");

    private String selectGroupDocument;


//    @Column(name = "PM_GROUP_NO")
//    private String pmGroupNo;
//    @Column(name = "PM_GROUP_DOC")
//    private String pmGroupDoc;
//@OneToMany(mappedBy = "glHead")
//private List<GLLine> glLines;


    public GLHead(ZFIHeader zfiHeader) {
        Timestamp documentCreateForIndependent = new Timestamp(zfiHeader.getCreated().getTime());

        documentCreateForIndependent.setHours(0);
        documentCreateForIndependent.setMinutes(0);
        documentCreateForIndependent.setSeconds(0);

        this.documentType = zfiHeader.getDocType();
        this.companyCode = zfiHeader.getCompCode();
        this.documentCreated = documentCreateForIndependent;
        this.documentCreatedReal = zfiHeader.getCreated();
        this.dateDoc = zfiHeader.getDateDoc();
        this.dateAcct = zfiHeader.getDateAcct();
        this.period = zfiHeader.getPeriod();
        this.currency = zfiHeader.getCurrency();
        this.amount = zfiHeader.getAmount();
        this.paymentCenter = zfiHeader.getPaymentCenter();
        this.brDocumentNo = zfiHeader.getBrDocNo();
        this.poDocumentNo = zfiHeader.getPoDocNo();
        this.invoiceDocumentNo = zfiHeader.getInvDocNo();
        this.reverseInvoiceDocumentNo = zfiHeader.getRevInvDocNo();
        this.originalDocumentNo = zfiHeader.getAccDocNo();
        this.originalFiscalYear = zfiHeader.getFiscalYear();
        this.reverseOriginalDocumentNo = zfiHeader.getRevAccDocNo();
        this.reverseOriginalFiscalYear = zfiHeader.getRevFiscalYear();
        this.paymentMethod = zfiHeader.getPaymentMethod();
        this.costCenter1 = zfiHeader.getCostCenter1();
        this.costCenter2 = zfiHeader.getCostCenter2();
        this.headerReference = zfiHeader.getHeaderReference();
        this.documentHeaderText = zfiHeader.getDocHeaderText();
        this.headerReference2 = zfiHeader.getHeaderReference2();
        this.reverseDateAcct = zfiHeader.getRevDateAcct();
        this.reverseReason = zfiHeader.getRevReason();
        this.userPark = zfiHeader.getUserPark();
        this.userPost = zfiHeader.getUserPost();
        this.originalDocument = zfiHeader.getOriginalDoc();
        this.referenceInterDocumentNo = zfiHeader.getRefInterDocNo();
        this.referenceInterCompanyCode = zfiHeader.getRefInterCompCode();
        this.referenceAutoGen = zfiHeader.getRefAutoGen();
        this.documentStatus = zfiHeader.getDocStatus();
        this.rpApproved = zfiHeader.getRpApproved();
        this.documentBaseType = zfiHeader.getDocBaseType();
    }


    public GLHead(long id, String documentType, String companyCode, String companyName, Timestamp dateDoc, Timestamp dateAcct,
                  int period, String currency, BigDecimal amount, String paymentCenter, String brDocumentNo, String poDocumentNo,
                  String invoiceDocumentNo, String reverseInvoiceDocumentNo, String originalDocumentNo, String originalFiscalYear,
                  String reverseOriginalDocumentNo, String reverseOriginalFiscalYear, String paymentMethod, String costCenter1,
                  String costCenter2, String headerReference, String docHeaderText, String headerReference2,
                  Timestamp reverseDateAcct, String reverseReason, Timestamp documentCreated, String userPark, String userPost, String originalDocument,
                  String referenceInterDocumentNo, String referenceInterCompanyCode, String referenceAutoGen,
                  String documentStatus, String rpApproved, String documentBaseType, String paymentDocumentNo, long paymentId
            , Timestamp created, Timestamp updated, String selectGroupDocument,Timestamp documentCreatedReal,String createdBy, String updateBy) {
        super(id, created, createdBy, updated, updateBy);
        this.documentType = documentType;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.dateDoc = dateDoc;
        this.dateAcct = dateAcct;
        this.period = period;
        this.currency = currency;
        this.amount = amount;
        this.paymentCenter = paymentCenter;
        this.brDocumentNo = brDocumentNo;
        this.poDocumentNo = poDocumentNo;
        this.invoiceDocumentNo = invoiceDocumentNo;
        this.reverseInvoiceDocumentNo = reverseInvoiceDocumentNo;
        this.originalDocumentNo = originalDocumentNo;
        this.originalFiscalYear = originalFiscalYear;
        this.reverseOriginalDocumentNo = reverseOriginalDocumentNo;
        this.reverseOriginalFiscalYear = reverseOriginalFiscalYear;
        this.paymentMethod = paymentMethod;
        this.costCenter1 = costCenter1;
        this.costCenter2 = costCenter2;
        this.headerReference = headerReference;
        this.documentHeaderText = docHeaderText;
        this.headerReference2 = headerReference2;
        this.reverseDateAcct = reverseDateAcct;
        this.reverseReason = reverseReason;
        this.documentCreated = documentCreated;
        this.userPark = userPark;
        this.userPost = userPost;
        this.originalDocument = originalDocument;
        this.referenceInterDocumentNo = referenceInterDocumentNo;
        this.referenceInterCompanyCode = referenceInterCompanyCode;
        this.referenceAutoGen = referenceAutoGen;
        this.documentStatus = documentStatus;
        this.rpApproved = rpApproved;
        this.documentBaseType = documentBaseType;
        this.paymentDocumentNo = paymentDocumentNo;
        this.paymentId = paymentId;
        this.selectGroupDocument = selectGroupDocument;
        this.documentCreatedReal = documentCreatedReal;

    }

    public GLHead(String companyCode, String originalDocumentNo, String originalFiscalYear, String paymentClearingDocNo, Long paymentId) {
        this.companyCode = companyCode;
        this.originalDocumentNo = originalDocumentNo;
        this.originalFiscalYear = originalFiscalYear;
        this.paymentDocumentNo = paymentClearingDocNo;
        this.paymentId = paymentId;
    }

    @Override
    public String toString() {
        return "GLHead{" +
                "documentType='" + documentType + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", dateDoc=" + dateDoc +
                ", dateAcct=" + dateAcct +
                ", period=" + period +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", paymentCenter='" + paymentCenter + '\'' +
                ", brDocumentNo='" + brDocumentNo + '\'' +
                ", poDocumentNo='" + poDocumentNo + '\'' +
                ", invoiceDocumentNo='" + invoiceDocumentNo + '\'' +
                ", reverseInvoiceDocumentNo='" + reverseInvoiceDocumentNo + '\'' +
                ", originalDocumentNo='" + originalDocumentNo + '\'' +
                ", originalFiscalYear='" + originalFiscalYear + '\'' +
                ", reverseOriginalDocumentNo='" + reverseOriginalDocumentNo + '\'' +
                ", reverseOriginalFiscalYear='" + reverseOriginalFiscalYear + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", costCenter1='" + costCenter1 + '\'' +
                ", costCenter2='" + costCenter2 + '\'' +
                ", headerReference='" + headerReference + '\'' +
                ", documentHeaderText='" + documentHeaderText + '\'' +
                ", headerReference2='" + headerReference2 + '\'' +
                ", reverseDateAcct=" + reverseDateAcct +
                ", reverseReason='" + reverseReason + '\'' +
                ", documentCreated=" + documentCreated +
                ", userPark='" + userPark + '\'' +
                ", userPost='" + userPost + '\'' +
                ", originalDocument='" + originalDocument + '\'' +
                ", referenceInterDocumentNo='" + referenceInterDocumentNo + '\'' +
                ", referenceInterCompanyCode='" + referenceInterCompanyCode + '\'' +
                ", referenceAutoGen='" + referenceAutoGen + '\'' +
                ", documentStatus='" + documentStatus + '\'' +
                ", rpApproved='" + rpApproved + '\'' +
                ", documentBaseType='" + documentBaseType + '\'' +
                ", paymentDocumentNo='" + paymentDocumentNo + '\'' +
                ", paymentId=" + paymentId +
                '}';
    }
}
