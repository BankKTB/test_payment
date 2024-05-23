package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.Vendor;
import th.com.bloomcode.paymentservice.repository.idem.VendorRepository;
import th.com.bloomcode.paymentservice.service.idem.VendorService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public ResponseEntity<Result<List<Vendor>>> findAllByValue(String valueCode) {
        Result<List<Vendor>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.vendorRepository.countAllByValueCode(valueCode);
        log.info("count Vendor : {} ", count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage(
                        "ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<Vendor> list = this.vendorRepository.findAllByValueCode(valueCode);
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
    public ResponseEntity<Result<Vendor>> findOneByValueCode2(String valueCode) {
        Result<Vendor> result = new Result<>();
        result.setTimestamp(new Date());
        Vendor area = this.vendorRepository.findOneByValueCode(valueCode);
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
    public Vendor findOneByValueCode(String valueCode) {
        return this.vendorRepository.findOneByValueCode(valueCode);
    }

    @Override
    @Cacheable(value = "vendor", key = "{ #valueCode, #compCode }", unless = "#result==null")
    public Vendor findOneByValueCodeAndCompCode(String valueCode, String compCode) {
        return this.vendorRepository.findOneByValueCodeAndCompCode(valueCode, compCode);
    }

    @Override
    @Cacheable(value = "vendorStatus", key = "{ #valueCode }", unless = "#result==null")
    public Vendor findOneByVendorCodeForStatus(String valueCode) {
        return this.vendorRepository.findOneByValueCodeForStatus(valueCode);
    }

    @Override
    @Cacheable(value = "payee", key = "{ #companyCode, #vendorCode,#alternativePayeeCode }", unless = "#result==null")
    public Vendor findOneAlternativePayee(String companyCode, String vendorCode, String alternativePayeeCode) {
        return vendorRepository.findOneAlternativePayee(companyCode, vendorCode, alternativePayeeCode);
    }

    @Override
    public Vendor findOne(String valueCode) {
        return vendorRepository.findOne(valueCode);
    }
}
