package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.SmartFileLog;

import java.util.List;

public interface SmartFileLogService {
    SmartFileLog save(SmartFileLog smartFileLog);
    void saveAll(List<SmartFileLog> smartFileLogs);
    List<SmartFileLog> findAll();
    SmartFileLog findOneById(Long id);
}
