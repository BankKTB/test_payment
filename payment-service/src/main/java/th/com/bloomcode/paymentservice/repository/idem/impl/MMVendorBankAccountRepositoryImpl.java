package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.Vendor;
import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.MMVendorBankAccountRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class MMVendorBankAccountRepositoryImpl extends MetadataJdbcRepository<VendorBankAccount, Long> implements MMVendorBankAccountRepository {
    static BeanPropertyRowMapper<VendorBankAccount> beanPropertyRowMapper = new BeanPropertyRowMapper<>(VendorBankAccount.class);
    private final JdbcTemplate jdbcTemplate;

    static RowMapper<VendorBankAccount> vendorBankAccountRowMapper = (rs, rowNum) -> new VendorBankAccount(
            rs.getString(VendorBankAccount.COLUMN_NAME_VENDOR),
            rs.getString(VendorBankAccount.COLUMN_NAME_VENDOR_NAME),
            rs.getString(VendorBankAccount.COLUMN_NAME_ACCOUNT_NO),
            rs.getString(VendorBankAccount.COLUMN_NAME_ROUTING_NO),
            rs.getString(VendorBankAccount.COLUMN_NAME_BANK_KEY),
            rs.getString(VendorBankAccount.COLUMN_NAME_IS_ACTIVE)
    );

//    static Updater<VendorBankAccount> vendorUpdater = (t, mapping) -> {
//        mapping.put(VendorBankAccount.COLUMN_NAME_C_BPARTNER_ID, t.getId());
//        mapping.put(VendorBankAccount.COLUMN_NAME_VALUE, t.getValueCode());
//        mapping.put(VendorBankAccount.COLUMN_NAME_NAME, t.getName());
//        mapping.put(VendorBankAccount.COLUMN_NAME_TAX_ID, t.getTaxId());
//    };
//    static Map<String, Integer> updaterType = Map.ofEntries(
//            entry(VendorBankAccount.COLUMN_NAME_C_BPARTNER_ID, Types.BIGINT),
//            entry(VendorBankAccount.COLUMN_NAME_VALUE, Types.NVARCHAR),
//            entry(VendorBankAccount.COLUMN_NAME_NAME, Types.NVARCHAR),
//            entry(VendorBankAccount.COLUMN_NAME_TAX_ID, Types.NVARCHAR)
//    );

    @Autowired
    public MMVendorBankAccountRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                beanPropertyRowMapper,
                null,
                null,
                Vendor.TABLE_NAME,
                Vendor.COLUMN_NAME_C_BPARTNER_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<VendorBankAccount> findByCondition(String request, String type,String paymentMethodType) {
        //        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT BA.C_BP_BANKACCOUNT_ID, ");
        sb.append(" BA.ACCOUNTNO, ");
        sb.append(" BA.ISACTIVE, ");
        sb.append(" BK.ROUTINGNO, ");
        sb.append(" BN.VALUECODE AS BANKKEY, ");
        sb.append(" PN.VALUE     AS VENDOR, ");
        sb.append(" PN.NAME      AS VENDORNAME ");
        sb.append(" FROM C_BP_BANKACCOUNT BA ");
        sb.append(" LEFT JOIN C_BANK BK ON BA.C_BANK_ID = BK.C_BANK_ID ");
        sb.append(" LEFT JOIN C_BPARTNER PN ON BA.C_BPARTNER_ID = PN.C_BPARTNER_ID ");
        sb.append(" LEFT JOIN TH_CABANKINFO BI ON BK.C_BANK_ID = BI.C_BANK_ID ");
        sb.append(" LEFT JOIN TH_CABANK BN ON BI.TH_CABANK_ID = BN.TH_CABANK_ID ");
        sb.append(" WHERE PN.ISACTIVE = 'Y' ");
        sb.append(" AND PN.ISVENDOR = 'Y' ");

        if (!Util.isEmpty(request)) {
            sb.append(
                    SqlUtil.whereClauseOr(
                            request, params, "PN.TAXID"));
        }
        //defect 12-10-2022 พี่ไผ่ให้คำตอบไม่ได้ติดเอาไว้ก่อน
//        if (!Util.isEmpty(paymentMethodType)) {
//            if (paymentMethodType.equalsIgnoreCase("direct")) {
//                if (Util.removeStarOrPercent(request).length() == 10) {
//                    sb.append(" AND VALUE LIKE 'V%' ");
//                }
//
//            }else{
//                sb.append(" AND VALUE NOT LIKE 'V%' ");
//            }
//        }

        sb.append(" ORDER BY ");
        sb.append(" BA.ACCOUNTNO ASC");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("objParams : {}", objParams);
        log.info("objParams : {}", sb.toString());

        return this.jdbcTemplate.query(sb.toString(), objParams, vendorBankAccountRowMapper);
    }


    public Long countByCondition(String request, String type,String paymentMethodType) {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" COUNT(1) ");
        sb.append(" FROM C_BP_BANKACCOUNT BA ");
        sb.append(" LEFT JOIN C_BANK BK ON BA.C_BANK_ID = BK.C_BANK_ID ");
        sb.append(" LEFT JOIN C_BPARTNER PN ON BA.C_BPARTNER_ID = PN.C_BPARTNER_ID ");
        sb.append(" LEFT JOIN TH_CABANKINFO BI ON BK.C_BANK_ID = BI.C_BANK_ID ");
        sb.append(" LEFT JOIN TH_CABANK BN ON BI.TH_CABANK_ID = BN.TH_CABANK_ID ");
        sb.append(" WHERE PN.ISACTIVE = 'Y' ");
        sb.append(" AND PN.ISVENDOR = 'Y' ");



        if (!Util.isEmpty(request)) {
            sb.append(
                    SqlUtil.whereClauseOr(
                            request, params, "PN.TAXID"));
        }
//        if (!Util.isEmpty(type)) {
//            if (type.equalsIgnoreCase("1")) {
//
//            }
//        }
        //defect 12-10-2022 พี่ไผ่ให้คำตอบไม่ได้ติดเอาไว้ก่อน
//        if (!Util.isEmpty(paymentMethodType)) {
//            if (paymentMethodType.equalsIgnoreCase("direct")) {
//                    sb.append(" AND VALUE NOT LIKE 'V%' ");
//            }else{
//                if (Util.removeStarOrPercent(request).length() == 10) {
//                    sb.append(" AND VALUE LIKE 'V%' ");
//                }
//            }
//        }


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("objParams : {}", objParams);
        log.info("sql : {}", sb.toString());
        return this.jdbcTemplate.queryForObject(sb.toString(), objParams, Long.class);
    }
}
