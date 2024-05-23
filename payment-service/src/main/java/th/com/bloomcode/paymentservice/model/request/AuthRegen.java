package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;

@Data
public class AuthRegen {
  private String username;
  private String password;
}
