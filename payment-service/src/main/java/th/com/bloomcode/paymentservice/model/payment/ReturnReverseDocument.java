package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
public
class ReturnReverseDocument extends BaseModel {

    public static final String TABLE_NAME = "RETURN_REVERSE_DOCUMENT";


    public static final String COLUMN_NAME_RETURN_REVERSE_DOCUMENT_ID = "ID";

    public static final String COLUMN_NAME_ORIGINAL_COMPANY_CODE = "ORIGINAL_COMPANY_CODE";
    public static final String COLUMN_NAME_ORIGINAL_DOCUMENT_NO = "ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_ORIGINAL_DOCUMENT_TYPE = "ORIGINAL_DOCUMENT_TYPE";
    public static final String COLUMN_NAME_REVERSE_ORIGINAL_COMPANY_CODE = "REVERSE_ORIGINAL_COMPANY_CODE";
    public static final String COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO = "REVERSE_ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR = "REVERSE_ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_TYPE = "REVERSE_ORIGINAL_DOCUMENT_TYPE";
    public static final String COLUMN_NAME_ORIGINAL_IDEM_STATUS = "ORIGINAL_IDEM_STATUS";
    public static final String COLUMN_NAME_ORIGINAL_USER_POST = "ORIGINAL_USER_POST";

    public static final String COLUMN_NAME_INVOICE_COMPANY_CODE = "INVOICE_COMPANY_CODE";
    public static final String COLUMN_NAME_INVOICE_DOCUMENT_NO = "INVOICE_DOCUMENT_NO";
    public static final String COLUMN_NAME_INVOICE_FISCAL_YEAR = "INVOICE_FISCAL_YEAR";
    public static final String COLUMN_NAME_INVOICE_DOCUMENT_TYPE = "INVOICE_DOCUMENT_TYPE";
    public static final String COLUMN_NAME_REVERSE_INVOICE_COMPANY_CODE = "REVERSE_INVOICE_COMPANY_CODE";
    public static final String COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO = "REVERSE_INVOICE_DOCUMENT_NO";
    public static final String COLUMN_NAME_REVERSE_INVOICE_FISCAL_YEAR = "REVERSE_INVOICE_FISCAL_YEAR";
    public static final String COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_TYPE = "REVERSE_INVOICE_DOCUMENT_TYPE";
    public static final String COLUMN_NAME_INVOICE_IDEM_STATUS = "INVOICE_IDEM_STATUS";
    public static final String COLUMN_NAME_INVOICE_USER_POST = "INVOICE_USER_POST";

    public static final String COLUMN_NAME_PAYMENT_COMPANY_CODE = "PAYMENT_COMPANY_CODE";
    public static final String COLUMN_NAME_PAYMENT_DOCUMENT_NO = "PAYMENT_DOCUMENT_NO";
    public static final String COLUMN_NAME_PAYMENT_FISCAL_YEAR = "PAYMENT_FISCAL_YEAR";
    public static final String COLUMN_NAME_PAYMENT_DOCUMENT_TYPE = "PAYMENT_DOCUMENT_TYPE";
    public static final String COLUMN_NAME_REVERSE_PAYMENT_COMPANY_CODE = "REVERSE_PAYMENT_COMPANY_CODE";
    public static final String COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_NO = "REVERSE_PAYMENT_DOCUMENT_NO";
    public static final String COLUMN_NAME_REVERSE_PAYMENT_FISCAL_YEAR = "REVERSE_PAYMENT_FISCAL_YEAR";
    public static final String COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_TYPE = "REVERSE_PAYMENT_DOCUMENT_TYPE";
    public static final String COLUMN_NAME_PAYMENT_IDEM_STATUS = "PAYMENT_IDEM_STATUS";
    public static final String COLUMN_NAME_PAYMENT_USER_POST = "PAYMENT_USER_POST";

    public static final String COLUMN_NAME_PO_DOC_NO = "PO_DOC_NO";

    public static final String COLUMN_NAME_AUTO_STEP3 = "AUTO_STEP3";

    private String originalCompanyCode;
    private String originalDocumentNo;
    private String originalFiscalYear;
    private String originalDocumentType;
    private String reverseOriginalCompanyCode;
    private String reverseOriginalDocumentNo;
    private String reverseOriginalFiscalYear;
    private String reverseOriginalDocumentType;
    private String originalIdemStatus;
    private String originalUserPost;

    private String invoiceCompanyCode;
    private String invoiceDocumentNo;
    private String invoiceFiscalYear;
    private String invoiceDocumentType;
    private String reverseInvoiceCompanyCode;
    private String reverseInvoiceDocumentNo;
    private String reverseInvoiceFiscalYear;
    private String reverseInvoiceDocumentType;
    private String invoiceIdemStatus;
    private String invoiceUserPost;

    private String paymentCompanyCode;
    private String paymentDocumentNo;
    private String paymentFiscalYear;
    private String paymentDocumentType;
    private String reversePaymentCompanyCode;
    private String reversePaymentDocumentNo;
    private String reversePaymentFiscalYear;
    private String reversePaymentDocumentType;
    private String paymentIdemStatus;
    private String paymentUserPost;
    private boolean autoStep3=false;
    private String poDocNo;


    private Long total;
    private Long success;
    private Long fail;
    private Long process;

    public ReturnReverseDocument() {

    }
    public ReturnReverseDocument(ProposalReturnLog proposalReturnLog) {
        this.originalCompanyCode = proposalReturnLog.getOriginalCompCode();
        this.originalDocumentNo = proposalReturnLog.getOriginalDocumentNo();
        this.originalFiscalYear = proposalReturnLog.getOriginalFiscalYear();
//        this.originalIdemStatus = originalIdemStatus;
//        this.originalUserPost = originalUserPost;

        this.invoiceCompanyCode = proposalReturnLog.getInvoiceCompCode();
        this.invoiceDocumentNo = proposalReturnLog.getInvoiceDocNo();
        this.invoiceFiscalYear = proposalReturnLog.getInvoiceFiscalYear();
//        this.invoiceIdemStatus = invoiceIdemStatus;
//        this.invoiceUserPost = invoiceUserPost;

        this.paymentCompanyCode = proposalReturnLog.getPaymentCompCode();
        this.paymentDocumentNo = proposalReturnLog.getPaymentDocNo();
        this.paymentFiscalYear = proposalReturnLog.getPaymentFiscalYear();
//        this.paymentIdemStatus = paymentIdemStatus;
//        this.paymentUserPost = paymentUserPost;

    }
}
