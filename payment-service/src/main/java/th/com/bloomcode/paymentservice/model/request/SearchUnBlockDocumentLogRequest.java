package th.com.bloomcode.paymentservice.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.adapter.JsonDateSerializer;

import java.sql.Timestamp;

@Data
public class SearchUnBlockDocumentLogRequest {

    private String companyCodeFrom;
    private String companyCodeTo;

    private String documentTypeFrom;
    private String documentTypeTo;

    private String originalDocumentNoFrom;
    private String originalDocumentNoTo;

    private String originalFiscalYearFrom;
    private String originalFiscalYearTo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp dateAcctFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp dateAcctTo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp unblockDateFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp unblockDateTo;

    private String valueOld;
    private String valueNew;


    //for report
    private String fiAreaFrom;
    private String fiAreaTo;

    private String paymentCenterFrom;
    private String paymentCenterTo;


    private String username;
}
