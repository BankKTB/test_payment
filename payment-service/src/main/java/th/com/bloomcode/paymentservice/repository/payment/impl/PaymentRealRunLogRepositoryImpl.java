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
import th.com.bloomcode.paymentservice.model.payment.PaymentRealRunLog;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentRealRunLogRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class PaymentRealRunLogRepositoryImpl extends MetadataJdbcRepository<PaymentRealRunLog, Long> implements PaymentRealRunLogRepository {
    static BeanPropertyRowMapper<PaymentRealRunLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PaymentRealRunLog.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<PaymentRealRunLog> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(PaymentRealRunLog.COLUMN_NAME_PAYMENT_REAL_RUN_LOG_ID, t.getId());
        mapping.put(PaymentRealRunLog.COLUMN_NAME_PAYMENT_ALIAS_ID, t.getPaymentAliasId());
        mapping.put(PaymentRealRunLog.COLUMN_NAME_SEQUENCE, t.getSequence());
        mapping.put(PaymentRealRunLog.COLUMN_NAME_LOG_DATE, t.getLogDate());
        mapping.put(PaymentRealRunLog.COLUMN_NAME_MESSAGE_TEXT, t.getMessageText());

    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(PaymentRealRunLog.COLUMN_NAME_PAYMENT_REAL_RUN_LOG_ID, Types.BIGINT),
            entry(PaymentRealRunLog.COLUMN_NAME_PAYMENT_ALIAS_ID, Types.BIGINT),
            entry(PaymentRealRunLog.COLUMN_NAME_SEQUENCE, Types.INTEGER),
            entry(PaymentRealRunLog.COLUMN_NAME_LOG_DATE, Types.TIMESTAMP),
            entry(PaymentRealRunLog.COLUMN_NAME_MESSAGE_TEXT, Types.NVARCHAR)

    );


    public PaymentRealRunLogRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, PaymentRealRunLog.TABLE_NAME, PaymentRealRunLog.COLUMN_NAME_PAYMENT_REAL_RUN_LOG_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Page<PaymentRealRunLog> findAllByPaymentAliasId(Long paymentAliasId, boolean success, int page, int size) {
        List<Object> params = new ArrayList<>();
        Pageable pageable = generateSQLPageable(page, size);
        params.add(paymentAliasId);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PAYMENT_REAL_RUN_LOG.*, ");
        sql.append("(SELECT COUNT(1) FROM PAYMENT_PROCESS WHERE PAYMENT_REAL_RUN_LOG.PAYMENT_ALIAS_ID = PAYMENT_PROCESS.PAYMENT_ALIAS_ID AND PAYMENT_PROCESS.IS_PROPOSAL = 0 and IS_CHILD = 0) AS TOTAL, ");
        sql.append("(SELECT COUNT(1) FROM PAYMENT_PROCESS WHERE PAYMENT_REAL_RUN_LOG.PAYMENT_ALIAS_ID = PAYMENT_PROCESS.PAYMENT_ALIAS_ID AND IDEM_STATUS = 'S' AND PAYMENT_PROCESS.IS_PROPOSAL = 0 and IS_CHILD = 0) AS SUCCESS, ");
        sql.append("(SELECT COUNT(1) FROM PAYMENT_PROCESS WHERE PAYMENT_REAL_RUN_LOG.PAYMENT_ALIAS_ID = PAYMENT_PROCESS.PAYMENT_ALIAS_ID AND IDEM_STATUS = 'E' AND PAYMENT_PROCESS.IS_PROPOSAL = 0 and IS_CHILD = 0) AS ERROR ");
        sql.append(" FROM " + PaymentRealRunLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(PaymentRealRunLog.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
        if (success) {
            sql.append(" AND (MESSAGE_TYPE = 'S') ");
        } else {
            sql.append(" AND (MESSAGE_TYPE = 'E') ");
        }
        sql.append(" ORDER by LOG_DATE asc ");
        sql.append(generatePageable(pageable, params));

        log.info("sql find PAYMENT_REAL_RUN_LOG {}", sql);
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        List<PaymentRealRunLog> list = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        int count = count(paymentAliasId, success);
        return new PageImpl<>(list, pageable, count);
    }

    private int count(Long paymentAliasId, boolean success) {
        List<Object> params = new ArrayList<>();
        params.add(paymentAliasId);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM " + PaymentRealRunLog.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(PaymentRealRunLog.COLUMN_NAME_PAYMENT_ALIAS_ID + " = ? ");
        if (success) {
            sql.append(" AND (MESSAGE_TYPE = 'S') ");
        } else {
            sql.append(" AND (MESSAGE_TYPE = 'E') ");
        }
        sql.append(" ORDER by sequence asc ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql);
        Integer count = this.jdbcTemplate.queryForObject(sql.toString(), objParams, Integer.class);
        return null == count ? 0 : count;
    }

    @Override
    public void deletePaymentRealRunLog(Long paymentAliasId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE ");
        sql.append(" FROM " + PaymentRealRunLog.TABLE_NAME);
        sql.append("  ");
        sql.append(" WHERE ");
        sql.append(" 1 = 1  ");
        sql.append(" AND PAYMENT_ALIAS_ID  = ? ");


        this.jdbcTemplate.update(sql.toString(), paymentAliasId);

    }

    @Override
    public void saveBatch(List<PaymentRealRunLog> paymentRealRunLogs) {
        final int batchSize = 1000;
        List<List<PaymentRealRunLog>> paymentRealRunLogsBatchs = Lists.partition(paymentRealRunLogs, batchSize);
        final String sqlSave = "INSERT INTO PAYMENT_REAL_RUN_LOG (LOG_DATE,  PAYMENT_ALIAS_ID, MESSAGE_TEXT, MESSAGE_TYPE, SEQUENCE) VALUES ( ?,  ?, ?, ?, ?)";
        for (List<PaymentRealRunLog> batch : paymentRealRunLogsBatchs) {
            this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    PaymentRealRunLog paymentProposalLog = batch.get(i);
//                    ps.setLong(1, paymentProposalLog.getId());
                    ps.setTimestamp(1, paymentProposalLog.getLogDate());
                    ps.setLong(2, paymentProposalLog.getPaymentAliasId());
                    ps.setString(3, paymentProposalLog.getMessageText());
                    ps.setString(4, paymentProposalLog.getMessageType());
                    ps.setLong(5, paymentProposalLog.getSequence());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public Long getSequenceByPaymentAliasId(Long paymentAliasId) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT max(SEQUENCE) ");
        sql.append(" FROM " + PaymentRealRunLog.TABLE_NAME);
        sql.append("  ");
        sql.append(" WHERE ");
        sql.append(" 1 = 1  ");
        sql.append(" AND PAYMENT_ALIAS_ID  = ? ");
        params.add(paymentAliasId);

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    private Pageable generateSQLPageable(int page, int size) {
        return PageRequest.of(page - 1, size);
    }
}
