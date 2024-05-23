package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.helper.ColumnRowMapper;
import th.com.bloomcode.paymentservice.helper.DBConnection;
import th.com.bloomcode.paymentservice.model.idem.BankBranch;
import th.com.bloomcode.paymentservice.model.idem.CAClient;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.BankBranchRepository;
import th.com.bloomcode.paymentservice.repository.idem.CAClientRepository;


import java.util.List;

@Repository
@Slf4j
public class CAClientRepositoryImpl extends MetadataJdbcRepository<CAClient, Long> implements CAClientRepository {
    static BeanPropertyRowMapper<CAClient> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CAClient.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CAClientRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                beanPropertyRowMapper,
                null,
                null,
                CAClient.TABLE_NAME,
                CAClient.COLUMN_NAME_AD_ORG_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CAClient> findAll() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT AD_ORG_ID, CLIENTVALUE, TARGETURL, CLIENT_ID, ROLE_ID, USERNAME, PASSWORD ");
        sql.append(" FROM TH_CACLIENT ");
        sql.append(" WHERE ISACTIVE = 'Y' ");

        log.info("sql : {}", sql.toString());

        return this.jdbcTemplate.query(sql.toString(), beanPropertyRowMapper);
    }
}
