package th.com.bloomcode.paymentservice.repository.idem;


import th.com.bloomcode.paymentservice.model.idem.BankAccountDetail;
import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;

import java.util.List;

public interface BankAccountDetailRepository {

    List<BankAccountDetail> findByCondition(String vendor,String value,String routingNo);


    Long countByCondition(String vendor,String value,String routingNo);
}
