package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogTR1;
import th.com.bloomcode.paymentservice.model.payment.dto.SummaryFromTR1;
import th.com.bloomcode.paymentservice.model.request.SummaryTR1Request;

import java.util.List;

public interface ProposalLogTR1Repository extends CrudRepository<ProposalLogTR1, Long> {
  void saveBatch(List<ProposalLogTR1> proposalLogTR1s);
  void saveBatch(List<SummaryFromTR1> summaryFromTR1, SummaryTR1Request request);
  void deleteAllByCriteria(SummaryTR1Request request);
  List<SummaryFromTR1> summaryReportFromTR1(SummaryTR1Request request);
}
