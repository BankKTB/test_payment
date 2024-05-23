package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.common.BaseDateRange;
import th.com.bloomcode.paymentservice.model.common.BaseRange;

import java.util.List;

@Data
public class DuplicatePaymentReport {
    private String companyCode;
    private List<BaseDateRange> paymentDate;
}
