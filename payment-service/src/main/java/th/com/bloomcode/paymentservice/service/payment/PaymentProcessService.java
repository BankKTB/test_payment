package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.webservice.model.response.APPaymentResponse;

import java.util.List;

public interface PaymentProcessService {
    PaymentProcess save(PaymentProcess paymentProcess);

    List<PaymentProcess> save(List<PaymentProcess> paymentProcess);

    void delete(PaymentProcess paymentProcess);

    void deleteAllByPaymentAliasId(Long paymentAliasId, boolean isProposal);

    List<PaymentProcess> findAll();

    PaymentProcess findOneById(Long id);

    Long findOneByPaymentAliasId(Long id);

    Long findOneByPaymentAliasIdNotParent(Long id);

    Long findOneByPaymentAliasIdForReverseDocument(Long id);

    Long countIdemReplyByPaymentAliasId(Long id);

    PaymentProcess findOneByPmGroupDocAndPmGroupNoAndProposalFalse(String pmGroupDoc, String pmGroupNo);

    PaymentProcess findOneByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(String accDocNo, String compCode, String fiscalYear);

    List<PaymentProcess> findAllByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(String accDocNo, String compCode, String fiscalYear);

    PaymentProcess findOneByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalAndIsChild(Long paymentId, String parentCompCode, String parentDocNo, String parentFiscalYear,boolean proposal,boolean child);


    List<PaymentProcess> findAllByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalTrueAndIsChildTrue(Long paymentId, String parentCompCode, String parentDocNo, String parentFiscalYear);

//    Long getNextSeries();
//
//    void updateNextSeries(Long lastSeq);

    void updatePaymentDocument(APPaymentResponse aPPaymentResponse);

    void updatePaymentDocument(APPaymentResponse aPPaymentResponse, String compCodeName);

    void saveBatch(List<PaymentProcess> paymentProcesses);

    PaymentProcess findOneIdemLastUpdatePaymentByPaymentAliasId(Long paymentAliasId);

    Long countIdemReversePaymentReplyByPaymentAliasId(Long paymentAliasId);

    Long countIdemCreatePaymentReplyByPaymentAliasId(Long paymentAliasId);

    void updateReversePaymentDocument(Long paymentAliasId);

    List<PaymentProcess> findAllByPaymentIdAndProposalNotChild(Long paymentId, boolean proposal);

    void updateProposalBlock(Long paymentProcessId);

    void restDocumentProposalErrorAfterRealRun(Long paymentId);

    void restDocumentProposalChildErrorAfterRealRun(Long paymentId);
}
