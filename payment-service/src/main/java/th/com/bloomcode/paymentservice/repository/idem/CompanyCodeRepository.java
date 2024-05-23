package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.CompanyCode;

import java.util.List;

public interface CompanyCodeRepository extends CrudRepository<CompanyCode, Long> {
    Long countAllByValueCode(String valueCode);
    List<CompanyCode> findAllByValueCode(String valueCode);
    CompanyCode findOneByValueCode(String valueCode);
    CompanyCode findOneByOldValueCode(String valueCode);
}
