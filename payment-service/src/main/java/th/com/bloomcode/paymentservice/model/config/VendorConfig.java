package th.com.bloomcode.paymentservice.model.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VendorConfig {

    private String vendorTaxIdFrom;
    private String vendorTaxIdTo;
    private boolean optionExclude;

}
