package th.com.bloomcode.paymentservice.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CompanyCondition {

    private String companyFrom;
    private String companyTo;
    private boolean optionExclude;
}
