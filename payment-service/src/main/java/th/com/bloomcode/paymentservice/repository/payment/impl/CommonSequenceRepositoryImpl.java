package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.CommonSequence;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcAutoRepository;
import th.com.bloomcode.paymentservice.repository.payment.CommonSequenceRepository;
import th.com.bloomcode.paymentservice.util.Constants;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class CommonSequenceRepositoryImpl extends MetadataJdbcAutoRepository<CommonSequence, Long> implements CommonSequenceRepository {

    static BeanPropertyRowMapper<CommonSequence> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CommonSequence.class);
    private final JdbcTemplate jdbcTemplate;


    static Updater<CommonSequence> CommonAliasUpdater = (t, mapping) -> {
        mapping.put(CommonSequence.COLUMN_NAME_COMMON_SEQUENCE_ID, t.getId());
        mapping.put(CommonSequence.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(CommonSequence.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(CommonSequence.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(CommonSequence.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(CommonSequence.COLUMN_NAME_SEQUENCE_NAME, t.getSequenceName());
        mapping.put(CommonSequence.COLUMN_NAME_SEQUENCE_KEY, t.getSequenceKey());
        mapping.put(CommonSequence.COLUMN_NAME_SEQUENCE_FROM, t.getSequenceFrom());
        mapping.put(CommonSequence.COLUMN_NAME_SEQUENCE_TO, t.getSequenceTo());
        mapping.put(CommonSequence.COLUMN_NAME_SEQUENCE_NO, t.getSequenceNo());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
        entry(CommonSequence.COLUMN_NAME_COMMON_SEQUENCE_ID, Types.BIGINT),
        entry(CommonSequence.COLUMN_NAME_CREATED, Types.TIMESTAMP),
        entry(CommonSequence.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
        entry(CommonSequence.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
        entry(CommonSequence.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
        entry(CommonSequence.COLUMN_NAME_SEQUENCE_NAME, Types.NVARCHAR),
        entry(CommonSequence.COLUMN_NAME_SEQUENCE_KEY, Types.NVARCHAR),
        entry(CommonSequence.COLUMN_NAME_SEQUENCE_FROM, Types.BIGINT),
        entry(CommonSequence.COLUMN_NAME_SEQUENCE_TO, Types.BIGINT),
        entry(CommonSequence.COLUMN_NAME_SEQUENCE_NO, Types.BIGINT)
    );

    static RowMapper<CommonSequence> userRowMapper = (rs, rowNum) -> new CommonSequence(
        rs.getLong(CommonSequence.COLUMN_NAME_COMMON_SEQUENCE_ID),
        rs.getTimestamp(CommonSequence.COLUMN_NAME_CREATED),
        rs.getString(CommonSequence.COLUMN_NAME_CREATED_BY),
        rs.getTimestamp(CommonSequence.COLUMN_NAME_UPDATED),
        rs.getString(CommonSequence.COLUMN_NAME_UPDATED_BY),
        rs.getString(CommonSequence.COLUMN_NAME_SEQUENCE_NAME),
        rs.getString(CommonSequence.COLUMN_NAME_SEQUENCE_KEY),
        rs.getLong(CommonSequence.COLUMN_NAME_SEQUENCE_FROM),
        rs.getLong(CommonSequence.COLUMN_NAME_SEQUENCE_TO),
        rs.getLong(CommonSequence.COLUMN_NAME_SEQUENCE_NO)
    );

    public CommonSequenceRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, CommonAliasUpdater, updaterType, CommonSequence.TABLE_NAME, CommonSequence.COLUMN_NAME_COMMON_SEQUENCE_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long getCurrentSeq(String seqName, String seqKey) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM COMMON_SEQUENCE ");
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(seqName)) {
            sql.append(SqlUtil.whereClause(seqName, "SEQUENCE_NAME", params));
        }
        if (!Util.isEmpty(seqKey)) {
            sql.append(SqlUtil.whereClause(seqKey, "SEQUENCE_KEY", params));
        }

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        List<CommonSequence> CommonSequences = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!CommonSequences.isEmpty()) {
            CommonSequence CommonSequence = CommonSequences.get(0);
            Long seqNo = CommonSequence.getSequenceNo();
            CommonSequence.setSequenceNo(seqNo + 1);
            this.save(CommonSequence);
            return seqNo;
        } else {
            Long seqNoFrom = 0L;
            Long seqNoTo = 0L;
            Long seqNo = 0L;
            if ("LOG_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.LOG_SEQ_MIN_VALUE;
                seqNoTo = Constants.LOG_SEQ_MAX_VALUE;
                seqNo = Constants.LOG_SEQ_MIN_VALUE;
            }
            CommonSequence CommonSequence = new CommonSequence();
            CommonSequence.setSequenceName(seqName);
            CommonSequence.setSequenceKey(seqKey);
            CommonSequence.setSequenceFrom(seqNoFrom);
            CommonSequence.setSequenceTo(seqNoTo);
            CommonSequence.setSequenceNo(seqNo + 1);
            CommonSequence.setCreated(new Timestamp(new Date().getTime()));
            CommonSequence.setCreatedBy("");
            CommonSequence.setUpdated(new Timestamp(new Date().getTime()));
            CommonSequence.setUpdatedBy("");
            this.save(CommonSequence);
            return seqNo;
        }
    }

    @Override
    public Long getCurrentSeq(String seqName, String seqKey, int allocate) {

        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM COMMON_SEQUENCE ");
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(seqName)) {
            sql.append(SqlUtil.whereClause(seqName, "SEQUENCE_NAME", params));
        }
        if (!Util.isEmpty(seqKey)) {
            sql.append(SqlUtil.whereClause(seqKey, "SEQUENCE_KEY", params));
        }

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        List<CommonSequence> CommonSequences = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!CommonSequences.isEmpty()) {
            CommonSequence CommonSequence = CommonSequences.get(0);
            Long seqNo = CommonSequence.getSequenceNo();
            CommonSequence.setSequenceNo(seqNo + 1);
            this.save(CommonSequence);
            return seqNo;
        } else {
            Long seqNoFrom = 0L;
            Long seqNoTo = 0L;
            Long seqNo = 0L;
            if ("LOG_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.LOG_SEQ_MIN_VALUE;
                seqNoTo = Constants.LOG_SEQ_MAX_VALUE;
                seqNo = Constants.LOG_SEQ_MIN_VALUE;
            }
            CommonSequence CommonSequence = new CommonSequence();
            CommonSequence.setSequenceName(seqName);
            CommonSequence.setSequenceKey(seqKey);
            CommonSequence.setSequenceFrom(seqNoFrom);
            CommonSequence.setSequenceTo(seqNoTo);
            CommonSequence.setSequenceNo(seqNo + allocate);
            CommonSequence.setCreated(new Timestamp(new Date().getTime()));
            CommonSequence.setCreatedBy("");
            CommonSequence.setUpdated(new Timestamp(new Date().getTime()));
            CommonSequence.setUpdatedBy("");
            this.save(CommonSequence);
            return seqNo;
        }
    }
}
