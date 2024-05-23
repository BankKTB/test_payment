package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.HouseBank;

import java.util.List;

public interface HouseBankRepository extends CrudRepository<HouseBank, Long> {
    Long countAllByValueCode(String valueCode);
    List<HouseBank> findAllByValueCode(String valueCode);
    HouseBank findOneByValueCode(String valueCode, String compCode, String bankBranch);
}
