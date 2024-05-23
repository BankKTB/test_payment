package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayee;

import java.util.List;

public interface CompanyPayeeService {
    ResponseEntity<Result<CompanyPayee>> save(CompanyPayee request);
    ResponseEntity<Result<CompanyPayee>> update(CompanyPayee request);
    ResponseEntity<Result<CompanyPayee>> delete(Long id);
    ResponseEntity<Result<List<CompanyPayee>>> findAll();
    ResponseEntity<Result<CompanyPayee>> findById(Long id);
    CompanyPayee findOneById(Long id);
}
