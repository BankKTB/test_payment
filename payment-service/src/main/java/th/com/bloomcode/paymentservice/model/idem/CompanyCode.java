package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CompanyCode extends BaseModel implements Serializable {
    public static final String TABLE_NAME = "TH_CACOMPCODE";
    public static final String COLUMN_NAME_TH_CACOMPCODE_ID = "TH_CACOMPCODE_ID";
    public static final String COLUMN_NAME_VALUECODE = "VALUECODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_ISACTIVE = "ISACTIVE";
    public static final String COLUMN_NAME_OLD_COMPCODE = "OLD_COMPCODE";
    public static final String COLUMN_NAME_MINISTRY = "MINISTRY";
    private Long compCodeId;
    private String valueCode;
    private String name;
    private String isActive;
    private String oldCompCode;
    private String ministry;

    public CompanyCode(long compCodeId, String valueCode, String name, String isActive, String oldCompCode, String ministry) {
        this.compCodeId = compCodeId;
        this.valueCode = valueCode;
        this.name = name;
        this.isActive = isActive;
        this.oldCompCode = oldCompCode;
        this.ministry = ministry;
    }
}
