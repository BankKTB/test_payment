package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPaying;
import th.com.bloomcode.paymentservice.repository.payment.CompanyPayingRepository;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayingService;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CompanyPayingServiceImpl implements CompanyPayingService {
    private final CompanyPayingRepository companyPayingRepository;

    public CompanyPayingServiceImpl(CompanyPayingRepository companyPayingRepository) {
        this.companyPayingRepository = companyPayingRepository;
    }

    @Override
    public ResponseEntity<Result<CompanyPaying>> save(CompanyPaying request) {
        Result<CompanyPaying> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != request.getCompanyCode() && !request.getCompanyCode().isEmpty()) {

                CompanyPaying checkDuplicate = companyPayingRepository.findOneByCompanyCode(request.getCompanyCode());

                if (null == checkDuplicate) {
                    CompanyPaying companyPaying = companyPayingRepository.save(request);

                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(companyPaying);
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

    @Override
    public ResponseEntity<Result<CompanyPaying>> update(CompanyPaying request) {
        Result<CompanyPaying> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != request.getId()) {
                CompanyPaying companyPaying = companyPayingRepository.findById(request.getId()).orElse(null);
                if (null != companyPaying) {
                    companyPaying.setMinimumAmountForPay(request.getMinimumAmountForPay());
                    companyPayingRepository.save(companyPaying);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(companyPaying);
                    return new ResponseEntity<>(result, HttpStatus.OK);
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

    @Override
    public ResponseEntity<Result<CompanyPaying>> delete(Long id) {
        Result<CompanyPaying> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != id) {
                CompanyPaying companyPaying = companyPayingRepository.findById(id).orElse(null);

                if (null != companyPaying) {
                    companyPayingRepository.deleteById(id);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(companyPaying);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setStatus(HttpStatus.NO_CONTENT.value());
                    result.setData(companyPaying);
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

    @Override
    public ResponseEntity<Result<List<CompanyPaying>>> findAll() {
        Result<List<CompanyPaying>> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            List<CompanyPaying> companyPayings = (List<CompanyPaying>) companyPayingRepository.findAll();
            if (companyPayings.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(companyPayings);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NO_CONTENT.value());
                result.setData(companyPayings);
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
    public ResponseEntity<Result<CompanyPaying>> findById(Long id) {
        Result<CompanyPaying> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            CompanyPaying companyPayings = companyPayingRepository.findById(id).orElse(null);
            if (companyPayings != null) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(companyPayings);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NO_CONTENT.value());
                result.setData(companyPayings);
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
    public CompanyPaying findOneById(Long id) {
        return this.companyPayingRepository.findById(id).orElse(null);
    }
}
