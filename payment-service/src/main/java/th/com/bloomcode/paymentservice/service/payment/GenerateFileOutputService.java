package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.GenerateFileOutput;

public interface GenerateFileOutputService {
    void delete(GenerateFileOutput generateFileOutput);
    GenerateFileOutput save(GenerateFileOutput generateFileOutput);
    GenerateFileOutput findOneByRefRunning(Long generateFileAliasId);
}
