package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayee;

@Data
public class CompanyPayeeHouseBankKeyConfigRespone {

    private Long id;

    private CompanyPayee companyPayee;

    private String houseBankKey;
    private String country;
    private String countryNameEn;
    private String bankBranch;

    private String bankName;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String city;
    private String zipCode;

    private String swiftCode;

}