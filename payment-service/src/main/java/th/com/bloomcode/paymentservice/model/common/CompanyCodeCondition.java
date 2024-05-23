package th.com.bloomcode.paymentservice.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CompanyCodeCondition {

    private String companyCodeFrom;
    private String companyCodeTo;
    private boolean optionExclude;
}
