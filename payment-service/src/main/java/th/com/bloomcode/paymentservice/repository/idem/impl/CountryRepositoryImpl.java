package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.Country;
import th.com.bloomcode.paymentservice.model.idem.Country;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.CountryRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CountryRepositoryImpl extends MetadataJdbcRepository<Country, Long> implements CountryRepository {
    static BeanPropertyRowMapper<Country> beanPropertyRowMapper = new BeanPropertyRowMapper<>(Country.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<Country> documentTypeUpdater = (t, mapping) -> {
        mapping.put(Country.COLUMN_NAME_C_COUNTRY_ID, t.getId());
        mapping.put(Country.COLUMN_NAME_COUNTRY_CODE, t.getValueCode());
        mapping.put(Country.COLUMN_NAME_NAME, t.getName());
        mapping.put(Country.COLUMN_NAME_DESCRIPTION, t.getDescription());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(Country.COLUMN_NAME_C_COUNTRY_ID, Types.BIGINT),
            entry(Country.COLUMN_NAME_COUNTRY_CODE, Types.NVARCHAR),
            entry(Country.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(Country.COLUMN_NAME_DESCRIPTION, Types.NVARCHAR)
    );

    static RowMapper<Country> userRowMapper = (rs, rowNum) -> new Country(
            rs.getLong(Country.COLUMN_NAME_C_COUNTRY_ID),
            rs.getString(Country.COLUMN_NAME_COUNTRY_CODE),
            rs.getString(Country.COLUMN_NAME_NAME),
            rs.getString(Country.COLUMN_NAME_DESCRIPTION)
    );

    @Autowired
    public CountryRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                Country.TABLE_NAME,
                Country.COLUMN_NAME_C_COUNTRY_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  COUNT(1) FROM C_COUNTRY CT WHERE CT.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "CT.COUNTRYCODE", "CT.NAME", "CT.DESCRIPTION"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<Country> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CT.C_COUNTRY_ID ");
        sql.append(" , CT.COUNTRYCODE ");
        sql.append(" , CT.NAME ");
        sql.append(" , CT.DESCRIPTION ");
        sql.append(" FROM C_COUNTRY CT ");
        sql.append(" WHERE CT.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "CT.COUNTRYCODE", "CT.NAME", "CT.DESCRIPTION"));
        }
        sql.append(" ORDER BY ");
        sql.append(" CT.COUNTRYCODE ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public Country findOneByValueCode(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CT.C_COUNTRY_ID ");
        sql.append(" , CT.COUNTRYCODE ");
        sql.append(" , CT.NAME ");
        sql.append(" , CT.DESCRIPTION ");
        sql.append(" FROM C_COUNTRY CT ");
        sql.append(" WHERE CT.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND CT.COUNTRYCODE like ? ");
        }
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[] { valueCode }, userRowMapper);
    }
}
