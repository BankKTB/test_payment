package th.com.bloomcode.paymentservice.authorization;

import th.com.bloomcode.paymentservice.model.idem.CAEffectiveAuthorization;

import java.sql.SQLException;

public class AuthorizationObject implements IAuthorizationObject {
  private boolean isExclude = false;
  private boolean isActive = false;
  private String fromValue = null;
  private String toValue = null;
  private String authorizationObject = null;
  private String authorizationAttribute = null;
  private String operation = null;

  public AuthorizationObject(CAEffectiveAuthorization CAEffectiveAuthorization)
      throws SQLException {
    setIsExclude("Y".equalsIgnoreCase(CAEffectiveAuthorization.getIsExclude()));
    setIsActive("Y".equalsIgnoreCase(CAEffectiveAuthorization.getIsActive()));
    setFromValue(CAEffectiveAuthorization.getFromValue());
    setToValue(CAEffectiveAuthorization.getToValue());
    setAuthorizationObject(CAEffectiveAuthorization.getAuthorizationObject());
    setAuthorizationAttribute(CAEffectiveAuthorization.getAuthorizationAttribute());
    setAuthorizationActivity(CAEffectiveAuthorization.getAuthorizationActivity());
  }

  @Override
  public boolean isExclude() {
    return isExclude;
  }

  public void setIsExclude(boolean isExclude) {
    this.isExclude = isExclude;
  }

  @Override
  public boolean isActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public String getFromValue() {
    return fromValue;
  }

  public void setFromValue(String fromValue) {
    this.fromValue = fromValue;
  }

  @Override
  public String getToValue() {
    return toValue;
  }

  public void setToValue(String toValue) {
    this.toValue = toValue;
  }

  @Override
  public String getAuthorizationObject() {
    return authorizationObject;
  }

  public void setAuthorizationObject(String authorizationObject) {
    this.authorizationObject = authorizationObject;
  }

  @Override
  public String getAuthorizationAttribute() {
    return authorizationAttribute;
  }

  public void setAuthorizationAttribute(String authorizationAttribute) {
    this.authorizationAttribute = authorizationAttribute;
  }

  @Override
  public String getAuthorizationActivity() {
    return operation;
  }

  public void setAuthorizationActivity(String operation) {
    this.operation = operation;
  }
}
