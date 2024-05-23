package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocumentDetailLog;

import java.util.List;

public interface ReverseDocumentDetailLogRepository extends CrudRepository<ReverseDocumentDetailLog, Long> {

    List<ReverseDocumentDetailLog> findDetailByDocument(String companyCode, String documentNo, String fiscalYear);
}
