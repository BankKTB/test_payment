package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class BankBook extends BaseModel {
    public static final String TABLE_NAME = "TH_CABANKBOOK";
    public static final String COLUMN_NAME_TH_CABANKBOOK_ID = "TH_CABANKBOOK_ID";
    public static final String COLUMN_NAME_VALUECODE = "VALUECODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_NAME_COMPANYCODE = "COMPANYCODE";
    private Long id;
    private String valueCode;
    private String name;
    private String description;
    private String companyCode;

    public BankBook(long id, String valueCode, String name, String description, String companyCode) {
        this.id = id;
        this.valueCode = valueCode;
        this.name = name;
        this.name = description;
        this.name = companyCode;
    }
}
