package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.AreaRepository;
import th.com.bloomcode.paymentservice.idem.dao.FundSourceRepository;
import th.com.bloomcode.paymentservice.idem.entity.Area;
import th.com.bloomcode.paymentservice.idem.entity.FundSource;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class FundSourceService {

    @Autowired
    private FundSourceRepository fundSourceRepository;

    public ResponseEntity<Result<List<FundSource>>> findByValueCodeContaining(String year, String key) {
        Result<List<FundSource>> result = new Result<>();

        result.setTimestamp(new Date());
        Long count = this.fundSourceRepository.countByKey(year, key);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<FundSource> list = this.fundSourceRepository.findByKey(year, key);
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

    public ResponseEntity<Result<FundSource>> findOneByValueCodeAndIsActiveTrue(String year, String valueCode) {
        Result<FundSource> result = new Result<>();

        result.setTimestamp(new Date());
        FundSource fundSource = this.fundSourceRepository.findOne(year, valueCode);
        if (null != fundSource) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(fundSource);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }

    public FundSource findOneByYearAndValueCode(String year, String valueCode) {

        return this.fundSourceRepository.findOneForFundType(year, valueCode);

    }


}
