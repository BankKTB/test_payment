package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocument;
import th.com.bloomcode.paymentservice.model.payment.ReturnReverseDocument;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocument;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;
import th.com.bloomcode.paymentservice.repository.payment.ReturnReverseDocumentRepository;
import th.com.bloomcode.paymentservice.repository.payment.ReverseDocumentRepository;
import th.com.bloomcode.paymentservice.service.payment.ReturnReverseDocumentService;
import th.com.bloomcode.paymentservice.service.payment.ReverseDocumentService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReturnReverseDocumentServiceImpl implements ReturnReverseDocumentService {
    private final ReturnReverseDocumentRepository returnReverseDocumentRepository;

    public ReturnReverseDocumentServiceImpl(ReturnReverseDocumentRepository returnReverseDocumentRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.returnReverseDocumentRepository = returnReverseDocumentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(ReturnReverseDocument returnReverseDocument) {

        if (null == returnReverseDocument.getId() || 0 == returnReverseDocument.getId()) {
            returnReverseDocument.setId(SqlUtil.getNextSeries(jdbcTemplate, ReturnReverseDocument.TABLE_NAME + ReturnReverseDocument.SEQ, null));
            returnReverseDocument.setCreated(new Timestamp(System.currentTimeMillis()));
            returnReverseDocument.setCreatedBy(returnReverseDocument.getOriginalUserPost());
        } else {
            returnReverseDocument.setUpdated(new Timestamp(System.currentTimeMillis()));
            returnReverseDocument.setUpdatedBy(returnReverseDocument.getOriginalUserPost());
        }
        log.info(" save Return ReverseDocument {} ",returnReverseDocument);
        returnReverseDocumentRepository.save(returnReverseDocument);
    }

    @Override
    public ResponseEntity<Result<List<ReturnReverseDocument>>> findByCondition(String value) {
        return null;
    }

    @Override
    public  List<ReturnReverseDocument> findByListDocument(List<ReverseDocumentRequest> request){
        return returnReverseDocumentRepository.findByListDocument(request);
    }

    @Override
    public ResponseEntity<Result<List<ReturnReverseDocument>>> pullMassStep4ReverseDocument(List<ReverseDocumentRequest> request) {
        Result<List<ReturnReverseDocument>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<ReturnReverseDocument> listDocument = returnReverseDocumentRepository.findByListDocument(request);
            Long total = (long) listDocument.size();
            Long success = listDocument.stream().filter(u -> null != u.getOriginalIdemStatus() && u.getOriginalIdemStatus().equalsIgnoreCase("S")).count();
            Long fail = listDocument.stream().filter(u -> null != u.getOriginalIdemStatus() && u.getOriginalIdemStatus().equalsIgnoreCase("E")).count();
            Long process = listDocument.stream().filter(u -> null != u.getOriginalIdemStatus() && u.getOriginalIdemStatus().equalsIgnoreCase("P")).count();

            for (ReturnReverseDocument returnReverseDocument : listDocument) {
                returnReverseDocument.setTotal(total);
                returnReverseDocument.setSuccess(success);
                returnReverseDocument.setFail(fail);
                returnReverseDocument.setProcess(process);
            }
            result.setData(listDocument);
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ReturnReverseDocument findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear,boolean payment) {
        List<ReturnReverseDocument> reverseDocumentList = returnReverseDocumentRepository.findOneByCompanyCodeAndDocumentNoAndFiscalYear(companyCode, documentNo, fiscalYear,payment);
        if (reverseDocumentList.size() > 0) {
            return reverseDocumentList.get(0);
        } else {
            return null;
        }
    }
    @Override
    public void updateReversePayment(String revCompanyCode, String revDocumentNo, String revFiscalYear,String companyCode, String documentNo, String fiscalYear){
        returnReverseDocumentRepository.updateReversePayment(revCompanyCode,revDocumentNo,revFiscalYear,companyCode,documentNo,fiscalYear);
    }
}
