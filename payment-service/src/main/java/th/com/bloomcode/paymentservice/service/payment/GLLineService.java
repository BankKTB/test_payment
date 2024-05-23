package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.GLLine;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.model.request.ChangeDocumentRequest;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

public interface GLLineService {


    void save(GLLine glLine);


    Result<List<GLLine>> findDocumentBlock(String companyCode, String docNo, String year);

    GLLine findOneUnBlockDocumentByCondition(String companyCode, String originalDocumentNo, String originalFiscalYear, boolean approve);

    GLLine findOneById(Long id);


    GLLine findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(String companyCode, String originalDocumentNo, String originalFiscalYear,
                                                                                        String accountType);

    GLLine findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndLine(String companyCode, String originalDocumentNo, String originalFiscalYear,
                                                                                 int line);

    ResponseEntity<Result<String>> changeDocument(HttpServletRequest httpServletRequest, ChangeDocumentRequest changeDocumentRequest);

    void updateBlockStatusBatch(List<UnBlockDocument> unBlockDocuments);

    void updateBlockStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String paymentBlock, boolean approve, String username, Timestamp updateDate);

    void updateBlockStatusErrorCase(String companyCode, String originalDocumentNo, String originalFiscalYear, String username, Timestamp updateDate);
}
