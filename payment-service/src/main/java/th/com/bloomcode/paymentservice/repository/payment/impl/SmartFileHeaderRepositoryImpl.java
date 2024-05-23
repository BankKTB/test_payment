package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.SmartFileHeader;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.SmartFileHeaderRepository;

import java.sql.Types;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class SmartFileHeaderRepositoryImpl extends MetadataJdbcRepository<SmartFileHeader, Long>
    implements SmartFileHeaderRepository {

  static BeanPropertyRowMapper<SmartFileHeader> beanPropertyRowMapper =
      new BeanPropertyRowMapper<>(SmartFileHeader.class);
  static Updater<SmartFileHeader> smartFileHeaderUpdater =
      (t, mapping) -> {
        mapping.put(SmartFileHeader.COLUMN_NAME_SMART_FILE_HEADER_ID, t.getId());
        mapping.put(SmartFileHeader.COLUMN_NAME_FILE_TYPE, t.getFileType());
        mapping.put(SmartFileHeader.COLUMN_NAME_REC_TYPE, t.getRecType());
        mapping.put(SmartFileHeader.COLUMN_NAME_REL_VER, t.getRelVer());
        mapping.put(SmartFileHeader.COLUMN_NAME_REL_DATE, t.getRelDate());
        mapping.put(SmartFileHeader.COLUMN_NAME_FILLER, t.getFiller());
        mapping.put(SmartFileHeader.COLUMN_NAME_IS_PROPOSAL, t.isProposal());
        mapping.put(SmartFileHeader.COLUMN_NAME_EFF_DATE, t.getEffDate());
        mapping.put(SmartFileHeader.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, t.getGenerateFileAliasId());
      };
  static Map<String, Integer> updaterType =
      Map.ofEntries(
          entry(SmartFileHeader.COLUMN_NAME_SMART_FILE_HEADER_ID, Types.BIGINT),
          entry(SmartFileHeader.COLUMN_NAME_FILE_TYPE, Types.NVARCHAR),
          entry(SmartFileHeader.COLUMN_NAME_REC_TYPE, Types.NVARCHAR),
          entry(SmartFileHeader.COLUMN_NAME_REL_VER, Types.NVARCHAR),
          entry(SmartFileHeader.COLUMN_NAME_REL_DATE, Types.NVARCHAR),
          entry(SmartFileHeader.COLUMN_NAME_FILLER, Types.NVARCHAR),
          entry(SmartFileHeader.COLUMN_NAME_IS_PROPOSAL, Types.BOOLEAN),
          entry(SmartFileHeader.COLUMN_NAME_EFF_DATE, Types.TIMESTAMP),
          entry(SmartFileHeader.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, Types.BIGINT));
  static RowMapper<SmartFileHeader> userRowMapper =
      (rs, rowNum) ->
          new SmartFileHeader(
              rs.getLong(SmartFileHeader.COLUMN_NAME_SMART_FILE_HEADER_ID),
              rs.getString(SmartFileHeader.COLUMN_NAME_FILE_TYPE),
              rs.getString(SmartFileHeader.COLUMN_NAME_REC_TYPE),
              rs.getString(SmartFileHeader.COLUMN_NAME_REL_VER),
              rs.getString(SmartFileHeader.COLUMN_NAME_REL_DATE),
              rs.getString(SmartFileHeader.COLUMN_NAME_FILLER),
              rs.getBoolean(SmartFileHeader.COLUMN_NAME_IS_PROPOSAL),
              rs.getTimestamp(SmartFileHeader.COLUMN_NAME_EFF_DATE));
  private final JdbcTemplate jdbcTemplate;

  public SmartFileHeaderRepositoryImpl(
      @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
    super(
        userRowMapper,
        smartFileHeaderUpdater,
        updaterType,
        SmartFileHeader.TABLE_NAME,
        SmartFileHeader.COLUMN_NAME_SMART_FILE_HEADER_ID,
        jdbcTemplate);
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public SmartFileHeader findOneByGenerateFileAliasId(Long id) {
    String sql =
        "SELECT * FROM "
            + SmartFileHeader.TABLE_NAME
            + " WHERE "
            + SmartFileHeader.COLUMN_NAME_GENERATE_FILE_ALIAS_ID
            + " = ?";
    return this.jdbcTemplate.query(sql, userRowMapper, id).get(0);
  }
}
