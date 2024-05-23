package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.CommonSequence;

public interface CommonSequenceRepository extends CrudRepository<CommonSequence, Long> {
  Long getCurrentSeq(String seqName, String seqKey);
  Long getCurrentSeq(String seqName, String seqKey, int allocate);
}
