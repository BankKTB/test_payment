package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.SmartFileLog;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.SmartFileLogRepository;

import java.sql.Types;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class SmartFileLogRepositoryImpl extends MetadataJdbcRepository<SmartFileLog, Long> implements SmartFileLogRepository {

  static BeanPropertyRowMapper<SmartFileLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(SmartFileLog.class);

  static Updater<SmartFileLog> smartFileLogUpdater = (t, mapping) -> {
    mapping.put(SmartFileLog.COLUMN_NAME_SMART_FILE_LOG_ID, t.getId());
    mapping.put(SmartFileLog.COLUMN_NAME_PAYMENT_DATE, t.getPaymentDate());
    mapping.put(SmartFileLog.COLUMN_NAME_PAYMENT_NAME, t.getPaymentName());
    mapping.put(SmartFileLog.COLUMN_NAME_VENDOR, t.getVendor());
    mapping.put(SmartFileLog.COLUMN_NAME_BANK_KEY, t.getBankKey());
    mapping.put(SmartFileLog.COLUMN_NAME_BANK_ACCOUNT_NO, t.getBankAccountNo());
    mapping.put(SmartFileLog.COLUMN_NAME_PAYMENT_METHOD, t.getPaymentMethod());
    mapping.put(SmartFileLog.COLUMN_NAME_PAYING_COMP_CODE, t.getPayingCompCode());
    mapping.put(SmartFileLog.COLUMN_NAME_PAYMENT_DOC_NO, t.getPaymentDocNo());
    mapping.put(SmartFileLog.COLUMN_NAME_PAYMENT_YEAR, t.getPaymentYear());
    mapping.put(SmartFileLog.COLUMN_NAME_COMP_CODE, t.getCompCode());
    mapping.put(SmartFileLog.COLUMN_NAME_INV_DOC_NO, t.getInvDocNo());
    mapping.put(SmartFileLog.COLUMN_NAME_FISCAL_YEAR, t.getFiscalYear());
    mapping.put(SmartFileLog.COLUMN_NAME_FI_AREA, t.getFiArea());

    mapping.put(SmartFileLog.COLUMN_NAME_TRANSFER_LEVEL, t.getTransferLevel());
    mapping.put(SmartFileLog.COLUMN_NAME_FEE, t.getFee());
    mapping.put(SmartFileLog.COLUMN_NAME_CREDIT_MEMO, t.getCreditMemo());
    mapping.put(SmartFileLog.COLUMN_NAME_ORIGINAL_ACC_DOC_NO, t.getOriginalAccDocNo());
    mapping.put(SmartFileLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
    mapping.put(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT, t.getOriginalWtxAmount());
    mapping.put(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_BASE, t.getOriginalWtxBase());
    mapping.put(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P, t.getOriginalWtxAmountP());
    mapping.put(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_BASE_P, t.getOriginalWtxBaseP());
    mapping.put(SmartFileLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR, t.getPaymentFiscalYear());
    mapping.put(SmartFileLog.COLUMN_NAME_PAYMENT_COMP_CODE, t.getPaymentCompCode());
    mapping.put(SmartFileLog.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, t.getGenerateFileAliasId());
  };

  static Map<String, Integer> updaterType = Map.ofEntries(
          entry(SmartFileLog.COLUMN_NAME_SMART_FILE_LOG_ID, Types.BIGINT),
          entry(SmartFileLog.COLUMN_NAME_PAYMENT_DATE, Types.TIMESTAMP),
          entry(SmartFileLog.COLUMN_NAME_PAYMENT_NAME, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_VENDOR, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_BANK_KEY, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_BANK_ACCOUNT_NO, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_PAYMENT_METHOD, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_PAYING_COMP_CODE, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_PAYMENT_DOC_NO, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_PAYMENT_YEAR, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_COMP_CODE, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_INV_DOC_NO, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_FISCAL_YEAR, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_FI_AREA, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_TRANSFER_LEVEL, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_FEE, Types.NUMERIC),
          entry(SmartFileLog.COLUMN_NAME_CREDIT_MEMO, Types.NUMERIC),
          entry(SmartFileLog.COLUMN_NAME_ORIGINAL_ACC_DOC_NO, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT, Types.NUMERIC),
          entry(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_BASE, Types.NUMERIC),
          entry(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P, Types.NUMERIC),
          entry(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_BASE_P, Types.NUMERIC),
          entry(SmartFileLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_PAYMENT_COMP_CODE, Types.NVARCHAR),
          entry(SmartFileLog.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, Types.BIGINT)
  );

  static RowMapper<SmartFileLog> userRowMapper = (rs, rowNum) -> new SmartFileLog(
          rs.getLong(SmartFileLog.COLUMN_NAME_SMART_FILE_LOG_ID),
          rs.getTimestamp(SmartFileLog.COLUMN_NAME_PAYMENT_DATE),
          rs.getString(SmartFileLog.COLUMN_NAME_PAYMENT_NAME),
          rs.getString(SmartFileLog.COLUMN_NAME_VENDOR),
          rs.getString(SmartFileLog.COLUMN_NAME_BANK_KEY),
          rs.getString(SmartFileLog.COLUMN_NAME_BANK_ACCOUNT_NO),
          rs.getString(SmartFileLog.COLUMN_NAME_PAYMENT_METHOD),
          rs.getString(SmartFileLog.COLUMN_NAME_PAYING_COMP_CODE),
          rs.getString(SmartFileLog.COLUMN_NAME_PAYMENT_DOC_NO),
          rs.getString(SmartFileLog.COLUMN_NAME_PAYMENT_YEAR),
          rs.getString(SmartFileLog.COLUMN_NAME_COMP_CODE),
          rs.getString(SmartFileLog.COLUMN_NAME_INV_DOC_NO),
          rs.getString(SmartFileLog.COLUMN_NAME_FISCAL_YEAR),
          rs.getString(SmartFileLog.COLUMN_NAME_FI_AREA),
          rs.getString(SmartFileLog.COLUMN_NAME_TRANSFER_LEVEL),
          rs.getBigDecimal(SmartFileLog.COLUMN_NAME_FEE),
          rs.getBigDecimal(SmartFileLog.COLUMN_NAME_CREDIT_MEMO),
          rs.getString(SmartFileLog.COLUMN_NAME_ORIGINAL_ACC_DOC_NO),
          rs.getString(SmartFileLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
          rs.getString(SmartFileLog.COLUMN_NAME_ORIGINAL_COMP_CODE),
          rs.getString(SmartFileLog.COLUMN_NAME_ORIGINAL_DOC_TYPE),
          rs.getBigDecimal(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT),
          rs.getBigDecimal(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_BASE),
          rs.getBigDecimal(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P),
          rs.getBigDecimal(SmartFileLog.COLUMN_NAME_ORIGINAL_WTX_BASE_P),
          rs.getString(SmartFileLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR),
          rs.getString(SmartFileLog.COLUMN_NAME_PAYMENT_COMP_CODE),
          null);

  public SmartFileLogRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
    super(userRowMapper, smartFileLogUpdater, updaterType, SmartFileLog.TABLE_NAME, SmartFileLog.COLUMN_NAME_SMART_FILE_LOG_ID, jdbcTemplate);
  }
}
