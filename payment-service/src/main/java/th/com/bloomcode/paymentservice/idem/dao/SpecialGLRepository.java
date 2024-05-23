package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.SpecialGL;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SpecialGLRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<SpecialGL> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" spgl.TH_CASPECIALGL_ID, ");
        sb.append(" spgl.VALUECODE, ");
        sb.append(" spgl.NAME ");
        sb.append(" FROM ");
        sb.append(" TH_CASPECIALGL spgl ");
        sb.append(" WHERE ");
        sb.append(" spgl.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "spgl.VALUECODE", "spgl.NAME"));
        }

        sb.append(" ORDER BY ");
        sb.append(" spgl.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), SpecialGL.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<SpecialGL> specialGL = q.getResultList();
        logger.info("specialGL : {}", specialGL);

        return specialGL;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_CASPECIALGL spgl ");
        sb.append(" WHERE ");
        sb.append(" spgl.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "spgl.VALUECODE", "spgl.NAME"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public SpecialGL findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" spgl.TH_CASPECIALGL_ID, ");
        sb.append(" spgl.VALUECODE, ");
        sb.append(" spgl.NAME ");
        sb.append(" FROM ");
        sb.append(" TH_CASPECIALGL spgl ");
        sb.append(" WHERE ");
        sb.append(" spgl.ISACTIVE = 'Y'  ");


        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "spgl.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), SpecialGL.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        SpecialGL specialGL = (SpecialGL) q.getSingleResult();
        logger.info("specialGL : {}", specialGL);

        return specialGL;
    }
}
