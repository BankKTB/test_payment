package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingPayMethodConfig;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.CompanyPayingPayMethodConfigRepository;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayingService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CompanyPayingPayMethodConfigRepositoryImpl extends MetadataJdbcRepository<CompanyPayingPayMethodConfig, Long> implements CompanyPayingPayMethodConfigRepository {
    static BeanPropertyRowMapper<CompanyPayingPayMethodConfig> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CompanyPayingPayMethodConfig.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<CompanyPayingPayMethodConfig> CompanyPayingPayMethodConfigUpdater = (t, mapping) -> {
        mapping.put(CompanyPayingPayMethodConfig.COLUMN_NAME_COMPANY_PAYING_PAY_METHOD_CONFIG_ID, t.getId());
        mapping.put(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_BANK_ANOTHER_COUNTRY, t.isAllowedBankAnotherCountry());
        mapping.put(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_CURRENCY_ANOTHER_COUNTRY, t.isAllowedCurrencyAnotherCountry());
        mapping.put(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_PARTNER_ANOTHER_COUNTRY, t.isAllowedPartnerAnotherCountry());
        mapping.put(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_SINGLE_PAYMENT, t.isAllowedSinglePayment());
        mapping.put(CompanyPayingPayMethodConfig.COLUMN_NAME_COMPANY_PAYING_ID, t.getCompanyPayingId());
        mapping.put(CompanyPayingPayMethodConfig.COLUMN_NAME_MAXIMUM_AMOUNT, t.getMaximumAmount());
        mapping.put(CompanyPayingPayMethodConfig.COLUMN_NAME_MINIMUM_AMOUNT, t.getMinimumAmount());
        mapping.put(CompanyPayingPayMethodConfig.COLUMN_NAME_PAY_METHOD, t.getPayMethod());
        mapping.put(CompanyPayingPayMethodConfig.COLUMN_NAME_PAY_METHOD_NAME, t.getPayMethodName());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(CompanyPayingPayMethodConfig.COLUMN_NAME_COMPANY_PAYING_PAY_METHOD_CONFIG_ID, Types.BIGINT),
            entry(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_BANK_ANOTHER_COUNTRY, Types.BOOLEAN),
            entry(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_CURRENCY_ANOTHER_COUNTRY, Types.BOOLEAN),
            entry(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_PARTNER_ANOTHER_COUNTRY, Types.BOOLEAN),
            entry(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_SINGLE_PAYMENT, Types.BOOLEAN),
            entry(CompanyPayingPayMethodConfig.COLUMN_NAME_COMPANY_PAYING_ID, Types.BIGINT),
            entry(CompanyPayingPayMethodConfig.COLUMN_NAME_MAXIMUM_AMOUNT, Types.NUMERIC),
            entry(CompanyPayingPayMethodConfig.COLUMN_NAME_MINIMUM_AMOUNT, Types.NUMERIC),
            entry(CompanyPayingPayMethodConfig.COLUMN_NAME_PAY_METHOD, Types.NVARCHAR),
            entry(CompanyPayingPayMethodConfig.COLUMN_NAME_PAY_METHOD_NAME, Types.NVARCHAR)
    );

    static RowMapper<CompanyPayingPayMethodConfig> userRowMapper = (rs, rowNum) -> new CompanyPayingPayMethodConfig(
            rs.getLong(CompanyPayingPayMethodConfig.COLUMN_NAME_COMPANY_PAYING_PAY_METHOD_CONFIG_ID),
            rs.getBoolean(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_BANK_ANOTHER_COUNTRY),
            rs.getBoolean(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_CURRENCY_ANOTHER_COUNTRY),
            rs.getBoolean(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_PARTNER_ANOTHER_COUNTRY),
            rs.getBoolean(CompanyPayingPayMethodConfig.COLUMN_NAME_ALLOWED_SINGLE_PAYMENT),
            rs.getLong(CompanyPayingPayMethodConfig.COLUMN_NAME_COMPANY_PAYING_ID),
            rs.getBigDecimal(CompanyPayingPayMethodConfig.COLUMN_NAME_MAXIMUM_AMOUNT),
            rs.getBigDecimal(CompanyPayingPayMethodConfig.COLUMN_NAME_MINIMUM_AMOUNT),
            rs.getString(CompanyPayingPayMethodConfig.COLUMN_NAME_PAY_METHOD),
            rs.getString(CompanyPayingPayMethodConfig.COLUMN_NAME_PAY_METHOD_NAME)
    );

    public CompanyPayingPayMethodConfigRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, CompanyPayingService companyPayingService) {
        super(userRowMapper, CompanyPayingPayMethodConfigUpdater, updaterType, CompanyPayingPayMethodConfig.TABLE_NAME, CompanyPayingPayMethodConfig.COLUMN_NAME_COMPANY_PAYING_PAY_METHOD_CONFIG_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CompanyPayingPayMethodConfig> findAllByCompanyPayingIdOrderByPayMethodAsc(Long companyPayingId) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPayingPayMethodConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(companyPayingId.toString())) {
            sql.append(SqlUtil.whereClause(companyPayingId.toString(), CompanyPayingPayMethodConfig.COLUMN_NAME_COMPANY_PAYING_ID, params));
        }
        sql.append(" ORDER BY PAY_METHOD ASC ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public CompanyPayingPayMethodConfig findOneByCompanyPayingIdAndPayMethod(Long companyPayingId, String payMethod) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPayingPayMethodConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(payMethod)) {
            sql.append(SqlUtil.whereClause(payMethod, CompanyPayingPayMethodConfig.COLUMN_NAME_PAY_METHOD, params));
        }
        if (!Util.isEmpty(companyPayingId.toString())) {
            sql.append(SqlUtil.whereClause(companyPayingId.toString(), CompanyPayingPayMethodConfig.COLUMN_NAME_COMPANY_PAYING_ID, params));
        }
        if (!Util.isEmpty(payMethod)) {
            sql.append(SqlUtil.whereClause(payMethod, CompanyPayingPayMethodConfig.COLUMN_NAME_PAY_METHOD, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<CompanyPayingPayMethodConfig> companyPayingPayMethodConfigs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!companyPayingPayMethodConfigs.isEmpty()) {
            return companyPayingPayMethodConfigs.get(0);
        } else {
            return null;
        }
    }
}
