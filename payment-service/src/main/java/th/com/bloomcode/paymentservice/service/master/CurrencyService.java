package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.CurrencyRepository;
import th.com.bloomcode.paymentservice.idem.entity.Currency;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public ResponseEntity<Result<List<Currency>>> findByLikeAll(String key) {
        Result<List<Currency>> result = new Result<>();

        result.setTimestamp(new Date());
        Long count = this.currencyRepository.countByKey(key);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<Currency> list = this.currencyRepository.findByKey(key);
                result.setMessage("");
                result.setStatus(HttpStatus.OK.value());
                result.setData(list);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<Result<Currency>> findOne(String valueCode) {
        Result<Currency> result = new Result<>();

        result.setTimestamp(new Date());
        Currency currency = this.currencyRepository.findOne(valueCode);
        if (null != currency) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(currency);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }
}
