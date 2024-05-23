package th.com.bloomcode.paymentservice.service.idem;



import th.com.bloomcode.paymentservice.model.idem.CAClient;

import java.util.List;

public interface CAClientService {
  List<CAClient> findAll();
}
