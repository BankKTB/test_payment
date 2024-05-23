package th.com.bloomcode.paymentservice.model.common;

import lombok.Data;

@Data
public class DocType {

  private String docTypeFrom;
  private String docTypeTo;
  private boolean optionExclude;
}
