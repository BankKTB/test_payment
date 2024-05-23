package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.BankBranch;
import th.com.bloomcode.paymentservice.repository.idem.BankBranchRepository;
import th.com.bloomcode.paymentservice.service.idem.BankBranchService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BankBranchServiceImpl implements BankBranchService {
    private final BankBranchRepository bankBranchRepository;
    public BankBranchServiceImpl(BankBranchRepository bankBranchRepository) {
        this.bankBranchRepository = bankBranchRepository;
    }

    @Override
    public ResponseEntity<Result<List<BankBranch>>> findAllByValue(String valueCode) {
        Result<List<BankBranch>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.bankBranchRepository.countAllByValue(valueCode);
        log.info("count BankBranch : {} " , count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<BankBranch> list = this.bankBranchRepository.findAllByValue(valueCode);
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
    public ResponseEntity<Result<BankBranch>> findOneByValue(String valueCode) {
        Result<BankBranch> result = new Result<>();
        result.setTimestamp(new Date());
        BankBranch area = this.bankBranchRepository.findOneByValue(valueCode);
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
