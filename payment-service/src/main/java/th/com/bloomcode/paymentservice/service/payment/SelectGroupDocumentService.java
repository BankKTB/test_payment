package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.SelectGroupDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;
import th.com.bloomcode.paymentservice.model.response.SelectGroupDocumentPreviewResponse;
import th.com.bloomcode.paymentservice.model.response.SelectGroupDocumentResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SelectGroupDocumentService {
    ResponseEntity<Result<SelectGroupDocumentResponse>> create(SelectGroupDocument selectGroupDocument);

    ResponseEntity<Result<SelectGroupDocumentPreviewResponse>> preview(HttpServletRequest httpServletRequest, SelectGroupDocument selectGroupDocument);

    SelectGroupDocument findOneByIndependentSelect(String defineName);
}
