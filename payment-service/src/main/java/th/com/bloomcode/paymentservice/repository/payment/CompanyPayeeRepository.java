package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayee;

public interface CompanyPayeeRepository  extends CrudRepository<CompanyPayee, Long> {
    CompanyPayee findOneByCompanyCode(String companyCode);
}
