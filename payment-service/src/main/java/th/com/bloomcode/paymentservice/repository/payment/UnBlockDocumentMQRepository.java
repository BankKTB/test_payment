package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentMQ;
import th.com.bloomcode.paymentservice.model.request.UnBlockChangeDocumentRequest;

import java.sql.Timestamp;
import java.util.List;

public interface UnBlockDocumentMQRepository extends CrudRepository<UnBlockDocumentMQ, Long> {

    List<UnBlockDocumentMQ> findMQLogByCondition(List<UnBlockChangeDocumentRequest> request);

    List<UnBlockDocumentMQ> findMQLogByCondition(String uuid);

    List<UnBlockDocumentMQ> findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear);

    void saveBatch(List<UnBlockDocumentMQ> UnBlockDocumentMQs);

    void updateBatch(List<UnBlockDocumentMQ> unBlockDocumentMQs);

    void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus,
                      String reverseOriginalDocumentNo, String reverseOriginalFiscalYear, String reverseDocumentType,
                      String reverseCompanyCode, String username, Timestamp updateDate);

    void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus,String newValue, String username, Timestamp updateDate);

}
