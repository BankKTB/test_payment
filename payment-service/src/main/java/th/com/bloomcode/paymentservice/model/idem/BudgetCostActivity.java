package th.com.bloomcode.paymentservice.model.idem;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
public class BudgetCostActivity extends BaseModel {
    public static final String TABLE_NAME = "TH_BGCOSTACTIVITY";
    public static final String COLUMN_NAME_TH_BGCOSTACTIVITY_ID = "TH_BGCOSTACTIVITY_ID";
    public static final String COLUMN_NAME_VALUECODE = "VALUECODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    private Long id;
    private String valueCode;
    private String name;
    private String description;

    public BudgetCostActivity(long id, String valueCode, String name, String description) {
        this.id = id;
        this.valueCode = valueCode;
        this.name = name;
        this.description = description;
    }
}
