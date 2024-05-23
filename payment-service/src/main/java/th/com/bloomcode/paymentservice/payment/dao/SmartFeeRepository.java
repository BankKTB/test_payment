package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.SmartFee;
import th.com.bloomcode.paymentservice.payment.entity.SumFileCondition;

import java.math.BigDecimal;
import java.util.List;

public interface SmartFeeRepository extends CrudRepository<SmartFee, Long> {
    List<SmartFee> findByOrderById();

    List<SmartFee> findAllByOrderByGlAccountAscAmountMinAsc();

    SmartFee findOneById(Long id);

    SmartFee findOneByGlAccountAndAmountMin(String glAccount,BigDecimal amountMin);

    SmartFee findOneByGlAccountAndAmountMax(String glAccount,BigDecimal amountMax);



    @Query("SELECT s FROM SmartFee s WHERE s.glAccount =:glAccount and (" + "  s.amountMin <= :amountMin or s.amountMax <= :amountMax) and (" + "  s.amountMin >= :amountMax or s.amountMax >= :amountMin) or   s.amountMax = :amountMin or s.amountMin = :amountMax   ")
    List<SmartFee> checkGlAccountAmountMinAndAmountMax(String glAccount, BigDecimal amountMin, BigDecimal amountMax);
}
