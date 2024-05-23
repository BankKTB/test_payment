package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocument;
import th.com.bloomcode.paymentservice.model.request.MassChangeDocumentRequest;
import th.com.bloomcode.paymentservice.repository.payment.MassChangeDocumentRepository;
import th.com.bloomcode.paymentservice.service.payment.MassChangeDocumentService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MassChangeDocumentServiceImpl implements MassChangeDocumentService {
    private final MassChangeDocumentRepository massChangeDocumentRepository;

    public MassChangeDocumentServiceImpl(MassChangeDocumentRepository massChangeDocumentRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.massChangeDocumentRepository = massChangeDocumentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void save(MassChangeDocument massChangeDocument) {

        if (null == massChangeDocument.getId() || 0 == massChangeDocument.getId()) {
            massChangeDocument.setId(SqlUtil.getNextSeries(jdbcTemplate, MassChangeDocument.TABLE_NAME + MassChangeDocument.SEQ, null));
            massChangeDocument.setCreated(new Timestamp(System.currentTimeMillis()));
            massChangeDocument.setCreatedBy(massChangeDocument.getUserPost());
        } else {
            massChangeDocument.setUpdated(new Timestamp(System.currentTimeMillis()));
            massChangeDocument.setUpdatedBy(massChangeDocument.getUserPost());
        }
        massChangeDocumentRepository.save(massChangeDocument);
    }

    @Override
    public ResponseEntity<Result<List<MassChangeDocument>>> findByCondition(String value) {
        return null;
    }

    @Override
    public  List<MassChangeDocument> findByListDocument(List<MassChangeDocumentRequest> request){
        return massChangeDocumentRepository.findByListDocument(request);
    }

    @Override
    public List<MassChangeDocument> findByListDocument(String uuid) {
        return massChangeDocumentRepository.findByListDocument(uuid);
    }

    @Override
    public void updateStatus(String companyCode, String documentNo, String fiscalYear, String status, String username, Timestamp updateDate) {
        massChangeDocumentRepository.updateStatus(companyCode, documentNo, fiscalYear, status, username, updateDate);
    }

    @Override
    public void saveBatch(List<MassChangeDocument> massChangeDocuments) {
        massChangeDocumentRepository.saveBatch(massChangeDocuments);
    }

    @Override
    public void updateBatch(List<MassChangeDocument> massChangeDocuments) {
        massChangeDocumentRepository.updateBatch(massChangeDocuments);
    }

    @Override
    public MassChangeDocument findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear) {
        List<MassChangeDocument> massChangeDocumentList = massChangeDocumentRepository.findOneByCompanyCodeAndDocumentNoAndFiscalYear(companyCode, documentNo, fiscalYear);
        if (massChangeDocumentList.size() > 0) {
            return massChangeDocumentList.get(0);
        } else {
            return null;
        }
    }
}
