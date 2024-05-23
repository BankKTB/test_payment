package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.util.List;

@Data
public class PaymentCountryReportResponse {

    private List<CountryReportResponse> country;

}