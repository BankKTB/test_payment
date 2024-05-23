package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.GPSCCode;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GPSCCodeRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<GPSCCode> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" GC.TH_MM_GPSCCODE_ID, ");
        sb.append(" GC.VALUECODE, ");
        sb.append(" GC.NAME, ");
        sb.append(" GC.DESCRIPTION, ");
        sb.append(" GG.VALUECODE AS GPSCGROUP ");
        sb.append(" FROM ");
        sb.append(" TH_MM_GPSCCODE GC, ");
        sb.append(" TH_MM_GPSCGRP GG ");
        sb.append(" WHERE ");
        sb.append(" GC.TH_MM_GPSCGRP_ID = GG.TH_MM_GPSCGRP_ID ");
        sb.append(" AND GC.ISACTIVE = 'Y'  ");
        sb.append(" AND GG.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "GC.VALUECODE", "GC.NAME", "GC.DESCRIPTION"));
        }

        sb.append(" ORDER BY ");
        sb.append(" GC.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), GPSCCode.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<GPSCCode> gpscCode = q.getResultList();
        logger.info("gpscCode : {}", gpscCode);

        return gpscCode;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_MM_GPSCCODE GC, ");
        sb.append(" TH_MM_GPSCGRP GG ");
        sb.append(" WHERE ");
        sb.append(" GC.TH_MM_GPSCGRP_ID = GG.TH_MM_GPSCGRP_ID ");
        sb.append(" AND GC.ISACTIVE = 'Y'  ");
        sb.append(" AND GG.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "GC.VALUECODE", "GC.NAME", "GC.DESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public GPSCCode findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" GC.TH_MM_GPSCCODE_ID, ");
        sb.append(" GC.VALUECODE, ");
        sb.append(" GC.NAME, ");
        sb.append(" GC.DESCRIPTION, ");
        sb.append(" GG.VALUECODE AS GPSCGROUP ");
        sb.append(" FROM ");
        sb.append(" TH_MM_GPSCCODE GC, ");
        sb.append(" TH_MM_GPSCGRP GG ");
        sb.append(" WHERE ");
        sb.append(" GC.TH_MM_GPSCGRP_ID = GG.TH_MM_GPSCGRP_ID ");
        sb.append(" AND GC.ISACTIVE = 'Y'  ");
        sb.append(" AND GG.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "GC.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), GPSCCode.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        GPSCCode gpscCode = (GPSCCode) q.getSingleResult();
        logger.info("gpscCode : {}", gpscCode);

        return gpscCode;
    }
}
