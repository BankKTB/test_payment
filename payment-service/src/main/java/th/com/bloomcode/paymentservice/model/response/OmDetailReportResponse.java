package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OmDetailReportResponse {

    private String status;
    private Date unblockDate;
    private String companyCode;
    private String area;
    private String paymentCenter;
    private String docType;
    private String fiscalYear;
    private String accDocNo;
    private Date postDate;
    private String paymentMethod;
    private BigDecimal amount;
    private BigDecimal taxFee;
    private BigDecimal netPrice;
    private String vendor;
    private String vendorName;


}