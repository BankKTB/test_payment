package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.NonBusinessDay;

import java.util.List;

public interface NonBusinessDayService {
    ResponseEntity<Result<List<NonBusinessDay>>> findAll();
    ResponseEntity<Result<List<NonBusinessDay>>> findByDateAndRangeDay(String dateBegin, String rangeDay);
}
