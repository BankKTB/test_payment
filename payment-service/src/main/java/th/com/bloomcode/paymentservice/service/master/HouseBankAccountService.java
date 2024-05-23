package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.HouseBankAccountRepository;
import th.com.bloomcode.paymentservice.idem.entity.HouseBankAccount;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class HouseBankAccountService {

    @Autowired
    private HouseBankAccountRepository houseBankAccountRepository;

    public ResponseEntity<Result<List<HouseBankAccount>>> findByLikeAll(String key) {
        Result<List<HouseBankAccount>> result = new Result<>();

        result.setTimestamp(new Date());
        Long count = this.houseBankAccountRepository.countByKey(key);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<HouseBankAccount> list = this.houseBankAccountRepository.findByKey(key);
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

    public ResponseEntity<Result<HouseBankAccount>> findOne(String compCode, String valueCode, String accountCode) {
        Result<HouseBankAccount> result = new Result<>();

        result.setTimestamp(new Date());
        HouseBankAccount houseBankAccount = this.houseBankAccountRepository.findOne(compCode, valueCode, accountCode);
        if (null != houseBankAccount) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(houseBankAccount);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }

    public HouseBankAccount findOneByCompCodeAndHouseBankKeyAndAccountCode(String compCode, String valueCode, String accountCode) {
        return this.houseBankAccountRepository.findOne(compCode, valueCode, accountCode);

    }
}
