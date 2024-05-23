package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.Area;

import java.util.List;


public interface AreaRepository extends CrudRepository<Area, Long> {
    Long countAllByValueCode(String valueCode);
    List<Area> findAllByValueCode(String valueCode);
    Area findOneByValueCode(String valueCode);
}
