package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.HouseBankRepository;
import th.com.bloomcode.paymentservice.idem.entity.HouseBank;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class HouseBankService {

    @Autowired
    private HouseBankRepository houseBankRepository;

    public ResponseEntity<Result<List<HouseBank>>> findByLikeAll(String key) {
        Result<List<HouseBank>> result = new Result<>();

        result.setTimestamp(new Date());
        Long count = this.houseBankRepository.countByKey(key);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<HouseBank> list = this.houseBankRepository.findByKey(key);
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

    public ResponseEntity<Result<HouseBank>> findOne(String compCode, String valueCode, String bankBranch) {
        Result<HouseBank> result = new Result<>();

        result.setTimestamp(new Date());
        HouseBank houseBank = this.houseBankRepository.findOne(compCode, valueCode, bankBranch);
        if (null != houseBank) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(houseBank);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }
}
