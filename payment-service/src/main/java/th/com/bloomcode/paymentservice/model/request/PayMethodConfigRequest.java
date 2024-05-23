package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;

@Data
public class PayMethodConfigRequest {

    private Long copyId;
    private String country;
    private String payMethod;
    private String payMethodName;
    private String payBy;
    private boolean allowedPersonnelPayment;
    private boolean bankDetail;
    private String documentTypeForPayment;


}