package th.com.bloomcode.paymentservice.repository.payment;

import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.repository.MetadataRepository;
import th.com.bloomcode.paymentservice.webservice.model.response.APPaymentResponse;

import java.util.List;

public interface PaymentProcessRepository extends MetadataRepository<PaymentProcess, Long> {
    PaymentProcess findOneByPmGroupDocAndPmGroupNoAndProposalFalse(String pmGroupDoc, String pmGroupNo);

    PaymentProcess findOneByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(String accDocNo, String compCode, String fiscalYear);

    List<PaymentProcess> findAllByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(String accDocNo, String compCode, String fiscalYear);

    List<PaymentProcess> findAllByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalTrueAndIsChildTrue(Long paymentId, String parentCompCode, String parentDocNo, String parentFiscalYear);

    Long findOneByPaymentAliasId(Long id);

    Long findOneByPaymentAliasIdNotParent(Long id);

    List<PaymentProcess> findAllByPaymentIdAndProposalNotChild(Long paymentId, boolean proposal);

    void restDocumentProposalErrorAfterRealRun(Long paymentId);

    void restDocumentProposalChildErrorAfterRealRun(Long paymentId);


    Long findOneByPaymentAliasIdForReverseDocument(Long id);

    void updatePaymentDocument(APPaymentResponse aPPaymentResponse);

    void updatePaymentDocument(APPaymentResponse aPPaymentResponse, String compCodeName);

    void saveBatch(List<PaymentProcess> paymentProcesses);

    PaymentProcess findOneIdemLastUpdatePaymentByPaymentAliasId(Long paymentAliasId);

    void updateReversePaymentDocument(Long paymentAliasId);

    Long countIdemReversePaymentReplyByPaymentAliasId(Long paymentAliasId);

    Long countIdemCreatePaymentReplyByPaymentAliasId(Long paymentAliasId);

    PaymentProcess findOneByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalAndIsChild(Long paymentId, String parentCompCode, String parentDocNo, String parentFiscalYear, boolean proposal, boolean child);

    void updateProposalBlock(Long paymentProcessId);

}
