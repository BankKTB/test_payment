package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogSum;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ProposalLogSumRepository extends CrudRepository<ProposalLogSum, Long> {
  void saveBatch(List<ProposalLogSum> proposalLog);
  void updateRegen(Timestamp paymentDate, String paymentName, String fileType, String fileName);
  List<ProposalLogSum> findAllProposalLogByReturnFile(Map<String, Object> paramsReceive);
  void updateBatch(List<ProposalLogSum> proposalLogSums);
}
