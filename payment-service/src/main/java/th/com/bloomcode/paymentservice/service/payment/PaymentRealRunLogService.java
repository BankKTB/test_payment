package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentRealRunLog;

import java.util.List;

public interface PaymentRealRunLogService {

  Long getNextSeries();
  void updateNextSeries(Long lastSeq);
  void addSuccessLog(Long  paymentRealRunLogId, Long seq, String messageText, Long paymentAliasId,  List<PaymentRealRunLog> paymentRealRunLogs);
  void addErrorLog(Long  paymentRealRunLogId, Long seq, String messageText, Long paymentAliasId,  List<PaymentRealRunLog> paymentRealRunLogs);
  void deletePaymentRealRunLog(Long paymentAliasId);
  ResponseEntity<Result<Page<PaymentRealRunLog>>> findAllByPaymentAliasId(Long paymentAliasId, boolean success, int page, int size);
  void saveBatch(List<PaymentRealRunLog> paymentProposalLogs);

  Long getSequenceByPaymentAliasId(Long paymentAliasId);


}