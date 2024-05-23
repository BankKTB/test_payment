//package th.com.bloomcode.paymentservice.repository.payment.impl;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//import th.com.bloomcode.paymentservice.model.payment.BankCode;
//import th.com.bloomcode.paymentservice.model.payment.PaymentRunErrorDocumentLog;
//import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
//import th.com.bloomcode.paymentservice.repository.payment.PaymentRunErrorDocumentLogRepository;
//
//import java.sql.Types;
//import java.util.List;
//import java.util.Map;
//
//import static java.util.Map.entry;
//
//@Repository
//@Slf4j
//public class PaymentRunErrorDocumentLogRepositoryImpl extends MetadataJdbcRepository<PaymentRunErrorDocumentLog, Long> implements PaymentRunErrorDocumentLogRepository {
//    static BeanPropertyRowMapper<PaymentRunErrorDocumentLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PaymentRunErrorDocumentLog.class);
//    private final JdbcTemplate jdbcTemplate;
//
//    static Updater<PaymentRunErrorDocumentLog> generateFileAliasUpdater = (t, mapping) -> {
//        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_PAYMENT_RUN_ERROR_DOCUMENT_LOG_ID, t.getId());
//        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_COMPANY_CODE, t.getInvoiceCompanyCode());
//        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_DOCUMENT_NO, t.getInvoiceDocumentNo());
//        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_FISCAL_YEAR, t.getInvoiceFiscalYear());
//        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_PM_GROUP_DOC, t.getPmGroupDoc());
//        mapping.put(PaymentRunErrorDocumentLog.COLUMN_NAME_PM_GROUP_NO, t.getPmGroupNo());
//
//    };
//
//    static Map<String, Integer> updaterType = Map.ofEntries(
//            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_PAYMENT_RUN_ERROR_DOCUMENT_LOG_ID, Types.BIGINT),
//            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_COMPANY_CODE, Types.NVARCHAR),
//            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_DOCUMENT_NO, Types.NVARCHAR),
//            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_INVOICE_FISCAL_YEAR, Types.NVARCHAR),
//            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_PM_GROUP_DOC, Types.NVARCHAR),
//            entry(PaymentRunErrorDocumentLog.COLUMN_NAME_PM_GROUP_NO, Types.NVARCHAR)
//    );
//
//
//    public PaymentRunErrorDocumentLogRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
//        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, BankCode.TABLE_NAME, BankCode.COLUMN_NAME_BANK_CODE_ID, jdbcTemplate);
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public List<PaymentRunErrorDocumentLog> findAllByOrderByFiscalYearAscAccDocNoAsc() {
//        return null;
//    }
//
//    @Override
//    public List<PaymentRunErrorDocumentLog> findDetailByInvoiceCompanyCodeNoAndInvoiceDocumentNoAndInvoiceFiscalYear(String invoiceCompanyCode, String invoiceDocumentNo, String invoiceFiscalYear) {
//        return null;
//    }
//}
