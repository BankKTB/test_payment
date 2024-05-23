package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.PaymentCenter;

import java.util.List;

public interface PaymentCenterService {
    ResponseEntity<Result<List<PaymentCenter>>> findAllByValue(String valueCode);
    ResponseEntity<Result<PaymentCenter>> findOneByValue(String valueCode);
    PaymentCenter findOneByValueCode(String valueCode);
}
