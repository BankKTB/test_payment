package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.model.response.SelectGroupDocumentDetailResponse;
import th.com.bloomcode.paymentservice.model.response.SelectGroupDocumentPreviewResponse;
import th.com.bloomcode.paymentservice.model.response.SelectGroupDocumentResponse;
import th.com.bloomcode.paymentservice.repository.payment.SelectGroupDocumentRepository;
import th.com.bloomcode.paymentservice.repository.payment.UnBlockDocumentRepository;
import th.com.bloomcode.paymentservice.service.payment.GLHeadService;
import th.com.bloomcode.paymentservice.service.payment.SelectGroupDocumentService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SelectGroupDocumentServiceImpl implements SelectGroupDocumentService {
    private final SelectGroupDocumentRepository selectGroupDocumentRepository;
    private final UnBlockDocumentRepository unBlockDocumentRepository;
    private final GLHeadService glHeadService;

    private final JdbcTemplate jdbcTemplate;

    public SelectGroupDocumentServiceImpl(SelectGroupDocumentRepository selectGroupDocumentRepository, UnBlockDocumentRepository unBlockDocumentRepository, GLHeadService glHeadService, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.selectGroupDocumentRepository = selectGroupDocumentRepository;
        this.unBlockDocumentRepository = unBlockDocumentRepository;
        this.glHeadService = glHeadService;
        this.jdbcTemplate = jdbcTemplate;
    }


    public SelectGroupDocument save(SelectGroupDocument selectGroupDocument) {
        if (null == selectGroupDocument.getId() || 0 == selectGroupDocument.getId()) {
            selectGroupDocument.setId(SqlUtil.getNextSeries(jdbcTemplate, SelectGroupDocument.TABLE_NAME + SelectGroupDocument.SEQ, null));
            selectGroupDocument.setCreated(new Timestamp(System.currentTimeMillis()));
            selectGroupDocument.setCreatedBy("SYSTEM");
        } else {
            selectGroupDocument.setUpdated(new Timestamp(System.currentTimeMillis()));
            selectGroupDocument.setUpdatedBy("SYSTEM");
        }
        return selectGroupDocumentRepository.save(selectGroupDocument);
    }

    @Override
    public ResponseEntity<Result<SelectGroupDocumentResponse>> create(SelectGroupDocument request) {
        Result<SelectGroupDocumentResponse> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            glHeadService.updateSelectGroupDocument(request);
            save(request);

            SelectGroupDocumentResponse selectGroupDocumentResponse = new SelectGroupDocumentResponse();

            List<UnBlockDocument> unBlock = unBlockDocumentRepository.previewSelectGroupDocument(request);

            List<SelectGroupDocumentDetailResponse> success = new ArrayList<>();
            List<SelectGroupDocumentDetailResponse> unSuccess = new ArrayList<>();
            for (UnBlockDocument item : unBlock) {
                SelectGroupDocumentDetailResponse selectGroupDocumentDetailResponse = new SelectGroupDocumentDetailResponse();
                if (!item.getDocumentType().equalsIgnoreCase("K2")) {
                    selectGroupDocumentDetailResponse.setCompanyCode(item.getCompanyCode());
                    selectGroupDocumentDetailResponse.setOriginalDocumentNo(item.getOriginalDocumentNo());
                    selectGroupDocumentDetailResponse.setOriginalFiscalYear(item.getOriginalFiscalYear());
                    selectGroupDocumentDetailResponse.setOriginalDocumentType(item.getDocumentType());
                    selectGroupDocumentDetailResponse.setAmount(item.getAmount());
                    selectGroupDocumentDetailResponse.setOldAssignment(item.getAssignment());
                    selectGroupDocumentDetailResponse.setNewAssignment(item.getSelectGroupDocument());
                    selectGroupDocumentDetailResponse.setRemark(null);
                    success.add(selectGroupDocumentDetailResponse);
                } else {
                    selectGroupDocumentDetailResponse.setCompanyCode(item.getCompanyCode());
                    selectGroupDocumentDetailResponse.setOriginalDocumentNo(item.getOriginalDocumentNo());
                    selectGroupDocumentDetailResponse.setOriginalFiscalYear(item.getOriginalFiscalYear());
                    selectGroupDocumentDetailResponse.setOriginalDocumentType(item.getDocumentType());
                    selectGroupDocumentDetailResponse.setAmount(item.getAmount());
                    selectGroupDocumentDetailResponse.setOldAssignment(item.getAssignment());
                    selectGroupDocumentDetailResponse.setNewAssignment(item.getSelectGroupDocument());
                    selectGroupDocumentDetailResponse.setRemark("ประเภทเอกสาร K2 ห้ามแก้ไข");
                    unSuccess.add(selectGroupDocumentDetailResponse);
                }

            }
            selectGroupDocumentResponse.setSuccess(success);
            selectGroupDocumentResponse.setUnSuccess(unSuccess);

            result.setStatus(HttpStatus.CREATED.value());
            result.setData(selectGroupDocumentResponse);
            return new ResponseEntity<>(result, HttpStatus.OK);

//            List<SelectGroupDocument> selectGroupDocuments = this.selectGroupDocumentRepository.findOneByIndependentSelect(request.getDefineName());
//            SelectGroupDocument checkDuplicate = null;
//            if (selectGroupDocuments.size() > 0) {
//                checkDuplicate = selectGroupDocuments.get(0);
//            }

//            if (null == checkDuplicate) {
//                SelectGroupDocument newSelectGroupDocument = save(request);
//                result.setStatus(HttpStatus.CREATED.value());
//                result.setData(newSelectGroupDocument);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                result.setStatus(HttpStatus.FORBIDDEN.value());
//                result.setData(null);
//                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<SelectGroupDocumentPreviewResponse>> preview(HttpServletRequest httpServletRequest, SelectGroupDocument selectGroupDocument) {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Result<SelectGroupDocumentPreviewResponse> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            SelectGroupDocumentPreviewResponse selectGroupDocumentPreviewResponse = new SelectGroupDocumentPreviewResponse();
            List<UnBlockDocument> unBlock = unBlockDocumentRepository.previewSelectGroupDocument(selectGroupDocument);

            if (unBlock.size() > 0) {
                selectGroupDocumentPreviewResponse.setUnBlockDocuments(unBlock);
                selectGroupDocumentPreviewResponse.setSumAmount(new BigDecimal(unBlock.stream().mapToDouble(item -> Double.parseDouble(item.getAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                result.setStatus(HttpStatus.OK.value());
                result.setData(selectGroupDocumentPreviewResponse);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Override
    public SelectGroupDocument findOneByIndependentSelect(String defineName) {
        try {
            List<SelectGroupDocument> selectGroupDocument = selectGroupDocumentRepository.findOneByIndependentSelect(defineName);
            if (selectGroupDocument.size() > 0) {
                return selectGroupDocument.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
