package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.BankBook;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BankBookRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<BankBook> findByKey(String compCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BB.TH_CABANKBOOK_ID, ");
        sb.append(" BB.VALUECODE AS VALUECODE, ");
        sb.append(" BB.NAME AS NAME, ");
        sb.append(" BB.DESCRIPTION AS DESCRIPTION, ");
        sb.append(" AD.VALUE AS COMPANYCODE ");
        sb.append(" FROM ");
        sb.append(" TH_CABANKBOOK BB, ");
        sb.append(" AD_ORG AD");
        sb.append(" WHERE ");
        sb.append(" BB.AD_ORG_ID = AD.AD_ORG_ID ");
        sb.append(" AND BB.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "AD.VALUE", params));
        }


        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BB.VALUECODE", "BB.NAME", "BB.DESCRIPTION"));
        }
        sb.append(" ORDER BY ");
        sb.append(" BB.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), BankBook.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<BankBook> bankBooks = q.getResultList();
        logger.info("bankBooks : {}", bankBooks);

        return bankBooks;
    }

    public Long countByKey(String compCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();


        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_CABANKBOOK BB, ");
        sb.append(" AD_ORG AD");
        sb.append(" WHERE ");
        sb.append(" BB.AD_ORG_ID = AD.AD_ORG_ID ");
        sb.append(" AND BB.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "AD.VALUE", params));
        }


        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BB.VALUECODE", "BB.NAME", "BB.DESCRIPTION"));
        }
        sb.append(" ORDER BY ");
        sb.append(" BB.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public BankBook findOne(String compCode, String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BB.TH_CABANKBOOK_ID, ");
        sb.append(" BB.VALUECODE AS VALUECODE, ");
        sb.append(" BB.NAME AS NAME, ");
        sb.append(" BB.DESCRIPTION AS DESCRIPTION, ");
        sb.append(" AD.VALUE AS COMPANYCODE ");
        sb.append(" FROM ");
        sb.append(" TH_CABANKBOOK BB, ");
        sb.append(" AD_ORG AD");
        sb.append(" WHERE ");
        sb.append(" BB.AD_ORG_ID = AD.AD_ORG_ID ");
        sb.append(" AND BB.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "AD.VALUE", params));
        }

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "BB.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), BankBook.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        BankBook bankBook = (BankBook) q.getSingleResult();
        logger.info("bankBook : {}", bankBook);

        return bankBook;
    }
}
