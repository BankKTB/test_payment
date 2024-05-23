package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;
import th.com.bloomcode.paymentservice.repository.idem.VendorBankAccountRepository;
import th.com.bloomcode.paymentservice.service.idem.VendorBankAccountService;

@Slf4j
@Service
public class VendorBankAccountServiceImpl implements VendorBankAccountService {
  private final VendorBankAccountRepository vendorBankAccountRepository;

  public VendorBankAccountServiceImpl(VendorBankAccountRepository vendorBankAccountRepository) {
    this.vendorBankAccountRepository = vendorBankAccountRepository;
  }

  @Override
  @Cacheable(value = "vendorBankAccount", key = "{ #bankAccountNo, #vendor }", unless = "#result==null")
  public VendorBankAccount findOneByBankAccountNoAndVendor(String bankAccountNo, String vendor) {
      return this.vendorBankAccountRepository.findOneByBankAccountNoAndVendor(bankAccountNo, vendor);
  }

  @Override
  @Cacheable(value = "vendorBankAccountTop", key = "{ #vendor }", unless = "#result==null")
  public VendorBankAccount findOneByVendor(String vendor) {
    return this.vendorBankAccountRepository.findOneByBankAccountNoAndVendor("", vendor);
  }
}
