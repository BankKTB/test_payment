package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.Country;

import java.util.List;

public interface CountryService {
    ResponseEntity<Result<List<Country>>> findAllByValueCode(String valueCode);
    ResponseEntity<Result<Country>> findOneByValueCode(String valueCode);
}
