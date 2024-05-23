package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.APUserChgBankAccNo;
import th.com.bloomcode.paymentservice.model.request.CheckAPUserBankAccNoRequest;

import java.util.List;

public interface APUserChgBankAccNoService {
    ResponseEntity<Result<List<APUserChgBankAccNo>>> findAllByValue(CheckAPUserBankAccNoRequest checkAPUserBankAccNoRequest);
}
