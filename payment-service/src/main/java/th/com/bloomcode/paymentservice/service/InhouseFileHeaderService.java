package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.payment.dao.InhouseFileHeaderRepository;
import th.com.bloomcode.paymentservice.payment.entity.InhouseFileHeader;

import java.util.List;

@Service
public class InhouseFileHeaderService {

    private final InhouseFileHeaderRepository inhouseFileHeaderRepository;

    @Autowired
    public InhouseFileHeaderService(InhouseFileHeaderRepository inhouseFileHeaderRepository) {
        this.inhouseFileHeaderRepository = inhouseFileHeaderRepository;
    }

    public InhouseFileHeader save(InhouseFileHeader inhouseFileHeader) {
        return inhouseFileHeaderRepository.save(inhouseFileHeader);
    }

    public List<InhouseFileHeader> findByGenerateFileAliasId(Long generateFileAliasId) {
        return inhouseFileHeaderRepository.findByGenerateFileAliasId(generateFileAliasId);
    }
}
