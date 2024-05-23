package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileOutput;

public interface GenerateFileOutputRepository extends CrudRepository<GenerateFileOutput, Long> {
    GenerateFileOutput findOneByRefRunning(Long generateFileAliasId);
}
