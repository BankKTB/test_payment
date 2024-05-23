package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.BankCode;
import th.com.bloomcode.paymentservice.repository.idem.BankCodeRepository;
import th.com.bloomcode.paymentservice.service.payment.BankCodeService;

import java.util.Date;
import java.util.List;

@Service
public class BankCodeServiceImpl implements BankCodeService {
    private final BankCodeRepository bankCodeRepository;

    public BankCodeServiceImpl(BankCodeRepository bankCodeRepository) {
        this.bankCodeRepository = bankCodeRepository;
    }

    @Override
    public ResponseEntity<Result<List<BankCode>>> findAll() {
        Result<List<BankCode>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<BankCode> bankCodes = bankCodeRepository.findAll(true);
            if (bankCodes.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(bankCodes);
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

    @Override
    public List<BankCode> findAllBank(boolean isActive) {
        return bankCodeRepository.findAll(isActive);
    }
}
