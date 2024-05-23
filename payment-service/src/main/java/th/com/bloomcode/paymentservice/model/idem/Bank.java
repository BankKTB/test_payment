package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class Bank extends BaseModel {
    public static final String TABLE_NAME = "TH_CABANK";
    public static final String COLUMN_NAME_TH_CABANK_ID = "TH_CABANK_ID";
    public static final String COLUMN_NAME_VALUECODE = "VALUECODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    private Long id;
    private String valueCode;
    private String name;

    public Bank(long id, String valueCode, String name) {
        this.id = id;
        this.valueCode = valueCode;
        this.name = name;
    }
}
