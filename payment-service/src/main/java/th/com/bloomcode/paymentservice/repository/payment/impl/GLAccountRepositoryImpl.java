package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.GLAccount;
import th.com.bloomcode.paymentservice.model.payment.GLAccount;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.GLAccountRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class GLAccountRepositoryImpl extends MetadataJdbcRepository<GLAccount, Long>  implements GLAccountRepository {
    static BeanPropertyRowMapper<GLAccount> beanPropertyRowMapper = new BeanPropertyRowMapper<>(GLAccount.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<GLAccount> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(GLAccount.COLUMN_NAME_GL_ACCOUNT_ID, t.getId());
        mapping.put(GLAccount.COLUMN_NAME_CENTRALGLACCOUNT, t.getCentralGlAccount());
        mapping.put(GLAccount.COLUMN_NAME_CENTRALGLACCOUNT_DESCRIPTION, t.getCentralGlAccountDescription());
        mapping.put(GLAccount.COLUMN_NAME_CENTRAL_POSTING, t.getCentralPosting());
        mapping.put(GLAccount.COLUMN_NAME_AGENCYGLACCOUNT, t.getAgencyGlAccount());
        mapping.put(GLAccount.COLUMN_NAME_AGENCYGLACCOUNT_DESCRIPTION, t.getAgencyGlAccountDescription());
        mapping.put(GLAccount.COLUMN_NAME_AGENCY_POSTING, t.getAgencyPosting());
        mapping.put(GLAccount.COLUMN_NAME_DOC_TYPE, t.getDocumentType());
        mapping.put(GLAccount.COLUMN_NAME_DR_CR, t.getDrCr());
        mapping.put(GLAccount.COLUMN_NAME_FUND_SOURCE, t.getFundSource());
        mapping.put(GLAccount.COLUMN_NAME_METHOD, t.getMethod());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(GLAccount.COLUMN_NAME_GL_ACCOUNT_ID, Types.BIGINT),
            entry(GLAccount.COLUMN_NAME_CENTRALGLACCOUNT, Types.NVARCHAR),
            entry(GLAccount.COLUMN_NAME_CENTRALGLACCOUNT_DESCRIPTION, Types.NVARCHAR),
            entry(GLAccount.COLUMN_NAME_CENTRAL_POSTING, Types.NVARCHAR),
            entry(GLAccount.COLUMN_NAME_AGENCYGLACCOUNT, Types.NVARCHAR),
            entry(GLAccount.COLUMN_NAME_AGENCYGLACCOUNT_DESCRIPTION, Types.NVARCHAR),
            entry(GLAccount.COLUMN_NAME_AGENCY_POSTING, Types.NVARCHAR),
            entry(GLAccount.COLUMN_NAME_DOC_TYPE, Types.NVARCHAR),
            entry(GLAccount.COLUMN_NAME_DR_CR, Types.NVARCHAR),
            entry(GLAccount.COLUMN_NAME_FUND_SOURCE, Types.NVARCHAR),
            entry(GLAccount.COLUMN_NAME_METHOD, Types.NVARCHAR)
    );

    static RowMapper<GLAccount> userRowMapper = (rs, rowNum) -> new GLAccount(
            rs.getLong(GLAccount.COLUMN_NAME_GL_ACCOUNT_ID),
            rs.getString(GLAccount.COLUMN_NAME_CENTRALGLACCOUNT),
            rs.getString(GLAccount.COLUMN_NAME_CENTRALGLACCOUNT_DESCRIPTION),
            rs.getString(GLAccount.COLUMN_NAME_CENTRAL_POSTING),
            rs.getString(GLAccount.COLUMN_NAME_AGENCYGLACCOUNT),
            rs.getString(GLAccount.COLUMN_NAME_AGENCYGLACCOUNT_DESCRIPTION),
            rs.getString(GLAccount.COLUMN_NAME_AGENCY_POSTING),
            rs.getString(GLAccount.COLUMN_NAME_DOC_TYPE),
            rs.getString(GLAccount.COLUMN_NAME_DR_CR),
            rs.getString(GLAccount.COLUMN_NAME_FUND_SOURCE),
            rs.getString(GLAccount.COLUMN_NAME_METHOD)
    );

    public GLAccountRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, generateFileAliasUpdater, updaterType, GLAccount.TABLE_NAME, GLAccount.COLUMN_NAME_GL_ACCOUNT_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GLAccount findOneByDocTypeAndFundSource(String docType, String fundSource) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(GLAccount.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(docType)) {
            sql.append(SqlUtil.whereClause(docType, GLAccount.COLUMN_NAME_DOC_TYPE, params));
        }
        if (!Util.isEmpty(fundSource)) {
            sql.append(SqlUtil.whereClause(fundSource, GLAccount.COLUMN_NAME_FUND_SOURCE, params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<GLAccount> companyPayeeBankAccountNoConfigs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!companyPayeeBankAccountNoConfigs.isEmpty()) {
            return companyPayeeBankAccountNoConfigs.get(0);
        } else {
            return null;
        }
    }
}
