package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayeeBankAccountNoConfig;

import java.util.List;

public interface CompanyPayeeBankAccountNoConfigService {
    ResponseEntity<Result<List<CompanyPayeeBankAccountNoConfig>>> findAllByHouseBankKeyId(String value);
    ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> save(CompanyPayeeBankAccountNoConfig request);
    ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> update(CompanyPayeeBankAccountNoConfig request);
    ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> delete(Long id);
    ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> findById(Long id);
    CompanyPayeeBankAccountNoConfig findOneById(Long id);
    void deleteAllByHouseBankKeyId(Long houseBankKeyId);
}
