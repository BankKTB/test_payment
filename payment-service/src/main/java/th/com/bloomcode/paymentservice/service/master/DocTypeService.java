package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.DocTypeRepository;
import th.com.bloomcode.paymentservice.idem.entity.DocType;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class DocTypeService {

    @Autowired
    private DocTypeRepository docTypeRepository;

    public ResponseEntity<Result<List<DocType>>> findByLikeAll(String key) {
        Result<List<DocType>> result = new Result<>();

        result.setTimestamp(new Date());
        Long count = this.docTypeRepository.countByKey(key);
        if (count > 0) {
            if (count > 500) {
                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
            } else {
                List<DocType> list = this.docTypeRepository.findByKey(key);
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

    public ResponseEntity<Result<DocType>> findOne(String valueCode) {
        Result<DocType> result = new Result<>();

        result.setTimestamp(new Date());
        DocType docType = this.docTypeRepository.findOne(valueCode);
        if (null != docType) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(docType);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบข้อมูล");
            result.setStatus(HttpStatus.NOT_FOUND.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

    }
}
