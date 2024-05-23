//package th.com.bloomcode.paymentservice.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.idem.dao.IdemConfigRepository;
//import th.com.bloomcode.paymentservice.idem.entity.IdemConfig;
//
//import java.util.List;
//
//@Service
//public class IdemConfigService {
//
//    private final IdemConfigRepository idemConfigRepository;
//
//    @Autowired
//    public IdemConfigService(IdemConfigRepository idemConfigRepository) {
//        this.idemConfigRepository = idemConfigRepository;
//    }
//
//    public IdemConfig findServiceByValueCode(String formId, String valueCode) {
//        String companyCode = "";
//        if (valueCode != null) {
//            companyCode = valueCode.substring(0, 2);
//        }
//
//        if (formId.equalsIgnoreCase("V01") || formId.equalsIgnoreCase("J12") || formId.equalsIgnoreCase("J13")
//                || formId.equalsIgnoreCase("J14") || formId.equalsIgnoreCase("E15") || formId.equalsIgnoreCase("E17")
//                || formId.equalsIgnoreCase("J41") || formId.equalsIgnoreCase("J42")) {
//            return idemConfigRepository.findByValueClientStartingWithAndIsActiveTrue("99");
//        } else {
//            return idemConfigRepository.findByValueClientStartingWithAndIsActiveTrue(companyCode);
//        }
//
//    }
//
//    public IdemConfig findServiceByValueCode(String valueCode) {
//        return this.findServiceByValueCode("", valueCode);
//    }
//
//    public List<IdemConfig> findAll() {
//        return this.idemConfigRepository.findAllByIsActiveTrue();
//    }
//
//}
