package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.NonBusinessDay;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.NonBusinessDayRepository;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class NonBusinessDayRepositoryImpl extends MetadataJdbcRepository<NonBusinessDay, Long> implements NonBusinessDayRepository {
    static BeanPropertyRowMapper<NonBusinessDay> beanPropertyRowMapper = new BeanPropertyRowMapper<>(NonBusinessDay.class);
    private final JdbcTemplate jdbcTemplate;
    static Updater<NonBusinessDay> documentTypeUpdater = (t, mapping) -> {
        mapping.put(NonBusinessDay.COLUMN_NAME_C_NONBUSINESSDAY_ID, t.getId());
        mapping.put(NonBusinessDay.COLUMN_NAME_NAME, t.getName());
        mapping.put(NonBusinessDay.COLUMN_NAME_ISACTIVE, t.getName());
        mapping.put(NonBusinessDay.COLUMN_NAME_DATE1, t.getName());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(NonBusinessDay.COLUMN_NAME_C_NONBUSINESSDAY_ID, Types.BIGINT),
            entry(NonBusinessDay.COLUMN_NAME_NAME, Types.NVARCHAR),
            entry(NonBusinessDay.COLUMN_NAME_ISACTIVE, Types.BOOLEAN),
            entry(NonBusinessDay.COLUMN_NAME_DATE1, Types.TIMESTAMP)
    );

    static RowMapper<NonBusinessDay> userRowMapper = (rs, rowNum) -> new NonBusinessDay(
            rs.getLong(NonBusinessDay.COLUMN_NAME_C_NONBUSINESSDAY_ID),
            rs.getString(NonBusinessDay.COLUMN_NAME_NAME),
            rs.getBoolean(NonBusinessDay.COLUMN_NAME_ISACTIVE),
            rs.getTimestamp(NonBusinessDay.COLUMN_NAME_DATE1)
    );

    @Autowired
    public NonBusinessDayRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                NonBusinessDay.TABLE_NAME,
                NonBusinessDay.COLUMN_NAME_C_NONBUSINESSDAY_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<NonBusinessDay> findAll() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" C_NONBUSINESSDAY_ID, ");
        sql.append(" ISACTIVE, ");
        sql.append(" NAME, ");
        sql.append(" DATE1 ");
        sql.append(" FROM ");
        sql.append(" C_NONBUSINESSDAY ");
        sql.append(" WHERE ISACTIVE = 'Y' ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, userRowMapper);
    }

    @Override
    public  List<NonBusinessDay>  findByDateAndRangeDay(String dateBegin, String rangeDay) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" C_NONBUSINESSDAY_ID, ");
        sql.append(" ISACTIVE, ");
        sql.append(" NAME, ");
        sql.append(" DATE1 ");
        sql.append(" FROM ");
        sql.append(" C_NONBUSINESSDAY ");
        sql.append(" WHERE 1 = 1 AND ISACTIVE = 'Y' ");
        if (!Util.isEmpty(dateBegin) && !Util.isEmpty(rangeDay)) {
            sql.append(" AND DATE1 BETWEEN TO_DATE(?,'YYYY-MM-DD') AND TO_DATE(?,'YYYY-MM-DD') + ? ");
        }
        return this.jdbcTemplate.query(sql.toString(), new Object[] { dateBegin, dateBegin, rangeDay}, userRowMapper);
    }
}
