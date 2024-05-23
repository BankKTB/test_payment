package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.util.Date;

@Data
public class PaymentAliasRequest {

    private Long id;


    private Date paymentDate;


    private String paymentName;

    private String parameterStatus;
    private String proposalStatus;
    private String runStatus;
    private String documentStatus;
    private String generateStatus;
    private Long paymentCreated;
    private Long paymentPosted;
    private String jsonText;

    private int proposalTotalDocument = 0;
    private int proposalSuccessDocument = 0;
    private Date proposalScheduleDate;
//	private Date proposalScheduleTime;

    private int runTotalDocument = 0;
    private int runSuccessDocument = 0;
    private Date runScheduleDate;
//	private Date runScheduleTime;

    private Long proposalTriggersId;
    private Long paymentTriggersId;

 
    private WSWebInfo webInfo;
}
