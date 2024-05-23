package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.HouseBankPaymentMethod;
import th.com.bloomcode.paymentservice.model.idem.HouseBankPaymentMethod;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.HouseBankPaymentMethodRepository;
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
public class HouseBankPaymentMethodRepositoryImpl extends MetadataJdbcRepository<HouseBankPaymentMethod, Long> implements HouseBankPaymentMethodRepository {
    static BeanPropertyRowMapper<HouseBankPaymentMethod> beanPropertyRowMapper = new BeanPropertyRowMapper<>(HouseBankPaymentMethod.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<HouseBankPaymentMethod> documentTypeUpdater = (t, mapping) -> {
        mapping.put(HouseBankPaymentMethod.COLUMN_NAME_HOUSEBANKACCOUNT_PM_ID, t.getId());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(HouseBankPaymentMethod.COLUMN_NAME_HOUSEBANKACCOUNT_PM_ID, Types.BIGINT)
    );

    static RowMapper<HouseBankPaymentMethod> userRowMapper = (rs, rowNum) -> new HouseBankPaymentMethod(
            rs.getLong(HouseBankPaymentMethod.COLUMN_NAME_HOUSEBANKACCOUNT_PM_ID),
            rs.getString(HouseBankPaymentMethod.COLUMN_NAME_HOUSEBANK),
            rs.getString(HouseBankPaymentMethod.COLUMN_NAME_BANKBRANCH),
            rs.getString(HouseBankPaymentMethod.COLUMN_NAME_COUNTRYCODE),
            rs.getString(HouseBankPaymentMethod.COLUMN_NAME_PAYMENT_METHOD),
            rs.getString(HouseBankPaymentMethod.COLUMN_NAME_CURRENCY),
            rs.getString(HouseBankPaymentMethod.COLUMN_NAME_ACCOUNTCODE),
            rs.getString(HouseBankPaymentMethod.COLUMN_NAME_GLACCOUNT),
            rs.getString(HouseBankPaymentMethod.COLUMN_NAME_BANKACCOUNTNO)
    );

    @Autowired
    public HouseBankPaymentMethodRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                HouseBankPaymentMethod.TABLE_NAME,
                HouseBankPaymentMethod.COLUMN_NAME_HOUSEBANKACCOUNT_PM_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" COUNT(1) ");
        sql.append(" FROM TH_CAHOUSEBANKACCOUNT_PM pm ");
        sql.append(" LEFT JOIN TH_CAHOUSEBANKACCOUNT ba ON pm.TH_CAHOUSEBANKACCOUNT_ID = ba.TH_CAHOUSEBANKACCOUNT_ID ");
        sql.append(" LEFT JOIN AD_CLIENT cl ON ba.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
        sql.append(" LEFT JOIN C_CURRENCY cr ON ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
        sql.append(" LEFT JOIN C_ElementValue el ON ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
        sql.append(" LEFT JOIN TH_CAPAYMENTMETHOD mt ON pm.TH_CAPAYMENTMETHOD_ID = mt.TH_CAPAYMENTMETHOD_ID ");
        sql.append(" LEFT JOIN TH_CAHOUSEBANK hb ON ba.TH_CAHOUSEBANK_ID = hb.TH_CAHOUSEBANK_ID ");
        sql.append(" LEFT JOIN C_BANK cb ON hb.C_BANK_ID = cb.C_BANK_ID ");
        sql.append(" LEFT JOIN C_COUNTRY ct ON hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sql.append(" WHERE pm.ISACTIVE = 'Y' ");
        sql.append(" and ba.ISACTIVE = 'Y' ");
        sql.append(" and cl.ISACTIVE = 'Y' ");
        sql.append(" and cr.ISACTIVE = 'Y' ");
        sql.append(" and el.ISACTIVE = 'Y' ");
        sql.append(" and mt.ISACTIVE = 'Y' ");
        sql.append(" and hb.ISACTIVE = 'Y' ");
        sql.append(" and cb.ISACTIVE = 'Y' ");
        sql.append(" and ct.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "CL.VALUE"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<HouseBankPaymentMethod> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" pm.TH_CAHOUSEBANKACCOUNT_PM_ID, ");
        sql.append(" hb.VALUECODE AS HOUSEBANK, ");
        sql.append(" hb.SHORTDESCRIPTION AS HOUSEBANKNAME, ");
        sql.append(" cb.ROUTINGNO   AS BANKBRANCH, ");
        sql.append(" ct.COUNTRYCODE as COUNTRYCODE, ");
        sql.append(" mt.VALUECODE AS PAYMENTMETHOD, ");
        sql.append(" cr.ISO_CODE  AS CURRENCY, ");
        sql.append(" ba.VALUECODE AS ACCOUNTCODE, ");
        sql.append(" el.VALUE     AS GLACCOUNT, ");
        sql.append(" ba.ACCOUNTNO   AS BANKACCOUNTNO ");
        sql.append(" FROM TH_CAHOUSEBANKACCOUNT_PM pm ");
        sql.append(" LEFT JOIN TH_CAHOUSEBANKACCOUNT ba ON pm.TH_CAHOUSEBANKACCOUNT_ID = ba.TH_CAHOUSEBANKACCOUNT_ID ");
        sql.append(" LEFT JOIN AD_CLIENT cl ON ba.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
        sql.append(" LEFT JOIN C_CURRENCY cr ON ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
        sql.append(" LEFT JOIN C_ElementValue el ON ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
        sql.append(" LEFT JOIN TH_CAPAYMENTMETHOD mt ON pm.TH_CAPAYMENTMETHOD_ID = mt.TH_CAPAYMENTMETHOD_ID ");
        sql.append(" LEFT JOIN TH_CAHOUSEBANK hb ON ba.TH_CAHOUSEBANK_ID = hb.TH_CAHOUSEBANK_ID ");
        sql.append(" LEFT JOIN C_BANK cb ON hb.C_BANK_ID = cb.C_BANK_ID ");
        sql.append(" LEFT JOIN C_COUNTRY ct ON hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sql.append(" WHERE pm.ISACTIVE = 'Y' ");
        sql.append(" and ba.ISACTIVE = 'Y' ");
        sql.append(" and cl.ISACTIVE = 'Y' ");
        sql.append(" and cr.ISACTIVE = 'Y' ");
        sql.append(" and el.ISACTIVE = 'Y' ");
        sql.append(" and mt.ISACTIVE = 'Y' ");
        sql.append(" and hb.ISACTIVE = 'Y' ");
        sql.append(" and cb.ISACTIVE = 'Y' ");
        sql.append(" and ct.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "CL.VALUE"));
        }
        sql.append(" ORDER BY ");
        sql.append(" mt.VALUECODE ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public HouseBankPaymentMethod findOneByValueCode(String client, String houseBankKey, String paymentMethod) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" pm.TH_CAHOUSEBANKACCOUNT_PM_ID, ");
        sql.append(" hb.VALUECODE AS HOUSEBANK, ");
        sql.append(" hb.SHORTDESCRIPTION AS HOUSEBANKNAME, ");
        sql.append(" cb.ROUTINGNO   AS BANKBRANCH, ");
        sql.append(" ct.COUNTRYCODE as COUNTRYCODE, ");
        sql.append(" mt.VALUECODE AS PAYMENTMETHOD, ");
        sql.append(" cr.ISO_CODE  AS CURRENCY, ");
        sql.append(" ba.VALUECODE AS ACCOUNTCODE, ");
        sql.append(" el.VALUE     AS GLACCOUNT, ");
        sql.append(" ba.ACCOUNTNO   AS BANKACCOUNTNO ");
        sql.append(" FROM TH_CAHOUSEBANKACCOUNT_PM pm ");
        sql.append(" LEFT JOIN TH_CAHOUSEBANKACCOUNT ba ON pm.TH_CAHOUSEBANKACCOUNT_ID = ba.TH_CAHOUSEBANKACCOUNT_ID ");
        sql.append(" LEFT JOIN AD_CLIENT cl ON ba.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
        sql.append(" LEFT JOIN C_CURRENCY cr ON ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
        sql.append(" LEFT JOIN C_ElementValue el ON ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
        sql.append(" LEFT JOIN TH_CAPAYMENTMETHOD mt ON pm.TH_CAPAYMENTMETHOD_ID = mt.TH_CAPAYMENTMETHOD_ID ");
        sql.append(" LEFT JOIN TH_CAHOUSEBANK hb ON ba.TH_CAHOUSEBANK_ID = hb.TH_CAHOUSEBANK_ID ");
        sql.append(" LEFT JOIN C_BANK cb ON hb.C_BANK_ID = cb.C_BANK_ID ");
        sql.append(" LEFT JOIN C_COUNTRY ct ON hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sql.append(" WHERE pm.ISACTIVE = 'Y' ");
        sql.append(" and ba.ISACTIVE = 'Y' ");
        sql.append(" and cl.ISACTIVE = 'Y' ");
        sql.append(" and cr.ISACTIVE = 'Y' ");
        sql.append(" and el.ISACTIVE = 'Y' ");
        sql.append(" and mt.ISACTIVE = 'Y' ");
        sql.append(" and hb.ISACTIVE = 'Y' ");
        sql.append(" and cb.ISACTIVE = 'Y' ");
        sql.append(" and ct.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(client)) {
            SqlUtil.whereClause(client, "CL.VALUE" , params);
        }

        if (!Util.isEmpty(houseBankKey)) {
            SqlUtil.whereClause(houseBankKey, "hb.VALUECODE" , params);
        }

        if (!Util.isEmpty(paymentMethod)) {
            SqlUtil.whereClause(paymentMethod, "mt.VALUECODE" , params);
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, userRowMapper);
    }
}
