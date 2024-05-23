package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.ReturnSearchProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.model.response.ReturnProposalLogResponse;
import th.com.bloomcode.paymentservice.model.response.ReturnReverseInvoiceResponse;
import th.com.bloomcode.paymentservice.model.response.ReturnReversePaymentResponse;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ProposalLogService {
    ProposalLog save(ProposalLog proposalLog);
    List<ProposalLog> selectProposalLogForGenerateJu(GenerateJuRequest request);
    void updateJuCreate(GenerateJuRequest request);
    ProposalLog findOneById(Long id);
    ProposalLog findOneByFileNameAndPaymentDocument(String fileName, String paymentDocument);
    ProposalLog findOneByPaymentDocumentAndPaymentCompCodeAndPaymentFiscalYear(String paymentDocument, String paymentCompCode, String paymentFiscalYear);
    ResponseEntity<Result<ProposalLog>> findByPaymentDocumentAndCompCodeAndPaymentFiscalYear(String paymentDocument, String paymentCompCode, String paymentFiscalYear);
    ProposalLog findOneByOriginalDocNoAndOriginalCompCodeAndOriginalFiscalYear(String originalDocNo, String originalCompCode, String originalFiscalYear);
    ProposalLog findOneByRefRunningAndFileTypeAndTransferLevel(Long refRunning, String fileType, String transferLevel);
    List<ReturnProposalLogResponse> findAllProposalLogReturn(ReturnSearchProposalLog request);
    List<ReturnReversePaymentResponse> findAllProposalLogReverseDocumentReturn(ReturnSearchProposalLog request);
    List<ReturnReverseInvoiceResponse> findAllProposalLogReverseInvoiceReturn(ReturnSearchProposalLog request);
    List<ProposalLog> findAllProposalLogByReturnFile(Map<String, Object> paramsReceive);
    List<ProposalLog> findAllProposalLogByReturnFile(String paymentDate, String paymentName, String effectiveDate);
    List<ProposalLog> findAllProposalLogByReturnFile(String effectiveDate, String fileName);
    List<ProposalLog> findAllProposalLogByReturnFileSum(Long refRunning, int refLine);
    List<ProposalLog> findAllProposalLogByReturnFileRerun(String effectiveDate, String fileName);
    int updateComplete(String effectiveDate, Timestamp created, String createdBy);
    int updateComplete(String effectiveDate, String paymentDate, String paymentName);
    int updateComplete(String effectiveDate, String fileName, Timestamp created, String createdBy);
    int resetComplete(String effectiveDate, String fileName);
    void updateRegen(Timestamp paymentDate, String paymentName, String fileType, String fileName);
    void updateRegenLevel1(Timestamp paymentDate, String paymentName, String fileName);
    boolean isExist(Timestamp paymentDate, String paymentName);
    boolean isFileExist(Timestamp paymentDate, String paymentName, String fileName);
    ProposalLog findOneFileName(Timestamp paymentDate, String paymentName, String fileName);
    ProposalLog findOneFileNameLevel1(Timestamp paymentDate, String paymentName);
    List<ProposalLog> findGroupFileName(Timestamp paymentDate, String paymentName);
    void saveBatch(List<ProposalLog> proposalLog);
    void updateBatch(List<ProposalLog> proposalLogs);
}
