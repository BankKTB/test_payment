package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {
    ResponseEntity<Result<List<PaymentMethod>>> findAllByValue(String valueCode);
    ResponseEntity<Result<PaymentMethod>> findOneByValue(String valueCode);
    PaymentMethod findOneByValueCode(String valueCode);
}
