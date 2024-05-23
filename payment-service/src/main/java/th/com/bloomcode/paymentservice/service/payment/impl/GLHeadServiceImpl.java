package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.config.ParameterConfig;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;
import th.com.bloomcode.paymentservice.repository.payment.GLHeadRepository;
import th.com.bloomcode.paymentservice.service.payment.GLHeadService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class GLHeadServiceImpl implements GLHeadService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GLHeadRepository glHeadRepository;
    private final JdbcTemplate jdbcTemplate;

    public GLHeadServiceImpl(GLHeadRepository glHeadRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.glHeadRepository = glHeadRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public GLHead save(GLHead glHead) {
        logger.info("==== Save GLHead : {}", glHead);
        if (null == glHead.getId() || 0 == glHead.getId()) {
            glHead.setId(SqlUtil.getNextSeries(jdbcTemplate, GLHead.TABLE_NAME + GLHead.SEQ, null));
            glHead.setCreated(new Timestamp(System.currentTimeMillis()));
            glHead.setCreatedBy("SYSTEM");
        } else {
            logger.info("==== Save update Rev : {}", glHead.getReverseOriginalDocumentNo());
            glHead.setUpdated(new Timestamp(System.currentTimeMillis()));
            glHead.setUpdatedBy("SYSTEM");
        }
        return glHeadRepository.save(glHead);
    }

    @Override
    public void resetGLHead(Long paymentAliasId) {
        this.glHeadRepository.resetGLHead(paymentAliasId);

    }

    @Override
    public void updateGLHead(String companyCode, String originalDocumentNo, String originalFiscalYear, String paymentDocumentNo, Long paymentAliasId) {
        this.glHeadRepository.updateGLHead(companyCode, originalDocumentNo, originalFiscalYear, paymentDocumentNo, paymentAliasId);
    }

    public List<GLHead> findOriginalDocNo(String docNo) {
        return this.glHeadRepository.findByOriginalDocStartsWith(docNo);
    }


    public GLHead findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String compCode, String accDocNo, String fiscalYear) {
        List<GLHead> glHeadList = this.glHeadRepository.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(compCode, accDocNo, fiscalYear);

        if (glHeadList.size() > 0) {
            GLHead glHead = glHeadList.get(0);
            return glHead;
        } else {
            return null;
        }
    }
    @Override
    public void updateGLHeadAfterReverseInvoice(String compCode, String accDocNo, String fiscalYear,String revAccDcNo,String revFiscalYear, Timestamp updateDate, String docStatus) {
       this.glHeadRepository.updateGLHeadAfterReverseInvoice(compCode, accDocNo, fiscalYear,revAccDcNo,revFiscalYear, updateDate, docStatus);

    }
    @Override
    public void updateGLHeadAfterReversePayment(String compCode, String accDocNo, String fiscalYear, String username, Timestamp updateDate) {
        this.glHeadRepository.updateGLHeadAfterReversePayment(compCode, accDocNo, fiscalYear, username, updateDate);

    }

    @Override
    public void updateSelectGroupDocument(SelectGroupDocument selectGroupDocument) {
        this.glHeadRepository.updateSelectGroupDocument(selectGroupDocument);
    }

    @Override
    public void updateBlockReverse(String companyCode, String originalDocumentNo, String originalFiscalYear, String reverseDocumentNo, String reverseFiscalYear, String username, Timestamp updateDate, String docStatus) {
        this.glHeadRepository.updateBlockReverse(companyCode, originalDocumentNo, originalFiscalYear, reverseDocumentNo, reverseFiscalYear, username, updateDate, docStatus);
    }

//    @Transactional
//    public void resetGlhead(Long paymentAliasId) {
//        Map<String, Object> params = new HashMap<>();
//        StringBuilder sb = new StringBuilder();
//        sb.append(" UPDATE ");
//        sb.append(" GL_HEAD ");
//        sb.append(" SET PAYMENT_DOC_NO = NULL , PAYMENT_ID = NULL ");
//        sb.append(" WHERE ");
//        sb.append(" 1 = 1  ");
//
//        if (paymentAliasId > 0) {
//            sb.append(SqlUtil.whereClause(paymentAliasId.toString(), "PAYMENT_ID", params));
//        }
//
//
//        Query q = entityManager.createNativeQuery(sb.toString());
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        q.executeUpdate();
//
//    }


}
