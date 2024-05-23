package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.JULine;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.payment.entity.mapping.JUDocument;
import th.com.bloomcode.paymentservice.payment.entity.mapping.JUDocumentExport;

import java.util.List;
import java.util.Map;

public interface JULineRepository extends CrudRepository<JULine, Long> {
    void deleteAllByJUHeadId(Long juHeadId);
    List<JUDocument> selectJuDetail(GenerateJuRequest request);
    List<JUDocumentExport> selectJuDetailExport(GenerateJuRequest request);
}
