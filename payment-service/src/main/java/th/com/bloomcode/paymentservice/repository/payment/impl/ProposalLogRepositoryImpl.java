package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.ReturnSearchProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.model.response.ReturnProposalLogResponse;
import th.com.bloomcode.paymentservice.model.response.ReturnReverseInvoiceResponse;
import th.com.bloomcode.paymentservice.model.response.ReturnReversePaymentResponse;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.ProposalLogRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
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
public class ProposalLogRepositoryImpl extends MetadataJdbcRepository<ProposalLog, Long> implements ProposalLogRepository {
    static BeanPropertyRowMapper<ProposalLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(ProposalLog.class);
    static BeanPropertyRowMapper<ReturnProposalLogResponse> returnProposalLogRowMapper = new BeanPropertyRowMapper<>(ReturnProposalLogResponse.class);
    static BeanPropertyRowMapper<ReturnReversePaymentResponse> returnReversePaymentRowMapper = new BeanPropertyRowMapper<>(ReturnReversePaymentResponse.class);
    static BeanPropertyRowMapper<ReturnReverseInvoiceResponse> returnReverseInvoiceRowMapper = new BeanPropertyRowMapper<>(ReturnReverseInvoiceResponse.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<ProposalLog> ProposalLogUpdater = (t, mapping) -> {
        mapping.put(ProposalLog.COLUMN_NAME_PROPOSAL_LOG_ID, t.getId());
        mapping.put(ProposalLog.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(ProposalLog.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(ProposalLog.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(ProposalLog.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(ProposalLog.COLUMN_NAME_REF_RUNNING, t.getRefRunning());
        mapping.put(ProposalLog.COLUMN_NAME_REF_LINE, t.getRefLine());
        mapping.put(ProposalLog.COLUMN_NAME_PAYMENT_DATE, t.getPaymentDate());
        mapping.put(ProposalLog.COLUMN_NAME_PAYMENT_NAME, t.getPaymentName());
        mapping.put(ProposalLog.COLUMN_NAME_ACCOUNT_NO_FROM, t.getAccountNoFrom());
        mapping.put(ProposalLog.COLUMN_NAME_ACCOUNT_NO_TO, t.getAccountNoTo());
        mapping.put(ProposalLog.COLUMN_NAME_FILE_TYPE, t.getFileType());
        mapping.put(ProposalLog.COLUMN_NAME_FILE_NAME, t.getFileName());
        mapping.put(ProposalLog.COLUMN_NAME_TRANSFER_DATE, t.getTransferDate());
        mapping.put(ProposalLog.COLUMN_NAME_VENDOR, t.getVendor());
        mapping.put(ProposalLog.COLUMN_NAME_BANK_KEY, t.getBankKey());
        mapping.put(ProposalLog.COLUMN_NAME_VENDOR_BANK_ACCOUNT, t.getVendorBankAccount());
        mapping.put(ProposalLog.COLUMN_NAME_TRANSFER_LEVEL, t.getTransferLevel());
        mapping.put(ProposalLog.COLUMN_NAME_PAY_ACCOUNT, t.getPayAccount());
        mapping.put(ProposalLog.COLUMN_NAME_PAYING_COMP_CODE, t.getPayingCompCode());
        mapping.put(ProposalLog.COLUMN_NAME_PAYMENT_DOCUMENT, t.getPaymentDocument());
        mapping.put(ProposalLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR, t.getPaymentFiscalYear());
        mapping.put(ProposalLog.COLUMN_NAME_REV_PAYMENT_DOCUMENT, t.getRevPaymentDocument());
        mapping.put(ProposalLog.COLUMN_NAME_REV_PAYMENT_FISCAL_YEAR, t.getRevPaymentFiscalYear());
        mapping.put(ProposalLog.COLUMN_NAME_PAYMENT_COMP_CODE, t.getPaymentCompCode());
        mapping.put(ProposalLog.COLUMN_NAME_FISCAL_YEAR, t.getFiscalYear());
        mapping.put(ProposalLog.COLUMN_NAME_FI_AREA, t.getFiArea());
        mapping.put(ProposalLog.COLUMN_NAME_AMOUNT, t.getAmount());
        mapping.put(ProposalLog.COLUMN_NAME_BANK_FEE, t.getBankFee());
        mapping.put(ProposalLog.COLUMN_NAME_CREDIT_MEMO_AMOUNT, t.getCreditMemoAmount());
        mapping.put(ProposalLog.COLUMN_NAME_CANCEL_DATE, t.getCancelDate());
        mapping.put(ProposalLog.COLUMN_NAME_IS_RERUN, t.isRerun());
        mapping.put(ProposalLog.COLUMN_NAME_INV_COMP_CODE, t.getInvCompCode());
        mapping.put(ProposalLog.COLUMN_NAME_INV_DOC_NO, t.getInvDocNo());
        mapping.put(ProposalLog.COLUMN_NAME_INV_FISCAL_YEAR, t.getInvFiscalYear());
        mapping.put(ProposalLog.COLUMN_NAME_INV_DOC_TYPE, t.getInvDocType());
        mapping.put(ProposalLog.COLUMN_NAME_REV_INV_DOC_NO, t.getRevInvDocNo());
        mapping.put(ProposalLog.COLUMN_NAME_REV_INV_FISCAL_YEAR, t.getRevInvFiscalYear());
        mapping.put(ProposalLog.COLUMN_NAME_ORIGINAL_COMP_CODE, t.getOriginalCompCode());
        mapping.put(ProposalLog.COLUMN_NAME_ORIGINAL_DOC_NO, t.getOriginalDocNo());
        mapping.put(ProposalLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
        mapping.put(ProposalLog.COLUMN_NAME_ORIGINAL_DOC_TYPE, t.getOriginalDocType());
        mapping.put(ProposalLog.COLUMN_NAME_REV_ORIGINAL_DOC_NO, t.getRevOriginalDocNo());
        mapping.put(ProposalLog.COLUMN_NAME_REV_ORIGINAL_FISCAL_YEAR, t.getRevOriginalFiscalYear());
        mapping.put(ProposalLog.COLUMN_NAME_REV_ORIGINAL_REASON, t.getRevOriginalReason());
        mapping.put(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT, t.getOriginalWtxAmount());
        mapping.put(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_BASE, t.getOriginalWtxBase());
        mapping.put(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P, t.getOriginalWtxAmountP());
        mapping.put(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_BASE_P, t.getOriginalWtxBaseP());
        mapping.put(ProposalLog.COLUMN_NAME_FILE_STATUS, t.getFileStatus());
        mapping.put(ProposalLog.COLUMN_NAME_SEND_STATUS, t.getSendStatus());
        mapping.put(ProposalLog.COLUMN_NAME_IS_JU_CREATE, t.isJuCreate());
        mapping.put(ProposalLog.COLUMN_NAME_INV_WTX_AMOUNT, t.getInvWtxAmount());
        mapping.put(ProposalLog.COLUMN_NAME_INV_WTX_BASE, t.getInvWtxBase());
        mapping.put(ProposalLog.COLUMN_NAME_INV_WTX_AMOUNT_P, t.getInvWtxAmountP());
        mapping.put(ProposalLog.COLUMN_NAME_INV_WTX_BASE_P, t.getInvWtxBaseP());
        mapping.put(ProposalLog.COLUMN_NAME_PAYMENT_TYPE, t.getPaymentType());
        mapping.put(ProposalLog.COLUMN_NAME_RETURN_DATE, t.getReturnDate());
        mapping.put(ProposalLog.COLUMN_NAME_RETURN_BY, t.getReturnBy());
        mapping.put(ProposalLog.COLUMN_NAME_REF_RUNNING_SUM, t.getRefRunningSum());
        mapping.put(ProposalLog.COLUMN_NAME_REF_LINE_SUM, t.getRefLineSum());
        mapping.put(ProposalLog.COLUMN_NAME_PROPOSAL_LOG_HEADER_ID, t.getProposalLogHeaderId());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(ProposalLog.COLUMN_NAME_PROPOSAL_LOG_ID, Types.BIGINT),
            entry(ProposalLog.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(ProposalLog.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
            entry(ProposalLog.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_REF_RUNNING, Types.BIGINT),
            entry(ProposalLog.COLUMN_NAME_REF_LINE, Types.INTEGER),
            entry(ProposalLog.COLUMN_NAME_PAYMENT_DATE, Types.TIMESTAMP),
            entry(ProposalLog.COLUMN_NAME_PAYMENT_NAME, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_ACCOUNT_NO_FROM, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_ACCOUNT_NO_TO, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_FILE_TYPE, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_FILE_NAME, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_TRANSFER_DATE, Types.TIMESTAMP),
            entry(ProposalLog.COLUMN_NAME_VENDOR, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_BANK_KEY, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_VENDOR_BANK_ACCOUNT, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_TRANSFER_LEVEL, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_PAY_ACCOUNT, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_PAYING_COMP_CODE, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_PAYMENT_DOCUMENT, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_REV_PAYMENT_DOCUMENT, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_REV_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_PAYMENT_COMP_CODE, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_FISCAL_YEAR, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_FI_AREA, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_AMOUNT, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_BANK_FEE, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_CREDIT_MEMO_AMOUNT, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_CANCEL_DATE, Types.TIMESTAMP),
            entry(ProposalLog.COLUMN_NAME_IS_RERUN, Types.BOOLEAN),
            entry(ProposalLog.COLUMN_NAME_INV_COMP_CODE, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_INV_DOC_NO, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_INV_FISCAL_YEAR, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_INV_DOC_TYPE, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_REV_INV_DOC_NO, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_REV_INV_FISCAL_YEAR, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_ORIGINAL_COMP_CODE, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_ORIGINAL_DOC_NO, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_ORIGINAL_DOC_TYPE, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_REV_ORIGINAL_DOC_NO, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_REV_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_REV_ORIGINAL_REASON, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_BASE, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_BASE_P, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_FILE_STATUS, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_SEND_STATUS, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_IS_JU_CREATE, Types.BOOLEAN),
            entry(ProposalLog.COLUMN_NAME_INV_WTX_AMOUNT, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_INV_WTX_BASE, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_INV_WTX_AMOUNT_P, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_INV_WTX_BASE_P, Types.NUMERIC),
            entry(ProposalLog.COLUMN_NAME_PAYMENT_TYPE, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_RETURN_DATE, Types.TIMESTAMP),
            entry(ProposalLog.COLUMN_NAME_RETURN_BY, Types.NVARCHAR),
            entry(ProposalLog.COLUMN_NAME_REF_RUNNING_SUM, Types.BIGINT),
            entry(ProposalLog.COLUMN_NAME_REF_LINE_SUM, Types.INTEGER),
            entry(ProposalLog.COLUMN_NAME_PROPOSAL_LOG_HEADER_ID, Types.BIGINT)
    );

    static RowMapper<ProposalLog> userRowMapper = (rs, rowNum) -> new ProposalLog(
            rs.getLong(ProposalLog.COLUMN_NAME_PROPOSAL_LOG_ID),
            rs.getTimestamp(ProposalLog.COLUMN_NAME_CREATED),
            rs.getString(ProposalLog.COLUMN_NAME_CREATED_BY),
            rs.getTimestamp(ProposalLog.COLUMN_NAME_UPDATED),
            rs.getString(ProposalLog.COLUMN_NAME_UPDATED_BY),
            rs.getLong(ProposalLog.COLUMN_NAME_REF_RUNNING),
            rs.getInt(ProposalLog.COLUMN_NAME_REF_LINE),
            rs.getTimestamp(ProposalLog.COLUMN_NAME_PAYMENT_DATE),
            rs.getString(ProposalLog.COLUMN_NAME_PAYMENT_NAME),
            rs.getString(ProposalLog.COLUMN_NAME_ACCOUNT_NO_FROM),
            rs.getString(ProposalLog.COLUMN_NAME_ACCOUNT_NO_TO),
            rs.getString(ProposalLog.COLUMN_NAME_FILE_TYPE),
            rs.getString(ProposalLog.COLUMN_NAME_FILE_NAME),
            rs.getTimestamp(ProposalLog.COLUMN_NAME_TRANSFER_DATE),
            rs.getString(ProposalLog.COLUMN_NAME_VENDOR),
            rs.getString(ProposalLog.COLUMN_NAME_BANK_KEY),
            rs.getString(ProposalLog.COLUMN_NAME_VENDOR_BANK_ACCOUNT),
            rs.getString(ProposalLog.COLUMN_NAME_TRANSFER_LEVEL),
            rs.getString(ProposalLog.COLUMN_NAME_PAY_ACCOUNT),
            rs.getString(ProposalLog.COLUMN_NAME_PAYING_COMP_CODE),
            rs.getString(ProposalLog.COLUMN_NAME_PAYMENT_DOCUMENT),
            rs.getString(ProposalLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR),
            rs.getString(ProposalLog.COLUMN_NAME_REV_PAYMENT_DOCUMENT),
            rs.getString(ProposalLog.COLUMN_NAME_REV_PAYMENT_FISCAL_YEAR),
            rs.getString(ProposalLog.COLUMN_NAME_PAYMENT_COMP_CODE),
            rs.getString(ProposalLog.COLUMN_NAME_FISCAL_YEAR),
            rs.getString(ProposalLog.COLUMN_NAME_FI_AREA),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_AMOUNT),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_BANK_FEE),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_CREDIT_MEMO_AMOUNT),
            rs.getTimestamp(ProposalLog.COLUMN_NAME_CANCEL_DATE),
            rs.getBoolean(ProposalLog.COLUMN_NAME_IS_RERUN),
            rs.getString(ProposalLog.COLUMN_NAME_INV_COMP_CODE),
            rs.getString(ProposalLog.COLUMN_NAME_INV_DOC_NO),
            rs.getString(ProposalLog.COLUMN_NAME_INV_FISCAL_YEAR),
            rs.getString(ProposalLog.COLUMN_NAME_INV_DOC_TYPE),
            rs.getString(ProposalLog.COLUMN_NAME_REV_INV_DOC_NO),
            rs.getString(ProposalLog.COLUMN_NAME_REV_INV_FISCAL_YEAR),
            rs.getString(ProposalLog.COLUMN_NAME_ORIGINAL_COMP_CODE),
            rs.getString(ProposalLog.COLUMN_NAME_ORIGINAL_DOC_NO),
            rs.getString(ProposalLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
            rs.getString(ProposalLog.COLUMN_NAME_ORIGINAL_DOC_TYPE),
            rs.getString(ProposalLog.COLUMN_NAME_REV_ORIGINAL_DOC_NO),
            rs.getString(ProposalLog.COLUMN_NAME_REV_ORIGINAL_FISCAL_YEAR),
            rs.getString(ProposalLog.COLUMN_NAME_REV_ORIGINAL_REASON),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_BASE),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_ORIGINAL_WTX_BASE_P),
            rs.getString(ProposalLog.COLUMN_NAME_FILE_STATUS),
            rs.getString(ProposalLog.COLUMN_NAME_SEND_STATUS),
            rs.getBoolean(ProposalLog.COLUMN_NAME_IS_JU_CREATE),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_INV_WTX_AMOUNT),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_INV_WTX_BASE),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_INV_WTX_AMOUNT_P),
            rs.getBigDecimal(ProposalLog.COLUMN_NAME_INV_WTX_BASE_P),
            rs.getString(ProposalLog.COLUMN_NAME_PAYMENT_TYPE),
            rs.getTimestamp(ProposalLog.COLUMN_NAME_RETURN_DATE),
            rs.getString(ProposalLog.COLUMN_NAME_RETURN_BY),
            rs.getLong(ProposalLog.COLUMN_NAME_REF_RUNNING_SUM),
            rs.getInt(ProposalLog.COLUMN_NAME_REF_LINE_SUM),
            rs.getLong(ProposalLog.COLUMN_NAME_PROPOSAL_LOG_HEADER_ID)
    );

    public ProposalLogRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, ProposalLogUpdater, updaterType, ProposalLog.TABLE_NAME, ProposalLog.COLUMN_NAME_PROPOSAL_LOG_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    /* GENERATE JU */
    @Override
    public List<ProposalLog> selectProposalLogByGenerateJu(GenerateJuRequest request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM PROPOSAL_LOG ");
        sql.append(" WHERE TRANSFER_LEVEL = 1 ");
        sql.append(" AND IS_JU_CREATE = 0 ");
        sql.append(" AND FILE_STATUS IS NOT NULL ");
        sql.append(" AND SEND_STATUS = 'S'  ");
        
        if (!Util.isEmpty(request.getPaymentDate())) {
            sql.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            sql.append(SqlUtil.dynamicCondition(request.getPaymentName(), "PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(request.getTransferDate())) {
            params.add(Util.timestampToString(request.getTransferDate()));
            sql.append(" AND TO_CHAR(TRANSFER_DATE,'YYYY-MM-DD') = ? " );
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql);
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public void updateJuCreate(GenerateJuRequest request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE ");
        sql.append(" PROPOSAL_LOG ");
        sql.append(" SET IS_JU_CREATE = 1 ");
        sql.append(" WHERE TRANSFER_LEVEL = 1 ");
        sql.append(" AND IS_JU_CREATE = 0 ");
        if (!Util.isEmpty(request.getPaymentDate())) {
            sql.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            sql.append(SqlUtil.dynamicCondition(request.getPaymentName(), "PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(request.getTransferDate())) {
            params.add(Util.timestampToString(request.getTransferDate()));
            sql.append(" AND TO_CHAR(TRANSFER_DATE,'YYYY-MM-DD') = ? " );
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql);
        log.info("params : {} " , params);
        this.jdbcTemplate.update(sql.toString(), objParams);
    }

    @Override
    public ProposalLog findOneByFileNameAndPaymentDocument(String fileName, String paymentDocument) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND IS_RERUN = 0 ");
        if (!Util.isEmpty(fileName)) {
            sql.append(SqlUtil.whereClause(fileName, "FILE_NAME", params));
        }
        if (!Util.isEmpty(paymentDocument)) {
            sql.append(SqlUtil.whereClause(paymentDocument, "PAYMENT_DOCUMENT", params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        List<ProposalLog> proposalLogs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!proposalLogs.isEmpty()) {
            return proposalLogs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public ProposalLog findOneByOriginalDocNoAndOriginalCompCodeAndOriginalFiscalYear(String originalDocNo, String originalCompCode, String originalFiscalYear) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1 = 1 AND IS_RERUN = 0 AND SEND_STATUS = 'S' ");
        if (!Util.isEmpty(originalDocNo)) {
            sql.append(SqlUtil.whereClause(originalDocNo, "ORIGINAL_DOC_NO", params));
        }
        if (!Util.isEmpty(originalCompCode)) {
            sql.append(SqlUtil.whereClause(originalCompCode, "ORIGINAL_COMP_CODE", params));
        }
        if (!Util.isEmpty(originalFiscalYear)) {
            sql.append(SqlUtil.whereClause(originalFiscalYear, "ORIGINAL_FISCAL_YEAR", params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        List<ProposalLog> proposalLogs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!proposalLogs.isEmpty()) {
            return proposalLogs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean isExist(Timestamp paymentDate, String paymentName) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(paymentDate)) {
          sql.append(SqlUtil.whereClauseEqual(paymentDate, "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(paymentName)) {
          sql.append(SqlUtil.whereClause(paymentName, "PAYMENT_NAME", params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        List<ProposalLog> proposalLogs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        return !proposalLogs.isEmpty();
    }

    @Override
    public boolean isFileExist(Timestamp paymentDate, String paymentName, String fileName) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND IS_RERUN = 0 ");
        if (!Util.isEmpty(paymentDate)) {
            sql.append(SqlUtil.whereClauseEqual(paymentDate, "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(paymentName)) {
            sql.append(SqlUtil.whereClause(paymentName, "PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(fileName)) {
            sql.append(SqlUtil.whereClause(fileName, "FILE_NAME", params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        List<ProposalLog> proposalLogs =
            this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        return !proposalLogs.isEmpty();
    }

    @Override
    public ProposalLog findOneFileName(Timestamp paymentDate, String paymentName, String fileName) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND IS_RERUN = 0 ");
        if (!Util.isEmpty(paymentDate)) {
            sql.append(SqlUtil.whereClauseEqual(paymentDate, "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(paymentName)) {
            sql.append(SqlUtil.whereClause(paymentName, "PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(fileName)) {
            sql.append(SqlUtil.whereClause(fileName, "FILE_NAME", params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        List<ProposalLog> proposalLogs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!proposalLogs.isEmpty()) {
            return proposalLogs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public ProposalLog findOneFileNameLevel1(Timestamp paymentDate, String paymentName) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND IS_RERUN = 0 AND TRANSFER_LEVEL = 1 ");
        if (!Util.isEmpty(paymentDate)) {
            sql.append(SqlUtil.whereClauseEqual(paymentDate, "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(paymentName)) {
            sql.append(SqlUtil.whereClause(paymentName, "PAYMENT_NAME", params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql);
        log.info("params : {} ", params);
        List<ProposalLog> proposalLogs = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!proposalLogs.isEmpty()) {
            return proposalLogs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<ProposalLog> findGroupFileName(Timestamp paymentDate, String paymentName) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" FILE_NAME ");
        sql.append(" FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND IS_RERUN = 0 AND TRANSFER_LEVEL = 9 AND VENDOR NOT IN ('BOT', '0000000000') ");
        if (!Util.isEmpty(paymentDate)) {
            sql.append(SqlUtil.whereClauseEqual(paymentDate, "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(paymentName)) {
            sql.append(SqlUtil.whereClause(paymentName, "PAYMENT_NAME", params));
        }
        sql.append(" GROUP BY FILE_NAME ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    /* RETURN */
    @Override
    public List<ReturnProposalLogResponse> findAllProposalLogReturn(ReturnSearchProposalLog request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ID ");
        sb.append("     , FILE_STATUS ");
        sb.append("     , REF_RUNNING ");
        sb.append("     , REF_LINE ");
        sb.append("     , TO_CHAR(PAYMENT_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS PAYMENT_DATE ");
        sb.append("     , PAYMENT_NAME ");
        sb.append("     , ACCOUNT_NO_FROM ");
        sb.append("     , ACCOUNT_NO_TO ");
        sb.append("     , FILE_TYPE ");
        sb.append("     , FILE_NAME ");
        sb.append("     , TO_CHAR(TRANSFER_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS TRANSFER_DATE ");
        sb.append("     , VENDOR ");
        sb.append("     , BANK_KEY ");
        sb.append("     , VENDOR_BANK_ACCOUNT ");
        sb.append("     , TRANSFER_LEVEL ");
        sb.append("     , PAY_ACCOUNT ");
        sb.append("     , PAYMENT_COMP_CODE ");
        sb.append("     , PAYMENT_DOCUMENT ");
        sb.append("     , AMOUNT ");
        sb.append(" FROM PROPOSAL_LOG ");
        sb.append(" WHERE 1 = 1 ");
//        sb.append(" AND VENDOR NOT IN ('BOT' , '0000000000') ");
        sb.append(" AND IS_RERUN = 0 ");
//        sb.append(" AND SEND_STATUS = 'S' ");
        if (!Util.isEmpty(request.getPaymentName())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentName(), "PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(request.getPaymentDate())) {
            sb.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getTransferDate())) {
            sb.append(SqlUtil.dynamicDateCondition(request.getTransferDate(), "TRANSFER_DATE", params));
        }
        if (!Util.isEmpty(request.getAccountNoFrom())) {
            sb.append(SqlUtil.dynamicCondition(request.getAccountNoFrom(), "ACCOUNT_NO_FROM", params));
        }
        if (!Util.isEmpty(request.getAccountNoTo())) {
            sb.append(SqlUtil.dynamicCondition(request.getAccountNoTo(), "ACCOUNT_NO_TO", params));
        }
        if (!Util.isEmpty(request.getVendor())) {
            sb.append(SqlUtil.dynamicCondition(request.getVendor(), "VENDOR", params));
        }
        if (!Util.isEmpty(request.getVendorFrom())) {
            sb.append(SqlUtil.dynamicCondition(request.getVendorFrom(), "VENDOR", params));
        }
        if (!Util.isEmpty(request.getVendorTo())) {
            sb.append(SqlUtil.dynamicCondition(request.getVendorTo(), "VENDOR", params));
        }
        if (!Util.isEmpty(request.getBankKey())) {
            sb.append(SqlUtil.dynamicCondition(request.getBankKey(), "BANK_KEY", params));
        }
        if (!Util.isEmpty(request.getVendorBankAccount())) {
            sb.append(SqlUtil.dynamicCondition(request.getVendorBankAccount(), "VENDOR_BANK_ACCOUNT", params));
        }
        if (!Util.isEmpty(request.getTransferLevel())) {
            sb.append(SqlUtil.dynamicCondition(request.getTransferLevel(), "TRANSFER_LEVEL", params));
        }
        if (!Util.isEmpty(request.getPayAccount())) {
            sb.append(SqlUtil.dynamicCondition(request.getPayAccount(), "PAY_ACCOUNT", params));
        }
        if (!Util.isEmpty(request.getPaymentCompCode())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentCompCode(), "PAYMENT_COMP_CODE", params));
        }
        if (!Util.isEmpty(request.getPaymentDocument())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentDocument(), "PAYMENT_DOCUMENT", params));
        }
        if (!Util.isEmpty(request.getRefLine())) {
            sb.append(SqlUtil.dynamicCondition(request.getRefLine(), "REF_LINE", params));
        }
        if (!Util.isEmpty(request.getRefRunning())) {
            sb.append(SqlUtil.dynamicCondition(request.getRefRunning(), "REF_RUNNING", params));
        }
        if (!Util.isEmpty(request.getFileName())) {
            sb.append(SqlUtil.dynamicCondition(request.getFileName(), "FILE_NAME", params));
        }

        List<String> fileTypeList = new ArrayList<String>();
        if (request.isFileTypeGiro()) {
            fileTypeList.add("GIRO");
        }
        if (request.isFileTypeInHouse()) {
            fileTypeList.add("INHOU");
        }
        if (request.isFileTypeSmart()) {
            fileTypeList.add("SMART");
        }
        if (request.isFileTypeSwift()) {
            fileTypeList.add("SWIFT");
        }
        if (fileTypeList.size() > 0) {
            sb.append(" AND ( ");
            int index = 0;
            for (String item : fileTypeList) {
                if (0 != index) {
                    sb.append(" OR ");
                }
                sb.append(" FILE_TYPE LIKE '").append(item).append("' ");
                index++;
            }
            sb.append(" ) ");
        }

        List<String> fileStatusList = new ArrayList<String>();
        if (request.isFileStatusComplete()) {
            fileStatusList.add("C");
        }
        if (request.isFileStatusReturn()) {
            fileStatusList.add("R");
        }
        if (request.isFileStatusNotSet()) {
            fileStatusList.add("NULL");
        }
        if (fileStatusList.size() > 0) {
            sb.append(" AND ( ");
            int indexStatus = 0;
            for (String item : fileStatusList) {
                if (0 != indexStatus) {
                    sb.append(" OR ");
                }
                if ("NULL".equalsIgnoreCase(item)) {
                    sb.append(" FILE_STATUS IS NULL ");
                } else {
                    sb.append(" FILE_STATUS LIKE '").append(item).append("' ");
                }
                indexStatus++;
            }
            sb.append(" ) ");
        }

        sb.append(" AND (SEND_STATUS = 'S') ");
        sb.append(" ORDER BY REF_LINE ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("params : {} {} ", params, params.size());
        log.info("objParams : {} ", objParams);
        log.info("query : {} ", sb.toString());
        this.jdbcTemplate.setFetchSize(5000);
        return this.jdbcTemplate.query(sb.toString(), objParams, returnProposalLogRowMapper);
    }

    @Override
    public List<ReturnReversePaymentResponse> findAllProposalLogReverseDocumentReturn(ReturnSearchProposalLog request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ID ");
        sb.append(" , CASE WHEN REV_PAYMENT_DOCUMENT IS NOT NULL THEN 1 ELSE 0 END AS REV_PAYMENT_STATUS ");
        sb.append(" , PAYMENT_COMP_CODE ");
        sb.append(" , PAYMENT_DOCUMENT ");
        sb.append(" , PAYMENT_FISCAL_YEAR ");
        sb.append(" , TO_CHAR(PAYMENT_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS PAYMENT_DATE ");
        sb.append(" , PAYMENT_NAME ");
        sb.append(" , VENDOR ");
        sb.append(" , ORIGINAL_COMP_CODE ");
        sb.append(" , ORIGINAL_DOC_NO ");
        sb.append(" , REF_LINE ");
        sb.append(" , ORIGINAL_FISCAL_YEAR ");
        sb.append(" , TO_CHAR(TRANSFER_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS TRANSFER_DATE  ");
        sb.append(" , FILE_NAME ");
        sb.append(" from PROPOSAL_LOG ");
        sb.append(" WHERE 1 = 1 ");
        sb.append(" AND FILE_STATUS = 'R' ");
//        sb.append(" AND VENDOR NOT IN ('BOT' , '0000000000') ");
//        sb.append(" AND REV_PAYMENT_DOCUMENT IS NULL ");
        if (!Util.isEmpty(request.getPaymentCompCode())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentCompCode(), "PAYMENT_COMP_CODE", params));
        }
        if (!Util.isEmpty(request.getPaymentDocument())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentDocument(), "PAYMENT_DOCUMENT", params));
        }
        if (!Util.isEmpty(request.getPaymentFiscalYear())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentFiscalYear(), "PAYMENT_FISCAL_YEAR", params));
        }
        if (!Util.isEmpty(request.getPaymentDate())) {
            sb.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentName(), "PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(request.getVendor())) {
            sb.append(SqlUtil.dynamicCondition(request.getVendor(), "VENDOR", params));
        }
        if (!Util.isEmpty(request.getOriginalCompCode())) {
            sb.append(SqlUtil.dynamicCondition(request.getOriginalCompCode(), "ORIGINAL_COMP_CODE", params));
        }
        if (!Util.isEmpty(request.getOriginalDocNo())) {
            sb.append(SqlUtil.dynamicCondition(request.getOriginalDocNo(), "ORIGINAL_DOC_NO", params));
        }
        if (!Util.isEmpty(request.getRefLine())) {
            sb.append(SqlUtil.dynamicCondition(request.getRefLine(), "REF_LINE", params));
        }
        if (!Util.isEmpty(request.getOriginalFiscalYear())) {
            sb.append(SqlUtil.dynamicCondition(request.getOriginalFiscalYear(), "ORIGINAL_FISCAL_YEAR", params));
        }
        if (!Util.isEmpty(request.getTransferDate())) {
            sb.append(SqlUtil.dynamicDateCondition(request.getTransferDate(), "TRANSFER_DATE", params));
        }
        if (!Util.isEmpty(request.getFileName())) {
            sb.append(SqlUtil.dynamicCondition(request.getFileName(), "FILE_NAME", params));
        }
        sb.append(" ORDER BY REF_LINE ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("params : {} {} ", params, params.size());
        log.info("objParams : {} ", objParams);
        log.info("query : {} ", sb.toString());
        return this.jdbcTemplate.query(sb.toString(), objParams, returnReversePaymentRowMapper);
    }

    @Override
    public List<ReturnReverseInvoiceResponse> findAllProposalLogReverseInvoiceReturn(ReturnSearchProposalLog request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ID ");
        sb.append(" , CASE WHEN REV_INV_DOC_NO IS NOT NULL THEN 1 ELSE 0 END AS REV_INVOICE_STATUS ");
        sb.append(" , NULL AS REV_INVOICE_REASON");
        sb.append(" , ORIGINAL_COMP_CODE ");
        sb.append(" , ORIGINAL_DOC_NO ");
        sb.append(" , REF_LINE ");
        sb.append(" , ORIGINAL_FISCAL_YEAR ");
        sb.append(" , TO_CHAR(PAYMENT_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS PAYMENT_DATE ");
        sb.append(" , PAYMENT_NAME ");
        sb.append(" , VENDOR ");
        sb.append(" , PAYING_COMP_CODE ");
        sb.append(" , PAYMENT_DOCUMENT ");
        sb.append(" , PAYMENT_FISCAL_YEAR ");
        sb.append(" , TO_CHAR(TRANSFER_DATE,'DD.MM.YYYY', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') AS TRANSFER_DATE  ");
        sb.append(" , FILE_NAME ");
        sb.append(" from PROPOSAL_LOG ");
        sb.append(" WHERE 1 = 1 ");
        sb.append(" AND FILE_STATUS = 'R' ");
//        sb.append(" AND VENDOR NOT IN ('BOT' , '0000000000') ");
        sb.append(" AND REV_PAYMENT_DOCUMENT IS NOT NULL ");
        sb.append(" AND REV_ORIGINAL_DOC_NO IS NULL ");
        if (!Util.isEmpty(request.getOriginalCompCode())) {
            sb.append(SqlUtil.dynamicCondition(request.getOriginalCompCode(), "ORIGINAL_COMP_CODE", params));
        }
        if (!Util.isEmpty(request.getOriginalDocNo())) {
            sb.append(SqlUtil.dynamicCondition(request.getOriginalDocNo(), "ORIGINAL_DOC_NO", params));
        }
        if (!Util.isEmpty(request.getRefLine())) {
            sb.append(SqlUtil.dynamicCondition(request.getRefLine(), "REF_LINE", params));
        }
        if (!Util.isEmpty(request.getOriginalFiscalYear())) {
            sb.append(SqlUtil.dynamicCondition(request.getOriginalFiscalYear(), "ORIGINAL_FISCAL_YEAR", params));
        }
        if (!Util.isEmpty(request.getPaymentDate())) {
            sb.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentName(), "PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(request.getVendor())) {
            sb.append(SqlUtil.dynamicCondition(request.getVendor(), "VENDOR", params));
        }
        if (!Util.isEmpty(request.getPayingCompCode())) {
            sb.append(SqlUtil.dynamicCondition(request.getPayingCompCode(), "PAYING_COMP_CODE", params));
        }
        if (!Util.isEmpty(request.getPaymentDocument())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentDocument(), "PAYMENT_DOCUMENT", params));
        }
        if (!Util.isEmpty(request.getPaymentFiscalYear())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentFiscalYear(), "PAYMENT_FISCAL_YEAR", params));
        }
        if (!Util.isEmpty(request.getTransferDate())) {
            sb.append(SqlUtil.dynamicDateCondition(request.getTransferDate(), "TRANSFER_DATE", params));
        }
        if (!Util.isEmpty(request.getFileName())) {
            sb.append(SqlUtil.dynamicCondition(request.getFileName(), "FILE_NAME", params));
        }
        sb.append(" ORDER BY REF_LINE ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("params : {} {} ", params, params.size());
        log.info("objParams : {} ", objParams);
        log.info("query : {} ", sb.toString());
        return this.jdbcTemplate.query(sb.toString(), objParams, returnReverseInvoiceRowMapper);
    }

    @Override
    public ProposalLog findOneByRefRunningAndFileTypeAndTransferLevel(Long refRunning, String fileType, String transferLevel) {
        List<Object> params = new ArrayList<>();
        params.add(refRunning);
        params.add(fileType);
        params.add(transferLevel);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + ProposalLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(ProposalLog.COLUMN_NAME_REF_RUNNING + " = ? ");
        sql.append(" AND " + ProposalLog.COLUMN_NAME_FILE_TYPE + " = ? ");
//        if (transferLevel.equalsIgnoreCase("1")) {
//            sql.append("          AND (TRANSFER_LEVEL = 1 OR TRANSFER_LEVEL = 2 OR (TRANSFER_LEVEL = 9 AND VENDOR = '0000000000'))           ");
//        } else {
            sql.append(" AND " + ProposalLog.COLUMN_NAME_TRANSFER_LEVEL + " = ? ");
//            sql.append(" AND " + ProposalLog.COLUMN_NAME_VENDOR + " <> '0000000000' ");
//            params.add(transferLevel);
//        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql.toString());
        List<ProposalLog> proposalLogs =  this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!proposalLogs.isEmpty()) {
            return proposalLogs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public ProposalLog findOneByPaymentDocumentAndPaymentCompCodeAndPaymentFiscalYear(String paymentDocument, String paymentCompanyCode, String paymentFiscalYear) {
        List<Object> params = new ArrayList<>();
        params.add(paymentDocument);
        params.add(paymentCompanyCode);
        params.add(paymentFiscalYear);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + ProposalLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(ProposalLog.COLUMN_NAME_PAYMENT_DOCUMENT + " = ? ");
        sql.append(" AND " + ProposalLog.COLUMN_NAME_PAYMENT_COMP_CODE + " = ? ");
        sql.append(" AND " + ProposalLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR + " = ? ");
        sql.append(" AND " + ProposalLog.COLUMN_NAME_IS_RERUN + " = 0 ");
        sql.append(" AND " + ProposalLog.COLUMN_NAME_SEND_STATUS + " = 'S' ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql.toString());
        List<ProposalLog> proposalLogs =  this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!proposalLogs.isEmpty()) {
            return proposalLogs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public ProposalLog findOneByPaymentDocumentAndCompCodeAndPaymentFiscalYear(String paymentDocument, String companyCode, String paymentFiscalYear) {
        List<Object> params = new ArrayList<>();
        params.add(paymentDocument);
        params.add(companyCode);
        params.add(paymentFiscalYear);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + ProposalLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(ProposalLog.COLUMN_NAME_PAYMENT_DOCUMENT + " = ? ");
        sql.append(" AND " + ProposalLog.COLUMN_NAME_ORIGINAL_COMP_CODE + " = ? ");
        sql.append(" AND " + ProposalLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR + " = ? ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql.toString());
        List<ProposalLog> proposalLogs =  this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!proposalLogs.isEmpty()) {
            return proposalLogs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<ProposalLog> findAllProposalLogByReturnFile(Map<String, Object> paramsReceive) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1=1 ");
        if (null != paramsReceive.get("paymentDate") && !Util.isEmpty(paramsReceive.get("paymentDate").toString())) {
            params.add(paramsReceive.get("paymentDate").toString());
            sql.append(" AND TO_CHAR(PAYMENT_DATE,'DD-MM-YYYY') = ? ");
        }
        if (null != paramsReceive.get("transferDate") && !Util.isEmpty(paramsReceive.get("transferDate").toString())) {
            params.add(paramsReceive.get("transferDate").toString());
            sql.append(" AND TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
        }
        if (null != paramsReceive.get("effectiveDateConvert") && !Util.isEmpty(paramsReceive.get("effectiveDateConvert").toString())) {
            params.add(paramsReceive.get("effectiveDateConvert").toString());
            sql.append(" AND TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
        }
        if (null != paramsReceive.get("paymentName") && !Util.isEmpty(paramsReceive.get("paymentName").toString())) {
            params.add(paramsReceive.get("paymentName").toString());
            sql.append(" AND PAYMENT_NAME = ? ");
        }
        if (null != paramsReceive.get("fileType") && !Util.isEmpty(paramsReceive.get("fileType").toString())) {
            params.add(paramsReceive.get("fileType").toString());
            sql.append(" AND FILE_TYPE = ? ");
        }
        if (null != paramsReceive.get("bankKey") && !Util.isEmpty(paramsReceive.get("bankKey").toString())) {
            params.add(paramsReceive.get("bankKey").toString());
            sql.append(" AND SUBSTR(BANK_KEY,1,3) = ? ");
        }
        if (null != paramsReceive.get("bankAccount") && !Util.isEmpty(paramsReceive.get("bankAccount").toString())) {
            params.add(paramsReceive.get("bankAccount").toString());
            sql.append(" AND VENDOR_BANK_ACCOUNT = ? ");
        }
        if (null != paramsReceive.get("vendorCode") && !Util.isEmpty(paramsReceive.get("vendorCode").toString())) {
            params.add(paramsReceive.get("vendorCode").toString());
            sql.append(" AND VENDOR = ? ");
        }
        if (null != paramsReceive.get("paymentCompCode") && !Util.isEmpty(paramsReceive.get("paymentCompCode").toString())) {
            params.add(paramsReceive.get("paymentCompCode").toString());
            sql.append(" AND PAYMENT_COMP_CODE = ? ");
        }
        if (null != paramsReceive.get("paymentDocument") && !Util.isEmpty(paramsReceive.get("paymentDocument").toString())) {
            params.add(paramsReceive.get("paymentDocument").toString());
            sql.append(" AND PAYMENT_DOCUMENT = ? ");
        }
        if (null != paramsReceive.get("paymentFiscalYear") && !Util.isEmpty(paramsReceive.get("paymentFiscalYear").toString())) {
            params.add(paramsReceive.get("paymentFiscalYear").toString());
            sql.append(" AND PAYMENT_FISCAL_YEAR = ? ");
        }

        if (null != paramsReceive.get("fiArea") && !Util.isEmpty(paramsReceive.get("fiArea").toString())) {
            params.add(paramsReceive.get("fiArea").toString());
            sql.append(" AND FI_AREA = ? ");
        }
        if (null != paramsReceive.get("amount") && !Util.isEmpty(paramsReceive.get("amount").toString())) {
            params.add(paramsReceive.get("amount").toString());
            sql.append(" AND AMOUNT = ? ");
        }

        sql.append(" AND SEND_STATUS = 'S' ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("params : {} {} ", params, params.size());
        log.info("objParams : {} ", objParams);
        log.info("query : {} ", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<ProposalLog> findAllProposalLogByReturnFile(String paymentDate, String paymentName, String effectiveDate) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1=1 AND FILE_TYPE = 'SMART' ");
        if (!Util.isEmpty(paymentDate)) {
            params.add(paymentDate);
            sql.append(" AND TO_CHAR(PAYMENT_DATE,'DD-MM-YYYY') = ? ");
        }
        if (!Util.isEmpty(effectiveDate)) {
            params.add(effectiveDate);
            sql.append(" AND TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
        }
        if (!Util.isEmpty(paymentName)) {
            params.add(paymentName);
            sql.append(" AND PAYMENT_NAME = ? ");
        }
        sql.append(" ORDER BY PAYMENT_DOCUMENT ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("params : {} {} ", params, params.size());
        log.info("objParams : {} ", objParams);
        log.info("query : {} ", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<ProposalLog> findAllProposalLogByReturnFile(String effectiveDate, String fileName) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1=1 AND FILE_TYPE = 'SMART' ");
        if (!Util.isEmpty(effectiveDate)) {
            params.add(effectiveDate);
            sql.append(" AND TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
        }
        if (!Util.isEmpty(fileName)) {
            params.add(fileName);
            sql.append(" AND FILE_NAME = ? ");
        }
        sql.append(" ORDER BY PAYMENT_DOCUMENT ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("params : {} {} ", params, params.size());
        log.info("objParams : {} ", objParams);
        log.info("query : {} ", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<ProposalLog> findAllProposalLogByReturnFileSum(Long refRunning, int refLine) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1=1 ");
//        if (!Util.isEmpty(refRunning)) {
            params.add(refRunning);
            sql.append(" AND REF_RUNNING_SUM = ? ");
//        }
//        if (!Util.isEmpty(refLine)) {
            params.add(refLine);
            sql.append(" AND REF_LINE_SUM = ? ");
//        }
        sql.append(" ORDER BY PAYMENT_DOCUMENT ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("params : {} {} ", params, params.size());
        log.info("objParams : {} ", objParams);
        log.info("query : {} ", sql.toString());
        this.jdbcTemplate.setFetchSize(5000);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public List<ProposalLog> findAllProposalLogByReturnFileRerun(String effectiveDate, String fileName) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM PROPOSAL_LOG ");
        sql.append(" WHERE 1=1 AND FILE_TYPE = 'SMART' AND FILE_STATUS IS NULL ");
        if (!Util.isEmpty(effectiveDate)) {
            params.add(effectiveDate);
            sql.append(" AND TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
        }
        if (!Util.isEmpty(fileName)) {
            params.add(fileName);
            sql.append(" AND FILE_NAME = ? ");
        }
        sql.append(" ORDER BY PAYMENT_DOCUMENT ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("params : {} {} ", params, params.size());
        log.info("objParams : {} ", objParams);
        log.info("query : {} ", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public int updateComplete(String effectiveDate, Timestamp created, String createdBy) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE PROPOSAL_LOG ");
        sb.append(" SET FILE_STATUS = 'C', ");
        sb.append(" SET REGEN_DATE = ?, ");
        sb.append(" SET REGEN_BY = ? ");
        sb.append(" WHERE TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
        sb.append(" AND FILE_TYPE = 'SMART' ");
        sb.append(" AND FILE_STATUS <> 'R' ");
        return this.jdbcTemplate.update(sb.toString(), created, createdBy, effectiveDate);
    }

    @Override
    public int updateComplete(String effectiveDate, String paymentDate, String paymentName) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE PROPOSAL_LOG ");
        sb.append(" SET FILE_STATUS = 'C' ");
        sb.append(" WHERE TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
        sb.append(" AND FILE_TYPE = 'SMART' ");
        sb.append(" AND FILE_STATUS <> 'R' ");
        if (!Util.isEmpty(paymentDate)) {
            sb.append(" AND TO_CHAR(PAYMENT_DATE,'DD-MM-YYYY') = ? ");
        }
        if (!Util.isEmpty(paymentName)) {
            sb.append(" AND PAYMENT_NAME = ? ");
        }
        if (!Util.isEmpty(paymentDate)) {
            return this.jdbcTemplate.update(sb.toString(), effectiveDate, paymentDate, paymentName);
        } else {
            return this.jdbcTemplate.update(sb.toString(), effectiveDate);
        }
    }

    @Override
    public int updateComplete(String effectiveDate, String fileName, Timestamp created, String createdBy) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE PROPOSAL_LOG ");
        sb.append(" SET FILE_STATUS = 'C', ");
        sb.append(" RETURN_DATE = ?, ");
        sb.append(" RETURN_BY = ? ");
        sb.append(" WHERE TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
        sb.append(" AND FILE_TYPE = 'SMART' ");
        sb.append(" AND FILE_STATUS <> 'R' OR FILE_STATUS IS NULL ");
        if (!Util.isEmpty(fileName)) {
            sb.append(" AND FILE_NAME = ? ");
        }

        return this.jdbcTemplate.update(sb.toString(), created, createdBy, effectiveDate, fileName);
    }

    @Override
    public int resetComplete(String effectiveDate, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE PROPOSAL_LOG ");
        sb.append(" SET FILE_STATUS = NULL ");
        sb.append(" WHERE TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
        sb.append(" AND FILE_TYPE = 'SMART' ");
        if (!Util.isEmpty(fileName)) {
            sb.append(" AND FILE_NAME = ? ");
        }

        return this.jdbcTemplate.update(sb.toString(), effectiveDate, fileName);
    }

    @Override
    public void updateRegen(Timestamp paymentDate, String paymentName, String fileType, String fileName) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE ");
        sql.append(" PROPOSAL_LOG ");
        sql.append(" SET SEND_STATUS = 'R', IS_RERUN = 1 ");
        sql.append(" WHERE 1=1 AND TRANSFER_LEVEL = 9 AND VENDOR NOT IN ('0000000000', 'BOT') ");
        if (!Util.isEmpty(paymentDate)) {
            sql.append(SqlUtil.whereClauseEqual(paymentDate, "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(paymentName)) {
            sql.append(SqlUtil.whereClause(paymentName, "PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(fileType)) {
            sql.append(SqlUtil.whereClause(fileType, "FILE_TYPE", params));
        }
        if (!Util.isEmpty(fileName)) {
            sql.append(SqlUtil.whereClause(fileName, "FILE_NAME", params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        int rtn = this.jdbcTemplate.update(sql.toString(), objParams);
        log.info("rtn : {}", rtn);
    }

    @Override
    public void updateRegenLevel1(Timestamp paymentDate, String paymentName, String fileName) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE ");
        sql.append(" PROPOSAL_LOG ");
        sql.append(" SET SEND_STATUS = NULL, IS_RERUN = 1 ");
        sql.append(" WHERE 1=1 AND (((TRANSFER_LEVEL = 1 OR TRANSFER_LEVEL = 2) AND VENDOR = 'BOT') OR (TRANSFER_LEVEL = 9 AND VENDOR = '0000000000'))");
        if (!Util.isEmpty(paymentDate)) {
            sql.append(SqlUtil.whereClauseEqual(paymentDate, "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(paymentName)) {
            sql.append(SqlUtil.whereClause(paymentName, "PAYMENT_NAME", params));
        }
//        if (!Util.isEmpty(fileName)) {
//            sql.append(SqlUtil.whereClause(fileName, "FILE_NAME", params));
//        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        int rtn = this.jdbcTemplate.update(sql.toString(), objParams);
        log.info("rtn : {}", rtn);
    }

    @Override
    public void saveBatch(List<ProposalLog> proposalLogs) {
        final int batchSize = 30000;
        List<List<ProposalLog>> proposalLogBatchs = Lists.partition(proposalLogs, batchSize);
        final String sqlSave = "INSERT /*+ enable_parallel_dml */ INTO PROPOSAL_LOG (ID, ACCOUNT_NO_FROM, ACCOUNT_NO_TO, AMOUNT, BANK_FEE, BANK_KEY, " +
                "CANCEL_DATE, CREATED, CREATED_BY, CREDIT_MEMO_AMOUNT, FI_AREA, FILE_NAME, FILE_STATUS, FILE_TYPE, FISCAL_YEAR, " +
                "INV_COMP_CODE, INV_DOC_NO, INV_FISCAL_YEAR, PAY_ACCOUNT, PAYING_COMP_CODE, PAYMENT_DATE, PAYMENT_DOCUMENT, " +
                "PAYMENT_NAME, REF_LINE, REF_RUNNING, IS_RERUN, SEND_STATUS, TRANSFER_DATE, TRANSFER_LEVEL, UPDATED, UPDATED_BY, " +
                "VENDOR, VENDOR_BANK_ACCOUNT, ORIGINAL_ACC_DOC_NO, ORIGINAL_FISCAL_YEAR, ORIGINAL_COMP_CODE, ORIGINAL_DOC_TYPE, " +
                "ORIGINAL_WTX_AMOUNT, ORIGINAL_WTX_BASE, ORIGINAL_WTX_AMOUNT_P, ORIGINAL_WTX_BASE_P, PAYMENT_FISCAL_YEAR, " +
                "PAYMENT_COMP_CODE, INV_DOC_TYPE, ORIGINAL_DOC_NO, ORIGINAL_YEAR, " +
                "PROPOSAL_LOG_HEADER_ID, REV_INV_DOC_NO, REV_INV_FISCAL_YEAR, REV_ORIGINAL_DOC_NO, REV_ORIGINAL_FISCAL_YEAR, " +
                "REV_PAYMENT_DOCUMENT, REV_PAYMENT_FISCAL_YEAR, REV_ORIGINAL_REASON, IS_JU_CREATE, REF_RUNNING_SUM, REF_LINE_SUM, " +
                "INV_WTX_AMOUNT, INV_WTX_BASE, INV_WTX_AMOUNT_P, INV_WTX_BASE_P, PAYMENT_TYPE, RETURN_DATE, RETURN_BY) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?)";
        for (List<ProposalLog> batch : proposalLogBatchs) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    ProposalLog proposalLog = batch.get(i);
                    int index = 0;
                    ps.setLong(++index, proposalLog.getId());
                    ps.setString(++index, proposalLog.getAccountNoFrom());
                    ps.setString(++index, proposalLog.getAccountNoTo());
                    ps.setBigDecimal(++index, proposalLog.getAmount());
                    ps.setBigDecimal(++index, proposalLog.getBankFee());
                    ps.setString(++index, proposalLog.getBankKey());
                    ps.setTimestamp(++index, proposalLog.getCancelDate());
                    ps.setTimestamp(++index, proposalLog.getCreated());
                    ps.setString(++index, proposalLog.getCreatedBy());
                    ps.setBigDecimal(++index, proposalLog.getCreditMemoAmount());
                    ps.setString(++index, proposalLog.getFiArea());
                    ps.setString(++index, proposalLog.getFileName());
                    ps.setString(++index, proposalLog.getFileStatus());
                    ps.setString(++index, proposalLog.getFileType());
                    ps.setString(++index, proposalLog.getFiscalYear());
                    ps.setString(++index, proposalLog.getInvCompCode());
                    ps.setString(++index, proposalLog.getInvDocNo());
                    ps.setString(++index, proposalLog.getInvFiscalYear());
                    ps.setString(++index, proposalLog.getPayAccount());
                    ps.setString(++index, proposalLog.getPayingCompCode());
                    ps.setTimestamp(++index, proposalLog.getPaymentDate());
                    ps.setString(++index, proposalLog.getPaymentDocument());
                    ps.setString(++index, proposalLog.getPaymentName());
                    ps.setInt(++index, proposalLog.getRefLine());
                    ps.setLong(++index, proposalLog.getRefRunning());
                    ps.setBoolean(++index, proposalLog.isRerun());
                    ps.setString(++index, proposalLog.getSendStatus());
                    ps.setTimestamp(++index, proposalLog.getTransferDate());
                    ps.setString(++index, proposalLog.getTransferLevel());
                    ps.setTimestamp(++index, proposalLog.getUpdated());
                    ps.setString(++index, proposalLog.getUpdatedBy());
                    ps.setString(++index, proposalLog.getVendor());
                    ps.setString(++index, proposalLog.getVendorBankAccount());
                    ps.setString(++index, proposalLog.getOriginalDocNo());
                    ps.setString(++index, proposalLog.getOriginalFiscalYear());
                    ps.setString(++index, proposalLog.getOriginalCompCode());
                    ps.setString(++index, proposalLog.getOriginalDocType());
                    ps.setBigDecimal(++index, proposalLog.getOriginalWtxAmount());
                    ps.setBigDecimal(++index, proposalLog.getOriginalWtxBase());
                    ps.setBigDecimal(++index, proposalLog.getOriginalWtxAmountP());
                    ps.setBigDecimal(++index, proposalLog.getOriginalWtxBaseP());
                    ps.setString(++index, proposalLog.getPaymentFiscalYear());
                    ps.setString(++index, proposalLog.getPaymentCompCode());
                    ps.setString(++index, proposalLog.getInvDocType());
                    ps.setString(++index, proposalLog.getOriginalDocNo());
                    ps.setString(++index, proposalLog.getOriginalFiscalYear());
                    ps.setLong(++index, proposalLog.getProposalLogHeaderId());
                    ps.setString(++index, proposalLog.getRevInvDocNo());
                    ps.setString(++index, proposalLog.getRevInvFiscalYear());
                    ps.setString(++index, proposalLog.getRevOriginalDocNo());
                    ps.setString(++index, proposalLog.getRevOriginalFiscalYear());
                    ps.setString(++index, proposalLog.getRevPaymentDocument());
                    ps.setString(++index, proposalLog.getRevPaymentFiscalYear());
                    ps.setString(++index, proposalLog.getRevOriginalReason());
                    ps.setBoolean(++index, proposalLog.isJuCreate());
                    ps.setLong(++index, proposalLog.getRefRunningSum());
                    ps.setInt(++index, proposalLog.getRefLineSum());
                    ps.setBigDecimal(++index, proposalLog.getInvWtxAmount());
                    ps.setBigDecimal(++index, proposalLog.getInvWtxBase());
                    ps.setBigDecimal(++index, proposalLog.getInvWtxAmountP());
                    ps.setBigDecimal(++index, proposalLog.getInvWtxBaseP());
                    ps.setString(++index, proposalLog.getPaymentType());
                    ps.setTimestamp(++index, proposalLog.getReturnDate());
                    ps.setString(++index, proposalLog.getReturnBy());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public void updateBatch(List<ProposalLog> proposalLogs) {
        final int batchSize = 30000;
        List<List<ProposalLog>> proposalLogsBatches = Lists.partition(proposalLogs, batchSize);
        final String sqlSave = "UPDATE /*+ ENABLE_PARALLEL_DML */ PROPOSAL_LOG SET FILE_STATUS = ?, RETURN_DATE = ?, RETURN_BY = ? WHERE ID = ?";
        for(List<ProposalLog> batch : proposalLogsBatches) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    int index = 0;
                    ProposalLog proposalLog = batch.get(i);
                    ps.setString(++index, proposalLog.getFileStatus());
                    ps.setTimestamp(++index, proposalLog.getReturnDate());
                    ps.setString(++index, proposalLog.getReturnBy());
                    ps.setLong(++index, proposalLog.getId());
                }
                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public void updateStatusBatch(List<ProposalLog> proposalLogs) {
        final int batchSize = 30000;
        List<List<ProposalLog>> proposalLogsBatches = Lists.partition(proposalLogs, batchSize);
        final String sqlSave = "UPDATE /*+ ENABLE_PARALLEL_DML */ PROPOSAL_LOG SET FILE_STATUS = ?, RETURN_DATE = ?, RETURN_BY = ? WHERE REF_RUNNING_SUM = ? AND REF_LINE_SUM = ?";
        for(List<ProposalLog> batch : proposalLogsBatches) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    int index = 0;
                    ProposalLog proposalLog = batch.get(i);
                    ps.setString(++index, proposalLog.getFileStatus());
                    ps.setTimestamp(++index, proposalLog.getReturnDate());
                    ps.setString(++index, proposalLog.getReturnBy());
                    ps.setLong(++index, proposalLog.getRefRunningSum());
                    ps.setInt(++index, proposalLog.getRefLineSum());
                }
                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }


}
