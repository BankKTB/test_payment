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
public class AssetGroup extends BaseModel {
    public static final String TABLE_NAME = "A_ASSET_GROUP";
    public static final String COLUMN_NAME_A_ASSET_GROUP_ID = "A_ASSET_GROUP_ID";
    public static final String COLUMN_NAME_VALUECODE = "VALUECODE";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_NAME_HELP = "HELP";
    private Long id;
    private String valueCode;
    private String description;
    private String help;

    public AssetGroup(long id, String valueCode, String name, String description, String help) {
        this.id = id;
        this.valueCode = valueCode;
        this.description = description;
        this.help = help;
    }

}
