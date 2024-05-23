package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

@Data
public class PaymentMethodReportResponse {

    private String paymentMethodCode;
    private String paymentMethodName;
    private int totalOrder;
    private String currency;
    private String amountPay;
    private String amountMinusFc;
    private String Lcurrency;
    private String amountLc;

}