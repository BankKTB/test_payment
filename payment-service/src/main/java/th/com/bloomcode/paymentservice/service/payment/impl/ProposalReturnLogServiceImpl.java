package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.ReturnSearchProposalLog;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogSum;
import th.com.bloomcode.paymentservice.model.payment.ProposalReturnLog;
import th.com.bloomcode.paymentservice.repository.payment.ProposalReturnLogRepository;
import th.com.bloomcode.paymentservice.service.payment.ProposalReturnLogService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ProposalReturnLogServiceImpl implements ProposalReturnLogService {

  private ProposalReturnLogRepository proposalReturnLogRepository;
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public ProposalReturnLogServiceImpl(
      ProposalReturnLogRepository proposalReturnLogRepository,
      @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
    this.proposalReturnLogRepository = proposalReturnLogRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  public ProposalReturnLog save(ProposalReturnLog proposalLogReturn) {
    if (null == proposalLogReturn.getId() || 0 == proposalLogReturn.getId()) {
      proposalLogReturn.setId(
          SqlUtil.getNextSeries(jdbcTemplate, PaymentProcess.TABLE_NAME + PaymentProcess.SEQ, null));
    }
    return proposalReturnLogRepository.save(proposalLogReturn);
  }

  @Override
  public ProposalReturnLog findOneByInvoiceDocumentAndPaymentDocument(ProposalLog proposalLog) {
    return proposalReturnLogRepository.findOneByInvoiceDocumentAndPaymentDocument(proposalLog);
  }

  @Override
  public void updateProposalReturnLogAfterStep3(ProposalLog proposalLog,String revAccDocNo,String revFiscalYear) {
    proposalReturnLogRepository.updateProposalReturnLogAfterStep3(proposalLog,revAccDocNo,revFiscalYear);
  }

  @Override
  public ProposalReturnLog findOneByInvoiceDocumentAndPaymentDocument(String invoiceCompCode, String invoiceDocNo, String invoiceFiscalYear, String paymentCompCode, String paymentDocNo, String paymentFiscalYear) {
    return proposalReturnLogRepository.findOneByInvoiceDocumentAndPaymentDocument(invoiceCompCode, invoiceDocNo, invoiceFiscalYear, paymentCompCode, paymentDocNo, paymentFiscalYear);
  }

  @Override
  public ProposalReturnLog findOneById(Long proposalReturnLogId) {
    return proposalReturnLogRepository.findById(proposalReturnLogId).orElse(null);
  }

  @Override
  public ResponseEntity<Result<List<ProposalReturnLog>>> getPropLogReverseDocPayment(
      ReturnSearchProposalLog request) {
    Result<List<ProposalReturnLog>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<ProposalReturnLog> returnProposalDocumentResponse =
          proposalReturnLogRepository.findPaymentAllByCondition(request);
      log.info("size {}", returnProposalDocumentResponse.size());
      result.setData(returnProposalDocumentResponse);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Result<List<ProposalReturnLog>>> getPropLogReverseDocInvoice(
      ReturnSearchProposalLog request) {
    Result<List<ProposalReturnLog>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<ProposalReturnLog> returnProposalDocumentResponse =
          proposalReturnLogRepository.findInvoiceAllByCondition(request);
      log.info("size {}", returnProposalDocumentResponse.size());
      result.setData(returnProposalDocumentResponse);
      result.setStatus(HttpStatus.OK.value());
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void saveBatch(List<ProposalReturnLog> proposalReturnLogs) {
    proposalReturnLogRepository.saveBatch(proposalReturnLogs);
  }

  @Override
  public void updateBatch(List<ProposalReturnLog> proposalReturnLogs) {
    proposalReturnLogRepository.updateBatch(proposalReturnLogs);
  }

  @Override
  public List<ProposalReturnLog> findOneReturnDocumentProposalReturnLog(
      String companyCode, String documentNo, String fiscalYear, boolean payment) {
    return proposalReturnLogRepository.findOneReturnDocumentProposalReturnLog(
        companyCode, documentNo, fiscalYear, payment);
  }

  @Override
  public void deleteProposalReturnByStep2Complete(ProposalLog proposalLog) {
    proposalReturnLogRepository.deleteProposalReturnByStep2Complete(proposalLog);
  }

  @Override
  public void deleteBatch(List<ProposalLog> proposalLogs) {
    proposalReturnLogRepository.deleteBatch(proposalLogs);
  }
}
