package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.OmDisplayColumnTable;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.OmDisplayColumnTableRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class OmDisplayColumnTableRepositoryImpl extends MetadataJdbcRepository<OmDisplayColumnTable, Long> implements OmDisplayColumnTableRepository {
    static BeanPropertyRowMapper<OmDisplayColumnTable> beanPropertyRowMapper = new BeanPropertyRowMapper<>(OmDisplayColumnTable.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<OmDisplayColumnTable> OmDisplayColumnTableUpdater = (t, mapping) -> {
        mapping.put(OmDisplayColumnTable.COLUMN_NAME_OM_DISPLAY_COLUMN_TABLE_ID, t.getId());
        mapping.put(OmDisplayColumnTable.COLUMN_NAME_NAME, t.getName());
        mapping.put(OmDisplayColumnTable.COLUMN_NAME_ROLE, t.getRole());
        mapping.put(OmDisplayColumnTable.COLUMN_NAME_IS_USER_ONLY, t.getIsUserOnly());
        mapping.put(OmDisplayColumnTable.COLUMN_NAME_JSON_TEXT, t.getJsonText());
        mapping.put(OmDisplayColumnTable.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(OmDisplayColumnTable.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(OmDisplayColumnTable.COLUMN_NAME_UPDATED, t.getUpdated());
    };
    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(OmDisplayColumnTable.COLUMN_NAME_OM_DISPLAY_COLUMN_TABLE_ID, Types.BIGINT),
            entry(OmDisplayColumnTable.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(OmDisplayColumnTable.COLUMN_NAME_ROLE, Types.NVARCHAR),
            entry(OmDisplayColumnTable.COLUMN_NAME_IS_USER_ONLY, Types.BOOLEAN),
            entry(OmDisplayColumnTable.COLUMN_NAME_JSON_TEXT, Types.NVARCHAR),
            entry(OmDisplayColumnTable.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(OmDisplayColumnTable.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(OmDisplayColumnTable.COLUMN_NAME_UPDATED, Types.TIMESTAMP)
    );

    static RowMapper<OmDisplayColumnTable> userRowMapper = (rs, rowNum) -> new OmDisplayColumnTable(
            rs.getLong(OmDisplayColumnTable.COLUMN_NAME_OM_DISPLAY_COLUMN_TABLE_ID),
            rs.getString(OmDisplayColumnTable.COLUMN_NAME_NAME),
            rs.getString(OmDisplayColumnTable.COLUMN_NAME_ROLE),
            rs.getBoolean(OmDisplayColumnTable.COLUMN_NAME_IS_USER_ONLY),
            rs.getString(OmDisplayColumnTable.COLUMN_NAME_JSON_TEXT)
            );

    public OmDisplayColumnTableRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, OmDisplayColumnTableUpdater, updaterType, OmDisplayColumnTable.TABLE_NAME, OmDisplayColumnTable.COLUMN_NAME_OM_DISPLAY_COLUMN_TABLE_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public OmDisplayColumnTable findOneByRoleAndName(String role, String name) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(OmDisplayColumnTable.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(role)) {
            sql.append(SqlUtil.whereClause(role, OmDisplayColumnTable.COLUMN_NAME_ROLE, params));
        }
        if (!Util.isEmpty(name)) {
            sql.append(SqlUtil.whereClause(name, OmDisplayColumnTable.COLUMN_NAME_NAME, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        List<OmDisplayColumnTable> omDisplayColumnTables = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!omDisplayColumnTables.isEmpty()) {
            return omDisplayColumnTables.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OmDisplayColumnTable> findAllByRoleAndUserAndValue(String role, String user, String value) {
        if (value != null) {
            value = value.replace("*", "%");
        }
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT * FROM " + OmDisplayColumnTable.TABLE_NAME);
        sb.append(" WHERE 1 = 1 ");
        params.add(role);
        params.add(user);
        sb.append(" AND (( " + OmDisplayColumnTable.COLUMN_NAME_ROLE  + " = ? AND " + OmDisplayColumnTable.COLUMN_NAME_CREATED_BY  + " = ? )  ");
        sb.append(" OR  ");
        params.add(role);
        sb.append(" ( " + OmDisplayColumnTable.COLUMN_NAME_ROLE  + " = ? AND " + OmDisplayColumnTable.COLUMN_NAME_IS_USER_ONLY  + " = 0 ))  ");
        if (!Util.isEmpty(value)) {
            sb.append(SqlUtil.whereClause(value, OmDisplayColumnTable.COLUMN_NAME_NAME, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("params : {} {} ", params, params.size());
        log.info("objParams : {} ", objParams);
        log.info("query : {} ", sb.toString());
        return this.jdbcTemplate.query(sb.toString(), objParams, beanPropertyRowMapper);
    }

}
