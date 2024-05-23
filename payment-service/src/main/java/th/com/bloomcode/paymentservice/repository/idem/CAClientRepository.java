package th.com.bloomcode.paymentservice.repository.idem;


import th.com.bloomcode.paymentservice.model.idem.CAClient;

import java.util.List;

public interface CAClientRepository {
    List<CAClient> findAll();
}
