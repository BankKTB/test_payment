package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class OmSearchCriteria  extends BaseModel {
    public static final String TABLE_NAME = "OM_SEARCH_CRITERIA";

    public static final String COLUMN_NAME_OM_SEARCH_CRITERIA_ID = "ID";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_ROLE = "ROLE";
    public static final String COLUMN_NAME_IS_USER_ONLY = "IS_USER_ONLY";
    public static final String COLUMN_NAME_JSON_TEXT = "JSON_TEXT";

    private String name;
    private String role;
    private Boolean isUserOnly;
    private String jsonText;
    public OmSearchCriteria(Long id
            , String name
            , String role
            , Boolean isUserOnly
            , String jsonText) {
        super(id);
        this.name = name;
        this.role = role;
        this.isUserOnly = isUserOnly;
        this.jsonText = jsonText;
    }
}
