package th.com.bloomcode.paymentservice.model;

import lombok.Data;

import java.util.List;

@Data
public class PayMethodConfigRequest {

    private Long id;
    private String payMethod;
    private List<PayMethodCurrencyConfigRequest> payMethodCurrencyConfigRequest;

}
