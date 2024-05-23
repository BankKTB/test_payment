package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

@Data
public class CurrencyDetailReportResponse {

    private String code;
    private String paymentMethod;
    private String country;
    private String amountPay;
    private String amountMinusFc;
    private String Lcurrency;
    private String amountLc;


}