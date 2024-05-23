package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.payment.dao.SumFileConditionRepository;
import th.com.bloomcode.paymentservice.payment.entity.SumFileCondition;

import java.util.Date;
import java.util.List;

@Service
public class SumFileConditionService {

    private final SumFileConditionRepository sumFileConditionRepository;

    @Autowired
    public SumFileConditionService(SumFileConditionRepository sumFileConditionRepository) {
        this.sumFileConditionRepository = sumFileConditionRepository;
    }

    public List<SumFileCondition> findAll() {
        return sumFileConditionRepository.findAll();
    }

    public ResponseEntity<Result<SumFileCondition>> save(SumFileCondition request) {
        Result<SumFileCondition> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != request.getPaymentMethod() && !request.getPaymentMethod().isEmpty()) {

                List<SumFileCondition> checkVendor = sumFileConditionRepository.checkVendorCreate(request.getPaymentMethod(), request.getVendorFrom(), request.getVendorTo());

                if (checkVendor.size() == 0) {
                    SumFileCondition sumFileCondition = sumFileConditionRepository.save(request);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(sumFileCondition);
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

    public ResponseEntity<Result<SumFileCondition>> update(SumFileCondition request) {
        Result<SumFileCondition> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != request.getId()) {

                List<SumFileCondition> checkVendor = sumFileConditionRepository.checkVendorEdit(request.getId(), request.getPaymentMethod(), request.getVendorFrom(), request.getVendorTo());

                if (checkVendor.size() == 0) {

                    SumFileCondition sumFileCondition = sumFileConditionRepository.findOneById(request.getId());

                    if (null != sumFileCondition) {
                        sumFileCondition.setPaymentMethod(request.getPaymentMethod());
                        sumFileCondition.setVendorFrom(request.getVendorFrom());
                        sumFileCondition.setVendorTo(request.getVendorTo());
                        sumFileCondition.setValidFrom(request.getValidFrom());
                        sumFileCondition.setValidTo(request.getValidTo());
                        sumFileCondition.setUpdated(request.getUpdated());
                        sumFileCondition.setUpdatedBy(request.getUpdatedBy());
                        sumFileCondition.setActive(request.isActive());
                        sumFileConditionRepository.save(sumFileCondition);
                        result.setStatus(HttpStatus.CREATED.value());
                        result.setData(sumFileCondition);
                        return new ResponseEntity<>(result, HttpStatus.OK);
                    }
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

    public ResponseEntity<Result<SumFileCondition>> delete(Long id) {
        Result<SumFileCondition> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != id) {
                SumFileCondition sumFileCondition = sumFileConditionRepository.findOneById(id);

                if (null != sumFileCondition) {
//                    sumFileConditionRepository.deleteById(id);
                    sumFileCondition.setActive(!sumFileCondition.isActive());
                    sumFileConditionRepository.save(sumFileCondition);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(sumFileCondition);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setStatus(HttpStatus.NO_CONTENT.value());
                    result.setData(sumFileCondition);
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

    public ResponseEntity<Result<List<SumFileCondition>>> findAllConfig() {
        Result<List<SumFileCondition>> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            List<SumFileCondition> sumFileConditions = sumFileConditionRepository.findAllByOrderByVendorFromAscValidFromAsc();
            if (sumFileConditions.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(sumFileConditions);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NO_CONTENT.value());
                result.setData(sumFileConditions);
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Result<SumFileCondition>> findById(Long id) {
        Result<SumFileCondition> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            SumFileCondition sumFileCondition = sumFileConditionRepository.findOneById(id);
            if (sumFileCondition != null) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(sumFileCondition);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NO_CONTENT.value());
                result.setData(sumFileCondition);
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
