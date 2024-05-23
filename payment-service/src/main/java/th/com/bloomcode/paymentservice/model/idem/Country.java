package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class Country extends BaseModel {
    public static final String TABLE_NAME = "C_COUNTRY";
    public static final String COLUMN_NAME_C_COUNTRY_ID = "C_COUNTRY_ID";
    public static final String COLUMN_NAME_COUNTRY_CODE = "COUNTRYCODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    private String valueCode;
    private String name;
    private String description;
    public Country(long id, String valueCode, String name, String description) {
        super(id);
        this.valueCode = valueCode;
        this.name = name;
        this.description = description;
    }

}
