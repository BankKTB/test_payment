package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.NonBusinessDay;

import java.util.List;

public interface NonBusinessDayRepository extends CrudRepository<NonBusinessDay, Long> {
    List<NonBusinessDay> findAll();
    List<NonBusinessDay>  findByDateAndRangeDay(String dateBegin, String rangeDay);
}
