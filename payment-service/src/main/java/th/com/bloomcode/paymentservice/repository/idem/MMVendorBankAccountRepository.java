package th.com.bloomcode.paymentservice.repository.idem;


import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;

import java.util.List;

public interface MMVendorBankAccountRepository {

  List<VendorBankAccount> findByCondition(String request,String type,String paymentMethodType);



  Long countByCondition(String request,String type,String paymentMethodType);
}
