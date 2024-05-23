package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.SubBook;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SubBookRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<SubBook> findByKey(String filter, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" SB.TH_CASUBBOOK_ID, ");
        sb.append(" SB.VALUECODE, ");
        sb.append(" SB.NAME, ");
        sb.append(" SB.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" TH_CASUBBOOK SB ");
        sb.append(" WHERE ");
        sb.append("  SB.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(filter)) {
            sb.append(SqlUtil.whereClause(filter, "SB.VALUECODE", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "SB.VALUECODE"));
        }
        sb.append(" ORDER BY ");
        sb.append(" SB.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), SubBook.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<SubBook> subBooks = q.getResultList();
        logger.info("SubBook : {}", subBooks);

        return subBooks;
    }

    public Long countByKey(String filter, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_CASUBBOOK SB ");
        sb.append(" WHERE ");
        sb.append("  SB.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(filter)) {
            sb.append(SqlUtil.whereClause(filter, "SB.VALUECODE", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "SB.VALUECODE"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public SubBook findOne(String filter, String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" SB.TH_CASUBBOOK_ID, ");
        sb.append(" SB.VALUECODE, ");
        sb.append(" SB.NAME, ");
        sb.append(" SB.DESCRIPTION ");
        sb.append(" FROM ");
        sb.append(" TH_CASUBBOOK SB ");
        sb.append(" WHERE ");
        sb.append("  SB.ISACTIVE = 'Y' ");

//		if (!Util.isEmpty(filter)) {
//			sb.append(SqlUtil.whereClauseOr(filter, "SB.VALUECODE", params));
//		}

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "SB.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), SubBook.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        SubBook subBook = (SubBook) q.getSingleResult();
        logger.info("subBook : {}", subBook);

        return subBook;
    }
}
