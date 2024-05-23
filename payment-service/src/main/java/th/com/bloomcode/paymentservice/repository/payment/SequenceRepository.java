package th.com.bloomcode.paymentservice.repository.payment;

public interface SequenceRepository {
  Long getNext(String seqName);
}
