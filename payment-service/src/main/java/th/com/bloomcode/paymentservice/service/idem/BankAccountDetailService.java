package th.com.bloomcode.paymentservice.service.idem;


import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.BankAccountDetail;
import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;

import java.util.List;

public interface BankAccountDetailService {

  ResponseEntity<Result<List<BankAccountDetail>>> findByCondition(String vendorCode,String value,String routingNo);


}
