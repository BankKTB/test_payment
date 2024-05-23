package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.PayMethodCurrencyConfig;

import java.util.List;

public interface PayMethodCurrencyConfigRepository extends CrudRepository<PayMethodCurrencyConfig, Long> {

    PayMethodCurrencyConfig findOneById(Long id);

    PayMethodCurrencyConfig findOneByIdAndPayMethodConfigId(Long id, Long payMethodConfigId);

    PayMethodCurrencyConfig findOneByPayMethodConfigIdAndCurrency(Long payMethodConfigId, String currency);

    List<PayMethodCurrencyConfig> findAllByPayMethodConfigId(Long payMethodConfigId);

//	void deleteByPayMethodConfigId(Long payMethodConfigId);

}
