package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileOutput;
import th.com.bloomcode.paymentservice.repository.payment.GenerateFileOutputRepository;
import th.com.bloomcode.paymentservice.service.payment.GenerateFileOutputService;

@Service
public class GenerateFileOutputServiceImpl implements GenerateFileOutputService {
  private final GenerateFileOutputRepository generateFileOutputRepository;
  public GenerateFileOutputServiceImpl(GenerateFileOutputRepository generateFileOutputRepository) {
    this.generateFileOutputRepository = generateFileOutputRepository;
  }

  @Override
  public GenerateFileOutput findOneByRefRunning(Long generateFileAliasId) {
    return generateFileOutputRepository.findOneByRefRunning(generateFileAliasId);
  }

  @Override
  public void delete(GenerateFileOutput generateFileOutput) {
    generateFileOutputRepository.delete(generateFileOutput);
  }

  @Override
  public GenerateFileOutput save(GenerateFileOutput generateFileOutput) {
    return generateFileOutputRepository.save(generateFileOutput);
  }
}
