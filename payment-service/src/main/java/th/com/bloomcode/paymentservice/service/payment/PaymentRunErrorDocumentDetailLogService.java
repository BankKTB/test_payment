package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentLog;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentLine;

import java.util.List;

public interface PaymentRunErrorDocumentDetailLogService {


    ResponseEntity<Result<List<PaymentRunErrorDocumentDetailLog>>> findErrorDetailByInvoice(String invoiceCompanyCode, String invoiceDocumentNo, String invoiceFiscalYear);

    PaymentRunErrorDocumentDetailLog save(PaymentRunErrorDocumentDetailLog paymentRunErrorDocumentDetailLog);

    ResponseEntity<Result<List<PaymentRunErrorDocumentDetailLog>>> findErrorDetailByPaymentAliasId(Long paymentAliasId);

}
