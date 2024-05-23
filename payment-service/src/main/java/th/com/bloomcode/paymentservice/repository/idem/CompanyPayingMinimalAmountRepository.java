package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingMinimalAmount;

import java.util.List;

public interface CompanyPayingMinimalAmountRepository extends CrudRepository<CompanyPayingMinimalAmount, Long> {
    Long countAllByValueCode(String valueCode);

    List<CompanyPayingMinimalAmount> findAllByValueCode(String valueCode);

    CompanyPayingMinimalAmount findOneByValueCode(String valueCode);
}
