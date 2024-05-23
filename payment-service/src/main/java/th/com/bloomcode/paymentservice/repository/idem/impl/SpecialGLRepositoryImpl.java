package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.helper.DBConnection;
import th.com.bloomcode.paymentservice.model.idem.SpecialGL;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.SpecialGLRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class SpecialGLRepositoryImpl  extends MetadataJdbcRepository<SpecialGL, Long> implements SpecialGLRepository {
    static BeanPropertyRowMapper<SpecialGL> beanPropertyRowMapper = new BeanPropertyRowMapper<>(SpecialGL.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<SpecialGL> documentTypeUpdater = (t, mapping) -> {
        mapping.put(SpecialGL.COLUMN_NAME_TH_CASPECIALGL_ID, t.getSpecialGlId());
        mapping.put(SpecialGL.COLUMN_NAME_VALUECODE, t.getValueCode());
        mapping.put(SpecialGL.COLUMN_NAME_NAME, t.getName());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(SpecialGL.COLUMN_NAME_TH_CASPECIALGL_ID, Types.BIGINT),
            entry(SpecialGL.COLUMN_NAME_VALUECODE, Types.NVARCHAR),
            entry(SpecialGL.COLUMN_NAME_NAME, Types.NVARCHAR)
    );

    static RowMapper<SpecialGL> userRowMapper = (rs, rowNum) -> new SpecialGL(
            rs.getLong(SpecialGL.COLUMN_NAME_TH_CASPECIALGL_ID),
            rs.getString(SpecialGL.COLUMN_NAME_VALUECODE),
            rs.getString(SpecialGL.COLUMN_NAME_NAME)
    );

    @Autowired
    public SpecialGLRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                SpecialGL.TABLE_NAME,
                SpecialGL.COLUMN_NAME_TH_CASPECIALGL_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) ");
        sql.append(" FROM TH_CASPECIALGL spgl  ");
        sql.append(" WHERE spgl.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "spgl.VALUECODE", "spgl.NAME"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<SpecialGL> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT spgl.TH_CASPECIALGL_ID ");
        sql.append(" , spgl.VALUECODE ");
        sql.append(" , spgl.NAME ");
        sql.append(" FROM TH_CASPECIALGL spgl ");
        sql.append(" WHERE spgl.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "spgl.VALUECODE", "spgl.NAME"));
        }
        sql.append(" ORDER BY ");
        sql.append(" spgl.VALUECODE ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public SpecialGL findOneByValueCode(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" spgl.TH_CASPECIALGL_ID, ");
        sql.append(" spgl.VALUECODE, ");
        sql.append(" spgl.NAME ");
        sql.append(" FROM ");
        sql.append(" TH_CASPECIALGL spgl ");
        sql.append(" WHERE ");
        sql.append(" spgl.ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND spgl.VALUECODE like ? ");
        }
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[] { valueCode }, userRowMapper);
    }
}
