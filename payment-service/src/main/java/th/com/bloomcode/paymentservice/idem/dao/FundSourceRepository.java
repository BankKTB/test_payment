package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.FundSource;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FundSourceRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<FundSource> findByKey(String year, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" FS.* ");
        sb.append(" FROM ");
        sb.append(" TH_BGFUNDSOURCE FS, C_YEAR CY");
        sb.append(" WHERE ");
        sb.append(" FS.C_YEAR_ID = CY.C_YEAR_ID ");
        sb.append(" AND ");
        sb.append(" FS.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "FS.VALUECODE", "FS.NAME", "FS.DESCRIPTION"));
        }
        sb.append(" ORDER BY ");
        sb.append(" FS.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), FundSource.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<FundSource> fundSources = q.getResultList();
        logger.info("fundSources : {}", fundSources);

        return fundSources;
    }

    public Long countByKey(String year, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" TH_BGFUNDSOURCE FS, C_YEAR CY");
        sb.append(" WHERE ");
        sb.append(" FS.C_YEAR_ID = CY.C_YEAR_ID ");
        sb.append(" AND ");
        sb.append(" FS.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "FS.VALUECODE", "FS.NAME", "FS.DESCRIPTION"));
        }
        sb.append(" ORDER BY ");
        sb.append(" FS.VALUECODE ASC");

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public FundSource findOne(String year, String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" FS.* ");
        sb.append(" FROM ");
        sb.append(" TH_BGFUNDSOURCE FS, C_YEAR CY");
        sb.append(" WHERE ");
        sb.append(" FS.C_YEAR_ID = CY.C_YEAR_ID ");
        sb.append(" AND ");
        sb.append(" FS.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(year)) {
            sb.append(SqlUtil.whereClause(year, "CY.FISCALYEAR", params));
        }

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "FS.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), FundSource.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        FundSource fundSource = (FundSource) q.getSingleResult();
        logger.info("fundSource : {}", fundSource);

        return fundSource;
    }

    public FundSource findOneForFundType(String year, String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" FS.*, ");
        sb.append("  FY.VALUECODE AS FUND_TYPE ");
        sb.append(" FROM ");
        sb.append(" TH_BGFUNDSOURCE FS, C_YEAR CY,  TH_BGFUNDTYPE FY");
        sb.append(" WHERE ");
        sb.append(" FS.C_YEAR_ID = CY.C_YEAR_ID ");
        sb.append(" AND FS.TH_BGFUNDTYPE_ID = FY.TH_BGFUNDTYPE_ID ");
        sb.append(" AND FS.ISACTIVE = 'Y' ");
        sb.append(" AND FY.ISACTIVE = 'Y' ");


        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "FS.VALUECODE", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), FundSource.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        FundSource fundSource = (FundSource) q.getSingleResult();
        logger.info("fundSource : {}", fundSource);

        return fundSource;
    }
}
