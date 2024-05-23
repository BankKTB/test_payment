package th.com.bloomcode.paymentservice.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UnblockDocumentLogDetailRequest {

  private String companyCode;

  private String fiscalYear;

  private String documentNo;
}
