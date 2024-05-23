package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

@Data
public class AreaDetailReportResponse {

    private String code;
    private String paymentMethod;
    private String currency;
    private String amountPay;
    private String amountMinusFc;
    private String Lcurrency;
    private String amountLc;

}