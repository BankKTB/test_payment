package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.payment.dao.PaymentIndependentConfigRepository;
import th.com.bloomcode.paymentservice.payment.entity.PaymentIndependentConfig;

import java.util.Date;
import java.util.List;

@Service
public class PaymentIndependentConfigService {

    private final PaymentIndependentConfigRepository paymentIndependentConfigRepository;

    @Autowired
    public PaymentIndependentConfigService(PaymentIndependentConfigRepository paymentIndependentConfigRepository) {
        this.paymentIndependentConfigRepository = paymentIndependentConfigRepository;
    }

    public ResponseEntity<Result<List<PaymentIndependentConfig>>> findOnlyStandard() {
        Result<List<PaymentIndependentConfig>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<PaymentIndependentConfig> paymentIndependentConfigs = this.paymentIndependentConfigRepository.findByActiveTrueAndStandardTrue();
            if (null != paymentIndependentConfigs && !paymentIndependentConfigs.isEmpty()) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(paymentIndependentConfigs);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Result<List<PaymentIndependentConfig>>> findByGroupName(String groupName) {
        Result<List<PaymentIndependentConfig>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<PaymentIndependentConfig> paymentIndependentConfigs = this.paymentIndependentConfigRepository.findByActiveTrueAndGroupName(groupName);
            if (null != paymentIndependentConfigs && !paymentIndependentConfigs.isEmpty()) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(paymentIndependentConfigs);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
