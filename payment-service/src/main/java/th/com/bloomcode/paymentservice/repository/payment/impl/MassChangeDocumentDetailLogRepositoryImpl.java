package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.MassChangeDocumentDetailLog;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.MassChangeDocumentDetailLogRepository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class MassChangeDocumentDetailLogRepositoryImpl extends MetadataJdbcRepository<MassChangeDocumentDetailLog, Long> implements MassChangeDocumentDetailLogRepository {
    static BeanPropertyRowMapper<MassChangeDocumentDetailLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(MassChangeDocumentDetailLog.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<MassChangeDocumentDetailLog> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(MassChangeDocumentDetailLog.COLUMN_NAME_MASS_CHANGE_DOCUMENT_DETAIL_LOG, t.getId());
        mapping.put(MassChangeDocumentDetailLog.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(MassChangeDocumentDetailLog.COLUMN_NAME_DOCUMENT_NO, t.getDocumentNo());
        mapping.put(MassChangeDocumentDetailLog.COLUMN_NAME_FISCAL_YEAR, t.getFiscalYear());
        mapping.put(MassChangeDocumentDetailLog.COLUMN_NAME_ERROR_CODE, t.getErrorCode());
        mapping.put(MassChangeDocumentDetailLog.COLUMN_NAME_LINE, t.getLine());
        mapping.put(MassChangeDocumentDetailLog.COLUMN_NAME_TEXT, t.getText());
        mapping.put(MassChangeDocumentDetailLog.COLUMN_NAME_CREATED, new Timestamp(System.currentTimeMillis()));
        mapping.put(MassChangeDocumentDetailLog.COLUMN_NAME_DATE_TIME, new Timestamp(System.currentTimeMillis()));

    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(MassChangeDocumentDetailLog.COLUMN_NAME_MASS_CHANGE_DOCUMENT_DETAIL_LOG, Types.BIGINT),
            entry(MassChangeDocumentDetailLog.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
            entry(MassChangeDocumentDetailLog.COLUMN_NAME_DOCUMENT_NO, Types.NVARCHAR),
            entry(MassChangeDocumentDetailLog.COLUMN_NAME_FISCAL_YEAR, Types.NVARCHAR),
            entry(MassChangeDocumentDetailLog.COLUMN_NAME_ERROR_CODE, Types.NVARCHAR),
            entry(MassChangeDocumentDetailLog.COLUMN_NAME_LINE, Types.NVARCHAR),
            entry(MassChangeDocumentDetailLog.COLUMN_NAME_TEXT, Types.NVARCHAR),
            entry(MassChangeDocumentDetailLog.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(MassChangeDocumentDetailLog.COLUMN_NAME_DATE_TIME, Types.TIMESTAMP)
    );


    public MassChangeDocumentDetailLogRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, MassChangeDocumentDetailLog.TABLE_NAME, MassChangeDocumentDetailLog.COLUMN_NAME_MASS_CHANGE_DOCUMENT_DETAIL_LOG, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<MassChangeDocumentDetailLog> findDetailByDocument(String invoiceCompanyCode, String invoiceDocumentNo, String invoiceFiscalYear) {
        List<Object> params = new ArrayList<>();
        params.add(invoiceCompanyCode);
        params.add(invoiceDocumentNo);
        params.add(invoiceFiscalYear);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + MassChangeDocumentDetailLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append("  " + MassChangeDocumentDetailLog.COLUMN_NAME_COMPANY_CODE + " = ? ");
        sql.append(" AND " + MassChangeDocumentDetailLog.COLUMN_NAME_DOCUMENT_NO + " = ? ");
        sql.append(" AND " + MassChangeDocumentDetailLog.COLUMN_NAME_FISCAL_YEAR + " = ? ");

        sql.append(" ORDER by DATE_TIME DESC ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }
}
