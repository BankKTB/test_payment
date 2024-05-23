package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyReportResponse {

    private String companyCode;
    private String companyName;
    private Date paymentDate;
    private String paymentName;
    private String city;
    private String userId;

    private Date postDate;


}