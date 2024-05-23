package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPaying;

public interface CompanyPayingRepository  extends CrudRepository<CompanyPaying, Long> {
    CompanyPaying findOneByCompanyCode(String companyCode);
}
