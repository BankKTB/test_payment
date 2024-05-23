package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentAliasResponse;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface PaymentAliasRepository extends CrudRepository<PaymentAlias, Long> {

    PaymentAlias findOneById(Long id);
    List<PaymentAliasResponse> findByCondition(String value);
    List<PaymentAliasResponse> findCreateJobByCondition(String value);
    List<PaymentAliasResponse> findCreateJobByCondition(Date paymentDate, String paymentName);
    List<PaymentAliasResponse> findSearchJobByCondition(String value);
    PaymentAlias findOneByPaymentDateAndPaymentName(Timestamp paymentDate, String paymentName);

}
