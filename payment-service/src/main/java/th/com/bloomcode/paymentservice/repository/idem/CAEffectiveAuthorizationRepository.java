package th.com.bloomcode.paymentservice.repository.idem;

import th.com.bloomcode.paymentservice.model.idem.CAEffectiveAuthorization;

import java.util.List;

public interface CAEffectiveAuthorizationRepository {
  List<CAEffectiveAuthorization> findByUsername(String username, String object, String activity, String attribute);
}
