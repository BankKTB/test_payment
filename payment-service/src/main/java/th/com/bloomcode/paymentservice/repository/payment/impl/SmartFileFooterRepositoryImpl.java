package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.SmartFileFooter;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.SmartFileFooterRepository;
import th.com.bloomcode.paymentservice.service.payment.SmartFileHeaderService;

import java.sql.Types;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class SmartFileFooterRepositoryImpl extends MetadataJdbcRepository<SmartFileFooter, Long> implements SmartFileFooterRepository {

  private static SmartFileHeaderService smartFileHeaderService;

  static BeanPropertyRowMapper<SmartFileFooter> beanPropertyRowMapper = new BeanPropertyRowMapper<>(SmartFileFooter.class);

  static Updater<SmartFileFooter> smartFileFooterUpdater = (t, mapping) -> {
    mapping.put(SmartFileFooter.COLUMN_NAME_SMART_FILE_FOOTER_ID, t.getId());
    mapping.put(SmartFileFooter.COLUMN_NAME_FILE_TYPE, t.getFileType());
    mapping.put(SmartFileFooter.COLUMN_NAME_REC_TYPE, t.getRecType());
    mapping.put(SmartFileFooter.COLUMN_NAME_REC_COUNT, t.getRecCount());
    mapping.put(SmartFileFooter.COLUMN_NAME_FILLER, t.getFiller());
    mapping.put(SmartFileFooter.COLUMN_NAME_AUTHORIZE, t.getAuthorize());
    mapping.put(SmartFileFooter.COLUMN_NAME_TOTAL_RECORD, t.getTotalRecord());
    mapping.put(SmartFileFooter.COLUMN_NAME_TOTAL_AMT, t.getTotalAmt());
    mapping.put(SmartFileFooter.COLUMN_NAME_SMART_FILE_HEADER_ID, t.getSmartFileHeaderId());
  };

  static Map<String, Integer> updaterType = Map.ofEntries(
    entry(SmartFileFooter.COLUMN_NAME_SMART_FILE_FOOTER_ID, Types.BIGINT),
    entry(SmartFileFooter.COLUMN_NAME_FILE_TYPE, Types.NVARCHAR),
    entry(SmartFileFooter.COLUMN_NAME_REC_TYPE, Types.NVARCHAR),
    entry(SmartFileFooter.COLUMN_NAME_REC_COUNT, Types.NVARCHAR),
    entry(SmartFileFooter.COLUMN_NAME_FILLER, Types.NVARCHAR),
    entry(SmartFileFooter.COLUMN_NAME_AUTHORIZE, Types.NVARCHAR),
    entry(SmartFileFooter.COLUMN_NAME_TOTAL_RECORD, Types.INTEGER),
    entry(SmartFileFooter.COLUMN_NAME_TOTAL_AMT, Types.NUMERIC),
    entry(SmartFileFooter.COLUMN_NAME_SMART_FILE_HEADER_ID, Types.BIGINT)
  );

  static RowMapper<SmartFileFooter> userRowMapper = (rs, rowNum) -> new SmartFileFooter(
          rs.getLong(SmartFileFooter.COLUMN_NAME_SMART_FILE_FOOTER_ID),
          rs.getString(SmartFileFooter.COLUMN_NAME_FILE_TYPE),
          rs.getString(SmartFileFooter.COLUMN_NAME_REC_TYPE),
          rs.getString(SmartFileFooter.COLUMN_NAME_REC_COUNT),
          rs.getString(SmartFileFooter.COLUMN_NAME_FILLER),
          rs.getString(SmartFileFooter.COLUMN_NAME_AUTHORIZE),
          rs.getInt(SmartFileFooter.COLUMN_NAME_TOTAL_RECORD),
          rs.getBigDecimal(SmartFileFooter.COLUMN_NAME_TOTAL_AMT),
          smartFileHeaderService.findOneById(rs.getLong(SmartFileFooter.COLUMN_NAME_SMART_FILE_HEADER_ID)));

  public SmartFileFooterRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, SmartFileHeaderService smartFileHeaderService) {
    super(userRowMapper, smartFileFooterUpdater, updaterType, SmartFileFooter.TABLE_NAME, SmartFileFooter.COLUMN_NAME_SMART_FILE_FOOTER_ID, jdbcTemplate);
    SmartFileFooterRepositoryImpl.smartFileHeaderService = smartFileHeaderService;
  }
}
