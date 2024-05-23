package th.com.bloomcode.paymentservice.model.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ParameterConfig {

    private Payment payment;
    private Parameter parameter;
    private List<Independent> independent;
    private AdditionLog additionLog;

}
