package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPaying;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.CompanyPayingRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CompanyPayingRepositoryImpl extends MetadataJdbcRepository<CompanyPaying, Long> implements CompanyPayingRepository {
    static BeanPropertyRowMapper<CompanyPaying> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CompanyPaying.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<CompanyPaying> CompanyPayingUpdater = (t, mapping) -> {
        mapping.put(CompanyPaying.COLUMN_NAME_COMPANY_PAYING_ID, t.getId());
        mapping.put(CompanyPaying.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(CompanyPaying.COLUMN_NAME_COMPANY_NAME, t.getCompanyName());
        mapping.put(CompanyPaying.COLUMN_NAME_MINIMUM_AMOUNT_FOR_PAY, t.getMinimumAmountForPay());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(CompanyPaying.COLUMN_NAME_COMPANY_PAYING_ID, Types.BIGINT),
            entry(CompanyPaying.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
            entry(CompanyPaying.COLUMN_NAME_COMPANY_NAME, Types.NVARCHAR),
            entry(CompanyPaying.COLUMN_NAME_MINIMUM_AMOUNT_FOR_PAY, Types.NVARCHAR)
    );

    static RowMapper<CompanyPaying> userRowMapper = (rs, rowNum) -> new CompanyPaying(
            rs.getLong(CompanyPaying.COLUMN_NAME_COMPANY_PAYING_ID),
            rs.getString(CompanyPaying.COLUMN_NAME_COMPANY_CODE),
            rs.getString(CompanyPaying.COLUMN_NAME_COMPANY_NAME),
            rs.getBigDecimal(CompanyPaying.COLUMN_NAME_MINIMUM_AMOUNT_FOR_PAY)
    );

    public CompanyPayingRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, CompanyPayingUpdater, updaterType, CompanyPaying.TABLE_NAME, CompanyPaying.COLUMN_NAME_COMPANY_PAYING_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CompanyPaying findOneByCompanyCode(String companyCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPaying.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(companyCode)) {
            sql.append(SqlUtil.whereClause(companyCode, CompanyPaying.COLUMN_NAME_COMPANY_CODE, params));
        }

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<CompanyPaying> companyPayeeBankAccountNoConfigs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!companyPayeeBankAccountNoConfigs.isEmpty()) {
            return companyPayeeBankAccountNoConfigs.get(0);
        } else {
            return null;
        }
    }

}
