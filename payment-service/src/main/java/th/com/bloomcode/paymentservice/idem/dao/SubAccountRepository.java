package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.SubAccount;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SubAccountRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<SubAccount> findByKey(String compCode, String areaCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" SB.TH_CASUBACCOUNT_ID, ");
        sb.append(" SB.VALUECODE AS VALUECODE, ");
        sb.append(" SB.NAME AS NAME, ");
        sb.append(" CT.VALUECODE AS CASUBACCOUNTCATEGORY, ");
        sb.append(" CC.VALUECODE AS SUBACCOUNTOWNER, ");
        sb.append(" CD.VALUECODE AS COMPANYCODE, ");
        sb.append(" BA.FIAREA AS AREA ");
        sb.append(" FROM ");
        sb.append(" TH_CASUBACCOUNT SB, ");
        sb.append(" TH_CASUBACCOUNTCATEGORY CT, ");
        sb.append(" TH_BGCOSTCENTER CC, ");
        sb.append(" TH_BGPAYMENTCENTER    PC, ");
        sb.append(" TH_CACOMPCODE         CD, ");
        sb.append(" TH_BGBUDGETAREA       BA ");
        sb.append(" WHERE ");
        sb.append(" CC.TH_BGPAYMENTCENTER_ID = PC.TH_BGPAYMENTCENTER_ID  ");
        sb.append(" AND CC.TH_CACOMPCODE_ID = CD.TH_CACOMPCODE_ID ");
        sb.append(" AND SB.SUBACCOUNTOWNER_ID = CC.TH_BGCOSTCENTER_ID ");
        sb.append(" AND SB. TH_CASUBACCOUNTCATEGORY_ID = CT.TH_CASUBACCOUNTCATEGORY_ID ");
        sb.append(" AND SB.ISACTIVE = 'Y' ");
        sb.append(" AND CT.ISACTIVE = 'Y' ");
        sb.append(" AND PC.ISACTIVE='Y' ");
        sb.append(" AND CD.ISACTIVE='Y' ");
        sb.append(" AND BA.ISACTIVE='Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "CD.VALUECODE", params));
        }
        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClause(areaCode, "BA.FIAREA", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "SB.VALUECODE", "SB.NAME"));
        }
        sb.append(" ORDER BY ");
        sb.append(" SB.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), SubAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<SubAccount> subAccounts = q.getResultList();
        logger.info("SubAccount : {}", subAccounts);

        return subAccounts;
    }

    public Long countByKey(String compCode, String areaCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_CASUBACCOUNT SB, ");
        sb.append(" TH_CASUBACCOUNTCATEGORY CT, ");
        sb.append(" TH_BGCOSTCENTER CC, ");
        sb.append(" TH_BGPAYMENTCENTER    PC, ");
        sb.append(" TH_CACOMPCODE         CD, ");
        sb.append(" TH_BGBUDGETAREA       BA ");
        sb.append(" WHERE ");
        sb.append(" CC.TH_BGPAYMENTCENTER_ID = PC.TH_BGPAYMENTCENTER_ID  ");
        sb.append(" AND CC.TH_CACOMPCODE_ID = CD.TH_CACOMPCODE_ID ");
        sb.append(" AND SB.SUBACCOUNTOWNER_ID = CC.TH_BGCOSTCENTER_ID ");
        sb.append(" AND SB. TH_CASUBACCOUNTCATEGORY_ID = CT.TH_CASUBACCOUNTCATEGORY_ID ");
        sb.append(" AND SB.ISACTIVE = 'Y' ");
        sb.append(" AND CT.ISACTIVE = 'Y' ");
        sb.append(" AND PC.ISACTIVE='Y' ");
        sb.append(" AND CD.ISACTIVE='Y' ");
        sb.append(" AND BA.ISACTIVE='Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "CD.VALUECODE", params));
        }
        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClause(areaCode, "BA.FIAREA", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "SB.VALUECODE", "SB.NAME"));
        }
        sb.append(" ORDER BY ");
        sb.append(" CC.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public SubAccount findOne(String compCode, String areaCode, String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" SB.TH_CASUBACCOUNT_ID, ");
        sb.append(" SB.VALUECODE AS VALUECODE, ");
        sb.append(" SB.NAME AS NAME, ");
        sb.append(" CT.VALUECODE AS CASUBACCOUNTCATEGORY, ");
        sb.append(" CC.VALUECODE AS SUBACCOUNTOWNER, ");
        sb.append(" CD.VALUECODE AS COMPANYCODE, ");
        sb.append(" BA.FIAREA AS AREA ");
        sb.append(" FROM ");
        sb.append(" TH_CASUBACCOUNT SB, ");
        sb.append(" TH_CASUBACCOUNTCATEGORY CT, ");
        sb.append(" TH_BGCOSTCENTER CC, ");
        sb.append(" TH_BGPAYMENTCENTER    PC, ");
        sb.append(" TH_CACOMPCODE         CD, ");
        sb.append(" TH_BGBUDGETAREA       BA ");
        sb.append(" WHERE ");
        sb.append(" CC.TH_BGPAYMENTCENTER_ID = PC.TH_BGPAYMENTCENTER_ID  ");
        sb.append(" AND CC.TH_CACOMPCODE_ID = CD.TH_CACOMPCODE_ID ");
        sb.append(" AND SB.SUBACCOUNTOWNER_ID = CC.TH_BGCOSTCENTER_ID ");
        sb.append(" AND SB. TH_CASUBACCOUNTCATEGORY_ID = CT.TH_CASUBACCOUNTCATEGORY_ID ");
        sb.append(" AND SB.ISACTIVE = 'Y' ");
        sb.append(" AND CT.ISACTIVE = 'Y' ");
        sb.append(" AND PC.ISACTIVE='Y' ");
        sb.append(" AND CD.ISACTIVE='Y' ");
        sb.append(" AND BA.ISACTIVE='Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "CD.VALUECODE", params));
        }
        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClause(areaCode, "BA.FIAREA", params));
        }

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "SB.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), SubAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        SubAccount subAccount = (SubAccount) q.getSingleResult();
        logger.info("subAccount : {}", subAccount);

        return subAccount;
    }
}
