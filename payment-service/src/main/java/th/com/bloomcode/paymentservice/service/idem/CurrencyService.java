package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.Currency;

import java.util.List;

public interface CurrencyService {
    ResponseEntity<Result<List<Currency>>> findAllByValueCode(String valueCode);
    ResponseEntity<Result<Currency>> findOneByValueCode(String valueCode);
}
