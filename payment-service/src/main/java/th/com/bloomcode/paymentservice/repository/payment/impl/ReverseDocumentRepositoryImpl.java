package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocument;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocument;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.ReverseDocumentRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class ReverseDocumentRepositoryImpl extends MetadataJdbcRepository<ReverseDocument, Long> implements ReverseDocumentRepository {
    static BeanPropertyRowMapper<ReverseDocument> beanPropertyRowMapper = new BeanPropertyRowMapper<>(ReverseDocument.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<ReverseDocument> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(ReverseDocument.COLUMN_NAME_PAYMENT_DOCUMENT_LOG_ID, t.getId());
        mapping.put(ReverseDocument.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(ReverseDocument.COLUMN_NAME_DOCUMENT_NO, t.getDocumentNo());
        mapping.put(ReverseDocument.COLUMN_NAME_FISCAL_YEAR, t.getFiscalYear());
        mapping.put(ReverseDocument.COLUMN_NAME_DOCUMENT_TYPE, t.getDocumentType());
        mapping.put(ReverseDocument.COLUMN_NAME_REVERSE_COMPANY_CODE, t.getReverseCompanyCode());
        mapping.put(ReverseDocument.COLUMN_NAME_REVERSE_DOCUMENT_NO, t.getReverseDocumentNo());
        mapping.put(ReverseDocument.COLUMN_NAME_REVERSE_FISCAL_YEAR, t.getReverseFiscalYear());
        mapping.put(ReverseDocument.COLUMN_NAME_REVERSE_DOCUMENT_TYPE, t.getReverseDocumentType());
        mapping.put(ReverseDocument.COLUMN_NAME_PO_DOC_NO, t.getPoDocNo());
        mapping.put(ReverseDocument.COLUMN_NAME_STATUS, t.getStatus());
        mapping.put(ReverseDocument.COLUMN_NAME_USER_POST, t.getUserPost());
        mapping.put(ReverseDocument.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(ReverseDocument.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(ReverseDocument.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(ReverseDocument.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(ReverseDocument.COLUMN_NAME_GROUP_DOC, t.getGroupDoc());
        mapping.put(ReverseDocument.COLUMN_NAME_COMPANY_CODE_AGENCY, t.getCompanyCodeAgency());
        mapping.put(ReverseDocument.COLUMN_NAME_DOCUMENT_NO_AGENCY, t.getDocumentNoAgency());
        mapping.put(ReverseDocument.COLUMN_NAME_FISCAL_YEAR_AGENCY, t.getFiscalYearAgency());

    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(ReverseDocument.COLUMN_NAME_PAYMENT_DOCUMENT_LOG_ID, Types.BIGINT),
            entry(ReverseDocument.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_DOCUMENT_NO, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_FISCAL_YEAR, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_DOCUMENT_TYPE, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_REVERSE_COMPANY_CODE, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_REVERSE_DOCUMENT_NO, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_REVERSE_FISCAL_YEAR, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_REVERSE_DOCUMENT_TYPE, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_PO_DOC_NO, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_USER_POST, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(ReverseDocument.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
            entry(ReverseDocument.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_STATUS, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_GROUP_DOC, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_COMPANY_CODE_AGENCY, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_DOCUMENT_NO_AGENCY, Types.NVARCHAR),
            entry(ReverseDocument.COLUMN_NAME_FISCAL_YEAR_AGENCY, Types.NVARCHAR)
    );


    public ReverseDocumentRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, ReverseDocument.TABLE_NAME, ReverseDocument.COLUMN_NAME_PAYMENT_DOCUMENT_LOG_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ReverseDocument> findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + ReverseDocument.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(" 1=1 ");


        if (!Util.isEmpty(companyCode)) {
            sql.append(SqlUtil.whereClauseEqual(companyCode, "COMPANY_CODE", params));
        }
        if (!Util.isEmpty(documentNo)) {
            sql.append(SqlUtil.whereClauseEqual(documentNo, "DOCUMENT_NO", params));
        }
        if (!Util.isEmpty(fiscalYear)) {
            sql.append(SqlUtil.whereClauseEqual(fiscalYear, "FISCAL_YEAR", params));
        }
//
//        if (!Util.isEmpty(request.getPaymentCenterFrom())) {
//            sql.append(SqlUtil.whereClauseRange(request.getPaymentCenterFrom(), request.getPaymentCenterTo(), "PAYMENT_CENTER", params));
//        }

//        if (!Util.isEmpty(request.getUsername())) {
//            sql.append(SqlUtil.whereClause(request.getUsername(), "USERNAME", params));
//        }


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        log.info("sql find One {}", sql);
        log.info("params : {}", Arrays.toString(objParams));
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<ReverseDocument> findByListDocument(List<ReverseDocumentRequest> request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + ReverseDocument.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(" 1=1 ");


        for (int x = 0; x < request.size(); x++) {
            params.add(request.get(x).getCompCode());
            params.add(request.get(x).getAccountDocNo());
            params.add(request.get(x).getFiscalYear());
            if (x == 0) {
                sql.append("  AND ((UPPER(COMPANY_CODE) LIKE UPPER(?)) ");
                sql.append("  AND (UPPER(DOCUMENT_NO) LIKE UPPER(?)) ");
                sql.append("  AND (UPPER(FISCAL_YEAR) LIKE UPPER(?))) ");
            } else {
                sql.append("  OR ((UPPER(COMPANY_CODE) LIKE UPPER(?)) ");
                sql.append("  AND (UPPER(DOCUMENT_NO) LIKE UPPER(?)) ");
                sql.append("  AND (UPPER(FISCAL_YEAR) LIKE UPPER(?))) ");

            }
        }


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        log.info("sql find One {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<ReverseDocument> findByListDocument(String uuid) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + ReverseDocument.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(" 1=1 ");
        params.add(uuid);
        sql.append("  AND ((UPPER(GROUP_DOC) LIKE UPPER(?))) ");


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        log.info("sql find One {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public void saveBatch(List<ReverseDocument> reverseDocuments) {
        try{
            final int batchSize = 30000;
            List<List<ReverseDocument>> reverseDocumentBatches = Lists.partition(reverseDocuments, batchSize);
            final String sqlSave =
                "INSERT /*+ ENABLE_PARALLEL_DML */ INTO REVERSE_DOCUMENT (ID, COMPANY_CODE, DOCUMENT_NO, FISCAL_YEAR, DOCUMENT_TYPE, REVERSE_COMPANY_CODE, " +
                    "                              REVERSE_DOCUMENT_NO, REVERSE_FISCAL_YEAR, REVERSE_DOCUMENT_TYPE, USER_POST, CREATED_BY, " +
                    "                              UPDATED_BY, CREATED, UPDATED, STATUS, COMPANY_CODE_AGENCY, DOCUMENT_NO_AGENCY, " +
                    "                              FISCAL_YEAR_AGENCY, GROUP_DOC, PO_DOC_NO) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            for (List<ReverseDocument> batch : reverseDocumentBatches) {
                this.jdbcTemplate.batchUpdate(
                    sqlSave,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            int index = 0;
                            ReverseDocument reverseDocument = batch.get(i);
                            ps.setLong(++index, reverseDocument.getId());
                            ps.setString(++index, reverseDocument.getCompanyCode());
                            ps.setString(++index, reverseDocument.getDocumentNo());
                            ps.setString(++index, reverseDocument.getFiscalYear());
                            ps.setString(++index, reverseDocument.getDocumentType());
                            ps.setString(++index, reverseDocument.getReverseCompanyCode());
                            ps.setString(++index, reverseDocument.getReverseDocumentNo());
                            ps.setString(++index, reverseDocument.getReverseFiscalYear());
                            ps.setString(++index, reverseDocument.getReverseDocumentType());
                            ps.setString(++index, reverseDocument.getUserPost());
                            ps.setString(++index, reverseDocument.getCreatedBy());
                            ps.setString(++index, reverseDocument.getUpdatedBy());
                            ps.setTimestamp(++index, reverseDocument.getCreated());
                            ps.setTimestamp(++index, reverseDocument.getUpdated());
                            ps.setString(++index, reverseDocument.getStatus());
                            ps.setString(++index, reverseDocument.getCompanyCodeAgency());
                            ps.setString(++index, reverseDocument.getDocumentNoAgency());
                            ps.setString(++index, reverseDocument.getFiscalYearAgency());
                            ps.setString(++index, reverseDocument.getGroupDoc());
                            ps.setString(++index, reverseDocument.getPoDocNo());
                        }

                        @Override
                        public int getBatchSize() {
                            return batch.size();
                        }
                    });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
