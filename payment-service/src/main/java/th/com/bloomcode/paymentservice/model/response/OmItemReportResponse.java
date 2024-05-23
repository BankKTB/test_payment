package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OmItemReportResponse {

    private String companyCode;
    private String companyName;
    private BigDecimal sumAmount;
    private BigDecimal sumTaxFee;
    private BigDecimal sumNetPrice;
    private int sumDocument;

    private List<OmDetailReportResponse> detail;

}