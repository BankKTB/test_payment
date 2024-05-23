package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.HouseBankAccount;
import th.com.bloomcode.paymentservice.repository.idem.HouseBankAccountRepository;
import th.com.bloomcode.paymentservice.service.idem.HouseBankAccountService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HouseBankAccountServiceImpl implements HouseBankAccountService {
    private final HouseBankAccountRepository houseBankAccountRepository;
    public HouseBankAccountServiceImpl(HouseBankAccountRepository houseBankAccountRepository) {
        this.houseBankAccountRepository = houseBankAccountRepository;
    }

    @Override
    public ResponseEntity<Result<List<HouseBankAccount>>> findAllByValueCode(String valueCode) {
        Result<List<HouseBankAccount>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.houseBankAccountRepository.countAllByValueCode(valueCode);
        log.info("count HouseBankAccount : {} " , count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<HouseBankAccount> list = this.houseBankAccountRepository.findAllByValueCode(valueCode);
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
    public ResponseEntity<Result<HouseBankAccount>> findOneByValueCode(String valueCode, String compCode, String accountCode) {
        Result<HouseBankAccount> result = new Result<>();
        result.setTimestamp(new Date());
        HouseBankAccount area = this.houseBankAccountRepository.findOneByValueCode(valueCode, compCode, accountCode);
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
}
