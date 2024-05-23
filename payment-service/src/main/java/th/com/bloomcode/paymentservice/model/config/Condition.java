package th.com.bloomcode.paymentservice.model.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Condition {

    private String conditionFieldFrom;
    private String conditionFieldTo;

    @Override
    public String toString() {
        return "Condition{" +
                "conditionFieldFrom='" + conditionFieldFrom + '\'' +
                ", conditionFieldTo='" + conditionFieldTo + '\'' +
                '}';
    }
}
