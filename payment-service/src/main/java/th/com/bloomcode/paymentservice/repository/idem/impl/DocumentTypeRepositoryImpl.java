package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.helper.DBConnection;
import th.com.bloomcode.paymentservice.model.idem.DocumentType;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.DocumentTypeRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class DocumentTypeRepositoryImpl extends MetadataJdbcRepository<DocumentType, Long> implements DocumentTypeRepository {
    static BeanPropertyRowMapper<DocumentType> beanPropertyRowMapper = new BeanPropertyRowMapper<>(DocumentType.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<DocumentType> documentTypeUpdater = (t, mapping) -> {
        mapping.put(DocumentType.COLUMN_NAME_ID, t.getId());
        mapping.put(DocumentType.COLUMN_NAME_NAME, t.getName());
        mapping.put(DocumentType.COLUMN_NAME_DESCRIPTION, t.getDescription());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(DocumentType.COLUMN_NAME_ID, Types.BIGINT),
            entry(DocumentType.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(DocumentType.COLUMN_NAME_DESCRIPTION, Types.NVARCHAR)
    );

    static RowMapper<DocumentType> userRowMapper = (rs, rowNum) -> new DocumentType(
            rs.getLong(DocumentType.COLUMN_NAME_ID),
            rs.getString(DocumentType.COLUMN_NAME_NAME),
            rs.getString(DocumentType.COLUMN_NAME_DESCRIPTION)
    );

    @Autowired
    public DocumentTypeRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                DocumentType.TABLE_NAME,
                DocumentType.COLUMN_NAME_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValue(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(1) ");
        sql.append(" FROM ");
        sql.append(" C_DOCTYPE dt ");
        sql.append(" WHERE ");
        sql.append(" dt.ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "dt.NAME", "dt.DESCRIPTION"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<DocumentType> findAllByValue(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct NAME ");
        sql.append(" , DESCRIPTION ");
        sql.append(" , C_DOCTYPE_ID ");
        sql.append(" FROM C_DOCTYPE ");
        sql.append(" WHERE length(NAME) = 2 ");
        sql.append(" and (DOCBASETYPE = 'GLJ' OR DOCBASETYPE = 'API' ");
        sql.append(" OR DOCBASETYPE = 'APP' OR DOCBASETYPE = 'APC' ) ");
        sql.append(" AND ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "NAME", "DESCRIPTION"));
        }
        sql.append(" ORDER BY ");
        sql.append(" NAME ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public DocumentType findOneByValue(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dt.C_DOCTYPE_ID ");
        sql.append(" , dt.NAME ");
        sql.append(" , dt.DESCRIPTION ");
        sql.append(" FROM C_DOCTYPE dt  ");
        sql.append(" WHERE dt.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND dt.NAME like ? ");
        }
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[] { valueCode }, userRowMapper);
    }
}
