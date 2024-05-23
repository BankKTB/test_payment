package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.config.ParameterConfig;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;

import java.sql.Timestamp;
import java.util.List;

public interface GLHeadService {
    GLHead save(GLHead glHead);
    void resetGLHead(Long paymentAliasId);
    void updateGLHead(String companyCode, String originalDocumentNo, String originalFiscalYear, String paymentDocumentNo, Long paymentAliasId);
    List<GLHead> findOriginalDocNo(String docNo);
    GLHead findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String compCode, String accDocNo, String fiscalYear);

    void updateSelectGroupDocument(SelectGroupDocument selectGroupDocument);

    void updateBlockReverse(String companyCode, String originalDocumentNo, String originalFiscalYear, String reverseDocumentNo, String reverseFiscalYear, String username, Timestamp updateDate, String docStatus);

    void updateGLHeadAfterReverseInvoice(String compCode, String accDocNo, String fiscalYear,String revAccDcNo,String revFiscalYear, Timestamp updateDate, String docStatus);

    void updateGLHeadAfterReversePayment(String compCode, String accDocNo, String fiscalYear, String username, Timestamp updateDate);
}
