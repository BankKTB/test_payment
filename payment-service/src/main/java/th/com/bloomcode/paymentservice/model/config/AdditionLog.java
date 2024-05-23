package th.com.bloomcode.paymentservice.model.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AdditionLog {

    private boolean checkBoxDueDate;
    private boolean checkBoxPaymentMethodAll;
    private boolean checkBoxPaymentMethodUnSuccess;
    private boolean checkBoxDisplayDetail;
    private List<VendorConfig> vendor;

}
