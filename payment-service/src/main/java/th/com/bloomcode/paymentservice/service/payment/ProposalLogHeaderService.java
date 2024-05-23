package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.ProposalLogHeader;

import java.sql.Timestamp;
import java.util.List;

public interface ProposalLogHeaderService {
    ProposalLogHeader save(ProposalLogHeader proposalLogHeader);
    List<ProposalLogHeader> findAll();
    ProposalLogHeader findOneById(Long id);
    void updateRegen(Timestamp paymentDate, String paymentName, Long refRunning);
}
