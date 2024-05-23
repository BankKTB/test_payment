package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class IdemConfig extends BaseModel {
    public static final String TABLE_NAME = "TH_CACLIENT";

    public static final String COLUMN_NAME_TH_CACLIENT_ID = "TH_CACLIENT_ID";
    public static final String COLUMN_NAME_AD_CLIENT_ID = "AD_CLIENT_ID";
    public static final String COLUMN_NAME_AD_ORG_ID = "AD_ORG_ID";
    public static final String COLUMN_NAME_CLIENTVALUE = "CLIENTVALUE";
    public static final String COLUMN_NAME_TARGETURL = "TARGETURL";
    public static final String COLUMN_NAME_CREATED = "CREATED";
    public static final String COLUMN_NAME_CREATEDBY = "CREATEDBY";
    public static final String COLUMN_NAME_ISACTIVE = "ISACTIVE";
    public static final String COLUMN_NAME_TH_CACLIENT_UU = "TH_CACLIENT_UU";
    public static final String COLUMN_NAME_UPDATED = "UPDATED";
    public static final String COLUMN_NAME_UPDATEDBY = "UPDATEDBY";
    public static final String COLUMN_NAME_CLIENT_ID = "CLIENT_ID";
    public static final String COLUMN_NAME_ROLE_ID = "ROLE_ID";
    public static final String COLUMN_NAME_USERNAME = "USERNAME";
    public static final String COLUMN_NAME_PASSWORD = "PASSWORD";

    private String adClientId;
    private Integer adOrgId;
    private String clientValue;
    private String targetUrl;
    private String createdBy;
    private Timestamp created;
    private String isActive;
    private String clientUU;
    private String updatedBy;
    private Timestamp updated;
    private Integer clientId;
    private Integer roleId;
    private String username;
    private String password;

    public IdemConfig(Long id,
              String adClientId,
                      Integer adOrgId,
              String clientValue,
              String targetUrl,
              String createdBy,
              Timestamp created,
                      String isActive,
                      String clientUU,
                      String updatedBy,
              Timestamp updated,
                      Integer clientId,
                      Integer roleId,
              String username,
              String password) {
        super(id);
        this.adClientId = adClientId;
        this.adOrgId = adOrgId;
        this.clientValue = clientValue;
        this.targetUrl = targetUrl;
        this.createdBy = createdBy;
        this.created = created;
        this.isActive = isActive;
        this.clientUU = clientUU;
        this.updatedBy = updatedBy;
        this.updated = updated;
        this.clientId = clientId;
        this.roleId = roleId;
        this.username = username;
        this.password = password;
    }
}
