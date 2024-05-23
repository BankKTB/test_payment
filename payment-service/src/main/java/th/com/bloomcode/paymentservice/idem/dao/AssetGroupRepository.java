package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.AssetGroup;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AssetGroupRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<AssetGroup> findByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" AG.A_ASSET_GROUP_ID, ");
        sb.append(" AG.NAME AS VALUECODE, ");
        sb.append(" AG.DESCRIPTION, ");
        sb.append(" AG.HELP ");
        sb.append(" FROM ");
        sb.append(" A_ASSET_GROUP AG ");
        sb.append(" WHERE ");
        sb.append(" AG.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "AG.HELP", "AG.NAME", "AG.DESCRIPTION"));
        }

        sb.append(" ORDER BY ");
        sb.append(" AG.HELP ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), AssetGroup.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<AssetGroup> assetGroup = q.getResultList();
        logger.info("assetGroup : {}", assetGroup);

        return assetGroup;
    }

    public Long countByKey(String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" A_ASSET_GROUP AG ");
        sb.append(" WHERE ");
        sb.append(" AG.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "AG.HELP", "AG.NAME", "AG.DESCRIPTION"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public AssetGroup findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" AG.A_ASSET_GROUP_ID, ");
        sb.append(" AG.NAME AS VALUECODE, ");
        sb.append(" AG.DESCRIPTION, ");
        sb.append(" AG.HELP ");
        sb.append(" FROM ");
        sb.append(" A_ASSET_GROUP AG ");
        sb.append(" WHERE ");
        sb.append(" AG.ISACTIVE = 'Y'  ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "AG.NAME", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), AssetGroup.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        AssetGroup assetGroup = (AssetGroup) q.getSingleResult();
        logger.info("assetGroup : {}", assetGroup);

        return assetGroup;
    }
}
