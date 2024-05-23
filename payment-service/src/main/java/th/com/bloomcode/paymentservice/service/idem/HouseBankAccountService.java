package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.HouseBankAccount;

import java.util.List;

public interface HouseBankAccountService {
    ResponseEntity<Result<List<HouseBankAccount>>> findAllByValueCode(String valueCode);
    ResponseEntity<Result<HouseBankAccount>> findOneByValueCode(String valueCode, String compCode, String accountCode);
}
