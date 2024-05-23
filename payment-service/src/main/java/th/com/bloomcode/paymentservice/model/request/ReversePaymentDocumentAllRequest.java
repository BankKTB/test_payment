package th.com.bloomcode.paymentservice.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.sql.Timestamp;


@Data
public class ReversePaymentDocumentAllRequest {


    private WSWebInfo webInfo;

    private Long paymentAliasId;

    private String reasonReverse = null;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp reverseDate = null;

}
