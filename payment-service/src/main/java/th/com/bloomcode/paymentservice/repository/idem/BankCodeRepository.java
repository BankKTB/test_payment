package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.BankCode;

import java.util.List;

public interface BankCodeRepository extends CrudRepository<BankCode, Long> {
  List<BankCode> findAll(boolean isActive);
}
