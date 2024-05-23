package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.HouseBankAccount;

import java.util.List;

public interface HouseBankAccountRepository extends CrudRepository<HouseBankAccount, Long> {
    Long countAllByValueCode(String valueCode);
    List<HouseBankAccount> findAllByValueCode(String valueCode);
    HouseBankAccount findOneByValueCode(String valueCode, String compCode, String accountCode);
}
