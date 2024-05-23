package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.VendorBankAccountRepository;
import th.com.bloomcode.paymentservice.idem.entity.VendorBankAccount;

import java.util.List;

@Service
public class VendorBankAccountService {

    @Autowired
    private VendorBankAccountRepository vendorBankAccountRepository;

    public List<VendorBankAccount> findAll() {
        return this.vendorBankAccountRepository.findAll();
    }

    public VendorBankAccount findOneByAccountNo(String accountNo) {
        return this.vendorBankAccountRepository.findOne(accountNo);
    }
}
