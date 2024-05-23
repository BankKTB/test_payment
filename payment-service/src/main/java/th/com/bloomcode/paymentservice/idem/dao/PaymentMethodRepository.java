package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.PaymentMethod;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PaymentMethodRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<PaymentMethod> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" pm.TH_CAPAYMENTMETHOD_ID, ");
        sb.append(" pm.VALUECODE, ");
        sb.append(" pm.NAME ");
        sb.append(" FROM ");
        sb.append(" TH_CAPAYMENTMETHOD pm ");
        sb.append(" WHERE ");
        sb.append(" pm.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "pm.VALUECODE", "pm.NAME"));
        }

        sb.append(" ORDER BY ");
        sb.append(" pm.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), PaymentMethod.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<PaymentMethod> paymentMethod = q.getResultList();
        logger.info("paymentMethod : {}", paymentMethod);

        return paymentMethod;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();


        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_CAPAYMENTMETHOD pm ");
        sb.append(" WHERE ");
        sb.append(" pm.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "pm.VALUECODE", "pm.NAME"));
        }


        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public PaymentMethod findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" pm.TH_CAPAYMENTMETHOD_ID, ");
        sb.append(" pm.VALUECODE, ");
        sb.append(" pm.NAME ");
        sb.append(" FROM ");
        sb.append(" TH_CAPAYMENTMETHOD pm ");
        sb.append(" WHERE ");
        sb.append(" pm.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "pm.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), PaymentMethod.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        PaymentMethod paymentMethod = (PaymentMethod) q.getSingleResult();
        logger.info("paymentMethod : {}", paymentMethod);

        return paymentMethod;
    }
}
