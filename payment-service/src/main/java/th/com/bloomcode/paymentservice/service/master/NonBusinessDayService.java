package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.HouseBankAccountRepository;
import th.com.bloomcode.paymentservice.idem.dao.NonBusinessDayRepository;
import th.com.bloomcode.paymentservice.idem.entity.HouseBank;
import th.com.bloomcode.paymentservice.idem.entity.HouseBankAccount;
import th.com.bloomcode.paymentservice.idem.entity.NonBusinessDay;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class NonBusinessDayService {
    @Autowired
    private NonBusinessDayRepository nonBusinessDayRepository;

    public ResponseEntity<Result<List<NonBusinessDay>>> findByDateAndRangeDay(String dateBegin, String rangeDay) {
        Result<List<NonBusinessDay>> result = new Result<>();
        result.setTimestamp(new Date());
        List<NonBusinessDay> list = this.nonBusinessDayRepository.findByDateAndRangeDay(dateBegin, rangeDay);
        result.setMessage("");
        result.setStatus(HttpStatus.OK.value());
        result.setData(list);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    public ResponseEntity<Result<List<NonBusinessDay>>> getAll() {
        Result<List<NonBusinessDay>> result = new Result<>();
        result.setTimestamp(new Date());
        List<NonBusinessDay> list = this.nonBusinessDayRepository.getAll();
        result.setMessage("");
        result.setStatus(HttpStatus.OK.value());
        result.setData(list);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
