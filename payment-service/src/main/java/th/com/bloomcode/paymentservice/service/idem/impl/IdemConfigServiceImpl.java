package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.idem.IdemConfig;
import th.com.bloomcode.paymentservice.repository.idem.IdemConfigRepository;
import th.com.bloomcode.paymentservice.service.idem.IdemConfigService;

import java.util.List;

@Slf4j
@Service
public class IdemConfigServiceImpl implements IdemConfigService {
    private final IdemConfigRepository idemConfigRepository;

    public IdemConfigServiceImpl(IdemConfigRepository idemConfigRepository) {
        this.idemConfigRepository = idemConfigRepository;
    }

    @Override
    public IdemConfig findServiceByValueCode(String formId, String valueCode) {
        String companyCode = "";
        if (valueCode != null) {
            companyCode = valueCode.substring(0, 2);
        }

        if (formId.equalsIgnoreCase("V01") || formId.equalsIgnoreCase("J12") || formId.equalsIgnoreCase("J13")
                || formId.equalsIgnoreCase("J14") || formId.equalsIgnoreCase("E15") || formId.equalsIgnoreCase("E17")
                || formId.equalsIgnoreCase("J41") || formId.equalsIgnoreCase("J42")) {
            return idemConfigRepository.findByValueClientStartingWithAndIsActiveTrue("99");
        } else {
            return idemConfigRepository.findByValueClientStartingWithAndIsActiveTrue(companyCode);
        }

    }

    @Override
    public IdemConfig findServiceByValueCode(String valueCode) {
        return this.findServiceByValueCode("", valueCode);
    }

    @Override
    public List<IdemConfig> findAll() {
        return this.idemConfigRepository.findAllByIsActiveTrue();
    }
}
