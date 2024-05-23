package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.common.BaseRange;
import th.com.bloomcode.paymentservice.model.common.CompanyCondition;
import th.com.bloomcode.paymentservice.model.common.PaymentMethod;
import th.com.bloomcode.paymentservice.model.config.*;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.GLHeadRepository;
import th.com.bloomcode.paymentservice.service.AuthorizeUtilService;
import th.com.bloomcode.paymentservice.util.DynamicCondition;
import th.com.bloomcode.paymentservice.util.JSONUtil;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class GLHeadRepositoryImpl extends MetadataJdbcRepository<GLHead, Long> implements GLHeadRepository {


    @Value("${payment.dblink.schema}")
    private String schema;

    @Value("${payment.dblink.user}")
    private String user;

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcTemplate2;
    private final AuthorizeUtilService authorizeUtilService;
    static BeanPropertyRowMapper<GLHead> beanPropertyRowMapper = new BeanPropertyRowMapper<>(GLHead.class);

    static Updater<GLHead> glHeadUpdater = (t, mapping) -> {
        mapping.put(GLHead.COLUMN_NAME_GL_HEAD_ID, t.getId());
        mapping.put(GLHead.COLUMN_NAME_DOCUMENT_TYPE, t.getDocumentType());
        mapping.put(GLHead.COLUMN_NAME_COMPANY_CODE, t.getCompanyCode());
        mapping.put(GLHead.COLUMN_NAME_COMPANY_NAME, t.getCompanyName());
        mapping.put(GLHead.COLUMN_NAME_DATE_DOC, t.getDateDoc());
        mapping.put(GLHead.COLUMN_NAME_DATE_ACCT, t.getDateAcct());
        mapping.put(GLHead.COLUMN_NAME_PERIOD, t.getPeriod());
        mapping.put(GLHead.COLUMN_NAME_CURRENCY, t.getCurrency());
        mapping.put(GLHead.COLUMN_NAME_AMOUNT, t.getAmount());
        mapping.put(GLHead.COLUMN_NAME_PAYMENT_CENTER, t.getPaymentCenter());
        mapping.put(GLHead.COLUMN_NAME_BR_DOCUMENT_NO, t.getBrDocumentNo());
        mapping.put(GLHead.COLUMN_NAME_PO_DOCUMENT_NO, t.getPoDocumentNo());
        mapping.put(GLHead.COLUMN_NAME_INVOICE_DOCUMENT_NO, t.getInvoiceDocumentNo());
        mapping.put(GLHead.COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO, t.getReverseInvoiceDocumentNo());
        mapping.put(GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, t.getOriginalDocumentNo());
        mapping.put(GLHead.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, t.getOriginalFiscalYear());
        mapping.put(GLHead.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO, t.getReverseOriginalDocumentNo());
        mapping.put(GLHead.COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR, t.getReverseOriginalFiscalYear());
        mapping.put(GLHead.COLUMN_NAME_PAYMENT_METHOD, t.getPaymentMethod());
        mapping.put(GLHead.COLUMN_NAME_COST_CENTER1, t.getCostCenter1());
        mapping.put(GLHead.COLUMN_NAME_COST_CENTER2, t.getCostCenter2());
        mapping.put(GLHead.COLUMN_NAME_HEADER_REFERENCE, t.getHeaderReference());
        mapping.put(GLHead.COLUMN_NAME_HEADER_REFERENCE2, t.getHeaderReference2());
        mapping.put(GLHead.COLUMN_NAME_DOCUMENT_HEADER_TEXT, t.getDocumentHeaderText());
        mapping.put(GLHead.COLUMN_NAME_REVERSE_DATE_ACCT, t.getReverseDateAcct());
        mapping.put(GLHead.COLUMN_NAME_REVERSE_REASON, t.getReverseReason());
        mapping.put(GLHead.COLUMN_NAME_DOCUMENT_CREATED, t.getDocumentCreated());
        mapping.put(GLHead.COLUMN_NAME_DOCUMENT_CREATED_REAL, t.getDocumentCreatedReal());
        mapping.put(GLHead.COLUMN_NAME_USER_PARK, t.getUserPark());
        mapping.put(GLHead.COLUMN_NAME_USER_POST, t.getUserPost());
        mapping.put(GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT, t.getOriginalDocument());
        mapping.put(GLHead.COLUMN_NAME_REFERENCE_INTER_DOCUMENT_NO, t.getReferenceInterDocumentNo());
        mapping.put(GLHead.COLUMN_NAME_REFERENCE_INTER_COMPANY_CODE, t.getReferenceInterCompanyCode());
        mapping.put(GLHead.COLUMN_NAME_REFERENCE_AUTO_GEN, t.getReferenceAutoGen());
        mapping.put(GLHead.COLUMN_NAME_DOCUMENT_STATUS, t.getDocumentStatus());
        mapping.put(GLHead.COLUMN_NAME_RP_APPROVED, t.getRpApproved());
        mapping.put(GLHead.COLUMN_NAME_DOCUMENT_BASE_TYPE, t.getDocumentBaseType());
        mapping.put(GLHead.COLUMN_NAME_PAYMENT_DOCUMENT_NO, t.getPaymentDocumentNo());
        mapping.put(GLHead.COLUMN_NAME_PAYMENT_ID, t.getPaymentId());
        mapping.put(GLHead.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(GLHead.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(GLHead.COLUMN_NAME_SELECT_GROUP_DOCUMENT, t.getSelectGroupDocument());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(

            entry(GLHead.COLUMN_NAME_GL_HEAD_ID, Types.BIGINT),
            entry(GLHead.COLUMN_NAME_DOCUMENT_TYPE, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_COMPANY_CODE, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_COMPANY_NAME, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_DATE_DOC, Types.TIMESTAMP),
            entry(GLHead.COLUMN_NAME_DATE_ACCT, Types.TIMESTAMP),
            entry(GLHead.COLUMN_NAME_PERIOD, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_CURRENCY, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_AMOUNT, Types.NUMERIC),
            entry(GLHead.COLUMN_NAME_PAYMENT_CENTER, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_BR_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_PO_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_INVOICE_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_PAYMENT_METHOD, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_COST_CENTER1, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_COST_CENTER2, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_HEADER_REFERENCE, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_HEADER_REFERENCE2, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_DOCUMENT_HEADER_TEXT, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_REVERSE_DATE_ACCT, Types.TIMESTAMP),
            entry(GLHead.COLUMN_NAME_REVERSE_REASON, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_DOCUMENT_CREATED, Types.TIMESTAMP),
            entry(GLHead.COLUMN_NAME_DOCUMENT_CREATED_REAL, Types.TIMESTAMP),
            entry(GLHead.COLUMN_NAME_USER_PARK, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_USER_POST, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_REFERENCE_INTER_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_REFERENCE_INTER_COMPANY_CODE, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_REFERENCE_AUTO_GEN, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_DOCUMENT_STATUS, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_RP_APPROVED, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_DOCUMENT_BASE_TYPE, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_PAYMENT_DOCUMENT_NO, Types.NVARCHAR),
            entry(GLHead.COLUMN_NAME_PAYMENT_ID, Types.BIGINT),
            entry(GLHead.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(GLHead.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
            entry(GLHead.COLUMN_NAME_SELECT_GROUP_DOCUMENT, Types.NVARCHAR)
    );

    static RowMapper<GLHead> userRowMapper = (rs, rowNum) -> new GLHead(
            rs.getLong(GLHead.COLUMN_NAME_GL_HEAD_ID),
            rs.getString(GLHead.COLUMN_NAME_DOCUMENT_TYPE),
            rs.getString(GLHead.COLUMN_NAME_COMPANY_CODE),
            rs.getString(GLHead.COLUMN_NAME_COMPANY_NAME),
            rs.getTimestamp(GLHead.COLUMN_NAME_DATE_DOC),
            rs.getTimestamp(GLHead.COLUMN_NAME_DATE_ACCT),
            rs.getInt(GLHead.COLUMN_NAME_PERIOD),
            rs.getString(GLHead.COLUMN_NAME_CURRENCY),
            rs.getBigDecimal(GLHead.COLUMN_NAME_AMOUNT),
            rs.getString(GLHead.COLUMN_NAME_PAYMENT_CENTER),
            rs.getString(GLHead.COLUMN_NAME_BR_DOCUMENT_NO),
            rs.getString(GLHead.COLUMN_NAME_PO_DOCUMENT_NO),
            rs.getString(GLHead.COLUMN_NAME_INVOICE_DOCUMENT_NO),
            rs.getString(GLHead.COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO),
            rs.getString(GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT_NO),
            rs.getString(GLHead.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
            rs.getString(GLHead.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO),
            rs.getString(GLHead.COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR),
            rs.getString(GLHead.COLUMN_NAME_PAYMENT_METHOD),
            rs.getString(GLHead.COLUMN_NAME_COST_CENTER1),
            rs.getString(GLHead.COLUMN_NAME_COST_CENTER2),
            rs.getString(GLHead.COLUMN_NAME_HEADER_REFERENCE),
            rs.getString(GLHead.COLUMN_NAME_HEADER_REFERENCE2),
            rs.getString(GLHead.COLUMN_NAME_DOCUMENT_HEADER_TEXT),
            rs.getTimestamp(GLHead.COLUMN_NAME_REVERSE_DATE_ACCT),
            rs.getString(GLHead.COLUMN_NAME_REVERSE_REASON),
            rs.getTimestamp(GLHead.COLUMN_NAME_DOCUMENT_CREATED),
            rs.getString(GLHead.COLUMN_NAME_USER_PARK),
            rs.getString(GLHead.COLUMN_NAME_USER_POST),
            rs.getString(GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT),
            rs.getString(GLHead.COLUMN_NAME_REFERENCE_INTER_DOCUMENT_NO),
            rs.getString(GLHead.COLUMN_NAME_REFERENCE_INTER_COMPANY_CODE),
            rs.getString(GLHead.COLUMN_NAME_REFERENCE_AUTO_GEN),
            rs.getString(GLHead.COLUMN_NAME_DOCUMENT_STATUS),
            rs.getString(GLHead.COLUMN_NAME_RP_APPROVED),
            rs.getString(GLHead.COLUMN_NAME_DOCUMENT_BASE_TYPE),
            rs.getString(GLHead.COLUMN_NAME_PAYMENT_DOCUMENT_NO),
            rs.getLong(GLHead.COLUMN_NAME_PAYMENT_ID),
            rs.getTimestamp(GLHead.COLUMN_NAME_CREATED),
            rs.getTimestamp(GLHead.COLUMN_NAME_UPDATED),
            rs.getString(GLHead.COLUMN_NAME_SELECT_GROUP_DOCUMENT),
            rs.getTimestamp(GLHead.COLUMN_NAME_DOCUMENT_CREATED_REAL),
            rs.getString(GLHead.COLUMN_NAME_CREATED_BY),
            rs.getString(GLHead.COLUMN_NAME_UPDATED_BY)

    );

    public GLHeadRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, @Qualifier("paymentNamedJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate2, AuthorizeUtilService authorizeUtilService) {
        super(userRowMapper, glHeadUpdater, updaterType, GLHead.TABLE_NAME, GLHead.COLUMN_NAME_GL_HEAD_ID, jdbcTemplate);

        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate2 = jdbcTemplate2;
        this.authorizeUtilService = authorizeUtilService;
    }

//    @Override
//    public GLHead findOneByDocTypeAndCompCodeAndAccDocNoAndFiscalYear(String documentType, String companyCode, String originalDocumentNo, String originalFiscalYear) {
//        List<Object> params = new ArrayList<>();
//        params.add(documentType);
//        params.add(companyCode);
//        params.add(originalDocumentNo);
//        params.add(originalFiscalYear);
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT * FROM " + GLHead.TABLE_NAME);
//        sql.append(" WHERE " + GLHead.COLUMN_NAME_DOCUMENT_TYPE + " = ?");
//        sql.append(" AND " + GLHead.COLUMN_NAME_COMPANY_CODE + " = ?");
//        sql.append(" AND " + GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ?");
//        sql.append(" AND " + GLHead.COLUMN_NAME_ORIGINAL_FISCAL_YEAR + " = ?");
//        Object[] objParams = new Object[params.size()];
//        params.toArray(objParams);
//        log.info("sql {}", sql.toString());
//        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, userRowMapper);
//    }

    @Override
    public List<GLHead> findByOriginalDocStartsWith(String originalDocumentNo) {
        List<Object> params = new ArrayList<>();
        params.add(originalDocumentNo);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + GLHead.TABLE_NAME);
        sql.append(" WHERE " + GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ?");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("findByOriginalDocStartsWith {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public List<GLHead> findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear) {
        List<Object> params = new ArrayList<>();
        params.add(companyCode);
        params.add(originalDocumentNo);
        params.add(originalFiscalYear);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + GLHead.TABLE_NAME);
        sql.append(" WHERE ");
        sql.append(GLHead.COLUMN_NAME_COMPANY_CODE + " = ? ");
        sql.append(" AND " + GLHead.COLUMN_NAME_ORIGINAL_DOCUMENT_NO + " = ? ");
        sql.append(" AND " + GLHead.COLUMN_NAME_ORIGINAL_FISCAL_YEAR + " = ? ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public void resetGLHead(Long paymentAliasId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE ");
        sql.append(" GL_HEAD ");
        sql.append(" SET PAYMENT_DOCUMENT_NO = NULL , PAYMENT_ID = NULL ");
        sql.append(" WHERE ");
        sql.append(" 1 = 1  ");
        sql.append(" AND PAYMENT_ID = ? ");
        this.jdbcTemplate.update(sql.toString(), paymentAliasId);
    }

    @Override
    public void updateGLHead(String companyCode, String originalDocumentNo, String originalFiscalYear, String paymentDocumentNo, Long paymentAliasId) {
        StringBuilder sql = new StringBuilder();

        sql.append(" UPDATE ");
        sql.append(" GL_HEAD ");
        sql.append(" SET PAYMENT_DOCUMENT_NO = ? , PAYMENT_ID = ? ");
        sql.append(" WHERE ");
        sql.append(" 1 = 1  ");
        sql.append(" AND COMPANY_CODE = ? ");
        sql.append(" AND ORIGINAL_DOCUMENT_NO = ? ");
        sql.append(" AND ORIGINAL_FISCAL_YEAR = ? ");

        this.jdbcTemplate.update(sql.toString(), paymentDocumentNo, paymentAliasId, companyCode, originalDocumentNo, originalFiscalYear);
    }

    private String generateSQLWhereAuthorizedCommon(JwtBody jwt, List<Object> params) {
        StringBuilder sb = new StringBuilder();

        String AUTHORIZATION_OBJECT_NAME = "FICOMMON";
        String AUTHORIZATION_ACTIVITY = AuthorizeUtilService.READ_ACTIVITY;
        String authSQL = "";
        // company code
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.COMPANY_CODE_ATTRIBUTE, "GH.COMPANY_CODE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // area
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.AREA_ATTRIBUTE, "LPAD(GL.FI_AREA, 5, 'P')", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // payment center
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.PAYMENT_CENTER_ATTRIBUTE, "GL.PAYMENT_CENTER", null);
        if (Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // cost center
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.COST_CENTER_ATTRIBUTE, "GL.COST_CENTER", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // doc type
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.DOC_TYPE_ATTRIBUTE, "GH.DOCUMENT_TYPE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // budget code
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.BUDGET_CODE_ATTRIBUTE, "GL.BG_CODE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // budget activity
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.BUDGET_ACTIVITY_ATTRIBUTE, "GL.BG_ACTIVITY", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // source of fund
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.FUND_SOURCE_ATTRIBUTE, "GL.FUND_SOURCE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // account type
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.ACCOUNT_TYPE_ATTIBUTE, "GL.ACCOUNT_TYPE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        return sb.toString();
    }

    private String generateSQLWhereAuthorizedCommon(String username) {
        StringBuilder sb = new StringBuilder();

        String AUTHORIZATION_OBJECT_NAME = "FICOMMON";
        String AUTHORIZATION_ACTIVITY = AuthorizeUtilService.READ_ACTIVITY;
        String authSQL = "";
        // company code
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.COMPANY_CODE_ATTRIBUTE, "GH.COMPANY_CODE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // area
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.AREA_ATTRIBUTE, "LPAD(GL.FI_AREA, 5, 'P')", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // payment center
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.PAYMENT_CENTER_ATTRIBUTE, "GL.PAYMENT_CENTER", null);
        if (Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // cost center
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.COST_CENTER_ATTRIBUTE, "GL.COST_CENTER", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // doc type
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.DOC_TYPE_ATTRIBUTE, "GH.DOCUMENT_TYPE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // budget code
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.BUDGET_CODE_ATTRIBUTE, "GL.BG_CODE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // budget activity
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.BUDGET_ACTIVITY_ATTRIBUTE, "GL.BG_ACTIVITY", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // source of fund
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.FUND_SOURCE_ATTRIBUTE, "GL.FUND_SOURCE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        // account type
        authSQL = authorizeUtilService.getUserAuthorizationSQL(username, AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.ACCOUNT_TYPE_ATTIBUTE, "GL.ACCOUNT_TYPE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        return sb.toString();
    }

    public void updateSelectGroupDocument(SelectGroupDocument selectGroupDocument) {
        List<Object> params = new ArrayList<>();
//        Map<String, Object> params2 = new HashMap<>();
        SelectGroupDocumentList selectGroupDocumentList = JSONUtil.convertJsonToObject(selectGroupDocument.getJsonText(), SelectGroupDocumentList.class);

        StringBuilder sql = new StringBuilder();

        sql.append(" UPDATE ");
        sql.append(" GL_HEAD GH ");
        params.add(selectGroupDocument.getDefineName());
//        params2.put("defineName", selectGroupDocument.getDefineName());
        sql.append(" SET SELECT_GROUP_DOCUMENT = ? ");
        sql.append(" WHERE ");
        sql.append(" 1 = 1  ");
        sql.append("  AND GH.DOCUMENT_TYPE != 'K2'  ");

        sql.append("  AND GH.PAYMENT_DOCUMENT_NO IS NULL           ");
        sql.append("  AND NVL(GH.PAYMENT_ID,0) = 0           ");

        int year = Integer.parseInt(selectGroupDocument.getFiscalYear()) - 543;
        params.add(year);
//        params2.put("fiscalYear", selectGroupDocument.getFiscalYear());
        sql.append(" AND ((UPPER(GH.ORIGINAL_FISCAL_YEAR) LIKE UPPER(?)  ");


        List<SelectGroupDocumentConfig> selectGroupDocumentConfigList = selectGroupDocumentList.getGroupList();
        int index = 0;
        for (int i = 0; i < selectGroupDocumentConfigList.size(); i++) {
            String checkOptionExclude = "";
            String from = "";
            String to = "";
            if (i == 0) {
                checkOptionExclude = "AND";
            } else {
                checkOptionExclude = "OR";
            }


            if (!Util.isEmpty(selectGroupDocumentConfigList.get(i).getCompanyCode())) {
                params.add(selectGroupDocumentConfigList.get(i).getCompanyCode());
                sql.append(checkOptionExclude + "  ((UPPER(GH.COMPANY_CODE) LIKE UPPER(?) ");
//                params2.put("GH.COMPANY_CODE" + i, selectGroupDocumentConfigList.get(i).getCompanyCode());

                List<BaseRange> listDocument = selectGroupDocumentConfigList.get(i).getList();

                DynamicCondition.baseRangeConditionOM(selectGroupDocumentConfigList.get(i).getList(), sql, params, "GH.ORIGINAL_DOCUMENT_NO");


                for (int x = 0; x < listDocument.size(); x++) {
                    String checkOptionExcludeStep2 = "";
                    if (x == 0) {
                        checkOptionExcludeStep2 = "AND";
                    } else {
                        if (listDocument.get(x - 1).isOptionExclude()) {
                            checkOptionExcludeStep2 = "AND";
                        } else {
                            if (listDocument.get(x).isOptionExclude()) {
                                checkOptionExcludeStep2 = "AND";
                            } else {
                                checkOptionExcludeStep2 = "OR";
                            }
                        }

                    }

                    if (!Util.isEmpty(listDocument.get(x).getFrom())) {

                        if (!Util.isEmpty(listDocument.get(x).getFrom())) {

                            from = listDocument.get(x).getFrom().concat(selectGroupDocumentConfigList.get(i).getCompanyCode()).concat(String.valueOf(year));

                        }
                        if (!Util.isEmpty(listDocument.get(x).getTo())) {
                            to = listDocument.get(x).getTo().concat(selectGroupDocumentConfigList.get(i).getCompanyCode()).concat(String.valueOf(year));
                        }
                        log.info(" from {} ", from);
                        log.info(" to {} ", to);

                        sql.append(" ) ");
                        if (x == 0) {
                            sql.append(" OR  ((UPPER(GH.COMPANY_CODE) LIKE UPPER('99999')) AND (UPPER(GH.DOCUMENT_TYPE) LIKE UPPER('KY')) ");

                        } else {
                            sql.append(checkOptionExcludeStep2 + "   ((UPPER(GH.COMPANY_CODE) LIKE UPPER('99999')) AND (UPPER(GH.DOCUMENT_TYPE) LIKE UPPER('KY')) ");

                        }


                        if (!listDocument.get(x).isOptionExclude()) {
                            if (!Util.isEmpty(listDocument.get(x).getFrom()) && !Util.isEmpty(listDocument.get(x).getTo())) {
                                sql.append(SqlUtil.newWhereClauseRangeOne(from, to,
                                        "GH.ORIGINAL_DOCUMENT", params, ++index, checkOptionExcludeStep2));
                            } else {
                                sql.append(SqlUtil.whereClause(from, "GH.ORIGINAL_DOCUMENT", params));
                            }
                        } else {
                            if (!Util.isEmpty(listDocument.get(x).getFrom()) && !Util.isEmpty(listDocument.get(x).getTo())) {
                                sql.append(SqlUtil.newWhereClauseNotRangeOne(from, to,
                                        "GH.ORIGINAL_DOCUMENT", params, ++index, checkOptionExcludeStep2));
                            } else {
                                sql.append(SqlUtil.whereClauseNot(from, "GH.ORIGINAL_DOCUMENT", params));
                            }

                        }
                    }

                }
                sql.append(" ) ");
                sql.append(" ) ");
            }

        }
        sql.append(" )) ");


        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        this.jdbcTemplate.update(sql.toString(), objParams);

    }

    @Override
    public void updateBlockReverse(String companyCode, String originalDocumentNo, String originalFiscalYear, String reverseDocumentNo, String reverseFiscalYear, String username, Timestamp updateDate, String docStatus) {
        final String sqlSave = "UPDATE GL_HEAD SET REVERSE_ORIGINAL_DOCUMENT_NO = ?, REVERSE_ORIGINAL_FISCAL_YEAR = ?, UPDATED_BY = ?, UPDATED = ?, DOCUMENT_STATUS = ? WHERE COMPANY_CODE = ? AND ORIGINAL_DOCUMENT_NO = ? AND ORIGINAL_FISCAL_YEAR = ?";
        this.jdbcTemplate.update(sqlSave, reverseDocumentNo, reverseFiscalYear, username, updateDate, docStatus, companyCode, originalDocumentNo, originalFiscalYear);
    }

    @Override
    public void updateGLHeadAfterReverseInvoice(String compCode, String accDocNo, String fiscalYear, String revAccDcNo, String revFiscalYear, Timestamp updateDate, String docStatus) {
        final String sqlSave = "UPDATE GL_HEAD SET REVERSE_ORIGINAL_DOCUMENT_NO = ?, REVERSE_ORIGINAL_FISCAL_YEAR = ?, UPDATED = ?, DOCUMENT_STATUS = ? WHERE COMPANY_CODE = ? AND ORIGINAL_DOCUMENT_NO = ? AND ORIGINAL_FISCAL_YEAR = ?";
        this.jdbcTemplate.update(sqlSave, revAccDcNo, revFiscalYear, updateDate, docStatus, compCode, accDocNo, fiscalYear);
    }

    @Override
    public void updateGLHeadAfterReversePayment(String compCode, String accDocNo, String fiscalYear, String username, Timestamp updateDate) {
        final String sqlSave = "UPDATE GL_HEAD SET PAYMENT_ID = ?, UPDATED_BY = ?, UPDATED = ? WHERE COMPANY_CODE = ? AND ORIGINAL_DOCUMENT_NO = ? AND ORIGINAL_FISCAL_YEAR = ?";
        this.jdbcTemplate.update(sqlSave, 0, username, updateDate, compCode, accDocNo, fiscalYear);
    }

}

