package th.com.bloomcode.paymentservice.repository.payment;

import th.com.bloomcode.paymentservice.model.payment.PaymentInformation;
import th.com.bloomcode.paymentservice.repository.MetadataRepository;

import java.util.List;

public interface PaymentInformationRepository extends MetadataRepository<PaymentInformation, Long> {
    PaymentInformation findOneByPmGroupDocAndPmGroupNoAndProposalFalse(String pmGroupDoc, String pmGroupNo);
    List<PaymentInformation> findAllByPaymentAliasIdAndProposalFalse(Long paymentAliasId, String proposal);
    void saveBatch(List<PaymentInformation> paymentInformations);
}
