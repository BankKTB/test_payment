package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class CustomTriggersRequest {
    private List<String> state;
    private String status;
    private Timestamp paymentDate;
    private String paymentName;
    private Long paymentAliasId;
    private Long paymentType;
    private Timestamp jobDate;
}
