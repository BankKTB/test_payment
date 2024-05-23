package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocument;
import th.com.bloomcode.paymentservice.model.request.MassChangeDocumentRequest;

import java.sql.Timestamp;
import java.util.List;

public interface MassChangeDocumentService {

    MassChangeDocument findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear);

    void save(MassChangeDocument reverseDocument);

    ResponseEntity<Result<List<MassChangeDocument>>> findByCondition(String value);

    List<MassChangeDocument> findByListDocument(List<MassChangeDocumentRequest> request);

    List<MassChangeDocument> findByListDocument(String uuid);

    void updateStatus(String companyCode, String documentNo, String fiscalYear, String status, String username, Timestamp updateDate);

    void saveBatch(List<MassChangeDocument> massChangeDocuments);

    void updateBatch(List<MassChangeDocument> massChangeDocuments);
}
