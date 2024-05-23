package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.SearchPaymentBlockRequest;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.model.request.PaymentBlockListDocumentRequest;

import java.util.List;

public interface UnBlockDocumentRepository extends CrudRepository<UnBlockDocument, Long> {

    List<UnBlockDocument> findUnBlockByCondition(JwtBody jwt, SearchPaymentBlockRequest searchPaymentBlockRequest);

    List<UnBlockDocument> findUnBlockForCheckRelation(JwtBody jwt,List<UnBlockDocument> unBlockDocument, SearchPaymentBlockRequest request);

    List<UnBlockDocument> findUnBlockParentByCondition(List<PaymentBlockListDocumentRequest> paymentBlockListDocumentRequestList);

    List<UnBlockDocument> previewSelectGroupDocument(SelectGroupDocument selectGroupDocument);

}
