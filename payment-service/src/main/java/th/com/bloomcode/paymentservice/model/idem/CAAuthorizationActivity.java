package th.com.bloomcode.paymentservice.model.idem;

import lombok.Data;
import th.com.bloomcode.paymentservice.helper.ColumnRowMapper;
import th.com.bloomcode.paymentservice.helper.DBConnection;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
public class CAAuthorizationActivity {

  public static final String SQL_ALL_FIELD_BY_VALUE_CODE =
      "SELECT * FROM TH_CAAuthActivity WHERE ValueCode = ?";

  @Column(name = "TH_CAAUTHACTIVITY_ID")
  private int authActivityId;

  @Column(name = "AD_CLIENT_ID")
  private int clientId;

  @Column(name = "AD_ORG_ID")
  private int orgId;

  @Column(name = "CREATED")
  private Timestamp created;

  @Column(name = "CREATEDBY")
  private int createdBy;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ISACTIVE")
  private String isActive;

  @Column(name = "NAME")
  private String name;

  @Column(name = "TH_CAAUTHACTIVITY_UU")
  private String authActivityUUID;

  @Column(name = "UPDATED")
  private Timestamp updated;

  @Column(name = "UPDATEDBY")
  private int updatedBy;

  @Column(name = "VALUECODE")
  private String valueCode;

  public static CAAuthorizationActivity get(String valueCode) {
    return DBConnection.getJdbcTemplate("idem")
        .queryForObject(
            SQL_ALL_FIELD_BY_VALUE_CODE,
            new Object[] {valueCode},
            ColumnRowMapper.newInstance(CAAuthorizationActivity.class));
  }
}
