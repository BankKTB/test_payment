package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.helper.ColumnRowMapper;
import th.com.bloomcode.paymentservice.model.idem.CABOBUser;
import th.com.bloomcode.paymentservice.repository.idem.CABOBUserRepository;
import th.com.bloomcode.paymentservice.util.Util;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class CABOBUserRepositoryImpl implements CABOBUserRepository {

  private final JdbcTemplate jdbcTemplate;

  public CABOBUserRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public boolean existByUsernameAndPassword(String username, String password) {
    List<Object> params = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    sb.append("          SELECT * FROM TH_CABOBUser           ");
    sb.append("          WHERE 1=1 AND ISACTIVE = 'Y' AND TYPE = 'PMT'          ");

    if (!Util.isEmpty(username)) {
      sb.append(" AND USERNAME = ? ");
      params.add(username);
    }

    if (!Util.isEmpty(password)) {
      sb.append(" AND PASSWORD = ? ");
      params.add(password);
    }

    Object[] objParams = new Object[params.size()];
    params.toArray(objParams);
    log.info("objParams : {}", objParams);
    log.info("sql : {}", sb.toString());

    List<CABOBUser> cabobUsers = this.jdbcTemplate.query(sb.toString(), objParams, ColumnRowMapper.newInstance(CABOBUser.class));
    return !cabobUsers.isEmpty();
  }
}
