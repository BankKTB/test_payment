package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;
import th.com.bloomcode.paymentservice.repository.idem.MMVendorBankAccountRepository;
import th.com.bloomcode.paymentservice.service.idem.MMVendorBankAccountService;


import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MMVendorBankAccountServicelmpl implements MMVendorBankAccountService {

  private final MMVendorBankAccountRepository mmVendorBankAccountRepository;

  public MMVendorBankAccountServicelmpl(
      MMVendorBankAccountRepository mmVendorBankAccountRepository) {
    this.mmVendorBankAccountRepository = mmVendorBankAccountRepository;
  }

  @Override
  public ResponseEntity<Result<List<VendorBankAccount>>> findByCondition(String request,String type,String paymentMethodType) {
    Result<List<VendorBankAccount>> result = new Result<>();

    result.setTimestamp(new Date());
    Long count = this.mmVendorBankAccountRepository.countByCondition(request,type,paymentMethodType);
    if (count > 0) {
//      if (count > 500) {
//        result.setMessage(
//            "ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่");
//        result.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
//        result.setData(null);
//        return new ResponseEntity<>(result, HttpStatus.PAYLOAD_TOO_LARGE);
//      } else {
        List<VendorBankAccount> list =
            this.mmVendorBankAccountRepository.findByCondition(request,type,paymentMethodType);
        result.setMessage("");
        result.setStatus(HttpStatus.OK.value());
        result.setData(list);
        return new ResponseEntity<>(result, HttpStatus.OK);
//      }
    } else {
      result.setMessage("ไม่พบข้อมูล");
      result.setStatus(HttpStatus.NOT_FOUND.value());
      result.setData(null);
      return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
  }


}
