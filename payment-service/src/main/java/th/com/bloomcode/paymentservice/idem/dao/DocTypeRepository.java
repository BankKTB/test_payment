package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.DocType;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DocTypeRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<DocType> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" distinct NAME, DESCRIPTION, C_DOCTYPE_ID ");
        sb.append(" FROM ");
        sb.append(" C_DOCTYPE ");
        sb.append(" WHERE ");
        sb.append(" length(NAME) = 2 ");
        sb.append(" and (DOCBASETYPE = 'GLJ' OR DOCBASETYPE = 'API' ");
        sb.append(" OR DOCBASETYPE = 'APP') ");
        sb.append(" AND ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "NAME", "DESCRIPTION"));
        }

        sb.append(" ORDER BY ");
        sb.append(" NAME ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), DocType.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<DocType> docType = q.getResultList();
        logger.info("docType : {}", docType);

        return docType;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();


        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" C_DOCTYPE dt ");
        sb.append(" WHERE ");
        sb.append(" dt.ISACTIVE = 'Y'  ");
        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "dt.NAME", "dt.DESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public DocType findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" dt.C_DOCTYPE_ID, ");
        sb.append(" dt.NAME, ");
        sb.append(" dt.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" C_DOCTYPE dt ");
        sb.append(" WHERE ");
        sb.append(" dt.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "dt.NAME", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), DocType.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        DocType docType = (DocType) q.getSingleResult();
        logger.info("docType : {}", docType);

        return docType;
    }
}
