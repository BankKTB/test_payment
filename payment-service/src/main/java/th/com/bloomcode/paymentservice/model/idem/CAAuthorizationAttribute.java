package th.com.bloomcode.paymentservice.model.idem;

import lombok.Data;
import th.com.bloomcode.paymentservice.helper.ColumnRowMapper;
import th.com.bloomcode.paymentservice.helper.DBConnection;

import javax.persistence.Column;

@Data
public class CAAuthorizationAttribute {

  public static final String SQL_ALL_FIELD_BY_VALUE_CODE =
      "SELECT * FROM TH_CAAuthAttribute WHERE ValueCode = ?";

  @Column(name = "TH_CAAUTHATTRIBUTE_ID")
  private int authAttributeId;

  @Column(name = "AD_CLIENT_ID")
  private int clientId;

  @Column(name = "AD_ORG_ID")
  private int orgId;

  @Column(name = "CREATED")
  private String created;

  @Column(name = "CREATEDBY")
  private int createdBy;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ISACTIVE")
  private String isActive;

  @Column(name = "NAME")
  private String name;

  @Column(name = "TH_CAAUTHATTRIBUTE_UU")
  private String authAttribute;

  @Column(name = "UPDATED")
  private String updated;

  @Column(name = "UPDATEDBY")
  private int updatedBy;

  @Column(name = "VALUECODE")
  private String valueCode;

  public static CAAuthorizationAttribute get(String valueCode) {
    return DBConnection.getJdbcTemplate("idem")
        .queryForObject(
            SQL_ALL_FIELD_BY_VALUE_CODE,
            new Object[] {valueCode},
            ColumnRowMapper.newInstance(CAAuthorizationAttribute.class));
  }
}
