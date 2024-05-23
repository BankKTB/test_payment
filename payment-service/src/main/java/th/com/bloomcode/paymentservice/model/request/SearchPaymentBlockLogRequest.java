//package th.com.bloomcode.paymentservice.model.request;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import lombok.Data;
//import th.com.bloomcode.paymentservice.webservice.adapter.JsonDateSerializer;
//
//import java.sql.Timestamp;
//
//@Data
//public class SearchPaymentBlockLogRequest {
//
//
//
//    private String compCodeFrom;
//    private String compCodeTo;
//
//    private String accDocNoFrom;
//    private String accDocNoTo;
//
//    private String fiscalYearFrom;
//    private String fiscalYearTo;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
//    @JsonSerialize(using = JsonDateSerializer.class)
//    private Timestamp unblockDate;
//
//    private String statusFrom;
//    private String statusTo;
//
//    private String fiAreaFrom;
//    private String fiAreaTo;
//
//    private String paymentCenterFrom;
//    private String paymentCenterTo;
//
//}
