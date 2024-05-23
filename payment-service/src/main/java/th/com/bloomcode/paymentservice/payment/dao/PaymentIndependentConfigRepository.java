package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.PaymentIndependentConfig;

import java.util.List;

public interface PaymentIndependentConfigRepository extends CrudRepository<PaymentIndependentConfig, Long> {

    List<PaymentIndependentConfig> findByActiveTrueAndStandardTrue();

    List<PaymentIndependentConfig> findByActiveTrueAndGroupName(String groupName);
}
