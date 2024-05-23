package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GLLineReportResponse {

    private Long paymentProcessId;
    private String code1;
    private String code2;
    private String originalDocumentNo;
    private String originalDocumentType;
    private String originalFiscalYear;
    private String invoiceDocumentNo;
    private String invoiceDocumentType;
    private String invoiceFiscalYear;
    private Date documentDate;
    private Date baseDate;
    private String paymentTerm;
    private String pk;
    private BigDecimal amountSumFc;
    private BigDecimal amountMinusFc;
    private BigDecimal amountNet;
    private String currencyNetPay;
    private String errorCode;
    private List<SpecialGLLineReportResponse> specialGLLineReportResponseList;


}