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
public class BudgetCode extends BaseModel {
    public static final String TABLE_NAME = "TH_BGBUDGETCODE";
    public static final String COLUMN_NAME_TH_BGBUDGETCODE_ID = "TH_BGBUDGETCODE_ID";
    public static final String COLUMN_NAME_VALUECODE = "VALUECODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_NAME_GFMISBUDGETCODE = "GFMISBUDGETCODE";
    private Long id;
    private String valueCode;
    private String name;
    private String description;
    private String gfmisBudgetCode;

    public BudgetCode(long id, String valueCode, String name, String gfmisBudgetCode) {
        this.id = id;
        this.valueCode = valueCode;
        this.name = name;
        this.gfmisBudgetCode = gfmisBudgetCode;
    }
}
