package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.BankBranch;

import java.util.List;

public interface BankBranchRepository extends CrudRepository<BankBranch, Long> {
    Long countAllByValue(String valueCode);
    List<BankBranch> findAllByValue(String valueCode);
    BankBranch findOneByValue(String valueCode);
}
