package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.PeriodControl;

import java.sql.Timestamp;
import java.util.List;


public interface PeriodControlRepository extends CrudRepository<PeriodControl, Long> {
    List<PeriodControl> findAllByCondition(int period,String fiscalYear, String orgValue);

    List<PeriodControl> findAllByCondition(Timestamp date, String orgValue);
}
