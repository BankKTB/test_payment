package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.payment.UnblockDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.request.UnblockDocumentLogDetailRequest;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.UnblockDocumentDetailLogRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class UnblockDocumentDetailLogRepositoryImpl extends MetadataJdbcRepository<UnblockDocumentDetailLog, Long> implements UnblockDocumentDetailLogRepository {
    static BeanPropertyRowMapper<UnblockDocumentDetailLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(UnblockDocumentDetailLog.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<UnblockDocumentDetailLog> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(UnblockDocumentDetailLog.COLUMN_NAME_UNBLOCK_DOCUMENT_DETAIL_LOG, t.getId());
        mapping.put(UnblockDocumentDetailLog.COLUMN_NAME_ORIGINAL_COMPANY_CODE, t.getOriginalCompanyCode());
        mapping.put(UnblockDocumentDetailLog.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, t.getOriginalDocumentNo());
        mapping.put(UnblockDocumentDetailLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
        mapping.put(UnblockDocumentDetailLog.COLUMN_NAME_ERROR_CODE, t.getErrorCode());
        mapping.put(UnblockDocumentDetailLog.COLUMN_NAME_LINE, t.getLine());
        mapping.put(UnblockDocumentDetailLog.COLUMN_NAME_TEXT, t.getText());
        mapping.put(UnblockDocumentDetailLog.COLUMN_NAME_CREATED, new Timestamp(System.currentTimeMillis()));
        mapping.put(UnblockDocumentDetailLog.COLUMN_NAME_DATE_TIME, new Timestamp(System.currentTimeMillis()));

    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(UnblockDocumentDetailLog.COLUMN_NAME_UNBLOCK_DOCUMENT_DETAIL_LOG, Types.BIGINT),
            entry(UnblockDocumentDetailLog.COLUMN_NAME_ORIGINAL_COMPANY_CODE, Types.NVARCHAR),
            entry(UnblockDocumentDetailLog.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
            entry(UnblockDocumentDetailLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
            entry(UnblockDocumentDetailLog.COLUMN_NAME_ERROR_CODE, Types.NVARCHAR),
            entry(UnblockDocumentDetailLog.COLUMN_NAME_LINE, Types.NVARCHAR),
            entry(UnblockDocumentDetailLog.COLUMN_NAME_TEXT, Types.NVARCHAR),
            entry(UnblockDocumentDetailLog.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(UnblockDocumentDetailLog.COLUMN_NAME_DATE_TIME, Types.TIMESTAMP)
    );


    public UnblockDocumentDetailLogRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, UnblockDocumentDetailLog.TABLE_NAME, UnblockDocumentDetailLog.COLUMN_NAME_UNBLOCK_DOCUMENT_DETAIL_LOG, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<UnblockDocumentDetailLog> findDetailByDocument(String invoiceCompanyCode, String invoiceDocumentNo, String invoiceFiscalYear) {
        List<Object> params = new ArrayList<>();
        params.add(invoiceCompanyCode);
        params.add(invoiceDocumentNo);
        params.add(invoiceFiscalYear);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + UnblockDocumentDetailLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append("  " + UnblockDocumentDetailLog.COLUMN_NAME_ORIGINAL_COMPANY_CODE + " = ? ");
        sql.append(" AND " + UnblockDocumentDetailLog.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ? ");
        sql.append(" AND " + UnblockDocumentDetailLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR + " = ? ");

        sql.append(" ORDER by DATE_TIME DESC ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<UnblockDocumentDetailLog> findLogDetail(UnblockDocumentLogDetailRequest request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT *            ");
        sb.append("          FROM UNBLOCK_DOCUMENT_DETAIL_LOG           ");

        sb.append(" WHERE 1 = 1 ");

        log.info(" request {} ", request);

        if (!Util.isEmpty(request.getCompanyCode())) {
            sb.append(SqlUtil.whereClause(request.getCompanyCode(), "ORIGINAL_COMPANY_CODE", params));
        }

        if (!Util.isEmpty(request.getDocumentNo())) {
            sb.append(SqlUtil.whereClause(request.getDocumentNo(), "ORIGINAL_DOCUMENT_NO", params));
        }

        if (!Util.isEmpty(request.getFiscalYear())) {
            sb.append(SqlUtil.whereClause(request.getFiscalYear(), "ORIGINAL_FISCAL_YEAR", params));
        }
        sb.append(" ORDER BY DATE_TIME DESC FETCH FIRST 1 ROWS ONLY ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);


        log.info("sql find One {}", sb.toString());
        return this.jdbcTemplate.query(sb.toString(), objParams, beanPropertyRowMapper);
    }
}
