//package th.com.bloomcode.paymentservice.repository.payment;
//
//import org.springframework.data.repository.CrudRepository;
//import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentLog;
//
//import java.util.List;
//
//public interface PaymentRunErrorDocumentLogRepository extends CrudRepository<PaymentRunErrorDocumentLog, Long> {
//
//    List<PaymentRunErrorDocumentLog> findAllByOrderByFiscalYearAscAccDocNoAsc();
//
//    List<PaymentRunErrorDocumentLog> findDetailByInvoiceCompanyCodeNoAndInvoiceDocumentNoAndInvoiceFiscalYear(
//            String invoiceCompanyCode, String invoiceDocumentNo, String invoiceFiscalYear);
//
//
//}
