package th.com.bloomcode.paymentservice.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.model.common.BaseDateRange;
import th.com.bloomcode.paymentservice.model.common.BaseRange;
import th.com.bloomcode.paymentservice.model.common.GlAccount;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.sql.Timestamp;
import java.util.List;

@Data
public class GenerateJuRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp transferDate;
    private List<BaseDateRange> paymentDate;
    private List<BaseRange> paymentName;
    private List<GlAccount> glAccounts;
    private boolean testRun;
    private String companyCode;
    private String docType;
    private String glCredit;
    private String keyDebit;
    private String keyCredit;
    private String fundSource;
    private String costCenter;
    private String budgetCost;
    private String fiArea;
    private String mainActivity;
    private String paymentCenter;
    private String username;
    private WSWebInfo webInfo;
}
