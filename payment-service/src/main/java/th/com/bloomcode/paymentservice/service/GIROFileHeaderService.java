package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.payment.dao.GIROFileHeaderRepository;
import th.com.bloomcode.paymentservice.payment.entity.GIROFileHeader;

import java.util.List;

@Service
public class GIROFileHeaderService {

    private final GIROFileHeaderRepository giroFileHeaderRepository;

    @Autowired
    public GIROFileHeaderService(GIROFileHeaderRepository giroFileHeaderRepository) {
        this.giroFileHeaderRepository = giroFileHeaderRepository;
    }

    public GIROFileHeader save(GIROFileHeader giroFileHeader) {
        return giroFileHeaderRepository.save(giroFileHeader);
    }

    public List<GIROFileHeader> findByGenerateFileAliasId(Long generateFileAliasId, boolean isTestRun) {
        return giroFileHeaderRepository.findByGenerateFileAliasIdAndProposal(generateFileAliasId, isTestRun);
    }
}
