package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.SpecialGL;
import th.com.bloomcode.paymentservice.repository.idem.SpecialGLRepository;
import th.com.bloomcode.paymentservice.service.idem.SpecialGLService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SpecialGLServiceImpl implements SpecialGLService {
    private final SpecialGLRepository specialGLRepository;

    public SpecialGLServiceImpl(SpecialGLRepository specialGLRepository) {
        this.specialGLRepository = specialGLRepository;
    }

    @Override
    public ResponseEntity<Result<List<SpecialGL>>> findAllByValue(String valueCode) {
        Result<List<SpecialGL>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.specialGLRepository.countAllByValueCode(valueCode);
        log.info("count SpecialGL : {} " , count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<SpecialGL> list = this.specialGLRepository.findAllByValueCode(valueCode);
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
    public ResponseEntity<Result<SpecialGL>> findOneByValue(String valueCode) {
        Result<SpecialGL> result = new Result<>();
        result.setTimestamp(new Date());
        SpecialGL area = this.specialGLRepository.findOneByValueCode(valueCode);
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
