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
public class AssetGroupAcct extends BaseModel {
    public static final String TABLE_NAME = "A_ASSET_GROUP_ACCT";
    public static final String COLUMN_NAME_A_ASSET_GROUP_ACCT_ID = "A_ASSET_GROUP_ACCT_ID";
    public static final String COLUMN_NAME_NAME = "NAME";
//    public static final String COLUMN_NAME_DEPRECIATIONAREA = "DEPRECIATIONAREA";
    public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
//    public static final String COLUMN_NAME_DEPRECIATIONMETHOD = "DEPRECIATIONMETHOD";
//    public static final String COLUMN_NAME_USELIFEYEARS = "USELIFEYEARS";
//    public static final String COLUMN_NAME_USELIFEMONTHS = "USELIFEMONTHS";
    private Long id;
    private String name;
//    private String depreciationArea;
    private String description;
//    private String depreciationMethod;
//    private String useLifeYears;
//    private String useLifeMonth;

    public AssetGroupAcct(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
