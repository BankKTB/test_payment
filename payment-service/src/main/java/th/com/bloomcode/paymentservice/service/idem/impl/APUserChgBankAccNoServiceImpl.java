package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.APUserChgBankAccNo;
import th.com.bloomcode.paymentservice.model.request.CheckAPUserBankAccNoRequest;
import th.com.bloomcode.paymentservice.repository.idem.APUserChgBankAccNoRepository;
import th.com.bloomcode.paymentservice.service.idem.APUserChgBankAccNoService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class APUserChgBankAccNoServiceImpl implements APUserChgBankAccNoService {
    private final APUserChgBankAccNoRepository apUserChgBankAccNoRepository;

    @Autowired
    public APUserChgBankAccNoServiceImpl(APUserChgBankAccNoRepository apUserChgBankAccNoRepository) {
        this.apUserChgBankAccNoRepository = apUserChgBankAccNoRepository;
    }


    @Override
    public ResponseEntity<Result<List<APUserChgBankAccNo>>> findAllByValue(CheckAPUserBankAccNoRequest checkAPUserBankAccNoRequest) {
        Result<List<APUserChgBankAccNo>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.apUserChgBankAccNoRepository.countAllByValueCode(checkAPUserBankAccNoRequest);
        log.info("count APUserChgBankAccNo : {} ", count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<APUserChgBankAccNo> list = this.apUserChgBankAccNoRepository.findAllByValueCode(checkAPUserBankAccNoRequest);
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
}
