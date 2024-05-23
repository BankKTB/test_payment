package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.DocumentType;

import java.util.List;

public interface DocumentTypeService {
    ResponseEntity<Result<List<DocumentType>>> findAllByValue(String valueCode);
    ResponseEntity<Result<DocumentType>> findOneByValue(String valueCode);
}
