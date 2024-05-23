package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.SelectGroupDocumentRepository;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class SelectGroupDocumentRepositoryImpl extends MetadataJdbcRepository<SelectGroupDocument, Long> implements SelectGroupDocumentRepository {
    static BeanPropertyRowMapper<SelectGroupDocument> beanPropertyRowMapper = new BeanPropertyRowMapper<>(SelectGroupDocument.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<SelectGroupDocument> generateFileAliasUpdater = (t, mapping) -> {
        mapping.put(SelectGroupDocument.COLUMN_NAME_SELECT_GROUP_DOCUMENT_ID, t.getId());
        mapping.put(SelectGroupDocument.COLUMN_NAME_FISCAL_YEAR, t.getFiscalYear());
        mapping.put(SelectGroupDocument.COLUMN_NAME_DEFINE_NAME, t.getDefineName());
        mapping.put(SelectGroupDocument.COLUMN_NAME_JSON_TEXT, t.getJsonText());
        mapping.put(SelectGroupDocument.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(SelectGroupDocument.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(SelectGroupDocument.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(SelectGroupDocument.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(SelectGroupDocument.COLUMN_NAME_SELECT_GROUP_DOCUMENT_ID, Types.BIGINT),
            entry(SelectGroupDocument.COLUMN_NAME_FISCAL_YEAR, Types.NVARCHAR),
            entry(SelectGroupDocument.COLUMN_NAME_DEFINE_NAME, Types.NVARCHAR),
            entry(SelectGroupDocument.COLUMN_NAME_JSON_TEXT, Types.NVARCHAR),
            entry(SelectGroupDocument.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(SelectGroupDocument.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(SelectGroupDocument.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
            entry(SelectGroupDocument.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR)
    );

//    static RowMapper<SelectGroupDocument> userRowMapper = (rs, rowNum) -> new SelectGroupDocument(
//            rs.getLong(SelectGroupDocument.COLUMN_NAME_BANK_CODE_ID),
//            rs.getString(SelectGroupDocument.COLUMN_NAME_ACCOUNT_NO),
//            rs.getString(SelectGroupDocument.COLUMN_NAME_BANK_KEY),
//            rs.getString(SelectGroupDocument.COLUMN_NAME_BANK_NAME),
//            rs.getString(SelectGroupDocument.COLUMN_NAME_BANK_SHORT_NAME),
//            rs.getString(SelectGroupDocument.COLUMN_NAME_INCST_CODE),
//            rs.getBoolean(SelectGroupDocument.COLUMN_NAME_IS_INHOUSE),
//            rs.getInt(SelectGroupDocument.COLUMN_NAME_INHOUSE_NO),
//            rs.getString(SelectGroupDocument.COLUMN_NAME_PAY_ACCOUNT)
//    );

    public SelectGroupDocumentRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, generateFileAliasUpdater, updaterType, SelectGroupDocument.TABLE_NAME, SelectGroupDocument.COLUMN_NAME_SELECT_GROUP_DOCUMENT_ID, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SelectGroupDocument> findOneByIndependentSelect(String defineName) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("          SELECT *           ");
        sql.append("          FROM " + SelectGroupDocument.TABLE_NAME + " ");
        sql.append("          WHERE 1=1           ");

        if (!Util.isEmpty(defineName)) {
            String textSearch = defineName.replace("*", "%");
            params.add(textSearch);
            sql.append(" AND " + SelectGroupDocument.COLUMN_NAME_DEFINE_NAME + " LIKE UPPER ( " + "  ? " + " ) ");
        }



        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql findOneByIndependentSelect {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }
}
