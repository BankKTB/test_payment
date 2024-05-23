package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.GLSubType;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GLSubTypeRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<GLSubType> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" ST.TH_GLSUBTYPE_ID, ");
        sb.append(" ST.VALUECODE, ");
        sb.append(" ST.NAME ");
        sb.append(" FROM ");
        sb.append(" TH_GLSUBTYPE ST ");
        sb.append(" WHERE ");
        sb.append(" ST.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "ST.VALUECODE", "ST.NAME"));
        }

        sb.append(" ORDER BY ");
        sb.append(" ST.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), GLSubType.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<GLSubType> glSubType = q.getResultList();
        logger.info("glSubType : {}", glSubType);

        return glSubType;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_GLSUBTYPE ST ");
        sb.append(" WHERE ");
        sb.append(" ST.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "ST.VALUECODE", "ST.NAME"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public GLSubType findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" ST.TH_GLSUBTYPE_ID, ");
        sb.append(" ST.VALUECODE, ");
        sb.append(" ST.NAME ");
        sb.append(" FROM ");
        sb.append(" TH_GLSUBTYPE ST ");
        sb.append(" WHERE ");
        sb.append(" ST.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "ST.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), GLSubType.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        GLSubType glSubType = (GLSubType) q.getSingleResult();
        logger.info("glSubType : {}", glSubType);

        return glSubType;
    }
}
