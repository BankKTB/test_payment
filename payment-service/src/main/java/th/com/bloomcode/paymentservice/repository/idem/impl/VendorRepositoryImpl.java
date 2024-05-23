package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.Vendor;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.VendorRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class VendorRepositoryImpl extends MetadataJdbcRepository<Vendor, Long> implements VendorRepository {
    static BeanPropertyRowMapper<Vendor> beanPropertyRowMapper = new BeanPropertyRowMapper<>(Vendor.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<Vendor> vendorUpdater = (t, mapping) -> {
        mapping.put(Vendor.COLUMN_NAME_C_BPARTNER_ID, t.getId());
        mapping.put(Vendor.COLUMN_NAME_VALUE, t.getValueCode());
        mapping.put(Vendor.COLUMN_NAME_NAME, t.getName());
        mapping.put(Vendor.COLUMN_NAME_TAX_ID, t.getTaxId());
    };
    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(Vendor.COLUMN_NAME_C_BPARTNER_ID, Types.BIGINT),
            entry(Vendor.COLUMN_NAME_VALUE, Types.NVARCHAR),
            entry(Vendor.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(Vendor.COLUMN_NAME_TAX_ID, Types.NVARCHAR)
    );

    static RowMapper<Vendor> userRowMapper = (rs, rowNum) -> new Vendor(
            rs.getLong(Vendor.COLUMN_NAME_C_BPARTNER_ID),
            rs.getString(Vendor.COLUMN_NAME_VALUE),
            rs.getString(Vendor.COLUMN_NAME_NAME),
            rs.getString(Vendor.COLUMN_NAME_TAX_ID),
            rs.getString(Vendor.COLUMN_NAME_VENDOR_GROUP),
            rs.getString(Vendor.COLUMN_NAME_CONFIRM_STATUS),
            rs.getString(Vendor.COLUMN_NAME_VENDOR_STATUS),
            rs.getString(Vendor.COLUMN_NAME_COMP_CODE),
            rs.getString(Vendor.COLUMN_NAME_IS_ACTIVE)
    );

    @Autowired
    public VendorRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                vendorUpdater,
                updaterType,
                Vendor.TABLE_NAME,
                Vendor.COLUMN_NAME_C_BPARTNER_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" COUNT(1) ");
        sql.append(" FROM C_BPARTNER PN ");
        sql.append(" LEFT JOIN C_BP_GROUP BG ON PN.C_BP_GROUP_ID = BG.C_BP_GROUP_ID ");
        sql.append(" LEFT JOIN TH_APBPACCESSCONTROL AC ON PN.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
        sql.append(" WHERE BG.ISACTIVE = 'Y' ");
        sql.append(" AND PN.ISACTIVE = 'Y' ");
        sql.append(" AND PN.ISVENDOR = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "PN.VALUE", "PN.NAME", "PN.TAXID"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<Vendor> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" DISTINCT PN.C_BPARTNER_ID, ");
        sql.append(" PN.TAXID, ");
        sql.append(" PN.VALUE, ");
        sql.append(" PN.NAME, ");
        sql.append(" PN.ISACTIVE, ");
        sql.append(" BG.VALUE AS VENDORGROUP, ");
        sql.append(" '' AS CONFIRMSTATUS, ");
        sql.append(" '' AS VENDORSTATUS, ");
        sql.append(" '0' AS COMPCODE ");
        sql.append(" FROM C_BPARTNER PN ");
        sql.append(" LEFT JOIN C_BP_GROUP BG ON PN.C_BP_GROUP_ID = BG.C_BP_GROUP_ID ");
        sql.append(" LEFT JOIN TH_APBPACCESSCONTROL AC ON PN.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
        sql.append(" WHERE BG.ISACTIVE = 'Y' ");
        sql.append(" AND PN.ISACTIVE = 'Y' ");
        sql.append(" AND PN.ISVENDOR = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "PN.VALUE", "PN.NAME", "PN.TAXID"));
        }
        sql.append(" ORDER BY ");
        sql.append(" PN.VALUE ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public Vendor findOneByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT PN.C_BPARTNER_ID, ");
        sql.append(" PN.TAXID, ");
        sql.append(" PN.VALUE, ");
        sql.append(" PN.NAME, ");
        sql.append(" PN.ISACTIVE, ");
        sql.append(" BG.VALUE AS VENDORGROUP, ");
        sql.append(" AC.CONFIRMSTATUS, ");
        sql.append(" PS.CONFIRMSTATUS AS VENDORSTATUS, ");
        sql.append(" OG.VALUE AS COMPCODE ");
        sql.append(" FROM C_BPARTNER PN LEFT JOIN C_BP_GROUP BG ON PN.C_BP_GROUP_ID = BG.C_BP_GROUP_ID ");
        sql.append(" LEFT JOIN TH_APBPACCESSCONTROL AC ON PN.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
        sql.append(" LEFT JOIN AD_ORG OG ON OG.AD_ORG_ID = AC.AD_ORG_ID ");
        sql.append(" LEFT JOIN TH_APBPartnerStatus PS ON PN.C_BPARTNER_ID = PS.C_BPARTNER_ID ");
        sql.append(" WHERE  ");
        sql.append(" 1=1 ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND PN.VALUE like ? ");
            params.add(valueCode);
        }

        sql.append(" ORDER BY PN.VALUE ASC ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        List<Vendor> vendors = this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
        if (!vendors.isEmpty()) {
            return vendors.get(0);
        } else {
            return null;
        }
    }

    public Vendor findOne(String valueCode) {
        List<Object> params = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        sb.append("          SELECT PN.C_BPARTNER_ID,           ");
        sb.append("          PN.TAXID,           ");
        sb.append("          PN.VALUE,           ");
        sb.append("          PN.NAME,           ");
        sb.append("          PN.ISACTIVE,           ");
        sb.append("          ''  AS VENDORGROUP,           ");
        sb.append("          AC.CONFIRMSTATUS,           ");
        sb.append("          ''  AS VENDORSTATUS,           ");
        sb.append("          '0' AS COMPCODE           ");
        sb.append("          FROM C_BPARTNER PN           ");
        sb.append("          JOIN TH_APBPARTNERSTATUS AC           ");
        sb.append("          ON PN.C_BPARTNER_ID = AC.C_BPARTNER_ID           ");
        sb.append("          AND PN.ISVENDOR = 'Y'           ");

        if (!Util.isEmpty(valueCode)) {
            sb.append(SqlUtil.whereClause(valueCode, "PN.VALUE", params));
        }
        log.info("valueCode : {}",valueCode);
        log.info("sql vendor : {}", sb.toString());

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        List<Vendor> vendors = this.jdbcTemplate.query(sb.toString(), objParams, userRowMapper);
        if (!vendors.isEmpty()) {
            return vendors.get(0);
        } else {
            return null;
        }
    }


    @Override
    public Vendor findOneByValueCodeAndCompCode(String valueCode, String compCode) {
        List<Object> params = new ArrayStack();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT PN.C_BPARTNER_ID, ");

        sql.append(" PN.TAXID, ");
        sql.append(" PN.VALUE, ");
        sql.append(" PN.NAME, ");
        sql.append(" PN.ISACTIVE, ");
        sql.append(" BG.VALUE AS VENDORGROUP, ");
        sql.append(" AC.CONFIRMSTATUS, ");
        sql.append(" PS.CONFIRMSTATUS AS VENDORSTATUS, ");
        sql.append(" OG.VALUE AS COMPCODE ");
        sql.append(" FROM C_BPARTNER PN LEFT JOIN C_BP_GROUP BG ON PN.C_BP_GROUP_ID = BG.C_BP_GROUP_ID ");
        sql.append(" LEFT JOIN TH_APBPACCESSCONTROL AC ON PN.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
        sql.append(" LEFT JOIN AD_ORG OG ON OG.AD_ORG_ID = AC.AD_ORG_ID ");
        sql.append(" LEFT JOIN TH_APBPartnerStatus PS ON PN.C_BPARTNER_ID = PS.C_BPARTNER_ID ");
        sql.append(" WHERE  ");
        sql.append(" 1=1 ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND PN.VALUE like ? ");
            params.add(valueCode);
        }
        if (!Util.isEmpty(compCode)) {
            sql.append(" AND OG.VALUE like ? ");
            params.add(compCode);
        }

        sql.append(" ORDER BY PN.VALUE ASC ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        List<Vendor> vendors = this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
        if (!vendors.isEmpty()) {
            return vendors.get(0);
        } else {
            return null;
        }
//        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{valueCode, compCode}, userRowMapper);
    }

    public Vendor findOneAlternativePayee(String payee) {
        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT PY.C_BPARTNER_ID,           ");
        sb.append("          PY.VALUE,           ");
        sb.append("          PY.NAME,           ");
        sb.append("          PY.TAXID,           ");
        sb.append("          BG.VALUE AS VENDORGROUP,           ");
        sb.append("          AC.CONFIRMSTATUS,           ");
        sb.append("          ''       AS VENDORSTATUS,           ");
        sb.append("          0'      AS COMPCODE,           ");
        sb.append("          PN.ISACTIVE           ");
        sb.append(" FROM C_BP_RELATION RL, ");
        sb.append(" AD_ORG AD, ");
        sb.append(" C_BPARTNER PN, ");
        sb.append(" C_BPARTNER PY, ");
        sb.append(" C_BP_GROUP BG, ");
        sb.append(" TH_APBPACCESSCONTROL AC ");
        sb.append(" WHERE RL.AD_ORG_ID = AD.AD_ORG_ID ");
        sb.append(" AND PN.C_BPARTNER_ID = RL.C_BPARTNER_ID ");
        sb.append(" AND RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID ");
        sb.append(" AND RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID ");
        sb.append(" AND PY.C_BP_GROUP_ID = BG.C_BP_GROUP_ID ");
        sb.append(" AND PY.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
        sb.append(" AND AC.APPROVALSTATUS = '1' ");
        if (!Util.isEmpty(payee)) {
            sb.append(" AND PY.VALUE like ? ");
        }


        return this.jdbcTemplate.queryForObject(sb.toString(), new Object[]{payee}, userRowMapper);
    }

    public Vendor findOneAlternativePayee(String companyCode, String vendorCode, String alternativePayeeCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT PY.C_BPARTNER_ID,           ");
        sb.append("          PY.TAXID,           ");
        sb.append("          PY.VALUE,           ");
        sb.append("          PY.NAME,           ");
        sb.append("          BG.VALUE    AS VENDORGROUP,           ");
        sb.append("          APS.CONFIRMSTATUS          AS CONFIRMSTATUS,           ");
        sb.append("          ''          AS VENDORSTATUS,           ");
        sb.append("          AD.VALUE    AS COMPCODE,           ");
        sb.append("          RL.ISACTIVE AS ISACTIVE           ");
        sb.append("          FROM C_BP_RELATION RL           ");
        sb.append("          INNER JOIN AD_ORG AD           ");
        sb.append("          ON RL.AD_ORG_ID = AD.AD_ORG_ID           ");
        sb.append("          INNER JOIN C_BPARTNER PN           ");
        sb.append("          ON PN.C_BPARTNER_ID = RL.C_BPARTNER_ID           ");
        sb.append("          INNER JOIN C_BPARTNER PY           ");
        sb.append("          ON RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID           ");
        sb.append("          INNER JOIN C_BP_GROUP BG           ");
        sb.append("          ON RL.C_BPARTNERRELATION_ID = PY.C_BPARTNER_ID           ");
        sb.append("          AND PY.C_BP_GROUP_ID = BG.C_BP_GROUP_ID           ");
        sb.append("          INNER JOIN TH_APAlternatePayeeStatus APS ON APS.C_BP_RELATION_ID = RL.C_BP_RELATION_ID           ");
        sb.append("          AND PY.C_BP_GROUP_ID = BG.C_BP_GROUP_ID           ");

        if (!Util.isEmpty(companyCode)) {
            sb.append(" AND AD.VALUE like ? ");
            params.add(companyCode);
        }

        if (!Util.isEmpty(vendorCode)) {
            sb.append(" AND PN.VALUECODE like ? ");
            params.add(vendorCode);
        }

        if (!Util.isEmpty(alternativePayeeCode)) {
            sb.append(" AND PY.VALUECODE like ? ");
            params.add(alternativePayeeCode);
        }


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("objParams : {}", objParams);
        log.info("sql : {}", sb.toString());

        List<Vendor> vendors = this.jdbcTemplate.query(sb.toString(), objParams, userRowMapper);
        if (!vendors.isEmpty()) {
            return vendors.get(0);
        } else {
            return null;
        }

//        return this.jdbcTemplate.queryForObject(sb.toString(), new Object[]{companyCode, vendorCode, alternativePayeeCode}, userRowMapper);
    }

    @Override
    public Vendor findOneByValueCodeForStatus(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append("          SELECT PN.C_BPARTNER_ID,           ");
        sql.append("          PN.TAXID,           ");
        sql.append("          PN.VALUE,           ");
        sql.append("          PN.NAME,           ");
        sql.append("          PN.ISACTIVE,           ");
        sql.append("          BG.VALUE         AS VENDORGROUP,           ");
        sql.append("          ''               AS CONFIRMSTATUS,           ");
        sql.append("          PS.CONFIRMSTATUS AS VENDORSTATUS,           ");
        sql.append("          ''               AS COMPCODE           ");
        sql.append("          FROM C_BPARTNER PN           ");
        sql.append("          LEFT JOIN C_BP_GROUP BG ON PN.C_BP_GROUP_ID = BG.C_BP_GROUP_ID           ");
        sql.append("          LEFT JOIN TH_APBPARTNERSTATUS PS ON PN.C_BPARTNER_ID = PS.C_BPARTNER_ID           ");
        sql.append("          WHERE 1 = 1           ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND PN.VALUE like ? ");
            params.add(valueCode);
        }

        log.info("valueCode {} ", valueCode);
        log.info("sql vendor Status {} ", sql.toString());

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("objParams : {}", objParams);
        log.info("sql : {}", sql.toString());

        List<Vendor> vendors = this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
        if (!vendors.isEmpty()) {
            return vendors.get(0);
        } else {
            return null;
        }

//        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{valueCode}, beanPropertyRowMapper);
    }
}
