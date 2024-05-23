package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.ProposalReturnLog;
import th.com.bloomcode.paymentservice.model.payment.ReturnReverseDocument;
import th.com.bloomcode.paymentservice.model.request.ReverseDocumentRequest;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.ReturnReverseDocumentRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class ReturnReverseDocumentRepositoryImpl extends MetadataJdbcRepository<ReturnReverseDocument, Long> implements ReturnReverseDocumentRepository {
    static BeanPropertyRowMapper<ReturnReverseDocument> beanPropertyRowMapper = new BeanPropertyRowMapper<>(ReturnReverseDocument.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<ReturnReverseDocument> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(ReturnReverseDocument.COLUMN_NAME_RETURN_REVERSE_DOCUMENT_ID, t.getId());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_COMPANY_CODE, t.getOriginalCompanyCode());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, t.getOriginalDocumentNo());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_TYPE, t.getOriginalDocumentType());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_ORIGINAL_COMPANY_CODE, t.getReverseOriginalCompanyCode());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO, t.getReverseOriginalDocumentNo());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR, t.getReverseOriginalFiscalYear());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_TYPE, t.getReverseOriginalDocumentType());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_IDEM_STATUS, t.getOriginalIdemStatus());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_USER_POST, t.getOriginalUserPost());

        mapping.put(ReturnReverseDocument.COLUMN_NAME_INVOICE_COMPANY_CODE, t.getInvoiceCompanyCode());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_INVOICE_DOCUMENT_NO, t.getInvoiceDocumentNo());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_INVOICE_FISCAL_YEAR, t.getInvoiceFiscalYear());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_INVOICE_DOCUMENT_TYPE, t.getInvoiceDocumentType());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_INVOICE_COMPANY_CODE, t.getReverseInvoiceCompanyCode());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO, t.getReverseInvoiceDocumentNo());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_INVOICE_FISCAL_YEAR, t.getReverseInvoiceFiscalYear());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_TYPE, t.getReverseInvoiceDocumentType());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_INVOICE_IDEM_STATUS, t.getInvoiceIdemStatus());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_INVOICE_USER_POST, t.getInvoiceUserPost());

        mapping.put(ReturnReverseDocument.COLUMN_NAME_PAYMENT_COMPANY_CODE, t.getPaymentCompanyCode());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_PAYMENT_DOCUMENT_NO, t.getPaymentDocumentNo());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_PAYMENT_FISCAL_YEAR, t.getPaymentFiscalYear());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_PAYMENT_DOCUMENT_TYPE, t.getPaymentDocumentType());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_PAYMENT_COMPANY_CODE, t.getReversePaymentCompanyCode());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_NO, t.getReversePaymentDocumentNo());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_PAYMENT_FISCAL_YEAR, t.getReversePaymentFiscalYear());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_TYPE, t.getReversePaymentDocumentType());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_PAYMENT_IDEM_STATUS, t.getPaymentIdemStatus());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_PAYMENT_USER_POST, t.getPaymentUserPost());

        mapping.put(ReturnReverseDocument.COLUMN_NAME_PO_DOC_NO, t.getPoDocNo());

        mapping.put(ReturnReverseDocument.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(ReturnReverseDocument.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(
                ReturnReverseDocument.COLUMN_NAME_AUTO_STEP3, t.isAutoStep3());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(ReturnReverseDocument.COLUMN_NAME_RETURN_REVERSE_DOCUMENT_ID, Types.BIGINT),

            entry(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_COMPANY_CODE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_TYPE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_ORIGINAL_COMPANY_CODE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_TYPE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_IDEM_STATUS, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_ORIGINAL_USER_POST, Types.NVARCHAR),

            entry(ReturnReverseDocument.COLUMN_NAME_INVOICE_COMPANY_CODE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_INVOICE_DOCUMENT_NO, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_INVOICE_FISCAL_YEAR, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_INVOICE_DOCUMENT_TYPE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_INVOICE_COMPANY_CODE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_INVOICE_FISCAL_YEAR, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_TYPE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_INVOICE_IDEM_STATUS, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_INVOICE_USER_POST, Types.NVARCHAR),

            entry(ReturnReverseDocument.COLUMN_NAME_PAYMENT_COMPANY_CODE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_PAYMENT_DOCUMENT_NO, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_PAYMENT_DOCUMENT_TYPE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_PAYMENT_COMPANY_CODE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_NO, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_TYPE, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_PAYMENT_IDEM_STATUS, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_PAYMENT_USER_POST, Types.NVARCHAR),

            entry(ReturnReverseDocument.COLUMN_NAME_PO_DOC_NO, Types.NVARCHAR),

            entry(ReturnReverseDocument.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(ReturnReverseDocument.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
            entry(ReturnReverseDocument.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
            entry(ReturnReverseDocument.COLUMN_NAME_AUTO_STEP3, Types.BOOLEAN)
    );


    public ReturnReverseDocumentRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, ReturnReverseDocument.TABLE_NAME, ReturnReverseDocument.COLUMN_NAME_RETURN_REVERSE_DOCUMENT_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ReturnReverseDocument> findOneByCompanyCodeAndDocumentNoAndFiscalYear(String companyCode, String documentNo, String fiscalYear,boolean payment) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + ReturnReverseDocument.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(" 1=1 ");

        if(payment){
            if (!Util.isEmpty(companyCode)) {
                sql.append(SqlUtil.whereClause(companyCode, "PAYMENT_COMPANY_CODE", params));
            }
            if (!Util.isEmpty(documentNo)) {
                sql.append(SqlUtil.whereClause(documentNo, "PAYMENT_DOCUMENT_NO", params));
            }
            if (!Util.isEmpty(fiscalYear)) {
                sql.append(SqlUtil.whereClause(fiscalYear, "PAYMENT_FISCAL_YEAR", params));
            }
     } else {

      if (!Util.isEmpty(companyCode)) {
        sql.append(SqlUtil.whereClause(companyCode, "ORIGINAL_COMPANY_CODE", params));
      }
      if (!Util.isEmpty(documentNo)) {
        sql.append(SqlUtil.whereClause(documentNo, "ORIGINAL_DOCUMENT_NO", params));
      }
      if (!Util.isEmpty(fiscalYear)) {
        sql.append(SqlUtil.whereClause(fiscalYear, "ORIGINAL_FISCAL_YEAR", params));
      }
}

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        log.info("sql find One {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public void updateReversePayment(String revCompanyCode, String revDocumentNo, String revFiscalYear,String companyCode, String documentNo, String fiscalYear) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE ");
        sql.append(" RETURN_REVERSE_DOCUMENT ");
        sql.append(" SET REVERSE_PAYMENT_COMPANY_CODE = ? , REVERSE_PAYMENT_DOCUMENT_NO = ? , REVERSE_PAYMENT_FISCAL_YEAR = ? , PAYMENT_IDEM_STATUS = ? ");
        sql.append(" WHERE ");
        sql.append(" 1 = 1  ");
        sql.append(" AND PAYMENT_COMPANY_CODE = ? ");
        sql.append(" AND PAYMENT_DOCUMENT_NO = ? ");
        sql.append(" AND PAYMENT_FISCAL_YEAR = ? ");
        this.jdbcTemplate.update(sql.toString(),revCompanyCode,revDocumentNo,revFiscalYear,"S",companyCode,documentNo,fiscalYear);
    }

    @Override
    public List<ReturnReverseDocument> findByListDocument(List<ReverseDocumentRequest> request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + ReturnReverseDocument.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(" 1=1 ");


        for (int x = 0; x < request.size(); x++) {
            params.add(request.get(x).getCompCode());
            params.add(request.get(x).getAccountDocNo());
            params.add(request.get(x).getFiscalYear());
            if (x == 0) {
                sql.append("  AND ((UPPER(ORIGINAL_COMPANY_CODE) LIKE UPPER(?)) ");
                sql.append("  AND (UPPER(ORIGINAL_DOCUMENT_NO) LIKE UPPER(?)) ");
                sql.append("  AND (UPPER(ORIGINAL_FISCAL_YEAR) LIKE UPPER(?))) ");
            } else {
                sql.append("  OR ((UPPER(ORIGINAL_COMPANY_CODE) LIKE UPPER(?)) ");
                sql.append("  AND (UPPER(ORIGINAL_DOCUMENT_NO) LIKE UPPER(?)) ");
                sql.append("  AND (UPPER(ORIGINAL_FISCAL_YEAR) LIKE UPPER(?))) ");

            }
        }


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        log.info("sql find One {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }
}
