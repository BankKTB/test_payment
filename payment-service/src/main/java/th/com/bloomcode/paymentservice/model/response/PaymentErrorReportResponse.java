package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.util.List;

@Data
public class PaymentErrorReportResponse {

    private String errorCode;

    private String description;
}