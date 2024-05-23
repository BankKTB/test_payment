package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocument;
import th.com.bloomcode.paymentservice.model.request.MassChangeDocumentRequest;

import java.sql.Timestamp;
import java.util.List;

public interface MassChangeDocumentRepository extends CrudRepository<MassChangeDocument, Long> {


    List<MassChangeDocument> findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear);

    List<MassChangeDocument> findByListDocument(List<MassChangeDocumentRequest> request);

    List<MassChangeDocument> findByListDocument(String uuid);

    void updateStatus(String companyCode, String documentNo, String fiscalYear, String status, String username, Timestamp updateDate);

    void saveBatch(List<MassChangeDocument> massChangeDocuments);

    void updateBatch(List<MassChangeDocument> massChangeDocuments);
}
