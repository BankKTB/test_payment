package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

@Data
public class BankDetailReportResponse {

    private String country;
    private String bankKey;
    private String bankAccountNo;
    private String cs;

    private int totalOrder;
    private String paymentMethod;
    private String currency;
    private String amountPay;
    private String amountMinusFc;
    private String Lcurrency;
    private String amountLc;

}