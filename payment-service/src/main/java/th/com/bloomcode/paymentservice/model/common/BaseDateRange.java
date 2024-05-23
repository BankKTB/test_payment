package th.com.bloomcode.paymentservice.model.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseDateRange {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp from;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp to;
    private boolean optionExclude;
}
