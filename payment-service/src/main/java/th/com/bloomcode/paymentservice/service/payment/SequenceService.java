package th.com.bloomcode.paymentservice.service.payment;

public interface SequenceService {
  Long getNext(String seqName);
}
