package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.SmartFileBatch;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.SmartFileBatchRepository;
import th.com.bloomcode.paymentservice.service.payment.SmartFileHeaderService;

import java.sql.Types;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class SmartFileBatchRepositoryImpl extends MetadataJdbcRepository<SmartFileBatch, Long> implements SmartFileBatchRepository {

  private static SmartFileHeaderService smartFileHeaderService;

  static BeanPropertyRowMapper<SmartFileBatch> beanPropertyRowMapper = new BeanPropertyRowMapper<>(SmartFileBatch.class);

  static Updater<SmartFileBatch> smartFileBatchUpdater = (t, mapping) -> {
    mapping.put(SmartFileBatch.COLUMN_NAME_SMART_FILE_BATCH_ID, t.getId());
    mapping.put(SmartFileBatch.COLUMN_NAME_FILE_TYPE, t.getFileType());
    mapping.put(SmartFileBatch.COLUMN_NAME_REC_TYPE, t.getRecType());
    mapping.put(SmartFileBatch.COLUMN_NAME_BATCH_NUM, t.getBatchNum());
    mapping.put(SmartFileBatch.COLUMN_NAME_SEND_BANK_CODE, t.getSendBankCode());
    mapping.put(SmartFileBatch.COLUMN_NAME_TOTAL_NUM, t.getTotalNum());
    mapping.put(SmartFileBatch.COLUMN_NAME_TOTAL_AMOUNT, t.getTotalAmount());
    mapping.put(SmartFileBatch.COLUMN_NAME_EFF_DATE, t.getEffDate());
    mapping.put(SmartFileBatch.COLUMN_NAME_KIND_TRANS, t.getKindTrans());
    mapping.put(SmartFileBatch.COLUMN_NAME_FILLER, t.getFiller());
    mapping.put(SmartFileBatch.COLUMN_NAME_SMART_FILE_HEADER_ID, t.getSmartFileHeaderId());
  };

  static Map<String, Integer> updaterType = Map.ofEntries(
    entry(SmartFileBatch.COLUMN_NAME_SMART_FILE_BATCH_ID, Types.BIGINT),
    entry(SmartFileBatch.COLUMN_NAME_FILE_TYPE, Types.NVARCHAR),
    entry(SmartFileBatch.COLUMN_NAME_REC_TYPE, Types.NVARCHAR),
    entry(SmartFileBatch.COLUMN_NAME_BATCH_NUM, Types.NVARCHAR),
    entry(SmartFileBatch.COLUMN_NAME_SEND_BANK_CODE, Types.NVARCHAR),
    entry(SmartFileBatch.COLUMN_NAME_TOTAL_NUM, Types.NVARCHAR),
    entry(SmartFileBatch.COLUMN_NAME_TOTAL_AMOUNT, Types.NVARCHAR),
    entry(SmartFileBatch.COLUMN_NAME_EFF_DATE, Types.NVARCHAR),
    entry(SmartFileBatch.COLUMN_NAME_KIND_TRANS, Types.NVARCHAR),
    entry(SmartFileBatch.COLUMN_NAME_FILLER,Types.NVARCHAR),
    entry(SmartFileBatch.COLUMN_NAME_SMART_FILE_HEADER_ID, Types.BIGINT)
  );

  static RowMapper<SmartFileBatch> userRowMapper = (rs, rowNum) -> new SmartFileBatch(
          rs.getLong(SmartFileBatch.COLUMN_NAME_SMART_FILE_BATCH_ID),
          rs.getString(SmartFileBatch.COLUMN_NAME_FILE_TYPE),
          rs.getString(SmartFileBatch.COLUMN_NAME_REC_TYPE),
          rs.getString(SmartFileBatch.COLUMN_NAME_BATCH_NUM),
          rs.getString(SmartFileBatch.COLUMN_NAME_SEND_BANK_CODE),
          rs.getString(SmartFileBatch.COLUMN_NAME_TOTAL_NUM),
          rs.getString(SmartFileBatch.COLUMN_NAME_TOTAL_AMOUNT),
          rs.getString(SmartFileBatch.COLUMN_NAME_EFF_DATE),
          rs.getString(SmartFileBatch.COLUMN_NAME_KIND_TRANS),
          rs.getString(SmartFileBatch.COLUMN_NAME_FILLER),
          smartFileHeaderService.findOneById(rs.getLong(SmartFileBatch.COLUMN_NAME_SMART_FILE_HEADER_ID)));

  public SmartFileBatchRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, SmartFileHeaderService smartFileHeaderService) {
    super(userRowMapper, smartFileBatchUpdater, updaterType, SmartFileBatch.TABLE_NAME, SmartFileBatch.COLUMN_NAME_SMART_FILE_BATCH_ID, jdbcTemplate);
    SmartFileBatchRepositoryImpl.smartFileHeaderService = smartFileHeaderService;
  }
}
