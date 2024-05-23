package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.SmartFileHeader;

import java.util.List;

public interface SmartFileHeaderService {
    SmartFileHeader save(SmartFileHeader smartFileHeader);
    List<SmartFileHeader> findAll();
    SmartFileHeader findOneById(Long id);
    SmartFileHeader findOneByGenerateFileAliasId(Long id);
}
