package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.APUserChgBankAccNo;
import th.com.bloomcode.paymentservice.model.idem.HouseBankPaymentMethod;
import th.com.bloomcode.paymentservice.model.request.CheckAPUserBankAccNoRequest;

import java.util.List;

public interface APUserChgBankAccNoRepository extends CrudRepository<APUserChgBankAccNo, Long> {
    Long countAllByValueCode(CheckAPUserBankAccNoRequest checkAPUserBankAccNoRequest);

    List<APUserChgBankAccNo> findAllByValueCode(CheckAPUserBankAccNoRequest checkAPUserBankAccNoRequest);
}
