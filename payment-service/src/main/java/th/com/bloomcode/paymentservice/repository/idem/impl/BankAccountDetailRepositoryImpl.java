package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.BankAccountDetail;
import th.com.bloomcode.paymentservice.model.idem.Vendor;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.BankAccountDetailRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class BankAccountDetailRepositoryImpl extends MetadataJdbcRepository<BankAccountDetail, Long> implements BankAccountDetailRepository {
    static BeanPropertyRowMapper<BankAccountDetail> beanPropertyRowMapper = new BeanPropertyRowMapper<>(BankAccountDetail.class);
    private final JdbcTemplate jdbcTemplate;

    static RowMapper<BankAccountDetail> vendorBankAccountRowMapper = (rs, rowNum) -> new BankAccountDetail(
            rs.getString(BankAccountDetail.COLUMN_NAME_BANK_KEY),
            rs.getString(BankAccountDetail.COLUMN_NAME_BANK_NAME),
            rs.getString(BankAccountDetail.COLUMN_NAME_ROUTING_NO),
            rs.getString(BankAccountDetail.COLUMN_NAME_BANK_BRANCH_NAME),
            rs.getString(BankAccountDetail.COLUMN_NAME_ACCOUNT_NO),
            rs.getString(BankAccountDetail.COLUMN_NAME_ACCOUNT_HOLDER_NAME),
            rs.getString(BankAccountDetail.COLUMN_NAME_IS_ACTIVE),
            rs.getString(BankAccountDetail.COLUMN_NAME_REFERENCE_DETAIL)
    );


    @Autowired
    public BankAccountDetailRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
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
    public List<BankAccountDetail> findByCondition(String vendor,String value,String routingNo) {
        //        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT            ");
        sb.append("          BN.VALUECODE AS BANKKEY,           ");
        sb.append("          BN.NAME      AS BANKNAME,           ");
        sb.append("          BK.ROUTINGNO,           ");
        sb.append("          BK.NAME      AS BANK_BRANCH_NAME,           ");
        sb.append("          BA.ACCOUNTNO,           ");
        sb.append("          BA.A_NAME    AS ACCOUNT_HOLDER_NAME,           ");
        sb.append("          BA.ISACTIVE,          ");
        sb.append("          BA.A_EMAIL AS REFERENCE_DETAIL           ");

        sb.append("          FROM C_BP_BANKACCOUNT BA           ");
        sb.append("          LEFT JOIN C_BANK BK ON BA.C_BANK_ID = BK.C_BANK_ID           ");
        sb.append("          LEFT JOIN C_BPARTNER PN ON BA.C_BPARTNER_ID = PN.C_BPARTNER_ID           ");
        sb.append("          LEFT JOIN TH_CABANKINFO BI ON BK.C_BANK_ID = BI.C_BANK_ID           ");
        sb.append("          LEFT JOIN TH_CABANK BN ON BI.TH_CABANK_ID = BN.TH_CABANK_ID           ");
        sb.append("          WHERE PN.ISACTIVE = 'Y'           ");
        sb.append("          AND PN.ISVENDOR = 'Y'           ");

        if (!Util.isEmpty(vendor)) {
            sb.append(SqlUtil.whereClause(vendor, "PN.VALUE", params));
        }

        if (!Util.isEmpty(value)) {
            sb.append(SqlUtil.whereClause(value, "BA.ACCOUNTNO", params));
        }
        if (!Util.isEmpty(routingNo)) {
            sb.append(SqlUtil.whereClause(routingNo, "BK.ROUTINGNO", params));
        }

        sb.append(" ORDER BY ");
        sb.append(" BA.ACCOUNTNO ASC");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("objParams : {}", objParams);
        log.info("objParams : {}", sb.toString());

        return this.jdbcTemplate.query(sb.toString(), objParams, vendorBankAccountRowMapper);
    }


    public Long countByCondition(String vendor,String value,String routingNo) {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" COUNT(1) ");
        sb.append("          FROM C_BP_BANKACCOUNT BA           ");
        sb.append("          LEFT JOIN C_BANK BK ON BA.C_BANK_ID = BK.C_BANK_ID           ");
        sb.append("          LEFT JOIN C_BPARTNER PN ON BA.C_BPARTNER_ID = PN.C_BPARTNER_ID           ");
        sb.append("          LEFT JOIN TH_CABANKINFO BI ON BK.C_BANK_ID = BI.C_BANK_ID           ");
        sb.append("          LEFT JOIN TH_CABANK BN ON BI.TH_CABANK_ID = BN.TH_CABANK_ID           ");
        sb.append("          WHERE PN.ISACTIVE = 'Y'           ");
        sb.append("          AND PN.ISVENDOR = 'Y'           ");

        if (!Util.isEmpty(vendor)) {
            sb.append(SqlUtil.whereClause(vendor, "PN.VALUE", params));
        }

        if (!Util.isEmpty(value)) {
            sb.append(SqlUtil.whereClause(value, "BA.ACCOUNTNO", params));
        }
        if (!Util.isEmpty(routingNo)) {
            sb.append(SqlUtil.whereClause(routingNo, "BK.ROUTINGNO", params));
        }

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("objParams : {}", objParams);
        log.info("sql : {}", sb.toString());
        return this.jdbcTemplate.queryForObject(sb.toString(), objParams, Long.class);
    }
}
