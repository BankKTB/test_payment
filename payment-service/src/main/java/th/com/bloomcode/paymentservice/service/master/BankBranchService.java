package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.BankBranchRepository;
import th.com.bloomcode.paymentservice.idem.entity.BankBranch;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class BankBranchService {

    @Autowired
    private BankBranchRepository bankBranchRepository;

    public ResponseEntity<Result<List<BankBranch>>> findByLikeAll(String key) {
        Result<List<BankBranch>> result = new Result<>();

        result.setTimestamp(new Date());
        Long count = this.bankBranchRepository.countByKey(key);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<BankBranch> list = this.bankBranchRepository.findByKey(key);
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

    public ResponseEntity<Result<BankBranch>> findOne(String valueCode) {
        Result<BankBranch> result = new Result<>();

        result.setTimestamp(new Date());
        BankBranch glAccount = this.bankBranchRepository.findOne(valueCode);
        if (null != glAccount) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(glAccount);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }
}
