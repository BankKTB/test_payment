package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.PaymentCenter;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.PaymentCenterRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class PaymentCenterRepositoryImpl extends MetadataJdbcRepository<PaymentCenter, Long> implements PaymentCenterRepository {
    static BeanPropertyRowMapper<PaymentCenter> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PaymentCenter.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<PaymentCenter> documentTypeUpdater = (t, mapping) -> {
        mapping.put(PaymentCenter.COLUMN_NAME_TH_BGPAYMENTCENTER_ID, t.getId());
        mapping.put(PaymentCenter.COLUMN_NAME_VALUECODE, t.getValueCode());
        mapping.put(PaymentCenter.COLUMN_NAME_NAME, t.getName());
        mapping.put(PaymentCenter.COLUMN_NAME_DESCRIPTION, t.getDescription());
        mapping.put(PaymentCenter.COLUMN_NAME_ISACTIVE, t.getIsActive());
        mapping.put(PaymentCenter.COLUMN_NAME_TH_BGBUDGETAREA_ID, t.getBudgetAreaId());
        mapping.put(PaymentCenter.COLUMN_NAME_TH_CACOMPCODE_ID, t.getCompanyCodeId());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(PaymentCenter.COLUMN_NAME_TH_BGPAYMENTCENTER_ID, Types.BIGINT),
            entry(PaymentCenter.COLUMN_NAME_VALUECODE, Types.NVARCHAR),
            entry(PaymentCenter.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(PaymentCenter.COLUMN_NAME_DESCRIPTION, Types.NVARCHAR),
            entry(PaymentCenter.COLUMN_NAME_ISACTIVE, Types.NVARCHAR),
            entry(PaymentCenter.COLUMN_NAME_TH_BGBUDGETAREA_ID, Types.BIGINT),
            entry(PaymentCenter.COLUMN_NAME_TH_CACOMPCODE_ID, Types.BIGINT)
    );

    static RowMapper<PaymentCenter> userRowMapper = (rs, rowNum) -> new PaymentCenter(
            rs.getLong(PaymentCenter.COLUMN_NAME_TH_BGPAYMENTCENTER_ID),
            rs.getString(PaymentCenter.COLUMN_NAME_VALUECODE),
            rs.getString(PaymentCenter.COLUMN_NAME_NAME),
            rs.getString(PaymentCenter.COLUMN_NAME_DESCRIPTION),
            rs.getString(PaymentCenter.COLUMN_NAME_ISACTIVE),
            rs.getLong(PaymentCenter.COLUMN_NAME_TH_BGPAYMENTCENTER_ID),
            rs.getLong(PaymentCenter.COLUMN_NAME_TH_BGPAYMENTCENTER_ID),
            rs.getString(PaymentCenter.COLUMN_NAME_AREA),
            rs.getString(PaymentCenter.COLUMN_NAME_COMPANY_CODE)
    );

    @Autowired
    public PaymentCenterRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                PaymentCenter.TABLE_NAME,
                PaymentCenter.COLUMN_NAME_TH_BGPAYMENTCENTER_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) ");
        sql.append(" FROM TH_BGPAYMENTCENTER PC ");
        sql.append(" LEFT JOIN TH_BGBUDGETAREA BA ON PC.TH_BGBUDGETAREA_ID = BA.TH_BGBUDGETAREA_ID ");
        sql.append(" LEFT JOIN TH_CACOMPCODE CC ON PC.TH_CACOMPCODE_ID = CC.TH_CACOMPCODE_ID ");
        sql.append(" WHERE PC.ISACTIVE = 'Y' ");
        sql.append(" AND ");
        sql.append(" BA.ISACTIVE = 'Y' ");
        sql.append(" AND ");
        sql.append(" CC.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "PC.VALUECODE", "PC.NAME", "PC.DESCRIPTION"));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<PaymentCenter> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" PC.*, BA.VALUECODE AS AREA, CC.VALUECODE AS COMPANY_CODE ");
        sql.append(" FROM TH_BGPAYMENTCENTER PC ");
        sql.append(" LEFT JOIN TH_BGBUDGETAREA BA ON PC.TH_BGBUDGETAREA_ID = BA.TH_BGBUDGETAREA_ID ");
        sql.append(" LEFT JOIN TH_CACOMPCODE CC ON PC.TH_CACOMPCODE_ID = CC.TH_CACOMPCODE_ID ");
        sql.append(" WHERE PC.ISACTIVE = 'Y' ");
        sql.append(" AND ");
        sql.append(" BA.ISACTIVE = 'Y' ");
        sql.append(" AND ");
        sql.append(" CC.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClauseOr(valueCode, params, "PC.VALUECODE", "PC.NAME", "PC.DESCRIPTION"));
        }
        sql.append(" ORDER BY ");
        sql.append(" PC.VALUECODE ASC");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public PaymentCenter findOneByValueCode(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" PC.*, BA.VALUECODE AS AREA, CC.VALUECODE AS COMPANY_CODE ");
        sql.append(" FROM TH_BGPAYMENTCENTER PC ");
        sql.append(" LEFT JOIN TH_BGBUDGETAREA BA ON PC.TH_BGBUDGETAREA_ID = BA.TH_BGBUDGETAREA_ID ");
        sql.append(" LEFT JOIN TH_CACOMPCODE CC ON PC.TH_CACOMPCODE_ID = CC.TH_CACOMPCODE_ID ");
        sql.append(" WHERE PC.ISACTIVE = 'Y' ");
        sql.append(" AND ");
        sql.append(" BA.ISACTIVE = 'Y' ");
        sql.append(" AND ");
        sql.append(" CC.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND PC.VALUECODE like ? ");
        }
        List<PaymentCenter> paymentCenters = this.jdbcTemplate.query(sql.toString(), new Object[] { valueCode }, userRowMapper);
        if (!paymentCenters.isEmpty()) {
            return paymentCenters.get(0);
        } else {
            return null;
        }
    }
}
