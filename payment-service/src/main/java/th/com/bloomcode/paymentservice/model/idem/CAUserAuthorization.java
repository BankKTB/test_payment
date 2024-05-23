package th.com.bloomcode.paymentservice.model.idem;

import lombok.Data;
import th.com.bloomcode.paymentservice.helper.ColumnRowMapper;
import th.com.bloomcode.paymentservice.helper.DBConnection;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
public class CAUserAuthorization {

  public static final String SQL_ALL_FIELD_BY_USER_ID =
      "SELECT * FROM TH_CAUserAuthorization WHERE TH_CAUser_ID = ?";

  @Column(name = "AD_CLIENT_ID")
  private int clientId;

  @Column(name = "AD_ORG_ID")
  private int orgId;

  @Column(name = "CREATED")
  private Timestamp created;

  @Column(name = "CREATEDBY")
  private String createdBy;

  @Column(name = "FROMVALUE")
  private String fromValue;

  @Column(name = "ISACTIVE")
  private String isActive;

  @Column(name = "ISEXCLUDE")
  private String isExclude;

  @Column(name = "TH_CAAUTHACTIVITY_ID")
  private int authActivityId;

  @Column(name = "TH_CAAUTHATTRIBUTE_ID")
  private int authAttributeId;

  @Column(name = "TH_CAUSERAUTHORIZATION_ID")
  private int userAuthorizationId;

  @Column(name = "TH_CAUSERAUTHORIZATION_UU")
  private String userAuthorizationUUID;

  @Column(name = "TH_CAUSER_ID")
  private int userId;

  @Column(name = "TOVALUE")
  private String toValue;

  @Column(name = "UPDATED")
  private Timestamp updated;

  @Column(name = "UPDATEDBY")
  private String updatedBy;

  @Column(name = "VALIDFROM")
  private Timestamp validFrom;

  @Column(name = "VALIDTO")
  private Timestamp validTo;

  @Column(name = "TH_CAAUTHOBJECT_ID")
  private int authObjectId;

  public CAUserAuthorization get(int userId) {
    return DBConnection.getJdbcTemplate("idem")
        .queryForObject(
            SQL_ALL_FIELD_BY_USER_ID,
            new Object[] {userId},
            ColumnRowMapper.newInstance(getClass()));
  }
}
