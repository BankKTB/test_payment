package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRunErrorDocumentDetailLog extends BaseModel {
    public static final String TABLE_NAME = "PAYMENT_RUN_ERROR_DOCUMENT_DETAIL_LOG";

    public static final String COLUMN_NAME_PAYMENT_RUN_ERROR_DOCUMENT_DETAIL_LOG = "ID";
    public static final String COLUMN_NAME_INVOICE_DOCUMENT_NO = "INVOICE_DOCUMENT_NO";
    public static final String COLUMN_NAME_INVOICE_COMPANY_CODE = "INVOICE_COMPANY_CODE";
    public static final String COLUMN_NAME_INVOICE_FISCAL_YEAR = "INVOICE_FISCAL_YEAR";
    public static final String COLUMN_NAME_PM_GROUP_NO = "PM_GROUP_NO";
    public static final String COLUMN_NAME_PM_GROUP_DOC = "PM_GROUP_DOC";
    public static final String COLUMN_NAME_ERROR_CODE = "ERROR_CODE";
    public static final String COLUMN_NAME_LINE = "LINE";
    public static final String COLUMN_NAME_TEXT = "TEXT";
    public static final String COLUMN_NAME_DATE_TIME = "DATE_TIME";
    public static final String COLUMN_NAME_PAYMENT_ALIAS_ID = "PAYMENT_ALIAS_ID";

    private String invoiceDocumentNo;
    private String invoiceCompanyCode;
    private String invoiceFiscalYear;
    private String pmGroupNo;
    private String pmGroupDoc;
    private String errorCode;
    private String line;
    private String text;
    private Timestamp dateTime;
    private Long paymentAliasId;


}
