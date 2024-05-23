package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.ConfigWebOnline;
import th.com.bloomcode.paymentservice.repository.payment.ConfigWebOnlineRepository;
import th.com.bloomcode.paymentservice.service.payment.ConfigWebOnlineService;

import java.util.List;

@Service
@Slf4j
public class ConfigWebOnlineServiceImpl implements ConfigWebOnlineService {
    private final ConfigWebOnlineRepository configWebOnlineRepository;

    public ConfigWebOnlineServiceImpl(ConfigWebOnlineRepository configWebOnlineRepository) {
        this.configWebOnlineRepository = configWebOnlineRepository;
    }

    public ConfigWebOnline findUrlByValueCode(String formId, String valueCode) {
        String companyCode = "";
        if (valueCode != null) {
            companyCode = valueCode.substring(0, 2);
        }
        if (formId.equalsIgnoreCase("V01") || formId.equalsIgnoreCase("J12") || formId.equalsIgnoreCase("J13")
                || formId.equalsIgnoreCase("J14") || formId.equalsIgnoreCase("E15") || formId.equalsIgnoreCase("E17")
                || formId.equalsIgnoreCase("J41") || formId.equalsIgnoreCase("J42")) {
            return configWebOnlineRepository.findFirstByValueCodeStartingWithAndIsActiveTrue("99");
        } else {
            return configWebOnlineRepository.findFirstByValueCodeStartingWithAndIsActiveTrue(companyCode);
        }

    }

    public ConfigWebOnline findUrlByValueCode(String valueCode) {
        return this.findUrlByValueCode("", valueCode);
    }

    public List<ConfigWebOnline> findAll() {
        return this.configWebOnlineRepository.findAllByIsActiveTrue();
    }
}
