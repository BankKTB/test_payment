package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;

import javax.persistence.Column;


@Data
public class PaymentBlockListDocumentRequest {


    private String documentType;
    private String companyCode;
    private String originalDocumentNo;
    private String originalFiscalYear;


}
