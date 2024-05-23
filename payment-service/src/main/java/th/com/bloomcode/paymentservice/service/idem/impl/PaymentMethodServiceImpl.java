package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.PaymentMethod;
import th.com.bloomcode.paymentservice.repository.idem.PaymentMethodRepository;
import th.com.bloomcode.paymentservice.service.idem.PaymentMethodService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }


    @Override
    public ResponseEntity<Result<List<PaymentMethod>>> findAllByValue(String valueCode) {
        Result<List<PaymentMethod>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.paymentMethodRepository.countAllByValueCode(valueCode);
        log.info("count PaymentMethod : {} " , count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<PaymentMethod> list = this.paymentMethodRepository.findAllByValueCode(valueCode);
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
    public ResponseEntity<Result<PaymentMethod>> findOneByValue(String valueCode) {
        Result<PaymentMethod> result = new Result<>();
        result.setTimestamp(new Date());
        PaymentMethod area = this.paymentMethodRepository.findOneByValueCode(valueCode);
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
    @Cacheable(value = "paymentMethod", key = "#valueCode", unless = "#result==null")
    public PaymentMethod findOneByValueCode(String valueCode) {
        return this.paymentMethodRepository.findOneByValueCode(valueCode);
    }
}
