package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileOutput;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcAutoRepository;
import th.com.bloomcode.paymentservice.repository.payment.GenerateFileOutputRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Slf4j
@Repository
public class GenerateFileOutputRepositoryImpl extends MetadataJdbcAutoRepository<GenerateFileOutput, Long> implements GenerateFileOutputRepository {
  private final JdbcTemplate jdbcTemplate;

  static Updater<GenerateFileOutput> generateFileOutputUpdater = (t, mapping) -> {
    mapping.put(GenerateFileOutput.COLUMN_NAME_GENERATE_FILE_OUTPUT_ID, t.getId());
    mapping.put(GenerateFileOutput.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, t.getGenerateFileAliasId());
    mapping.put(GenerateFileOutput.COLUMN_NAME_JSON_TEXT, t.getJsonText());
  };

  static Map<String, Integer> updaterType = Map.ofEntries(
          entry(GenerateFileOutput.COLUMN_NAME_GENERATE_FILE_OUTPUT_ID, Types.BIGINT),
          entry(GenerateFileOutput.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, Types.NVARCHAR),
          entry(GenerateFileOutput.COLUMN_NAME_JSON_TEXT, Types.NVARCHAR)
  );

  static RowMapper<GenerateFileOutput> userRowMapper = (rs, rowNum) -> new GenerateFileOutput(
          rs.getLong(GenerateFileOutput.COLUMN_NAME_GENERATE_FILE_OUTPUT_ID),
          rs.getLong(GenerateFileOutput.COLUMN_NAME_GENERATE_FILE_ALIAS_ID),
          rs.getString(GenerateFileOutput.COLUMN_NAME_JSON_TEXT)
  );

  public GenerateFileOutputRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
    super(userRowMapper, generateFileOutputUpdater, updaterType, GenerateFileOutput.TABLE_NAME, GenerateFileOutput.COLUMN_NAME_GENERATE_FILE_OUTPUT_ID, jdbcTemplate);
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public GenerateFileOutput findOneByRefRunning(Long generateFileAliasId) {
    List<Object> params = new ArrayList<>();

    StringBuilder sql = new StringBuilder();
    sql.append("          SELECT *           ");
    sql.append("          FROM GENERATE_FILE_OUTPUT  ");
    sql.append("          WHERE 1=1          ");

    sql.append(SqlUtil.whereClause(generateFileAliasId, "GENERATE_FILE_ALIAS_ID", params));

    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql {}", sql.toString());
    List<GenerateFileOutput> generateFileOutput = this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    if (!generateFileOutput.isEmpty()) {
      return generateFileOutput.get(0);
    } else {
      return null;
    }
  }
}
