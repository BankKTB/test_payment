package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.Income;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class IncomeRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<Income> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" IA.TH_CAINCOME_ACCOUNT_ID, IC.VALUECODE, IC.NAME, IC.SHORTDESCRIPTION, IC.FUNDSOURCEPATTERN, EV1.VALUE AS FROMACCOUNT, EV2.VALUE AS TOACCOUNT ");
        sb.append(" FROM ");
        sb.append(" TH_CAINCOME IC,");
        sb.append(" TH_CAINCOME_ACCOUNT IA,");
        sb.append(" C_ELEMENTVALUE EV1,");
        sb.append(" C_ELEMENTVALUE EV2");
        sb.append(" WHERE ");
        sb.append(" IC.TH_CAINCOME_ID = IA.TH_CAINCOME_ID ");
        sb.append(" AND IA.FROMACCOUNT_ID = EV1.C_ELEMENTVALUE_ID ");
        sb.append(" AND IA.TOACCOUNT_ID = EV2.C_ELEMENTVALUE_ID ");
        sb.append(" AND IC.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "IC.VALUECODE", "IC.NAME", "IC.SHORTDESCRIPTION"));
        }

        sb.append(" ORDER BY ");
        sb.append(" IC.VALUECODE ASC ,");
        sb.append(" EV1.VALUE  ASC ,");
        sb.append(" EV2.VALUE  ASC");


        Query q = entityManager.createNativeQuery(sb.toString(), Income.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<Income> incomes = q.getResultList();
        logger.info("incomes : {}", incomes);

        return incomes;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_CAINCOME IC,");
        sb.append(" TH_CAINCOME_ACCOUNT IA,");
        sb.append(" C_ELEMENTVALUE EV1,");
        sb.append(" C_ELEMENTVALUE EV2");
        sb.append(" WHERE ");
        sb.append(" IC.TH_CAINCOME_ID = IA.TH_CAINCOME_ID ");
        sb.append(" AND IA.FROMACCOUNT_ID = EV1.C_ELEMENTVALUE_ID ");
        sb.append(" AND IA.TOACCOUNT_ID = EV2.C_ELEMENTVALUE_ID ");
        sb.append(" AND IC.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "IC.VALUECODE", "IC.NAME", "IC.SHORTDESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public Income findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" IA.TH_CAINCOME_ACCOUNT_ID, IC.VALUECODE, IC.NAME, IC.SHORTDESCRIPTION, IC.FUNDSOURCEPATTERN, EV1.VALUE AS FROMACCOUNT, EV2.VALUE AS TOACCOUNT ");
        sb.append(" FROM ");
        sb.append(" TH_CAINCOME IC,");
        sb.append(" TH_CAINCOME_ACCOUNT IA,");
        sb.append(" C_ELEMENTVALUE EV1,");
        sb.append(" C_ELEMENTVALUE EV2");
        sb.append(" WHERE ");
        sb.append(" IC.TH_CAINCOME_ID = IA.TH_CAINCOME_ID ");
        sb.append(" AND IA.FROMACCOUNT_ID = EV1.C_ELEMENTVALUE_ID ");
        sb.append(" AND IA.TOACCOUNT_ID = EV2.C_ELEMENTVALUE_ID ");
        sb.append(" AND IC.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "IC.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), Income.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
            q.setMaxResults(1);
        }
        Income income = (Income) q.getSingleResult();
        logger.info("income : {}", income);

        return income;
    }
}
