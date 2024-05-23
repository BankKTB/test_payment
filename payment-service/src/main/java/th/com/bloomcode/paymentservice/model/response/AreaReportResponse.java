package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.util.List;

@Data
public class AreaReportResponse {

    private String areaCode;
    private String areaName;

    private String sumCurrency;
    private String sumAmountPay;
    private String sumAmountMinusFc;
    private String sumLcurrency;
    private String sumAmountLc;

    private List<AreaDetailReportResponse> detail;

}