package th.com.bloomcode.paymentservice.model;

import lombok.Data;
import th.com.bloomcode.paymentservice.payment.entity.PayMethodCurrencyConfig;

import java.util.List;

@Data
public class PayMethodCurrencyConfigRequest {

    private Long payMethodConfigId;
    private List<PayMethodCurrencyConfig> payMethodCurrencyConfig;

}
