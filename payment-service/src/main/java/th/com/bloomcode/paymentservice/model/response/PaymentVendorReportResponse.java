package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import java.util.List;

@Data
public class PaymentVendorReportResponse {

    private CompanyReportResponse company;

    private List<VendorReportResponse> vendor;
}