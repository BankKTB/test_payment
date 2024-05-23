package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentDetailLog;
import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentLog;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentRunErrorDocumentDetailLogRepository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class PaymentRunErrorDocumentDetailLogRepositoryImpl extends MetadataJdbcRepository<PaymentRunErrorDocumentDetailLog, Long> implements PaymentRunErrorDocumentDetailLogRepository {
    static BeanPropertyRowMapper<PaymentRunErrorDocumentDetailLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PaymentRunErrorDocumentDetailLog.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<PaymentRunErrorDocumentDetailLog> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_PAYMENT_RUN_ERROR_DOCUMENT_DETAIL_LOG, t.getId());
        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_COMPANY_CODE, t.getInvoiceCompanyCode());
        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_DOCUMENT_NO, t.getInvoiceDocumentNo());
        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_FISCAL_YEAR, t.getInvoiceFiscalYear());
        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_PM_GROUP_DOC, t.getPmGroupDoc());
        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_PM_GROUP_NO, t.getPmGroupNo());
        mapping.put(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_ERROR_CODE, t.getErrorCode());
        mapping.put(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_LINE, t.getLine());
        mapping.put(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_TEXT, t.getText());
        mapping.put(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_CREATED, new Timestamp(System.currentTimeMillis()));
        mapping.put(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_DATE_TIME, new Timestamp(System.currentTimeMillis()));
        mapping.put(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_PAYMENT_ALIAS_ID, t.getPaymentAliasId());

    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_PAYMENT_RUN_ERROR_DOCUMENT_DETAIL_LOG, Types.BIGINT),
            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_COMPANY_CODE, Types.NVARCHAR),
            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_DOCUMENT_NO, Types.NVARCHAR),
            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_FISCAL_YEAR, Types.NVARCHAR),
            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_PM_GROUP_DOC, Types.NVARCHAR),
            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_PM_GROUP_NO, Types.NVARCHAR),
            entry(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_ERROR_CODE, Types.NVARCHAR),
            entry(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_LINE, Types.NVARCHAR),
            entry(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_TEXT, Types.NVARCHAR),
            entry(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_DATE_TIME, Types.TIMESTAMP),
            entry(PaymentRunErrorDocumentDetailLog.COLUMN_NAME_PAYMENT_ALIAS_ID, Types.BIGINT)
    );


    public PaymentRunErrorDocumentDetailLogRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, PaymentRunErrorDocumentDetailLog.TABLE_NAME, PaymentRunErrorDocumentDetailLog.COLUMN_NAME_PAYMENT_RUN_ERROR_DOCUMENT_DETAIL_LOG, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<PaymentRunErrorDocumentDetailLog> findErrorDetailByPaymentAliasId(Long paymentAliasId) {
        List<Object> params = new ArrayList<>();
        params.add(paymentAliasId);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + PaymentRunErrorDocumentDetailLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append("  " + PaymentRunErrorDocumentDetailLog.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");

        sql.append(" ORDER by DATE_TIME DESC ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<PaymentRunErrorDocumentDetailLog> findDetailByInvoice(String invoiceCompanyCode, String invoiceDocumentNo, String invoiceFiscalYear) {
        List<Object> params = new ArrayList<>();
        params.add(invoiceCompanyCode);
        params.add(invoiceDocumentNo);
        params.add(invoiceFiscalYear);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + PaymentRunErrorDocumentDetailLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append("  " + PaymentRunErrorDocumentDetailLog.COLUMN_NAME_INVOICE_COMPANY_CODE + " = ? ");
        sql.append(" AND " + PaymentRunErrorDocumentDetailLog.COLUMN_NAME_INVOICE_DOCUMENT_NO + " = ? ");
        sql.append(" AND " + PaymentRunErrorDocumentDetailLog.COLUMN_NAME_INVOICE_FISCAL_YEAR + " = ? ");

        sql.append(" ORDER by DATE_TIME DESC ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }
}
