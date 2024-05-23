package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.CompanyCode;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.CompanyCodeRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CompanyCodeRepositoryImpl extends MetadataJdbcRepository<CompanyCode, Long> implements CompanyCodeRepository {
    static BeanPropertyRowMapper<CompanyCode> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CompanyCode.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<CompanyCode> documentTypeUpdater = (t, mapping) -> {
        mapping.put(CompanyCode.COLUMN_NAME_TH_CACOMPCODE_ID, t.getCompCodeId());
        mapping.put(CompanyCode.COLUMN_NAME_VALUECODE, t.getValueCode());
        mapping.put(CompanyCode.COLUMN_NAME_NAME, t.getName());
        mapping.put(CompanyCode.COLUMN_NAME_ISACTIVE, t.getIsActive());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(CompanyCode.COLUMN_NAME_TH_CACOMPCODE_ID, Types.BIGINT),
            entry(CompanyCode.COLUMN_NAME_VALUECODE, Types.NVARCHAR),
            entry(CompanyCode.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(CompanyCode.COLUMN_NAME_ISACTIVE, Types.NVARCHAR)
    );

    static RowMapper<CompanyCode> userRowMapper = (rs, rowNum) -> new CompanyCode(
            rs.getLong(CompanyCode.COLUMN_NAME_TH_CACOMPCODE_ID),
            rs.getString(CompanyCode.COLUMN_NAME_VALUECODE),
            rs.getString(CompanyCode.COLUMN_NAME_NAME),
            rs.getString(CompanyCode.COLUMN_NAME_ISACTIVE),
            rs.getString(CompanyCode.COLUMN_NAME_OLD_COMPCODE),
            rs.getString(CompanyCode.COLUMN_NAME_MINISTRY)
    );


    @Autowired
    public CompanyCodeRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                CompanyCode.TABLE_NAME,
                CompanyCode.COLUMN_NAME_TH_CACOMPCODE_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) ");
        sql.append(" FROM TH_CACOMPCODE CC ");
        sql.append(" LEFT JOIN TH_CACOMPCODEMAPPING CMP ON  CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID AND CC.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID ");
        sql.append(" LEFT JOIN TH_CAMINISTRY CM ON CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID AND CM.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID ");
        sql.append(" WHERE CC.ISACTIVE = 'Y'");
        sql.append(" AND CMP.ISACTIVE = 'Y' ");
        sql.append(" AND CM.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "CC.VALUECODE", "CC.NAME", "CMP.OLDCOMPCODE", "CM.NAME"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<CompanyCode> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  CC.*, CMP.OLDCOMPCODE AS OLD_COMPCODE, CM.NAME AS MINISTRY  ");
        sql.append(" FROM TH_CACOMPCODE CC ");
        sql.append(" LEFT JOIN TH_CACOMPCODEMAPPING CMP ON  CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID AND CC.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID ");
        sql.append(" LEFT JOIN TH_CAMINISTRY CM ON CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID AND CM.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID ");
        sql.append(" WHERE CC.ISACTIVE = 'Y' ");
        sql.append(" AND CMP.ISACTIVE = 'Y' ");
        sql.append(" AND CM.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "CC.VALUECODE", "CC.NAME", "CMP.OLDCOMPCODE", "CM.NAME"));
        }
        sql.append(" ORDER BY ");
        sql.append(" CC.VALUECODE ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public CompanyCode findOneByValueCode(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  CC.*, CMP.OLDCOMPCODE AS OLD_COMPCODE, CM.NAME AS MINISTRY  ");
        sql.append(" FROM TH_CACOMPCODE CC ");
        sql.append(" LEFT JOIN TH_CACOMPCODEMAPPING CMP ON  CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID ");
        sql.append(" LEFT JOIN TH_CAMINISTRY CM ON CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID AND CM.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID ");
        sql.append(" WHERE CC.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND CC.VALUECODE like ? ");
        }
        List<CompanyCode> companyCodes = this.jdbcTemplate.query(sql.toString(), new Object[] { valueCode }, userRowMapper);
        if (!companyCodes.isEmpty()) {
            return companyCodes.get(0);
        } else {
            return null;
        }
    }

    @Override
    public CompanyCode findOneByOldValueCode(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  CC.*, CMP.OLDCOMPCODE AS OLD_COMPCODE, CM.NAME AS MINISTRY  ");
        sql.append(" FROM TH_CACOMPCODE CC ");
        sql.append(" LEFT JOIN TH_CACOMPCODEMAPPING CMP ON  CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID ");
        sql.append(" LEFT JOIN TH_CAMINISTRY CM ON CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID AND CM.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID ");
        sql.append(" WHERE CC.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND CMP.OLDCOMPCODE like ? ");
        }
        List<CompanyCode> companyCodes = this.jdbcTemplate.query(sql.toString(), new Object[] { valueCode }, userRowMapper);
        if (!companyCodes.isEmpty()) {
            return companyCodes.get(0);
        } else {
            return null;
        }
    }
}
