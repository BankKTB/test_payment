package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogSum;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.ProposalLogSumRepository;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogService;
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
public class ProposalLogSumRepositoryImpl extends MetadataJdbcRepository<ProposalLogSum, Long>
    implements ProposalLogSumRepository {

  static BeanPropertyRowMapper<ProposalLogSum> beanPropertyRowMapper = new BeanPropertyRowMapper<>(ProposalLogSum.class);
  private final JdbcTemplate jdbcTemplate;
  static Updater<ProposalLogSum> smartFileLogUpdater =
      (t, mapping) -> {
        mapping.put(ProposalLogSum.COLUMN_NAME_PROPOSAL_LOG_SUM_ID, t.getId());
        mapping.put(ProposalLogSum.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(ProposalLogSum.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(ProposalLogSum.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(ProposalLogSum.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(ProposalLogSum.COLUMN_NAME_REF_RUNNING, t.getRefRunning());
        mapping.put(ProposalLogSum.COLUMN_NAME_REF_LINE, t.getRefLine());
        mapping.put(ProposalLogSum.COLUMN_NAME_PAYMENT_DATE, t.getPaymentDate());
        mapping.put(ProposalLogSum.COLUMN_NAME_PAYMENT_NAME, t.getPaymentName());
        mapping.put(ProposalLogSum.COLUMN_NAME_ACCOUNT_NO_FROM, t.getAccountNoFrom());
        mapping.put(ProposalLogSum.COLUMN_NAME_ACCOUNT_NO_TO, t.getAccountNoTo());
        mapping.put(ProposalLogSum.COLUMN_NAME_FILE_TYPE, t.getFileType());
        mapping.put(ProposalLogSum.COLUMN_NAME_FILE_NAME, t.getFileName());
        mapping.put(ProposalLogSum.COLUMN_NAME_TRANSFER_DATE, t.getTransferDate());
        mapping.put(ProposalLogSum.COLUMN_NAME_VENDOR, t.getVendor());
        mapping.put(ProposalLogSum.COLUMN_NAME_BANK_KEY, t.getBankKey());
        mapping.put(ProposalLogSum.COLUMN_NAME_VENDOR_BANK_ACCOUNT, t.getVendorBankAccount());
        mapping.put(ProposalLogSum.COLUMN_NAME_TRANSFER_LEVEL, t.getTransferLevel());
        mapping.put(ProposalLogSum.COLUMN_NAME_PAY_ACCOUNT, t.getPayAccount());
        mapping.put(ProposalLogSum.COLUMN_NAME_PAYING_COMP_CODE, t.getPayingCompCode());
        mapping.put(ProposalLogSum.COLUMN_NAME_PAYMENT_DOCUMENT, t.getPaymentDocument());
        mapping.put(ProposalLogSum.COLUMN_NAME_PAYMENT_FISCAL_YEAR, t.getPaymentFiscalYear());
        mapping.put(ProposalLogSum.COLUMN_NAME_REV_PAYMENT_DOCUMENT, t.getRevPaymentDocument());
        mapping.put(ProposalLogSum.COLUMN_NAME_REV_PAYMENT_FISCAL_YEAR, t.getRevPaymentFiscalYear());
        mapping.put(ProposalLogSum.COLUMN_NAME_PAYMENT_COMP_CODE, t.getPaymentCompCode());
        mapping.put(ProposalLogSum.COLUMN_NAME_FISCAL_YEAR, t.getFiscalYear());
        mapping.put(ProposalLogSum.COLUMN_NAME_FI_AREA, t.getFiArea());
        mapping.put(ProposalLogSum.COLUMN_NAME_AMOUNT, t.getAmount());
        mapping.put(ProposalLogSum.COLUMN_NAME_BANK_FEE, t.getBankFee());
        mapping.put(ProposalLogSum.COLUMN_NAME_CREDIT_MEMO_AMOUNT, t.getCreditMemoAmount());
        mapping.put(ProposalLogSum.COLUMN_NAME_CANCEL_DATE, t.getCancelDate());
        mapping.put(ProposalLogSum.COLUMN_NAME_IS_RERUN, t.isRerun());
        mapping.put(ProposalLogSum.COLUMN_NAME_INV_COMP_CODE, t.getInvCompCode());
        mapping.put(ProposalLogSum.COLUMN_NAME_INV_DOC_NO, t.getInvDocNo());
        mapping.put(ProposalLogSum.COLUMN_NAME_INV_FISCAL_YEAR, t.getInvFiscalYear());
        mapping.put(ProposalLogSum.COLUMN_NAME_INV_DOC_TYPE, t.getInvDocType());
        mapping.put(ProposalLogSum.COLUMN_NAME_REV_INV_DOC_NO, t.getRevInvDocNo());
        mapping.put(ProposalLogSum.COLUMN_NAME_REV_INV_FISCAL_YEAR, t.getRevInvFiscalYear());
        mapping.put(ProposalLogSum.COLUMN_NAME_ORIGINAL_COMP_CODE, t.getOriginalCompCode());
        mapping.put(ProposalLogSum.COLUMN_NAME_ORIGINAL_DOC_NO, t.getOriginalDocNo());
        mapping.put(ProposalLogSum.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
        mapping.put(ProposalLogSum.COLUMN_NAME_ORIGINAL_DOC_TYPE, t.getOriginalDocType());
        mapping.put(ProposalLogSum.COLUMN_NAME_REV_ORIGINAL_DOC_NO, t.getRevOriginalDocNo());
        mapping.put(ProposalLogSum.COLUMN_NAME_REV_ORIGINAL_FISCAL_YEAR, t.getRevOriginalFiscalYear());
        mapping.put(ProposalLogSum.COLUMN_NAME_REV_ORIGINAL_REASON, t.getRevOriginalReason());
        mapping.put(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_AMOUNT, t.getOriginalWtxAmount());
        mapping.put(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_BASE, t.getOriginalWtxBase());
        mapping.put(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P, t.getOriginalWtxAmountP());
        mapping.put(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_BASE_P, t.getOriginalWtxBaseP());
        mapping.put(ProposalLogSum.COLUMN_NAME_FILE_STATUS, t.getFileStatus());
        mapping.put(ProposalLogSum.COLUMN_NAME_SEND_STATUS, t.getSendStatus());
        mapping.put(ProposalLogSum.COLUMN_NAME_IS_JU_CREATE, t.isJuCreate());
        mapping.put(ProposalLogSum.COLUMN_NAME_INV_WTX_AMOUNT, t.getInvWtxAmount());
        mapping.put(ProposalLogSum.COLUMN_NAME_INV_WTX_BASE, t.getInvWtxBase());
        mapping.put(ProposalLogSum.COLUMN_NAME_INV_WTX_AMOUNT_P, t.getInvWtxAmountP());
        mapping.put(ProposalLogSum.COLUMN_NAME_INV_WTX_BASE_P, t.getInvWtxBaseP());
        mapping.put(ProposalLogSum.COLUMN_NAME_PAYMENT_TYPE, t.getPaymentType());
        mapping.put(ProposalLogSum.COLUMN_NAME_RETURN_DATE, t.getReturnDate());
        mapping.put(ProposalLogSum.COLUMN_NAME_RETURN_BY, t.getReturnBy());
        mapping.put(ProposalLogSum.COLUMN_NAME_PROPOSAL_LOG_HEADER_ID, t.getProposalLogHeaderId());
      };
  static Map<String, Integer> updaterType =
      Map.ofEntries(
          entry(ProposalLogSum.COLUMN_NAME_PROPOSAL_LOG_SUM_ID, Types.BIGINT),
          entry(ProposalLogSum.COLUMN_NAME_CREATED, Types.TIMESTAMP),
          entry(ProposalLogSum.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
          entry(ProposalLogSum.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_REF_RUNNING, Types.BIGINT),
          entry(ProposalLogSum.COLUMN_NAME_REF_LINE, Types.INTEGER),
          entry(ProposalLogSum.COLUMN_NAME_PAYMENT_DATE, Types.TIMESTAMP),
          entry(ProposalLogSum.COLUMN_NAME_PAYMENT_NAME, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_ACCOUNT_NO_FROM, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_ACCOUNT_NO_TO, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_FILE_TYPE, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_FILE_NAME, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_TRANSFER_DATE, Types.TIMESTAMP),
          entry(ProposalLogSum.COLUMN_NAME_VENDOR, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_BANK_KEY, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_VENDOR_BANK_ACCOUNT, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_TRANSFER_LEVEL, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_PAY_ACCOUNT, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_PAYING_COMP_CODE, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_PAYMENT_DOCUMENT, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_REV_PAYMENT_DOCUMENT, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_REV_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_PAYMENT_COMP_CODE, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_FI_AREA, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_AMOUNT, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_BANK_FEE, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_CREDIT_MEMO_AMOUNT, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_CANCEL_DATE, Types.TIMESTAMP),
          entry(ProposalLogSum.COLUMN_NAME_IS_RERUN, Types.BOOLEAN),
          entry(ProposalLogSum.COLUMN_NAME_INV_COMP_CODE, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_INV_DOC_NO, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_INV_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_INV_DOC_TYPE, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_REV_INV_DOC_NO, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_REV_INV_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_ORIGINAL_COMP_CODE, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_ORIGINAL_DOC_NO, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_ORIGINAL_DOC_TYPE, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_REV_ORIGINAL_DOC_NO, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_REV_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_REV_ORIGINAL_REASON, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_AMOUNT, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_BASE, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_BASE_P, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_FILE_STATUS, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_SEND_STATUS, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_IS_JU_CREATE, Types.BOOLEAN),
          entry(ProposalLogSum.COLUMN_NAME_INV_WTX_AMOUNT, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_INV_WTX_BASE, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_INV_WTX_AMOUNT_P, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_INV_WTX_BASE_P, Types.NUMERIC),
          entry(ProposalLogSum.COLUMN_NAME_PAYMENT_TYPE, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_RETURN_DATE, Types.TIMESTAMP),
          entry(ProposalLogSum.COLUMN_NAME_RETURN_BY, Types.NVARCHAR),
          entry(ProposalLogSum.COLUMN_NAME_PROPOSAL_LOG_HEADER_ID, Types.BIGINT));
  private static ProposalLogService proposalLogService;
  static RowMapper<ProposalLogSum> userRowMapper =
      (rs, rowNum) ->
          new ProposalLogSum(
              rs.getLong(ProposalLogSum.COLUMN_NAME_PROPOSAL_LOG_SUM_ID),
              rs.getTimestamp(ProposalLogSum.COLUMN_NAME_CREATED),
              rs.getString(ProposalLogSum.COLUMN_NAME_CREATED_BY),
              rs.getTimestamp(ProposalLogSum.COLUMN_NAME_UPDATED),
              rs.getString(ProposalLogSum.COLUMN_NAME_UPDATED_BY),
              rs.getLong(ProposalLogSum.COLUMN_NAME_REF_RUNNING),
              rs.getInt(ProposalLogSum.COLUMN_NAME_REF_LINE),
              rs.getTimestamp(ProposalLogSum.COLUMN_NAME_PAYMENT_DATE),
              rs.getString(ProposalLogSum.COLUMN_NAME_PAYMENT_NAME),
              rs.getString(ProposalLogSum.COLUMN_NAME_ACCOUNT_NO_FROM),
              rs.getString(ProposalLogSum.COLUMN_NAME_ACCOUNT_NO_TO),
              rs.getString(ProposalLogSum.COLUMN_NAME_FILE_TYPE),
              rs.getString(ProposalLogSum.COLUMN_NAME_FILE_NAME),
              rs.getTimestamp(ProposalLogSum.COLUMN_NAME_TRANSFER_DATE),
              rs.getString(ProposalLogSum.COLUMN_NAME_VENDOR),
              rs.getString(ProposalLogSum.COLUMN_NAME_BANK_KEY),
              rs.getString(ProposalLogSum.COLUMN_NAME_VENDOR_BANK_ACCOUNT),
              rs.getString(ProposalLogSum.COLUMN_NAME_TRANSFER_LEVEL),
              rs.getString(ProposalLogSum.COLUMN_NAME_PAY_ACCOUNT),
              rs.getString(ProposalLogSum.COLUMN_NAME_PAYING_COMP_CODE),
              rs.getString(ProposalLogSum.COLUMN_NAME_PAYMENT_DOCUMENT),
              rs.getString(ProposalLogSum.COLUMN_NAME_PAYMENT_FISCAL_YEAR),
              rs.getString(ProposalLogSum.COLUMN_NAME_REV_PAYMENT_DOCUMENT),
              rs.getString(ProposalLogSum.COLUMN_NAME_REV_PAYMENT_FISCAL_YEAR),
              rs.getString(ProposalLogSum.COLUMN_NAME_PAYMENT_COMP_CODE),
              rs.getString(ProposalLogSum.COLUMN_NAME_FISCAL_YEAR),
              rs.getString(ProposalLogSum.COLUMN_NAME_FI_AREA),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_AMOUNT),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_BANK_FEE),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_CREDIT_MEMO_AMOUNT),
              rs.getTimestamp(ProposalLogSum.COLUMN_NAME_CANCEL_DATE),
              rs.getBoolean(ProposalLogSum.COLUMN_NAME_IS_RERUN),
              rs.getString(ProposalLogSum.COLUMN_NAME_INV_COMP_CODE),
              rs.getString(ProposalLogSum.COLUMN_NAME_INV_DOC_NO),
              rs.getString(ProposalLogSum.COLUMN_NAME_INV_FISCAL_YEAR),
              rs.getString(ProposalLogSum.COLUMN_NAME_INV_DOC_TYPE),
              rs.getString(ProposalLogSum.COLUMN_NAME_REV_INV_DOC_NO),
              rs.getString(ProposalLogSum.COLUMN_NAME_REV_INV_FISCAL_YEAR),
              rs.getString(ProposalLogSum.COLUMN_NAME_ORIGINAL_COMP_CODE),
              rs.getString(ProposalLogSum.COLUMN_NAME_ORIGINAL_DOC_NO),
              rs.getString(ProposalLogSum.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
              rs.getString(ProposalLogSum.COLUMN_NAME_ORIGINAL_DOC_TYPE),
              rs.getString(ProposalLogSum.COLUMN_NAME_REV_ORIGINAL_DOC_NO),
              rs.getString(ProposalLogSum.COLUMN_NAME_REV_ORIGINAL_FISCAL_YEAR),
              rs.getString(ProposalLogSum.COLUMN_NAME_REV_ORIGINAL_REASON),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_AMOUNT),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_BASE),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_AMOUNT_P),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_ORIGINAL_WTX_BASE_P),
              rs.getString(ProposalLogSum.COLUMN_NAME_FILE_STATUS),
              rs.getString(ProposalLogSum.COLUMN_NAME_SEND_STATUS),
              rs.getBoolean(ProposalLogSum.COLUMN_NAME_IS_JU_CREATE),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_INV_WTX_AMOUNT),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_INV_WTX_BASE),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_INV_WTX_AMOUNT_P),
              rs.getBigDecimal(ProposalLogSum.COLUMN_NAME_INV_WTX_BASE_P),
              rs.getString(ProposalLogSum.COLUMN_NAME_PAYMENT_TYPE),
              rs.getTimestamp(ProposalLogSum.COLUMN_NAME_RETURN_DATE),
              rs.getString(ProposalLogSum.COLUMN_NAME_RETURN_BY),
              rs.getLong(ProposalLogSum.COLUMN_NAME_PROPOSAL_LOG_HEADER_ID));

  public ProposalLogSumRepositoryImpl(
      @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, ProposalLogService proposalLogService) {
    super(
        userRowMapper,
        smartFileLogUpdater,
        updaterType,
        ProposalLogSum.TABLE_NAME,
        ProposalLogSum.COLUMN_NAME_PROPOSAL_LOG_SUM_ID,
        jdbcTemplate);
    this.jdbcTemplate = jdbcTemplate;
    ProposalLogSumRepositoryImpl.proposalLogService = proposalLogService;
  }

  @Override
  public void saveBatch(List<ProposalLogSum> proposalLogSums) {
    final int batchSize = 30000;
    List<List<ProposalLogSum>> proposalLogSumBatchs = Lists.partition(proposalLogSums, batchSize);
    final String sqlSave = "INSERT /*+ enable_parallel_dml */ INTO PROPOSAL_LOG_SUM (ID, ACCOUNT_NO_FROM, ACCOUNT_NO_TO, AMOUNT, BANK_FEE, BANK_KEY, " +
        "CANCEL_DATE, CREATED, CREATED_BY, CREDIT_MEMO_AMOUNT, FI_AREA, FILE_NAME, FILE_STATUS, FILE_TYPE, FISCAL_YEAR, " +
        "INV_COMP_CODE, INV_DOC_NO, INV_FISCAL_YEAR, PAY_ACCOUNT, PAYING_COMP_CODE, PAYMENT_DATE, PAYMENT_DOCUMENT, " +
        "PAYMENT_NAME, REF_LINE, REF_RUNNING, IS_RERUN, SEND_STATUS, TRANSFER_DATE, TRANSFER_LEVEL, UPDATED, UPDATED_BY, " +
        "VENDOR, VENDOR_BANK_ACCOUNT, ORIGINAL_ACC_DOC_NO, ORIGINAL_FISCAL_YEAR, ORIGINAL_COMP_CODE, ORIGINAL_DOC_TYPE, " +
        "ORIGINAL_WTX_AMOUNT, ORIGINAL_WTX_BASE, ORIGINAL_WTX_AMOUNT_P, ORIGINAL_WTX_BASE_P, PAYMENT_FISCAL_YEAR, " +
        "PAYMENT_COMP_CODE, INV_DOC_TYPE, ORIGINAL_DOC_NO, ORIGINAL_YEAR, " +
        "PROPOSAL_LOG_HEADER_ID, REV_INV_DOC_NO, REV_INV_FISCAL_YEAR, REV_ORIGINAL_DOC_NO, REV_ORIGINAL_FISCAL_YEAR, " +
        "REV_PAYMENT_DOCUMENT, REV_PAYMENT_FISCAL_YEAR, REV_ORIGINAL_REASON, IS_JU_CREATE, " +
        "INV_WTX_AMOUNT, INV_WTX_BASE, INV_WTX_AMOUNT_P, INV_WTX_BASE_P, PAYMENT_TYPE, RETURN_DATE, RETURN_BY) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
        "?, ?)";
    for (List<ProposalLogSum> batch : proposalLogSumBatchs) {
      this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i)
            throws SQLException {
          ProposalLogSum proposalLogSum = batch.get(i);
          int index = 0;
          ps.setLong(++index, proposalLogSum.getId());
          ps.setString(++index, proposalLogSum.getAccountNoFrom());
          ps.setString(++index, proposalLogSum.getAccountNoTo());
          ps.setBigDecimal(++index, proposalLogSum.getAmount());
          ps.setBigDecimal(++index, proposalLogSum.getBankFee());
          ps.setString(++index, proposalLogSum.getBankKey());
          ps.setTimestamp(++index, proposalLogSum.getCancelDate());
          ps.setTimestamp(++index, proposalLogSum.getCreated());
          ps.setString(++index, proposalLogSum.getCreatedBy());
          ps.setBigDecimal(++index, proposalLogSum.getCreditMemoAmount());
          ps.setString(++index, proposalLogSum.getFiArea());
          ps.setString(++index, proposalLogSum.getFileName());
          ps.setString(++index, proposalLogSum.getFileStatus());
          ps.setString(++index, proposalLogSum.getFileType());
          ps.setString(++index, proposalLogSum.getFiscalYear());
          ps.setString(++index, proposalLogSum.getInvCompCode());
          ps.setString(++index, proposalLogSum.getInvDocNo());
          ps.setString(++index, proposalLogSum.getInvFiscalYear());
          ps.setString(++index, proposalLogSum.getPayAccount());
          ps.setString(++index, proposalLogSum.getPayingCompCode());
          ps.setTimestamp(++index, proposalLogSum.getPaymentDate());
          ps.setString(++index, proposalLogSum.getPaymentDocument());
          ps.setString(++index, proposalLogSum.getPaymentName());
          ps.setInt(++index, proposalLogSum.getRefLine());
          ps.setLong(++index, proposalLogSum.getRefRunning());
          ps.setBoolean(++index, proposalLogSum.isRerun());
          ps.setString(++index, proposalLogSum.getSendStatus());
          ps.setTimestamp(++index, proposalLogSum.getTransferDate());
          ps.setString(++index, proposalLogSum.getTransferLevel());
          ps.setTimestamp(++index, proposalLogSum.getUpdated());
          ps.setString(++index, proposalLogSum.getUpdatedBy());
          ps.setString(++index, proposalLogSum.getVendor());
          ps.setString(++index, proposalLogSum.getVendorBankAccount());
          ps.setString(++index, proposalLogSum.getOriginalDocNo());
          ps.setString(++index, proposalLogSum.getOriginalFiscalYear());
          ps.setString(++index, proposalLogSum.getOriginalCompCode());
          ps.setString(++index, proposalLogSum.getOriginalDocType());
          ps.setBigDecimal(++index, proposalLogSum.getOriginalWtxAmount());
          ps.setBigDecimal(++index, proposalLogSum.getOriginalWtxBase());
          ps.setBigDecimal(++index, proposalLogSum.getOriginalWtxAmountP());
          ps.setBigDecimal(++index, proposalLogSum.getOriginalWtxBaseP());
          ps.setString(++index, proposalLogSum.getPaymentFiscalYear());
          ps.setString(++index, proposalLogSum.getPaymentCompCode());
          ps.setString(++index, proposalLogSum.getInvDocType());
          ps.setString(++index, proposalLogSum.getOriginalDocNo());
          ps.setString(++index, proposalLogSum.getOriginalFiscalYear());
          ps.setLong(++index, proposalLogSum.getProposalLogHeaderId());
          ps.setString(++index, proposalLogSum.getRevInvDocNo());
          ps.setString(++index, proposalLogSum.getRevInvFiscalYear());
          ps.setString(++index, proposalLogSum.getRevOriginalDocNo());
          ps.setString(++index, proposalLogSum.getRevOriginalFiscalYear());
          ps.setString(++index, proposalLogSum.getRevPaymentDocument());
          ps.setString(++index, proposalLogSum.getRevPaymentFiscalYear());
          ps.setString(++index, proposalLogSum.getRevOriginalReason());
          ps.setBoolean(++index, proposalLogSum.isJuCreate());
          ps.setBigDecimal(++index, proposalLogSum.getInvWtxAmount());
          ps.setBigDecimal(++index, proposalLogSum.getInvWtxBase());
          ps.setBigDecimal(++index, proposalLogSum.getInvWtxAmountP());
          ps.setBigDecimal(++index, proposalLogSum.getInvWtxBaseP());
          ps.setString(++index, proposalLogSum.getPaymentType());
          ps.setTimestamp(++index, proposalLogSum.getReturnDate());
          ps.setString(++index, proposalLogSum.getReturnBy());
        }

        @Override
        public int getBatchSize() {
          return batch.size();
        }
      });
    }
  }

  @Override
  public void updateRegen(Timestamp paymentDate, String paymentName, String fileType, String fileName) {
    List<Object> params = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    sql.append(" UPDATE ");
    sql.append(" PROPOSAL_LOG_SUM ");
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
    log.info("params : {} ", params);
    int rtn = this.jdbcTemplate.update(sql.toString(), objParams);
    log.info("rtn : {}", rtn);
  }

  @Override
  public List<ProposalLogSum> findAllProposalLogByReturnFile(Map<String, Object> paramsReceive) {
    List<Object> params = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    sql.append(" SELECT * FROM PROPOSAL_LOG_SUM ");
    sql.append(" WHERE 1=1  ");
    if (null != paramsReceive.get("paymentDate") && !Util.isEmpty(paramsReceive.get("paymentDate").toString())) {
      params.add(paramsReceive.get("paymentDate").toString());
      sql.append(" AND TO_CHAR(PAYMENT_DATE,'DD-MM-YYYY') = ? ");
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
//        if (null != paramsReceive.get("paymentCompCode") && !Util.isEmpty(paramsReceive.get("paymentCompCode").toString())) {
//            params.add(paramsReceive.get("paymentCompCode").toString());
//            sql.append(" AND PAYMENT_COMP_CODE = ? ");
//        }
//        if (null != paramsReceive.get("paymentDocument") && !Util.isEmpty(paramsReceive.get("paymentDocument").toString())) {
//            params.add(paramsReceive.get("paymentDocument").toString());
//            sql.append(" AND PAYMENT_DOCUMENT = ? ");
//        }
//        if (null != paramsReceive.get("paymentFiscalYear") && !Util.isEmpty(paramsReceive.get("paymentFiscalYear").toString())) {
//            params.add(paramsReceive.get("paymentFiscalYear").toString());
//            sql.append(" AND PAYMENT_FISCAL_YEAR = ? ");
//        }


//        if (null != paramsReceive.get("transferDate") && !Util.isEmpty(paramsReceive.get("transferDate").toString())) {
//            params.add(paramsReceive.get("transferDate").toString());
//            sql.append(" AND TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
//        }
//        if (null != paramsReceive.get("effectiveDateConvert") && !Util.isEmpty(paramsReceive.get("effectiveDateConvert").toString())) {
//            params.add(paramsReceive.get("effectiveDateConvert").toString());
//            sql.append(" AND TO_CHAR(TRANSFER_DATE,'DD-MM-YYYY') = ? ");
//        }

//        if (null != paramsReceive.get("fiArea") && !Util.isEmpty(paramsReceive.get("fiArea").toString())) {
//            params.add(paramsReceive.get("fiArea").toString());
//            sql.append(" AND FI_AREA = ? ");
//        }

    if (null != paramsReceive.get("amount") && !Util.isEmpty(paramsReceive.get("amount").toString())) {
      params.add(paramsReceive.get("amount").toString());
      sql.append(" AND AMOUNT = ? ");
    }

    sql.append(" AND SEND_STATUS = 'S' AND FILE_STATUS IS NULL ");

    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);

    for (Object a : objParams) {
      log.info("Object {}", a);
    }
    log.info("params : {} {} ", params, params.size());
    log.info("objParams : {} ", objParams);
    log.info("query : {} ", sql.toString());
    return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
  }

  @Override
  public void updateBatch(List<ProposalLogSum> proposalLogSums) {
    final int batchSize = 30000;
    List<List<ProposalLogSum>> proposalLogSumsBatches = Lists.partition(proposalLogSums, batchSize);
    final String sqlSave = "UPDATE /*+ ENABLE_PARALLEL_DML */ PROPOSAL_LOG_SUM SET FILE_STATUS = ?, RETURN_DATE = ?, RETURN_BY = ? WHERE ID = ?";
    for (List<ProposalLogSum> batch : proposalLogSumsBatches) {
      this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i)
            throws SQLException {
          int index = 0;
          ProposalLogSum proposalLogSum = batch.get(i);
          ps.setString(++index, proposalLogSum.getFileStatus());
          ps.setTimestamp(++index, proposalLogSum.getReturnDate());
          ps.setString(++index, proposalLogSum.getReturnBy());
          ps.setLong(++index, proposalLogSum.getId());
        }

        @Override
        public int getBatchSize() {
          return batch.size();
        }
      });
    }
  }
}
