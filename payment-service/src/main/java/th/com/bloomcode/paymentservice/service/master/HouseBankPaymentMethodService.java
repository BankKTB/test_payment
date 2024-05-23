//package th.com.bloomcode.paymentservice.service.master;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.idem.dao.HouseBankPaymentMethodRepository;
//import th.com.bloomcode.paymentservice.idem.entity.HouseBankPaymentMethod;
//import th.com.bloomcode.paymentservice.model.Result;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class HouseBankPaymentMethodService {
//
//    @Autowired
//    private HouseBankPaymentMethodRepository houseBankPaymentMethodRepository;
//
//    public ResponseEntity<Result<List<HouseBankPaymentMethod>>> findByLikeAll(String key) {
//        Result<List<HouseBankPaymentMethod>> result = new Result<>();
//
//        result.setTimestamp(new Date());
//        Long count = this.houseBankPaymentMethodRepository.countByKey(key);
//        if (count > 0) {
//            if (count > 500) {
//                result.setMessage("ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
//                result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
//                result.setData(null);
//                return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
//            } else {
//                List<HouseBankPaymentMethod> list = this.houseBankPaymentMethodRepository.findByKey(key);
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
//    public ResponseEntity<Result<HouseBankPaymentMethod>> findOne(String client, String houseBankKey, String paymentMethod) {
//        Result<HouseBankPaymentMethod> result = new Result<>();
//
//        result.setTimestamp(new Date());
//        HouseBankPaymentMethod houseBankPaymentMethod = this.houseBankPaymentMethodRepository.findOne(client, houseBankKey, paymentMethod);
//        if (null != houseBankPaymentMethod) {
//            result.setMessage("");
//            result.setStatus(HttpStatus.OK.value());
//            result.setData(houseBankPaymentMethod);
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
//    public HouseBankPaymentMethod findOneByHouseBankAndPaymentMethod(String client, String hounseBankKey, String paymentMethod) {
//        return this.houseBankPaymentMethodRepository.findOne(client, hounseBankKey, paymentMethod);
//    }
//
//    public List<HouseBankPaymentMethod> findAllByCompanyCodePay(String key) {
//        return this.houseBankPaymentMethodRepository.findByKey(key);
//    }
//
//}
//
