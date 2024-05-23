package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.ReturnSearchProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogSum;
import th.com.bloomcode.paymentservice.model.payment.ProposalReturnLog;

import java.util.List;

public interface ProposalReturnLogService {

    ProposalReturnLog save(ProposalReturnLog proposalReturnLog);

    ProposalReturnLog findOneByInvoiceDocumentAndPaymentDocument(ProposalLog proposalLog);

    void updateProposalReturnLogAfterStep3(ProposalLog proposalLog,String revAccDocNo,String revFiscalYear);

    ProposalReturnLog findOneByInvoiceDocumentAndPaymentDocument(String invoiceCompCode, String invoiceDocNo, String invoiceFiscalYear, String paymentCompCode, String paymentDocNo, String paymentFiscalYear);

    ProposalReturnLog findOneById(Long  proposalReturnLogId);

    ResponseEntity<Result<List<ProposalReturnLog>>> getPropLogReverseDocPayment(ReturnSearchProposalLog request);

    ResponseEntity<Result<List<ProposalReturnLog>>> getPropLogReverseDocInvoice(ReturnSearchProposalLog request);

    void saveBatch(List<ProposalReturnLog> proposalReturnLogs);

    void updateBatch(List<ProposalReturnLog> proposalReturnLogs);

    List<ProposalReturnLog> findOneReturnDocumentProposalReturnLog(String companyCode,String documentNo,String fiscalYear,boolean payment);

    void deleteProposalReturnByStep2Complete(ProposalLog proposalLog);

    void deleteBatch(List<ProposalLog> proposalLogs);
}

