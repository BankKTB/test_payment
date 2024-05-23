package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.common.BaseDateRange;
import th.com.bloomcode.paymentservice.model.common.BaseRange;

import java.util.List;

@Data
public class SummaryTR1Request {
    private String period;
    private String fiscalYear;
    private List<BaseRange> refNumber;
    private List<BaseDateRange> paymentDate;
    private List<BaseRange> paymentName;
    private Boolean updateTablePropLogTr1;
    private String username;
}
