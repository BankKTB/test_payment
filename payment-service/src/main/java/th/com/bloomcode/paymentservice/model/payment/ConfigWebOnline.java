package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class ConfigWebOnline  extends BaseModel {
    public static final String TABLE_NAME = "CONFIG_WEBONLINE";

    public static final String COLUMN_NAME_CONFIG_WEB_ONLINE_ID = "ID";
    public static final String COLUMN_NAME_VALUE_CODE = "VALUE_CODE";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_URL = "URL";
    public static final String COLUMN_NAME_IS_ACTIVE = "IS_ACTIVE";

    private String valueCode;
    private String name;
    private String url;
    private boolean isActive;

    public ConfigWebOnline(Long id,
                           String valueCode,
                           String name,
                           String url, boolean isActive) {
        super(id);
        this.valueCode = valueCode;
        this.name = name;
        this.url = url;
        this.isActive = isActive;
    }
}
