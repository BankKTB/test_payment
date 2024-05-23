//package th.com.bloomcode.paymentservice.idem.dao;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//import th.com.bloomcode.paymentservice.idem.entity.Vendor;
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
//public class VendorRepository {
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    @Qualifier("idemEntityManagerFactory")
//    private EntityManager entityManager;
//
//    public List<Vendor> findAll() {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT PN.C_BPARTNER_ID, ");
//        sb.append(" PN.TAXID, ");
//        sb.append(" PN.VALUE, ");
//        sb.append(" PN.NAME, ");
//        sb.append(" PN.ISACTIVE, ");
//        sb.append(" BG.VALUE AS VENDORGROUP, ");
//        sb.append(" AC.CONFIRMSTATUS, ");
//        sb.append(" PS.CONFIRMSTATUS AS VENDORSTATUS, ");
//        sb.append(" OG.VALUE AS COMPCODE ");
//        sb.append(" FROM C_BPARTNER PN, ");
//        sb.append(" C_BP_GROUP BG, ");
//        sb.append(" AD_ORG OG, ");
//        sb.append(" TH_APBPACCESSCONTROL AC, ");
//        sb.append(" TH_APBPartnerStatus PS ");
//        sb.append(" WHERE PN.C_BP_GROUP_ID = BG.C_BP_GROUP_ID ");
//        sb.append(" AND PN.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
//        sb.append(" AND OG.AD_ORG_ID = AC.AD_ORG_ID ");
//        sb.append(" AND PN.C_BPARTNER_ID = PS.C_BPARTNER_ID ");
//        sb.append(" ORDER BY PN.VALUE ASC ");
//
//        Query q = entityManager.createNativeQuery(sb.toString(), Vendor.class);
//        List<Vendor> vendors = q.getResultList();
//        logger.info("vendors : {}", vendors);
//
//        return vendors;
//    }
//
//    public List<Vendor> findByKey(String key) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" PN.C_BPARTNER_ID, ");
//        sb.append(" PN.TAXID, ");
//        sb.append(" PN.VALUE, ");
//        sb.append(" PN.NAME, ");
//        sb.append(" PN.ISACTIVE, ");
//        sb.append(" BG.VALUE AS VENDORGROUP, ");
//        sb.append(" '' AS CONFIRMSTATUS, ");
//        sb.append(" '' AS VENDORSTATUS, ");
//        sb.append(" '0' AS COMPCODE ");
//        sb.append(" FROM ");
//        sb.append(" C_BPARTNER PN, ");
//        sb.append(" C_BP_GROUP BG ");
//        sb.append(" WHERE ");
//        sb.append(" PN.C_BP_GROUP_ID = BG.C_BP_GROUP_ID ");
//        sb.append(" AND BG.ISACTIVE = 'Y' ");
//        sb.append(" AND PN.ISACTIVE = 'Y' ");
//        sb.append(" AND PN.ISVENDOR = 'Y' ");
//
//        if (!Util.isEmpty(key)) {
//            sb.append(SqlUtil.whereClauseOr(key, params, "PN.VALUE", "PN.NAME", "PN.TAXID"));
//        }
//        sb.append(" ORDER BY ");
//        sb.append(" PN.VALUE ASC");
//
//        Query q = entityManager.createNativeQuery(sb.toString(), Vendor.class);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        List<Vendor> vendors = q.getResultList();
//        logger.info("vendors : {}", vendors);
//
//        return vendors;
//    }
//
//    public Long countByKey(String key) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" COUNT(1) ");
//        sb.append(" FROM ");
//        sb.append(" C_BPARTNER PN, ");
//        sb.append(" C_BP_GROUP BG, ");
//        sb.append(" TH_APBPACCESSCONTROL AC ");
//        sb.append(" WHERE ");
//        sb.append(" PN.C_BP_GROUP_ID = BG.C_BP_GROUP_ID ");
//        sb.append(" AND BG.ISACTIVE = 'Y' ");
//        sb.append(" AND PN.ISACTIVE = 'Y' ");
//        sb.append(" AND PN.ISVENDOR = 'Y' ");
//        sb.append(" AND PN.C_BPARTNER_ID = AC.C_BPARTNER_ID");
//
//        if (!Util.isEmpty(key)) {
//            sb.append(SqlUtil.whereClauseOr(key, params, "PN.VALUE", "PN.NAME", "PN.TAXID"));
//        }
//
//        Query q = entityManager.createNativeQuery(sb.toString());
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        Long count = ((Number) q.getSingleResult()).longValue();
//        logger.info("count : {}", count);
//
//        return count;
//    }
//
//    public Vendor findOne(String valueCode) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
////        sb.append("SELECT ");
////        sb.append(" PN.C_BPARTNER_ID, ");
////        sb.append(" PN.TAXID, ");
////        sb.append(" PN.VALUE, ");
////        sb.append(" PN.NAME, ");
////        sb.append(" PN.ISACTIVE, ");
////        sb.append(" BG.VALUE AS VENDORGROUP, ");
////        sb.append(" AC.CONFIRMSTATUS, ");
////        sb.append(" '' AS VENDORSTATUS, ");
////        sb.append(" '0' AS COMPCODE ");
////        sb.append(" FROM ");
////        sb.append(" C_BPARTNER PN, ");
////        sb.append(" C_BP_GROUP BG, ");
////        sb.append(" TH_APBPACCESSCONTROL AC ");
////        sb.append(" WHERE ");
////        sb.append(" PN.C_BP_GROUP_ID = BG.C_BP_GROUP_ID ");
////        sb.append(" AND BG.ISACTIVE = 'Y' ");
//////        sb.append(" AND PN.ISACTIVE = 'Y' ");
////        sb.append(" AND PN.ISVENDOR = 'Y' ");
////        sb.append(" AND PN.C_BPARTNER_ID = AC.C_BPARTNER_ID");
//
//        sb.append("          SELECT PN.C_BPARTNER_ID,           ");
//        sb.append("          PN.TAXID,           ");
//        sb.append("          PN.VALUE,           ");
//        sb.append("          PN.NAME,           ");
//        sb.append("          PN.ISACTIVE,           ");
//        sb.append("          ''  AS VENDORGROUP,           ");
//        sb.append("          AC.CONFIRMSTATUS,           ");
//        sb.append("          ''  AS VENDORSTATUS,           ");
//        sb.append("          '0' AS COMPCODE           ");
//        sb.append("          FROM C_BPARTNER PN           ");
//        sb.append("          JOIN TH_APBPARTNERSTATUS AC           ");
//        sb.append("          ON PN.C_BPARTNER_ID = AC.C_BPARTNER_ID           ");
//        sb.append("          AND PN.ISVENDOR = 'Y'           ");
//
//        if (!Util.isEmpty(valueCode)) {
//            sb.append(SqlUtil.whereClause(valueCode, "PN.VALUE", params));
//        }
//        logger.info("valueCode : {}",valueCode);
//        logger.info("sql vendor : {}", sb.toString());
//
//        Query q = entityManager.createNativeQuery(sb.toString(), Vendor.class);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        Vendor vendor = (Vendor) q.getSingleResult();
//        logger.info("vendor : {}", vendor);
//
//        return vendor;
//    }
//
//    public List<Vendor> findAllAlternatePayee() {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" PY.C_BPARTNER_ID, ");
//        sb.append(" PY.TAXID, ");
//        sb.append(" PY.VALUE, ");
//        sb.append(" PY.NAME, ");
//        sb.append(" PN.ISACTIVE, ");
//        sb.append(" BG.VALUE AS VENDORGROUP, ");
//        sb.append(" AC.CONFIRMSTATUS, ");
//        sb.append(" '' AS VENDORSTATUS, ");
//        sb.append(" '0' AS COMPCODE ");
//        sb.append(" FROM ");
//        sb.append(" C_BP_RELATION   RL, ");
//        sb.append(" AD_ORG          AD, ");
//        sb.append(" C_BPARTNER      PN,  ");
//        sb.append(" C_BPARTNER      PY,  ");
//        sb.append(" C_BP_GROUP      BG, ");
//        sb.append(" TH_APBPACCESSCONTROL AC ");
//        sb.append(" WHERE ");
//        sb.append(" RL.AD_ORG_ID = AD.AD_ORG_ID  ");
//        sb.append(" AND PN.C_BPARTNER_ID = RL.C_BPARTNER_ID  ");
//        sb.append(" AND RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID ");
//        sb.append(" AND RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID ");
//        sb.append(" AND PY.C_BP_GROUP_ID = BG.C_BP_GROUP_ID  ");
//        sb.append(" AND PY.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
//        sb.append(" ORDER BY ");
//        sb.append(" PY.VALUE ASC");
//
//        Query q = entityManager.createNativeQuery(sb.toString(), Vendor.class);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        List<Vendor> vendors = q.getResultList();
//        logger.info("vendors : {}", vendors);
//
//        return vendors;
//    }
//
//    public List<Vendor> findByKeyAlternative(String compCode, String vendorTaxId, String key) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" PY.C_BPARTNER_ID, ");
//        sb.append(" PY.TAXID, ");
//        sb.append(" PY.VALUE, ");
//        sb.append(" PY.NAME, ");
//        sb.append(" PN.ISACTIVE, ");
//        sb.append(" BG.VALUE AS VENDORGROUP, ");
//        sb.append(" AC.CONFIRMSTATUS, ");
//        sb.append(" '' AS VENDORSTATUS, ");
//        sb.append(" '0' AS COMPCODE ");
//        sb.append(" FROM ");
//        sb.append(" C_BP_RELATION   RL, ");
//        sb.append(" AD_ORG          AD, ");
//        sb.append(" C_BPARTNER      PN,  ");
//        sb.append(" C_BPARTNER      PY,  ");
//        sb.append(" C_BP_GROUP      BG, ");
//        sb.append(" TH_APBPACCESSCONTROL AC ");
//        sb.append(" WHERE ");
//        sb.append(" RL.AD_ORG_ID = AD.AD_ORG_ID  ");
//        sb.append(" AND PN.C_BPARTNER_ID = RL.C_BPARTNER_ID  ");
//        sb.append(" AND RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID ");
//        sb.append(" AND RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID ");
//        sb.append(" AND PY.C_BP_GROUP_ID = BG.C_BP_GROUP_ID  ");
//        sb.append(" AND PY.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
//        sb.append("  ");
//
//        if (!Util.isEmpty(compCode)) {
//            sb.append(SqlUtil.whereClause(compCode, "AD.VALUE", params));
//        }
//        if (!Util.isEmpty(vendorTaxId)) {
//            sb.append(SqlUtil.whereClause(vendorTaxId, "PN.TAXID", params));
//        }
//        if (!Util.isEmpty(key)) {
//            sb.append(SqlUtil.whereClauseOr(key, params, "PY.VALUE", "PY.NAME", "PY.TAXID"));
//        }
//        sb.append(" ORDER BY ");
//        sb.append(" PY.VALUE ASC");
//
//        Query q = entityManager.createNativeQuery(sb.toString(), Vendor.class);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        List<Vendor> vendors = q.getResultList();
//        logger.info("vendors : {}", vendors);
//
//        return vendors;
//    }
//
//    public Long countByKeyAlternative(String compCode, String vendorTaxId, String key) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" COUNT(1) ");
//        sb.append(" FROM ");
//        sb.append(" C_BP_RELATION   RL, ");
//        sb.append(" AD_ORG          AD, ");
//        sb.append(" C_BPARTNER      PN,  ");
//        sb.append(" C_BPARTNER      PY,  ");
//        sb.append(" C_BP_GROUP      BG, ");
//        sb.append(" TH_APBPACCESSCONTROL AC ");
//        sb.append(" WHERE ");
//        sb.append(" RL.AD_ORG_ID = AD.AD_ORG_ID  ");
//        sb.append(" AND PN.C_BPARTNER_ID = RL.C_BPARTNER_ID  ");
//        sb.append(" AND RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID ");
//        sb.append(" AND RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID ");
//        sb.append(" AND PY.C_BP_GROUP_ID = BG.C_BP_GROUP_ID  ");
//        sb.append(" AND PY.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
//        sb.append("  ");
//
//        if (!Util.isEmpty(compCode)) {
//            sb.append(SqlUtil.whereClause(compCode, "AD.VALUE", params));
//        }
//        if (!Util.isEmpty(vendorTaxId)) {
//            sb.append(SqlUtil.whereClause(vendorTaxId, "PN.TAXID", params));
//        }
//        if (!Util.isEmpty(key)) {
//            sb.append(SqlUtil.whereClauseOr(key, params, "PY.VALUE", "PY.NAME", "PY.TAXID"));
//        }
//        sb.append(" ORDER BY ");
//        sb.append(" PY.VALUE ASC");
//
//        Query q = entityManager.createNativeQuery(sb.toString());
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        Long count = ((Number) q.getSingleResult()).longValue();
//        logger.info("count : {}", count);
//
//        return count;
//    }
//
//    public Vendor findOneAlternative(String compCode, String vendorTaxId, String valueCode) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT PY.C_BPARTNER_ID, ");
//        sb.append(" PY.TAXID, ");
//        sb.append(" PY.VALUE, ");
//        sb.append(" PY.NAME, ");
//        sb.append(" PN.ISACTIVE, ");
//        sb.append(" BG.VALUE AS VENDORGROUP, ");
//        sb.append(" AC.CONFIRMSTATUS, ");
//        sb.append(" '' AS VENDORSTATUS, ");
//        sb.append(" '0' AS COMPCODE ");
//        sb.append(" FROM C_BP_RELATION RL, ");
//        sb.append(" AD_ORG AD, ");
//        sb.append(" C_BPARTNER PN, ");
//        sb.append(" C_BPARTNER PY, ");
//        sb.append(" C_BP_GROUP BG, ");
//        sb.append(" TH_APBPACCESSCONTROL AC ");
//        sb.append(" WHERE RL.AD_ORG_ID = AD.AD_ORG_ID ");
//        sb.append(" AND PN.C_BPARTNER_ID = RL.C_BPARTNER_ID ");
//        sb.append(" AND RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID ");
//        sb.append(" AND RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID ");
//        sb.append(" AND PY.C_BP_GROUP_ID = BG.C_BP_GROUP_ID ");
//        sb.append(" AND PY.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
//        sb.append(" AND AC.APPROVALSTATUS = '1' ");
//
//        if (!Util.isEmpty(compCode)) {
//            sb.append(SqlUtil.whereClause(compCode, "AD.VALUE", params));
//        }
//        if (!Util.isEmpty(vendorTaxId)) {
//            sb.append(SqlUtil.whereClause(vendorTaxId, "PN.TAXID", params));
//        }
//        if (!Util.isEmpty(valueCode)) {
//            sb.append(SqlUtil.whereClause(valueCode, "PY.TAXID", params));
//        }
//
//        Query q = entityManager.createNativeQuery(sb.toString(), Vendor.class);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        Vendor vendor = (Vendor) q.getSingleResult();
//        logger.info("vendor : {}", vendor);
//
//        return vendor;
//    }
//}
