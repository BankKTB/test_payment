package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.BankBranch;

import java.util.List;

public interface BankBranchService {
    ResponseEntity<Result<List<BankBranch>>> findAllByValue(String valueCode);
    ResponseEntity<Result<BankBranch>> findOneByValue(String valueCode);
}
