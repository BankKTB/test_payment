package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocument;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;

import java.util.List;

public interface ReverseDocumentService {

    ReverseDocument findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear);

    void save(ReverseDocument reverseDocument);

    ResponseEntity<Result<List<ReverseDocument>>> findByCondition(String value);

    List<ReverseDocument> findByListDocument(List<ReverseDocumentRequest> request);

    List<ReverseDocument> findByListDocument(String uuid);
    void saveBatch(List<ReverseDocument> reverseDocuments);

}
