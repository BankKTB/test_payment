package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.*;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingBankConfig;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingBankConfig;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.CompanyPayingBankConfigRepository;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayingService;
import th.com.bloomcode.paymentservice.service.payment.PaymentAliasService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CompanyPayingBankConfigRepositoryImpl  extends MetadataJdbcRepository<CompanyPayingBankConfig, Long> implements CompanyPayingBankConfigRepository {
    static BeanPropertyRowMapper<CompanyPayingBankConfig> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CompanyPayingBankConfig.class);
    private final JdbcTemplate jdbcTemplate;
    private static CompanyPayingService companyPayingService;


    static Updater<CompanyPayingBankConfig> CompanyPayingBankConfigUpdater = (t, mapping) -> {
        mapping.put(CompanyPayingBankConfig.COLUMN_NAME_COMPANY_PAYING_BANK_CONFIG_ID, t.getId());
        mapping.put(CompanyPayingBankConfig.COLUMN_NAME_ACCOUNT_CODE, t.getAccountCode());
        mapping.put(CompanyPayingBankConfig.COLUMN_NAME_COMPANY_PAYING_ID, t.getCompanyPayingId());
        mapping.put(CompanyPayingBankConfig.COLUMN_NAME_CURRENCY, t.getCurrency());
        mapping.put(CompanyPayingBankConfig.COLUMN_NAME_GL_ACCOUNT, t.getGlAccount());
        mapping.put(CompanyPayingBankConfig.COLUMN_NAME_HOUSE_BANK_KEY, t.getHouseBankKey());
        mapping.put(CompanyPayingBankConfig.COLUMN_NAME_PAY_METHOD, t.getPayMethod());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(CompanyPayingBankConfig.COLUMN_NAME_COMPANY_PAYING_BANK_CONFIG_ID, Types.BIGINT),
            entry(CompanyPayingBankConfig.COLUMN_NAME_ACCOUNT_CODE, Types.NVARCHAR),
            entry(CompanyPayingBankConfig.COLUMN_NAME_COMPANY_PAYING_ID, Types.BIGINT),
            entry(CompanyPayingBankConfig.COLUMN_NAME_CURRENCY, Types.NVARCHAR),
            entry(CompanyPayingBankConfig.COLUMN_NAME_GL_ACCOUNT, Types.NVARCHAR),
            entry(CompanyPayingBankConfig.COLUMN_NAME_HOUSE_BANK_KEY, Types.NVARCHAR),
            entry(CompanyPayingBankConfig.COLUMN_NAME_PAY_METHOD, Types.NVARCHAR)
    );

    static RowMapper<CompanyPayingBankConfig> userRowMapper = (rs, rowNum) -> new CompanyPayingBankConfig(
            rs.getLong(CompanyPayingBankConfig.COLUMN_NAME_COMPANY_PAYING_BANK_CONFIG_ID),
            rs.getString(CompanyPayingBankConfig.COLUMN_NAME_ACCOUNT_CODE),
            rs.getLong(CompanyPayingBankConfig.COLUMN_NAME_COMPANY_PAYING_ID),
            CompanyPayingBankConfigRepositoryImpl.companyPayingService.findOneById(rs.getLong(CompanyPayingBankConfig.COLUMN_NAME_COMPANY_PAYING_ID)),
            rs.getString(CompanyPayingBankConfig.COLUMN_NAME_CURRENCY),
            rs.getString(CompanyPayingBankConfig.COLUMN_NAME_GL_ACCOUNT),
            rs.getString(CompanyPayingBankConfig.COLUMN_NAME_HOUSE_BANK_KEY),
            rs.getString(CompanyPayingBankConfig.COLUMN_NAME_PAY_METHOD)
    );

    public CompanyPayingBankConfigRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, CompanyPayingService companyPayingService) {
        super(userRowMapper, CompanyPayingBankConfigUpdater, updaterType, CompanyPayingBankConfig.TABLE_NAME, CompanyPayingBankConfig.COLUMN_NAME_COMPANY_PAYING_BANK_CONFIG_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        CompanyPayingBankConfigRepositoryImpl.companyPayingService = companyPayingService;
    }

    @Override
    public CompanyPayingBankConfig findOneByPayMethodAndCompanyPayingId(String payMethod, Long CompanyPayingId) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPayingBankConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(payMethod)) {
            sql.append(SqlUtil.whereClause(payMethod, CompanyPayingBankConfig.COLUMN_NAME_PAY_METHOD, params));
        }
        if (!Util.isEmpty(CompanyPayingId.toString())) {
            sql.append(SqlUtil.whereClause(CompanyPayingId.toString(), CompanyPayingBankConfig.COLUMN_NAME_COMPANY_PAYING_ID, params));
        }

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<CompanyPayingBankConfig> companyPayeeBankAccountNoConfigs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!companyPayeeBankAccountNoConfigs.isEmpty()) {
            return companyPayeeBankAccountNoConfigs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<CompanyPayingBankConfig> findAllByCompanyPayingIdOrderByPayMethodAsc(Long companyPayingId) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPayingBankConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(companyPayingId.toString())) {
            sql.append(SqlUtil.whereClause(companyPayingId.toString(), CompanyPayingBankConfig.COLUMN_NAME_COMPANY_PAYING_ID, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);

    }
}
