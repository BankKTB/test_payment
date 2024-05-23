package th.com.bloomcode.paymentservice.model.idem;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import th.com.bloomcode.paymentservice.helper.ColumnRowMapper;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Data
@Slf4j
public class CAUser {

  public static final String SQL_ALL_FIELD_BY_USERNAME =
      "SELECT * FROM TH_CAUser WHERE UserName = ?";

  @Column(name = "TH_CAUSER_ID")
  private int userId;

  @Column(name = "AD_CLIENT_ID")
  private int clientId;

  @Column(name = "AD_ORG_ID")
  private int orgId;

  @Column(name = "CREATED")
  private Timestamp created;

  @Column(name = "CREATEDBY")
  private int createdBy;

  @Column(name = "ISACTIVE")
  private String isActive;

  @Column(name = "TH_CAUSER_UU")
  private String userUUID;

  @Column(name = "UPDATED")
  private Timestamp updated;

  @Column(name = "UPDATEDBY")
  private int updatedBy;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "VALIDFROM")
  private Timestamp validFrom;

  @Column(name = "VALIDTO")
  private Timestamp validTo;

  public static CAUser get(String username, JdbcTemplate jdbcTemplate) {
    return jdbcTemplate.queryForObject(
            CAUser.SQL_ALL_FIELD_BY_USERNAME,
            new Object[] {username},
            ColumnRowMapper.newInstance(CAUser.class));
  }

  public boolean isAuthorized(String object, String operation, String attribute, String value, JdbcTemplate jdbcTemplate) {
    if ("Y".equalsIgnoreCase(isActive) && isValid(new Date())) {
      StringBuilder sql = new StringBuilder("SELECT * FROM TH_CAEffectiveAuthorization");
      sql.append(
          " WHERE th_causer_id=? AND regexp_like(?, authorizationobject) AND authorizationactivity=? and authorizationattribute=?");
      sql.append(" AND (regexp_like(fromvalue,'^\\s*\\*\\s*$')");
      sql.append(
          " OR (tovalue IS NOT NULL AND ? BETWEEN regexp_replace(fromvalue,'[*%]$','') AND regexp_replace(tovalue,'[*%]$','Z'))");
      sql.append(
          " OR (tovalue IS NULL AND ? LIKE regexp_replace(regexp_replace(fromvalue,'[*]','%'),'[+]','_')))");
      sql.append(" ORDER BY isexclude DESC");

      try {
        // prepare sql statement
        CAEffectiveAuthorization CAEffectiveAuthorization = jdbcTemplate.queryForObject(
                    sql.toString(),
                    new Object[] {userId, object, operation, attribute, value, value},
                    ColumnRowMapper.newInstance(CAEffectiveAuthorization.class));
        return null != CAEffectiveAuthorization
            && "N".equalsIgnoreCase(CAEffectiveAuthorization.getIsExclude());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return false;
      } finally {
        // TODO close DB
      }
    }

    return false;
  }

  public boolean isValid(Date date) {
    if (date == null) return false;

    // Clear time portion
    Calendar cal = Calendar.getInstance();
    cal.setTime(getValidFrom());
    cal.clear(Calendar.HOUR);
    cal.clear(Calendar.MINUTE);
    cal.clear(Calendar.SECOND);
    Date validFrom = cal.getTime();

    cal.setTime(getValidTo());
    cal.clear(Calendar.HOUR);
    cal.clear(Calendar.MINUTE);
    cal.clear(Calendar.SECOND);
    cal.add(Calendar.DATE, 1);
    Date validTo = cal.getTime();

    return validFrom.compareTo(date) <= 0 && validTo.compareTo(date) > 0;
  }
}
