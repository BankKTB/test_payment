package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OmReportResponse {


    private BigDecimal totalAmount;
    private BigDecimal totalTaxFee;
    private BigDecimal totalNetPrice;

    private List<OmItemReportResponse> items;

}