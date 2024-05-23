package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.payment.entity.PaymentBlockLog;

@Repository
public interface PaymentBlockLogRepository extends CrudRepository<PaymentBlockLog, Long> {


    PaymentBlockLog findOneByCompCodeAndAccDocNoAndFiscalYear(String compCode,String accDocNo,String fiscalYear);


}
