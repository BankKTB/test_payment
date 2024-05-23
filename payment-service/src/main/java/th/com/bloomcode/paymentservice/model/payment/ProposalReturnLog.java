package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ProposalReturnLog extends BaseModel {
    public static final String TABLE_NAME = "PROPOSAL_RETURN_LOG";

    public static final String COLUMN_NAME_PROPOSAL_LOG_RETURN_ID = "ID";

    public static final String COLUMN_NAME_INVOICE_COMP_CODE = "INVOICE_COMP_CODE";
    public static final String COLUMN_NAME_INVOICE_DOC_NO = "INVOICE_DOC_NO";
    public static final String COLUMN_NAME_INVOICE_LINE = "INVOICE_LINE";
    public static final String COLUMN_NAME_INVOICE_FISCAL_YEAR = "INVOICE_FISCAL_YEAR";

    public static final String COLUMN_NAME_PAYMENT_COMP_CODE = "PAYMENT_COMP_CODE";
    public static final String COLUMN_NAME_PAYMENT_DOC_NO = "PAYMENT_DOC_NO";
    public static final String COLUMN_NAME_PAYMENT_FISCAL_YEAR = "PAYMENT_FISCAL_YEAR";


    public static final String COLUMN_NAME_PAYMENT_DATE = "PAYMENT_DATE";
    public static final String COLUMN_NAME_PAYMENT_NAME = "PAYMENT_NAME";
    public static final String COLUMN_NAME_VENDOR = "VENDOR";
    public static final String COLUMN_NAME_PAYMENT_METHOD = "PAYMENT_METHOD";

    public static final String COLUMN_NAME_BANK_KEY = "BANK_KEY";
    public static final String COLUMN_NAME_FILE_NAME = "FILE_NAME";

    public static final String COLUMN_NAME_TRANSFER_DATE = "TRANSFER_DATE";
    public static final String COLUMN_NAME_RESET_REVERSE_FLAG = "RESET_REVERSE_FLAG";

    public static final String COLUMN_NAME_REV_INVOICE_DOC_NO = "REV_INVOICE_DOC_NO";
    public static final String COLUMN_NAME_REV_INVOICE_FISCAL_YEAR = "REV_INVOICE_FISCAL_YEAR";

    public static final String COLUMN_NAME_REV_PAYMENT_DOC_NO = "REV_PAYMENT_DOC_NO";
    public static final String COLUMN_NAME_REV_PAYMENT_FISCAL_YEAR = "REV_PAYMENT_FISCAL_YEAR";


    public static final String COLUMN_NAME_ORIGINAL_COMP_CODE = "ORIGINAL_COMP_CODE";
    public static final String COLUMN_NAME_ORIGINAL_DOCUMENT_NO = "ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";

    public static final String COLUMN_NAME_REV_ORIGINAL_COMP_CODE = "REV_ORIGINAL_COMP_CODE";
    public static final String COLUMN_NAME_REV_ORIGINAL_DOCUMENT_NO = "REV_ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_REV_ORIGINAL_FISCAL_YEAR = "REV_ORIGINAL_FISCAL_YEAR";

    public static final String COLUMN_NAME_AUTO_STEP3 = "AUTO_STEP3";

    public static final String COLUMN_NAME_REASON_CODE = "REASON_CODE";
    public static final String COLUMN_NAME_REASON_TEXT = "REASON_TEXT";


    private String invoiceCompCode;
    private String invoiceDocNo;
    private int invoiceLine;
    private String invoiceFiscalYear;
    private String paymentCompCode;
    private String paymentDocNo;
    private String paymentFiscalYear;
    private Timestamp paymentDate;
    private String paymentName;
    private String vendor;
    private String paymentMethod;
    private String bankKey;
    private String fileName;
    private Timestamp transferDate;
    private String resetReverseFlag;
    private String revInvoiceDocNo;
    private String revInvoiceFiscalYear;
    private String revPaymentDocNo;
    private String revPaymentFiscalYear;

    private String originalCompCode;
    private String originalDocumentNo;
    private String originalFiscalYear;

    private String revOriginalCompCode;
    private String revOriginalDocumentNo;
    private String revOriginalFiscalYear;
    private boolean autoStep3 = false;
    private String reasonCode;
    private String reasonText;




}
