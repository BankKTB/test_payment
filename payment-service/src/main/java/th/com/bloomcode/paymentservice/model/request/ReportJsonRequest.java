package th.com.bloomcode.paymentservice.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class ReportJsonRequest {

  private String userWeb;
  private String paymentCenter;
  private String compCode;
  private String docDateStart;
  private String docDateFinish;
  private String format;
  private String formId;
  private String dateType;

  private String docNo;
  private String fiscYear;

  private String recordId;
  private String brDocNo;

  private String poDocNo;
  private String mrDocNo;
  private String reportType;
}
