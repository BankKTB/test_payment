package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.JUHead;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.webservice.model.response.APPaymentResponse;

import java.sql.Timestamp;
import java.util.List;

public interface JuHeadRepository extends CrudRepository<JUHead, Long> {
    List<JUHead> findJuHeadByListPaymentDateAndListPaymentName(GenerateJuRequest request);

    JUHead findAllByPaymentDateAndPaymentNameAndTestRun(Timestamp paymentDate, String paymentName, boolean testRun);

    void updateJuDocument(APPaymentResponse aPPaymentResponse, String messageQueueId);
}
