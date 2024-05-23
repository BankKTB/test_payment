package th.com.bloomcode.paymentservice.model.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Payment {

    private String paymentDate;
    private String paymentName;
    private String status;

}
