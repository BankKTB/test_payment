package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.PaymentInformation;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.model.payment.dto.*;
import th.com.bloomcode.paymentservice.model.request.DuplicatePaymentReport;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentReportRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@Slf4j
public class PaymentReportRepositoryImpl extends MetadataJdbcRepository<PaymentReport, Long> implements PaymentReportRepository {

    static BeanPropertyRowMapper<PaymentAlias> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PaymentAlias.class);

    private final JdbcTemplate jdbcTemplate;


    static RowMapper<PaymentReport> rowMapperResponse = (rs, rowNum) -> new PaymentReport(
            rs.getLong(PaymentProcess.COLUMN_NAME_PAYMENT_PROCESS_ID),
            rs.getString(PaymentProcess.COLUMN_NAME_ACCOUNT_TYPE),
            rs.getString(PaymentProcess.COLUMN_NAME_ASSET_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_ASSET_SUB_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_ASSIGNMENT),
            rs.getString(PaymentProcess.COLUMN_NAME_BANK_ACCOUNT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_BR_DOCUMENT_NO),
            rs.getInt(PaymentProcess.COLUMN_NAME_BR_LINE),
            rs.getString(PaymentProcess.COLUMN_NAME_BUDGET_ACCOUNT),
            rs.getString(PaymentProcess.COLUMN_NAME_BUDGET_ACTIVITY),
            rs.getString(PaymentProcess.COLUMN_NAME_BUDGET_ACTIVITY_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_COST_CENTER),
            rs.getString(PaymentProcess.COLUMN_NAME_COST_CENTER_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_CURRENCY),
            rs.getTimestamp(PaymentProcess.COLUMN_NAME_DATE_ACCT),
            rs.getTimestamp(PaymentProcess.COLUMN_NAME_DATE_BASE_LINE),
            rs.getTimestamp(PaymentProcess.COLUMN_NAME_DATE_DOC),
            rs.getString(PaymentProcess.COLUMN_NAME_DR_CR),
            rs.getString(PaymentProcess.COLUMN_NAME_ERROR_CODE),
            rs.getString(PaymentProcess.COLUMN_NAME_FI_AREA),
            rs.getString(PaymentProcess.COLUMN_NAME_FI_AREA_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_FUND_CENTER),
            rs.getString(PaymentProcess.COLUMN_NAME_FUND_CENTER_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_FUND_SOURCE),
            rs.getString(PaymentProcess.COLUMN_NAME_FUND_SOURCE_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_GL_ACCOUNT),
            rs.getString(PaymentProcess.COLUMN_NAME_GL_ACCOUNT_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_HEADER_REFERENCE),
            rs.getString(PaymentProcess.COLUMN_NAME_HOUSE_BANK),
            rs.getString(PaymentProcess.COLUMN_NAME_IDEM_STATUS),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_AMOUNT),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_AMOUNT_PAID),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_COMPANY_CODE),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_COMPANY_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_DOCUMENT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_DOCUMENT_TYPE),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_FISCAL_YEAR),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_PAYMENT_CENTER),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_WTX_AMOUNT),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_WTX_AMOUNT_P),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_WTX_BASE),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_WTX_BASE_P),
            rs.getBoolean(PaymentProcess.COLUMN_NAME_IS_CHILD),
            rs.getInt(PaymentProcess.COLUMN_NAME_LINE),
            rs.getString(PaymentProcess.COLUMN_NAME_LINE_ITEM_TEXT),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_AMOUNT),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_AMOUNT_PAID),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_COMPANY_CODE),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_COMPANY_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_DOCUMENT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_DOCUMENT_TYPE),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_PAYMENT_CENTER),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_AMOUNT),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_BASE),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_BASE_P),
            rs.getString(PaymentProcess.COLUMN_NAME_PARENT_COMPANY_CODE),
            rs.getString(PaymentProcess.COLUMN_NAME_PARENT_DOCUMENT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_PARENT_FISCAL_YEAR),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_BLOCK),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_CENTER),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_COMPANY_CODE),
            rs.getTimestamp(PaymentProcess.COLUMN_NAME_PAYMENT_DATE),
            rs.getTimestamp(PaymentProcess.COLUMN_NAME_PAYMENT_DATE_ACCT),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_DOCUMENT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_FISCAL_YEAR),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_METHOD),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_METHOD_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_REFERENCE),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_TERM),
            rs.getString(PaymentProcess.COLUMN_NAME_PM_GROUP_DOC),
            rs.getString(PaymentProcess.COLUMN_NAME_PM_GROUP_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_PO_DOCUMENT_NO),
            rs.getInt(PaymentProcess.COLUMN_NAME_PO_LINE),
            rs.getString(PaymentProcess.COLUMN_NAME_POSTING_KEY),
            rs.getBoolean(PaymentProcess.COLUMN_NAME_IS_PROPOSAL),
            rs.getBoolean(PaymentProcess.COLUMN_NAME_IS_PROPOSAL_BLOCK),
            rs.getString(PaymentProcess.COLUMN_NAME_REFERENCE1),
            rs.getString(PaymentProcess.COLUMN_NAME_REFERENCE2),
            rs.getString(PaymentProcess.COLUMN_NAME_REFERENCE3),
            rs.getString(PaymentProcess.COLUMN_NAME_SPECIAL_GL),
            rs.getString(PaymentProcess.COLUMN_NAME_SPECIAL_GL_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_STATUS),
            rs.getString(PaymentProcess.COLUMN_NAME_TRADING_PARTNER),
            rs.getString(PaymentProcess.COLUMN_NAME_TRADING_PARTNER_NAME),
            rs.getLong(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID), //infomation
            rs.getString(PaymentInformation.COLUMN_NAME_ACCOUNT_HOLDER_NAME),
            rs.getString(PaymentInformation.COLUMN_NAME_ADDRESS),
            rs.getString(PaymentInformation.COLUMN_NAME_CITY),
            rs.getString(PaymentInformation.COLUMN_NAME_COUNTRY),
            rs.getString(PaymentInformation.COLUMN_NAME_COUNTRY_NAME),
            rs.getTimestamp(PaymentInformation.COLUMN_NAME_DATE_DUE),
            rs.getTimestamp(PaymentInformation.COLUMN_NAME_DATE_VALUE),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME1),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME2),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME3),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME4),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_ADDRESS),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_KEY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_REFERENCE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_CITY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_COUNTRY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME1),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME2),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME3),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME4),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_POSTAL_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_TAX_ID),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_TITLE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_ACCOUNT_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_COUNTRY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_KEY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_NAME),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_COMPANY_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_GL_ACCOUNT),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_HOUSE_BANK),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYMENT_SPECIAL_GL),
            rs.getString(PaymentInformation.COLUMN_NAME_POSTAL_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_SWIFT_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_FLAG),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_NAME),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_TAX_ID),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_TITLE),
            rs.getString(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_COMPANY_CODE),
            rs.getString(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_FISCAL_YEAR),
            rs.getString(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_TYPE),
            rs.getString(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_STATUS),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_DOCUMENT_TYPE),
            rs.getBoolean(PaymentProcess.COLUMN_NAME_IS_HAVE_CHILD)
    );

    static RowMapper<PaymentReportPaging> rowMapperPagingResponse = (rs, rowNum) -> new PaymentReportPaging(
            rs.getLong(PaymentProcess.COLUMN_NAME_PAYMENT_PROCESS_ID),
            rs.getString(PaymentProcess.COLUMN_NAME_ACCOUNT_TYPE),
            rs.getString(PaymentProcess.COLUMN_NAME_ASSET_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_ASSET_SUB_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_ASSIGNMENT),
            rs.getString(PaymentProcess.COLUMN_NAME_BANK_ACCOUNT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_BR_DOCUMENT_NO),
            rs.getInt(PaymentProcess.COLUMN_NAME_BR_LINE),
            rs.getString(PaymentProcess.COLUMN_NAME_BUDGET_ACCOUNT),
            rs.getString(PaymentProcess.COLUMN_NAME_BUDGET_ACTIVITY),
            rs.getString(PaymentProcess.COLUMN_NAME_BUDGET_ACTIVITY_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_COST_CENTER),
            rs.getString(PaymentProcess.COLUMN_NAME_COST_CENTER_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_CURRENCY),
            rs.getTimestamp(PaymentProcess.COLUMN_NAME_DATE_ACCT),
            rs.getTimestamp(PaymentProcess.COLUMN_NAME_DATE_BASE_LINE),
            rs.getTimestamp(PaymentProcess.COLUMN_NAME_DATE_DOC),
            rs.getString(PaymentProcess.COLUMN_NAME_DR_CR),
            rs.getString(PaymentProcess.COLUMN_NAME_ERROR_CODE),
            rs.getString(PaymentProcess.COLUMN_NAME_FI_AREA),
            rs.getString(PaymentProcess.COLUMN_NAME_FI_AREA_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_FUND_CENTER),
            rs.getString(PaymentProcess.COLUMN_NAME_FUND_CENTER_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_FUND_SOURCE),
            rs.getString(PaymentProcess.COLUMN_NAME_FUND_SOURCE_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_GL_ACCOUNT),
            rs.getString(PaymentProcess.COLUMN_NAME_GL_ACCOUNT_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_HEADER_REFERENCE),
            rs.getString(PaymentProcess.COLUMN_NAME_HOUSE_BANK),
            rs.getString(PaymentProcess.COLUMN_NAME_IDEM_STATUS),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_AMOUNT),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_AMOUNT_PAID),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_COMPANY_CODE),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_COMPANY_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_DOCUMENT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_DOCUMENT_TYPE),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_FISCAL_YEAR),
            rs.getString(PaymentProcess.COLUMN_NAME_INVOICE_PAYMENT_CENTER),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_WTX_AMOUNT),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_WTX_AMOUNT_P),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_WTX_BASE),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_INVOICE_WTX_BASE_P),
            rs.getBoolean(PaymentProcess.COLUMN_NAME_IS_CHILD),
            rs.getInt(PaymentProcess.COLUMN_NAME_LINE),
            rs.getString(PaymentProcess.COLUMN_NAME_LINE_ITEM_TEXT),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_AMOUNT),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_AMOUNT_PAID),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_COMPANY_CODE),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_COMPANY_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_DOCUMENT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_DOCUMENT_TYPE),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
            rs.getString(PaymentProcess.COLUMN_NAME_ORIGINAL_PAYMENT_CENTER),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_AMOUNT),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_BASE),
            rs.getBigDecimal(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_BASE_P),
            rs.getString(PaymentProcess.COLUMN_NAME_PARENT_COMPANY_CODE),
            rs.getString(PaymentProcess.COLUMN_NAME_PARENT_DOCUMENT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_PARENT_FISCAL_YEAR),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_BLOCK),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_CENTER),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_COMPANY_CODE),
            rs.getTimestamp(PaymentProcess.COLUMN_NAME_PAYMENT_DATE),
            rs.getTimestamp(PaymentProcess.COLUMN_NAME_PAYMENT_DATE_ACCT),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_DOCUMENT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_FISCAL_YEAR),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_METHOD),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_METHOD_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_REFERENCE),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_TERM),
            rs.getString(PaymentProcess.COLUMN_NAME_PM_GROUP_DOC),
            rs.getString(PaymentProcess.COLUMN_NAME_PM_GROUP_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_PO_DOCUMENT_NO),
            rs.getInt(PaymentProcess.COLUMN_NAME_PO_LINE),
            rs.getString(PaymentProcess.COLUMN_NAME_POSTING_KEY),
            rs.getBoolean(PaymentProcess.COLUMN_NAME_IS_PROPOSAL),
            rs.getBoolean(PaymentProcess.COLUMN_NAME_IS_PROPOSAL_BLOCK),
            rs.getString(PaymentProcess.COLUMN_NAME_REFERENCE1),
            rs.getString(PaymentProcess.COLUMN_NAME_REFERENCE2),
            rs.getString(PaymentProcess.COLUMN_NAME_REFERENCE3),
            rs.getString(PaymentProcess.COLUMN_NAME_SPECIAL_GL),
            rs.getString(PaymentProcess.COLUMN_NAME_SPECIAL_GL_NAME),
            rs.getString(PaymentProcess.COLUMN_NAME_STATUS),
            rs.getString(PaymentProcess.COLUMN_NAME_TRADING_PARTNER),
            rs.getString(PaymentProcess.COLUMN_NAME_TRADING_PARTNER_NAME),
            rs.getLong(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID), //infomation
            rs.getString(PaymentInformation.COLUMN_NAME_ACCOUNT_HOLDER_NAME),
            rs.getString(PaymentInformation.COLUMN_NAME_ADDRESS),
            rs.getString(PaymentInformation.COLUMN_NAME_CITY),
            rs.getString(PaymentInformation.COLUMN_NAME_COUNTRY),
            rs.getString(PaymentInformation.COLUMN_NAME_COUNTRY_NAME),
            rs.getTimestamp(PaymentInformation.COLUMN_NAME_DATE_DUE),
            rs.getTimestamp(PaymentInformation.COLUMN_NAME_DATE_VALUE),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME1),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME2),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME3),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME4),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_ADDRESS),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_KEY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_REFERENCE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_CITY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_COUNTRY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME1),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME2),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME3),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME4),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_POSTAL_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_TAX_ID),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_TITLE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_ACCOUNT_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_COUNTRY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_KEY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_NAME),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_COMPANY_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_GL_ACCOUNT),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_HOUSE_BANK),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYMENT_SPECIAL_GL),
            rs.getString(PaymentInformation.COLUMN_NAME_POSTAL_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_SWIFT_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_FLAG),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_NAME),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_TAX_ID),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_TITLE),
            rs.getString(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_COMPANY_CODE),
            rs.getString(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_NO),
            rs.getString(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_FISCAL_YEAR),
            rs.getString(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_TYPE),
            rs.getString(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_STATUS),
            rs.getString(PaymentProcess.COLUMN_NAME_PAYMENT_DOCUMENT_TYPE),
            rs.getBoolean(PaymentProcess.COLUMN_NAME_IS_HAVE_CHILD)
    );

    public PaymentReportRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(rowMapperResponse, null, null, null, null, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<PaymentReport> findProposalAllByPaymentAliasIdAndIsProposalAndIsChild(Long paymentAliasId, boolean isProposal, boolean isChild) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PC.ID,           ");
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
        sql.append("          PC.COST_CENTER,           ");
        sql.append("          PC.COST_CENTER_NAME,           ");
        sql.append("          PC.CURRENCY,           ");
        sql.append("          PC.DATE_ACCT,           ");
        sql.append("          PC.DATE_BASE_LINE,           ");
        sql.append("          PC.DATE_DOC,           ");
        sql.append("          PC.DR_CR,           ");
        sql.append("          PC.ERROR_CODE,           ");
        sql.append("          PC.FI_AREA,           ");
        sql.append("          PC.FI_AREA_NAME,           ");
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
        sql.append("          PC.INVOICE_AMOUNT_PAID,           ");
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
        sql.append("          PC.IS_CHILD,           ");
        sql.append("          PC.IS_PROPOSAL,           ");
        sql.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sql.append("          PC.LINE,           ");
        sql.append("          PC.LINE_ITEM_TEXT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
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
        sql.append("          PC.POSTING_KEY,           ");
        sql.append("          PC.PO_DOCUMENT_NO,           ");
        sql.append("          PC.PO_LINE,           ");
        sql.append("          PC.REFERENCE1,           ");
        sql.append("          PC.REFERENCE2,           ");
        sql.append("          PC.REFERENCE3,           ");
        sql.append("          PC.SPECIAL_GL,           ");
        sql.append("          PC.SPECIAL_GL_NAME,           ");
        sql.append("          PC.STATUS,           ");
        sql.append("          PC.TRADING_PARTNER,           ");
        sql.append("          PC.TRADING_PARTNER_NAME,           ");
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
        sql.append("          PC.REVERSE_PAYMENT_COMPANY_CODE,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_NO,           ");
        sql.append("          PC.REVERSE_PAYMENT_FISCAL_YEAR,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.REVERSE_PAYMENT_STATUS,           ");
        sql.append("          PC.PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.IS_HAVE_CHILD           ");
        sql.append("          FROM PAYMENT_PROCESS PC           ");
        sql.append("          LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID           ");
        sql.append("          WHERE 1=1           ");
        if (isProposal) {
            sql.append("          AND PC.IS_PROPOSAL = '1'           ");
            sql.append("          AND PC.STATUS = 'S'           ");
        } else {
            sql.append("          AND PC.IS_PROPOSAL = '0'           ");
            sql.append("          AND PC.IDEM_STATUS = 'S'           ");
        }
        if (isChild) {
            sql.append("          AND PC.IS_CHILD = '1'           ");
        } else {
            sql.append("          AND PC.IS_CHILD = '0'           ");
        }
        params.add(paymentAliasId);
        sql.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperResponse);
    }

    @Override
    public List<PaymentReport> findAllByPaymentAliasIdAndIsProposalAndIsChild(Long paymentAliasId, boolean isProposal, boolean isChild) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PC.ID,           ");
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
        sql.append("          PC.COST_CENTER,           ");
        sql.append("          PC.COST_CENTER_NAME,           ");
        sql.append("          PC.CURRENCY,           ");
        sql.append("          PC.DATE_ACCT,           ");
        sql.append("          PC.DATE_BASE_LINE,           ");
        sql.append("          PC.DATE_DOC,           ");
        sql.append("          PC.DR_CR,           ");
        sql.append("          PC.ERROR_CODE,           ");
        sql.append("          PC.FI_AREA,           ");
        sql.append("          PC.FI_AREA_NAME,           ");
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
        sql.append("          PC.INVOICE_AMOUNT_PAID,           ");
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
        sql.append("          PC.IS_CHILD,           ");
        sql.append("          PC.IS_PROPOSAL,           ");
        sql.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sql.append("          PC.LINE,           ");
        sql.append("          PC.LINE_ITEM_TEXT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
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
        sql.append("          PC.POSTING_KEY,           ");
        sql.append("          PC.PO_DOCUMENT_NO,           ");
        sql.append("          PC.PO_LINE,           ");
        sql.append("          PC.REFERENCE1,           ");
        sql.append("          PC.REFERENCE2,           ");
        sql.append("          PC.REFERENCE3,           ");
        sql.append("          PC.SPECIAL_GL,           ");
        sql.append("          PC.SPECIAL_GL_NAME,           ");
        sql.append("          PC.STATUS,           ");
        sql.append("          PC.TRADING_PARTNER,           ");
        sql.append("          PC.TRADING_PARTNER_NAME,           ");
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
        sql.append("          PC.REVERSE_PAYMENT_COMPANY_CODE,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_NO,           ");
        sql.append("          PC.REVERSE_PAYMENT_FISCAL_YEAR,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.REVERSE_PAYMENT_STATUS,           ");
        sql.append("          PC.PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.IS_HAVE_CHILD           ");
        sql.append("          FROM PAYMENT_PROCESS PC           ");
        sql.append("          LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID           ");
        sql.append("          WHERE 1=1           ");
        if (isProposal) {
            sql.append("          AND PC.IS_PROPOSAL = '1'           ");
            sql.append("          AND PC.IS_PROPOSAL_BLOCK = '0'           ");
//            sql.append("          AND PC.STATUS = 'S'           ");
        } else {
            sql.append("          AND PC.IS_PROPOSAL = '0'           ");
            sql.append("          AND PC.IDEM_STATUS = 'S'           ");
        }
        if (isChild) {
            sql.append("          AND PC.IS_CHILD = '1'           ");
        } else {
            sql.append("          AND PC.IS_CHILD = '0'           ");
        }
        params.add(paymentAliasId);
        sql.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperResponse);
    }

    @Override
    public List<PaymentReport> findPaymentDocumentAllByPaymentAliasIdAndIsProposalAndIsChild(Long paymentAliasId, boolean isProposal, boolean isChild) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PC.ID,           ");
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
        sql.append("          PC.COST_CENTER,           ");
        sql.append("          PC.COST_CENTER_NAME,           ");
        sql.append("          PC.CURRENCY,           ");
        sql.append("          PC.DATE_ACCT,           ");
        sql.append("          PC.DATE_BASE_LINE,           ");
        sql.append("          PC.DATE_DOC,           ");
        sql.append("          PC.DR_CR,           ");
        sql.append("          PC.ERROR_CODE,           ");
        sql.append("          PC.FI_AREA,           ");
        sql.append("          PC.FI_AREA_NAME,           ");
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
        sql.append("          PC.INVOICE_AMOUNT_PAID,           ");
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
        sql.append("          PC.IS_CHILD,           ");
        sql.append("          PC.IS_PROPOSAL,           ");
        sql.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sql.append("          PC.LINE,           ");
        sql.append("          PC.LINE_ITEM_TEXT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
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
        sql.append("          PC.POSTING_KEY,           ");
        sql.append("          PC.PO_DOCUMENT_NO,           ");
        sql.append("          PC.PO_LINE,           ");
        sql.append("          PC.REFERENCE1,           ");
        sql.append("          PC.REFERENCE2,           ");
        sql.append("          PC.REFERENCE3,           ");
        sql.append("          PC.SPECIAL_GL,           ");
        sql.append("          PC.SPECIAL_GL_NAME,           ");
        sql.append("          PC.STATUS,           ");
        sql.append("          PC.TRADING_PARTNER,           ");
        sql.append("          PC.TRADING_PARTNER_NAME,           ");
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
        sql.append("          PC.REVERSE_PAYMENT_COMPANY_CODE,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_NO,           ");
        sql.append("          PC.REVERSE_PAYMENT_FISCAL_YEAR,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.REVERSE_PAYMENT_STATUS,           ");
        sql.append("          PC.PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.IS_HAVE_CHILD           ");
        sql.append("          FROM PAYMENT_PROCESS PC           ");
        sql.append("          LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID           ");
        sql.append("          WHERE 1=1           ");

        if (isProposal) {
            sql.append("          AND PC.IS_PROPOSAL = '1'           ");
            sql.append("          AND PC.IS_PROPOSAL_BLOCK = '0'           ");
        } else {
            sql.append("          AND PC.IS_PROPOSAL = '0'           ");
        }
        if (isChild) {
            sql.append("          AND PC.IS_CHILD = '1'           ");
        } else {
            sql.append("          AND PC.IS_CHILD = '0'           ");
        }
        params.add(paymentAliasId);
        sql.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");
        // for reverse
        sql.append("          AND PC.PAYMENT_DOCUMENT_NO IS NOT NULL           ");
        sql.append("          AND PC.IDEM_STATUS = 'S'           ");


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql);
        log.info("param {}", Arrays.toString(objParams));
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperResponse);
    }

    @Override
    public List<PaymentReport> findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(String parentCompanyCode, String parentDocumentNo, String parentFiscalYear, boolean isProposal, boolean isChild,Long paymentAliasId) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PC.ID,           ");
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
        sql.append("          PC.COST_CENTER,           ");
        sql.append("          PC.COST_CENTER_NAME,           ");
        sql.append("          PC.CURRENCY,           ");
        sql.append("          PC.DATE_ACCT,           ");
        sql.append("          PC.DATE_BASE_LINE,           ");
        sql.append("          PC.DATE_DOC,           ");
        sql.append("          PC.DR_CR,           ");
        sql.append("          PC.ERROR_CODE,           ");
        sql.append("          PC.FI_AREA,           ");
        sql.append("          PC.FI_AREA_NAME,           ");
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
        sql.append("          PC.INVOICE_AMOUNT_PAID,           ");
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
        sql.append("          PC.IS_CHILD,           ");
        sql.append("          PC.IS_PROPOSAL,           ");
        sql.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sql.append("          PC.LINE,           ");
        sql.append("          PC.LINE_ITEM_TEXT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
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
        sql.append("          PC.POSTING_KEY,           ");
        sql.append("          PC.PO_DOCUMENT_NO,           ");
        sql.append("          PC.PO_LINE,           ");
        sql.append("          PC.REFERENCE1,           ");
        sql.append("          PC.REFERENCE2,           ");
        sql.append("          PC.REFERENCE3,           ");
        sql.append("          PC.SPECIAL_GL,           ");
        sql.append("          PC.SPECIAL_GL_NAME,           ");
        sql.append("          PC.STATUS,           ");
        sql.append("          PC.TRADING_PARTNER,           ");
        sql.append("          PC.TRADING_PARTNER_NAME,           ");
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
        sql.append("          PC.REVERSE_PAYMENT_COMPANY_CODE,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_NO,           ");
        sql.append("          PC.REVERSE_PAYMENT_FISCAL_YEAR,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.REVERSE_PAYMENT_STATUS,           ");
        sql.append("          PC.PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.IS_HAVE_CHILD           ");
        sql.append("          FROM PAYMENT_PROCESS PC           ");
        sql.append("          LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID           ");
        sql.append("          WHERE 1=1           ");

        if (isProposal) {
            sql.append("          AND PC.IS_PROPOSAL = '1'           ");
            sql.append("          AND PC.IS_PROPOSAL_BLOCK = '0'           ");
        } else {
            sql.append("          AND PC.IS_PROPOSAL = '0'           ");
        }
        if (isChild) {
            sql.append("          AND PC.IS_CHILD = '1'           ");
        } else {
            sql.append("          AND PC.IS_CHILD = '0'           ");
        }
        params.add(parentCompanyCode);
        params.add(parentDocumentNo);
        params.add(parentFiscalYear);
        params.add(paymentAliasId);
        sql.append("          AND PC.PARENT_COMPANY_CODE = ?           ");
        sql.append("          AND PC.PARENT_DOCUMENT_NO = ?           ");
        sql.append("          AND PC.PARENT_FISCAL_YEAR = ?           ");
        sql.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperResponse);
    }

    @Override
    public Page<PaymentReportPaging> findAllByPaymentAliasIdAndIsProposalAndIsChild(Long paymentAliasId, boolean proposal, String vendor, String bankAccount, int page, int size) {

        List<Object> params = new ArrayList<>();
        Pageable pageable = generateSQLPageable(page, size);
        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PC.ID,           ");
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
        sql.append("          PC.COST_CENTER,           ");
        sql.append("          PC.COST_CENTER_NAME,           ");
        sql.append("          PC.CURRENCY,           ");
        sql.append("          PC.DATE_ACCT,           ");
        sql.append("          PC.DATE_BASE_LINE,           ");
        sql.append("          PC.DATE_DOC,           ");
        sql.append("          PC.DR_CR,           ");
        sql.append("          PC.ERROR_CODE,           ");
        sql.append("          PC.FI_AREA,           ");
        sql.append("          PC.FI_AREA_NAME,           ");
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
        sql.append("          PC.INVOICE_AMOUNT_PAID,           ");
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
        sql.append("          PC.IS_CHILD,           ");
        sql.append("          PC.IS_PROPOSAL,           ");
        sql.append("          PC.IS_PROPOSAL_BLOCK,           ");
        sql.append("          PC.LINE,           ");
        sql.append("          PC.LINE_ITEM_TEXT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT,           ");
        sql.append("          PC.ORIGINAL_AMOUNT_PAID,           ");
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
        sql.append("          PC.POSTING_KEY,           ");
        sql.append("          PC.PO_DOCUMENT_NO,           ");
        sql.append("          PC.PO_LINE,           ");
        sql.append("          PC.REFERENCE1,           ");
        sql.append("          PC.REFERENCE2,           ");
        sql.append("          PC.REFERENCE3,           ");
        sql.append("          PC.SPECIAL_GL,           ");
        sql.append("          PC.SPECIAL_GL_NAME,           ");
        sql.append("          PC.STATUS,           ");
        sql.append("          PC.TRADING_PARTNER,           ");
        sql.append("          PC.TRADING_PARTNER_NAME,           ");
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
        sql.append("          PC.REVERSE_PAYMENT_COMPANY_CODE,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_NO,           ");
        sql.append("          PC.REVERSE_PAYMENT_FISCAL_YEAR,           ");
        sql.append("          PC.REVERSE_PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.REVERSE_PAYMENT_STATUS,           ");
        sql.append("          PC.PAYMENT_DOCUMENT_TYPE,           ");
        sql.append("          PC.IS_HAVE_CHILD           ");
        sql.append("          FROM PAYMENT_PROCESS PC           ");
        sql.append("          LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID           ");
        sql.append("          WHERE 1=1           ");

        if (proposal) {
            sql.append("          AND PC.IS_PROPOSAL = '1'           ");
            sql.append("          AND PC.IS_PROPOSAL_BLOCK = '0'           ");
        } else {
            sql.append("          AND PC.IS_PROPOSAL = '0'           ");
        }
//        if (isChild) {
//            sql.append("          AND PC.IS_CHILD = '1'           ");
//        } else {
        sql.append("          AND PC.IS_CHILD = '0'           ");
//        }
        params.add(paymentAliasId);
        sql.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");
        params.add(vendor);
        sql.append("          AND PI.VENDOR_CODE = ?           ");
        params.add(bankAccount);
        sql.append("          AND PC.BANK_ACCOUNT_NO = ?           ");

        sql.append(generatePageable(pageable, params));


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql);
        List<PaymentReportPaging> list = this.jdbcTemplate.query(sql.toString(), objParams, rowMapperPagingResponse);
        int count = count(paymentAliasId, proposal, vendor, bankAccount, "ALL");
        return new PageImpl<>(list, pageable, count);
//        return new PageImpl<List<PaymentProposalLog>>(pageable, new BigDecimal(count).divide(new BigDecimal(pageable.getPageSize()), RoundingMode.UP).intValue(), count, list);
    }

    private Pageable generateSQLPageable(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

    @Override
    public int count(Long paymentAliasId, boolean proposal, String vendor, String bankAccount, String status) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT COUNT(1)           ");
        sql.append("          FROM PAYMENT_PROCESS PC           ");
        sql.append("          LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID           ");
        sql.append("          WHERE 1=1           ");

        if (proposal) {
            sql.append("          AND PC.IS_PROPOSAL = '1'           ");
            sql.append("          AND PC.IS_PROPOSAL_BLOCK = '0'           ");
        } else {
            sql.append("          AND PC.IS_PROPOSAL = '0'           ");
        }
//        if (isChild) {
//            sql.append("          AND PC.IS_CHILD = '1'           ");
//        } else {
        sql.append("          AND PC.IS_CHILD = '0'           ");
//        }
        params.add(paymentAliasId);
        sql.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");
        params.add(vendor);
        sql.append("          AND PI.VENDOR_CODE = ?           ");
        params.add(bankAccount);
        sql.append("          AND PI.PAYEE_BANK_ACCOUNT_NO = ?           ");

        if (!status.equalsIgnoreCase("ALL")) {
            if (status.equalsIgnoreCase("S")) {
                sql.append("          AND PC.STATUS = 'S'          ");
            } else if (!status.equalsIgnoreCase("E")) {
                sql.append("          AND PC.STATUS = 'E'          ");
            }

        }


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql);

        Integer count = this.jdbcTemplate.queryForObject(sql.toString(), objParams, Integer.class);
        return null == count ? 0 : count;
    }

    @Override
    public List<DuplicatePaymentReportResponse> findAllDuplicatePaymentReport(DuplicatePaymentReport request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ORIGINAL_COMP_CODE ");
        sb.append(" , ORIGINAL_DOC_NO ");
        sb.append(" , TO_CHAR(TO_NUMBER(ORIGINAL_FISCAL_YEAR) + 543) AS ORIGINAL_FISCAL_YEAR ");
        sb.append(" , PAYMENT_DOCUMENT ");
        sb.append(" , COUNT(1)  AS COUNT_DUPLICATE");
        sb.append(" , SUM(AMOUNT)  AS AMOUNT");
        sb.append(" , PAYMENT_NAME ");
        sb.append(" , PAYMENT_DATE ");
        sb.append("FROM PROPOSAL_LOG ");
        sb.append("WHERE IS_RERUN = 0 ");
        sb.append("AND (FILE_STATUS <> 'R' OR FILE_STATUS IS NULL) ");
        sb.append(" AND PAYMENT_DOCUMENT IS NOT NULL ");
        if (!Util.isEmpty(request.getPaymentDate())) {
            sb.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getCompanyCode())) {
            sb.append(SqlUtil.whereClause(request.getCompanyCode(), "ORIGINAL_COMP_CODE", params));
        }
        sb.append(" GROUP BY ORIGINAL_COMP_CODE, ORIGINAL_DOC_NO, ORIGINAL_FISCAL_YEAR, PAYMENT_DOCUMENT, PAYMENT_NAME, PAYMENT_DATE ");
        sb.append(" HAVING COUNT(1) > 1 ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("query {} {}", sb, objParams);
        return this.jdbcTemplate.query(sb.toString(), objParams, new BeanPropertyRowMapper<>(DuplicatePaymentReportResponse.class));
    }
}
