package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.HouseBankPaymentMethod;

import java.util.List;

public interface HouseBankPaymentMethodService {
    ResponseEntity<Result<List<HouseBankPaymentMethod>>> findAllByValueCode(String valueCode);
    ResponseEntity<Result<HouseBankPaymentMethod>> findOneByValueCode(String client, String houseBankKey, String paymentMethod);
    List<HouseBankPaymentMethod> findAllByCompanyCodePay(String compCode);
}
