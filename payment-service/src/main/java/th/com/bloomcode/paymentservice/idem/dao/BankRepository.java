package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.Bank;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BankRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<Bank> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CB.TH_CABANK_ID , ");
        sb.append(" CB.VALUECODE AS VALUECODE , ");
        sb.append(" CB.NAME AS NAME ");
        sb.append(" FROM ");
        sb.append(" TH_CABANK CB");
        sb.append(" WHERE ");
        sb.append(" CB.ISACTIVE ='Y'");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CB.VALUECODE", "CB.NAME"));
        }
        sb.append(" ORDER BY ");
        sb.append(" CB.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), Bank.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<Bank> banks = q.getResultList();
        logger.info("banks : {}", banks);

        return banks;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_CABANK CB");
        sb.append(" WHERE ");
        sb.append(" CB.ISACTIVE ='Y'");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CB.VALUECODE", "CB.NAME"));
        }
        sb.append(" ORDER BY ");
        sb.append(" CB.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public Bank findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CB.TH_CABANK_ID , ");
        sb.append(" CB.VALUECODE AS VALUECODE , ");
        sb.append(" CB.NAME AS NAME ");
        sb.append(" FROM ");
        sb.append(" TH_CABANK CB");
        sb.append(" WHERE ");
        sb.append(" CB.ISACTIVE ='Y'");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "CB.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), Bank.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Bank bank = (Bank) q.getSingleResult();
        logger.info("bank : {}", bank);

        return bank;
    }
}
