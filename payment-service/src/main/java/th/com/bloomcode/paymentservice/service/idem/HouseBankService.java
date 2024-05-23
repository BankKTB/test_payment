package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.HouseBank;

import java.util.List;

public interface HouseBankService {
    ResponseEntity<Result<List<HouseBank>>> findAllByValueCode(String valueCode);
    ResponseEntity<Result<HouseBank>> findOneByValueCode(String valueCode, String compCode, String bankBranch);
    HouseBank findOneById(Long id);
}
