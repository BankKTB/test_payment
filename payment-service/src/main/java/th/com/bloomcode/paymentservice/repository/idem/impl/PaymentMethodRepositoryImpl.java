package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.PaymentMethod;
import th.com.bloomcode.paymentservice.model.idem.PaymentMethod;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.PaymentMethodRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class PaymentMethodRepositoryImpl extends MetadataJdbcRepository<PaymentMethod, Long> implements PaymentMethodRepository {
    static BeanPropertyRowMapper<PaymentMethod> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PaymentMethod.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<PaymentMethod> documentTypeUpdater = (t, mapping) -> {
        mapping.put(PaymentMethod.COLUMN_NAME_C_PAYMENT_METHOD_ID, t.getId());
        mapping.put(PaymentMethod.COLUMN_NAME_VALUE_CODE, t.getValueCode());
        mapping.put(PaymentMethod.COLUMN_NAME_NAME, t.getName());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(PaymentMethod.COLUMN_NAME_C_PAYMENT_METHOD_ID, Types.BIGINT),
            entry(PaymentMethod.COLUMN_NAME_VALUE_CODE, Types.NVARCHAR),
            entry(PaymentMethod.COLUMN_NAME_NAME, Types.NVARCHAR)
    );

    static RowMapper<PaymentMethod> userRowMapper = (rs, rowNum) -> new PaymentMethod(
            rs.getLong(PaymentMethod.COLUMN_NAME_C_PAYMENT_METHOD_ID),
            rs.getString(PaymentMethod.COLUMN_NAME_VALUE_CODE),
            rs.getString(PaymentMethod.COLUMN_NAME_NAME)
    );

    @Autowired
    public PaymentMethodRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                PaymentMethod.TABLE_NAME,
                PaymentMethod.COLUMN_NAME_C_PAYMENT_METHOD_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) ");
        sql.append(" FROM TH_CAPAYMENTMETHOD pm ");
        sql.append(" WHERE pm.ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "pm.VALUECODE", "pm.NAME"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<PaymentMethod> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" pm.TH_CAPAYMENTMETHOD_ID, ");
        sql.append(" pm.VALUECODE, ");
        sql.append(" pm.NAME ");
        sql.append(" FROM ");
        sql.append(" TH_CAPAYMENTMETHOD pm ");
        sql.append(" WHERE ");
        sql.append(" pm.ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "pm.VALUECODE", "pm.NAME"));
        }
        sql.append(" ORDER BY ");
        sql.append(" pm.VALUECODE ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public PaymentMethod findOneByValueCode(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" pm.TH_CAPAYMENTMETHOD_ID, ");
        sql.append(" pm.VALUECODE, ");
        sql.append(" pm.NAME ");
        sql.append(" FROM ");
        sql.append(" TH_CAPAYMENTMETHOD pm ");
        sql.append(" WHERE ");
        sql.append(" pm.ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND pm.VALUECODE like ? ");
        }
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[] { valueCode }, userRowMapper);
    }
}
