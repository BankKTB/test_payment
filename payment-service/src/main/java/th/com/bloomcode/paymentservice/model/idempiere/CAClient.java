package th.com.bloomcode.paymentservice.model.idempiere;

import lombok.Data;

import javax.persistence.Column;

@Data
public class CAClient {
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
