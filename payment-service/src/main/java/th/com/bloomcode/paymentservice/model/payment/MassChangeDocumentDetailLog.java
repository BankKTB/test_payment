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
class MassChangeDocumentDetailLog extends BaseModel {

    public static final String TABLE_NAME = "MASS_CHANGE_DOCUMENT_DETAIL_LOG";


    public static final String COLUMN_NAME_MASS_CHANGE_DOCUMENT_DETAIL_LOG = "ID";
    public static final String COLUMN_NAME_DOCUMENT_NO = "DOCUMENT_NO";
    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_FISCAL_YEAR = "FISCAL_YEAR";
    public static final String COLUMN_NAME_ERROR_CODE = "ERROR_CODE";
    public static final String COLUMN_NAME_LINE = "LINE";
    public static final String COLUMN_NAME_TEXT = "TEXT";
    public static final String COLUMN_NAME_DATE_TIME = "DATE_TIME";

    private String documentNo;
    private String companyCode;
    private String fiscalYear;

    private String errorCode;
    private String line;
    private String text;
    private Timestamp dateTime;
}
