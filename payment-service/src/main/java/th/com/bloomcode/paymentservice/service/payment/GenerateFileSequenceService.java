package th.com.bloomcode.paymentservice.service.payment;

import java.sql.Timestamp;

public interface GenerateFileSequenceService {
  Long getCurrentSeq(String seqName, Timestamp effectiveDate);
  Long getCurrentSeq(String seqName, Timestamp effectiveDate, int allocate);
}
