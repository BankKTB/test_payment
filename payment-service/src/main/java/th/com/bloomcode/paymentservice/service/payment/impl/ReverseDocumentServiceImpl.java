package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocument;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;
import th.com.bloomcode.paymentservice.repository.payment.ReverseDocumentRepository;
import th.com.bloomcode.paymentservice.service.payment.ReverseDocumentService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class ReverseDocumentServiceImpl implements ReverseDocumentService {
    private final ReverseDocumentRepository reverseDocumentRepository;

    public ReverseDocumentServiceImpl(ReverseDocumentRepository reverseDocumentRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.reverseDocumentRepository = reverseDocumentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(ReverseDocument reverseDocument) {

        if (null == reverseDocument.getId() || 0 == reverseDocument.getId()) {
            reverseDocument.setId(SqlUtil.getNextSeries(jdbcTemplate, ReverseDocument.TABLE_NAME + GLHead.SEQ, null));
            reverseDocument.setCreated(new Timestamp(System.currentTimeMillis()));
            reverseDocument.setCreatedBy(reverseDocument.getUserPost());
        } else {
            reverseDocument.setUpdated(new Timestamp(System.currentTimeMillis()));
            reverseDocument.setUpdatedBy(reverseDocument.getUserPost());
        }
        log.info(" save ReverseDocument {} ",reverseDocument);
        reverseDocumentRepository.save(reverseDocument);
    }

    @Override
    public ResponseEntity<Result<List<ReverseDocument>>> findByCondition(String value) {
        return null;
    }

    @Override
    public  List<ReverseDocument> findByListDocument(List<ReverseDocumentRequest> request){
        return reverseDocumentRepository.findByListDocument(request);
    }
    @Override
    public  List<ReverseDocument> findByListDocument(String uuid){
        return reverseDocumentRepository.findByListDocument(uuid);
    }

    @Override
    public void saveBatch(List<ReverseDocument> reverseDocuments) {
        reverseDocumentRepository.saveBatch(reverseDocuments);
    }

    @Override
    public ReverseDocument findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear) {
        List<ReverseDocument> reverseDocumentList = reverseDocumentRepository.findOneByCompanyCodeAndDocumentNoAndFiscalYear(companyCode, documentNo, fiscalYear);
        if (!reverseDocumentList.isEmpty()) {
            return reverseDocumentList.get(0);
        } else {
            return null;
        }
    }
}
