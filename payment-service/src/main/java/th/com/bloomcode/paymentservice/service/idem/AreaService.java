package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.Area;

import java.util.List;

public interface AreaService {
    ResponseEntity<Result<List<Area>>> findAllByValue(String valueCode);
    ResponseEntity<Result<Area>> findOneByValue(String valueCode);
    Area findOneByValueCode(String valueCode);
}
