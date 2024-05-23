package th.com.bloomcode.paymentservice.service.idem;

import th.com.bloomcode.paymentservice.model.idem.IdemConfig;

import java.util.List;

public interface
IdemConfigService {
    IdemConfig findServiceByValueCode(String formId, String valueCode);
    IdemConfig findServiceByValueCode(String valueCode);
    List<IdemConfig> findAll();
}
