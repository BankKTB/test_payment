package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingPayMethodConfig;

import java.util.List;

public interface
CompanyPayingPayMethodConfigRepository  extends CrudRepository<CompanyPayingPayMethodConfig, Long> {
    List<CompanyPayingPayMethodConfig> findAllByCompanyPayingIdOrderByPayMethodAsc(Long companyPayingId);
    CompanyPayingPayMethodConfig findOneByCompanyPayingIdAndPayMethod(Long companyPayingId, String payMethod);
}
