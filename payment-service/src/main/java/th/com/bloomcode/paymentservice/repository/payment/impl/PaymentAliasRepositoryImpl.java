package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentAliasResponse;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentAliasRepository;
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
public class PaymentAliasRepositoryImpl extends MetadataJdbcRepository<PaymentAlias, Long> implements PaymentAliasRepository {

    static BeanPropertyRowMapper<PaymentAlias> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PaymentAlias.class);

    private final JdbcTemplate jdbcTemplate;

    static Updater<PaymentAlias> PaymentAliasUpdater = (t, mapping) -> {
        mapping.put(PaymentAlias.COLUMN_NAME_PAYMENT_ALIAS_ID, t.getId());
        mapping.put(PaymentAlias.COLUMN_NAME_PAYMENT_DATE, t.getPaymentDate());
        mapping.put(PaymentAlias.COLUMN_NAME_PAYMENT_NAME, t.getPaymentName().toUpperCase());
        mapping.put(PaymentAlias.COLUMN_NAME_PARAMETER_STATUS, t.getParameterStatus());
        mapping.put(PaymentAlias.COLUMN_NAME_PROPOSAL_STATUS, t.getProposalStatus());
        mapping.put(PaymentAlias.COLUMN_NAME_RUN_STATUS, t.getRunStatus());
        mapping.put(PaymentAlias.COLUMN_NAME_DOCUMENT_STATUS, t.getDocumentStatus());
        mapping.put(PaymentAlias.COLUMN_NAME_GENERATE_STATUS, t.getGenerateStatus());
        mapping.put(PaymentAlias.COLUMN_NAME_PROPOSAL_JOB_STATUS, t.getProposalJobStatus());
        mapping.put(PaymentAlias.COLUMN_NAME_RUN_JOB_STATUS, t.getRunJobStatus());
        mapping.put(PaymentAlias.COLUMN_NAME_PAYMENT_CREATED, t.getPaymentCreated());
        mapping.put(PaymentAlias.COLUMN_NAME_PAYMENT_POSTED, t.getPaymentPosted());
        mapping.put(PaymentAlias.COLUMN_NAME_JSON_TEXT, t.getJsonText());
        mapping.put(PaymentAlias.COLUMN_NAME_PROPOSAL_TOTAL_DOCUMENT, t.getProposalTotalDocument());
        mapping.put(PaymentAlias.COLUMN_NAME_PROPOSAL_SUCCESS_DOCUMENT, t.getProposalSuccessDocument());
        mapping.put(PaymentAlias.COLUMN_NAME_PROPOSAL_SCHEDULE_DATE, t.getProposalScheduleDate());
        mapping.put(PaymentAlias.COLUMN_NAME_RUN_TOTAL_DOCUMENT, t.getRunTotalDocument());
        mapping.put(PaymentAlias.COLUMN_NAME_RUN_SUCCESS_DOCUMENT, t.getRunSuccessDocument());
        mapping.put(PaymentAlias.COLUMN_NAME_RUN_SCHEDULE_DATE, t.getRunScheduleDate());
        mapping.put(PaymentAlias.COLUMN_NAME_PROPOSAL_TRIGGERS_ID, t.getProposalTriggersId());
        mapping.put(PaymentAlias.COLUMN_NAME_PAYMENT_TRIGGERS_ID, t.getPaymentTriggersId());
        mapping.put(PaymentAlias.COLUMN_NAME_PROPOSAL_START, t.getProposalStart());
        mapping.put(PaymentAlias.COLUMN_NAME_PROPOSAL_END, t.getProposalEnd());
        mapping.put(PaymentAlias.COLUMN_NAME_RUN_START, t.getRunStart());
        mapping.put(PaymentAlias.COLUMN_NAME_RUN_END, t.getRunEnd());
        mapping.put(PaymentAlias.COLUMN_NAME_RUN_REVERSE_DOCUMENT, t.getRunReverseDocument());
        mapping.put(PaymentAlias.COLUMN_NAME_IDEM_END, t.getIdemEnd());
        mapping.put(PaymentAlias.COLUMN_NAME_IDEM_CREATE_PAYMENT_REPLY, t.getIdemCreatePaymentReply());
        mapping.put(PaymentAlias.COLUMN_NAME_IDEM_REVERSE_PAYMENT_REPLY, t.getIdemReversePaymentReply());
        mapping.put(PaymentAlias.COLUMN_NAME_RUN_REPAIR_DOCUMENT, t.getRunRepairDocument());
        mapping.put(PaymentAlias.COLUMN_NAME_RUN_ERROR_DOCUMENT, t.getRunErrorDocument());
        mapping.put(PaymentAlias.COLUMN_NAME_CREATED, new Timestamp(System.currentTimeMillis()));
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(PaymentAlias.COLUMN_NAME_PAYMENT_ALIAS_ID, Types.BIGINT),
            entry(PaymentAlias.COLUMN_NAME_PAYMENT_DATE, Types.TIMESTAMP),
            entry(PaymentAlias.COLUMN_NAME_PAYMENT_NAME, Types.NVARCHAR),
            entry(PaymentAlias.COLUMN_NAME_PARAMETER_STATUS, Types.NVARCHAR),
            entry(PaymentAlias.COLUMN_NAME_PROPOSAL_STATUS, Types.NVARCHAR),
            entry(PaymentAlias.COLUMN_NAME_RUN_STATUS, Types.NVARCHAR),
            entry(PaymentAlias.COLUMN_NAME_DOCUMENT_STATUS, Types.NVARCHAR),
            entry(PaymentAlias.COLUMN_NAME_GENERATE_STATUS, Types.NVARCHAR),
            entry(PaymentAlias.COLUMN_NAME_PROPOSAL_JOB_STATUS, Types.NVARCHAR),
            entry(PaymentAlias.COLUMN_NAME_RUN_JOB_STATUS, Types.NVARCHAR),
            entry(PaymentAlias.COLUMN_NAME_PAYMENT_CREATED, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_PAYMENT_POSTED, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_JSON_TEXT, Types.NVARCHAR),
            entry(PaymentAlias.COLUMN_NAME_PROPOSAL_TOTAL_DOCUMENT, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_PROPOSAL_SUCCESS_DOCUMENT, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_PROPOSAL_SCHEDULE_DATE, Types.TIMESTAMP),
            entry(PaymentAlias.COLUMN_NAME_RUN_TOTAL_DOCUMENT, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_RUN_SUCCESS_DOCUMENT, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_RUN_SCHEDULE_DATE, Types.TIMESTAMP),
            entry(PaymentAlias.COLUMN_NAME_PROPOSAL_TRIGGERS_ID, Types.BIGINT),
            entry(PaymentAlias.COLUMN_NAME_PAYMENT_TRIGGERS_ID, Types.BIGINT),
            entry(PaymentAlias.COLUMN_NAME_PROPOSAL_START, Types.TIMESTAMP),
            entry(PaymentAlias.COLUMN_NAME_PROPOSAL_END, Types.TIMESTAMP),
            entry(PaymentAlias.COLUMN_NAME_RUN_START, Types.TIMESTAMP),
            entry(PaymentAlias.COLUMN_NAME_RUN_END, Types.TIMESTAMP),
            entry(PaymentAlias.COLUMN_NAME_RUN_REVERSE_DOCUMENT, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_IDEM_END, Types.TIMESTAMP),
            entry(PaymentAlias.COLUMN_NAME_IDEM_CREATE_PAYMENT_REPLY, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_IDEM_REVERSE_PAYMENT_REPLY, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_RUN_REPAIR_DOCUMENT, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_RUN_ERROR_DOCUMENT, Types.INTEGER),
            entry(PaymentAlias.COLUMN_NAME_CREATED, Types.TIMESTAMP)

    );

    static RowMapper<PaymentAlias> userRowMapper = (rs, rowNum) -> new PaymentAlias(
            rs.getLong(PaymentAlias.COLUMN_NAME_PAYMENT_ALIAS_ID),
            rs.getTimestamp(PaymentAlias.COLUMN_NAME_PAYMENT_DATE),
            rs.getString(PaymentAlias.COLUMN_NAME_PAYMENT_NAME),
            rs.getString(PaymentAlias.COLUMN_NAME_PARAMETER_STATUS),
            rs.getString(PaymentAlias.COLUMN_NAME_PROPOSAL_STATUS),
            rs.getString(PaymentAlias.COLUMN_NAME_RUN_STATUS),
            rs.getString(PaymentAlias.COLUMN_NAME_DOCUMENT_STATUS),
            rs.getString(PaymentAlias.COLUMN_NAME_GENERATE_STATUS),
            rs.getString(PaymentAlias.COLUMN_NAME_PROPOSAL_JOB_STATUS),
            rs.getString(PaymentAlias.COLUMN_NAME_RUN_JOB_STATUS),
            rs.getLong(PaymentAlias.COLUMN_NAME_PAYMENT_CREATED),
            rs.getLong(PaymentAlias.COLUMN_NAME_PAYMENT_POSTED),
            rs.getString(PaymentAlias.COLUMN_NAME_JSON_TEXT),
            rs.getInt(PaymentAlias.COLUMN_NAME_PROPOSAL_TOTAL_DOCUMENT),
            rs.getInt(PaymentAlias.COLUMN_NAME_PROPOSAL_SUCCESS_DOCUMENT),
            rs.getTimestamp(PaymentAlias.COLUMN_NAME_PROPOSAL_SCHEDULE_DATE),
            rs.getInt(PaymentAlias.COLUMN_NAME_RUN_TOTAL_DOCUMENT),
            rs.getInt(PaymentAlias.COLUMN_NAME_RUN_SUCCESS_DOCUMENT),
            rs.getTimestamp(PaymentAlias.COLUMN_NAME_RUN_SCHEDULE_DATE),
            rs.getLong(PaymentAlias.COLUMN_NAME_PROPOSAL_TRIGGERS_ID),
            rs.getLong(PaymentAlias.COLUMN_NAME_PAYMENT_TRIGGERS_ID),
            rs.getTimestamp(PaymentAlias.COLUMN_NAME_PROPOSAL_START),
            rs.getTimestamp(PaymentAlias.COLUMN_NAME_PROPOSAL_END),
            rs.getTimestamp(PaymentAlias.COLUMN_NAME_RUN_START),
            rs.getTimestamp(PaymentAlias.COLUMN_NAME_RUN_END),
            rs.getInt(PaymentAlias.COLUMN_NAME_RUN_REVERSE_DOCUMENT),
            rs.getTimestamp(PaymentAlias.COLUMN_NAME_IDEM_END),
            rs.getInt(PaymentAlias.COLUMN_NAME_IDEM_CREATE_PAYMENT_REPLY),
            rs.getInt(PaymentAlias.COLUMN_NAME_IDEM_REVERSE_PAYMENT_REPLY),
            rs.getInt(PaymentAlias.COLUMN_NAME_RUN_REPAIR_DOCUMENT),
            rs.getInt(PaymentAlias.COLUMN_NAME_RUN_ERROR_DOCUMENT)
    );


    static RowMapper<PaymentAliasResponse> rowMapperResponse = (rs, rowNum) -> new PaymentAliasResponse(
            rs.getLong(PaymentAliasResponse.COLUMN_NAME_PAYMENT_ALIAS_ID),
            rs.getTimestamp(PaymentAliasResponse.COLUMN_NAME_PAYMENT_DATE),
            rs.getString(PaymentAliasResponse.COLUMN_NAME_PAYMENT_NAME),
            rs.getString(PaymentAliasResponse.COLUMN_NAME_PARAMETER_STATUS),
            rs.getString(PaymentAliasResponse.COLUMN_NAME_PROPOSAL_STATUS),
            rs.getString(PaymentAliasResponse.COLUMN_NAME_RUN_STATUS),
            rs.getString(PaymentAliasResponse.COLUMN_NAME_PROPOSAL_JOB_STATUS),
            rs.getString(PaymentAliasResponse.COLUMN_NAME_RUN_JOB_STATUS),
            rs.getString(PaymentAliasResponse.COLUMN_NAME_PARAMETER_JSON_TEXT),
            rs.getString(PaymentAliasResponse.COLUMN_NAME_STATUS_NAME),
            rs.getTimestamp(PaymentAliasResponse.COLUMN_NAME_PROPOSAL_START),
            rs.getTimestamp(PaymentAliasResponse.COLUMN_NAME_PROPOSAL_END),
            rs.getTimestamp(PaymentAliasResponse.COLUMN_NAME_RUN_START),
            rs.getTimestamp(PaymentAliasResponse.COLUMN_NAME_RUN_END));

    public PaymentAliasRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, PaymentAliasUpdater, updaterType, PaymentAlias.TABLE_NAME, PaymentAlias.COLUMN_NAME_PAYMENT_ALIAS_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PaymentAlias findOneById(Long id) {
        if (id == null) {
            return null;
        }
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT *           ");
        sql.append("          FROM PAYMENT_ALIAS PA  ");
        sql.append("          WHERE 1=1          ");

        sql.append(SqlUtil.whereClause(id, "PA.ID", params));
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<PaymentAlias> paymentAlias = this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
        if (!paymentAlias.isEmpty()) {
            return paymentAlias.get(0);
        } else {
            return null;
        }
    }

    @Override
    public PaymentAlias findOneByPaymentDateAndPaymentName(Timestamp paymentDate, String paymentName) {
        List<Object> params = new ArrayList<>();
//        params.add(paymentName);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + PaymentAlias.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(" 1 = 1 ");

        sql.append(SqlUtil.whereClause(paymentName, PaymentAlias.COLUMN_NAME_PAYMENT_NAME, params));
        sql.append(SqlUtil.whereClauseEqual(paymentDate, PaymentAlias.COLUMN_NAME_PAYMENT_DATE, params));


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql);
        List<PaymentAlias> paymentAliases = this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
        if (!paymentAliases.isEmpty()) {
            return paymentAliases.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<PaymentAliasResponse> findByCondition(String value) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
               sql.append("          SELECT ID, PAYMENT_DATE,           ");
        sql.append("          PAYMENT_NAME,           ");
        sql.append("          PARAMETER_STATUS,           ");
        sql.append("          PROPOSAL_STATUS,           ");
        sql.append("          RUN_STATUS,           ");
        sql.append("          PROPOSAL_JOB_STATUS,           ");
        sql.append("          RUN_JOB_STATUS,           ");
        sql.append("          RUN_START,           ");
        sql.append("          RUN_END,           ");
        sql.append("          PROPOSAL_START,           ");
        sql.append("          PROPOSAL_END,           ");
        sql.append("          JSON_TEXT       AS PARAMETER_JSON_TEXT,          ");
        sql.append("          CASE           ");
        sql.append("          WHEN RUN_JOB_STATUS IS NOT NULL AND RUN_JOB_STATUS = 'W' THEN 'ตั้งเวลาชำระเงินแล้ว'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'S' AND NVL(IDEM_CREATE_PAYMENT_REPLY,0) = NVL(RUN_TOTAL_DOCUMENT,0) THEN 'ชำระเงินสำเร็จ'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'S' AND NVL(IDEM_CREATE_PAYMENT_REPLY,0) != NVL(RUN_TOTAL_DOCUMENT,0) THEN 'กำลังประมวลผลชำระเงิน'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'P' THEN 'กำลังประมวลผลชำระเงิน'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'R' THEN 'กำลังกลับรายการเอกสารจ่าย'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'E' THEN 'ชำระเงินไม่สำเร็จ'           ");
        sql.append("          WHEN PROPOSAL_JOB_STATUS IS NOT NULL AND PROPOSAL_JOB_STATUS = 'W' THEN 'ตั้งเวลาข้อเสนอแล้ว'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'S' THEN 'สร้างข้อเสนอสำเร็จ'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'P' THEN 'กำลังสร้างข้อเสนอ'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'E' THEN 'สร้างข้อเสนอไม่สำเร็จ'           ");
        sql.append("          WHEN PARAMETER_STATUS IS NOT NULL AND PARAMETER_STATUS = 'S' THEN 'บันทึกพารามิเตอร์สำเร็จ'           ");
        sql.append("          ELSE '' END AS STATUS_NAME           ");

        sql.append("          FROM PAYMENT_ALIAS           ");
        sql.append("          WHERE 1=1           ");
        sql.append("          AND PAYMENT_DATE >= ADD_MONTHS(SYSDATE, -3)           ");


        if (!Util.isEmpty(value)) {
            String textSearch = value.replace("*", "%");
            params.add(textSearch);
            sql.append(" AND " + PaymentAlias.COLUMN_NAME_PAYMENT_NAME + " LIKE UPPER ( " + "  ? " + " ) ");
        }

        sql.append("      ORDER BY PAYMENT_DATE DESC , CREATED DESC       ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        this.jdbcTemplate.setFetchSize(5000);
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperResponse);
    }

    @Override
    public List<PaymentAliasResponse> findCreateJobByCondition(String value) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT ID, PAYMENT_DATE,           ");
        sql.append("          PAYMENT_NAME,           ");
        sql.append("          PARAMETER_STATUS,           ");
        sql.append("          PROPOSAL_STATUS,           ");
        sql.append("          RUN_STATUS,           ");
        sql.append("          PROPOSAL_JOB_STATUS,           ");
        sql.append("          RUN_JOB_STATUS,           ");
        sql.append("          RUN_START,           ");
        sql.append("          RUN_END,           ");
        sql.append("          PROPOSAL_START,           ");
        sql.append("          PROPOSAL_END,           ");
        sql.append("          JSON_TEXT       AS PARAMETER_JSON_TEXT,          ");
        sql.append("          CASE           ");
        sql.append("          WHEN RUN_JOB_STATUS IS NOT NULL AND RUN_JOB_STATUS = 'W' THEN 'ตั้งเวลาชำระเงินแล้ว'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'S' AND NVL(IDEM_CREATE_PAYMENT_REPLY,0) = NVL(RUN_TOTAL_DOCUMENT,0) THEN 'ชำระเงินสำเร็จ'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'S' AND NVL(IDEM_CREATE_PAYMENT_REPLY,0) != NVL(RUN_TOTAL_DOCUMENT,0) THEN 'กำลังประมวลผลชำระเงิน'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'P' THEN 'กำลังประมวลผลชำระเงิน'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'R' THEN 'กำลังกลับรายการเอกสารจ่าย'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'E' THEN 'ชำระเงินไม่สำเร็จ'           ");
        sql.append("          WHEN PROPOSAL_JOB_STATUS IS NOT NULL AND PROPOSAL_JOB_STATUS = 'W' THEN 'ตั้งเวลาข้อเสนอแล้ว'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'S' THEN 'สร้างข้อเสนอสำเร็จ'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'P' THEN 'กำลังสร้างข้อเสนอ'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'E' THEN 'สร้างข้อเสนอไม่สำเร็จ'           ");
        sql.append("          WHEN PARAMETER_STATUS IS NOT NULL AND PARAMETER_STATUS = 'S' THEN 'บันทึกพารามิเตอร์สำเร็จ'           ");
        sql.append("          ELSE '' END AS STATUS_NAME,           ");
        sql.append("          PROPOSAL_JOB_STATUS           ");
        sql.append("          FROM PAYMENT_ALIAS           ");
        sql.append("          WHERE 1=1           ");
        sql.append("          AND PARAMETER_STATUS = 'S'           ");
        sql.append("          AND (RUN_STATUS IS NULL OR RUN_STATUS <> 'S' OR RUN_STATUS <> 'R')           ");

        sql.append("          AND (RUN_JOB_STATUS IS NULL OR RUN_JOB_STATUS = 'E' )         ");


        if (!Util.isEmpty(value)) {
            String textSearch = value.replace("*", "%");
            params.add(textSearch);
            sql.append(" AND " + PaymentAlias.COLUMN_NAME_PAYMENT_NAME + " LIKE UPPER ( " + "  ? " + " ) ");
        }


        sql.append("      ORDER BY PAYMENT_DATE DESC , CREATED DESC       ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperResponse);
    }

    @Override
    public List<PaymentAliasResponse> findCreateJobByCondition(Date paymentDate, String paymentName) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT ID, PAYMENT_DATE,           ");
        sql.append("          PAYMENT_NAME,           ");
        sql.append("          PARAMETER_STATUS,           ");
        sql.append("          PROPOSAL_STATUS,           ");
        sql.append("          RUN_STATUS,           ");
        sql.append("          PROPOSAL_JOB_STATUS,           ");
        sql.append("          RUN_JOB_STATUS,           ");
        sql.append("          RUN_START,           ");
        sql.append("          RUN_END,           ");
        sql.append("          PROPOSAL_START,           ");
        sql.append("          PROPOSAL_END,           ");
        sql.append("          JSON_TEXT       AS PARAMETER_JSON_TEXT,          ");
        sql.append("          CASE           ");
        sql.append("          WHEN RUN_JOB_STATUS IS NOT NULL AND RUN_JOB_STATUS = 'W' THEN 'ตั้งเวลาชำระเงินแล้ว'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'S' AND NVL(IDEM_CREATE_PAYMENT_REPLY,0) = NVL(RUN_TOTAL_DOCUMENT,0) THEN 'ชำระเงินสำเร็จ'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'S' AND NVL(IDEM_CREATE_PAYMENT_REPLY,0) != NVL(RUN_TOTAL_DOCUMENT,0) THEN 'กำลังประมวลผลชำระเงิน'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'P' THEN 'กำลังประมวลผลชำระเงิน'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'R' THEN 'กำลังกลับรายการเอกสารจ่าย'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'E' THEN 'ชำระเงินไม่สำเร็จ'           ");
        sql.append("          WHEN PROPOSAL_JOB_STATUS IS NOT NULL AND PROPOSAL_JOB_STATUS = 'W' THEN 'ตั้งเวลาข้อเสนอแล้ว'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'S' THEN 'สร้างข้อเสนอสำเร็จ'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'P' THEN 'กำลังสร้างข้อเสนอ'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'E' THEN 'สร้างข้อเสนอไม่สำเร็จ'           ");
        sql.append("          WHEN PARAMETER_STATUS IS NOT NULL AND PARAMETER_STATUS = 'S' THEN 'บันทึกพารามิเตอร์สำเร็จ'           ");
        sql.append("          ELSE '' END AS STATUS_NAME,           ");
        sql.append("          PROPOSAL_JOB_STATUS           ");
        sql.append("          FROM PAYMENT_ALIAS           ");
        sql.append("          WHERE 1=1           ");
        sql.append("          AND PARAMETER_STATUS = 'S'           ");
        sql.append("          AND (RUN_STATUS IS NULL OR RUN_STATUS <> 'S' OR RUN_STATUS <> 'R')           ");

        sql.append("          AND (RUN_JOB_STATUS IS NULL OR RUN_JOB_STATUS = 'E' )         ");

        sql.append(SqlUtil.whereClause(paymentName, PaymentAlias.COLUMN_NAME_PAYMENT_NAME, params));
        sql.append(SqlUtil.whereClauseEqual(paymentDate, PaymentAlias.COLUMN_NAME_PAYMENT_DATE, params));

        sql.append("      ORDER BY PAYMENT_DATE DESC , CREATED DESC       ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperResponse);
    }

    @Override
    public List<PaymentAliasResponse> findSearchJobByCondition(String value) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT ID, PAYMENT_DATE,           ");
        sql.append("          PAYMENT_NAME,           ");
        sql.append("          PARAMETER_STATUS,           ");
        sql.append("          PROPOSAL_STATUS,           ");
        sql.append("          RUN_STATUS,           ");
        sql.append("          PROPOSAL_JOB_STATUS,           ");
        sql.append("          RUN_JOB_STATUS,           ");
        sql.append("          RUN_START,           ");
        sql.append("          RUN_END,           ");
        sql.append("          PROPOSAL_START,           ");
        sql.append("          PROPOSAL_END,           ");
        sql.append("          JSON_TEXT       AS PARAMETER_JSON_TEXT,          ");
        sql.append("          CASE           ");
        sql.append("          WHEN RUN_JOB_STATUS IS NOT NULL AND RUN_JOB_STATUS = 'W' THEN 'ตั้งเวลาชำระเงินแล้ว'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'S' AND NVL(IDEM_CREATE_PAYMENT_REPLY,0) = NVL(RUN_TOTAL_DOCUMENT,0) THEN 'ชำระเงินสำเร็จ'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'S' AND NVL(IDEM_CREATE_PAYMENT_REPLY,0) != NVL(RUN_TOTAL_DOCUMENT,0) THEN 'กำลังประมวลผลชำระเงิน'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'P' THEN 'กำลังประมวลผลชำระเงิน'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'R' THEN 'กำลังกลับรายการเอกสารจ่าย'           ");
        sql.append("          WHEN RUN_STATUS IS NOT NULL AND RUN_STATUS = 'E' THEN 'ชำระเงินไม่สำเร็จ'           ");
        sql.append("          WHEN PROPOSAL_JOB_STATUS IS NOT NULL AND PROPOSAL_JOB_STATUS = 'W' THEN 'ตั้งเวลาข้อเสนอแล้ว'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'S' THEN 'สร้างข้อเสนอสำเร็จ'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'P' THEN 'กำลังสร้างข้อเสนอ'           ");
        sql.append("          WHEN PROPOSAL_STATUS IS NOT NULL AND PROPOSAL_STATUS = 'E' THEN 'สร้างข้อเสนอไม่สำเร็จ'           ");
        sql.append("          WHEN PARAMETER_STATUS IS NOT NULL AND PARAMETER_STATUS = 'S' THEN 'บันทึกพารามิเตอร์สำเร็จ'           ");
        sql.append("          ELSE '' END AS STATUS_NAME,           ");
        sql.append("          PROPOSAL_JOB_STATUS           ");
        sql.append("          FROM PAYMENT_ALIAS           ");
        sql.append("          WHERE 1=1           ");
        sql.append("          AND PARAMETER_STATUS = 'S'           ");
//        sql.append("          AND PROPOSAL_JOB_STATUS IS NOT NULL           ");
//        sql.append("          AND (RUN_STATUS IS NULL OR RUN_STATUS <> 'S')           ");

        // benz edit for search all payment include not job and job
//        sql.append("          AND PROPOSAL_JOB_STATUS IS NOT NULL OR RUN_JOB_STATUS IS NOT NULL           ");


        if (!Util.isEmpty(value)) {
            String textSearch = value.replace("*", "%");
            params.add(textSearch);
            sql.append(" AND " + PaymentAlias.COLUMN_NAME_PAYMENT_NAME + " LIKE UPPER ( " + "  ? " + " ) ");
        }


        sql.append("      ORDER BY PAYMENT_DATE DESC , CREATED DESC       ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, rowMapperResponse);
    }
}
