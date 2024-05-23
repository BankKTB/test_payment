package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.GLLine;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;

import java.sql.Timestamp;
import java.util.List;

public interface GLLineRepository extends CrudRepository<GLLine, Long> {

    List<GLLine> findByCompCodeAndAccDocNoAndFiscalYear(String companyCode, String docNo, String year);

    List<GLLine> findByCompCodeAndAccDocNoAndFiscalYearAndPaymentBlockStartsWith(String companyCode, String docNo,
                                                                                 String year, String paymentBlock);

    List<GLLine> findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountTypeAndPaymentBlockStartsWith(String companyCode, String originalDocumentNo, String originalFiscalYear,
                                                                                                                 String accountType,String paymentBlock);


    List<GLLine> findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndLine(String companyCode, String originalDocumentNo, String originalFiscalYear,
                                                                                       int line);

    List<GLLine> findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(String companyCode, String originalDocumentNo, String originalFiscalYear,
                                                                                        String accountType);

    void updateBlockStatusBatch(List<UnBlockDocument> unBlockDocuments);

    void updateBlockStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String accountType, String paymentBlockOld, String paymentBlockNew, String username, Timestamp updateDate);
    void updateBlockStatusErrorCase(String companyCode, String originalDocumentNo, String originalFiscalYear, String username, Timestamp updateDate);

}
