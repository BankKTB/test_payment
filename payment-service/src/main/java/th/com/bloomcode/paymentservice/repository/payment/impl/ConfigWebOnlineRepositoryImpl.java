package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.ConfigWebOnline;
import th.com.bloomcode.paymentservice.model.payment.ConfigWebOnline;
import th.com.bloomcode.paymentservice.model.payment.ConfigWebOnline;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.ConfigWebOnlineRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class ConfigWebOnlineRepositoryImpl extends MetadataJdbcRepository<ConfigWebOnline, Long> implements ConfigWebOnlineRepository {
    static BeanPropertyRowMapper<ConfigWebOnline> beanPropertyRowMapper = new BeanPropertyRowMapper<>(ConfigWebOnline.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<ConfigWebOnline> ConfigWebOnlineUpdater = (t, mapping) -> {
        mapping.put(ConfigWebOnline.COLUMN_NAME_CONFIG_WEB_ONLINE_ID, t.getId());
        mapping.put(ConfigWebOnline.COLUMN_NAME_VALUE_CODE, t.getValueCode());
        mapping.put(ConfigWebOnline.COLUMN_NAME_NAME, t.getName());
        mapping.put(ConfigWebOnline.COLUMN_NAME_URL, t.getUrl());
        mapping.put(ConfigWebOnline.COLUMN_NAME_IS_ACTIVE, t.isActive());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(ConfigWebOnline.COLUMN_NAME_CONFIG_WEB_ONLINE_ID, Types.BIGINT),
            entry(ConfigWebOnline.COLUMN_NAME_VALUE_CODE, Types.NVARCHAR),
            entry(ConfigWebOnline.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(ConfigWebOnline.COLUMN_NAME_URL, Types.NVARCHAR),
            entry(ConfigWebOnline.COLUMN_NAME_IS_ACTIVE, Types.BOOLEAN)
    );

    static RowMapper<ConfigWebOnline> userRowMapper = (rs, rowNum) -> new ConfigWebOnline(
            rs.getLong(ConfigWebOnline.COLUMN_NAME_CONFIG_WEB_ONLINE_ID),
            rs.getString(ConfigWebOnline.COLUMN_NAME_VALUE_CODE),
            rs.getString(ConfigWebOnline.COLUMN_NAME_NAME),
            rs.getString(ConfigWebOnline.COLUMN_NAME_URL),
            rs.getBoolean(ConfigWebOnline.COLUMN_NAME_IS_ACTIVE)
    );

    public ConfigWebOnlineRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, ConfigWebOnlineUpdater, updaterType, ConfigWebOnline.TABLE_NAME, ConfigWebOnline.COLUMN_NAME_CONFIG_WEB_ONLINE_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ConfigWebOnline findFirstByValueCodeStartingWithAndIsActiveTrue(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(ConfigWebOnline.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND VALUE_CODE like ?%");
        }
        sql.append(" AND IS_ACTIVE = 1 ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<ConfigWebOnline> CcnfigWebOnlines = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!CcnfigWebOnlines.isEmpty()) {
            return CcnfigWebOnlines.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<ConfigWebOnline> findAllByIsActiveTrue() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(ConfigWebOnline.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND IS_ACTIVE = 1 ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

}
