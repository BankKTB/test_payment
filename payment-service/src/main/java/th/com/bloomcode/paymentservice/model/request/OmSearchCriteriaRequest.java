package th.com.bloomcode.paymentservice.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class OmSearchCriteriaRequest {
    private String createBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp createDate;
    private Long id;
    private String jsonText;
    private String name;
    private String role;
    private Boolean userOnly;
}
