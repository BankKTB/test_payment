package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.CostCenter;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CostCenterRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<CostCenter> findByKey(String compCode, String paymentCenter, String areaCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CC.TH_BGCOSTCENTER_ID, ");
        sb.append(" CC.VALUECODE, ");
        sb.append(" CC.NAME, ");
        sb.append(" CC.DESCRIPTION, ");
        sb.append(" CD.VALUECODE AS COMPANYCODE, ");
        sb.append(" PC.VALUECODE AS PAYMENTCENTER, ");
        sb.append(" BA.FIAREA AS AREA ");
        sb.append(" FROM ");
        sb.append(" TH_BGCOSTCENTER       CC, ");
        sb.append(" TH_BGPAYMENTCENTER    PC, ");
        sb.append(" TH_BGBUDGETAREA       BA, ");
        sb.append(" TH_CACOMPCODE         CD ");

        sb.append(" WHERE ");
        sb.append(" CC.TH_BGPAYMENTCENTER_ID = PC.TH_BGPAYMENTCENTER_ID  ");
        sb.append(" AND CC.TH_CACOMPCODE_ID = CD.TH_CACOMPCODE_ID ");
        sb.append(" AND PC.TH_BGBUDGETAREA_ID = BA.TH_BGBUDGETAREA_ID ");
        sb.append(" AND CC.ISACTIVE = 'Y' ");
        sb.append(" AND PC.ISACTIVE = 'Y' ");
        sb.append(" AND BA.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE='Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "CD.VALUECODE", params));
        }
        if (!Util.isEmpty(paymentCenter)) {
            sb.append(SqlUtil.whereClause(paymentCenter, "PC.VALUECODE", params));
        }
        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClause(areaCode, "BA.FIAREA", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CC.VALUECODE", "CC.NAME", "CC.DESCRIPTION"
            ));
        }
        sb.append(" ORDER BY ");
        sb.append(" CC.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), CostCenter.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<CostCenter> costCenter = q.getResultList();
        logger.info("costCenter : {}", costCenter);

        return costCenter;
    }

    public Long countByKey(String compCode, String paymentCenter, String areaCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_BGCOSTCENTER       CC, ");
        sb.append(" TH_BGPAYMENTCENTER    PC, ");
        sb.append(" TH_BGBUDGETAREA       BA, ");
        sb.append(" TH_CACOMPCODE         CD ");

        sb.append(" WHERE ");
        sb.append(" CC.TH_BGPAYMENTCENTER_ID = PC.TH_BGPAYMENTCENTER_ID  ");
        sb.append(" AND CC.TH_CACOMPCODE_ID = CD.TH_CACOMPCODE_ID ");
        sb.append(" AND PC.TH_BGBUDGETAREA_ID = BA.TH_BGBUDGETAREA_ID ");
        sb.append(" AND CC.ISACTIVE = 'Y' ");
        sb.append(" AND PC.ISACTIVE = 'Y' ");
        sb.append(" AND BA.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE = 'Y' ");


        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "CD.VALUECODE", params));
        }
        if (!Util.isEmpty(paymentCenter)) {
            sb.append(SqlUtil.whereClause(paymentCenter, "PC.VALUECODE", params));
        }
        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClause(areaCode, "BA.FIAREA", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CC.VALUECODE", "CC.NAME", "CC.DESCRIPTION"));
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

    public CostCenter findOne(String compCode, String paymentCenter, String areaCode, String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CC.TH_BGCOSTCENTER_ID, ");
        sb.append(" CC.VALUECODE, ");
        sb.append(" CC.NAME, ");
        sb.append(" CC.DESCRIPTION, ");
        sb.append(" CD.VALUECODE AS COMPANYCODE, ");
        sb.append(" PC.VALUECODE AS PAYMENTCENTER, ");
        sb.append(" BA.FIAREA AS AREA ");
        sb.append(" FROM ");
        sb.append(" TH_BGCOSTCENTER       CC, ");
        sb.append(" TH_BGPAYMENTCENTER    PC, ");
        sb.append(" TH_BGBUDGETAREA       BA, ");
        sb.append(" TH_CACOMPCODE         CD ");

        sb.append(" WHERE ");
        sb.append(" CC.TH_BGPAYMENTCENTER_ID = PC.TH_BGPAYMENTCENTER_ID  ");
        sb.append(" AND CC.TH_CACOMPCODE_ID = CD.TH_CACOMPCODE_ID ");
        sb.append(" AND PC.TH_BGBUDGETAREA_ID = BA.TH_BGBUDGETAREA_ID ");
        sb.append(" AND CC.ISACTIVE = 'Y' ");
        sb.append(" AND PC.ISACTIVE = 'Y' ");
        sb.append(" AND BA.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClause(compCode, "CD.VALUECODE", params));
        }
        if (!Util.isEmpty(paymentCenter)) {
            sb.append(SqlUtil.whereClause(paymentCenter, "PC.VALUECODE", params));
        }
        if (!Util.isEmpty(areaCode)) {
            sb.append(SqlUtil.whereClause(areaCode, "BA.FIAREA", params));
        }

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "CC.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), CostCenter.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        CostCenter costCenter = (CostCenter) q.getSingleResult();
        logger.info("costCenter : {}", costCenter);

        return costCenter;
    }

    public List<CostCenter> findByKeyNoOwner(String compCode, String paymentCenter, String areaCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CC.TH_BGCOSTCENTER_ID, ");
        sb.append(" CC.VALUECODE, ");
        sb.append(" CC.NAME, ");
        sb.append(" CC.DESCRIPTION, ");
        sb.append(" CD.VALUECODE AS COMPANYCODE, ");
        sb.append(" PC.VALUECODE AS PAYMENTCENTER, ");
        sb.append(" BA.FIAREA AS AREA ");
        sb.append(" FROM ");
        sb.append(" TH_BGCOSTCENTER       CC, ");
        sb.append(" TH_BGPAYMENTCENTER    PC, ");
        sb.append(" TH_BGBUDGETAREA       BA, ");
        sb.append(" TH_CACOMPCODE         CD ");

        sb.append(" WHERE ");
        sb.append(" CC.TH_BGPAYMENTCENTER_ID = PC.TH_BGPAYMENTCENTER_ID  ");
        sb.append(" AND CC.TH_CACOMPCODE_ID = CD.TH_CACOMPCODE_ID ");
        sb.append(" AND PC.TH_BGBUDGETAREA_ID = BA.TH_BGBUDGETAREA_ID ");
        sb.append(" AND CC.ISACTIVE = 'Y' ");
        sb.append(" AND PC.ISACTIVE = 'Y' ");
        sb.append(" AND BA.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE='Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClauseNot(compCode, "CD.VALUECODE", params));
        }
//		if (!Util.isEmpty(paymentCenter)) {
//			sb.append(SqlUtil.whereClauseNot(paymentCenter, "PC.VALUECODE", params));
//		}
//		if (!Util.isEmpty(areaCode)) {
//			sb.append(SqlUtil.whereClauseNot(areaCode, "BA.FIAREA", params));
//		}

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CC.VALUECODE", "CC.NAME", "CC.DESCRIPTION"));
        }
        sb.append(" ORDER BY ");
        sb.append(" CC.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), CostCenter.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<CostCenter> costCenter = q.getResultList();
        logger.info("costCenter : {}", costCenter);

        return costCenter;
    }

    public Long countByKeyNoOwner(String compCode, String paymentCenter, String areaCode, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_BGCOSTCENTER       CC, ");
        sb.append(" TH_BGPAYMENTCENTER    PC, ");
        sb.append(" TH_BGBUDGETAREA       BA, ");
        sb.append(" TH_CACOMPCODE         CD ");

        sb.append(" WHERE ");
        sb.append(" CC.TH_BGPAYMENTCENTER_ID = PC.TH_BGPAYMENTCENTER_ID  ");
        sb.append(" AND CC.TH_CACOMPCODE_ID = CD.TH_CACOMPCODE_ID ");
        sb.append(" AND PC.TH_BGBUDGETAREA_ID = BA.TH_BGBUDGETAREA_ID ");
        sb.append(" AND CC.ISACTIVE = 'Y' ");
        sb.append(" AND PC.ISACTIVE = 'Y' ");
        sb.append(" AND BA.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE='Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClauseNot(compCode, "CD.VALUECODE", params));
        }
//		if (!Util.isEmpty(paymentCenter)) {
//			sb.append(SqlUtil.whereClauseNot(paymentCenter, "PC.VALUECODE", params));
//		}
//		if (!Util.isEmpty(areaCode)) {
//			sb.append(SqlUtil.whereClauseNot(areaCode, "BA.FIAREA", params));
//		}

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "CC.VALUECODE", "CC.NAME", "CC.DESCRIPTION"));
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

    public CostCenter findOneNoOwner(String compCode, String paymentCenter, String areaCode, String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" CC.TH_BGCOSTCENTER_ID, ");
        sb.append(" CC.VALUECODE, ");
        sb.append(" CC.NAME, ");
        sb.append(" CC.DESCRIPTION, ");
        sb.append(" CD.VALUECODE AS COMPANYCODE, ");
        sb.append(" PC.VALUECODE AS PAYMENTCENTER, ");
        sb.append(" BA.FIAREA AS AREA ");
        sb.append(" FROM ");
        sb.append(" TH_BGCOSTCENTER       CC, ");
        sb.append(" TH_BGPAYMENTCENTER    PC, ");
        sb.append(" TH_BGBUDGETAREA       BA, ");
        sb.append(" TH_CACOMPCODE         CD ");

        sb.append(" WHERE ");
        sb.append(" CC.TH_BGPAYMENTCENTER_ID = PC.TH_BGPAYMENTCENTER_ID  ");
        sb.append(" AND CC.TH_CACOMPCODE_ID = CD.TH_CACOMPCODE_ID ");
        sb.append(" AND PC.TH_BGBUDGETAREA_ID = BA.TH_BGBUDGETAREA_ID ");
        sb.append(" AND CC.ISACTIVE = 'Y' ");
        sb.append(" AND PC.ISACTIVE = 'Y' ");
        sb.append(" AND BA.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE = 'Y' ");
        sb.append(" AND CD.ISACTIVE='Y' ");

        if (!Util.isEmpty(compCode)) {
            sb.append(SqlUtil.whereClauseNot(compCode, "CD.VALUECODE", params));
        }
//		if (!Util.isEmpty(paymentCenter)) {
//			sb.append(SqlUtil.whereClauseNot(paymentCenter, "PC.VALUECODE", params));
//		}
//		if (!Util.isEmpty(areaCode)) {
//			sb.append(SqlUtil.whereClauseNot(areaCode, "BA.FIAREA", params));
//		}

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "CC.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), CostCenter.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        CostCenter costCenter = (CostCenter) q.getSingleResult();
        logger.info("costCenter : {}", costCenter);

        return costCenter;
    }
}
