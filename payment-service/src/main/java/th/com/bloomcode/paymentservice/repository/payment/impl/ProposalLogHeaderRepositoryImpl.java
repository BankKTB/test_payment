package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogHeader;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.ProposalLogHeaderRepository;
import th.com.bloomcode.paymentservice.service.payment.GenerateFileAliasService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class ProposalLogHeaderRepositoryImpl extends MetadataJdbcRepository<ProposalLogHeader, Long>
    implements ProposalLogHeaderRepository {

  static BeanPropertyRowMapper<ProposalLogHeader> beanPropertyRowMapper =
      new BeanPropertyRowMapper<>(ProposalLogHeader.class);

  private final JdbcTemplate jdbcTemplate;
  static Updater<ProposalLogHeader> smartFileLogUpdater =
      (t, mapping) -> {
        mapping.put(ProposalLogHeader.COLUMN_NAME_PROPOSAL_LOG_HEADER_ID, t.getId());
        mapping.put(ProposalLogHeader.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(ProposalLogHeader.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(ProposalLogHeader.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(ProposalLogHeader.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(ProposalLogHeader.COLUMN_NAME_REF_RUNNING, t.getRefRunning());
        mapping.put(ProposalLogHeader.COLUMN_NAME_PAYMENT_DATE, t.getPaymentDate());
        mapping.put(ProposalLogHeader.COLUMN_NAME_PAYMENT_NAME, t.getPaymentName());
        mapping.put(ProposalLogHeader.COLUMN_NAME_SUM_RECORD, t.isSumRecord());
        mapping.put(ProposalLogHeader.COLUMN_NAME_IS_CANCEL, t.isCancel());
        mapping.put(ProposalLogHeader.COLUMN_NAME_IS_USE, t.isUse());
        mapping.put(
            ProposalLogHeader.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, t.getGenerateFileAliasId());
      };
  static Map<String, Integer> updaterType =
      Map.ofEntries(
          entry(ProposalLogHeader.COLUMN_NAME_PROPOSAL_LOG_HEADER_ID, Types.BIGINT),
          entry(ProposalLogHeader.COLUMN_NAME_CREATED, Types.TIMESTAMP),
          entry(ProposalLogHeader.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
          entry(ProposalLogHeader.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
          entry(ProposalLogHeader.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
          entry(ProposalLogHeader.COLUMN_NAME_REF_RUNNING, Types.BIGINT),
          entry(ProposalLogHeader.COLUMN_NAME_PAYMENT_DATE, Types.TIMESTAMP),
          entry(ProposalLogHeader.COLUMN_NAME_PAYMENT_NAME, Types.NVARCHAR),
          entry(ProposalLogHeader.COLUMN_NAME_SUM_RECORD, Types.BOOLEAN),
          entry(ProposalLogHeader.COLUMN_NAME_IS_CANCEL, Types.BOOLEAN),
          entry(ProposalLogHeader.COLUMN_NAME_IS_USE, Types.BOOLEAN),
          entry(ProposalLogHeader.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, Types.BIGINT));
  private static GenerateFileAliasService generateFileAliasService;
  static RowMapper<ProposalLogHeader> userRowMapper =
      (rs, rowNum) ->
          new ProposalLogHeader(
              rs.getLong(ProposalLogHeader.COLUMN_NAME_PROPOSAL_LOG_HEADER_ID),
              rs.getTimestamp(ProposalLogHeader.COLUMN_NAME_CREATED),
              rs.getString(ProposalLogHeader.COLUMN_NAME_CREATED_BY),
              rs.getTimestamp(ProposalLogHeader.COLUMN_NAME_UPDATED),
              rs.getString(ProposalLogHeader.COLUMN_NAME_UPDATED_BY),
              rs.getLong(ProposalLogHeader.COLUMN_NAME_REF_RUNNING),
              rs.getTimestamp(ProposalLogHeader.COLUMN_NAME_PAYMENT_DATE),
              rs.getString(ProposalLogHeader.COLUMN_NAME_PAYMENT_NAME),
              rs.getBoolean(ProposalLogHeader.COLUMN_NAME_SUM_RECORD),
              rs.getBoolean(ProposalLogHeader.COLUMN_NAME_IS_CANCEL),
              rs.getBoolean(ProposalLogHeader.COLUMN_NAME_IS_USE),
              rs.getLong(ProposalLogHeader.COLUMN_NAME_GENERATE_FILE_ALIAS_ID),
              ProposalLogHeaderRepositoryImpl.generateFileAliasService.findOneById(
                  rs.getLong(ProposalLogHeader.COLUMN_NAME_GENERATE_FILE_ALIAS_ID)));

  public ProposalLogHeaderRepositoryImpl(
      @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, GenerateFileAliasService generateFileAliasService) {
    super(
        userRowMapper,
        smartFileLogUpdater,
        updaterType,
        ProposalLogHeader.TABLE_NAME,
        ProposalLogHeader.COLUMN_NAME_PROPOSAL_LOG_HEADER_ID,
        jdbcTemplate);
    this.jdbcTemplate = jdbcTemplate;
    ProposalLogHeaderRepositoryImpl.generateFileAliasService = generateFileAliasService;
  }

  @Override
  public void updateRegen(Timestamp paymentDate, String paymentName, Long refRunning) {
    List<Object> params = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    sql.append(" UPDATE ");
    sql.append(" PROPOSAL_LOG_HEADER ");
    sql.append(" SET IS_USE = 0 ");
    sql.append(" WHERE 1=1 ");
    if (!Util.isEmpty(paymentDate)) {
      sql.append(SqlUtil.whereClauseEqual(paymentDate, "PAYMENT_DATE", params));
    }
    if (!Util.isEmpty(paymentName)) {
      sql.append(SqlUtil.whereClause(paymentName, "PAYMENT_NAME", params));
    }
    if (!Util.isEmpty(refRunning)) {
      sql.append(SqlUtil.whereClauseNotEqual(refRunning, "REF_RUNNING", params));
    }
    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql {}", sql);
    log.info("params : {} " , params);
    int rtn = this.jdbcTemplate.update(sql.toString(), objParams);
    log.info("rtn : {}", rtn);
  }
}
