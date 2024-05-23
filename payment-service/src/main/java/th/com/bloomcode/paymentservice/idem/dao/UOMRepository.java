package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.UOM;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UOMRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<UOM> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" UM.C_UOM_ID, ");
        sb.append(" UM.NAME AS VALUECODE , ");
        sb.append(" UM.NAME AS NAME, ");
        sb.append(" UM.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" C_UOM UM ");
        sb.append(" WHERE ");
        sb.append(" UM.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "UM.NAME", "UM.DESCRIPTION"));
        }

        sb.append(" ORDER BY ");
        sb.append(" UM.NAME ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), UOM.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<UOM> uom = q.getResultList();
        logger.info("uom : {}", uom);

        return uom;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" C_UOM UM ");
        sb.append(" WHERE ");
        sb.append(" UM.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "UM.NAME", "UM.DESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public UOM findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" UM.C_UOM_ID, ");
        sb.append(" UM.NAME AS VALUECODE , ");
        sb.append(" UM.NAME AS NAME, ");
        sb.append(" UM.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" C_UOM UM ");
        sb.append(" WHERE ");
        sb.append(" UM.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "UM.NAME", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), UOM.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        UOM uom = (UOM) q.getSingleResult();
        logger.info("uom : {}", uom);

        return uom;
    }
}
