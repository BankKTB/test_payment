package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.domain.Page;
import th.com.bloomcode.paymentservice.model.payment.PaymentProposalLog;
import th.com.bloomcode.paymentservice.repository.MetadataRepository;

import java.util.List;

public interface PaymentProposalLogRepository extends MetadataRepository<PaymentProposalLog, Long> {


    Page<PaymentProposalLog> findAllByPaymentAliasIdAndProposal(Long paymentAliasId, boolean success, int page, int size);

    void deletePaymentProposalLog(Long paymentAliasId,boolean isProposal);
    void saveBatch(List<PaymentProposalLog> proposalLogs);

}
