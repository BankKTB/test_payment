package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.JUHead;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.JuHeadRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;
import th.com.bloomcode.paymentservice.webservice.model.response.APPaymentResponse;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class JuHeadRepositoryImpl extends MetadataJdbcRepository<JUHead, Long> implements JuHeadRepository {
    @Value("${payment.dblink.schema}")
    private String schema;

    @Value("${payment.dblink.user}")
    private String user;

    private final JdbcTemplate jdbcTemplate;
    static BeanPropertyRowMapper<JUHead> beanPropertyRowMapper = new BeanPropertyRowMapper<>(JUHead.class);
    static Updater<JUHead> juHeadUpdater = (t, mapping) -> {
        mapping.put(JUHead.COLUMN_NAME_ID, t.getId());
        mapping.put(JUHead.COLUMN_NAME_PAYMENT_DATE, t.getPaymentDate());
        mapping.put(JUHead.COLUMN_NAME_PAYMENT_NAME, t.getPaymentName());
        mapping.put(JUHead.COLUMN_NAME_DOC_TYPE, t.getDocumentType());
        mapping.put(JUHead.COLUMN_NAME_DOCUMENT_NO, t.getDocumentNo());
        mapping.put(JUHead.COLUMN_NAME_REFERENCE, t.getReference());
        mapping.put(JUHead.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(JUHead.COLUMN_NAME_PAYMENT_CENTER, t.getPaymentCenter());
        mapping.put(JUHead.COLUMN_NAME_USERNAME, t.getPaymentCenter());
        mapping.put(JUHead.COLUMN_NAME_DOCUMENT_STATUS, t.getPaymentCenter());
        mapping.put(JUHead.COLUMN_NAME_DOCUMENT_STATUS_NAME, t.getDocumentStatusName());
        mapping.put(JUHead.COLUMN_NAME_DATE_DOC, t.getDateDoc());
        mapping.put(JUHead.COLUMN_NAME_DATE_ACCT, t.getDateAcct());
        mapping.put(JUHead.COLUMN_NAME_TRANSFER_DATE, t.getTransferDate());
        mapping.put(JUHead.COLUMN_NAME_AMOUNT_CR, t.getAmountCr());
        mapping.put(JUHead.COLUMN_NAME_GL_ACCOUNT_CR, t.getGlAccountCr());
        mapping.put(JUHead.COLUMN_NAME_TEST_RUN, t.getTestRun());
        mapping.put(JUHead.COLUMN_NAME_MESSAGE_QUEUE_ID, t.getMessageQueueId());
        mapping.put(JUHead.COLUMN_NAME_FISCAL_YEAR, t.getFiscalYear());
        mapping.put(JUHead.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(JUHead.COLUMN_NAME_UPDATED, t.getUpdated());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(JUHead.COLUMN_NAME_ID, Types.BIGINT),
            entry(JUHead.COLUMN_NAME_PAYMENT_DATE, Types.TIMESTAMP),
            entry(JUHead.COLUMN_NAME_PAYMENT_NAME, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_DOC_TYPE, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_DOCUMENT_NO, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_REFERENCE, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_PAYMENT_CENTER, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_USERNAME, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_DOCUMENT_STATUS, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_DOCUMENT_STATUS_NAME, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_DATE_DOC, Types.TIMESTAMP),
            entry(JUHead.COLUMN_NAME_DATE_ACCT, Types.TIMESTAMP),
            entry(JUHead.COLUMN_NAME_TRANSFER_DATE, Types.TIMESTAMP),
            entry(JUHead.COLUMN_NAME_AMOUNT_CR, Types.NUMERIC),
            entry(JUHead.COLUMN_NAME_GL_ACCOUNT_CR, Types.NUMERIC),
            entry(JUHead.COLUMN_NAME_TEST_RUN, Types.BOOLEAN),
            entry(JUHead.COLUMN_NAME_MESSAGE_QUEUE_ID, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_FISCAL_YEAR, Types.NVARCHAR),
            entry(JUHead.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(JUHead.COLUMN_NAME_UPDATED, Types.TIMESTAMP)
    );

    static RowMapper<JUHead> userRowMapper = (rs, rowNum) -> new JUHead(
            rs.getLong(JUHead.COLUMN_NAME_ID),
            rs.getTimestamp(JUHead.COLUMN_NAME_PAYMENT_DATE),
            rs.getString(JUHead.COLUMN_NAME_PAYMENT_NAME),
            rs.getString(JUHead.COLUMN_NAME_DOC_TYPE),
            rs.getString(JUHead.COLUMN_NAME_DOCUMENT_NO),
            rs.getString(JUHead.COLUMN_NAME_REFERENCE),
            rs.getString(JUHead.COLUMN_NAME_COMPANY_CODE),
            rs.getString(JUHead.COLUMN_NAME_PAYMENT_CENTER),
            rs.getString(JUHead.COLUMN_NAME_USERNAME),
            rs.getString(JUHead.COLUMN_NAME_DOCUMENT_STATUS),
            rs.getString(JUHead.COLUMN_NAME_DOCUMENT_STATUS_NAME),
            rs.getTimestamp(JUHead.COLUMN_NAME_DATE_DOC),
            rs.getTimestamp(JUHead.COLUMN_NAME_DATE_ACCT),
            rs.getTimestamp(JUHead.COLUMN_NAME_TRANSFER_DATE),
            rs.getBigDecimal(JUHead.COLUMN_NAME_AMOUNT_CR),
            rs.getBigDecimal(JUHead.COLUMN_NAME_GL_ACCOUNT_CR),
            rs.getBoolean(JUHead.COLUMN_NAME_TEST_RUN),
            rs.getString(JUHead.COLUMN_NAME_MESSAGE_QUEUE_ID),
            rs.getString(JUHead.COLUMN_NAME_FISCAL_YEAR)
    );

    public JuHeadRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, juHeadUpdater, updaterType, JUHead.TABLE_NAME, JUHead.COLUMN_NAME_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<JUHead> findJuHeadByListPaymentDateAndListPaymentName(GenerateJuRequest request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" * ");
        sb.append(" FROM JU_HEAD ");
        sb.append(" WHERE 1 = 1 ");
        sb.append(" AND TEST_RUN = 1 ");
        if (!Util.isEmpty(request.getPaymentDate())) {
            sb.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            sb.append(SqlUtil.dynamicCondition(request.getPaymentName(), "PAYMENT_NAME", params));
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql findJuHeadByListPaymentDateAndListPaymentName {}", sb.toString());
        log.info("params findJuHeadByListPaymentDateAndListPaymentName : {} ", params);
        log.info("objParams findJuHeadByListPaymentDateAndListPaymentName : {} ", objParams);
        return this.jdbcTemplate.query(sb.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public JUHead findAllByPaymentDateAndPaymentNameAndTestRun(Timestamp paymentDate, String paymentName, boolean testRun) {
        List<Object> params = new ArrayList<>();
        log.info("-------------paymentDate-------------- : {} ", paymentDate);
        params.add(paymentDate);
        params.add(paymentName);
        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT * FROM ");
        sql.append(JUHead.TABLE_NAME);
        sql.append("  WHERE ");
        sql.append("  PAYMENT_DATE  ");
        sql.append("  AND PAYMENT_NAME like ? ");
        if (testRun) {
            sql.append(" AND TEST_RUN = 1 ");
        } else {
            sql.append(" AND TEST_RUN = 0 ");
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, userRowMapper);
    }

    public List<JUHead> findJuDetail(GenerateJuRequest request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT L.ID      ");
        sql.append("          , L.WTX_AMOUNT           ");
        sql.append("          , L.WTX_AMOUNT_P           ");
        sql.append("          , L.WTX_BASE           ");
        sql.append("          , L.WTX_BASE_P           ");
        sql.append("          , L.ACCOUNT_NO_FROM           ");
        sql.append("          , L.ACCOUNT_NO_TO           ");
        sql.append("          , L.AMOUNT_DR           ");
        sql.append("          , L.ASSIGNMENT           ");
        sql.append("          , L.BG_CODE           ");
        sql.append("          , L.BR_DOC_NO           ");
        sql.append("          , L.COST_ACTIVITY           ");
        sql.append("          , L.COST_CENTER           ");
        sql.append("          , L.DEPOSIT_ACCOUNT           ");
        sql.append("          , L.DEPOSIT_ACCOUNT_OWNER           ");
        sql.append("          , L.FI_AREA           ");
        sql.append("          , L.FILE_NAME           ");
        sql.append("          , L.FILE_TYPE           ");
        sql.append("          , L.FUND_SOURCE           ");
        sql.append("          , L.GL_ACCOUNT_DR           ");
        sql.append("          , L.MAIN_ACTIVITY           ");
        sql.append("          , L.PAY_ACCOUNT           ");
        sql.append("          , L.POSTING_KEY           ");
        sql.append("          , L.REF_LINE           ");
        sql.append("          , L.REF_RUNNING           ");
        sql.append("          , L.SUB_ACCOUNT           ");
        sql.append("          , L.SUB_ACCOUNT_OWNER           ");
        sql.append("          , L.JU_HEAD_ID           ");
        sql.append("          , H.PAYMENT_CENTER           ");
        sql.append("          , H.PAYMENT_DATE           ");
        sql.append("          , H.PAYMENT_NAME           ");
        sql.append("          , H.REFERENCE           ");
        sql.append("          , H.TEST_RUN           ");
        sql.append("          , H.TRANSFER_DATE           ");
        sql.append("          , H.USERNAME           ");
        sql.append("          , H.AMOUNT_CR           ");
        sql.append("          , H.COMPANY_CODE           ");
        sql.append("          , H.DATE_ACCT           ");
        sql.append("          , H.DATE_DOC           ");
        sql.append("          , H.DOC_TYPE           ");
        sql.append("          , H.DOCUMENT_NO           ");
        sql.append("          , H.DOCUMENT_STATUS           ");
        sql.append("          , H.DOCUMENT_STATUS_NAME           ");
        sql.append("          , H.GL_ACCOUNT_CR           ");
        sql.append("          , CC.NAME  AS COMP_CODE_NAME           ");
        sql.append("          , FS.NAME  AS FUND_SOURCE_NAME           ");
        sql.append("          , BA.NAME  AS MAIN_ACTIVITY_NAME           ");
        sql.append("          , BC.NAME  AS BG_CODE_NAME           ");
        sql.append("          , BGC.NAME AS COST_CENTER_NAME           ");
        sql.append("          , PC.NAME  AS PAYMENT_CENTER_NAME           ");
        sql.append("          , H.ID  AS JU_HEAD_ID           ");
        sql.append("   FROM JU_LINE L           ");
        sql.append("          JOIN JU_HEAD H ON L.JU_HEAD_ID = H.ID   ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_CACOMPCODE").append(" CC ON H.COMPANY_CODE = CC.VALUECODE ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGFundSource").append(" FS ON L.FUND_SOURCE = FS.VALUECODE ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGFundSource").append(" FS ON L.FUND_SOURCE = FS.VALUECODE ");
        sql.append("          LEFT JOIN (SELECT VALUECODE, NAME            ");
        sql.append("          FROM ").append(schema).append(".TH_BGBudgetActivity");
        sql.append("          GROUP BY VALUECODE, NAME) BA ON L.MAIN_ACTIVITY = BA.VALUECODE   ");
        sql.append("          LEFT JOIN (SELECT VALUECODE, NAME           ");
        sql.append("          FROM ").append(schema).append(".TH_BGBudgetCode");
        sql.append("          GROUP BY VALUECODE, NAME) BC ON L.BG_CODE = BC.VALUECODE   ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGCostCenter").append(" BGC ON L.COST_CENTER = BGC.VALUECODE  ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGPAYMENTCenter").append(" PC ON H.PAYMENT_CENTER = PC.VALUECODE  ");
        if (!Util.isEmpty(request.getPaymentDate())) {
            sql.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "H.PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            sql.append(SqlUtil.dynamicCondition(request.getPaymentName(), "H.PAYMENT_NAME", params));
        }
        if (request.isTestRun()) {
            sql.append(" AND TEST_RUN = 1 ");
        } else {
            sql.append(" AND TEST_RUN = 0 ");
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

    @Override
    public void updateJuDocument(APPaymentResponse aPPaymentResponse, String messageQueueId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE ");
        sql.append(" JU_HEAD ");
        sql.append(" SET DOCUMENT_NO = ?, FISCAL_YEAR = ? , DATE_DOC = ? , UPDATED = ?   ");
        sql.append(" WHERE ");
        sql.append(" 1 = 1  ");
        sql.append(" AND MESSAGE_QUEUE_ID = ? ");


        this.jdbcTemplate.update(sql.toString(), aPPaymentResponse.getAccDocNo(), aPPaymentResponse.getFiscalYear(), aPPaymentResponse.getDateDoc(), new Timestamp(System.currentTimeMillis()), messageQueueId);


    }
}
