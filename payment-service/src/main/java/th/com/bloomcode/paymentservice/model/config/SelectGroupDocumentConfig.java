package th.com.bloomcode.paymentservice.model.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import th.com.bloomcode.paymentservice.model.common.BaseRange;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SelectGroupDocumentConfig {


    private String companyCode;
    private List<BaseRange> list;

}
