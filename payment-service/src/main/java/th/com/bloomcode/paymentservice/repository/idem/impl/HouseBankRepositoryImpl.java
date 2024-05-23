package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.HouseBank;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.HouseBankRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.Column;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class HouseBankRepositoryImpl extends MetadataJdbcRepository<HouseBank, Long> implements HouseBankRepository {
    static BeanPropertyRowMapper<HouseBank> beanPropertyRowMapper = new BeanPropertyRowMapper<>(HouseBank.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<HouseBank> documentTypeUpdater = (t, mapping) -> {
        mapping.put(HouseBank.COLUMN_NAME_TH_CAHOUSEBANK_ID, t.getId());
        mapping.put(HouseBank.COLUMN_NAME_VALUE_CODE, t.getValueCode());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(HouseBank.COLUMN_NAME_TH_CAHOUSEBANK_ID, Types.BIGINT),
            entry(HouseBank.COLUMN_NAME_VALUE_CODE, Types.NVARCHAR)
    );

    static RowMapper<HouseBank> userRowMapper = (rs, rowNum) -> new HouseBank(
            rs.getLong(HouseBank.COLUMN_NAME_TH_CAHOUSEBANK_ID),
            rs.getString(HouseBank.COLUMN_NAME_VALUE_CODE),
            rs.getString(HouseBank.COLUMN_NAME_COUNTRYCODE),
            rs.getString(HouseBank.COLUMN_NAME_COUNTRYNAME),
            rs.getString(HouseBank.COLUMN_NAME_SWIFTCODE),
            rs.getString(HouseBank.COLUMN_NAME_ADDRESS1),
            rs.getString(HouseBank.COLUMN_NAME_ADDRESS2),
            rs.getString(HouseBank.COLUMN_NAME_ADDRESS3),
            rs.getString(HouseBank.COLUMN_NAME_ADDRESS4),
            rs.getString(HouseBank.COLUMN_NAME_ADDRESS5)
    );

    @Autowired
    public HouseBankRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                HouseBank.TABLE_NAME,
                HouseBank.COLUMN_NAME_TH_CAHOUSEBANK_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" COUNT(1) ");
        sql.append(" FROM TH_CAHOUSEBANK hb ");
        sql.append(" LEFT JOIN C_BANK cb ON hb.C_BANK_ID = cb.C_BANK_ID ");
        sql.append(" LEFT JOIN AD_CLIENT cl ON hb.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
        sql.append(" LEFT JOIN C_COUNTRY ct ON hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sql.append(" LEFT JOIN C_LOCATION lc ON cb.C_LOCATION_ID = lc.C_LOCATION_ID ");
        sql.append(" WHERE  hb.ISACTIVE = 'Y' ");
        sql.append(" and cb.ISACTIVE = 'Y' ");
        sql.append(" and cl.ISACTIVE = 'Y' ");
        sql.append(" and ct.ISACTIVE = 'Y' ");
//        sql.append(" and lc.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "CL.VALUE"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<HouseBank> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" hb.TH_CAHOUSEBANK_ID, ");
        sql.append(" hb.VALUECODE, ");
        sql.append(" ct.COUNTRYCODE, ");
        sql.append(" ct.NAME as COUNTRYNAME, ");
        sql.append(" cb.ROUTINGNO AS BANKBRANCH, ");
        sql.append(" cb.DESCRIPTION, ");
        sql.append(" cb.SWIFTCODE, ");
        sql.append(" lc.ADDRESS1, ");
        sql.append(" lc.ADDRESS2, ");
        sql.append(" lc.ADDRESS3, ");
        sql.append(" lc.ADDRESS4, ");
        sql.append(" lc.ADDRESS5 ");
        sql.append(" FROM TH_CAHOUSEBANK hb ");
        sql.append(" LEFT JOIN C_BANK cb ON hb.C_BANK_ID = cb.C_BANK_ID ");
        sql.append(" LEFT JOIN AD_CLIENT cl ON hb.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
        sql.append(" LEFT JOIN C_COUNTRY ct ON hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sql.append(" LEFT JOIN C_LOCATION lc ON cb.C_LOCATION_ID = lc.C_LOCATION_ID ");
        sql.append(" WHERE  hb.ISACTIVE = 'Y' ");
        sql.append(" and cb.ISACTIVE = 'Y' ");
        sql.append(" and cl.ISACTIVE = 'Y' ");
        sql.append(" and ct.ISACTIVE = 'Y' ");
//        sql.append(" and lc.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "CL.VALUE"));
        }
        sql.append(" ORDER BY ");
        sql.append(" CL.VALUE ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public HouseBank findOneByValueCode(String valueCode, String compCode, String bankBranch) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" hb.TH_CAHOUSEBANK_ID, ");
        sql.append(" hb.VALUECODE, ");
        sql.append(" ct.COUNTRYCODE, ");
        sql.append(" ct.NAME as COUNTRYNAME, ");
        sql.append(" cb.ROUTINGNO AS BANKBRANCH, ");
        sql.append(" cb.DESCRIPTION, ");
        sql.append(" cb.SWIFTCODE, ");
        sql.append(" lc.ADDRESS1, ");
        sql.append(" lc.ADDRESS2, ");
        sql.append(" lc.ADDRESS3, ");
        sql.append(" lc.ADDRESS4, ");
        sql.append(" lc.ADDRESS5 ");
        sql.append(" FROM TH_CAHOUSEBANK hb ");
        sql.append(" LEFT JOIN C_BANK cb ON hb.C_BANK_ID = cb.C_BANK_ID ");
        sql.append(" LEFT JOIN AD_CLIENT cl ON hb.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
        sql.append(" LEFT JOIN C_COUNTRY ct ON hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sql.append(" LEFT JOIN C_LOCATION lc ON cb.C_LOCATION_ID = lc.C_LOCATION_ID ");
        sql.append(" WHERE  hb.ISACTIVE = 'Y' ");
        sql.append(" and cb.ISACTIVE = 'Y' ");
        sql.append(" and cl.ISACTIVE = 'Y' ");
        sql.append(" and ct.ISACTIVE = 'Y' ");
//        sql.append(" and lc.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(compCode)) {
            sql.append(" AND cr.VALUE like ? ");
        }
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND hb.VALUECODE like ? ");
        }
        if (!Util.isEmpty(bankBranch)) {
            sql.append(" AND cr.ISO_CODE like ? ");
        }
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[] { compCode, valueCode, bankBranch }, userRowMapper);
    }
}
