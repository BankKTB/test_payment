package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class Currency extends BaseModel {
    public static final String TABLE_NAME = "C_CURRENCY";
    public static final String COLUMN_NAME_C_CURRENCY_ID = "C_CURRENCY_ID";
    public static final String COLUMN_NAME_VALUE_CODE = "VALUECODE";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    private String valueCode;
    private String description;
    public Currency(long id, String valueCode, String description) {
        super(id);
        this.valueCode = valueCode;
        this.description = description;
    }
}
