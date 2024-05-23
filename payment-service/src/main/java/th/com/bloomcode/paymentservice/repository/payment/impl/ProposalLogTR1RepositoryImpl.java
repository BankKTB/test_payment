package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.helper.ColumnRowMapper;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogTR1;
import th.com.bloomcode.paymentservice.model.payment.dto.SummaryFromTR1;
import th.com.bloomcode.paymentservice.model.request.SummaryTR1Request;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.ProposalLogTR1Repository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Repository
public class ProposalLogTR1RepositoryImpl extends MetadataJdbcRepository<ProposalLogTR1, Long> implements ProposalLogTR1Repository {
    final static Pattern pattern = Pattern.compile("L.*?");

    static BeanPropertyRowMapper<ProposalLogTR1> beanPropertyRowMapper = new BeanPropertyRowMapper<>(ProposalLogTR1.class);

    private final JdbcTemplate jdbcTemplate;

    public ProposalLogTR1RepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(null, null, null, ProposalLogTR1.TABLE_NAME, ProposalLogTR1.COLUMN_NAME_PROPOSAL_LOG_TR1_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveBatch(List<ProposalLogTR1> proposalLogTR1s) {
        final int batchSize = 30000;
        List<List<ProposalLogTR1>> proposalLogTR1sBatchs = Lists.partition(proposalLogTR1s, batchSize);
        final String sqlSave =
                "insert /*+ enable_parallel_dml */ into PROPOSAL_LOG_TR1 (ID, TRANSFER_DATE, COMP_CODE, FUND_SOURCE, BUDGET_CODE, Z_INDEX, DOC_TYPE, GL_ACCOUNT, "
                        + "AMOUNT, CREATED, CREATED_BY, UPDATED, UPDATED_BY) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for (List<ProposalLogTR1> batch : proposalLogTR1sBatchs) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    ProposalLogTR1 proposalLogTR1 = batch.get(i);
                    int index = 0;
                    ps.setLong(++index, proposalLogTR1.getId());
                    ps.setTimestamp(++index, proposalLogTR1.getTransferDate());
                    ps.setString(++index, proposalLogTR1.getCompCode());
                    ps.setString(++index, proposalLogTR1.getFundSource());
                    ps.setString(++index, proposalLogTR1.getBudgetCode());
                    ps.setString(++index, proposalLogTR1.getZIndex());
                    ps.setString(++index, proposalLogTR1.getDocType());
                    ps.setString(++index, proposalLogTR1.getGlAccount());
                    ps.setBigDecimal(++index, proposalLogTR1.getAmount());
                    ps.setTimestamp(++index, proposalLogTR1.getCreated());
                    ps.setString(++index, proposalLogTR1.getCreatedBy());
                    ps.setTimestamp(++index, proposalLogTR1.getUpdated());
                    ps.setString(++index, proposalLogTR1.getUpdatedBy());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public void saveBatch(List<SummaryFromTR1> summaryFromTR1s, SummaryTR1Request request) {
        final int batchSize = 30000;
        List<List<SummaryFromTR1>> proposalLogTR1sBatchs = Lists.partition(summaryFromTR1s, batchSize);

        final String sqlSave =
                "insert /*+ enable_parallel_dml */ into PROPOSAL_LOG_TR1 (TRANSFER_DATE, COMP_CODE, FUND_SOURCE, BUDGET_CODE, Z_INDEX, DOC_TYPE, GL_ACCOUNT, "
                        + "AMOUNT, CREATED_BY, UPDATED_BY, PERIOD_NO, FISCAL_YEAR, REF_RUNNING, PAYMENT_DATE, PAYMENT_NAME) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for (List<SummaryFromTR1> batch : proposalLogTR1sBatchs) {
            //TODO DELETE BEFORE INSERT NEW
            for (SummaryFromTR1 batch1 : batch) {
                deleteAllByCriteria1(request.getPeriod(), request.getFiscalYear(), batch1.getRefRunning(), batch1.getPaymentName());
            }
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    SummaryFromTR1 summaryFromTR1 = batch.get(i);
                    int index = 0;
                    ps.setTimestamp(++index, summaryFromTR1.getTransferDate());
                    ps.setString(++index, summaryFromTR1.getOriginalCompCode());
                    ps.setString(++index, summaryFromTR1.getFundSource());
                    ps.setString(++index, summaryFromTR1.getBudgetCode());
                    ps.setString(++index, generateZIndex(summaryFromTR1));
                    ps.setString(++index, generateDocType(summaryFromTR1));
                    ps.setString(++index, generateGlAccount(summaryFromTR1));
                    ps.setBigDecimal(++index, summaryFromTR1.getAmount());
                    ps.setString(++index, request.getUsername());
                    ps.setString(++index, request.getUsername());
                    ps.setString(++index, summaryFromTR1.getPeriodNo());
                    ps.setString(++index, String.valueOf(Integer.parseInt(summaryFromTR1.getOriginalFiscalYear()) - 543));
                    ps.setLong(++index, summaryFromTR1.getRefRunning());
                    ps.setTimestamp(++index, summaryFromTR1.getPaymentDate());
                    ps.setString(++index, summaryFromTR1.getPaymentName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public void deleteAllByCriteria(SummaryTR1Request request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE ");
        sql.append(" FROM ");
        sql.append(" PROPOSAL_LOG_TR1 P");
        sql.append(" WHERE 1=1 ");
        if (!Util.isEmpty(request.getPeriod())) {
            sql.append(SqlUtil.whereClause(request.getPeriod(), "P.PERIOD_NO", params));
        }
        if (!Util.isEmpty(request.getFiscalYear())) {
            sql.append(SqlUtil.whereClause(request.getFiscalYear(), "P.FISCAL_YEAR", params));
        }
        if (!Util.isEmpty(request.getRefNumber())) {
            sql.append(SqlUtil.dynamicCondition(request.getRefNumber(), "P.REF_RUNNING", params));
        }
        if (!Util.isEmpty(request.getPaymentDate())) {
            sql.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "P.PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            sql.append(SqlUtil.dynamicCondition(request.getPaymentName(), "P.PAYMENT_NAME", params));
        }
        log.info("params delete {} ", params);
//        this.jdbcTemplate.update(sql.toString(), params);
        this.jdbcTemplate.query(sql.toString(), (PreparedStatementSetter) params, ColumnRowMapper.newInstance(ProposalLogTR1.class));
    }

    public void deleteAllByCriteria1(String periodNo, String fiscalYear, Long refRunning, String paymentName) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE ");
        sql.append(" FROM ");
        sql.append(" PROPOSAL_LOG_TR1 P");
        sql.append(" WHERE 1=1 ");
        if (!Util.isEmpty(periodNo)) {
            sql.append(" AND P.PERIOD_NO = ? ");
        }
        if (!Util.isEmpty(fiscalYear)) {
            sql.append(" AND P.FISCAL_YEAR = ? ");
        }
        if (!Util.isEmpty(refRunning)) {
            sql.append(" AND P.REF_RUNNING = ? ");
        }
        if (!Util.isEmpty(paymentName)) {
            sql.append(" AND P.PAYMENT_NAME = ? ");
        }
        this.jdbcTemplate.update(sql.toString(), periodNo, fiscalYear, refRunning, paymentName);
    }

    private String generateZIndex(SummaryFromTR1 summaryFromTR1) {
        if (null != summaryFromTR1 && null != summaryFromTR1.getDocType() && summaryFromTR1.getDocType().length() > 0) {
            if (pattern.matcher(summaryFromTR1.getDocType()).matches()) {
                return "M";
            }
        }
        return "N";
    }

    private String generateDocType(SummaryFromTR1 summaryFromTR1) {
        if (null != summaryFromTR1 && null != summaryFromTR1.getDocType() && summaryFromTR1.getDocType().length() > 0) {
            if (pattern.matcher(summaryFromTR1.getDocType()).matches()) {
                return summaryFromTR1.getDocType();
            }
        }
        return "";
    }

    private String generateGlAccount(SummaryFromTR1 summaryFromTR1) {
        if (null != summaryFromTR1
                && null != summaryFromTR1.getDocType() && null != summaryFromTR1.getOriginalCompCode()
                && summaryFromTR1.getDocType().length() > 0 && summaryFromTR1.getOriginalCompCode().length() > 0) {
            if (pattern.matcher(summaryFromTR1.getDocType()).matches() || "03009".equalsIgnoreCase(summaryFromTR1.getOriginalCompCode())) {
                return summaryFromTR1.getGlAccount();
            }
        }
        return "";
    }

    @Override
    public List<SummaryFromTR1> summaryReportFromTR1(SummaryTR1Request request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("WITH TMPLEVEL9 AS ( ");
        sb.append("  select PL.REF_RUNNING, P.PERIODNO ");
        sb.append("  FROM PROPOSAL_LOG PL ");
        sb.append("  LEFT JOIN C_PERIOD P ON TRUNC(PL.TRANSFER_DATE, 'dd') BETWEEN P.STARTDATE AND P.ENDDATE ");
        sb.append("  LEFT JOIN C_YEAR Y ON P.C_YEAR_ID = Y.C_YEAR_ID ");
        sb.append("  where 1 = 1 ");
        sb.append("  AND PL.VENDOR = 'BOT' ");
        sb.append("  AND PL.TRANSFER_LEVEL = '1' ");
        sb.append("  AND PL.IS_RERUN = 0 ");
        if (!Util.isEmpty(request.getPeriod())) {
            sb.append(SqlUtil.whereClause(request.getPeriod(), "P.PERIODNO", params));
        }
        if (!Util.isEmpty(request.getFiscalYear())) {
            sb.append(SqlUtil.whereClause(request.getFiscalYear(), "Y.FISCALYEAR", params));
        }
        if (!Util.isEmpty(request.getRefNumber())) {
            sb.append(SqlUtil.dynamicCondition(request.getRefNumber(), "PL.REF_RUNNING", params));
        }
        if (!Util.isEmpty(request.getPaymentDate())) {
            sb.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PL.PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentName(), "PL.PAYMENT_NAME", params));
        }
        sb.append(") , TMP AS ( ");
        sb.append(" select PL.REF_RUNNING ");
        sb.append(", PL.PAYMENT_DATE ");
        sb.append(", TO_CHAR(T9.PERIODNO) AS PERIODNO ");
        sb.append(", PL.ORIGINAL_DOC_NO ");
        sb.append(", PL.VENDOR ");
        sb.append(", PL.AMOUNT ");
        sb.append(", PL.FILE_TYPE ");
        sb.append(", PL.TRANSFER_LEVEL ");
        sb.append(", PL.TRANSFER_DATE ");
        sb.append(", PL.ORIGINAL_COMP_CODE ");
        sb.append(", PL.INV_COMP_CODE ");
        sb.append(", PL.PAYMENT_NAME ");
        sb.append(", PL.REF_LINE ");
        sb.append(", PL.PAYMENT_DOCUMENT ");
        sb.append(", PL.ORIGINAL_FISCAL_YEAR ");
        sb.append(", PL.PAYMENT_FISCAL_YEAR ");
        sb.append(", CASE ");
        sb.append(" WHEN PL.FILE_STATUS = 'R' THEN 'O' ");
        sb.append(" WHEN GH.DOCUMENT_TYPE LIKE 'L%' THEN GH.DOCUMENT_TYPE ");
        sb.append(" ELSE '' END AS DOCUMENT_TYPE ");
        sb.append(", GL.FUND_CENTER ");
        sb.append(", GL.FUND_SOURCE ");
        sb.append(", CASE ");
        sb.append(" WHEN (GH.DOCUMENT_TYPE LIKE 'L2' OR PL.ORIGINAL_COMP_CODE = '03009') THEN (SELECT GL_ACCOUNT FROM GL_HEAD IGH LEFT JOIN GL_LINE IGL ON IGH.ID = IGL.GL_HEAD_ID WHERE IGH.ORIGINAL_DOCUMENT = GH.ORIGINAL_DOCUMENT AND IGH.COMPANY_CODE = '99999' AND IGL.ACCOUNT_TYPE = 'S') ");
        sb.append(" WHEN (GH.DOCUMENT_TYPE LIKE 'L%' OR GH.COMPANY_CODE = '03009') THEN GL.GL_ACCOUNT ");
        sb.append(" ELSE '' END                                                        AS GL_ACCOUNT ");
        sb.append(", GL.BG_CODE ");
        sb.append(", DECODE(PL.FILE_STATUS, 'R', PL.FILE_STATUS, '') AS FILE_STATUS ");
        sb.append(", CASE WHEN GH.DOCUMENT_TYPE LIKE 'L%' THEN 'M' ELSE '' END AS FLAG ");
        sb.append(", CASE WHEN PL.VENDOR = 'BOT' AND PL.TRANSFER_LEVEL = '1' THEN 'LEVEL1' ");
        sb.append("       WHEN PL.VENDOR NOT IN ('0000000000', 'BOT') AND PL.TRANSFER_LEVEL = '9' AND IS_RERUN = 0 THEN 'LEVEL3' ");
        sb.append("       WHEN PL.VENDOR NOT IN ('0000000000', 'BOT') AND PL.TRANSFER_LEVEL = '9' AND IS_RERUN = 0 AND FILE_STATUS = 'R' THEN 'LEVEL3_RETURN'          ");
        sb.append("  ELSE 'OTHER_LEVEL' END AS ROW_LEVEL ");
        sb.append(" FROM TMPLEVEL9 T9 LEFT JOIN PROPOSAL_LOG PL on PL.REF_RUNNING = T9.REF_RUNNING ");
        sb.append(" LEFT JOIN GL_HEAD GH on PL.ORIGINAL_DOC_NO = GH.ORIGINAL_DOCUMENT_NO ");
        sb.append("         AND PL.ORIGINAL_FISCAL_YEAR = GH.ORIGINAL_FISCAL_YEAR AND ");
        sb.append("         PL.ORIGINAL_COMP_CODE = GH.COMPANY_CODE ");
        sb.append(" LEFT JOIN GL_LINE GL on GL.GL_HEAD_ID = GH.ID AND GL.ACCOUNT_TYPE = 'S' ");
        sb.append(" WHERE PL.IS_RERUN = 0 ");
        sb.append(" ) ");
        sb.append(" SELECT CASE WHEN ROW_LEVEL is null and REF_RUNNING is null THEN 'ROW_SUMMARY' ");
        sb.append("             when ROW_LEVEL is null and REF_RUNNING is not null THEN 'GROUP_SUM' ");
        sb.append("             ELSE ROW_LEVEL END AS ROW_LEVEL, ");
        sb.append("  REF_RUNNING, ");
        sb.append("  DECODE(LENGTH(PERIODNO), 1, '0' || PERIODNO, PERIODNO)  AS PERIOD_NO, ");
        sb.append("  TO_CHAR(EXTRACT(year FROM TRANSFER_DATE) + 543) AS YEAR, ");
        sb.append("  TRANSFER_DATE, ");
        sb.append("  ORIGINAL_COMP_CODE, ");
        sb.append("  INV_COMP_CODE, ");
        sb.append("  FUND_SOURCE, ");
        sb.append("  FUND_CENTER, ");
        sb.append("  SUM(AMOUNT)                      AS AMOUNT, ");
        sb.append("  PAYMENT_NAME, ");
        sb.append("  PAYMENT_DATE, ");
        sb.append("  VENDOR, ");
        sb.append("  NVL(REF_LINE, 0) AS REF_LINE, ");
        sb.append("  CASE ");
        sb.append("  WHEN ROW_LEVEL is null and REF_RUNNING is null THEN '3_ROW_SUMMARY' ");
        sb.append("  when ROW_LEVEL is null and REF_RUNNING is not null THEN '2_GROUP_SUM' ");
        sb.append("  ELSE '1_' || ROW_LEVEL END                         AS ROW_LEVEL_ORDER, ");
        sb.append("  ORIGINAL_DOC_NO, ");
        sb.append("  TO_CHAR(TO_NUMBER(ORIGINAL_FISCAL_YEAR) + 543) AS ORIGINAL_FISCAL_YEAR, ");
        sb.append("  PAYMENT_DOCUMENT, ");
        sb.append("  TO_CHAR(TO_NUMBER(PAYMENT_FISCAL_YEAR) + 543) AS PAYMENT_FISCAL_YEAR, ");
        sb.append("  DOCUMENT_TYPE                AS DOC_TYPE, ");
        sb.append("  GL_ACCOUNT, ");
        sb.append("  FLAG, ");
        sb.append("  BG_CODE AS BUDGET_CODE, ");
        sb.append("  FILE_STATUS ");
        sb.append(" FROM TMP ");
        sb.append(" where ROW_LEVEL NOT IN ( 'OTHER_LEVEL', 'LEVEL2') ");
        sb.append(" GROUP BY ROLLUP ((REF_RUNNING), ( PAYMENT_DATE, PERIODNO, VENDOR, ORIGINAL_DOC_NO, ");
        sb.append(" ROW_LEVEL, TRANSFER_DATE, ORIGINAL_COMP_CODE, INV_COMP_CODE, PAYMENT_NAME,REF_LINE,FILE_STATUS, ");
        sb.append(" PAYMENT_DOCUMENT, ORIGINAL_FISCAL_YEAR, PAYMENT_FISCAL_YEAR, FUND_SOURCE, FUND_CENTER, DOCUMENT_TYPE, GL_ACCOUNT, BG_CODE, FLAG )) ");
        sb.append(" ORDER BY CASE WHEN (ROW_LEVEL_ORDER LIKE '3_ROW_SUMMARY') THEN 1 ELSE 0 END ASC, REF_RUNNING ");
        sb.append(" , CASE WHEN (ROW_LEVEL_ORDER LIKE '2_GROUP_SUM') THEN 1 ELSE 0 END ASC , ROW_LEVEL_ORDER, REF_LINE ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("query {} {}", sb, objParams);
        return this.jdbcTemplate.query(sb.toString(), objParams, new BeanPropertyRowMapper<>(SummaryFromTR1.class));
    }
}
