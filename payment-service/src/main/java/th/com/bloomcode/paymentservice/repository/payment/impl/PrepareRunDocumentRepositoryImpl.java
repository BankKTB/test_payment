package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PrepareRunDocumentRepository;
import th.com.bloomcode.paymentservice.webservice.model.APPaymentLine;

import java.util.List;

@Repository
@Slf4j
public class PrepareRunDocumentRepositoryImpl extends MetadataJdbcRepository<PrepareRunDocument, Long> implements PrepareRunDocumentRepository {

    private final JdbcTemplate jdbcTemplate;

    static RowMapper<PrepareRunDocument> prepareRunRowMapper = (rs, rowNum) -> new PrepareRunDocument(
            rs.getString(PrepareRunDocument.COLUMN_NAME_ACCOUNT_TYPE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ASSET_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ASSET_SUB_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ASSIGNMENT),
            rs.getString(PrepareRunDocument.COLUMN_NAME_BANK_ACCOUNT_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_BR_DOCUMENT_NO),
            rs.getInt(PrepareRunDocument.COLUMN_NAME_BR_LINE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_BUDGET_ACCOUNT),
            rs.getString(PrepareRunDocument.COLUMN_NAME_BUDGET_ACTIVITY),
            rs.getString(PrepareRunDocument.COLUMN_NAME_BUDGET_ACTIVITY_NAME),
            rs.getBoolean(PrepareRunDocument.COLUMN_NAME_IS_CHILD),
            rs.getString(PrepareRunDocument.COLUMN_NAME_COST_CENTER),
            rs.getString(PrepareRunDocument.COLUMN_NAME_COST_CENTER_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_CURRENCY),
            rs.getTimestamp(PrepareRunDocument.COLUMN_NAME_DATE_ACCT),
            rs.getTimestamp(PrepareRunDocument.COLUMN_NAME_DATE_BASE_LINE),
            rs.getTimestamp(PrepareRunDocument.COLUMN_NAME_DATE_DOC),
            rs.getString(PrepareRunDocument.COLUMN_NAME_DR_CR),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ERROR_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_FI_AREA),
            rs.getString(PrepareRunDocument.COLUMN_NAME_FI_AREA_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_FUND_CENTER),
            rs.getString(PrepareRunDocument.COLUMN_NAME_FUND_CENTER_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_FUND_SOURCE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_FUND_SOURCE_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_GL_ACCOUNT),
            rs.getString(PrepareRunDocument.COLUMN_NAME_GL_ACCOUNT_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_HEADER_REFERENCE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_HOUSE_BANK),
            rs.getString(PrepareRunDocument.COLUMN_NAME_IDEM_STATUS),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_INVOICE_AMOUNT),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_INVOICE_AMOUNT_PAID),
            rs.getString(PrepareRunDocument.COLUMN_NAME_INVOICE_COMPANY_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_INVOICE_COMPANY_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_INVOICE_DOCUMENT_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_INVOICE_DOCUMENT_TYPE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_INVOICE_FISCAL_YEAR),
            rs.getString(PrepareRunDocument.COLUMN_NAME_INVOICE_PAYMENT_CENTER),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_INVOICE_WTX_AMOUNT),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_INVOICE_WTX_AMOUNT_P),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_INVOICE_WTX_BASE),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_INVOICE_WTX_BASE_P),
            rs.getInt(PrepareRunDocument.COLUMN_NAME_LINE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_LINE_ITEM_TEXT),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_ORIGINAL_AMOUNT),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_ORIGINAL_AMOUNT_PAID),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ORIGINAL_COMPANY_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ORIGINAL_COMPANY_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_TYPE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ORIGINAL_PAYMENT_CENTER),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_ORIGINAL_WTX_AMOUNT),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_ORIGINAL_WTX_BASE),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_ORIGINAL_WTX_BASE_P),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PARENT_COMPANY_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PARENT_DOCUMENT_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PARENT_FISCAL_YEAR),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_BLOCK),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_CENTER),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_COMPANY_CODE),
            rs.getTimestamp(PrepareRunDocument.COLUMN_NAME_PAYMENT_DATE),
            rs.getTimestamp(PrepareRunDocument.COLUMN_NAME_PAYMENT_DATE_ACCT),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_DOCUMENT_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_FISCAL_YEAR),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_METHOD),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_METHOD_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_REFERENCE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_TERM),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PM_GROUP_DOC),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PM_GROUP_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PO_DOCUMENT_NO),
            rs.getInt(PrepareRunDocument.COLUMN_NAME_PO_LINE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_POSTING_KEY),
            rs.getBoolean(PrepareRunDocument.COLUMN_NAME_IS_PROPOSAL),
            rs.getBoolean(PrepareRunDocument.COLUMN_NAME_IS_PROPOSAL_BLOCK),
            rs.getString(PrepareRunDocument.COLUMN_NAME_REFERENCE1),
            rs.getString(PrepareRunDocument.COLUMN_NAME_REFERENCE2),
            rs.getString(PrepareRunDocument.COLUMN_NAME_REFERENCE3),
            rs.getString(PrepareRunDocument.COLUMN_NAME_SPECIAL_GL),
            rs.getString(PrepareRunDocument.COLUMN_NAME_SPECIAL_GL_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_STATUS),
            rs.getString(PrepareRunDocument.COLUMN_NAME_TRADING_PARTNER),
            rs.getString(PrepareRunDocument.COLUMN_NAME_TRADING_PARTNER_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ACCOUNT_HOLDER_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_ADDRESS),
            rs.getString(PrepareRunDocument.COLUMN_NAME_CITY),
            rs.getString(PrepareRunDocument.COLUMN_NAME_COUNTRY),
            rs.getString(PrepareRunDocument.COLUMN_NAME_COUNTRY_NAME),
            rs.getTimestamp(PrepareRunDocument.COLUMN_NAME_DATE_DUE),
            rs.getTimestamp(PrepareRunDocument.COLUMN_NAME_DATE_VALUE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_NAME1),
            rs.getString(PrepareRunDocument.COLUMN_NAME_NAME2),
            rs.getString(PrepareRunDocument.COLUMN_NAME_NAME3),
            rs.getString(PrepareRunDocument.COLUMN_NAME_NAME4),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_ADDRESS),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_BANK),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_BANK_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_BANK_KEY),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_BANK_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_BANK_REFERENCE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_CITY),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_COUNTRY),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_NAME1),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_NAME2),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_NAME3),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_NAME4),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_POSTAL_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_TAX_ID),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYEE_TITLE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYING_BANK_ACCOUNT_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYING_BANK_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYING_BANK_COUNTRY),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYING_BANK_KEY),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYING_BANK_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYING_BANK_NO),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYING_COMPANY_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYING_GL_ACCOUNT),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYING_HOUSE_BANK),
            rs.getString(PrepareRunDocument.COLUMN_NAME_PAYMENT_SPECIAL_GL),
            rs.getString(PrepareRunDocument.COLUMN_NAME_POSTAL_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_SWIFT_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_VENDOR_CODE),
            rs.getString(PrepareRunDocument.COLUMN_NAME_VENDOR_FLAG),
            rs.getString(PrepareRunDocument.COLUMN_NAME_VENDOR_NAME),
            rs.getString(PrepareRunDocument.COLUMN_NAME_VENDOR_TAX_ID),
            rs.getString(PrepareRunDocument.COLUMN_NAME_VENDOR_TITLE),
            rs.getLong(PrepareRunDocument.COLUMN_NAME_PAYMENT_ALIAS_ID),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_CREDIT_MEMO),
            rs.getBigDecimal(PrepareRunDocument.COLUMN_NAME_WTX_CREDIT_MEMO)
    );


    static RowMapper<PrepareRealRunDocument> prepareRealRunRowMapper = (rs, rowNum) -> new PrepareRealRunDocument(
            rs.getLong(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_PROCESS_ID),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ACCOUNT_TYPE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ASSET_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ASSET_SUB_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ASSIGNMENT),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_BANK_ACCOUNT_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_BR_DOCUMENT_NO),
            rs.getInt(PrepareRealRunDocument.COLUMN_NAME_BR_LINE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_BUDGET_ACCOUNT),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_BUDGET_ACTIVITY),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_BUDGET_ACTIVITY_NAME),
            rs.getBoolean(PrepareRealRunDocument.COLUMN_NAME_IS_CHILD),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_COST_CENTER),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_COST_CENTER_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_CURRENCY),
            rs.getTimestamp(PrepareRealRunDocument.COLUMN_NAME_DATE_ACCT),
            rs.getTimestamp(PrepareRealRunDocument.COLUMN_NAME_DATE_BASE_LINE),
            rs.getTimestamp(PrepareRealRunDocument.COLUMN_NAME_DATE_DOC),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_DR_CR),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ERROR_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_FI_AREA),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_FUND_CENTER),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_FUND_CENTER_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_FUND_SOURCE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_FUND_SOURCE_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_GL_ACCOUNT),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_GL_ACCOUNT_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_HEADER_REFERENCE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_HOUSE_BANK),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_IDEM_STATUS),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_INVOICE_AMOUNT),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_INVOICE_COMPANY_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_INVOICE_COMPANY_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_INVOICE_DOCUMENT_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_INVOICE_DOCUMENT_TYPE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_INVOICE_FISCAL_YEAR),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_INVOICE_PAYMENT_CENTER),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_INVOICE_WTX_AMOUNT),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_INVOICE_WTX_AMOUNT_P),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_INVOICE_WTX_BASE),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_INVOICE_WTX_BASE_P),
            rs.getInt(PrepareRealRunDocument.COLUMN_NAME_LINE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_LINE_ITEM_TEXT),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_AMOUNT),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_COMPANY_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_COMPANY_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_TYPE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_PAYMENT_CENTER),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_WTX_AMOUNT),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_WTX_BASE),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_WTX_BASE_P),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PARENT_COMPANY_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PARENT_DOCUMENT_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PARENT_FISCAL_YEAR),
            rs.getLong(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_ALIAS_ID),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_BLOCK),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_CENTER),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_COMPANY_CODE),
            rs.getTimestamp(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_DATE),
            rs.getTimestamp(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_DATE_ACCT),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_DOCUMENT_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_FISCAL_YEAR),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_METHOD),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_METHOD_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_REFERENCE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_TERM),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PM_GROUP_DOC),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PM_GROUP_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PO_DOCUMENT_NO),
            rs.getInt(PrepareRealRunDocument.COLUMN_NAME_PO_LINE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_POSTING_KEY),
            rs.getBoolean(PrepareRealRunDocument.COLUMN_NAME_IS_PROPOSAL),
            rs.getBoolean(PrepareRealRunDocument.COLUMN_NAME_IS_PROPOSAL_BLOCK),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_REFERENCE1),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_REFERENCE2),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_REFERENCE3),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_SPECIAL_GL),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_SPECIAL_GL_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_STATUS),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_TRADING_PARTNER),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_TRADING_PARTNER_NAME),
            rs.getTimestamp(PrepareRealRunDocument.COLUMN_NAME_CREATED),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_CREATED_BY),
            rs.getTimestamp(PrepareRealRunDocument.COLUMN_NAME_UPDATED),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_UPDATED_BY),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_INVOICE_AMOUNT_PAID),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_AMOUNT_PAID),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_FI_AREA_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_COMPANY_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_REVERSE_PAYMENT_COMPANY_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_REVERSE_PAYMENT_FISCAL_YEAR),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_TYPE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_DOCUMENT_TYPE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_IDEM_UPDATE),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_INVOICE_WTX_AMOUNTP),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_INVOICE_WTX_BASEP),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_WTX_AMOUNTP),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_ORIGINAL_WTX_BASEP),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_SPECIALGL),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_SPECIALGLNAME),
            rs.getBoolean(PrepareRealRunDocument.COLUMN_NAME_IS_HAVE_CHILD),
            rs.getBoolean(PrepareRealRunDocument.COLUMN_NAME_HAVE_CHILD),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_CREDIT_MEMO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_REVERSE_PAYMENT_STATUS),
            rs.getBigDecimal(PrepareRealRunDocument.COLUMN_NAME_WTX_CREDIT_MEMO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_INFORMATION_ID),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ACCOUNT_HOLDER_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_ADDRESS),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_CITY),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_COUNTRY),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_COUNTRY_NAME),
            rs.getTimestamp(PrepareRealRunDocument.COLUMN_NAME_DATE_DUE),
            rs.getTimestamp(PrepareRealRunDocument.COLUMN_NAME_DATE_VALUE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_NAME1),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_NAME2),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_NAME3),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_NAME4),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_ADDRESS),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_BANK_KEY),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_BANK_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_BANK_REFERENCE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_CITY),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_COUNTRY),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_NAME1),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_NAME2),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_NAME3),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_NAME4),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_POSTAL_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_TAX_ID),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_TITLE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYING_BANK_ACCOUNT_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYING_BANK_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYING_BANK_COUNTRY),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYING_BANK_KEY),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYING_BANK_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYING_BANK_NO),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYING_COMPANY_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYING_GL_ACCOUNT),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYING_HOUSE_BANK),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_SPECIAL_GL),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_POSTAL_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_SWIFT_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_VENDOR_CODE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_VENDOR_FLAG),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_VENDOR_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_VENDOR_TAX_ID),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_VENDOR_TITLE),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_BANK),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYEE_BANK_NAME),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYINGGLACCOUNT),
            rs.getString(PrepareRealRunDocument.COLUMN_NAME_PAYMENT_SPECIALGL)
    );

    public PrepareRunDocumentRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(prepareRunRowMapper, null, null, null, null, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PrepareRealRunDocument> findProposalDocument(Long paymentAliasId, boolean isProposal) {
        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PC.ID     AS PAYMENT_PROCESS_ID,           ");
        sql.append("          PC.ACCOUNT_TYPE,           ");
        sql.append("          PC.ASSET_NO,           ");
        sql.append("          PC.ASSET_SUB_NO,           ");
        sql.append("          PC.ASSIGNMENT,           ");
        sql.append("          PC.BANK_ACCOUNT_NO,           ");
        sql.append("          PC.BR_DOCUMENT_NO,           ");
        sql.append("          PC.BR_LINE,           ");
        sql.append("          PC.BUDGET_ACCOUNT,           ");
        sql.append("          PC.BUDGET_ACTIVITY,           ");
        sql.append("          PC.BUDGET_ACTIVITY_NAME,           ");
        sql.append("          PC.IS_CHILD,           ");
        sql.append("          PC.COST_CENTER,           ");
        sql.append("          PC.COST_CENTER_NAME,           ");
        sql.append("          PC.CURRENCY,           ");
        sql.append("          PC.DATE_ACCT,           ");
        sql.append("          PC.DATE_BASE_LINE,           ");
        sql.append("          PC.DATE_DOC,           ");
        sql.append("          PC.DR_CR,           ");
        sql.append("          PC.ERROR_CODE,           ");
        sql.append("          PC.FI_AREA,           ");
        sql.append("          PC.FUND_CENTER,           ");
        sql.append("          PC.FUND_CENTER_NAME,           ");
        sql.append("          PC.FUND_SOURCE,           ");
        sql.append("          PC.FUND_SOURCE_NAME,           ");
        sql.append("          PC.GL_ACCOUNT,           ");
        sql.append("          PC.GL_ACCOUNT_NAME,           ");
        sql.append("          PC.HEADER_REFERENCE,           ");
        sql.append("          PC.HOUSE_BANK,           ");
        sql.append("          PC.IDEM_STATUS,           ");
        sql.append("          PC.INVOICE_AMOUNT,           ");
        sql.append("          PC.INVOICE_COMPANY_CODE,           ");
        sql.append("          PC.INVOICE_COMPANY_NAME,           ");
        sql.append("          PC.INVOICE_DOCUMENT_NO,           ");
        sql.append("          PC.INVOICE_DOCUMENT_TYPE,           ");
        sql.append("          PC.INVOICE_FISCAL_YEAR,           ");
        sql.append("          PC.INVOICE_PAYMENT_CENTER,           ");
        sql.append("          PC.INVOICE_WTX_AMOUNT,           ");
        sql.append("          PC.INVOICE_WTX_AMOUNT_P,           ");
        sql.append("          PC.INVOICE_WTX_BASE,           ");
        sql.append("          PC.INVOICE_WTX_BASE_P,           ");
        sql.append("          PC.LINE,           ");
        sql.append("          PC.LINE_ITEM_TEXT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT,           ");
        sql.append("          PC.ORIGINAL_COMPANY_CODE,           ");
        sql.append("          PC.ORIGINAL_COMPANY_NAME,           ");
        sql.append("          PC.ORIGINAL_DOCUMENT_NO,           ");
        sql.append("          PC.ORIGINAL_DOCUMENT_TYPE,           ");
        sql.append("          PC.ORIGINAL_FISCAL_YEAR,           ");
        sql.append("          PC.ORIGINAL_PAYMENT_CENTER,           ");
        sql.append("          PC.ORIGINAL_WTX_AMOUNT,           ");
        sql.append("          PC.ORIGINAL_WTX_AMOUNT_P,           ");
        sql.append("          PC.ORIGINAL_WTX_BASE,           ");
        sql.append("          PC.ORIGINAL_WTX_BASE_P,           ");
        sql.append("          PC.PARENT_COMPANY_CODE,           ");
        sql.append("          PC.PARENT_DOCUMENT_NO,           ");
        sql.append("          PC.PARENT_FISCAL_YEAR,           ");
        sql.append("          PC.PAYMENT_ALIAS_ID,           ");
        sql.append("          PC.PAYMENT_BLOCK,           ");
        sql.append("          PC.PAYMENT_CENTER,           ");
        sql.append("          PC.PAYMENT_COMPANY_CODE,           ");
        sql.append("          PC.PAYMENT_DATE,           ");
        sql.append("          PC.PAYMENT_DATE_ACCT,           ");
        sql.append("          PC.PAYMENT_DOCUMENT_NO,           ");
        sql.append("          PC.PAYMENT_FISCAL_YEAR,           ");
        sql.append("          PC.PAYMENT_METHOD,           ");
        sql.append("          PC.PAYMENT_METHOD_NAME,           ");
        sql.append("          PC.PAYMENT_NAME,           ");
        sql.append("          PC.PAYMENT_REFERENCE,           ");
        sql.append("          PC.PAYMENT_TERM,           ");
        sql.append("          PC.PM_GROUP_DOC,           ");
        sql.append("          PC.PM_GROUP_NO,           ");
        sql.append("          PC.PO_DOCUMENT_NO,           ");
        sql.append("          PC.PO_LINE,           ");
        sql.append("          PC.POSTING_KEY,           ");
        sql.append("          PC.IS_PROPOSAL,           ");
        sql.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sql.append("          PC.REFERENCE1,           ");
        sql.append("          PC.REFERENCE2,           ");
        sql.append("          PC.REFERENCE3,           ");
        sql.append("          PC.SPECIAL_GL,           ");
        sql.append("          PC.SPECIAL_GL_NAME,           ");
        sql.append("          PC.STATUS,           ");
        sql.append("          PC.TRADING_PARTNER,           ");
        sql.append("          PC.TRADING_PARTNER_NAME,           ");
        sql.append("          PC.CREATED,           ");
        sql.append("          PC.CREATED_BY,           ");
        sql.append("          PC.UPDATED,           ");
        sql.append("          PC.UPDATED_BY,           ");
        sql.append("          PC.INVOICE_AMOUNT_PAID,           ");
        sql.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
        sql.append("          PC.FI_AREA_NAME,           ");
        sql.append("          PC.PAYMENT_COMPANY_NAME,           ");
        sql.append("          PC.REVERSE_PAYMENT_COMPANY_CODE,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_NO,           ");
        sql.append("          PC.REVERSE_PAYMENT_FISCAL_YEAR,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.IDEM_UPDATE,           ");
        sql.append("          PC.INVOICE_WTX_AMOUNTP,           ");
        sql.append("          PC.INVOICE_WTX_BASEP,           ");
        sql.append("          PC.ORIGINAL_WTX_AMOUNTP,           ");
        sql.append("          PC.ORIGINAL_WTX_BASEP,           ");
        sql.append("          PC.SPECIALGL,           ");
        sql.append("          PC.SPECIALGLNAME,           ");
        sql.append("          PC.IS_HAVE_CHILD,           ");
        sql.append("          PC.HAVE_CHILD,           ");
        sql.append("          PC.CREDIT_MEMO,           ");
        sql.append("          PC.REVERSE_PAYMENT_STATUS,           ");
        sql.append("          PC.WTX_CREDIT_MEMO,           ");
        sql.append("          PI.ID   AS PAYMENT_INFORMATION_ID,           ");
        sql.append("          PI.ACCOUNT_HOLDER_NAME,           ");
        sql.append("          PI.ADDRESS,           ");
        sql.append("          PI.CITY,           ");
        sql.append("          PI.COUNTRY,           ");
        sql.append("          PI.COUNTRY_NAME,           ");
        sql.append("          PI.DATE_DUE,           ");
        sql.append("          PI.DATE_VALUE,           ");
        sql.append("          PI.NAME1,           ");
        sql.append("          PI.NAME2,           ");
        sql.append("          PI.NAME3,           ");
        sql.append("          PI.NAME4,           ");
        sql.append("          PI.PAYEE_ADDRESS,           ");
        sql.append("          PI.PAYEE_BANK_ACCOUNT_NO,           ");
        sql.append("          PI.PAYEE_BANK_KEY,           ");
        sql.append("          PI.PAYEE_BANK_NO,           ");
        sql.append("          PI.PAYEE_BANK_REFERENCE,           ");
        sql.append("          PI.PAYEE_CITY,           ");
        sql.append("          PI.PAYEE_CODE,           ");
        sql.append("          PI.PAYEE_COUNTRY,           ");
        sql.append("          PI.PAYEE_NAME1,           ");
        sql.append("          PI.PAYEE_NAME2,           ");
        sql.append("          PI.PAYEE_NAME3,           ");
        sql.append("          PI.PAYEE_NAME4,           ");
        sql.append("          PI.PAYEE_POSTAL_CODE,           ");
        sql.append("          PI.PAYEE_TAX_ID,           ");
        sql.append("          PI.PAYEE_TITLE,           ");
        sql.append("          PI.PAYING_BANK_ACCOUNT_NO,           ");
        sql.append("          PI.PAYING_BANK_CODE,           ");
        sql.append("          PI.PAYING_BANK_COUNTRY,           ");
        sql.append("          PI.PAYING_BANK_KEY,           ");
        sql.append("          PI.PAYING_BANK_NAME,           ");
        sql.append("          PI.PAYING_BANK_NO,           ");
        sql.append("          PI.PAYING_COMPANY_CODE,           ");
        sql.append("          PI.PAYING_GL_ACCOUNT,           ");
        sql.append("          PI.PAYING_HOUSE_BANK,           ");
        sql.append("          PI.PAYMENT_SPECIAL_GL,           ");
        sql.append("          PI.POSTAL_CODE,           ");
        sql.append("          PI.SWIFT_CODE,           ");
        sql.append("          PI.VENDOR_CODE,           ");
        sql.append("          PI.VENDOR_FLAG,           ");
        sql.append("          PI.VENDOR_NAME,           ");
        sql.append("          PI.VENDOR_TAX_ID,           ");
        sql.append("          PI.VENDOR_TITLE,           ");
        sql.append("          PI.PAYEE_BANK,           ");
        sql.append("          PI.PAYEE_BANK_NAME,           ");
        sql.append("          PI.PAYINGGLACCOUNT,           ");
        sql.append("          PI.PAYMENT_SPECIALGL           ");
        sql.append("          FROM PAYMENT_PROCESS PC           ");
        sql.append("          LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID           ");
        sql.append("          WHERE PC.STATUS = 'S'           ");
        sql.append("          AND PC.IS_CHILD = '0'           ");
        if (isProposal) {
            sql.append("          AND PC.IS_PROPOSAL = '1'           ");
            sql.append("          AND PC.IS_PROPOSAL_BLOCK = '0'           ");
        } else {
            sql.append("          AND PC.IS_PROPOSAL = '0'           ");
            sql.append("          AND PC.PAYMENT_DOCUMENT_NO IS NOT NULL           ");
        }
        sql.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");
        sql.append("          ORDER BY PI.VENDOR_CODE           ");

        log.info(" sql real run : {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), prepareRealRunRowMapper, paymentAliasId);
    }

    public List<PrepareRunDocument> selectDocument(Long paymentAliasId, boolean isProposal) {
        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT PC.ACCOUNT_TYPE,           ");
        sb.append("          PC.ASSET_NO,           ");
        sb.append("          PC.ASSET_SUB_NO,           ");
        sb.append("          PC.ASSIGNMENT,           ");
        sb.append("          PC.BANK_ACCOUNT_NO,           ");
        sb.append("          PC.BR_DOCUMENT_NO,           ");
        sb.append("          PC.BR_LINE,           ");
        sb.append("          PC.BUDGET_ACCOUNT,           ");
        sb.append("          PC.BUDGET_ACTIVITY,           ");
        sb.append("          PC.BUDGET_ACTIVITY_NAME,           ");
        sb.append("          PC.IS_CHILD,           ");
        sb.append("          PC.COST_CENTER,           ");
        sb.append("          PC.COST_CENTER_NAME,           ");
        sb.append("          PC.CURRENCY,           ");
        sb.append("          PC.DATE_ACCT,           ");
        sb.append("          PC.DATE_BASE_LINE,           ");
        sb.append("          PC.DATE_DOC,           ");
        sb.append("          PC.DR_CR,           ");
        sb.append("          PC.ERROR_CODE,           ");
        sb.append("          PC.FI_AREA,           ");
        sb.append("          PC.FI_AREA_NAME,           ");
        sb.append("          PC.FUND_CENTER,           ");
        sb.append("          PC.FUND_CENTER_NAME,           ");
        sb.append("          PC.FUND_SOURCE,           ");
        sb.append("          PC.FUND_SOURCE_NAME,           ");
        sb.append("          PC.GL_ACCOUNT,           ");
        sb.append("          PC.GL_ACCOUNT_NAME,           ");
        sb.append("          PC.HEADER_REFERENCE,           ");
        sb.append("          PC.HOUSE_BANK,           ");
        sb.append("          PC.IDEM_STATUS,           ");
        sb.append("          PC.INVOICE_AMOUNT,           ");
        sb.append("          PC.INVOICE_AMOUNT_PAID,           ");
        sb.append("          PC.INVOICE_COMPANY_CODE,           ");
        sb.append("          PC.INVOICE_COMPANY_NAME,           ");
        sb.append("          PC.INVOICE_DOCUMENT_NO,           ");
        sb.append("          PC.INVOICE_DOCUMENT_TYPE,           ");
        sb.append("          PC.INVOICE_FISCAL_YEAR,           ");
        sb.append("          PC.INVOICE_PAYMENT_CENTER,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT_P,           ");
        sb.append("          PC.INVOICE_WTX_BASE,           ");
        sb.append("          PC.INVOICE_WTX_BASE_P,           ");
        sb.append("          PC.LINE,           ");
        sb.append("          PC.LINE_ITEM_TEXT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
        sb.append("          PC.ORIGINAL_COMPANY_CODE,           ");
        sb.append("          PC.ORIGINAL_COMPANY_NAME,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_NO,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_TYPE,           ");
        sb.append("          PC.ORIGINAL_FISCAL_YEAR,           ");
        sb.append("          PC.ORIGINAL_PAYMENT_CENTER,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT_P,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE_P,           ");
        sb.append("          PC.PARENT_COMPANY_CODE,           ");
        sb.append("          PC.PARENT_DOCUMENT_NO,           ");
        sb.append("          PC.PARENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_ALIAS_ID,           ");
        sb.append("          PC.PAYMENT_BLOCK,           ");
        sb.append("          PC.PAYMENT_CENTER,           ");
        sb.append("          PC.PAYMENT_COMPANY_CODE,           ");
        sb.append("          PC.PAYMENT_DATE,           ");
        sb.append("          PC.PAYMENT_DATE_ACCT,           ");
        sb.append("          PC.PAYMENT_DOCUMENT_NO,           ");
        sb.append("          PC.PAYMENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_METHOD,           ");
        sb.append("          PC.PAYMENT_METHOD_NAME,           ");
        sb.append("          PC.PAYMENT_NAME,           ");
        sb.append("          PC.PAYMENT_REFERENCE,           ");
        sb.append("          PC.PAYMENT_TERM,           ");
        sb.append("          PC.PM_GROUP_DOC,           ");
        sb.append("          PC.PM_GROUP_NO,           ");
        sb.append("          PC.PO_DOCUMENT_NO,           ");
        sb.append("          PC.PO_LINE,           ");
        sb.append("          PC.POSTING_KEY,           ");
        sb.append("          PC.IS_PROPOSAL,           ");
        sb.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sb.append("          PC.REFERENCE1,           ");
        sb.append("          PC.REFERENCE2,           ");
        sb.append("          PC.REFERENCE3,           ");
        sb.append("          PC.SPECIAL_GL,           ");
        sb.append("          PC.SPECIAL_GL_NAME,           ");
        sb.append("          PC.STATUS,           ");
        sb.append("          PC.TRADING_PARTNER,           ");
        sb.append("          PC.TRADING_PARTNER_NAME,           ");
        sb.append("          IM.ACCOUNT_HOLDER_NAME,           ");
        sb.append("          IM.ADDRESS,           ");
        sb.append("          IM.CITY,           ");
        sb.append("          IM.COUNTRY,           ");
        sb.append("          IM.COUNTRY_NAME,           ");
        sb.append("          IM.DATE_DUE,           ");
        sb.append("          IM.DATE_VALUE,           ");
        sb.append("          IM.NAME1,           ");
        sb.append("          IM.NAME2,           ");
        sb.append("          IM.NAME3,           ");
        sb.append("          IM.NAME4,           ");
        sb.append("          IM.PAYEE_ADDRESS,           ");
        sb.append("          IM.PAYEE_BANK,           ");
        sb.append("          IM.PAYEE_BANK_NAME,           ");
        sb.append("          IM.PAYEE_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYEE_BANK_KEY,           ");
        sb.append("          IM.PAYEE_BANK_NO,           ");
        sb.append("          IM.PAYEE_BANK_REFERENCE,           ");
        sb.append("          IM.PAYEE_CITY,           ");
        sb.append("          IM.PAYEE_CODE,           ");
        sb.append("          IM.PAYEE_COUNTRY,           ");
        sb.append("          IM.PAYEE_NAME1,           ");
        sb.append("          IM.PAYEE_NAME2,           ");
        sb.append("          IM.PAYEE_NAME3,           ");
        sb.append("          IM.PAYEE_NAME4,           ");
        sb.append("          IM.PAYEE_POSTAL_CODE,           ");
        sb.append("          IM.PAYEE_TAX_ID,           ");
        sb.append("          IM.PAYEE_TITLE,           ");
        sb.append("          IM.PAYING_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYING_BANK_CODE,           ");
        sb.append("          IM.PAYING_BANK_COUNTRY,           ");
        sb.append("          IM.PAYING_BANK_KEY,           ");
        sb.append("          IM.PAYING_BANK_NAME,           ");
        sb.append("          IM.PAYING_BANK_NO,           ");
        sb.append("          IM.PAYING_COMPANY_CODE,           ");
        sb.append("          IM.PAYING_GL_ACCOUNT,           ");
        sb.append("          IM.PAYING_HOUSE_BANK,           ");
        sb.append("          IM.PAYMENT_SPECIAL_GL,           ");
        sb.append("          IM.POSTAL_CODE,           ");
        sb.append("          IM.SWIFT_CODE,           ");
        sb.append("          IM.VENDOR_CODE,           ");
        sb.append("          IM.VENDOR_FLAG,           ");
        sb.append("          IM.VENDOR_NAME,           ");
        sb.append("          IM.VENDOR_TAX_ID,           ");
        sb.append("          IM.VENDOR_TITLE,           ");
        sb.append("          PC.CREDIT_MEMO,           ");
        sb.append("          PC.WTX_CREDIT_MEMO           ");
        sb.append("          FROM PAYMENT_PROCESS PC           ");
        sb.append("          LEFT JOIN PAYMENT_INFORMATION IM ON PC.ID = IM.PAYMENT_PROCESS_ID           ");
        sb.append("          WHERE PC.STATUS = 'S'           ");
        sb.append("          AND PC.IS_CHILD = '0'           ");
        if (isProposal) {
            sb.append("          AND PC.IS_PROPOSAL = '1'           ");
            sb.append("          AND PC.IS_PROPOSAL_BLOCK = '0'           ");
        } else {
            sb.append("          AND PC.IS_PROPOSAL = '0'           ");
            sb.append("          AND PC.PAYMENT_DOCUMENT_NO IS NOT NULL           ");
        }
        sb.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");
        sb.append("          ORDER BY IM.VENDOR_CODE           ");

        log.info(" sql real run : {}", sb.toString());
        return this.jdbcTemplate.query(sb.toString(), prepareRunRowMapper, paymentAliasId);
    }

    @Override
    public List<PrepareRunDocument> findRepairDocument(Long paymentProcessId, boolean isProposal) {
        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT PC.ACCOUNT_TYPE,           ");
        sb.append("          PC.ASSET_NO,           ");
        sb.append("          PC.ASSET_SUB_NO,           ");
        sb.append("          PC.ASSIGNMENT,           ");
        sb.append("          PC.BANK_ACCOUNT_NO,           ");
        sb.append("          PC.BR_DOCUMENT_NO,           ");
        sb.append("          PC.BR_LINE,           ");
        sb.append("          PC.BUDGET_ACCOUNT,           ");
        sb.append("          PC.BUDGET_ACTIVITY,           ");
        sb.append("          PC.BUDGET_ACTIVITY_NAME,           ");
        sb.append("          PC.IS_CHILD,           ");
        sb.append("          PC.COST_CENTER,           ");
        sb.append("          PC.COST_CENTER_NAME,           ");
        sb.append("          PC.CURRENCY,           ");
        sb.append("          PC.DATE_ACCT,           ");
        sb.append("          PC.DATE_BASE_LINE,           ");
        sb.append("          PC.DATE_DOC,           ");
        sb.append("          PC.DR_CR,           ");
        sb.append("          PC.ERROR_CODE,           ");
        sb.append("          PC.FI_AREA,           ");
        sb.append("          PC.FI_AREA_NAME,           ");
        sb.append("          PC.FUND_CENTER,           ");
        sb.append("          PC.FUND_CENTER_NAME,           ");
        sb.append("          PC.FUND_SOURCE,           ");
        sb.append("          PC.FUND_SOURCE_NAME,           ");
        sb.append("          PC.GL_ACCOUNT,           ");
        sb.append("          PC.GL_ACCOUNT_NAME,           ");
        sb.append("          PC.HEADER_REFERENCE,           ");
        sb.append("          PC.HOUSE_BANK,           ");
        sb.append("          PC.IDEM_STATUS,           ");
        sb.append("          PC.INVOICE_AMOUNT,           ");
        sb.append("          PC.INVOICE_AMOUNT_PAID,           ");
        sb.append("          PC.INVOICE_COMPANY_CODE,           ");
        sb.append("          PC.INVOICE_COMPANY_NAME,           ");
        sb.append("          PC.INVOICE_DOCUMENT_NO,           ");
        sb.append("          PC.INVOICE_DOCUMENT_TYPE,           ");
        sb.append("          PC.INVOICE_FISCAL_YEAR,           ");
        sb.append("          PC.INVOICE_PAYMENT_CENTER,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT_P,           ");
        sb.append("          PC.INVOICE_WTX_BASE,           ");
        sb.append("          PC.INVOICE_WTX_BASE_P,           ");
        sb.append("          PC.LINE,           ");
        sb.append("          PC.LINE_ITEM_TEXT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
        sb.append("          PC.ORIGINAL_COMPANY_CODE,           ");
        sb.append("          PC.ORIGINAL_COMPANY_NAME,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_NO,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_TYPE,           ");
        sb.append("          PC.ORIGINAL_FISCAL_YEAR,           ");
        sb.append("          PC.ORIGINAL_PAYMENT_CENTER,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT_P,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE_P,           ");
        sb.append("          PC.PARENT_COMPANY_CODE,           ");
        sb.append("          PC.PARENT_DOCUMENT_NO,           ");
        sb.append("          PC.PARENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_ALIAS_ID,           ");
        sb.append("          PC.PAYMENT_BLOCK,           ");
        sb.append("          PC.PAYMENT_CENTER,           ");
        sb.append("          PC.PAYMENT_COMPANY_CODE,           ");
        sb.append("          PC.PAYMENT_DATE,           ");
        sb.append("          PC.PAYMENT_DATE_ACCT,           ");
        sb.append("          PC.PAYMENT_DOCUMENT_NO,           ");
        sb.append("          PC.PAYMENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_METHOD,           ");
        sb.append("          PC.PAYMENT_METHOD_NAME,           ");
        sb.append("          PC.PAYMENT_NAME,           ");
        sb.append("          PC.PAYMENT_REFERENCE,           ");
        sb.append("          PC.PAYMENT_TERM,           ");
        sb.append("          PC.PM_GROUP_DOC,           ");
        sb.append("          PC.PM_GROUP_NO,           ");
        sb.append("          PC.PO_DOCUMENT_NO,           ");
        sb.append("          PC.PO_LINE,           ");
        sb.append("          PC.POSTING_KEY,           ");
        sb.append("          PC.IS_PROPOSAL,           ");
        sb.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sb.append("          PC.REFERENCE1,           ");
        sb.append("          PC.REFERENCE2,           ");
        sb.append("          PC.REFERENCE3,           ");
        sb.append("          PC.SPECIAL_GL,           ");
        sb.append("          PC.SPECIAL_GL_NAME,           ");
        sb.append("          PC.STATUS,           ");
        sb.append("          PC.TRADING_PARTNER,           ");
        sb.append("          PC.TRADING_PARTNER_NAME,           ");
        sb.append("          IM.ACCOUNT_HOLDER_NAME,           ");
        sb.append("          IM.ADDRESS,           ");
        sb.append("          IM.CITY,           ");
        sb.append("          IM.COUNTRY,           ");
        sb.append("          IM.COUNTRY_NAME,           ");
        sb.append("          IM.DATE_DUE,           ");
        sb.append("          IM.DATE_VALUE,           ");
        sb.append("          IM.NAME1,           ");
        sb.append("          IM.NAME2,           ");
        sb.append("          IM.NAME3,           ");
        sb.append("          IM.NAME4,           ");
        sb.append("          IM.PAYEE_ADDRESS,           ");
        sb.append("          IM.PAYEE_BANK,           ");
        sb.append("          IM.PAYEE_BANK_NAME,           ");
        sb.append("          IM.PAYEE_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYEE_BANK_KEY,           ");
        sb.append("          IM.PAYEE_BANK_NO,           ");
        sb.append("          IM.PAYEE_BANK_REFERENCE,           ");
        sb.append("          IM.PAYEE_CITY,           ");
        sb.append("          IM.PAYEE_CODE,           ");
        sb.append("          IM.PAYEE_COUNTRY,           ");
        sb.append("          IM.PAYEE_NAME1,           ");
        sb.append("          IM.PAYEE_NAME2,           ");
        sb.append("          IM.PAYEE_NAME3,           ");
        sb.append("          IM.PAYEE_NAME4,           ");
        sb.append("          IM.PAYEE_POSTAL_CODE,           ");
        sb.append("          IM.PAYEE_TAX_ID,           ");
        sb.append("          IM.PAYEE_TITLE,           ");
        sb.append("          IM.PAYING_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYING_BANK_CODE,           ");
        sb.append("          IM.PAYING_BANK_COUNTRY,           ");
        sb.append("          IM.PAYING_BANK_KEY,           ");
        sb.append("          IM.PAYING_BANK_NAME,           ");
        sb.append("          IM.PAYING_BANK_NO,           ");
        sb.append("          IM.PAYING_COMPANY_CODE,           ");
        sb.append("          IM.PAYING_GL_ACCOUNT,           ");
        sb.append("          IM.PAYING_HOUSE_BANK,           ");
        sb.append("          IM.PAYMENT_SPECIAL_GL,           ");
        sb.append("          IM.POSTAL_CODE,           ");
        sb.append("          IM.SWIFT_CODE,           ");
        sb.append("          IM.VENDOR_CODE,           ");
        sb.append("          IM.VENDOR_FLAG,           ");
        sb.append("          IM.VENDOR_NAME,           ");
        sb.append("          IM.VENDOR_TAX_ID,           ");
        sb.append("          IM.VENDOR_TITLE,           ");
        sb.append("          PC.CREDIT_MEMO,    ");
        sb.append("          PC.WTX_CREDIT_MEMO    ");
        sb.append("          FROM PAYMENT_PROCESS PC           ");
        sb.append("          LEFT JOIN PAYMENT_INFORMATION IM ON PC.ID = IM.PAYMENT_PROCESS_ID           ");
        sb.append("          WHERE PC.STATUS = 'S'           ");
        sb.append("          AND PC.IS_CHILD = '0'           ");
        if (isProposal) {
            sb.append("          AND PC.IS_PROPOSAL = '1'           ");
            sb.append("          AND PC.IS_PROPOSAL_BLOCK = '0'           ");
        } else {
            sb.append("          AND PC.IS_PROPOSAL = '0'           ");
        }
        sb.append("          AND PC.ID = ?           ");
        sb.append("          ORDER BY IM.VENDOR_CODE           ");

        log.info(" sql repair run : {}", sb.toString());
        return this.jdbcTemplate.query(sb.toString(), prepareRunRowMapper, paymentProcessId);
    }

    @Override
    public List<PrepareRunDocument> selectK3orKXDocument(APPaymentLine apPaymentLine, boolean isProposal,Long paymentAliasId) {
        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT PC.ACCOUNT_TYPE,           ");
        sb.append("          PC.ASSET_NO,           ");
        sb.append("          PC.ASSET_SUB_NO,           ");
        sb.append("          PC.ASSIGNMENT,           ");
        sb.append("          PC.BANK_ACCOUNT_NO,           ");
        sb.append("          PC.BR_DOCUMENT_NO,           ");
        sb.append("          PC.BR_LINE,           ");
        sb.append("          PC.BUDGET_ACCOUNT,           ");
        sb.append("          PC.BUDGET_ACTIVITY,           ");
        sb.append("          PC.BUDGET_ACTIVITY_NAME,           ");
        sb.append("          PC.IS_CHILD,           ");
        sb.append("          PC.COST_CENTER,           ");
        sb.append("          PC.COST_CENTER_NAME,           ");
        sb.append("          PC.CURRENCY,           ");
        sb.append("          PC.DATE_ACCT,           ");
        sb.append("          PC.DATE_BASE_LINE,           ");
        sb.append("          PC.DATE_DOC,           ");
        sb.append("          PC.DR_CR,           ");
        sb.append("          PC.ERROR_CODE,           ");
        sb.append("          PC.FI_AREA,           ");
        sb.append("          PC.FI_AREA_NAME,           ");
        sb.append("          PC.FUND_CENTER,           ");
        sb.append("          PC.FUND_CENTER_NAME,           ");
        sb.append("          PC.FUND_SOURCE,           ");
        sb.append("          PC.FUND_SOURCE_NAME,           ");
        sb.append("          PC.GL_ACCOUNT,           ");
        sb.append("          PC.GL_ACCOUNT_NAME,           ");
        sb.append("          PC.HEADER_REFERENCE,           ");
        sb.append("          PC.HOUSE_BANK,           ");
        sb.append("          PC.IDEM_STATUS,           ");
        sb.append("          PC.INVOICE_AMOUNT,           ");
        sb.append("          PC.INVOICE_AMOUNT_PAID,           ");
        sb.append("          PC.INVOICE_COMPANY_CODE,           ");
        sb.append("          PC.INVOICE_COMPANY_NAME,           ");
        sb.append("          PC.INVOICE_DOCUMENT_NO,           ");
        sb.append("          PC.INVOICE_DOCUMENT_TYPE,           ");
        sb.append("          PC.INVOICE_FISCAL_YEAR,           ");
        sb.append("          PC.INVOICE_PAYMENT_CENTER,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT_P,           ");
        sb.append("          PC.INVOICE_WTX_BASE,           ");
        sb.append("          PC.INVOICE_WTX_BASE_P,           ");
        sb.append("          PC.LINE,           ");
        sb.append("          PC.LINE_ITEM_TEXT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
        sb.append("          PC.ORIGINAL_COMPANY_CODE,           ");
        sb.append("          PC.ORIGINAL_COMPANY_NAME,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_NO,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_TYPE,           ");
        sb.append("          PC.ORIGINAL_FISCAL_YEAR,           ");
        sb.append("          PC.ORIGINAL_PAYMENT_CENTER,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT_P,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE_P,           ");
        sb.append("          PC.PARENT_COMPANY_CODE,           ");
        sb.append("          PC.PARENT_DOCUMENT_NO,           ");
        sb.append("          PC.PARENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_ALIAS_ID,           ");
        sb.append("          PC.PAYMENT_BLOCK,           ");
        sb.append("          PC.PAYMENT_CENTER,           ");
        sb.append("          PC.PAYMENT_COMPANY_CODE,           ");
        sb.append("          PC.PAYMENT_DATE,           ");
        sb.append("          PC.PAYMENT_DATE_ACCT,           ");
        sb.append("          PC.PAYMENT_DOCUMENT_NO,           ");
        sb.append("          PC.PAYMENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_METHOD,           ");
        sb.append("          PC.PAYMENT_METHOD_NAME,           ");
        sb.append("          PC.PAYMENT_NAME,           ");
        sb.append("          PC.PAYMENT_REFERENCE,           ");
        sb.append("          PC.PAYMENT_TERM,           ");
        sb.append("          PC.PM_GROUP_DOC,           ");
        sb.append("          PC.PM_GROUP_NO,           ");
        sb.append("          PC.PO_DOCUMENT_NO,           ");
        sb.append("          PC.PO_LINE,           ");
        sb.append("          PC.POSTING_KEY,           ");
        sb.append("          PC.IS_PROPOSAL,           ");
        sb.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sb.append("          PC.REFERENCE1,           ");
        sb.append("          PC.REFERENCE2,           ");
        sb.append("          PC.REFERENCE3,           ");
        sb.append("          PC.SPECIAL_GL,           ");
        sb.append("          PC.SPECIAL_GL_NAME,           ");
        sb.append("          PC.STATUS,           ");
        sb.append("          PC.TRADING_PARTNER,           ");
        sb.append("          PC.TRADING_PARTNER_NAME,           ");
        sb.append("          IM.ACCOUNT_HOLDER_NAME,           ");
        sb.append("          IM.ADDRESS,           ");
        sb.append("          IM.CITY,           ");
        sb.append("          IM.COUNTRY,           ");
        sb.append("          IM.COUNTRY_NAME,           ");
        sb.append("          IM.DATE_DUE,           ");
        sb.append("          IM.DATE_VALUE,           ");
        sb.append("          IM.NAME1,           ");
        sb.append("          IM.NAME2,           ");
        sb.append("          IM.NAME3,           ");
        sb.append("          IM.NAME4,           ");
        sb.append("          IM.PAYEE_ADDRESS,           ");
        sb.append("          IM.PAYEE_BANK,           ");
        sb.append("          IM.PAYEE_BANK_NAME,           ");
        sb.append("          IM.PAYEE_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYEE_BANK_KEY,           ");
        sb.append("          IM.PAYEE_BANK_NO,           ");
        sb.append("          IM.PAYEE_BANK_REFERENCE,           ");
        sb.append("          IM.PAYEE_CITY,           ");
        sb.append("          IM.PAYEE_CODE,           ");
        sb.append("          IM.PAYEE_COUNTRY,           ");
        sb.append("          IM.PAYEE_NAME1,           ");
        sb.append("          IM.PAYEE_NAME2,           ");
        sb.append("          IM.PAYEE_NAME3,           ");
        sb.append("          IM.PAYEE_NAME4,           ");
        sb.append("          IM.PAYEE_POSTAL_CODE,           ");
        sb.append("          IM.PAYEE_TAX_ID,           ");
        sb.append("          IM.PAYEE_TITLE,           ");
        sb.append("          IM.PAYING_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYING_BANK_CODE,           ");
        sb.append("          IM.PAYING_BANK_COUNTRY,           ");
        sb.append("          IM.PAYING_BANK_KEY,           ");
        sb.append("          IM.PAYING_BANK_NAME,           ");
        sb.append("          IM.PAYING_BANK_NO,           ");
        sb.append("          IM.PAYING_COMPANY_CODE,           ");
        sb.append("          IM.PAYING_GL_ACCOUNT,           ");
        sb.append("          IM.PAYING_HOUSE_BANK,           ");
        sb.append("          IM.PAYMENT_SPECIAL_GL,           ");
        sb.append("          IM.POSTAL_CODE,           ");
        sb.append("          IM.SWIFT_CODE,           ");
        sb.append("          IM.VENDOR_CODE,           ");
        sb.append("          IM.VENDOR_FLAG,           ");
        sb.append("          IM.VENDOR_NAME,           ");
        sb.append("          IM.VENDOR_TAX_ID,           ");
        sb.append("          IM.VENDOR_TITLE,          ");
        sb.append("          PC.CREDIT_MEMO,          ");
        sb.append("          PC.WTX_CREDIT_MEMO    ");
        sb.append("          FROM PAYMENT_PROCESS PC           ");
        sb.append("          LEFT JOIN PAYMENT_INFORMATION IM ON PC.ID = IM.PAYMENT_PROCESS_ID           ");
        sb.append("          WHERE PC.STATUS = 'S'           ");
        sb.append("          AND PC.IS_CHILD = '1'           ");
        if (isProposal) {
            sb.append("          AND PC.IS_PROPOSAL = '1'           ");
            sb.append("          AND PC.IS_PROPOSAL_BLOCK = '0'           ");
        } else {
            sb.append("          AND PC.IS_PROPOSAL = '0'           ");
        }

        sb.append("        AND  PC.PARENT_COMPANY_CODE = ?           ");
        sb.append("        AND  PC.PARENT_DOCUMENT_NO = ?           ");
        sb.append("        AND  PC.PARENT_FISCAL_YEAR = ?         ");
        sb.append("        AND  PC.PAYMENT_ALIAS_ID = ?         ");

        log.info(" sql real run k3 kx : {}", sb.toString());
        log.info(" PARENT_COMPANY_CODE : {}", apPaymentLine.getInvCompCode());
        log.info(" PARENT_DOCUMENT_NO : {}", apPaymentLine.getInvDocNo());
        log.info(" PARENT_FISCAL_YEAR : {}", apPaymentLine.getInvFiscalYear());
        return this.jdbcTemplate.query(sb.toString(), prepareRunRowMapper, apPaymentLine.getInvCompCode(), apPaymentLine.getInvDocNo(), apPaymentLine.getInvFiscalYear(),paymentAliasId);
    }

    @Override
    public List<PrepareRunDocument> selectDocumentForGen(Long paymentAliasId, boolean isProposal) {
        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT PC.ACCOUNT_TYPE,           ");
        sb.append("          PC.ASSET_NO,           ");
        sb.append("          PC.ASSET_SUB_NO,           ");
        sb.append("          PC.ASSIGNMENT,           ");
        sb.append("          PC.BANK_ACCOUNT_NO,           ");
        sb.append("          PC.BR_DOCUMENT_NO,           ");
        sb.append("          PC.BR_LINE,           ");
        sb.append("          PC.BUDGET_ACCOUNT,           ");
        sb.append("          PC.BUDGET_ACTIVITY,           ");
        sb.append("          PC.BUDGET_ACTIVITY_NAME,           ");
        sb.append("          PC.IS_CHILD,           ");
        sb.append("          PC.COST_CENTER,           ");
        sb.append("          PC.COST_CENTER_NAME,           ");
        sb.append("          PC.CURRENCY,           ");
        sb.append("          PC.DATE_ACCT,           ");
        sb.append("          PC.DATE_BASE_LINE,           ");
        sb.append("          PC.DATE_DOC,           ");
        sb.append("          PC.DR_CR,           ");
        sb.append("          PC.ERROR_CODE,           ");
        sb.append("          PC.FI_AREA,           ");
        sb.append("          PC.FI_AREA_NAME,           ");
        sb.append("          PC.FUND_CENTER,           ");
        sb.append("          PC.FUND_CENTER_NAME,           ");
        sb.append("          PC.FUND_SOURCE,           ");
        sb.append("          PC.FUND_SOURCE_NAME,           ");
        sb.append("          PC.GL_ACCOUNT,           ");
        sb.append("          PC.GL_ACCOUNT_NAME,           ");
        sb.append("          PC.HEADER_REFERENCE,           ");
        sb.append("          PC.HOUSE_BANK,           ");
        sb.append("          PC.IDEM_STATUS,           ");
        sb.append("          PC.INVOICE_AMOUNT,           ");
        sb.append("          PC.INVOICE_AMOUNT_PAID,           ");
        sb.append("          PC.INVOICE_COMPANY_CODE,           ");
        sb.append("          PC.INVOICE_COMPANY_NAME,           ");
        sb.append("          PC.INVOICE_DOCUMENT_NO,           ");
        sb.append("          PC.INVOICE_DOCUMENT_TYPE,           ");
        sb.append("          PC.INVOICE_FISCAL_YEAR,           ");
        sb.append("          PC.INVOICE_PAYMENT_CENTER,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT_P,           ");
        sb.append("          PC.INVOICE_WTX_BASE,           ");
        sb.append("          PC.INVOICE_WTX_BASE_P,           ");
        sb.append("          PC.LINE,           ");
        sb.append("          PC.LINE_ITEM_TEXT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
        sb.append("          PC.ORIGINAL_COMPANY_CODE,           ");
        sb.append("          PC.ORIGINAL_COMPANY_NAME,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_NO,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_TYPE,           ");
        sb.append("          PC.ORIGINAL_FISCAL_YEAR,           ");
        sb.append("          PC.ORIGINAL_PAYMENT_CENTER,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT_P,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE_P,           ");
        sb.append("          PC.PARENT_COMPANY_CODE,           ");
        sb.append("          PC.PARENT_DOCUMENT_NO,           ");
        sb.append("          PC.PARENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_ALIAS_ID,           ");
        sb.append("          PC.PAYMENT_BLOCK,           ");
        sb.append("          PC.PAYMENT_CENTER,           ");
        sb.append("          PC.PAYMENT_COMPANY_CODE,           ");
        sb.append("          PC.PAYMENT_DATE,           ");
        sb.append("          PC.PAYMENT_DATE_ACCT,           ");
        sb.append("          PC.PAYMENT_DOCUMENT_NO,           ");
        sb.append("          PC.PAYMENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_METHOD,           ");
        sb.append("          PC.PAYMENT_METHOD_NAME,           ");
        sb.append("          PC.PAYMENT_NAME,           ");
        sb.append("          PC.PAYMENT_REFERENCE,           ");
        sb.append("          PC.PAYMENT_TERM,           ");
        sb.append("          PC.PM_GROUP_DOC,           ");
        sb.append("          PC.PM_GROUP_NO,           ");
        sb.append("          PC.PO_DOCUMENT_NO,           ");
        sb.append("          PC.PO_LINE,           ");
        sb.append("          PC.POSTING_KEY,           ");
        sb.append("          PC.IS_PROPOSAL,           ");
        sb.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sb.append("          PC.REFERENCE1,           ");
        sb.append("          PC.REFERENCE2,           ");
        sb.append("          PC.REFERENCE3,           ");
        sb.append("          PC.SPECIAL_GL,           ");
        sb.append("          PC.SPECIAL_GL_NAME,           ");
        sb.append("          PC.STATUS,           ");
        sb.append("          PC.TRADING_PARTNER,           ");
        sb.append("          PC.TRADING_PARTNER_NAME,           ");
        sb.append("          IM.ACCOUNT_HOLDER_NAME,           ");
        sb.append("          IM.ADDRESS,           ");
        sb.append("          IM.CITY,           ");
        sb.append("          IM.COUNTRY,           ");
        sb.append("          IM.COUNTRY_NAME,           ");
        sb.append("          IM.DATE_DUE,           ");
        sb.append("          IM.DATE_VALUE,           ");
        sb.append("          IM.NAME1,           ");
        sb.append("          IM.NAME2,           ");
        sb.append("          IM.NAME3,           ");
        sb.append("          IM.NAME4,           ");
        sb.append("          IM.PAYEE_ADDRESS,           ");
        sb.append("          IM.PAYEE_BANK,           ");
        sb.append("          IM.PAYEE_BANK_NAME,           ");
        sb.append("          IM.PAYEE_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYEE_BANK_KEY,           ");
        sb.append("          IM.PAYEE_BANK_NO,           ");
        sb.append("          IM.PAYEE_BANK_REFERENCE,           ");
        sb.append("          IM.PAYEE_CITY,           ");
        sb.append("          IM.PAYEE_CODE,           ");
        sb.append("          IM.PAYEE_COUNTRY,           ");
        sb.append("          IM.PAYEE_NAME1,           ");
        sb.append("          IM.PAYEE_NAME2,           ");
        sb.append("          IM.PAYEE_NAME3,           ");
        sb.append("          IM.PAYEE_NAME4,           ");
        sb.append("          IM.PAYEE_POSTAL_CODE,           ");
        sb.append("          IM.PAYEE_TAX_ID,           ");
        sb.append("          IM.PAYEE_TITLE,           ");
        sb.append("          IM.PAYING_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYING_BANK_CODE,           ");
        sb.append("          IM.PAYING_BANK_COUNTRY,           ");
        sb.append("          IM.PAYING_BANK_KEY,           ");
        sb.append("          IM.PAYING_BANK_NAME,           ");
        sb.append("          IM.PAYING_BANK_NO,           ");
        sb.append("          IM.PAYING_COMPANY_CODE,           ");
        sb.append("          IM.PAYING_GL_ACCOUNT,           ");
        sb.append("          IM.PAYING_HOUSE_BANK,           ");
        sb.append("          IM.PAYMENT_SPECIAL_GL,           ");
        sb.append("          IM.POSTAL_CODE,           ");
        sb.append("          IM.SWIFT_CODE,           ");
        sb.append("          IM.VENDOR_CODE,           ");
        sb.append("          IM.VENDOR_FLAG,           ");
        sb.append("          IM.VENDOR_NAME,           ");
        sb.append("          IM.VENDOR_TAX_ID,           ");
        sb.append("          IM.VENDOR_TITLE,           ");
        sb.append("          NVL(PC.CREDIT_MEMO, 0) AS CREDIT_MEMO,           ");
        sb.append("          NVL(PC.WTX_CREDIT_MEMO, 0) AS WTX_CREDIT_MEMO    ");
        sb.append("          FROM PAYMENT_PROCESS PC           ");
        sb.append("          LEFT JOIN PAYMENT_INFORMATION IM ON PC.ID = IM.PAYMENT_PROCESS_ID           ");
        sb.append("          WHERE PC.STATUS = 'S'           ");
        sb.append("          AND PC.PAYMENT_REFERENCE IS NOT NULL           ");
        if (isProposal) {
            sb.append("          AND PC.IS_PROPOSAL = 1           ");
            sb.append("          AND PC.IS_PROPOSAL_BLOCK = 0           ");
        } else {
            sb.append("          AND PC.IS_PROPOSAL = 0           ");
            sb.append("          AND PC.PAYMENT_DOCUMENT_NO IS NOT NULL           ");
            sb.append("          AND PC.PAYMENT_DOCUMENT_NO <> 'XXXXXXXXXX'           ");
            sb.append("          AND PC.IDEM_STATUS = 'S'           ");
        }
        sb.append("          AND PC.REVERSE_PAYMENT_COMPANY_CODE IS NULL AND PC.REVERSE_PAYMENT_DOCUMENT_NO IS NULL AND PC.REVERSE_PAYMENT_DOCUMENT_TYPE IS NULL AND PC.REVERSE_PAYMENT_FISCAL_YEAR IS NULL           ");
        sb.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");
        sb.append("          ORDER BY IM.VENDOR_CODE, PC.PAYMENT_DOCUMENT_NO, PC.IS_CHILD           ");

        log.info(" sql real run : {}", sb.toString());
        this.jdbcTemplate.setFetchSize(5000);
        return this.jdbcTemplate.query(sb.toString(), prepareRunRowMapper, paymentAliasId);
    }

    @Override
    public List<PrepareRunDocument> selectDocumentForReGen(Long paymentAliasId, String fileName, boolean isProposal) {
        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT PC.ACCOUNT_TYPE,           ");
        sb.append("          PC.ASSET_NO,           ");
        sb.append("          PC.ASSET_SUB_NO,           ");
        sb.append("          PC.ASSIGNMENT,           ");
        sb.append("          PC.BANK_ACCOUNT_NO,           ");
        sb.append("          PC.BR_DOCUMENT_NO,           ");
        sb.append("          PC.BR_LINE,           ");
        sb.append("          PC.BUDGET_ACCOUNT,           ");
        sb.append("          PC.BUDGET_ACTIVITY,           ");
        sb.append("          PC.BUDGET_ACTIVITY_NAME,           ");
        sb.append("          PC.IS_CHILD,           ");
        sb.append("          PC.COST_CENTER,           ");
        sb.append("          PC.COST_CENTER_NAME,           ");
        sb.append("          PC.CURRENCY,           ");
        sb.append("          PC.DATE_ACCT,           ");
        sb.append("          PC.DATE_BASE_LINE,           ");
        sb.append("          PC.DATE_DOC,           ");
        sb.append("          PC.DR_CR,           ");
        sb.append("          PC.ERROR_CODE,           ");
        sb.append("          PC.FI_AREA,           ");
        sb.append("          PC.FI_AREA_NAME,           ");
        sb.append("          PC.FUND_CENTER,           ");
        sb.append("          PC.FUND_CENTER_NAME,           ");
        sb.append("          PC.FUND_SOURCE,           ");
        sb.append("          PC.FUND_SOURCE_NAME,           ");
        sb.append("          PC.GL_ACCOUNT,           ");
        sb.append("          PC.GL_ACCOUNT_NAME,           ");
        sb.append("          PC.HEADER_REFERENCE,           ");
        sb.append("          PC.HOUSE_BANK,           ");
        sb.append("          PC.IDEM_STATUS,           ");
        sb.append("          PC.INVOICE_AMOUNT,           ");
        sb.append("          PC.INVOICE_AMOUNT_PAID,           ");
        sb.append("          PC.INVOICE_COMPANY_CODE,           ");
        sb.append("          PC.INVOICE_COMPANY_NAME,           ");
        sb.append("          PC.INVOICE_DOCUMENT_NO,           ");
        sb.append("          PC.INVOICE_DOCUMENT_TYPE,           ");
        sb.append("          PC.INVOICE_FISCAL_YEAR,           ");
        sb.append("          PC.INVOICE_PAYMENT_CENTER,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT_P,           ");
        sb.append("          PC.INVOICE_WTX_BASE,           ");
        sb.append("          PC.INVOICE_WTX_BASE_P,           ");
        sb.append("          PC.LINE,           ");
        sb.append("          PC.LINE_ITEM_TEXT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
        sb.append("          PC.ORIGINAL_COMPANY_CODE,           ");
        sb.append("          PC.ORIGINAL_COMPANY_NAME,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_NO,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_TYPE,           ");
        sb.append("          PC.ORIGINAL_FISCAL_YEAR,           ");
        sb.append("          PC.ORIGINAL_PAYMENT_CENTER,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT_P,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE_P,           ");
        sb.append("          PC.PARENT_COMPANY_CODE,           ");
        sb.append("          PC.PARENT_DOCUMENT_NO,           ");
        sb.append("          PC.PARENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_ALIAS_ID,           ");
        sb.append("          PC.PAYMENT_BLOCK,           ");
        sb.append("          PC.PAYMENT_CENTER,           ");
        sb.append("          PC.PAYMENT_COMPANY_CODE,           ");
        sb.append("          PC.PAYMENT_DATE,           ");
        sb.append("          PC.PAYMENT_DATE_ACCT,           ");
        sb.append("          PC.PAYMENT_DOCUMENT_NO,           ");
        sb.append("          PC.PAYMENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_METHOD,           ");
        sb.append("          PC.PAYMENT_METHOD_NAME,           ");
        sb.append("          PC.PAYMENT_NAME,           ");
        sb.append("          PC.PAYMENT_REFERENCE,           ");
        sb.append("          PC.PAYMENT_TERM,           ");
        sb.append("          PC.PM_GROUP_DOC,           ");
        sb.append("          PC.PM_GROUP_NO,           ");
        sb.append("          PC.PO_DOCUMENT_NO,           ");
        sb.append("          PC.PO_LINE,           ");
        sb.append("          PC.POSTING_KEY,           ");
        sb.append("          PC.IS_PROPOSAL,           ");
        sb.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sb.append("          PC.REFERENCE1,           ");
        sb.append("          PC.REFERENCE2,           ");
        sb.append("          PC.REFERENCE3,           ");
        sb.append("          PC.SPECIAL_GL,           ");
        sb.append("          PC.SPECIAL_GL_NAME,           ");
        sb.append("          PC.STATUS,           ");
        sb.append("          PC.TRADING_PARTNER,           ");
        sb.append("          PC.TRADING_PARTNER_NAME,           ");
        sb.append("          IM.ACCOUNT_HOLDER_NAME,           ");
        sb.append("          IM.ADDRESS,           ");
        sb.append("          IM.CITY,           ");
        sb.append("          IM.COUNTRY,           ");
        sb.append("          IM.COUNTRY_NAME,           ");
        sb.append("          IM.DATE_DUE,           ");
        sb.append("          IM.DATE_VALUE,           ");
        sb.append("          IM.NAME1,           ");
        sb.append("          IM.NAME2,           ");
        sb.append("          IM.NAME3,           ");
        sb.append("          IM.NAME4,           ");
        sb.append("          IM.PAYEE_ADDRESS,           ");
        sb.append("          IM.PAYEE_BANK,           ");
        sb.append("          IM.PAYEE_BANK_NAME,           ");
        sb.append("          IM.PAYEE_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYEE_BANK_KEY,           ");
        sb.append("          IM.PAYEE_BANK_NO,           ");
        sb.append("          IM.PAYEE_BANK_REFERENCE,           ");
        sb.append("          IM.PAYEE_CITY,           ");
        sb.append("          IM.PAYEE_CODE,           ");
        sb.append("          IM.PAYEE_COUNTRY,           ");
        sb.append("          IM.PAYEE_NAME1,           ");
        sb.append("          IM.PAYEE_NAME2,           ");
        sb.append("          IM.PAYEE_NAME3,           ");
        sb.append("          IM.PAYEE_NAME4,           ");
        sb.append("          IM.PAYEE_POSTAL_CODE,           ");
        sb.append("          IM.PAYEE_TAX_ID,           ");
        sb.append("          IM.PAYEE_TITLE,           ");
        sb.append("          IM.PAYING_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYING_BANK_CODE,           ");
        sb.append("          IM.PAYING_BANK_COUNTRY,           ");
        sb.append("          IM.PAYING_BANK_KEY,           ");
        sb.append("          IM.PAYING_BANK_NAME,           ");
        sb.append("          IM.PAYING_BANK_NO,           ");
        sb.append("          IM.PAYING_COMPANY_CODE,           ");
        sb.append("          IM.PAYING_GL_ACCOUNT,           ");
        sb.append("          IM.PAYING_HOUSE_BANK,           ");
        sb.append("          IM.PAYMENT_SPECIAL_GL,           ");
        sb.append("          IM.POSTAL_CODE,           ");
        sb.append("          IM.SWIFT_CODE,           ");
        sb.append("          IM.VENDOR_CODE,           ");
        sb.append("          IM.VENDOR_FLAG,           ");
        sb.append("          IM.VENDOR_NAME,           ");
        sb.append("          IM.VENDOR_TAX_ID,           ");
        sb.append("          IM.VENDOR_TITLE,           ");
        sb.append("          NVL(PC.CREDIT_MEMO, 0) AS CREDIT_MEMO,           ");
        sb.append("          NVL(PC.WTX_CREDIT_MEMO, 0) AS WTX_CREDIT_MEMO    ");
        sb.append("          FROM PAYMENT_PROCESS PC           ");
        sb.append("          LEFT JOIN PAYMENT_INFORMATION IM ON PC.ID = IM.PAYMENT_PROCESS_ID           ");
        sb.append("          WHERE PC.STATUS = 'S'           ");
        sb.append("          AND PC.PAYMENT_REFERENCE IS NOT NULL           ");
        if (isProposal) {
            sb.append("          AND PC.IS_PROPOSAL = 1           ");
            sb.append("          AND PC.IS_PROPOSAL_BLOCK = 0           ");
        } else {
            sb.append("          AND PC.IS_PROPOSAL = 0           ");
            sb.append("          AND PC.PAYMENT_DOCUMENT_NO IS NOT NULL           ");
            sb.append("          AND PC.PAYMENT_DOCUMENT_NO <> 'XXXXXXXXXX'           ");
            sb.append("          AND PC.IDEM_STATUS = 'S'           ");
        }
        sb.append("          AND PC.REVERSE_PAYMENT_COMPANY_CODE IS NULL AND PC.REVERSE_PAYMENT_DOCUMENT_NO IS NULL AND PC.REVERSE_PAYMENT_DOCUMENT_TYPE IS NULL AND PC.REVERSE_PAYMENT_FISCAL_YEAR IS NULL           ");
        sb.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");
        sb.append("          AND PC.PAYMENT_DOCUMENT_NO IN (SELECT PAYMENT_DOCUMENT FROM PROPOSAL_LOG WHERE FILE_NAME = ?)           ");
        sb.append("          ORDER BY IM.VENDOR_CODE, PC.PAYMENT_DOCUMENT_NO, PC.IS_CHILD           ");

        log.info(" sql real run : {}", sb.toString());
        this.jdbcTemplate.setFetchSize(5000);
        return this.jdbcTemplate.query(sb.toString(), prepareRunRowMapper, paymentAliasId, fileName);
    }

    @Override
    public List<PrepareRunDocument> findChild(Long paymentAliasId, String compCode, String documentNo, String fiscalYear) {
        StringBuilder sb = new StringBuilder();
        sb.append("          SELECT PC.ACCOUNT_TYPE,           ");
        sb.append("          PC.ASSET_NO,           ");
        sb.append("          PC.ASSET_SUB_NO,           ");
        sb.append("          PC.ASSIGNMENT,           ");
        sb.append("          PC.BANK_ACCOUNT_NO,           ");
        sb.append("          PC.BR_DOCUMENT_NO,           ");
        sb.append("          PC.BR_LINE,           ");
        sb.append("          PC.BUDGET_ACCOUNT,           ");
        sb.append("          PC.BUDGET_ACTIVITY,           ");
        sb.append("          PC.BUDGET_ACTIVITY_NAME,           ");
        sb.append("          PC.IS_CHILD,           ");
        sb.append("          PC.COST_CENTER,           ");
        sb.append("          PC.COST_CENTER_NAME,           ");
        sb.append("          PC.CURRENCY,           ");
        sb.append("          PC.DATE_ACCT,           ");
        sb.append("          PC.DATE_BASE_LINE,           ");
        sb.append("          PC.DATE_DOC,           ");
        sb.append("          PC.DR_CR,           ");
        sb.append("          PC.ERROR_CODE,           ");
        sb.append("          PC.FI_AREA,           ");
        sb.append("          PC.FI_AREA_NAME,           ");
        sb.append("          PC.FUND_CENTER,           ");
        sb.append("          PC.FUND_CENTER_NAME,           ");
        sb.append("          PC.FUND_SOURCE,           ");
        sb.append("          PC.FUND_SOURCE_NAME,           ");
        sb.append("          PC.GL_ACCOUNT,           ");
        sb.append("          PC.GL_ACCOUNT_NAME,           ");
        sb.append("          PC.HEADER_REFERENCE,           ");
        sb.append("          PC.HOUSE_BANK,           ");
        sb.append("          PC.IDEM_STATUS,           ");
        sb.append("          PC.INVOICE_AMOUNT,           ");
        sb.append("          PC.INVOICE_AMOUNT_PAID,           ");
        sb.append("          PC.INVOICE_COMPANY_CODE,           ");
        sb.append("          PC.INVOICE_COMPANY_NAME,           ");
        sb.append("          PC.INVOICE_DOCUMENT_NO,           ");
        sb.append("          PC.INVOICE_DOCUMENT_TYPE,           ");
        sb.append("          PC.INVOICE_FISCAL_YEAR,           ");
        sb.append("          PC.INVOICE_PAYMENT_CENTER,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT,           ");
        sb.append("          PC.INVOICE_WTX_AMOUNT_P,           ");
        sb.append("          PC.INVOICE_WTX_BASE,           ");
        sb.append("          PC.INVOICE_WTX_BASE_P,           ");
        sb.append("          PC.LINE,           ");
        sb.append("          PC.LINE_ITEM_TEXT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
        sb.append("          PC.ORIGINAL_COMPANY_CODE,           ");
        sb.append("          PC.ORIGINAL_COMPANY_NAME,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_NO,           ");
        sb.append("          PC.ORIGINAL_DOCUMENT_TYPE,           ");
        sb.append("          PC.ORIGINAL_FISCAL_YEAR,           ");
        sb.append("          PC.ORIGINAL_PAYMENT_CENTER,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT,           ");
        sb.append("          PC.ORIGINAL_WTX_AMOUNT_P,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE,           ");
        sb.append("          PC.ORIGINAL_WTX_BASE_P,           ");
        sb.append("          PC.PARENT_COMPANY_CODE,           ");
        sb.append("          PC.PARENT_DOCUMENT_NO,           ");
        sb.append("          PC.PARENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_ALIAS_ID,           ");
        sb.append("          PC.PAYMENT_BLOCK,           ");
        sb.append("          PC.PAYMENT_CENTER,           ");
        sb.append("          PC.PAYMENT_COMPANY_CODE,           ");
        sb.append("          PC.PAYMENT_DATE,           ");
        sb.append("          PC.PAYMENT_DATE_ACCT,           ");
        sb.append("          PC.PAYMENT_DOCUMENT_NO,           ");
        sb.append("          PC.PAYMENT_FISCAL_YEAR,           ");
        sb.append("          PC.PAYMENT_METHOD,           ");
        sb.append("          PC.PAYMENT_METHOD_NAME,           ");
        sb.append("          PC.PAYMENT_NAME,           ");
        sb.append("          PC.PAYMENT_REFERENCE,           ");
        sb.append("          PC.PAYMENT_TERM,           ");
        sb.append("          PC.PM_GROUP_DOC,           ");
        sb.append("          PC.PM_GROUP_NO,           ");
        sb.append("          PC.PO_DOCUMENT_NO,           ");
        sb.append("          PC.PO_LINE,           ");
        sb.append("          PC.POSTING_KEY,           ");
        sb.append("          PC.IS_PROPOSAL,           ");
        sb.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sb.append("          PC.REFERENCE1,           ");
        sb.append("          PC.REFERENCE2,           ");
        sb.append("          PC.REFERENCE3,           ");
        sb.append("          PC.SPECIAL_GL,           ");
        sb.append("          PC.SPECIAL_GL_NAME,           ");
        sb.append("          PC.STATUS,           ");
        sb.append("          PC.TRADING_PARTNER,           ");
        sb.append("          PC.TRADING_PARTNER_NAME,           ");
        sb.append("          IM.ACCOUNT_HOLDER_NAME,           ");
        sb.append("          IM.ADDRESS,           ");
        sb.append("          IM.CITY,           ");
        sb.append("          IM.COUNTRY,           ");
        sb.append("          IM.COUNTRY_NAME,           ");
        sb.append("          IM.DATE_DUE,           ");
        sb.append("          IM.DATE_VALUE,           ");
        sb.append("          IM.NAME1,           ");
        sb.append("          IM.NAME2,           ");
        sb.append("          IM.NAME3,           ");
        sb.append("          IM.NAME4,           ");
        sb.append("          IM.PAYEE_ADDRESS,           ");
        sb.append("          IM.PAYEE_BANK,           ");
        sb.append("          IM.PAYEE_BANK_NAME,           ");
        sb.append("          IM.PAYEE_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYEE_BANK_KEY,           ");
        sb.append("          IM.PAYEE_BANK_NO,           ");
        sb.append("          IM.PAYEE_BANK_REFERENCE,           ");
        sb.append("          IM.PAYEE_CITY,           ");
        sb.append("          IM.PAYEE_CODE,           ");
        sb.append("          IM.PAYEE_COUNTRY,           ");
        sb.append("          IM.PAYEE_NAME1,           ");
        sb.append("          IM.PAYEE_NAME2,           ");
        sb.append("          IM.PAYEE_NAME3,           ");
        sb.append("          IM.PAYEE_NAME4,           ");
        sb.append("          IM.PAYEE_POSTAL_CODE,           ");
        sb.append("          IM.PAYEE_TAX_ID,           ");
        sb.append("          IM.PAYEE_TITLE,           ");
        sb.append("          IM.PAYING_BANK_ACCOUNT_NO,           ");
        sb.append("          IM.PAYING_BANK_CODE,           ");
        sb.append("          IM.PAYING_BANK_COUNTRY,           ");
        sb.append("          IM.PAYING_BANK_KEY,           ");
        sb.append("          IM.PAYING_BANK_NAME,           ");
        sb.append("          IM.PAYING_BANK_NO,           ");
        sb.append("          IM.PAYING_COMPANY_CODE,           ");
        sb.append("          IM.PAYING_GL_ACCOUNT,           ");
        sb.append("          IM.PAYING_HOUSE_BANK,           ");
        sb.append("          IM.PAYMENT_SPECIAL_GL,           ");
        sb.append("          IM.POSTAL_CODE,           ");
        sb.append("          IM.SWIFT_CODE,           ");
        sb.append("          IM.VENDOR_CODE,           ");
        sb.append("          IM.VENDOR_FLAG,           ");
        sb.append("          IM.VENDOR_NAME,           ");
        sb.append("          IM.VENDOR_TAX_ID,           ");
        sb.append("          IM.VENDOR_TITLE,           ");
        sb.append("          PC.CREDIT_MEMO,           ");
        sb.append("          PC.WTX_CREDIT_MEMO    ");
        sb.append("          FROM PAYMENT_PROCESS PC           ");
        sb.append("          LEFT JOIN PAYMENT_INFORMATION IM ON PC.ID = IM.PAYMENT_PROCESS_ID           ");
        sb.append("          WHERE PC.STATUS = 'S'           ");
        sb.append("          AND PC.IS_PROPOSAL = 0           ");
        sb.append("          AND PC.IS_CHILD = 1           ");
        sb.append("          AND PC.REVERSE_PAYMENT_COMPANY_CODE IS NULL AND PC.REVERSE_PAYMENT_DOCUMENT_NO IS NULL AND PC.REVERSE_PAYMENT_DOCUMENT_TYPE IS NULL AND PC.REVERSE_PAYMENT_FISCAL_YEAR IS NULL           ");
        sb.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");
        sb.append("          AND PC.PARENT_COMPANY_CODE = ?           ");
        sb.append("          AND PC.PARENT_DOCUMENT_NO = ?           ");
        sb.append("          AND PC.PARENT_FISCAL_YEAR = ?           ");
        sb.append("          ORDER BY IM.VENDOR_CODE, PC.PAYMENT_DOCUMENT_NO, PC.IS_CHILD           ");

        log.info(" sql real run : {}", sb);
        this.jdbcTemplate.setFetchSize(5000);
        return this.jdbcTemplate.query(sb.toString(), prepareRunRowMapper, paymentAliasId, compCode, documentNo, fiscalYear);
    }
}
