package th.com.bloomcode.paymentservice.service.payment;


import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingBankConfig;

import java.util.List;

public interface
CompanyPayingBankConfigService {
    ResponseEntity<Result<CompanyPayingBankConfig>> save(CompanyPayingBankConfig request);
    ResponseEntity<Result<CompanyPayingBankConfig>> update(CompanyPayingBankConfig request);
    ResponseEntity<Result<List<CompanyPayingBankConfig>>> findAllByCompanyPayingId(Long companyPayingId);
    ResponseEntity<Result<CompanyPayingBankConfig>> deleteById(Long id);
}
