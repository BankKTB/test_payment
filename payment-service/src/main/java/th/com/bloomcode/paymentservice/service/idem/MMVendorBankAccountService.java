package th.com.bloomcode.paymentservice.service.idem;


import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;

import java.util.List;

public interface MMVendorBankAccountService {

  ResponseEntity<Result<List<VendorBankAccount>>> findByCondition(String request,String type,String paymentMethodType);


}
