package th.com.bloomcode.paymentservice.model.common;

import lombok.Data;

@Data
public class PaymentMethod {

  private String paymentMethodFrom;
  private String paymentMethodTo;
  private boolean optionExclude;
}
