//package th.com.bloomcode.paymentservice.idem.dao;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//import th.com.bloomcode.paymentservice.idem.entity.HouseBankPaymentMethod;
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
//public class HouseBankPaymentMethodRepository {
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    @Qualifier("idemEntityManagerFactory")
//    private EntityManager entityManager;
//
//    public List<HouseBankPaymentMethod> findByKey(String key) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" pm.TH_CAHOUSEBANKACCOUNT_PM_ID, ");
//        sb.append(" hb.VALUECODE AS HOUSEBANK, ");
//        sb.append(" hb.SHORTDESCRIPTION AS HOUSEBANKNAME, ");
//        sb.append(" cb.ROUTINGNO   AS BANKBRANCH, ");
//        sb.append(" ct.COUNTRYCODE as COUNTRYCODE, ");
//        sb.append(" mt.VALUECODE AS PAYMENTMETHOD, ");
//        sb.append(" cr.ISO_CODE  AS CURRENCY, ");
//        sb.append(" ba.VALUECODE AS ACCOUNTCODE, ");
//        sb.append(" el.VALUE     AS GLACCOUNT, ");
//        sb.append(" ba.ACCOUNTNO   AS BANKACCOUNTNO ");
//        sb.append(" FROM ");
//        sb.append(" TH_CAHOUSEBANKACCOUNT_PM pm, ");
//        sb.append(" TH_CAHOUSEBANKACCOUNT ba, ");
//        sb.append(" TH_CAHOUSEBANK hb, ");
//        sb.append(" C_BANK cb, ");
//        sb.append(" TH_CAPAYMENTMETHOD mt, ");
//        sb.append(" AD_CLIENT cl, ");
//        sb.append(" C_CURRENCY cr, ");
//        sb.append(" C_ElementValue el, ");
//        sb.append(" C_COUNTRY ct ");
//        sb.append(" WHERE ");
//        sb.append(" pm.TH_CAHOUSEBANKACCOUNT_ID = ba.TH_CAHOUSEBANKACCOUNT_ID ");
//        sb.append(" and ba.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
//        sb.append(" and ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
//        sb.append(" and ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
//        sb.append(" and pm.TH_CAPAYMENTMETHOD_ID = mt.TH_CAPAYMENTMETHOD_ID ");
//        sb.append(" and hb.TH_CAHOUSEBANK_ID = ba.TH_CAHOUSEBANK_ID ");
//        sb.append(" and hb.C_BANK_ID = cb.C_BANK_ID ");
//        sb.append(" and hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
//        sb.append(" and pm.ISACTIVE = 'Y' ");
//        sb.append(" and ba.ISACTIVE = 'Y' ");
//        sb.append(" and cl.ISACTIVE = 'Y' ");
//        sb.append(" and cr.ISACTIVE = 'Y' ");
//        sb.append(" and el.ISACTIVE = 'Y' ");
//        sb.append(" and mt.ISACTIVE = 'Y' ");
//        sb.append(" and hb.ISACTIVE = 'Y' ");
//        sb.append(" and cb.ISACTIVE = 'Y' ");
//        sb.append(" and ct.ISACTIVE = 'Y' ");
//
//
//        if (!Util.isEmpty(key)) {
//            sb.append(SqlUtil.whereClauseOr(key, params, "CL.VALUE"));
//        }
//
//        sb.append(" ORDER BY ");
//        sb.append(" mt.VALUECODE ASC");
//
//        Query q = entityManager.createNativeQuery(sb.toString(), HouseBankPaymentMethod.class);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        List<HouseBankPaymentMethod> houseBankPaymentMethod = q.getResultList();
//        logger.info("houseBankPaymentMethod : {}", houseBankPaymentMethod);
//
//        return houseBankPaymentMethod;
//    }
//
//    public Long countByKey(String key) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" COUNT(1) ");
//        sb.append(" FROM ");
//        sb.append(" TH_CAHOUSEBANKACCOUNT_PM pm, ");
//        sb.append(" TH_CAHOUSEBANKACCOUNT ba, ");
//        sb.append(" TH_CAHOUSEBANK hb, ");
//        sb.append(" C_BANK cb, ");
//        sb.append(" TH_CAPAYMENTMETHOD mt, ");
//        sb.append(" AD_CLIENT cl, ");
//        sb.append(" C_CURRENCY cr, ");
//        sb.append(" C_ElementValue el, ");
//        sb.append(" C_COUNTRY ct ");
//        sb.append(" WHERE ");
//        sb.append(" pm.TH_CAHOUSEBANKACCOUNT_ID = ba.TH_CAHOUSEBANKACCOUNT_ID ");
//        sb.append(" and ba.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
//        sb.append(" and ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
//        sb.append(" and ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
//        sb.append(" and pm.TH_CAPAYMENTMETHOD_ID = mt.TH_CAPAYMENTMETHOD_ID ");
//        sb.append(" and hb.TH_CAHOUSEBANK_ID = ba.TH_CAHOUSEBANK_ID ");
//        sb.append(" and hb.C_BANK_ID = cb.C_BANK_ID ");
//        sb.append(" and hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
//        sb.append(" and pm.ISACTIVE = 'Y' ");
//        sb.append(" and ba.ISACTIVE = 'Y' ");
//        sb.append(" and cl.ISACTIVE = 'Y' ");
//        sb.append(" and cr.ISACTIVE = 'Y' ");
//        sb.append(" and el.ISACTIVE = 'Y' ");
//        sb.append(" and mt.ISACTIVE = 'Y' ");
//        sb.append(" and hb.ISACTIVE = 'Y' ");
//        sb.append(" and cb.ISACTIVE = 'Y' ");
//        sb.append(" and ct.ISACTIVE = 'Y' ");
//
//
//        if (!Util.isEmpty(key)) {
//            sb.append(SqlUtil.whereClauseOr(key, params, "CL.VALUE"));
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
//    public HouseBankPaymentMethod findOne(String client, String houseBankKey, String paymentMethod) {
//        Map<String, Object> params = new HashMap<>();
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//        sb.append(" pm.TH_CAHOUSEBANKACCOUNT_PM_ID, ");
//        sb.append(" hb.VALUECODE AS HOUSEBANK, ");
//        sb.append(" hb.SHORTDESCRIPTION AS HOUSEBANKNAME, ");
//        sb.append(" cb.ROUTINGNO   AS BANKBRANCH, ");
//        sb.append(" ct.COUNTRYCODE as COUNTRYCODE, ");
//        sb.append(" mt.VALUECODE AS PAYMENTMETHOD, ");
//        sb.append(" cr.ISO_CODE  AS CURRENCY, ");
//        sb.append(" ba.VALUECODE AS ACCOUNTCODE, ");
//        sb.append(" el.VALUE     AS GLACCOUNT, ");
//        sb.append(" ba.ACCOUNTNO   AS BANKACCOUNTNO ");
//        sb.append(" FROM ");
//        sb.append(" TH_CAHOUSEBANKACCOUNT_PM pm, ");
//        sb.append(" TH_CAHOUSEBANKACCOUNT ba, ");
//        sb.append(" TH_CAHOUSEBANK hb, ");
//        sb.append(" C_BANK cb, ");
//        sb.append(" TH_CAPAYMENTMETHOD mt, ");
//        sb.append(" AD_CLIENT cl, ");
//        sb.append(" C_CURRENCY cr, ");
//        sb.append(" C_ElementValue el, ");
//        sb.append(" C_COUNTRY ct ");
//        sb.append(" WHERE ");
//        sb.append(" pm.TH_CAHOUSEBANKACCOUNT_ID = ba.TH_CAHOUSEBANKACCOUNT_ID ");
//        sb.append(" and ba.AD_CLIENT_ID = cl.AD_CLIENT_ID ");
//        sb.append(" and ba.C_CURRENCY_ID = cr.C_CURRENCY_ID ");
//        sb.append(" and ba.ACCOUNT_ID = el.C_ELEMENTVALUE_ID ");
//        sb.append(" and pm.TH_CAPAYMENTMETHOD_ID = mt.TH_CAPAYMENTMETHOD_ID ");
//        sb.append(" and hb.TH_CAHOUSEBANK_ID = ba.TH_CAHOUSEBANK_ID ");
//        sb.append(" and hb.C_BANK_ID = cb.C_BANK_ID ");
//        sb.append(" and hb.C_COUNTRY_ID = ct.C_COUNTRY_ID ");
//        sb.append(" and pm.ISACTIVE = 'Y' ");
//        sb.append(" and ba.ISACTIVE = 'Y' ");
//        sb.append(" and cl.ISACTIVE = 'Y' ");
//        sb.append(" and cr.ISACTIVE = 'Y' ");
//        sb.append(" and el.ISACTIVE = 'Y' ");
//        sb.append(" and mt.ISACTIVE = 'Y' ");
//        sb.append(" and hb.ISACTIVE = 'Y' ");
//        sb.append(" and cb.ISACTIVE = 'Y' ");
//        sb.append(" and ct.ISACTIVE = 'Y' ");
//
//
//        if (!Util.isEmpty(client)) {
//            sb.append(SqlUtil.whereClause(client, "CL.VALUE", params));
//        }
//
//        if (!Util.isEmpty(houseBankKey)) {
//            sb.append(SqlUtil.whereClause(houseBankKey, "hb.VALUECODE", params));
//        }
//
//        if (!Util.isEmpty(paymentMethod)) {
//            sb.append(SqlUtil.whereClause(paymentMethod, "mt.VALUECODE", params));
//        }
//        Query q = entityManager.createNativeQuery(sb.toString(), HouseBankPaymentMethod.class);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        HouseBankPaymentMethod houseBankPaymentMethod = (HouseBankPaymentMethod) q.getSingleResult();
//        logger.info("houseBankPaymentMethod : {}", houseBankPaymentMethod);
//
//        return houseBankPaymentMethod;
//    }
//}
