package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.repository.payment.PaymentProcessRepository;
import th.com.bloomcode.paymentservice.service.payment.PaymentProcessService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.webservice.model.response.APPaymentResponse;

import java.util.List;

@Slf4j
@Service
public class PaymentProcessServiceImpl implements PaymentProcessService {
    private final PaymentProcessRepository paymentProcessRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PaymentProcessServiceImpl(PaymentProcessRepository paymentProcessRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.paymentProcessRepository = paymentProcessRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public PaymentProcess save(PaymentProcess paymentProcess) {
        if (null == paymentProcess.getId() || 0 == paymentProcess.getId()) {
            paymentProcess.setId(SqlUtil.getNextSeries(jdbcTemplate, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ, null));
        }
        return paymentProcessRepository.save(paymentProcess);
    }

    @Override
    public List<PaymentProcess> save(List<PaymentProcess> paymentProcess) {
        return (List<PaymentProcess>) paymentProcessRepository.saveAll(paymentProcess);
    }

    @Override
    public void delete(PaymentProcess paymentProcess) {
        paymentProcessRepository.delete(paymentProcess);
    }

    @Override
    public void deleteAllByPaymentAliasId(Long paymentAliasId, boolean isProposal) {

        StringBuilder sql = new StringBuilder();
        sql.append("DELETE ");
        sql.append(" FROM ");
        sql.append(" PAYMENT_PROCESS P");
        sql.append(" WHERE ");
        sql.append(" 1 = 1  ");
        sql.append(" AND P.PAYMENT_ALIAS_ID  = ?");

        if (!isProposal) {
            sql.append(" AND " + PaymentProcess.COLUMN_NAME_IS_PROPOSAL + " = 0 ");
        }

        log.info("paymentAliasId {}", paymentAliasId);
        log.info("deleteAllByPaymentAliasId {}", sql.toString());

        this.jdbcTemplate.update(sql.toString(), paymentAliasId);

    }

    @Override
    public List<PaymentProcess> findAll() {
        return (List<PaymentProcess>) paymentProcessRepository.findAll();
    }

    @Override
    public PaymentProcess findOneById(Long id) {
        return paymentProcessRepository.findById(id).orElse(null);
    }

    @Override
    public Long findOneByPaymentAliasId(Long id) {
        return paymentProcessRepository.findOneByPaymentAliasId(id);
    }

    @Override
    public Long findOneByPaymentAliasIdNotParent(Long id) {
        return paymentProcessRepository.findOneByPaymentAliasIdNotParent(id);
    }

    @Override
    public Long findOneByPaymentAliasIdForReverseDocument(Long id) {
        return paymentProcessRepository.findOneByPaymentAliasIdForReverseDocument(id);
    }

    @Override
    public Long countIdemReplyByPaymentAliasId(Long id) {
        return null;
    }

    @Override
    public PaymentProcess findOneByPmGroupDocAndPmGroupNoAndProposalFalse(String pmGroupDoc, String pmGroupNo) {
        return paymentProcessRepository.findOneByPmGroupDocAndPmGroupNoAndProposalFalse(pmGroupDoc, pmGroupNo);
    }

    @Override
    public PaymentProcess findOneByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(String accDocNo, String compCode, String fiscalYear) {
        return paymentProcessRepository.findOneByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(accDocNo, compCode, fiscalYear);
    }

    @Override
    public List<PaymentProcess> findAllByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(String accDocNo, String compCode, String fiscalYear) {
        return paymentProcessRepository.findAllByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(accDocNo, compCode, fiscalYear);
    }

    @Override
    public PaymentProcess findOneByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalAndIsChild(Long paymentId, String parentCompCode, String parentDocNo, String parentFiscalYear, boolean proposal, boolean child) {
        return paymentProcessRepository.findOneByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalAndIsChild(paymentId, parentCompCode, parentDocNo, parentFiscalYear, proposal, child);
    }

    @Override
    public List<PaymentProcess> findAllByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalTrueAndIsChildTrue(Long paymentId, String parentCompCode, String parentDocNo, String parentFiscalYear) {
        return null;
    }

//    @Override
//    public synchronized Long getNextSeries() {
//        return paymentProcessRepository.getNextSeries();
//    }
//
//    @Override
//    public void updateNextSeries(Long lastSeq) {
//        paymentProcessRepository.updateNextSeries(lastSeq);
//    }

    @Override
    public void updatePaymentDocument(APPaymentResponse aPPaymentResponse) {
        this.paymentProcessRepository.updatePaymentDocument(aPPaymentResponse);

    }

    @Override
    public void updatePaymentDocument(APPaymentResponse aPPaymentResponse, String compCodeName) {
        this.paymentProcessRepository.updatePaymentDocument(aPPaymentResponse);
    }

    @Override
    public void saveBatch(List<PaymentProcess> paymentProcesses) {
        this.paymentProcessRepository.saveBatch(paymentProcesses);
    }

    @Override
    public PaymentProcess findOneIdemLastUpdatePaymentByPaymentAliasId(Long paymentAliasId) {
        return paymentProcessRepository.findOneIdemLastUpdatePaymentByPaymentAliasId(paymentAliasId);
    }

    @Override
    public Long countIdemReversePaymentReplyByPaymentAliasId(Long paymentAliasId) {
        return paymentProcessRepository.countIdemReversePaymentReplyByPaymentAliasId(paymentAliasId);
    }

    @Override
    public Long countIdemCreatePaymentReplyByPaymentAliasId(Long paymentAliasId) {
        return paymentProcessRepository.countIdemCreatePaymentReplyByPaymentAliasId(paymentAliasId);
    }

    @Override
    public void updateReversePaymentDocument(Long paymentAliasId) {
        this.paymentProcessRepository.updateReversePaymentDocument(paymentAliasId);
    }

    @Override
    public List<PaymentProcess> findAllByPaymentIdAndProposalNotChild(Long paymentId, boolean proposal) {
        return this.paymentProcessRepository.findAllByPaymentIdAndProposalNotChild(paymentId, proposal);
    }

    @Override
    public void updateProposalBlock(Long paymentProcessId) {
        this.paymentProcessRepository.updateProposalBlock(paymentProcessId);
    }

    @Override
    public void restDocumentProposalErrorAfterRealRun(Long paymentId) {
        paymentProcessRepository.restDocumentProposalErrorAfterRealRun(paymentId);
    }
    @Override
    public void restDocumentProposalChildErrorAfterRealRun(Long paymentId) {
        paymentProcessRepository.restDocumentProposalChildErrorAfterRealRun(paymentId);
    }
}
