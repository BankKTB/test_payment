package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;

import java.util.List;

public interface SelectGroupDocumentRepository extends CrudRepository<SelectGroupDocument, Long> {


    List<SelectGroupDocument> findOneByIndependentSelect(String defineName);

}
