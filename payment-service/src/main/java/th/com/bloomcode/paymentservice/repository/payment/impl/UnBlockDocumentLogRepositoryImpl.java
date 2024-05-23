package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.request.SearchUnBlockDocumentLogRequest;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.UnBlockDocumentLogRepository;
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
public class UnBlockDocumentLogRepositoryImpl extends MetadataJdbcRepository<UnBlockDocumentLog, Long> implements UnBlockDocumentLogRepository {

  private final JdbcTemplate jdbcTemplate;
  static BeanPropertyRowMapper<UnBlockDocumentLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(UnBlockDocumentLog.class);

  static Updater<UnBlockDocumentLog> unBlockDocumentLogUpdater =
      (t, mapping) -> {
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_UNBLOCK_DOCUMENT_LOG_ID, t.getId());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_VALUE_OLD, t.getValueOld());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_VALUE_NEW, t.getValueNew());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_UNBLOCK_DATE, t.getUnblockDate());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_FI_AREA, t.getFiArea());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_PAYMENT_CENTER, t.getPaymentCenter());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_DOCUMENT_TYPE, t.getDocumentType());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, t.getOriginalDocumentNo());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_DATE_ACCT, t.getDateAcct());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_PAYMENT_METHOD, t.getPaymentMethod());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_AMOUNT, t.getAmount());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_WTX_TYPE, t.getWtxType());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_WTX_CODE, t.getWtxCode());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_WTX_BASE, t.getWtxBase());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_WTX_AMOUNT, t.getWtxAmount());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_WTX_TYPE_P, t.getWtxTypeP());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_WTX_CODE_P, t.getWtxCodeP());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_WTX_BASE_P, t.getWtxBaseP());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_WTX_AMOUNT_P, t.getWtxAmountP());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_VENDOR, t.getVendor());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_VENDOR_NAME, t.getVendorName());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_USER_POST, t.getUserPost());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_USER_NAME, t.getUserName());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_REASON, t.getReason());
        mapping.put(UnBlockDocumentLog.COLUMN_NAME_IDEM_STATUS, t.getIdemStatus());
      };
  static Map<String, Integer> updaterType =
      Map.ofEntries(
          entry(UnBlockDocumentLog.COLUMN_NAME_UNBLOCK_DOCUMENT_LOG_ID, Types.BIGINT),
          entry(UnBlockDocumentLog.COLUMN_NAME_VALUE_OLD, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_VALUE_NEW, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_UNBLOCK_DATE, Types.TIMESTAMP),
          entry(UnBlockDocumentLog.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_FI_AREA, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_PAYMENT_CENTER, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_DOCUMENT_TYPE, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_DATE_ACCT, Types.TIMESTAMP),
          entry(UnBlockDocumentLog.COLUMN_NAME_PAYMENT_METHOD, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_AMOUNT, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_WTX_TYPE, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_WTX_CODE, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_WTX_BASE, Types.NUMERIC),
          entry(UnBlockDocumentLog.COLUMN_NAME_WTX_AMOUNT, Types.NUMERIC),
          entry(UnBlockDocumentLog.COLUMN_NAME_WTX_TYPE_P, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_WTX_CODE_P, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_WTX_BASE_P, Types.NUMERIC),
          entry(UnBlockDocumentLog.COLUMN_NAME_WTX_AMOUNT_P, Types.NUMERIC),
          entry(UnBlockDocumentLog.COLUMN_NAME_VENDOR, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_VENDOR_NAME, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_USER_POST, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_USER_NAME, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_CREATED, Types.TIMESTAMP),
          entry(UnBlockDocumentLog.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_REASON, Types.NVARCHAR),
          entry(UnBlockDocumentLog.COLUMN_NAME_IDEM_STATUS, Types.NVARCHAR)
      );

  public UnBlockDocumentLogRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
    super(beanPropertyRowMapper, unBlockDocumentLogUpdater, updaterType, UnBlockDocumentLog.TABLE_NAME, UnBlockDocumentLog.COLUMN_NAME_UNBLOCK_DOCUMENT_LOG_ID, jdbcTemplate);
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<UnBlockDocumentLog> findLogByCondition(SearchUnBlockDocumentLogRequest request) {
    List<Object> params = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + UnBlockDocumentLog.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(" 1=1 ");

    if (!Util.isEmpty(request.getCompanyCodeFrom())) {
      sql.append(SqlUtil.whereClauseRange(request.getCompanyCodeFrom(), request.getCompanyCodeTo(), UnBlockDocumentLog.COLUMN_NAME_COMPANY_CODE, params));
    }
    if (!Util.isEmpty(request.getDocumentTypeFrom())) {
      sql.append(SqlUtil.whereClauseRange(request.getDocumentTypeFrom(), request.getDocumentTypeTo(), UnBlockDocumentLog.COLUMN_NAME_DOCUMENT_TYPE, params));
    }
    if (!Util.isEmpty(request.getOriginalDocumentNoFrom())) {
      sql.append(SqlUtil.whereClauseRange(request.getOriginalDocumentNoFrom(), request.getOriginalDocumentNoFrom(), UnBlockDocumentLog.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, params));
    }
    if (!Util.isEmpty(request.getOriginalFiscalYearFrom())) {
      sql.append(SqlUtil.whereClauseRange(request.getOriginalFiscalYearFrom(), request.getOriginalFiscalYearTo(), UnBlockDocumentLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, params));
    }
    if (!Util.isEmpty(request.getDateAcctFrom())) {
      sql.append(SqlUtil.whereClauseRange(request.getDateAcctFrom(), request.getDateAcctTo(), UnBlockDocumentLog.COLUMN_NAME_DATE_ACCT, params));
    }
    if (!Util.isEmpty(request.getUnblockDateFrom())) {
      sql.append(SqlUtil.whereClauseRange(request.getUnblockDateFrom(), request.getUnblockDateTo(), UnBlockDocumentLog.COLUMN_NAME_UNBLOCK_DATE, params));
    }
    if (!Util.isEmpty(request.getValueOld())) {
      sql.append(SqlUtil.whereClause(request.getValueOld(), UnBlockDocumentLog.COLUMN_NAME_VALUE_OLD, params));
    }
    if (!Util.isEmpty(request.getValueNew())) {
      sql.append(SqlUtil.whereClause(request.getValueNew(), UnBlockDocumentLog.COLUMN_NAME_VALUE_NEW, params));
    }

//        if (!Util.isEmpty(request.getFiAreaFrom())) {
//            sql.append(SqlUtil.whereClauseRange(request.getFiAreaFrom(), request.getFiAreaTo(), "FI_AREA", params));
//        }
//
//        if (!Util.isEmpty(request.getPaymentCenterFrom())) {
//            sql.append(SqlUtil.whereClauseRange(request.getPaymentCenterFrom(), request.getPaymentCenterTo(), "PAYMENT_CENTER", params));
//        }

//        if (!Util.isEmpty(request.getUsername())) {
//            sql.append(SqlUtil.whereClause(request.getUsername(), "USERNAME", params));
//        }


    sql.append(" ORDER BY ");
    sql.append(" ORIGINAL_FISCAL_YEAR DESC , ORIGINAL_DOCUMENT_NO ASC  ");

    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);


    log.info("sql find One {}", sql.toString());
    return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
  }

  @Override
  public List<UnBlockDocumentLog> findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear) {
    List<Object> params = new ArrayList<>();
    params.add(companyCode);
    params.add(originalDocumentNo);
    params.add(originalFiscalYear);
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + UnBlockDocumentLog.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(UnBlockDocumentLog.COLUMN_NAME_COMPANY_CODE + " = ? ");
    sql.append(" AND " + UnBlockDocumentLog.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ? ");
    sql.append(" AND " + UnBlockDocumentLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR + " = ? ");
    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear {}", sql.toString());
    return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
  }

  @Override
  public void saveBatch(List<UnBlockDocumentLog> unBlockDocumentLogs) {
    final int batchSize = 30000;
    List<List<UnBlockDocumentLog>> unBlockDocumentLogsBatches = Lists.partition(unBlockDocumentLogs, batchSize);
    final String sqlSave = "INSERT /*+ ENABLE_PARALLEL_DML */ INTO UNBLOCK_DOCUMENT_LOG (ID, AMOUNT, COMPANY_CODE, CREATED, CREATED_BY, DATE_ACCT, DOCUMENT_TYPE, FI_AREA, " +
        "ORIGINAL_DOCUMENT_NO, ORIGINAL_FISCAL_YEAR, PAYMENT_CENTER, PAYMENT_METHOD, " +
        "UNBLOCK_DATE, UPDATED, UPDATED_BY, USER_NAME, USER_POST, VALUE_NEW, VALUE_OLD, VENDOR, " +
        "VENDOR_NAME, WTX_AMOUNT, WTX_AMOUNT_P, WTX_BASE, WTX_BASE_P, WTX_CODE, WTX_CODE_P, " +
        "WTX_TYPE, WTX_TYPE_P, IDEM_STATUS, REASON, GROUP_DOC) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    for (List<UnBlockDocumentLog> batch : unBlockDocumentLogsBatches) {
      this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i)
            throws SQLException {
          int index = 0;
          UnBlockDocumentLog unBlockDocumentLog = batch.get(i);
          ps.setLong(++index, unBlockDocumentLog.getId());
          ps.setBigDecimal(++index, unBlockDocumentLog.getAmount());
          ps.setString(++index, unBlockDocumentLog.getCompanyCode());
          ps.setTimestamp(++index, unBlockDocumentLog.getCreated());
          ps.setString(++index, unBlockDocumentLog.getCreatedBy());
          ps.setTimestamp(++index, unBlockDocumentLog.getDateAcct());
          ps.setString(++index, unBlockDocumentLog.getDocumentType());
          ps.setString(++index, unBlockDocumentLog.getFiArea());
          ps.setString(++index, unBlockDocumentLog.getOriginalDocumentNo());
          ps.setString(++index, unBlockDocumentLog.getOriginalFiscalYear());
          ps.setString(++index, unBlockDocumentLog.getPaymentCenter());
          ps.setString(++index, unBlockDocumentLog.getPaymentMethod());
          ps.setTimestamp(++index, unBlockDocumentLog.getUnblockDate());
          ps.setTimestamp(++index, unBlockDocumentLog.getUpdated());
          ps.setString(++index, unBlockDocumentLog.getUpdatedBy());
          ps.setString(++index, unBlockDocumentLog.getUserName());
          ps.setString(++index, unBlockDocumentLog.getUserPost());
          ps.setString(++index, unBlockDocumentLog.getValueNew());
          ps.setString(++index, unBlockDocumentLog.getValueOld());
          ps.setString(++index, unBlockDocumentLog.getVendor());
          ps.setString(++index, unBlockDocumentLog.getVendorName());
          ps.setBigDecimal(++index, unBlockDocumentLog.getWtxAmount());
          ps.setBigDecimal(++index, unBlockDocumentLog.getWtxAmountP());
          ps.setBigDecimal(++index, unBlockDocumentLog.getWtxBase());
          ps.setBigDecimal(++index, unBlockDocumentLog.getWtxBaseP());
          ps.setString(++index, unBlockDocumentLog.getWtxCode());
          ps.setString(++index, unBlockDocumentLog.getWtxCodeP());
          ps.setString(++index, unBlockDocumentLog.getWtxType());
          ps.setString(++index, unBlockDocumentLog.getWtxTypeP());
          ps.setString(++index, unBlockDocumentLog.getIdemStatus());
          ps.setString(++index, unBlockDocumentLog.getReason());
          ps.setString(++index, unBlockDocumentLog.getGroupDoc());
        }

        @Override
        public int getBatchSize() {
          return batch.size();
        }
      });
    }
  }

  @Override
  public void updateBatch(List<UnBlockDocumentLog> unBlockDocumentLogs) {
    final int batchSize = 30000;
    List<List<UnBlockDocumentLog>> unBlockDocumentLogsBatches = Lists.partition(unBlockDocumentLogs, batchSize);
    final String sqlSave = "UPDATE /*+ ENABLE_PARALLEL_DML */ UNBLOCK_DOCUMENT_LOG SET GROUP_DOC = ?, VALUE_NEW = ? WHERE ID = ?";
    for (List<UnBlockDocumentLog> batch : unBlockDocumentLogsBatches) {
      this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i)
            throws SQLException {
          int index = 0;
          UnBlockDocumentLog unBlockDocumentLog = batch.get(i);
          ps.setString(++index, unBlockDocumentLog.getGroupDoc());
          ps.setString(++index, unBlockDocumentLog.getValueNew());
          ps.setLong(++index, unBlockDocumentLog.getId());
        }

        @Override
        public int getBatchSize() {
          return batch.size();
        }
      });
    }
  }

  @Override
  public void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus, String newValue, String username, Timestamp updateDate) {
    final String sqlSave = "UPDATE UNBLOCK_DOCUMENT_LOG SET IDEM_STATUS = ? , VALUE_NEW = ?, UPDATED_BY = ?, UPDATED = ? WHERE COMPANY_CODE = ? AND ORIGINAL_DOCUMENT_NO = ? AND ORIGINAL_FISCAL_YEAR = ?";
    this.jdbcTemplate.update(sqlSave, idemStatus, newValue, username, updateDate, companyCode, originalDocumentNo, originalFiscalYear);
  }
}
