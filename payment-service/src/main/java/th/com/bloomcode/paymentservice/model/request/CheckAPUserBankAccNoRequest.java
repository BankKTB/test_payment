package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;


@Data
public class CheckAPUserBankAccNoRequest {


    private String documentType;
    private WSWebInfo webInfo;


}
