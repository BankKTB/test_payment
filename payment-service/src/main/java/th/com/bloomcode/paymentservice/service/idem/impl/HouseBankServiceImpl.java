package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.HouseBank;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.repository.idem.HouseBankRepository;
import th.com.bloomcode.paymentservice.service.idem.HouseBankService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HouseBankServiceImpl implements HouseBankService {
    private final HouseBankRepository houseBankRepository;

    public HouseBankServiceImpl(HouseBankRepository houseBankRepository) {
        this.houseBankRepository = houseBankRepository;
    }

    @Override
    public ResponseEntity<Result<List<HouseBank>>> findAllByValueCode(String valueCode) {
        Result<List<HouseBank>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.houseBankRepository.countAllByValueCode(valueCode);
        log.info("count HouseBank : {} " , count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<HouseBank> list = this.houseBankRepository.findAllByValueCode(valueCode);
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
    public ResponseEntity<Result<HouseBank>> findOneByValueCode(String valueCode, String compCode, String bankBranch) {
        Result<HouseBank> result = new Result<>();
        result.setTimestamp(new Date());
        HouseBank area = this.houseBankRepository.findOneByValueCode(valueCode, compCode, bankBranch);
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
    public HouseBank findOneById(Long id) {
        return houseBankRepository.findById(id).orElse(null);
    }
}
