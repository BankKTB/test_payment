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
class MassChangeDocument extends BaseModel {

    public static final String TABLE_NAME = "MASS_CHANGE_DOCUMENT";


    public static final String COLUMN_NAME_MASS_CHANGE_DOCUMENT_ID = "ID";

    public static final String COLUMN_NAME_COMPANY_CODE = "COMPANY_CODE";
    public static final String COLUMN_NAME_DOCUMENT_NO = "DOCUMENT_NO";
    public static final String COLUMN_NAME_FISCAL_YEAR = "FISCAL_YEAR";
    public static final String COLUMN_NAME_PAYMENT_BLOCK = "PAYMENT_BLOCK";
    public static final String COLUMN_NAME_STATUS = "STATUS";

    public static final String COLUMN_NAME_USER_POST = "USER_POST";
    public static final String COLUMN_NAME_GROUP_DOC = "GROUP_DOC";

    private String companyCode;
    private String documentNo;
    private String fiscalYear;

    private String paymentBlock;


    private String status;

    private String userPost;
    private String groupDoc;

    private Long total;
    private Long success;
    private Long fail;
    private Long process;
}
