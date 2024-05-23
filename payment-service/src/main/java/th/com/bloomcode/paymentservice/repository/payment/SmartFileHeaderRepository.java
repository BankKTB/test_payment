package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.SmartFileHeader;

public interface SmartFileHeaderRepository extends CrudRepository<SmartFileHeader, Long> {
    SmartFileHeader findOneByGenerateFileAliasId(Long id);
}
