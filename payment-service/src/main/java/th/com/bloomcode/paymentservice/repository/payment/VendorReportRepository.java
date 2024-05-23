package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.VendorReport;

import java.util.List;

public interface VendorReportRepository extends CrudRepository<VendorReport, Long> {
    List<VendorReport> findAllByPaymentAliasIdAndIsProposalAndIsChild(Long paymentAliasId, boolean proposal, boolean child);
}
