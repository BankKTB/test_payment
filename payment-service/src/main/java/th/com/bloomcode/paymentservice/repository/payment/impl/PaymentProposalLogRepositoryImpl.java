package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.PaymentProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogSum;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentProposalLogRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class PaymentProposalLogRepositoryImpl extends MetadataJdbcRepository<PaymentProposalLog, Long> implements PaymentProposalLogRepository {
    static BeanPropertyRowMapper<PaymentProposalLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PaymentProposalLog.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<PaymentProposalLog> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(PaymentProposalLog.COLUMN_NAME_PAYMENT_PROPOSAL_LOG_ID, t.getId());
        mapping.put(PaymentProposalLog.COLUMN_NAME_PAYMENT_ALIAS_ID, t.getPaymentAliasId());
        mapping.put(PaymentProposalLog.COLUMN_NAME_SEQUENCE, t.getSequence());
        mapping.put(PaymentProposalLog.COLUMN_NAME_LOG_DATE, t.getLogDate());
        mapping.put(PaymentProposalLog.COLUMN_NAME_MESSAGE_TEXT, t.getMessageText());
        mapping.put(PaymentProposalLog.COLUMN_NAME_MESSAGE_CLASS, t.getMessageClass());
        mapping.put(PaymentProposalLog.COLUMN_NAME_MESSAGE_NO, t.getMessageNo());
        mapping.put(PaymentProposalLog.COLUMN_NAME_MESSAGE_TYPE, t.getMessageType());
        mapping.put(PaymentProposalLog.COLUMN_NAME_IS_PROPOSAL, t.isProposal());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(PaymentProposalLog.COLUMN_NAME_PAYMENT_PROPOSAL_LOG_ID, Types.BIGINT),
            entry(PaymentProposalLog.COLUMN_NAME_PAYMENT_ALIAS_ID, Types.BIGINT),
            entry(PaymentProposalLog.COLUMN_NAME_SEQUENCE, Types.INTEGER),
            entry(PaymentProposalLog.COLUMN_NAME_LOG_DATE, Types.TIMESTAMP),
            entry(PaymentProposalLog.COLUMN_NAME_MESSAGE_TEXT, Types.NVARCHAR),
            entry(PaymentProposalLog.COLUMN_NAME_MESSAGE_CLASS, Types.NVARCHAR),
            entry(PaymentProposalLog.COLUMN_NAME_MESSAGE_NO, Types.NVARCHAR),
            entry(PaymentProposalLog.COLUMN_NAME_MESSAGE_TYPE, Types.NVARCHAR),
            entry(PaymentProposalLog.COLUMN_NAME_IS_PROPOSAL, Types.BOOLEAN)
    );

//    static RowMapper<PaymentProposalLog> userRowMapper = (rs, rowNum) -> new PaymentProposalLog(
//            rs.getLong(PaymentProposalLog.COLUMN_NAME_BANK_CODE_ID),
//            rs.getString(PaymentProposalLog.COLUMN_NAME_ACCOUNT_NO),
//            rs.getString(PaymentProposalLog.COLUMN_NAME_BANK_KEY),
//            rs.getString(PaymentProposalLog.COLUMN_NAME_BANK_NAME),
//            rs.getString(PaymentProposalLog.COLUMN_NAME_BANK_SHORT_NAME),
//            rs.getString(PaymentProposalLog.COLUMN_NAME_INCST_CODE),
//            rs.getBoolean(PaymentProposalLog.COLUMN_NAME_IS_INHOUSE),
//            rs.getInt(PaymentProposalLog.COLUMN_NAME_INHOUSE_NO),
//            rs.getString(PaymentProposalLog.COLUMN_NAME_PAY_ACCOUNT)
//    );

    public PaymentProposalLogRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, PaymentProposalLog.TABLE_NAME, PaymentProposalLog.COLUMN_NAME_PAYMENT_PROPOSAL_LOG_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Page<PaymentProposalLog> findAllByPaymentAliasIdAndProposal(Long paymentAliasId, boolean success, int page, int size) {
        List<Object> params = new ArrayList<>();
        Pageable pageable = generateSQLPageable(page, size);
        params.add(paymentAliasId);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PAYMENT_PROPOSAL_LOG.*, ");
        sql.append("(SELECT COUNT(1) FROM PAYMENT_PROCESS WHERE PAYMENT_PROPOSAL_LOG.PAYMENT_ALIAS_ID = PAYMENT_PROCESS.PAYMENT_ALIAS_ID AND PAYMENT_PROCESS.IS_PROPOSAL = 1 AND PAYMENT_PROCESS.IS_CHILD = 0) AS TOTAL, ");
        sql.append("(SELECT COUNT(1) FROM PAYMENT_PROCESS WHERE PAYMENT_PROPOSAL_LOG.PAYMENT_ALIAS_ID = PAYMENT_PROCESS.PAYMENT_ALIAS_ID AND STATUS = 'S' AND PAYMENT_PROCESS.IS_PROPOSAL = 1 AND PAYMENT_PROCESS.IS_CHILD = 0) AS SUCCESS, ");
        sql.append("(SELECT COUNT(1) FROM PAYMENT_PROCESS WHERE PAYMENT_PROPOSAL_LOG.PAYMENT_ALIAS_ID = PAYMENT_PROCESS.PAYMENT_ALIAS_ID AND STATUS = 'E' AND PAYMENT_PROCESS.IS_PROPOSAL = 1 AND PAYMENT_PROCESS.IS_CHILD = 0) AS ERROR ");
        sql.append(" FROM " + PaymentProposalLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(PaymentProposalLog.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
        if (success) {
            sql.append(" AND (MESSAGE_TYPE = 'S') ");
        } else {
            sql.append(" AND (MESSAGE_TYPE = 'E') ");
        }
        sql.append(" ORDER by sequence ");
        sql.append(generatePageable(pageable, params));


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql);
        List<PaymentProposalLog> list = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        int count = count(paymentAliasId, success);
        return new PageImpl<>(list, pageable, count);
//        return new PageImpl<List<PaymentProposalLog>>(pageable, new BigDecimal(count).divide(new BigDecimal(pageable.getPageSize()), RoundingMode.UP).intValue(), count, list);
    }

    private int count(Long paymentAliasId, boolean success) {
        List<Object> params = new ArrayList<>();
        params.add(paymentAliasId);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM " + PaymentProposalLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(PaymentProposalLog.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
        if (success) {
            sql.append(" AND (MESSAGE_TYPE = 'S')");
        } else {
            sql.append(" AND (MESSAGE_TYPE = 'E') ");
        }

        sql.append(" ORDER by sequence ");


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql);
        Integer count = this.jdbcTemplate.queryForObject(sql.toString(), objParams, Integer.class);
        return null == count ? 0 : count;
    }

    @Override
    public void deletePaymentProposalLog(Long paymentAliasId, boolean isProposal) {
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE ");
        sql.append(" FROM " + PaymentProposalLog.TABLE_NAME);
        sql.append("  ");
        sql.append(" WHERE ");
        sql.append(" 1 = 1  ");
        sql.append(" AND PAYMENT_ALIAS_ID  = ? ");

        if (!isProposal) {
            sql.append(" AND " + PaymentProposalLog.COLUMN_NAME_IS_PROPOSAL + " = 0 ");
        }

        this.jdbcTemplate.update(sql.toString(), paymentAliasId);
    }

    @Override
    public void saveBatch(List<PaymentProposalLog> proposalLogs) {
        final int batchSize = 30000;
        long proposalLogsSize = proposalLogs.size();
        final long[] id = {SqlUtil.getNextSeries(
                this.jdbcTemplate, PaymentProposalLog.TABLE_NAME + PaymentProposalLog.SEQ, proposalLogsSize)};
//        SqlUtil.updateNextSeries(
//                this.jdbcTemplate, id[0] + proposalLogsSize, PaymentProposalLog.TABLE_NAME + PaymentProposalLog.SEQ);
        List<List<PaymentProposalLog>> proposalLogBatchs = Lists.partition(proposalLogs, batchSize);
        final String sqlSave = "INSERT /*+ ENABLE_PARALLEL_DML */ INTO PAYMENT_PROPOSAL_LOG (ID, LOG_DATE, MESSAGE_CLASS, MESSAGE_NO, MESSAGE_TEXT, MESSAGE_TYPE, PAYMENT_ALIAS_ID, SEQUENCE, IS_PROPOSAL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        for (List<PaymentProposalLog> batch : proposalLogBatchs) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    PaymentProposalLog paymentProposalLog = batch.get(i);
                    ps.setLong(1, id[0]++);
                    ps.setTimestamp(2, paymentProposalLog.getLogDate());
                    ps.setString(3, paymentProposalLog.getMessageClass());
                    ps.setString(4, paymentProposalLog.getMessageNo());
                    ps.setString(5, paymentProposalLog.getMessageText());
                    ps.setString(6, paymentProposalLog.getMessageType());
                    ps.setLong(7, paymentProposalLog.getPaymentAliasId());
                    ps.setInt(8, paymentProposalLog.getSequence());
                    ps.setBoolean(9, paymentProposalLog.isProposal());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    private Pageable generateSQLPageable(int page, int size) {
        return PageRequest.of(page - 1, size);
    }
}
