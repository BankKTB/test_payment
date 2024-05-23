package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.HouseBankAccount;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HouseBankAccountRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<HouseBankAccount> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" ba.TH_CAHOUSEBANKACCOUNT_ID, ");
        sb.append(" hb.VALUECODE, ");
        sb.append(" ba.VALUECODE as ACCOUNTCODE, ");
        sb.append(" ba.SHORTDESCRIPTION, ");
        sb.append(" ba.ACCOUNTNO as BANKACCOUNTNO, ");
        sb.append(" el.VALUE     as GLACCOUNT, ");
        sb.append(" ct.COUNTRYCODE, ");
        sb.append(" ct.NAME as COUNTRYNAME, ");
        sb.append(" cb.ROUTINGNO AS BANKBRANCH, ");
        sb.append(" cb.DESCRIPTION, ");
        sb.append(" cb.SWIFTCODE, ");
        sb.append(" lc.ADDRESS1, ");
        sb.append(" lc.ADDRESS2, ");
        sb.append(" lc.ADDRESS3, ");
        sb.append(" lc.ADDRESS4, ");
        sb.append(" lc.ADDRESS5, ");
        sb.append(" cr.ISO_CODE as CURRENCY ");
        sb.append(" FROM ");
        sb.append(" TH_CAHOUSEBANKACCOUNT ba, ");
        sb.append(" TH_CAHOUSEBANK hb, ");
        sb.append(" C_BANK cb, ");
        sb.append(" AD_CLIENT cl, ");
        sb.append(" C_COUNTRY ct, ");
        sb.append(" C_LOCATION lc, ");
        sb.append(" C_ELEMENTVALUE el, ");
        sb.append(" C_CURRENCY cr ");
        sb.append(" WHERE ");
        sb.append(" ba.TH_CAHOUSEBANK_ID = hb.TH_CAHOUSEBANK_ID ");
        sb.append(" and hb.C_BANK_ID = cb.C_BANK_ID ");
        sb.append(" and hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sb.append(" and cb.C_LOCATION_ID=lc.C_LOCATION_ID ");
        sb.append(" and ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
        sb.append(" and ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
        sb.append(" and ba.ISACTIVE = 'Y' ");
        sb.append(" and hb.ISACTIVE = 'Y' ");
        sb.append(" and cb.ISACTIVE = 'Y' ");
        sb.append(" and cl.ISACTIVE = 'Y' ");
        sb.append(" and ct.ISACTIVE = 'Y' ");
        sb.append(" and lc.ISACTIVE = 'Y' ");
        sb.append(" and el.ISACTIVE = 'Y' ");
        sb.append(" and cr.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CL.VALUE"));
        }

        sb.append(" ORDER BY ");
        sb.append(" CL.VALUE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), HouseBankAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<HouseBankAccount> houseBankAccount = q.getResultList();
        logger.info("houseBankAccount : {}", houseBankAccount);

        return houseBankAccount;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_CAHOUSEBANKACCOUNT ba, ");
        sb.append(" TH_CAHOUSEBANK hb, ");
        sb.append(" C_BANK cb, ");
        sb.append(" AD_CLIENT cl, ");
        sb.append(" C_COUNTRY ct, ");
        sb.append(" C_LOCATION lc, ");
        sb.append(" C_ELEMENTVALUE el, ");
        sb.append(" C_CURRENCY cr ");
        sb.append(" WHERE ");
        sb.append(" ba.TH_CAHOUSEBANK_ID = hb.TH_CAHOUSEBANK_ID ");
        sb.append(" and hb.C_BANK_ID = cb.C_BANK_ID ");
        sb.append(" and hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sb.append(" and cb.C_LOCATION_ID=lc.C_LOCATION_ID ");
        sb.append(" and ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
        sb.append(" and ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
        sb.append(" and ba.ISACTIVE = 'Y' ");
        sb.append(" and hb.ISACTIVE = 'Y' ");
        sb.append(" and cb.ISACTIVE = 'Y' ");
        sb.append(" and cl.ISACTIVE = 'Y' ");
        sb.append(" and ct.ISACTIVE = 'Y' ");
        sb.append(" and lc.ISACTIVE = 'Y' ");
        sb.append(" and el.ISACTIVE = 'Y' ");
        sb.append(" and cr.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CL.VALUE"));
        }

        sb.append(" ORDER BY ");
        sb.append(" CL.VALUE ASC");

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public HouseBankAccount findOne(String compCode, String valueCode, String accountCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" ba.TH_CAHOUSEBANKACCOUNT_ID, ");
        sb.append(" hb.VALUECODE, ");
        sb.append(" ba.VALUECODE as ACCOUNTCODE, ");
        sb.append(" ba.SHORTDESCRIPTION, ");
        sb.append(" ba.ACCOUNTNO as BANKACCOUNTNO, ");
        sb.append(" el.VALUE     as GLACCOUNT, ");
        sb.append(" ct.COUNTRYCODE, ");
        sb.append(" ct.NAME as COUNTRYNAME, ");
        sb.append(" cb.ROUTINGNO AS BANKBRANCH, ");
        sb.append(" cb.DESCRIPTION, ");
        sb.append(" cb.SWIFTCODE, ");
        sb.append(" lc.ADDRESS1, ");
        sb.append(" lc.ADDRESS2, ");
        sb.append(" lc.ADDRESS3, ");
        sb.append(" lc.ADDRESS4, ");
        sb.append(" lc.ADDRESS5, ");
        sb.append(" cr.ISO_CODE as CURRENCY ");
        sb.append(" FROM ");
        sb.append(" TH_CAHOUSEBANKACCOUNT ba, ");
        sb.append(" TH_CAHOUSEBANK hb, ");
        sb.append(" C_BANK cb, ");
        sb.append(" AD_CLIENT cl, ");
        sb.append(" C_COUNTRY ct, ");
        sb.append(" C_LOCATION lc, ");
        sb.append(" C_ELEMENTVALUE el, ");
        sb.append(" C_CURRENCY cr ");
        sb.append(" WHERE ");
        sb.append(" ba.TH_CAHOUSEBANK_ID = hb.TH_CAHOUSEBANK_ID ");
        sb.append(" and hb.C_BANK_ID = cb.C_BANK_ID ");
        sb.append(" and hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sb.append(" and cb.C_LOCATION_ID=lc.C_LOCATION_ID ");
        sb.append(" and ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
        sb.append(" and ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
        sb.append(" and ba.ISACTIVE = 'Y' ");
        sb.append(" and hb.ISACTIVE = 'Y' ");
        sb.append(" and cb.ISACTIVE = 'Y' ");
        sb.append(" and cl.ISACTIVE = 'Y' ");
        sb.append(" and ct.ISACTIVE = 'Y' ");
        sb.append(" and lc.ISACTIVE = 'Y' ");
        sb.append(" and el.ISACTIVE = 'Y' ");
        sb.append(" and cr.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "CL.VALUE", params));
        }
        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "hb.VALUECODE", params));
        }
        if (!Util.isEmpty(accountCode)) {
            sb.append(SqlUtil.whereClause(accountCode, "ba.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), HouseBankAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        HouseBankAccount houseBankAccount = (HouseBankAccount) q.getSingleResult();
        logger.info("houseBankAccount : {}", houseBankAccount);

        return houseBankAccount;
    }
}
