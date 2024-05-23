package th.com.bloomcode.paymentservice.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaymentResult {
    private List<PaymentProcess> paymentProcesses;
    private List<PaymentInformation> paymentInformations;
}
