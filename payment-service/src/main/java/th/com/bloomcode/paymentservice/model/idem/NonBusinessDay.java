package th.com.bloomcode.paymentservice.model.idem;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class NonBusinessDay extends BaseModel {
    public static final String TABLE_NAME = "C_NONBUSINESSDAY";
    public static final String COLUMN_NAME_C_NONBUSINESSDAY_ID = "C_NONBUSINESSDAY_ID";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_ISACTIVE = "ISACTIVE";
    public static final String COLUMN_NAME_DATE1 = "DATE1";
    private Long id;
    private String name;
    private boolean isActive;
    private Timestamp date1;
    public NonBusinessDay(long id, String name, boolean isActive, Timestamp date1) {
        super(id);
        this.name = name;
        this.isActive = isActive;
        this.date1 = date1;
    }
}
