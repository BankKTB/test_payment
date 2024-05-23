package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.CAClient;
import th.com.bloomcode.paymentservice.model.idem.CAEffectiveAuthorization;
import th.com.bloomcode.paymentservice.repository.idem.CAEffectiveAuthorizationRepository;

import java.util.List;

@Slf4j
@Repository
public class CAEffectiveAuthorizationRepositoryImpl implements CAEffectiveAuthorizationRepository {

  static BeanPropertyRowMapper<CAEffectiveAuthorization> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CAEffectiveAuthorization.class);
  private final JdbcTemplate jdbcTemplate;

  public CAEffectiveAuthorizationRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  @Cacheable(value = "caEffectiveAuthorization", key = "{#username, #object, #activity, #attribute}", unless = "#result==null")
  public List<CAEffectiveAuthorization> findByUsername(String username, String object, String activity, String attribute) {
    String sb = " SELECT ea.* FROM TH_CAEffectiveAuthorization ea" +
//                " INNER JOIN TH_CAUser u ON (u.TH_CAUser_ID=ea.TH_CAUser_ID)" +
            " WHERE ea.UserName = ? AND ea.AuthorizationObject = ? AND ea.AuthorizationActivity = ? AND AuthorizationAttribute = ?";
    log.info("sql : {}", sb.toString());
    return jdbcTemplate
            .query(
                    sb,
                    new Object[]{username, object, activity, attribute},
                    beanPropertyRowMapper);
  }
}
