package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.IdemConfig;

import java.util.List;

public interface IdemConfigRepository extends CrudRepository<IdemConfig, Long> {
    IdemConfig findByValueClientStartingWithAndIsActiveTrue(String valueCode);
    List<IdemConfig> findAllByIsActiveTrue();
}
