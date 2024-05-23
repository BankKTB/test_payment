package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.util.List;

@Data
public class BankReportResponse {

    private String bankAccountNo;
    private String bankName;
    private String bankBranchName;

    private int sumTotalOrder;
    private String sumCurrency;
    private String sumAmountPay;
    private String sumAmountMinusFc;
    private String sumLcurrency;
    private String sumAmountLc;
    private List<BankDetailReportResponse> detail;

}