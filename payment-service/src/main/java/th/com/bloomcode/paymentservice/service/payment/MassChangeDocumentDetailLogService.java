package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocumentDetailLog;

import java.util.List;

public interface MassChangeDocumentDetailLogService {


    ResponseEntity<Result<List<MassChangeDocumentDetailLog>>> findErrorDetailByDocument(String companyCode, String documentNo, String fiscalYear);

    MassChangeDocumentDetailLog save(MassChangeDocumentDetailLog massChangeDocumentDetailLog);



}
