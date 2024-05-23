package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class UnBlockDocumentMQResponse extends BaseModel {

    private String valueOld;
    private String valueNew;
    private Timestamp unblockDate;
    private String companyCode;
    private String fiArea;
    private String paymentCenter;
    private String documentType;
    private String originalFiscalYear;
    private String originalDocumentNo;
    private Timestamp dateAcct;
    private String paymentMethod;
    private BigDecimal amount;
    private String wtxType;
    private String wtxCode;
    private BigDecimal wtxBase;
    private BigDecimal wtxAmount;
    private String wtxTypeP;
    private String wtxCodeP;
    private BigDecimal wtxBaseP;
    private BigDecimal wtxAmountP;
    private String vendor;
    private String vendorName;
    private String userPost;
    private String userName;
    private String reason;
    private String idemStatus;

    private String reverseCompanyCode;
    private String reverseDocumentType;
    private String reverseOriginalFiscalYear;
    private String reverseOriginalDocumentNo;

    private Long total;
    private Long success;
    private Long fail;
    private Long process;
}
