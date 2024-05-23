package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.OmSearchCriteria;
import th.com.bloomcode.paymentservice.model.request.OmSearchCriteriaRequest;

import java.util.List;

public interface OmSearchCriteriaService {
    ResponseEntity<Result<OmSearchCriteria>> save(OmSearchCriteriaRequest request);
    ResponseEntity<Result<OmSearchCriteria>> update(OmSearchCriteriaRequest request);
    ResponseEntity<Result<OmSearchCriteria>> getOneById(Long id);
    ResponseEntity<Result<List<OmSearchCriteria>>> getAllByRole(String role, String user, String value);
    ResponseEntity<Result<OmSearchCriteria>> deleteById(Long id);
    void delete(Long id);
}
