package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.request.SearchUnBlockDocumentLogRequest;

import java.sql.Timestamp;
import java.util.List;

public interface UnBlockDocumentLogRepository extends CrudRepository<UnBlockDocumentLog, Long> {

    List<UnBlockDocumentLog> findLogByCondition(SearchUnBlockDocumentLogRequest searchUnBlockDocumentLogRequest);

    List<UnBlockDocumentLog> findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear);

    void saveBatch(List<UnBlockDocumentLog> unBlockDocumentLogs);

    void updateBatch(List<UnBlockDocumentLog> unBlockDocumentLogs);

    void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus , String newValue, String username, Timestamp updateDate);
}
