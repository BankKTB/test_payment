package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.APUserChgBankAccNo;
import th.com.bloomcode.paymentservice.model.request.ChangeDocumentRequest;
import th.com.bloomcode.paymentservice.model.request.CheckAPUserBankAccNoRequest;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.APUserChgBankAccNoRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.TimestampUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class APUserChgBankAccNoRepositoryImpl extends MetadataJdbcRepository<APUserChgBankAccNo, Long> implements APUserChgBankAccNoRepository {
    static BeanPropertyRowMapper<APUserChgBankAccNo> beanPropertyRowMapper = new BeanPropertyRowMapper<>(APUserChgBankAccNo.class);
    private final JdbcTemplate jdbcTemplate;


    static RowMapper<APUserChgBankAccNo> userRowMapper = (rs, rowNum) -> new APUserChgBankAccNo(
            rs.getLong(APUserChgBankAccNo.COLUMN_NAME_APUSERCHGBANKACCNO_ID),
            rs.getString(APUserChgBankAccNo.COLUMN_NAME_IS_ACTIVE),
            rs.getString(APUserChgBankAccNo.COLUMN_NAME_USERWEBONLINE),
            rs.getString(APUserChgBankAccNo.COLUMN_NAME_DOCTYPENAME),
            rs.getTimestamp(APUserChgBankAccNo.COLUMN_NAME_VALID_FROM),
            rs.getTimestamp(APUserChgBankAccNo.COLUMN_NAME_VALID_TO)
    );

    @Autowired
    public APUserChgBankAccNoRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                null,
                null,
                APUserChgBankAccNo.TABLE_NAME,
                APUserChgBankAccNo.COLUMN_NAME_APUSERCHGBANKACCNO_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long countAllByValueCode(CheckAPUserBankAccNoRequest checkAPUserBankAccNoRequest) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(1) FROM TH_APUSERCHGBANKACCNO ");
        sql.append(" where 1=1  ");
        sql.append(" AND ISACTIVE = 'Y' ");

        params.add(TimestampUtil.timestampToString(new Timestamp(System.currentTimeMillis())));
        sql.append(" AND TO_CHAR(VALIDFROM,'YYYY-MM-DD') <= ? ");
        params.add(TimestampUtil.timestampToString(new Timestamp(System.currentTimeMillis())));
        sql.append(" AND TO_CHAR(VALIDTO,'YYYY-MM-DD') >= ? ");

        if (!Util.isEmpty(checkAPUserBankAccNoRequest.getWebInfo().getUserWebOnline())) {
            sql.append(SqlUtil.whereClause(checkAPUserBankAccNoRequest.getWebInfo().getUserWebOnline(), "USERWEBONLINE", params));
        }

        if (!Util.isEmpty(checkAPUserBankAccNoRequest.getDocumentType())) {
            sql.append(SqlUtil.whereClause(checkAPUserBankAccNoRequest.getDocumentType(), "DOCTYPENAME", params));
        }





        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.queryForObject(sql.toString(), objParams, Long.class);
    }

    @Override
    public List<APUserChgBankAccNo> findAllByValueCode(CheckAPUserBankAccNoRequest checkAPUserBankAccNoRequest) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT TH_APUSERCHGBANKACCNO_ID, ");
        sql.append("  ISACTIVE, ");
        sql.append("  USERWEBONLINE, ");
        sql.append("  DOCTYPENAME, ");
        sql.append("  VALIDFROM, ");
        sql.append("  VALIDTO ");
        sql.append(" FROM TH_APUSERCHGBANKACCNO  ");
        sql.append(" where 1=1  ");
        sql.append(" AND ISACTIVE = 'Y' ");

        params.add(TimestampUtil.timestampToString(new Timestamp(System.currentTimeMillis())));
        sql.append(" AND TO_CHAR(VALIDFROM,'YYYY-MM-DD') <= ? ");
        params.add(TimestampUtil.timestampToString(new Timestamp(System.currentTimeMillis())));
        sql.append(" AND TO_CHAR(VALIDTO,'YYYY-MM-DD') >= ? ");

        if (!Util.isEmpty(checkAPUserBankAccNoRequest.getWebInfo().getUserWebOnline())) {
            sql.append(SqlUtil.whereClause(checkAPUserBankAccNoRequest.getWebInfo().getUserWebOnline(), "USERWEBONLINE", params));
        }

        if (!Util.isEmpty(checkAPUserBankAccNoRequest.getDocumentType())) {
            sql.append(SqlUtil.whereClause(checkAPUserBankAccNoRequest.getDocumentType(), "DOCTYPENAME", params));
        }




        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} ", params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }


}