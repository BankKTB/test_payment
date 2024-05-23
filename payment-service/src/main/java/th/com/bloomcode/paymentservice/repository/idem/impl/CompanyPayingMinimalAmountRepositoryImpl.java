package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingMinimalAmount;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.CompanyPayingMinimalAmountRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CompanyPayingMinimalAmountRepositoryImpl extends MetadataJdbcRepository<CompanyPayingMinimalAmount, Long> implements CompanyPayingMinimalAmountRepository {
    static BeanPropertyRowMapper<CompanyPayingMinimalAmount> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CompanyPayingMinimalAmount.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<CompanyPayingMinimalAmount> documentTypeUpdater = (t, mapping) -> {
        mapping.put(CompanyPayingMinimalAmount.COLUMN_NAME_TH_APMINAMTCONTROL_ID, t.getId());
        mapping.put(CompanyPayingMinimalAmount.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(CompanyPayingMinimalAmount.COLUMN_NAME_MINIMAL_AMOUNT, t.getMiniMalAmount());

    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(CompanyPayingMinimalAmount.COLUMN_NAME_TH_APMINAMTCONTROL_ID, Types.BIGINT),
            entry(CompanyPayingMinimalAmount.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
            entry(CompanyPayingMinimalAmount.COLUMN_NAME_MINIMAL_AMOUNT, Types.BIGINT)
    );

    static RowMapper<CompanyPayingMinimalAmount> userRowMapper = (rs, rowNum) -> new CompanyPayingMinimalAmount(
            rs.getLong(CompanyPayingMinimalAmount.COLUMN_NAME_TH_APMINAMTCONTROL_ID),
            rs.getString(CompanyPayingMinimalAmount.COLUMN_NAME_COMPANY_CODE),
            rs.getBigDecimal(CompanyPayingMinimalAmount.COLUMN_NAME_MINIMAL_AMOUNT)
    );

    @Autowired
    public CompanyPayingMinimalAmountRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                CompanyPayingMinimalAmount.TABLE_NAME,
                CompanyPayingMinimalAmount.COLUMN_NAME_TH_APMINAMTCONTROL_ID
                ,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT COUNT(1) ");
        sql.append("          FROM TH_APMINAMTCONTROL AP           ");
        sql.append("          LEFT JOIN TH_CACOMPCODE CA ON AP.TH_CACOMPCODE_ID = CA.TH_CACOMPCODE_ID           ");

        sql.append("          WHERE CA.ISACTIVE = 'Y'           ");
        sql.append("          AND AP.ISACTIVE = 'Y'           ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClause(valueCode, "CA.VALUECODE", params));
        }

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<CompanyPayingMinimalAmount> findAllByValueCode(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT TH_APMINAMTCONTROL_ID,           ");
        sql.append("          CA.VALUECODE  AS COMPANY_CODE,           ");
        sql.append("          AP.MINIMUMAMT AS MINIMAL_AMOUNT           ");
        sql.append("          FROM TH_APMINAMTCONTROL AP           ");
        sql.append("          LEFT JOIN TH_CACOMPCODE CA ON AP.TH_CACOMPCODE_ID = CA.TH_CACOMPCODE_ID           ");

        sql.append("          WHERE CA.ISACTIVE = 'Y'           ");
        sql.append("          AND AP.ISACTIVE = 'Y'           ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(SqlUtil.whereClause(valueCode, "CA.VALUECODE", params));
        }

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public CompanyPayingMinimalAmount findOneByValueCode(String valueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT TH_APMINAMTCONTROL_ID,           ");
        sql.append("          CA.VALUECODE  AS COMPANY_CODE,           ");
        sql.append("          AP.MINIMUMAMT AS MINIMAL_AMOUNT           ");
        sql.append("          FROM TH_APMINAMTCONTROL AP           ");
        sql.append("          LEFT JOIN TH_CACOMPCODE CA ON AP.TH_CACOMPCODE_ID = CA.TH_CACOMPCODE_ID           ");

        sql.append("          WHERE CA.ISACTIVE = 'Y'           ");
        sql.append("          AND AP.ISACTIVE = 'Y'           ");

        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND CA.VALUECODE like ? ");
        }
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{valueCode}, userRowMapper);
    }
}
