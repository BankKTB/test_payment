package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.APUserChgBankAccNo;
import th.com.bloomcode.paymentservice.model.payment.PayMethodConfig;
import th.com.bloomcode.paymentservice.model.request.CheckAPUserBankAccNoRequest;

import java.util.List;


public interface PayMethodForPaymentRepository extends CrudRepository<PayMethodConfig, Long> {
    List<PayMethodConfig> findAllOrderByCountryAscPayMethodAsc();
}
