package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.CompCode;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CompCodeRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<CompCode> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT CC.*,           ");
        sb.append("          CMP.OLDCOMPCODE,           ");
        sb.append("          CM.NAME AS MINISTRY           ");
        sb.append("          FROM TH_CACOMPCODE CC           ");
        sb.append("          LEFT JOIN TH_CACOMPCODEMAPPING CMP           ");
        sb.append("          ON CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID           ");
        sb.append("          AND CC.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID           ");
        sb.append("          AND CMP.ISACTIVE = 'Y'           ");
        sb.append("          LEFT JOIN TH_CAMINISTRY CM           ");
        sb.append("          ON CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID           ");
        sb.append("          AND CM.ISACTIVE = 'Y'           ");
        sb.append("          WHERE CC.ISACTIVE = 'Y'           ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CC.VALUECODE", "CC.NAME", "CMP.OLDCOMPCODE", "CM.NAME"));
        }
        sb.append(" ORDER BY ");
        sb.append(" CC.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), CompCode.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<CompCode> compCodes = q.getResultList();
        logger.info("compCodes : {}", compCodes);

        return compCodes;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append("          FROM TH_CACOMPCODE CC           ");
        sb.append("          LEFT JOIN TH_CACOMPCODEMAPPING CMP           ");
        sb.append("          ON CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID           ");
        sb.append("          AND CC.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID           ");
        sb.append("          AND CMP.ISACTIVE = 'Y'           ");
        sb.append("          LEFT JOIN TH_CAMINISTRY CM           ");
        sb.append("          ON CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID           ");
        sb.append("          AND CM.ISACTIVE = 'Y'           ");
        sb.append("          WHERE CC.ISACTIVE = 'Y'           ");

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

    public CompCode findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT CC.*,           ");
        sb.append("          CMP.OLDCOMPCODE,           ");
        sb.append("          CM.NAME AS MINISTRY           ");
        sb.append("          FROM TH_CACOMPCODE CC           ");
        sb.append("          LEFT JOIN TH_CACOMPCODEMAPPING CMP           ");
        sb.append("          ON CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID           ");
        sb.append("          AND CC.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID           ");
        sb.append("          AND CMP.ISACTIVE = 'Y'           ");
        sb.append("          LEFT JOIN TH_CAMINISTRY CM           ");
        sb.append("          ON CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID           ");
        sb.append("          AND CM.ISACTIVE = 'Y'           ");
        sb.append("          WHERE CC.ISACTIVE = 'Y'           ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "CC.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), CompCode.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        CompCode compCode = (CompCode) q.getSingleResult();
        logger.info("compCode : {}", compCode);

        return compCode;
    }

    public CompCode findOneByOldValueCode(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT CC.*,           ");
        sb.append("          CMP.OLDCOMPCODE,           ");
        sb.append("          CM.NAME AS MINISTRY           ");
        sb.append("          FROM TH_CACOMPCODE CC           ");
        sb.append("          LEFT JOIN TH_CACOMPCODEMAPPING CMP           ");
        sb.append("          ON CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID           ");
        sb.append("          AND CC.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID           ");
        sb.append("          AND CMP.ISACTIVE = 'Y'           ");
        sb.append("          LEFT JOIN TH_CAMINISTRY CM           ");
        sb.append("          ON CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID           ");
        sb.append("          AND CM.ISACTIVE = 'Y'           ");
        sb.append("          WHERE CC.ISACTIVE = 'Y'           ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "CMP.OLDCOMPCODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), CompCode.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        CompCode compCode = null;
        if (q.getResultList().size() > 0) {
            compCode = (CompCode) q.getSingleResult();
        }
        logger.info("compCode : {}", compCode);

        return compCode;
    }


    public List<CompCode> findAllByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT CC.*,           ");
        sb.append("          CMP.OLDCOMPCODE,           ");
        sb.append("          CM.NAME AS MINISTRY           ");
        sb.append("          FROM TH_CACOMPCODE CC           ");
        sb.append("          LEFT JOIN TH_CACOMPCODEMAPPING CMP           ");
        sb.append("          ON CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID           ");
        sb.append("          AND CC.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID           ");
        sb.append("          AND CMP.ISACTIVE = 'Y'           ");
        sb.append("          LEFT JOIN TH_CAMINISTRY CM           ");
        sb.append("          ON CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID           ");
        sb.append("          AND CM.ISACTIVE = 'Y'           ");
        sb.append("          WHERE CC.ISACTIVE = 'Y'           ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CC.VALUECODE", "CC.NAME", "CMP.OLDCOMPCODE", "CM.NAME"));
        }
        sb.append(" ORDER BY ");
        sb.append(" CC.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), CompCode.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<CompCode> compCodes = q.getResultList();
        logger.info("compCodes : {}", compCodes);

        return compCodes;
    }

    public Long countAllByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT CC.*,           ");
        sb.append("          CMP.OLDCOMPCODE,           ");
        sb.append("          CM.NAME AS MINISTRY           ");
        sb.append("          FROM TH_CACOMPCODE CC           ");
        sb.append("          LEFT JOIN TH_CACOMPCODEMAPPING CMP           ");
        sb.append("          ON CC.TH_CACOMPCODE_ID = CMP.TH_CACOMPCODE_ID           ");
        sb.append("          AND CC.TH_CAMINISTRY_ID = CMP.TH_CAMINISTRY_ID           ");
        sb.append("          AND CMP.ISACTIVE = 'Y'           ");
        sb.append("          LEFT JOIN TH_CAMINISTRY CM           ");
        sb.append("          ON CC.TH_CAMINISTRY_ID = CM.TH_CAMINISTRY_ID           ");
        sb.append("          AND CM.ISACTIVE = 'Y'           ");
        sb.append("          WHERE CC.ISACTIVE = 'Y'           ");

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
}
