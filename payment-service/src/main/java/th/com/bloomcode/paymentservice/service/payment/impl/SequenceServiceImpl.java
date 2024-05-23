package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.repository.payment.SequenceRepository;
import th.com.bloomcode.paymentservice.service.payment.SequenceService;

@Service
public class SequenceServiceImpl implements SequenceService {

  private final SequenceRepository sequenceRepository;

  public SequenceServiceImpl(SequenceRepository sequenceRepository) {
    this.sequenceRepository = sequenceRepository;
  }

  @Override
  public Long getNext(String seqName) {
    return this.sequenceRepository.getNext(seqName);
  }
}
