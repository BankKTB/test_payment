package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.Currency;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    Long countAllByValueCode(String valueCode);
    List<Currency> findAllByValueCode(String valueCode);
    Currency findOneByValueCode(String valueCode);
}
