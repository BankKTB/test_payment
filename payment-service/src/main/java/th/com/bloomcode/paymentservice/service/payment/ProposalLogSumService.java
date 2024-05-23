package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogSum;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ProposalLogSumService {
    ProposalLogSum save(ProposalLogSum proposalLogSum);
    List<ProposalLogSum> findAll();
    ProposalLogSum findOneById(Long id);
    void saveBatch(List<ProposalLogSum> proposalLogSums);
    void updateRegen(Timestamp paymentDate, String paymentName, String fileType, String fileName);
    List<ProposalLogSum> findAllProposalLogByReturnFile(Map<String, Object> paramsReceive);
    void updateBatch(List<ProposalLogSum> proposalLogSums);
}
