package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.config.ParameterConfig;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;

import java.sql.Timestamp;
import java.util.List;

public interface GLHeadRepository extends CrudRepository<GLHead, Long> {

    List<GLHead> findByOriginalDocStartsWith(String docNo);

    List<GLHead> findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear);

    void resetGLHead(Long paymentAliasId);

    void updateGLHead(String companyCode, String originalDocumentNo, String originalFiscalYear, String paymentDocumentNo, Long paymentAliasId);

    void updateSelectGroupDocument(SelectGroupDocument selectGroupDocument);

    void updateBlockReverse(String companyCode, String originalDocumentNo, String originalFiscalYear, String reverseDocumentNo, String reverseFiscalYear, String username, Timestamp updateDate, String docStatus);

 void updateGLHeadAfterReverseInvoice(String compCode, String accDocNo, String fiscalYear,String revAccDcNo,String revFiscalYear, Timestamp updateDate, String docStatus);

    void updateGLHeadAfterReversePayment(String compCode, String accDocNo, String fiscalYear, String username, Timestamp updateDate);

}
