package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.util.List;

@Data
public class CurrencyReportResponse {

    private String currencyCode;
    private String currencyName;

    private String sumCurrency;
    private String sumAmountPay;
    private String sumAmountMinusFc;
    private String sumLcurrency;
    private String sumAmountLc;
    private List<CurrencyDetailReportResponse> detail;

}