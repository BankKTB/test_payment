//package th.com.bloomcode.paymentservice.idem.dao;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//import th.com.bloomcode.paymentservice.idem.entity.GLAccount;
//import th.com.bloomcode.paymentservice.util.SqlUtil;
//import th.com.bloomcode.paymentservice.util.Util;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public class GLAccountRepository {
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    @Qualifier("idemEntityManagerFactory")
//    private EntityManager entityManager;
//
//    public List<GLAccount> findByKey(String key) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" EV.* ");
//        sb.append(" FROM ");
//        sb.append(" C_ELEMENTVALUE EV");
//        sb.append(" WHERE ");
//        sb.append(" EV.ISSUMMARY = 'N' ");
//        sb.append(" AND ");
//        sb.append(" EV.ISACTIVE = 'Y' ");
//        if (!Util.isEmpty(key)) {
//            sb.append(SqlUtil.whereClauseOr(key, params, "EV.VALUE", "EV.NAME", "EV.DESCRIPTION"));
//        }
//        sb.append(" ORDER BY ");
//        sb.append(" EV.VALUE ASC");
//
//        Query q = entityManager.createNativeQuery(sb.toString(), GLAccount.class);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        List<GLAccount> glAccounts = q.getResultList();
//        logger.info("glAccounts : {}", glAccounts);
//
//        return glAccounts;
//    }
//
//    public Long countByKey(String key) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" COUNT(1) ");
//        sb.append(" FROM ");
//        sb.append(" C_ELEMENTVALUE EV");
//        sb.append(" WHERE ");
//        sb.append(" EV.ISSUMMARY = 'N' ");
//        sb.append(" AND ");
//        sb.append(" EV.ISACTIVE = 'Y' ");
//        if (!Util.isEmpty(key)) {
//            sb.append(SqlUtil.whereClauseOr(key, params, "EV.VALUE", "EV.NAME", "EV.DESCRIPTION"));
//        }
//
//        Query q = entityManager.createNativeQuery(sb.toString());
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        Long count = ((Number) q.getSingleResult()).longValue();
//        logger.info("count : {}", count);
//
//        return count;
//    }
//
//    public GLAccount findOne(String valueCode) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" EV.* ");
//        sb.append(" FROM ");
//        sb.append(" C_ELEMENTVALUE EV");
//        sb.append(" WHERE ");
//        sb.append(" EV.ISSUMMARY = 'N' ");
//        sb.append(" AND ");
//        sb.append(" EV.ISACTIVE = 'Y' ");
//        if (!Util.isEmpty(valueCode)) {
//            sb.append(SqlUtil.whereClause(valueCode, "EV.VALUE", params));
//        }
//
//        Query q = entityManager.createNativeQuery(sb.toString(), GLAccount.class);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        GLAccount glAccount = (GLAccount) q.getSingleResult();
//        logger.info("glAccount : {}", glAccount);
//
//        return glAccount;
//    }
//}
