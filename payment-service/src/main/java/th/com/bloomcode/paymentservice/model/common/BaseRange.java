package th.com.bloomcode.paymentservice.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class BaseRange {
    private String from;
    private String to;
    private boolean optionExclude;
}
