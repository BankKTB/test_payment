package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.BankCode;

import java.util.List;

public interface BankCodeService {
    ResponseEntity<Result<List<BankCode>>> findAll();
    List<BankCode> findAllBank(boolean isActive);
}
