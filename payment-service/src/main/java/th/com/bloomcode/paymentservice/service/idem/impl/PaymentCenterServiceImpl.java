package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.PaymentCenter;
import th.com.bloomcode.paymentservice.repository.idem.PaymentCenterRepository;
import th.com.bloomcode.paymentservice.service.idem.PaymentCenterService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PaymentCenterServiceImpl implements PaymentCenterService {
    private final PaymentCenterRepository paymentCenterRepository;

    public PaymentCenterServiceImpl(PaymentCenterRepository paymentCenterRepository) {
        this.paymentCenterRepository = paymentCenterRepository;
    }

    @Override
    public ResponseEntity<Result<List<PaymentCenter>>> findAllByValue(String valueCode) {
        Result<List<PaymentCenter>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.paymentCenterRepository.countAllByValueCode(valueCode);
        log.info("count PaymentCenter : {} " , count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<PaymentCenter> list = this.paymentCenterRepository.findAllByValueCode(valueCode);
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

    @Override
    public ResponseEntity<Result<PaymentCenter>> findOneByValue(String valueCode) {
        Result<PaymentCenter> result = new Result<>();
        result.setTimestamp(new Date());
        PaymentCenter area = this.paymentCenterRepository.findOneByValueCode(valueCode);
        if (null != area) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(area);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }

    @Override
    @Cacheable(value = "paymentCenter", key = "{ #valueCode }", unless = "#result==null")
    public PaymentCenter findOneByValueCode(String valueCode) {
        return this.paymentCenterRepository.findOneByValueCode(valueCode);
    }
}
