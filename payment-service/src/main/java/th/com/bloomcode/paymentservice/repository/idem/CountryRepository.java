package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.Country;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Long> {
    Long countAllByValueCode(String valueCode);
    List<Country> findAllByValueCode(String valueCode);
    Country findOneByValueCode(String valueCode);
}