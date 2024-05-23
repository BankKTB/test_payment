package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.payment.GLHead;
import th.com.bloomcode.paymentservice.model.payment.GLLine;

import java.util.List;

@Data
public class PaymentDocumentDetailResponse {

    GLHead header;
    List<GLLine> items;
}
