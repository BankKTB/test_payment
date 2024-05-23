package th.com.bloomcode.paymentservice.model.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import th.com.bloomcode.paymentservice.model.common.CompanyCondition;

import java.sql.Timestamp;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Parameter {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp postDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp saveDate;
    private String paymentMethod;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp paymentDate;
    private String company;
    private List<VendorConfig> vendor;
    private List<CompanyCondition> companyCondition;

}
