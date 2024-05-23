package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.SmartFileDetail;

import java.util.List;

public interface SmartFileDetailService {
    SmartFileDetail save(SmartFileDetail smartFileDetail);
    List<SmartFileDetail> findAll();
    SmartFileDetail findOneById(Long id);
}
