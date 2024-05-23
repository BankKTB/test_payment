package th.com.bloomcode.paymentservice.authorization;

public interface IAuthorizationFactory {
  IAuthorizationProvider get(String authObjectName, Object data);
}
