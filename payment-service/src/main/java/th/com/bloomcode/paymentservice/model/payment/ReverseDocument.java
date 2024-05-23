package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public
class ReverseDocument extends BaseModel {

    public static final String TABLE_NAME = "REVERSE_DOCUMENT";


    public static final String COLUMN_NAME_PAYMENT_DOCUMENT_LOG_ID = "ID";

    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_DOCUMENT_NO = "DOCUMENT_NO";
    public static final String COLUMN_NAME_FISCAL_YEAR = "FISCAL_YEAR";
    public static final String COLUMN_NAME_DOCUMENT_TYPE = "DOCUMENT_TYPE";

    public static final String COLUMN_NAME_REVERSE_COMPANY_CODE = "REVERSE_COMPANY_CODE";
    public static final String COLUMN_NAME_REVERSE_DOCUMENT_NO = "REVERSE_DOCUMENT_NO";
    public static final String COLUMN_NAME_REVERSE_FISCAL_YEAR = "REVERSE_FISCAL_YEAR";
    public static final String COLUMN_NAME_REVERSE_DOCUMENT_TYPE = "REVERSE_DOCUMENT_TYPE";
    public static final String COLUMN_NAME_PO_DOC_NO = "PO_DOC_NO";
    public static final String COLUMN_NAME_STATUS = "STATUS";

    public static final String COLUMN_NAME_COMPANY_CODE_AGENCY = "COMPANY_CODE_AGENCY";
    public static final String COLUMN_NAME_DOCUMENT_NO_AGENCY = "DOCUMENT_NO_AGENCY";
    public static final String COLUMN_NAME_FISCAL_YEAR_AGENCY = "FISCAL_YEAR_AGENCY";

    public static final String COLUMN_NAME_USER_POST = "USER_POST";
    public static final String COLUMN_NAME_GROUP_DOC = "GROUP_DOC";


    private String companyCode;
    private String documentNo;
    private String fiscalYear;
    private String documentType;

    private String companyCodeAgency;
    private String documentNoAgency;
    private String fiscalYearAgency;

    private String reverseCompanyCode;
    private String reverseDocumentNo;
    private String reverseFiscalYear;
    private String reverseDocumentType;

    private String poDocNo;

    private String status;

    private String userPost;


    private String groupDoc;

    private Long total;
    private Long success;
    private Long fail;
    private Long process;

    @Override
    public String toString() {
        return "ReverseDocument{" +
                "companyCode='" + companyCode + '\'' +
                ", documentNo='" + documentNo + '\'' +
                ", fiscalYear='" + fiscalYear + '\'' +
                ", documentType='" + documentType + '\'' +
                ", reverseCompanyCode='" + reverseCompanyCode + '\'' +
                ", reverseDocumentNo='" + reverseDocumentNo + '\'' +
                ", reverseFiscalYear='" + reverseFiscalYear + '\'' +
                ", reverseDocumentType='" + reverseDocumentType + '\'' +
                ", status='" + status + '\'' +
                ", userPost='" + userPost + '\'' +
                '}';
    }
}
