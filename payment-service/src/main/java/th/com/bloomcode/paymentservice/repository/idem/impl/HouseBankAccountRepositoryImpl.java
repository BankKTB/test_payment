package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.HouseBankAccount;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.HouseBankAccountRepository;
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
public class HouseBankAccountRepositoryImpl extends MetadataJdbcRepository<HouseBankAccount, Long> implements HouseBankAccountRepository {
    static BeanPropertyRowMapper<HouseBankAccount> beanPropertyRowMapper = new BeanPropertyRowMapper<>(HouseBankAccount.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<HouseBankAccount> documentTypeUpdater = (t, mapping) -> {
        mapping.put(HouseBankAccount.COLUMN_NAME_TH_CAHOUSEBANKACCOUNT_ID, t.getId());
        mapping.put(HouseBankAccount.COLUMN_NAME_VALUE_CODE, t.getValueCode());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(HouseBankAccount.COLUMN_NAME_TH_CAHOUSEBANKACCOUNT_ID, Types.BIGINT),
            entry(HouseBankAccount.COLUMN_NAME_VALUE_CODE, Types.NVARCHAR)
    );

    static RowMapper<HouseBankAccount> userRowMapper = (rs, rowNum) -> new HouseBankAccount(
            rs.getLong(HouseBankAccount.COLUMN_NAME_TH_CAHOUSEBANKACCOUNT_ID),
            rs.getString(HouseBankAccount.COLUMN_NAME_VALUE_CODE),
            rs.getString(HouseBankAccount.COLUMN_NAME_ACCOUNTCODE),
            rs.getString(HouseBankAccount.COLUMN_NAME_SHORTDESCRIPTION),
            rs.getString(HouseBankAccount.COLUMN_NAME_BANKACCOUNTNO),
            rs.getString(HouseBankAccount.COLUMN_NAME_GLACCOUNT),
            rs.getString(HouseBankAccount.COLUMN_NAME_COUNTRYCODE),
            rs.getString(HouseBankAccount.COLUMN_NAME_COUNTRYNAME),
            rs.getString(HouseBankAccount.COLUMN_NAME_BANKBRANCH),
            rs.getString(HouseBankAccount.COLUMN_NAME_DESCRIPTION),
            rs.getString(HouseBankAccount.COLUMN_NAME_SWIFTCODE),
            rs.getString(HouseBankAccount.COLUMN_NAME_ADDRESS1),
            rs.getString(HouseBankAccount.COLUMN_NAME_ADDRESS2),
            rs.getString(HouseBankAccount.COLUMN_NAME_ADDRESS3),
            rs.getString(HouseBankAccount.COLUMN_NAME_ADDRESS4),
            rs.getString(HouseBankAccount.COLUMN_NAME_ADDRESS5)
    );

    @Autowired
    public HouseBankAccountRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                HouseBankAccount.TABLE_NAME,
                HouseBankAccount.COLUMN_NAME_TH_CAHOUSEBANKACCOUNT_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" COUNT(1) ");
        sql.append(" FROM TH_CAHOUSEBANKACCOUNT ba ");
        sql.append(" LEFT JOIN TH_CAHOUSEBANK hb ON ba.TH_CAHOUSEBANK_ID = hb.TH_CAHOUSEBANK_ID ");
        sql.append(" LEFT JOIN C_BANK cb ON hb.C_BANK_ID = cb.C_BANK_ID ");
        sql.append(" LEFT JOIN C_COUNTRY ct ON hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sql.append(" LEFT JOIN C_LOCATION lc ON cb.C_LOCATION_ID = lc.C_LOCATION_ID ");
        sql.append(" LEFT JOIN C_ELEMENTVALUE el ON ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
        sql.append(" LEFT JOIN C_CURRENCY cr ON ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
        sql.append(" LEFT JOIN AD_CLIENT cl ON ba.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
        sql.append(" WHERE  ba.ISACTIVE = 'Y' ");
        sql.append(" and hb.ISACTIVE = 'Y' ");
        sql.append(" and cb.ISACTIVE = 'Y' ");
        sql.append(" and cl.ISACTIVE = 'Y' ");
        sql.append(" and ct.ISACTIVE = 'Y' ");
//        sql.append(" and lc.ISACTIVE = 'Y' ");
        sql.append(" and el.ISACTIVE = 'Y' ");
        sql.append(" and cr.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "CL.VALUE"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<HouseBankAccount> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" ba.TH_CAHOUSEBANKACCOUNT_ID, ");
        sql.append(" hb.VALUECODE, ");
        sql.append(" ba.VALUECODE as ACCOUNTCODE, ");
        sql.append(" ba.SHORTDESCRIPTION, ");
        sql.append(" ba.ACCOUNTNO as BANKACCOUNTNO, ");
        sql.append(" el.VALUE     as GLACCOUNT, ");
        sql.append(" ct.COUNTRYCODE, ");
        sql.append(" ct.NAME as COUNTRYNAME, ");
        sql.append(" cb.ROUTINGNO AS BANKBRANCH, ");
        sql.append(" cb.DESCRIPTION, ");
        sql.append(" cb.SWIFTCODE, ");
        sql.append(" lc.ADDRESS1, ");
        sql.append(" lc.ADDRESS2, ");
        sql.append(" lc.ADDRESS3, ");
        sql.append(" lc.ADDRESS4, ");
        sql.append(" lc.ADDRESS5, ");
        sql.append(" cr.ISO_CODE as CURRENCY ");
        sql.append(" FROM TH_CAHOUSEBANKACCOUNT ba ");
        sql.append(" LEFT JOIN TH_CAHOUSEBANK hb ON ba.TH_CAHOUSEBANK_ID = hb.TH_CAHOUSEBANK_ID ");
        sql.append(" LEFT JOIN C_BANK cb ON hb.C_BANK_ID = cb.C_BANK_ID ");
        sql.append(" LEFT JOIN C_COUNTRY ct ON hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sql.append(" LEFT JOIN C_LOCATION lc ON cb.C_LOCATION_ID = lc.C_LOCATION_ID ");
        sql.append(" LEFT JOIN C_ELEMENTVALUE el ON ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
        sql.append(" LEFT JOIN C_CURRENCY cr ON ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
        sql.append(" LEFT JOIN AD_CLIENT cl ON ba.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
        sql.append(" WHERE ba.ISACTIVE = 'Y' ");
        sql.append(" and hb.ISACTIVE = 'Y' ");
        sql.append(" and cb.ISACTIVE = 'Y' ");
        sql.append(" and cl.ISACTIVE = 'Y' ");
        sql.append(" and ct.ISACTIVE = 'Y' ");
//        sql.append(" and lc.ISACTIVE = 'Y' ");
        sql.append(" and el.ISACTIVE = 'Y' ");
        sql.append(" and cr.ISACTIVE = 'Y' ");
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
    public HouseBankAccount findOneByValueCode(String valueCode, String compCode, String accountCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" ba.TH_CAHOUSEBANKACCOUNT_ID, ");
        sql.append(" hb.VALUECODE, ");
        sql.append(" ba.VALUECODE as ACCOUNTCODE, ");
        sql.append(" ba.SHORTDESCRIPTION, ");
        sql.append(" ba.ACCOUNTNO as BANKACCOUNTNO, ");
        sql.append(" el.VALUE     as GLACCOUNT, ");
        sql.append(" ct.COUNTRYCODE, ");
        sql.append(" ct.NAME as COUNTRYNAME, ");
        sql.append(" cb.ROUTINGNO AS BANKBRANCH, ");
        sql.append(" cb.DESCRIPTION, ");
        sql.append(" cb.SWIFTCODE, ");
        sql.append(" lc.ADDRESS1, ");
        sql.append(" lc.ADDRESS2, ");
        sql.append(" lc.ADDRESS3, ");
        sql.append(" lc.ADDRESS4, ");
        sql.append(" lc.ADDRESS5, ");
        sql.append(" cr.ISO_CODE as CURRENCY ");
        sql.append(" FROM TH_CAHOUSEBANKACCOUNT ba ");
        sql.append(" LEFT JOIN TH_CAHOUSEBANK hb ON ba.TH_CAHOUSEBANK_ID = hb.TH_CAHOUSEBANK_ID ");
        sql.append(" LEFT JOIN C_BANK cb ON hb.C_BANK_ID = cb.C_BANK_ID ");
        sql.append(" LEFT JOIN C_COUNTRY ct ON hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sql.append(" LEFT JOIN C_LOCATION lc ON cb.C_LOCATION_ID = lc.C_LOCATION_ID ");
        sql.append(" LEFT JOIN C_ELEMENTVALUE el ON ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
        sql.append(" LEFT JOIN C_CURRENCY cr ON ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
        sql.append(" LEFT JOIN AD_CLIENT cl ON ba.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
        sql.append(" WHERE ba.ISACTIVE = 'Y' ");
        sql.append(" and hb.ISACTIVE = 'Y' ");
        sql.append(" and cb.ISACTIVE = 'Y' ");
        sql.append(" and cl.ISACTIVE = 'Y' ");
        sql.append(" and ct.ISACTIVE = 'Y' ");
//        sql.append(" and lc.ISACTIVE = 'Y' ");
        sql.append(" and el.ISACTIVE = 'Y' ");
        sql.append(" and cr.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sql.append(" AND CL.VALUE like ? ");
        }
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND hb.VALUECODE like ? ");
        }
        if (!Util.isEmpty(accountCode)) {
            sql.append(" AND ba.VALUECODE like ? ");
        }
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[] { compCode, valueCode, accountCode }, userRowMapper);
    }
}
