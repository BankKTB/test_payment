package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.ReturnSearchProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogHeader;
import th.com.bloomcode.paymentservice.model.payment.ReturnReverseDocument;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.model.response.ReturnProposalLogResponse;
import th.com.bloomcode.paymentservice.model.response.ReturnReverseInvoiceResponse;
import th.com.bloomcode.paymentservice.model.response.ReturnReversePaymentResponse;
import th.com.bloomcode.paymentservice.repository.payment.ProposalLogRepository;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProposalLogServiceImpl implements ProposalLogService {
    private final JdbcTemplate jdbcTemplate;
    private final ProposalLogRepository proposalLogRepository;

    public ProposalLogServiceImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, ProposalLogRepository proposalLogRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.proposalLogRepository = proposalLogRepository;
    }

    public ProposalLog save(ProposalLog proposalLog) {
        if (null == proposalLog.getId() || 0 == proposalLog.getId()) {
            proposalLog.setId(SqlUtil.getNextSeries(jdbcTemplate, ProposalLogHeader.TABLE_NAME + ProposalLogHeader.SEQ, null));
        }
        return proposalLogRepository.save(proposalLog);
    }

    @Override
    public List<ProposalLog> selectProposalLogForGenerateJu(GenerateJuRequest request) {
        return proposalLogRepository.selectProposalLogByGenerateJu(request);
    }

    @Override
    public void updateJuCreate(GenerateJuRequest request) {
        proposalLogRepository.updateJuCreate(request);
    }

    @Override
    public ProposalLog findOneById(Long id) {
        return proposalLogRepository.findById(id).orElse(null);
    }

    @Override
    public ProposalLog findOneByFileNameAndPaymentDocument(String fileName, String paymentDocument) {
        return proposalLogRepository.findOneByFileNameAndPaymentDocument(fileName, paymentDocument);
    }

    @Override
    public ProposalLog findOneByPaymentDocumentAndPaymentCompCodeAndPaymentFiscalYear(String paymentDocument, String paymentCompCode, String paymentFiscalYear) {
        return proposalLogRepository.findOneByPaymentDocumentAndPaymentCompCodeAndPaymentFiscalYear(paymentDocument, paymentCompCode, paymentFiscalYear);
    }

    @Override
    public ResponseEntity<Result<ProposalLog>> findByPaymentDocumentAndCompCodeAndPaymentFiscalYear(String paymentDocument, String paymentCompCode, String paymentFiscalYear) {
        Result<ProposalLog> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            ProposalLog document = proposalLogRepository.findOneByPaymentDocumentAndCompCodeAndPaymentFiscalYear(paymentDocument, paymentCompCode, paymentFiscalYear);

            result.setData(document);
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
    public ProposalLog findOneByOriginalDocNoAndOriginalCompCodeAndOriginalFiscalYear(String originalDocNo, String originalCompCode, String originalFiscalYear) {
        return proposalLogRepository.findOneByOriginalDocNoAndOriginalCompCodeAndOriginalFiscalYear(originalDocNo, originalCompCode, originalFiscalYear);
    }

    @Override
    public boolean isExist(Timestamp paymentDate, String paymentName) {
        return proposalLogRepository.isExist(paymentDate, paymentName);
    }

    @Override
    public boolean isFileExist(Timestamp paymentDate, String paymentName, String fileName) {
        return proposalLogRepository.isFileExist(paymentDate, paymentName, fileName);
    }

    @Override
    public ProposalLog findOneFileName(Timestamp paymentDate, String paymentName, String fileName) {
        return proposalLogRepository.findOneFileName(paymentDate, paymentName, fileName);
    }

    @Override
    public ProposalLog findOneFileNameLevel1(Timestamp paymentDate, String paymentName) {
        return proposalLogRepository.findOneFileNameLevel1(paymentDate, paymentName);
    }

    @Override
    public List<ProposalLog> findGroupFileName(Timestamp paymentDate, String paymentName) {
        return proposalLogRepository.findGroupFileName(paymentDate, paymentName);
    }

    @Override
    public void saveBatch(List<ProposalLog> proposalLog) {
        proposalLogRepository.saveBatch(proposalLog);
    }

    @Override
    public void updateBatch(List<ProposalLog> proposalLogs) {
        proposalLogRepository.updateBatch(proposalLogs);
    }

    @Override
    public ProposalLog findOneByRefRunningAndFileTypeAndTransferLevel(Long refRunning, String fileType, String transferLevel) {
        return proposalLogRepository.findOneByRefRunningAndFileTypeAndTransferLevel(refRunning, fileType, transferLevel);
    }

    @Override
    public List<ReturnProposalLogResponse> findAllProposalLogReturn(ReturnSearchProposalLog request) {
        return proposalLogRepository.findAllProposalLogReturn(request);
    }

    @Override
    public List<ReturnReversePaymentResponse> findAllProposalLogReverseDocumentReturn(ReturnSearchProposalLog request) {
        return proposalLogRepository.findAllProposalLogReverseDocumentReturn(request);
    }

    @Override
    public List<ReturnReverseInvoiceResponse> findAllProposalLogReverseInvoiceReturn(ReturnSearchProposalLog request) {
        return proposalLogRepository.findAllProposalLogReverseInvoiceReturn(request);
    }

    @Override
    public List<ProposalLog> findAllProposalLogByReturnFile(Map<String, Object> paramsReceive) {
        return proposalLogRepository.findAllProposalLogByReturnFile(paramsReceive);
    }

    @Override
    public List<ProposalLog> findAllProposalLogByReturnFile(String paymentDate, String paymentName, String effectiveDate) {
        return proposalLogRepository.findAllProposalLogByReturnFile(paymentDate, paymentName, effectiveDate);
    }

    @Override
    public List<ProposalLog> findAllProposalLogByReturnFile(String effectiveDate, String fileName) {
        return proposalLogRepository.findAllProposalLogByReturnFile(effectiveDate, fileName);
    }

    @Override
    public List<ProposalLog> findAllProposalLogByReturnFileSum(Long refRunning, int refLine) {
        return proposalLogRepository.findAllProposalLogByReturnFileSum(refRunning, refLine);
    }

    @Override
    public List<ProposalLog> findAllProposalLogByReturnFileRerun(String effectiveDate, String fileName) {
        return proposalLogRepository.findAllProposalLogByReturnFileRerun(effectiveDate, fileName);
    }

    @Override
    public int updateComplete(String effectiveDate, Timestamp created, String createdBy) {
        return proposalLogRepository.updateComplete(effectiveDate, created, createdBy);
    }

    @Override
    public int updateComplete(String effectiveDate, String paymentDate, String paymentName) {
        return proposalLogRepository.updateComplete(effectiveDate, paymentName, paymentDate);
    }

    @Override
    public int updateComplete(String effectiveDate, String fileName, Timestamp created, String createdBy) {
        return proposalLogRepository.updateComplete(effectiveDate, fileName, created, createdBy);
    }

    @Override
    public int resetComplete(String effectiveDate, String fileName) {
        return proposalLogRepository.resetComplete(effectiveDate, fileName);
    }

    @Override
    public void updateRegen(Timestamp paymentDate, String paymentName, String fileType, String fileName) {
        this.proposalLogRepository.updateRegen(paymentDate, paymentName, fileType, fileName);
    }

    @Override
    public void updateRegenLevel1(Timestamp paymentDate, String paymentName, String fileName) {
        this.proposalLogRepository.updateRegenLevel1(paymentDate, paymentName, fileName);
    }

}
