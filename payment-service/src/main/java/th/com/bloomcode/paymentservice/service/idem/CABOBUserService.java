package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;

public interface CABOBUserService {
    ResponseEntity<Result<Boolean>> existByUsernameAndPassword(String username, String password);
}
