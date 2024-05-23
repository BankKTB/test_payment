package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.PaymentArrange;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentArrangeRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class PaymentArrangeRepositoryImpl extends MetadataJdbcRepository<PaymentArrange, Long> implements PaymentArrangeRepository {
    static BeanPropertyRowMapper<PaymentArrange> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PaymentArrange.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<PaymentArrange> saveType = (t, mapping) -> {  //SAVE

        mapping.put(PaymentArrange.COLUMN_NAME_TH_PAYMENT_ARRANGE_ID, t.getId());
        mapping.put(PaymentArrange.COLUMN_NAME_ARRANGE_CODE, t.getArrangeCode());
        mapping.put(PaymentArrange.COLUMN_NAME_ARRANGE_NAME, t.getArrangeName());
        mapping.put(PaymentArrange.COLUMN_NAME_ARRANGE_DESCRIPTION, t.getArrangeDescription());
        mapping.put(PaymentArrange.COLUMN_NAME_ARRANGE_JSON_TEXT, t.getArrangeJsonText());
        mapping.put(PaymentArrange.COLUMN_NAME_ARRANGE_DEFAULT, t.isArrangeDefault());
        mapping.put(PaymentArrange.COLUMN_NAME_ACTIVE, t.isActive());
        mapping.put(PaymentArrange.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(PaymentArrange.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(PaymentArrange.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(PaymentArrange.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
    };

    static Map<String, Integer> updaterType = Map.ofEntries( // UPDATE
            entry(PaymentArrange.COLUMN_NAME_TH_PAYMENT_ARRANGE_ID, Types.BIGINT),
            entry(PaymentArrange.COLUMN_NAME_ARRANGE_CODE, Types.NVARCHAR),
            entry(PaymentArrange.COLUMN_NAME_ARRANGE_NAME, Types.NVARCHAR),
            entry(PaymentArrange.COLUMN_NAME_ARRANGE_DESCRIPTION, Types.NVARCHAR),
            entry(PaymentArrange.COLUMN_NAME_ARRANGE_JSON_TEXT, Types.NVARCHAR),
            entry(PaymentArrange.COLUMN_NAME_ACTIVE, Types.BOOLEAN),
            entry(PaymentArrange.COLUMN_NAME_ARRANGE_DEFAULT, Types.BOOLEAN),
            entry(PaymentArrange.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(PaymentArrange.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(PaymentArrange.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
            entry(PaymentArrange.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR)
    );

    static RowMapper<PaymentArrange> rowMapperType = (rs, rowNum) -> new PaymentArrange( //GET
                            rs.getLong(PaymentArrange.COLUMN_NAME_TH_PAYMENT_ARRANGE_ID),
                            rs.getString(PaymentArrange.COLUMN_NAME_ARRANGE_CODE),
                            rs.getString(PaymentArrange.COLUMN_NAME_ARRANGE_NAME),
                            rs.getString(PaymentArrange.COLUMN_NAME_ARRANGE_DESCRIPTION),
                            rs.getString(PaymentArrange.COLUMN_NAME_ARRANGE_JSON_TEXT),
                            rs.getBoolean(PaymentArrange.COLUMN_NAME_ACTIVE),
                            rs.getBoolean(PaymentArrange.COLUMN_NAME_ARRANGE_DEFAULT),
                            rs.getTimestamp(PaymentArrange.COLUMN_NAME_CREATED),
                            rs.getString(PaymentArrange.COLUMN_NAME_CREATED_BY),
                            rs.getTimestamp(PaymentArrange.COLUMN_NAME_UPDATED),
                            rs.getString(PaymentArrange.COLUMN_NAME_UPDATED_BY)
                    );


    public PaymentArrangeRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(rowMapperType, saveType, updaterType, PaymentArrange.TABLE_NAME, PaymentArrange.COLUMN_NAME_TH_PAYMENT_ARRANGE_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PaymentArrange findOneByArrangeCodeAndArrangeName(String arrangeCode, String arrangeName) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(PaymentArrange.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(arrangeCode)) {
            sql.append(SqlUtil.whereClause(arrangeCode, PaymentArrange.COLUMN_NAME_ARRANGE_CODE, params));
        }
        if (!Util.isEmpty(arrangeName)) {
            sql.append(SqlUtil.whereClause(arrangeName, PaymentArrange.COLUMN_NAME_ARRANGE_NAME, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        List<PaymentArrange> variantConfigs = this.jdbcTemplate.query(sql.toString(), objParams, rowMapperType);
        if (!variantConfigs.isEmpty()) {
            return variantConfigs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<PaymentArrange> findAllByArrangeCode(String arrangeCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(PaymentArrange.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(arrangeCode)) {
            sql.append(SqlUtil.whereClause(arrangeCode, PaymentArrange.COLUMN_NAME_ARRANGE_CODE, params));
        }
        sql.append(" ORDER BY TH_PAYMENT_ARRANGE_ID DESC ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperType);
    }

    @Override
    public List<PaymentArrange> findByArrangeCodeDefaultArrange(String arrangeCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(PaymentArrange.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(arrangeCode)) {
            sql.append(SqlUtil.whereClause(arrangeCode, PaymentArrange.COLUMN_NAME_ARRANGE_CODE, params));
        }
        sql.append("   AND ARRANGE_DEFAULT = 1 ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperType);
    }

    @Override
    public PaymentArrange findOneByArrangeId(Long arrangeId) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(PaymentArrange.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(arrangeId)) {
            sql.append(SqlUtil.whereClause(arrangeId, PaymentArrange.COLUMN_NAME_TH_PAYMENT_ARRANGE_ID, params));
        }
        sql.append(" ORDER BY TH_PAYMENT_ARRANGE_ID DESC ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        List<PaymentArrange> variantConfigs = this.jdbcTemplate.query(sql.toString(), objParams, rowMapperType);
        if (!variantConfigs.isEmpty()) {
            return variantConfigs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<PaymentArrange> findAllByArrangeDefaultStatus(Long arrangeId, boolean status) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(PaymentArrange.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(arrangeId) && status) {
            sql.append(SqlUtil.whereClause(arrangeId, PaymentArrange.COLUMN_NAME_TH_PAYMENT_ARRANGE_ID, params));
        } else if (!Util.isEmpty(arrangeId) && !status) {
            sql.append(SqlUtil.whereClauseNot(arrangeId, PaymentArrange.COLUMN_NAME_TH_PAYMENT_ARRANGE_ID, params));
        }
        sql.append(" ORDER BY TH_PAYMENT_ARRANGE_ID DESC ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperType);
    }

}
