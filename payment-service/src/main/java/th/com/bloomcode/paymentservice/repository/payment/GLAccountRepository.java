package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.GLAccount;

public interface GLAccountRepository extends CrudRepository<GLAccount, Long> {
    GLAccount findOneByDocTypeAndFundSource(String docType, String fundSource);
}
