package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileSequence;

import java.sql.Timestamp;

public interface GenerateFileSequenceRepository extends CrudRepository<GenerateFileSequence, Long> {
  Long getCurrentSeq(String seqName, Timestamp effectiveDate);
  Long getCurrentSeq(String seqName, Timestamp effectiveDate, int allocate);
}
