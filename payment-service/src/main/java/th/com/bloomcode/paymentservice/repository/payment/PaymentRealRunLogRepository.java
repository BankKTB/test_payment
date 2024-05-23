package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.domain.Page;
import th.com.bloomcode.paymentservice.model.payment.PaymentRealRunLog;
import th.com.bloomcode.paymentservice.repository.MetadataRepository;

import java.util.List;

public interface PaymentRealRunLogRepository extends MetadataRepository<PaymentRealRunLog, Long> {


    Page<PaymentRealRunLog> findAllByPaymentAliasId(Long paymentAliasId, boolean success, int page, int size);

    void deletePaymentRealRunLog(Long paymentAliasId);
    void saveBatch(List<PaymentRealRunLog> paymentRealRunLogs);

    Long getSequenceByPaymentAliasId(Long paymentAliasId);

}
