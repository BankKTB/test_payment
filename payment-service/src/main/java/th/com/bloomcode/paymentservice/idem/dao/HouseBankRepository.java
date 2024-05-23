package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.HouseBank;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HouseBankRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<HouseBank> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" hb.TH_CAHOUSEBANK_ID, ");
        sb.append(" hb.VALUECODE, ");
        sb.append(" ct.COUNTRYCODE, ");
        sb.append(" ct.NAME as COUNTRYNAME, ");
        sb.append(" cb.ROUTINGNO AS BANKBRANCH, ");
        sb.append(" cb.DESCRIPTION, ");
        sb.append(" cb.SWIFTCODE, ");
        sb.append(" lc.ADDRESS1, ");
        sb.append(" lc.ADDRESS2, ");
        sb.append(" lc.ADDRESS3, ");
        sb.append(" lc.ADDRESS4, ");
        sb.append(" lc.ADDRESS5 ");
        sb.append(" FROM ");
        sb.append(" TH_CAHOUSEBANK hb, ");
        sb.append(" C_BANK cb, ");
        sb.append(" AD_CLIENT cl, ");
        sb.append(" C_COUNTRY ct, ");
        sb.append(" C_LOCATION lc ");
        sb.append(" WHERE ");
        sb.append(" hb.C_BANK_ID = cb.C_BANK_ID ");
        sb.append("  and hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sb.append("  and cb.C_LOCATION_ID=lc.C_LOCATION_ID ");
        sb.append(" and hb.ISACTIVE = 'Y' ");
        sb.append(" and cb.ISACTIVE = 'Y' ");
        sb.append(" and cl.ISACTIVE = 'Y' ");
        sb.append(" and ct.ISACTIVE = 'Y' ");
        sb.append(" and lc.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CL.VALUE"));
        }

        sb.append(" ORDER BY ");
        sb.append(" CL.VALUE ASC");

        logger.info("sql : {}", sb.toString());

        Query q = entityManager.createNativeQuery(sb.toString(), HouseBank.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<HouseBank> houseBank = q.getResultList();
        logger.info("houseBank : {}", houseBank);

        return houseBank;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_CAHOUSEBANK hb, ");
        sb.append(" C_BANK cb, ");
        sb.append(" AD_CLIENT cl, ");
        sb.append(" C_COUNTRY ct, ");
        sb.append(" C_LOCATION lc ");
        sb.append(" WHERE ");
        sb.append(" hb.C_BANK_ID = cb.C_BANK_ID ");
        sb.append("  and hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sb.append("  and cb.C_LOCATION_ID=lc.C_LOCATION_ID ");
        sb.append(" and hb.ISACTIVE = 'Y' ");
        sb.append(" and cb.ISACTIVE = 'Y' ");
        sb.append(" and cl.ISACTIVE = 'Y' ");
        sb.append(" and ct.ISACTIVE = 'Y' ");
        sb.append(" and lc.ISACTIVE = 'Y' ");

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

    public HouseBank findOne(String compCode, String valueCode, String bankBranch) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" hb.TH_CAHOUSEBANK_ID, ");
        sb.append(" hb.VALUECODE, ");
        sb.append(" ct.COUNTRYCODE, ");
        sb.append(" ct.NAME as COUNTRYNAME, ");
        sb.append(" cb.ROUTINGNO AS BANKBRANCH, ");
        sb.append(" cb.DESCRIPTION, ");
        sb.append(" cb.SWIFTCODE, ");
        sb.append(" lc.ADDRESS1, ");
        sb.append(" lc.ADDRESS2, ");
        sb.append(" lc.ADDRESS3, ");
        sb.append(" lc.ADDRESS4, ");
        sb.append(" lc.ADDRESS5 ");
        sb.append(" FROM ");
        sb.append(" TH_CAHOUSEBANK hb, ");
        sb.append(" C_BANK cb, ");
        sb.append(" AD_CLIENT cl, ");
        sb.append(" C_COUNTRY ct, ");
        sb.append(" C_LOCATION lc ");
        sb.append(" WHERE ");
        sb.append(" hb.C_BANK_ID = cb.C_BANK_ID ");
        sb.append("  and hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
        sb.append("  and cb.C_LOCATION_ID=lc.C_LOCATION_ID ");
        sb.append(" and hb.ISACTIVE = 'Y' ");
        sb.append(" and cb.ISACTIVE = 'Y' ");
        sb.append(" and cl.ISACTIVE = 'Y' ");
        sb.append(" and ct.ISACTIVE = 'Y' ");
        sb.append(" and lc.ISACTIVE = 'Y' ");


        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClauseOr(compCode, params, "CL.VALUE"));
        }
        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClauseOr(valueCode, params, "hb.VALUECODE"));
        }
        if (!Util.isEmpty(bankBranch)) {
            sb.append(SqlUtil.whereClauseOr(bankBranch, params, "cb.ROUTINGNO"));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), HouseBank.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        HouseBank houseBank = (HouseBank) q.getSingleResult();
        logger.info("houseBank : {}", houseBank);

        return houseBank;
    }
}
