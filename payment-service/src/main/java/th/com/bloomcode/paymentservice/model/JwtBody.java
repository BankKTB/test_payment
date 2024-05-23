package th.com.bloomcode.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class JwtBody implements Serializable {

  @JsonProperty("sub")
  private String sub;

  @JsonProperty("aud")
  private String aud;

  @JsonProperty("nbf")
  private String nbf;

  @JsonProperty("azp")
  private String azp;

  @JsonProperty("scope")
  private String scope;

  @JsonProperty("iss")
  private String iss;

  @JsonProperty("SN")
  private String SN;

  @JsonProperty("CN")
  private String CN;

  @JsonProperty("exp")
  private String exp;

  @JsonProperty("iat")
  private String iat;

  @JsonProperty("jti")
  private String jti;
}
