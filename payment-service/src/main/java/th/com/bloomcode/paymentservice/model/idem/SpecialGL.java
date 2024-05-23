package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class SpecialGL extends BaseModel {
    public static final String TABLE_NAME = "TH_CASPECIALGL";
    public static final String COLUMN_NAME_TH_CASPECIALGL_ID = "TH_CASPECIALGL_ID";
    public static final String COLUMN_NAME_VALUECODE = "VALUECODE";
    public static final String COLUMN_NAME_NAME = "NAME";

    private Long specialGlId;
    private String valueCode;
    private String name;

    public SpecialGL(long specialGlId, String valueCode, String name) {
        this.specialGlId = specialGlId;
        this.valueCode = valueCode;
        this.name = name;
    }
}
