package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.ReturnSearchProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogSum;
import th.com.bloomcode.paymentservice.model.payment.ProposalReturnLog;

import java.util.List;

public interface ProposalReturnLogRepository extends CrudRepository<ProposalReturnLog, Long> {


    ProposalReturnLog findOneByInvoiceDocumentAndPaymentDocument(ProposalLog proposalLog);
    ProposalReturnLog findOneByInvoiceDocumentAndPaymentDocument(String invoiceCompCode, String invoiceDocNo, String invoiceFiscalYear, String paymentCompCode, String paymentDocNo, String paymentFiscalYear);

    List<ProposalReturnLog> findPaymentAllByCondition(ReturnSearchProposalLog request);

    List<ProposalReturnLog> findInvoiceAllByCondition(ReturnSearchProposalLog request);

    void saveBatch(List<ProposalReturnLog> proposalReturnLogs);

    void updateBatch(List<ProposalReturnLog> proposalReturnLogs);

    List<ProposalReturnLog> findOneReturnDocumentProposalReturnLog(String companyCode,String documentNo,String fiscalYear,boolean payment);

    void deleteProposalReturnByStep2Complete(ProposalLog proposalLog);

    void deleteBatch(List<ProposalLog> proposalLogs);

    void updateProposalReturnLogAfterStep3(ProposalLog proposalLog,String revAccDocNo,String revFiscalYear);
}

