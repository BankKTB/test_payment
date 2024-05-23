package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.BankAccountDetail;
import th.com.bloomcode.paymentservice.repository.idem.BankAccountDetailRepository;
import th.com.bloomcode.paymentservice.service.idem.BankAccountDetailService;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BankAccountDetailServicelmpl implements BankAccountDetailService {

    private final BankAccountDetailRepository bankAccountDetailRepository;

    public BankAccountDetailServicelmpl(
            BankAccountDetailRepository bankAccountDetailRepository) {
        this.bankAccountDetailRepository = bankAccountDetailRepository;
    }

    @Override
    public ResponseEntity<Result<List<BankAccountDetail>>> findByCondition(String vendor,String value,String routingNo) {
        Result<List<BankAccountDetail>> result = new Result<>();

        result.setTimestamp(new Date());
        Long count = this.bankAccountDetailRepository.countByCondition(vendor,value,routingNo);
        if (count > 0) {
            if (count > 500) {
                result.setMessage(
                        "ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<BankAccountDetail> list =
                        this.bankAccountDetailRepository.findByCondition(vendor,value,routingNo);
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
