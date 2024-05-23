package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.common.CompanyCondition;
import th.com.bloomcode.paymentservice.model.common.PaymentMethod;
import th.com.bloomcode.paymentservice.model.config.*;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareProposalDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareSelectProposalDocument;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentAliasRepository;
import th.com.bloomcode.paymentservice.repository.payment.PrepareProposalDocumentRepository;
import th.com.bloomcode.paymentservice.service.AuthorizeUtilService;
import th.com.bloomcode.paymentservice.util.DynamicCondition;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
 public class PrepareProposalDocumentRepositoryImpl extends MetadataJdbcRepository<PrepareProposalDocument, Long> implements PrepareProposalDocumentRepository {

    @Value("${payment.dblink.schema}")
    private String schema;

    @Value("${payment.dblink.user}")
    private String user;

    private final AuthorizeUtilService authorizeUtilService;
    private final JdbcTemplate jdbcTemplate;
    private static PaymentAliasRepository paymentAliasRepository;
    static BeanPropertyRowMapper<PrepareProposalDocument> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PrepareProposalDocument.class);


    static RowMapper<PrepareProposalDocument> userRowMapper = (rs, rowNum) -> new PrepareProposalDocument(
            rs.getLong(PrepareProposalDocument.COLUMN_NAME_GL_HEAD_ID),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_DOCUMENT_TYPE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_COMPANY_CODE),
            rs.getTimestamp(PrepareProposalDocument.COLUMN_NAME_DATE_DOC),
            rs.getTimestamp(PrepareProposalDocument.COLUMN_NAME_DATE_ACCT),
            rs.getInt(PrepareProposalDocument.COLUMN_NAME_PERIOD),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_CURRENCY),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_INVOICE_DOCUMENT_NO),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_NO),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_COST_CENTER1),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_COST_CENTER2),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_HEADER_REFERENCE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_DOCUMENT_HEADER_TEXT),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_HEADER_REFERENCE2),
            rs.getTimestamp(PrepareProposalDocument.COLUMN_NAME_REVERSE_DATE_ACCT),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REVERSE_REASON),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_ORIGINAL_DOCUMENT),
            rs.getTimestamp(PrepareProposalDocument.COLUMN_NAME_DOCUMENT_CREATED),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_USER_PARK),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_USER_POST),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_POSTING_KEY),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_ACCOUNT_TYPE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_DR_CR),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_GL_ACCOUNT),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_FI_AREA),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_COST_CENTER),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_FUND_SOURCE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_BG_CODE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_BG_ACTIVITY),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_COST_ACTIVITY),
            rs.getBigDecimal(PrepareProposalDocument.COLUMN_NAME_AMOUNT),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REFERENCE3),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_ASSIGNMENT),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_BR_DOCUMENT_NO),
            rs.getInt(PrepareProposalDocument.COLUMN_NAME_BR_LINE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_CENTER),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_BANK_BOOK),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_SUB_ACCOUNT),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_SUB_ACCOUNT_OWNER),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_DEPOSIT_ACCOUNT),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_GPSC),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_GPSC_GROUP),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_LINE_ITEM_TEXT),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_LINE_DESC),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_TERM),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_METHOD),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_WTX_TYPE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_WTX_CODE),
            rs.getBigDecimal(PrepareProposalDocument.COLUMN_NAME_WTX_BASE),
            rs.getBigDecimal(PrepareProposalDocument.COLUMN_NAME_WTX_AMOUNT),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_WTX_TYPE_P),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_WTX_CODE_P),
            rs.getBigDecimal(PrepareProposalDocument.COLUMN_NAME_WTX_BASE_P),
            rs.getBigDecimal(PrepareProposalDocument.COLUMN_NAME_WTX_AMOUNT_P),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_VENDOR),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_VENDOR_TAX_ID),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_TAX_ID),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_BANK_ACCOUNT_NO),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_BANK_BRANCH_NO),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_TRADING_PARTNER),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_TRADING_PARTNER_PARK),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_SPECIAL_GL),
            rs.getTimestamp(PrepareProposalDocument.COLUMN_NAME_DATE_BASE_LINE),
            rs.getTimestamp(PrepareProposalDocument.COLUMN_NAME_DATE_VALUE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_ASSET_NO),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_ASSET_SUB_NO),
            rs.getBigDecimal(PrepareProposalDocument.COLUMN_NAME_QTY),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_UOM),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REFERENCE1),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REFERENCE2),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PO_DOCUMENT_NO),
            rs.getInt(PrepareProposalDocument.COLUMN_NAME_PO_LINE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_INCOME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_BLOCK),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_REFERENCE),
            rs.getInt(PrepareProposalDocument.COLUMN_NAME_LINE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_COMP_CODE_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_CENTER_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_COST_CENTER_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_METHOD_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_FUND_SOURCE_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_BG_CODE_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_BG_ACTIVITY_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_NAME1),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_NAME2),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_TAX_ID),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_ADDRESS),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_CITY),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_POSTAL),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REGION_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_COUNTRY),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_COUNTRY_CODE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_BANK),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_BANK_ACCOUNT_TYPE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_BANK_NO),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_ACCOUNT_HOLDER_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_BANK_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_BANK_KEY),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_SWIFT_CODE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_BANK_REFERENCE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_AREA_NAME),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_VENDOR_ACTIVE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_VENDOR_STATUS),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_ACTIVE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_STATUS),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYEE_CONFIRM),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REAL_ORIGINAL_DOCUMENT_NO),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REAL_ORIGINAL_DOCUMENT_TYPE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REAL_ORIGINAL_COMPANY_CODE),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_REAL_ORIGINAL_FISCAL_YEAR),
            rs.getString(PrepareProposalDocument.COLUMN_NAME_LINE_PAYMENT_CENTER),
            rs.getBigDecimal(PrepareProposalDocument.COLUMN_NAME_LINE_WTX_AMOUNT),
            rs.getBigDecimal(PrepareProposalDocument.COLUMN_NAME_LINE_WTX_BASE),
            rs.getBigDecimal(PrepareProposalDocument.COLUMN_NAME_LINE_WTX_AMOUNT_P),
            rs.getBigDecimal(PrepareProposalDocument.COLUMN_NAME_LINE_WTX_BASE_P)

//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_CLEARING_DATE),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_CLEARING_ENTRY_DATE),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_CLEARING_DOC_NO),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_CLEARING_YEAR),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_ID),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_DATE),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_NAME),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYMENT_DATE_ACCT),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_HOUSE_BANK),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYING_COMP_CODE),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYING_COMP_CODE_NAME),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYING_BANK_CODE),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYING_HOUSE_BANK),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYING_BANK_ACCOUNT_NO),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYING_BANK_COUNTRY),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYING_BANK_NO),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYING_GL_ACCOUNT),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYING_BANK_KEY),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_PAYING_BANK_NAME),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_AMOUNT_PAID),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_STATUS),
//            rs.getString(PrepareProposalDocument.COLUMN_NAME_ERROR_CODE)
    );




    static RowMapper<PrepareSelectProposalDocument> prepareSelectProposalRowMapper = (rs, rowNum) -> new PrepareSelectProposalDocument(
            rs.getLong(PrepareSelectProposalDocument.COLUMN_NAME_ID),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REAL_ORIGINAL_DOCUMENT_TYPE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REAL_ORIGINAL_DOCUMENT_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REAL_ORIGINAL_COMPANY_CODE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REAL_ORIGINAL_FISCAL_YEAR),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_LINE_PAYMENT_CENTER),
            rs.getBigDecimal(PrepareSelectProposalDocument.COLUMN_NAME_LINE_WTX_AMOUNT),
            rs.getBigDecimal(PrepareSelectProposalDocument.COLUMN_NAME_LINE_WTX_BASE),
            rs.getBigDecimal(PrepareSelectProposalDocument.COLUMN_NAME_LINE_WTX_AMOUNT_P),
            rs.getBigDecimal(PrepareSelectProposalDocument.COLUMN_NAME_LINE_WTX_BASE_P),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_DOCUMENT_TYPE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_COMPANY_CODE),
            rs.getTimestamp(PrepareSelectProposalDocument.COLUMN_NAME_DATE_DOC),
            rs.getTimestamp(PrepareSelectProposalDocument.COLUMN_NAME_DATE_ACCT),
            rs.getInt(PrepareSelectProposalDocument.COLUMN_NAME_PERIOD),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_CURRENCY),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_INVOICE_DOCUMENT_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_COST_CENTER1),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_COST_CENTER2),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_HEADER_REFERENCE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_DOCUMENT_HEADER_TEXT),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_HEADER_REFERENCE2),
            rs.getTimestamp(PrepareSelectProposalDocument.COLUMN_NAME_REVERSE_DATE_ACCT),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REVERSE_REASON),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ORIGINAL_DOCUMENT),
            rs.getTimestamp(PrepareSelectProposalDocument.COLUMN_NAME_DOCUMENT_CREATED),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_USER_PARK),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_USER_POST),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_POSTING_KEY),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ACCOUNT_TYPE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_DR_CR),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_GL_ACCOUNT),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_FI_AREA),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_COST_CENTER),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_FUND_SOURCE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_BG_CODE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_BG_ACTIVITY),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_COST_ACTIVITY),
            rs.getBigDecimal(PrepareSelectProposalDocument.COLUMN_NAME_AMOUNT),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REFERENCE3),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ASSIGNMENT),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_BR_DOCUMENT_NO),
            rs.getInt(PrepareSelectProposalDocument.COLUMN_NAME_BR_LINE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_PAYMENT_CENTER),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_BANK_BOOK),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_SUB_ACCOUNT),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_SUB_ACCOUNT_OWNER),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_DEPOSIT_ACCOUNT),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_GPSC),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_GPSC_GROUP),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_LINE_ITEM_TEXT),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_LINE_DESC),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_PAYMENT_TERM),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_PAYMENT_METHOD),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_WTX_TYPE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_WTX_CODE),
            rs.getBigDecimal(PrepareSelectProposalDocument.COLUMN_NAME_WTX_BASE),
            rs.getBigDecimal(PrepareSelectProposalDocument.COLUMN_NAME_WTX_AMOUNT),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_WTX_TYPE_P),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_WTX_CODE_P),
            rs.getBigDecimal(PrepareSelectProposalDocument.COLUMN_NAME_WTX_BASE_P),
            rs.getBigDecimal(PrepareSelectProposalDocument.COLUMN_NAME_WTX_AMOUNT_P),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_VENDOR),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_VENDOR_TAX_ID),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_PAYEE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_PAYEE_TAX_ID),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_BANK_ACCOUNT_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_BANK_BRANCH_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_TRADING_PARTNER),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_TRADING_PARTNER_PARK),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_SPECIAL_GL),
            rs.getTimestamp(PrepareSelectProposalDocument.COLUMN_NAME_DATE_BASE_LINE),
            rs.getTimestamp(PrepareSelectProposalDocument.COLUMN_NAME_DATE_VALUE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ASSET_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ASSET_SUB_NO),
            rs.getBigDecimal(PrepareSelectProposalDocument.COLUMN_NAME_QTY),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_UOM),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REFERENCE1),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_REFERENCE2),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_PO_DOCUMENT_NO),
            rs.getInt(PrepareSelectProposalDocument.COLUMN_NAME_PO_LINE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_INCOME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_PAYMENT_BLOCK),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_PAYMENT_REFERENCE),
            rs.getInt(PrepareSelectProposalDocument.COLUMN_NAME_LINE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_COMP_CODE_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_PAYMENT_CENTER_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_COST_CENTER_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_PAYMENT_METHOD_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_FUND_SOURCE_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_BG_CODE_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_BG_ACTIVITY_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_NAME1),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_NAME2),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_TAX_ID),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_VENDOR_ACTIVE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_ADDRESS),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_CITY),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_POSTAL),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_REGION_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_COUNTRY),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_COUNTRY_CODE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_PAYEE_BANK_ACCOUNT_TYPE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_PAYEE_BANK_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_PAYEE_BANK_ACCOUNT_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_ACCOUNT_HOLDER_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_PAYEE_BANK),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_PAYEE_BANK_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_PAYEE_BANK_KEY),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_SWIFT_CODE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_PAYEE_BANK_REFERENCE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_AREA_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_VENDOR_STATUS),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_NAME1),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_NAME2),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_TAX_ID),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_VENDOR_ACTIVE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_ADDRESS),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_CITY),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_POSTAL),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_REGION_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_COUNTRY),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_COUNTRY_CODE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_PAYEE_BANK_ACCOUNT_TYPE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_PAYEE_BANK_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_PAYEE_BANK_ACCOUNT_NO),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_ACCOUNT_HOLDER_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_PAYEE_BANK),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_PAYEE_BANK_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_PAYEE_BANK_KEY),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_SWIFT_CODE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_PAYEE_BANK_REFERENCE),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_AREA_NAME),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_VENDOR_STATUS),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_ALTERNATIVE_APPROVAL_STATUS),
            rs.getString(PrepareSelectProposalDocument.COLUMN_NAME_MAIN_APPROVAL_STATUS)
    );

    public PrepareProposalDocumentRepositoryImpl(AuthorizeUtilService authorizeUtilService, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, PaymentAliasRepository paymentAliasRepository) {
        super(userRowMapper, null, null, null, null, jdbcTemplate);
        this.authorizeUtilService = authorizeUtilService;
        this.jdbcTemplate = jdbcTemplate;
        PrepareProposalDocumentRepositoryImpl.paymentAliasRepository = paymentAliasRepository;
    }

    @Override
    public List<PrepareProposalDocument> findUnBlockDocumentCanPayByParameter(ParameterConfig parameterConfig, String username) {
        List<Object> params = new ArrayList<>();

        Parameter parameter = parameterConfig.getParameter();
        AdditionLog additionLog = parameterConfig.getAdditionLog();
        List<CompanyCondition> companyConditions = parameter.getCompanyCondition();
        List<VendorConfig> vendors = parameter.getVendor();
        List<Independent> independents = parameterConfig.getIndependent();

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT H.DOCUMENT_TYPE AS REAL_ORIGINAL_DOCUMENT_TYPE,TMP.* ");
        sql.append(" FROM ( ");

        sql.append(" SELECT  /*+ USE_NL(GL) ORDERED */ ");
        sql.append("          GH.ID                           AS ID,           ");

        sql.append("          substr(GH.ORIGINAL_DOCUMENT, 0, 10) AS REAL_ORIGINAL_DOCUMENT_NO,           ");
        sql.append("          substr(GH.ORIGINAL_DOCUMENT, 11, 5) AS REAL_ORIGINAL_COMPANY_CODE,           ");
        sql.append("          substr(GH.ORIGINAL_DOCUMENT, 16, 4) AS REAL_ORIGINAL_FISCAL_YEAR,           ");
        sql.append("          GL.PAYMENT_CENTER                   AS LINE_PAYMENT_CENTER,           ");
        sql.append("          GL.WTX_AMOUNT                       AS LINE_WTX_AMOUNT,           ");
        sql.append("          GL.WTX_BASE                         AS LINE_WTX_BASE,           ");
        sql.append("          GL.WTX_AMOUNT_P                     AS LINE_WTX_AMOUNT_P,           ");
        sql.append("          GL.WTX_BASE_P                       AS LINE_WTX_BASE_P,           ");

        sql.append("          GH.DOCUMENT_TYPE                ,           ");
        sql.append("          GH.COMPANY_CODE                 ,           ");
        sql.append("          GH.DATE_DOC                     ,           ");
        sql.append("          GH.DATE_ACCT                    ,           ");
        sql.append("          GH.PERIOD                       ,           ");
        sql.append("          GH.CURRENCY                     ,           ");
        sql.append("          GH.INVOICE_DOCUMENT_NO          ,           ");
        sql.append("          GH.REVERSE_INVOICE_DOCUMENT_NO  ,           ");
        sql.append("          GH.ORIGINAL_DOCUMENT_NO         ,           ");
        sql.append("          GH.ORIGINAL_FISCAL_YEAR         ,           ");
        sql.append("          GH.REVERSE_ORIGINAL_DOCUMENT_NO ,           ");
        sql.append("          GH.REVERSE_ORIGINAL_FISCAL_YEAR ,           ");
        sql.append("          GH.COST_CENTER1                 ,           ");
        sql.append("          GH.COST_CENTER2                 ,           ");
        sql.append("          GH.HEADER_REFERENCE             ,           ");
        sql.append("          GH.DOCUMENT_HEADER_TEXT         ,           ");
        sql.append("          GH.HEADER_REFERENCE2            ,           ");
        sql.append("          GH.REVERSE_DATE_ACCT            ,           ");
        sql.append("          GH.REVERSE_REASON               ,           ");
        sql.append("          GH.ORIGINAL_DOCUMENT            ,           ");
        sql.append("          GH.DOCUMENT_CREATED             ,           ");
        sql.append("          GH.USER_PARK                    ,           ");
        sql.append("          GH.USER_POST                    ,           ");
        sql.append("          GL.POSTING_KEY                  ,           ");
        sql.append("          GL.ACCOUNT_TYPE                 ,           ");
        sql.append("          GL.DR_CR                        ,           ");
        sql.append("          GL.GL_ACCOUNT                   ,           ");
        sql.append("          GL.FI_AREA                      ,           ");
        sql.append("          GL.COST_CENTER                  ,           ");
        sql.append("          GL.FUND_SOURCE                  ,           ");
        sql.append("          GL.BG_CODE                      ,           ");
        sql.append("          GL.BG_ACTIVITY                  ,           ");
        sql.append("          GL.COST_ACTIVITY                ,           ");
        sql.append("          GL.AMOUNT                       ,           ");
        sql.append("          GL.REFERENCE3                   ,           ");
        sql.append("          GL.ASSIGNMENT                   ,           ");
        sql.append("          GL.BR_DOCUMENT_NO               ,           ");
        sql.append("          GL.BR_LINE                      ,           ");
        sql.append("          GL.PAYMENT_CENTER               ,           ");
        sql.append("          GL.BANK_BOOK                    ,           ");
        sql.append("          GL.SUB_ACCOUNT                  ,           ");
        sql.append("          GL.SUB_ACCOUNT_OWNER            ,           ");
        sql.append("          GL.DEPOSIT_ACCOUNT              ,           ");
        sql.append("          GL.DEPOSIT_ACCOUNT_OWNER        ,           ");
        sql.append("          GL.GPSC                         ,           ");
        sql.append("          GL.GPSC_GROUP                   ,           ");
        sql.append("          GL.LINE_ITEM_TEXT               ,           ");
        sql.append("          GL.LINE_DESC                    ,           ");
        sql.append("          GL.PAYMENT_TERM                 ,           ");
        sql.append("          GL.PAYMENT_METHOD               ,           ");
        sql.append("          GL.WTX_TYPE                     ,           ");
        sql.append("          GL.WTX_CODE                     ,           ");
        sql.append("          GL.WTX_BASE                     ,           ");
        sql.append("          nvl(GL.WTX_AMOUNT, 0)           AS WTX_AMOUNT,           ");
        sql.append("          GL.WTX_TYPE_P                   ,           ");
        sql.append("          GL.WTX_CODE_P                   ,           ");
        sql.append("          GL.WTX_BASE_P                   ,           ");
        sql.append("          GL.WTX_AMOUNT_P                 ,           ");
        sql.append("          GL.VENDOR                       ,           ");
        sql.append("          GL.VENDOR_TAX_ID                ,           ");
        sql.append("          GL.PAYEE                        ,           ");
        sql.append("          GL.PAYEE_TAX_ID                 ,           ");
        sql.append("          GL.BANK_ACCOUNT_NO              ,           ");
        sql.append("          GL.BANK_BRANCH_NO               ,           ");
        sql.append("          GL.TRADING_PARTNER              ,           ");
        sql.append("          GL.TRADING_PARTNER_PARK         ,           ");
        sql.append("          GL.SPECIAL_GL                   ,           ");
        sql.append("          GL.DATE_BASE_LINE               ,           ");
        sql.append("          GL.DATE_VALUE                   ,           ");
        sql.append("          GL.ASSET_NO                     ,           ");
        sql.append("          GL.ASSET_SUB_NO                 ,           ");
        sql.append("          GL.QTY                          ,           ");
        sql.append("          GL.UOM                          ,           ");
        sql.append("          GL.REFERENCE1                   ,           ");
        sql.append("          GL.REFERENCE2                   ,           ");
        sql.append("          GL.PO_DOCUMENT_NO               ,           ");
        sql.append("          GL.PO_LINE                      ,           ");
        sql.append("          GL.INCOME                       ,           ");
        sql.append("          GL.PAYMENT_BLOCK                ,           ");
        sql.append("          GL.PAYMENT_REFERENCE            ,           ");
        sql.append("          GL.LINE                         ,           ");
        sql.append("          CD.NAME                         AS COMP_CODE_NAME,           ");
        sql.append("          PC.NAME                         AS PAYMENT_CENTER_NAME,           ");
        sql.append("          CC.NAME                         AS COST_CENTER_NAME,           ");
        sql.append("          PM.NAME                         AS PAYMENT_METHOD_NAME,           ");
        sql.append("          SF.NAME                         AS FUND_SOURCE_NAME,           ");
        sql.append("          BC.NAME                         AS BG_CODE_NAME,           ");
        sql.append("          MA.NAME                         AS BG_ACTIVITY_NAME,           ");
        sql.append("          VD.NAME                         AS NAME1,           ");
        sql.append("          VD.NAME2                        AS NAME2,           ");
        sql.append("          VD.TAXID                        AS TAX_ID,           ");
        sql.append("          TRIM(VL.ADDRESS1 || ' ' || VL.ADDRESS2 || ' ' || VL.ADDRESS3 || ' ' || VL.ADDRESS4 || ' ' || VL.ADDRESS5)         AS ADDRESS,           ");
        sql.append("          VL.CITY                         AS CITY,           ");
        sql.append("          VL.POSTAL                       AS POSTAL,           ");
        sql.append("          VL.REGIONNAME                   AS REGION_NAME,           ");
        sql.append("          VC.NAME                         AS COUNTRY,           ");
        sql.append("          VC.COUNTRYCODE                  AS COUNTRY_CODE,           ");
        sql.append("          VB.BANKACCOUNTTYPE              AS PAYEE_BANK_ACCOUNT_TYPE,           ");
        sql.append("          VB.ROUTINGNO                    AS PAYEE_BANK_NO,           ");
        sql.append("          VB.ACCOUNTNO                    AS PAYEE_BANK_ACCOUNT_NO,           ");
        sql.append("          VB.A_NAME                       AS ACCOUNT_HOLDER_NAME,           ");
        sql.append("          SUBSTR(BK.ROUTINGNO, 1, 3)      AS PAYEE_BANK,           ");
        sql.append("          cb.NAME                         AS PAYEE_BANK_NAME,           ");
        sql.append("          BK.ROUTINGNO                    AS PAYEE_BANK_KEY,           ");
        sql.append("          BK.SWIFTCODE                    AS SWIFT_CODE,           ");
        sql.append("          BK.DESCRIPTION                  AS PAYEE_BANK_REFERENCE,           ");
        sql.append("          BA.NAME                         AS AREA_NAME,           ");
        sql.append("          VD.ISACTIVE           AS VENDOR_ACTIVE,           ");
        sql.append("          PS.CONFIRMSTATUS      AS VENDOR_STATUS,           ");
        sql.append("          (CASE WHEN gl.PAYEE IS NULL THEN NULL ELSE VD.ISACTIVE END)                             AS PAYEE_ACTIVE,           ");
        sql.append("          (CASE WHEN gl.PAYEE IS NULL THEN NULL ELSE PS.CONFIRMSTATUS END)                       AS PAYEE_STATUS,           ");
        sql.append("          (CASE WHEN gl.PAYEE IS NULL THEN NULL ELSE 'Y' END)                       AS PAYEE_CONFIRM           ");
        sql.append("FROM ");
        sql.append("    GL_HEAD               gh ");
        sql.append("    INNER JOIN gl_line               gl ON gh.ORIGINAL_DOCUMENT_NO = gl.ORIGINAL_DOCUMENT_NO AND GH.DOCUMENT_BASE_TYPE = 'API' ");
        sql.append("    AND (gl.PAYMENT_BLOCK = '' OR gl.PAYMENT_BLOCK = ' ' OR gl.PAYMENT_BLOCK IS NULL) AND gl.ACCOUNT_TYPE = 'K' AND GH.COMPANY_CODE = GL.COMPANY_CODE AND GH.ORIGINAL_FISCAL_YEAR = GL.ORIGINAL_FISCAL_YEAR ");
        sql.append("    INNER JOIN ").append(schema).append(".TH_CAPAYMENTMETHOD").append(" pm ON gl.payment_method = pm.valuecode ");
        log.info("companyConditions {}", companyConditions);
        if (null != companyConditions && !companyConditions.isEmpty()) {
            if (companyConditions.get(0).getCompanyFrom().equalsIgnoreCase("99999")) {
                sql.append("    AND pm.isdirect = 'N' ");
            } else {
                sql.append("    AND pm.isdirect = 'Y' ");
            }
        }
//        log.info("companyConditions {}", companyConditions);
//        if (null != companyConditions && !companyConditions.isEmpty()) {
//            log.info("inside");
//            if (companyConditions.get(0).getCompanyFrom().equalsIgnoreCase("99999")) {
//                sql.append("    AND (gl.PAYMENT_BLOCK = '' OR gl.PAYMENT_BLOCK = ' ' OR gl.PAYMENT_BLOCK IS NULL) AND gl.ACCOUNT_TYPE = 'K' AND GH.COMPANY_CODE = GL.COMPANY_CODE AND GH.ORIGINAL_FISCAL_YEAR = GL.ORIGINAL_FISCAL_YEAR ");
//                sql.append("    AND GL.PAYMENT_METHOD IN ('2', '4', '7', '9', 'L')");
//            } else {
//                sql.append("    AND (gl.PAYMENT_BLOCK = '' OR gl.PAYMENT_BLOCK = ' ' OR gl.PAYMENT_BLOCK IS NULL) AND gl.ACCOUNT_TYPE = 'K' AND GH.COMPANY_CODE = GL.COMPANY_CODE AND GH.ORIGINAL_FISCAL_YEAR = GL.ORIGINAL_FISCAL_YEAR  ");
//                sql.append("    AND( (GH.DOCUMENT_TYPE = 'K0' AND GL.PAYMENT_METHOD IN ('A', 'B','J', 'I')) ");
//                sql.append("    OR (GH.DOCUMENT_TYPE = 'K3' AND GH.DOCUMENT_BASE_TYPE = 'API') ");
//                sql.append("    OR GH.DOCUMENT_TYPE NOT IN ('KL', 'K1', 'K0', 'KN', 'KI', 'K2','K3','KE','KX')   ) ");
//                sql.append("    AND GL.PAYMENT_METHOD IN ('1', '3', 'I', 'J', 'K', 'A', 'B', 'F','C','8','H','D','G','E')   ");
//            }
//        } else {
//            log.info("yes no");
//            sql.append("    AND (gl.PAYMENT_BLOCK = '' OR gl.PAYMENT_BLOCK = ' ' OR gl.PAYMENT_BLOCK IS NULL) AND gl.ACCOUNT_TYPE = 'K' AND GH.COMPANY_CODE = GL.COMPANY_CODE AND GH.ORIGINAL_FISCAL_YEAR = GL.ORIGINAL_FISCAL_YEAR  ");
//            sql.append("    AND GL.PAYMENT_METHOD IN ('2', '4', '9', 'L')   ");
//        }

        sql.append(generateSQLWhereAuthorizedCommon(username));

//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".AD_ORG@").append(user).append(" ad on ad.value = gh.COMPANY_CODE ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_CACOMPCODE@").append(user).append(" cd ON gh.COMPANY_CODE = cd.valuecode ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGPAYMENTCENTER@").append(user).append(" pc ON gl.payment_center = pc.valuecode ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGCOSTCENTER@").append(user).append(" cc ON gl.cost_center = cc.valuecode ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGFUNDSOURCE@").append(user).append(" sf ON gl.fund_source = sf.valuecode ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_CAPAYMENTMETHOD@").append(user).append(" pm ON gl.payment_method = pm.valuecode ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_YEAR@").append(user).append(" cy ON cy.fiscalyear = gh.ORIGINAL_FISCAL_YEAR ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETCODE@").append(user).append(" bc ON gl.bg_code = bc.valuecode AND ad.ad_org_id = bc.ad_org_id and bc.c_year_id = cy.c_year_id ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETACTIVITY@").append(user).append(" ma ON gl.bg_activity = ma.valuecode AND (ad.ad_org_id = ma.ad_org_id OR ad.ad_org_id = '0') AND ma.c_year_id = cy.c_year_id ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bpartner@").append(user).append(" vd ON vd.value = (CASE WHEN gl.PAYEE IS NULL THEN gl.VENDOR ELSE gl.PAYEE END) ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bpartner_location@").append(user).append(" vdl ON vd.c_bpartner_id = vdl.c_bpartner_id ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_location@").append(user).append(" vl ON vdl.c_location_id = vl.c_location_id ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_country@").append(user).append("             vc ON vl.c_country_id = vc.c_country_id ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bp_bankaccount@").append(user).append("      vb ON vd.c_bpartner_id = vb.c_bpartner_id ");
////        sql.append("                                      AND gl.bank_account_no = vb.accountno ");
//        sql.append("                                      AND gl.bank_account_no = vb.accountno  AND vb.isactive = 'Y' ");
//
//
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bank@").append(user).append("                bk ON vb.c_bank_id = bk.c_bank_id ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".th_cabank@").append(user).append("                cb ON substr(bk.routingno, 1, 3) = cb.valuecode ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".th_bgbudgetarea@").append(user).append(" ba on ba.fiarea = gl.FI_AREA ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_APBPARTNERSTATUS@").append(user).append(" PS ON VD.C_BPARTNER_ID = PS.C_BPARTNER_ID ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_BPARTNER@").append(user).append(" VDM ON VDM.VALUE = GL.VENDOR ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_BP_RELATION@").append(user).append(" RL  ON DECODE(GL.PAYEE, NULL, 0, VDM.C_BPARTNER_ID) = DECODE(GL.PAYEE, NULL, 1, RL.C_BPARTNER_ID) AND RL.AD_ORG_ID = AD.AD_ORG_ID ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_BPARTNER@").append(user).append(" PY  ON DECODE(GL.PAYEE, NULL, 0, RL.C_BPARTNERRELATION_ID) =  DECODE(GL.PAYEE, NULL, 1, PY.C_BPARTNER_ID) AND DECODE(GL.PAYEE, NULL, '0', PY.VALUE) = DECODE(GL.PAYEE, NULL, '1', GL.PAYEE) ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_APALTERNATEPAYEESTATUS@").append(user).append(" APS ON APS.C_BP_RELATION_ID = RL.C_BP_RELATION_ID ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_APBPARTNERSTATUS@").append(user).append(" PYS ON PYS.C_BPARTNER_ID = PY.C_BPARTNER_ID ");

        sql.append("    LEFT OUTER JOIN ").append(schema).append(".AD_ORG").append(" ad on ad.value = gh.COMPANY_CODE ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_CACOMPCODE").append(" cd ON gh.COMPANY_CODE = cd.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGPAYMENTCENTER").append(" pc ON gl.payment_center = pc.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGCOSTCENTER").append(" cc ON gl.cost_center = cc.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGFUNDSOURCE").append(" sf ON gl.fund_source = sf.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_YEAR").append(" cy ON cy.fiscalyear = gh.ORIGINAL_FISCAL_YEAR ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETCODE").append(" bc ON gl.bg_code = bc.valuecode AND ad.ad_org_id = bc.ad_org_id and bc.c_year_id = cy.c_year_id ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETACTIVITY").append(" ma ON gl.bg_activity = ma.valuecode AND (ad.ad_org_id = ma.ad_org_id OR ad.ad_org_id = '0') AND ma.c_year_id = cy.c_year_id ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bpartner").append(" vd ON vd.value = gl.vendor ");
        //note อันล่างไม่รู้ว่าทำไมตอนนั้น ถ้าเป็นโอนสิทธิ์ทำไมถึงใช้ตัวล่าง
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bpartner").append(" vd ON vd.value = (CASE WHEN gl.PAYEE IS NULL THEN gl.VENDOR ELSE gl.PAYEE END) ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bpartner_location").append(" vdl ON vd.c_bpartner_id = vdl.c_bpartner_id ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_location").append(" vl ON vdl.c_location_id = vl.c_location_id ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_country").append("             vc ON vl.c_country_id = vc.c_country_id ");
        // Edit duplicate
        // dup
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bp_bankaccount").append("      vb ON vd.c_bpartner_id = vb.c_bpartner_id ");
//        sql.append("                                      AND gl.bank_account_no = vb.accountno AND GL.BANK_ACCOUNT_HOLDER_NAME = VB.A_NAME AND vb.isactive = 'Y' ");
        // no dup
        sql.append("          OUTER APPLY (           ");
        sql.append("          SELECT *           ");
        sql.append("          FROM ").append(schema).append(".C_BP_BANKACCOUNT BB           ");
        sql.append("          WHERE BB.ISACTIVE = 'Y'           ");
        sql.append("          AND vd.C_BPARTNER_ID = BB.C_BPARTNER_ID           ");
        sql.append("          AND GL.BANK_ACCOUNT_NO = BB.ACCOUNTNO           ");
        sql.append("          ORDER BY BB.ACCOUNTNO           ");
        sql.append("          FETCH FIRST ROW ONLY           ");
        sql.append("          ) vb           ");

        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bank").append("                bk ON vb.c_bank_id = bk.c_bank_id ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".th_cabank").append("                cb ON substr(bk.routingno, 1, 3) = cb.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".th_bgbudgetarea").append(" ba on ba.fiarea = gl.FI_AREA ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_APBPARTNERSTATUS").append(" PS ON VD.C_BPARTNER_ID = PS.C_BPARTNER_ID ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_BPARTNER").append(" VDM ON VDM.VALUE = GL.VENDOR ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_BP_RELATION").append(" RL  ON DECODE(GL.PAYEE, NULL, 0, VDM.C_BPARTNER_ID) = DECODE(GL.PAYEE, NULL, 1, RL.C_BPARTNER_ID) AND RL.AD_ORG_ID = AD.AD_ORG_ID ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_BPARTNER").append(" PY  ON DECODE(GL.PAYEE, NULL, 0, RL.C_BPARTNERRELATION_ID) =  DECODE(GL.PAYEE, NULL, 1, PY.C_BPARTNER_ID) AND DECODE(GL.PAYEE, NULL, '0', PY.VALUE) = DECODE(GL.PAYEE, NULL, '1', GL.PAYEE) ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_APALTERNATEPAYEESTATUS").append(" APS ON APS.C_BP_RELATION_ID = RL.C_BP_RELATION_ID ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_APBPARTNERSTATUS").append(" PYS ON PYS.C_BPARTNER_ID = PY.C_BPARTNER_ID ");
        sql.append("WHERE ");
        sql.append("    1 = 1 ");
        sql.append("          AND GH.PAYMENT_DOCUMENT_NO IS NULL           ");
        sql.append("          AND GL.CLEARING_DOCUMENT_NO IS NULL           ");
        sql.append("          AND NVL(GH.PAYMENT_ID,0) = 0           ");
        sql.append("          AND GH.REVERSE_ORIGINAL_DOCUMENT_NO IS NULL           ");
        sql.append("           AND (VD.VALUE IS NOT NULL OR GL.PAYEE IS NULL)           ");

//        if (!Util.isEmpty(parameter.getPostDate())) {
//            Timestamp postDate = Util.stringToTimestamp(parameter.getSaveDate());
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(postDate);
//            String fiscalYear = "";
//            if (calendar.get(Calendar.MONTH) >= Calendar.OCTOBER) {
//                fiscalYear = String.valueOf(calendar.get(Calendar.YEAR) + 1);
//            }
//            sql.append(SqlUtil.whereClause(fiscalYear, "GH.ORIGINAL_FISCAL_YEAR", params));
//        }

        if (!Util.isEmpty(parameter.getSaveDate())) {
            sql.append(SqlUtil.whereClause(parameter.getSaveDate(), "GH.DOCUMENT_CREATED", params));
        }

        if (!Util.isEmpty(parameter.getPaymentDate())) {
            sql.append(SqlUtil.whereClauseLess(parameter.getPaymentDate(), "GL.DATE_BASE_LINE", params));
        }

        DynamicCondition.companyCondition(companyConditions, sql, params);
        DynamicCondition.newVendorCondition(vendors, sql, params, "VD.VALUE");
        DynamicCondition.newIndependentCondition(independents, sql, params);

        List<PaymentMethod> paymentMethodList = new ArrayList<>();

        for (char ch : parameter.getPaymentMethod().toCharArray()) {
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setPaymentMethodFrom(String.valueOf(ch));
            paymentMethod.setOptionExclude(false);
            paymentMethodList.add(paymentMethod);
        }

        DynamicCondition.paymentMethodForRun(paymentMethodList, sql, params);


        sql.append("            )TMP           ");
        sql.append("           LEFT  JOIN  GL_HEAD H           ");
        sql.append("            ON TMP.REAL_ORIGINAL_DOCUMENT_NO = H.ORIGINAL_DOCUMENT_NO           ");
        sql.append("           AND TMP.REAL_ORIGINAL_COMPANY_CODE = H.COMPANY_CODE           ");
        sql.append("           AND TMP.REAL_ORIGINAL_FISCAL_YEAR = H.ORIGINAL_FISCAL_YEAR           ");
//        sql.append("           FOR UPDATE           ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

//        for (int i = 0; i < objParams.length; i++) {
//            System.out.println(objParams[i]);
//        }
        log.info("sql  {}", sql.toString());
        this.jdbcTemplate.setFetchSize(5000);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public List<PrepareProposalDocument> findDocumentK3OrKX(String originalDocument, String originalFiscalYear, String originalCompanyCode, String username) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT H.DOCUMENT_TYPE AS REAL_ORIGINAL_DOCUMENT_TYPE,TMP.* ");
        sql.append(" FROM ( ");

        sql.append(" SELECT /*+ NO_USE_NL(PS,BA) */");
        sql.append("          GH.ID                           AS ID,           ");

        sql.append("          substr(GH.ORIGINAL_DOCUMENT, 0, 10) AS REAL_ORIGINAL_DOCUMENT_NO,           ");
        sql.append("          substr(GH.ORIGINAL_DOCUMENT, 11, 5) AS REAL_ORIGINAL_COMPANY_CODE,           ");
        sql.append("          substr(GH.ORIGINAL_DOCUMENT, 16, 4) AS REAL_ORIGINAL_FISCAL_YEAR,           ");
        sql.append("          GL.PAYMENT_CENTER                   AS LINE_PAYMENT_CENTER,           ");
        sql.append("          GL.WTX_AMOUNT                       AS LINE_WTX_AMOUNT,           ");
        sql.append("          GL.WTX_BASE                         AS LINE_WTX_BASE,           ");
        sql.append("          GL.WTX_AMOUNT_P                     AS LINE_WTX_AMOUNT_P,           ");
        sql.append("          GL.WTX_BASE_P                       AS LINE_WTX_BASE_P,           ");

        sql.append("          GH.DOCUMENT_TYPE                ,           ");
        sql.append("          GH.COMPANY_CODE                 ,           ");
        sql.append("          GH.DATE_DOC                     ,           ");
        sql.append("          GH.DATE_ACCT                    ,           ");
        sql.append("          GH.PERIOD                       ,           ");
        sql.append("          GH.CURRENCY                     ,           ");
        sql.append("          GH.INVOICE_DOCUMENT_NO          ,           ");
        sql.append("          GH.REVERSE_INVOICE_DOCUMENT_NO  ,           ");
        sql.append("          GH.ORIGINAL_DOCUMENT_NO         ,           ");
        sql.append("          GH.ORIGINAL_FISCAL_YEAR         ,           ");
        sql.append("          GH.REVERSE_ORIGINAL_DOCUMENT_NO ,           ");
        sql.append("          GH.REVERSE_ORIGINAL_FISCAL_YEAR ,           ");
        sql.append("          GH.COST_CENTER1                 ,           ");
        sql.append("          GH.COST_CENTER2                 ,           ");
        sql.append("          GH.HEADER_REFERENCE             ,           ");
        sql.append("          GH.DOCUMENT_HEADER_TEXT         ,           ");
        sql.append("          GH.HEADER_REFERENCE2            ,           ");
        sql.append("          GH.REVERSE_DATE_ACCT            ,           ");
        sql.append("          GH.REVERSE_REASON               ,           ");
        sql.append("          GH.ORIGINAL_DOCUMENT            ,           ");
        sql.append("          GH.DOCUMENT_CREATED             ,           ");
        sql.append("          GH.USER_PARK                    ,           ");
        sql.append("          GH.USER_POST                    ,           ");
        sql.append("          GL.POSTING_KEY                  ,           ");
        sql.append("          GL.ACCOUNT_TYPE                 ,           ");
        sql.append("          GL.DR_CR                        ,           ");
        sql.append("          GL.GL_ACCOUNT                   ,           ");
        sql.append("          GL.FI_AREA                      ,           ");
        sql.append("          GL.COST_CENTER                  ,           ");
        sql.append("          GL.FUND_SOURCE                  ,           ");
        sql.append("          GL.BG_CODE                      ,           ");
        sql.append("          GL.BG_ACTIVITY                  ,           ");
        sql.append("          GL.COST_ACTIVITY                ,           ");
        sql.append("          GL.AMOUNT                       ,           ");
        sql.append("          GL.REFERENCE3                   ,           ");
        sql.append("          GL.ASSIGNMENT                   ,           ");
        sql.append("          GL.BR_DOCUMENT_NO               ,           ");
        sql.append("          GL.BR_LINE                      ,           ");
        sql.append("          GL.PAYMENT_CENTER               ,           ");
        sql.append("          GL.BANK_BOOK                    ,           ");
        sql.append("          GL.SUB_ACCOUNT                  ,           ");
        sql.append("          GL.SUB_ACCOUNT_OWNER            ,           ");
        sql.append("          GL.DEPOSIT_ACCOUNT              ,           ");
        sql.append("          GL.DEPOSIT_ACCOUNT_OWNER        ,           ");
        sql.append("          GL.GPSC                         ,           ");
        sql.append("          GL.GPSC_GROUP                   ,           ");
        sql.append("          GL.LINE_ITEM_TEXT               ,           ");
        sql.append("          GL.LINE_DESC                    ,           ");
        sql.append("          GL.PAYMENT_TERM                 ,           ");
        sql.append("          GL.PAYMENT_METHOD               ,           ");
        sql.append("          GL.WTX_TYPE                     ,           ");
        sql.append("          GL.WTX_CODE                     ,           ");
        sql.append("          GL.WTX_BASE                     ,           ");
        sql.append("          nvl(GL.WTX_AMOUNT, 0)           AS WTX_AMOUNT,           ");
        sql.append("          GL.WTX_TYPE_P                   ,           ");
        sql.append("          GL.WTX_CODE_P                   ,           ");
        sql.append("          GL.WTX_BASE_P                   ,           ");
        sql.append("          GL.WTX_AMOUNT_P                 ,           ");
        sql.append("          GL.VENDOR                       ,           ");
        sql.append("          GL.VENDOR_TAX_ID                ,           ");
        sql.append("          GL.PAYEE                        ,           ");
        sql.append("          GL.PAYEE_TAX_ID                 ,           ");
        sql.append("          GL.BANK_ACCOUNT_NO              ,           ");
        sql.append("          GL.BANK_BRANCH_NO               ,           ");
        sql.append("          GL.TRADING_PARTNER              ,           ");
        sql.append("          GL.TRADING_PARTNER_PARK         ,           ");
        sql.append("          GL.SPECIAL_GL                   ,           ");
        sql.append("          GL.DATE_BASE_LINE               ,           ");
        sql.append("          GL.DATE_VALUE                   ,           ");
        sql.append("          GL.ASSET_NO                     ,           ");
        sql.append("          GL.ASSET_SUB_NO                 ,           ");
        sql.append("          GL.QTY                          ,           ");
        sql.append("          GL.UOM                          ,           ");
        sql.append("          GL.REFERENCE1                   ,           ");
        sql.append("          GL.REFERENCE2                   ,           ");
        sql.append("          GL.PO_DOCUMENT_NO               ,           ");
        sql.append("          GL.PO_LINE                      ,           ");
        sql.append("          GL.INCOME                       ,           ");
        sql.append("          GL.PAYMENT_BLOCK                ,           ");
        sql.append("          GL.PAYMENT_REFERENCE            ,           ");
        sql.append("          GL.LINE                         ,           ");
        sql.append("          CD.NAME                         AS COMP_CODE_NAME,           ");
        sql.append("          PC.NAME                         AS PAYMENT_CENTER_NAME,           ");
        sql.append("          CC.NAME                         AS COST_CENTER_NAME,           ");
        sql.append("          PM.NAME                         AS PAYMENT_METHOD_NAME,           ");
        sql.append("          SF.NAME                         AS FUND_SOURCE_NAME,           ");
        sql.append("          BC.NAME                         AS BG_CODE_NAME,           ");
        sql.append("          MA.NAME                         AS BG_ACTIVITY_NAME,           ");
        sql.append("          VD.NAME                         AS NAME1,           ");
        sql.append("          VD.NAME2                        AS NAME2,           ");
        sql.append("          VD.TAXID                        AS TAX_ID,           ");
        sql.append("          TRIM(VL.ADDRESS1 || ' ' || VL.ADDRESS2 || ' ' || VL.ADDRESS3 || ' ' || VL.ADDRESS4 || ' ' || VL.ADDRESS5)         AS ADDRESS,           ");
        sql.append("          VL.CITY                         AS CITY,           ");
        sql.append("          VL.POSTAL                       AS POSTAL,           ");
        sql.append("          VL.REGIONNAME                   AS REGION_NAME,           ");
        sql.append("          VC.NAME                         AS COUNTRY,           ");
        sql.append("          VC.COUNTRYCODE                  AS COUNTRY_CODE,           ");
        sql.append("          VB.BANKACCOUNTTYPE              AS PAYEE_BANK_ACCOUNT_TYPE,           ");
        sql.append("          VB.ROUTINGNO                    AS PAYEE_BANK_NO,           ");
        sql.append("          VB.ACCOUNTNO                    AS PAYEE_BANK_ACCOUNT_NO,           ");
        sql.append("          VB.A_NAME                       AS ACCOUNT_HOLDER_NAME,           ");
        sql.append("          SUBSTR(BK.ROUTINGNO, 1, 3)      AS PAYEE_BANK,           ");
        sql.append("          cb.NAME                         AS PAYEE_BANK_NAME,           ");
        sql.append("          BK.ROUTINGNO                    AS PAYEE_BANK_KEY,           ");
        sql.append("          BK.SWIFTCODE                    AS SWIFT_CODE,           ");
        sql.append("          BK.DESCRIPTION                  AS PAYEE_BANK_REFERENCE,           ");
        sql.append("          BA.NAME                         AS AREA_NAME,           ");
        sql.append("          VD.ISACTIVE           AS VENDOR_ACTIVE,           ");
        sql.append("          PS.CONFIRMSTATUS      AS VENDOR_STATUS,           ");
        sql.append("          (CASE WHEN gl.PAYEE IS NULL THEN NULL ELSE VD.ISACTIVE END)                             AS PAYEE_ACTIVE,           ");
        sql.append("          (CASE WHEN gl.PAYEE IS NULL THEN NULL ELSE PS.CONFIRMSTATUS END)                       AS PAYEE_STATUS,           ");
        sql.append("          (CASE WHEN gl.PAYEE IS NULL THEN NULL ELSE 'Y' END)                       AS PAYEE_CONFIRM           ");
        sql.append("FROM ");
        sql.append("    GL_HEAD               gh ");
        sql.append("    INNER JOIN gl_line               gl ON gh.ORIGINAL_DOCUMENT_NO = gl.ORIGINAL_DOCUMENT_NO ");
        sql.append("    AND (gl.PAYMENT_BLOCK = '' OR gl.PAYMENT_BLOCK = ' ' OR gl.PAYMENT_BLOCK IS NULL) AND gl.ACCOUNT_TYPE = 'K' AND GH.COMPANY_CODE = GL.COMPANY_CODE AND GH.ORIGINAL_FISCAL_YEAR = GL.ORIGINAL_FISCAL_YEAR  ");

//        sql.append(generateSQLWhereAuthorizedCommon(jwt, params));

        sql.append("    LEFT OUTER JOIN ").append(schema).append(".AD_ORG").append(" ad on ad.value = gh.COMPANY_CODE ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_CACOMPCODE").append(" cd ON gh.COMPANY_CODE = cd.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGPAYMENTCENTER").append(" pc ON gl.payment_center = pc.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGCOSTCENTER").append(" cc ON gl.cost_center = cc.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGFUNDSOURCE").append(" sf ON gl.fund_source = sf.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_CAPAYMENTMETHOD").append(" pm ON gl.payment_method = pm.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_YEAR").append(" cy ON cy.fiscalyear = gh.ORIGINAL_FISCAL_YEAR ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETCODE").append(" bc ON gl.bg_code = bc.valuecode AND ad.ad_org_id = bc.ad_org_id and bc.c_year_id = cy.c_year_id ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETACTIVITY").append(" ma ON gl.bg_activity = ma.valuecode AND (ad.ad_org_id = ma.ad_org_id OR ad.ad_org_id = '0') AND ma.c_year_id = cy.c_year_id ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bpartner").append(" vd ON vd.value = (CASE WHEN gl.PAYEE IS NULL THEN gl.VENDOR ELSE gl.PAYEE END) ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bpartner_location").append(" vdl ON vd.c_bpartner_id = vdl.c_bpartner_id ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_location").append(" vl ON vdl.c_location_id = vl.c_location_id ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_country").append("             vc ON vl.c_country_id = vc.c_country_id ");
        // Edit duplicate
        // dup
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bp_bankaccount").append("      vb ON vd.c_bpartner_id = vb.c_bpartner_id ");
//        sql.append("                                      AND gl.bank_account_no = vb.accountno AND GL.BANK_ACCOUNT_HOLDER_NAME = VB.A_NAME AND vb.isactive = 'Y' ");
        // no dup
        sql.append("          OUTER APPLY (           ");
        sql.append("          SELECT *           ");
        sql.append("          FROM ").append(schema).append(".C_BP_BANKACCOUNT BB           ");
        sql.append("          WHERE BB.ISACTIVE = 'Y'           ");
        sql.append("          AND vd.C_BPARTNER_ID = BB.C_BPARTNER_ID           ");
        sql.append("          AND GL.BANK_ACCOUNT_NO = BB.ACCOUNTNO           ");
        sql.append("          ORDER BY BB.ACCOUNTNO           ");
        sql.append("          FETCH FIRST ROW ONLY           ");
        sql.append("          ) vb           ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".c_bank").append("                bk ON vb.c_bank_id = bk.c_bank_id ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".th_cabank").append("                cb ON substr(bk.routingno, 1, 3) = cb.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".th_bgbudgetarea").append(" ba on ba.fiarea = gl.FI_AREA ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_APBPARTNERSTATUS").append(" PS ON VD.C_BPARTNER_ID = PS.C_BPARTNER_ID ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_BPARTNER").append(" VDM ON VDM.VALUE = GL.VENDOR ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_BP_RELATION").append(" RL  ON DECODE(GL.PAYEE, NULL, 0, VDM.C_BPARTNER_ID) = DECODE(GL.PAYEE, NULL, 1, RL.C_BPARTNER_ID) AND RL.AD_ORG_ID = AD.AD_ORG_ID ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_BPARTNER").append(" PY  ON DECODE(GL.PAYEE, NULL, 0, RL.C_BPARTNERRELATION_ID) =  DECODE(GL.PAYEE, NULL, 1, PY.C_BPARTNER_ID) AND DECODE(GL.PAYEE, NULL, '0', PY.VALUE) = DECODE(GL.PAYEE, NULL, '1', GL.PAYEE) ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_APALTERNATEPAYEESTATUS").append(" APS ON APS.C_BP_RELATION_ID = RL.C_BP_RELATION_ID ");
//        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_APBPARTNERSTATUS").append(" PYS ON PYS.C_BPARTNER_ID = PY.C_BPARTNER_ID ");
        sql.append("WHERE ");
        sql.append("    1 = 1 ");
        sql.append("          AND GH.PAYMENT_DOCUMENT_NO IS NULL           ");
        sql.append("          AND GL.CLEARING_DOCUMENT_NO IS NULL           ");
        sql.append("          AND NVL(GH.PAYMENT_ID,0) = 0           ");
        sql.append("          AND GH.REVERSE_ORIGINAL_DOCUMENT_NO IS NULL           ");
        // Benz edit, need account type = 'K' and doc vendor for gen file
        sql.append("   AND GH.DOCUMENT_BASE_TYPE = 'APC' ");
//        sql.append("   AND GH.DOCUMENT_BASE_TYPE = 'API' ");
        params.add(originalDocument);
        sql.append("   AND GL.INVOICE_DOCUMENT_NO = ? ");
//        sql.append("   AND GL.INVOICE_DOCUMENT_NO = (SELECT GL.ORIGINAL_DOCUMENT_NO FROM GL_HEAD GH LEFT JOIN GL_LINE GL ON GH.ID = GL.GL_HEAD_ID WHERE GL.INVOICE_DOCUMENT_NO = ? AND GL.ACCOUNT_TYPE = 'K') ");
        params.add(originalFiscalYear);
        sql.append("   AND GL.INVOICE_FISCAL_YEAR = ? ");
        params.add(originalCompanyCode);
        sql.append("   AND GH.COMPANY_CODE = ? ");


        sql.append("            )TMP           ");
        sql.append("           LEFT  JOIN  GL_HEAD H           ");
        sql.append("            ON TMP.REAL_ORIGINAL_DOCUMENT_NO = H.ORIGINAL_DOCUMENT_NO           ");
        sql.append("           AND TMP.REAL_ORIGINAL_COMPANY_CODE = H.COMPANY_CODE           ");
        sql.append("           AND TMP.REAL_ORIGINAL_FISCAL_YEAR = H.ORIGINAL_FISCAL_YEAR           ");
        this.jdbcTemplate.setFetchSize(5000);
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public synchronized Long getNextSeries(boolean isProposal) {
        String sql = "";
        if (isProposal) {
            sql = "SELECT PROPOSAL_DOCUMENT_SEQ.NEXTVAL FROM DUAL";
        } else {
            sql = "SELECT PAYMENT_DOCUMENT_SEQ.NEXTVAL FROM DUAL";
        }
        return this.jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public void updateNextSeries(Long lastNumber, boolean isProposal) {
        String sql;
        if (isProposal) {
            sql = "ALTER SEQUENCE PROPOSAL_DOCUMENT_SEQ RESTART START WITH " + lastNumber;
        } else {
            sql = "ALTER SEQUENCE PAYMENT_DOCUMENT_SEQ RESTART START WITH " + lastNumber;
        }
        this.jdbcTemplate.update(sql);
    }



    private String generateSQLWhereAuthorizedCommon(String username) {
        StringBuilder sb = new StringBuilder();

        String AUTHORIZATION_OBJECT_NAME = "FICOMMON";
        String AUTHORIZATION_ACTIVITY = AuthorizeUtilService.READ_ACTIVITY;
        String authSQL = "";
        // company code
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.COMPANY_CODE_ATTRIBUTE, "GH.COMPANY_CODE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // area
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.AREA_ATTRIBUTE, "LPAD(GL.FI_AREA, 5, 'P')", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // payment center
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.PAYMENT_CENTER_ATTRIBUTE, "GL.PAYMENT_CENTER", null);
        if (Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // cost center
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.COST_CENTER_ATTRIBUTE, "GL.COST_CENTER", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // doc type
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.DOC_TYPE_ATTRIBUTE, "GH.DOCUMENT_TYPE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // budget code
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.BUDGET_CODE_ATTRIBUTE, "GL.BG_CODE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // budget activity
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.BUDGET_ACTIVITY_ATTRIBUTE, "GL.BG_ACTIVITY", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // source of fund
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.FUND_SOURCE_ATTRIBUTE, "GL.FUND_SOURCE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // account type
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.ACCOUNT_TYPE_ATTIBUTE, "GL.ACCOUNT_TYPE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        return sb.toString();
    }

    private String generateSQLWhereAuthorizedAuto(String username) {
        StringBuilder sb = new StringBuilder();

        String AUTHORIZATION_OBJECT_NAME = "FICOMMON";
        String AUTHORIZATION_ACTIVITY = AuthorizeUtilService.READ_ACTIVITY;
        String authSQL = "";
        // company code
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.COMPANY_CODE_ATTRIBUTE, "GH.COMPANY_CODE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // area
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.AREA_ATTRIBUTE, "LPAD(GL.FI_AREA, 5, 'P')", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // payment center
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.PAYMENT_CENTER_ATTRIBUTE, "GL.PAYMENT_CENTER", null);
        if (Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // cost center
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.COST_CENTER_ATTRIBUTE, "GL.COST_CENTER", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // doc type
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.DOC_TYPE_ATTRIBUTE, "GH.DOCUMENT_TYPE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // budget code
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.BUDGET_CODE_ATTRIBUTE, "GL.BG_CODE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // budget activity
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.BUDGET_ACTIVITY_ATTRIBUTE, "GL.BG_ACTIVITY", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // source of fund
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.FUND_SOURCE_ATTRIBUTE, "GL.FUND_SOURCE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // account type
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.ACCOUNT_TYPE_ATTIBUTE, "GL.ACCOUNT_TYPE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        return sb.toString();
    }

    @Override
    public List<PrepareSelectProposalDocument> findUnBlockDocumentCanPayByParameterNew(ParameterConfig parameterConfig, String username, Date runDate) {
        List<Object> params = new ArrayList<>();

        Parameter parameter = parameterConfig.getParameter();
        AdditionLog additionLog = parameterConfig.getAdditionLog();
        List<CompanyCondition> companyConditions = parameter.getCompanyCondition();
        List<VendorConfig> vendors = parameter.getVendor();
        List<Independent> independents = parameterConfig.getIndependent();

        StringBuilder sql = new StringBuilder();

        sql.append("          SELECT H.DOCUMENT_TYPE AS REAL_ORIGINAL_DOCUMENT_TYPE, TMP.*           ");
        sql.append("          FROM (SELECT /*+ USE_MERGE(AC_ALTERNATIVE,PS_ALTERNATIVE,BA_ALTERNATIVE,CB_ALTERNATIVE,BK_ALTERNATIVE,VB_ALTERNATIVE,VC_ALTERNATIVE,VL_ALTERNATIVE,VDL_ALTERNATIVE,VD_ALTERNATIVE,AC_MAIN,PS_MAIN,BA_MAIN,CB_MAIN,BK_MAIN,VB_MAIN,VC_MAIN,VL_MAIN,VDL_MAIN,VD_MAIN,MA,BC,CY,SF,CC,PC,CD,AD,PM) ORDERED */ GH.ID                                  AS ID,           ");
        sql.append("          substr(GH.ORIGINAL_DOCUMENT, 0, 10)    AS REAL_ORIGINAL_DOCUMENT_NO,           ");
        sql.append("          substr(GH.ORIGINAL_DOCUMENT, 11, 5)    AS REAL_ORIGINAL_COMPANY_CODE,           ");
        sql.append("          substr(GH.ORIGINAL_DOCUMENT, 16, 4)    AS REAL_ORIGINAL_FISCAL_YEAR,           ");
        sql.append("          GL.PAYMENT_CENTER                      AS LINE_PAYMENT_CENTER,           ");
        sql.append("          GL.WTX_AMOUNT                          AS LINE_WTX_AMOUNT,           ");
        sql.append("          GL.WTX_BASE                            AS LINE_WTX_BASE,           ");
        sql.append("          GL.WTX_AMOUNT_P                        AS LINE_WTX_AMOUNT_P,           ");
        sql.append("          GL.WTX_BASE_P                          AS LINE_WTX_BASE_P,           ");
        sql.append("          GH.DOCUMENT_TYPE,           ");
        sql.append("          GH.COMPANY_CODE,           ");
        sql.append("          GH.DATE_DOC,           ");
        sql.append("          GH.DATE_ACCT,           ");
        sql.append("          GH.PERIOD,           ");
        sql.append("          GH.CURRENCY,           ");
        sql.append("          GH.INVOICE_DOCUMENT_NO,           ");
        sql.append("          GH.REVERSE_INVOICE_DOCUMENT_NO,           ");
        sql.append("          GH.ORIGINAL_DOCUMENT_NO,           ");
        sql.append("          GH.ORIGINAL_FISCAL_YEAR,           ");
        sql.append("          GH.REVERSE_ORIGINAL_DOCUMENT_NO,           ");
        sql.append("          GH.REVERSE_ORIGINAL_FISCAL_YEAR,           ");
        sql.append("          GH.COST_CENTER1,           ");
        sql.append("          GH.COST_CENTER2,           ");
        sql.append("          GH.HEADER_REFERENCE,           ");
        sql.append("          GH.DOCUMENT_HEADER_TEXT,           ");
        sql.append("          GH.HEADER_REFERENCE2,           ");
        sql.append("          GH.REVERSE_DATE_ACCT,           ");
        sql.append("          GH.REVERSE_REASON,           ");
        sql.append("          GH.ORIGINAL_DOCUMENT,           ");
        sql.append("          GH.DOCUMENT_CREATED,           ");
        sql.append("          GH.USER_PARK,           ");
        sql.append("          GH.USER_POST,           ");
        sql.append("          GL.POSTING_KEY,           ");
        sql.append("          GL.ACCOUNT_TYPE,           ");
        sql.append("          GL.DR_CR,           ");
        sql.append("          GL.GL_ACCOUNT,           ");
        sql.append("          GL.FI_AREA,           ");
        sql.append("          GL.COST_CENTER,           ");
        sql.append("          GL.FUND_SOURCE,           ");
        sql.append("          GL.BG_CODE,           ");
        sql.append("          GL.BG_ACTIVITY,           ");
        sql.append("          GL.COST_ACTIVITY,           ");
        sql.append("          GL.AMOUNT,           ");
        sql.append("          GL.REFERENCE3,           ");
        sql.append("          GL.ASSIGNMENT,           ");
        sql.append("          GL.BR_DOCUMENT_NO,           ");
        sql.append("          GL.BR_LINE,           ");
        sql.append("          GL.PAYMENT_CENTER,           ");
        sql.append("          GL.BANK_BOOK,           ");
        sql.append("          GL.SUB_ACCOUNT,           ");
        sql.append("          GL.SUB_ACCOUNT_OWNER,           ");
        sql.append("          GL.DEPOSIT_ACCOUNT,           ");
        sql.append("          GL.DEPOSIT_ACCOUNT_OWNER,           ");
        sql.append("          GL.GPSC,           ");
        sql.append("          GL.GPSC_GROUP,           ");
        sql.append("          GL.LINE_ITEM_TEXT,           ");
        sql.append("          GL.LINE_DESC,           ");
        sql.append("          GL.PAYMENT_TERM,           ");
        sql.append("          GL.PAYMENT_METHOD,           ");
        sql.append("          GL.WTX_TYPE,           ");
        sql.append("          GL.WTX_CODE,           ");
        sql.append("          GL.WTX_BASE,           ");
        sql.append("          nvl(GL.WTX_AMOUNT, 0)                  AS WTX_AMOUNT,           ");
        sql.append("          GL.WTX_TYPE_P,           ");
        sql.append("          GL.WTX_CODE_P,           ");
        sql.append("          GL.WTX_BASE_P,           ");
        sql.append("          GL.WTX_AMOUNT_P,           ");
        sql.append("          GL.VENDOR,           ");
        sql.append("          GL.VENDOR_TAX_ID,           ");
        sql.append("          GL.PAYEE,           ");
        sql.append("          GL.PAYEE_TAX_ID,           ");
        sql.append("          GL.BANK_ACCOUNT_NO,           ");
        sql.append("          GL.BANK_BRANCH_NO,           ");
        sql.append("          GL.TRADING_PARTNER,           ");
        sql.append("          GL.TRADING_PARTNER_PARK,           ");
        sql.append("          GL.SPECIAL_GL,           ");
        sql.append("          GL.DATE_BASE_LINE,           ");
        sql.append("          GL.DATE_VALUE,           ");
        sql.append("          GL.ASSET_NO,           ");
        sql.append("          GL.ASSET_SUB_NO,           ");
        sql.append("          GL.QTY,           ");
        sql.append("          GL.UOM,           ");
        sql.append("          GL.REFERENCE1,           ");
        sql.append("          GL.REFERENCE2,           ");
        sql.append("          GL.PO_DOCUMENT_NO,           ");
        sql.append("          GL.PO_LINE,           ");
        sql.append("          GL.INCOME,           ");
        sql.append("          GL.PAYMENT_BLOCK,           ");
        sql.append("          GL.PAYMENT_REFERENCE,           ");
        sql.append("          GL.LINE,           ");
        sql.append("          CD.NAME                                AS COMP_CODE_NAME,           ");
        sql.append("          PC.NAME                                AS PAYMENT_CENTER_NAME,           ");
        sql.append("          CC.NAME                                AS COST_CENTER_NAME,           ");
        sql.append("          PM.NAME                                AS PAYMENT_METHOD_NAME,           ");
        sql.append("          SF.NAME                                AS FUND_SOURCE_NAME,           ");
        sql.append("          BC.NAME                                AS BG_CODE_NAME,           ");
        sql.append("          MA.NAME                                AS BG_ACTIVITY_NAME,           ");
        sql.append("          VD_MAIN.NAME                           AS MAIN_NAME1,           ");
        sql.append("          VD_MAIN.NAME2                          AS MAIN_NAME2,           ");
        sql.append("          VD_MAIN.TAXID                          AS MAIN_TAX_ID,           ");
        sql.append("          VD_MAIN.ISACTIVE                       AS MAIN_VENDOR_ACTIVE,           ");
        sql.append("          TRIM(VL_MAIN.ADDRESS1 || ' ' || VL_MAIN.ADDRESS2 || ' ' || VL_MAIN.ADDRESS3 ||           ");
        sql.append("          '' ||           ");
        sql.append("          VL_MAIN.ADDRESS4 || ' ' ||           ");
        sql.append("          VL_MAIN.ADDRESS5)                 AS MAIN_ADDRESS,           ");
        sql.append("          VL_MAIN.CITY                           AS MAIN_CITY,           ");
        sql.append("          VL_MAIN.POSTAL                         AS MAIN_POSTAL,           ");
        sql.append("          VL_MAIN.REGIONNAME                     AS MAIN_REGION_NAME,           ");
        sql.append("          VC_MAIN.NAME                           AS MAIN_COUNTRY,           ");
        sql.append("          VC_MAIN.COUNTRYCODE                    AS MAIN_COUNTRY_CODE,           ");
        sql.append("          VB_MAIN.BANKACCOUNTTYPE                AS MAIN_PAYEE_BANK_ACCOUNT_TYPE,           ");
        sql.append("          VB_MAIN.ROUTINGNO                      AS MAIN_PAYEE_BANK_NO,           ");
        sql.append("          VB_MAIN.ACCOUNTNO                      AS MAIN_PAYEE_BANK_ACCOUNT_NO,           ");
        sql.append("          VB_MAIN.A_NAME                         AS MAIN_ACCOUNT_HOLDER_NAME,           ");
        sql.append("          SUBSTR(BK_MAIN.ROUTINGNO, 1, 3)        AS MAIN_PAYEE_BANK,           ");
        sql.append("          CB_MAIN.NAME                           AS MAIN_PAYEE_BANK_NAME,           ");
        sql.append("          BK_MAIN.ROUTINGNO                      AS MAIN_PAYEE_BANK_KEY,           ");
        sql.append("          BK_MAIN.SWIFTCODE                      AS MAIN_SWIFT_CODE,           ");
        sql.append("          BK_MAIN.DESCRIPTION                    AS MAIN_PAYEE_BANK_REFERENCE,           ");
        sql.append("          BA_MAIN.NAME                           AS MAIN_AREA_NAME,           ");

        sql.append("          PS_MAIN.CONFIRMSTATUS                  AS MAIN_VENDOR_STATUS,           ");
        sql.append("          AC_MAIN.APPROVALSTATUS           AS MAIN_APPROVAL_STATUS,           ");

        sql.append("          VD_ALTERNATIVE.NAME                    AS ALTERNATIVE_NAME1,           ");
        sql.append("          VD_ALTERNATIVE.NAME2                   AS ALTERNATIVE_NAME2,           ");
        sql.append("          VD_ALTERNATIVE.TAXID                   AS ALTERNATIVE_TAX_ID,           ");
        sql.append("          VD_ALTERNATIVE.ISACTIVE                AS ALTERNATIVE_VENDOR_ACTIVE,           ");
        sql.append("          TRIM(VL_ALTERNATIVE.ADDRESS1 || ' ' || VL_ALTERNATIVE.ADDRESS2 || ' ' ||           ");
        sql.append("          VL_ALTERNATIVE.ADDRESS3 || ' ' ||           ");
        sql.append("          VL_ALTERNATIVE.ADDRESS4 || ' ' ||           ");
        sql.append("          VL_ALTERNATIVE.ADDRESS5)          AS ALTERNATIVE_ADDRESS,           ");
        sql.append("          VL_ALTERNATIVE.CITY                    AS ALTERNATIVE_CITY,           ");
        sql.append("          VL_ALTERNATIVE.POSTAL                  AS ALTERNATIVE_POSTAL,           ");
        sql.append("          VL_ALTERNATIVE.REGIONNAME              AS ALTERNATIVE_REGION_NAME,           ");
        sql.append("          VC_ALTERNATIVE.NAME                    AS ALTERNATIVE_COUNTRY,           ");
        sql.append("          VC_ALTERNATIVE.COUNTRYCODE             AS ALTERNATIVE_COUNTRY_CODE,           ");
        sql.append("          VB_ALTERNATIVE.BANKACCOUNTTYPE         AS ALTERNATIVE_PAYEE_BANK_ACCOUNT_TYPE,           ");
        sql.append("          VB_ALTERNATIVE.ROUTINGNO               AS ALTERNATIVE_PAYEE_BANK_NO,           ");
        sql.append("          VB_ALTERNATIVE.ACCOUNTNO               AS ALTERNATIVE_PAYEE_BANK_ACCOUNT_NO,           ");
        sql.append("          VB_ALTERNATIVE.A_NAME                  AS ALTERNATIVE_ACCOUNT_HOLDER_NAME,           ");
        sql.append("          SUBSTR(BK_ALTERNATIVE.ROUTINGNO, 1, 3) AS ALTERNATIVE_PAYEE_BANK,           ");
        sql.append("          CB_ALTERNATIVE.NAME                    AS ALTERNATIVE_PAYEE_BANK_NAME,           ");
        sql.append("          BK_ALTERNATIVE.ROUTINGNO               AS ALTERNATIVE_PAYEE_BANK_KEY,           ");
        sql.append("          BK_ALTERNATIVE.SWIFTCODE               AS ALTERNATIVE_SWIFT_CODE,           ");
        sql.append("          BK_ALTERNATIVE.DESCRIPTION             AS ALTERNATIVE_PAYEE_BANK_REFERENCE,           ");
        sql.append("          BA_ALTERNATIVE.NAME                    AS ALTERNATIVE_AREA_NAME,           ");
        sql.append("          PS_ALTERNATIVE.CONFIRMSTATUS           AS ALTERNATIVE_VENDOR_STATUS,          ");
        sql.append("          AC_ALTERNATIVE.APPROVALSTATUS           AS ALTERNATIVE_APPROVAL_STATUS           ");
        sql.append("FROM ");
        sql.append("    GL_HEAD               gh ");
        sql.append("    INNER JOIN gl_line               gl ON gh.ORIGINAL_DOCUMENT_NO = gl.ORIGINAL_DOCUMENT_NO AND GH.DOCUMENT_BASE_TYPE = 'API' AND gl.UPDATED < ? ");
        sql.append("    AND (gl.PAYMENT_BLOCK = '' OR gl.PAYMENT_BLOCK = ' ' OR gl.PAYMENT_BLOCK IS NULL) AND gl.ACCOUNT_TYPE = 'K' AND GH.COMPANY_CODE = GL.COMPANY_CODE AND GH.ORIGINAL_FISCAL_YEAR = GL.ORIGINAL_FISCAL_YEAR ");
        sql.append("    INNER JOIN ").append(schema).append(".TH_CAPAYMENTMETHOD").append(" pm ON gl.payment_method = pm.valuecode ");
        log.info("companyConditions {}", companyConditions);
        if (null != companyConditions && !companyConditions.isEmpty()) {
            if (companyConditions.get(0).getCompanyFrom().equalsIgnoreCase("99999")) {
                sql.append("    AND pm.isdirect = 'N' ");
            } else {
                sql.append("    AND pm.isdirect = 'Y' ");
            }
        }
        sql.append(generateSQLWhereAuthorizedCommon(username));
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".AD_ORG").append(" ad on ad.value = gh.COMPANY_CODE ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_CACOMPCODE").append(" cd ON gh.COMPANY_CODE = cd.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGPAYMENTCENTER").append(" pc ON gl.payment_center = pc.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGCOSTCENTER").append(" cc ON gl.cost_center = cc.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGFUNDSOURCE").append(" sf ON gl.fund_source = sf.valuecode ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".C_YEAR").append(" cy ON cy.fiscalyear = gh.ORIGINAL_FISCAL_YEAR ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETCODE").append(" bc ON gl.bg_code = bc.valuecode AND ad.ad_org_id = bc.ad_org_id and bc.c_year_id = cy.c_year_id ");
        sql.append("    LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETACTIVITY").append(" ma ON gl.bg_activity = ma.valuecode AND (ad.ad_org_id = ma.ad_org_id OR ad.ad_org_id = '0') AND ma.c_year_id = cy.c_year_id ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".C_BPARTNER VD_MAIN           ");
        sql.append("          ON VD_MAIN.VALUE = GL.VENDOR           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".C_BPARTNER_LOCATION VDL_MAIN           ");
        sql.append("          ON VD_MAIN.C_BPARTNER_ID = VDL_MAIN.C_BPARTNER_ID           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".C_LOCATION VL_MAIN ON VDL_MAIN.C_LOCATION_ID = VL_MAIN.C_LOCATION_ID           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".C_COUNTRY VC_MAIN ON VL_MAIN.C_COUNTRY_ID = VC_MAIN.C_COUNTRY_ID           ");
        sql.append("          OUTER APPLY (           ");
        sql.append("          SELECT *           ");
        sql.append("          FROM ").append(schema).append(".C_BP_BANKACCOUNT BB           ");
        sql.append("          WHERE BB.ISACTIVE = 'Y'           ");
        sql.append("          AND VD_MAIN.C_BPARTNER_ID = BB.C_BPARTNER_ID           ");
        sql.append("          AND GL.BANK_ACCOUNT_NO = BB.ACCOUNTNO           ");
        sql.append("          ORDER BY BB.ACCOUNTNO           ");
        sql.append("          FETCH FIRST ROW ONLY           ");
        sql.append("          ) VB_MAIN           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".C_BANK BK_MAIN ON VB_MAIN.C_BANK_ID = BK_MAIN.C_BANK_ID           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".TH_CABANK CB_MAIN ON CB_MAIN.VALUECODE = substr(BK_MAIN.ROUTINGNO, 1, 3)           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETAREA BA_MAIN ON BA_MAIN.FIAREA = GL.FI_AREA           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".TH_APBPARTNERSTATUS PS_MAIN ON VD_MAIN.C_BPARTNER_ID = PS_MAIN.C_BPARTNER_ID           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".TH_APBPACCESSCONTROL AC_MAIN ON VD_MAIN.C_BPARTNER_ID = AC_MAIN.C_BPARTNER_ID           ");
        sql.append("           AND AD.AD_ORG_ID = AC_MAIN.AD_ORG_ID          ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".C_BPARTNER VD_ALTERNATIVE           ");
        sql.append("          ON VD_ALTERNATIVE.VALUE = GL.PAYEE           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".C_BPARTNER_LOCATION VDL_ALTERNATIVE           ");
        sql.append("          ON VD_ALTERNATIVE.C_BPARTNER_ID = VDL_ALTERNATIVE.C_BPARTNER_ID           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".C_LOCATION VL_ALTERNATIVE           ");
        sql.append("          ON VDL_ALTERNATIVE.C_LOCATION_ID = VL_ALTERNATIVE.C_LOCATION_ID           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".C_COUNTRY VC_ALTERNATIVE           ");
        sql.append("          ON VL_ALTERNATIVE.C_COUNTRY_ID = VC_ALTERNATIVE.C_COUNTRY_ID           ");
        sql.append("          OUTER APPLY (           ");
        sql.append("          SELECT *           ");
        sql.append("          FROM ").append(schema).append(".C_BP_BANKACCOUNT BB           ");
        sql.append("          WHERE BB.ISACTIVE = 'Y'           ");
        sql.append("          AND VD_ALTERNATIVE.C_BPARTNER_ID = BB.C_BPARTNER_ID           ");
        sql.append("          AND GL.BANK_ACCOUNT_NO = BB.ACCOUNTNO           ");
        sql.append("          ORDER BY BB.ACCOUNTNO           ");
        sql.append("          FETCH FIRST ROW ONLY           ");
        sql.append("          ) VB_ALTERNATIVE           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".C_BANK BK_ALTERNATIVE ON VB_ALTERNATIVE.C_BANK_ID = BK_ALTERNATIVE.C_BANK_ID           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".TH_CABANK CB_ALTERNATIVE           ");
        sql.append("          ON CB_ALTERNATIVE.VALUECODE = substr(BK_ALTERNATIVE.ROUTINGNO, 1, 3)           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".TH_BGBUDGETAREA BA_ALTERNATIVE ON BA_ALTERNATIVE.FIAREA = GL.FI_AREA           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".TH_APBPARTNERSTATUS PS_ALTERNATIVE           ");
        sql.append("          ON VD_ALTERNATIVE.C_BPARTNER_ID = PS_ALTERNATIVE.C_BPARTNER_ID           ");
        sql.append("          LEFT OUTER JOIN ").append(schema).append(".TH_APBPACCESSCONTROL AC_ALTERNATIVE ON VD_ALTERNATIVE.C_BPARTNER_ID = AC_ALTERNATIVE.C_BPARTNER_ID           ");
        sql.append("           AND AD.AD_ORG_ID = AC_ALTERNATIVE.AD_ORG_ID          ");
        sql.append("WHERE ");
        sql.append("    1 = 1 ");
//        sql.append(" AND (GH.COMPANY_CODE, GH.ORIGINAL_DOCUMENT_NO, GH.ORIGINAL_FISCAL_YEAR) NOT IN (SELECT INVOICE_COMPANY_CODE, INVOICE_DOCUMENT_NO, INVOICE_FISCAL_YEAR FROM PAYMENT_PROCESS WHERE IS_PROPOSAL = 0 AND IDEM_STATUS = 'S') ");
        sql.append("          AND GH.PAYMENT_DOCUMENT_NO IS NULL           ");
        sql.append("          AND GL.CLEARING_DOCUMENT_NO IS NULL           ");
        sql.append("          AND NVL(GH.PAYMENT_ID,0) = 0           ");
        sql.append("          AND GH.REVERSE_ORIGINAL_DOCUMENT_NO IS NULL           ");

        params.add(runDate);

        if (!Util.isEmpty(parameter.getSaveDate())) {
            sql.append(SqlUtil.whereClause(parameter.getSaveDate(), "GH.DOCUMENT_CREATED", params));
        }

        if (!Util.isEmpty(parameter.getPaymentDate())) {
            sql.append(SqlUtil.whereClauseLess(parameter.getPaymentDate(), "GL.DATE_BASE_LINE", params));
        }

        DynamicCondition.companyConditionTuning(companyConditions, sql, params);
        DynamicCondition.newVendorConditionTuning(vendors, sql, params, "VD_MAIN.VALUE");
        DynamicCondition.newIndependentConditionTuning(independents, sql, params);

        List<PaymentMethod> paymentMethodList = new ArrayList<>();

        for (char ch : parameter.getPaymentMethod().toCharArray()) {
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setPaymentMethodFrom(String.valueOf(ch));
            paymentMethod.setOptionExclude(false);
            paymentMethodList.add(paymentMethod);
        }

        DynamicCondition.paymentMethodForRunTuning(paymentMethodList, sql, params);


        sql.append("            )TMP           ");
        sql.append("           LEFT  JOIN  GL_HEAD H           ");
        sql.append("            ON TMP.REAL_ORIGINAL_DOCUMENT_NO = H.ORIGINAL_DOCUMENT_NO           ");
        sql.append("           AND TMP.REAL_ORIGINAL_COMPANY_CODE = H.COMPANY_CODE           ");
        sql.append("           AND TMP.REAL_ORIGINAL_FISCAL_YEAR = H.ORIGINAL_FISCAL_YEAR           ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        log.info("sql  {}", sql.toString());
        this.jdbcTemplate.setFetchSize(5000);
        return this.jdbcTemplate.query(sql.toString(), objParams, prepareSelectProposalRowMapper);
    }

    @Override
    public void newLockProposalDocument(List<PrepareSelectProposalDocument> prepareSelectProposalDocumentList, Long paymentAliasId) {
        final int batchSize = 30000;
        List<List<PrepareSelectProposalDocument>> listBatch = Lists.partition(prepareSelectProposalDocumentList, batchSize);
        final String sqlSave = "UPDATE /*+ ENABLE_PARALLEL_DML */ GL_HEAD SET PAYMENT_ID = ?  WHERE ID = ?";
        for(List<PrepareSelectProposalDocument> batch : listBatch) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    int index = 0;
                    PrepareSelectProposalDocument prepareSelectProposalDocument = batch.get(i);
                    ps.setLong(++index, paymentAliasId);
                    ps.setLong(++index, prepareSelectProposalDocument.getId());

                }
                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }
}

