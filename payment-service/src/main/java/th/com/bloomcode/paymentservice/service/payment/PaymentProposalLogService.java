package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentProposalLog;

import java.util.List;

public interface PaymentProposalLogService {

  Long getNextSeries();
  void updateNextSeries(Long lastSeq);
  void addSuccessLog(Long paymentProposalLogId, int seq, String messageText, String messageClass, String messageNo, Long paymentAliasId, boolean proposal, List<PaymentProposalLog> paymentProposalLogs);
  void addErrorLog(Long paymentProposalLogId, int seq, String messageText, String messageClass, String messageNo, Long paymentAliasId, boolean proposal, List<PaymentProposalLog> paymentProposalLogs);
  void deletePaymentProposalLog(Long paymentAliasId,boolean isProposal);
  ResponseEntity<Result<Page<PaymentProposalLog>>> findAllByPaymentAliasIdAndProposal(Long paymentAliasId, boolean success, int page, int size);
  void saveBatch(List<PaymentProposalLog> paymentProposalLogs);
}