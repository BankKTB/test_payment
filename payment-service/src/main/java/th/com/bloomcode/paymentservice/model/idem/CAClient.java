package th.com.bloomcode.paymentservice.model.idem;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.BaseModel;

import javax.persistence.Column;

@Data
public class CAClient extends BaseModel {


    public static final String TABLE_NAME = "TH_CACLIENT";
    public static final String COLUMN_NAME_AD_ORG_ID = "AD_ORG_ID";
    public static final String COLUMN_NAME_CLIENTVALUE = "CLIENTVALUE";
    public static final String COLUMN_NAME_TARGETURL = "TARGETURL";
    public static final String COLUMN_NAME_CLIENT_ID = "CLIENT_ID";
    public static final String COLUMN_NAME_ROLE_ID = "ROLE_ID";
    public static final String COLUMN_NAME_USERNAME = "USERNAME";
    public static final String COLUMN_NAME_PASSWORD = "PASSWORD";

    @Column(name = "AD_ORG_ID")
    public int orgId;

    @Column(name = "CLIENTVALUE")
    public String clientValue;

    @Column(name = "TARGETURL")
    public String targetUrl;

    @Column(name = "CLIENT_ID")
    public int clientId;

    @Column(name = "ROLE_ID")
    public int roleId;

    @Column(name = "USERNAME")
    public String username;

    @Column(name = "PASSWORD")
    public String password;
}
