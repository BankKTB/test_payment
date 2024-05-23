package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.AssetGroupAcct;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AssetGroupAcctRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<AssetGroupAcct> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" GP.A_ASSET_GROUP_ACCT_ID, ");
        sb.append(" AG.NAME, ");
        sb.append(" DA.TH_FA_DEPRECIATIONAREA AS DEPRECIATIONAREA, ");
        sb.append(" DA.NAME AS DESCRIPTION, ");
        sb.append(" DP.NAME AS DEPRECIATIONMETHOD, ");
        sb.append(" GP.USELIFEYEARS, ");
        sb.append(" GP.USELIFEMONTHS ");
        sb.append(" FROM ");
        sb.append(" A_ASSET_GROUP_ACCT   GP, ");
        sb.append(" A_ASSET_GROUP        AG, ");
        sb.append(" TH_FA_DEPREAREA      DA, ");
        sb.append(" A_DEPRECIATION       DP ");
        sb.append(" WHERE ");
        sb.append(" GP.A_ASSET_GROUP_ID = AG.A_ASSET_GROUP_ID ");
        sb.append(" AND GP.TH_FA_DEPREAREA_ID = DA.TH_FA_DEPREAREA_ID ");
        sb.append(" AND GP.A_DEPRECIATION_ID = DP.A_DEPRECIATION_ID ");
        sb.append(" AND GP.ISACTIVE = 'Y' ");
        sb.append(" AND AG.ISACTIVE = 'Y' ");
        sb.append(" AND DA.ISACTIVE = 'Y' ");
        sb.append(" AND DP.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "AG.HELP", "DA.TH_FA_DEPRECIATIONAREA", "DA.NAME", "DP.NAME", "GP.USELIFEYEARS", "GP.USELIFEMONTHS"));
        }

        sb.append(" ORDER BY ");
        sb.append(" DA.TH_FA_DEPRECIATIONAREA ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), AssetGroupAcct.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<AssetGroupAcct> assetGroupAcct = q.getResultList();
        logger.info("assetGroupAcct : {}", assetGroupAcct);

        return assetGroupAcct;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" A_ASSET_GROUP_ACCT   GP, ");
        sb.append(" A_ASSET_GROUP        AG, ");
        sb.append(" TH_FA_DEPREAREA      DA, ");
        sb.append(" A_DEPRECIATION       DP ");
        sb.append(" WHERE ");
        sb.append(" GP.A_ASSET_GROUP_ID = AG.A_ASSET_GROUP_ID ");
        sb.append(" AND GP.TH_FA_DEPREAREA_ID = DA.TH_FA_DEPREAREA_ID ");
        sb.append(" AND GP.A_DEPRECIATION_ID = DP.A_DEPRECIATION_ID ");
        sb.append(" AND GP.ISACTIVE = 'Y' ");
        sb.append(" AND AG.ISACTIVE = 'Y' ");
        sb.append(" AND DA.ISACTIVE = 'Y' ");
        sb.append(" AND DP.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "AG.HELP", "DA.TH_FA_DEPRECIATIONAREA", "DA.NAME", "DP.NAME", "GP.USELIFEYEARS", "GP.USELIFEMONTHS"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public AssetGroupAcct findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");


        sb.append(" GP.A_ASSET_GROUP_ACCT_ID, ");
        sb.append(" AG.NAME, ");
        sb.append(" DA.TH_FA_DEPRECIATIONAREA AS DEPRECIATIONAREA, ");
        sb.append(" DA.NAME AS DESCRIPTION, ");
        sb.append(" DP.NAME AS DEPRECIATIONMETHOD, ");
        sb.append(" GP.USELIFEYEARS, ");
        sb.append(" GP.USELIFEMONTHS ");
        sb.append(" FROM ");
        sb.append(" A_ASSET_GROUP_ACCT   GP, ");
        sb.append(" A_ASSET_GROUP        AG, ");
        sb.append(" TH_FA_DEPREAREA      DA, ");
        sb.append(" A_DEPRECIATION       DP ");
        sb.append(" WHERE ");
        sb.append(" GP.A_ASSET_GROUP_ID = AG.A_ASSET_GROUP_ID ");
        sb.append(" AND GP.TH_FA_DEPREAREA_ID = DA.TH_FA_DEPREAREA_ID ");
        sb.append(" AND GP.A_DEPRECIATION_ID = DP.A_DEPRECIATION_ID ");
        sb.append(" AND GP.ISACTIVE = 'Y' ");
        sb.append(" AND AG.ISACTIVE = 'Y' ");
        sb.append(" AND DA.ISACTIVE = 'Y' ");
        sb.append(" AND DP.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "AG.NAME", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), AssetGroupAcct.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        AssetGroupAcct assetGroupAcct = (AssetGroupAcct) q.getSingleResult();
        logger.info("assetGroupAcct : {}", assetGroupAcct);

        return assetGroupAcct;
    }
}
