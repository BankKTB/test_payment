package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayeeHouseBankKeyConfig;

import java.util.List;

public interface CompanyPayeeHouseBankKeyConfigRepository extends CrudRepository<CompanyPayeeHouseBankKeyConfig, Long> {
    List<CompanyPayeeHouseBankKeyConfig> findAllByCompanyPayeeIdOrderByHouseBankKeyAscCountryAscBankBranchAsc(Long companyPayeeId);
    List<CompanyPayeeHouseBankKeyConfig> findAllByHouseBankKeyAndCountryAndBankBranchAndCompanyPayeeId(String houseBankKey, String country,
                                                                                                       String bankBranch, Long companyPayeeId);
    CompanyPayeeHouseBankKeyConfig findOneByHouseBankKeyAndCountryAndBankBranchAndCompanyPayeeId(String houseBankKey, String country,
                                                                                                       String bankBranch, Long companyPayeeId);
}
