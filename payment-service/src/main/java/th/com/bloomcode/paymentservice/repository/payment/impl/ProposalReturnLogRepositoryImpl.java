package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.ReturnSearchProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogSum;
import th.com.bloomcode.paymentservice.model.payment.ProposalReturnLog;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.ProposalReturnLogRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class ProposalReturnLogRepositoryImpl extends MetadataJdbcRepository<ProposalReturnLog, Long>
    implements ProposalReturnLogRepository {
  static BeanPropertyRowMapper<ProposalReturnLog> beanPropertyRowMapper =
      new BeanPropertyRowMapper<>(ProposalReturnLog.class);
  private final JdbcTemplate jdbcTemplate;

  static Updater<ProposalReturnLog> proposalLogReturnUpdater =
      (t, mapping) -> {
        mapping.put(ProposalReturnLog.COLUMN_NAME_PROPOSAL_LOG_RETURN_ID, t.getId());
        mapping.put(ProposalReturnLog.COLUMN_NAME_INVOICE_COMP_CODE, t.getInvoiceCompCode());
        mapping.put(ProposalReturnLog.COLUMN_NAME_INVOICE_DOC_NO, t.getInvoiceDocNo());
        mapping.put(ProposalReturnLog.COLUMN_NAME_INVOICE_LINE, t.getInvoiceLine());
        mapping.put(ProposalReturnLog.COLUMN_NAME_INVOICE_FISCAL_YEAR, t.getInvoiceFiscalYear());
        mapping.put(ProposalReturnLog.COLUMN_NAME_PAYMENT_COMP_CODE, t.getPaymentCompCode());
        mapping.put(ProposalReturnLog.COLUMN_NAME_PAYMENT_DOC_NO, t.getPaymentDocNo());
        mapping.put(ProposalReturnLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR, t.getPaymentFiscalYear());
        mapping.put(ProposalReturnLog.COLUMN_NAME_PAYMENT_DATE, t.getPaymentDate());
        mapping.put(ProposalReturnLog.COLUMN_NAME_PAYMENT_NAME, t.getPaymentName());
        mapping.put(ProposalReturnLog.COLUMN_NAME_VENDOR, t.getVendor());
        mapping.put(ProposalReturnLog.COLUMN_NAME_PAYMENT_METHOD, t.getPaymentMethod());
        mapping.put(ProposalReturnLog.COLUMN_NAME_BANK_KEY, t.getBankKey());
        mapping.put(ProposalReturnLog.COLUMN_NAME_FILE_NAME, t.getFileName());
        mapping.put(ProposalReturnLog.COLUMN_NAME_TRANSFER_DATE, t.getTransferDate());
        mapping.put(ProposalReturnLog.COLUMN_NAME_RESET_REVERSE_FLAG, t.getResetReverseFlag());
        mapping.put(ProposalReturnLog.COLUMN_NAME_REV_INVOICE_DOC_NO, t.getRevInvoiceDocNo());
        mapping.put(
            ProposalReturnLog.COLUMN_NAME_REV_INVOICE_FISCAL_YEAR, t.getRevInvoiceFiscalYear());
        mapping.put(ProposalReturnLog.COLUMN_NAME_REV_PAYMENT_DOC_NO, t.getRevPaymentDocNo());
        mapping.put(
            ProposalReturnLog.COLUMN_NAME_REV_PAYMENT_FISCAL_YEAR, t.getRevPaymentFiscalYear());
        mapping.put(ProposalReturnLog.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(ProposalReturnLog.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(ProposalReturnLog.COLUMN_NAME_ORIGINAL_COMP_CODE, t.getOriginalCompCode());
        mapping.put(ProposalReturnLog.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, t.getOriginalDocumentNo());
        mapping.put(ProposalReturnLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
        mapping.put(
            ProposalReturnLog.COLUMN_NAME_REV_ORIGINAL_COMP_CODE, t.getRevOriginalCompCode());
        mapping.put(
            ProposalReturnLog.COLUMN_NAME_REV_ORIGINAL_DOCUMENT_NO, t.getRevOriginalDocumentNo());
        mapping.put(
            ProposalReturnLog.COLUMN_NAME_REV_ORIGINAL_FISCAL_YEAR, t.getRevOriginalFiscalYear());
        mapping.put(
                ProposalReturnLog.COLUMN_NAME_AUTO_STEP3, t.isAutoStep3());
        mapping.put(ProposalReturnLog.COLUMN_NAME_REASON_CODE, t.getReasonCode());
        mapping.put(ProposalReturnLog.COLUMN_NAME_REASON_TEXT, t.getReasonText());
//        mapping.put(ProposalReturnLog.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
//        mapping.put(ProposalReturnLog.COLUMN_NAME_UPDATED, t.getUpdated());
      };

  static Map<String, Integer> updaterType =
      Map.ofEntries(
          entry(ProposalReturnLog.COLUMN_NAME_PROPOSAL_LOG_RETURN_ID, Types.BIGINT),
          entry(ProposalReturnLog.COLUMN_NAME_INVOICE_COMP_CODE, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_INVOICE_DOC_NO, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_INVOICE_LINE, Types.INTEGER),
          entry(ProposalReturnLog.COLUMN_NAME_INVOICE_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_PAYMENT_COMP_CODE, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_PAYMENT_DOC_NO, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_PAYMENT_DATE, Types.TIMESTAMP),
          entry(ProposalReturnLog.COLUMN_NAME_PAYMENT_NAME, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_VENDOR, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_PAYMENT_METHOD, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_BANK_KEY, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_FILE_NAME, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_TRANSFER_DATE, Types.TIMESTAMP),
          entry(ProposalReturnLog.COLUMN_NAME_RESET_REVERSE_FLAG, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_REV_INVOICE_DOC_NO, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_REV_INVOICE_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_REV_PAYMENT_DOC_NO, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_REV_PAYMENT_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_CREATED, Types.TIMESTAMP),
          entry(ProposalReturnLog.COLUMN_NAME_ORIGINAL_COMP_CODE, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_REV_ORIGINAL_COMP_CODE, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_REV_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
          entry(ProposalReturnLog.COLUMN_NAME_REV_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
              entry(ProposalReturnLog.COLUMN_NAME_AUTO_STEP3, Types.BOOLEAN),
              entry(ProposalReturnLog.COLUMN_NAME_REASON_CODE, Types.NVARCHAR),
              entry(ProposalReturnLog.COLUMN_NAME_REASON_TEXT, Types.NVARCHAR));
//          entry(ProposalReturnLog.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
//          entry(ProposalReturnLog.COLUMN_NAME_UPDATED, Types.TIMESTAMP));

  public ProposalReturnLogRepositoryImpl(
      @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
    super(
        beanPropertyRowMapper,
        proposalLogReturnUpdater,
        updaterType,
        ProposalReturnLog.TABLE_NAME,
        ProposalReturnLog.COLUMN_NAME_PROPOSAL_LOG_RETURN_ID,
        jdbcTemplate);
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public ProposalReturnLog findOneByInvoiceDocumentAndPaymentDocument(ProposalLog proposalLog) {
    List<Object> params = new ArrayList<>();

    log.info("sql proposalLog {}", proposalLog);

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + ProposalReturnLog.TABLE_NAME);
    sql.append(" WHERE 1=1 ");
    if (!Util.isEmpty(proposalLog.getInvCompCode())) {
      params.add(proposalLog.getInvCompCode());
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_INVOICE_COMP_CODE + " = ? ");
    }
    if (!Util.isEmpty(proposalLog.getInvDocNo())) {
      params.add(proposalLog.getInvDocNo());
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_INVOICE_DOC_NO + " = ? ");
    }
    if (!Util.isEmpty(proposalLog.getInvFiscalYear())) {
      params.add(proposalLog.getInvFiscalYear());
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_INVOICE_FISCAL_YEAR + " = ? ");
    }
    if (!Util.isEmpty(proposalLog.getPaymentCompCode())) {
      params.add(proposalLog.getPaymentCompCode());
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_PAYMENT_COMP_CODE + " = ? ");
    }
    if (!Util.isEmpty(proposalLog.getPaymentDocument())) {
      params.add(proposalLog.getPaymentDocument());
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_PAYMENT_DOC_NO + " = ? ");
    }
    if (!Util.isEmpty(proposalLog.getPaymentFiscalYear())) {
      params.add(proposalLog.getPaymentFiscalYear());
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR + " = ? ");
    }

    Object[] objParams = new Object[params.size()];

    for (Object a : objParams) {
      log.info("Object {}", a);
    }
    params.toArray(objParams);
    log.info("sql return {}", sql.toString());
    log.info("params : {} {} ", params, params.size());
    log.info("objParams : {} ", objParams);
    List<ProposalReturnLog> proposalReturnLogs =
        this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    if (!proposalReturnLogs.isEmpty()) {
      return proposalReturnLogs.get(0);
    } else {
      return null;
    }
  }

  @Override
  public ProposalReturnLog findOneByInvoiceDocumentAndPaymentDocument(String invoiceCompCode, String invoiceDocNo, String invoiceFiscalYear, String paymentCompCode, String paymentDocNo, String paymentFiscalYear) {
    List<Object> params = new ArrayList<>();

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + ProposalReturnLog.TABLE_NAME);
    sql.append(" WHERE 1=1 ");
    if (!Util.isEmpty(invoiceCompCode)) {
      params.add(invoiceCompCode);
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_INVOICE_COMP_CODE + " = ? ");
    }
    if (!Util.isEmpty(invoiceDocNo)) {
      params.add(invoiceDocNo);
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_INVOICE_DOC_NO + " = ? ");
    }
    if (!Util.isEmpty(invoiceFiscalYear)) {
      params.add(invoiceFiscalYear);
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_INVOICE_FISCAL_YEAR + " = ? ");
    }
    if (!Util.isEmpty(paymentCompCode)) {
      params.add(paymentCompCode);
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_PAYMENT_COMP_CODE + " = ? ");
    }
    if (!Util.isEmpty(paymentDocNo)) {
      params.add(paymentDocNo);
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_PAYMENT_DOC_NO + " = ? ");
    }
    if (!Util.isEmpty(paymentFiscalYear)) {
      params.add(paymentFiscalYear);
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR + " = ? ");
    }

    Object[] objParams = new Object[params.size()];

    for (Object a : objParams) {
      log.info("Object {}", a);
    }
    params.toArray(objParams);
    log.info("sql return {}", sql);
    log.info("params : {} {} ", params, params.size());
    log.info("objParams : {} ", objParams);
    List<ProposalReturnLog> proposalReturnLogs =
            this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    if (!proposalReturnLogs.isEmpty()) {
      return proposalReturnLogs.get(0);
    } else {
      return null;
    }
  }

  @Override
  public List<ProposalReturnLog> findPaymentAllByCondition(ReturnSearchProposalLog request) {
    List<Object> params = new ArrayList<>();

    StringBuilder sql = new StringBuilder();
    sql.append("          SELECT PRL.AUTO_STEP3,           ");
    sql.append("          PRL.REASON_TEXT,           ");
    sql.append("          PRL.ID,           ");
    sql.append("          PRL.INVOICE_COMP_CODE,           ");
    sql.append("          PRL.INVOICE_DOC_NO,           ");
    sql.append("          PRL.INVOICE_FISCAL_YEAR,           ");
    sql.append("          PRL.PAYMENT_COMP_CODE,           ");
    sql.append("          PRL.PAYMENT_DOC_NO,           ");
    sql.append("          PRL.PAYMENT_FISCAL_YEAR,           ");
    sql.append("          PRL.PAYMENT_DATE,           ");
    sql.append("          PRL.PAYMENT_NAME,           ");
    sql.append("          PRL.VENDOR,           ");
    sql.append("          PRL.PAYMENT_METHOD,           ");
    sql.append("          PRL.BANK_KEY,           ");
    sql.append("          PRL.REASON_CODE,           ");
    sql.append("          PRL.FILE_NAME,           ");
    sql.append("          PRL.TRANSFER_DATE,           ");
    sql.append("          PRL.RESET_REVERSE_FLAG,           ");
    sql.append("          PRL.REV_INVOICE_DOC_NO,           ");
    sql.append("          PRL.REV_INVOICE_FISCAL_YEAR,           ");
    sql.append("          PRL.CREATED,           ");
    sql.append("          PRL.CREATED_BY,           ");
    sql.append("          PRL.REV_PAYMENT_DOC_NO,           ");
    sql.append("          PRL.REV_PAYMENT_FISCAL_YEAR,           ");
    sql.append("          PRL.ORIGINAL_COMP_CODE,           ");
    sql.append("          PRL.ORIGINAL_DOCUMENT_NO,           ");
    sql.append("          PRL.ORIGINAL_FISCAL_YEAR,           ");
    sql.append("          PRL.REV_ORIGINAL_COMP_CODE,           ");
    sql.append("          PRL.REV_ORIGINAL_DOCUMENT_NO,           ");
    sql.append("          PRL.REV_ORIGINAL_FISCAL_YEAR,           ");
    sql.append("          (SELECT count(*)           ");
    sql.append("          FROM GL_LINE GL           ");
    sql.append("          WHERE PRL.ORIGINAL_COMP_CODE = GL.COMPANY_CODE           ");
    sql.append("          AND PRL.ORIGINAL_DOCUMENT_NO = GL.ORIGINAL_DOCUMENT_NO           ");
    sql.append("          AND PRL.ORIGINAL_FISCAL_YEAR = GL.ORIGINAL_FISCAL_YEAR           ");
    sql.append("          AND GL.ACCOUNT_TYPE = 'K') AS INVOICE_LINE           ");
    sql.append("          FROM PROPOSAL_RETURN_LOG PRL           ");
    sql.append("          WHERE 1 = 1           ");
    sql.append(" AND REV_PAYMENT_DOC_NO IS NULL ");
    sql.append(" AND REV_PAYMENT_FISCAL_YEAR IS NULL ");

    if (!Util.isEmpty(request.getPaymentDate())) {
      sql.append(
          SqlUtil.dynamicDateCondition(
              request.getPaymentDate(), ProposalReturnLog.COLUMN_NAME_PAYMENT_DATE, params));
    }
    if (!Util.isEmpty(request.getPaymentName())) {
      sql.append(
          SqlUtil.dynamicCondition(
              request.getPaymentName(), ProposalReturnLog.COLUMN_NAME_PAYMENT_NAME, params));
    }

    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql find One {}", objParams);
    for(Object a :objParams){
      log.info("sql find One {}", a);
    }
    log.info("sql find One {}", sql.toString());

    return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
  }

  @Override
  public List<ProposalReturnLog> findInvoiceAllByCondition(ReturnSearchProposalLog request) {
    List<Object> params = new ArrayList<>();

    StringBuilder sql = new StringBuilder();
    sql.append("          SELECT PRL.AUTO_STEP3,           ");
    sql.append("          PRL.REASON_TEXT,           ");
    sql.append("          PRL.ID,           ");
    sql.append("          PRL.INVOICE_COMP_CODE,           ");
    sql.append("          PRL.INVOICE_DOC_NO,           ");
    sql.append("          PRL.INVOICE_FISCAL_YEAR,           ");
    sql.append("          PRL.PAYMENT_COMP_CODE,           ");
    sql.append("          PRL.PAYMENT_DOC_NO,           ");
    sql.append("          PRL.PAYMENT_FISCAL_YEAR,           ");
    sql.append("          PRL.PAYMENT_DATE,           ");
    sql.append("          PRL.PAYMENT_NAME,           ");
    sql.append("          PRL.VENDOR,           ");
    sql.append("          PRL.PAYMENT_METHOD,           ");
    sql.append("          PRL.BANK_KEY,           ");
    sql.append("          PRL.REASON_CODE,           ");
    sql.append("          PRL.FILE_NAME,           ");
    sql.append("          PRL.TRANSFER_DATE,           ");
    sql.append("          PRL.RESET_REVERSE_FLAG,           ");
    sql.append("          PRL.REV_INVOICE_DOC_NO,           ");
    sql.append("          PRL.REV_INVOICE_FISCAL_YEAR,           ");
    sql.append("          PRL.CREATED,           ");
    sql.append("          PRL.CREATED_BY,           ");
    sql.append("          PRL.REV_PAYMENT_DOC_NO,           ");
    sql.append("          PRL.REV_PAYMENT_FISCAL_YEAR,           ");
    sql.append("          PRL.ORIGINAL_COMP_CODE,           ");
    sql.append("          PRL.ORIGINAL_DOCUMENT_NO,           ");
    sql.append("          PRL.ORIGINAL_FISCAL_YEAR,           ");
    sql.append("          PRL.REV_ORIGINAL_COMP_CODE,           ");
    sql.append("          PRL.REV_ORIGINAL_DOCUMENT_NO,           ");
    sql.append("          PRL.REV_ORIGINAL_FISCAL_YEAR,           ");
    sql.append("          (SELECT count(*)           ");
    sql.append("          FROM GL_LINE GL           ");
    sql.append("          WHERE PRL.ORIGINAL_COMP_CODE = GL.COMPANY_CODE           ");
    sql.append("          AND PRL.ORIGINAL_DOCUMENT_NO = GL.ORIGINAL_DOCUMENT_NO           ");
    sql.append("          AND PRL.ORIGINAL_FISCAL_YEAR = GL.ORIGINAL_FISCAL_YEAR           ");
    sql.append("          AND GL.ACCOUNT_TYPE = 'K') AS INVOICE_LINE           ");
    sql.append("          FROM PROPOSAL_RETURN_LOG PRL           ");
    sql.append(" WHERE 1 = 1");
//    sql.append(" AND REV_PAYMENT_DOC_NO IS NOT NULL ");
//    sql.append(" AND REV_PAYMENT_FISCAL_YEAR IS NOT NULL ");
    //        sql.append(" AND REV_INVOICE_DOC_NO IS NULL ");
    //        sql.append(" AND REV_INVOICE_FISCAL_YEAR IS NULL ");
    sql.append(" AND REV_ORIGINAL_DOCUMENT_NO IS NULL ");
    sql.append(" AND REV_ORIGINAL_FISCAL_YEAR IS NULL ");

    if (!Util.isEmpty(request.getPaymentDate())) {
      sql.append(
          SqlUtil.dynamicDateCondition(
              request.getPaymentDate(), ProposalReturnLog.COLUMN_NAME_PAYMENT_DATE, params));
    }
    if (!Util.isEmpty(request.getPaymentName())) {
      sql.append(
          SqlUtil.dynamicCondition(
              request.getPaymentName(), ProposalReturnLog.COLUMN_NAME_PAYMENT_NAME, params));
    }

    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("sql find One {}", sql.toString());

    return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
  }

  @Override
  public void saveBatch(List<ProposalReturnLog> proposalReturnLogs) {
    final int batchSize = 30000;
    List<List<ProposalReturnLog>> proposalReturnLogsBatchs =
        Lists.partition(proposalReturnLogs, batchSize);
    final String sqlSave =
        "INSERT /*+ enable_parallel_dml */ INTO PROPOSAL_RETURN_LOG (ID, INVOICE_COMP_CODE, INVOICE_DOC_NO, INVOICE_LINE, INVOICE_FISCAL_YEAR, "
            + "PAYMENT_COMP_CODE, PAYMENT_DOC_NO, PAYMENT_FISCAL_YEAR, PAYMENT_DATE, PAYMENT_NAME, "
            + "VENDOR, PAYMENT_METHOD, BANK_KEY, FILE_NAME, TRANSFER_DATE, RESET_REVERSE_FLAG, "
            + "REV_INVOICE_DOC_NO, REV_INVOICE_FISCAL_YEAR, CREATED, CREATED_BY, REV_PAYMENT_DOC_NO, "
            + "REV_PAYMENT_FISCAL_YEAR, ORIGINAL_COMP_CODE, ORIGINAL_DOCUMENT_NO, "
            + "ORIGINAL_FISCAL_YEAR, REV_ORIGINAL_COMP_CODE, REV_ORIGINAL_DOCUMENT_NO, "
            + "REV_ORIGINAL_FISCAL_YEAR, REASON_CODE, REASON_TEXT) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    for (List<ProposalReturnLog> batch : proposalReturnLogsBatchs) {
      this.jdbcTemplate.batchUpdate(
          sqlSave,
          new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ProposalReturnLog proposalLogSum = batch.get(i);
              int index = 0;
              ps.setLong(++index, proposalLogSum.getId());
              ps.setString(++index, proposalLogSum.getInvoiceCompCode());
              ps.setString(++index, proposalLogSum.getInvoiceDocNo());
              ps.setInt(++index, proposalLogSum.getInvoiceLine());
              ps.setString(++index, proposalLogSum.getInvoiceFiscalYear());
              ps.setString(++index, proposalLogSum.getPaymentCompCode());
              ps.setString(++index, proposalLogSum.getPaymentDocNo());
              ps.setString(++index, proposalLogSum.getPaymentFiscalYear());
              ps.setTimestamp(++index, proposalLogSum.getPaymentDate());
              ps.setString(++index, proposalLogSum.getPaymentName());
              ps.setString(++index, proposalLogSum.getVendor());
              ps.setString(++index, proposalLogSum.getPaymentMethod());
              ps.setString(++index, proposalLogSum.getBankKey());
              ps.setString(++index, proposalLogSum.getFileName());
              ps.setTimestamp(++index, proposalLogSum.getTransferDate());
              ps.setString(++index, proposalLogSum.getResetReverseFlag());
              ps.setString(++index, proposalLogSum.getRevInvoiceDocNo());
              ps.setString(++index, proposalLogSum.getRevInvoiceFiscalYear());
              ps.setTimestamp(++index, proposalLogSum.getCreated());
              ps.setString(++index, proposalLogSum.getCreatedBy());
              ps.setString(++index, proposalLogSum.getRevPaymentDocNo());
              ps.setString(++index, proposalLogSum.getRevPaymentFiscalYear());
              ps.setString(++index, proposalLogSum.getOriginalCompCode());
              ps.setString(++index, proposalLogSum.getOriginalDocumentNo());
              ps.setString(++index, proposalLogSum.getOriginalFiscalYear());
              ps.setString(++index, proposalLogSum.getRevOriginalCompCode());
              ps.setString(++index, proposalLogSum.getRevOriginalDocumentNo());
              ps.setString(++index, proposalLogSum.getRevOriginalFiscalYear());
              ps.setString(++index, proposalLogSum.getReasonCode());
              ps.setString(++index, proposalLogSum.getReasonText());
            }

            @Override
            public int getBatchSize() {
              return batch.size();
            }
          });
    }
  }

  @Override
  public void updateBatch(List<ProposalReturnLog> proposalReturnLogs) {
    final int batchSize = 30000;
    List<List<ProposalReturnLog>> proposalReturnLogsBatches =
        Lists.partition(proposalReturnLogs, batchSize);
    final String sqlSave =
        "UPDATE /*+ ENABLE_PARALLEL_DML */ PROPOSAL_RETURN_LOG SET INVOICE_COMP_CODE = ?, INVOICE_DOC_NO = ?, INVOICE_FISCAL_YEAR = ?, PAYMENT_COMP_CODE = ?, "
            + "PAYMENT_DOC_NO = ?, PAYMENT_FISCAL_YEAR = ?, PAYMENT_DATE = ?, PAYMENT_NAME = ?, VENDOR = ?, BANK_KEY = ?, FILE_NAME = ?, TRANSFER_DATE = ?, REASON_CODE = ?, REASON_TEXT = ? WHERE ID = ?";
    for (List<ProposalReturnLog> batch : proposalReturnLogsBatches) {
      this.jdbcTemplate.batchUpdate(
          sqlSave,
          new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              int index = 0;
              ProposalReturnLog proposalReturnLog = batch.get(i);
              ps.setString(++index, proposalReturnLog.getInvoiceCompCode());
              ps.setString(++index, proposalReturnLog.getInvoiceDocNo());
              ps.setString(++index, proposalReturnLog.getInvoiceFiscalYear());
              ps.setString(++index, proposalReturnLog.getPaymentCompCode());
              ps.setString(++index, proposalReturnLog.getPaymentDocNo());
              ps.setString(++index, proposalReturnLog.getPaymentFiscalYear());
              ps.setTimestamp(++index, proposalReturnLog.getPaymentDate());
              ps.setString(++index, proposalReturnLog.getPaymentName());
              ps.setString(++index, proposalReturnLog.getVendor());
              ps.setString(++index, proposalReturnLog.getBankKey());
              ps.setString(++index, proposalReturnLog.getFileName());
              ps.setTimestamp(++index, proposalReturnLog.getTransferDate());
              ps.setString(++index, proposalReturnLog.getReasonCode());
              ps.setString(++index, proposalReturnLog.getReasonText());
              ps.setLong(++index, proposalReturnLog.getId());
            }

            @Override
            public int getBatchSize() {
              return batch.size();
            }
          });
    }
  }

  @Override
  public List<ProposalReturnLog> findOneReturnDocumentProposalReturnLog(
      String companyCode, String documentNo, String fiscalYear, boolean payment) {
    List<Object> params = new ArrayList<>();

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT * FROM " + ProposalReturnLog.TABLE_NAME);
    sql.append(" WHERE ");
    sql.append(" 1 = 1 ");

    if (payment) {
      if (!Util.isEmpty(companyCode)) {
        params.add(companyCode);
        sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_PAYMENT_COMP_CODE + " = ? ");
      }
      if (!Util.isEmpty(documentNo)) {
        params.add(documentNo);
        sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_PAYMENT_DOC_NO + " = ? ");
      }
      if (!Util.isEmpty(fiscalYear)) {
        params.add(fiscalYear);
        sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_PAYMENT_FISCAL_YEAR + " = ? ");
      }
    } else {
      if (!Util.isEmpty(companyCode)) {
        params.add(companyCode);
        sql.append(ProposalReturnLog.COLUMN_NAME_INVOICE_COMP_CODE + " = ? ");
      }
      if (!Util.isEmpty(documentNo)) {
        params.add(documentNo);
        sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_INVOICE_DOC_NO + " = ? ");
      }
      if (!Util.isEmpty(fiscalYear)) {
        params.add(fiscalYear);
        sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_INVOICE_FISCAL_YEAR + " = ? ");
      }
    }

    Object[] objParams = new Object[params.size()];

    for (Object a : objParams) {
      log.info("Object {}", a);
    }
    params.toArray(objParams);
    log.info("sql return {}", sql.toString());
    log.info("params : {} {} ", params, params.size());
    log.info("objParams : {} ", objParams);
    return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);

  }

  @Override
  public void deleteProposalReturnByStep2Complete(ProposalLog proposalLog) {
    StringBuilder sql = new StringBuilder();
    sql.append(" DELETE ");
    sql.append(" FROM " + ProposalReturnLog.TABLE_NAME);
    sql.append("  ");
    sql.append(" WHERE ");
    sql.append(" 1 = 1  ");


    if (!Util.isEmpty(proposalLog.getOriginalCompCode())) {
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_ORIGINAL_COMP_CODE + " = ? ");
    } else {
      return;
    }
    if (!Util.isEmpty(proposalLog.getOriginalDocNo())) {
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ? ");
    } else {
      return;
    }
    if (!Util.isEmpty(proposalLog.getOriginalFiscalYear())) {
      sql.append(" AND " + ProposalReturnLog.COLUMN_NAME_ORIGINAL_FISCAL_YEAR + " = ? ");
    } else {
      return;
    }
    log.info("delete return {} ", sql.toString());
    this.jdbcTemplate.update(sql.toString(), proposalLog.getOriginalCompCode(),proposalLog.getOriginalDocNo(),proposalLog.getOriginalFiscalYear());
  }

  @Override
  public void deleteBatch(List<ProposalLog> proposalLogs) {
    final int batchSize = 30000;
    List<List<ProposalLog>> proposalLogsBatches = Lists.partition(proposalLogs, batchSize);
    final String sqlSave = "DELETE /*+ ENABLE_PARALLEL_DML */ FROM PROPOSAL_RETURN_LOG WHERE ORIGINAL_COMP_CODE = ? AND ORIGINAL_DOCUMENT_NO = ? AND ORIGINAL_FISCAL_YEAR = ? ";
    for(List<ProposalLog> batch : proposalLogsBatches) {
      this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i)
                throws SQLException {
          int index = 0;
          ProposalLog proposalLog = batch.get(i);
          ps.setString(++index, proposalLog.getOriginalCompCode());
          ps.setString(++index, proposalLog.getOriginalDocNo());
          ps.setString(++index, proposalLog.getOriginalFiscalYear());
        }
        @Override
        public int getBatchSize() {
          return batch.size();
        }
      });
    }
  }
  @Override
  public void updateProposalReturnLogAfterStep3(ProposalLog proposalLog,String revAccDocNo,String revFiscalYear) {
    StringBuilder sql = new StringBuilder();
    sql.append(" UPDATE ");
    sql.append(" PROPOSAL_RETURN_LOG ");
    sql.append(" SET REV_PAYMENT_DOC_NO = ?, REV_PAYMENT_FISCAL_YEAR = ? ");
    sql.append(" WHERE ");
    sql.append(" 1 = 1  ");
    sql.append(" AND PAYMENT_DOC_NO = ? ");
    sql.append(" AND PAYMENT_COMP_CODE = ? ");
    sql.append(" AND PAYMENT_FISCAL_YEAR = ? ");
    log.info("sql updateProposalReturnLogAfterStep3 {}", sql.toString());
    this.jdbcTemplate.update(sql.toString(),  revAccDocNo,revFiscalYear,proposalLog.getPaymentDocument(),proposalLog.getPaymentCompCode(),proposalLog.getPaymentFiscalYear());
  }
}
