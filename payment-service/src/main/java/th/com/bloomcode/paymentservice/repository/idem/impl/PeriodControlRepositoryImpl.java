package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.PeriodControl;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.PeriodControlRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class PeriodControlRepositoryImpl extends MetadataJdbcRepository<PeriodControl, Long> implements PeriodControlRepository {
    static BeanPropertyRowMapper<PeriodControl> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PeriodControl.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<PeriodControl> areaUpdater = (t, mapping) -> {
        mapping.put(PeriodControl.COLUMN_NAME_TH_CAPERIODCONTROL_ID, t.getId());
        mapping.put(PeriodControl.COLUMN_NAME_PERIODSTATUS, t.getPeriodNo());
        mapping.put(PeriodControl.COLUMN_NAME_POSTINGKEY_ACCOUNTTYPE, t.getPostingKeyAccountType());
        mapping.put(PeriodControl.COLUMN_NAME_PERIODNO, t.getPeriodNo());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(PeriodControl.COLUMN_NAME_TH_CAPERIODCONTROL_ID, Types.BIGINT),
            entry(PeriodControl.COLUMN_NAME_PERIODSTATUS, Types.NVARCHAR),
            entry(PeriodControl.COLUMN_NAME_POSTINGKEY_ACCOUNTTYPE, Types.NVARCHAR),
            entry(PeriodControl.COLUMN_NAME_PERIODNO, Types.NVARCHAR)
    );

    static RowMapper<PeriodControl> userRowMapper = (rs, rowNum) -> new PeriodControl(
            rs.getLong(PeriodControl.COLUMN_NAME_TH_CAPERIODCONTROL_ID),
            rs.getString(PeriodControl.COLUMN_NAME_PERIODSTATUS),
            rs.getString(PeriodControl.COLUMN_NAME_POSTINGKEY_ACCOUNTTYPE),
            rs.getString(PeriodControl.COLUMN_NAME_PERIODNO)
    );

    @Autowired
    public PeriodControlRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                areaUpdater,
                updaterType,
                PeriodControl.TABLE_NAME,
                PeriodControl.COLUMN_NAME_TH_CAPERIODCONTROL_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<PeriodControl> findAllByCondition(int period,String fiscalYear, String orgValue) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PC.TH_CAPERIODCONTROL_ID, PC.PERIODSTATUS, POSTINGKEY_ACCOUNTTYPE, PERIODNO          ");
        sql.append("          FROM C_PERIOD PR           ");
        sql.append("          LEFT JOIN C_YEAR CY ON PR.C_YEAR_ID = CY.C_YEAR_ID           ");
        sql.append("          LEFT JOIN TH_CAPERIODCONTROL PC ON PR.C_PERIOD_ID = PC.C_PERIOD_ID           ");
        sql.append("          LEFT JOIN TH_CACOMPCODE CC ON PC.TH_CACOMPCODE_ID = CC.TH_CACOMPCODE_ID           ");
        sql.append("          WHERE (PC.POSTINGKEY_ACCOUNTTYPE = 'K' OR PC.POSTINGKEY_ACCOUNTTYPE = 'S')           ");
        sql.append("          AND PC.PERIODSTATUS = 'O'           ");
        sql.append("          AND PC.ISACTIVE = 'Y'           ");


        if (!Util.isEmpty(orgValue)) {
            if(!orgValue.equalsIgnoreCase("0")){
                sql.append(SqlUtil.whereClause(orgValue, "CC.VALUECODE", params));
            }else{
                sql.append("          AND CC.VALUECODE IS NULL           ");
            }

        }
        if (!Util.isEmpty(String.valueOf(period))) {
            params.add(period);
            sql.append("  AND PERIODNO = ? ");
        }
        if (!Util.isEmpty(fiscalYear)) {
            params.add(fiscalYear);
            sql.append("  AND FISCALYEAR = ? ");
        }


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public List<PeriodControl> findAllByCondition(Timestamp currentDate, String orgValue) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PC.TH_CAPERIODCONTROL_ID, PC.PERIODSTATUS, POSTINGKEY_ACCOUNTTYPE, PERIODNO           ");
        sql.append("          FROM C_PERIOD PR           ");
        sql.append("          LEFT JOIN C_YEAR CY ON PR.C_YEAR_ID = CY.C_YEAR_ID           ");
        sql.append("          LEFT JOIN TH_CAPERIODCONTROL PC ON PR.C_PERIOD_ID = PC.C_PERIOD_ID           ");
        sql.append("          LEFT JOIN AD_ORG AD ON PR.AD_ORG_ID = AD.AD_ORG_ID           ");
        sql.append("          WHERE (PC.POSTINGKEY_ACCOUNTTYPE = 'K' OR PC.POSTINGKEY_ACCOUNTTYPE = 'S')           ");
        sql.append("          AND PC.PERIODSTATUS = 'O'           ");
        sql.append("          AND PC.ISACTIVE = 'Y'           ");
        if (!Util.isEmpty(orgValue)) {
            sql.append(SqlUtil.whereClause(orgValue, "AD.VALUE", params));
        }
        if (!Util.isEmpty(currentDate)) {
            params.add(currentDate);
            params.add(currentDate);
            sql.append(" AND PR.STARTDATE <= ? ");
            sql.append(" AND PR.ENDDATE >= ? ");
        }


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }



}