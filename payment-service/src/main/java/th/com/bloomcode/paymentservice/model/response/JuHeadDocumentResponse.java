package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class JuHeadDocumentResponse {


    private Date paymentDate;
    private String paymentName;
    private String docType;
    private String documentNo;
    private String reference;
    private String companyCode;
    private String fiscalYear;
    private String companyName;
    private String paymentCenter;
    private String paymentCenterName;
    private String username;
    private String documentStatus;
    private String documentStatusName;
    private Timestamp dateDoc;
    private Timestamp dateAcct;
    private Timestamp transferDate;
    private BigDecimal amountCr;
    private String glAccountCr;
    private String glAccountCrName;
    private List<JuLineDocumentResponse> list;
    private Long juHeadId;

}