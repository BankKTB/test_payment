package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.ProcureMethod;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProcureMethodRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<ProcureMethod> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" PM.TH_MM_PROCUREMETHOD_ID, ");
        sb.append(" PM.VALUECODE, ");
        sb.append(" PM.NAME, ");
        sb.append(" PM.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" TH_MM_PROCUREMETHOD PM ");
        sb.append(" WHERE ");
        sb.append(" PM.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "PM.VALUECODE", "PM.NAME", "PM.DESCRIPTION"));
        }

        sb.append(" ORDER BY ");
        sb.append(" PM.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), ProcureMethod.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<ProcureMethod> procureMethod = q.getResultList();
        logger.info("procureMethod : {}", procureMethod);

        return procureMethod;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");
        sb.append(" COUNT(1) ");

        sb.append(" FROM ");
        sb.append(" TH_MM_PROCUREMETHOD PM ");
        sb.append(" WHERE ");
        sb.append(" PM.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "PM.VALUECODE", "PM.NAME", "PM.DESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public ProcureMethod findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" PM.TH_MM_PROCUREMETHOD_ID, ");
        sb.append(" PM.VALUECODE, ");
        sb.append(" PM.NAME, ");
        sb.append(" PM.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" TH_MM_GPSCGRP PM ");
        sb.append(" WHERE ");
        sb.append(" PM.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "PM.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), ProcureMethod.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        ProcureMethod procureMethod = (ProcureMethod) q.getSingleResult();
        logger.info("procureMethod : {}", procureMethod);

        return procureMethod;
    }
}
