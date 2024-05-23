package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.SearchPaymentBlockRequest;
import th.com.bloomcode.paymentservice.model.common.BaseRange;
import th.com.bloomcode.paymentservice.model.common.CompanyCodeCondition;
import th.com.bloomcode.paymentservice.model.config.SelectGroupDocumentConfig;
import th.com.bloomcode.paymentservice.model.config.SelectGroupDocumentList;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;
import th.com.bloomcode.paymentservice.model.payment.SmartFileLog;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.model.request.PaymentBlockListDocumentRequest;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.UnBlockDocumentRepository;
import th.com.bloomcode.paymentservice.service.AuthorizeUtilService;
import th.com.bloomcode.paymentservice.util.DynamicCondition;
import th.com.bloomcode.paymentservice.util.JSONUtil;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
@Slf4j
public class UnBlockDocumentRepositoryImpl extends MetadataJdbcRepository<UnBlockDocument, Long>
        implements UnBlockDocumentRepository {

    @Value("${payment.dblink.schema}")
    private String schema;

    @Value("${payment.dblink.user}")
    private String user;

    private final AuthorizeUtilService authorizeUtilService;

    static BeanPropertyRowMapper<SmartFileLog> beanPropertyRowMapper =
            new BeanPropertyRowMapper<>(SmartFileLog.class);
    static RowMapper<UnBlockDocument> userRowMapper =
            (rs, rowNum) ->
                    new UnBlockDocument(
                            rs.getLong(UnBlockDocument.COLUMN_NAME_GL_HEAD_ID),
                            rs.getString(UnBlockDocument.COLUMN_NAME_LINE_INVOICE_DOCUMENT_NO),
                            rs.getString(UnBlockDocument.COLUMN_NAME_LINE_COMPANY_CODE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_LINE_INVOICE_FISCAL_YEAR),
                            rs.getString(UnBlockDocument.COLUMN_NAME_LINE_DOCUMENT_BASE_TYPE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_LINE_DOCUMENT_TYPE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_LINE_PAYMENT_BLOCK),
                            rs.getInt(UnBlockDocument.COLUMN_NAME_COUNT_CHILD),
                            rs.getString(UnBlockDocument.COLUMN_NAME_PO_DOCUMENT_NO),
                            rs.getTimestamp(UnBlockDocument.COLUMN_NAME_REVERSE_DATE_ACCT),
                            rs.getString(UnBlockDocument.COLUMN_NAME_REVERSE_REASON),
                            rs.getString(UnBlockDocument.COLUMN_NAME_INVOICE_DOCUMENT_NO),
                            rs.getString(UnBlockDocument.COLUMN_NAME_REVERSE_INVOICE_DOCUMENT_NO),
                            rs.getString(UnBlockDocument.COLUMN_NAME_HEADER_REFERENCE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_HEADER_REFERENCE2),
                            rs.getString(UnBlockDocument.COLUMN_NAME_DOCUMENT_HEADER_TEXT),
                            rs.getString(UnBlockDocument.COLUMN_NAME_ORIGINAL_DOCUMENT_NO),
                            rs.getString(UnBlockDocument.COLUMN_NAME_ORIGINAL_FISCAL_YEAR),
                            rs.getString(UnBlockDocument.COLUMN_NAME_REVERSE_ORIGINAL_DOCUMENT_NO),
                            rs.getString(UnBlockDocument.COLUMN_NAME_REVERSE_ORIGINAL_FISCAL_YEAR),
                            rs.getString(UnBlockDocument.COLUMN_NAME_COST_CENTER1),
                            rs.getString(UnBlockDocument.COLUMN_NAME_COST_CENTER2),
                            rs.getString(UnBlockDocument.COLUMN_NAME_DOCUMENT_TYPE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_COMPANY_CODE),
                            rs.getTimestamp(UnBlockDocument.COLUMN_NAME_DATE_DOC),
                            rs.getTimestamp(UnBlockDocument.COLUMN_NAME_DATE_ACCT),
                            rs.getInt(UnBlockDocument.COLUMN_NAME_PERIOD),
                            rs.getString(UnBlockDocument.COLUMN_NAME_CURRENCY),
                            rs.getString(UnBlockDocument.COLUMN_NAME_ORIGINAL_DOCUMENT),
                            rs.getTimestamp(UnBlockDocument.COLUMN_NAME_DOCUMENT_CREATED),
                            rs.getString(UnBlockDocument.COLUMN_NAME_USER_PARK),
                            rs.getString(UnBlockDocument.COLUMN_NAME_USER_POST),
                            rs.getString(UnBlockDocument.COLUMN_NAME_POSTING_KEY),
                            rs.getString(UnBlockDocument.COLUMN_NAME_ACCOUNT_TYPE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_DR_CR),
                            rs.getString(UnBlockDocument.COLUMN_NAME_GL_ACCOUNT),
                            rs.getString(UnBlockDocument.COLUMN_NAME_FI_AREA),
                            rs.getString(UnBlockDocument.COLUMN_NAME_COST_CENTER),
                            rs.getString(UnBlockDocument.COLUMN_NAME_FUND_SOURCE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_BG_CODE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_BG_ACTIVITY),
                            rs.getString(UnBlockDocument.COLUMN_NAME_COST_ACTIVITY),
                            rs.getBigDecimal(UnBlockDocument.COLUMN_NAME_AMOUNT),
                            rs.getString(UnBlockDocument.COLUMN_NAME_REFERENCE3),
                            rs.getString(UnBlockDocument.COLUMN_NAME_ASSIGNMENT),
                            rs.getString(UnBlockDocument.COLUMN_NAME_BR_DOCUMENT_NO),
                            rs.getString(UnBlockDocument.COLUMN_NAME_BR_LINE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_PAYMENT_CENTER),
                            rs.getString(UnBlockDocument.COLUMN_NAME_BANK_BOOK),
                            rs.getString(UnBlockDocument.COLUMN_NAME_BANK_BRANCH_NO),
                            rs.getString(UnBlockDocument.COLUMN_NAME_TRADING_PARTNER),
                            rs.getString(UnBlockDocument.COLUMN_NAME_TRADING_PARTNER_PARK),
                            rs.getString(UnBlockDocument.COLUMN_NAME_SPECIAL_GL),
                            rs.getTimestamp(UnBlockDocument.COLUMN_NAME_DATE_BASE_LINE),
                            rs.getTimestamp(UnBlockDocument.COLUMN_NAME_DATE_VALUE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_ASSET_NO),
                            rs.getString(UnBlockDocument.COLUMN_NAME_ASSET_SUB_NO),
                            rs.getBigDecimal(UnBlockDocument.COLUMN_NAME_QTY),
                            rs.getString(UnBlockDocument.COLUMN_NAME_UOM),
                            rs.getString(UnBlockDocument.COLUMN_NAME_INCOME),
                            rs.getString(UnBlockDocument.COLUMN_NAME_REFERENCE1),
                            rs.getString(UnBlockDocument.COLUMN_NAME_REFERENCE2),
                            rs.getInt(UnBlockDocument.COLUMN_NAME_PO_LINE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_PAYMENT_REFERENCE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_SUB_ACCOUNT),
                            rs.getString(UnBlockDocument.COLUMN_NAME_SUB_ACCOUNT_OWNER),
                            rs.getString(UnBlockDocument.COLUMN_NAME_DEPOSIT_ACCOUNT),
                            rs.getString(UnBlockDocument.COLUMN_NAME_DEPOSIT_ACCOUNT_OWNER),
                            rs.getString(UnBlockDocument.COLUMN_NAME_GPSC),
                            rs.getString(UnBlockDocument.COLUMN_NAME_GPSC_GROUP),
                            rs.getString(UnBlockDocument.COLUMN_NAME_LINE_ITEM_TEXT),
                            rs.getString(UnBlockDocument.COLUMN_NAME_LINE_DESC),
                            rs.getString(UnBlockDocument.COLUMN_NAME_PAYMENT_TERM),
                            rs.getString(UnBlockDocument.COLUMN_NAME_PAYMENT_METHOD),
                            rs.getString(UnBlockDocument.COLUMN_NAME_WTX_TYPE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_WTX_CODE),
                            rs.getBigDecimal(UnBlockDocument.COLUMN_NAME_WTX_BASE),
                            rs.getBigDecimal(UnBlockDocument.COLUMN_NAME_WTX_AMOUNT),
                            rs.getString(UnBlockDocument.COLUMN_NAME_WTX_TYPE_P),
                            rs.getString(UnBlockDocument.COLUMN_NAME_WTX_CODE_P),
                            rs.getBigDecimal(UnBlockDocument.COLUMN_NAME_WTX_BASE_P),
                            rs.getBigDecimal(UnBlockDocument.COLUMN_NAME_WTX_AMOUNT_P),
                            rs.getString(UnBlockDocument.COLUMN_NAME_PAYEE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_PAYEE_TAX_ID),
                            rs.getString(UnBlockDocument.COLUMN_NAME_VENDOR),
                            rs.getString(UnBlockDocument.COLUMN_NAME_VENDOR_NAME),
                            rs.getString(UnBlockDocument.COLUMN_NAME_VENDOR_TAX_ID),
                            rs.getString(UnBlockDocument.COLUMN_NAME_BANK_ACCOUNT_NO),
                            rs.getString(UnBlockDocument.COLUMN_NAME_BANK_ACCOUNT_HOLDER_NAME),
                            rs.getString(UnBlockDocument.COLUMN_NAME_PAYMENT_BLOCK),
                            rs.getBoolean(UnBlockDocument.COLUMN_NAME_CONFIRM_VENDOR),
                            rs.getString(UnBlockDocument.COLUMN_NAME_FUND_TYPE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_DOCUMENT_BASE_TYPE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_SELECT_GROUP_DOCUMENT),
                            rs.getLong(UnBlockDocument.COLUMN_NAME_PAYMENT_ID),
                            rs.getString(UnBlockDocument.COLUMN_NAME_PAYMENT_NAME),
                            rs.getTimestamp(UnBlockDocument.COLUMN_NAME_PAYMENT_DATE),
                            rs.getString(UnBlockDocument.COLUMN_NAME_PROPOSAL_STATUS),
                            rs.getString(UnBlockDocument.COLUMN_NAME_RUN_STATUS)
                    );
    private final JdbcTemplate jdbcTemplate;

    public UnBlockDocumentRepositoryImpl(
            @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, AuthorizeUtilService authorizeUtilService) {
        super(userRowMapper, null, null, null, null, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        this.authorizeUtilService = authorizeUtilService;
    }

    @Override
    public List<UnBlockDocument> findUnBlockByCondition(JwtBody jwt,
                                                        SearchPaymentBlockRequest searchPaymentBlockRequest) {
        try {
            List<Object> params = new ArrayList<>();
            StringBuilder sql = new StringBuilder();
//            sql.append("          SELECT H.ID                                                                                   ID,           ");
//            sql.append("          DECODE(L.INVOICE_DOCUMENT_NO, NULL, LC.ORIGINAL_DOCUMENT_NO, L.INVOICE_DOCUMENT_NO) AS LINE_INVOICE_DOCUMENT_NO,           ");
//            sql.append("          DECODE(L.INVOICE_FISCAL_YEAR, NULL, LC.ORIGINAL_FISCAL_YEAR, L.INVOICE_FISCAL_YEAR) AS LINE_INVOICE_FISCAL_YEAR,           ");
//            sql.append("          DECODE(HP.DOCUMENT_BASE_TYPE, NULL, HC.DOCUMENT_BASE_TYPE, HP.DOCUMENT_BASE_TYPE)                                                               AS LINE_DOCUMENT_BASE_TYPE,           ");
//            sql.append("          DECODE(HP.COMPANY_CODE, NULL, HC.COMPANY_CODE, HP.COMPANY_CODE)                                                               AS LINE_COMPANY_CODE,           ");
//            sql.append("          DECODE(HP.DOCUMENT_TYPE, NULL, HC.DOCUMENT_TYPE, HP.DOCUMENT_TYPE)                                                                    AS LINE_DOCUMENT_TYPE,           ");
//            sql.append("          (SELECT COUNT(1) ");
//            sql.append("          FROM GL_HEAD GH3 ");
//            sql.append("          WHERE GH3.HEADER_REFERENCE = H.HEADER_REFERENCE ");
//            sql.append("          AND GH3.DOCUMENT_TYPE IN ('K3', 'KA', 'KB', 'KG', 'KC') ");
//            sql.append("          AND H.HEADER_REFERENCE LIKE 'AA#%' ");
//            sql.append("           GROUP BY GH3.HEADER_REFERENCE)                                                     AS COUNT_CHILD, ");
//            sql.append("          H.PO_DOCUMENT_NO                                                                       PO_DOCUMENT_NO,           ");
//            sql.append("          H.REVERSE_DATE_ACCT                                                                    REVERSE_DATE_ACCT,           ");
//            sql.append("          H.REVERSE_REASON                                                                       REVERSE_REASON,           ");
//            sql.append("          H.INVOICE_DOCUMENT_NO                                                                  INVOICE_DOCUMENT_NO,           ");
//            sql.append("          H.REVERSE_INVOICE_DOCUMENT_NO                                                          REVERSE_INVOICE_DOCUMENT_NO,           ");
//            sql.append("          H.HEADER_REFERENCE                                                                     HEADER_REFERENCE,           ");
//            sql.append("          H.HEADER_REFERENCE2                                                                    HEADER_REFERENCE2,           ");
//            sql.append("          H.DOCUMENT_HEADER_TEXT                                                                 DOCUMENT_HEADER_TEXT,           ");
//            sql.append("          H.ORIGINAL_DOCUMENT_NO                                                                 ORIGINAL_DOCUMENT_NO,           ");
//            sql.append("          H.ORIGINAL_FISCAL_YEAR                                                                 ORIGINAL_FISCAL_YEAR,           ");
//            sql.append("          H.REVERSE_ORIGINAL_DOCUMENT_NO                                                         REVERSE_ORIGINAL_DOCUMENT_NO,           ");
//            sql.append("          H.REVERSE_ORIGINAL_FISCAL_YEAR                                                         REVERSE_ORIGINAL_FISCAL_YEAR,           ");
//            sql.append("          H.COST_CENTER1                                                                         COST_CENTER1,           ");
//            sql.append("          H.COST_CENTER2                                                                         COST_CENTER2,           ");
//            sql.append("          H.DOCUMENT_TYPE                                                                        DOCUMENT_TYPE,           ");
//            sql.append("          H.COMPANY_CODE                                                                         COMPANY_CODE,           ");
//            sql.append("          H.DATE_DOC                                                                             DATE_DOC,           ");
//            sql.append("          H.DATE_ACCT                                                                            DATE_ACCT,           ");
//            sql.append("          H.PERIOD                                                                               PERIOD,           ");
//            sql.append("          H.CURRENCY                                                                             CURRENCY,           ");
//            sql.append("          H.ORIGINAL_DOCUMENT                                                                    ORIGINAL_DOCUMENT,           ");
//            sql.append("          H.DOCUMENT_CREATED                                                                     DOCUMENT_CREATED,           ");
//            sql.append("          H.USER_PARK                                                                            USER_PARK,           ");
//            sql.append("          H.USER_POST                                                                            USER_POST,           ");
//            sql.append("          L.POSTING_KEY                                                                          POSTING_KEY,           ");
//            sql.append("          L.ACCOUNT_TYPE                                                                         ACCOUNT_TYPE,           ");
//            sql.append("          L.DR_CR                                                                                DR_CR,           ");
//            sql.append("          L.GL_ACCOUNT                                                                           GL_ACCOUNT,           ");
//            sql.append("          L.FI_AREA                                                                              FI_AREA,           ");
//            sql.append("          L.COST_CENTER                                                                          COST_CENTER,           ");
//            sql.append("          L.FUND_SOURCE                                                                          FUND_SOURCE,           ");
//            sql.append("          L.BG_CODE                                                                              BG_CODE,           ");
//            sql.append("          L.BG_ACTIVITY                                                                          BG_ACTIVITY,           ");
//            sql.append("          L.COST_ACTIVITY                                                                        COST_ACTIVITY,           ");
//            sql.append("          SUM_L.AMOUNT                                                                        AS AMOUNT,           ");
//            sql.append("          L.REFERENCE3                                                                           REFERENCE3,           ");
//            sql.append("          L.ASSIGNMENT                                                                           ASSIGNMENT,           ");
//            sql.append("          H.BR_DOCUMENT_NO                                                                       BR_DOCUMENT_NO,           ");
//            sql.append("          CNT_L.BR_LINE                                                                       AS BR_LINE,           ");
//            sql.append("          L.PAYMENT_CENTER                                                                       PAYMENT_CENTER,           ");
//            sql.append("          L.BANK_BOOK                                                                            BANK_BOOK,           ");
//            sql.append("          L.BANK_BRANCH_NO                                                                       BANK_BRANCH_NO,           ");
//            sql.append("          L.TRADING_PARTNER                                                                      TRADING_PARTNER,           ");
//            sql.append("          L.TRADING_PARTNER_PARK                                                                 TRADING_PARTNER_PARK,           ");
//            sql.append("          L.SPECIAL_GL                                                                           SPECIAL_GL,           ");
//            sql.append("          L.DATE_BASE_LINE                                                                       DATE_BASE_LINE,           ");
//            sql.append("          L.DATE_VALUE                                                                           DATE_VALUE,           ");
//            sql.append("          L.ASSET_NO                                                                             ASSET_NO,           ");
//            sql.append("          L.ASSET_SUB_NO                                                                         ASSET_SUB_NO,           ");
//            sql.append("          L.QTY                                                                                  QTY,           ");
//            sql.append("          L.UOM                                                                                  UOM,           ");
//            sql.append("          L.INCOME                                                                               INCOME,           ");
//            sql.append("          L.REFERENCE1                                                                           REFERENCE1,           ");
//            sql.append("          L.REFERENCE2                                                                           REFERENCE2,           ");
//            sql.append("          L.PO_LINE                                                                           AS PO_LINE,           ");
//            sql.append("          L.PAYMENT_REFERENCE                                                                    PAYMENT_REFERENCE,           ");
//            sql.append("          L.SUB_ACCOUNT                                                                          SUB_ACCOUNT,           ");
//            sql.append("          L.SUB_ACCOUNT_OWNER                                                                    SUB_ACCOUNT_OWNER,           ");
//            sql.append("          L.DEPOSIT_ACCOUNT                                                                      DEPOSIT_ACCOUNT,           ");
//            sql.append("          L.DEPOSIT_ACCOUNT_OWNER                                                                DEPOSIT_ACCOUNT_OWNER,           ");
//            sql.append("          L.GPSC                                                                                 GPSC,           ");
//            sql.append("          L.GPSC_GROUP                                                                           GPSC_GROUP,           ");
//            sql.append("          L.LINE_ITEM_TEXT                                                                       LINE_ITEM_TEXT,           ");
//            sql.append("          L.LINE_DESC                                                                            LINE_DESC,           ");
//            sql.append("          L.PAYMENT_TERM                                                                         PAYMENT_TERM,           ");
//            sql.append("          L.PAYMENT_METHOD                                                                       PAYMENT_METHOD,           ");
//            sql.append("          L.WTX_TYPE                                                                             WTX_TYPE,           ");
//            sql.append("          L.WTX_CODE                                                                             WTX_CODE,           ");
//            sql.append("          L.WTX_BASE                                                                             WTX_BASE,           ");
//            sql.append("          L.WTX_AMOUNT                                                                           WTX_AMOUNT,           ");
//            sql.append("          L.WTX_TYPE_P                                                                           WTX_TYPE_P,           ");
//            sql.append("          L.WTX_CODE_P                                                                           WTX_CODE_P,           ");
//            sql.append("          L.WTX_BASE_P                                                                           WTX_BASE_P,           ");
//            sql.append("          L.WTX_AMOUNT_P                                                                         WTX_AMOUNT_P,           ");
//            sql.append("          L.PAYEE                                                                                PAYEE,           ");
//            sql.append("          L.PAYEE_TAX_ID                                                                         PAYEE_TAX_ID,           ");
//            sql.append("          L.VENDOR                                                                               VENDOR,           ");
//            sql.append("          L.VENDOR_NAME                                                                          VENDOR_NAME,           ");
//            sql.append("          L.VENDOR_TAX_ID                                                                        VENDOR_TAX_ID,           ");
//            sql.append("          L.BANK_ACCOUNT_NO                                                                      BANK_ACCOUNT_NO,           ");
//            sql.append("          L.BANK_ACCOUNT_HOLDER_NAME                                                             BANK_ACCOUNT_HOLDER_NAME,           ");
//            sql.append("          L.PAYMENT_BLOCK                                                                        PAYMENT_BLOCK,           ");
//            sql.append("          AC.CONFIRMSTATUS                                                                       CONFIRM_VENDOR,           ");
//            sql.append("          L.FUND_TYPE                                                                            FUND_TYPE,           ");
//            sql.append("          H.DOCUMENT_BASE_TYPE                                                                   DOCUMENT_BASE_TYPE,           ");
//            sql.append("          H.SELECT_GROUP_DOCUMENT                                                                SELECT_GROUP_DOCUMENT           ");
//            sql.append("          FROM GL_HEAD H           ");
//            sql.append("          LEFT JOIN GL_LINE L ON H.ID = L.GL_HEAD_ID           ");
//            sql.append("          LEFT JOIN (SELECT COUNT(1) AS BR_LINE, ORIGINAL_DOCUMENT_NO FROM GL_LINE GROUP BY ORIGINAL_DOCUMENT_NO) CNT_L           ");
//            sql.append("          ON H.ORIGINAL_DOCUMENT_NO = CNT_L.ORIGINAL_DOCUMENT_NO           ");
//            sql.append("          LEFT JOIN (SELECT SUM(AMOUNT) AS AMOUNT, ORIGINAL_DOCUMENT_NO, ORIGINAL_FISCAL_YEAR           ");
//            sql.append("          FROM GL_LINE           ");
//            sql.append("          WHERE ACCOUNT_TYPE = 'K'           ");
//            sql.append("          GROUP BY ORIGINAL_DOCUMENT_NO, ORIGINAL_FISCAL_YEAR) SUM_L           ");
//            sql.append("          ON H.ORIGINAL_DOCUMENT_NO = SUM_L.ORIGINAL_DOCUMENT_NO           ");
//            sql.append("          AND H.ORIGINAL_FISCAL_YEAR = SUM_L.ORIGINAL_FISCAL_YEAR           ");
//            sql.append("          LEFT JOIN GL_LINE LC ON LC.INVOICE_DOCUMENT_NO = L.ORIGINAL_DOCUMENT_NO AND LC.ACCOUNT_TYPE = 'K' AND LC.COMPANY_CODE = L.COMPANY_CODE AND LC.INVOICE_FISCAL_YEAR = L.INVOICE_FISCAL_YEAR           ");
//            sql.append("          LEFT JOIN GL_HEAD HC ON LC.GL_HEAD_ID = HC.ID           ");
//            sql.append("          LEFT JOIN GL_LINE LP ON LP.ORIGINAL_DOCUMENT_NO = L.INVOICE_DOCUMENT_NO AND LP.ACCOUNT_TYPE = 'K' AND LP.COMPANY_CODE = L.COMPANY_CODE AND LP.INVOICE_FISCAL_YEAR = L.INVOICE_FISCAL_YEAR           ");
//            sql.append("          LEFT JOIN GL_HEAD HP ON LP.GL_HEAD_ID = HP.ID           ");
            // tuning
            sql.append("          WITH BANK_ACCOUNT AS (SELECT *           ");
            sql.append("          FROM ").append(schema).append(".C_BP_BANKACCOUNT           ");
            sql.append("          WHERE ROWID in           ");
            sql.append("          (SELECT MAX(ROWID)           ");
            sql.append("          FROM ").append(schema).append(".C_BP_BANKACCOUNT BB           ");
            sql.append("          WHERE BB.ISACTIVE = 'Y'           ");
            sql.append("          GROUP BY BB.C_BPARTNER_ID, BB.ACCOUNTNO))           ");
            sql.append("          SELECT H.ID,           ");
            sql.append("          L.INVOICE_DOCUMENT_NO                            AS LINE_INVOICE_DOCUMENT_NO,           ");
            sql.append("          L.INVOICE_FISCAL_YEAR                             AS LINE_INVOICE_FISCAL_YEAR,           ");
            sql.append("          NULL                            AS LINE_DOCUMENT_BASE_TYPE,           ");
            sql.append("          NULL                            AS LINE_COMPANY_CODE,           ");
            sql.append("          NULL                            AS LINE_DOCUMENT_TYPE,           ");
            sql.append("          NULL                            AS LINE_PAYMENT_BLOCK,           ");


            sql.append("          (SELECT COUNT(1)           ");
            sql.append("          FROM GL_HEAD GH3           ");
            sql.append("          WHERE GH3.HEADER_REFERENCE = H.HEADER_REFERENCE           ");
            sql.append("          AND GH3.DOCUMENT_TYPE IN ('K3', 'KA', 'KB', 'KG', 'KC')           ");
            sql.append("          AND H.HEADER_REFERENCE LIKE 'AA#%'           ");
            sql.append("          GROUP BY GH3.HEADER_REFERENCE)                                                     AS COUNT_CHILD,           ");
            sql.append("          H.PO_DOCUMENT_NO                                                                       PO_DOCUMENT_NO,           ");
            sql.append("          H.REVERSE_DATE_ACCT                                                                    REVERSE_DATE_ACCT,           ");
            sql.append("          H.REVERSE_REASON                                                                       REVERSE_REASON,           ");
            sql.append("          H.INVOICE_DOCUMENT_NO                                                                  INVOICE_DOCUMENT_NO,           ");
            sql.append("          H.REVERSE_INVOICE_DOCUMENT_NO                                                          REVERSE_INVOICE_DOCUMENT_NO,           ");
            sql.append("          H.HEADER_REFERENCE                                                                     HEADER_REFERENCE,           ");
            sql.append("          H.HEADER_REFERENCE2                                                                    HEADER_REFERENCE2,           ");
            sql.append("          H.DOCUMENT_HEADER_TEXT                                                                 DOCUMENT_HEADER_TEXT,           ");
            sql.append("          H.ORIGINAL_DOCUMENT_NO                                                                 ORIGINAL_DOCUMENT_NO,           ");
            sql.append("          H.ORIGINAL_FISCAL_YEAR                                                                 ORIGINAL_FISCAL_YEAR,           ");
            sql.append("          H.REVERSE_ORIGINAL_DOCUMENT_NO                                                         REVERSE_ORIGINAL_DOCUMENT_NO,           ");
            sql.append("          H.REVERSE_ORIGINAL_FISCAL_YEAR                                                         REVERSE_ORIGINAL_FISCAL_YEAR,           ");
            sql.append("          H.COST_CENTER1                                                                         COST_CENTER1,           ");
            sql.append("          H.COST_CENTER2                                                                         COST_CENTER2,           ");
            sql.append("          H.DOCUMENT_TYPE                                                                        DOCUMENT_TYPE,           ");
            sql.append("          H.COMPANY_CODE                                                                         COMPANY_CODE,           ");
            sql.append("          H.DATE_DOC                                                                             DATE_DOC,           ");
            sql.append("          H.DATE_ACCT                                                                            DATE_ACCT,           ");
            sql.append("          H.PERIOD                                                                               PERIOD,           ");
            sql.append("          H.CURRENCY                                                                             CURRENCY,           ");
            sql.append("          H.ORIGINAL_DOCUMENT                                                                    ORIGINAL_DOCUMENT,           ");
            sql.append("          H.DOCUMENT_CREATED                                                                     DOCUMENT_CREATED,           ");
            sql.append("          H.USER_PARK                                                                            USER_PARK,           ");
            sql.append("          H.USER_POST                                                                            USER_POST,           ");
            sql.append("          L.POSTING_KEY                                                                          POSTING_KEY,           ");
            sql.append("          L.ACCOUNT_TYPE                                                                         ACCOUNT_TYPE,           ");
            sql.append("          L.DR_CR                                                                                DR_CR,           ");
            sql.append("          L.GL_ACCOUNT                                                                           GL_ACCOUNT,           ");
            sql.append("          L.FI_AREA                                                                              FI_AREA,           ");
            sql.append("          L.COST_CENTER                                                                          COST_CENTER,           ");
            sql.append("          L.FUND_SOURCE                                                                          FUND_SOURCE,           ");
            sql.append("          L.BG_CODE                                                                              BG_CODE,           ");
            sql.append("          L.BG_ACTIVITY                                                                          BG_ACTIVITY,           ");
            sql.append("          L.COST_ACTIVITY                                                                        COST_ACTIVITY,           ");
            sql.append("          SUM_L.AMOUNT                                                                        AS AMOUNT,           ");
            sql.append("          L.REFERENCE3                                                                           REFERENCE3,           ");
            sql.append("          L.ASSIGNMENT                                                                           ASSIGNMENT,           ");
            sql.append("          H.BR_DOCUMENT_NO                                                                       BR_DOCUMENT_NO,           ");
            sql.append("          CNT_L.BR_LINE                                                                       AS BR_LINE,           ");
            sql.append("          L.PAYMENT_CENTER                                                                       PAYMENT_CENTER,           ");
            sql.append("          L.BANK_BOOK                                                                            BANK_BOOK,           ");
            sql.append("          L.BANK_BRANCH_NO                                                                       BANK_BRANCH_NO,           ");
            sql.append("          L.TRADING_PARTNER                                                                      TRADING_PARTNER,           ");
            sql.append("          L.TRADING_PARTNER_PARK                                                                 TRADING_PARTNER_PARK,           ");
            sql.append("          L.SPECIAL_GL                                                                           SPECIAL_GL,           ");
            sql.append("          L.DATE_BASE_LINE                                                                       DATE_BASE_LINE,           ");
            sql.append("          L.DATE_VALUE                                                                           DATE_VALUE,           ");
            sql.append("          L.ASSET_NO                                                                             ASSET_NO,           ");
            sql.append("          L.ASSET_SUB_NO                                                                         ASSET_SUB_NO,           ");
            sql.append("          L.QTY                                                                                  QTY,           ");
            sql.append("          L.UOM                                                                                  UOM,           ");
            sql.append("          L.INCOME                                                                               INCOME,           ");
            sql.append("          L.REFERENCE1                                                                           REFERENCE1,           ");
            sql.append("          L.REFERENCE2                                                                           REFERENCE2,           ");
            sql.append("          L.PO_LINE                                                                           AS PO_LINE,           ");
            sql.append("          L.PAYMENT_REFERENCE                                                                    PAYMENT_REFERENCE,           ");
            sql.append("          L.SUB_ACCOUNT                                                                          SUB_ACCOUNT,           ");
            sql.append("          L.SUB_ACCOUNT_OWNER                                                                    SUB_ACCOUNT_OWNER,           ");
            sql.append("          L.DEPOSIT_ACCOUNT                                                                      DEPOSIT_ACCOUNT,           ");
            sql.append("          L.DEPOSIT_ACCOUNT_OWNER                                                                DEPOSIT_ACCOUNT_OWNER,           ");
            sql.append("          L.GPSC                                                                                 GPSC,           ");
            sql.append("          L.GPSC_GROUP                                                                           GPSC_GROUP,           ");
            sql.append("          L.LINE_ITEM_TEXT                                                                       LINE_ITEM_TEXT,           ");
            sql.append("          L.LINE_DESC                                                                            LINE_DESC,           ");
            sql.append("          L.PAYMENT_TERM                                                                         PAYMENT_TERM,           ");
            sql.append("          L.PAYMENT_METHOD                                                                       PAYMENT_METHOD,           ");
            sql.append("          L.WTX_TYPE                                                                             WTX_TYPE,           ");
            sql.append("          L.WTX_CODE                                                                             WTX_CODE,           ");
            sql.append("          L.WTX_BASE                                                                             WTX_BASE,           ");
            sql.append("          L.WTX_AMOUNT                                                                           WTX_AMOUNT,           ");
            sql.append("          L.WTX_TYPE_P                                                                           WTX_TYPE_P,           ");
            sql.append("          L.WTX_CODE_P                                                                           WTX_CODE_P,           ");
            sql.append("          L.WTX_BASE_P                                                                           WTX_BASE_P,           ");
            sql.append("          L.WTX_AMOUNT_P                                                                         WTX_AMOUNT_P,           ");
            sql.append("          L.PAYEE                                                                                PAYEE,           ");
            sql.append("          L.PAYEE_TAX_ID                                                                         PAYEE_TAX_ID,           ");
            sql.append("          L.VENDOR                                                                               VENDOR,           ");
            sql.append("          VD.NAME                                                                          VENDOR_NAME,           ");
            sql.append("          L.VENDOR_TAX_ID                                                                        VENDOR_TAX_ID,           ");
            sql.append("          L.BANK_ACCOUNT_NO                                                                      BANK_ACCOUNT_NO,           ");
            sql.append("          VB.A_NAME                                                             BANK_ACCOUNT_HOLDER_NAME,           ");
            sql.append("          L.PAYMENT_BLOCK                                                                        PAYMENT_BLOCK,           ");
            sql.append("          AC.CONFIRMSTATUS                                                                       CONFIRM_VENDOR,           ");
            sql.append("          FT.VALUECODE                                                                           FUND_TYPE,           ");
            sql.append("          H.DOCUMENT_BASE_TYPE                                                                   DOCUMENT_BASE_TYPE,           ");
            sql.append("          H.SELECT_GROUP_DOCUMENT                                                                SELECT_GROUP_DOCUMENT,          ");
            sql.append("          NULL  AS PAYMENT_ID,           ");
            sql.append("          NULL  AS PAYMENT_NAME,           ");
            sql.append("          NULL  AS PAYMENT_DATE,           ");
            sql.append("          NULL  AS PROPOSAL_STATUS,           ");
            sql.append("          NULL  AS RUN_STATUS           ");
            sql.append("          FROM GL_HEAD H           ");
            sql.append("          LEFT JOIN GL_LINE L ON H.ID = L.GL_HEAD_ID           ");
            sql.append("          LEFT JOIN (SELECT COUNT(1) AS BR_LINE, ORIGINAL_DOCUMENT_NO, COMPANY_CODE FROM GL_LINE GROUP BY ORIGINAL_DOCUMENT_NO, COMPANY_CODE) CNT_L           ");
            sql.append("          ON H.ORIGINAL_DOCUMENT_NO = CNT_L.ORIGINAL_DOCUMENT_NO AND H.COMPANY_CODE = CNT_L.COMPANY_CODE           ");
            sql.append("          LEFT JOIN (SELECT SUM(AMOUNT) AS AMOUNT, ORIGINAL_DOCUMENT_NO, ORIGINAL_FISCAL_YEAR, COMPANY_CODE           ");
            sql.append("          FROM GL_LINE           ");
            sql.append("          WHERE ACCOUNT_TYPE = 'K'           ");
            sql.append("          GROUP BY ORIGINAL_DOCUMENT_NO, ORIGINAL_FISCAL_YEAR, COMPANY_CODE) SUM_L           ");
            sql.append("          ON H.ORIGINAL_DOCUMENT_NO = SUM_L.ORIGINAL_DOCUMENT_NO AND           ");
            sql.append("          H.ORIGINAL_FISCAL_YEAR = SUM_L.ORIGINAL_FISCAL_YEAR AND           ");
            sql.append("          H.COMPANY_CODE = SUM_L.COMPANY_CODE           ");
            sql.append("          LEFT JOIN ").append(schema).append(".C_YEAR cy ON L.ORIGINAL_FISCAL_YEAR = cy.FISCALYEAR           ");
            sql.append("          LEFT JOIN ").append(schema).append(".TH_BGFUNDSOURCE fs ON L.FUND_SOURCE = fs.VALUECODE AND cy.C_YEAR_ID = fs.C_YEAR_ID           ");
            sql.append("          LEFT JOIN ").append(schema).append(".TH_BGFUNDTYPE ft ON FS.TH_BGFUNDTYPE_ID = FT.TH_BGFUNDTYPE_ID           ");
            sql.append("    LEFT  JOIN ").append(schema).append(".C_BPARTNER").append(" VD ON VD.VALUE = DECODE(L.PAYEE,null,L.VENDOR,L.PAYEE) ");
            sql.append("    LEFT  JOIN ").append(schema).append(".TH_APBPARTNERSTATUS").append(" AC ON VD.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
            // tuning
            sql.append("    LEFT JOIN BANK_ACCOUNT VB ON VD.C_BPARTNER_ID = VB.C_BPARTNER_ID AND L.BANK_ACCOUNT_NO = VB.ACCOUNTNO ");
//            sql.append("          OUTER APPLY (           ");
//            sql.append("          SELECT *           ");
//
//            sql.append("    FROM ").append(schema).append(".C_BP_BANKACCOUNT BB");
//            sql.append("          WHERE BB.ISACTIVE = 'Y'           ");
//            sql.append("          AND vd.C_BPARTNER_ID = BB.C_BPARTNER_ID           ");
//            sql.append("          AND L.BANK_ACCOUNT_NO = BB.ACCOUNTNO           ");
//            sql.append("          ORDER BY BB.ACCOUNTNO           ");
//            sql.append("          FETCH FIRST ROW ONLY           ");
//            sql.append("          ) vb           ");


            sql.append("          WHERE H.ORIGINAL_DOCUMENT_NO = L.ORIGINAL_DOCUMENT_NO           ");
            sql.append("          AND H.ORIGINAL_FISCAL_YEAR = L.ORIGINAL_FISCAL_YEAR           ");
            sql.append("          AND H.COMPANY_CODE = L.COMPANY_CODE           ");
            sql.append("          AND H.REVERSE_ORIGINAL_DOCUMENT_NO IS NULL           ");
            sql.append("          AND H.DOCUMENT_BASE_TYPE <> 'KY'           ");
            sql.append("          AND H.COMPANY_CODE <> '99999'           ");

//            sql.append("          AND (L.PAYMENT_BLOCK = 'B' OR (L.PAYMENT_BLOCK IN ('0','A') AND H.DOCUMENT_BASE_TYPE = 'APC'))           ");
            sql.append("          AND L.PAYMENT_BLOCK = 'B' ");
            sql.append("          AND L.ACCOUNT_TYPE = 'K'           ");

            sql.append(generateSQLWhereAuthorized(jwt, params));


            if (searchPaymentBlockRequest.getListFiArea() != null && searchPaymentBlockRequest.getListFiArea().size() > 0) {
                DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getListFiArea(), sql, params, "L.FI_AREA");

            }else{
                if (!Util.isEmpty(searchPaymentBlockRequest.getFiAreaFrom())) {
                    sql.append(
                            SqlUtil.whereClauseRange(
                                    searchPaymentBlockRequest.getFiAreaFrom(),
                                    searchPaymentBlockRequest.getFiAreaTo(),
                                    "L.FI_AREA",
                                    params));
                }
            }

            if (!Util.isEmpty(searchPaymentBlockRequest.getFiscalYear())) {
                sql.append(
                        SqlUtil.whereClause(
                                searchPaymentBlockRequest.getFiscalYear(), "H.ORIGINAL_FISCAL_YEAR", params));
            }

            if (!Util.isEmpty(searchPaymentBlockRequest.getDateAcctFrom())) {

                DynamicCondition.baseDateRangeConditionOM(searchPaymentBlockRequest.getListPostDate(), sql, params, "H.DATE_ACCT");
//                sql.append(
//                        SqlUtil.whereClauseRangeNotAddDay(
//                                searchPaymentBlockRequest.getDateAcctFrom(),
//                                searchPaymentBlockRequest.getDateAcctTo(),
//                                "H.DATE_ACCT",
//                                params));
            }


            if (!Util.isEmpty(searchPaymentBlockRequest.getDateDocFrom())) {

                DynamicCondition.baseDateRangeConditionOM(searchPaymentBlockRequest.getListDocumentDate(), sql, params, "H.DATE_DOC");

//                sql.append(
//                        SqlUtil.whereClauseRangeNotAddDay(
//                                searchPaymentBlockRequest.getDateDocFrom(),
//                                searchPaymentBlockRequest.getDateDocTo(),
//                                "H.DATE_DOC",
//                                params));
            }

            if (!Util.isEmpty(searchPaymentBlockRequest.getDateCreatedFrom())) {

                DynamicCondition.baseDateRangeConditionOM(searchPaymentBlockRequest.getListDocumentCreateDate(), sql, params, "H.DOCUMENT_CREATED");
//                sql.append(
//                        SqlUtil.whereClauseRangeNotAddDay(
//                                searchPaymentBlockRequest.getDateCreatedFrom(),
//                                searchPaymentBlockRequest.getDateCreatedTo(),
//                                "H.DOCUMENT_CREATED",
//                                params));
            }

            if (!Util.isEmpty(searchPaymentBlockRequest.getSpecialGlFrom())) {
                sql.append(
                        SqlUtil.whereClauseRange(
                                searchPaymentBlockRequest.getSpecialGlFrom(),
                                searchPaymentBlockRequest.getSpecialGlTo(),
                                "L.SPECIAL_GL",
                                params));
            }
            //

            List<BaseRange> listCompCode = new ArrayList<>();
            if(searchPaymentBlockRequest.getListCompanyCode().size()>0){
               for(CompanyCodeCondition companyCodeCondition:searchPaymentBlockRequest.getListCompanyCode()){

                   BaseRange baseRange = new BaseRange();
                   baseRange.setFrom(companyCodeCondition.getCompanyCodeFrom());
                   baseRange.setTo(companyCodeCondition.getCompanyCodeTo());
                   baseRange.setOptionExclude(companyCodeCondition.isOptionExclude());
                   listCompCode.add(baseRange);
               }
            }
//            DynamicCondition.companyConditionOM(
//                    searchPaymentBlockRequest.getListCompanyCode(), sql, params);
            DynamicCondition.baseRangeConditionOM(listCompCode, sql, params,"H.COMPANY_CODE");
//            DynamicCondition.docTypeConditionOM(searchPaymentBlockRequest.getListDocType(), sql, params);
//            DynamicCondition.paymentMethodConditionOM(
//                    searchPaymentBlockRequest.getListPaymentMethod(), sql, params);
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getListDocType(), sql, params, "H.DOCUMENT_TYPE");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getListPaymentMethod(), sql, params, "L.PAYMENT_METHOD");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getVendor(), sql, params, "L.VENDOR");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getPaymentCenter(), sql, params, "L.PAYMENT_CENTER");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getListDocumentNo(), sql, params, "H.ORIGINAL_DOCUMENT_NO");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getListHeaderReference(), sql, params, "H.HEADER_REFERENCE");

            sql.append("          ORDER BY H.DATE_DOC DESC, H.ORIGINAL_DOCUMENT_NO DESC           ");

            Object[] objParams = new Object[params.size()];
            params.toArray(objParams);

            log.info("sql : {} ", sql);
            log.info("params : {} ", params);
            log.info("objParams : {} ", objParams);
            this.jdbcTemplate.setFetchSize(5000);
            return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<UnBlockDocument> findUnBlockForCheckRelation(JwtBody jwt, List<UnBlockDocument> unBlockDocument, SearchPaymentBlockRequest searchPaymentBlockRequest) {
        try {
            List<Object> params = new ArrayList<>();
            StringBuilder sql = new StringBuilder();

            // tuning 2
//            // tuning
//            sql.append("          WITH BANK_ACCOUNT AS (SELECT *           ");
//            sql.append("          FROM ").append(schema).append(".C_BP_BANKACCOUNT           ");
//            sql.append("          WHERE ROWID in           ");
//            sql.append("          (SELECT MAX(ROWID)           ");
//            sql.append("          FROM ").append(schema).append(".C_BP_BANKACCOUNT BB           ");
//            sql.append("          WHERE BB.ISACTIVE = 'Y'           ");
//            sql.append("          GROUP BY BB.C_BPARTNER_ID, BB.ACCOUNTNO))           ");
            sql.append("          SELECT H.ID,           ");
            sql.append("          DECODE(L.INVOICE_DOCUMENT_NO, NULL, LC.ORIGINAL_DOCUMENT_NO, L.INVOICE_DOCUMENT_NO) AS LINE_INVOICE_DOCUMENT_NO,           ");
            sql.append("          DECODE(L.INVOICE_FISCAL_YEAR, NULL, LC.ORIGINAL_FISCAL_YEAR, L.INVOICE_FISCAL_YEAR) AS LINE_INVOICE_FISCAL_YEAR,           ");
            sql.append("          DECODE(HP.DOCUMENT_BASE_TYPE, NULL, HC.DOCUMENT_BASE_TYPE, HP.DOCUMENT_BASE_TYPE)   AS LINE_DOCUMENT_BASE_TYPE,           ");
            sql.append("          DECODE(L.COMPANY_CODE, NULL, LC.COMPANY_CODE, L.COMPANY_CODE)                     AS LINE_COMPANY_CODE,           ");
            sql.append("          NH.DOCUMENT_TYPE                     AS LINE_DOCUMENT_TYPE,           ");
            sql.append("          NL.PAYMENT_BLOCK                            AS LINE_PAYMENT_BLOCK,           ");
            // tuning 2
//            sql.append("          (SELECT COUNT(1)           ");
//            sql.append("          FROM GL_HEAD GH3           ");
//            sql.append("          WHERE GH3.HEADER_REFERENCE = H.HEADER_REFERENCE           ");
//            sql.append("          AND GH3.DOCUMENT_TYPE IN ('K3', 'KA', 'KB', 'KG', 'KC')           ");
//            sql.append("          AND H.HEADER_REFERENCE LIKE 'AA#%'           ");
//            sql.append("          GROUP BY GH3.HEADER_REFERENCE)                                                     AS COUNT_CHILD,           ");
            sql.append(" 0 AS COUNT_CHILD, ");
            sql.append("          H.PO_DOCUMENT_NO                                                                       PO_DOCUMENT_NO,           ");
            sql.append("          H.REVERSE_DATE_ACCT                                                                    REVERSE_DATE_ACCT,           ");
            sql.append("          H.REVERSE_REASON                                                                       REVERSE_REASON,           ");
            sql.append("          H.INVOICE_DOCUMENT_NO                                                                  INVOICE_DOCUMENT_NO,           ");
            sql.append("          H.REVERSE_INVOICE_DOCUMENT_NO                                                          REVERSE_INVOICE_DOCUMENT_NO,           ");
            sql.append("          H.HEADER_REFERENCE                                                                     HEADER_REFERENCE,           ");
            sql.append("          H.HEADER_REFERENCE2                                                                    HEADER_REFERENCE2,           ");
            sql.append("          H.DOCUMENT_HEADER_TEXT                                                                 DOCUMENT_HEADER_TEXT,           ");
            sql.append("          H.ORIGINAL_DOCUMENT_NO                                                                 ORIGINAL_DOCUMENT_NO,           ");
            sql.append("          H.ORIGINAL_FISCAL_YEAR                                                                 ORIGINAL_FISCAL_YEAR,           ");
            sql.append("          H.REVERSE_ORIGINAL_DOCUMENT_NO                                                         REVERSE_ORIGINAL_DOCUMENT_NO,           ");
            sql.append("          H.REVERSE_ORIGINAL_FISCAL_YEAR                                                         REVERSE_ORIGINAL_FISCAL_YEAR,           ");
            sql.append("          H.COST_CENTER1                                                                         COST_CENTER1,           ");
            sql.append("          H.COST_CENTER2                                                                         COST_CENTER2,           ");
            sql.append("          H.DOCUMENT_TYPE                                                                        DOCUMENT_TYPE,           ");
            sql.append("          H.COMPANY_CODE                                                                         COMPANY_CODE,           ");
            sql.append("          H.DATE_DOC                                                                             DATE_DOC,           ");
            sql.append("          H.DATE_ACCT                                                                            DATE_ACCT,           ");
            sql.append("          H.PERIOD                                                                               PERIOD,           ");
            sql.append("          H.CURRENCY                                                                             CURRENCY,           ");
            sql.append("          H.ORIGINAL_DOCUMENT                                                                    ORIGINAL_DOCUMENT,           ");
            sql.append("          H.DOCUMENT_CREATED                                                                     DOCUMENT_CREATED,           ");
            sql.append("          H.USER_PARK                                                                            USER_PARK,           ");
            sql.append("          H.USER_POST                                                                            USER_POST,           ");
            sql.append("          L.POSTING_KEY                                                                          POSTING_KEY,           ");
            sql.append("          L.ACCOUNT_TYPE                                                                         ACCOUNT_TYPE,           ");
            sql.append("          L.DR_CR                                                                                DR_CR,           ");
            sql.append("          L.GL_ACCOUNT                                                                           GL_ACCOUNT,           ");
            sql.append("          L.FI_AREA                                                                              FI_AREA,           ");
            sql.append("          L.COST_CENTER                                                                          COST_CENTER,           ");
            sql.append("          L.FUND_SOURCE                                                                          FUND_SOURCE,           ");
            sql.append("          L.BG_CODE                                                                              BG_CODE,           ");
            sql.append("          L.BG_ACTIVITY                                                                          BG_ACTIVITY,           ");
            sql.append("          L.COST_ACTIVITY                                                                        COST_ACTIVITY,           ");
            sql.append("          SUM_L.AMOUNT                                                                        AS AMOUNT,           ");
            sql.append("          L.REFERENCE3                                                                           REFERENCE3,           ");
            sql.append("          L.ASSIGNMENT                                                                           ASSIGNMENT,           ");
            sql.append("          H.BR_DOCUMENT_NO                                                                       BR_DOCUMENT_NO,           ");
            sql.append("          CNT_L.BR_LINE                                                                       AS BR_LINE,           ");
            sql.append("          L.PAYMENT_CENTER                                                                       PAYMENT_CENTER,           ");
            sql.append("          L.BANK_BOOK                                                                            BANK_BOOK,           ");
            sql.append("          L.BANK_BRANCH_NO                                                                       BANK_BRANCH_NO,           ");
            sql.append("          L.TRADING_PARTNER                                                                      TRADING_PARTNER,           ");
            sql.append("          L.TRADING_PARTNER_PARK                                                                 TRADING_PARTNER_PARK,           ");
            sql.append("          L.SPECIAL_GL                                                                           SPECIAL_GL,           ");
            sql.append("          L.DATE_BASE_LINE                                                                       DATE_BASE_LINE,           ");
            sql.append("          L.DATE_VALUE                                                                           DATE_VALUE,           ");
            sql.append("          L.ASSET_NO                                                                             ASSET_NO,           ");
            sql.append("          L.ASSET_SUB_NO                                                                         ASSET_SUB_NO,           ");
            sql.append("          L.QTY                                                                                  QTY,           ");
            sql.append("          L.UOM                                                                                  UOM,           ");
            sql.append("          L.INCOME                                                                               INCOME,           ");
            sql.append("          L.REFERENCE1                                                                           REFERENCE1,           ");
            sql.append("          L.REFERENCE2                                                                           REFERENCE2,           ");
            sql.append("          L.PO_LINE                                                                           AS PO_LINE,           ");
            sql.append("          L.PAYMENT_REFERENCE                                                                    PAYMENT_REFERENCE,           ");
            sql.append("          L.SUB_ACCOUNT                                                                          SUB_ACCOUNT,           ");
            sql.append("          L.SUB_ACCOUNT_OWNER                                                                    SUB_ACCOUNT_OWNER,           ");
            sql.append("          L.DEPOSIT_ACCOUNT                                                                      DEPOSIT_ACCOUNT,           ");
            sql.append("          L.DEPOSIT_ACCOUNT_OWNER                                                                DEPOSIT_ACCOUNT_OWNER,           ");
            sql.append("          L.GPSC                                                                                 GPSC,           ");
            sql.append("          L.GPSC_GROUP                                                                           GPSC_GROUP,           ");
            sql.append("          L.LINE_ITEM_TEXT                                                                       LINE_ITEM_TEXT,           ");
            sql.append("          L.LINE_DESC                                                                            LINE_DESC,           ");
            sql.append("          L.PAYMENT_TERM                                                                         PAYMENT_TERM,           ");
            sql.append("          L.PAYMENT_METHOD                                                                       PAYMENT_METHOD,           ");
            sql.append("          L.WTX_TYPE                                                                             WTX_TYPE,           ");
            sql.append("          L.WTX_CODE                                                                             WTX_CODE,           ");
            sql.append("          L.WTX_BASE                                                                             WTX_BASE,           ");
            sql.append("          L.WTX_AMOUNT                                                                           WTX_AMOUNT,           ");
            sql.append("          L.WTX_TYPE_P                                                                           WTX_TYPE_P,           ");
            sql.append("          L.WTX_CODE_P                                                                           WTX_CODE_P,           ");
            sql.append("          L.WTX_BASE_P                                                                           WTX_BASE_P,           ");
            sql.append("          L.WTX_AMOUNT_P                                                                         WTX_AMOUNT_P,           ");
            sql.append("          L.PAYEE                                                                                PAYEE,           ");
            sql.append("          L.PAYEE_TAX_ID                                                                         PAYEE_TAX_ID,           ");
            sql.append("          L.VENDOR                                                                               VENDOR,           ");
            // tuning 2
//            sql.append("          VD.NAME                                                                          VENDOR_NAME,           ");
            sql.append(" '' AS VENDOR_NAME, ");
            sql.append("          L.VENDOR_TAX_ID                                                                        VENDOR_TAX_ID,           ");
            sql.append("          L.BANK_ACCOUNT_NO                                                                      BANK_ACCOUNT_NO,           ");
            // tuning 2
            sql.append(" '' AS BANK_ACCOUNT_HOLDER_NAME, ");
//            sql.append("          VB.A_NAME                                                              BANK_ACCOUNT_HOLDER_NAME,           ");
            sql.append("          L.PAYMENT_BLOCK                                                                        PAYMENT_BLOCK,           ");
            // tuning 2
            sql.append(" '' AS CONFIRM_VENDOR, ");
//            sql.append("          AC.CONFIRMSTATUS                                                                       CONFIRM_VENDOR,           ");
            sql.append("          FT.VALUECODE                                                                            FUND_TYPE,           ");
            sql.append("          H.DOCUMENT_BASE_TYPE                                                                   DOCUMENT_BASE_TYPE,           ");
            sql.append("          H.SELECT_GROUP_DOCUMENT                                                                SELECT_GROUP_DOCUMENT,          ");
            sql.append("          NH.PAYMENT_ID,           ");
            sql.append("          PA.PAYMENT_NAME,           ");
            sql.append("          PA.PAYMENT_DATE,           ");
            sql.append("          PA.PROPOSAL_STATUS    AS PROPOSAL_STATUS,           ");
            sql.append("          PA.RUN_STATUS     AS RUN_STATUS        ");
            sql.append("          FROM GL_HEAD H           ");
            sql.append("          LEFT JOIN GL_LINE L ON H.ID = L.GL_HEAD_ID           ");
            sql.append("          LEFT JOIN (SELECT COUNT(1) AS BR_LINE, ORIGINAL_DOCUMENT_NO, COMPANY_CODE FROM GL_LINE GROUP BY ORIGINAL_DOCUMENT_NO, COMPANY_CODE) CNT_L           ");
            sql.append("          ON H.ORIGINAL_DOCUMENT_NO = CNT_L.ORIGINAL_DOCUMENT_NO AND H.COMPANY_CODE = CNT_L.COMPANY_CODE           ");
            sql.append("          LEFT JOIN (SELECT SUM(AMOUNT) AS AMOUNT, ORIGINAL_DOCUMENT_NO, ORIGINAL_FISCAL_YEAR, COMPANY_CODE           ");
            sql.append("          FROM GL_LINE           ");
            sql.append("          WHERE ACCOUNT_TYPE = 'K'           ");
            sql.append("          GROUP BY ORIGINAL_DOCUMENT_NO, ORIGINAL_FISCAL_YEAR, COMPANY_CODE) SUM_L           ");
            sql.append("          ON H.ORIGINAL_DOCUMENT_NO = SUM_L.ORIGINAL_DOCUMENT_NO AND           ");
            sql.append("          H.ORIGINAL_FISCAL_YEAR = SUM_L.ORIGINAL_FISCAL_YEAR AND           ");
            sql.append("          H.COMPANY_CODE = SUM_L.COMPANY_CODE           ");
            sql.append("          LEFT JOIN GL_LINE LC ON LC.INVOICE_DOCUMENT_NO = H.ORIGINAL_DOCUMENT_NO AND LC.ACCOUNT_TYPE = 'K' AND           ");
            sql.append("          LC.COMPANY_CODE = H.COMPANY_CODE AND LC.INVOICE_FISCAL_YEAR = H.ORIGINAL_FISCAL_YEAR           ");
            sql.append("          LEFT JOIN GL_HEAD HC ON LC.GL_HEAD_ID = HC.ID           ");
            sql.append("          LEFT JOIN GL_LINE LP ON LP.ORIGINAL_DOCUMENT_NO = HC.ORIGINAL_DOCUMENT_NO AND LP.ACCOUNT_TYPE = 'K' AND           ");
            sql.append("          LP.COMPANY_CODE = L.COMPANY_CODE AND LP.INVOICE_FISCAL_YEAR = H.ORIGINAL_FISCAL_YEAR           ");
            sql.append("          LEFT JOIN GL_HEAD HP ON LP.GL_HEAD_ID = HP.ID           ");
            sql.append("          LEFT JOIN ").append(schema).append(".C_YEAR cy ON L.ORIGINAL_FISCAL_YEAR = cy.FISCALYEAR           ");
            sql.append("          LEFT JOIN ").append(schema).append(".TH_BGFUNDSOURCE fs ON L.FUND_SOURCE = fs.VALUECODE AND cy.C_YEAR_ID = fs.C_YEAR_ID           ");
            sql.append("          LEFT JOIN ").append(schema).append(".TH_BGFUNDTYPE ft ON FS.TH_BGFUNDTYPE_ID = FT.TH_BGFUNDTYPE_ID           ");
            // tuning 2
//            sql.append("    LEFT  JOIN ").append(schema).append(".C_BPARTNER").append(" VD ON VD.VALUE = DECODE(L.PAYEE,null,L.VENDOR,L.PAYEE) ");
//            sql.append("    LEFT  JOIN ").append(schema).append(".TH_APBPARTNERSTATUS").append(" AC ON VD.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
//            // tuning
//            sql.append("    LEFT JOIN BANK_ACCOUNT VB ON VD.C_BPARTNER_ID = VB.C_BPARTNER_ID AND L.BANK_ACCOUNT_NO = VB.ACCOUNTNO ");
//            sql.append("          OUTER APPLY (           ");
//            sql.append("          SELECT *           ");
//
//            sql.append("    FROM ").append(schema).append(".C_BP_BANKACCOUNT BB");
//            sql.append("          WHERE BB.ISACTIVE = 'Y'           ");
//            sql.append("          AND vd.C_BPARTNER_ID = BB.C_BPARTNER_ID           ");
//            sql.append("          AND L.BANK_ACCOUNT_NO = BB.ACCOUNTNO           ");
//            sql.append("          ORDER BY BB.ACCOUNTNO           ");
//            sql.append("          FETCH FIRST ROW ONLY           ");
//            sql.append("          ) vb           ");
            sql.append("          LEFT JOIN GL_HEAD NH           ");
            sql.append("          ON NH.ORIGINAL_DOCUMENT_NO =           ");
            sql.append("          DECODE(L.INVOICE_DOCUMENT_NO, NULL, LC.ORIGINAL_DOCUMENT_NO, L.INVOICE_DOCUMENT_NO)           ");
            sql.append("          AND NH.ORIGINAL_FISCAL_YEAR =           ");
            sql.append("          DECODE(L.INVOICE_FISCAL_YEAR, NULL, LC.ORIGINAL_FISCAL_YEAR, L.INVOICE_FISCAL_YEAR)           ");
            sql.append("          AND NH.COMPANY_CODE = DECODE(L.COMPANY_CODE, NULL, LC.COMPANY_CODE, L.COMPANY_CODE)           ");
            sql.append("          LEFT JOIN GL_LINE NL ON NH.ID = NL.GL_HEAD_ID AND NL.ACCOUNT_TYPE = 'K'           ");
            sql.append("          LEFT JOIN PAYMENT_ALIAS PA ON PA.ID = NH.PAYMENT_ID           ");
            sql.append("          WHERE H.ORIGINAL_DOCUMENT_NO = L.ORIGINAL_DOCUMENT_NO           ");
            sql.append("          AND H.ORIGINAL_FISCAL_YEAR = L.ORIGINAL_FISCAL_YEAR           ");
            sql.append("          AND H.COMPANY_CODE = L.COMPANY_CODE           ");
            sql.append("          AND H.REVERSE_ORIGINAL_DOCUMENT_NO IS NULL           ");
            sql.append("          AND H.DOCUMENT_BASE_TYPE <> 'KY'           ");
            sql.append("          AND H.COMPANY_CODE <> '99999'           ");
            sql.append("          AND L.ACCOUNT_TYPE = 'K'           ");
            sql.append("          AND NH.REVERSE_ORIGINAL_DOCUMENT_NO IS NULL               ");
            sql.append(generateSQLWhereAuthorized(jwt, params));
            sql.append("  AND (H.COMPANY_CODE, H.ORIGINAL_DOCUMENT_NO, H.ORIGINAL_FISCAL_YEAR) IN (   ");
            sql.append("  SELECT H.COMPANY_CODE, H.ORIGINAL_DOCUMENT_NO, H.ORIGINAL_FISCAL_YEAR ");
            sql.append("          FROM GL_HEAD H           ");
            sql.append("          LEFT JOIN GL_LINE L ON H.ID = L.GL_HEAD_ID           ");
            // tuning 2
//            sql.append("          LEFT JOIN (SELECT COUNT(1) AS BR_LINE, ORIGINAL_DOCUMENT_NO, COMPANY_CODE FROM GL_LINE GROUP BY ORIGINAL_DOCUMENT_NO, COMPANY_CODE) CNT_L           ");
//            sql.append("          ON H.ORIGINAL_DOCUMENT_NO = CNT_L.ORIGINAL_DOCUMENT_NO AND H.COMPANY_CODE = CNT_L.COMPANY_CODE           ");
//            sql.append("          LEFT JOIN (SELECT SUM(AMOUNT) AS AMOUNT, ORIGINAL_DOCUMENT_NO, ORIGINAL_FISCAL_YEAR, COMPANY_CODE           ");
//            sql.append("          FROM GL_LINE           ");
//            sql.append("          WHERE ACCOUNT_TYPE = 'K'           ");
//            sql.append("          GROUP BY ORIGINAL_DOCUMENT_NO, ORIGINAL_FISCAL_YEAR, COMPANY_CODE) SUM_L           ");
//            sql.append("          ON H.ORIGINAL_DOCUMENT_NO = SUM_L.ORIGINAL_DOCUMENT_NO AND           ");
//            sql.append("          H.ORIGINAL_FISCAL_YEAR = SUM_L.ORIGINAL_FISCAL_YEAR AND           ");
//            sql.append("          H.COMPANY_CODE = SUM_L.COMPANY_CODE           ");
//            sql.append("    LEFT  JOIN ").append(schema).append(".C_BPARTNER").append(" VD ON VD.VALUE = DECODE(L.PAYEE,null,L.VENDOR,L.PAYEE) ");
//            sql.append("    LEFT  JOIN ").append(schema).append(".TH_APBPARTNERSTATUS").append(" AC ON VD.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
            sql.append("          WHERE H.ORIGINAL_DOCUMENT_NO = L.ORIGINAL_DOCUMENT_NO           ");
            sql.append("          AND H.ORIGINAL_FISCAL_YEAR = L.ORIGINAL_FISCAL_YEAR           ");
            sql.append("          AND H.COMPANY_CODE = L.COMPANY_CODE           ");
            sql.append("          AND H.REVERSE_ORIGINAL_DOCUMENT_NO IS NULL           ");
            sql.append("          AND H.DOCUMENT_BASE_TYPE <> 'KY'           ");
            sql.append("          AND H.COMPANY_CODE <> '99999'           ");
            sql.append("          AND L.PAYMENT_BLOCK = 'B' ");
            sql.append("          AND L.ACCOUNT_TYPE = 'K'           ");

            sql.append(generateSQLWhereAuthorized(jwt, params));

            if (searchPaymentBlockRequest.getListFiArea() != null && searchPaymentBlockRequest.getListFiArea().size() > 0) {
                DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getListFiArea(), sql, params, "L.FI_AREA");
            }else{
                if (!Util.isEmpty(searchPaymentBlockRequest.getFiAreaFrom())) {
                    sql.append(
                            SqlUtil.whereClauseRange(
                                    searchPaymentBlockRequest.getFiAreaFrom(),
                                    searchPaymentBlockRequest.getFiAreaTo(),
                                    "L.FI_AREA",
                                    params));
                }
            }

            if (!Util.isEmpty(searchPaymentBlockRequest.getFiscalYear())) {
                sql.append(
                        SqlUtil.whereClause(
                                searchPaymentBlockRequest.getFiscalYear(), "H.ORIGINAL_FISCAL_YEAR", params));
            }

            if (!Util.isEmpty(searchPaymentBlockRequest.getDateAcctFrom())) {
                DynamicCondition.baseDateRangeConditionOM(searchPaymentBlockRequest.getListPostDate(), sql, params, "H.DATE_ACCT");
            }


            if (!Util.isEmpty(searchPaymentBlockRequest.getDateDocFrom())) {
                DynamicCondition.baseDateRangeConditionOM(searchPaymentBlockRequest.getListDocumentDate(), sql, params, "H.DATE_DOC");
            }

            if (!Util.isEmpty(searchPaymentBlockRequest.getDateCreatedFrom())) {
                DynamicCondition.baseDateRangeConditionOM(searchPaymentBlockRequest.getListDocumentCreateDate(), sql, params, "H.DOCUMENT_CREATED");
            }
            if (!Util.isEmpty(searchPaymentBlockRequest.getSpecialGlFrom())) {
                sql.append(
                        SqlUtil.whereClauseRange(
                                searchPaymentBlockRequest.getSpecialGlFrom(),
                                searchPaymentBlockRequest.getSpecialGlTo(),
                                "L.SPECIAL_GL",
                                params));
            }
            List<BaseRange> listCompCode = new ArrayList<>();
            if(searchPaymentBlockRequest.getListCompanyCode().size()>0){
                for(CompanyCodeCondition companyCodeCondition:searchPaymentBlockRequest.getListCompanyCode()){

                    BaseRange baseRange = new BaseRange();
                    baseRange.setFrom(companyCodeCondition.getCompanyCodeFrom());
                    baseRange.setTo(companyCodeCondition.getCompanyCodeTo());
                    baseRange.setOptionExclude(companyCodeCondition.isOptionExclude());
                    listCompCode.add(baseRange);
                }
            }
            DynamicCondition.baseRangeConditionOM(listCompCode, sql, params,"H.COMPANY_CODE");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getListDocType(), sql, params, "H.DOCUMENT_TYPE");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getListPaymentMethod(), sql, params, "L.PAYMENT_METHOD");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getVendor(), sql, params, "L.VENDOR");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getPaymentCenter(), sql, params, "L.PAYMENT_CENTER");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getListDocumentNo(), sql, params, "H.ORIGINAL_DOCUMENT_NO");
            DynamicCondition.baseRangeConditionOM(searchPaymentBlockRequest.getListHeaderReference(), sql, params, "H.HEADER_REFERENCE");
            sql.append(" ) ");
            Object[] objParams = new Object[params.size()];
            params.toArray(objParams);

            log.info("sql checkRelation : {} ", sql);
            log.info("params : {} ", params);
            log.info("objParams : {} ", objParams);
            this.jdbcTemplate.setFetchSize(5000);
            return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<UnBlockDocument> findUnBlockParentByCondition(
            List<PaymentBlockListDocumentRequest> request) {
        try {
            List<Object> params = new ArrayList<>();
            //          params.add(companyCode);
            //          params.add(originalDocumentNo);
            //          params.add(originalFiscalYear);

            StringBuilder sql = new StringBuilder();
            List<PaymentBlockListDocumentRequest> listNotK3Kx = request.stream()
                    .filter(p -> (!"K3".equalsIgnoreCase(p.getDocumentType()) && !"KX".equalsIgnoreCase(p.getDocumentType()))).collect(toList());
            log.info("listNotK3Kx {} ", listNotK3Kx.size());

            List<PaymentBlockListDocumentRequest> listK3Kx = request.stream()
                    .filter(p -> ("K3".equalsIgnoreCase(p.getDocumentType()) || "KX".equalsIgnoreCase(p.getDocumentType())
                    )).collect(toList());
            log.info("listK3Kx {} ", listK3Kx.size());

            if (listNotK3Kx.size() > 0) {
                sql.append("          SELECT H.ID                           ID,           ");
                sql.append("          L.INVOICE_DOCUMENT_NO AS       LINE_INVOICE_DOCUMENT_NO,           ");
                sql.append("          L.INVOICE_FISCAL_YEAR AS       LINE_INVOICE_FISCAL_YEAR,           ");
                sql.append("          ''                                                               AS LINE_DOCUMENT_BASE_TYPE,           ");
                sql.append("          ''                                                               AS LINE_COMPANY_CODE,           ");
                sql.append("          ''                                                                    AS LINE_DOCUMENT_TYPE,           ");
                sql.append("          H.PO_DOCUMENT_NO               PO_DOCUMENT_NO,           ");
                sql.append("          H.REVERSE_DATE_ACCT            REVERSE_DATE_ACCT,           ");
                sql.append("          H.REVERSE_REASON               REVERSE_REASON,           ");
                sql.append("          H.INVOICE_DOCUMENT_NO          INVOICE_DOCUMENT_NO,           ");
                sql.append(
                        "          H.REVERSE_INVOICE_DOCUMENT_NO  REVERSE_INVOICE_DOCUMENT_NO,           ");
                sql.append("          H.HEADER_REFERENCE             HEADER_REFERENCE,           ");
                sql.append("          H.HEADER_REFERENCE2            HEADER_REFERENCE2,           ");
                sql.append("          H.DOCUMENT_HEADER_TEXT         DOCUMENT_HEADER_TEXT,           ");
                sql.append("          H.ORIGINAL_DOCUMENT_NO         ORIGINAL_DOCUMENT_NO,           ");
                sql.append("          H.ORIGINAL_FISCAL_YEAR         ORIGINAL_FISCAL_YEAR,           ");
                sql.append(
                        "          H.REVERSE_ORIGINAL_DOCUMENT_NO REVERSE_ORIGINAL_DOCUMENT_NO,           ");
                sql.append(
                        "          H.REVERSE_ORIGINAL_FISCAL_YEAR REVERSE_ORIGINAL_FISCAL_YEAR,           ");
                sql.append("          H.COST_CENTER1                 COST_CENTER1,           ");
                sql.append("          H.COST_CENTER2                 COST_CENTER2,           ");
                sql.append("          H.DOCUMENT_TYPE                DOCUMENT_TYPE,           ");
                sql.append("          H.COMPANY_CODE                 COMPANY_CODE,           ");
                sql.append("          H.DATE_DOC                     DATE_DOC,           ");
                sql.append("          H.DATE_ACCT                    DATE_ACCT,           ");
                sql.append("          H.PERIOD                       PERIOD,           ");
                sql.append("          H.CURRENCY                     CURRENCY,           ");
                sql.append("          H.ORIGINAL_DOCUMENT            ORIGINAL_DOCUMENT,           ");
                sql.append("          H.DOCUMENT_CREATED             DOCUMENT_CREATED,           ");
                sql.append("          H.USER_PARK                    USER_PARK,           ");
                sql.append("          H.USER_POST                    USER_POST,           ");
                sql.append("          L.POSTING_KEY                  POSTING_KEY,           ");
                sql.append("          L.ACCOUNT_TYPE                 ACCOUNT_TYPE,           ");
                sql.append("          L.DR_CR                        DR_CR,           ");
                sql.append("          L.GL_ACCOUNT                   GL_ACCOUNT,           ");
                sql.append("          L.FI_AREA                      FI_AREA,           ");
                sql.append("          L.COST_CENTER                  COST_CENTER,           ");
                sql.append("          L.FUND_SOURCE                  FUND_SOURCE,           ");
                sql.append("          L.BG_CODE                      BG_CODE,           ");
                sql.append("          L.BG_ACTIVITY                  BG_ACTIVITY,           ");
                sql.append("          L.COST_ACTIVITY                COST_ACTIVITY,           ");
                sql.append("          SUM_L.AMOUNT          AS       AMOUNT,           ");
                sql.append("          L.REFERENCE3                   REFERENCE3,           ");
                sql.append("          L.ASSIGNMENT                   ASSIGNMENT,           ");
                sql.append("          H.BR_DOCUMENT_NO               BR_DOCUMENT_NO,           ");
                sql.append("          CNT_L.BR_LINE         AS       BR_LINE,           ");
                sql.append("          L.PAYMENT_CENTER               PAYMENT_CENTER,           ");
                sql.append("          L.BANK_BOOK                    BANK_BOOK,           ");
                sql.append("          L.BANK_BRANCH_NO               BANK_BRANCH_NO,           ");
                sql.append("          L.TRADING_PARTNER              TRADING_PARTNER,           ");
                sql.append("          L.TRADING_PARTNER_PARK         TRADING_PARTNER_PARK,           ");
                sql.append("          L.SPECIAL_GL                   SPECIAL_GL,           ");
                sql.append("          L.DATE_BASE_LINE               DATE_BASE_LINE,           ");
                sql.append("          L.DATE_VALUE                   DATE_VALUE,           ");
                sql.append("          L.ASSET_NO                     ASSET_NO,           ");
                sql.append("          L.ASSET_SUB_NO                 ASSET_SUB_NO,           ");
                sql.append("          L.QTY                          QTY,           ");
                sql.append("          L.UOM                          UOM,           ");
                sql.append("          L.INCOME                       INCOME,           ");
                sql.append("          L.REFERENCE1                   REFERENCE1,           ");
                sql.append("          L.REFERENCE2                   REFERENCE2,           ");
                sql.append("          L.PO_LINE             AS       PO_LINE,           ");
                sql.append("          L.PAYMENT_REFERENCE            PAYMENT_REFERENCE,           ");
                sql.append("          L.SUB_ACCOUNT                  SUB_ACCOUNT,           ");
                sql.append("          L.SUB_ACCOUNT_OWNER            SUB_ACCOUNT_OWNER,           ");
                sql.append("          L.DEPOSIT_ACCOUNT              DEPOSIT_ACCOUNT,           ");
                sql.append("          L.DEPOSIT_ACCOUNT_OWNER        DEPOSIT_ACCOUNT_OWNER,           ");
                sql.append("          L.GPSC                         GPSC,           ");
                sql.append("          L.GPSC_GROUP                   GPSC_GROUP,           ");
                sql.append("          L.LINE_ITEM_TEXT               LINE_ITEM_TEXT,           ");
                sql.append("          L.LINE_DESC                    LINE_DESC,           ");
                sql.append("          L.PAYMENT_TERM                 PAYMENT_TERM,           ");
                sql.append("          L.PAYMENT_METHOD               PAYMENT_METHOD,           ");
                sql.append("          L.WTX_TYPE                     WTX_TYPE,           ");
                sql.append("          L.WTX_CODE                     WTX_CODE,           ");
                sql.append("          L.WTX_BASE                     WTX_BASE,           ");
                sql.append("          L.WTX_AMOUNT                   WTX_AMOUNT,           ");
                sql.append("          L.WTX_TYPE_P                   WTX_TYPE_P,           ");
                sql.append("          L.WTX_CODE_P                   WTX_CODE_P,           ");
                sql.append("          L.WTX_BASE_P                   WTX_BASE_P,           ");
                sql.append("          L.WTX_AMOUNT_P                 WTX_AMOUNT_P,           ");
                sql.append("          L.PAYEE                        PAYEE,           ");
                sql.append("          L.PAYEE_TAX_ID                 PAYEE_TAX_ID,           ");
                sql.append("          L.VENDOR                       VENDOR,           ");
                sql.append("          L.VENDOR_NAME                  VENDOR_NAME,           ");
                sql.append("          L.VENDOR_TAX_ID                VENDOR_TAX_ID,           ");
                sql.append("          L.BANK_ACCOUNT_NO              BANK_ACCOUNT_NO,           ");
                sql.append("          L.BANK_ACCOUNT_HOLDER_NAME     BANK_ACCOUNT_HOLDER_NAME,           ");
                sql.append("          L.PAYMENT_BLOCK                PAYMENT_BLOCK,           ");
                sql.append("          L.CONFIRM_VENDOR               CONFIRM_VENDOR,           ");
                sql.append("          L.FUND_TYPE                    FUND_TYPE,           ");
                sql.append("          H.DOCUMENT_BASE_TYPE           DOCUMENT_BASE_TYPE,           ");
                sql.append("          H.SELECT_GROUP_DOCUMENT           SELECT_GROUP_DOCUMENT           ");
                sql.append("          FROM GL_HEAD H           ");
                sql.append("          LEFT JOIN GL_LINE L           ");
                sql.append("          ON H.ORIGINAL_DOCUMENT_NO = L.ORIGINAL_DOCUMENT_NO AND           ");
                sql.append(
                        "          H.ORIGINAL_FISCAL_YEAR = L.ORIGINAL_FISCAL_YEAR AND H.COMPANY_CODE = L.COMPANY_CODE           ");
                sql.append(
                        "          LEFT JOIN (SELECT COUNT(1) AS BR_LINE, ORIGINAL_DOCUMENT_NO FROM GL_LINE GROUP BY ORIGINAL_DOCUMENT_NO) CNT_L           ");
                sql.append("          ON H.ORIGINAL_DOCUMENT_NO = CNT_L.ORIGINAL_DOCUMENT_NO           ");
                sql.append(
                        "          LEFT JOIN (SELECT SUM(AMOUNT) AS AMOUNT, ORIGINAL_DOCUMENT_NO           ");
                sql.append("          FROM GL_LINE           ");
                sql.append("          WHERE ACCOUNT_TYPE = 'K'           ");
                sql.append(
                        "          GROUP BY ORIGINAL_DOCUMENT_NO) SUM_L ON H.ORIGINAL_DOCUMENT_NO = SUM_L.ORIGINAL_DOCUMENT_NO           ");
                sql.append("          WHERE H.REVERSE_ORIGINAL_DOCUMENT_NO IS NULL           ");
                sql.append("          AND H.DOCUMENT_BASE_TYPE NOT IN ('KY')           ");
                sql.append("          AND H.COMPANY_CODE NOT IN ('99999')           ");
                sql.append("          AND L.PAYMENT_BLOCK = 'B'           ");
                sql.append("          AND L.ACCOUNT_TYPE = 'K'           ");


                log.info(" before parent {} ", sql.toString());

                sql.append("   AND H.DOCUMENT_BASE_TYPE = 'APC'           ");
                DynamicCondition.findChildCondition(listNotK3Kx, sql, params);
            }
            if (listNotK3Kx.size() > 0 && listK3Kx.size() > 0) {
                sql.append("   UNION           ");
            }


            if (listK3Kx.size() > 0) {
                sql.append("          SELECT           ");
                sql.append("          GH2.ID                           ID,           ");
                sql.append("          H.ORIGINAL_DOCUMENT_NO AS       LINE_INVOICE_DOCUMENT_NO,           ");
                sql.append("          H.ORIGINAL_FISCAL_YEAR AS       LINE_INVOICE_FISCAL_YEAR,           ");
                sql.append("          ''                                                               AS LINE_DOCUMENT_BASE_TYPE,           ");
                sql.append("          ''                                                               AS LINE_COMPANY_CODE,           ");
                sql.append("          ''                                                                    AS LINE_DOCUMENT_TYPE,           ");
                sql.append("          GH2.PO_DOCUMENT_NO               PO_DOCUMENT_NO,           ");
                sql.append("          GH2.REVERSE_DATE_ACCT            REVERSE_DATE_ACCT,           ");
                sql.append("          GH2.REVERSE_REASON               REVERSE_REASON,           ");
                sql.append("          GH2.INVOICE_DOCUMENT_NO          INVOICE_DOCUMENT_NO,           ");
                sql.append("          GH2.REVERSE_INVOICE_DOCUMENT_NO  REVERSE_INVOICE_DOCUMENT_NO,           ");
                sql.append("          GH2.HEADER_REFERENCE             HEADER_REFERENCE,           ");
                sql.append("          GH2.HEADER_REFERENCE2            HEADER_REFERENCE2,           ");
                sql.append("          GH2.DOCUMENT_HEADER_TEXT         DOCUMENT_HEADER_TEXT,           ");
                sql.append("          GH2.ORIGINAL_DOCUMENT_NO         ORIGINAL_DOCUMENT_NO,           ");
                sql.append("          GH2.ORIGINAL_FISCAL_YEAR         ORIGINAL_FISCAL_YEAR,           ");
                sql.append("          GH2.REVERSE_ORIGINAL_DOCUMENT_NO REVERSE_ORIGINAL_DOCUMENT_NO,           ");
                sql.append("          GH2.REVERSE_ORIGINAL_FISCAL_YEAR REVERSE_ORIGINAL_FISCAL_YEAR,           ");
                sql.append("          GH2.COST_CENTER1                 COST_CENTER1,           ");
                sql.append("          GH2.COST_CENTER2                 COST_CENTER2,           ");
                sql.append("          GH2.DOCUMENT_TYPE                DOCUMENT_TYPE,           ");
                sql.append("          GH2.COMPANY_CODE                 COMPANY_CODE,           ");
                sql.append("          GH2.DATE_DOC                     DATE_DOC,           ");
                sql.append("          GH2.DATE_ACCT                    DATE_ACCT,           ");
                sql.append("          GH2.PERIOD                       PERIOD,           ");
                sql.append("          GH2.CURRENCY                     CURRENCY,           ");
                sql.append("          GH2.ORIGINAL_DOCUMENT            ORIGINAL_DOCUMENT,           ");
                sql.append("          GH2.DOCUMENT_CREATED             DOCUMENT_CREATED,           ");
                sql.append("          GH2.USER_PARK                    USER_PARK,           ");
                sql.append("          GH2.USER_POST                    USER_POST,           ");
                sql.append("          GL2.POSTING_KEY                  POSTING_KEY,           ");
                sql.append("          GL2.ACCOUNT_TYPE                 ACCOUNT_TYPE,           ");
                sql.append("          GL2.DR_CR                        DR_CR,           ");
                sql.append("          GL2.GL_ACCOUNT                   GL_ACCOUNT,           ");
                sql.append("          GL2.FI_AREA                      FI_AREA,           ");
                sql.append("          GL2.COST_CENTER                  COST_CENTER,           ");
                sql.append("          GL2.FUND_SOURCE                  FUND_SOURCE,           ");
                sql.append("          GL2.BG_CODE                      BG_CODE,           ");
                sql.append("          GL2.BG_ACTIVITY                  BG_ACTIVITY,           ");
                sql.append("          GL2.COST_ACTIVITY                COST_ACTIVITY,           ");
                sql.append("          SUM_L2.AMOUNT           AS       AMOUNT,           ");
                sql.append("          GL2.REFERENCE3                   REFERENCE3,           ");
                sql.append("          GL2.ASSIGNMENT                   ASSIGNMENT,           ");
                sql.append("          GH2.BR_DOCUMENT_NO               BR_DOCUMENT_NO,           ");
                sql.append("          CNT_L2.BR_LINE          AS       BR_LINE,           ");
                sql.append("          GL2.PAYMENT_CENTER               PAYMENT_CENTER,           ");
                sql.append("          GL2.BANK_BOOK                    BANK_BOOK,           ");
                sql.append("          GL2.BANK_BRANCH_NO               BANK_BRANCH_NO,           ");
                sql.append("          GL2.TRADING_PARTNER              TRADING_PARTNER,           ");
                sql.append("          GL2.TRADING_PARTNER_PARK         TRADING_PARTNER_PARK,           ");
                sql.append("          GL2.SPECIAL_GL                   SPECIAL_GL,           ");
                sql.append("          GL2.DATE_BASE_LINE               DATE_BASE_LINE,           ");
                sql.append("          GL2.DATE_VALUE                   DATE_VALUE,           ");
                sql.append("          GL2.ASSET_NO                     ASSET_NO,           ");
                sql.append("          GL2.ASSET_SUB_NO                 ASSET_SUB_NO,           ");
                sql.append("          GL2.QTY                          QTY,           ");
                sql.append("          GL2.UOM                          UOM,           ");
                sql.append("          GL2.INCOME                       INCOME,           ");
                sql.append("          GL2.REFERENCE1                   REFERENCE1,           ");
                sql.append("          GL2.REFERENCE2                   REFERENCE2,           ");
                sql.append("          GL2.PO_LINE             AS       PO_LINE,           ");
                sql.append("          GL2.PAYMENT_REFERENCE            PAYMENT_REFERENCE,           ");
                sql.append("          GL2.SUB_ACCOUNT                  SUB_ACCOUNT,           ");
                sql.append("          GL2.SUB_ACCOUNT_OWNER            SUB_ACCOUNT_OWNER,           ");
                sql.append("          GL2.DEPOSIT_ACCOUNT              DEPOSIT_ACCOUNT,           ");
                sql.append("          GL2.DEPOSIT_ACCOUNT_OWNER        DEPOSIT_ACCOUNT_OWNER,           ");
                sql.append("          GL2.GPSC                         GPSC,           ");
                sql.append("          GL2.GPSC_GROUP                   GPSC_GROUP,           ");
                sql.append("          GL2.LINE_ITEM_TEXT               LINE_ITEM_TEXT,           ");
                sql.append("          GL2.LINE_DESC                    LINE_DESC,           ");
                sql.append("          GL2.PAYMENT_TERM                 PAYMENT_TERM,           ");
                sql.append("          GL2.PAYMENT_METHOD               PAYMENT_METHOD,           ");
                sql.append("          GL2.WTX_TYPE                     WTX_TYPE,           ");
                sql.append("          GL2.WTX_CODE                     WTX_CODE,           ");
                sql.append("          GL2.WTX_BASE                     WTX_BASE,           ");
                sql.append("          GL2.WTX_AMOUNT                   WTX_AMOUNT,           ");
                sql.append("          GL2.WTX_TYPE_P                   WTX_TYPE_P,           ");
                sql.append("          GL2.WTX_CODE_P                   WTX_CODE_P,           ");
                sql.append("          GL2.WTX_BASE_P                   WTX_BASE_P,           ");
                sql.append("          GL2.WTX_AMOUNT_P                 WTX_AMOUNT_P,           ");
                sql.append("          GL2.PAYEE                        PAYEE,           ");
                sql.append("          GL2.PAYEE_TAX_ID                 PAYEE_TAX_ID,           ");
                sql.append("          GL2.VENDOR                       VENDOR,           ");
                sql.append("          GL2.VENDOR_NAME                  VENDOR_NAME,           ");
                sql.append("          GL2.VENDOR_TAX_ID                VENDOR_TAX_ID,           ");
                sql.append("          GL2.BANK_ACCOUNT_NO              BANK_ACCOUNT_NO,           ");
                sql.append("          GL2.BANK_ACCOUNT_HOLDER_NAME     BANK_ACCOUNT_HOLDER_NAME,           ");
                sql.append("          GL2.PAYMENT_BLOCK                PAYMENT_BLOCK,           ");
                sql.append("          GL2.CONFIRM_VENDOR               CONFIRM_VENDOR,           ");
                sql.append("          GL2.FUND_TYPE                    FUND_TYPE,           ");
                sql.append("          GH2.DOCUMENT_BASE_TYPE           DOCUMENT_BASE_TYPE,           ");
                sql.append("          GH2.SELECT_GROUP_DOCUMENT        SELECT_GROUP_DOCUMENT           ");
                sql.append("          FROM GL_HEAD H           ");
                sql.append("          LEFT JOIN GL_LINE L ON H.ORIGINAL_DOCUMENT_NO = L.ORIGINAL_DOCUMENT_NO AND           ");
                sql.append("          H.ORIGINAL_FISCAL_YEAR = L.ORIGINAL_FISCAL_YEAR AND           ");
                sql.append("          H.COMPANY_CODE = L.COMPANY_CODE           ");
                sql.append("          LEFT JOIN (SELECT COUNT(1) AS BR_LINE, ORIGINAL_DOCUMENT_NO           ");
                sql.append("          FROM GL_LINE           ");
                sql.append("          GROUP BY ORIGINAL_DOCUMENT_NO) CNT_L           ");
                sql.append("          ON H.ORIGINAL_DOCUMENT_NO = CNT_L.ORIGINAL_DOCUMENT_NO           ");
                sql.append("          LEFT JOIN (SELECT SUM(AMOUNT) AS AMOUNT, ORIGINAL_DOCUMENT_NO           ");
                sql.append("          FROM GL_LINE           ");
                sql.append("          WHERE ACCOUNT_TYPE = 'K'           ");
                sql.append("          GROUP BY ORIGINAL_DOCUMENT_NO) SUM_L ON H.ORIGINAL_DOCUMENT_NO = SUM_L.ORIGINAL_DOCUMENT_NO           ");
                sql.append("          LEFT JOIN GL_HEAD GH2 ON GH2.ORIGINAL_DOCUMENT_NO = L.INVOICE_DOCUMENT_NO           ");
                sql.append("          AND GH2.COMPANY_CODE = H.COMPANY_CODE           ");
                sql.append("          LEFT JOIN GL_LINE GL2 ON GH2.ORIGINAL_DOCUMENT_NO = GL2.ORIGINAL_DOCUMENT_NO AND           ");
                sql.append("          GH2.ORIGINAL_FISCAL_YEAR = GL2.ORIGINAL_FISCAL_YEAR AND           ");
                sql.append("          GH2.COMPANY_CODE = GL2.COMPANY_CODE           ");
                sql.append("          LEFT JOIN (SELECT COUNT(1) AS BR_LINE, ORIGINAL_DOCUMENT_NO           ");
                sql.append("          FROM GL_LINE           ");
                sql.append("          GROUP BY ORIGINAL_DOCUMENT_NO) CNT_L2           ");
                sql.append("          ON GH2.ORIGINAL_DOCUMENT_NO = CNT_L2.ORIGINAL_DOCUMENT_NO           ");
                sql.append("          LEFT JOIN (SELECT SUM(AMOUNT) AS AMOUNT, ORIGINAL_DOCUMENT_NO           ");
                sql.append("          FROM GL_LINE           ");
                sql.append("          WHERE ACCOUNT_TYPE = 'K'           ");
                sql.append("          GROUP BY ORIGINAL_DOCUMENT_NO) SUM_L2 ON GH2.ORIGINAL_DOCUMENT_NO = SUM_L2.ORIGINAL_DOCUMENT_NO           ");
                sql.append("          WHERE H.REVERSE_ORIGINAL_DOCUMENT_NO IS NULL           ");
                sql.append("          AND H.DOCUMENT_BASE_TYPE NOT IN ('KY')           ");
                sql.append("          AND H.COMPANY_CODE NOT IN ('99999')           ");
                sql.append("          AND L.PAYMENT_BLOCK = 'B'           ");
                sql.append("          AND L.ACCOUNT_TYPE = 'K'           ");
                sql.append("          AND GL2.ACCOUNT_TYPE = 'K'           ");
                sql.append("          AND GH2.DOCUMENT_TYPE NOT IN ('K3','KX')            ");

                DynamicCondition.findParentCondition(listK3Kx, sql, params);
            }

//            sql.append("          ORDER BY H.DATE_DOC DESC, H.ORIGINAL_DOCUMENT_NO DESC           ");

            log.info(" after find parent {} ", sql.toString());

            Object[] objParams = new Object[params.size()];
            params.toArray(objParams);
            return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<UnBlockDocument> previewSelectGroupDocument(SelectGroupDocument selectGroupDocument) {
        try {
            List<Object> params = new ArrayList<>();
            StringBuilder sql = new StringBuilder();
            sql.append("          SELECT H.ID                           ID,           ");
            sql.append("          L.INVOICE_DOCUMENT_NO AS       LINE_INVOICE_DOCUMENT_NO,           ");
            sql.append("          L.INVOICE_FISCAL_YEAR AS       LINE_INVOICE_FISCAL_YEAR,           ");
            sql.append("          ''                                                               AS LINE_DOCUMENT_BASE_TYPE,           ");
            sql.append("          ''                                                               AS LINE_COMPANY_CODE,           ");
            sql.append("          ''                                                                    AS LINE_DOCUMENT_TYPE,           ");
            sql.append("          ''                COUNT_CHILD,           ");
            sql.append("          H.PO_DOCUMENT_NO               PO_DOCUMENT_NO,           ");
            sql.append("          H.REVERSE_DATE_ACCT            REVERSE_DATE_ACCT,           ");
            sql.append("          H.REVERSE_REASON               REVERSE_REASON,           ");
            sql.append("          H.INVOICE_DOCUMENT_NO          INVOICE_DOCUMENT_NO,           ");
            sql.append(
                    "          H.REVERSE_INVOICE_DOCUMENT_NO  REVERSE_INVOICE_DOCUMENT_NO,           ");
            sql.append("          H.HEADER_REFERENCE             HEADER_REFERENCE,           ");
            sql.append("          H.HEADER_REFERENCE2            HEADER_REFERENCE2,           ");
            sql.append("          H.DOCUMENT_HEADER_TEXT         DOCUMENT_HEADER_TEXT,           ");
            sql.append("          H.ORIGINAL_DOCUMENT_NO         ORIGINAL_DOCUMENT_NO,           ");
            sql.append("          H.ORIGINAL_FISCAL_YEAR         ORIGINAL_FISCAL_YEAR,           ");
            sql.append(
                    "          H.REVERSE_ORIGINAL_DOCUMENT_NO REVERSE_ORIGINAL_DOCUMENT_NO,           ");
            sql.append(
                    "          H.REVERSE_ORIGINAL_FISCAL_YEAR REVERSE_ORIGINAL_FISCAL_YEAR,           ");
            sql.append("          H.COST_CENTER1                 COST_CENTER1,           ");
            sql.append("          H.COST_CENTER2                 COST_CENTER2,           ");
            sql.append("          H.DOCUMENT_TYPE                DOCUMENT_TYPE,           ");
            sql.append("          H.COMPANY_CODE                 COMPANY_CODE,           ");
            sql.append("          H.DATE_DOC                     DATE_DOC,           ");
            sql.append("          H.DATE_ACCT                    DATE_ACCT,           ");
            sql.append("          H.PERIOD                       PERIOD,           ");
            sql.append("          H.CURRENCY                     CURRENCY,           ");
            sql.append("          H.ORIGINAL_DOCUMENT            ORIGINAL_DOCUMENT,           ");
            sql.append("          H.DOCUMENT_CREATED             DOCUMENT_CREATED,           ");
            sql.append("          H.USER_PARK                    USER_PARK,           ");
            sql.append("          H.USER_POST                    USER_POST,           ");
            sql.append("          L.POSTING_KEY                  POSTING_KEY,           ");
            sql.append("          L.ACCOUNT_TYPE                 ACCOUNT_TYPE,           ");
            sql.append("          L.DR_CR                        DR_CR,           ");
            sql.append("          L.GL_ACCOUNT                   GL_ACCOUNT,           ");
            sql.append("          L.FI_AREA                      FI_AREA,           ");
            sql.append("          L.COST_CENTER                  COST_CENTER,           ");
            sql.append("          L.FUND_SOURCE                  FUND_SOURCE,           ");
            sql.append("          L.BG_CODE                      BG_CODE,           ");
            sql.append("          L.BG_ACTIVITY                  BG_ACTIVITY,           ");
            sql.append("          L.COST_ACTIVITY                COST_ACTIVITY,           ");
            sql.append("          SUM_L.AMOUNT          AS       AMOUNT,           ");
            sql.append("          L.REFERENCE3                   REFERENCE3,           ");
            sql.append("          L.ASSIGNMENT                   ASSIGNMENT,           ");
            sql.append("          H.BR_DOCUMENT_NO               BR_DOCUMENT_NO,           ");
            sql.append("          CNT_L.BR_LINE         AS       BR_LINE,           ");
            sql.append("          L.PAYMENT_CENTER               PAYMENT_CENTER,           ");
            sql.append("          L.BANK_BOOK                    BANK_BOOK,           ");
            sql.append("          L.BANK_BRANCH_NO               BANK_BRANCH_NO,           ");
            sql.append("          L.TRADING_PARTNER              TRADING_PARTNER,           ");
            sql.append("          L.TRADING_PARTNER_PARK         TRADING_PARTNER_PARK,           ");
            sql.append("          L.SPECIAL_GL                   SPECIAL_GL,           ");
            sql.append("          L.DATE_BASE_LINE               DATE_BASE_LINE,           ");
            sql.append("          L.DATE_VALUE                   DATE_VALUE,           ");
            sql.append("          L.ASSET_NO                     ASSET_NO,           ");
            sql.append("          L.ASSET_SUB_NO                 ASSET_SUB_NO,           ");
            sql.append("          L.QTY                          QTY,           ");
            sql.append("          L.UOM                          UOM,           ");
            sql.append("          L.INCOME                       INCOME,           ");
            sql.append("          L.REFERENCE1                   REFERENCE1,           ");
            sql.append("          L.REFERENCE2                   REFERENCE2,           ");
            sql.append("          L.PO_LINE             AS       PO_LINE,           ");
            sql.append("          L.PAYMENT_REFERENCE            PAYMENT_REFERENCE,           ");
            sql.append("          L.SUB_ACCOUNT                  SUB_ACCOUNT,           ");
            sql.append("          L.SUB_ACCOUNT_OWNER            SUB_ACCOUNT_OWNER,           ");
            sql.append("          L.DEPOSIT_ACCOUNT              DEPOSIT_ACCOUNT,           ");
            sql.append("          L.DEPOSIT_ACCOUNT_OWNER        DEPOSIT_ACCOUNT_OWNER,           ");
            sql.append("          L.GPSC                         GPSC,           ");
            sql.append("          L.GPSC_GROUP                   GPSC_GROUP,           ");
            sql.append("          L.LINE_ITEM_TEXT               LINE_ITEM_TEXT,           ");
            sql.append("          L.LINE_DESC                    LINE_DESC,           ");
            sql.append("          L.PAYMENT_TERM                 PAYMENT_TERM,           ");
            sql.append("          L.PAYMENT_METHOD               PAYMENT_METHOD,           ");
            sql.append("          L.WTX_TYPE                     WTX_TYPE,           ");
            sql.append("          L.WTX_CODE                     WTX_CODE,           ");
            sql.append("          L.WTX_BASE                     WTX_BASE,           ");
            sql.append("          L.WTX_AMOUNT                   WTX_AMOUNT,           ");
            sql.append("          L.WTX_TYPE_P                   WTX_TYPE_P,           ");
            sql.append("          L.WTX_CODE_P                   WTX_CODE_P,           ");
            sql.append("          L.WTX_BASE_P                   WTX_BASE_P,           ");
            sql.append("          L.WTX_AMOUNT_P                 WTX_AMOUNT_P,           ");
            sql.append("          L.PAYEE                        PAYEE,           ");
            sql.append("          L.PAYEE_TAX_ID                 PAYEE_TAX_ID,           ");
            sql.append("          L.VENDOR                       VENDOR,           ");
            sql.append("          VD.NAME                  VENDOR_NAME,           ");
            sql.append("          L.VENDOR_TAX_ID                VENDOR_TAX_ID,           ");
            sql.append("          L.BANK_ACCOUNT_NO              BANK_ACCOUNT_NO,           ");
            sql.append("          VB.A_NAME     BANK_ACCOUNT_HOLDER_NAME,           ");
            sql.append("          L.PAYMENT_BLOCK                PAYMENT_BLOCK,           ");
            sql.append("          AC.CONFIRMSTATUS               CONFIRM_VENDOR,           ");
            sql.append("          L.FUND_TYPE                    FUND_TYPE,           ");
            sql.append("          H.DOCUMENT_BASE_TYPE           DOCUMENT_BASE_TYPE,           ");
            sql.append("          H.SELECT_GROUP_DOCUMENT           SELECT_GROUP_DOCUMENT           ");
            sql.append("          FROM GL_HEAD H           ");
            sql.append("          LEFT JOIN GL_LINE L ON H.ID = L.GL_HEAD_ID           ");
            sql.append(
                    "          LEFT JOIN (SELECT COUNT(1) AS BR_LINE, ORIGINAL_DOCUMENT_NO FROM GL_LINE GROUP BY ORIGINAL_DOCUMENT_NO) CNT_L           ");
            sql.append("          ON H.ORIGINAL_DOCUMENT_NO = CNT_L.ORIGINAL_DOCUMENT_NO           ");
            sql.append(
                    "          LEFT JOIN (SELECT SUM(AMOUNT) AS AMOUNT, ORIGINAL_DOCUMENT_NO           ");
            sql.append("          FROM GL_LINE           ");
            sql.append("          WHERE ACCOUNT_TYPE = 'K'           ");
            sql.append(
                    "          GROUP BY ORIGINAL_DOCUMENT_NO) SUM_L ON H.ORIGINAL_DOCUMENT_NO = SUM_L.ORIGINAL_DOCUMENT_NO           ");
            sql.append("    LEFT  JOIN ").append(schema).append(".C_BPARTNER").append(" VD ON VD.VALUE = L.VENDOR ");
            sql.append("    LEFT  JOIN ").append(schema).append(".TH_APBPARTNERSTATUS").append(" AC ON VD.C_BPARTNER_ID = AC.C_BPARTNER_ID ");
            sql.append("          OUTER APPLY (           ");
            sql.append("          SELECT *           ");

            sql.append("    FROM ").append(schema).append(".C_BP_BANKACCOUNT BB");
            sql.append("          WHERE BB.ISACTIVE = 'Y'           ");
            sql.append("          AND vd.C_BPARTNER_ID = BB.C_BPARTNER_ID           ");
            sql.append("          AND L.BANK_ACCOUNT_NO = BB.ACCOUNTNO           ");
            sql.append("          ORDER BY BB.ACCOUNTNO           ");
            sql.append("          FETCH FIRST ROW ONLY           ");
            sql.append("          ) vb           ");
            sql.append("          WHERE H.ORIGINAL_DOCUMENT_NO = L.ORIGINAL_DOCUMENT_NO           ");
            sql.append("  AND L.ACCOUNT_TYPE='K'  ");
            sql.append("  AND (L.PAYMENT_BLOCK = '' OR L.PAYMENT_BLOCK = ' ' OR L.PAYMENT_BLOCK IS NULL)  ");

            sql.append("  AND H.PAYMENT_DOCUMENT_NO IS NULL           ");
            sql.append("  AND NVL(H.PAYMENT_ID,0) = 0           ");


//            sql.append("  AND H.DOCUMENT_TYPE != 'K2'  ");

            log.info("selectGroupDocument {}", selectGroupDocument);

            int year = Integer.parseInt(selectGroupDocument.getFiscalYear()) - 543;
            params.add(year);
//        params2.put("fiscalYear", selectGroupDocument.getFiscalYear());
            sql.append(" AND ((UPPER(H.ORIGINAL_FISCAL_YEAR) LIKE UPPER(?)  ");

            SelectGroupDocumentList selectGroupDocumentList = JSONUtil.convertJsonToObject(selectGroupDocument.getJsonText(), SelectGroupDocumentList.class);
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
                    sql.append(checkOptionExclude + "  ((UPPER(H.COMPANY_CODE) LIKE UPPER(?) ");
//                params2.put("GH.COMPANY_CODE" + i, selectGroupDocumentConfigList.get(i).getCompanyCode());

                    List<BaseRange> listDocument = selectGroupDocumentConfigList.get(i).getList();

                    DynamicCondition.baseRangeConditionOM(selectGroupDocumentConfigList.get(i).getList(), sql, params, "H.ORIGINAL_DOCUMENT_NO");


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
                                sql.append(" OR  ((UPPER(H.COMPANY_CODE) LIKE UPPER('99999')) AND (UPPER(H.DOCUMENT_TYPE) LIKE UPPER('KY')) ");

                            } else {
                                sql.append(checkOptionExcludeStep2 + "   ((UPPER(H.COMPANY_CODE) LIKE UPPER('99999')) AND (UPPER(H.DOCUMENT_TYPE) LIKE UPPER('KY')) ");

                            }


                            if (!listDocument.get(x).isOptionExclude()) {
                                if (!Util.isEmpty(listDocument.get(x).getFrom()) && !Util.isEmpty(listDocument.get(x).getTo())) {
                                    sql.append(SqlUtil.newWhereClauseRangeOne(from, to,
                                            "H.ORIGINAL_DOCUMENT", params, ++index, checkOptionExcludeStep2));
                                } else {
                                    sql.append(SqlUtil.whereClause(from, "H.ORIGINAL_DOCUMENT", params));
                                }
                            } else {
                                if (!Util.isEmpty(listDocument.get(x).getFrom()) && !Util.isEmpty(listDocument.get(x).getTo())) {
                                    sql.append(SqlUtil.newWhereClauseNotRangeOne(from, to,
                                            "H.ORIGINAL_DOCUMENT", params, ++index, checkOptionExcludeStep2));
                                } else {
                                    sql.append(SqlUtil.whereClauseNot(from, "H.ORIGINAL_DOCUMENT", params));
                                }

                            }
                        }

                    }
                    sql.append(" ) ");
                    sql.append(" ) ");
                }

            }
            sql.append(" )) ");


            sql.append("          ORDER BY H.DATE_DOC DESC, H.ORIGINAL_DOCUMENT_NO DESC           ");

            Object[] objParams = new Object[params.size()];
            params.toArray(objParams);

            log.info("sql : {} ", sql);
            log.info("params : {} ", params);
            log.info("objParams : {} ", objParams);
            return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateSQLWhereAuthorized(JwtBody jwt, List<Object> params) {
        StringBuilder sb = new StringBuilder();

        String AUTHORIZATION_OBJECT_NAME = "FICOMMON";
        String AUTHORIZATION_ACTIVITY = AuthorizeUtilService.UPDATE_PBK_ACTIVITY;
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
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.PAYMENT_CENTER_ATTRIBUTE, "L.PAYMENT_CENTER", null);
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
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.DOC_TYPE_ATTRIBUTE, "H.DOCUMENT_TYPE", null);
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
        authSQL = authorizeUtilService.getUserAuthorizationSQL(jwt.getSub(), AUTHORIZATION_OBJECT_NAME,
                AUTHORIZATION_ACTIVITY, AuthorizeUtilService.ACCOUNT_TYPE_ATTIBUTE, "L.ACCOUNT_TYPE", null);
        if (!Util.isEmpty(authSQL)) {
            sb.append(" AND (").append(authSQL).append(")");
        }

        return sb.toString();
    }
}
