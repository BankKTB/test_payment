package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public
class UnblockDocumentDetailLog extends BaseModel {

    public static final String TABLE_NAME = "UNBLOCK_DOCUMENT_DETAIL_LOG";


    public static final String COLUMN_NAME_UNBLOCK_DOCUMENT_DETAIL_LOG = "ID";
    public static final String COLUMN_NAME_ORIGINAL_DOCUMENT_NO = "ORIGINAL_DOCUMENT_NO";
    public static final String COLUMN_NAME_ORIGINAL_COMPANY_CODE = "ORIGINAL_COMPANY_CODE";
    public static final String COLUMN_NAME_ORIGINAL_FISCAL_YEAR = "ORIGINAL_FISCAL_YEAR";
    public static final String COLUMN_NAME_ERROR_CODE = "ERROR_CODE";
    public static final String COLUMN_NAME_LINE = "LINE";
    public static final String COLUMN_NAME_TEXT = "TEXT";
    public static final String COLUMN_NAME_DATE_TIME = "DATE_TIME";

    private String originalDocumentNo;
    private String originalCompanyCode;
    private String originalFiscalYear;

    private String errorCode;
    private String line;
    private String text;
    private Timestamp dateTime;
}
