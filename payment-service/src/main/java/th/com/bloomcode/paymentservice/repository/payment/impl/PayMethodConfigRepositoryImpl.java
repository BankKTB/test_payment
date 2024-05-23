package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.PayMethodConfig;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PayMethodConfigRepository;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class PayMethodConfigRepositoryImpl extends MetadataJdbcRepository<PayMethodConfig, Long>
    implements PayMethodConfigRepository {

  private final JdbcTemplate jdbcTemplate;
  static Updater<PayMethodConfig> payMethodConfigUpdater =
      (t, mapping) -> {
        mapping.put(PayMethodConfig.COLUMN_NAME_PAY_METHOD_CONFIG_ID, t.getId());
        mapping.put(PayMethodConfig.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(PayMethodConfig.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(PayMethodConfig.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(PayMethodConfig.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(
            PayMethodConfig.COLUMN_NAME_IS_ALLOWED_PERSONNEL_PAYMENT,
            t.isAllowedPersonnelPayment());
        mapping.put(PayMethodConfig.COLUMN_NAME_IS_BANK_DETAIL, t.isBankDetail());
        mapping.put(PayMethodConfig.COLUMN_NAME_COUNTRY, t.getCountry());
        mapping.put(PayMethodConfig.COLUMN_NAME_COUNTRY_NAME_EN, t.getCountryNameEn());
        mapping.put(
            PayMethodConfig.COLUMN_NAME_DOCUMENT_TYPE_FOR_PAYMENT, t.getDocumentTypeForPayment());
        mapping.put(PayMethodConfig.COLUMN_NAME_IS_PAY_BY, t.isPayBy());
        mapping.put(PayMethodConfig.COLUMN_NAME_PAY_METHOD, t.getPayMethod());
        mapping.put(PayMethodConfig.COLUMN_NAME_PAY_METHOD_NAME, t.getPayMethodName());
      };

  static Map<String, Integer> updaterType =
      Map.ofEntries(
          entry(PayMethodConfig.COLUMN_NAME_PAY_METHOD_CONFIG_ID, Types.BIGINT),
          entry(PayMethodConfig.COLUMN_NAME_CREATED, Types.TIMESTAMP),
          entry(PayMethodConfig.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
          entry(PayMethodConfig.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
          entry(PayMethodConfig.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
          entry(PayMethodConfig.COLUMN_NAME_IS_ALLOWED_PERSONNEL_PAYMENT, Types.NVARCHAR),
          entry(PayMethodConfig.COLUMN_NAME_IS_BANK_DETAIL, Types.NVARCHAR),
          entry(PayMethodConfig.COLUMN_NAME_COUNTRY, Types.NVARCHAR),
          entry(PayMethodConfig.COLUMN_NAME_COUNTRY_NAME_EN, Types.NVARCHAR),
          entry(PayMethodConfig.COLUMN_NAME_DOCUMENT_TYPE_FOR_PAYMENT, Types.NVARCHAR),
          entry(PayMethodConfig.COLUMN_NAME_IS_PAY_BY, Types.NVARCHAR),
          entry(PayMethodConfig.COLUMN_NAME_PAY_METHOD, Types.NVARCHAR),
          entry(PayMethodConfig.COLUMN_NAME_PAY_METHOD_NAME, Types.NVARCHAR));

  static RowMapper<PayMethodConfig> payMethodConfigRowMapper =
      (rs, rowNum) ->
          new PayMethodConfig(
              rs.getLong(PayMethodConfig.COLUMN_NAME_PAY_METHOD_CONFIG_ID),
              rs.getTimestamp(PayMethodConfig.COLUMN_NAME_CREATED),
              rs.getString(PayMethodConfig.COLUMN_NAME_CREATED_BY),
              rs.getTimestamp(PayMethodConfig.COLUMN_NAME_UPDATED),
              rs.getString(PayMethodConfig.COLUMN_NAME_UPDATED_BY),
              rs.getBoolean(PayMethodConfig.COLUMN_NAME_IS_ALLOWED_PERSONNEL_PAYMENT),
              rs.getBoolean(PayMethodConfig.COLUMN_NAME_IS_BANK_DETAIL),
              rs.getString(PayMethodConfig.COLUMN_NAME_COUNTRY),
              rs.getString(PayMethodConfig.COLUMN_NAME_COUNTRY_NAME_EN),
              rs.getString(PayMethodConfig.COLUMN_NAME_DOCUMENT_TYPE_FOR_PAYMENT),
              rs.getBoolean(PayMethodConfig.COLUMN_NAME_IS_PAY_BY),
              rs.getString(PayMethodConfig.COLUMN_NAME_PAY_METHOD),
              rs.getString(PayMethodConfig.COLUMN_NAME_PAY_METHOD_NAME));

  public PayMethodConfigRepositoryImpl(
      @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
    super(
        payMethodConfigRowMapper,
        payMethodConfigUpdater,
        updaterType,
        PayMethodConfig.TABLE_NAME,
        PayMethodConfig.COLUMN_NAME_PAY_METHOD_CONFIG_ID,
        jdbcTemplate);
    this.jdbcTemplate = jdbcTemplate;
  }

  //  @Override
  //  public List<PayMethodConfig> findAllOrderByCountryAscPayMethodAsc() {
  //    StringBuilder sql = new StringBuilder();
  //    sql.append(" SELECT * FROM PAY_METHOD_CONFIG ");
  //    sql.append(" WHERE 1=1 ");
  //    sql.append(" ORDER BY COUNTRY ASC, PAY_METHOD ASC");
  //    return this.jdbcTemplate.query(sql.toString(), payMethodConfigRowMapper);
  //  }

  @Override
  public List<PayMethodConfig> findAllOrderByCountryAscPayMethodAsc() {
    StringBuilder sql = new StringBuilder();
    sql.append(" SELECT  ");
    sql.append("          ''           AS IS_ALLOWED_PERSONNEL_PAYMENT,           ");
    sql.append("          ''           AS IS_BANK_DETAIL,           ");
    sql.append("          'TH'         AS COUNTRY,           ");
    sql.append("          ''           AS COUNTRY_NAME_EN,           ");
    sql.append("          DT.NAME      AS DOCUMENT_TYPE_FOR_PAYMENT,           ");
    sql.append("          ''           AS IS_PAY_BY,           ");
    sql.append("          PA.VALUECODE AS PAY_METHOD,           ");
    sql.append("          PA.NAME      AS PAY_METHOD_NAME,           ");
    sql.append("          PA.CREATED,           ");
    sql.append("          PA.CREATEDBY,           ");
    sql.append("          PA.UPDATED,           ");
    sql.append("          PA.UPDATEDBY           ");
    sql.append("          FROM TH_CAPAYMENTMETHOD PA           ");
    sql.append("          LEFT JOIN C_DOCTYPE DT ON PA.C_DOCTYPE_PAYMENT_ID = DT.C_DOCTYPE_ID           ");
    sql.append(" WHERE 1=1 ");
    sql.append(" ORDER BY COUNTRY ASC, PAY_METHOD ASC");
    return this.jdbcTemplate.query(sql.toString(), payMethodConfigRowMapper);
  }
}
