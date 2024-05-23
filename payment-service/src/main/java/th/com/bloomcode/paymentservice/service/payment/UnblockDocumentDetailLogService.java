package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.payment.UnblockDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.request.UnblockDocumentLogDetailRequest;

import java.util.List;

public interface UnblockDocumentDetailLogService {


    ResponseEntity<Result<List<UnblockDocumentDetailLog>>> findErrorDetailByDocument(String originalCompanyCode, String originalDocumentNo, String originalFiscalYear);

    UnblockDocumentDetailLog save(UnblockDocumentDetailLog unblockDocumentDetailLog);

    ResponseEntity<Result<List<UnblockDocumentDetailLog>>> findLogDetail(UnblockDocumentLogDetailRequest request);

}
