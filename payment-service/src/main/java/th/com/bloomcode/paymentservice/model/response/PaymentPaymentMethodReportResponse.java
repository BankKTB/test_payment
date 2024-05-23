package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.util.List;

@Data
public class PaymentPaymentMethodReportResponse {


    private int sumTotalOrder;
    private String sumLcurrency;
    private String sumAmountLc;
    private List<PaymentMethodReportResponse> paymentMethod;

}