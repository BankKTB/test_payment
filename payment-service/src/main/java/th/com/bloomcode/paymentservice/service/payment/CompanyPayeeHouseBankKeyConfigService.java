package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayeeHouseBankKeyConfig;
import th.com.bloomcode.paymentservice.model.response.CompanyPayeeHouseBankKeyConfigRespone;

import java.util.List;

public interface CompanyPayeeHouseBankKeyConfigService {
    ResponseEntity<Result<List<CompanyPayeeHouseBankKeyConfig>>> findAllByCompanyPayeeId(Long companyPayeeId);
    ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> save(CompanyPayeeHouseBankKeyConfig request);
    ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> update(CompanyPayeeHouseBankKeyConfig request);
    ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> delete(Long id);
    ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> findById(Long id);
    CompanyPayeeHouseBankKeyConfig findOneById(Long id);
}
