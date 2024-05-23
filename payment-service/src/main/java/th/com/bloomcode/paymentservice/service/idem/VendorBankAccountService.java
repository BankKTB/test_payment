package th.com.bloomcode.paymentservice.service.idem;

import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;

public interface VendorBankAccountService {
    VendorBankAccount findOneByBankAccountNoAndVendor(String bankAccountNo, String vendor);
    VendorBankAccount findOneByVendor(String vendor);
}
