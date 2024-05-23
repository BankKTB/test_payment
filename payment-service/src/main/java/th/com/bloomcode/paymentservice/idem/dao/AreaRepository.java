package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.Area;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AreaRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<Area> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BA.TH_BGBUDGETAREA_ID, ");
        sb.append(" BA.VALUECODE, ");
        sb.append(" BA.NAME, ");
        sb.append(" BA.DESCRIPTION, ");
        sb.append(" BA.FIAREA ");
        sb.append(" FROM ");
        sb.append(" TH_BGBUDGETAREA BA ");
        sb.append(" WHERE ");
        sb.append(" BA.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BA.VALUECODE", "BA.NAME", "BA.DESCRIPTION"));
        }

        sb.append(" ORDER BY ");
        sb.append(" BA.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), Area.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<Area> area = q.getResultList();
        logger.info("area : {}", area);

        return area;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_BGBUDGETAREA BA ");
        sb.append(" WHERE ");
        sb.append(" BA.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BA.VALUECODE", "BA.NAME", "BA.DESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public Area findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BA.TH_BGBUDGETAREA_ID, ");
        sb.append(" BA.VALUECODE, ");
        sb.append(" BA.NAME, ");
        sb.append(" BA.DESCRIPTION, ");
        sb.append(" BA.FIAREA ");
        sb.append(" FROM ");
        sb.append(" TH_BGBUDGETAREA BA ");
        sb.append(" WHERE ");
        sb.append(" BA.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "BA.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), Area.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Area area = (Area) q.getSingleResult();
        logger.info("area : {}", area);

        return area;
    }
}
