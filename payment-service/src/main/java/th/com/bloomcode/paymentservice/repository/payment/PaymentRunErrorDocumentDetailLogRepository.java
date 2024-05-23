package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentLog;

import java.util.List;

public interface PaymentRunErrorDocumentDetailLogRepository extends CrudRepository<PaymentRunErrorDocumentDetailLog, Long> {

    List<PaymentRunErrorDocumentDetailLog> findErrorDetailByPaymentAliasId(Long paymentAliasId);

    List<PaymentRunErrorDocumentDetailLog> findDetailByInvoice(String invoiceCompanyCode, String invoiceDocumentNo, String invoiceFiscalYear);

}
