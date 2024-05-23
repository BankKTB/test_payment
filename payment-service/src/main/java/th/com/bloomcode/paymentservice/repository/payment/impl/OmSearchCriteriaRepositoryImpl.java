package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.OmSearchCriteria;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.OmSearchCriteriaRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class OmSearchCriteriaRepositoryImpl extends MetadataJdbcRepository<OmSearchCriteria, Long> implements OmSearchCriteriaRepository {
    static BeanPropertyRowMapper<OmSearchCriteria> beanPropertyRowMapper = new BeanPropertyRowMapper<>(OmSearchCriteria.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<OmSearchCriteria> OmSearchCriteriaUpdater = (t, mapping) -> {
        mapping.put(OmSearchCriteria.COLUMN_NAME_OM_SEARCH_CRITERIA_ID, t.getId());
        mapping.put(OmSearchCriteria.COLUMN_NAME_NAME, t.getName());
        mapping.put(OmSearchCriteria.COLUMN_NAME_ROLE, t.getRole());
        mapping.put(OmSearchCriteria.COLUMN_NAME_IS_USER_ONLY, t.getIsUserOnly());
        mapping.put(OmSearchCriteria.COLUMN_NAME_JSON_TEXT, t.getJsonText());
        mapping.put(OmSearchCriteria.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(OmSearchCriteria.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(OmSearchCriteria.COLUMN_NAME_UPDATED, t.getUpdated());
    };
    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(OmSearchCriteria.COLUMN_NAME_OM_SEARCH_CRITERIA_ID, Types.BIGINT),
            entry(OmSearchCriteria.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(OmSearchCriteria.COLUMN_NAME_ROLE, Types.NVARCHAR),
            entry(OmSearchCriteria.COLUMN_NAME_IS_USER_ONLY, Types.BOOLEAN),
            entry(OmSearchCriteria.COLUMN_NAME_JSON_TEXT, Types.NVARCHAR),
            entry(OmSearchCriteria.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(OmSearchCriteria.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(OmSearchCriteria.COLUMN_NAME_UPDATED, Types.TIMESTAMP)
    );

    static RowMapper<OmSearchCriteria> userRowMapper = (rs, rowNum) -> new OmSearchCriteria(
            rs.getLong(OmSearchCriteria.COLUMN_NAME_OM_SEARCH_CRITERIA_ID),
            rs.getString(OmSearchCriteria.COLUMN_NAME_NAME),
            rs.getString(OmSearchCriteria.COLUMN_NAME_ROLE),
            rs.getBoolean(OmSearchCriteria.COLUMN_NAME_IS_USER_ONLY),
            rs.getString(OmSearchCriteria.COLUMN_NAME_JSON_TEXT)
    );

    public OmSearchCriteriaRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, OmSearchCriteriaUpdater, updaterType, OmSearchCriteria.TABLE_NAME, OmSearchCriteria.COLUMN_NAME_OM_SEARCH_CRITERIA_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public OmSearchCriteria findOneByNameAndRole(String name, String role) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(OmSearchCriteria.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(name)) {
            sql.append(SqlUtil.whereClause(name, OmSearchCriteria.COLUMN_NAME_NAME, params));
        }
        if (!Util.isEmpty(role)) {
            sql.append(SqlUtil.whereClause(role, OmSearchCriteria.COLUMN_NAME_ROLE, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        List<OmSearchCriteria> omDisplayColumnTables = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!omDisplayColumnTables.isEmpty()) {
            return omDisplayColumnTables.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OmSearchCriteria> findAllByRoleAndUserAndValue(String role, String user, String value) {
        if (value != null) {
            value = value.replace("*", "%");
        }
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT * FROM " + OmSearchCriteria.TABLE_NAME);
        sb.append(" WHERE 1 = 1 ");
        params.add(role);
        params.add(user);
        sb.append(" AND (( " + OmSearchCriteria.COLUMN_NAME_ROLE  + " = ? AND " + OmSearchCriteria.COLUMN_NAME_CREATED_BY  + " = ? )  ");
        sb.append(" OR  ");
        params.add(role);
        sb.append(" ( " + OmSearchCriteria.COLUMN_NAME_ROLE  + " = ? AND " + OmSearchCriteria.COLUMN_NAME_IS_USER_ONLY  + " = 0 ))  ");
        if (!Util.isEmpty(value)) {
            sb.append(SqlUtil.whereClause(value, OmSearchCriteria.COLUMN_NAME_NAME, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("params : {} {} ", params, params.size());
        log.info("objParams : {} ", objParams);
        log.info("query : {} ", sb.toString());
        return this.jdbcTemplate.query(sb.toString(), objParams, beanPropertyRowMapper);
    }


}
