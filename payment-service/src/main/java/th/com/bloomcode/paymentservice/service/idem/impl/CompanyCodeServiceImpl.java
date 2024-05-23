package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.CompanyCode;
import th.com.bloomcode.paymentservice.repository.idem.CompanyCodeRepository;
import th.com.bloomcode.paymentservice.service.idem.CompanyCodeService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CompanyCodeServiceImpl implements CompanyCodeService {
    private final CompanyCodeRepository companyCodeRepository;

    public CompanyCodeServiceImpl(CompanyCodeRepository companyCodeRepository) {
        this.companyCodeRepository = companyCodeRepository;
    }

    @Override
    public ResponseEntity<Result<List<CompanyCode>>> findAllByValue(String valueCode) {
        Result<List<CompanyCode>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.companyCodeRepository.countAllByValueCode(valueCode);
        log.info("count CompanyCode : {} " , count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<CompanyCode> list = this.companyCodeRepository.findAllByValueCode(valueCode);
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
    public ResponseEntity<Result<CompanyCode>> findOneByValue(String valueCode) {
        Result<CompanyCode> result = new Result<>();
        result.setTimestamp(new Date());
        CompanyCode area = this.companyCodeRepository.findOneByValueCode(valueCode);
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
    @Cacheable(value = "companyCode", key = "{ #valueCode }", unless = "#result==null")
    public CompanyCode findOneByValueCode(String valueCode) {
        return this.companyCodeRepository.findOneByValueCode(valueCode);
    }

    @Override
    @Cacheable(value = "oldCompanyCode", key = "{ #valueCode }", unless = "#result==null")
    public CompanyCode findOneByOldValueCode(String valueCode) {
        return this.companyCodeRepository.findOneByValueCode(valueCode);
    }
}
