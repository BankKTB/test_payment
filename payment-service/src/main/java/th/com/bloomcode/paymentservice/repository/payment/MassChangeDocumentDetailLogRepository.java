package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocumentDetailLog;

import java.util.List;

public interface MassChangeDocumentDetailLogRepository extends CrudRepository<MassChangeDocumentDetailLog, Long> {

    List<MassChangeDocumentDetailLog> findDetailByDocument(String companyCode, String documentNo, String fiscalYear);
}
