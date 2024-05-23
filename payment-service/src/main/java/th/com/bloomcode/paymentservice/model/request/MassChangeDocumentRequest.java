package th.com.bloomcode.paymentservice.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.sql.Timestamp;


@Data
public class MassChangeDocumentRequest {


    private WSWebInfo webInfo;

    private String compCode = null;

    private String accountDocNo = null;

    private String fiscalYear = null;

    private String paymentBlock = null;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
//    private Timestamp dateBaseLine = null;



    private int flag = 0; // 0=Test, 1=Do

}
