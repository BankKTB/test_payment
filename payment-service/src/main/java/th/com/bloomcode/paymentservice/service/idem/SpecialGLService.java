package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.SpecialGL;

import java.util.List;

public interface SpecialGLService {
    ResponseEntity<Result<List<SpecialGL>>> findAllByValue(String valueCode);
    ResponseEntity<Result<SpecialGL>> findOneByValue(String valueCode);
}
