package th.com.bloomcode.paymentservice.model.idem;

import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
public class CABOBUser {
    @Column(name = "TH_CABOBUSER_ID")
    private int bobUserId;

    @Column(name = "AD_CLIENT_ID")
    private int clientId;

    @Column(name = "AD_ORG_ID")
    private int orgId;

    @Column(name = "CREATED")
    private String created;

    @Column(name = "CREATEDBY")
    private int createdBy;

    @Column(name = "ISACTIVE")
    private String isActive;

    @Column(name = "TH_CABOBUSER_UU")
    private String bobUserUU;

    @Column(name = "UPDATED")
    private String updated;

    @Column(name = "UPDATEDBY")
    private int updatedBy;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "VALIDFROM")
    private Timestamp validFrom;

    @Column(name = "VALIDTO")
    private Timestamp validTo;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME2")
    private String name2;

    @Column(name = "TH_CACOMPCODE_ID")
    private Long compCodeId;

    @Column(name = "TH_BGCOSTCENTER_ID")
    private Long costCenterId;
}
