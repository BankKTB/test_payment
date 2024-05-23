package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogHeader;
import th.com.bloomcode.paymentservice.repository.payment.ProposalLogHeaderRepository;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogHeaderService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ProposalLogHeaderServiceImpl implements ProposalLogHeaderService {
    private final ProposalLogHeaderRepository proposalLogHeaderRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProposalLogHeaderServiceImpl(ProposalLogHeaderRepository proposalLogHeaderRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.proposalLogHeaderRepository = proposalLogHeaderRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public ProposalLogHeader save(ProposalLogHeader proposalLogHeader) {
        if (null == proposalLogHeader.getId() || 0 == proposalLogHeader.getId()) {
            proposalLogHeader.setId(SqlUtil.getNextSeries(jdbcTemplate, ProposalLogHeader.TABLE_NAME + ProposalLogHeader.SEQ, null));
        }
        return proposalLogHeaderRepository.save(proposalLogHeader);
    }

    @Override
    public List<ProposalLogHeader> findAll() {
        return (List<ProposalLogHeader>) proposalLogHeaderRepository.findAll();
    }
    
    @Override
    public ProposalLogHeader findOneById(Long id) {
        return proposalLogHeaderRepository.findById(id).orElse(null);
    }

    @Override
    public void updateRegen(Timestamp paymentDate, String paymentName, Long refRunning) {
        this.proposalLogHeaderRepository.updateRegen(paymentDate, paymentName, refRunning);
    }
}
