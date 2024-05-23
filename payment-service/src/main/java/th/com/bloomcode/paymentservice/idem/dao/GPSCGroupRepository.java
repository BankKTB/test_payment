package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.GPSCGroup;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GPSCGroupRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<GPSCGroup> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" GG.TH_MM_GPSCGRP_ID, ");
        sb.append(" GG.VALUECODE, ");
        sb.append(" GG.NAME, ");
        sb.append(" GG.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" TH_MM_GPSCGRP GG ");
        sb.append(" WHERE ");
        sb.append(" GG.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "GG.VALUECODE", "GG.NAME", "GG.DESCRIPTION"));
        }

        sb.append(" ORDER BY ");
        sb.append(" GG.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), GPSCGroup.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<GPSCGroup> gpscGroup = q.getResultList();
        logger.info("gpscGroup : {}", gpscGroup);

        return gpscGroup;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_MM_GPSCGRP GG ");
        sb.append(" WHERE ");
        sb.append(" GG.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "GG.VALUECODE", "GG.NAME", "GG.DESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public GPSCGroup findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" GG.TH_MM_GPSCGRP_ID, ");
        sb.append(" GG.VALUECODE, ");
        sb.append(" GG.NAME, ");
        sb.append(" GG.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" TH_MM_GPSCGRP GG ");
        sb.append(" WHERE ");
        sb.append(" GG.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "GG.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), GPSCGroup.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        GPSCGroup gpscGroup = (GPSCGroup) q.getSingleResult();
        logger.info("gpscGroup : {}", gpscGroup);

        return gpscGroup;
    }
}
