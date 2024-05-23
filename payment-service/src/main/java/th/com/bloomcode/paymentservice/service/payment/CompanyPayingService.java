package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPaying;

import java.util.List;

public interface CompanyPayingService {
    ResponseEntity<Result<CompanyPaying>> save(CompanyPaying request);
    ResponseEntity<Result<CompanyPaying>> update(CompanyPaying request);
    ResponseEntity<Result<CompanyPaying>> delete(Long id);
    ResponseEntity<Result<List<CompanyPaying>>> findAll();
    ResponseEntity<Result<CompanyPaying>> findById(Long id);
    CompanyPaying findOneById(Long id);
}
