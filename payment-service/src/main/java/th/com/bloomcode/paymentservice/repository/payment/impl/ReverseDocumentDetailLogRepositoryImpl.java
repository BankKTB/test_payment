package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.ReverseDocumentDetailLog;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.ReverseDocumentDetailLogRepository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class ReverseDocumentDetailLogRepositoryImpl extends MetadataJdbcRepository<ReverseDocumentDetailLog, Long> implements ReverseDocumentDetailLogRepository {
    static BeanPropertyRowMapper<ReverseDocumentDetailLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(ReverseDocumentDetailLog.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<ReverseDocumentDetailLog> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(ReverseDocumentDetailLog.COLUMN_NAME_REVERSE_DOCUMENT_DETAIL_LOG, t.getId());
        mapping.put(ReverseDocumentDetailLog.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(ReverseDocumentDetailLog.COLUMN_NAME_DOCUMENT_NO, t.getDocumentNo());
        mapping.put(ReverseDocumentDetailLog.COLUMN_NAME_FISCAL_YEAR, t.getFiscalYear());
        mapping.put(ReverseDocumentDetailLog.COLUMN_NAME_ERROR_CODE, t.getErrorCode());
        mapping.put(ReverseDocumentDetailLog.COLUMN_NAME_LINE, t.getLine());
        mapping.put(ReverseDocumentDetailLog.COLUMN_NAME_TEXT, t.getText());
        mapping.put(ReverseDocumentDetailLog.COLUMN_NAME_CREATED, new Timestamp(System.currentTimeMillis()));
        mapping.put(ReverseDocumentDetailLog.COLUMN_NAME_DATE_TIME, new Timestamp(System.currentTimeMillis()));

    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(ReverseDocumentDetailLog.COLUMN_NAME_REVERSE_DOCUMENT_DETAIL_LOG, Types.BIGINT),
            entry(ReverseDocumentDetailLog.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
            entry(ReverseDocumentDetailLog.COLUMN_NAME_DOCUMENT_NO, Types.NVARCHAR),
            entry(ReverseDocumentDetailLog.COLUMN_NAME_FISCAL_YEAR, Types.NVARCHAR),
            entry(ReverseDocumentDetailLog.COLUMN_NAME_ERROR_CODE, Types.NVARCHAR),
            entry(ReverseDocumentDetailLog.COLUMN_NAME_LINE, Types.NVARCHAR),
            entry(ReverseDocumentDetailLog.COLUMN_NAME_TEXT, Types.NVARCHAR),
            entry(ReverseDocumentDetailLog.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(ReverseDocumentDetailLog.COLUMN_NAME_DATE_TIME, Types.TIMESTAMP)
    );


    public ReverseDocumentDetailLogRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, ReverseDocumentDetailLog.TABLE_NAME, ReverseDocumentDetailLog.COLUMN_NAME_REVERSE_DOCUMENT_DETAIL_LOG, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<ReverseDocumentDetailLog> findDetailByDocument(String invoiceCompanyCode, String invoiceDocumentNo, String invoiceFiscalYear) {
        List<Object> params = new ArrayList<>();
        params.add(invoiceCompanyCode);
        params.add(invoiceDocumentNo);
        params.add(invoiceFiscalYear);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + ReverseDocumentDetailLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append("  " + ReverseDocumentDetailLog.COLUMN_NAME_COMPANY_CODE + " = ? ");
        sql.append(" AND " + ReverseDocumentDetailLog.COLUMN_NAME_DOCUMENT_NO + " = ? ");
        sql.append(" AND " + ReverseDocumentDetailLog.COLUMN_NAME_FISCAL_YEAR + " = ? ");

        sql.append(" ORDER by DATE_TIME DESC ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }
}
