//package th.com.bloomcode.paymentservice.service.master;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.idem.dao.VendorRepository;
//import th.com.bloomcode.paymentservice.idem.entity.Vendor;
//import th.com.bloomcode.paymentservice.model.Result;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class VendorService {
//
//    @Autowired
//    private VendorRepository vendorRepository;
//
//    public List<Vendor> findAll() {
//        return this.vendorRepository.findAll();
//    }
//
//    public ResponseEntity<Result<List<Vendor>>> findByLikeAll(String key) {
//        Result<List<Vendor>> result = new Result<>();
//
//        result.setTimestamp(new Date());
//        Long count = this.vendorRepository.countByKey(key);
//        if (count > 0) {
//            if (count > 500) {
//                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
//                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
//                result.setData(null);
//                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
//            } else {
//                List<Vendor> list = this.vendorRepository.findByKey(key);
//                result.setMessage("");
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(list);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            }
//        } else {
//            result.setMessage("ไม่พบข้อมูล");
//            result.setStatus(HttpStatus.NOT_FOUND.value());
//            result.setData(null);
//            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//        }
//
//    }
//
//    public ResponseEntity<Result<Vendor>> findOne(String valueCode) {
//        Result<Vendor> result = new Result<>();
//
//        result.setTimestamp(new Date());
//        Vendor glAccount = this.vendorRepository.findOne(valueCode);
//        if (null != glAccount) {
//            result.setMessage("");
//            result.setStatus(HttpStatus.OK.value());
//            result.setData(glAccount);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } else {
//            result.setMessage("ไม่พบข้อมูล");
//            result.setStatus(HttpStatus.NOT_FOUND.value());
//            result.setData(null);
//            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//        }
//
//    }
//
//    public Vendor findOneByValueCode(String valueCode) {
//        return this.vendorRepository.findOne(valueCode);
//    }
//
//    public ResponseEntity<Result<List<Vendor>>> findByLikeAllAlternativeVendor(String compCode, String vendorTaxId, String key) {
//        Result<List<Vendor>> result = new Result<>();
//
//        result.setTimestamp(new Date());
//        Long count = this.vendorRepository.countByKeyAlternative(compCode, vendorTaxId, key);
//        if (count > 0) {
//            if (count > 500) {
//                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
//                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
//                result.setData(null);
//                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
//            } else {
//                List<Vendor> list = this.vendorRepository.findByKeyAlternative(compCode, vendorTaxId, key);
//                result.setMessage("");
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(list);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            }
//        } else {
//            result.setMessage("ไม่พบข้อมูล");
//            result.setStatus(HttpStatus.NOT_FOUND.value());
//            result.setData(null);
//            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//        }
//
//    }
//
//    public List<Vendor> findAllAlternativeVendor() {
//        return this.vendorRepository.findByKeyAlternative("", "", "");
//    }
//
//    public ResponseEntity<Result<Vendor>> findOneAlternativeVendor(String compCode, String vendorTaxId, String valueCode) {
//        Result<Vendor> result = new Result<>();
//
//        result.setTimestamp(new Date());
//        Vendor glAccount = this.vendorRepository.findOneAlternative(compCode, vendorTaxId, valueCode);
//        if (null != glAccount) {
//            result.setMessage("");
//            result.setStatus(HttpStatus.OK.value());
//            result.setData(glAccount);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } else {
//            result.setMessage("ไม่พบข้อมูล");
//            result.setStatus(HttpStatus.NOT_FOUND.value());
//            result.setData(null);
//            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//        }
//
//    }
//}
