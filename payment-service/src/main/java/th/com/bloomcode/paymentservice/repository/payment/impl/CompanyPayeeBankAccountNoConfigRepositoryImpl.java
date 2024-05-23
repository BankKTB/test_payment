package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayeeBankAccountNoConfig;
import th.com.bloomcode.paymentservice.model.payment.OmDisplayColumnTable;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.CompanyPayeeBankAccountNoConfigRepository;
import th.com.bloomcode.paymentservice.service.idem.HouseBankService;
import th.com.bloomcode.paymentservice.service.payment.PaymentAliasService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.Query;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CompanyPayeeBankAccountNoConfigRepositoryImpl extends MetadataJdbcRepository<CompanyPayeeBankAccountNoConfig, Long> implements CompanyPayeeBankAccountNoConfigRepository {
    static BeanPropertyRowMapper<CompanyPayeeBankAccountNoConfig> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CompanyPayeeBankAccountNoConfig.class);
    private final JdbcTemplate jdbcTemplate;
    private static HouseBankService houseBankService;

    static MetadataJdbcRepository.Updater<CompanyPayeeBankAccountNoConfig> CompanyPayeeBankAccountNoConfigUpdater = (t, mapping) -> {
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG_ID, t.getId());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ACCOUNT_CODE, t.getAccountCode());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ACCOUNT_DESCRIPTION, t.getAccountDescription());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS1, t.getAddress1());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS2, t.getAddress2());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS3, t.getAddress3());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS4, t.getAddress4());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS5, t.getAddress5());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_ACCOUNT_NO, t.getBankAccountNo());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_ACCOUNT_NO_ETC, t.getBankAccountNoEtc());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_BRANCH, t.getBankBranch());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_NAME, t.getBankName());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_CITY, t.getCity());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_COUNTRY, t.getCountry());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_COUNTRY_NAME_EN, t.getCountryNameEn());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_CURRENCY, t.getCurrency());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_GL_ACCOUNT, t.getGlAccount());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_HOUSE_BANK_KEY, t.getHouseBankKey());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_HOUSE_BANK_KEY_ID, t.getHouseBankKeyId());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_KEY_CONTROL, t.getKeyControl());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_SWIFT_CODE, t.getSwiftCode());
        mapping.put(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ZIP_CODE, t.getZipCode());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG_ID, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ACCOUNT_CODE, Types.BIGINT),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ACCOUNT_DESCRIPTION, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS1, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS2, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS3, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS4, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS5, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_ACCOUNT_NO, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_ACCOUNT_NO_ETC, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_BRANCH, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_NAME, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_CITY, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_COUNTRY, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_COUNTRY_NAME_EN, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_CURRENCY, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_GL_ACCOUNT, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_HOUSE_BANK_KEY, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_HOUSE_BANK_KEY_ID, Types.BIGINT),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_KEY_CONTROL, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_SWIFT_CODE, Types.NVARCHAR),
            entry(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ZIP_CODE, Types.NVARCHAR)
    );

    static RowMapper<CompanyPayeeBankAccountNoConfig> userRowMapper = (rs, rowNum) -> new CompanyPayeeBankAccountNoConfig(
            rs.getLong(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG_ID),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ACCOUNT_CODE),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ACCOUNT_DESCRIPTION),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS1),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS2),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS3),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS4),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ADDRESS5),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_ACCOUNT_NO),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_ACCOUNT_NO_ETC),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_BRANCH),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_BANK_NAME),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_CITY),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_COUNTRY),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_COUNTRY_NAME_EN),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_CURRENCY),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_GL_ACCOUNT),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_HOUSE_BANK_KEY),
            rs.getLong(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_HOUSE_BANK_KEY_ID),
            CompanyPayeeBankAccountNoConfigRepositoryImpl.houseBankService.findOneById(rs.getLong(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_HOUSE_BANK_KEY_ID)),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_SWIFT_CODE),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_KEY_CONTROL),
            rs.getString(CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ZIP_CODE)
    );

    public CompanyPayeeBankAccountNoConfigRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, HouseBankService houseBankService) {
        super(userRowMapper, CompanyPayeeBankAccountNoConfigUpdater, updaterType, CompanyPayeeBankAccountNoConfig.TABLE_NAME, CompanyPayeeBankAccountNoConfig.COLUMN_NAME_COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        CompanyPayeeBankAccountNoConfigRepositoryImpl.houseBankService = houseBankService;
    }


    @Override
    public CompanyPayeeBankAccountNoConfig findOneByAccountCode(String accountCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPayeeBankAccountNoConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");

        if (!Util.isEmpty(accountCode)) {
            sql.append(SqlUtil.whereClause(accountCode, CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ACCOUNT_CODE, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        List<CompanyPayeeBankAccountNoConfig> companyPayeeBankAccountNoConfigs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!companyPayeeBankAccountNoConfigs.isEmpty()) {
            return companyPayeeBankAccountNoConfigs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<CompanyPayeeBankAccountNoConfig> findAllByHouseBankKeyIdOrderByAccountCodeAsc(String houseBankKeyId) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(CompanyPayeeBankAccountNoConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(houseBankKeyId)) {
            sql.append(SqlUtil.whereClause(houseBankKeyId, CompanyPayeeBankAccountNoConfig.COLUMN_NAME_ACCOUNT_CODE, params));
        }
        sql.append("          ORDER BY ACCOUNT_CODE ASC           ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public void deleteAllByHouseBankKeyId(Long houseBankKeyId) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE ");
        sql.append(" FROM ");
        sql.append(" COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG P");
        sql.append(" WHERE ");
        sql.append(" P.HOUSE_BANK_KEY_ID  = ? ");
        this.jdbcTemplate.update(sql.toString(), houseBankKeyId);
    }

}
