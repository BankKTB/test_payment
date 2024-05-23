package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.BudgetActivity;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BudgetActivityRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<BudgetActivity> findByKey(String year, String compCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BA.TH_BGBUDGETACTIVITY_ID, ");
        sb.append(" BA.VALUECODE, ");
        sb.append("  BA.NAME ");
        sb.append(" FROM ");
        sb.append(" TH_BGBUDGETACTIVITY   BA, ");
        sb.append(" AD_ORG                OG, ");
        sb.append(" C_YEAR CY ");
        sb.append(" WHERE ");
        sb.append(" BA.AD_ORG_ID = OG.AD_ORG_ID  ");
        sb.append(" AND BA.C_YEAR_ID = CY.C_YEAR_ID ");
        sb.append(" AND OG.ISSUMMARY = 'N' ");
        sb.append(" AND BA.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(compCode)) {
            sb.append(" AND ( OG.VALUE = '0' ");
            sb.append(SqlUtil.whereClauseOr(compCode, "OG.VALUE", params));
            sb.append(" )");
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BA.VALUECODE", "BA.NAME"));
        }

        sb.append(" ORDER BY ");
        sb.append(" BA.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), BudgetActivity.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<BudgetActivity> budgetActivity = q.getResultList();
        logger.info("budgetActivity : {}", budgetActivity);

        return budgetActivity;
    }

    public Long countByKey(String year, String compCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_BGBUDGETACTIVITY   BA, ");
        sb.append(" AD_ORG                OG, ");
        sb.append(" C_YEAR CY ");
        sb.append(" WHERE ");
        sb.append(" BA.AD_ORG_ID = OG.AD_ORG_ID  ");
        sb.append(" AND BA.C_YEAR_ID = CY.C_YEAR_ID ");
        sb.append(" AND OG.ISSUMMARY = 'N' ");
        sb.append(" AND BA.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(compCode)) {
            sb.append(" AND ( OG.VALUE = '0' ");
            sb.append(SqlUtil.whereClauseOr(compCode, "OG.VALUE", params));
            sb.append(" )");
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BA.VALUECODE", "BA.NAME"));
        }
        sb.append(" ORDER BY ");
        sb.append(" BA.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public BudgetActivity findOne(String year, String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BA.TH_BGBUDGETACTIVITY_ID, ");
        sb.append(" BA.VALUECODE, ");
        sb.append("  BA.NAME ");
        sb.append(" FROM ");
        sb.append(" TH_BGBUDGETACTIVITY   BA, ");
        sb.append(" AD_ORG                OG, ");
        sb.append(" C_YEAR CY ");
        sb.append(" WHERE ");
        sb.append(" BA.AD_ORG_ID = OG.AD_ORG_ID  ");
        sb.append(" AND BA.C_YEAR_ID = CY.C_YEAR_ID ");
        sb.append(" AND OG.ISSUMMARY = 'N' ");
        sb.append(" AND BA.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "BA.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), BudgetActivity.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        BudgetActivity budgetActivity = (BudgetActivity) q.getSingleResult();
        logger.info("budgetActivity : {}", budgetActivity);

        return budgetActivity;
    }
}
