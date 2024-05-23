package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.ReturnReason;

public interface ReturnReasonRepository extends CrudRepository<ReturnReason, Long> {

    ReturnReason findOneByReasonNo(String reasonNo);
}
