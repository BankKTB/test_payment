package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogHeader;

import java.sql.Timestamp;

public interface ProposalLogHeaderRepository extends CrudRepository<ProposalLogHeader, Long> {
  void updateRegen(Timestamp paymentDate, String paymentName, Long refRunning);
}
