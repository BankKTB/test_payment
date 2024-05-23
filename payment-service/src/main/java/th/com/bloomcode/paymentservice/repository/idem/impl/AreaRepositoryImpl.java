package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.Area;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.AreaRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class AreaRepositoryImpl extends MetadataJdbcRepository<Area, Long> implements AreaRepository {
    static BeanPropertyRowMapper<Area> beanPropertyRowMapper = new BeanPropertyRowMapper<>(Area.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<Area> areaUpdater = (t, mapping) -> {
        mapping.put(Area.COLUMN_NAME_BUDGET_AREA_ID, t.getId());
        mapping.put(Area.COLUMN_NAME_VALUECODE, t.getUpdatedBy());
        mapping.put(Area.COLUMN_NAME_NAME, t.getName());
        mapping.put(Area.COLUMN_NAME_DESCRIPTION, t.getDescription());
        mapping.put(Area.COLUMN_NAME_FI_AREA, t.getFiArea());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(Area.COLUMN_NAME_BUDGET_AREA_ID, Types.BIGINT),
            entry(Area.COLUMN_NAME_VALUECODE, Types.NVARCHAR),
            entry(Area.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(Area.COLUMN_NAME_DESCRIPTION, Types.NVARCHAR),
            entry(Area.COLUMN_NAME_FI_AREA, Types.NVARCHAR)
    );

    static RowMapper<Area> userRowMapper = (rs, rowNum) -> new Area(
            rs.getLong(Area.COLUMN_NAME_BUDGET_AREA_ID),
            rs.getString(Area.COLUMN_NAME_VALUECODE),
            rs.getString(Area.COLUMN_NAME_NAME),
            rs.getString(Area.COLUMN_NAME_DESCRIPTION),
            rs.getString(Area.COLUMN_NAME_FI_AREA)
    );

    @Autowired
    public AreaRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                areaUpdater,
                updaterType,
                Area.TABLE_NAME,
                Area.COLUMN_NAME_BUDGET_AREA_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(1) FROM TH_BGBUDGETAREA BA WHERE BA.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "BA.VALUECODE", "BA.NAME", "BA.DESCRIPTION"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<Area> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT BA.TH_BGBUDGETAREA_ID ");
        sql.append(" , BA.VALUECODE ");
        sql.append(" , BA.NAME ");
        sql.append(" , BA.DESCRIPTION ");
        sql.append(" , BA.FIAREA ");
        sql.append(" FROM TH_BGBUDGETAREA BA WHERE BA.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "BA.VALUECODE", "BA.NAME", "BA.DESCRIPTION"));
        }
        sql.append(" ORDER BY ");
        sql.append(" BA.VALUECODE ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public Area findOneByValueCode(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT BA.TH_BGBUDGETAREA_ID ");
        sql.append(" , BA.VALUECODE ");
        sql.append(" , BA.NAME ");
        sql.append(" , BA.DESCRIPTION ");
        sql.append(" , BA.FIAREA ");
        sql.append(" FROM TH_BGBUDGETAREA BA ");
        sql.append(" WHERE BA.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND BA.VALUECODE like LPAD(?, 5, 'P') ");
        }
        List<Area> areas = this.jdbcTemplate.query(sql.toString(), new Object[] { valueCode }, userRowMapper);
        if (!areas.isEmpty()) {
            return areas.get(0);
        } else {
            return null;
        }
    }
}