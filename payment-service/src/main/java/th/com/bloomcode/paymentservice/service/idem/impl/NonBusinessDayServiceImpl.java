package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.NonBusinessDay;
import th.com.bloomcode.paymentservice.repository.idem.NonBusinessDayRepository;
import th.com.bloomcode.paymentservice.service.idem.NonBusinessDayService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class NonBusinessDayServiceImpl implements NonBusinessDayService {
    private final NonBusinessDayRepository nonBusinessDayRepository;

    public NonBusinessDayServiceImpl(NonBusinessDayRepository nonBusinessDayRepository) {
        this.nonBusinessDayRepository = nonBusinessDayRepository;
    }

    @Override
    public ResponseEntity<Result<List<NonBusinessDay>>> findAll() {
        Result<List<NonBusinessDay>> result = new Result<>();
        result.setTimestamp(new Date());
        List<NonBusinessDay> list = this.nonBusinessDayRepository.findAll();
        log.info("count NonBusinessDay : {} " , list.size());
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(list);
            return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Result<List<NonBusinessDay>>> findByDateAndRangeDay(String dateBegin, String rangeDay) {
        Result<List<NonBusinessDay>> result = new Result<>();
        result.setTimestamp(new Date());
        List<NonBusinessDay> listByDateAndRangeDay = this.nonBusinessDayRepository.findByDateAndRangeDay(dateBegin, rangeDay);
        log.info("count NonBusinessDay findByDateAndRangeDay : {} " , listByDateAndRangeDay.size());
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(listByDateAndRangeDay);
            return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
