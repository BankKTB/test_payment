package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.BankCode;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocument;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;

import java.util.List;

public interface ReverseDocumentRepository extends CrudRepository<ReverseDocument, Long> {


    List<ReverseDocument> findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear);

    List<ReverseDocument> findByListDocument(List<ReverseDocumentRequest> request);

    List<ReverseDocument> findByListDocument(String  uuid);
    void saveBatch(List<ReverseDocument> reverseDocuments);
}
