//package th.com.bloomcode.paymentservice.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.payment.dao.SmartFileHeaderRepository;
//import th.com.bloomcode.paymentservice.payment.entity.SmartFileHeader;
//
//@Service
//public class SmartFileHeaderService {
//
//    private final SmartFileHeaderRepository smartFileHeaderRepository;
//
//    @Autowired
//    public SmartFileHeaderService(SmartFileHeaderRepository smartFileHeaderRepository) {
//        this.smartFileHeaderRepository = smartFileHeaderRepository;
//    }
//
//    public SmartFileHeader save(SmartFileHeader smartFileHeader) {
//        return smartFileHeaderRepository.save(smartFileHeader);
//    }
//
//    public SmartFileHeader findOneByGenerateFileAliasId(Long generateFileAliasId, boolean isTestRun) {
//        return smartFileHeaderRepository.findOneByGenerateFileAliasIdAndProposal(generateFileAliasId, isTestRun);
//    }
//}
