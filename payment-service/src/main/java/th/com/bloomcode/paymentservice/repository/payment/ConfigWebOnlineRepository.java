package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.ConfigWebOnline;

import java.util.List;

public interface
ConfigWebOnlineRepository extends CrudRepository<ConfigWebOnline, Long> {
    ConfigWebOnline findFirstByValueCodeStartingWithAndIsActiveTrue(String valueCode);
    List<ConfigWebOnline> findAllByIsActiveTrue();
}
