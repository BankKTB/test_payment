package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;

public interface VendorBankAccountRepository extends CrudRepository<VendorBankAccount, Long> {
    VendorBankAccount findOneByBankAccountNoAndVendor(String bankAccountNo, String vendor);
}
