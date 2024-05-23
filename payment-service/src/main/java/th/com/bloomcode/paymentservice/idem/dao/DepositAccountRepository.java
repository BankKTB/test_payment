package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.DepositAccount;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DepositAccountRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<DepositAccount> findByKey(String compCode, String areaCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" ca.TH_CADEPOSITACCOUNT_ID, ");
        sb.append(" ca.valuecode, ");
        sb.append(" ca.name, ");
        sb.append(" cc.valuecode AS depositaccountowner, ");
        sb.append(" cd.valuecode AS companycode, ");
        sb.append(" ba.fiarea AS area, ");
        sb.append(" ct.valuecode AS caDepositAccountCategory ");
        sb.append(" FROM ");
        sb.append(" th_cadepositaccount   ca, ");
        sb.append(" th_bgcostcenter       cc, ");
        sb.append(" th_bgpaymentcenter    pc, ");
        sb.append(" th_bgbudgetarea       ba, ");
        sb.append(" th_cacompcode         cd, ");
        sb.append(" th_caDepositAccountCategory         ct  ");
        sb.append(" WHERE ");
        sb.append(" ca.depositaccountowner_id = cc.th_bgcostcenter_id ");
        sb.append(" AND cc.th_bgpaymentcenter_id = pc.th_bgpaymentcenter_id ");
        sb.append(" AND cc.th_cacompcode_id = cd.th_cacompcode_id ");
        sb.append(" AND pc.th_bgbudgetarea_id = ba.th_bgbudgetarea_id ");
        sb.append(" AND ca.th_cadepositaccountcategory_id = ct.th_cadepositaccountcategory_id  ");
        sb.append(" AND ca.isactive = 'Y' ");
        sb.append(" AND cc.isactive = 'Y' ");
        sb.append(" AND pc.isactive = 'Y' ");
        sb.append(" AND ba.isactive = 'Y' ");
        sb.append(" AND cd.isactive = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "cd.valuecode", params));
        }

        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClause(areaCode, "ba.fiarea", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "ca.valuecode", "ca.name", "cc.valuecode"));
        }
        sb.append(" ORDER BY ");
        sb.append(" ca.valuecode ASC ,cc.valuecode ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), DepositAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<DepositAccount> depositAccounts = q.getResultList();
        logger.info("depositAccounts : {}", depositAccounts);

        return depositAccounts;
    }

    public Long countByKey(String compCode, String areaCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" th_cadepositaccount   ca, ");
        sb.append(" th_bgcostcenter       cc, ");
        sb.append(" th_bgpaymentcenter    pc, ");
        sb.append(" th_bgbudgetarea       ba, ");
        sb.append(" th_cacompcode         cd, ");
        sb.append(" th_caDepositAccountCategory         ct  ");
        sb.append(" WHERE ");
        sb.append(" ca.depositaccountowner_id = cc.th_bgcostcenter_id ");
        sb.append(" AND cc.th_bgpaymentcenter_id = pc.th_bgpaymentcenter_id ");
        sb.append(" AND cc.th_cacompcode_id = cd.th_cacompcode_id ");
        sb.append(" AND pc.th_bgbudgetarea_id = ba.th_bgbudgetarea_id ");
        sb.append(" AND ca.th_cadepositaccountcategory_id = ct.th_cadepositaccountcategory_id  ");
        sb.append(" AND ca.isactive = 'Y' ");
        sb.append(" AND cc.isactive = 'Y' ");
        sb.append(" AND pc.isactive = 'Y' ");
        sb.append(" AND ba.isactive = 'Y' ");
        sb.append(" AND cd.isactive = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "cd.valuecode", params));
        }

        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClause(areaCode, "ba.fiarea", params));
        }
        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "ca.valuecode", "ca.name", "cc.valuecode"));
        }


        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public DepositAccount findOne(String compCode, String areaCode, String depositAccountKey, String depositAccountOwner) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" ca.TH_CADEPOSITACCOUNT_ID, ");
        sb.append(" ca.valuecode, ");
        sb.append(" ca.name, ");
        sb.append(" cc.valuecode AS depositaccountowner, ");
        sb.append(" cd.valuecode AS companycode, ");
        sb.append(" ba.fiarea AS area , ");
        sb.append(" ct.valuecode AS caDepositAccountCategory ");
        sb.append(" FROM ");
        sb.append(" th_cadepositaccount   ca, ");
        sb.append(" th_bgcostcenter       cc, ");
        sb.append(" th_bgpaymentcenter    pc, ");
        sb.append(" th_bgbudgetarea       ba, ");
        sb.append(" th_cacompcode         cd, ");
        sb.append(" th_caDepositAccountCategory         ct  ");
        sb.append(" WHERE ");
        sb.append(" ca.depositaccountowner_id = cc.th_bgcostcenter_id ");
        sb.append(" AND cc.th_bgpaymentcenter_id = pc.th_bgpaymentcenter_id ");
        sb.append(" AND cc.th_cacompcode_id = cd.th_cacompcode_id ");
        sb.append(" AND pc.th_bgbudgetarea_id = ba.th_bgbudgetarea_id ");
        sb.append(" AND ca.th_cadepositaccountcategory_id = ct.th_cadepositaccountcategory_id  ");
        sb.append(" AND ca.isactive = 'Y' ");
        sb.append(" AND cc.isactive = 'Y' ");
        sb.append(" AND pc.isactive = 'Y' ");
        sb.append(" AND ba.isactive = 'Y' ");
        sb.append(" AND cd.isactive = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "cd.valuecode", params));
        }

        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClause(areaCode, "ba.fiarea", params));
        }

        if (!Util.isEmpty(depositAccountKey)) {
            sb.append(SqlUtil.whereClause(depositAccountKey, "ca.valuecode", params));
        }
        if (!Util.isEmpty(depositAccountOwner)) {
            sb.append(SqlUtil.whereClause(depositAccountOwner, "cc.valuecode", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), DepositAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        DepositAccount depositAccount = (DepositAccount) q.getSingleResult();
        logger.info("depositAccount : {}", depositAccount);

        return depositAccount;
    }

    public List<DepositAccount> findByKeyNoOwner(String compCode, String areaCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" ca.TH_CADEPOSITACCOUNT_ID, ");
        sb.append(" ca.valuecode, ");
        sb.append(" ca.name, ");
        sb.append(" cc.valuecode AS depositaccountowner, ");
        sb.append(" cd.valuecode AS companycode, ");
        sb.append(" ba.fiarea AS area, ");
        sb.append(" ct.valuecode AS caDepositAccountCategory ");
        sb.append(" FROM ");
        sb.append(" th_cadepositaccount   ca, ");
        sb.append(" th_bgcostcenter       cc, ");
        sb.append(" th_bgpaymentcenter    pc, ");
        sb.append(" th_bgbudgetarea       ba, ");
        sb.append(" th_cacompcode         cd, ");
        sb.append(" th_caDepositAccountCategory         ct  ");
        sb.append(" WHERE ");
        sb.append(" ca.depositaccountowner_id = cc.th_bgcostcenter_id ");
        sb.append(" AND cc.th_bgpaymentcenter_id = pc.th_bgpaymentcenter_id ");
        sb.append(" AND cc.th_cacompcode_id = cd.th_cacompcode_id ");
        sb.append(" AND pc.th_bgbudgetarea_id = ba.th_bgbudgetarea_id ");
        sb.append(" AND ca.th_cadepositaccountcategory_id = ct.th_cadepositaccountcategory_id  ");
        sb.append(" AND ca.isactive = 'Y' ");
        sb.append(" AND cc.isactive = 'Y' ");
        sb.append(" AND pc.isactive = 'Y' ");
        sb.append(" AND ba.isactive = 'Y' ");
        sb.append(" AND cd.isactive = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClauseNot(compCode, "cd.valuecode", params));
        }

        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClauseNot(areaCode, "ba.fiarea", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "ca.valuecode", "ca.name", "cc.valuecode"));
        }
        sb.append(" ORDER BY ");
        sb.append(" ca.valuecode ASC ,cc.valuecode ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), DepositAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<DepositAccount> depositAccounts = q.getResultList();
        logger.info("depositAccounts : {}", depositAccounts);

        return depositAccounts;
    }

    public Long countByKeyNoOwner(String compCode, String areaCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" th_cadepositaccount   ca, ");
        sb.append(" th_bgcostcenter       cc, ");
        sb.append(" th_bgpaymentcenter    pc, ");
        sb.append(" th_bgbudgetarea       ba, ");
        sb.append(" th_cacompcode         cd, ");
        sb.append(" th_caDepositAccountCategory         ct  ");
        sb.append(" WHERE ");
        sb.append(" ca.depositaccountowner_id = cc.th_bgcostcenter_id ");
        sb.append(" AND cc.th_bgpaymentcenter_id = pc.th_bgpaymentcenter_id ");
        sb.append(" AND cc.th_cacompcode_id = cd.th_cacompcode_id ");
        sb.append(" AND pc.th_bgbudgetarea_id = ba.th_bgbudgetarea_id ");
        sb.append(" AND ca.th_cadepositaccountcategory_id = ct.th_cadepositaccountcategory_id  ");
        sb.append(" AND ca.isactive = 'Y' ");
        sb.append(" AND cc.isactive = 'Y' ");
        sb.append(" AND pc.isactive = 'Y' ");
        sb.append(" AND ba.isactive = 'Y' ");
        sb.append(" AND cd.isactive = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClauseNot(compCode, "cd.valuecode", params));
        }

        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClauseNot(areaCode, "ba.fiarea", params));
        }
        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "ca.valuecode", "ca.name", "cc.valuecode"));
        }


        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public DepositAccount findOneNoOwner(String compCode, String areaCode, String depositAccountKey, String depositAccountOwner) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" ca.TH_CADEPOSITACCOUNT_ID, ");
        sb.append(" ca.valuecode, ");
        sb.append(" ca.name, ");
        sb.append(" cc.valuecode AS depositaccountowner, ");
        sb.append(" cd.valuecode AS companycode, ");
        sb.append(" ba.fiarea AS area, ");
        sb.append(" ct.valuecode AS caDepositAccountCategory ");
        sb.append(" FROM ");
        sb.append(" th_cadepositaccount   ca, ");
        sb.append(" th_bgcostcenter       cc, ");
        sb.append(" th_bgpaymentcenter    pc, ");
        sb.append(" th_bgbudgetarea       ba, ");
        sb.append(" th_cacompcode         cd, ");
        sb.append(" th_caDepositAccountCategory         ct  ");
        sb.append(" WHERE ");
        sb.append(" ca.depositaccountowner_id = cc.th_bgcostcenter_id ");
        sb.append(" AND cc.th_bgpaymentcenter_id = pc.th_bgpaymentcenter_id ");
        sb.append(" AND cc.th_cacompcode_id = cd.th_cacompcode_id ");
        sb.append(" AND pc.th_bgbudgetarea_id = ba.th_bgbudgetarea_id ");
        sb.append(" AND ca.th_cadepositaccountcategory_id = ct.th_cadepositaccountcategory_id  ");
        sb.append(" AND ca.isactive = 'Y' ");
        sb.append(" AND cc.isactive = 'Y' ");
        sb.append(" AND pc.isactive = 'Y' ");
        sb.append(" AND ba.isactive = 'Y' ");
        sb.append(" AND cd.isactive = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClauseNot(compCode, "cd.valuecode", params));
        }

        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClauseNot(areaCode, "ba.fiarea", params));
        }

        if (!Util.isEmpty(depositAccountKey)) {
            sb.append(SqlUtil.whereClause(depositAccountKey, "ca.valuecode", params));
        }

        if (!Util.isEmpty(depositAccountOwner)) {
            sb.append(SqlUtil.whereClause(depositAccountOwner, "cc.valuecode", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), DepositAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        DepositAccount depositAccount = (DepositAccount) q.getSingleResult();
        logger.info("depositAccount : {}", depositAccount);

        return depositAccount;
    }
}
