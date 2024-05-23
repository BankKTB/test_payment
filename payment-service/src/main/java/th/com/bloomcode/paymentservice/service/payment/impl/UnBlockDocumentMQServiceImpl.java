package th.com.bloomcode.paymentservice.service.payment.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentMQ;
import th.com.bloomcode.paymentservice.model.request.UnBlockChangeDocumentRequest;
import th.com.bloomcode.paymentservice.model.response.UnBlockDocumentMQResponse;
import th.com.bloomcode.paymentservice.repository.payment.UnBlockDocumentMQRepository;
import th.com.bloomcode.paymentservice.service.payment.UnBlockDocumentMQService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UnBlockDocumentMQServiceImpl implements UnBlockDocumentMQService {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final UnBlockDocumentMQRepository unBlockDocumentMQRepository;
    private final JdbcTemplate jdbcTemplate;

    public UnBlockDocumentMQServiceImpl(UnBlockDocumentMQRepository unBlockDocumentMQRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.unBlockDocumentMQRepository = unBlockDocumentMQRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    public UnBlockDocumentMQ save(UnBlockDocumentMQ unBlockDocumentMQ) {
        logger.info("==== Save unBlockDocumentMQ : {}", unBlockDocumentMQ);
        if (null == unBlockDocumentMQ.getId() || 0 == unBlockDocumentMQ.getId()) {
            unBlockDocumentMQ.setId(SqlUtil.getNextSeries(jdbcTemplate, UnBlockDocumentMQ.TABLE_NAME + UnBlockDocumentMQ.SEQ, null));
            unBlockDocumentMQ.setCreated(new Timestamp(System.currentTimeMillis()));
            unBlockDocumentMQ.setCreatedBy("SYSTEM");
        } else {
            unBlockDocumentMQ.setUpdated(new Timestamp(System.currentTimeMillis()));
            unBlockDocumentMQ.setUpdatedBy("IDEM");
        }
        return unBlockDocumentMQRepository.save(unBlockDocumentMQ);
    }


    @Override
    public ResponseEntity<Result<List<UnBlockDocumentMQResponse>>> findMQLogByCondition(List<UnBlockChangeDocumentRequest> request) {
        Result<List<UnBlockDocumentMQResponse>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<UnBlockDocumentMQ> unBlockDocumentMQList = unBlockDocumentMQRepository.findMQLogByCondition(request);
            List<UnBlockDocumentMQResponse> unBlockDocumentMQResponses = new ArrayList<>();
            Long total = (long) unBlockDocumentMQList.size();
            Long success = unBlockDocumentMQList.stream().filter(u -> u.getIdemStatus().equalsIgnoreCase("S")).count();
            Long fail = unBlockDocumentMQList.stream().filter(u -> u.getIdemStatus().equalsIgnoreCase("E")).count();
            Long process = unBlockDocumentMQList.stream().filter(u -> u.getIdemStatus().equalsIgnoreCase("P")).count();
            for (UnBlockDocumentMQ unBlockDocumentMQ : unBlockDocumentMQList) {
                UnBlockDocumentMQResponse unBlockDocumentMQResponse = new UnBlockDocumentMQResponse();
                BeanUtils.copyProperties(unBlockDocumentMQ, unBlockDocumentMQResponse);
                unBlockDocumentMQResponse.setTotal(total);
                unBlockDocumentMQResponse.setSuccess(success);
                unBlockDocumentMQResponse.setFail(fail);
                unBlockDocumentMQResponse.setProcess(process);
                unBlockDocumentMQResponses.add(unBlockDocumentMQResponse);
            }
            result.setStatus(HttpStatus.OK.value());
            result.setData(unBlockDocumentMQResponses);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<List<UnBlockDocumentMQResponse>>> findMQLogByCondition(String uuid) {
        Result<List<UnBlockDocumentMQResponse>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<UnBlockDocumentMQ> unBlockDocumentMQList = unBlockDocumentMQRepository.findMQLogByCondition(uuid);
            List<UnBlockDocumentMQResponse> unBlockDocumentMQResponses = new ArrayList<>();
            Long total = (long) unBlockDocumentMQList.size();
            Long success = unBlockDocumentMQList.stream().filter(u -> u.getIdemStatus().equalsIgnoreCase("S")).count();
            Long fail = unBlockDocumentMQList.stream().filter(u -> u.getIdemStatus().equalsIgnoreCase("E")).count();
            Long process = unBlockDocumentMQList.stream().filter(u -> u.getIdemStatus().equalsIgnoreCase("P")).count();
            for (UnBlockDocumentMQ unBlockDocumentMQ : unBlockDocumentMQList) {
                UnBlockDocumentMQResponse unBlockDocumentMQResponse = new UnBlockDocumentMQResponse();
                BeanUtils.copyProperties(unBlockDocumentMQ, unBlockDocumentMQResponse);
                unBlockDocumentMQResponse.setTotal(total);
                unBlockDocumentMQResponse.setSuccess(success);
                unBlockDocumentMQResponse.setFail(fail);
                unBlockDocumentMQResponse.setProcess(process);
                unBlockDocumentMQResponses.add(unBlockDocumentMQResponse);
            }
            result.setStatus(HttpStatus.OK.value());
            result.setData(unBlockDocumentMQResponses);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void saveBatch(List<UnBlockDocumentMQ> unBlockDocumentMQs) {
        unBlockDocumentMQRepository.saveBatch(unBlockDocumentMQs);
    }

    @Override
    public void updateBatch(List<UnBlockDocumentMQ> unBlockDocumentMQs) {
        unBlockDocumentMQRepository.updateBatch(unBlockDocumentMQs);
    }

    @Override
    public void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus, String reverseOriginalDocumentNo, String reverseOriginalFiscalYear, String reverseDocumentType, String reverseCompanyCode, String username, Timestamp updateDate) {
        unBlockDocumentMQRepository.updateStatus(companyCode, originalDocumentNo, originalFiscalYear, idemStatus, reverseOriginalDocumentNo, reverseOriginalFiscalYear, reverseDocumentType, reverseCompanyCode, username, updateDate);
    }

    @Override
    public void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus, String newValue, String username, Timestamp updateDate) {
        unBlockDocumentMQRepository.updateStatus(companyCode, originalDocumentNo, originalFiscalYear, idemStatus,newValue, username, updateDate);
    }

    @Override
    public UnBlockDocumentMQ findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear) {
        List<UnBlockDocumentMQ> unBlockDocumentLogList = unBlockDocumentMQRepository.findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
                companyCode, originalDocumentNo,
                originalFiscalYear);
        if (unBlockDocumentLogList.size() > 0) {
            UnBlockDocumentMQ unBlockDocumentLog = unBlockDocumentLogList.get(0);
            return unBlockDocumentLog;
        } else {
            return null;
        }
    }
}
