package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.BankBranch;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.BankBranchRepository;
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
public class BankBranchRepositoryImpl extends MetadataJdbcRepository<BankBranch, Long>  implements BankBranchRepository {
    static BeanPropertyRowMapper<BankBranch> beanPropertyRowMapper = new BeanPropertyRowMapper<>(BankBranch.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<BankBranch> bankBranchUpdater = (t, mapping) -> {
        mapping.put(BankBranch.COLUMN_NAME_C_BANK_ID, t.getId());
        mapping.put(BankBranch.COLUMN_NAME_NAME, t.getName());
        mapping.put(BankBranch.COLUMN_NAME_DESCRIPTION, t.getDescription());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(BankBranch.COLUMN_NAME_C_BANK_ID, Types.BIGINT),
            entry(BankBranch.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(BankBranch.COLUMN_NAME_DESCRIPTION, Types.NVARCHAR)
    );

    static RowMapper<BankBranch> userRowMapper = (rs, rowNum) -> new BankBranch(
            rs.getLong(BankBranch.COLUMN_NAME_C_BANK_ID),
            rs.getString(BankBranch.COLUMN_NAME_NAME),
            rs.getString(BankBranch.COLUMN_NAME_DESCRIPTION),
            rs.getString(BankBranch.COLUMN_NAME_BANK_CODE)
    );

    @Autowired
    public BankBranchRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                bankBranchUpdater,
                updaterType,
                BankBranch.TABLE_NAME,
                BankBranch.COLUMN_NAME_C_BANK_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValue(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) ");
        sql.append(" FROM C_BANK BB ");
        sql.append(" LEFT JOIN TH_CABANKINFO BF ON BB.C_BANK_ID = BF.C_BANK_ID ");
        sql.append(" LEFT JOIN TH_CABANK CB ON BF.TH_CABANK_ID = CB.TH_CABANK_ID ");
        sql.append(" WHERE BB.ISACTIVE = 'Y' ");
        sql.append(" AND BF.ISACTIVE = 'Y' ");
        sql.append(" AND CB.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "BB.ROUTINGNO", "BB.NAME", "BB.DESCRIPTION"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<BankBranch> findAllByValue(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT BB.C_BANK_ID ");
        sql.append(" , BB.ROUTINGNO AS VALUECODE ");
        sql.append(" , BB.NAME ");
        sql.append(" , BB.DESCRIPTION ");
        sql.append(" , CB.VALUECODE AS BANK_CODE ");
        sql.append(" FROM C_BANK BB ");
        sql.append(" LEFT JOIN TH_CABANKINFO BF ON BB.C_BANK_ID = BF.C_BANK_ID ");
        sql.append(" LEFT JOIN TH_CABANK CB ON BF.TH_CABANK_ID = CB.TH_CABANK_ID ");
        sql.append(" WHERE BB.ISACTIVE = 'Y' ");
        sql.append(" AND BF.ISACTIVE = 'Y' ");
        sql.append(" AND CB.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "BB.ROUTINGNO", "BB.NAME", "BB.DESCRIPTION"));
        }
        sql.append(" ORDER BY ");
        sql.append(" BB.ROUTINGNO ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public BankBranch findOneByValue(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" BB.C_BANK_ID, ");
        sql.append(" BB.ROUTINGNO AS VALUECODE, ");
        sql.append(" BB.NAME AS NAME, ");
        sql.append(" BB.DESCRIPTION AS DESCRIPTION, ");
        sql.append(" CB.VALUECODE AS BANK_CODE ");
        sql.append(" FROM C_BANK BB ");
        sql.append(" LEFT JOIN TH_CABANKINFO BF ON BB.C_BANK_ID = BF.C_BANK_ID ");
        sql.append(" LEFT JOIN TH_CABANK CB ON BF.TH_CABANK_ID = CB.TH_CABANK_ID ");
        sql.append(" WHERE BB.ISACTIVE = 'Y' ");
        sql.append(" AND BF.ISACTIVE = 'Y' ");
        sql.append(" AND CB.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND BB.ROUTINGNO like ? ");
        }
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[] { valueCode }, userRowMapper);
    }

}
