package th.com.bloomcode.paymentservice.model.request;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class UnBlockChangeDocumentRequest extends UnBlockDocumentLog {


    private boolean canUnblock;
    private boolean approve;
    private WSWebInfo webInfo;
}
