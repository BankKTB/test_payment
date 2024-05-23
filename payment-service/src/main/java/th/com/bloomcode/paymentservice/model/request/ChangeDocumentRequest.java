package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;


@Data
public class ChangeDocumentRequest {


    private String compCode;
    private String accountDocNo;
    private String fiscalYear;
    private String paymentBlock;
    private String assignment;
    private String lineDesc;
    private String reference1;
    private String bankAccNo;
    private WSWebInfo webInfo;


}
