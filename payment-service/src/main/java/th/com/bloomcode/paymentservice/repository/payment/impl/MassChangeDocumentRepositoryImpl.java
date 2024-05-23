package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocument;
import th.com.bloomcode.paymentservice.model.request.MassChangeDocumentRequest;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.MassChangeDocumentRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class MassChangeDocumentRepositoryImpl extends MetadataJdbcRepository<MassChangeDocument, Long> implements MassChangeDocumentRepository {
    static BeanPropertyRowMapper<MassChangeDocument> beanPropertyRowMapper = new BeanPropertyRowMapper<>(MassChangeDocument.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<MassChangeDocument> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(MassChangeDocument.COLUMN_NAME_MASS_CHANGE_DOCUMENT_ID, t.getId());
        mapping.put(MassChangeDocument.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(MassChangeDocument.COLUMN_NAME_DOCUMENT_NO, t.getDocumentNo());
        mapping.put(MassChangeDocument.COLUMN_NAME_FISCAL_YEAR, t.getFiscalYear());
        mapping.put(MassChangeDocument.COLUMN_NAME_PAYMENT_BLOCK, t.getPaymentBlock());
        mapping.put(MassChangeDocument.COLUMN_NAME_STATUS, t.getStatus());
        mapping.put(MassChangeDocument.COLUMN_NAME_USER_POST, t.getUserPost());
        mapping.put(MassChangeDocument.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(MassChangeDocument.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(MassChangeDocument.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(MassChangeDocument.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(MassChangeDocument.COLUMN_NAME_GROUP_DOC, t.getGroupDoc());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(MassChangeDocument.COLUMN_NAME_MASS_CHANGE_DOCUMENT_ID, Types.BIGINT),
            entry(MassChangeDocument.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
            entry(MassChangeDocument.COLUMN_NAME_DOCUMENT_NO, Types.NVARCHAR),
            entry(MassChangeDocument.COLUMN_NAME_FISCAL_YEAR, Types.NVARCHAR),
            entry(MassChangeDocument.COLUMN_NAME_PAYMENT_BLOCK, Types.NVARCHAR),
            entry(MassChangeDocument.COLUMN_NAME_USER_POST, Types.NVARCHAR),
            entry(MassChangeDocument.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(MassChangeDocument.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
            entry(MassChangeDocument.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(MassChangeDocument.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
            entry(MassChangeDocument.COLUMN_NAME_STATUS, Types.NVARCHAR),
            entry(MassChangeDocument.COLUMN_NAME_GROUP_DOC, Types.NVARCHAR)
    );


    public MassChangeDocumentRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, MassChangeDocument.TABLE_NAME, MassChangeDocument.COLUMN_NAME_MASS_CHANGE_DOCUMENT_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MassChangeDocument> findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + MassChangeDocument.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(" 1=1 ");


        if (!Util.isEmpty(companyCode)) {
            sql.append(SqlUtil.whereClause(companyCode, "COMPANY_CODE", params));
        }
        if (!Util.isEmpty(documentNo)) {
            sql.append(SqlUtil.whereClause(documentNo, "DOCUMENT_NO", params));
        }
        if (!Util.isEmpty(fiscalYear)) {
            sql.append(SqlUtil.whereClause(fiscalYear, "FISCAL_YEAR", params));
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

        log.info("sql find One {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<MassChangeDocument> findByListDocument(List<MassChangeDocumentRequest> request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + MassChangeDocument.TABLE_NAME);
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
    public List<MassChangeDocument> findByListDocument(String uuid) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + MassChangeDocument.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(" 1=1 ");

        if (!Util.isEmpty(uuid)) {
            params.add(uuid);
            sql.append("  AND (UPPER(GROUP_DOC) LIKE UPPER(?)) ");
        }



        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        log.info("sql find One {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public void updateStatus(String companyCode, String documentNo, String fiscalYear, String status, String username, Timestamp updateDate) {
        final String sqlSave = "UPDATE MASS_CHANGE_DOCUMENT SET STATUS = ?, UPDATED_BY = ?, UPDATED = ? WHERE COMPANY_CODE = ? AND DOCUMENT_NO = ? AND FISCAL_YEAR = ?";
        this.jdbcTemplate.update(sqlSave, status, username, updateDate, companyCode, documentNo, fiscalYear);
    }

    @Override
    public void saveBatch(List<MassChangeDocument> massChangeDocuments) {
        final int batchSize = 30000;
        List<List<MassChangeDocument>> massChangeDocumentsBatches = Lists.partition(massChangeDocuments, batchSize);
        final String sqlSave = "INSERT /*+ ENABLE_PARALLEL_DML */ INTO MASS_CHANGE_DOCUMENT (COMPANY_CODE, DOCUMENT_NO, FISCAL_YEAR, PAYMENT_BLOCK, USER_POST, " +
                "CREATED_BY, UPDATED_BY, CREATED, UPDATED, STATUS, GROUP_DOC) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for(List<MassChangeDocument> batch : massChangeDocumentsBatches) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    int index = 0;
                    MassChangeDocument massChangeDocument = batch.get(i);
                    ps.setString(++index, massChangeDocument.getCompanyCode());
                    ps.setString(++index, massChangeDocument.getDocumentNo());
                    ps.setString(++index, massChangeDocument.getFiscalYear());
                    ps.setString(++index, massChangeDocument.getPaymentBlock());
                    ps.setString(++index, massChangeDocument.getUserPost());
                    ps.setString(++index, massChangeDocument.getCreatedBy());
                    ps.setString(++index, massChangeDocument.getUpdatedBy());
                    ps.setTimestamp(++index, massChangeDocument.getCreated());
                    ps.setTimestamp(++index, massChangeDocument.getUpdated());
                    ps.setString(++index, massChangeDocument.getStatus());
                    ps.setString(++index, massChangeDocument.getGroupDoc());
                }
                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public void updateBatch(List<MassChangeDocument> massChangeDocuments) {
        final int batchSize = 30000;
        List<List<MassChangeDocument>> massChangeDocumentsBatches = Lists.partition(massChangeDocuments, batchSize);
        final String sqlSave = "UPDATE /*+ ENABLE_PARALLEL_DML */ MASS_CHANGE_DOCUMENT SET GROUP_DOC = ?, STATUS = ?, UPDATED = ?, UPDATED_BY = ?,PAYMENT_BLOCK = ? WHERE ID = ?";
        for(List<MassChangeDocument> batch : massChangeDocumentsBatches) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    int index = 0;
                    MassChangeDocument massChangeDocument = batch.get(i);
                    ps.setString(++index, massChangeDocument.getGroupDoc());
                    ps.setString(++index, massChangeDocument.getStatus());
                    ps.setTimestamp(++index, massChangeDocument.getUpdated());
                    ps.setString(++index, massChangeDocument.getUpdatedBy());
                    ps.setString(++index, massChangeDocument.getPaymentBlock());
                    ps.setLong(++index, massChangeDocument.getId());
                }
                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }
}
