package th.com.bloomcode.paymentservice.model.payment.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReversePaymentReport {

  private List<PaymentReport> data;
  private Long total;
  private Long success;
  private Long fail;
  private Long process;
}
