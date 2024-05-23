package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.GLAccount;

import java.util.List;

public interface GLAccountService {
    GLAccount findOneByDocTypeAndFundSource(String docType, String fundSource);
    List<GLAccount> findAll();
}
