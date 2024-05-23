package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.VendorBankAccount;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VendorBankAccountRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<VendorBankAccount> findAll() {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BA.C_BP_BANKACCOUNT_ID, ");
        sb.append(" BA.ACCOUNTNO, ");
        sb.append(" BA.ISACTIVE, ");
        sb.append(" BK.ROUTINGNO, ");
        sb.append(" BN.VALUECODE AS BANKKEY, ");
        sb.append(" BN.NAME AS BANKNAME, ");
        sb.append(" PN.VALUE AS VENDOR, ");
        sb.append(" BA.A_NAME AS BANK_ACCOUNT_HOLDER_NAME, ");
        sb.append(" PN.NAME AS VENDORNAME ");
        sb.append(" FROM ");
        sb.append(" C_BP_BANKACCOUNT BA, ");
        sb.append(" C_BANK BK, ");
        sb.append(" C_BPARTNER PN, ");
        sb.append(" TH_CABANKINFO BI, ");
        sb.append(" TH_CABANK BN ");
        sb.append(" WHERE ");
        sb.append(" BA.C_BANK_ID = BK.C_BANK_ID ");
        sb.append(" AND BA.C_BPARTNER_ID = PN.C_BPARTNER_ID ");
        sb.append(" AND BI.TH_CABANK_ID = BN.TH_CABANK_ID ");
        sb.append(" AND BK.C_BANK_ID = BI.C_BANK_ID ");
        sb.append(" AND PN.ISACTIVE = 'Y' ");
        sb.append(" AND PN.ISVENDOR = 'Y' ");
        sb.append(" ORDER BY ");
        sb.append(" BA.ACCOUNTNO ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), VendorBankAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<VendorBankAccount> vendorBankAccounts = q.getResultList();
        logger.info("vendorBankAccounts : {}", vendorBankAccounts);

        return vendorBankAccounts;
    }

    public List<VendorBankAccount> findByKey(String taxId, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BA.C_BP_BANKACCOUNT_ID, ");
        sb.append(" BA.ACCOUNTNO, ");
        sb.append(" BA.ISACTIVE, ");
        sb.append(" BK.ROUTINGNO, ");
        sb.append(" BN.VALUECODE AS BANKKEY, ");
        sb.append(" BN.NAME AS BANKNAME, ");
        sb.append(" PN.VALUE AS VENDOR, ");
        sb.append(" BA.A_NAME AS BANK_ACCOUNT_HOLDER_NAME, ");
        sb.append(" PN.NAME AS VENDORNAME ");
        sb.append(" FROM ");
        sb.append(" C_BP_BANKACCOUNT BA, ");
        sb.append(" C_BANK BK, ");
        sb.append(" C_BPARTNER PN, ");
        sb.append(" TH_CABANKINFO BI, ");
        sb.append(" TH_CABANK BN ");
        sb.append(" WHERE ");
        sb.append(" BA.C_BANK_ID = BK.C_BANK_ID ");
        sb.append(" AND BA.C_BPARTNER_ID = PN.C_BPARTNER_ID ");
        sb.append(" AND BI.TH_CABANK_ID = BN.TH_CABANK_ID ");
        sb.append(" AND BK.C_BANK_ID = BI.C_BANK_ID ");
        sb.append(" AND PN.ISACTIVE = 'Y' ");
        sb.append(" AND PN.ISVENDOR = 'Y' ");
//    sb.append(" AND BA.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(taxId)) {
            sb.append(SqlUtil.whereClause(taxId, "PN.TAXID", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BA.ACCOUNTNO", "BN.VALUECODE", "PN.VALUE", "PN.NAME"));
        }
        sb.append(" ORDER BY ");
        sb.append(" BA.ACCOUNTNO ASC");

        Query q = entityManager.createNativeQuery(sb.toString(), VendorBankAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<VendorBankAccount> vendorBankAccounts = q.getResultList();
        logger.info("vendorBankAccounts : {}", vendorBankAccounts);

        return vendorBankAccounts;
    }

    public Long countByKey(String taxId, String key) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM ");
        sb.append(" C_BP_BANKACCOUNT BA, ");
        sb.append(" C_BANK BK, ");
        sb.append(" C_BPARTNER PN, ");
        sb.append(" TH_CABANKINFO BI, ");
        sb.append(" TH_CABANK BN ");
        sb.append(" WHERE ");
        sb.append(" BA.C_BANK_ID = BK.C_BANK_ID ");
        sb.append(" AND BA.C_BPARTNER_ID = PN.C_BPARTNER_ID ");
        sb.append(" AND BI.TH_CABANK_ID = BN.TH_CABANK_ID ");
        sb.append(" AND BK.C_BANK_ID = BI.C_BANK_ID ");
        sb.append(" AND PN.ISACTIVE = 'Y' ");
        sb.append(" AND PN.ISVENDOR = 'Y' ");
//    sb.append(" AND BA.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(taxId)) {
            sb.append(SqlUtil.whereClause(taxId, "PN.TAXID", params));
        }

        if (!Util.isEmpty(key)) {
            sb.append(SqlUtil.whereClauseOr(key, params, "BA.ACCOUNTNO", "BN.VALUECODE", "PN.VALUE", "PN.NAME"));
        }

        Query q = entityManager.createNativeQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = ((Number) q.getSingleResult()).longValue();
        logger.info("count : {}", count);

        return count;
    }

    public VendorBankAccount findOne(String valueCode) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" BA.C_BP_BANKACCOUNT_ID, ");
        sb.append(" BA.ACCOUNTNO, ");
        sb.append(" BA.ISACTIVE, ");
        sb.append(" BK.ROUTINGNO, ");
        sb.append(" BN.VALUECODE AS BANKKEY, ");
        sb.append(" BN.NAME AS BANKNAME, ");
        sb.append(" PN.VALUE AS VENDOR, ");
        sb.append(" BA.A_NAME AS BANK_ACCOUNT_HOLDER_NAME, ");
        sb.append(" PN.NAME AS VENDORNAME ");
        sb.append(" FROM ");
        sb.append(" C_BP_BANKACCOUNT BA, ");
        sb.append(" C_BANK BK, ");
        sb.append(" C_BPARTNER PN, ");
        sb.append(" TH_CABANKINFO BI, ");
        sb.append(" TH_CABANK BN ");
        sb.append(" WHERE ");
        sb.append(" BA.C_BANK_ID = BK.C_BANK_ID ");
        sb.append(" AND BA.C_BPARTNER_ID = PN.C_BPARTNER_ID ");
        sb.append(" AND BI.TH_CABANK_ID = BN.TH_CABANK_ID ");
        sb.append(" AND BK.C_BANK_ID = BI.C_BANK_ID ");
        sb.append(" AND PN.ISACTIVE = 'Y' ");
        sb.append(" AND PN.ISVENDOR = 'Y' ");
//    sb.append(" AND BA.ISACTIVE = 'Y' ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "BA.ACCOUNTNO", params));
        }

        Query q = entityManager.createNativeQuery(sb.toString(), VendorBankAccount.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
            q.setMaxResults(1);
        }
        VendorBankAccount vendorBankAccount = (VendorBankAccount) q.getSingleResult();
        logger.info("vendorBankAccount : {}", vendorBankAccount);

        return vendorBankAccount;
    }


}
