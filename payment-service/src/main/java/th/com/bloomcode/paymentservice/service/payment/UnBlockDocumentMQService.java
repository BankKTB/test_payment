package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentMQ;
import th.com.bloomcode.paymentservice.model.request.UnBlockChangeDocumentRequest;
import th.com.bloomcode.paymentservice.model.response.UnBlockDocumentMQResponse;

import java.sql.Timestamp;
import java.util.List;

public interface UnBlockDocumentMQService {


    UnBlockDocumentMQ save(UnBlockDocumentMQ unBlockDocumentMQ);

    UnBlockDocumentMQ findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear);


    ResponseEntity<Result<List<UnBlockDocumentMQResponse>>> findMQLogByCondition(List<UnBlockChangeDocumentRequest> request);

    ResponseEntity<Result<List<UnBlockDocumentMQResponse>>> findMQLogByCondition(String uuid);

    void saveBatch(List<UnBlockDocumentMQ> unBlockDocumentMQs);

    void updateBatch(List<UnBlockDocumentMQ> unBlockDocumentMQs);

    void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus,
                      String reverseOriginalDocumentNo, String reverseOriginalFiscalYear, String reverseDocumentType,
                      String reverseCompanyCode, String username, Timestamp updateDate);

    void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus,String newValue, String username, Timestamp updateDate);

}
