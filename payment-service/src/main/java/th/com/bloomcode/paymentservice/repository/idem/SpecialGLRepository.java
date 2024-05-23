package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.SpecialGL;

import java.util.List;

public interface SpecialGLRepository extends CrudRepository<SpecialGL, Long> {
    Long countAllByValueCode(String valueCode);
    List<SpecialGL> findAllByValueCode(String valueCode);
    SpecialGL findOneByValueCode(String valueCode);
}
