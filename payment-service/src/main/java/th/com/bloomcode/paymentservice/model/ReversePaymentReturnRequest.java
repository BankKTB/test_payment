package th.com.bloomcode.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ReversePaymentReturnRequest {
    private WSWebInfo webInfo;
    private String compCode;
    private String accountDocNo;
    private String fiscalYear;

    private String reasonNo = null;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp revDateAcct = null;

    private String reason = null;

    private int flag = 0; // 0=Test, 1=Do
}
