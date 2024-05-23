package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.PaymentMethodRepository;
import th.com.bloomcode.paymentservice.idem.entity.PaymentMethod;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public ResponseEntity<Result<List<PaymentMethod>>> findByLikeAll(String key) {
        Result<List<PaymentMethod>> result = new Result<>();

        result.setTimestamp(new Date());
        Long count = this.paymentMethodRepository.countByKey(key);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<PaymentMethod> list = this.paymentMethodRepository.findByKey(key);
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

    public ResponseEntity<Result<PaymentMethod>> findOne(String valueCode) {
        Result<PaymentMethod> result = new Result<>();

        result.setTimestamp(new Date());
        PaymentMethod paymentMethod = this.paymentMethodRepository.findOne(valueCode);
        if (null != paymentMethod) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentMethod);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }

    public List<PaymentMethod> findAll(String key) {
        return this.paymentMethodRepository.findByKey(key);
    }
}
