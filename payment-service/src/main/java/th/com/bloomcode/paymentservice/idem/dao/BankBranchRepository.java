package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.BankBranch;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BankBranchRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<BankBranch> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BB.C_BANK_ID, ");
        sb.append(" BB.ROUTINGNO AS VALUECODE, ");
        sb.append(" BB.NAME AS NAME, ");
        sb.append(" BB.DESCRIPTION AS DESCRIPTION, ");
        sb.append(" CB.VALUECODE AS BANKCODE ");
        sb.append(" FROM ");
        sb.append(" C_BANK BB,");
        sb.append(" TH_CABANKINFO BF, ");
        sb.append(" TH_CABANK CB ");
        sb.append(" WHERE ");
        sb.append(" BB.C_BANK_ID = BF.C_BANK_ID ");
        sb.append(" AND BF.TH_CABANK_ID = CB.TH_CABANK_ID ");
        sb.append(" AND BB.ISACTIVE = 'Y' ");
        sb.append(" AND BF.ISACTIVE = 'Y' ");
        sb.append(" AND CB.ISACTIVE = 'Y' ");


        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BB.ROUTINGNO", "BB.NAME", "BB.DESCRIPTION"));
        }
        sb.append(" ORDER BY ");
        sb.append(" BB.ROUTINGNO ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), BankBranch.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<BankBranch> bankBranchs = q.getResultList();
        logger.info("bankBranchs : {}", bankBranchs);

        return bankBranchs;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" C_BANK BB,");
        sb.append(" TH_CABANKINFO BF, ");
        sb.append(" TH_CABANK CB ");
        sb.append(" WHERE ");
        sb.append(" BB.C_BANK_ID = BF.C_BANK_ID ");
        sb.append(" AND BF.TH_CABANK_ID = CB.TH_CABANK_ID ");
        sb.append(" AND BB.ISACTIVE = 'Y' ");
        sb.append(" AND BF.ISACTIVE = 'Y' ");
        sb.append(" AND CB.ISACTIVE = 'Y' ");


        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BB.ROUTINGNO", "BB.NAME", "BB.DESCRIPTION"));
        }
        sb.append(" ORDER BY ");
        sb.append(" BB.ROUTINGNO ASC");

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public BankBranch findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BB.C_BANK_ID, ");
        sb.append(" BB.ROUTINGNO AS VALUECODE, ");
        sb.append(" BB.NAME AS NAME, ");
        sb.append(" BB.DESCRIPTION AS DESCRIPTION, ");
        sb.append(" CB.VALUECODE AS BANKCODE ");
        sb.append(" FROM ");
        sb.append(" C_BANK BB,");
        sb.append(" TH_CABANKINFO BF, ");
        sb.append(" TH_CABANK CB ");
        sb.append(" WHERE ");
        sb.append(" BB.C_BANK_ID = BF.C_BANK_ID ");
        sb.append(" AND BF.TH_CABANK_ID = CB.TH_CABANK_ID ");
        sb.append(" AND BB.ISACTIVE = 'Y' ");
        sb.append(" AND BF.ISACTIVE = 'Y' ");
        sb.append(" AND CB.ISACTIVE = 'Y' ");


        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "BB.ROUTINGNO", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), BankBranch.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        BankBranch bankBranch = (BankBranch) q.getSingleResult();
        logger.info("bankBranch : {}", bankBranch);

        return bankBranch;
    }
}
