package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogTR1;
import th.com.bloomcode.paymentservice.model.payment.dto.SummaryFromTR1;
import th.com.bloomcode.paymentservice.model.request.SummaryTR1Request;

import java.util.List;

public interface ProposalLogTR1Service {
  void saveBatch(List<ProposalLogTR1> proposalLogTR1s);
  ResponseEntity<Result<List<SummaryFromTR1>>> getSummaryReportFromTR1(SummaryTR1Request request);
}
