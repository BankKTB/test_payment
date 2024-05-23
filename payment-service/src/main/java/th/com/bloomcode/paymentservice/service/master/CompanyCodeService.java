package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.CompCodeRepository;
import th.com.bloomcode.paymentservice.idem.entity.CompCode;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class CompanyCodeService {

    @Autowired
    private CompCodeRepository compCodeRepository;

    public ResponseEntity<Result<List<CompCode>>> findByValueCodeContaining(String key) {
        Result<List<CompCode>> result = new Result<>();

        result.setTimestamp(new Date());
        Long count = this.compCodeRepository.countByKey(key);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<CompCode> list = this.compCodeRepository.findByKey(key);
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

    public ResponseEntity<Result<CompCode>> findOne(String valueCode) {
        Result<CompCode> result = new Result<>();

        result.setTimestamp(new Date());
        CompCode compCode = findOneByValueCode(valueCode);
        if (null != compCode) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(compCode);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }

    public CompCode findOneByValueCode(String valueCode) {
        return this.compCodeRepository.findOne(valueCode);
    }

    public CompCode findOneByOldValueCode(String valueCode) {
        return this.compCodeRepository.findOneByOldValueCode(valueCode);
    }


    public List<CompCode> findAll(String key) {
        return this.compCodeRepository.findByKey(key);
    }
}
