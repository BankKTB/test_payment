package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.PaymentCenter;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PaymentCenterRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<PaymentCenter> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" PC.*, BA.VALUECODE AS AREA, CC.VALUECODE AS COMPANYCODE ");
        sb.append(" FROM ");
        sb.append(" TH_BGPAYMENTCENTER PC, TH_BGBUDGETAREA BA, TH_CACOMPCODE CC");
        sb.append(" WHERE ");
        sb.append(" PC.TH_BGBUDGETAREA_ID = BA.TH_BGBUDGETAREA_ID ");
        sb.append(" AND ");
        sb.append(" PC.TH_CACOMPCODE_ID = CC.TH_CACOMPCODE_ID ");
        sb.append(" AND ");
        sb.append(" PC.ISACTIVE = 'Y' ");
        sb.append(" AND ");
        sb.append(" BA.ISACTIVE = 'Y' ");
        sb.append(" AND ");
        sb.append(" CC.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "PC.VALUECODE", "PC.NAME", "PC.DESCRIPTION"));
        }
        sb.append(" ORDER BY ");
        sb.append(" PC.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), PaymentCenter.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<PaymentCenter> paymentCenters = q.getResultList();
        logger.info("paymentCenters : {}", paymentCenters);

        return paymentCenters;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_CACOMPCODE CC, TH_CACOMPCODEMAPPING CMP, TH_CAMINISTRY CM");
        sb.append(" WHERE ");
        sb.append(" CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID ");
        sb.append(" AND ");
        sb.append(" CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID");
        sb.append(" AND ");
        sb.append(" CC.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID");
        sb.append(" AND ");
        sb.append(" CM.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID");
        sb.append(" AND ");
        sb.append(" CC.ISACTIVE = 'Y' ");
        sb.append(" AND ");
        sb.append(" CMP.ISACTIVE = 'Y' ");
        sb.append(" AND ");
        sb.append(" CM.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CC.VALUECODE", "CC.NAME", "CMP.OLDCOMPCODE", "CM.NAME"));
        }
        sb.append(" ORDER BY ");
        sb.append(" CC.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public PaymentCenter findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CC.*, CMP.OLDCOMPCODE, CM.NAME AS MINISTRY ");
        sb.append(" FROM ");
        sb.append(" TH_CACOMPCODE CC, TH_CACOMPCODEMAPPING CMP, TH_CAMINISTRY CM");
        sb.append(" WHERE ");
        sb.append(" CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID ");
        sb.append(" AND ");
        sb.append(" CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID");
        sb.append(" AND ");
        sb.append(" CC.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID");
        sb.append(" AND ");
        sb.append(" CM.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID");
        sb.append(" AND ");
        sb.append(" CC.ISACTIVE = 'Y' ");
        sb.append(" AND ");
        sb.append(" CMP.ISACTIVE = 'Y' ");
        sb.append(" AND ");
        sb.append(" CM.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "CC.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), PaymentCenter.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        PaymentCenter paymentCenter = (PaymentCenter) q.getSingleResult();
        logger.info("paymentCenter : {}", paymentCenter);

        return paymentCenter;
    }
}
