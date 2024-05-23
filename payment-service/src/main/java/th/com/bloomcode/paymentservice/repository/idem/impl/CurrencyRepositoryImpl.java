package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.Currency;
import th.com.bloomcode.paymentservice.model.idem.Currency;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.CurrencyRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CurrencyRepositoryImpl extends MetadataJdbcRepository<Currency, Long>  implements CurrencyRepository {
    static BeanPropertyRowMapper<Currency> beanPropertyRowMapper = new BeanPropertyRowMapper<>(Currency.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<Currency> documentTypeUpdater = (t, mapping) -> {
        mapping.put(Currency.COLUMN_NAME_C_CURRENCY_ID, t.getId());
        mapping.put(Currency.COLUMN_NAME_VALUE_CODE, t.getValueCode());
        mapping.put(Currency.COLUMN_NAME_DESCRIPTION, t.getDescription());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(Currency.COLUMN_NAME_C_CURRENCY_ID, Types.BIGINT),
            entry(Currency.COLUMN_NAME_VALUE_CODE, Types.NVARCHAR),
            entry(Currency.COLUMN_NAME_DESCRIPTION, Types.NVARCHAR)
    );

    static RowMapper<Currency> userRowMapper = (rs, rowNum) -> new Currency(
            rs.getLong(Currency.COLUMN_NAME_C_CURRENCY_ID),
            rs.getString(Currency.COLUMN_NAME_VALUE_CODE),
            rs.getString(Currency.COLUMN_NAME_DESCRIPTION)
    );

    @Autowired
    public CurrencyRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                Currency.TABLE_NAME,
                Currency.COLUMN_NAME_C_CURRENCY_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" COUNT(1) ");
        sql.append(" FROM ");
        sql.append(" C_CURRENCY cr ");
        sql.append(" WHERE ");
        sql.append(" cr.ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "cr.ISO_CODE", "cr.DESCRIPTION"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<Currency> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" cr.C_CURRENCY_ID, ");
        sql.append(" cr.ISO_CODE AS VALUECODE, ");
        sql.append(" cr.DESCRIPTION ");
        sql.append(" FROM ");
        sql.append(" C_CURRENCY cr ");
        sql.append(" WHERE ");
        sql.append(" cr.ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "cr.ISO_CODE", "cr.DESCRIPTION"));
        }
        sql.append(" ORDER BY ");
        sql.append(" cr.ISO_CODE ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public Currency findOneByValueCode(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" cr.C_CURRENCY_ID, ");
        sql.append(" cr.ISO_CODE AS VALUECODE, ");
        sql.append(" cr.DESCRIPTION ");
        sql.append(" FROM ");
        sql.append(" C_CURRENCY cr ");
        sql.append(" WHERE ");
        sql.append(" cr.ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND cr.ISO_CODE like ? ");
        }
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[] { valueCode }, userRowMapper);
    }
}
