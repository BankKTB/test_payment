package th.com.bloomcode.paymentservice.service.payment.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.GLLine;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.model.request.ChangeDocumentRequest;
import th.com.bloomcode.paymentservice.repository.payment.GLLineRepository;
import th.com.bloomcode.paymentservice.service.payment.GLHeadService;
import th.com.bloomcode.paymentservice.service.payment.GLLineService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GLLineServiceImpl implements GLLineService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GLHeadService glHeadService;
    private final GLLineRepository glLineRepository;
    private final JdbcTemplate jdbcTemplate;

    public GLLineServiceImpl(GLHeadService glHeadService, GLLineRepository glLineRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.glHeadService = glHeadService;
        this.glLineRepository = glLineRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    public void save(GLLine glLine) {
        logger.info("==== Save GLLine : {}", glLine);
        if (null == glLine.getId() || 0 == glLine.getId()) {
            glLine.setId(SqlUtil.getNextSeries(jdbcTemplate, GLLine.TABLE_NAME + GLLine.SEQ, null));
            glLine.setCreated(new Timestamp(System.currentTimeMillis()));
            glLine.setCreatedBy("SYSTEM");
        } else {
            glLine.setUpdated(new Timestamp(System.currentTimeMillis()));
            glLine.setUpdatedBy("SYSTEM");
        }
        glLineRepository.save(glLine);
    }

    public Result<List<GLLine>> findDocumentBlock(String companyCode, String docNo, String year) {
        Result<List<GLLine>> result = new Result<>();
        try {
            List<GLHead> glHeads = this.glHeadService.findOriginalDocNo(docNo);

            for (GLHead glHead : glHeads) {
                List<GLLine> glLines = this.glLineRepository
                        .findByCompCodeAndAccDocNoAndFiscalYearAndPaymentBlockStartsWith(companyCode,
                                glHead.getOriginalDocumentNo(), year, "B");

                if (null != glLines && !glLines.isEmpty()) {
                    result.setStatus(HttpStatus.OK.value());
                    result.setData(glLines);
                    result.setMessage(Result.SUCCESS);
                } else {
                    result.setStatus(HttpStatus.NOT_FOUND.value());
                    result.setData(new ArrayList<>());
                    result.setMessage(Result.NOT_FOUND);
                }
            }
//      List<GLLine> glLines = this.glLineRepository.findByCompCodeAndAccDocNoAndFiscalYearAndPaymentBlock(companyCode, docNo, year, "B");

        } catch (Exception e) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setData(new ArrayList<>());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    public GLLine findOneUnBlockDocumentByCondition(String companyCode, String originalDocumentNo, String originalFiscalYear, boolean approve) {

        if (approve) {
            List<GLLine> glLineList = this.glLineRepository.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountTypeAndPaymentBlockStartsWith(
                    companyCode, originalDocumentNo, originalFiscalYear, "K", "B%");
            if (glLineList.size() > 0) {
                GLLine glLine = glLineList.get(0);
                return glLine;
            } else {
                return null;
            }
        } else {
            List<GLLine> glLineList = this.glLineRepository.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountTypeAndPaymentBlockStartsWith(
                    companyCode, originalDocumentNo, originalFiscalYear, "K", "E%");
            if (glLineList.size() > 0) {
                GLLine glLine = glLineList.get(0);
                return glLine;
            } else {
                return null;
            }
        }
    }

    public GLLine findOneById(Long id) {
        return this.glLineRepository.findById(id).orElse(null);
    }

    @Override
    public GLLine findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(String companyCode, String originalDocumentNo, String originalFiscalYear, String accountType) {
        List<GLLine> glLineList = glLineRepository.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(
                companyCode, originalDocumentNo,
                originalFiscalYear, accountType);
        if (glLineList.size() > 0) {
            GLLine glLine = glLineList.get(0);
            return glLine;
        } else {
            return null;
        }
    }


    public GLLine findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndLine(String companyCode, String originalDocumentNo, String originalFiscalYear, int line) {
        List<GLLine> glLineList = glLineRepository.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndLine(
                companyCode, originalDocumentNo,
                originalFiscalYear, line);
        if (glLineList.size() > 0) {
            GLLine glLine = glLineList.get(0);
            return glLine;
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<Result<String>> changeDocument(HttpServletRequest httpServletRequest, ChangeDocumentRequest changeDocumentRequest) {
        Result<String> result = new Result<>();
        result.setTimestamp(new Date());
        try {
//            GLLine checkHave = findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(changeDocumentRequest.getCompCode(), changeDocumentRequest.getAccountDocNo(), changeDocumentRequest.getFiscalYear(), "K");
//            if (checkHave != null) {
//                checkHave.setPaymentBlock(changeDocumentRequest.getPaymentBlock());
//                checkHave.setAssignment(changeDocumentRequest.getAssignment());
//                checkHave.setLineDesc(changeDocumentRequest.getLineDesc());
//                checkHave.setReference1(changeDocumentRequest.getReference1());
//                checkHave.setBankAccountNo(changeDocumentRequest.getBankAccNo());
//                glLineRepository.save(checkHave);
//            }
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public void updateBlockStatusBatch(List<UnBlockDocument> unBlockDocuments) {
        glLineRepository.updateBlockStatusBatch(unBlockDocuments);
    }

    @Override
    public void updateBlockStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String paymentBlock, boolean approve, String username, Timestamp updateDate) {
        if (approve) {
            this.glLineRepository.updateBlockStatus(companyCode, originalDocumentNo, originalFiscalYear, "K", "B%", paymentBlock, username, updateDate);
        } else {
            this.glLineRepository.updateBlockStatus(companyCode, originalDocumentNo, originalFiscalYear, "K", "E%", paymentBlock, username, updateDate);
        }
    }

    @Override
    public void updateBlockStatusErrorCase(String companyCode, String originalDocumentNo, String originalFiscalYear, String username, Timestamp updateDate) {
        this.glLineRepository.updateBlockStatusErrorCase(companyCode, originalDocumentNo, originalFiscalYear, username, updateDate);

    }


}
