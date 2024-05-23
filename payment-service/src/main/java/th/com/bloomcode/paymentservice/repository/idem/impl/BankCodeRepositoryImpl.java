package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.BankCode;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.BankCodeRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class BankCodeRepositoryImpl extends MetadataJdbcRepository<BankCode, Long>  implements BankCodeRepository {
    static BeanPropertyRowMapper<BankCode> beanPropertyRowMapper = new BeanPropertyRowMapper<>(BankCode.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BankCodeRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                beanPropertyRowMapper,
                null,
                null,
                BankCode.TABLE_NAME,
                BankCode.COLUMN_NAME_TH_CABANK_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BankCode> findAll(boolean isActive) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT BANKACCOUNTNO   AS ACCOUNT_NO, ");
        sql.append(" VALUECODE       AS BANK_KEY, ");
        sql.append(" NAME            AS BANK_NAME, ");
        sql.append(" SHORTNAME       AS BANK_SHORT_NAME, ");
        sql.append(" SWIFTCODE       AS INCST_CODE, ");
        sql.append(" NVL(ISINHOUSEFORMAT, 'N') AS IN_HOUSE, ");
        sql.append(" INHOUSENO       AS IN_HOUSE_NO, ");
        sql.append(" PAYMENTTYPE     AS PAY_ACCOUNT, ");
        sql.append(" CB.ISACTIVE     AS ACTIVE ");
        sql.append(" FROM TH_CABANK CB LEFT JOIN TH_CABANKPAYMENTTYPE PT ON CB.TH_CABANK_ID = PT.TH_CABANK_ID ");
        if (isActive) {
            sql.append(" WHERE CB.ISACTIVE = 'Y' ");
        }
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        log.info("params : {} " , params);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }

}
