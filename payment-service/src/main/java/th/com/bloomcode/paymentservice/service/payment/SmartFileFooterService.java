package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.SmartFileFooter;

import java.util.List;

public interface SmartFileFooterService {
    SmartFileFooter save(SmartFileFooter smartFileFooter);
    List<SmartFileFooter> findAll();
    SmartFileFooter findOneById(Long id);
}
