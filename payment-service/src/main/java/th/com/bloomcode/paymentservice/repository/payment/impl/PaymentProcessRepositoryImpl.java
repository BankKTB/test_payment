package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentAliasRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentProcessRepository;
import th.com.bloomcode.paymentservice.util.Util;
import th.com.bloomcode.paymentservice.webservice.model.response.APPaymentResponse;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class PaymentProcessRepositoryImpl extends MetadataJdbcRepository<PaymentProcess, Long>
    implements PaymentProcessRepository {

  private final JdbcTemplate jdbcTemplate;
  private static PaymentAliasRepository paymentAliasRepository;
  static BeanPropertyRowMapper<PaymentProcess> beanPropertyRowMapper =
      new BeanPropertyRowMapper<>(PaymentProcess.class);
  private static Map<Long, PaymentAlias> paymentAliasMap = new HashMap<>();

  static Updater<PaymentProcess> paymentProcessUpdater =
      (t, mapping) -> {
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_PROCESS_ID, t.getId());
        mapping.put(PaymentProcess.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(PaymentProcess.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(PaymentProcess.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(PaymentProcess.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(PaymentProcess.COLUMN_NAME_ACCOUNT_TYPE, t.getAccountType());
        mapping.put(PaymentProcess.COLUMN_NAME_ASSET_NO, t.getAssetNo());
        mapping.put(PaymentProcess.COLUMN_NAME_ASSET_SUB_NO, t.getAssetSubNo());
        mapping.put(PaymentProcess.COLUMN_NAME_ASSIGNMENT, t.getAssignment());
        mapping.put(PaymentProcess.COLUMN_NAME_BANK_ACCOUNT_NO, t.getBankAccountNo());
        mapping.put(PaymentProcess.COLUMN_NAME_BR_DOCUMENT_NO, t.getBrDocumentNo());
        mapping.put(PaymentProcess.COLUMN_NAME_BR_LINE, t.getBrLine());
        mapping.put(PaymentProcess.COLUMN_NAME_BUDGET_ACCOUNT, t.getBudgetAccount());
        mapping.put(PaymentProcess.COLUMN_NAME_BUDGET_ACTIVITY, t.getBudgetActivity());
        mapping.put(PaymentProcess.COLUMN_NAME_BUDGET_ACTIVITY_NAME, t.getBudgetActivityName());
        mapping.put(PaymentProcess.COLUMN_NAME_COST_CENTER, t.getCostCenter());
        mapping.put(PaymentProcess.COLUMN_NAME_COST_CENTER_NAME, t.getCostCenterName());
        mapping.put(PaymentProcess.COLUMN_NAME_CURRENCY, t.getCurrency());
        mapping.put(PaymentProcess.COLUMN_NAME_DATE_ACCT, t.getDateAcct());
        mapping.put(PaymentProcess.COLUMN_NAME_DATE_BASE_LINE, t.getDateBaseLine());
        mapping.put(PaymentProcess.COLUMN_NAME_DATE_DOC, t.getDateDoc());
        mapping.put(PaymentProcess.COLUMN_NAME_DR_CR, t.getDrCr());
        mapping.put(PaymentProcess.COLUMN_NAME_ERROR_CODE, t.getErrorCode());
        mapping.put(PaymentProcess.COLUMN_NAME_FI_AREA, t.getFiArea());
        mapping.put(PaymentProcess.COLUMN_NAME_FI_AREA_NAME, t.getFiAreaName());
        mapping.put(PaymentProcess.COLUMN_NAME_FUND_CENTER, t.getFundCenter());
        mapping.put(PaymentProcess.COLUMN_NAME_FUND_CENTER_NAME, t.getFundCenterName());
        mapping.put(PaymentProcess.COLUMN_NAME_FUND_SOURCE, t.getFundSource());
        mapping.put(PaymentProcess.COLUMN_NAME_FUND_SOURCE_NAME, t.getFundSourceName());
        mapping.put(PaymentProcess.COLUMN_NAME_GL_ACCOUNT, t.getGlAccount());
        mapping.put(PaymentProcess.COLUMN_NAME_GL_ACCOUNT_NAME, t.getGlAccountName());
        mapping.put(PaymentProcess.COLUMN_NAME_HEADER_REFERENCE, t.getHeaderReference());
        mapping.put(PaymentProcess.COLUMN_NAME_HOUSE_BANK, t.getHouseBank());
        mapping.put(PaymentProcess.COLUMN_NAME_IDEM_STATUS, t.getIdemStatus());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_AMOUNT, t.getInvoiceAmount());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_AMOUNT_PAID, t.getInvoiceAmountPaid());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_COMPANY_CODE, t.getInvoiceCompanyCode());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_COMPANY_NAME, t.getInvoiceCompanyName());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_DOCUMENT_NO, t.getInvoiceDocumentNo());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_DOCUMENT_TYPE, t.getInvoiceDocumentType());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_FISCAL_YEAR, t.getInvoiceFiscalYear());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_PAYMENT_CENTER, t.getInvoicePaymentCenter());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_WTX_AMOUNT, t.getInvoiceWtxAmount());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_WTX_AMOUNT_P, t.getInvoiceWtxAmountP());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_WTX_BASE, t.getInvoiceWtxBase());
        mapping.put(PaymentProcess.COLUMN_NAME_INVOICE_WTX_BASE_P, t.getInvoiceWtxBaseP());
        mapping.put(PaymentProcess.COLUMN_NAME_IS_CHILD, t.isChild());
        mapping.put(PaymentProcess.COLUMN_NAME_LINE, t.getLine());
        mapping.put(PaymentProcess.COLUMN_NAME_LINE_ITEM_TEXT, t.getLineItemText());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_AMOUNT, t.getOriginalAmount());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_AMOUNT_PAID, t.getOriginalAmountPaid());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_COMPANY_CODE, t.getOriginalCompanyCode());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_COMPANY_NAME, t.getOriginalCompanyName());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, t.getOriginalDocumentNo());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_DOCUMENT_TYPE, t.getOriginalDocumentType());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
        mapping.put(
            PaymentProcess.COLUMN_NAME_ORIGINAL_PAYMENT_CENTER, t.getOriginalPaymentCenter());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_AMOUNT, t.getOriginalWtxAmount());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P, t.getOriginalWtxAmountP());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_BASE, t.getOriginalWtxBase());
        mapping.put(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_BASE_P, t.getOriginalWtxBaseP());
        mapping.put(PaymentProcess.COLUMN_NAME_PARENT_COMPANY_CODE, t.getParentCompanyCode());
        mapping.put(PaymentProcess.COLUMN_NAME_PARENT_DOCUMENT_NO, t.getParentDocumentNo());
        mapping.put(PaymentProcess.COLUMN_NAME_PARENT_FISCAL_YEAR, t.getParentFiscalYear());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_BLOCK, t.getPaymentBlock());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_CENTER, t.getPaymentCenter());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_COMPANY_CODE, t.getPaymentCompanyCode());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_DATE, t.getPaymentDate());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_DATE_ACCT, t.getPaymentDateAcct());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_DOCUMENT_NO, t.getPaymentDocumentNo());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_FISCAL_YEAR, t.getPaymentFiscalYear());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_METHOD, t.getPaymentMethod());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_METHOD_NAME, t.getPaymentMethodName());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_NAME, t.getPaymentName());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_REFERENCE, t.getPaymentReference());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_TERM, t.getPaymentTerm());
        mapping.put(PaymentProcess.COLUMN_NAME_PM_GROUP_DOC, t.getPmGroupDoc());
        mapping.put(PaymentProcess.COLUMN_NAME_PM_GROUP_NO, t.getPmGroupNo());
        mapping.put(PaymentProcess.COLUMN_NAME_PO_DOCUMENT_NO, t.getPoDocumentNo());
        mapping.put(PaymentProcess.COLUMN_NAME_PO_LINE, t.getPoLine());
        mapping.put(PaymentProcess.COLUMN_NAME_POSTING_KEY, t.getPostingKey());
        mapping.put(PaymentProcess.COLUMN_NAME_IS_PROPOSAL, t.isProposal());
        mapping.put(PaymentProcess.COLUMN_NAME_IS_PROPOSAL_BLOCK, t.isProposalBlock());
        mapping.put(PaymentProcess.COLUMN_NAME_REFERENCE1, t.getReference1());
        mapping.put(PaymentProcess.COLUMN_NAME_REFERENCE2, t.getReference2());
        mapping.put(PaymentProcess.COLUMN_NAME_REFERENCE3, t.getReference3());
        mapping.put(PaymentProcess.COLUMN_NAME_SPECIAL_GL, t.getSpecialGL());
        mapping.put(PaymentProcess.COLUMN_NAME_SPECIAL_GL_NAME, t.getSpecialGLName());
        mapping.put(PaymentProcess.COLUMN_NAME_STATUS, t.getStatus());
        mapping.put(PaymentProcess.COLUMN_NAME_TRADING_PARTNER, t.getTradingPartner());
        mapping.put(PaymentProcess.COLUMN_NAME_TRADING_PARTNER_NAME, t.getTradingPartnerName());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID, t.getPaymentAliasId());
        mapping.put(
            PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_COMPANY_CODE,
            t.getReversePaymentCompanyCode());
        mapping.put(
            PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_NO,
            t.getReversePaymentDocumentNo());
        mapping.put(
            PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_FISCAL_YEAR,
            t.getReversePaymentFiscalYear());
        mapping.put(
            PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_TYPE,
            t.getReversePaymentDocumentType());
        mapping.put(PaymentProcess.COLUMN_NAME_PAYMENT_DOCUMENT_TYPE, t.getPaymentDocumentType());
        mapping.put(PaymentProcess.COLUMN_NAME_IDEM_UPDATE, t.getIdemUpdate());
        mapping.put(PaymentProcess.COLUMN_NAME_IS_HAVE_CHILD, t.isHaveChild());
        mapping.put(PaymentProcess.COLUMN_NAME_CREDIT_MEMO, t.getCreditMemo());
        mapping.put(PaymentProcess.COLUMN_NAME_WTX_CREDIT_MEMO, t.getWtxCreditMemo());
        mapping.put(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_STATUS, t.getReversePaymentStatus());
      };

  static Map<String, Integer> updaterType =
      Map.<String, Integer>ofEntries(
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_PROCESS_ID, Types.BIGINT),
          entry(PaymentProcess.COLUMN_NAME_CREATED, Types.TIMESTAMP),
          entry(PaymentProcess.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
          entry(PaymentProcess.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ACCOUNT_TYPE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ASSET_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ASSET_SUB_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ASSIGNMENT, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_BANK_ACCOUNT_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_BR_DOCUMENT_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_BR_LINE, Types.INTEGER),
          entry(PaymentProcess.COLUMN_NAME_BUDGET_ACCOUNT, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_BUDGET_ACTIVITY, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_BUDGET_ACTIVITY_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_COST_CENTER, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_COST_CENTER_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_CURRENCY, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_DATE_ACCT, Types.TIMESTAMP),
          entry(PaymentProcess.COLUMN_NAME_DATE_BASE_LINE, Types.TIMESTAMP),
          entry(PaymentProcess.COLUMN_NAME_DATE_DOC, Types.TIMESTAMP),
          entry(PaymentProcess.COLUMN_NAME_DR_CR, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ERROR_CODE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_FI_AREA, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_FI_AREA_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_FUND_CENTER, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_FUND_CENTER_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_FUND_SOURCE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_FUND_SOURCE_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_GL_ACCOUNT, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_GL_ACCOUNT_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_HEADER_REFERENCE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_HOUSE_BANK, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_IDEM_STATUS, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_AMOUNT, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_AMOUNT_PAID, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_COMPANY_CODE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_COMPANY_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_DOCUMENT_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_DOCUMENT_TYPE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_FISCAL_YEAR, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_PAYMENT_CENTER, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_WTX_AMOUNT, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_WTX_AMOUNT_P, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_WTX_BASE, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_INVOICE_WTX_BASE_P, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_IS_CHILD, Types.BOOLEAN),
          entry(PaymentProcess.COLUMN_NAME_LINE, Types.INTEGER),
          entry(PaymentProcess.COLUMN_NAME_LINE_ITEM_TEXT, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_AMOUNT, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_AMOUNT_PAID, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_COMPANY_CODE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_COMPANY_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_DOCUMENT_TYPE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_PAYMENT_CENTER, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_AMOUNT, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_BASE, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_ORIGINAL_WTX_BASE_P, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_PARENT_COMPANY_CODE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PARENT_DOCUMENT_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PARENT_FISCAL_YEAR, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_BLOCK, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_CENTER, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_COMPANY_CODE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_DATE, Types.TIMESTAMP),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_DATE_ACCT, Types.TIMESTAMP),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_DOCUMENT_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_METHOD, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_METHOD_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_REFERENCE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_TERM, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PM_GROUP_DOC, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PM_GROUP_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PO_DOCUMENT_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PO_LINE, Types.INTEGER),
          entry(PaymentProcess.COLUMN_NAME_POSTING_KEY, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_IS_PROPOSAL, Types.BOOLEAN),
          entry(PaymentProcess.COLUMN_NAME_IS_PROPOSAL_BLOCK, Types.BOOLEAN),
          entry(PaymentProcess.COLUMN_NAME_REFERENCE1, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_REFERENCE2, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_REFERENCE3, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_SPECIAL_GL, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_SPECIAL_GL_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_STATUS, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_TRADING_PARTNER, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_TRADING_PARTNER_NAME, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID, Types.BIGINT),
          entry(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_COMPANY_CODE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_NO, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_DOCUMENT_TYPE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_PAYMENT_DOCUMENT_TYPE, Types.NVARCHAR),
          entry(PaymentProcess.COLUMN_NAME_IDEM_UPDATE, Types.TIMESTAMP),
          entry(PaymentProcess.COLUMN_NAME_IS_HAVE_CHILD, Types.BOOLEAN),
          entry(PaymentProcess.COLUMN_NAME_CREDIT_MEMO, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_WTX_CREDIT_MEMO, Types.NUMERIC),
          entry(PaymentProcess.COLUMN_NAME_REVERSE_PAYMENT_STATUS, Types.NVARCHAR));

  static RowMapper<PaymentProcess> paymentProcessRowMapper =
      (rs, rowNum) ->
          new PaymentProcess(
              rs.getLong(PaymentProcess.COLUMN_NAME_PAYMENT_PROCESS_ID),
              rs.getTimestamp(PaymentProcess.COLUMN_NAME_CREATED),
              rs.getString(PaymentProcess.COLUMN_NAME_CREATED_BY),
              rs.getTimestamp(PaymentProcess.COLUMN_NAME_UPDATED),
              rs.getString(PaymentProcess.COLUMN_NAME_UPDATED_BY),
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
              rs.getLong(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID),
              getPaymentAlias(rs.getLong(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID)),
              rs.getTimestamp(PaymentProcess.COLUMN_NAME_IDEM_UPDATE),
              rs.getBoolean(PaymentProcess.COLUMN_NAME_IS_HAVE_CHILD));

  public PaymentProcessRepositoryImpl(
      @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate,
      PaymentAliasRepository paymentAliasRepository) {
    super(
        paymentProcessRowMapper,
        paymentProcessUpdater,
        updaterType,
        PaymentProcess.TABLE_NAME,
        PaymentProcess.COLUMN_NAME_PAYMENT_PROCESS_ID,
        jdbcTemplate);
    this.jdbcTemplate = jdbcTemplate;
    PaymentProcessRepositoryImpl.paymentAliasRepository = paymentAliasRepository;
  }

  @Override
  public PaymentProcess findOneByPmGroupDocAndPmGroupNoAndProposalFalse(
      String pmGroupDoc, String pmGroupNo) {
    List<Object> params = new ArrayList<>();

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    if (!Util.isEmpty(pmGroupDoc)) {
      params.add(pmGroupDoc);
      sql.append(PaymentProcess.COLUMN_NAME_PM_GROUP_DOC + " = ? ");
    }
    if (!Util.isEmpty(pmGroupNo)) {
      params.add(pmGroupNo);
      sql.append(" AND " + PaymentProcess.COLUMN_NAME_PM_GROUP_NO + " = ? ");
    }

    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql find One {}", sql.toString());
    List<PaymentProcess> paymentProcesses =
        this.jdbcTemplate.query(sql.toString(), objParams, paymentProcessRowMapper);
    if (!paymentProcesses.isEmpty()) {
      removePaymentAlias(paymentProcesses.get(0).getPaymentAliasId());
      return paymentProcesses.get(0);
    } else {
      return null;
    }
  }

  @Override
  public PaymentProcess findOneByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(
      String accDocNo, String compCode, String fiscalYear) {
    List<Object> params = new ArrayList<>();
    params.add(accDocNo);
    params.add(compCode);
    params.add(fiscalYear);
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(PaymentProcess.COLUMN_NAME_PAYMENT_DOCUMENT_NO + " = ? ");
    sql.append(" AND " + PaymentProcess.COLUMN_NAME_PAYMENT_COMPANY_CODE + " = ? ");
    sql.append(" AND " + PaymentProcess.COLUMN_NAME_PAYMENT_FISCAL_YEAR + " = ? ");
    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql find One {}", sql.toString());
    List<PaymentProcess> paymentProcesses =
        this.jdbcTemplate.query(sql.toString(), objParams, paymentProcessRowMapper);
    if (!paymentProcesses.isEmpty()) {
      removePaymentAlias(paymentProcesses.get(0).getPaymentAliasId());
      return paymentProcesses.get(0);
    } else {
      return null;
    }
  }

  @Override
  public List<PaymentProcess> findAllByPaymentDocNoAndPaymentCompCodeAndPaymentFiscalYear(String accDocNo, String compCode, String fiscalYear) {
    List<Object> params = new ArrayList<>();
    params.add(accDocNo);
    params.add(compCode);
    params.add(fiscalYear);
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(PaymentProcess.COLUMN_NAME_PAYMENT_DOCUMENT_NO + " = ? ");
    sql.append(" AND " + PaymentProcess.COLUMN_NAME_PAYMENT_COMPANY_CODE + " = ? ");
    sql.append(" AND " + PaymentProcess.COLUMN_NAME_PAYMENT_FISCAL_YEAR + " = ? ");
    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql find One {}", sql);
    List<PaymentProcess> paymentProcesses =
        this.jdbcTemplate.query(sql.toString(), objParams, paymentProcessRowMapper);

    for (PaymentProcess paymentProcess : paymentProcesses) {
      removePaymentAlias(paymentProcess.getPaymentAliasId());
    }

    return paymentProcesses;
  }

  @Override
  public List<PaymentProcess>
      findAllByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalTrueAndIsChildTrue(
          Long paymentId, String parentCompCode, String parentDocNo, String parentFiscalYear) {
    List<Object> params = new ArrayList<>();
    params.add(paymentId);
    params.add(parentCompCode);
    params.add(parentDocNo);
    params.add(parentFiscalYear);
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
    sql.append(" AND " + PaymentProcess.COLUMN_NAME_PARENT_COMPANY_CODE + " = ? ");
    sql.append(" AND " + PaymentProcess.COLUMN_NAME_PARENT_DOCUMENT_NO + " = ? ");
    sql.append(" AND " + PaymentProcess.COLUMN_NAME_PARENT_FISCAL_YEAR + " = ? ");
    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql find One {}", sql.toString());
    List<PaymentProcess> paymentProcesses =
        this.jdbcTemplate.query(sql.toString(), objParams, paymentProcessRowMapper);
    if (!paymentProcesses.isEmpty()) {
      removePaymentAlias(paymentProcesses.get(0).getPaymentAliasId());
      return paymentProcesses;
    } else {
      return null;
    }
  }

  @Override
  public Long findOneByPaymentAliasId(Long id) {
    List<Object> params = new ArrayList<>();
    params.add(id);
    StringBuilder sql = new StringBuilder();
    sql.append(" SELECT COUNT(1) FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
    sql.append(" AND IS_PROPOSAL = 0 ");
    sql.append(" AND PAYMENT_DOCUMENT_NO IS NOT NULL ");
    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql {}", sql.toString());
    log.info("params : {} ", params);
    return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
  }

  @Override
  public Long findOneByPaymentAliasIdNotParent(Long id) {
    List<Object> params = new ArrayList<>();
    params.add(id);
    StringBuilder sql = new StringBuilder();
    sql.append(" SELECT COUNT(1) FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
    sql.append(" AND IS_PROPOSAL = 0 ");
    sql.append(" AND PAYMENT_DOCUMENT_NO IS NOT NULL ");
    //        sql.append(" AND  INVOICE_DOCUMENT_TYPE NOT IN ('KX','K3') ");
    sql.append(" AND PAYMENT_REFERENCE IS NOT NULL ");
    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql {}", sql.toString());
    log.info("params : {} ", params);
    return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
  }

  @Override
  public List<PaymentProcess> findAllByPaymentIdAndProposalNotChild(
      Long paymentId, boolean proposal) {
    List<Object> params = new ArrayList<>();
    params.add(paymentId);
    StringBuilder sql = new StringBuilder();
    sql.append(
        " SELECT * FROM PAYMENT_PROCESS PC LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID");
    sql.append(" WHERE PC.PAYMENT_ALIAS_ID = ?");

    //        sql.append(" AND PAYMENT_DOCUMENT_NO IS NOT NULL ");
    //        sql.append(" AND  INVOICE_DOCUMENT_TYPE NOT IN ('KX','K3') ");
    sql.append(
        " AND (PC.PAYMENT_REFERENCE IS NOT NULL OR (PI.PAYEE_CODE IS NOT NULL AND PC.PAYMENT_REFERENCE       IS NULL)) ");

    if (proposal) {
      sql.append(" AND IS_PROPOSAL = 1");
    } else {
      sql.append(" AND IS_PROPOSAL = 0 ");
    }
    sql.append(" AND IS_CHILD = 0 ");

    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql find findAllByPaymentIdAndProposalNotChild {}", sql.toString());
    this.jdbcTemplate.setFetchSize(5000);
    List<PaymentProcess> paymentProcesses =
        this.jdbcTemplate.query(sql.toString(), objParams, paymentProcessRowMapper);
    if (!paymentProcesses.isEmpty()) {
      removePaymentAlias(paymentProcesses.get(0).getPaymentAliasId());
    }
    return paymentProcesses;
  }

  @Override
  public void restDocumentProposalErrorAfterRealRun(Long paymentId) {
    StringBuilder sql = new StringBuilder();
      sql.append("          UPDATE GL_HEAD           ");
      sql.append("          SET PAYMENT_ID = 0 , PAYMENT_DOCUMENT_NO = NULL          ");
      sql.append("          WHERE (ORIGINAL_DOCUMENT_NO, COMPANY_CODE, ORIGINAL_FISCAL_YEAR) IN           ");
      sql.append("          (SELECT PC.INVOICE_DOCUMENT_NO, PC.INVOICE_COMPANY_CODE, PC.INVOICE_FISCAL_YEAR           ");
      sql.append("          FROM PAYMENT_PROCESS PC           ");
      sql.append("          LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID           ");
      sql.append("          WHERE PC.PAYMENT_ALIAS_ID = ?           ");
      sql.append("          AND ((IS_PROPOSAL = 1 AND PC.STATUS = 'E') OR (IS_PROPOSAL = 0 AND PC.IDEM_STATUS = 'E')))           ");
    this.jdbcTemplate.update(sql.toString(), paymentId);
  }

  @Override
  public void restDocumentProposalChildErrorAfterRealRun(Long paymentId) {
    StringBuilder sql = new StringBuilder();
    sql.append("          UPDATE GL_HEAD           ");
    sql.append("          SET PAYMENT_ID = 0 , PAYMENT_DOCUMENT_NO = NULL           ");
    sql.append("          WHERE (ORIGINAL_DOCUMENT_NO, COMPANY_CODE, ORIGINAL_FISCAL_YEAR) IN (           ");
    sql.append("          SELECT INVOICE_DOCUMENT_NO, INVOICE_COMPANY_CODE, INVOICE_FISCAL_YEAR           ");
    sql.append("          FROM PAYMENT_PROCESS           ");
    sql.append("          WHERE (PARENT_DOCUMENT_NO, PARENT_COMPANY_CODE, PARENT_FISCAL_YEAR) IN           ");
    sql.append("          (SELECT PC.INVOICE_DOCUMENT_NO, PC.INVOICE_COMPANY_CODE, PC.INVOICE_FISCAL_YEAR           ");
    sql.append("          FROM PAYMENT_PROCESS PC           ");
    sql.append("          LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID           ");
    sql.append("          WHERE PC.PAYMENT_ALIAS_ID = ?           ");
    sql.append("          AND ((IS_PROPOSAL = 1 AND PC.STATUS = 'E') OR (IS_PROPOSAL = 0 AND PC.IDEM_STATUS = 'E'))           ");
    sql.append("          )           ");
    sql.append("          AND PAYMENT_ALIAS_ID = ?           ");
    sql.append("          )           ");
    this.jdbcTemplate.update(sql.toString(), paymentId,paymentId);
  }

  @Override
  public void updateReversePaymentDocument(Long paymentAliasId) {
    StringBuilder sql = new StringBuilder();
    sql.append(" UPDATE ");
    sql.append(" PAYMENT_PROCESS ");
    sql.append(" SET REVERSE_PAYMENT_STATUS = ? ");
    sql.append(" WHERE ");
    sql.append(" 1 = 1  ");
    sql.append(" AND PAYMENT_ALIAS_ID = ? ");
    //        sql.append(" AND PAYMENT_DOCUMENT_NO = ? ");
    //        sql.append(" AND PAYMENT_FISCAL_YEAR = ? ");
    sql.append(" AND IS_CHILD = 0 ");
    sql.append(" AND IS_PROPOSAL = 0 ");
    this.jdbcTemplate.update(sql.toString(), "W", paymentAliasId);
  }

  @Override
  public Long countIdemReversePaymentReplyByPaymentAliasId(Long paymentAliasId) {
    List<Object> params = new ArrayList<>();
    params.add(paymentAliasId);
    StringBuilder sql = new StringBuilder();
    sql.append(" SELECT COUNT(1) FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
    sql.append(" AND IS_PROPOSAL = 0 ");
    sql.append(" AND PAYMENT_DOCUMENT_NO IS NOT NULL ");
    //        sql.append(" AND INVOICE_DOCUMENT_TYPE NOT IN ('KX','K3') ");
    sql.append(" AND PAYMENT_REFERENCE IS NOT NULL ");
    sql.append(" AND REVERSE_PAYMENT_STATUS != 'W' ");
    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql {}", sql.toString());
    log.info("params : {} ", params);
    return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
  }

  @Override
  public Long countIdemCreatePaymentReplyByPaymentAliasId(Long paymentAliasId) {
    List<Object> params = new ArrayList<>();
    params.add(paymentAliasId);
    StringBuilder sql = new StringBuilder();
    sql.append(" SELECT COUNT(1) FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
    sql.append(" AND IS_PROPOSAL = 0 ");
    //        sql.append(" AND PAYMENT_DOCUMENT_NO IS NOT NULL ");
    //        sql.append(" AND INVOICE_DOCUMENT_TYPE NOT IN ('KX','K3') ");
    //        sql.append(" AND PAYMENT_REFERENCE IS NOT NULL ");
    sql.append(" AND IDEM_UPDATE IS NOT NULL ");
    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql count idem {}", sql.toString());
    log.info("params : {} ", params);
    return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
  }

  @Override
  public PaymentProcess
      findOneByPaymentIdAndParentCompCodeAndParentDocNoAndParentFiscalYearAndProposalAndIsChild(
          Long paymentId,
          String parentCompCode,
          String parentDocNo,
          String parentFiscalYear,
          boolean proposal,
          boolean child) {
    List<Object> params = new ArrayList<>();
    params.add(paymentId);
    params.add(parentCompCode);
    params.add(parentDocNo);
    params.add(parentFiscalYear);

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
    sql.append(" AND " + PaymentProcess.COLUMN_NAME_PARENT_COMPANY_CODE + " = ? ");
    sql.append(" AND " + PaymentProcess.COLUMN_NAME_PARENT_DOCUMENT_NO + " = ? ");
    sql.append(" AND " + PaymentProcess.COLUMN_NAME_PARENT_FISCAL_YEAR + " = ? ");
    if (proposal) {
      sql.append(" AND IS_PROPOSAL = 1");
    } else {
      sql.append(" AND IS_PROPOSAL = 0 ");
    }

    if (child) {
      sql.append(" AND IS_CHILD = 1");
    } else {
      sql.append(" AND IS_CHILD = 0 ");
    }

    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql find One {}", sql.toString());
    List<PaymentProcess> paymentProcesses =
        this.jdbcTemplate.query(sql.toString(), objParams, paymentProcessRowMapper);
    if (!paymentProcesses.isEmpty()) {
      removePaymentAlias(paymentProcesses.get(0).getPaymentAliasId());
      return paymentProcesses.get(0);
    } else {
      return null;
    }
  }

  @Override
  public void updateProposalBlock(Long paymentProcessId) {
    StringBuilder sql = new StringBuilder();
    sql.append(" UPDATE ");
    sql.append(" PAYMENT_PROCESS ");
    sql.append(" SET IS_PROPOSAL_BLOCK = ? ");
    sql.append(" WHERE ");
    sql.append(" 1 = 1  ");
    sql.append(" AND ID = ? ");
    log.info("sql updateProposalBlock {}", sql.toString());
    this.jdbcTemplate.update(sql.toString(), 1, paymentProcessId);
  }

  @Override
  public Long findOneByPaymentAliasIdForReverseDocument(Long id) {
    List<Object> params = new ArrayList<>();
    params.add(id);
    StringBuilder sql = new StringBuilder();
    sql.append(" SELECT COUNT(1) FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
    sql.append(" AND IS_PROPOSAL = 0 ");
    sql.append(" AND REVERSE_PAYMENT_DOCUMENT_NO IS NOT NULL ");
    sql.append(" AND INVOICE_DOCUMENT_TYPE NOT IN ('KX','K3') ");
    sql.append(" AND REVERSE_PAYMENT_DOCUMENT_NO IS NOT NULL ");
    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql {}", sql.toString());
    log.info("params : {} ", params);
    return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
  }

  @Override
  public void updatePaymentDocument(APPaymentResponse aPPaymentResponse) {
    StringBuilder sql = new StringBuilder();
    sql.append(" UPDATE ");
    sql.append(" PAYMENT_PROCESS ");
    sql.append(
        " SET IDEM_STATUS = ?, IDEM_UPDATE = ? , PAYMENT_COMPANY_CODE = ? , PAYMENT_DOCUMENT_NO = ? , PAYMENT_FISCAL_YEAR = ? ");
    sql.append(" WHERE ");
    sql.append(" 1 = 1  ");
    sql.append(" AND PM_GROUP_DOC = ? ");
    sql.append(" AND PM_GROUP_NO = ? ");
    sql.append(" AND IS_PROPOSAL = 0 ");
    this.jdbcTemplate.update(
        sql.toString(),
        "S",
        new Timestamp(System.currentTimeMillis()),
        aPPaymentResponse.getCompCode(),
        aPPaymentResponse.getAccDocNo(),
        aPPaymentResponse.getFiscalYear(),
        aPPaymentResponse.getPmGroupDoc(),
        aPPaymentResponse.getPmGroupNo());
  }

  @Override
  public void updatePaymentDocument(APPaymentResponse aPPaymentResponse, String compCodeName) {
    StringBuilder sql = new StringBuilder();
    sql.append(" UPDATE ");
    sql.append(" PAYMENT_PROCESS ");
    sql.append(
        " SET IDEM_STATUS = ?, IDEM_UPDATE = ? , PAYMENT_COMPANY_CODE = ?, PAYMENT_COMPANY_NAME = ?, PAYMENT_DOCUMENT_NO = ?, PAYMENT_FISCAL_YEAR = ? ");
    sql.append(" WHERE ");
    sql.append(" 1 = 1  ");
    sql.append(" AND PM_GROUP_DOC = ? ");
    sql.append(" AND PM_GROUP_NO = ? ");
    sql.append(" AND IS_PROPOSAL = 0 ");
    this.jdbcTemplate.update(
        sql.toString(),
        "S",
        new Timestamp(System.currentTimeMillis()),
        aPPaymentResponse.getCompCode(),
        compCodeName,
        aPPaymentResponse.getAccDocNo(),
        aPPaymentResponse.getFiscalYear(),
        aPPaymentResponse.getPmGroupDoc(),
        aPPaymentResponse.getPmGroupNo());
  }

  @Override
  public void saveBatch(List<PaymentProcess> paymentProcesses) {
    try{
      final int batchSize = 30000;
      List<List<PaymentProcess>> paymentProcessBatches = Lists.partition(paymentProcesses, batchSize);
      final String sqlSave =
          "INSERT /*+ ENABLE_PARALLEL_DML */ INTO PAYMENT_PROCESS (ID, ACCOUNT_TYPE, ASSET_NO, ASSET_SUB_NO, ASSIGNMENT, BANK_ACCOUNT_NO, "
              + "BR_DOCUMENT_NO, BR_LINE, BUDGET_ACCOUNT, BUDGET_ACTIVITY, BUDGET_ACTIVITY_NAME, IS_CHILD, COST_CENTER, COST_CENTER_NAME, CURRENCY, "
              + "DATE_ACCT, DATE_BASE_LINE, DATE_DOC, DR_CR, ERROR_CODE, FI_AREA, FUND_CENTER, FUND_CENTER_NAME, FUND_SOURCE, FUND_SOURCE_NAME, "
              + "GL_ACCOUNT, GL_ACCOUNT_NAME, HEADER_REFERENCE, HOUSE_BANK, IDEM_STATUS, INVOICE_AMOUNT, INVOICE_COMPANY_CODE, INVOICE_COMPANY_NAME, "
              + "INVOICE_DOCUMENT_NO, INVOICE_DOCUMENT_TYPE, INVOICE_FISCAL_YEAR, INVOICE_PAYMENT_CENTER, INVOICE_WTX_AMOUNT, INVOICE_WTX_AMOUNT_P, "
              + "INVOICE_WTX_BASE, INVOICE_WTX_BASE_P, LINE, LINE_ITEM_TEXT, ORIGINAL_AMOUNT, ORIGINAL_COMPANY_CODE, ORIGINAL_COMPANY_NAME, "
              + "ORIGINAL_DOCUMENT_NO, ORIGINAL_DOCUMENT_TYPE, ORIGINAL_FISCAL_YEAR, ORIGINAL_PAYMENT_CENTER, ORIGINAL_WTX_AMOUNT, ORIGINAL_WTX_AMOUNT_P, "
              + "ORIGINAL_WTX_BASE, ORIGINAL_WTX_BASE_P, PARENT_COMPANY_CODE, PARENT_DOCUMENT_NO, PARENT_FISCAL_YEAR, PAYMENT_ALIAS_ID, PAYMENT_BLOCK, "
              + "PAYMENT_CENTER, PAYMENT_COMPANY_CODE, PAYMENT_DATE, PAYMENT_DATE_ACCT, PAYMENT_DOCUMENT_NO, PAYMENT_FISCAL_YEAR, PAYMENT_METHOD, "
              + "PAYMENT_METHOD_NAME, PAYMENT_NAME, PAYMENT_REFERENCE, PAYMENT_TERM, PM_GROUP_DOC, PM_GROUP_NO, PO_DOCUMENT_NO, PO_LINE, POSTING_KEY, "
              + "IS_PROPOSAL, IS_PROPOSAL_BLOCK, REFERENCE1, REFERENCE2, REFERENCE3, SPECIAL_GL, SPECIAL_GL_NAME, STATUS, TRADING_PARTNER, "
              + "TRADING_PARTNER_NAME, CREATED, CREATED_BY, UPDATED, UPDATED_BY, INVOICE_AMOUNT_PAID, ORIGINAL_AMOUNT_PAID, FI_AREA_NAME,IS_HAVE_CHILD,CREDIT_MEMO,WTX_CREDIT_MEMO) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
      for (List<PaymentProcess> batch : paymentProcessBatches) {
        this.jdbcTemplate.batchUpdate(
            sqlSave,
            new BatchPreparedStatementSetter() {
              @Override
              public void setValues(PreparedStatement ps, int i) throws SQLException {
                int index = 0;
                PaymentProcess paymentProcess = batch.get(i);
                ps.setLong(++index, paymentProcess.getId());
                ps.setString(++index, paymentProcess.getAccountType());
                ps.setString(++index, paymentProcess.getAssetNo());
                ps.setString(++index, paymentProcess.getAssetSubNo());
                ps.setString(++index, paymentProcess.getAssignment());
                ps.setString(++index, paymentProcess.getBankAccountNo());
                ps.setString(++index, paymentProcess.getBrDocumentNo());
                ps.setInt(++index, paymentProcess.getBrLine());
                ps.setString(++index, paymentProcess.getBudgetAccount());
                ps.setString(++index, paymentProcess.getBudgetActivity());
                ps.setString(++index, paymentProcess.getBudgetActivityName());
                ps.setBoolean(++index, paymentProcess.isChild());
                ps.setString(++index, paymentProcess.getCostCenter());
                ps.setString(++index, paymentProcess.getCostCenterName());
                ps.setString(++index, paymentProcess.getCurrency());
                ps.setTimestamp(++index, paymentProcess.getDateAcct());
                ps.setTimestamp(++index, paymentProcess.getDateBaseLine());
                ps.setTimestamp(++index, paymentProcess.getDateDoc());
                ps.setString(++index, paymentProcess.getDrCr());
                ps.setString(++index, paymentProcess.getErrorCode());
                ps.setString(++index, paymentProcess.getFiArea());
                ps.setString(++index, paymentProcess.getFundCenter());
                ps.setString(++index, paymentProcess.getFundCenterName());
                ps.setString(++index, paymentProcess.getFundSource());
                ps.setString(++index, paymentProcess.getFundSourceName());
                ps.setString(++index, paymentProcess.getGlAccount());
                ps.setString(++index, paymentProcess.getGlAccountName());
                ps.setString(++index, paymentProcess.getHeaderReference());
                ps.setString(++index, paymentProcess.getHouseBank());
                ps.setString(++index, paymentProcess.getIdemStatus());
                ps.setBigDecimal(++index, paymentProcess.getInvoiceAmount());
                ps.setString(++index, paymentProcess.getInvoiceCompanyCode());
                ps.setString(++index, paymentProcess.getInvoiceCompanyName());
                ps.setString(++index, paymentProcess.getInvoiceDocumentNo());
                ps.setString(++index, paymentProcess.getInvoiceDocumentType());
                ps.setString(++index, paymentProcess.getInvoiceFiscalYear());
                ps.setString(++index, paymentProcess.getInvoicePaymentCenter());
                ps.setBigDecimal(++index, paymentProcess.getInvoiceWtxAmount());
                ps.setBigDecimal(++index, paymentProcess.getInvoiceWtxAmountP());
                ps.setBigDecimal(++index, paymentProcess.getInvoiceWtxBase());
                ps.setBigDecimal(++index, paymentProcess.getInvoiceWtxBaseP());
                ps.setInt(++index, paymentProcess.getLine());
                ps.setString(++index, paymentProcess.getLineItemText());
                ps.setBigDecimal(++index, paymentProcess.getOriginalAmount());
                ps.setString(++index, paymentProcess.getOriginalCompanyCode());
                ps.setString(++index, paymentProcess.getOriginalCompanyName());
                ps.setString(++index, paymentProcess.getOriginalDocumentNo());
                ps.setString(++index, paymentProcess.getOriginalDocumentType());
                ps.setString(++index, paymentProcess.getOriginalFiscalYear());
                ps.setString(++index, paymentProcess.getOriginalPaymentCenter());
                ps.setBigDecimal(++index, paymentProcess.getOriginalWtxAmount());
                ps.setBigDecimal(++index, paymentProcess.getOriginalWtxAmountP());
                ps.setBigDecimal(++index, paymentProcess.getOriginalWtxBase());
                ps.setBigDecimal(++index, paymentProcess.getOriginalWtxBaseP());
                ps.setString(++index, paymentProcess.getParentCompanyCode());
                ps.setString(++index, paymentProcess.getParentDocumentNo());
                ps.setString(++index, paymentProcess.getParentFiscalYear());
                ps.setLong(++index, paymentProcess.getPaymentAliasId());
                ps.setString(++index, paymentProcess.getPaymentBlock());
                ps.setString(++index, paymentProcess.getPaymentCenter());
                ps.setString(++index, paymentProcess.getPaymentCompanyCode());
                ps.setTimestamp(++index, paymentProcess.getPaymentDate());
                ps.setTimestamp(++index, paymentProcess.getPaymentDateAcct());
                ps.setString(++index, paymentProcess.getPaymentDocumentNo());
                ps.setString(++index, paymentProcess.getPaymentFiscalYear());
                ps.setString(++index, paymentProcess.getPaymentMethod());
                ps.setString(++index, paymentProcess.getPaymentMethodName());
                ps.setString(++index, paymentProcess.getPaymentName());
                ps.setString(++index, paymentProcess.getPaymentReference());
                ps.setString(++index, paymentProcess.getPaymentTerm());
                ps.setString(++index, paymentProcess.getPmGroupDoc());
                ps.setString(++index, paymentProcess.getPmGroupNo());
                ps.setString(++index, paymentProcess.getPoDocumentNo());
                ps.setInt(++index, paymentProcess.getPoLine());
                ps.setString(++index, paymentProcess.getPostingKey());
                ps.setBoolean(++index, paymentProcess.isProposal());
                ps.setBoolean(++index, paymentProcess.isProposalBlock());
                ps.setString(++index, paymentProcess.getReference1());
                ps.setString(++index, paymentProcess.getReference2());
                ps.setString(++index, paymentProcess.getReference3());
                ps.setString(++index, paymentProcess.getSpecialGL());
                ps.setString(++index, paymentProcess.getSpecialGLName());
                ps.setString(++index, paymentProcess.getStatus());
                ps.setString(++index, paymentProcess.getTradingPartner());
                ps.setString(++index, paymentProcess.getTradingPartnerName());
                ps.setTimestamp(++index, paymentProcess.getCreated());
                ps.setString(++index, paymentProcess.getCreatedBy());
                ps.setTimestamp(++index, paymentProcess.getUpdated());
                ps.setString(++index, paymentProcess.getUpdatedBy());
                ps.setBigDecimal(++index, paymentProcess.getInvoiceAmountPaid());
                ps.setBigDecimal(++index, paymentProcess.getOriginalAmountPaid());
                ps.setString(++index, paymentProcess.getFiAreaName());
                ps.setBoolean(++index, paymentProcess.isHaveChild());
                ps.setBigDecimal(++index, paymentProcess.getCreditMemo());
                ps.setBigDecimal(++index, paymentProcess.getWtxCreditMemo());
              }

              @Override
              public int getBatchSize() {
                return batch.size();
              }
            });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public PaymentProcess findOneIdemLastUpdatePaymentByPaymentAliasId(Long paymentAliasId) {
    List<Object> params = new ArrayList<>();
    params.add(paymentAliasId);

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + PaymentProcess.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(PaymentProcess.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
    sql.append(" AND IS_PROPOSAL = 0 ");
    sql.append(" AND IDEM_UPDATE IS NOT NULL ");
    sql.append(" ORDER BY IDEM_UPDATE DESC ");

    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql find One {}", sql.toString());
    List<PaymentProcess> paymentProcesses =
        this.jdbcTemplate.query(sql.toString(), objParams, paymentProcessRowMapper);
    if (!paymentProcesses.isEmpty()) {
      removePaymentAlias(paymentProcesses.get(0).getPaymentAliasId());
      return paymentProcesses.get(0);
    } else {
      return null;
    }
  }

  private static PaymentAlias getPaymentAlias(Long id) {
    if (null == paymentAliasMap.get(id)) {
      paymentAliasMap.put(id, PaymentProcessRepositoryImpl.paymentAliasRepository.findOneById(id));
    }
    return paymentAliasMap.get(id);
  }

  private static void removePaymentAlias(Long id) {
    if (null != paymentAliasMap.get(id)) {
      paymentAliasMap.remove(id);
    }
  }
}
