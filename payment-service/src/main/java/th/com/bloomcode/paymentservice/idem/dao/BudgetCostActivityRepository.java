package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.BudgetCostActivity;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BudgetCostActivityRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<BudgetCostActivity> findByKey(String year, String compCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CA.TH_BGCOSTACTIVITY_ID, ");
        sb.append(" CA.VALUECODE, ");
        sb.append(" CA.NAME, ");
        sb.append(" CA.DESCRIPTION, ");
        sb.append(" OG.VALUE AS COMPCODE ");
        sb.append(" FROM ");
        sb.append(" TH_BGCOSTACTIVITY CA, ");
        sb.append(" AD_ORG OG, ");
        sb.append(" C_YEAR CY ");
        sb.append(" WHERE ");
        sb.append(" CA.AD_ORG_ID = OG.AD_ORG_ID ");
        sb.append(" AND CA.C_YEAR_ID = CY.C_YEAR_ID ");
        sb.append(" AND OG.ISSUMMARY = 'N' ");
        sb.append(" AND CA.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "OG.VALUE", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CA.VALUECODE", "CA.NAME", "CA.DESCRIPTION"));
        }
        sb.append(" ORDER BY ");
        sb.append(" CA.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), BudgetCostActivity.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<BudgetCostActivity> budgetCostActivitys = q.getResultList();
        logger.info("budgetCostActivitys : {}", budgetCostActivitys);

        return budgetCostActivitys;
    }

    public Long countByKey(String year, String compCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_BGCOSTACTIVITY CA, ");
        sb.append(" AD_ORG OG, ");
        sb.append(" C_YEAR CY ");
        sb.append(" WHERE ");
        sb.append(" CA.AD_ORG_ID = OG.AD_ORG_ID ");
        sb.append(" AND CA.C_YEAR_ID = CY.C_YEAR_ID ");
        sb.append(" AND OG.ISSUMMARY = 'N' ");
        sb.append(" AND CA.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "OG.VALUE", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CA.VALUECODE", "CA.NAME", "CA.DESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public BudgetCostActivity findOne(String year, String compCode, String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CA.TH_BGCOSTACTIVITY_ID, ");
        sb.append(" CA.VALUECODE, ");
        sb.append(" CA.NAME, ");
        sb.append(" CA.DESCRIPTION, ");
        sb.append(" OG.VALUE AS COMPCODE ");
        sb.append(" FROM ");
        sb.append(" TH_BGCOSTACTIVITY CA, ");
        sb.append(" AD_ORG OG, ");
        sb.append(" C_YEAR CY ");
        sb.append(" WHERE ");
        sb.append(" CA.AD_ORG_ID = OG.AD_ORG_ID ");
        sb.append(" AND CA.C_YEAR_ID = CY.C_YEAR_ID ");
        sb.append(" AND OG.ISSUMMARY = 'N' ");
        sb.append(" AND CA.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "OG.VALUE", params));
        }

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "CA.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), BudgetCostActivity.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        BudgetCostActivity budgetCostActivity = (BudgetCostActivity) q.getSingleResult();
        logger.info("budgetCostActivity : {}", budgetCostActivity);

        return budgetCostActivity;
    }
}
