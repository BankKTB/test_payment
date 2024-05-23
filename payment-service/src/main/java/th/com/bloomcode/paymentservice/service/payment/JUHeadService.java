package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.model.payment.JUHead;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.webservice.model.response.APPaymentResponse;

import java.sql.Timestamp;
import java.util.List;

public interface JUHeadService {
    JUHead save(JUHead juHead);

    JUHead findAllByPaymentDateAndPaymentNameAndTestRun(Timestamp paymentDate, String paymentName, boolean testRun);

    void deleteAllById(Long id);

    List<JUHead> findJuHeadByListPaymentDateAndListPaymentName(GenerateJuRequest request);

    void updateJuDocument(APPaymentResponse aPPaymentResponse, String messageQueueId);
}
