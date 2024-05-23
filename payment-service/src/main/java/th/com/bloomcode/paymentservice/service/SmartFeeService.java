package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.payment.dao.SmartFeeRepository;
import th.com.bloomcode.paymentservice.payment.entity.SmartFee;

import java.util.Date;
import java.util.List;

@Service
public class SmartFeeService {

    private final SmartFeeRepository smartFeeRepository;

    @Autowired
    public SmartFeeService(SmartFeeRepository smartFeeRepository) {
        this.smartFeeRepository = smartFeeRepository;
    }

    public List<SmartFee> findAll() {
        return smartFeeRepository.findByOrderById();
    }


    public ResponseEntity<Result<SmartFee>> save(SmartFee request) {
        Result<SmartFee> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (request.getGlAccount() != null && !request.getGlAccount().isEmpty()) {
                SmartFee checkDuplicateMin = smartFeeRepository.findOneByGlAccountAndAmountMin(request.getGlAccount(),  request.getAmountMin());
                SmartFee checkDuplicateMax = smartFeeRepository.findOneByGlAccountAndAmountMax(request.getGlAccount(),  request.getAmountMax());

                List<SmartFee> checkDuplicate = smartFeeRepository.checkGlAccountAmountMinAndAmountMax(request.getGlAccount(),  request.getAmountMin(), request.getAmountMax());


//                if (null == checkDuplicateMin && null == checkDuplicateMax) {
                    if (checkDuplicate.size() == 0) {
                    SmartFee smartFee = smartFeeRepository.save(request);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(smartFee);
                    return new ResponseEntity<>(result, HttpStatus.OK);

                } else {
                    result.setMessage("already exist");
                    result.setStatus(HttpStatus.CONFLICT.value());
                    result.setData(null);
                    return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<Result<SmartFee>> update(SmartFee request) {
        Result<SmartFee> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != request.getId()) {
                SmartFee smartFee = smartFeeRepository.findOneById(request.getId());
                if (null != smartFee) {
                    smartFee.setBankFee(request.getBankFee());
                    smartFee.setBotFee(request.getBotFee());
                    smartFee.setSamedayBankFee(request.getSamedayBankFee());
                    smartFee.setSamedayBotFee(request.getSamedayBotFee());
                    smartFeeRepository.save(smartFee);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(smartFee);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
            } else {
                result.setMessage("already exist");
                result.setStatus(HttpStatus.CONFLICT.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<Result<SmartFee>> delete(Long id) {
        Result<SmartFee> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != id) {
                SmartFee smartFee = smartFeeRepository.findOneById(id);

                if (null != smartFee) {
                    smartFeeRepository.deleteById(id);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(smartFee);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setStatus(HttpStatus.NO_CONTENT.value());
                    result.setData(smartFee);
                    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<Result<List<SmartFee>>> findAllConfig() {
        Result<List<SmartFee>> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            List<SmartFee> smartFees = smartFeeRepository.findAllByOrderByGlAccountAscAmountMinAsc();
            if (smartFees.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(smartFees);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NO_CONTENT.value());
                result.setData(smartFees);
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Result<SmartFee>> findById(Long id) {
        Result<SmartFee> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            SmartFee smartFee = smartFeeRepository.findOneById(id);
            if (smartFee != null) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(smartFee);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NO_CONTENT.value());
                result.setData(smartFee);
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
