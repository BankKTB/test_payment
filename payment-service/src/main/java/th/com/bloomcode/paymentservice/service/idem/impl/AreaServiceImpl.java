package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.Area;
import th.com.bloomcode.paymentservice.repository.idem.AreaRepository;
import th.com.bloomcode.paymentservice.service.idem.AreaService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;

    @Autowired
    public AreaServiceImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Override
    public ResponseEntity<Result<List<Area>>> findAllByValue(String valueCode) {
        Result<List<Area>> result = new Result<>();
        result.setTimestamp(new Date());
        Long count = this.areaRepository.countAllByValueCode(valueCode);
        log.info("count Area : {} " , count);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<Area> list = this.areaRepository.findAllByValueCode(valueCode);
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
    public ResponseEntity<Result<Area>> findOneByValue(String valueCode) {
        Result<Area> result = new Result<>();
        result.setTimestamp(new Date());
        Area area = this.areaRepository.findOneByValueCode(valueCode);
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
    @Cacheable(value = "area", key = "{ #valueCode }", unless = "#result==null")
    public Area findOneByValueCode(String valueCode) {
        return this.areaRepository.findOneByValueCode(valueCode);
    }


}
