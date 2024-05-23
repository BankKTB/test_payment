package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.PaymentInformation;

import java.util.List;

public interface PaymentInformationService {
    PaymentInformation save(PaymentInformation paymentInformation);
    List<PaymentInformation> save(List<PaymentInformation> paymentInformation);
    List<PaymentInformation> findAll();
    PaymentInformation findOneById(Long id);
    PaymentInformation findOneByPmGroupDocAndPmGroupNoAndProposalFalse(String pmGroupDoc, String pmGroupNo);
    List<PaymentInformation> findAllByPaymentAliasIdAndProposal(Long paymentAliasId, String proposal);
//    Long getNextSeries();
//    void updateNextSeries(Long lastSeq);
    void saveBatch(List<PaymentInformation> paymentInformations);
}
