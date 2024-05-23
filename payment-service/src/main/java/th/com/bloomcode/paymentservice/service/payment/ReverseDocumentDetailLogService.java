package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocumentDetailLog;

import java.util.List;

public interface ReverseDocumentDetailLogService {


    ResponseEntity<Result<List<ReverseDocumentDetailLog>>> findErrorDetailByDocument(String companyCode, String documentNo, String fiscalYear);

    ReverseDocumentDetailLog save(ReverseDocumentDetailLog peverseDocumentDetailLog);



}
