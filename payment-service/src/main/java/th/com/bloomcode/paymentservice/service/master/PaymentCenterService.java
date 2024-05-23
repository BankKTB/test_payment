package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.PaymentCenterRepository;
import th.com.bloomcode.paymentservice.idem.entity.PaymentCenter;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class PaymentCenterService {

    @Autowired
    private PaymentCenterRepository paymentCenterRepository;

    public ResponseEntity<Result<List<PaymentCenter>>> findByLikeAll(String key) {
        Result<List<PaymentCenter>> result = new Result<>();

        result.setTimestamp(new Date());
        Long count = this.paymentCenterRepository.countByKey(key);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<PaymentCenter> list = this.paymentCenterRepository.findByKey(key);
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

    public ResponseEntity<Result<PaymentCenter>> findOne(String valueCode) {
        Result<PaymentCenter> result = new Result<>();

        result.setTimestamp(new Date());
        PaymentCenter paymentCenter = this.paymentCenterRepository.findOne(valueCode);
        if (null != paymentCenter) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentCenter);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }
}
