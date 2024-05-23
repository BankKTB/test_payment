package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.payment.dto.UnBlockDocument;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SelectGroupDocumentDetailResponse {

    private String companyCode;
    private String originalFiscalYear;
    private String originalDocumentNo;
    private String originalDocumentType;
    private BigDecimal amount;
    private String remark;
    private String newAssignment;
    private String oldAssignment;

}