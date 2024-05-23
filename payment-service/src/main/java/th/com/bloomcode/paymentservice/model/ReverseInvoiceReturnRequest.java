package th.com.bloomcode.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.sql.Timestamp;

@Data
public class ReverseInvoiceReturnRequest {
    private Long id;

    private WSWebInfo webInfo;

    private String compCode = null;

    private String accountDocNo = null;

    private String fiscalYear = null;

    private String reasonNo = null;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
    private Timestamp revDateAcct = null;

    private String reason = null;

    private int flag = 0; // 0=Test, 1=Do

//
//    private Long id;
//    private String invoiceDocument;
//    private String invoiceCompCode;
//    private String invoiceFiscalYear;
}
