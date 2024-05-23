package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.DocumentType;

import java.util.List;

public interface DocumentTypeRepository  extends CrudRepository<DocumentType, Long> {
    Long countAllByValue(String valueCode);
    List<DocumentType> findAllByValue(String valueCode);
    DocumentType findOneByValue(String valueCode);
}
