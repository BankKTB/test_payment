package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.DuplicatePaymentReportResponse;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentAliasResponse;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReport;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReportPaging;
import th.com.bloomcode.paymentservice.model.request.DuplicatePaymentReport;

import java.sql.Timestamp;
import java.util.List;

public interface PaymentReportRepository extends CrudRepository<PaymentReport, Long> {

    List<PaymentReport> findProposalAllByPaymentAliasIdAndIsProposalAndIsChild(Long paymentAliasId, boolean proposal, boolean child);


    List<PaymentReport> findAllByPaymentAliasIdAndIsProposalAndIsChild(Long paymentAliasId, boolean proposal, boolean child);

    List<PaymentReport> findPaymentDocumentAllByPaymentAliasIdAndIsProposalAndIsChild(Long paymentAliasId, boolean proposal, boolean child);

    List<PaymentReport> findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(String parentCompanyCode, String parentDocumentNo, String parentFiscalYear, boolean proposal, boolean child,Long paymentAliasId);

    Page<PaymentReportPaging> findAllByPaymentAliasIdAndIsProposalAndIsChild(Long paymentAliasId, boolean proposal, String vendor, String bankAccount, int page, int size);


    int count(Long paymentAliasId, boolean proposal, String vendor, String bankAccount,String status);

    List<DuplicatePaymentReportResponse> findAllDuplicatePaymentReport(DuplicatePaymentReport request);
}
