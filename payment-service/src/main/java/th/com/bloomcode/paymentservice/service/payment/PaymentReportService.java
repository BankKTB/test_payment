package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.VendorReport;
import th.com.bloomcode.paymentservice.model.payment.dto.DuplicatePaymentReportResponse;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReport;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReportPaging;
import th.com.bloomcode.paymentservice.model.request.DuplicatePaymentReport;
import th.com.bloomcode.paymentservice.model.response.*;

import java.util.List;

public interface PaymentReportService {
    ResponseEntity<Result<List<VendorReport>>> findVendorReport(Long PaymentAliasId, Long type) throws Exception;

    ResponseEntity<Result<Page<PaymentReportPaging>>> findVendorReportDetail(Long paymentAliasId, boolean proposal, String vendor, String bankAccount, int page, int size) throws Exception;

    ResponseEntity<Result<CompanyReportResponse>> findCompanyReport(Long paymentAliasId) throws Exception;

    ResponseEntity<Result<List<PaymentErrorReportResponse>>> findErrorReport(Long id, Long type) throws Exception;

    ResponseEntity<Result<PaymentAreaReportResponse>> findAreaReport(Long id, Long type) throws Exception;

    ResponseEntity<Result<PaymentCountryReportResponse>> findCountryReport(Long id, Long type) throws Exception;

    ResponseEntity<Result<PaymentCurrencyReportResponse>> findCurrencyReport(Long id, Long type) throws Exception;

    ResponseEntity<Result<PaymentPaymentMethodReportResponse>> findPaymentMethodReport(Long id, Long type) throws Exception;

    ResponseEntity<Result<PaymentBankReportResponse>> findBankReport(Long id, Long type) throws Exception;

    List<PaymentReport> findDocumentPayment(Long paymentAliasId);

    List<PaymentReport> findProposalDocument(Long paymentAliasId);

    ResponseEntity<Result<List<DuplicatePaymentReportResponse>>> findAllDuplicatePaymentReport(DuplicatePaymentReport request);
}
