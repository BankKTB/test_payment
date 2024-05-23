package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.payment.entity.PayMethodCurrencyConfig;

import java.util.List;

@Data
public class PayMethodCurrencyConfigResponse {

    private Long payMethodConfigId;
    private List<PayMethodCurrencyConfig> PayMethodCurrencyConfig;

}
