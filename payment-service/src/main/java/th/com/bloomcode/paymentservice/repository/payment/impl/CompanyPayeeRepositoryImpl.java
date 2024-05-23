package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayee;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.CompanyPayeeRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CompanyPayeeRepositoryImpl  extends MetadataJdbcRepository<CompanyPayee, Long> implements CompanyPayeeRepository {
    static BeanPropertyRowMapper<CompanyPayee> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CompanyPayee.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<CompanyPayee> CompanyPayeeUpdater = (t, mapping) -> {
        mapping.put(CompanyPayee.COLUMN_NAME_COMPANY_PAYEE_ID, t.getId());
        mapping.put(CompanyPayee.COLUMN_NAME_AMOUNT_DUE_DATE, t.getAmountDueDate());
        mapping.put(CompanyPayee.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(CompanyPayee.COLUMN_NAME_COMPANY_NAME, t.getCompanyName());
        mapping.put(CompanyPayee.COLUMN_NAME_COMPANY_PAYEE_CODE, t.getCompanyPayeeCode());
        mapping.put(CompanyPayee.COLUMN_NAME_COMPANY_PAYING_CODE, t.getCompanyPayingCode());
        mapping.put(CompanyPayee.COLUMN_NAME_PAYMENT_METHOD, t.getPaymentMethod());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(CompanyPayee.COLUMN_NAME_COMPANY_PAYEE_ID, Types.BIGINT),
            entry(CompanyPayee.COLUMN_NAME_AMOUNT_DUE_DATE, Types.INTEGER),
            entry(CompanyPayee.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
            entry(CompanyPayee.COLUMN_NAME_COMPANY_NAME, Types.NVARCHAR),
            entry(CompanyPayee.COLUMN_NAME_COMPANY_PAYEE_CODE, Types.NVARCHAR),
            entry(CompanyPayee.COLUMN_NAME_COMPANY_PAYING_CODE, Types.NVARCHAR),
            entry(CompanyPayee.COLUMN_NAME_PAYMENT_METHOD, Types.NVARCHAR)
    );

    static RowMapper<CompanyPayee> userRowMapper = (rs, rowNum) -> new CompanyPayee(
            rs.getLong(CompanyPayee.COLUMN_NAME_COMPANY_PAYEE_ID),
            rs.getInt(CompanyPayee.COLUMN_NAME_AMOUNT_DUE_DATE),
            rs.getString(CompanyPayee.COLUMN_NAME_COMPANY_CODE),
            rs.getString(CompanyPayee.COLUMN_NAME_COMPANY_NAME),
            rs.getString(CompanyPayee.COLUMN_NAME_COMPANY_PAYEE_CODE),
            rs.getString(CompanyPayee.COLUMN_NAME_COMPANY_PAYING_CODE),
            rs.getString(CompanyPayee.COLUMN_NAME_PAYMENT_METHOD)
    );

    public CompanyPayeeRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, CompanyPayeeUpdater, updaterType, CompanyPayee.TABLE_NAME, CompanyPayee.COLUMN_NAME_COMPANY_PAYEE_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CompanyPayee findOneByCompanyCode(String companyCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPayee.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(companyCode)) {
            sql.append(SqlUtil.whereClause(companyCode, CompanyPayee.COLUMN_NAME_COMPANY_CODE, params));
        }

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<CompanyPayee> companyPayeeBankAccountNoConfigs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!companyPayeeBankAccountNoConfigs.isEmpty()) {
            return companyPayeeBankAccountNoConfigs.get(0);
        } else {
            return null;
        }
    }

}
