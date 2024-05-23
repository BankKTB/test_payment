package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;


@Data
public class DocumentRequest {

    private String compCode;
    private String paymentDocNo;
    private String fiscalYear;
}
