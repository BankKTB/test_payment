package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SelectGroupDocumentResponse {


    private List<SelectGroupDocumentDetailResponse> success;
    private List<SelectGroupDocumentDetailResponse> unSuccess;

}