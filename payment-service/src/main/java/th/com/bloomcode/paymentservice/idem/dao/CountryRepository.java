package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.Area;
import th.com.bloomcode.paymentservice.idem.entity.Country;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CountryRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<Country> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CT.C_COUNTRY_ID, ");
        sb.append(" CT.COUNTRYCODE, ");
        sb.append(" CT.NAME, ");
        sb.append(" CT.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" C_COUNTRY CT ");
        sb.append(" WHERE ");
        sb.append(" CT.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CT.COUNTRYCODE", "CT.NAME", "CT.DESCRIPTION"));
        }

        sb.append(" ORDER BY ");
        sb.append(" CT.COUNTRYCODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), Country.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<Country> country = q.getResultList();
        logger.info("country : {}", country);

        return country;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");

        sb.append(" FROM ");
        sb.append(" C_COUNTRY CT ");
        sb.append(" WHERE ");
        sb.append(" CT.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CT.COUNTRYCODE", "CT.NAME", "CT.DESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public Country findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CT.C_COUNTRY_ID, ");
        sb.append(" CT.COUNTRYCODE, ");
        sb.append(" CT.NAME, ");
        sb.append(" CT.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" C_COUNTRY CT ");
        sb.append(" WHERE ");
        sb.append(" CT.ISACTIVE = 'Y'  ");


        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "CT.COUNTRYCODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), Area.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Country country = (Country) q.getSingleResult();
        logger.info("country : {}", country);

        return country;
    }
}
