package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Area extends BaseModel implements Serializable {
    public static final String TABLE_NAME = "TH_BGBUDGETAREA";
    public static final String COLUMN_NAME_BUDGET_AREA_ID = "TH_BGBUDGETAREA_ID";
    public static final String COLUMN_NAME_VALUECODE = "VALUECODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_NAME_FI_AREA = "FIAREA";
    private String valueCode;
    private String name;
    private String description;
    private String fiArea;

    public Area(long id, String valueCode, String name, String description, String fiArea) {
        super(id);
        this.valueCode = valueCode;
        this.name = name;
        this.description = description;
        this.fiArea = fiArea;
    }
}
