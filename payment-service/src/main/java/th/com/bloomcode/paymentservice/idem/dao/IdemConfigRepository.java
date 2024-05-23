//package th.com.bloomcode.paymentservice.idem.dao;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//import th.com.bloomcode.paymentservice.idem.entity.IdemConfig;
//import th.com.bloomcode.paymentservice.util.SqlUtil;
//import th.com.bloomcode.paymentservice.util.Util;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public class IdemConfigRepository {
//
//  private final Logger logger = LoggerFactory.getLogger(getClass());
//
//  private final EntityManager entityManager;
//
//  @Autowired
//  public IdemConfigRepository(@Qualifier("idemEntityManagerFactory") EntityManager entityManager) {
//    this.entityManager = entityManager;
//  }
//
//  public List<IdemConfig> findAllByIsActiveTrue() {
//    Map<String, Object> params = new HashMap<>();
//
//    StringBuilder sb = new StringBuilder();
//    sb.append("SELECT ");
//    sb.append(" * ");
//    sb.append(" FROM ");
//    sb.append(" TH_CACLIENT cl");
//    sb.append(" WHERE ");
//    sb.append(" cl.ISACTIVE = 'Y'");
//
//
//    Query q = entityManager.createNativeQuery(sb.toString(), IdemConfig.class);
//    for (Map.Entry<String, Object> entry : params.entrySet()) {
//      q.setParameter(entry.getKey(), entry.getValue());
//    }
//    List<IdemConfig> idemConfig = q.getResultList();
//    logger.info("idemConfig : {}", idemConfig);
//
//    return idemConfig;
//  }
//
//  public IdemConfig findByValueClientStartingWithAndIsActiveTrue(String prefixCompCode) {
//    Map<String, Object> params = new HashMap<>();
//
//    StringBuilder sb = new StringBuilder();
//    sb.append("SELECT ");
//    sb.append(" * ");
//    sb.append(" FROM ");
//    sb.append(" TH_CACLIENT cl");
//    sb.append(" WHERE ");
//    sb.append(" cl.ISACTIVE = 'Y'");
//
//
//    if (!Util.isEmpty(prefixCompCode)) {
//      sb.append(SqlUtil.whereClause(prefixCompCode, "cl.CLIENTVALUE", params));
//    }
//
//
//
//
//    Query q = entityManager.createNativeQuery(sb.toString(), IdemConfig.class);
//    for (Map.Entry<String, Object> entry : params.entrySet()) {
//      q.setParameter(entry.getKey(), entry.getValue());
//    }
//    IdemConfig idemConfig = (IdemConfig) q.getSingleResult();
//    logger.info("idemConfig : {}", idemConfig);
//
//    return idemConfig;
//  }
//
//
//}
