package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.repository.payment.GenerateFileSequenceRepository;
import th.com.bloomcode.paymentservice.service.payment.GenerateFileSequenceService;

import java.sql.Timestamp;

@Service
public class GenerateFileSequenceServiceImpl implements GenerateFileSequenceService {

  private final GenerateFileSequenceRepository generateFileSequenceRepository;

  @Autowired
  public GenerateFileSequenceServiceImpl(GenerateFileSequenceRepository generateFileSequenceRepository) {
    this.generateFileSequenceRepository = generateFileSequenceRepository;
  }

  @Override
  public synchronized Long getCurrentSeq(String seqName, Timestamp effectiveDate) {
    return generateFileSequenceRepository.getCurrentSeq(seqName, effectiveDate);
  }

  @Override
  public Long getCurrentSeq(String seqName, Timestamp effectiveDate, int allocate) {
    return generateFileSequenceRepository.getCurrentSeq(seqName, effectiveDate, allocate);
  }
}
