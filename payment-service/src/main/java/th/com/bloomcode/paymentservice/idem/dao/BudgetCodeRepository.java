package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.BudgetCode;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BudgetCodeRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<BudgetCode> findByKey(String year, String compCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BC.TH_BGBUDGETCODE_ID, ");
        sb.append(" BC.VALUECODE, ");
        sb.append(" BC.NAME, ");
        sb.append(" BC.DESCRIPTION, ");
        sb.append(" CY.FISCALYEAR, ");
        sb.append(" BAC.VALUECODE AS BUDGETACCOUNT, ");
        sb.append(" FS.VALUECODE AS FUNDSOURCE, ");
        sb.append(" BC.GFMISBUDGETCODE ");

        sb.append(" FROM ");
        sb.append(" TH_BGBUDGETCODE BC ");
        sb.append(" INNER JOIN AD_ORG OG ON BC.AD_ORG_ID = OG.AD_ORG_ID ");
        sb.append(" INNER JOIN C_YEAR CY ON BC.C_YEAR_ID = CY.C_YEAR_ID AND CY.ISACTIVE = 'Y' ");
        sb.append(" LEFT OUTER JOIN TH_BGBUDGETCODEATTR   BA ON BC.TH_BGBUDGETCODE_ID = BA.TH_BGBUDGETCODE_ID AND BA.ISACTIVE = 'Y' ");
        sb.append(" LEFT OUTER JOIN TH_BGBUDGETACCOUNT    BAC ON BA.TH_BGBUDGETACCOUNT_ID = BAC.TH_BGBUDGETACCOUNT_ID AND BAC.ISACTIVE = 'Y' ");
        sb.append("  LEFT OUTER JOIN TH_BGFUNDSOURCE FS ON BA.TH_BGFUNDSOURCE_ID = FS.TH_BGFUNDSOURCE_ID AND FS.ISACTIVE = 'Y' ");
        sb.append(" WHERE ");
        sb.append(" BC.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "OG.VALUE", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BC.VALUECODE", "BC.NAME", "BC.DESCRIPTION", "BC.GFMISBUDGETCODE"));
        }

        sb.append(" ORDER BY ");
        sb.append(" BC.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), BudgetCode.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<BudgetCode> budgetCode = q.getResultList();
        logger.info("budgetCode : {}", budgetCode);

        return budgetCode;
    }

    public Long countByKey(String year, String compCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_BGBUDGETCODE BC ");
        sb.append(" INNER JOIN AD_ORG OG ON BC.AD_ORG_ID = OG.AD_ORG_ID ");
        sb.append(" INNER JOIN C_YEAR CY ON BC.C_YEAR_ID = CY.C_YEAR_ID AND CY.ISACTIVE = 'Y' ");
        sb.append(" LEFT OUTER JOIN TH_BGBUDGETCODEATTR   BA ON BC.TH_BGBUDGETCODE_ID = BA.TH_BGBUDGETCODE_ID AND BA.ISACTIVE = 'Y' ");
        sb.append(" LEFT OUTER JOIN TH_BGBUDGETACCOUNT    BAC ON BA.TH_BGBUDGETACCOUNT_ID = BAC.TH_BGBUDGETACCOUNT_ID AND BAC.ISACTIVE = 'Y' ");
        sb.append("  LEFT OUTER JOIN TH_BGFUNDSOURCE FS ON BA.TH_BGFUNDSOURCE_ID = FS.TH_BGFUNDSOURCE_ID AND FS.ISACTIVE = 'Y' ");
        sb.append(" WHERE ");
        sb.append(" BC.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "OG.VALUE", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BC.VALUECODE", "BC.NAME", "BC.DESCRIPTION", "BC.GFMISBUDGETCODE"));
        }

        sb.append(" ORDER BY ");
        sb.append(" BC.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public BudgetCode findOne(String year, String compCode, String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BC.TH_BGBUDGETCODE_ID, ");
        sb.append(" BC.VALUECODE, ");
        sb.append(" BC.NAME, ");
        sb.append(" BC.DESCRIPTION, ");
        sb.append(" CY.FISCALYEAR, ");
        sb.append(" BAC.VALUECODE AS BUDGETACCOUNT, ");
        sb.append(" FS.VALUECODE AS FUNDSOURCE, ");
        sb.append(" BC.GFMISBUDGETCODE ");
        sb.append(" FROM ");
        sb.append(" TH_BGBUDGETCODE BC ");
        sb.append(" INNER JOIN AD_ORG OG ON BC.AD_ORG_ID = OG.AD_ORG_ID ");
        sb.append(" INNER JOIN C_YEAR CY ON BC.C_YEAR_ID = CY.C_YEAR_ID AND CY.ISACTIVE = 'Y' ");
        sb.append(" LEFT OUTER JOIN TH_BGBUDGETCODEATTR   BA ON BC.TH_BGBUDGETCODE_ID = BA.TH_BGBUDGETCODE_ID AND BA.ISACTIVE = 'Y' ");
        sb.append(" LEFT OUTER JOIN TH_BGBUDGETACCOUNT    BAC ON BA.TH_BGBUDGETACCOUNT_ID = BAC.TH_BGBUDGETACCOUNT_ID AND BAC.ISACTIVE = 'Y' ");
        sb.append("  LEFT OUTER JOIN TH_BGFUNDSOURCE FS ON BA.TH_BGFUNDSOURCE_ID = FS.TH_BGFUNDSOURCE_ID AND FS.ISACTIVE = 'Y' ");
        sb.append(" WHERE ");
        sb.append(" BC.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "OG.VALUE", params));
        }

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "BC.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), BudgetCode.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        BudgetCode budgetCode = (BudgetCode) q.getSingleResult();
        logger.info("budgetCode : {}", budgetCode);

        return budgetCode;
    }
}
