package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileSequence;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcAutoRepository;
import th.com.bloomcode.paymentservice.repository.payment.GenerateFileSequenceRepository;
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
public class GenerateFileSequenceRepositoryImpl extends MetadataJdbcAutoRepository<GenerateFileSequence, Long> implements GenerateFileSequenceRepository {

    static BeanPropertyRowMapper<GenerateFileSequence> beanPropertyRowMapper = new BeanPropertyRowMapper<>(GenerateFileSequence.class);
    private final JdbcTemplate jdbcTemplate;


    static Updater<GenerateFileSequence> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(GenerateFileSequence.COLUMN_NAME_GENERATE_FILE_SEQUENCE_ID, t.getId());
        mapping.put(GenerateFileSequence.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(GenerateFileSequence.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(GenerateFileSequence.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(GenerateFileSequence.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(GenerateFileSequence.COLUMN_NAME_SEQUENCE_NAME, t.getSequenceName());
        mapping.put(GenerateFileSequence.COLUMN_NAME_EFFECTIVE_DATE, t.getEffectiveDate());
        mapping.put(GenerateFileSequence.COLUMN_NAME_SEQUENCE_FROM, t.getSequenceFrom());
        mapping.put(GenerateFileSequence.COLUMN_NAME_SEQUENCE_TO, t.getSequenceTo());
        mapping.put(GenerateFileSequence.COLUMN_NAME_SEQUENCE_NO, t.getSequenceNo());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
        entry(GenerateFileSequence.COLUMN_NAME_GENERATE_FILE_SEQUENCE_ID, Types.BIGINT),
        entry(GenerateFileSequence.COLUMN_NAME_CREATED, Types.TIMESTAMP),
        entry(GenerateFileSequence.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
        entry(GenerateFileSequence.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
        entry(GenerateFileSequence.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
        entry(GenerateFileSequence.COLUMN_NAME_SEQUENCE_NAME, Types.NVARCHAR),
        entry(GenerateFileSequence.COLUMN_NAME_EFFECTIVE_DATE, Types.TIMESTAMP),
        entry(GenerateFileSequence.COLUMN_NAME_SEQUENCE_FROM, Types.BIGINT),
        entry(GenerateFileSequence.COLUMN_NAME_SEQUENCE_TO, Types.BIGINT),
        entry(GenerateFileSequence.COLUMN_NAME_SEQUENCE_NO, Types.BIGINT)
    );

    static RowMapper<GenerateFileSequence> userRowMapper = (rs, rowNum) -> new GenerateFileSequence(
        rs.getLong(GenerateFileSequence.COLUMN_NAME_GENERATE_FILE_SEQUENCE_ID),
        rs.getTimestamp(GenerateFileSequence.COLUMN_NAME_CREATED),
        rs.getString(GenerateFileSequence.COLUMN_NAME_CREATED_BY),
        rs.getTimestamp(GenerateFileSequence.COLUMN_NAME_UPDATED),
        rs.getString(GenerateFileSequence.COLUMN_NAME_UPDATED_BY),
        rs.getString(GenerateFileSequence.COLUMN_NAME_SEQUENCE_NAME),
        rs.getTimestamp(GenerateFileSequence.COLUMN_NAME_EFFECTIVE_DATE),
        rs.getLong(GenerateFileSequence.COLUMN_NAME_SEQUENCE_FROM),
        rs.getLong(GenerateFileSequence.COLUMN_NAME_SEQUENCE_TO),
        rs.getLong(GenerateFileSequence.COLUMN_NAME_SEQUENCE_NO)
    );

    public GenerateFileSequenceRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, generateFileAliasUpdater, updaterType, GenerateFileSequence.TABLE_NAME, GenerateFileSequence.COLUMN_NAME_GENERATE_FILE_SEQUENCE_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long getCurrentSeq(String seqName, Timestamp effectiveDate) {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM GENERATE_FILE_SEQUENCE ");
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(seqName)) {
            sql.append(SqlUtil.whereClause(seqName, "SEQUENCE_NAME", params));
        }
        if (!Util.isEmpty(effectiveDate)) {
            sql.append(SqlUtil.whereClauseEqual(effectiveDate, "EFFECTIVE_DATE", params));
        }

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        List<GenerateFileSequence> generateFileSequences = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!generateFileSequences.isEmpty()) {
            GenerateFileSequence generateFileSequence = generateFileSequences.get(0);
            Long seqNo = generateFileSequence.getSequenceNo();
            generateFileSequence.setSequenceNo(generateFileSequence.getSequenceNo() + 1);
            this.save(generateFileSequence);
            return seqNo;
        } else {
            Long seqNoFrom = 0L;
            Long seqNoTo = 0L;
            Long seqNo = 0L;
            if ("PAIN_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.PAIN_SEQ_MIN_VALUE;
                seqNoTo = Constants.PAIN_SEQ_MAX_VALUE;
                seqNo = Constants.PAIN_SEQ_MIN_VALUE;
            } else if ("SWIFT_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.SWIFT_SEQ_MIN_VALUE;
                seqNoTo = Constants.SWIFT_SEQ_MAX_VALUE;
                seqNo = Constants.SWIFT_SEQ_MIN_VALUE;
            } else if ("SWIFT_RUNNING_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.SWIFT_RUNNING_SEQ_MIN_VALUE;
                seqNoTo = Constants.SWIFT_RUNNING_SEQ_MAX_VALUE;
                seqNo = Constants.SWIFT_RUNNING_SEQ_MIN_VALUE;
            } else if ("GIRO_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.GIRO_SEQ_MIN_VALUE;
                seqNoTo = Constants.GIRO_SEQ_MAX_VALUE;
                seqNo = Constants.GIRO_SEQ_MIN_VALUE;
            } else if ("INHOUSE_TMB_SEQ".equalsIgnoreCase(seqName) || "INHOUSE_GSB_SEQ".equalsIgnoreCase(seqName) || "INHOUSE_BAAC_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.INHOUSE_SEQ_MIN_VALUE;
                seqNoTo = Constants.INHOUSE_SEQ_MAX_VALUE;
                seqNo = Constants.INHOUSE_SEQ_MIN_VALUE;
            } else if ("GGIRO_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.GGIRO_SEQ_MIN_VALUE;
                seqNoTo = Constants.GGIRO_SEQ_MAX_VALUE;
                seqNo = Constants.GGIRO_SEQ_MIN_VALUE;
            } else if ("INH_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.INH_SEQ_MIN_VALUE;
                seqNoTo = Constants.INH_SEQ_MAX_VALUE;
                seqNo = Constants.INH_SEQ_MIN_VALUE;
            } else if ("GM_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.GM_SEQ_MIN_VALUE;
                seqNoTo = Constants.GM_SEQ_MAX_VALUE;
                seqNo = Constants.GM_SEQ_MIN_VALUE;
            }
            GenerateFileSequence generateFileSequence = new GenerateFileSequence();
            generateFileSequence.setSequenceName(seqName);
            generateFileSequence.setEffectiveDate(effectiveDate);
            generateFileSequence.setSequenceFrom(seqNoFrom);
            generateFileSequence.setSequenceTo(seqNoTo);
            generateFileSequence.setSequenceNo(seqNo + 1);
            generateFileSequence.setCreated(new Timestamp(new Date().getTime()));
            generateFileSequence.setCreatedBy(jwt.getSub());
            generateFileSequence.setUpdated(new Timestamp(new Date().getTime()));
            generateFileSequence.setUpdatedBy(jwt.getSub());
            this.save(generateFileSequence);
            return seqNo;
        }
    }

    @Override
    public Long getCurrentSeq(String seqName, Timestamp effectiveDate, int allocate) {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM GENERATE_FILE_SEQUENCE ");
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(seqName)) {
            sql.append(SqlUtil.whereClause(seqName, "SEQUENCE_NAME", params));
        }
        if (!Util.isEmpty(effectiveDate)) {
            sql.append(SqlUtil.whereClauseEqual(effectiveDate, "EFFECTIVE_DATE", params));
        }

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        List<GenerateFileSequence> generateFileSequences = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        log.info("generateFileSequences : {}", generateFileSequences);
        if (!generateFileSequences.isEmpty()) {
            GenerateFileSequence generateFileSequence = generateFileSequences.get(0);
            Long seqNo = generateFileSequence.getSequenceNo();
            generateFileSequence.setSequenceNo(generateFileSequence.getSequenceNo() + allocate);
            this.save(generateFileSequence);
            return seqNo;
        } else {
            Long seqNoFrom = 0L;
            Long seqNoTo = 0L;
            Long seqNo = 0L;
            if ("PAIN_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.PAIN_SEQ_MIN_VALUE;
                seqNoTo = Constants.PAIN_SEQ_MAX_VALUE;
                seqNo = Constants.PAIN_SEQ_MIN_VALUE;
            } else if ("SWIFT_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.SWIFT_SEQ_MIN_VALUE;
                seqNoTo = Constants.SWIFT_SEQ_MAX_VALUE;
                seqNo = Constants.SWIFT_SEQ_MIN_VALUE;
            } else if ("SWIFT_RUNNING_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.SWIFT_RUNNING_SEQ_MIN_VALUE;
                seqNoTo = Constants.SWIFT_RUNNING_SEQ_MAX_VALUE;
                seqNo = Constants.SWIFT_RUNNING_SEQ_MIN_VALUE;
            } else if ("GIRO_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.GIRO_SEQ_MIN_VALUE;
                seqNoTo = Constants.GIRO_SEQ_MAX_VALUE;
                seqNo = Constants.GIRO_SEQ_MIN_VALUE;
            } else if ("INHOUSE_TMB_SEQ".equalsIgnoreCase(seqName) || "INHOUSE_GSB_SEQ".equalsIgnoreCase(seqName) || "INHOUSE_BAAC_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.INHOUSE_SEQ_MIN_VALUE;
                seqNoTo = Constants.INHOUSE_SEQ_MAX_VALUE;
                seqNo = Constants.INHOUSE_SEQ_MIN_VALUE;
            } else if ("GGIRO_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.GGIRO_SEQ_MIN_VALUE;
                seqNoTo = Constants.GGIRO_SEQ_MAX_VALUE;
                seqNo = Constants.GGIRO_SEQ_MIN_VALUE;
            } else if ("INH_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.INH_SEQ_MIN_VALUE;
                seqNoTo = Constants.INH_SEQ_MAX_VALUE;
                seqNo = Constants.INH_SEQ_MIN_VALUE;
            } else if ("GM_SEQ".equalsIgnoreCase(seqName)) {
                seqNoFrom = Constants.GM_SEQ_MIN_VALUE;
                seqNoTo = Constants.GM_SEQ_MAX_VALUE;
                seqNo = Constants.GM_SEQ_MIN_VALUE;
            }
            GenerateFileSequence generateFileSequence = new GenerateFileSequence();
            generateFileSequence.setSequenceName(seqName);
            generateFileSequence.setEffectiveDate(effectiveDate);
            generateFileSequence.setSequenceFrom(seqNoFrom);
            generateFileSequence.setSequenceTo(seqNoTo);
            generateFileSequence.setSequenceNo(seqNo + allocate);
            generateFileSequence.setCreated(new Timestamp(new Date().getTime()));
            generateFileSequence.setCreatedBy(jwt.getSub());
            generateFileSequence.setUpdated(new Timestamp(new Date().getTime()));
            generateFileSequence.setUpdatedBy(jwt.getSub());
            this.save(generateFileSequence);
            return seqNo;
        }
    }
}
