package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.SearchPaymentBlockRequest;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentMQ;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.model.request.PaymentBlockListDocumentRequest;
import th.com.bloomcode.paymentservice.model.request.UnBlockChangeDocumentRequest;
import th.com.bloomcode.paymentservice.model.response.UnBlockDocumentResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UnBlockDocumentService {


    ResponseEntity<Result<List<UnBlockChangeDocumentRequest>>> changePaymentBlock(List<UnBlockChangeDocumentRequest> request);

    ResponseEntity<Result<UnBlockDocumentResponse>> findUnBlockByCondition(HttpServletRequest httpServletRequest, SearchPaymentBlockRequest request);

    ResponseEntity<Result<List<UnBlockDocument>>> findUnBlockParentByCondition(List<PaymentBlockListDocumentRequest> request);

}
