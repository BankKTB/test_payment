package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogSum;
import th.com.bloomcode.paymentservice.repository.payment.ProposalLogSumRepository;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogSumService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class ProposalLogSumServiceImpl implements ProposalLogSumService {
    private final ProposalLogSumRepository proposalLogSumRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProposalLogSumServiceImpl(ProposalLogSumRepository proposalLogSumRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.proposalLogSumRepository = proposalLogSumRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public ProposalLogSum save(ProposalLogSum proposalLogSum) {
        if (null == proposalLogSum.getId() || 0 == proposalLogSum.getId()) {
            proposalLogSum.setId(SqlUtil.getNextSeries(jdbcTemplate, ProposalLogSum.TABLE_NAME + ProposalLogSum.SEQ, null));
        }
        return proposalLogSumRepository.save(proposalLogSum);
    }

    @Override
    public List<ProposalLogSum> findAll() {
        return (List<ProposalLogSum>) proposalLogSumRepository.findAll();
    }
    
    @Override
    public ProposalLogSum findOneById(Long id) {
        return proposalLogSumRepository.findById(id).orElse(null);
    }

    @Override
    public void saveBatch(List<ProposalLogSum> proposalLogSums) {
        proposalLogSumRepository.saveBatch(proposalLogSums);
    }

    @Override
    public void updateRegen(Timestamp paymentDate, String paymentName, String fileType, String fileName) {
        this.proposalLogSumRepository.updateRegen(paymentDate, paymentName, fileType, fileName);
    }

    @Override
    public List<ProposalLogSum> findAllProposalLogByReturnFile(Map<String, Object> paramsReceive) {
        return this.proposalLogSumRepository.findAllProposalLogByReturnFile(paramsReceive);
    }

    @Override
    public void updateBatch(List<ProposalLogSum> proposalLogSums) {
        this.proposalLogSumRepository.updateBatch(proposalLogSums);
    }
}
