package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.ReturnReverseDocument;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocument;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;

import java.util.List;

public interface ReturnReverseDocumentService {

    ReturnReverseDocument findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear,boolean payment);

    void save(ReturnReverseDocument returnReverseDocument);

    ResponseEntity<Result<List<ReturnReverseDocument>>> findByCondition(String value);

    List<ReturnReverseDocument> findByListDocument(List<ReverseDocumentRequest> request);


    ResponseEntity<Result<List<ReturnReverseDocument>>> pullMassStep4ReverseDocument(List<ReverseDocumentRequest> request);

    void updateReversePayment(String revCompanyCode, String revDocumentNo, String revFiscalYear,String companyCode, String documentNo, String fiscalYear);

}
