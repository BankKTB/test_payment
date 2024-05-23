package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.payment.JULine;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.payment.entity.mapping.JUDocument;
import th.com.bloomcode.paymentservice.payment.entity.mapping.JUDocumentExport;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.JULineRepository;
import th.com.bloomcode.paymentservice.service.AuthorizeUtilService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class JULineRepositoryImpl extends MetadataJdbcRepository<JULine, Long> implements JULineRepository {
    @Value("${payment.dblink.schema}")
    private String schema;

    @Value("${payment.dblink.user}")
    private String user;

    private final AuthorizeUtilService authorizeUtilService;

    static BeanPropertyRowMapper<JULine> beanPropertyRowMapper = new BeanPropertyRowMapper<>(JULine.class);
    static BeanPropertyRowMapper<JUDocument> beanPropertyRowMapperJUDocument = new BeanPropertyRowMapper<>(JUDocument.class);
    static BeanPropertyRowMapper<JUDocumentExport> beanPropertyRowMapperJUDocumentExport = new BeanPropertyRowMapper<>(JUDocumentExport.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<JULine> JULineUpdater = (t, mapping) -> {
        mapping.put(JULine.COLUMN_NAME_ID, t.getId());
        mapping.put(JULine.COLUMN_NAME_REF_RUNNING, t.getRefRunning());
        mapping.put(JULine.COLUMN_NAME_PAY_ACCOUNT, t.getPayAccount());
        mapping.put(JULine.COLUMN_NAME_REF_LINE, t.getRefLine());
        mapping.put(JULine.COLUMN_NAME_ACCOUNT_NO_FROM, t.getAccountNoFrom());
        mapping.put(JULine.COLUMN_NAME_ACCOUNT_NO_TO, t.getAccountNoTo());
        mapping.put(JULine.COLUMN_NAME_FILE_TYPE, t.getFileType());
        mapping.put(JULine.COLUMN_NAME_FILE_NAME, t.getFileName());
        mapping.put(JULine.COLUMN_NAME_AMOUNT_DR, t.getAmountDr());
        mapping.put(JULine.COLUMN_NAME_GL_ACCOUNT_DR, t.getGlAccountDr());
        mapping.put(JULine.COLUMN_NAME_ASSIGNMENT, t.getAssignment());
        mapping.put(JULine.COLUMN_NAME_BG_CODE, t.getBgCode());
        mapping.put(JULine.COLUMN_NAME_COST_CENTER, t.getCostCenter());
        mapping.put(JULine.COLUMN_NAME_FI_AREA, t.getFiArea());
        mapping.put(JULine.COLUMN_NAME_POSTING_KEY, t.getPostingKey());
        mapping.put(JULine.COLUMN_NAME_FUND_SOURCE, t.getFundSource());
        mapping.put(JULine.COLUMN_NAME_BR_DOC_NO, t.getBrDocNo());
        mapping.put(JULine.COLUMN_NAME_WTX_AMOUNT, t.getWtxAmount());
        mapping.put(JULine.COLUMN_NAME_WTX_BASE, t.getWtxBase());
        mapping.put(JULine.COLUMN_NAME_WTX_AMOUNT_P, t.getWtxAmountP());
        mapping.put(JULine.COLUMN_NAME_WTX_BASE_P, t.getWtxBaseP());
        mapping.put(JULine.COLUMN_NAME_MAIN_ACTIVITY, t.getMainActivity());
        mapping.put(JULine.COLUMN_NAME_COST_ACTIVITY, t.getCostActivity());
        mapping.put(JULine.COLUMN_NAME_SUB_ACCOUNT, t.getSubAccount());
        mapping.put(JULine.COLUMN_NAME_SUB_ACCOUNT_OWNER, t.getSubAccountOwner());
        mapping.put(JULine.COLUMN_NAME_DEPOSIT_ACCOUNT, t.getDepositAccount());
        mapping.put(JULine.COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER, t.getDepositAccountOwner());
        mapping.put(JULine.COLUMN_NAME_JU_HEAD_ID, t.getJuHeadId());
    };
    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(JULine.COLUMN_NAME_ID, Types.BIGINT),
            entry(JULine.COLUMN_NAME_REF_RUNNING, Types.BIGINT),
            entry(JULine.COLUMN_NAME_PAY_ACCOUNT, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_REF_LINE, Types.INTEGER),
            entry(JULine.COLUMN_NAME_ACCOUNT_NO_FROM, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_ACCOUNT_NO_TO, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_FILE_TYPE, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_FILE_NAME, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_AMOUNT_DR, Types.NUMERIC),
            entry(JULine.COLUMN_NAME_GL_ACCOUNT_DR, Types.NUMERIC),
            entry(JULine.COLUMN_NAME_ASSIGNMENT, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_BG_CODE, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_COST_CENTER, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_FI_AREA, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_POSTING_KEY, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_FUND_SOURCE, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_BR_DOC_NO, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_WTX_AMOUNT, Types.NUMERIC),
            entry(JULine.COLUMN_NAME_WTX_BASE, Types.NUMERIC),
            entry(JULine.COLUMN_NAME_WTX_AMOUNT_P, Types.NUMERIC),
            entry(JULine.COLUMN_NAME_WTX_BASE_P, Types.NUMERIC),
            entry(JULine.COLUMN_NAME_MAIN_ACTIVITY, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_COST_ACTIVITY, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_SUB_ACCOUNT, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_SUB_ACCOUNT_OWNER, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_DEPOSIT_ACCOUNT, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER, Types.NVARCHAR),
            entry(JULine.COLUMN_NAME_JU_HEAD_ID, Types.BIGINT)
    );

    static RowMapper<JULine> userRowMapper = (rs, rowNum) -> new JULine(
            rs.getLong(JULine.COLUMN_NAME_ID),
            rs.getLong(JULine.COLUMN_NAME_REF_RUNNING),
            rs.getString(JULine.COLUMN_NAME_PAY_ACCOUNT),
            rs.getInt(JULine.COLUMN_NAME_REF_LINE),
            rs.getString(JULine.COLUMN_NAME_ACCOUNT_NO_FROM),
            rs.getString(JULine.COLUMN_NAME_ACCOUNT_NO_TO),
            rs.getString(JULine.COLUMN_NAME_FILE_TYPE),
            rs.getString(JULine.COLUMN_NAME_FILE_NAME),
            rs.getBigDecimal(JULine.COLUMN_NAME_AMOUNT_DR),
            rs.getBigDecimal(JULine.COLUMN_NAME_GL_ACCOUNT_DR),
            rs.getString(JULine.COLUMN_NAME_ASSIGNMENT),
            rs.getString(JULine.COLUMN_NAME_BG_CODE),
            rs.getString(JULine.COLUMN_NAME_COST_CENTER),
            rs.getString(JULine.COLUMN_NAME_FI_AREA),
            rs.getString(JULine.COLUMN_NAME_POSTING_KEY),
            rs.getString(JULine.COLUMN_NAME_FUND_SOURCE),
            rs.getString(JULine.COLUMN_NAME_BR_DOC_NO),
            rs.getBigDecimal(JULine.COLUMN_NAME_WTX_AMOUNT),
            rs.getBigDecimal(JULine.COLUMN_NAME_WTX_BASE),
            rs.getBigDecimal(JULine.COLUMN_NAME_WTX_AMOUNT_P),
            rs.getBigDecimal(JULine.COLUMN_NAME_WTX_BASE_P),
            rs.getString(JULine.COLUMN_NAME_MAIN_ACTIVITY),
            rs.getString(JULine.COLUMN_NAME_COST_ACTIVITY),
            rs.getString(JULine.COLUMN_NAME_SUB_ACCOUNT),
            rs.getString(JULine.COLUMN_NAME_SUB_ACCOUNT_OWNER),
            rs.getString(JULine.COLUMN_NAME_DEPOSIT_ACCOUNT),
            rs.getString(JULine.COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER),
            rs.getLong(JULine.COLUMN_NAME_JU_HEAD_ID)
    );

    public JULineRepositoryImpl(AuthorizeUtilService authorizeUtilService, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(userRowMapper, JULineUpdater, updaterType, JULine.TABLE_NAME, JULine.COLUMN_NAME_ID, jdbcTemplate);
        this.authorizeUtilService = authorizeUtilService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<JUDocument> selectJuDetail(GenerateJuRequest request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("  WITH TBG AS ( ");
        sql.append("    SELECT BC.TH_BGBUDGETCODE_ID ");
        sql.append("         , L.BG_CODE ");
        sql.append("         , L.ID ");
        sql.append("         , BC.C_YEAR_ID ");
        sql.append("    FROM JU_LINE L ");
        sql.append("        JOIN JU_HEAD H ON L.JU_HEAD_ID = H.ID ");
        sql.append("        LEFT JOIN ").append(schema).append(".TH_BGBUDGETCODE BC ON L.BG_CODE = BC.VALUECODE ");
        sql.append("        LEFT JOIN ").append(schema).append(".C_YEAR CY ON BC.C_YEAR_ID = CY.C_YEAR_ID ");
        sql.append("        LEFT JOIN ").append(schema).append(".TH_BGCostCenter").append(" BGC ON L.COST_CENTER = BGC.VALUECODE  ");
        sql.append("        LEFT JOIN ").append(schema).append(".TH_BGPAYMENTCenter").append(" PC ON H.PAYMENT_CENTER = PC.VALUECODE  ");

        if (request.isTestRun()) {
            sql.append(" AND TEST_RUN = 1 ");
        } else {
            sql.append(" AND TEST_RUN = 0 ");
        }
        sql.append(" where  (CY.FISCALYEAR = LPAD(SUBSTR(L.FUND_SOURCE, 0, 2), 4, 25) - 543 OR BC.C_YEAR_ID is null) ");
        sql.append(generateSQLWhereAuthorizedCommon());
        if (!Util.isEmpty(request.getPaymentDate())) {
            System.out.println("H.PAYMENT_DATE : "+request.getPaymentDate());
            sql.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "H.PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            System.out.println("H.PAYMENT_NAME : "+request.getPaymentName());
            sql.append(SqlUtil.dynamicCondition(request.getPaymentName(), "H.PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(request.getTransferDate())) {
            System.out.println("H.TRANSFER_DATE : "+request.getTransferDate());
            params.add(Util.timestampToString(request.getTransferDate()));
            sql.append(" AND TO_CHAR(H.TRANSFER_DATE,'YYYY-MM-DD') = ? ");
        }

        sql.append("    AND BC.C_YEAR_ID IS NULL ");
        sql.append("  ) ");
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
        sql.append("          , H.FISCAL_YEAR           ");
        sql.append("          , GLCR.NAME  AS GL_ACCOUNT_CR_NAME           ");
        sql.append("          , GLDR.NAME  AS GL_ACCOUNT_DR_NAME           ");
        sql.append("          , CC.NAME  AS COMPANY_NAME           ");
        sql.append("          , FS.NAME  AS FUND_SOURCE_NAME           ");
        sql.append("          , BA.NAME  AS MAIN_ACTIVITY_NAME           ");
        sql.append("          , BC.NAME  AS BG_CODE_NAME           ");
        sql.append("          , BGC.NAME AS COST_CENTER_NAME           ");
        sql.append("          , PC.NAME  AS PAYMENT_CENTER_NAME           ");
        sql.append("          , H.ID  AS JU_HEAD_ID           ");
        sql.append("   FROM JU_LINE L           ");
        sql.append("          JOIN JU_HEAD H ON L.JU_HEAD_ID = H.ID   ");
        sql.append("          LEFT JOIN ").append(schema).append(".C_ELEMENTVALUE").append(" GLCR ON H.GL_ACCOUNT_CR = GLCR.VALUE ");
        sql.append("          LEFT JOIN ").append(schema).append(".C_ELEMENTVALUE").append(" GLDR ON L.GL_ACCOUNT_DR = GLDR.VALUE ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_CACOMPCODE").append(" CC ON H.COMPANY_CODE = CC.VALUECODE ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGFundSource").append(" FS ON L.FUND_SOURCE = FS.VALUECODE ");
        sql.append("          LEFT JOIN (SELECT VALUECODE, NAME            ");
        sql.append("          FROM ").append(schema).append(".TH_BGBudgetActivity");
        sql.append("          GROUP BY VALUECODE, NAME) BA ON L.MAIN_ACTIVITY = BA.VALUECODE   ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGBUDGETCODE BC ON L.BG_CODE = BC.VALUECODE AND 1 = (  ");
        sql.append("                CASE  ");
        sql.append("                    WHEN BC.C_YEAR_ID IS NULL AND BC.C_YEAR_ID IN (SELECT T.C_YEAR_ID FROM TBG T WHERE T.BG_CODE = L.BG_CODE) THEN 1  ");
        sql.append("                    WHEN BC.C_YEAR_ID IS NOT NULL AND BC.C_YEAR_ID NOT IN (SELECT T.C_YEAR_ID FROM TBG T WHERE T.BG_CODE = L.BG_CODE) THEN 1  ");
        sql.append("                    ELSE 0  ");
        sql.append("                END  ");
        sql.append("               )  ");
        sql.append("          LEFT JOIN ").append(schema).append(".C_YEAR").append(" CY ON BC.C_YEAR_ID = CY.C_YEAR_ID  ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGCostCenter").append(" BGC ON L.COST_CENTER = BGC.VALUECODE  ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGPAYMENTCenter").append(" PC ON H.PAYMENT_CENTER = PC.VALUECODE  ");

        if (request.isTestRun()) {
            sql.append(" AND TEST_RUN = 1 ");
        } else {
            sql.append(" AND TEST_RUN = 0 ");
        }
        sql.append(" where  (CY.FISCALYEAR = LPAD(SUBSTR(L.FUND_SOURCE, 0, 2), 4, 25) - 543 OR BC.C_YEAR_ID is null) ");
        sql.append(generateSQLWhereAuthorizedCommon());
        if (!Util.isEmpty(request.getPaymentDate())) {
            System.out.println("H.PAYMENT_DATE : "+request.getPaymentDate());
            sql.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "H.PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            System.out.println("H.PAYMENT_NAME : "+request.getPaymentName());
            sql.append(SqlUtil.dynamicCondition(request.getPaymentName(), "H.PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(request.getTransferDate())) {
            System.out.println("H.TRANSFER_DATE : "+request.getTransferDate());
            params.add(Util.timestampToString(request.getTransferDate()));
            sql.append(" AND TO_CHAR(H.TRANSFER_DATE,'YYYY-MM-DD') = ? ");
        }

        sql.append(" ORDER BY H.PAYMENT_DATE DESC, H.PAYMENT_NAME ASC ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql selectJuDetail : {}", sql.toString());
        log.info("params selectJuDetail : {} ", params);


        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapperJUDocument);
    }

    @Override
    public List<JUDocumentExport> selectJuDetailExport(GenerateJuRequest request) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("  WITH TBG AS ( ");
        sql.append("    SELECT BC.TH_BGBUDGETCODE_ID ");
        sql.append("         , L.BG_CODE ");
        sql.append("         , L.ID ");
        sql.append("         , BC.C_YEAR_ID ");
        sql.append("    FROM JU_LINE L ");
        sql.append("        JOIN JU_HEAD H ON L.JU_HEAD_ID = H.ID ");
        sql.append("        LEFT JOIN ").append(schema).append(".TH_BGBUDGETCODE BC ON L.BG_CODE = BC.VALUECODE ");
        sql.append("        LEFT JOIN ").append(schema).append(".C_YEAR CY ON BC.C_YEAR_ID = CY.C_YEAR_ID ");
        sql.append("        LEFT JOIN ").append(schema).append(".TH_BGCostCenter").append(" BGC ON L.COST_CENTER = BGC.VALUECODE  ");
        sql.append("        LEFT JOIN ").append(schema).append(".TH_BGPAYMENTCenter").append(" PC ON H.PAYMENT_CENTER = PC.VALUECODE  ");

        if (request.isTestRun()) {
            sql.append(" AND TEST_RUN = 1 ");
        } else {
            sql.append(" AND TEST_RUN = 0 ");
        }

        sql.append(" where  (CY.FISCALYEAR = LPAD(SUBSTR(L.FUND_SOURCE, 0, 2), 4, 25) - 543 OR BC.C_YEAR_ID is null) ");
        sql.append(generateSQLWhereAuthorizedCommon());

        if (!Util.isEmpty(request.getPaymentDate())) {
            sql.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "H.PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            sql.append(SqlUtil.dynamicCondition(request.getPaymentName(), "H.PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(request.getTransferDate())) {
            params.add(Util.timestampToString(request.getTransferDate()));
            sql.append(" AND TO_CHAR(H.TRANSFER_DATE,'YYYY-MM-DD') = ? ");
        }

        sql.append("    AND BC.C_YEAR_ID IS NULL ");
        sql.append("  ) ");
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
        sql.append("          , H.FISCAL_YEAR           ");
        sql.append("          , GLCR.NAME  AS GL_ACCOUNT_CR_NAME           ");
        sql.append("          , GLDR.NAME  AS GL_ACCOUNT_DR_NAME           ");
        sql.append("          , CC.NAME  AS COMPANY_NAME           ");
        sql.append("          , FS.NAME  AS FUND_SOURCE_NAME           ");
        sql.append("          , BA.NAME  AS MAIN_ACTIVITY_NAME           ");
        sql.append("          , BC.NAME  AS BG_CODE_NAME           ");
        sql.append("          , BGC.NAME AS COST_CENTER_NAME           ");
        sql.append("          , PC.NAME  AS PAYMENT_CENTER_NAME           ");
        sql.append("          , H.ID  AS JU_HEAD_ID           ");
        sql.append("          , 0 AS CREDIT           ");
        sql.append("   FROM JU_LINE L           ");
        sql.append("          JOIN JU_HEAD H ON L.JU_HEAD_ID = H.ID   ");
        sql.append("          LEFT JOIN ").append(schema).append(".C_ELEMENTVALUE").append(" GLCR ON H.GL_ACCOUNT_CR = GLCR.VALUE ");
        sql.append("          LEFT JOIN ").append(schema).append(".C_ELEMENTVALUE").append(" GLDR ON L.GL_ACCOUNT_DR = GLDR.VALUE ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_CACOMPCODE").append(" CC ON H.COMPANY_CODE = CC.VALUECODE ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGFundSource").append(" FS ON L.FUND_SOURCE = FS.VALUECODE ");
        sql.append("          LEFT JOIN (SELECT VALUECODE, NAME            ");
        sql.append("          FROM ").append(schema).append(".TH_BGBudgetActivity");
        sql.append("          GROUP BY VALUECODE, NAME) BA ON L.MAIN_ACTIVITY = BA.VALUECODE   ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGBUDGETCODE BC ON L.BG_CODE = BC.VALUECODE AND 1 = (  ");
        sql.append("                CASE  ");
        sql.append("                    WHEN BC.C_YEAR_ID IS NULL AND BC.C_YEAR_ID IN (SELECT T.C_YEAR_ID FROM TBG T WHERE T.BG_CODE = L.BG_CODE) THEN 1  ");
        sql.append("                    WHEN BC.C_YEAR_ID IS NOT NULL AND BC.C_YEAR_ID NOT IN (SELECT T.C_YEAR_ID FROM TBG T WHERE T.BG_CODE = L.BG_CODE) THEN 1  ");
        sql.append("                    ELSE 0  ");
        sql.append("                END  ");
        sql.append("               )  ");
        sql.append("          LEFT JOIN ").append(schema).append(".C_YEAR").append(" CY ON BC.C_YEAR_ID = CY.C_YEAR_ID  ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGCostCenter").append(" BGC ON L.COST_CENTER = BGC.VALUECODE  ");
        sql.append("          LEFT JOIN ").append(schema).append(".TH_BGPAYMENTCenter").append(" PC ON H.PAYMENT_CENTER = PC.VALUECODE  ");

        if (request.isTestRun()) {
            sql.append(" AND TEST_RUN = 1 ");
        } else {
            sql.append(" AND TEST_RUN = 0 ");
        }
        
        sql.append(" where  (CY.FISCALYEAR = LPAD(SUBSTR(L.FUND_SOURCE, 0, 2), 4, 25) - 543 OR BC.C_YEAR_ID is null) ");
        sql.append(generateSQLWhereAuthorizedCommon());

        if (!Util.isEmpty(request.getPaymentDate())) {
            sql.append(SqlUtil.dynamicDateCondition(request.getPaymentDate(), "H.PAYMENT_DATE", params));
        }
        if (!Util.isEmpty(request.getPaymentName())) {
            sql.append(SqlUtil.dynamicCondition(request.getPaymentName(), "H.PAYMENT_NAME", params));
        }
        if (!Util.isEmpty(request.getTransferDate())) {
            params.add(Util.timestampToString(request.getTransferDate()));
            sql.append(" AND TO_CHAR(H.TRANSFER_DATE,'YYYY-MM-DD') = ? ");
        }

        sql.append(" ORDER BY H.PAYMENT_DATE DESC, H.PAYMENT_NAME ASC ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql selectJuDetail export: {}", sql.toString());
        log.info("params selectJuDetail export: {} ", params);


        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapperJUDocumentExport);
    }

    @Override
    public void deleteAllByJUHeadId(Long juHeadId) {
        String sql = String.format(" DELETE FROM %s WHERE %s = ?", JULine.TABLE_NAME, "JU_HEAD_ID");
        ;
        this.jdbcTemplate.update(sql, juHeadId);
    }

    private String generateSQLWhereAuthorizedCommon() {
        JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
        StringBuilder sb = new StringBuilder();

        String AUTHORIZATION_OBJECT_NAME = "FICOMMON";
        String AUTHORIZATION_ACTIVITY = AuthorizeUtilService.READ_ACTIVITY;
        String authSQL = "";
        // company code
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
            AUTHORIZATION_ACTIVITY, AuthorizeUtilService.COMPANY_CODE_ATTRIBUTE, "H.COMPANY_CODE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // area
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
            AUTHORIZATION_ACTIVITY, AuthorizeUtilService.AREA_ATTRIBUTE, "LPAD(L.FI_AREA, 5, 'P')", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // payment center
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
            AUTHORIZATION_ACTIVITY, AuthorizeUtilService.PAYMENT_CENTER_ATTRIBUTE, "H.PAYMENT_CENTER", null);
        if (Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // cost center
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
            AUTHORIZATION_ACTIVITY, AuthorizeUtilService.COST_CENTER_ATTRIBUTE, "L.COST_CENTER", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // doc type
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
            AUTHORIZATION_ACTIVITY, AuthorizeUtilService.DOC_TYPE_ATTRIBUTE, "H.DOC_TYPE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // budget code
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
            AUTHORIZATION_ACTIVITY, AuthorizeUtilService.BUDGET_CODE_ATTRIBUTE, "L.BG_CODE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // budget activity
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
            AUTHORIZATION_ACTIVITY, AuthorizeUtilService.BUDGET_ACTIVITY_ATTRIBUTE, "L.BG_ACTIVITY", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // source of fund
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
            AUTHORIZATION_ACTIVITY, AuthorizeUtilService.FUND_SOURCE_ATTRIBUTE, "L.FUND_SOURCE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // account type
//        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
//            AUTHORIZATION_ACTIVITY, AuthorizeUtilService.ACCOUNT_TYPE_ATTIBUTE, "L.ACCOUNT_TYPE", null);
//        if (!Util.isEmpty(authSQL)) {
//            sb.append(" AND (").append(authSQL).append(")");
//        }

        return sb.toString();
    }
}
