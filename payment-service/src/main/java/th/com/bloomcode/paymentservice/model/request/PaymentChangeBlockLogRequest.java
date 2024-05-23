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
//public class PaymentChangeBlockLogRequest {
//
//    private String compCodeFrom;
//    private String compCodeTo;
//
//    private String docTypeFrom;
//    private String docTypeTo;
//
//    private String accDocNoFrom;
//    private String accDocNoTo;
//
//    private String fiscalYearFrom;
//    private String fiscalYearTo;
//
//    private String postDateFrom;
//    private String postDateTo;
//    private String approveDateFrom;
//    private String approveDateTo;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
//    @JsonSerialize(using = JsonDateSerializer.class)
//    private Timestamp unblockDate;
//
//    private String valueOld;
//    private String valueNew;
//
//    private String username;
//
//
//}
