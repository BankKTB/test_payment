package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.payment.UnblockDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.request.UnblockDocumentLogDetailRequest;

import java.util.List;

public interface UnblockDocumentDetailLogRepository extends CrudRepository<UnblockDocumentDetailLog, Long> {

    List<UnblockDocumentDetailLog> findDetailByDocument(String companyCode, String documentNo, String fiscalYear);

    List<UnblockDocumentDetailLog> findLogDetail(UnblockDocumentLogDetailRequest request);
}
