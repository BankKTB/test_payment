package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.SmartFileBatch;

import java.util.List;

public interface SmartFileBatchService {
    SmartFileBatch save(SmartFileBatch smartFileBatch);
    List<SmartFileBatch> findAll();
    SmartFileBatch findOneById(Long id);
}
