package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.SmartFileBatch;

public interface SmartFileBatchRepository extends CrudRepository<SmartFileBatch, Long> {
}
