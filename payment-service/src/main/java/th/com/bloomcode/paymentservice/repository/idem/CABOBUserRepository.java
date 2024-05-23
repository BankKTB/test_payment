package th.com.bloomcode.paymentservice.repository.idem;

public interface CABOBUserRepository {
  boolean existByUsernameAndPassword(String username, String password);
}
