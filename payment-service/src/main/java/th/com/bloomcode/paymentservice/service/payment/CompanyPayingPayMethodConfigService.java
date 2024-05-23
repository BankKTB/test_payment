package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingPayMethodConfig;

import java.util.List;

public interface
CompanyPayingPayMethodConfigService {
    ResponseEntity<Result<List<CompanyPayingPayMethodConfig>>> findAllByCompanyPayingId(Long companyPayingId);
    ResponseEntity<Result<CompanyPayingPayMethodConfig>> save(CompanyPayingPayMethodConfig request);
    ResponseEntity<Result<CompanyPayingPayMethodConfig>> update(CompanyPayingPayMethodConfig request);
    ResponseEntity<Result<CompanyPayingPayMethodConfig>> delete(Long id);
    ResponseEntity<Result<CompanyPayingPayMethodConfig>> findById(Long id);
    CompanyPayingPayMethodConfig findOneById(Long id);
}
