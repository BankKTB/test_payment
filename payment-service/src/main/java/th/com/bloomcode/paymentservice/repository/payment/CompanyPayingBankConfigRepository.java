package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingBankConfig;

import java.util.List;

public interface CompanyPayingBankConfigRepository extends CrudRepository<CompanyPayingBankConfig, Long> {
    CompanyPayingBankConfig findOneByPayMethodAndCompanyPayingId(String payMethod, Long CompanyPayingId);
    List<CompanyPayingBankConfig> findAllByCompanyPayingIdOrderByPayMethodAsc(Long companyPayingId);
}
