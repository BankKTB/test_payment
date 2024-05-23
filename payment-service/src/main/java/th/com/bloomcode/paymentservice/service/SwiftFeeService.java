package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.payment.dao.SwiftFeeRepository;
import th.com.bloomcode.paymentservice.payment.entity.SwiftFee;
import th.com.bloomcode.paymentservice.payment.entity.SwiftFee;

import java.util.Date;
import java.util.List;

@Service
public class SwiftFeeService {

    private final SwiftFeeRepository swiftFeeRepository;

    @Autowired
    public SwiftFeeService(SwiftFeeRepository swiftFeeRepository) {
        this.swiftFeeRepository = swiftFeeRepository;
    }

    public boolean checkSwiftFormat(String bankKey) {
        return swiftFeeRepository.existsByBankKeyAndAndSmartIsTrue(bankKey);
    }

    public List<SwiftFee> findAll() {
        return swiftFeeRepository.findBySmartIsTrue();
    }

    public ResponseEntity<Result<SwiftFee>> save(SwiftFee request) {
        Result<SwiftFee> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != request.getBankKey() && !request.getBankKey().isEmpty()) {
//
                SwiftFee checkDuplicate = swiftFeeRepository.findOneByBankKey(request.getBankKey());
//
                if (null == checkDuplicate) {
                    SwiftFee swiftFee = swiftFeeRepository.save(request);

                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(swiftFee);
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

    public ResponseEntity<Result<SwiftFee>> update(SwiftFee request) {
        Result<SwiftFee> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != request.getId()) {


                SwiftFee swiftFee = swiftFeeRepository.findOneById(request.getId());

                if (null != swiftFee) {
                    swiftFee.setBaseAmount(request.getBaseAmount());
                    swiftFee.setBaseFee(request.getBaseFee());
                    swiftFee.setVarAmount(request.getVarAmount());
                    swiftFee.setVarFee(request.getVarFee());
                    swiftFee.setMaxFee(request.getMaxFee());
                    swiftFee.setSmart(request.isSmart());
                    swiftFeeRepository.save(swiftFee);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(swiftFee);
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

    public ResponseEntity<Result<SwiftFee>> delete(Long id) {
        Result<SwiftFee> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != id) {
                SwiftFee swiftFee = swiftFeeRepository.findOneById(id);

                if (null != swiftFee) {
                    swiftFeeRepository.deleteById(id);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(swiftFee);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setStatus(HttpStatus.NO_CONTENT.value());
                    result.setData(swiftFee);
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

    public ResponseEntity<Result<List<SwiftFee>>> findAllConfig() {
        Result<List<SwiftFee>> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            List<SwiftFee> swiftFees = swiftFeeRepository.findAllByOrderByBankKeyAsc();
            if (swiftFees.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(swiftFees);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NO_CONTENT.value());
                result.setData(swiftFees);
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Result<SwiftFee>> findById(Long id) {
        Result<SwiftFee> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            SwiftFee swiftFee = swiftFeeRepository.findOneById(id);
            if (swiftFee != null) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(swiftFee);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NO_CONTENT.value());
                result.setData(swiftFee);
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
