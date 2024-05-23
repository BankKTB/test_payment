package th.com.bloomcode.paymentservice.service.idem;

import th.com.bloomcode.paymentservice.model.idem.PeriodControl;

import java.sql.Timestamp;
import java.util.List;

public interface PeriodControlService {

    List<PeriodControl> findAllByCondition(int period,String fiscalYear, String orgValue);

    List<PeriodControl> findAllByCondition(Timestamp date, String orgValue);

}
