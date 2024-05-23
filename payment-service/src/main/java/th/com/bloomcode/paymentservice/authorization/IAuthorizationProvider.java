package th.com.bloomcode.paymentservice.authorization;

import java.util.List;
import java.util.Map;

public interface IAuthorizationProvider {
  Map<String, List<String>> createAuthorizationCheckData();
}
