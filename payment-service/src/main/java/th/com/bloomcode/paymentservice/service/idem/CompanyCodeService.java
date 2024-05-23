package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.CompanyCode;

import java.util.List;

public interface CompanyCodeService {
    ResponseEntity<Result<List<CompanyCode>>> findAllByValue(String valueCode);
    ResponseEntity<Result<CompanyCode>> findOneByValue(String valueCode);
    CompanyCode findOneByValueCode(String valueCode);
    CompanyCode findOneByOldValueCode(String valueCode);
}
