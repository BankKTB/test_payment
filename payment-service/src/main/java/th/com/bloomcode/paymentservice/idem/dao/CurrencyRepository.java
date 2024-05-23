package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.Currency;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CurrencyRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<Currency> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" cr.C_CURRENCY_ID, ");
        sb.append(" cr.ISO_CODE AS VALUECODE, ");
        sb.append(" cr.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" C_CURRENCY cr ");
        sb.append(" WHERE ");
        sb.append(" cr.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "cr.ISO_CODE", "cr.DESCRIPTION"));
        }

        sb.append(" ORDER BY ");
        sb.append(" cr.ISO_CODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), Currency.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<Currency> currency = q.getResultList();
        logger.info("currency : {}", currency);

        return currency;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" C_CURRENCY cr ");
        sb.append(" WHERE ");
        sb.append(" cr.ISACTIVE = 'Y'  ");


        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "cr.ISO_CODE", "cr.DESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public Currency findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        sb.append(" cr.C_CURRENCY_ID, ");
        sb.append(" cr.ISO_CODE AS VALUECODE, ");
        sb.append(" cr.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" C_CURRENCY cr ");
        sb.append(" WHERE ");
        sb.append(" cr.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "cr.ISO_CODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), Currency.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Currency currency = (Currency) q.getSingleResult();
        logger.info("currency : {}", currency);

        return currency;
    }
}
