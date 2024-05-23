package th.com.bloomcode.paymentservice.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class GenerateFileAliasRequest {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp generateFileDate;


    private String generateFileName;

    private String fileName;

    private int swiftAmountDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp swiftDate;
    private int smartAmountDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp smartDate;
    private int giroAmountDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp giroDate;
    private int inhouseAmountDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp inhouseDate;
    private boolean createAgain;
    private boolean testRun;

    private String parameterStatus;
    private String proposalStatus;
    private String runStatus;


    private Long paymentAliasId;


}
