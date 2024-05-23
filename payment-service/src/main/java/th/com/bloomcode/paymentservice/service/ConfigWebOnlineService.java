//package th.com.bloomcode.paymentservice.service;
//
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.payment.dao.ConfigWebOnlineRepository;
//import th.com.bloomcode.paymentservice.payment.entity.ConfigWebOnline;
//
//import java.util.List;
//
//@Service
//public class ConfigWebOnlineService {
//
//	private final ConfigWebOnlineRepository configWebOnlineRepository;
//
//	public ConfigWebOnlineService(ConfigWebOnlineRepository configWebOnlineRepository) {
//		this.configWebOnlineRepository = configWebOnlineRepository;
//	}
//
//	public ConfigWebOnline findUrlByValueCode(String formId, String valueCode) {
//		String companyCode = "";
//		if (valueCode != null) {
//			companyCode = valueCode.substring(0, 2);
//		}
//
//		if (formId.equalsIgnoreCase("V01") || formId.equalsIgnoreCase("J12") || formId.equalsIgnoreCase("J13")
//						|| formId.equalsIgnoreCase("J14") || formId.equalsIgnoreCase("E15") || formId.equalsIgnoreCase("E17")
//						|| formId.equalsIgnoreCase("J41") || formId.equalsIgnoreCase("J42")) {
//			return configWebOnlineRepository.findFirstByValueCodeStartingWithAndIsActiveTrue("99");
//		} else {
//			return configWebOnlineRepository.findFirstByValueCodeStartingWithAndIsActiveTrue(companyCode);
//		}
//
//	}
//
//	public ConfigWebOnline findUrlByValueCode(String valueCode) {
//		return this.findUrlByValueCode("", valueCode);
//	}
//
//	public List<ConfigWebOnline> findAll() {
//		return this.configWebOnlineRepository.findAllByIsActiveTrue();
//	}
//
//}
