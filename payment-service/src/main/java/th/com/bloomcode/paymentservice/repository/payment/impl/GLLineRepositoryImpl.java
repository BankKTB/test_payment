package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.GLLine;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.GLLineRepository;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class GLLineRepositoryImpl extends MetadataJdbcRepository<GLLine, Long> implements GLLineRepository {

    private final JdbcTemplate jdbcTemplate;
    static BeanPropertyRowMapper<GLLine> beanPropertyRowMapper = new BeanPropertyRowMapper<>(GLLine.class);

    static Updater<GLLine> glLineUpdater = (t, mapping) -> {
        mapping.put(GLLine.COLUMN_NAME_GL_LINE_ID, t.getId());
        mapping.put(GLLine.COLUMN_NAME_POSTING_KEY, t.getPostingKey());
        mapping.put(GLLine.COLUMN_NAME_ACCOUNT_TYPE, t.getAccountType());
        mapping.put(GLLine.COLUMN_NAME_DR_CR, t.getDrCr());
        mapping.put(GLLine.COLUMN_NAME_GL_ACCOUNT, t.getGlAccount());
        mapping.put(GLLine.COLUMN_NAME_FI_AREA, t.getFiArea());
        mapping.put(GLLine.COLUMN_NAME_FI_AREA_NAME, t.getFiAreaName());
        mapping.put(GLLine.COLUMN_NAME_COST_CENTER, t.getCostCenter());
        mapping.put(GLLine.COLUMN_NAME_FUND_SOURCE, t.getFundSource());
        mapping.put(GLLine.COLUMN_NAME_BG_CODE, t.getBgCode());
        mapping.put(GLLine.COLUMN_NAME_BG_ACTIVITY, t.getBgActivity());
        mapping.put(GLLine.COLUMN_NAME_COST_ACTIVITY, t.getCostActivity());
        mapping.put(GLLine.COLUMN_NAME_AMOUNT, t.getAmount());
        mapping.put(GLLine.COLUMN_NAME_REFERENCE3, t.getReference3());
        mapping.put(GLLine.COLUMN_NAME_ASSIGNMENT, t.getAssignment());
        mapping.put(GLLine.COLUMN_NAME_ASSIGNMENT_SPECIAL_GL, t.getAssignmentSpecialGL());
        mapping.put(GLLine.COLUMN_NAME_BR_DOCUMENT_NO, t.getBrDocumentNo());
        mapping.put(GLLine.COLUMN_NAME_BR_LINE, t.getBrLine());
        mapping.put(GLLine.COLUMN_NAME_PAYMENT_CENTER, t.getPaymentCenter());
        mapping.put(GLLine.COLUMN_NAME_PAYMENT_CENTER_NAME, t.getPaymentCenterName());
        mapping.put(GLLine.COLUMN_NAME_BANK_BOOK, t.getBankBook());
        mapping.put(GLLine.COLUMN_NAME_SUB_BOOK, t.getSubBook());
        mapping.put(GLLine.COLUMN_NAME_SUB_ACCOUNT_TYPE, t.getSubAccountType());
        mapping.put(GLLine.COLUMN_NAME_SUB_ACCOUNT, t.getSubAccount());
        mapping.put(GLLine.COLUMN_NAME_SUB_ACCOUNT_OWNER, t.getSubAccountOwner());
        mapping.put(GLLine.COLUMN_NAME_DEPOSIT_ACCOUNT, t.getDepositAccount());
        mapping.put(GLLine.COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER, t.getDepositAccountOwner());
        mapping.put(GLLine.COLUMN_NAME_GPSC, t.getGpsc());
        mapping.put(GLLine.COLUMN_NAME_GPSC_GROUP, t.getGpscGroup());
        mapping.put(GLLine.COLUMN_NAME_LINE_ITEM_TEXT, t.getLineItemText());
        mapping.put(GLLine.COLUMN_NAME_LINE_DESC, t.getLineDesc());
        mapping.put(GLLine.COLUMN_NAME_PAYMENT_TERM, t.getPaymentTerm());
        mapping.put(GLLine.COLUMN_NAME_PAYMENT_METHOD, t.getPaymentMethod());
        mapping.put(GLLine.COLUMN_NAME_WTX_TYPE, t.getWtxType());
        mapping.put(GLLine.COLUMN_NAME_WTX_CODE, t.getWtxCode());
        mapping.put(GLLine.COLUMN_NAME_WTX_BASE, t.getWtxBase());
        mapping.put(GLLine.COLUMN_NAME_WTX_AMOUNT, t.getWtxAmount());
        mapping.put(GLLine.COLUMN_NAME_WTX_TYPE_P, t.getWtxTypeP());
        mapping.put(GLLine.COLUMN_NAME_WTX_CODE_P, t.getWtxCodeP());
        mapping.put(GLLine.COLUMN_NAME_WTX_BASE_P, t.getWtxBaseP());
        mapping.put(GLLine.COLUMN_NAME_WTX_AMOUNT_P, t.getWtxAmountP());
        mapping.put(GLLine.COLUMN_NAME_VENDOR, t.getVendor());
        mapping.put(GLLine.COLUMN_NAME_VENDOR_NAME, t.getVendorName());
        mapping.put(GLLine.COLUMN_NAME_CONFIRM_VENDOR, t.isConfirmVendor());
        mapping.put(GLLine.COLUMN_NAME_VENDOR_TAX_ID, t.getVendorTaxId());
        mapping.put(GLLine.COLUMN_NAME_PAYEE, t.getPayee());
        mapping.put(GLLine.COLUMN_NAME_PAYEE_NAME, t.getPayeeName());
        mapping.put(GLLine.COLUMN_NAME_PAYEE_TAX_ID, t.getPayeeTaxId());
        mapping.put(GLLine.COLUMN_NAME_BANK_KEY, t.getBankKey());
        mapping.put(GLLine.COLUMN_NAME_BANK_NAME, t.getBankName());
        mapping.put(GLLine.COLUMN_NAME_BANK_ACCOUNT_NO, t.getBankAccountNo());
        mapping.put(GLLine.COLUMN_NAME_BANK_ACCOUNT_HOLDER_NAME, t.getBankAccountHolderName());
        mapping.put(GLLine.COLUMN_NAME_BANK_BRANCH_NO, t.getBankBranchNo());
        mapping.put(GLLine.COLUMN_NAME_TRADING_PARTNER, t.getTradingPartner());
        mapping.put(GLLine.COLUMN_NAME_TRADING_PARTNER_PARK, t.getTradingPartnerPark());
        mapping.put(GLLine.COLUMN_NAME_SPECIAL_GL, t.getSpecialGL());
        mapping.put(GLLine.COLUMN_NAME_DATE_BASE_LINE, t.getDateBaseLine());
        mapping.put(GLLine.COLUMN_NAME_DUE_DATE, t.getDueDate());
        mapping.put(GLLine.COLUMN_NAME_DATE_VALUE, t.getDateValue());
        mapping.put(GLLine.COLUMN_NAME_ASSET_NO, t.getAssetNo());
        mapping.put(GLLine.COLUMN_NAME_ASSET_SUB_NO, t.getAssetSubNo());
        mapping.put(GLLine.COLUMN_NAME_QTY, t.getQty());
        mapping.put(GLLine.COLUMN_NAME_UOM, t.getUom());
        mapping.put(GLLine.COLUMN_NAME_REFERENCE1, t.getReference1());
        mapping.put(GLLine.COLUMN_NAME_REFERENCE2, t.getReference2());
        mapping.put(GLLine.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(GLLine.COLUMN_NAME_PO_DOCUMENT_NO, t.getPoDocumentNo());
        mapping.put(GLLine.COLUMN_NAME_PO_LINE, t.getPoLine());
        mapping.put(GLLine.COLUMN_NAME_MR_FISCAL_YEAR, t.getMrFiscalYear());
        mapping.put(GLLine.COLUMN_NAME_MR_DOCUMENT_NO, t.getMrDocumentNo());
        mapping.put(GLLine.COLUMN_NAME_MR_LINE, t.getMrLine());
        mapping.put(GLLine.COLUMN_NAME_INVOICE_FISCAL_YEAR, t.getInvoiceFiscalYear());
        mapping.put(GLLine.COLUMN_NAME_INVOICE_DOCUMENT_NO, t.getInvoiceDocumentNo());
        mapping.put(GLLine.COLUMN_NAME_INVOICE_LINE, t.getInvoiceLine());
        mapping.put(GLLine.COLUMN_NAME_REFERENCE_INVOICE_FISCAL_YEAR, t.getReferenceInvoiceFiscalYear());
        mapping.put(GLLine.COLUMN_NAME_REFERENCE_INVOICE_DOCUMENT_NO, t.getReferenceInvoiceDocumentNo());
        mapping.put(GLLine.COLUMN_NAME_REFERENCE_INVOICE_LINE, t.getReferenceInvoiceLine());
        mapping.put(GLLine.COLUMN_NAME_CLEARING_FISCAL_YEAR, t.getClearingFiscalYear());
        mapping.put(GLLine.COLUMN_NAME_CLEARING_DOCUMENT_NO, t.getClearingDocumentNo());
        mapping.put(GLLine.COLUMN_NAME_CLEARING_DOCUMENT_TYPE, t.getClearingDocumentType());
        mapping.put(GLLine.COLUMN_NAME_CLEARING_DATE_DOC, t.getClearingDateDoc());
        mapping.put(GLLine.COLUMN_NAME_CLEARING_DATE_ACCT, t.getClearingDateAcct());
        mapping.put(GLLine.COLUMN_NAME_INCOME, t.getIncome());
        mapping.put(GLLine.COLUMN_NAME_PAYMENT_BLOCK, t.getPaymentBlock());
        mapping.put(GLLine.COLUMN_NAME_PAYMENT_REFERENCE, t.getPaymentReference());
        mapping.put(GLLine.COLUMN_NAME_AUTO_GEN, t.isAutoGen());
        mapping.put(GLLine.COLUMN_NAME_WTX, t.isWtx());
        mapping.put(GLLine.COLUMN_NAME_LINE, t.getLine());
        mapping.put(GLLine.COLUMN_NAME_BG_ACCOUNT, t.getBgAccount());
        mapping.put(GLLine.COLUMN_NAME_FUND_CENTER, t.getFundCenter());
        mapping.put(GLLine.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, t.getOriginalDocumentNo());
        mapping.put(GLLine.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
        mapping.put(GLLine.COLUMN_NAME_FUND_TYPE, t.getFundType());
        mapping.put(GLLine.COLUMN_NAME_GL_HEAD_ID, t.getGlHeadId());
        mapping.put(GLLine.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(GLLine.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(GLLine.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(GLLine.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(GLLine.COLUMN_NAME_GL_LINE_ID, Types.BIGINT),
            entry(GLLine.COLUMN_NAME_POSTING_KEY, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_ACCOUNT_TYPE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_DR_CR, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_GL_ACCOUNT, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_FI_AREA, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_FI_AREA_NAME, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_COST_CENTER, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_FUND_SOURCE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_BG_CODE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_BG_ACTIVITY, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_COST_ACTIVITY, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_AMOUNT, Types.NUMERIC),
            entry(GLLine.COLUMN_NAME_REFERENCE3, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_ASSIGNMENT, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_ASSIGNMENT_SPECIAL_GL, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_BR_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_BR_LINE, Types.INTEGER),
            entry(GLLine.COLUMN_NAME_PAYMENT_CENTER, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_PAYMENT_CENTER_NAME, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_BANK_BOOK, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_SUB_BOOK, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_SUB_ACCOUNT_TYPE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_SUB_ACCOUNT, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_SUB_ACCOUNT_OWNER, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_DEPOSIT_ACCOUNT, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_GPSC, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_GPSC_GROUP, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_LINE_ITEM_TEXT, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_LINE_DESC, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_PAYMENT_TERM, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_PAYMENT_METHOD, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_WTX_TYPE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_WTX_CODE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_WTX_BASE, Types.NUMERIC),
            entry(GLLine.COLUMN_NAME_WTX_AMOUNT, Types.NUMERIC),
            entry(GLLine.COLUMN_NAME_WTX_TYPE_P, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_WTX_CODE_P, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_WTX_BASE_P, Types.NUMERIC),
            entry(GLLine.COLUMN_NAME_WTX_AMOUNT_P, Types.NUMERIC),
            entry(GLLine.COLUMN_NAME_VENDOR, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_VENDOR_NAME, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_CONFIRM_VENDOR, Types.BOOLEAN),
            entry(GLLine.COLUMN_NAME_VENDOR_TAX_ID, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_PAYEE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_PAYEE_NAME, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_PAYEE_TAX_ID, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_BANK_KEY, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_BANK_NAME, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_BANK_ACCOUNT_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_BANK_ACCOUNT_HOLDER_NAME, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_BANK_BRANCH_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_TRADING_PARTNER, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_TRADING_PARTNER_PARK, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_SPECIAL_GL, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_DATE_BASE_LINE, Types.TIMESTAMP),
            entry(GLLine.COLUMN_NAME_DUE_DATE, Types.TIMESTAMP),
            entry(GLLine.COLUMN_NAME_DATE_VALUE, Types.TIMESTAMP),
            entry(GLLine.COLUMN_NAME_ASSET_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_ASSET_SUB_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_QTY, Types.NUMERIC),
            entry(GLLine.COLUMN_NAME_UOM, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_REFERENCE1, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_REFERENCE2, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_PO_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_PO_LINE, Types.INTEGER),
            entry(GLLine.COLUMN_NAME_MR_FISCAL_YEAR, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_MR_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_MR_LINE, Types.INTEGER),
            entry(GLLine.COLUMN_NAME_INVOICE_FISCAL_YEAR, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_INVOICE_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_INVOICE_LINE, Types.INTEGER),
            entry(GLLine.COLUMN_NAME_REFERENCE_INVOICE_FISCAL_YEAR, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_REFERENCE_INVOICE_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_REFERENCE_INVOICE_LINE, Types.INTEGER),
            entry(GLLine.COLUMN_NAME_CLEARING_FISCAL_YEAR, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_CLEARING_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_CLEARING_DOCUMENT_TYPE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_CLEARING_DATE_DOC, Types.TIMESTAMP),
            entry(GLLine.COLUMN_NAME_CLEARING_DATE_ACCT, Types.TIMESTAMP),
            entry(GLLine.COLUMN_NAME_INCOME, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_PAYMENT_BLOCK, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_PAYMENT_REFERENCE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_AUTO_GEN, Types.BOOLEAN),
            entry(GLLine.COLUMN_NAME_WTX, Types.BOOLEAN),
            entry(GLLine.COLUMN_NAME_LINE, Types.INTEGER),
            entry(GLLine.COLUMN_NAME_BG_ACCOUNT, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_FUND_CENTER, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_FUND_TYPE, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_GL_HEAD_ID, Types.BIGINT),
            entry(GLLine.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(GLLine.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(GLLine.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
            entry(GLLine.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR)
    );

    static RowMapper<GLLine> userRowMapper = (rs, rowNum) -> new GLLine(
            rs.getLong(GLLine.COLUMN_NAME_GL_LINE_ID),
            rs.getString(GLLine.COLUMN_NAME_POSTING_KEY),
            rs.getString(GLLine.COLUMN_NAME_ACCOUNT_TYPE),
            rs.getString(GLLine.COLUMN_NAME_DR_CR),
            rs.getString(GLLine.COLUMN_NAME_GL_ACCOUNT),
            rs.getString(GLLine.COLUMN_NAME_FI_AREA),
            rs.getString(GLLine.COLUMN_NAME_FI_AREA_NAME),
            rs.getString(GLLine.COLUMN_NAME_COST_CENTER),
            rs.getString(GLLine.COLUMN_NAME_FUND_SOURCE),
            rs.getString(GLLine.COLUMN_NAME_BG_CODE),
            rs.getString(GLLine.COLUMN_NAME_BG_ACTIVITY),
            rs.getString(GLLine.COLUMN_NAME_COST_ACTIVITY),
            rs.getBigDecimal(GLLine.COLUMN_NAME_AMOUNT),
            rs.getString(GLLine.COLUMN_NAME_REFERENCE3),
            rs.getString(GLLine.COLUMN_NAME_ASSIGNMENT),
            rs.getString(GLLine.COLUMN_NAME_ASSIGNMENT_SPECIAL_GL),
            rs.getString(GLLine.COLUMN_NAME_BR_DOCUMENT_NO),
            rs.getInt(GLLine.COLUMN_NAME_BR_LINE),
            rs.getString(GLLine.COLUMN_NAME_PAYMENT_CENTER),
            rs.getString(GLLine.COLUMN_NAME_PAYMENT_CENTER_NAME),
            rs.getString(GLLine.COLUMN_NAME_BANK_BOOK),
            rs.getString(GLLine.COLUMN_NAME_SUB_BOOK),
            rs.getString(GLLine.COLUMN_NAME_SUB_ACCOUNT_TYPE),
            rs.getString(GLLine.COLUMN_NAME_SUB_ACCOUNT),
            rs.getString(GLLine.COLUMN_NAME_SUB_ACCOUNT_OWNER),
            rs.getString(GLLine.COLUMN_NAME_DEPOSIT_ACCOUNT),
            rs.getString(GLLine.COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER),
            rs.getString(GLLine.COLUMN_NAME_GPSC),
            rs.getString(GLLine.COLUMN_NAME_GPSC_GROUP),
            rs.getString(GLLine.COLUMN_NAME_LINE_ITEM_TEXT),
            rs.getString(GLLine.COLUMN_NAME_LINE_DESC),
            rs.getString(GLLine.COLUMN_NAME_PAYMENT_TERM),
            rs.getString(GLLine.COLUMN_NAME_PAYMENT_METHOD),
            rs.getString(GLLine.COLUMN_NAME_WTX_TYPE),
            rs.getString(GLLine.COLUMN_NAME_WTX_CODE),
            rs.getBigDecimal(GLLine.COLUMN_NAME_WTX_BASE),
            rs.getBigDecimal(GLLine.COLUMN_NAME_WTX_AMOUNT),
            rs.getString(GLLine.COLUMN_NAME_WTX_TYPE_P),
            rs.getString(GLLine.COLUMN_NAME_WTX_CODE_P),
            rs.getBigDecimal(GLLine.COLUMN_NAME_WTX_BASE_P),
            rs.getBigDecimal(GLLine.COLUMN_NAME_WTX_AMOUNT_P),
            rs.getString(GLLine.COLUMN_NAME_VENDOR),
            rs.getString(GLLine.COLUMN_NAME_VENDOR_NAME),
            rs.getBoolean(GLLine.COLUMN_NAME_CONFIRM_VENDOR),
            rs.getString(GLLine.COLUMN_NAME_VENDOR_TAX_ID),
            rs.getString(GLLine.COLUMN_NAME_PAYEE),
            rs.getString(GLLine.COLUMN_NAME_PAYEE_NAME),
            rs.getString(GLLine.COLUMN_NAME_PAYEE_TAX_ID),
            rs.getString(GLLine.COLUMN_NAME_BANK_KEY),
            rs.getString(GLLine.COLUMN_NAME_BANK_NAME),
            rs.getString(GLLine.COLUMN_NAME_BANK_ACCOUNT_NO),
            rs.getString(GLLine.COLUMN_NAME_BANK_ACCOUNT_HOLDER_NAME),
            rs.getString(GLLine.COLUMN_NAME_BANK_BRANCH_NO),
            rs.getString(GLLine.COLUMN_NAME_TRADING_PARTNER),
            rs.getString(GLLine.COLUMN_NAME_TRADING_PARTNER_PARK),
            rs.getString(GLLine.COLUMN_NAME_SPECIAL_GL),
            rs.getTimestamp(GLLine.COLUMN_NAME_DATE_BASE_LINE),
            rs.getTimestamp(GLLine.COLUMN_NAME_DUE_DATE),
            rs.getTimestamp(GLLine.COLUMN_NAME_DATE_VALUE),
            rs.getString(GLLine.COLUMN_NAME_ASSET_NO),
            rs.getString(GLLine.COLUMN_NAME_ASSET_SUB_NO),
            rs.getBigDecimal(GLLine.COLUMN_NAME_QTY),
            rs.getString(GLLine.COLUMN_NAME_UOM),
            rs.getString(GLLine.COLUMN_NAME_REFERENCE1),
            rs.getString(GLLine.COLUMN_NAME_REFERENCE2),
            rs.getString(GLLine.COLUMN_NAME_COMPANY_CODE),
            rs.getString(GLLine.COLUMN_NAME_PO_DOCUMENT_NO),
            rs.getInt(GLLine.COLUMN_NAME_PO_LINE),
            rs.getString(GLLine.COLUMN_NAME_MR_FISCAL_YEAR),
            rs.getString(GLLine.COLUMN_NAME_MR_DOCUMENT_NO),
            rs.getInt(GLLine.COLUMN_NAME_MR_LINE),
            rs.getString(GLLine.COLUMN_NAME_INVOICE_FISCAL_YEAR),
            rs.getString(GLLine.COLUMN_NAME_INVOICE_DOCUMENT_NO),
            rs.getInt(GLLine.COLUMN_NAME_INVOICE_LINE),
            rs.getString(GLLine.COLUMN_NAME_REFERENCE_INVOICE_FISCAL_YEAR),
            rs.getString(GLLine.COLUMN_NAME_REFERENCE_INVOICE_DOCUMENT_NO),
            rs.getInt(GLLine.COLUMN_NAME_REFERENCE_INVOICE_LINE),
            rs.getString(GLLine.COLUMN_NAME_CLEARING_FISCAL_YEAR),
            rs.getString(GLLine.COLUMN_NAME_CLEARING_DOCUMENT_NO),
            rs.getString(GLLine.COLUMN_NAME_CLEARING_DOCUMENT_TYPE),
            rs.getTimestamp(GLLine.COLUMN_NAME_CLEARING_DATE_DOC),
            rs.getTimestamp(GLLine.COLUMN_NAME_CLEARING_DATE_ACCT),
            rs.getString(GLLine.COLUMN_NAME_INCOME),
            rs.getString(GLLine.COLUMN_NAME_PAYMENT_BLOCK),
            rs.getString(GLLine.COLUMN_NAME_PAYMENT_REFERENCE),
            rs.getBoolean(GLLine.COLUMN_NAME_AUTO_GEN),
            rs.getBoolean(GLLine.COLUMN_NAME_WTX),
            rs.getInt(GLLine.COLUMN_NAME_LINE),
            rs.getString(GLLine.COLUMN_NAME_BG_ACCOUNT),
            rs.getString(GLLine.COLUMN_NAME_FUND_CENTER),
            rs.getString(GLLine.COLUMN_NAME_ORIGINAL_DOCUMENT_NO),
            rs.getString(GLLine.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
            rs.getString(GLLine.COLUMN_NAME_FUND_TYPE),
            rs.getLong(GLLine.COLUMN_NAME_GL_HEAD_ID)
    );

    public GLLineRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, glLineUpdater, updaterType, GLLine.TABLE_NAME, GLLine.COLUMN_NAME_GL_LINE_ID, jdbcTemplate);

        this.jdbcTemplate = jdbcTemplate;
    }


//    @Override
//    public List<GLHead> findByOriginalDocStartsWith(String originalDocumentNo) {
//        List<Object> params = new ArrayList<>();
//        params.add(originalDocumentNo);
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT * FROM " + GLHead.TABLE_NAME);
//        sql.append(" WHERE " + GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ?");
//        Object[] objParams = new Object[params.size()];
//        params.toArray(objParams);
//        log.debug("sql {}", sql.toString());
//        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
//    }
//
//    @Override
//    public GLHead findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear) {
//        List<Object> params = new ArrayList<>();
//        params.add(companyCode);
//        params.add(originalDocumentNo);
//        params.add(originalFiscalYear);
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT * FROM " + GLHead.TABLE_NAME);
//        sql.append(" WHERE ");
//        sql.append(GLHead.COLUMN_NAME_COMPANY_CODE + " = ? ");
//        sql.append(" AND " + GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ? ");
//        sql.append(" AND " + GLHead.COLUMN_NAME_ORIGINAL_FISCAL_YEAR + " = ? ");
//        Object[] objParams = new Object[params.size()];
//        params.toArray(objParams);
//        log.debug("sql {}", sql.toString());
//        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, userRowMapper);
//    }


    @Override
    public List<GLLine> findByCompCodeAndAccDocNoAndFiscalYear(String companyCode, String docNo, String year) {
        return null;
    }

    @Override
    public List<GLLine> findByCompCodeAndAccDocNoAndFiscalYearAndPaymentBlockStartsWith(String companyCode, String docNo, String year, String paymentBlock) {
        return null;
    }

    @Override
    public List<GLLine> findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountTypeAndPaymentBlockStartsWith(String companyCode, String originalDocumentNo, String originalFiscalYear, String accountType, String paymentBlock) {
        List<Object> params = new ArrayList<>();
        params.add(companyCode);
        params.add(originalDocumentNo);
        params.add(originalFiscalYear);
        params.add(accountType);
        params.add(paymentBlock);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + GLLine.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(GLLine.COLUMN_NAME_COMPANY_CODE + " = ? ");
        sql.append(" AND " + GLLine.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ? ");
        sql.append(" AND " + GLLine.COLUMN_NAME_ORIGINAL_FISCAL_YEAR + " = ? ");
        sql.append(" AND " + GLLine.COLUMN_NAME_ACCOUNT_TYPE + " = ? ");

        if(!Util.isEmpty(paymentBlock) && paymentBlock.startsWith("B")){
            sql.append(" AND (" + GLLine.COLUMN_NAME_PAYMENT_BLOCK + " LIKE " + " ? ");
            sql.append(" OR PAYMENT_BLOCK = '' OR PAYMENT_BLOCK= ' ' OR PAYMENT_BLOCK IS NULL) ");
        }else{
            sql.append(" AND " + GLLine.COLUMN_NAME_PAYMENT_BLOCK + " LIKE " + " ? ");
        }


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
//        log.info("objParams {}", objParams);


        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }


    @Override
    public List<GLLine> findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndLine(String companyCode, String originalDocumentNo, String originalFiscalYear, int line) {
        List<Object> params = new ArrayList<>();
        params.add(companyCode);
        params.add(originalDocumentNo);
        params.add(originalFiscalYear);
        params.add(line);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + GLLine.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(GLLine.COLUMN_NAME_COMPANY_CODE + " = ? ");
        sql.append(" AND " + GLLine.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ? ");
        sql.append(" AND " + GLLine.COLUMN_NAME_ORIGINAL_FISCAL_YEAR + " = ? ");
        sql.append(" AND " + GLLine.COLUMN_NAME_LINE + " = ? ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.debug("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public List<GLLine> findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(String companyCode, String originalDocumentNo, String originalFiscalYear, String accountType) {
        List<Object> params = new ArrayList<>();
        params.add(companyCode);
        params.add(originalDocumentNo);
        params.add(originalFiscalYear);
        params.add(accountType);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + GLLine.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(GLLine.COLUMN_NAME_COMPANY_CODE + " = ? ");
        sql.append(" AND " + GLLine.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ? ");
        sql.append(" AND " + GLLine.COLUMN_NAME_ORIGINAL_FISCAL_YEAR + " = ? ");
        sql.append(" AND " + GLLine.COLUMN_NAME_ACCOUNT_TYPE + " = ? ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public void updateBlockStatusBatch(List<UnBlockDocument> unBlockDocuments) {
        final int batchSize = 30000;
        List<List<UnBlockDocument>> unBlockDocumentsBatchs = Lists.partition(unBlockDocuments, batchSize);
        final String sqlUpdate = "UPDATE /*+ ENABLE_PARALLEL_DML */ GL_LINE SET PAYMENT_BLOCK = ? WHERE COMPANY_CODE = ? AND ORIGINAL_DOCUMENT_NO = ? AND ORIGINAL_FISCAL_YEAR = ? AND ACCOUNT_TYPE = ?";
        for (List<UnBlockDocument> batch : unBlockDocumentsBatchs) {
            this.jdbcTemplate.batchUpdate(sqlUpdate, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    UnBlockDocument unBlockDocument = batch.get(i);
                    ps.setString(1, unBlockDocument.getPaymentBlock());
                    ps.setString(2, unBlockDocument.getCompanyCode());
                    ps.setString(3, unBlockDocument.getOriginalDocumentNo());
                    ps.setString(4, unBlockDocument.getOriginalFiscalYear());
                    ps.setString(5, unBlockDocument.getAccountType());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public void updateBlockStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String accountType, String paymentBlockOld, String paymentBlockNew, String username, Timestamp updateDate) {
        final String sqlSave = "UPDATE GL_LINE SET PAYMENT_BLOCK = ?, UPDATED_BY = ?, UPDATED = ? WHERE COMPANY_CODE = ? AND ORIGINAL_DOCUMENT_NO = ? AND ORIGINAL_FISCAL_YEAR = ? AND ACCOUNT_TYPE = ? AND PAYMENT_BLOCK LIKE ?";
        this.jdbcTemplate.update(sqlSave, paymentBlockNew, username, updateDate, companyCode, originalDocumentNo, originalFiscalYear, accountType, paymentBlockOld);
    }

    @Override
    public void updateBlockStatusErrorCase(String companyCode, String originalDocumentNo, String originalFiscalYear, String username, Timestamp updateDate) {
        final String sqlSave = "UPDATE GL_LINE SET PAYMENT_BLOCK = ?, UPDATED_BY = ?, UPDATED = ? WHERE COMPANY_CODE = ? AND ORIGINAL_DOCUMENT_NO = ? AND ORIGINAL_FISCAL_YEAR = ? AND ACCOUNT_TYPE = ?";
        this.jdbcTemplate.update(sqlSave, "B", username, updateDate, companyCode, originalDocumentNo, originalFiscalYear, "K");
    }
}
