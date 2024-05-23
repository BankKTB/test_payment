package th.com.bloomcode.paymentservice.model.idem;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CAEffectiveAuthorization implements Serializable {

  @Column(name = "TH_CAUSER_ID")
  public int userId;

  @Column(name = "TH_CAEFFECTIVEAUTHORIZATION_ID")
  public int effectiveAuthorizationId;

  @Column(name = "AUTHORIZATIONATTRIBUTE")
  public String authorizationAttribute;

  @Column(name = "FROMVALUE")
  public String fromValue;

  @Column(name = "TOVALUE")
  public String toValue;

  @Column(name = "ISEXCLUDE")
  public String isExclude;

  @Column(name = "ISACTIVE")
  public String isActive;

  @Column(name = "CREATED")
  public Timestamp created;

  @Column(name = "UPDATED")
  public Timestamp updated;

  @Column(name = "CREATEDBY")
  public int createdBy;

  @Column(name = "UPDATEDBY")
  public int updatedBy;

  @Column(name = "AD_CLIENT_ID")
  public int clientId;

  @Column(name = "AD_ORG_ID")
  public int orgId;

  @Column(name = "AUTHORIZATIONOBJECT")
  public String authorizationObject;

  @Column(name = "AUTHORIZATIONACTIVITY")
  public String authorizationActivity;
}
