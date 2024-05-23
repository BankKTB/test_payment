package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.ReturnReverseDocument;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocument;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;

import java.util.List;

public interface ReturnReverseDocumentRepository extends CrudRepository<ReturnReverseDocument, Long> {


    List<ReturnReverseDocument> findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear,boolean payment);

    List<ReturnReverseDocument> findByListDocument(List<ReverseDocumentRequest> request);


    void updateReversePayment(String revCompanyCode, String revDocumentNo, String revFiscalYear,String companyCode, String documentNo, String fiscalYear);
}


