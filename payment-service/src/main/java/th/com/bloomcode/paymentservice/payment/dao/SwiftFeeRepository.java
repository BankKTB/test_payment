package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.SmartFee;
import th.com.bloomcode.paymentservice.payment.entity.SwiftFee;

import java.math.BigDecimal;
import java.util.List;

public interface SwiftFeeRepository extends CrudRepository<SwiftFee, Long> {

    boolean existsByBankKeyAndAndSmartIsTrue(String bankKey);
    List<SwiftFee> findBySmartIsTrue();

    List<SwiftFee> findAllByOrderByBankKeyAsc();

    SwiftFee findOneById(Long id);

    SwiftFee findOneByBankKey(String bankKey);


}
