package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.PaymentArrange;
import th.com.bloomcode.paymentservice.repository.payment.PaymentArrangeRepository;
import th.com.bloomcode.paymentservice.service.payment.PaymentArrangeService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PaymentArrangeServiceImpl implements PaymentArrangeService {

    private final PaymentArrangeRepository paymentArrangeRepository;
    private final JdbcTemplate jdbcTemplate;

    public PaymentArrangeServiceImpl(
            PaymentArrangeRepository paymentArrangeRepository,
            @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate
    ) {
        this.paymentArrangeRepository = paymentArrangeRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public ResponseEntity<Result<PaymentArrange>> save(HttpServletRequest httpServletRequest, PaymentArrange request) {
            JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
            Result<PaymentArrange> result = new Result<>();
            result.setTimestamp(new Date());
            try {
                PaymentArrange checkDuplicate = paymentArrangeRepository.findOneByArrangeCodeAndArrangeName(
                        request.getArrangeCode(), request.getArrangeName()
                );
                if (null == checkDuplicate) {
                    if (null == request.getId() || 0 == request.getId()) {
                        request.setId(SqlUtil.getNextSeries(jdbcTemplate, PaymentArrange.TABLE_NAME + PaymentAlias.SEQ, null));
                    }
                    request.setActive(true);
                    request.setArrangeDefault(false);
                    request.setCreatedBy(jwt.getSN());
                    request.setUpdatedBy(jwt.getSN());
                    request.setCreated(new Timestamp(new Date().getTime()));
                    request.setUpdated(new Timestamp(new Date().getTime()));
                    PaymentArrange paymentArrange = paymentArrangeRepository.save(request);

                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(paymentArrange);
                    return new ResponseEntity<>(result, HttpStatus.OK);
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

    }

    @Override
    public ResponseEntity<Result<List<PaymentArrange>>> findAllByArrangeCode(HttpServletRequest httpServletRequest, String arrangeCode) {
        Result<List<PaymentArrange>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<PaymentArrange> paymentArrange = paymentArrangeRepository.findAllByArrangeCode(arrangeCode);
            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentArrange);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<Result<List<PaymentArrange>>> findByArrangeCodeDefaultArrange(HttpServletRequest httpServletRequest, String arrangeCode) {
        Result<List<PaymentArrange>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<PaymentArrange> paymentArrange = paymentArrangeRepository.findByArrangeCodeDefaultArrange(arrangeCode);
            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentArrange);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<Result<PaymentArrange>> findOneByArrangeCodeAndArrangeName(HttpServletRequest httpServletRequest, String arrangeCode, String arrangeName) {
        Result<PaymentArrange> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            PaymentArrange paymentArrange = paymentArrangeRepository.findOneByArrangeCodeAndArrangeName(
                    arrangeCode, arrangeName
            );
            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentArrange);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<PaymentArrange>> findOneById(HttpServletRequest httpServletRequest, Long arrangeId) {
        Result<PaymentArrange> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            PaymentArrange paymentArrange = paymentArrangeRepository.findOneByArrangeId(arrangeId);
            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentArrange);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<PaymentArrange>> edit(HttpServletRequest httpServletRequest, PaymentArrange request, Long id) {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Result<PaymentArrange> result = new Result<>();
        result.setTimestamp(new Date());
        try {
//            boolean found = paymentArrangeRepository.existsById(id);
            PaymentArrange paymentArrange = paymentArrangeRepository.findOneByArrangeId(id);
            if (!Util.isEmpty(paymentArrange.getId())) {
                request.setId(id);
                request.setActive(paymentArrange.isActive());
                request.setCreated(paymentArrange.getCreated());
                request.setCreatedBy(paymentArrange.getCreatedBy());
                request.setUpdated(new Timestamp(new Date().getTime()));
                request.setUpdatedBy(jwt.getSN());
                PaymentArrange arrangeConfig = paymentArrangeRepository.save(request);

                result.setStatus(HttpStatus.CREATED.value());
                result.setData(arrangeConfig);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setMessage("not exist");
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setData(null);
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
    public ResponseEntity<Result> delete(HttpServletRequest httpServletRequest, Long id) {
        try {
            paymentArrangeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<PaymentArrange>> editDefaultArrange(HttpServletRequest httpServletRequest, Long id) {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Result<PaymentArrange> result = new Result<>();
        result.setTimestamp(new Date());
        
        try {
            PaymentArrange paymentArrange = paymentArrangeRepository.findOneByArrangeId(id);
            paymentArrange.setId(id);
            paymentArrange.setArrangeDefault(true);
            paymentArrange.setUpdated(new Timestamp(new Date().getTime()));
            paymentArrange.setUpdatedBy(jwt.getSN());
            PaymentArrange arrangeConfig = paymentArrangeRepository.save(paymentArrange);

            List<PaymentArrange> paymentArrangeOther = paymentArrangeRepository.findAllByArrangeDefaultStatus(id, false);
            if (!Util.isEmpty(paymentArrangeOther) && paymentArrangeOther.size() > 0) {
                paymentArrangeOther.forEach(c -> {
                    c.setArrangeDefault(false);
                    c.setUpdated(new Timestamp(new Date().getTime()));
                    c.setUpdatedBy(jwt.getSN());
                    paymentArrangeRepository.save(c);
                });
            }

            result.setStatus(HttpStatus.CREATED.value());
            result.setData(arrangeConfig);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
