package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentMQ;
import th.com.bloomcode.paymentservice.model.request.UnBlockChangeDocumentRequest;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.UnBlockDocumentMQRepository;

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
public class UnBlockDocumentMQRepositoryImpl extends MetadataJdbcRepository<UnBlockDocumentMQ, Long> implements UnBlockDocumentMQRepository {

    private final JdbcTemplate jdbcTemplate;
    static BeanPropertyRowMapper<UnBlockDocumentMQ> beanPropertyRowMapper = new BeanPropertyRowMapper<>(UnBlockDocumentMQ.class);

    static Updater<UnBlockDocumentMQ> unBlockDocumentLogUpdater =
            (t, mapping) -> {
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_UNBLOCK_DOCUMENT_LOG_ID, t.getId());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_VALUE_OLD, t.getValueOld());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_VALUE_NEW, t.getValueNew());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_UNBLOCK_DATE, t.getUnblockDate());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_FI_AREA, t.getFiArea());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_PAYMENT_CENTER, t.getPaymentCenter());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_DOCUMENT_TYPE, t.getDocumentType());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, t.getOriginalDocumentNo());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_DATE_ACCT, t.getDateAcct());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_PAYMENT_METHOD, t.getPaymentMethod());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_AMOUNT, t.getAmount());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_WTX_TYPE, t.getWtxType());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_WTX_CODE, t.getWtxCode());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_WTX_BASE, t.getWtxBase());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_WTX_AMOUNT, t.getWtxAmount());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_WTX_TYPE_P, t.getWtxTypeP());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_WTX_CODE_P, t.getWtxCodeP());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_WTX_BASE_P, t.getWtxBaseP());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_WTX_AMOUNT_P, t.getWtxAmountP());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_VENDOR, t.getVendor());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_VENDOR_NAME, t.getVendorName());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_USER_POST, t.getUserPost());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_USER_NAME, t.getUserName());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_CREATED, t.getCreated());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_REASON, t.getReason());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_IDEM_STATUS, t.getIdemStatus());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_REVERSE_COMPANY_CODE, t.getReverseCompanyCode());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_REVERSE_DOCUMENT_TYPE, t.getReverseDocumentType());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO, t.getReverseOriginalDocumentNo());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR, t.getReverseOriginalFiscalYear());
                mapping.put(UnBlockDocumentMQ.COLUMN_NAME_GROUP_DOC, t.getGroupDoc());
            };
    static Map<String, Integer> updaterType =
            Map.ofEntries(
                    entry(UnBlockDocumentMQ.COLUMN_NAME_UNBLOCK_DOCUMENT_LOG_ID, Types.BIGINT),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_VALUE_OLD, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_VALUE_NEW, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_UNBLOCK_DATE, Types.TIMESTAMP),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_FI_AREA, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_PAYMENT_CENTER, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_DOCUMENT_TYPE, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_DATE_ACCT, Types.TIMESTAMP),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_PAYMENT_METHOD, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_AMOUNT, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_WTX_TYPE, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_WTX_CODE, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_WTX_BASE, Types.NUMERIC),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_WTX_AMOUNT, Types.NUMERIC),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_WTX_TYPE_P, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_WTX_CODE_P, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_WTX_BASE_P, Types.NUMERIC),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_WTX_AMOUNT_P, Types.NUMERIC),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_VENDOR, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_VENDOR_NAME, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_USER_POST, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_USER_NAME, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_CREATED, Types.TIMESTAMP),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_REASON, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_IDEM_STATUS, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_REVERSE_COMPANY_CODE, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_REVERSE_DOCUMENT_TYPE, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
                    entry(UnBlockDocumentMQ.COLUMN_NAME_GROUP_DOC, Types.NVARCHAR)
            );

    public UnBlockDocumentMQRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, unBlockDocumentLogUpdater, updaterType, UnBlockDocumentMQ.TABLE_NAME, UnBlockDocumentMQ.COLUMN_NAME_UNBLOCK_DOCUMENT_LOG_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UnBlockDocumentMQ> findMQLogByCondition(List<UnBlockChangeDocumentRequest> request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + UnBlockDocumentMQ.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(" 1=1 ");


        for (int i = 0; i < request.size(); i++) {
            String checkOptionExclude = "";
            if (i == 0) {
                checkOptionExclude = "AND (";
            } else {
                checkOptionExclude = "OR";
            }
            params.add(request.get(i).getCompanyCode());
            params.add(request.get(i).getOriginalDocumentNo());
            params.add(request.get(i).getOriginalFiscalYear());
            sql.append(checkOptionExclude + "  (UPPER(COMPANY_CODE) LIKE UPPER(?) AND UPPER(ORIGINAL_DOCUMENT_NO) LIKE UPPER(?) AND UPPER(ORIGINAL_FISCAL_YEAR) LIKE UPPER(?)) ");

        }
        sql.append(")");


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);


        log.info("sql log mq {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<UnBlockDocumentMQ> findMQLogByCondition(String uuid) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + UnBlockDocumentMQ.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(" GROUP_DOC = ? ");

        params.add(uuid);

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);


        log.info("sql log mq {}", sql);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<UnBlockDocumentMQ> findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear) {
        List<Object> params = new ArrayList<>();
        params.add(companyCode);
        params.add(originalDocumentNo);
        params.add(originalFiscalYear);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + UnBlockDocumentMQ.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(UnBlockDocumentMQ.COLUMN_NAME_COMPANY_CODE + " = ? ");
        sql.append(" AND " + UnBlockDocumentMQ.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ? ");
        sql.append(" AND " + UnBlockDocumentMQ.COLUMN_NAME_ORIGINAL_FISCAL_YEAR + " = ? ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public void saveBatch(List<UnBlockDocumentMQ> unBlockDocumentMQs) {
        final int batchSize = 30000;
        List<List<UnBlockDocumentMQ>> UnBlockDocumentMQsBatches = Lists.partition(unBlockDocumentMQs, batchSize);
        final String sqlSave = "INSERT /*+ ENABLE_PARALLEL_DML */ INTO UNBLOCK_DOCUMENT_MQ (ID, AMOUNT, COMPANY_CODE, CREATED, CREATED_BY, DATE_ACCT, DOCUMENT_TYPE, FI_AREA, " +
                "ORIGINAL_DOCUMENT_NO, ORIGINAL_FISCAL_YEAR, PAYMENT_CENTER, PAYMENT_METHOD, " +
                "UNBLOCK_DATE, UPDATED, UPDATED_BY, USER_NAME, USER_POST, VALUE_NEW, VALUE_OLD, VENDOR, " +
                "VENDOR_NAME, WTX_AMOUNT, WTX_AMOUNT_P, WTX_BASE, WTX_BASE_P, WTX_CODE, WTX_CODE_P, " +
                "WTX_TYPE, WTX_TYPE_P, IDEM_STATUS, " +
                "REASON, REVERSE_ORIGINAL_DOCUMENT_NO, REVERSE_COMPANY_CODE, REVERSE_DOCUMENT_TYPE, " +
                "REVERSE_ORIGINAL_FISCAL_YEAR, GROUP_DOC) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for(List<UnBlockDocumentMQ> batch : UnBlockDocumentMQsBatches) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    int index = 0;
                    UnBlockDocumentMQ unBlockDocumentMQ = batch.get(i);
                    ps.setLong(++index, unBlockDocumentMQ.getId());
                    ps.setBigDecimal(++index, unBlockDocumentMQ.getAmount());
                    ps.setString(++index, unBlockDocumentMQ.getCompanyCode());
                    ps.setTimestamp(++index, unBlockDocumentMQ.getCreated());
                    ps.setString(++index, unBlockDocumentMQ.getCreatedBy());
                    ps.setTimestamp(++index, unBlockDocumentMQ.getDateAcct());
                    ps.setString(++index, unBlockDocumentMQ.getDocumentType());
                    ps.setString(++index, unBlockDocumentMQ.getFiArea());
                    ps.setString(++index, unBlockDocumentMQ.getOriginalDocumentNo());
                    ps.setString(++index, unBlockDocumentMQ.getOriginalFiscalYear());
                    ps.setString(++index, unBlockDocumentMQ.getPaymentCenter());
                    ps.setString(++index, unBlockDocumentMQ.getPaymentMethod());
                    ps.setTimestamp(++index, unBlockDocumentMQ.getUnblockDate());
                    ps.setTimestamp(++index, unBlockDocumentMQ.getUpdated());
                    ps.setString(++index, unBlockDocumentMQ.getUpdatedBy());
                    ps.setString(++index, unBlockDocumentMQ.getUserName());
                    ps.setString(++index, unBlockDocumentMQ.getUserPost());
                    ps.setString(++index, unBlockDocumentMQ.getValueNew());
                    ps.setString(++index, unBlockDocumentMQ.getValueOld());
                    ps.setString(++index, unBlockDocumentMQ.getVendor());
                    ps.setString(++index, unBlockDocumentMQ.getVendorName());
                    ps.setBigDecimal(++index, unBlockDocumentMQ.getWtxAmount());
                    ps.setBigDecimal(++index, unBlockDocumentMQ.getWtxAmountP());
                    ps.setBigDecimal(++index, unBlockDocumentMQ.getWtxBase());
                    ps.setBigDecimal(++index, unBlockDocumentMQ.getWtxBaseP());
                    ps.setString(++index, unBlockDocumentMQ.getWtxCode());
                    ps.setString(++index, unBlockDocumentMQ.getWtxCodeP());
                    ps.setString(++index, unBlockDocumentMQ.getWtxType());
                    ps.setString(++index, unBlockDocumentMQ.getWtxTypeP());
                    ps.setString(++index, unBlockDocumentMQ.getIdemStatus());
                    ps.setString(++index, unBlockDocumentMQ.getReason());
                    ps.setString(++index, unBlockDocumentMQ.getReverseOriginalDocumentNo());
                    ps.setString(++index, unBlockDocumentMQ.getReverseCompanyCode());
                    ps.setString(++index, unBlockDocumentMQ.getReverseDocumentType());
                    ps.setString(++index, unBlockDocumentMQ.getReverseOriginalFiscalYear());
                    ps.setString(++index, unBlockDocumentMQ.getGroupDoc());
                }
                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public void updateBatch(List<UnBlockDocumentMQ> unBlockDocumentMQs) {
        final int batchSize = 30000;
        List<List<UnBlockDocumentMQ>> unBlockDocumentMQsBatches = Lists.partition(unBlockDocumentMQs, batchSize);
        final String sqlSave = "UPDATE /*+ ENABLE_PARALLEL_DML */ UNBLOCK_DOCUMENT_MQ SET GROUP_DOC = ? , IDEM_STATUS = ?, VALUE_NEW = ?, UNBLOCK_DATE = ?,UPDATED = ? WHERE ID = ?";
        for(List<UnBlockDocumentMQ> batch : unBlockDocumentMQsBatches) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    int index = 0;
                    UnBlockDocumentMQ unBlockDocumentMQ = batch.get(i);
                    ps.setString(++index, unBlockDocumentMQ.getGroupDoc());
                    ps.setString(++index, unBlockDocumentMQ.getIdemStatus());
                    ps.setString(++index, unBlockDocumentMQ.getValueNew());
                    ps.setTimestamp(++index, new Timestamp(System.currentTimeMillis()));
                    ps.setTimestamp(++index, new Timestamp(System.currentTimeMillis()));
                    ps.setLong(++index, unBlockDocumentMQ.getId());


                }
                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus, String reverseOriginalDocumentNo, String reverseOriginalFiscalYear, String reverseDocumentType, String reverseCompanyCode, String username, Timestamp updateDate) {
        final String sqlSave = "UPDATE UNBLOCK_DOCUMENT_MQ SET IDEM_STATUS = ?, REVERSE_ORIGINAL_DOCUMENT_NO = ?, REVERSE_ORIGINAL_FISCAL_YEAR = ?, REVERSE_DOCUMENT_TYPE = ?, REVERSE_COMPANY_CODE = ?, UPDATED_BY = ?, UPDATED = ? WHERE COMPANY_CODE = ? AND ORIGINAL_DOCUMENT_NO = ? AND ORIGINAL_FISCAL_YEAR = ?";
        this.jdbcTemplate.update(sqlSave, idemStatus, reverseOriginalDocumentNo, reverseOriginalFiscalYear, reverseDocumentType, reverseCompanyCode, username, updateDate, companyCode, originalDocumentNo, originalFiscalYear);
    }

    @Override
    public void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus, String newValue, String username, Timestamp updateDate) {
        final String sqlSave = "UPDATE UNBLOCK_DOCUMENT_MQ SET IDEM_STATUS = ?,VALUE_NEW = ?, UPDATED_BY = ?, UPDATED = ? WHERE COMPANY_CODE = ? AND ORIGINAL_DOCUMENT_NO = ? AND ORIGINAL_FISCAL_YEAR = ?";
        this.jdbcTemplate.update(sqlSave, idemStatus, newValue, username, updateDate,companyCode, originalDocumentNo, originalFiscalYear);
    }

}
