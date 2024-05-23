package th.com.bloomcode.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentAliasRequest {


    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;
    private String paymentName;
    private String parameterJsonText;

}