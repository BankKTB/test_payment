package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.util.List;

@Data
public class VendorReportResponse {

    private String vendorCode;
    private String vendorNameTH;
    private String vendorNameEn;
    private String address;
    private String bankName;
    private String bankBranch;
    private String bankNo;
    private String bankAccountNo;

    private List<GLLineSuccessReportResponse> glLine;

}