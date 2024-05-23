package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.PaymentMethod;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingMinimalAmount;

import java.util.List;

public interface CompanyPayingMinimalAmountService {
    List<CompanyPayingMinimalAmount> findAllByValue(String valueCode);

//    ResponseEntity<Result<CompanyPayingMinimalAmount>> findOneByValue(String valueCode);
//
    CompanyPayingMinimalAmount findOneByValueCode(String valueCode);
}
