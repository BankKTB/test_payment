package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayee;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayeeHouseBankKeyConfig;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileAlias;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.CompanyPayeeHouseBankKeyConfigRepository;
import th.com.bloomcode.paymentservice.service.idem.HouseBankService;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayeeService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CompanyPayeeHouseBankKeyConfigRepositoryImpl extends MetadataJdbcRepository<CompanyPayeeHouseBankKeyConfig, Long>  implements CompanyPayeeHouseBankKeyConfigRepository {
    static BeanPropertyRowMapper<CompanyPayeeHouseBankKeyConfig> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CompanyPayeeHouseBankKeyConfig.class);
    private final JdbcTemplate jdbcTemplate;
    private static CompanyPayeeService companyPayeeService;

    static MetadataJdbcRepository.Updater<CompanyPayeeHouseBankKeyConfig> CompanyPayeeHouseBankKeyConfigUpdater = (t, mapping) -> {
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG_ID, t.getId());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS1, t.getAddress1());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS2, t.getAddress2());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS3, t.getAddress3());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS4, t.getAddress4());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS5, t.getAddress5());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_BANK_BRANCH, t.getBankBranch());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_BANK_NAME, t.getBankName());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_CITY, t.getCity());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_ID, t.getCompanyPayeeId());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COUNTRY, t.getCountry());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COUNTRY_NAME_EN, t.getCountryNameEn());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_HOUSE_BANK_KEY, t.getHouseBankKey());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_SWIFT_CODE, t.getSwiftCode());
        mapping.put(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ZIP_CODE, t.getZipCode());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG_ID, Types.BIGINT),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS1, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS2, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS3, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS4, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS5, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_BANK_BRANCH, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_BANK_NAME, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_CITY, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_ID, Types.BIGINT),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COUNTRY, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COUNTRY_NAME_EN, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_HOUSE_BANK_KEY, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_SWIFT_CODE, Types.NVARCHAR),
            entry(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ZIP_CODE, Types.NVARCHAR)
    );

    static RowMapper<CompanyPayeeHouseBankKeyConfig> userRowMapper = (rs, rowNum) -> new CompanyPayeeHouseBankKeyConfig(
            rs.getLong(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG_ID),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS1),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS2),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS3),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS4),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ADDRESS5),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_BANK_BRANCH),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_BANK_NAME),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_CITY),
            rs.getLong(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_ID),
            CompanyPayeeHouseBankKeyConfigRepositoryImpl.companyPayeeService.findOneById(rs.getLong(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_ID)),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COUNTRY),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COUNTRY_NAME_EN),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_HOUSE_BANK_KEY),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_SWIFT_CODE),
            rs.getString(CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_ZIP_CODE)
    );

    public CompanyPayeeHouseBankKeyConfigRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, HouseBankService houseBankService) {
        super(userRowMapper, CompanyPayeeHouseBankKeyConfigUpdater, updaterType, CompanyPayeeHouseBankKeyConfig.TABLE_NAME, CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
         CompanyPayeeHouseBankKeyConfigRepositoryImpl.companyPayeeService = companyPayeeService;
    }

    @Override
    public List<CompanyPayeeHouseBankKeyConfig> findAllByCompanyPayeeIdOrderByHouseBankKeyAscCountryAscBankBranchAsc(Long companyPayeeId) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPayeeHouseBankKeyConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(companyPayeeId.toString())) {
            sql.append(SqlUtil.whereClause(companyPayeeId.toString(), CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_ID, params));
        }
        sql.append("          ORDER BY HOUSE_BANK_KEY ASC, COLUMN_NAME_COUNTRY ASC           ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<CompanyPayeeHouseBankKeyConfig> findAllByHouseBankKeyAndCountryAndBankBranchAndCompanyPayeeId(String houseBankKey, String country,
                            String bankBranch, Long companyPayeeId) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPayeeHouseBankKeyConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(houseBankKey)) {
            sql.append(SqlUtil.whereClause(houseBankKey, CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_HOUSE_BANK_KEY, params));
        }
        if (!Util.isEmpty(country)) {
            sql.append(SqlUtil.whereClause(country, CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COUNTRY, params));
        }
        if (!Util.isEmpty(bankBranch)) {
            sql.append(SqlUtil.whereClause(bankBranch, CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_BANK_BRANCH, params));
        }
        if (!Util.isEmpty(companyPayeeId.toString())) {
            sql.append(SqlUtil.whereClause(companyPayeeId.toString(), CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_ID, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public CompanyPayeeHouseBankKeyConfig findOneByHouseBankKeyAndCountryAndBankBranchAndCompanyPayeeId(String houseBankKey, String country,
                                                                                                              String bankBranch, Long companyPayeeId) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPayeeHouseBankKeyConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(houseBankKey)) {
            sql.append(SqlUtil.whereClause(houseBankKey, CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_HOUSE_BANK_KEY, params));
        }
        if (!Util.isEmpty(country)) {
            sql.append(SqlUtil.whereClause(country, CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COUNTRY, params));
        }
        if (!Util.isEmpty(bankBranch)) {
            sql.append(SqlUtil.whereClause(bankBranch, CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_BANK_BRANCH, params));
        }
        if (!Util.isEmpty(companyPayeeId.toString())) {
            sql.append(SqlUtil.whereClause(companyPayeeId.toString(), CompanyPayeeHouseBankKeyConfig.COLUMN_NAME_COMPANY_PAYEE_ID, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<CompanyPayeeHouseBankKeyConfig> companyPayeeBankAccountNoConfigs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!companyPayeeBankAccountNoConfigs.isEmpty()) {
            return companyPayeeBankAccountNoConfigs.get(0);
        } else {
            return null;
        }
    }
}
