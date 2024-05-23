package th.com.bloomcode.paymentservice.authorization;

public interface IAuthorizationObject {
  boolean isExclude();

  boolean isActive();

  String getFromValue();

  String getToValue();

  String getAuthorizationObject();

  String getAuthorizationAttribute();

  String getAuthorizationActivity();

  //	public void setIsExclude(boolean exclude);
  //	public void setIsActive(boolean active);
  //	public void setFromValue(String value);
  //	public void setToValue(String value);
  //	public void setAuthorizationObject(String value);
  //	public void setAuthorizationAttribute(String value);
  //	public void setAuthorizationActivity(String value);
}
