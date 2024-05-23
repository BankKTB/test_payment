package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.repository.payment.CommonSequenceRepository;
import th.com.bloomcode.paymentservice.service.payment.CommonSequenceService;

@Service
public class CommonSequenceServiceImpl implements CommonSequenceService {

  private final CommonSequenceRepository commonSequenceRepository;

  @Autowired
  public CommonSequenceServiceImpl(CommonSequenceRepository commonSequenceRepository) {
    this.commonSequenceRepository = commonSequenceRepository;
  }

  @Override
  public synchronized Long getCurrentSeq(String seqName, String seqKey) {
    return commonSequenceRepository.getCurrentSeq(seqName, seqKey);
  }

  @Override
  public synchronized Long getCurrentSeq(String seqName, String seqKey, int allocate) {
    return commonSequenceRepository.getCurrentSeq(seqName, seqKey, allocate);
  }
}
