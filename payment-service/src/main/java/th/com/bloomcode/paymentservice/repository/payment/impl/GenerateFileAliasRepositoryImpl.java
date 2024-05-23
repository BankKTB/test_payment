package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.GenerateFileAliasResponse;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.GenerateFileAliasRepository;
import th.com.bloomcode.paymentservice.service.payment.PaymentAliasService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class GenerateFileAliasRepositoryImpl extends MetadataJdbcRepository<GenerateFileAlias, Long> implements GenerateFileAliasRepository {

    static BeanPropertyRowMapper<GenerateFileAlias> beanPropertyRowMapper = new BeanPropertyRowMapper<>(GenerateFileAlias.class);
    private final JdbcTemplate jdbcTemplate;
    private static PaymentAliasService paymentAliasService;


    static Updater<GenerateFileAlias> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(GenerateFileAlias.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, t.getId());
        mapping.put(GenerateFileAlias.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(GenerateFileAlias.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(GenerateFileAlias.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(GenerateFileAlias.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(GenerateFileAlias.COLUMN_NAME_GENERATE_FILE_DATE, t.getGenerateFileDate());
        mapping.put(GenerateFileAlias.COLUMN_NAME_GENERATE_FILE_NAME, t.getGenerateFileName());
        mapping.put(GenerateFileAlias.COLUMN_NAME_FILE_NAME, t.getFileName());
        mapping.put(GenerateFileAlias.COLUMN_NAME_SWIFT_AMOUNT_DAY, t.getSwiftAmountDay());
        mapping.put(GenerateFileAlias.COLUMN_NAME_SWIFT_DATE, t.getSwiftDate());
        mapping.put(GenerateFileAlias.COLUMN_NAME_SMART_AMOUNT_DAY, t.getSmartAmountDay());
        mapping.put(GenerateFileAlias.COLUMN_NAME_SMART_DATE, t.getSmartDate());
        mapping.put(GenerateFileAlias.COLUMN_NAME_GIRO_AMOUNT_DAY, t.getGiroAmountDay());
        mapping.put(GenerateFileAlias.COLUMN_NAME_GIRO_DATE, t.getGiroDate());
        mapping.put(GenerateFileAlias.COLUMN_NAME_INHOUSE_AMOUNT_DAY, t.getInhouseAmountDay());
        mapping.put(GenerateFileAlias.COLUMN_NAME_INHOUSE_DATE, t.getInhouseDate());
        mapping.put(GenerateFileAlias.COLUMN_NAME_IS_CREATE_AGAIN, t.isCreateAgain());
        mapping.put(GenerateFileAlias.COLUMN_NAME_IS_TEST_RUN, t.isTestRun());
        mapping.put(GenerateFileAlias.COLUMN_NAME_RUN_STATUS, t.getRunStatus());
        mapping.put(GenerateFileAlias.COLUMN_NAME_PAYMENT_ALIAS_ID, t.getPaymentAliasId());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(GenerateFileAlias.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, Types.BIGINT),
            entry(GenerateFileAlias.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(GenerateFileAlias.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(GenerateFileAlias.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
            entry(GenerateFileAlias.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
            entry(GenerateFileAlias.COLUMN_NAME_GENERATE_FILE_DATE, Types.TIMESTAMP),
            entry(GenerateFileAlias.COLUMN_NAME_GENERATE_FILE_NAME, Types.NVARCHAR),
            entry(GenerateFileAlias.COLUMN_NAME_FILE_NAME, Types.NVARCHAR),
            entry(GenerateFileAlias.COLUMN_NAME_SWIFT_AMOUNT_DAY, Types.INTEGER),
            entry(GenerateFileAlias.COLUMN_NAME_SWIFT_DATE, Types.TIMESTAMP),
            entry(GenerateFileAlias.COLUMN_NAME_SMART_AMOUNT_DAY, Types.INTEGER),
            entry(GenerateFileAlias.COLUMN_NAME_SMART_DATE, Types.TIMESTAMP),
            entry(GenerateFileAlias.COLUMN_NAME_GIRO_AMOUNT_DAY, Types.INTEGER),
            entry(GenerateFileAlias.COLUMN_NAME_GIRO_DATE, Types.TIMESTAMP),
            entry(GenerateFileAlias.COLUMN_NAME_INHOUSE_AMOUNT_DAY, Types.INTEGER),
            entry(GenerateFileAlias.COLUMN_NAME_INHOUSE_DATE, Types.TIMESTAMP),
            entry(GenerateFileAlias.COLUMN_NAME_IS_CREATE_AGAIN, Types.BOOLEAN),
            entry(GenerateFileAlias.COLUMN_NAME_IS_TEST_RUN, Types.BOOLEAN),
            entry(GenerateFileAlias.COLUMN_NAME_RUN_STATUS, Types.NVARCHAR),
            entry(GenerateFileAlias.COLUMN_NAME_PAYMENT_ALIAS_ID, Types.BIGINT)
    );

    static RowMapper<GenerateFileAlias> userRowMapper = (rs, rowNum) -> new GenerateFileAlias(
            rs.getLong(GenerateFileAlias.COLUMN_NAME_GENERATE_FILE_ALIAS_ID),
            rs.getTimestamp(GenerateFileAlias.COLUMN_NAME_CREATED),
            rs.getString(GenerateFileAlias.COLUMN_NAME_CREATED_BY),
            rs.getTimestamp(GenerateFileAlias.COLUMN_NAME_UPDATED),
            rs.getString(GenerateFileAlias.COLUMN_NAME_UPDATED_BY),
            rs.getTimestamp(GenerateFileAlias.COLUMN_NAME_GENERATE_FILE_DATE),
            rs.getString(GenerateFileAlias.COLUMN_NAME_GENERATE_FILE_NAME),
            rs.getString(GenerateFileAlias.COLUMN_NAME_FILE_NAME),
            rs.getInt(GenerateFileAlias.COLUMN_NAME_SWIFT_AMOUNT_DAY),
            rs.getTimestamp(GenerateFileAlias.COLUMN_NAME_SWIFT_DATE),
            rs.getInt(GenerateFileAlias.COLUMN_NAME_SMART_AMOUNT_DAY),
            rs.getTimestamp(GenerateFileAlias.COLUMN_NAME_SMART_DATE),
            rs.getInt(GenerateFileAlias.COLUMN_NAME_GIRO_AMOUNT_DAY),
            rs.getTimestamp(GenerateFileAlias.COLUMN_NAME_GIRO_DATE),
            rs.getInt(GenerateFileAlias.COLUMN_NAME_INHOUSE_AMOUNT_DAY),
            rs.getTimestamp(GenerateFileAlias.COLUMN_NAME_INHOUSE_DATE),
            rs.getBoolean(GenerateFileAlias.COLUMN_NAME_IS_CREATE_AGAIN),
            rs.getBoolean(GenerateFileAlias.COLUMN_NAME_IS_TEST_RUN),
            rs.getString(GenerateFileAlias.COLUMN_NAME_RUN_STATUS),
            rs.getLong(GenerateFileAlias.COLUMN_NAME_PAYMENT_ALIAS_ID),
            GenerateFileAliasRepositoryImpl.paymentAliasService.findOneById(rs.getLong(GenerateFileAlias.COLUMN_NAME_PAYMENT_ALIAS_ID))
    );

    static RowMapper<GenerateFileAliasResponse> userRowMapperDisplay = (rs, rowNum) -> new GenerateFileAliasResponse(

            rs.getLong(GenerateFileAliasResponse.COLUMN_NAME_PAYMENT_ALIAS_ID),
            rs.getString(GenerateFileAliasResponse.COLUMN_NAME_PAYMENT_RUN_STATUS),
            rs.getString(GenerateFileAliasResponse.COLUMN_NAME_FILE_NAME),
            rs.getInt(GenerateFileAliasResponse.COLUMN_NAME_SWIFT_AMOUNT_DAY),
            rs.getTimestamp(GenerateFileAliasResponse.COLUMN_NAME_SWIFT_DATE),
            rs.getInt(GenerateFileAliasResponse.COLUMN_NAME_SMART_AMOUNT_DAY),
            rs.getTimestamp(GenerateFileAliasResponse.COLUMN_NAME_SMART_DATE),
            rs.getInt(GenerateFileAliasResponse.COLUMN_NAME_GIRO_AMOUNT_DAY),
            rs.getTimestamp(GenerateFileAliasResponse.COLUMN_NAME_GIRO_DATE),
            rs.getInt(GenerateFileAliasResponse.COLUMN_NAME_INHOUSE_AMOUNT_DAY),
            rs.getTimestamp(GenerateFileAliasResponse.COLUMN_NAME_INHOUSE_DATE),
            rs.getTimestamp(GenerateFileAliasResponse.COLUMN_NAME_GENERATE_FILE_DATE),
            rs.getString(GenerateFileAliasResponse.COLUMN_NAME_GENERATE_FILE_NAME),
            rs.getString(GenerateFileAliasResponse.COLUMN_NAME_GENERATE_FILE_RUN_STATUS),
            rs.getString(GenerateFileAliasResponse.COLUMN_NAME_GENERATE_FILE_RUN_STATUS_NAME),
            rs.getBoolean(GenerateFileAliasResponse.COLUMN_NAME_GENERATE_FILE_CREATE_AGAIN),
            rs.getLong(GenerateFileAliasResponse.COLUMN_NAME_GENERATE_FILE_ALIAS_ID),
            rs.getInt(GenerateFileAliasResponse.COLUMN_NAME_RUN_TOTAL_DOCUMENT),
            rs.getInt(GenerateFileAliasResponse.COLUMN_NAME_RUN_SUCCESS_DOCUMENT),
            rs.getInt(GenerateFileAliasResponse.COLUMN_NAME_RUN_REVERSE_DOCUMENT),
            rs.getInt(GenerateFileAliasResponse.COLUMN_NAME_IDEM_CREATE_PAYMENT_REPLY),
            rs.getInt(GenerateFileAliasResponse.COLUMN_NAME_IDEM_REVERSE_PAYMENT_REPLY)

    );

    public GenerateFileAliasRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, PaymentAliasService paymentAliasService) {
        super(userRowMapper, generateFileAliasUpdater, updaterType, GenerateFileAlias.TABLE_NAME, GenerateFileAlias.COLUMN_NAME_GENERATE_FILE_ALIAS_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        GenerateFileAliasRepositoryImpl.paymentAliasService = paymentAliasService;
    }

    @Override
    public List<GenerateFileAliasResponse> findByCondition(String value) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PA.ID                                     AS PAYMENT_ALIAS_ID,           ");
        sql.append("          PA.RUN_STATUS                             AS PAYMENT_RUN_STATUS,           ");
        sql.append("          GA.FILE_NAME                              AS FILE_NAME,           ");
        sql.append("          NVL(GA.SWIFT_AMOUNT_DAY, 0)               AS SWIFT_AMOUNT_DAY,           ");
        sql.append("          GA.SWIFT_DATE                             AS SWIFT_DATE,           ");
        sql.append("          NVL(GA.SMART_AMOUNT_DAY, 0)               AS SMART_AMOUNT_DAY,           ");
        sql.append("          GA.SMART_DATE                             AS SMART_DATE,           ");
        sql.append("          NVL(GA.GIRO_AMOUNT_DAY, 0)                AS GIRO_AMOUNT_DAY,           ");
        sql.append("          GA.GIRO_DATE                              AS GIRO_DATE,           ");
        sql.append("          NVL(GA.INHOUSE_AMOUNT_DAY, 0)             AS INHOUSE_AMOUNT_DAY,           ");
        sql.append("          GA.INHOUSE_DATE                           AS INHOUSE_DATE,           ");
        sql.append("          PA.PAYMENT_DATE                           AS GENERATE_FILE_DATE,           ");
        sql.append("          PA.PAYMENT_NAME                           AS GENERATE_FILE_NAME,           ");
        sql.append("          CASE           ");
        sql.append("          WHEN GA.RUN_STATUS IS NULL THEN 'W' ELSE GA.RUN_STATUS END AS GENERATE_FILE_RUN_STATUS,           ");
        sql.append("          CASE           ");
        sql.append("          WHEN GA.RUN_STATUS IS NULL THEN 'รอดำเนินการสร้างไฟล์ชำระเงิน'           ");
        sql.append("          WHEN GA.RUN_STATUS = 'P' THEN 'กำลังประมวลผลการสร้างไฟล์'           ");
        sql.append("          WHEN GA.RUN_STATUS = 'S' THEN 'สร้างไฟล์ชำระเงินสำเร็จ'           ");
        sql.append("          ELSE 'สร้างไฟล์ชำระเงินไม่สำเร็จ' END AS GENERATE_FILE_RUN_STATUS_NAME,           ");
        sql.append("          GA.ID                                     AS GENERATE_FILE_ALIAS_ID,           ");
        sql.append("          GA.CREATE_AGAIN                           AS GENERATE_FILE_CREATE_AGAIN,           ");
        sql.append("          PA.RUN_TOTAL_DOCUMENT,           ");
        sql.append("          PA.RUN_SUCCESS_DOCUMENT,           ");
        sql.append("          PA.RUN_REVERSE_DOCUMENT,           ");
        sql.append("          PA.IDEM_CREATE_PAYMENT_REPLY,           ");
        sql.append("          PA.IDEM_REVERSE_PAYMENT_REPLY           ");
        sql.append("          FROM PAYMENT_ALIAS PA           ");
        sql.append("          LEFT JOIN GENERATE_FILE_ALIAS GA ON PA.ID = GA.PAYMENT_ALIAS_ID           ");
        sql.append("          WHERE PA.RUN_STATUS = 'S'           ");
        sql.append("          AND PA.RUN_REPAIR_DOCUMENT = 0           ");
        sql.append("          AND PA.RUN_TOTAL_DOCUMENT = PA.IDEM_CREATE_PAYMENT_REPLY           ");

        if (!Util.isEmpty(value)) {
            sql.append(SqlUtil.whereClauseOr(value, params, "PA.PAYMENT_NAME"));
        }
        sql.append("          ORDER BY PA.PAYMENT_DATE DESC           ");





        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapperDisplay);
    }

    @Override
    public List<GenerateFileAliasResponse> findByReturn(String value) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PA.ID                                     AS PAYMENT_ALIAS_ID,           ");
        sql.append("          PA.RUN_STATUS                             AS PAYMENT_RUN_STATUS,           ");
        sql.append("          GA.FILE_NAME                              AS FILE_NAME,           ");
        sql.append("          NVL(GA.SWIFT_AMOUNT_DAY, 0)               AS SWIFT_AMOUNT_DAY,           ");
        sql.append("          GA.SWIFT_DATE                             AS SWIFT_DATE,           ");
        sql.append("          NVL(GA.SMART_AMOUNT_DAY, 0)               AS SMART_AMOUNT_DAY,           ");
        sql.append("          GA.SMART_DATE                             AS SMART_DATE,           ");
        sql.append("          NVL(GA.GIRO_AMOUNT_DAY, 0)                AS GIRO_AMOUNT_DAY,           ");
        sql.append("          GA.GIRO_DATE                              AS GIRO_DATE,           ");
        sql.append("          NVL(GA.INHOUSE_AMOUNT_DAY, 0)             AS INHOUSE_AMOUNT_DAY,           ");
        sql.append("          GA.INHOUSE_DATE                           AS INHOUSE_DATE,           ");
        sql.append("          PA.PAYMENT_DATE                           AS GENERATE_FILE_DATE,           ");
        sql.append("          PA.PAYMENT_NAME                           AS GENERATE_FILE_NAME,           ");
        sql.append("          CASE           ");
        sql.append("          WHEN GA.RUN_STATUS IS NULL THEN 'W' ELSE GA.RUN_STATUS END AS GENERATE_FILE_RUN_STATUS,           ");
        sql.append("          CASE           ");
        sql.append("          WHEN GA.RUN_STATUS IS NULL THEN 'รอดำเนินการสร้างไฟล์ชำระเงิน'           ");
        sql.append("          WHEN GA.RUN_STATUS = 'P' THEN 'กำลังประมวลผลการสร้างไฟล์'           ");
        sql.append("          WHEN GA.RUN_STATUS = 'S' THEN 'สร้างไฟล์ชำระเงินสำเร็จ'           ");
        sql.append("          ELSE 'สร้างไฟล์ชำระเงินไม่สำเร็จ' END AS GENERATE_FILE_RUN_STATUS_NAME,           ");
        sql.append("          GA.ID                                     AS GENERATE_FILE_ALIAS_ID,           ");
        sql.append("          GA.CREATE_AGAIN                           AS GENERATE_FILE_CREATE_AGAIN,           ");
        sql.append("          PA.RUN_TOTAL_DOCUMENT,           ");
        sql.append("          PA.RUN_SUCCESS_DOCUMENT,           ");
        sql.append("          PA.RUN_REVERSE_DOCUMENT,           ");
        sql.append("          PA.IDEM_CREATE_PAYMENT_REPLY,           ");
        sql.append("          PA.IDEM_REVERSE_PAYMENT_REPLY           ");
        sql.append("          FROM PAYMENT_ALIAS PA           ");
        sql.append("          LEFT JOIN GENERATE_FILE_ALIAS GA ON PA.ID = GA.PAYMENT_ALIAS_ID           ");
        sql.append("          WHERE PA.RUN_STATUS = 'S' AND GA.RUN_STATUS = 'S'           ");

        if (!Util.isEmpty(value)) {
            sql.append(SqlUtil.whereClauseOr(value, params, "PA.PAYMENT_NAME"));
        }
        sql.append("          ORDER BY PA.PAYMENT_DATE DESC           ");





        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapperDisplay);
    }

    @Override
    public GenerateFileAliasResponse findOneByGenerateFileDateAndGenerateFileName(Timestamp generateFileDate, String generateFileName) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PA.ID                                     AS PAYMENT_ALIAS_ID,           ");
        sql.append("          PA.RUN_STATUS                             AS PAYMENT_RUN_STATUS,           ");
        sql.append("          GA.FILE_NAME                              AS FILE_NAME,           ");
        sql.append("          NVL(GA.SWIFT_AMOUNT_DAY, 0)               AS SWIFT_AMOUNT_DAY,           ");
        sql.append("          GA.SWIFT_DATE                             AS SWIFT_DATE,           ");
        sql.append("          NVL(GA.SMART_AMOUNT_DAY, 0)               AS SMART_AMOUNT_DAY,           ");
        sql.append("          GA.SMART_DATE                             AS SMART_DATE,           ");
        sql.append("          NVL(GA.GIRO_AMOUNT_DAY, 0)                AS GIRO_AMOUNT_DAY,           ");
        sql.append("          GA.GIRO_DATE                              AS GIRO_DATE,           ");
        sql.append("          NVL(GA.INHOUSE_AMOUNT_DAY, 0)             AS INHOUSE_AMOUNT_DAY,           ");
        sql.append("          GA.INHOUSE_DATE                           AS INHOUSE_DATE,           ");
        sql.append("          PA.PAYMENT_DATE                           AS GENERATE_FILE_DATE,           ");
        sql.append("          PA.PAYMENT_NAME                           AS GENERATE_FILE_NAME,           ");
        sql.append("          CASE           ");
        sql.append("          WHEN GA.RUN_STATUS IS NULL THEN '' ELSE GA.RUN_STATUS END AS GENERATE_FILE_RUN_STATUS,           ");
        sql.append("          CASE           ");
        sql.append("          WHEN GA.RUN_STATUS IS NULL THEN 'รอดำเนินการสร้างไฟล์ชำระเงิน'           ");
        sql.append("          WHEN GA.RUN_STATUS = 'P' THEN 'กำลังประมวลผลการสร้างไฟล์'           ");
        sql.append("          WHEN GA.RUN_STATUS = 'S' THEN 'สร้างไฟล์ชำระเงินสำเร็จ'           ");
        sql.append("          ELSE 'สร้างไฟล์ชำระเงินไม่สำเร็จ' END AS GENERATE_FILE_RUN_STATUS_NAME,           ");
        sql.append("          GA.ID                                     AS GENERATE_FILE_ALIAS_ID,           ");
        sql.append("          GA.CREATE_AGAIN                           AS GENERATE_FILE_CREATE_AGAIN,          ");
        sql.append("          PA.RUN_TOTAL_DOCUMENT,           ");
        sql.append("          PA.RUN_SUCCESS_DOCUMENT,           ");
        sql.append("          PA.RUN_REVERSE_DOCUMENT,           ");
        sql.append("          PA.IDEM_CREATE_PAYMENT_REPLY,           ");
        sql.append("          PA.IDEM_REVERSE_PAYMENT_REPLY           ");
        sql.append("          FROM PAYMENT_ALIAS PA           ");
        sql.append("          LEFT JOIN GENERATE_FILE_ALIAS GA ON PA.ID = GA.PAYMENT_ALIAS_ID           ");
        sql.append("          WHERE PA.RUN_STATUS = 'S'           ");
        sql.append("          AND PA.RUN_REPAIR_DOCUMENT = 0           ");
        sql.append("          AND PA.RUN_TOTAL_DOCUMENT = PA.IDEM_CREATE_PAYMENT_REPLY           ");

        if (!Util.isEmpty(generateFileDate)) {
            sql.append(SqlUtil.whereClause(generateFileDate, "PA.PAYMENT_DATE", params));
        }

        if (!Util.isEmpty(generateFileName)) {
            sql.append(SqlUtil.whereClause(generateFileName, "PA.PAYMENT_NAME", params));
        }
        sql.append("          ORDER BY PA.PAYMENT_DATE DESC           ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("objParams {}", objParams);
        log.info("sql {}", sql.toString());
        List<GenerateFileAliasResponse> generateFileAlias = this.jdbcTemplate.query(sql.toString(), objParams, userRowMapperDisplay);
        if (!generateFileAlias.isEmpty()) {
            return generateFileAlias.get(0);
        } else {
            return null;
        }
    }

    @Override
    public GenerateFileAliasResponse findOneByGenerateFileDateNotLessThanAndGenerateFileName(Timestamp generateFileDate, String generateFileName) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PA.ID                                     AS PAYMENT_ALIAS_ID,           ");
        sql.append("          PA.RUN_STATUS                             AS PAYMENT_RUN_STATUS,           ");
        sql.append("          GA.FILE_NAME                              AS FILE_NAME,           ");
        sql.append("          NVL(GA.SWIFT_AMOUNT_DAY, 0)               AS SWIFT_AMOUNT_DAY,           ");
        sql.append("          GA.SWIFT_DATE                             AS SWIFT_DATE,           ");
        sql.append("          NVL(GA.SMART_AMOUNT_DAY, 0)               AS SMART_AMOUNT_DAY,           ");
        sql.append("          GA.SMART_DATE                             AS SMART_DATE,           ");
        sql.append("          NVL(GA.GIRO_AMOUNT_DAY, 0)                AS GIRO_AMOUNT_DAY,           ");
        sql.append("          GA.GIRO_DATE                              AS GIRO_DATE,           ");
        sql.append("          NVL(GA.INHOUSE_AMOUNT_DAY, 0)             AS INHOUSE_AMOUNT_DAY,           ");
        sql.append("          GA.INHOUSE_DATE                           AS INHOUSE_DATE,           ");
        sql.append("          PA.PAYMENT_DATE                           AS GENERATE_FILE_DATE,           ");
        sql.append("          PA.PAYMENT_NAME                           AS GENERATE_FILE_NAME,           ");
        sql.append("          CASE           ");
        sql.append("          WHEN GA.RUN_STATUS IS NULL THEN 'W' ELSE GA.RUN_STATUS END AS GENERATE_FILE_RUN_STATUS,           ");
        sql.append("          CASE           ");
        sql.append("          WHEN GA.RUN_STATUS IS NULL THEN 'รอดำเนินการสร้างไฟล์ชำระเงิน'           ");
        sql.append("          WHEN GA.RUN_STATUS = 'P' THEN 'กำลังประมวลผลการสร้างไฟล์'           ");
        sql.append("          WHEN GA.RUN_STATUS = 'S' THEN 'สร้างไฟล์ชำระเงินสำเร็จ'           ");
        sql.append("          ELSE 'สร้างไฟล์ชำระเงินไม่สำเร็จ' END AS GENERATE_FILE_RUN_STATUS_NAME,           ");
        sql.append("          GA.ID                                     AS GENERATE_FILE_ALIAS_ID,           ");
        sql.append("          GA.CREATE_AGAIN                           AS GENERATE_FILE_CREATE_AGAIN,          ");
        sql.append("          PA.RUN_TOTAL_DOCUMENT,           ");
        sql.append("          PA.RUN_SUCCESS_DOCUMENT,           ");
        sql.append("          PA.RUN_REVERSE_DOCUMENT,           ");
        sql.append("          PA.IDEM_CREATE_PAYMENT_REPLY,           ");
        sql.append("          PA.IDEM_REVERSE_PAYMENT_REPLY           ");
        sql.append("          FROM PAYMENT_ALIAS PA           ");
        sql.append("          LEFT JOIN GENERATE_FILE_ALIAS GA ON PA.ID = GA.PAYMENT_ALIAS_ID           ");
        sql.append("          WHERE PA.RUN_STATUS = 'S'           ");
        sql.append("          AND PA.RUN_REPAIR_DOCUMENT = 0           ");
        sql.append("          AND PA.RUN_TOTAL_DOCUMENT = PA.IDEM_CREATE_PAYMENT_REPLY           ");

        if (!Util.isEmpty(generateFileDate)) {
            sql.append(SqlUtil.whereClauseEqual(generateFileDate, "PA.PAYMENT_DATE", params));
        }

        if (!Util.isEmpty(generateFileName)) {
            sql.append(SqlUtil.whereClause(generateFileName, "PA.PAYMENT_NAME", params));
        }
        sql.append("          ORDER BY PA.PAYMENT_DATE DESC           ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("objParams {}", objParams);
        log.info("sql {}", sql.toString());
        List<GenerateFileAliasResponse> generateFileAlias = this.jdbcTemplate.query(sql.toString(), objParams, userRowMapperDisplay);
        if (!generateFileAlias.isEmpty()) {
            return generateFileAlias.get(0);
        } else {
            return null;
        }
    }

    @Override
    public GenerateFileAlias findOneById(Long id) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT *           ");
        sql.append("          FROM GENERATE_FILE_ALIAS GA  ");
        sql.append("          WHERE 1=1          ");

        sql.append(SqlUtil.whereClause(id, "GA.ID", params));

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<GenerateFileAlias> generateFileAlias = this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
        if (!generateFileAlias.isEmpty()) {
            return generateFileAlias.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean existByGenerateFileDateAndGenerateFileName(Timestamp generateFileDate, String generateFileName) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT PA.ID                                     AS PAYMENT_ALIAS_ID,           ");
        sql.append("          PA.RUN_STATUS                             AS PAYMENT_RUN_STATUS,           ");
        sql.append("          GA.FILE_NAME                              AS FILE_NAME,           ");
        sql.append("          NVL(GA.SWIFT_AMOUNT_DAY, 0)               AS SWIFT_AMOUNT_DAY,           ");
        sql.append("          GA.SWIFT_DATE                             AS SWIFT_DATE,           ");
        sql.append("          NVL(GA.SMART_AMOUNT_DAY, 0)               AS SMART_AMOUNT_DAY,           ");
        sql.append("          GA.SMART_DATE                             AS SMART_DATE,           ");
        sql.append("          NVL(GA.GIRO_AMOUNT_DAY, 0)                AS GIRO_AMOUNT_DAY,           ");
        sql.append("          GA.GIRO_DATE                              AS GIRO_DATE,           ");
        sql.append("          NVL(GA.INHOUSE_AMOUNT_DAY, 0)             AS INHOUSE_AMOUNT_DAY,           ");
        sql.append("          GA.INHOUSE_DATE                           AS INHOUSE_DATE,           ");
        sql.append("          PA.PAYMENT_DATE                           AS GENERATE_FILE_DATE,           ");
        sql.append("          PA.PAYMENT_NAME                           AS GENERATE_FILE_NAME,           ");
        sql.append("          CASE           ");
        sql.append("          WHEN GA.RUN_STATUS IS NULL THEN 'W' ELSE GA.RUN_STATUS END AS GENERATE_FILE_RUN_STATUS,           ");
        sql.append("          CASE           ");
        sql.append("          WHEN GA.RUN_STATUS IS NULL THEN 'รอดำเนินการสร้างไฟล์ชำระเงิน'           ");
        sql.append("          WHEN GA.RUN_STATUS = 'P' THEN 'กำลังประมวลผลการสร้างไฟล์'           ");
        sql.append("          WHEN GA.RUN_STATUS = 'S' THEN 'สร้างไฟล์ชำระเงินสำเร็จ'           ");
        sql.append("          ELSE 'สร้างไฟล์ชำระเงินไม่สำเร็จ' END AS GENERATE_FILE_RUN_STATUS_NAME,           ");
        sql.append("          GA.ID                                     AS GENERATE_FILE_ALIAS_ID,           ");
        sql.append("          GA.CREATE_AGAIN                           AS GENERATE_FILE_CREATE_AGAIN,           ");
        sql.append("          PA.RUN_TOTAL_DOCUMENT,           ");
        sql.append("          PA.RUN_SUCCESS_DOCUMENT,           ");
        sql.append("          PA.RUN_REVERSE_DOCUMENT,           ");
        sql.append("          PA.IDEM_CREATE_PAYMENT_REPLY,           ");
        sql.append("          PA.IDEM_REVERSE_PAYMENT_REPLY           ");
        sql.append("          FROM PAYMENT_ALIAS PA           ");
        sql.append("          LEFT JOIN GENERATE_FILE_ALIAS GA ON PA.ID = GA.PAYMENT_ALIAS_ID           ");
        sql.append("          WHERE PA.RUN_STATUS = 'S'           ");

        if (!Util.isEmpty(generateFileDate)) {
            sql.append(SqlUtil.whereClause(generateFileDate, "PA.PAYMENT_DATE", params));
        }

        if (!Util.isEmpty(generateFileName)) {
            sql.append(SqlUtil.whereClause(generateFileName, "PA.PAYMENT_NAME", params));
        }
        sql.append("          ORDER BY PA.PAYMENT_DATE DESC           ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<GenerateFileAliasResponse> generateFileAlias = this.jdbcTemplate.query(sql.toString(), objParams, userRowMapperDisplay);
        return !generateFileAlias.isEmpty();
    }
}
