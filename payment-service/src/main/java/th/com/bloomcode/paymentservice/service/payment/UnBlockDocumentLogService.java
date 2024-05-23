package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.request.SearchUnBlockDocumentLogRequest;
import th.com.bloomcode.paymentservice.model.response.OmReportResponse;

import java.sql.Timestamp;
import java.util.List;

public interface UnBlockDocumentLogService {


    UnBlockDocumentLog save(UnBlockDocumentLog unBlockDocumentLog);

    UnBlockDocumentLog findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear);

    ResponseEntity<Result<List<UnBlockDocumentLog>>> findLogByCondition(SearchUnBlockDocumentLogRequest searchUnBlockDocumentLogRequest);

    ResponseEntity<Result<OmReportResponse>> findReportUnblockByCondition(SearchUnBlockDocumentLogRequest searchUnBlockDocumentLogRequest);

    void saveBatch(List<UnBlockDocumentLog> unBlockDocumentLogs);

    void updateBatch(List<UnBlockDocumentLog> unBlockDocumentLogs);

    void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus, String newValue, String username, Timestamp updateDate);
}
