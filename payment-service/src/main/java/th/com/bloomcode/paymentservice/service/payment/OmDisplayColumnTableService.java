package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.OmDisplayColumnTable;
import th.com.bloomcode.paymentservice.model.request.OmDisplayColumnTableRequest;

import java.util.List;

public interface OmDisplayColumnTableService {
    ResponseEntity<Result<OmDisplayColumnTable>> save(OmDisplayColumnTableRequest request);
    ResponseEntity<Result<OmDisplayColumnTable>> update(OmDisplayColumnTableRequest request);
    ResponseEntity<Result<OmDisplayColumnTable>> getOneById(Long id);
    ResponseEntity<Result<List<OmDisplayColumnTable>>> getAllByRole(String role, String user, String value);
    ResponseEntity<Result<OmDisplayColumnTable>> getOneByRoleAndName(String role, String name);
    ResponseEntity<Result<OmDisplayColumnTable>> deleteById(Long id);
    void delete(Long id);
}
