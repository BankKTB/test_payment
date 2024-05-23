package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GLLineSuccessReportResponse extends GLLineReportResponse {

    private String glStatus;
    //	private String glAccount;
    private String paymentDocNo;
    private String paymentCompCode;
    private String paymentFiscalYear;
    private String bankAgent;
    private String bankAgentAccount;
    private String paymentMethod;
    private String vendorName;

    private BigDecimal amountPay;
    private String currencyPay;


    private BigDecimal SummaryAmountSumFc;
    private BigDecimal SummaryAmountMinusFc;
    private BigDecimal SummaryAmountNet;

    private List<GLLineErrorReportResponse> error;

}