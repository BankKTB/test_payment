package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class SelectGroupDocument extends BaseModel {
    public static final String TABLE_NAME = "SELECT_GROUP_DOCUMENT";

    public static final String COLUMN_NAME_SELECT_GROUP_DOCUMENT_ID = "ID";
    public static final String COLUMN_NAME_FISCAL_YEAR = "FISCAL_YEAR";
    public static final String COLUMN_NAME_DEFINE_NAME = "DEFINE_NAME";
    public static final String COLUMN_NAME_JSON_TEXT = "JSON_TEXT";

    private String fiscalYear;
    private String defineName;
    private String jsonText;


    @Override
    public String toString() {
        return "SelectGroupDocument{" +
                "fiscalYear='" + fiscalYear + '\'' +
                ", defineName='" + defineName + '\'' +
                ", jsonText='" + jsonText + '\'' +
                '}';
    }
}
