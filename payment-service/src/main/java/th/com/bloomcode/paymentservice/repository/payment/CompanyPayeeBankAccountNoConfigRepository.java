package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayeeBankAccountNoConfig;

import java.util.List;

public interface CompanyPayeeBankAccountNoConfigRepository extends CrudRepository<CompanyPayeeBankAccountNoConfig, Long> {
    CompanyPayeeBankAccountNoConfig findOneByAccountCode(String accountCode);
    List<CompanyPayeeBankAccountNoConfig> findAllByHouseBankKeyIdOrderByAccountCodeAsc(String houseBankKeyId);
    void deleteAllByHouseBankKeyId(Long houseBankKeyId);
}
