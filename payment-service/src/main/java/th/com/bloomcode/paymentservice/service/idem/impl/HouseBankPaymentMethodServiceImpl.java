package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.HouseBankPaymentMethod;
import th.com.bloomcode.paymentservice.repository.idem.HouseBankPaymentMethodRepository;
import th.com.bloomcode.paymentservice.service.idem.HouseBankPaymentMethodService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HouseBankPaymentMethodServiceImpl implements HouseBankPaymentMethodService {
    private final HouseBankPaymentMethodRepository houseBankPaymentMethodRepository;

    public HouseBankPaymentMethodServiceImpl(HouseBankPaymentMethodRepository houseBankPaymentMethodRepository) {
        this.houseBankPaymentMethodRepository = houseBankPaymentMethodRepository;
    }

    @Override
    public ResponseEntity<Result<List<HouseBankPaymentMethod>>> findAllByValueCode(String valueCode) {
        Result<List<HouseBankPaymentMethod>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.houseBankPaymentMethodRepository.countAllByValueCode(valueCode);
        log.info("count HouseBankPaymentMethod : {} " , count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<HouseBankPaymentMethod> list = this.houseBankPaymentMethodRepository.findAllByValueCode(valueCode);
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
    public ResponseEntity<Result<HouseBankPaymentMethod>> findOneByValueCode(String client, String houseBankKey, String paymentMethod) {
        Result<HouseBankPaymentMethod> result = new Result<>();
        result.setTimestamp(new Date());
        HouseBankPaymentMethod area = this.houseBankPaymentMethodRepository.findOneByValueCode(client, houseBankKey, paymentMethod);
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
    public List<HouseBankPaymentMethod> findAllByCompanyCodePay(String compCode) {
        return this.houseBankPaymentMethodRepository.findAllByValueCode(compCode);
    }
}
