package th.com.bloomcode.paymentservice.service.payment;

import java.sql.Timestamp;

public interface CommonSequenceService {
  Long getCurrentSeq(String seqName, String seqKey);
  Long getCurrentSeq(String seqName, String seqKey, int allocate);
}
